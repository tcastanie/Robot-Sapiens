/*
 * Created on 3 mai 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package madkit.explorer;




import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;


import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;


/**
 * @author B_ROUIFED
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ExplorerFindFile implements ActionListener {
	
	   protected JTable tableDetails;
	   protected JFrame frame;
	   	
	   private ExplorerGUI adaptee;
	
	public ExplorerFindFile(ExplorerGUI adaptee)
	{
		this.adaptee = adaptee;
 
	}

	   /* (non-Javadoc)
		* @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		*/
	   public void actionPerformed(ActionEvent e) {
		   jMenuFindFile_actionPerformed(e);
		
		
	   }

	public void jMenuFindFile_actionPerformed(ActionEvent e) 
	{
		String pathToExplore = adaptee.explorer.absolutePath.intern();
		String arg1 = "Enter the Name of File or Folder you want to find  in " + pathToExplore;
		String arg2 = "FIND FILES OR FOLDERS  ";


//		File fileEntered  = adaptee.askForName(null,arg1,arg2);

		String newName  = ExplorerOptionPane.askForName(null,arg1,arg2);
		File fileEntered  = new File(newName);
		
		File dir;
		dir = new File(pathToExplore);
		   	
//		FindFileModel model = new FindFileModel(dir, fileEntered);
		ExplorerFindFileModel model = new ExplorerFindFileModel(dir, fileEntered);
 		tableDetails = new JTable(model);
		tableDetails.setSelectionBackground(Color.blue);
		tableDetails.setSelectionForeground(Color.white);

		tableDetails.getColumnModel().getColumn(4).setMinWidth(200);
		tableDetails.getColumnModel().getColumn(4).setMaxWidth(400);

		tableDetails.getColumnModel().getColumn(0).setResizable(true);
		tableDetails.getColumnModel().getColumn(1).setResizable(true);
		tableDetails.getColumnModel().getColumn(2).setResizable(true);
		tableDetails.getColumnModel().getColumn(3).setResizable(true);
		tableDetails.getColumnModel().getColumn(4).setResizable(true);
		tableDetails.setAutoResizeMode(4);

		   
 			
            tableDetails.addMouseListener(new MouseListener()
            {

				public void mouseClicked(MouseEvent arg0) {

					int clickCount = arg0.getClickCount();
					int colClicked = tableDetails.getSelectedColumn();
					int lignClicked = tableDetails.getSelectedRow();
					String selectedPath = (String)tableDetails.getValueAt(lignClicked,4).toString();
					


					tableDetails.setToolTipText(selectedPath);
					
					if (clickCount == 2)
					{
						frame.toBack();
						File selectedFile = new File (selectedPath+File.separator+
						(String)tableDetails.getValueAt(lignClicked,0).toString());	

						adaptee.setRootPath(selectedPath);
					}
							 					
				}                                                                                                                                                                                                                                                                                                                                                                                                                                                  


				
				
				public void mouseEntered(MouseEvent arg0) {
					// TODO Auto-generated method stub
					
				}
				
				public void mouseExited(MouseEvent arg0) {
					// TODO Auto-generated method stub
					
				}

				public void mousePressed(MouseEvent arg0) {
					// TODO Auto-generated method stub
					
				}

				public void mouseReleased(MouseEvent arg0) {
					// TODO Auto-generated method stub
					
				}
            });
		   
		   String detailName = "Find File: Directory " + pathToExplore + " and File to Find : " +  newName;
		   frame = new JFrame(detailName);
		   frame.getContentPane().add(new JScrollPane(tableDetails),"Center");
		   frame.setSize(600,400);
		   frame.setVisible(true);

	   }
	
	}

   

