/*
 * Created on 10 mai 2004
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
public class ExplorerActionParent extends ExplorerAdapterActions{

	public ExplorerActionParent()
	{
	}
	/**
	 * @param adaptee
	 */
	public ExplorerActionParent(ExplorerGUI _adaptee, String _source) {

		this.adaptee = _adaptee;
		this.source = _source;
		

	}

	public void exec()
	{
		if (source  != null)
		{
			
			File file = (new File(source));

		}
			boolean bool = adaptee.explorer.readUp();
			if (!bool)
			{
				String path = adaptee.textField.getText();
				if(path.equals (adaptee.initialPath))
				{
				}
				else
				{
						String parent =
							path.substring(0, path.lastIndexOf(File.separatorChar));
						adaptee.setRootPath(parent);
				}
			}
	}


}
