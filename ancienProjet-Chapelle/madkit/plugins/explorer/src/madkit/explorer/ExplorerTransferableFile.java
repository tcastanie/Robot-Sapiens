/*
 * Created on 26 mai 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package madkit.explorer;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ExplorerTransferableFile implements Transferable {
	List fileList;        // The list of files


  public ExplorerTransferableFile(File[] files) {

	fileList = new ArrayList();
	for (int i = 0; i < files.length; i++) {
	  fileList.add(files[i]);
	}
  }

  // Implementation of the Transferable interface
  public DataFlavor[] getTransferDataFlavors() {
	return new DataFlavor[]{DataFlavor.javaFileListFlavor};
  }

  public boolean isDataFlavorSupported(DataFlavor fl) {
	return fl.equals(DataFlavor.javaFileListFlavor);
  }

  public Object getTransferData(DataFlavor fl) {
	if (!isDataFlavorSupported(fl)) {
	  return null;
	}
	return fileList;
  }

}