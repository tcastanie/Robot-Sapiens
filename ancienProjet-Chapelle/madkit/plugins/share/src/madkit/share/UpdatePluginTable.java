package madkit.share;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;

import javax.swing.*;
import javax.swing.table.*;



import javax.swing.table.TableColumn;

class UpdatePluginTableModel extends GenericTableModel {
    
	Vector stockUpdatedPlugin=new Vector();
	int nbLine;
   	
	public UpdatePluginTableModel(Vector stock){
		super(4, stock.size());	
		stockUpdatedPlugin=stock;
		nbLine=stockUpdatedPlugin.size();
		setColumnName(0,"Plugin Name");
		setColumnName(1,"Plugin Version");
		setColumnName(2,"Server Plugin Version");
		setColumnName(3,"Update");
	
		for(int i=0;i<nbLine;i++){
			setValueAt(((UpdatedPlugin)stockUpdatedPlugin.elementAt(i)).getPluginName(), i, 0);     
			setValueAt(((UpdatedPlugin)stockUpdatedPlugin.elementAt(i)).getPluginVersion().toString(), i, 1);
			setValueAt(((UpdatedPlugin)stockUpdatedPlugin.elementAt(i)).getServerPluginVersion().toString(),i,2);
			setValueAt((new Boolean("true")), i, 3);
		}
	} 
	
	public void sendRequestPlugin(PluginShareAgent shareAgent){
		for(int i=0;i<nbLine;i++){
			if( (((Boolean)getValueAt(i,3)).equals(new Boolean("true"))) ){
//				System.out.println("ask plugin " + ((UpdatedPlugin)stockUpdatedPlugin.elementAt(i)).pluginName+
//								", to "+((UpdatedPlugin)stockUpdatedPlugin.elementAt(i)).getServerAddress());
			shareAgent.sendMessage( ((UpdatedPlugin)(stockUpdatedPlugin.elementAt(i))).getServerAddress(),
					  new FicMessage( ((UpdatedPlugin)(stockUpdatedPlugin.elementAt(i))).getVirtualPath(),true));
			}
		}
	}
    
	public void checkAll(){
		for(int i=0;i<nbLine;i++){
			setValueAt((new Boolean("true")), i, 3);
		}
	}

	public void unCheckAll(){
		for(int i=0;i<nbLine;i++){
			setValueAt((new Boolean("false")), i, 3);
		}
	}
	
	void update(Vector stock){
		stockUpdatedPlugin=stock;
		nbLine=stockUpdatedPlugin.size();
		reset(4, stock.size());	
	
		for(int i=0;i<nbLine;i++){
			setValueAt(((UpdatedPlugin)stockUpdatedPlugin.elementAt(i)).getPluginName(), i, 0);     
			setValueAt(((UpdatedPlugin)stockUpdatedPlugin.elementAt(i)).getPluginVersion().toString(), i, 1);
			setValueAt(((UpdatedPlugin)stockUpdatedPlugin.elementAt(i)).getServerPluginVersion().toString(),i,2);
			setValueAt((new Boolean("true")), i, 3);
		}
	} 
	   
}


public class UpdatePluginTable extends GenericTable {

	Vector stockUpdatedPlugin;
	PluginShareAgent shareAgent;
	
	public UpdatePluginTable(Vector stockUpdatedPlugin, PluginShareAgent ag){
		super(new UpdatePluginTableModel(stockUpdatedPlugin));
		shareAgent = ag;
		initTable();
	}

	private void initTable(){
		((TableColumn)(table.getColumnModel().getColumn(1))).setPreferredWidth(50);
		((TableColumn)(table.getColumnModel().getColumn(2))).setPreferredWidth(50);
		((TableColumn)(table.getColumnModel().getColumn(3))).setPreferredWidth(10);
	}

	public void click(int row, int col){
	if (col == 3) {
		Boolean b = (Boolean)(model.getValueAt(row, col));
		boolean bool = b.booleanValue();
		if (bool) model.setValueAt((new Boolean("false")), row, col);
		else model.setValueAt((new Boolean("true")), row, col);
		table.repaint();
	}
	}

	public void doubleClick(int row, int col){
	}
	
	public void updateTable(Vector stock){
		((UpdatePluginTableModel) model).update(stock);
		repaint();
	}

}


/* class UpdatePluginTableFrame extends JPanel  {

    UpdatePluginTable table;
    Vector stockUpdatedPlugin;
	PluginShareAgent shareAgent;
    JPanel tablePanel;
//    JPanel buttonPanel, validPanel, checkPanel;
    // JPanel titlePanel;
//    JButton validButton, checkButton, unCheckButton;
   // JLabel title;


    public UpdatePluginTableFrame(Vector update,PluginShareAgent s){
    	stockUpdatedPlugin=update;
    	shareAgent = s;
		table = new UpdatePluginTable(stockUpdatedPlugin);
		
		tablePanel = new JPanel();
//		buttonPanel = new JPanel();
//		titlePanel = new JPanel();
//		validPanel = new JPanel();
//		checkPanel = new JPanel();
//		validButton = new JButton("VALID");
//		checkButton = new JButton("check all");
//		unCheckButton = new JButton("uncheck all");
	
//		validButton.addActionListener(this);
//		checkButton.addActionListener(this);
//		unCheckButton.addActionListener(this);

	
		// title = new JLabel("===== PLUGIN(S) UPDATING : Check your plugin(s) =====");
		
		tablePanel.setLayout(new BorderLayout());
//		buttonPanel.setLayout(new GridLayout(2,1));
//		titlePanel.setLayout(new FlowLayout());
		
//		validPanel.add(validButton,null);
//		checkPanel.add(checkButton,null);
//		checkPanel.add(unCheckButton,null);
	
		tablePanel.add(table, BorderLayout.CENTER);
//		buttonPanel.add(checkPanel);
//		buttonPanel.add(validPanel);
//		titlePanel.add(title,null);
	//	getContentPane().add(tablePanel, BorderLayout.CENTER);
	//	getContentPane().add(buttonPanel, BorderLayout.SOUTH);
	//	getContentPane().add(titlePanel, BorderLayout.NORTH);
		
	
		add(tablePanel, BorderLayout.CENTER);
//		add(buttonPanel, BorderLayout.SOUTH);
//		add(titlePanel, BorderLayout.NORTH);
		
	//	addWindowListener(new WindowAdapter() {
	//		public void windowClosing(WindowEvent e) {
	//		    dispose();
	//		}
	//	    });
    } 
    
    public void updateTable(Vector update){
    	System.out.println("updating table "+update);
		table.update(update);
		tablePanel.repaint();
    }

//    public void actionPerformed(ActionEvent e){
//	if(e.getSource() == validButton){
//	    ((UpdatePluginTableModel)table.getTableModel()).sendRequestPlugin(shareAgent);
//	    repaint();
//	    //dispose();
//	}
//	if(e.getSource() == checkButton){
//	    ((UpdatePluginTableModel)table.getTableModel()).checkAll();
//	    repaint();
//	}
//	if(e.getSource() == unCheckButton){
//	    ((UpdatePluginTableModel)table.getTableModel()).unCheckAll();
//	    repaint();
//	}	
//    }
} */
