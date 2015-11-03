/*
* GroupMessageTracer.java - List all messages
* Copyright (C) 1998-2002 Olivier Gutknecht
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
package madkit.system;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.table.DefaultTableModel;

import madkit.kernel.Agent;
import madkit.kernel.AgentAddress;
import madkit.kernel.Kernel;
import madkit.kernel.KernelMessage;
import madkit.kernel.Message;


class GroupDialog extends JDialog implements ActionListener
{
    JComboBox groupChooser;
    GroupMessageTracer ag;

    public GroupDialog(GroupMessageTracer _ag){
        super();
        setTitle("observing a group");
        ag = _ag;
        getContentPane().setLayout(new BorderLayout(10,10));

        // les boutons du bas
        JPanel buttonPanel = new JPanel();
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");

        buttonPanel.add(okButton);
        okButton.addActionListener(this);
        buttonPanel.add(cancelButton);
        cancelButton.addActionListener(this);

        getContentPane().add(new JLabel("Select a group to observe, or enter a new one"),"North");
        getContentPane().add(buttonPanel,"South");

        // le panel du milieu
        JPanel middlePanel = new JPanel(new BorderLayout());
        getContentPane().add(middlePanel,"Center");

        JPanel labelPanel = new JPanel(new GridLayout(2,1));
        middlePanel.add(labelPanel,"West");

        JPanel fieldPanel = new JPanel(new GridLayout(2,1));
        middlePanel.add(fieldPanel,"Center");

        JLabel groupLabel = new JLabel("group : ");
        labelPanel.add(groupLabel);

        groupChooser = new JComboBox();
        groupChooser.setEditable(true);
        showAllGroups();
       /* groupChooser.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JComboBox cb = (JComboBox)e.getSource();
                String groupName = (String)cb.getSelectedItem();
                //	    ag.enterGroup(groupName);
                //showRoles(groupName);
            }}); */
        fieldPanel.add(groupChooser);

        pack();
        show();
    }

    void showAllGroups()
    {
	    Vector groups = ag.getAllGroups();

    	groupChooser.removeAllItems();
    	for(int i=0; i<groups.size(); i++){
	    groupChooser.addItem((String)groups.elementAt(i));
    	}
    }


    public void actionPerformed(ActionEvent e) {
	String s = e.getActionCommand();
	if (s.equals("OK")) {
	    ag.enterGroup((String)groupChooser.getSelectedItem());
	    //((PanelEditor)ag.getGUIObject()).refresh();
	    dispose();
	} else
	    dispose();
    }
}


/** Our simple GUI for the MessageTracer */
class GroupMessageTracerGUI extends JPanel {
    JTable table;
    DefaultTableModel model;
    Vector messages = new Vector();

    GroupMessageTracer myAgent;

    String[] columnNames= {"Sender", "Receiver", "Message Class", "Content", "Date" };


    void addTool(JToolBar toolBar, String name, String imageName) {
        JButton b;
        if ((imageName == null) || (imageName.equals(""))) {
            b = (JButton) toolBar.add(new JButton(name));
            b.setActionCommand(name);
        }
        else {
            ImageIcon i=null;
            java.net.URL u = this.getClass().getResource(imageName);
            if (u != null)
                i = new ImageIcon (u);

            if ((i != null) && (i.getImage()!=null))
                b = (JButton) toolBar.add(new JButton(i));
            else
                b = (JButton) toolBar.add(new JButton(name));
            b.setActionCommand(name);
        }

        b.setToolTipText(name);
        b.setMargin(new Insets(0,0,0,0));
        b.addActionListener(
                    new ActionListener(){
                        public void actionPerformed(ActionEvent e) {
                            command(e.getActionCommand());
                        }
                    });
    }

  public GroupMessageTracerGUI(GroupMessageTracer _ag)
  {   myAgent=_ag;
      //      setSize(200,100);
      setLayout(new BorderLayout());
      setTitle("GroupMessageTracer");

      JToolBar toolbar = new JToolBar();
      addTool(toolbar,"observe group","/images/agents/joingroup.gif"); // "/demo/agents/system/joingroup.gif"
      addTool(toolbar,"clear messages","/toolbarButtonGraphics/general/Delete24.gif"); // "/demo/agents/system/joingroup.gif"
      //addTool(toolbar,"refresh","/toolbarButtonGraphics/general/Refresh.gif"); // "/demo/agents/system/joingroup.gif"
      add("North",toolbar);

      model =  new DefaultTableModel(columnNames,0);
      table = new JTable(model);
      JScrollPane scrollPane = new JScrollPane(table);
      table.setPreferredScrollableViewportSize(new Dimension(500, 70));
      add("Center",scrollPane);
  }

