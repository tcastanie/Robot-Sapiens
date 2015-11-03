/*
 * Created on 8 juin 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package madkit.explorer;

/**
 * @author B_ROUIFED
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ExplorerActionHome extends ExplorerAdapterActions{

	public ExplorerActionHome()
	{
	}
	/**
	 * @param adaptee
	 */
	public ExplorerActionHome(ExplorerGUI _adaptee, String _source) {

		this.adaptee = _adaptee;

		

	}

	public void exec()
	{
		adaptee.setRootPath(adaptee.initialPath);
	}


}
