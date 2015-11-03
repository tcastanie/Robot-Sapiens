package madkit.share;

import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.net.*;

import javax.swing.table.TableColumn;


class UpdateTableModel extends GenericTableModel {
    
	Vector stockUpdatedFile=new Vector();
	AbstractShareAgent shareAgent;
	ImageIcon icon;
	int nbLine;
	URL url;
   	
	public UpdateTableModel(Vector stock, AbstractShareAgent s){
	super(5, stock.size());
	shareAgent = s;	
		stockUpdatedFile=stock;
	nbLine=stockUpdatedFile.size();
	url = this.getClass().getResource("/images/kde/document.png");
	if (url != null) icon = new ImageIcon(url);
	setColumnName(0,"File");
	setColumnName(1,"Path");
	setColumnName(2,"Server");
	setColumnName(3,"Last Modification");
	setColumnName(4,"Check");

	for(int i=0;i<nbLine;i++){
		setValueAt(icon, i ,0);
		setValueAt(((UpdatedFile)stockUpdatedFile.elementAt(i)).getPath(), i, 1);     
		setValueAt(((UpdatedFile)stockUpdatedFile.elementAt(i)).getServerName(), i, 2);
		long time = ((UpdatedFile)stockUpdatedFile.elementAt(i)).getLastModified();
		Date d= new Date(time);
		setValueAt(d.toString(),i,3);
		setValueAt((new Boolean("true")), i, 4);
	}
	} 
	
	public void updateVector(){
	int i=0;
	int j=0;
	while(i<nbLine && !stockUpdatedFile.isEmpty()){
		if( ! (((Boolean)getValueAt(j,4)).equals(new Boolean("true"))) ){
		stockUpdatedFile.remove(i);
		nbLine=nbLine-1; 
		j++;
		}
		else{
		i++;
		j++;
		}
	}
	((FileMaj)((AbstractShareAgent)shareAgent).getFileMaj()).lastUpdate(stockUpdatedFile);
	}
    
	public void checkAll(){
	for(int i=0;i<nbLine;i++){
		setValueAt((new Boolean("true")), i, 4);
	}
	}

	public void unCheckAll(){
	for(int i=0;i<nbLine;i++){
		setValueAt((new Boolean("false")), i, 4);
	}
	}
}



class UpdateTable extends GenericTable {

    UpdateTable(Vector stockUpdatedFile, AbstractShareAgent s){
		super(new UpdateTableModel(stockUpdatedFile, s));
		initTable();
    }

    private void initTable(){
	menu.addCommand("interpret","interpret");
	((TableColumn)(table.getColumnModel().getColumn(0))).setPreferredWidth(10);
	((TableColumn)(table.getColumnModel().getColumn(1))).setPreferredWidth(150);
	((TableColumn)(table.getColumnModel().getColumn(2))).setPreferredWidth(50);
	((TableColumn)(table.getColumnModel().getColumn(4))).setPreferredWidth(10);
    }

    public void click(int row, int col){
	if (col == 4) {
	    Boolean b = (Boolean)(model.getValueAt(row, col));
	    boolean bool = b.booleanValue();
	    if (bool) model.setValueAt((new Boolean("false")), row, col);
	    else model.setValueAt((new Boolean("true")), row, col);
	    table.repaint();
	}
    }

    public void doubleClick(int row, int col){
    }

}


public class UpdateTableFrame extends JFrame implements ActionListener{

	UpdateTable table;
	Vector stockUpdatedFile;
	AbstractShareAgent shareAgent;
	JPanel tablePanel, buttonPanel, titlePanel, validPanel, checkPanel;
	JButton validButton, checkButton, unCheckButton;
	JLabel title;


	public UpdateTableFrame(Vector update,AbstractShareAgent s){
		stockUpdatedFile=update;
		shareAgent = s;
	table = new UpdateTable(stockUpdatedFile, shareAgent);
	tablePanel = new JPanel();
	buttonPanel = new JPanel();
	titlePanel = new JPanel();
	validPanel = new JPanel();
	checkPanel = new JPanel();
	validButton = new JButton("VALID");
	checkButton = new JButton("check all");
	unCheckButton = new JButton("uncheck all");

	validButton.addActionListener(this);
	checkButton.addActionListener(this);
	unCheckButton.addActionListener(this);

	
	title = new JLabel("===== AUTO UPDATING : Check your File(s) =====");
	
	tablePanel.setLayout(new BorderLayout());
	buttonPanel.setLayout(new GridLayout(2,1));
	titlePanel.setLayout(new FlowLayout());
	
	validPanel.add(validButton,null);
	checkPanel.add(checkButton,null);
	checkPanel.add(unCheckButton,null);

	tablePanel.add(table, BorderLayout.CENTER);
	buttonPanel.add(checkPanel);
	buttonPanel.add(validPanel);
	titlePanel.add(title,null);
	getContentPane().add(tablePanel, BorderLayout.CENTER);
	getContentPane().add(buttonPanel, BorderLayout.SOUTH);
	getContentPane().add(titlePanel, BorderLayout.NORTH);
	addWindowListener(new WindowAdapter() {
		public void windowClosing(WindowEvent e) {
			dispose();
		}
		});
	} 

	public void actionPerformed(ActionEvent e){
	if(e.getSource() == validButton){
		((UpdateTableModel)table.getTableModel()).updateVector();
		dispose();
	}
	if(e.getSource() == checkButton){
		((UpdateTableModel)table.getTableModel()).checkAll();
		repaint();
	}
	if(e.getSource() == unCheckButton){
		((UpdateTableModel)table.getTableModel()).unCheckAll();
		repaint();
	}	
	}
}