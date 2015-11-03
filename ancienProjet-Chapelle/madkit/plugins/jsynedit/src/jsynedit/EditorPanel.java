/*
* EditorPanel.java -a NotePad agent, to edit text and send string messages to other agents
* Copyright (C) 1998-2002 Jacques Ferber
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
package jsynedit;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import madkit.kernel.*;
import madkit.utils.graphics.*;


/*
class EditorJoinGroup extends JDialog implements ActionListener
{
    String currentGroup;
    String currentRole;

    String[] myGroupList;
    Vector allGroups;
    String[] roles;

    JComboBox groupChooser;
    JComboBox roleChooser;

    boolean activeChooser=true;

    EditorAgent ag;

    public EditorJoinGroup(EditorAgent _ag, Frame frame, Vector allGrList){

	super(frame,true);
	setTitle("Join a group");
        this.setLocationRelativeTo(frame);
	// setDefaultCloseOperation(DISPOSE_ON_CLOSE);

	ag = _ag;

	myGroupList = ag.askGetGroups();
	allGroups = allGrList;

	getContentPane().setLayout(new BorderLayout(10,10));

	// les boutons du bas
	JPanel buttonPanel = new JPanel();
	JButton okButton = new JButton("OK");
	JButton applyButton = new JButton("Apply");
	JButton cancelButton = new JButton("Cancel");
	getContentPane().add(buttonPanel,"South");

	buttonPanel.add(okButton);
	okButton.addActionListener(this);

	   	// le panel du milieu

		JPanel middlePanel = new JPanel(new BorderLayout());
		getContentPane().add(middlePanel,"Center");
		Border chooserBorder = new TitledBorder(null, "Select the group and role to join",
					       		TitledBorder.LEFT, TitledBorder.TOP);

		Border emptyBorder = new EmptyBorder(5,5,5,5);
		Border compoundBorder = new CompoundBorder( chooserBorder, emptyBorder);

		middlePanel.setBorder(compoundBorder);



		   	JPanel selectChooserPanel = new JPanel(new GridLayout(2,1,10,20));
		   	JPanel selectLabelPanel = new JPanel(new GridLayout(2,1,10,20));
			middlePanel.add(selectLabelPanel,"West");
			middlePanel.add(selectChooserPanel,"Center");

		   	// la selection des groupes...
			JLabel groupLabel = new JLabel("group : ");
			selectLabelPanel.add(groupLabel);
	  		// selectLabelPanel.setMargin(new Insets(10,10,10,10));

			JComboBox groupChooser = new JComboBox(allGroups);
			groupChooser.setEditable(true);
        	groupChooser.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	              	JComboBox cb = (JComboBox)e.getSource();
		           	currentGroup = (String)cb.getSelectedItem();
			        // ag.enterGroup(currentGroup);

		           	String[] roles = ag.askGetRoles(currentGroup);
				   	activeChooser=false;
    				roleChooser.removeAllItems();
    				for(int i=0; i<roles.length; i++){
    					roleChooser.addItem(roles[i]);
    				}
    				activeChooser=true;
		           //cb.setSelectedItem(groupName);
	            	}
        		});

        	selectChooserPanel.add(groupChooser);

        	// la selection des roles dans un groupe

			JLabel roleLabel = new JLabel("role : ");
			selectLabelPanel.add(roleLabel);

			roleChooser = new JComboBox();
			roleChooser.setEditable(true);
	  		// groupChooser.setMargin(new Insets(10,10,10,10));
        	roleChooser.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	            	if (activeChooser){
	               		JComboBox cb = (JComboBox)e.getSource();
		           		currentRole = (String)cb.getSelectedItem();
		           		ag.enterRole(currentGroup,currentRole);
		           		//cb.setSelectedItem(roleName);
		           	}
	            }
        	});

        	selectChooserPanel.add(roleChooser);

	   pack();
	   show();
	}

	public void actionPerformed(ActionEvent e) {
				String s = e.getActionCommand();
                if (s.equals("OK")) {
                	//ag.askRequestRole(currentGroup, currentRole);
                	dispose();
                } else
                	dispose();
    }
}
*/

class EditorGroupDialog extends JDialog implements ActionListener
{
    JComboBox commChooser;
    JComboBox groupChooser;
    JComboBox roleChooser;
    JTextField passwdField;
    JSynEditAgent ag;

    String community=Kernel.DEFAULT_COMMUNITY;

