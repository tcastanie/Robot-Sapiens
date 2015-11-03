/*
 * Created on 10 mai 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package madkit.explorer;


import javax.swing.undo.CannotUndoException;





/**
 * @author B_ROUIFED
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ExplorerActionUndo extends ExplorerAdapterActions
{

	public ExplorerActionUndo()
	{
	}
	/**
	 * @param adaptee
	 */
	public ExplorerActionUndo(ExplorerGUI _adaptee, String _source) {
		this.adaptee = _adaptee;
		this.source = _source;
		
	}

	public void exec()
	{
		System.out.println("Nous sommes dans UNDO method exec");
		try
		{
			adaptee.undoManager.undo();
		}catch(CannotUndoException ue){}
		finally
		{
			updateButtons();
		}
		
	}
	
}
