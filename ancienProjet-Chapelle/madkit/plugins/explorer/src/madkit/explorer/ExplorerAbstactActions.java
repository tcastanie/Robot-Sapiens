/*
 * Created on 10 mai 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package madkit.explorer;



import java.awt.datatransfer.Transferable;

import java.io.File;
import java.io.IOException;

import javax.swing.undo.AbstractUndoableEdit;



/**
 * @author B_ROUIFED
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public abstract class ExplorerAbstactActions extends AbstractUndoableEdit implements Transferable{
	
	ExplorerGUI adaptee;
	String source;
	String destination;
	File sourceFile;
	File destinationFile;



	
	abstract void exec() throws InstantiationException, IllegalAccessException, 
									  ClassNotFoundException, IOException; 


}

		
		
	


