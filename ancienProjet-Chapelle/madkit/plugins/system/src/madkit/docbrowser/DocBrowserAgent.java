/*
 * DocBrowserAgent.java - Created on Feb 25, 2004
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
 * Last Update: $Date: 2004/05/05 09:50:22 $
 */

package madkit.docbrowser;

import java.io.File;
import java.util.StringTokenizer;

import madkit.kernel.Agent;
import madkit.kernel.Message;
import madkit.kernel.StringMessage;


/**
 * @author Sebastian Rodriguez - sebastian.rodriguez@utbm.fr
 *
 * @version $Revision: 1.1 $
 */
public class DocBrowserAgent extends Agent {
    public static final String community="public";
    public static final String group="system";
    public static final String DOC_BROWSER_ROLE="docbrowser";
    
    private boolean _alive=true;
    private BrowserGUI gui;
    private File madkitDirectory;
    
    private String docIndex;
    
    public DocBrowserAgent(){
        this(null);
    }
    
    public DocBrowserAgent(File madkitDir){
        if(madkitDir==null){
        	madkitDirectory = new File(System.getProperty("madkit.dir"));
    // JF bug correction: the madkit.dir is in the System property
    //        String test=System.getProperty("user.dir")+File.separatorChar+"cache"+File.separatorChar+"docs.xml";
    //        File testFile=new File(test);
    //        if(testFile.exists()){
    //            _madkitDirectory=new File(System.getProperty("user.dir"));
    //        }
        }else{
            madkitDirectory=madkitDir.getAbsoluteFile();
        }
        
        docIndex="file:"+madkitDirectory.getPath()+File.separatorChar+"docs"+File.separatorChar+"index.html";
    }
    /* (non-Javadoc)
     * @see madkit.kernel.Agent#live()
     */
    public void live() {
        //set the initial page to the index
        if(hasGUI()){
            gui.setPage(docIndex);
        }
        
        while(_alive){
            Message msg=waitNextMessage();
            
            if(!_alive) return;
            exitImmediatlyOnKill();
            if(msg instanceof StringMessage){
                handleRequest(((StringMessage)msg).getString());
            }
        }
    }
    
    /* (non-Javadoc)
     * @see madkit.kernel.AbstractAgent#activate()
     */
    public void activate() {
        println("DocBrowserAgent activated");
        createGroup(false,community,group,null,null);
        requestRole(community,group,DOC_BROWSER_ROLE,null);
        this.broadcastMessage(community,group,"plugin",new StringMessage("getdoc"));
    }
    
    /* (non-Javadoc)
     * @see madkit.kernel.AbstractAgent#end()
     */
    public void end() {
       println("Stopping DocBrowserAgent");
    }
    /* (non-Javadoc)
     * @see madkit.kernel.AbstractAgent#initGUI()
     */
    public void initGUI() {
        File xml=new File(madkitDirectory.getAbsolutePath()+File.separatorChar+"cache"+File.separatorChar+"docs.xml");
        gui=new BrowserGUI(xml,this);
        setGUIObject(gui);
    }
    
    
    
    /**
     * @param string
     */
    private void handleRequest(String string) {
        StringTokenizer stk=new StringTokenizer(string,"£");
        String action=stk.nextToken();
        if(action==null){
            println("Unknown Request "+action);
            return;
        }
        
        if(action.equalsIgnoreCase("displayURL")){
            
        }else  if (action.equalsIgnoreCase("displayDoc")){
            stk.nextToken();
            String pluginName=stk.nextToken();
            stk.nextToken();
            String section=stk.nextToken();
            if(hasGUI()){
                gui.displayDoc(pluginName,section);
            }
        } else if (action.equalsIgnoreCase("displayLink")){
			//stk.nextToken();
			String sectionName=stk.nextToken();
	    	//stk.nextToken();
		    String displayName=stk.nextToken();
			//stk.nextToken();
			String url=stk.nextToken();
		    //stk.nextToken();
			String pluginName=stk.nextToken();
		    if(hasGUI()){
				DocLink link=new DocLink(
						sectionName,//section name
						displayName,//display's the DocRef name
						"file:"+url,	// l'url
						pluginName); //plugin name
				gui.displayLink(pluginName, link);
		    }
        }
		else {
			println("Unknown Request "+action);
		}
    }
}
