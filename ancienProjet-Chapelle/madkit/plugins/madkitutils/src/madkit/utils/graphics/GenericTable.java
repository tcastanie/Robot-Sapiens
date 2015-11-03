/*
 * Created on 25 déc. 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package madkit.utils.graphics;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.table.*;

/**
 * @author Jaco
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
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
    
	/**
	 * @return Returns the menu.
	 */
	public GenericTableMenu getMenu() {
		return menu;
	}

	/**
	 * @return Returns the model.
	 */
	public TableModel getModel() {
		return model;
	}

	/**
	 * @return Returns the table.
	 */
	public JTable getTable() {
		return table;
	}

}
