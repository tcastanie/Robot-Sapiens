/*
 * Created on 29 avr. 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package madkit.explorer;

import madkit.TreeTools.DirIconDescriptor;

/**
 * @author B_ROUIFED
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ExplorerDirIcon extends DirIconDescriptor
{

	/**
	 * @param locked
	 */
	public ExplorerDirIcon(boolean locked) {
		super(locked);
		System.out.println("explorerdirIcon");
		super.addCommand("rename file", "rename");
	}
	

}
