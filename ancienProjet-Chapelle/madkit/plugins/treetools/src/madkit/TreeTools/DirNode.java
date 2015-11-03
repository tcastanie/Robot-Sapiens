package madkit.TreeTools;

import javax.swing.*;
import java.io.*;

public class DirNode extends AbstractFileNode{

    DirIconDescriptor dirNodeDescriptor; 

    public DirNode(File file, boolean access){
        super(file);
	dirNodeDescriptor = new DirIconDescriptor(access); 
        this.setDescriptor(dirNodeDescriptor);
    }

    public DirNode(Entry entry, boolean access){
	super(entry);
	dirNodeDescriptor = new DirIconDescriptor(access); 
	this.setDescriptor(dirNodeDescriptor);
    }

    public ImageIcon getLeafIcon(){
	return dirNodeDescriptor.getLeafIcon();
    }

    public ImageIcon getBranchIcon(int k, boolean open){
	return dirNodeDescriptor.getBranchIcon(k,open);
    }
}
