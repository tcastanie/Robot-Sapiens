package madkit.TreeTools;

import javax.swing.*;
import java.net.URL;


public class DirIconDescriptor extends GenericIconDescriptor{

    ImageIcon foldOpened;
    ImageIcon foldClosed;
    ImageIcon foldLocked;
    boolean access;

    public DirIconDescriptor(boolean locked){
		super("/images/folder.png");
		foldOpened = loadIcon("/images/kde/folder_open.png");
		foldClosed = loadIcon("/images/kde/folder.png");
		foldLocked = loadIcon("/images/kde/folder_locked.png");
		access = locked;	
		if (locked){
			this.imageAddress = "/images/folder_locked.png";
		}
		// ici brd
			addCommand("rename Folder", "rename");
			addCommand("delete Folder", "delete");
		// fin ici brd
    }

/*	DirIconDescriptor(boolean locked){
	super("/images/folder.png");
	if (locked){
		this.imageAddress = "/images/folder_locked.png";
		this.imageIcon = loadImage(imageAddress);
	}
	} */

    ImageIcon loadIcon(String path){
		URL url = this.getClass().getResource(path); 
		if (url != null){
		    return new ImageIcon(url);
		}
	        return null;
    }

    ImageIcon getLeafIcon(){
	//System.out.println(" Class DirNodeDescriptor, method getLeafIcon");
	if (access && foldClosed != null) return foldClosed;
	if (!access && foldLocked != null) return foldLocked;
	return super.getImage();
    }

    ImageIcon getBranchIcon(int num, boolean bool){
	if (bool && foldOpened != null){
	    // System.out.println("Open OK");
	    return foldOpened;
	}
	if (!bool && foldClosed != null){
	    //System.out.println("Close OK"); 
	    return foldClosed;
	}
	return super.getImage();
    } 
}

