/*
 * Created on 25 déc. 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package madkit.utils.graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

/**
 * @author Jaco
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class GenericTableMenu implements ActionListener {

	JPopupMenu popup;
	GenericTable table;

	public GenericTableMenu(GenericTable _table){
	popup = new JPopupMenu();
	table = _table;
	}

	public GenericTableMenu(GenericTable _table, String[][] commands){
	popup = new JPopupMenu();
	table = _table;
	for (int i=0; i<commands.length; i++){
		addCommand(commands[i][0], commands[i][1]);
	}
	}

	public JPopupMenu getPopup(){
	return popup;
	}
    
	public void addCommand(String item, String action){
	JMenuItem menuItem = new JMenuItem(item);
	menuItem.setActionCommand(action);
	popup.add(menuItem);
	menuItem.removeAll();
	menuItem.addActionListener(this);
	}

	public void actionPerformed(ActionEvent event){
	String c = event.getActionCommand();
	Class[] parameterTypes = new Class[] {};
	try {
		Method command = table.getClass().getMethod(c,parameterTypes);
		command.invoke(table, new Object[]{});
	} catch (NoSuchMethodException e) {
		System.out.println("command : " + c + " unknown");
	} catch (IllegalAccessException e) {
		System.out.println(e);
	} catch (InvocationTargetException e) {
		System.out.println(e + ", command: " + c);
	}
	}
}