    public void addMessage(String s, String r, String cl, String c, String d){
        String[] m = new String[5];
        m[0]=s;
        m[1]=r;
        m[2]=cl;
        m[3]=c;
        m[4]=d;
        model.addRow(m);
    }

    void command(String a){
      if (a.equals("observe group")) observeGroup();
      if (a.equals("clear messages")) {
        model=new DefaultTableModel(columnNames,0);
        table.setModel(model);
      }
    }

    void observeGroup(){
        new GroupDialog(myAgent);
    }

   void setTitle(String title){
		Container c = this;
		while (!((c instanceof JFrame) ||
				 (c instanceof Frame)
			  || (c instanceof JInternalFrame))){
			if (c == null)
				return;
			else
				c = c.getParent();
		}
        if (c instanceof Frame){
            ((Frame) c).setTitle(title);
        } else if (c instanceof JInternalFrame){
            ((JInternalFrame) c).setTitle(title);
        }
	}

}



/**
 *  This is the third major version of the MessageTracer Agent.
 *  As it names implies, this tracer traces only messages of a given group.

  @author Olivier Gutknecht and Jacques Ferber (for GroupModifications)
  @version 1.1d

   */


public class GroupMessageTracer extends Agent
{
    AgentAddress mykernel;
    boolean debug = true;
    GroupMessageTracerGUI gui;

    String targetGroup;
    Vector  agents;

    Hashtable orgdump = new Hashtable();

    boolean parseTime = true;

    /**
       * Get the value of parseTime.
       * @return Value of parseTime.
       */
    public boolean getParseTime() {return parseTime;}

    /**
       * Set the value of parseTime.
       * @param v  Value to assign to parseTime.
       */
    public void setParseTime(boolean  v) {this.parseTime = v;}


  public void initGUI()
  {
      setGUIObject(gui=new GroupMessageTracerGUI(this));
  }

  public void activate()
  {

      int b = this.createGroup(false,"public","system",null,null);

      requestRole("public","system", "tracer",null);
      mykernel = getAgentWithRole("system","kernel");
       sendMessage(mykernel,new KernelMessage(KernelMessage.INVOKE,Kernel.DUMP_ORGANIZATION));
      sendMessage(mykernel,
          new KernelMessage(KernelMessage.REQUEST_MONITOR_HOOK, Kernel.SEND_MESSAGE));
      sendMessage(mykernel,
          new KernelMessage(KernelMessage.REQUEST_MONITOR_HOOK, Kernel.SEND_BROADCAST_MESSAGE));
     /* sendMessage(mykernel,
          new KernelMessage(KernelMessage.REQUEST_MONITOR_HOOK, Kernel.CREATE_GROUP));
      sendMessage(mykernel,
          new KernelMessage(KernelMessage.REQUEST_MONITOR_HOOK, Kernel.LEAVE_GROUP));
      sendMessage(mykernel,
          new KernelMessage(KernelMessage.REQUEST_MONITOR_HOOK, Kernel.ADD_MEMBER_ROLE));
      sendMessage(mykernel,
          new KernelMessage(KernelMessage.REQUEST_MONITOR_HOOK, Kernel.REMOVE_MEMBER_ROLE));
      sendMessage(mykernel,
          new KernelMessage(KernelMessage.REQUEST_MONITOR_HOOK, Kernel.KILL_AGENT));
      sendMessage(mykernel,
          new KernelMessage(KernelMessage.REQUEST_MONITOR_HOOK, Kernel.MIGRATION));
      sendMessage(mykernel,
          new KernelMessage(KernelMessage.REQUEST_MONITOR_HOOK, Kernel.RESTORE_AGENT));
      sendMessage(mykernel,
          new KernelMessage(KernelMessage.REQUEST_MONITOR_HOOK, Kernel.CONNECTED_TO));
      sendMessage(mykernel,
          new KernelMessage(KernelMessage.REQUEST_MONITOR_HOOK, Kernel.DISCONNECTED_FROM));
      sendMessage(mykernel,
          new KernelMessage(KernelMessage.REQUEST_MONITOR_HOOK, Kernel.NEW_COMMUNITY));
      sendMessage(mykernel,
		  new KernelMessage(KernelMessage.REQUEST_MONITOR_HOOK, Kernel.DELETE_COMMUNITY)); */
  }

