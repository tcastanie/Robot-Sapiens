/*
 * Created on 6 mai 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package madkit.explorer;


import java.awt.Toolkit;
import java.awt.datatransfer.*;
import java.io.File;


/**
 * @author B_ROUIFED
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ExplorerActionCopy extends ExplorerAdapterActions {

	
	public ExplorerActionCopy()
	{
	}
	/**
	 * @param adaptee
	 */
	public ExplorerActionCopy(ExplorerGUI _adaptee, String _source) {

		this.adaptee = _adaptee;
		this.source = _source;
		

	}

	public void exec()
	{


		if (source != null)
		{
			adaptee.bPast.setEnabled(true);
			adaptee.jMenuEditPast.setEnabled(true);
			adaptee.explorer.repaint();
			adaptee.jMenuEditPast.setEnabled(true);
			copy(source);
		}else
		{
			String firstMessage = "No file selected to be copied";
			String secondMessage = "COPY FILE";
			ExplorerOptionPane.showMessage(firstMessage, secondMessage);
		}

	}
	
	public void copy(String text)
	{
//
 	 	sourceFile= new File(text);
 	 	String pathName = sourceFile.getAbsolutePath();

  	 	File [] files = {sourceFile};
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		ExplorerTransferableFile selection = new ExplorerTransferableFile(files);
		 
		clipboard.setContents(selection,null);

	}

}