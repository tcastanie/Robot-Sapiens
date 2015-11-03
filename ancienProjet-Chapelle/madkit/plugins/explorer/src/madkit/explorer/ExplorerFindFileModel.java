package madkit.explorer;
import java.io.File;
import java.util.Date;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;




/*
 * Created on 15 juin 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

/**
 * @author B_ROUIFED
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ExplorerFindFileModel extends AbstractTableModel{

	private Vector vectorPathFile = new Vector();
	private String pathFile;
	int j = 0;
	int l = 0;
	private String s;
	private String t;
	protected File dir;
	protected String[] fileNames;
		
	protected String[] columnNames = new String[]
   {
	   "Name", "Size", "Last Modified", "Directory?" , "Path"
   };
	
   	protected Class[] columnClasses = new Class[]
   {	
				  	
		String.class, Long.class, Date.class, Boolean.class , String.class 	
   };
	
   //This table model is true for all folders
   
   	public ExplorerFindFileModel(){}
   	
   	public ExplorerFindFileModel(File dir, File file)
   	{
	   	String	nameToFind = (String)file.toString(); 

	   	this.dir = dir;

	   	String dirString = (String)this.dir.toString();
	   	findDirectories(dirString,nameToFind);
			
		this.fileNames = (String[])vectorPathFile.toArray(new String[vectorPathFile.size()]);

		if (vectorPathFile.size() < 1)
	   	{
			String arg1 = "Search Files : No FILES " + nameToFind + " FOUND ";
			String arg2 = "FIND FILES OR FOLDERS  ";
			ExplorerOptionPane.showMessage(arg1,arg2);
        }
            	   
   	}
	

   	public int getColumnCount() {
	   	return 5;
   	}

   /* (non-Javadoc)
	* @see javax.swing.table.TableModel#getRowCount()
	*/
   	public int getRowCount() {
	   	return fileNames.length; //return nomber of files
   	}


   	public String getColumnName (int col){
	   	return columnNames[col];
   	}
	
   	public Class getColumnClass(int col){
	   	return columnClasses[col];
   	}
	
   	public Object getValueAt(int row, int col) {

	// parcours de l'arbre à partir de dir
		File f = new File(fileNames[row]);

	   	switch(col)
	   	{	
			case 0 : return f.getName();			
			case 1 : return new Long (f.length());
			case 2 : return new Date(f.lastModified());			
			case 3 : return f.isDirectory()? Boolean.TRUE : Boolean.FALSE ;
			case 4 : return f.getParent();
		   	default: return null;
	   }
		
   	}

	public Vector   findDirectories(String dirString,String nameToFind)
	{
		String [] args = new String[]{dirString};
			
		try {
				File pathName = new File(args[0]);
				String[] fileToFind = pathName.list();
				for(int i = 0; i < fileToFind.length; i++)
				{
					File f = new File(pathName.getPath(), fileToFind[i]);
					if (f.isDirectory())
					{
						findDirectories(f.getPath(),nameToFind);
					}
					s = nameToFind.toLowerCase();
					t = fileToFind[i].toLowerCase();
					j = t.indexOf(s);
                    
					if(j != -1)
					{
						pathFile = pathName+File.separator+fileToFind[i];
						vectorPathFile.addElement(pathFile);
					}
 
				}
			} catch (Exception e) {
//			e.printStackTrace();
			}

			return vectorPathFile;
	}


}
