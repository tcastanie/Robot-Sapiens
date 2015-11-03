/*
 * Created on 6 mai 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package madkit.explorer;

import java.awt.event.*;

import java.io.File;
import java.lang.reflect.*;
import java.util.HashMap;

import javax.swing.*;



/**
 * @author B_ROUIFED
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ExplorerActions implements ActionListener{

	ExplorerGUI adaptee;
	static String actionToolBar;
	static String actionBefore;
	HashMap listActions = new HashMap();
	String action;
	static String nameToCopy;
	String name = null;
	String fileClicked =null;

	public ExplorerActions(ExplorerGUI adaptee)
	{
		this.adaptee = adaptee;
		putHashmap();

	

	}
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {

        try
        {
			Icon icon = Icon.LastSelected;
			String path = adaptee.explorer.getAbsolutePath();
			fileClicked = path+File.separatorChar+icon.name;
	
        }catch(Exception ccex)
        {
        }
        
		actionToolBar = null;
		JButton source = new JButton();
        actionToolBar = e.getActionCommand().toLowerCase();
		if (actionToolBar.equals (null) || actionToolBar.equals(""))
		{
			source = (JButton)e.getSource();
			actionToolBar = source.getName().toLowerCase();
		}
		execActionToolBar();
	
	}
	
	
	public void execActionToolBar()
	{
		action = exec(actionToolBar);

		if (actionToolBar.equals ("copy") || actionToolBar.equals("cut"))
		{
			actionBefore = actionToolBar;
		}

		try 
		{


			Class classe = Class.forName("madkit.explorer.Explorer" + action);
/*
			Method [] methods = classe.getDeclaredMethods();
			for(int i= 0; i< methods.length; i++)
			{
				System.out.println("declared Methods = " + methods[i].toString());
			}
			
			Class [] types = {Class.forName("madkit.explorer.ExplorerGUI"), 
							  Class.forName("java.lang.String")};
			Object [] params = {adaptee,fileClicked};
			Method method = classe.getMethod("staticExec",types);
			method.invoke(null, params);
*/
			Constructor [] constructor = classe.getDeclaredConstructors();
//			for(int i= 0; i< constructor.length; i++)
//			{
//				System.out.println("const = " + constructor[i].toString());
//			}
			Method mt = classe.getMethod("exec",new Class[0]);
			Object [] arg = {adaptee, fileClicked};
			Object objetInstance  = constructor[0].newInstance(arg);
 
			mt.invoke(objetInstance,new Object[0]);

		} catch (ClassNotFoundException ex)
		{
		  		System.err.println("Class does not exist "  + ex.getMessage());
		} catch (Exception ccex){
				System.err.println("Class : " +ccex);
		  // ccex.printStackTrace();
		}
		
  
	}
	

	
	
	public void putHashmap()
	{
		listActions.put("home","ActionHome");
		listActions.put("forward","ActionForward");
		listActions.put("backward","ActionBackward");				
		listActions.put("parent","ActionParent");				
		listActions.put("cut","ActionCopy");				
		listActions.put("copy","ActionCopy");				
		listActions.put("past","ActionPast");				
		listActions.put("undo","ActionUndo");				
		listActions.put("redo","ActionRedo");	
		listActions.put("new","ActionCreateFile");			
		listActions.put("create folder","ActionCreateFile");			
//		listActions.put("drag","ActionDrag");				
//		listActions.put("drop","ActionDrop");				

	}

	public String exec(String actionToolBar)
	{
		String action;
		action = (String)listActions.get(actionToolBar).toString();				
		return 	action;		

	}
	

}
