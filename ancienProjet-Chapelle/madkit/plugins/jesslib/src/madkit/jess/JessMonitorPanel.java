/*
* JessMonitorPanel.java - JessEditor agents, a simple editor to evaluate Jess rules
* Copyright (C) 2000-2002 Jacques Ferber
*
* This program is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License
* as published by the Free Software Foundation; either version 2
* of the License, or any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
*/
package madkit.jess;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import madkit.utils.agents.AbstractEditorPanel;

class EditorSetTargetDialog extends JDialog implements ActionListener
{

	String currentGroup;
	String currentRole;

	Vector groups;
	String[] roles;
	JList roleList;

	JessMonitor ag;

	public EditorSetTargetDialog(JessMonitor _ag){

		super();
		setTitle("Setting a target");
	  	// setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		ag = _ag;

	  	getContentPane().setLayout(new BorderLayout(10,10));

	  	// les boutons du bas
	  	JPanel buttonPanel = new JPanel();
	    JButton okButton = new JButton("OK");
	    JButton applyButton = new JButton("Apply");
	    JButton cancelButton = new JButton("Cancel");

	   	buttonPanel.add(okButton);
	   	okButton.addActionListener(this);
	   	buttonPanel.add(applyButton);
	   	applyButton.addActionListener(this);
	   	buttonPanel.add(cancelButton);
	   	cancelButton.addActionListener(this);

		getContentPane().add(buttonPanel,"South");


		JPanel middlePanel = new JPanel(new GridLayout(1,2,10,10));
		getContentPane().add(middlePanel,"Center");

		JPanel groupListPanel = new JPanel(new BorderLayout());
		middlePanel.add(groupListPanel);

    	// GroupListPanel
		JLabel groupLabel = new JLabel("Groups");
		groupListPanel.add(groupLabel,"North");
		groups = ag.askGetGroups();
		JList groupList = new JList(groups);
		JScrollPane groupListScrollPane = new JScrollPane(groupList);
    	groupListPanel.add(groupListScrollPane, "Center");

    	// RoleListPanel
    	JPanel roleListPanel = new JPanel(new BorderLayout());
		middlePanel.add(roleListPanel);

		JLabel roleLabel = new JLabel("Roles");
		roleListPanel.add(roleLabel,"North");
		roleList = new JList();
		JScrollPane roleListScrollPane = new JScrollPane(roleList);
    	roleListPanel.add(roleListScrollPane, "Center");


    	// les actions
    	groupList.addListSelectionListener(new ListSelectionListener(){
    		public void valueChanged(ListSelectionEvent e) {
    			if (e.getValueIsAdjusting())
    				return;
    			JList theList = (JList)e.getSource();
    			if (!theList.isSelectionEmpty()) {
    				int index = theList.getSelectedIndex();
    				currentGroup = (String) groups.elementAt(index);
					ag.setTargetGroup(currentGroup);
    				roles=ag.askGetRoles(currentGroup);
					roleList.setListData(roles);
				}
			}
        });

       roleList.addListSelectionListener(new ListSelectionListener(){
    		public void valueChanged(ListSelectionEvent e) {
    			if (e.getValueIsAdjusting())
    				return;
    			JList theList = (JList)e.getSource();
    			int index = theList.getSelectedIndex();
    			ag.setTargetRole(currentRole);
    			currentRole = (String) roles[index];
    		}
        });


	   pack();
	   show();
	}



	public void actionPerformed(ActionEvent e) {
				String s = e.getActionCommand();
                if (s.equals("OK")) {
                	ag.setTarget(currentGroup, currentRole);
                	dispose();
                } else if (s.equals("Apply")) {
                	ag.setTarget(currentGroup, currentRole);
                } else
                	dispose();
    }
}

class JessMonitorPanel extends AbstractEditorPanel {

 	boolean[] watchItems=new boolean[]{false,false,false,false};

    protected JPanel commandPanel;

 	JLabel currentTargetLabel;