    public EditorGroupDialog(JSynEditAgent _ag, Frame parent){

	super(parent,true);
	setTitle("Entering a new group");
        this.setLocationRelativeTo(parent);
	ag = _ag;
	getContentPane().setLayout(new BorderLayout(10,10));
	//	System.err.prinln("Ga?");

	// les boutons du bas
	JPanel buttonPanel = new JPanel();
	JButton okButton = new JButton("OK");
	JButton cancelButton = new JButton("Cancel");

	buttonPanel.add(okButton);
	okButton.addActionListener(this);
	buttonPanel.add(cancelButton);
	cancelButton.addActionListener(this);

	getContentPane().add(new JLabel("Choose among existing groups and roles, or enter new role"),"North");
	getContentPane().add(buttonPanel,"South");

	// le panel du milieu
	JPanel middlePanel = new JPanel(new BorderLayout());
	getContentPane().add(middlePanel,"Center");

	JPanel labelPanel = new JPanel(new GridLayout(4,1));
	middlePanel.add(labelPanel,"West");

	JPanel fieldPanel = new JPanel(new GridLayout(4,1));
	middlePanel.add(fieldPanel,"Center");


	JLabel commLabel = new JLabel("communities : ");
	labelPanel.add(commLabel);
	JLabel groupLabel = new JLabel("group : ");
	labelPanel.add(groupLabel);
	JLabel roleLabel = new JLabel("role : ");
	labelPanel.add(roleLabel);
	JLabel paswdLabel = new JLabel("password : ");
	labelPanel.add(paswdLabel);

        commChooser = new JComboBox();
	commChooser.setEditable(true);
        String[] comm = ag.getAvailableCommunities();
        for(int i=0; i<comm.length; i++){
	    commChooser.addItem(comm[i]);
    	}

	commChooser.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    JComboBox cb = (JComboBox)e.getSource();
		    community = (String)cb.getSelectedItem();
                    showAllGroups();
		    //showRoles(groupName);
		}});
	fieldPanel.add(commChooser);

	groupChooser = new JComboBox();
	groupChooser.setEditable(true);
	showAllGroups();
	groupChooser.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    JComboBox cb = (JComboBox)e.getSource();
		    String groupName = (String)cb.getSelectedItem();
		    //	    ag.enterGroup(groupName);
		    showRoles(groupName);
		}});
	fieldPanel.add(groupChooser);
	roleChooser = new JComboBox();
	roleChooser.setEditable(true);
	showRoles((String)groupChooser.getSelectedItem());
	fieldPanel.add(roleChooser);

        passwdField = new JTextField();
        fieldPanel.add(passwdField);

	pack();
	show();
    }

    void showAllGroups()
    {
	String[] groups=ag.getExistingGroups(community);

    	groupChooser.removeAllItems();
    	for(int i=0; i<groups.length; i++){
	    groupChooser.addItem(groups[i]);
    	}
    }

    void showRoles(String group){
        //System.out.println("showRoles: " + community + ", group: " + group);
	if ((community != null) && (group!=null) && (!group.equals("")))
	    {
		String[] v = ag.getExistingRoles(community,group);
		roleChooser.removeAllItems();
		//		for(int i=0; i<roles.length; i++){
		for (int i=0; i<v.length; i++){
                    roleChooser.addItem(v[i]);
                }
	    }
    }

    public void actionPerformed(ActionEvent e) {
	String s = e.getActionCommand();
	if (s.equals("OK")) {
	    ag.enterGroupRole(
                    community,
                    (String)groupChooser.getSelectedItem(),
                    (String)roleChooser.getSelectedItem(),
                    passwdField.getText());
	    dispose();
	} else
	    dispose();
    }
}

class EditorSetTargetDialog extends JDialog implements ActionListener
{

	String currentGroup;
	String currentRole;

	String[] groups;
	String[] roles;
	JList roleList;

	JSynEditAgent ag;

