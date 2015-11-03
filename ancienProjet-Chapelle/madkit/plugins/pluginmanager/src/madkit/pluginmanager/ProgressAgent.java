/*
 * ProgressAgent.java - Created on Jan 31, 2004
 * 
 * Copyright (C) 2003-2004 Sebastian Rodriguez
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
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
 * Last Update: $Date: 2004/04/14 11:55:47 $
 */

package madkit.pluginmanager;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import madkit.kernel.Agent;
import madkit.kernel.Message;
import madkit.kernel.StringMessage;



/**
 * @author Sebastian Rodriguez - sebastian.rodriguez@utbm.fr
 *
 * @version $Revision: 1.3 $
 */
public class ProgressAgent extends Agent implements UpdateRoles{
	private boolean _alive=true;
	private ProgressGUI _gui;
	
	/**
	 * 
	 */
	public ProgressAgent() {
		super();
		
	}

	/* (non-Javadoc)
	 * @see madkit.kernel.Agent#live()
	 */
	public void live() {
		while(_alive){
			Message m=waitNextMessage(1000);
			exitImmediatlyOnKill();
			if(m instanceof DownloadStatus){
				_gui.updateStatus((DownloadStatus) m);
			}else if(m instanceof StringMessage){
				hide(Boolean.valueOf(((StringMessage)m).getString()).booleanValue());
			}
			
		}
	}

	/* (non-Javadoc)
	 * @see madkit.kernel.AbstractAgent#activate()
	 */
	public void activate() {
		requestRole(community, group, PROGRESS,null);
	}

	/* (non-Javadoc)
	 * @see madkit.kernel.AbstractAgent#end()
	 */
	public void end() {
		leaveRole(community, group, PROGRESS);
	}

	/* (non-Javadoc)
	 * @see madkit.kernel.AbstractAgent#initGUI()
	 */
	public void initGUI() {
		_gui=new ProgressGUI(this);
		setGUIObject(_gui);
		
	}
	

	public void hide(boolean hide){
		if(hide){
			disposeMyGUI();
			_alive=false;
		}else{
			redisplayMyGUI();
			_gui.refresh();
		}
	}

}

class ProgressGUI extends JPanel{
	private JTable _table;
	private DownloadsTableModel _model;
	private ProgressAgent _agent;
	public ProgressGUI(ProgressAgent agent){
		_agent=agent;
		setLayout(new BorderLayout());
		
		_table=setupTable();
		
		JScrollPane scroll=new JScrollPane();
		scroll.setViewportView(_table);
		add(scroll,BorderLayout.CENTER);
		
		JPanel btnPanel=new JPanel();
		JButton bHide=new JButton("Hide");
		bHide.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				_agent.hide(true);
				
			}
		});
		btnPanel.add(bHide);
		
		add(btnPanel,BorderLayout.SOUTH);
	}
	
	private JTable setupTable(){
		JTable t=new JTable();
		_model=new DownloadsTableModel();
		t.setModel(_model);
		TableColumn column=t.getColumnModel().getColumn(DownloadsTableModel.PROGRESS);
		column.setCellRenderer(new DownloadTableCellRenderer());
		return t;
	}
	
	public void refresh(){
		
		_model.fireTableDataChanged();
	}
	
	public void updateStatus(DownloadStatus msg){
		_model.addDownloading(msg.getPluginName(),msg);
		_model.fireTableDataChanged();
	}
}


/////////////////////////////////////////////////////////////////////////////////////////////
/////////// Table Model
///////////////////////////////////////////////////////////////////////////////////////////

class DownloadsTableModel extends AbstractTableModel{
	private Hashtable _downloading;
	
	final static int PLUGIN_NAME=0;
	final static int PROGRESS=1;
	final static int DETAILS=2;
	
	public final static  String[] _columnNames={"Plugin",
															"Progress",
															"Downloaded"
															};
	public DownloadsTableModel(){
		_downloading=new Hashtable();
	}

	/*------------------------------------------------------------------------------------*/
	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	public int getRowCount() {
		//removeDone();
		return _downloading.size();
	}

	/**
	 * 
	 */
	public void removeDone() {
		
		for (Iterator it = _downloading.values().iterator(); it.hasNext();) {
			DownloadStatus element = (DownloadStatus) it.next();
			if(element.getSize()==element.getDownloaded()){
				_downloading.remove(element.getPluginName());
		
			}
			
		}
		
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
		Vector v=new Vector(_downloading.values());
		DownloadStatus status=(DownloadStatus) v.get(rowIndex);
	
		switch (columnIndex) {
			case PLUGIN_NAME :
					result=status.getPluginName();
				break;
			case PROGRESS :
					result=new Integer(status.getDownloaded());
				break;
			case DETAILS :
				return status.getDownloaded()+" / "+status.getSize();
				//break;
			default :
				break;
		}
		return result;
	}
	
	public String getColumnName(int col){
		return _columnNames[col];
	}
	
	public boolean isCellEditable(int row, int col){return false;}
	
	public Class getColumnClass(int c) {
	   return getValueAt(0, c).getClass();
   }
	
	public void setValueAt(Object obj, int row, int col){}
	
	public void addDownloading(String name, DownloadStatus status){
		//if(status.getSize()==status.getDownloaded()){
			//_downloading.remove(name);
		//}else{
			_downloading.put(name,status);
		//}		
		
	}

	/**
	 * @param object
	 * @return
	 */
	public int getFileSize(int row) {
		return ((DownloadStatus)_downloading.get(getValueAt(row,0))).getSize();
		
	}
	
}

class DownloadTableCellRenderer extends DefaultTableCellRenderer {
		
		public Component getTableCellRendererComponent
				(JTable table, Object value, boolean isSelected,boolean hasFocus, int row, int column){
				if(column==DownloadsTableModel.PROGRESS){
					if(value instanceof Integer){
						DownloadsTableModel model =(DownloadsTableModel) table.getModel();
						int size=model.getFileSize(row);
						int d=((Integer)value).intValue();
						JProgressBar pb=new JProgressBar(0,size);
						pb.setValue(d);
						
						return pb;
					}
					
				}
				return super.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,column);
		 }

}