  public void enterGroup(String group){
         if ((targetGroup != null) && (!group.equals(targetGroup))){
             leaveGroup(targetGroup);
         }
           if (group != null){
             int i=requestRole(group,"$MessageTracer",null);
             targetGroup = group;
         }
   }

   public void leaveGroups(){
     if (targetGroup != null){
         leaveGroup(targetGroup);
         targetGroup = null;
     }
   }


   public Vector askGetGroups(){
       return(getGroups());
   }

     protected void requestDumpCommunities(){
         sendMessage(mykernel,new KernelMessage(KernelMessage.INVOKE,Kernel.DUMP_COMMUNITIES));
   }

     protected Vector getAllGroups()
     {
  	   Vector v = new Vector(orgdump.keySet());
  	   v.removeElement("system");
  	   return v;
     }


  public void live()
  {
    while(true)
      {
    Message e = waitNextMessage();
    if (e instanceof KernelMessage)
    
        handleMessage((KernelMessage)e);
      }
  }

  public Vector getAgents(String group){
    if (group!=null){
      Vector agents=new Vector();
      String[] targetRoles=getRoles(targetGroup);
      int imax=targetRoles.length;
      for(int i=0;i<imax;i++){
        AgentAddress[] agArray = this.getAgentsWithRole(targetGroup,targetRoles[i]);
        int jmax=agArray.length;
        for(int j=0;j<jmax;j++)
            agents.addElement(agArray[j]);
      }
      return(agents);
    } return null;
  }

  protected void handleMessage(KernelMessage m){
    if (m.getOperation() == Kernel.DUMP_ORGANIZATION){
  		Hashtable org = null;
  		// reply from DUMP_ORG
		if (m.getType() == KernelMessage.REPLY) {
    		org = (Hashtable) m.getArgument();
		orgdump = org;//joinGroupInteractively(org);
		}
    } else if (m.getOperation() == Kernel.SEND_MESSAGE){
	    Message orig = (Message) m.getArgument();
	    if (hasGUI()){
            if ((targetGroup!=null) && (agents != null)){ // filtering of messages external to the group
                if (!(agents.contains(orig.getSender()) && agents.contains(orig.getReceiver())))
                    return;
            }
		    String s =  orig.getClass().getName();
		    s = s.substring(s.lastIndexOf('.')+1);

		    String time;

		    if (parseTime)
			{
			    Date d = orig.getCreationDate();
			    SimpleDateFormat formatter = new SimpleDateFormat ("HH:mm:ss SSSS");
			    time = formatter.format(d);
			}
		    else
			    time = Long.toString(orig.getCreationDate().getTime());

		    gui.addMessage(orig.getSender().getName(), orig.getReceiver().getName(),
				   s,//orig.getClass().toString(),
				   orig.toString(),
				   time);
		} else
		    println("Trace"+orig);
	} else if (m.getOperation() == Kernel.SEND_BROADCAST_MESSAGE){
        Vector v = (Vector)m.getArgument();
	    String g = (String) v.elementAt(0);
	    String r = (String) v.elementAt(1);
	    Message orig = (Message) v.elementAt(2);
        String s =  orig.getClass().getName();
        s = s.substring(s.lastIndexOf('.')+1);

        if (hasGUI()){
            if ((targetGroup!=null) && (agents != null)){ // filtering of messages external to the group
                if (!(targetGroup.equals(g)) && (agents.contains(orig.getSender())))
                    return;
            }
		    String time;

		    if (parseTime)
			{
			    Date d = orig.getCreationDate();
			    SimpleDateFormat formatter = new SimpleDateFormat ("HH:mm:ss SSSS");
			    time = formatter.format(d);
			}
		    else
			    time = Long.toString(orig.getCreationDate().getTime());
	        gui.addMessage(orig.getSender().getName(),
                 "<"+g+","+r+">", s, orig.toString(), time);
        }
	}
    /*else if ((m.getOperation() == Kernel.CREATE_GROUP) ||
               (m.getOperation() == Kernel.REMOVE_MEMBER_ROLE) ||
               (m.getOperation() == Kernel.ADD_MEMBER_ROLE) ||
               (m.getOperation() == Kernel.LEAVE_GROUP))
      sendMessage(mykernel,new KernelMessage(KernelMessage.INVOKE,Kernel.DUMP_ORGANIZATION));
      agents=getAgents(targetGroup); */

  }

}











