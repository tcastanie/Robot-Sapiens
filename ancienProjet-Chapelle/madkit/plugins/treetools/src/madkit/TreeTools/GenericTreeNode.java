package madkit.TreeTools;

import javax.swing.*;
import javax.swing.tree.*;

public class GenericTreeNode extends DefaultMutableTreeNode {

    GenericIconDescriptor descriptor;

    public GenericTreeNode(String name){
	super(name);
    }

    public GenericIconDescriptor getDescriptor(){
        return descriptor;
    }

    public void setDescriptor(GenericIconDescriptor desc){
	descriptor = desc;
    }

    public void execute(){
    }

    public ImageIcon getLeafIcon(){
	return null;
    }

    public ImageIcon getBranchIcon(int k, boolean open){
	return null;
    }
}
