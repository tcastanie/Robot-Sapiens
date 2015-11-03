/*
 * RouterGUI.javaCreated on Dec 15, 2003
 *
 * Copyright (C) 2003 Sebastian Rodriguez
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.

 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 *Last Update $Date: 2003/12/17 16:33:14 $

 */
package madkit.netcomm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

/**
 * @author Sebastian Rodriguez - sebastian.rodriguez@utbm.fr
 *	@version $Revision: 1.1 $
 */
class RouterGUI extends JPanel {
	private RouterAgent _agent;
	private RouteTableModel _model;
	public RouterGUI(RouterAgent agent){
		_agent=agent;
		
		setLayout(new BorderLayout());
		
		JScrollPane scroll=new JScrollPane();
		JTable table=setupTable();
		
		scroll.setViewportView(table);
		add(scroll,BorderLayout.CENTER);
		setPreferredSize(new Dimension(200,200));
	}
	
	private JTable setupTable(){
		JTable table=new JTable();
		_model=new RouteTableModel(_agent);
		table.setModel(_model);
		CustomTableCellRenderer render= new CustomTableCellRenderer();
		for(int i=0;i<_model.getColumnCount();i++){
			TableColumn column=table.getColumnModel().getColumn(i);
			column.setCellRenderer(render);   
	   }
		return table;
	}
	
	public void updateRoutes(){
		_model.fireTableDataChanged();
		
	}
}

class RouteTableModel extends AbstractTableModel{
	RouterAgent _agent;
	
	private final static int HOST=0;
	private final static int PORT=1;
	private final static int PROTOCOL=2;
	
	private String[] _columnNames={"Host",
															"Port",
															"Protocol"
															};
	public RouteTableModel(RouterAgent agent){
		_agent=agent;
	}

	/*------------------------------------------------------------------------------------*/
	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	public int getRowCount() {
		if(_agent==null  || _agent.routeTable==null)
			return 0;
		else
		return _agent.routeTable.keySet().size();
	}

	/*------------------------------------------------------------------------------------*/
	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	public int getColumnCount() {
		return _columnNames.length;
	}

	/*------------------------------------------------------------------------------------*/
	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	public Object getValueAt(int rowIndex, int columnIndex) {
		Object result=null;
		Vector v=new Vector(_agent.routeTable.values());
		DistantKernelInformation info=(DistantKernelInformation) v.get(rowIndex);
		switch (columnIndex) {
			case HOST :
					result=info.getHost();
				break;
			case PORT :
					result=new Integer(info.getSocketKernel().getPort());
				break;
			case PROTOCOL :
				result=info.getProtocol();
				break;
			default :
				break;
		}
		return result;
	}
	
	public String getColumnName(int col){
		return _columnNames[col];
	}
	
	public boolean isCellEditable(int row, int col){
		return false;
	}
	
	public void setValueAt(Object obj, int row, int col){}
}


class CustomTableCellRenderer extends DefaultTableCellRenderer {
		private static final Color CONFIG_COLOR=Color.YELLOW;
		
		public Component getTableCellRendererComponent
				(JTable table, Object value, boolean isSelected,boolean hasFocus, int row, int column){
				Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				cell.setForeground(table.getForeground());
				// if cell is selected, set background color to default cell selection background color
				if (isSelected) {
				  cell.setBackground(table.getSelectionBackground());
				}
				// otherwise, set cell background color to our custom color
				else {
				 
				   if(value instanceof String){
					if(((String)value).equals(RouterAgent.CONFIG)){
						cell.setBackground(CONFIG_COLOR);
					}else{
						cell.setBackground(Color.WHITE);
					}
				   }
				   
				}

				return cell;
		 }

}
