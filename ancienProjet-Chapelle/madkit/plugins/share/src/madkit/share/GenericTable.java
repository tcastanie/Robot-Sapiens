package madkit.share;
import java.awt.BorderLayout;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import javax.swing.table.AbstractTableModel;

class GenericTableModel extends AbstractTableModel {

    String[] columnNames;
    Object[][] data;

    public GenericTableModel(int columns,int rows){
	super();
	columnNames = new String[columns];
	data = new Object[rows][columns];
    }
    
    public void reset(int cols, int rows){
    	data = new Object[rows][cols];
    }

    public int getColumnCount() {
	return columnNames.length;
    }
    
    public int getRowCount() {
	return data.length;
    }
    
    public String getColumnName(int col) {
	return columnNames[col];
    }

    public void setColumnName(int col, String name) {
	columnNames[col] = name;
    }
    
    public Object getValueAt(int row, int col) {
	return data[row][col];
    }

    public void setValueAt(Object object, int row, int col) {
	data[row][col] = object;
    }

    public Class getColumnClass(int c) {
	return getValueAt(0, c).getClass();
    }

}

public class GenericTable extends JPanel {

    JTable table;
    TableModel model;
    JScrollPane scrollPane;
    GenericTableMenu menu;
    boolean debug;

    public GenericTable(){
	model = new DefaultTableModel();
	installTable();
    }

    public GenericTable(TableModel _model){
	model = _model;
	installTable();
    }
    
    public TableModel getTableModel() {return model;}

    public void installTable(){
	debug = false;
	table = new JTable(model);
	menu = new GenericTableMenu(this);
	setLayout(new BorderLayout());
	scrollPane = new JScrollPane(table);
	add(scrollPane, BorderLayout.CENTER);
	table.addMouseListener(new MouseAdapter() {
		public void mouseClicked(MouseEvent e) {
		    int row = table.getSelectedRow();
		    int col = table.getSelectedColumn();
		    int clickCount = e.getClickCount();
		    if (debug) {
			System.out.println("--------------------------------------");
			System.out.println("Mouse Event Detected");
			System.out.println("Selected Row : "+row);
			System.out.println("Selected Column : "+col);
			System.out.println("--------------------------------------");
		    }
		    if ((clickCount == 1) && ((e.getModifiers() & InputEvent.BUTTON1_MASK) == InputEvent.BUTTON1_MASK)) click(row, col);
		    if ((clickCount == 2) && ((e.getModifiers() & InputEvent.BUTTON1_MASK) == InputEvent.BUTTON1_MASK)) doubleClick(row, col);
		    if ((e.getModifiers() & InputEvent.BUTTON3_MASK) == InputEvent.BUTTON3_MASK){
			menu.getPopup().show(e.getComponent(), e.getX(), e.getY());
			rightClick(row,col);
		    }
		}
	    });
    }

    public void click(int row, int col){
	if (debug) {
	    System.out.println("--------------------------------------");
	    System.out.println("Simple Click Detected");
	    System.out.println("Selected : row-> "+row+" column-> "+col);
	    System.out.println("--------------------------------------");
	}
    }
    
    public void doubleClick(int row, int col){
	if (debug) {
	    System.out.println("--------------------------------------");
	    System.out.println("Double Click Detected");
	    System.out.println("Selected : row-> "+row+" column-> "+col);
	    System.out.println("--------------------------------------");
	}
    }
    
    public void rightClick(int row, int col){
	if (debug) {
	    System.out.println("--------------------------------------");
	    System.out.println("Right Click Detected");
	    System.out.println("Selected : row-> "+row+" column-> "+col);
	    System.out.println("--------------------------------------");
	}
    }
    
}

