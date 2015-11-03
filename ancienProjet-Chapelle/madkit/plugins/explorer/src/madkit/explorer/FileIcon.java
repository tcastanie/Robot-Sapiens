package madkit.explorer;

import java.io.*;
import madkit.TreeTools.*;
import java.awt.*;
import madkit.kernel.*;
import java.net.URL;
import javax.swing.*;

public class FileIcon extends Icon {

    File file;
    int iconSize;
  
	
    public FileIcon(AbstractAgent ag, File file, GenericIconDescriptor desc, int iconSize, IconPanel iconPanel){
		super(ag, desc, file.getName(), iconSize, iconPanel);
		this.file = file;
		this.setDescriptor(desc);
    	this.iconSize = iconSize;
    }

    public File getFile(){return file;}
    
	protected Image getImage(){
		ImageIcon im = desc.getImage(file);
		if (im == null){
			URL url = this.getClass().getResource("/images/kde/document.png");
			if (url != null){
				im = new ImageIcon(url);
			}
		}
		if (im == null)
			return null;
		else
			return im.getImage();
	}
	
	public void info(){
		JOptionPane.showMessageDialog(iconPanel, 
				"NodeType: "+getClass().getName()+
				"\nFile: "+getFile(), "Properties of "+getName(), 
				JOptionPane.INFORMATION_MESSAGE);
	}

//	fonction rename file    
	 public void rename(){

		 String url = file.getAbsolutePath();
		 String currentPath = file.getParent();
		 String oldName = file.getName();
		 String shortOldName = Utils.getFileNameFromPath(oldName);
        
		 String arg1 ="TO"; 
		 String arg2 ="RENAME FILE "; 
		 String newName  = ExplorerOptionPane.askForName(shortOldName,arg1,arg2);
		 // System.out.println("newName = "+newName);
		 File oldFile = new File(url);
		 File newFile = new File(url);
		
		 if ((newName != null)  && (!newName.equals(shortOldName)))
		 {
 			 iconPanel.renameItem(desc, oldName, newName);
  			 newFile = new File(currentPath + File.separatorChar + newName);
			 oldFile.renameTo(newFile);
  			 file = newFile;
		 }

 		
	  }
    
//	fonction rename file    
	 public void delete(){

		
		String url = file.getAbsolutePath();
		String currentPath = file.getParent();
		String firstMessage = "Do you really want to delete this file ?";
		String secondMessage = "DELETE FILE ";
		int request = ExplorerOptionPane.yesNo(firstMessage, secondMessage);
		if (request == JOptionPane.YES_OPTION)
		{

			File newFile = new File(currentPath);
			boolean delet = file.delete();
			if(delet)
			{		
				String oldName = file.getName();
				iconPanel.renameItem(desc, oldName, null);
				file = newFile;
			}
		}
 	 		
	  }
    
    
    public void execute(){
		String url = file.getAbsolutePath();
        boolean windows = isWindowsPlatform();
        String cmd = null;
        try{
	    if (windows){
		// cmd = 'rundll32 url.dll,FileProtocolHandler http://...'
		cmd = WIN_PATH + " " + WIN_FLAG + " " + url;
		Process p = Runtime.getRuntime().exec(cmd);
	    }
	    else{
		// Under Unix, Netscape has to be running for the "-remote"
		// command to work.  So, we try sending the command and
		// check for an exit value.  If the exit command is 0,
		// it worked, otherwise we need to start the browser.
		// cmd = 'netscape -remote openURL(http://www.javaworld.com)'
		cmd = UNIX_PATH + " " + UNIX_FLAG + "(" + url + ")";
		Process p = Runtime.getRuntime().exec(cmd);
		try{
		    // wait for exit code -- if it's 0, command worked,
		    // otherwise we need to start the browser up.
		    int exitCode = p.waitFor();
		    if (exitCode != 0){
			// Command failed, start up the browser
			// cmd = 'netscape http://www.javaworld.com'
			cmd = UNIX_PATH + " "  + url;
			p = Runtime.getRuntime().exec(cmd);
		    }
		}
		catch(InterruptedException x){
		    System.err.println("Error bringing up browser, cmd='" +
				       cmd + "'");
		    System.err.println("Caught: " + x);
		}
	    }
	}
        catch(IOException x){
	    // couldn't exec browser
	    System.err.println("Could not invoke browser, command=" + cmd);
	    System.err.println("Caught: " + x);
	}
    }
    /**
     * Try to determine whether this application is running under Windows
     * or some other platform by examing the "os.name" property.
     *
     * @return true if this application is running under a Windows OS
     */
    public boolean isWindowsPlatform(){
        String os = System.getProperty("os.name");
        if ( os != null && os.startsWith(WIN_ID))
            return true;
        else
            return false;
    }
   
    // Used to identify the windows platform.
    private static final String WIN_ID = "Windows";
    // The default system browser under windows.
    private static final String WIN_PATH = "rundll32";
    // The flag to display a url.
    private static final String WIN_FLAG = "url.dll,FileProtocolHandler";
    // The default browser under unix.
    private static final String UNIX_PATH = "konqueror";
    // The flag to display a url.
    private static final String UNIX_FLAG = "-remote openURL";
}