    JessMonitorPanel (JessMonitor _ag) {
      		super(_ag);

      		JMenuBar myMenubar = getMenubar();
	        // menu "Control"
	        JMenu menuControl=new JMenu("Actions");
	        myMenubar.add(menuControl);

	        addMenuItem(this, menuControl, "Launch", "launch", KeyEvent.VK_J, KeyEvent.VK_L);
	        addMenuItem(this, menuControl, "Select", "select", KeyEvent.VK_J, -1);
	        addMenuItem(this, menuControl, "Run", "run", KeyEvent.VK_J, KeyEvent.VK_J);
	        addMenuItem(this, menuControl, "Halt", "halt", KeyEvent.VK_T, KeyEvent.VK_T);
	        addMenuItem(this, menuControl, "Facts", "facts", KeyEvent.VK_F, KeyEvent.VK_F);
	        addMenuItem(this, menuControl, "Rules", "rules", KeyEvent.VK_R, KeyEvent.VK_R);
	        addMenuItem(this, menuControl, "Reset", "reset", KeyEvent.VK_D, KeyEvent.VK_D);
	        addMenuItem(this, menuControl, "Reset", "reset", KeyEvent.VK_D, KeyEvent.VK_D);
	        addMenuItem(this, menuControl, "Re-init", "reinit", KeyEvent.VK_Y, KeyEvent.VK_Y);
	        addMenuItem(this, menuControl, "Eval buffer", "evalBuffer", KeyEvent.VK_B, KeyEvent.VK_B);


	        JToolBar mytoolBar = getToolbar();
	        mytoolBar.addSeparator();
		//			/addTool(toolBar, "print", "demo/agents/system/print.gif");
	        addTool(mytoolBar, "launch", "Launch", "/images/toolbars/launch.gif");
	        addTool(mytoolBar, "select", "Select", "/images/toolbars/settarget.gif");
			mytoolBar.addSeparator();
	        addTool(mytoolBar, "run", "Run","/images/toolbars/run.gif");
	        addTool(mytoolBar, "halt", "Halt", "/images/toolbars/halt.gif");
	        addTool(mytoolBar, "facts", "Facts","/images/toolbars/facts.gif");
	        addTool(mytoolBar, "rules", "Rules","/images/toolbars/rules.gif");
	        addTool(mytoolBar, "reset", "Reset","/images/toolbars/reset.gif");
	        addTool(mytoolBar, "reinit", "Re-init","/images/toolbars/reinit.gif");
	        addTool(mytoolBar, "watch", "Watch all","/images/toolbars/watch.gif");
	        addTool(mytoolBar, "unwatch", "Unwatch all","/images/toolbars/unwatch.gif");
			mytoolBar.addSeparator();
	        addTool(mytoolBar, "evalBuffer", "Send buffer", "/images/toolbars/sendbuf.gif");
	        addTool(mytoolBar, "evalSelection", "Send selection", "/images/toolbars/sendsel.gif");

	        JPanel displayLabelPanel = new JPanel(new BorderLayout());
		  	getContentPane().add(displayLabelPanel,"South");

 			currentTargetLabel=new JLabel("Target : none");
 			displayLabelPanel.add(currentTargetLabel,"West");

    }

   public void println(String s){
   		stdout().println(s);
   }

   void setRecipientLabel(String s){
    	currentTargetLabel.setText("Target : "+s);
    }


 	public void command(String c){
 		if (c.equals("evalBuffer")) evalBuffer();
 		else if (c.equals("evalSelection")) evalSelection();
 		else if (c.equals("run"))
     		((JessMonitor) ag).sendControlMessage("run");
 		else if (c.equals("halt"))
     		((JessMonitor) ag).sendControlMessage("halt");
   		else if(c.equals("reset"))
  	 		((JessMonitor) ag).sendControlMessage("reset");
   		else if(c.equals("facts"))
  	 		((JessMonitor) ag).sendControlMessage("facts");
   		else if(c.equals("rules"))
  	 		((JessMonitor) ag).sendControlMessage("rules");
   		else if(c.equals("reinit"))
  	 		((JessMonitor) ag).sendControlMessage("reinit");
   		else if(c.equals("watch"))
  	 		((JessMonitor) ag).sendControlMessage("watch", "all");
   		else if(c.equals("unwatch"))
  	 		((JessMonitor) ag).sendControlMessage("unwatch", "all");
   		else if(c.equals("load"))
  	 		loadFile();
  	 	else if (c.equals("launch"))
  	 		launchJessFromFile();
  	 	else if (c.equals("select"))
  	 		selectTarget();
  	 	else super.command(c);
 	}

   void selectTarget(){
   		// System.out.println("creation de groupe..");
   		new EditorSetTargetDialog((JessMonitor) ag);
   }

    void evalBuffer() {
        String s = inputArea.getText();
     	((JessMonitor) ag).sendControlMessage("eval",s);
   }

   void evalSelection() {
     	String s = inputArea.getSelectedText();
     	((JessMonitor) ag).sendControlMessage("eval",s);
   }

   void loadFile() {
		Object result;
		// SEditApp.setInstallFileName(false);
		if (getFileDialog(LOAD, "Load file","clp")) {
       		if (getCurrentFile() != null)
  	 			((JessMonitor) ag).sendControlMessage("load", getCurrentFile());
  	 	}
	}

   void launchJessFromFile() {
		Object result;
		if (getFileDialog(LOAD,"Launch File","clp")) {
			if (getCurrentFile() != null){
				((JessMonitor) ag).launch(getCurrentFile());
			}
		}
 	}

}
