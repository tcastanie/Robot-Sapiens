/*
 * Created on 9 juin 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package madkit.explorer;

import java.util.Vector;

/**
 * @author B_ROUIFED
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ExplorerBackForWard {
	

		static Vector vectorSelection = new Vector();
		static Vector vectorNode = new Vector();
	   	static int vectorGuiPos;

		public static void init(ExplorerGUI adaptee)
		{
//	
			 vectorGuiPos = -1;
			 adaptee.backward.setEnabled(false);				 	 
			 adaptee.bForward.setEnabled(false);
	}
		
}
