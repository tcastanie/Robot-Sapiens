/*
 * Created on 25 mai 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package madkit.explorer;

import java.io.File;


/**
 * @author B_ROUIFED
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ExplorerActionCreateFile extends ExplorerAdapterActions {
	
	File fileEntered;

/**
 * @param adaptee
 */
	public ExplorerActionCreateFile(ExplorerGUI _adaptee, String _source) {

		this.adaptee = _adaptee;
		this.source = _source;
		

	}

	public void exec()
	{


		String folderToCreate = adaptee.explorer.absolutePath.intern();
		String pathToExplore = adaptee.explorer.absolutePath.intern();
		String arg0;
		if (ExplorerActions.actionToolBar.equals("new"))
			arg0 = "FILE ";
		else
			arg0 = "FOLDER ";
		String arg1 = "Enter the Name of the " + arg0 + " you want to CREATE  ";
		String arg2 = "CREATE " + arg0;

		String name = pathToExplore+File.separatorChar;
		String nameEntered  = ExplorerOptionPane.askForName(name,arg1,arg2);
		fileEntered  = new File(nameEntered);
	 
		if (fileEntered.exists())
		{
			
			String firstMessage = "MadKit cannot execute this action,This Folder or this File "
								+ fileEntered 
								+ " already exists ";
			
			String SecondMessage = null;

		}else
			createFile();
			
	}
	
	public void createFile()	
	{
		boolean creat =false;
		if (ExplorerActions.actionToolBar.equals("new"))
		{
			try
			{
				creat = fileEntered.createNewFile();
			}catch(Exception exc)
			{
				System.out.println("Exeption = " + exc);						
			}
		}
		else
			creat = fileEntered.mkdir();
		if (creat)
			{
				System.out.println("The file is created");			
				adaptee.explorer.read();
			}
			else
				System.out.println("the file is NOT created");

	}

}