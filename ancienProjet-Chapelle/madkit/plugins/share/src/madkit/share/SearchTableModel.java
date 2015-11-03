package madkit.share;
import java.net.URL;
import java.util.Vector;

import javax.swing.ImageIcon;

class SearchTableModel extends GenericTableModel {
    
    Vector stockResultSearchFile = new Vector();
    int sizeResult;
    ImageIcon icon;
    URL url;
	
    public SearchTableModel(Vector stock){
	super(4, stock.size());	
        stockResultSearchFile=stock;
	sizeResult=stock.size();
	setColumnName(0,"File");
	setColumnName(1,"Name");
	setColumnName(2,"Size (Ko)");
	setColumnName(3,"Server");

	String fileName;
	String serverName;
	float sizeFile;

	for(int i=0;i<sizeResult;i++){
	    fileName = ((SearchedFile)(stockResultSearchFile.elementAt(i))).getVirtualPath();
	    serverName = ((SearchedFile)(stockResultSearchFile.elementAt(i))).getServerName();
	    sizeFile = ((SearchedFile)(stockResultSearchFile.elementAt(i))).getSizeFile();
	    if( ((SearchedFile)(stockResultSearchFile.elementAt(i))).isDirectory() )
		url = this.getClass().getResource("/images/share/foldOpened.png");
	    else url = this.getClass().getResource("/images/kde/binary.png");
	    if (url != null) icon = new ImageIcon(url);
	    setValueAt(icon, i ,0);
	    setValueAt(fileName,i,1);
	    setValueAt(sizeFile+"",i,2);
	    setValueAt(serverName,i,3);
	}
    } 
}
