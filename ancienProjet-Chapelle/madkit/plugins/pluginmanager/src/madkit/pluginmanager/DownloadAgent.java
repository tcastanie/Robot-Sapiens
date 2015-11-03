/*
 * DownloadAgent.java - Created on Feb 1, 2004
 * 
 * Copyright (C) 2003-2004 Sebastian Rodriguez
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.

 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * Last Update: $Date: 2004/03/12 17:25:14 $
 */

package madkit.pluginmanager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import madkit.kernel.Agent;
import madkit.kernel.AgentAddress;
import HTTPClient.AuthorizationInfo;
import HTTPClient.AuthorizationPrompter;
import HTTPClient.DefaultAuthHandler;
import HTTPClient.HTTPConnection;
import HTTPClient.HTTPResponse;
import HTTPClient.ModuleException;
import HTTPClient.NVPair;

/**Download Agent takes care of downloading plugins from the 
 * server.
 * @author Sebastian Rodriguez - sebastian.rodriguez@utbm.fr
 *
 * @version $Revision: 1.2 $
 */
class DownloadAgent extends Agent implements UpdateRoles {

    
    private static final String MADKTI_REALM="Madkit-Plugin-Download";
	private PluginInformation _info=null;
	private boolean _done;
	
	private AgentAddress progress;
	private File _tmpDir;
	//private File _finalDir;
	
	private String _fileName;
	
	private String _downloadURL;
	private String _server;
	
	public DownloadAgent(String server, String file,File tmpDir){
		_server=server.toString();
		_downloadURL=_server+"/download.php?file="+file.toString();
		_tmpDir=new File(tmpDir.getAbsolutePath()+File.separatorChar+"files");
		
		_tmpDir.mkdirs();
		_tmpDir.deleteOnExit();
		
	}
	/**
	 * 
	 */
	public DownloadAgent(PluginInformation plugin,File tmpDir) {
		super();
		_info=plugin;
		_tmpDir=new File(tmpDir.getAbsolutePath()+File.separatorChar+"files");
		
		_tmpDir.mkdirs();
		_tmpDir.deleteOnExit();
		
		//_finalDir=finalDir;
	}

	public void activate(){
		requestRole(community,group,DOWNLOADER,null);
		
	}
	
	public void live(){
		 progress=getAgentWithRole(community,group,PROGRESS);
		int downloaded=0;
		//create the needed variables
		if(_info==null){
			_fileName="cache"+File.separatorChar+"plugins.zip";
		}else{
			_downloadURL=_info.getDownloadURL();
			_fileName=_info.getFileName();
			if(_fileName==null){//this is a meta package
				informDone(null);
				return;
			}
		}
		
		
		HTTPResponse rsp=null;
		int statusCode = 300;
		
		// We will retry up to 3 times.
		for (int attempt = 0; statusCode >= 300 && attempt < 3; attempt++) {
			try {
				URL url=new URL(_downloadURL);
				
				DefaultAuthHandler.setAuthorizationPrompter(new MyAuthPrompter());
				HTTPConnection connection=new HTTPConnection(url);
				//connection.addBasicAuthorization(MADKTI_REALM,System.getProperty("madkit.user.name"),System.getProperty("madkit.user.email"));
				// execute the method.
				rsp= connection.Get(url.getFile());
				statusCode=rsp.getStatusCode();
			} catch (IOException e) {
				if(e instanceof MalformedURLException){
					JOptionPane.showMessageDialog(new JFrame(),"Bad Server URL: "+_downloadURL);
					return;
				}
				println("Failed to download file.");
				e.printStackTrace();
			} catch (ModuleException e) {
				//logger.debug("ModuleException caught ",e);
				debug("ModuleException caught "+e.getMessage());
			}
		}
		
		File tmpFile=null;

		// Check that we didn't run out of retries.
		if (statusCode < 300 && rsp!=null) {
		    tmpFile=new File(_tmpDir.getAbsolutePath()+File.separatorChar+_fileName);
			if(tmpFile.exists()){
				tmpFile.delete();
			}else{//ensure that the path exists
				tmpFile.getParentFile().mkdirs();
			}
			FileOutputStream out;
			try {
				out = new FileOutputStream(tmpFile);
				InputStream in = rsp.getInputStream();
				byte[] buffer = new byte[1024];
				int len ;
				while ((len = in.read(buffer)) > 0) {
				   out.write(buffer, 0, len);
				   downloaded+=len;
				   informStatus(downloaded);
				}
				in.close();
				out.close();
			} catch (FileNotFoundException e) {
				System.out.println("File not found!!!!!");
				e.printStackTrace();
			} catch (IOException e) {
				System.err.println("IOException caught ");
			} catch (ModuleException e) {
				//logger.debug("ModuleException caught ",e);
			    System.err.println("ModuleException caught "+e.getMessage());
				debug("ModuleException caught "+e.getMessage());
			}
			
			
			
		}else{
			try {
			    String reason=" ";
			    if(rsp!=null){
			        reason=rsp.getReasonLine();
			    }
                println("Status code "+statusCode+" Reason: "+reason);
            } catch (IOException e) {
                //logger.debug("IOException caught ",e);
                debug("IOException caught "+e.getMessage());
            } catch (ModuleException e) {
                //logger.debug("ModuleException caught ",e);
                debug("ModuleException caught "+e.getMessage());
            }
		}
		informDone(tmpFile);//inform the result

	}
	/**
	 * 
	 */
	private void informDone(File f) {
		AgentAddress manager=getAgentWithRole(community,group,PLUGIN_MANAGER);
		if(_info==null){
			
			sendMessage(manager,new DownloadFinished("plugins.zip",f,_server));
		}else{
			sendMessage(manager,new DownloadFinished(_info.getName(),f,_server));
			informStatus(_info.getSize());
		}
		
		
	}
	private void informStatus(int downloaded) {
		if(progress==null)
			progress=getAgentWithRole(community,group,PROGRESS);
		if(progress!=null&&_info!=null)
			sendMessage(progress,new DownloadStatus(_info.getName(),_info.getSize(),downloaded));
	}
	
}

class MyAuthPrompter implements AuthorizationPrompter{
    private String _username;
    private String _email;
    
    public MyAuthPrompter(){
        loadInfo();
    }
    
    /* (non-Javadoc)
     * @see HTTPClient.AuthorizationPrompter#getUsernamePassword(HTTPClient.AuthorizationInfo, boolean)
     */
    public NVPair getUsernamePassword(AuthorizationInfo challenge, boolean forProxy) {
        if(forProxy){
            System.out.println("proxy auth not implemented yet");
        }
        if(_username==null || _email==null){
            
            System.out.println("need to provide an email and username yet");
        }
        System.out.println("returning : "+_username+" "+_email);
        NVPair nvp=new NVPair(_username,_email);
        return nvp;
    }
    
    private void loadInfo(){
        _username=System.getProperty("madkit.user.name");
        _email=System.getProperty("madkit.user.email");
    }
}