	public EditorSetTargetDialog(JSynEditAgent _ag, Frame parent){

		super(parent,true);
		setTitle("Setting a target");
                setLocationRelativeTo(parent);
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
		groups =   ag.askGetGroups();
		JList groupList = new JList(groups);
		groupList.setFixedCellWidth(20);
		JScrollPane groupListScrollPane = new JScrollPane(groupList);
		groupListPanel.add(groupListScrollPane, "Center");
		//		groupListScrollPane.setSize(20,20);

		// RoleListPanel
		JPanel roleListPanel = new JPanel(new BorderLayout());
		middlePanel.add(roleListPanel);

		JLabel roleLabel = new JLabel("Roles");
		roleListPanel.add(roleLabel,"North");
		roleList = new JList();
		roleList.setFixedCellWidth(20);
		JScrollPane roleListScrollPane = new JScrollPane(roleList);
		roleListPanel.add(roleListScrollPane, "Center");
		//	roleListScrollPane.setSize(20,20);


		// les actions
		groupList.addListSelectionListener(new ListSelectionListener(){
			public void valueChanged(ListSelectionEvent e) {
			    if (e.getValueIsAdjusting())
    				return;
			    JList theList = (JList)e.getSource();
			    if (!theList.isSelectionEmpty()) {
    				int index = theList.getSelectedIndex();
    				currentGroup = groups[index];
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
    			try
{
    JList theList = (JList)e.getSource();
    int index = theList.getSelectedIndex();
    ag.setTargetRole(currentRole);
    currentRole = (String) roles[index];
}
catch (ArrayIndexOutOfBoundsException ex) {}		}

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


class EditorLeaveRoleDialog extends JDialog implements ActionListener
{

	String currentGroup;
	String currentRole;

	String[] groups;
	String[] roles;
	JList roleList;

	JSynEditAgent ag;

	public EditorLeaveRoleDialog(JSynEditAgent _ag, Frame parent){

		super(parent,true);
		setTitle("Leaving a role");
                this.setLocationRelativeTo(parent);
	  	// setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		ag = _ag;

	  	getContentPane().setLayout(new BorderLayout(10,10));

	  	// les boutons du bas
	  	JPanel buttonPanel = new JPanel();
		JButton okButton = new JButton("Leave");
		JButton cancelButton = new JButton("Cancel");

	   	buttonPanel.add(okButton);
	   	okButton.addActionListener(this);
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
		groupList.setFixedCellWidth(20);
		JScrollPane groupListScrollPane = new JScrollPane(groupList);
		groupListPanel.add(groupListScrollPane, "Center");
		//		groupListScrollPane.setSize(20,20);

		// RoleListPanel
		JPanel roleListPanel = new JPanel(new BorderLayout());
		middlePanel.add(roleListPanel);

		JLabel roleLabel = new JLabel("Roles");
		roleListPanel.add(roleLabel,"North");
		roleList = new JList();
		roleList.setFixedCellWidth(20);
		JScrollPane roleListScrollPane = new JScrollPane(roleList);
		roleListPanel.add(roleListScrollPane, "Center");
		//	roleListScrollPane.setSize(20,20);


		// les actions
        groupList.addListSelectionListener(new ListSelectionListener(){
                public void valueChanged(ListSelectionEvent e) {
                    if (e.getValueIsAdjusting())
                        return;
                    JList theList = (JList)e.getSource();
                                    if (!theList.isSelectionEmpty()) {
                                        int index = theList.getSelectedIndex();
                                        currentGroup = (String) groups[index];
                                        roles=ag.askGetOwnRoles(currentGroup);
                                        roleList.setListData(roles);
                                    }
                    }
                });

       roleList.addListSelectionListener(new ListSelectionListener(){
    		public void valueChanged(ListSelectionEvent e) {
    			if (e.getValueIsAdjusting())
    				return;
    			try
{
    JList theList = (JList)e.getSource();
    int index = theList.getSelectedIndex();
    //  ag.setTargetRole(currentRole);
    currentRole = (String) roles[index];
}
catch (ArrayIndexOutOfBoundsException ex) {}		}

        });


	   pack();
	   show();
	}



	public void actionPerformed(ActionEvent e) {
				String s = e.getActionCommand();
                if (s.equals("OK")) {
                	ag.leaveGR(currentGroup, currentRole);
                	dispose();
                } else
                	dispose();
    }
}



class EditorPanel extends AbstractEditorPanel {

 	JComboBox groupChooser;
 	JComboBox roleChooser;
 	boolean activeChooser=false;

 	JLabel currentGroupLabel;
 	JLabel currentRoleLabel;

        JCheckBoxMenuItem showMessagesItem;
        JCheckBoxMenuItem showOutMessagesItem;

    protected JPanel commandPanel;


      EditorPanel (JSynEditAgent _ag){
      		super(_ag);

      		JMenuBar myMenubar = getMenubar();
	        // menu "Control"
	        JMenu menuControl=new JMenu("Actions");
	        myMenubar.add(menuControl);
	        addMenuItem(this, menuControl, "Create agent from script", "createAgentFromScript", KeyEvent.VK_D, KeyEvent.VK_D);
	        addMenuItem(this, menuControl, "Join group/role", "joingroup", KeyEvent.VK_J, KeyEvent.VK_J);
	        addMenuItem(this, menuControl, "Leave group/role", "requestLeaveRole", KeyEvent.VK_J, KeyEvent.VK_J);
	        addMenuItem(this, menuControl, "Set target", "setTarget", KeyEvent.VK_T, KeyEvent.VK_T);
	        addMenuItem(this, menuControl, "Send selection", "sendsel", KeyEvent.VK_E, KeyEvent.VK_E);
	        addMenuItem(this, menuControl, "Send buffer", "sendbuff", KeyEvent.VK_B, KeyEvent.VK_B);

               // JMenu menuOptions=new JMenu("EditorPanelOptions");
	        //myMenubar.add(menuOptions);
	        	menuOptions.addSeparator();
                showMessagesItem = new JCheckBoxMenuItem("Show incoming messages");
                showMessagesItem.setActionCommand("showInMessages");
                showMessagesItem.setSelected(true);
                showMessagesItem.addActionListener(this);
                menuOptions.add(showMessagesItem);


                showOutMessagesItem = new JCheckBoxMenuItem("Show outgoing messages");
                showOutMessagesItem.setActionCommand("showOutMessages");
                showOutMessagesItem.setSelected(true);
                showOutMessagesItem.addActionListener(this);
                menuOptions.add(showOutMessagesItem);


	        JToolBar mytoolBar = getToolbar();
	        mytoolBar.addSeparator();
		//			/addTool(toolBar, "print", "demo/agents/system/print.gif");
	        addTool(mytoolBar, "joingroup", "Join group","/images/toolbars/joingroup.gif");
	        addTool(mytoolBar, "requestLeaveRole", "Leave role","/images/toolbars/leaverole.gif");
	        addTool(mytoolBar, "setTarget", "Set target (group/role)", "/images/toolbars/settarget.gif");
			mytoolBar.addSeparator();
	        addTool(mytoolBar, "sendbuff", "Send buffer", "/images/toolbars/sendbuf.gif");
	        addTool(mytoolBar, "sendsel", "Send selection", "/images/toolbars/sendsel.gif");

		   	JPanel displayLabelPanel = new JPanel(new BorderLayout());
		  	getContentPane().add(displayLabelPanel,"South");

 			currentGroupLabel=new JLabel("Target group: none");
 			currentRoleLabel=new JLabel("Target role: none");
 			displayLabelPanel.add(currentGroupLabel,"West");
 			displayLabelPanel.add(currentRoleLabel,"East");

    }


 	public void command(String c){
 		if (c.equals("sendbuff")) evalBuffer();
 		else if (c.equals("sendsel")) evalSelection();
 		else if (c.equals("setTarget")) setTarget();
		else if (c.equals("createAgentFromScript")) {
			if (getInputArea()!= null){
				String path = getInputArea().getCurrentFile();
				((JSynEditAgent) ag).createAgentFromScript(path);
			}
			else System.err.println("No files");
		}
 		else if (c.equals("requestLeaveRole")) requestLeaveRole();
 		else if (c.equals("joingroup")) requestJoinGroup();
                else if (c.equals("showInMessages")){
		    boolean b = showMessagesItem.isSelected();
                    ((JSynEditAgent)ag).showInMessages(b);
                }
                else if (c.equals("showOutMessages")){
		    boolean b = showOutMessagesItem.isSelected();
                    ((JSynEditAgent)ag).showOutMessages(b);
                }
 		else super.command(c);
 	}

    void showCurrentGroup(String group){
    	currentGroupLabel.setText("Target group: "+group);
    }

    void showCurrentRole(String role){
    	currentRoleLabel.setText("Target role: "+role);
    }

    void evalBuffer() {
        String s = inputArea.getText();
     	((JSynEditAgent) ag).sendString(s);
   }

   void evalSelection() {
     	String s = inputArea.getSelectedText();
     	((JSynEditAgent) ag).sendString(s);
   }

   void setTarget(){
   		// System.out.println("creation de groupe..");
   		new EditorSetTargetDialog((JSynEditAgent) ag, GraphicUtils.getRealFrameParent(this));
   }


    void requestLeaveRole(){
   		// System.out.println("creation de groupe..");
   		new EditorLeaveRoleDialog((JSynEditAgent) ag, GraphicUtils.getRealFrameParent(this));
   }

   void requestJoinGroup(){
   		((JSynEditAgent) ag).requestJoinGroup();
              new EditorGroupDialog((JSynEditAgent)ag, GraphicUtils.getRealFrameParent(this));
   }

   void joinGroup(Vector groups){
       //new EditorJoinGroup((EditorAgent) ag,groups);
       //       ag.refresh();
      new EditorGroupDialog((JSynEditAgent)ag, GraphicUtils.getRealFrameParent(this));
   }

}
