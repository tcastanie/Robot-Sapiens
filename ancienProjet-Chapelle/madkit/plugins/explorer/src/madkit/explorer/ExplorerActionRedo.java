/*
 * Created on 10 mai 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package madkit.explorer;

import javax.swing.undo.CannotRedoException;

/**
 * @author B_ROUIFED
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ExplorerActionRedo extends ExplorerAdapterActions{

	public ExplorerActionRedo()
	{
	}
	/**
	 * @param adaptee
	 */
	public ExplorerActionRedo(ExplorerGUI adaptee, String _nameToRedo) {
	}

	public void exec()
	{
		System.out.println("Nous sommes dans ExplorerActionRedo method exec");
		try
		{
			adaptee.undoManager.redo();
		}catch(CannotRedoException re){}
		finally
		{
			updateButtons();
			
		}
		
	}
}
