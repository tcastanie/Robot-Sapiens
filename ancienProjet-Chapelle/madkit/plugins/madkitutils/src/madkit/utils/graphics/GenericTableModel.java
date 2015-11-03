/*
 * Created on 25 déc. 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package madkit.utils.graphics;

import javax.swing.table.*;

/**
 * @author Jaco
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class GenericTableModel extends AbstractTableModel {

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
