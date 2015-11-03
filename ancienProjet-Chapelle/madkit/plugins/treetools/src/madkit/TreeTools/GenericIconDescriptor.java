package madkit.TreeTools;

import java.lang.reflect.*;
import javax.swing.*;
import java.awt.event.*;
import java.net.URL;
import java.io.*;

public class GenericIconDescriptor implements ActionListener {

	String imageAddress;
	ImageIcon imageIcon;
	JPopupMenu popup;
	//GenericTreeNode currentNode;
	Object arg;


	public GenericIconDescriptor(){
		this.imageAddress = "/images/mime_ascii.png";
	}
	public GenericIconDescriptor(String im) {
		imageAddress = im;
	}

	public GenericIconDescriptor(String im, String[][] commands) {
		this(im);
		for (int i = 0; i < commands.length; i++) {
			addCommand(commands[i][0], commands[i][1]);
		}
	}

	public ImageIcon getImage() {
		if (imageAddress != null) {
			if (imageIcon == null) {
				URL url = this.getClass().getResource(imageAddress);
				if (url != null)
					imageIcon = new ImageIcon(url);
			}
			return imageIcon;
		}
		return null;
	}
	
	public ImageIcon getImage(File f){
		return getImage();
	}

	public JPopupMenu getPopup() {
		return popup;
	}

/*	GenericTreeNode getCurrentNode() {
		return currentNode;
	}

	void setCurrentNode(GenericTreeNode aNode) {
		currentNode = aNode;
	} */
	
	public Object getArg(){
		return arg;
	}
	public void setArg(Object arg){
		this.arg = arg;
	}

	public void addCommand(String item, String action) {
		if (popup == null) {
			popup = new JPopupMenu();
		}
		JMenuItem menuItem = new JMenuItem(item);
		menuItem.setActionCommand(action);
		popup.add(menuItem);
		menuItem.removeAll();
		menuItem.addActionListener(this);
	}
	public void actionPerformed(ActionEvent ev) {
		String c = ev.getActionCommand();
		Class[] parameterTypes = new Class[] {};
		Method command=null;
		try {
			command = arg.getClass().getMethod(c, parameterTypes);
			command.invoke(arg, new Object[] {});
		} catch (NoSuchMethodException e) {
			try {	Class [] paramTypes2 = new Class[1];
				paramTypes2[0] = Class.forName("java.lang.String");
				command = arg.getClass().getMethod("command", paramTypes2);
				command.invoke(arg, new Object[] {c});
			} catch(Exception exc){
				System.err.println(exc.getMessage());
				exc.printStackTrace();
			}
		} catch (IllegalAccessException e) {
			System.out.println(e);
		} catch (InvocationTargetException e) {
			System.out.println(e + ", command: " + c);
		}
	}

}
