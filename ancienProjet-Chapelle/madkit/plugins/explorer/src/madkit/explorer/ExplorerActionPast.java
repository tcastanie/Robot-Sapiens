/*
 * Created on 10 mai 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package madkit.explorer;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * @author B_ROUIFED
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ExplorerActionPast extends ExplorerAdapterActions{

	private List fileList;        // The list of files


	public ExplorerActionPast()
	
	{
	}
	/**
	 * @param adaptee
	 */
	public ExplorerActionPast(ExplorerGUI _adaptee, String _source) {

		this.adaptee = _adaptee;
		this.source = _source;
		

	}

	public void exec()
	{
		if (source != null)
		{
			adaptee.bUndo.setEnabled(true);
			adaptee.jMenuEditUndo.setEnabled(true);
			adaptee.explorer.repaint();
			adaptee.jMenuEditPast.setEnabled(true);
			past();
		}else
		{
			String firstMessage = "No file selected to be Pasted";
			String SecondMessage = "PAST";
			ExplorerOptionPane.showMessage(firstMessage, SecondMessage);
		}

	}
	
	public void past()
	{
  
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		Transferable contents = clipboard.getContents(null);
		if(contents != null)
		{
			try
			{

				if(contents.isDataFlavorSupported(DataFlavor.javaFileListFlavor))
				{
					// file object(s) have been dropped
					Object  data  = contents.getTransferData(DataFlavor.javaFileListFlavor);
					fileList = new ArrayList();
					fileList = (List)data;
				
					Iterator iterator = fileList.iterator();
					while(iterator.hasNext())
					{
						File oldFile = (File)iterator.next();
						
						String path = adaptee.textField.getText();
						File newFile = new File (path+File.separator+oldFile.getName());

						ExplorerDirectoryFile explorerDirectoryFile = new ExplorerDirectoryFile(adaptee.explorer,oldFile, newFile);

					}
				}
			}
			catch(Exception e)
			{
			}
		}

	}

		
}
