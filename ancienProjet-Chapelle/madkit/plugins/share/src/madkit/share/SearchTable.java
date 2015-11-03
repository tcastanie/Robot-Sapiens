package madkit.share;
import java.util.Vector;

import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;

class SearchTable extends GenericTable {

    int selectedRow=-1;
    Vector stockResultSearchFile;

    public SearchTable(Vector stock){
	super(new SearchTableModel(stock));
	stockResultSearchFile=stock;
	initTable();
    }

    private void initTable(){
	((TableColumn)(table.getColumnModel().getColumn(0))).setPreferredWidth(10);
	((TableColumn)(table.getColumnModel().getColumn(1))).setPreferredWidth(200);
	((TableColumn)(table.getColumnModel().getColumn(2))).setPreferredWidth(20);
	ListSelectionModel rowSM = table.getSelectionModel();
	rowSM.addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                    if (e.getValueIsAdjusting()) return;
                    ListSelectionModel lsm = (ListSelectionModel)e.getSource();
                    if (!lsm.isSelectionEmpty()) {          
			selectedRow = lsm.getMinSelectionIndex();
		    }
		}
	    });
    }

    public int getSelectedRow() {return selectedRow;}
    public Vector getVector() {return stockResultSearchFile;}

    public void click(int row, int col){
    }

    public void doubleClick(int row, int col){
    }

}
