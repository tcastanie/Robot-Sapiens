/*
 * Created on 10 mai 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package madkit.explorer;

import javax.swing.JOptionPane;
import javax.swing.UIManager;


/**
 * @author B_ROUIFED
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ExplorerOptionPane {
	
	static int request = -1;
	static String newName;

	public static int yesNo(String firstMessage,String secondMessage)
	{
		englishMess();		
		try
		{
			request = JOptionPane.showConfirmDialog(null,
													firstMessage,
													secondMessage, 
													JOptionPane.YES_NO_OPTION);
		}catch(Exception e) {
		   e.printStackTrace();
		}
		return request;
	}
	
	public static String askForName(String name,String arg1,String arg2) 
	{
		englishMess();		
		newName = name;
		 try
		 {
			newName = (String) JOptionPane.showInputDialog(null,
										   arg1,
										   arg2,
										   JOptionPane.PLAIN_MESSAGE,
										   null,
										   null,
										  name);
		 }catch(Exception e) {
			e.printStackTrace();
		 }
		return(newName);
 

	 }

	public static void showMessage(String firstMessage,String secondMessage)
	{
		englishMess();		
		JOptionPane.showMessageDialog(null,
													firstMessage,
													secondMessage, 
													JOptionPane.WARNING_MESSAGE);
	}
    
	public static void englishMess()
	{
		

		UIManager.put("OptionPane.yesButtonText", "Yes");        
		UIManager.put("OptionPane.noButtonText", "No"); 
		UIManager.put("OptionPane.cancelButtonText", "Cancel"); 

	}
}
