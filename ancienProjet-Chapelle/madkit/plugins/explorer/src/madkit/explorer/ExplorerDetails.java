/*
 * Created on 3 mai 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package madkit.explorer;

import java.awt.event.*;
import java.io.File;
import java.util.Date;

import javax.swing.*;
import javax.swing.table.*;

/**
 * @author B_ROUIFED
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ExplorerDetails implements ActionListener {
	
	ExplorerGUI adaptee;
	
	public ExplorerDetails(ExplorerGUI adaptee)
	{
		this.adaptee = adaptee;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		jMenuDisplayDetails_actionPerformed(e);
		
	}


	//Display | Details action performed
	public void jMenuDisplayDetails_actionPerformed(ActionEvent e) {

		String pathToExplore = adaptee.explorer.absolutePath.intern();

		File dir;
		dir = new File(pathToExplore);
		FileTableModel model = new FileTableModel(dir);
		
		JTable detailsTable = new JTable(model);

		String detailName = "Directory details: " + pathToExplore;
        JFrame frame = new JFrame(detailName);
        frame.getContentPane().add(new JScrollPane(detailsTable),"Center");
        frame.setSize(600,400);
        frame.setVisible(true);

	}

}

class FileTableModel extends AbstractTableModel
{
	protected File dir;
	protected String[] filenames;
	
	protected String[] columnNames = new String[]
	{
		"name", "size", "last modified", "Directory?" 
	};
	
	protected Class[] columnClasses = new Class[]
	{
		String.class, Long.class, Date.class, Boolean.class 	
	};
	
	//This table model is true for all folders
	public FileTableModel(File dir)
	{
		this.dir = dir;
		this.filenames = dir.list();       // save the list of files of folders
	}
	

	public int getColumnCount() {
		return 4;
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	public int getRowCount() {
		return filenames.length; //return nomber of files
	}


	public String getColumnName (int col)
	{
		return columnNames[col];
	}
	
	public Class getColumnClass(int col)
	{
		return columnClasses[col];
	}
	
	public Object getValueAt(int row, int col) {
		File f = new File(dir,filenames[row]);
		switch(col)
		{
			case 0 : return filenames[row];			
			case 1 : return new Long (f.length());
			case 2 : return new Date(f.lastModified());			
			case 3 : return f.isDirectory()? Boolean.TRUE : Boolean.FALSE ;
			default: return null;
		}
		
	}
}
