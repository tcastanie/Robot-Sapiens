/*
* OrganizationTracer.java - List all organizational events
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
import java.awt.Dimension;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import madkit.kernel.AGRTrio;
import madkit.kernel.Agent;
import madkit.kernel.AgentAddress;
import madkit.kernel.Kernel;
import madkit.kernel.KernelMessage;
import madkit.kernel.Message;

/** Our simple GUI for the OrganizationTracer */
class OrganizationTracerGUI extends JPanel
{ 
    JTable table;
    DefaultTableModel model;
    Vector messages = new Vector();
    
  public OrganizationTracerGUI()
  {
      //      setSize(200,100);
      setLayout(new BorderLayout());
      String[] columnNames= {"Agent", "Action", "Group", "Role", "Date" };
      model =  new DefaultTableModel(columnNames,0);
      table = new JTable(model);
      JScrollPane scrollPane = new JScrollPane(table);
      table.setPreferredScrollableViewportSize(new Dimension(500, 70));
      add("Center",scrollPane);
  }
    
    public void addMessage(String s, String a, String g, String r, String d)
    {
	String[] m = new String[5];
	m[0]=s;
	m[1]=a;
	m[2]=g;
	m[3]=r;
	m[4]=d;
	model.addRow(m);
    }
    
  
}
  


/** This is the second major version of the OrganizationTracer Agent.
    It handles the new generic kernel naming scheme. However, it is
    still specialized in BSD socket communications. The final
    architecture, with a generic Communicator agent being the
    representative for a group a "expert" communication agents (CORBA,
    BSD, ...) should appear in MadKit v. 1.2. Stay tuned.

  @author Olivier Gutknecht
  @version 1.1d */

public class OrganizationTracer extends Agent
{
  AgentAddress mykernel;  
    boolean debug = true;
    OrganizationTracerGUI gui;
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
      setGUIObject(gui=new OrganizationTracerGUI());
  }

  public void activate()
  {
      println("Activated");
      
      joinGroup("system");
      requestRole("system", "tracer");
      mykernel = getAgentWithRole("system","kernel");
      sendMessage(mykernel,
		  new KernelMessage(KernelMessage.REQUEST_MONITOR_HOOK, Kernel.CREATE_GROUP));
      sendMessage(mykernel,
		  new KernelMessage(KernelMessage.REQUEST_MONITOR_HOOK, Kernel.LEAVE_GROUP));
      sendMessage(mykernel,
		  new KernelMessage(KernelMessage.REQUEST_MONITOR_HOOK, Kernel.ADD_MEMBER_ROLE));
      sendMessage(mykernel,
		  new KernelMessage(KernelMessage.REQUEST_MONITOR_HOOK, Kernel.REMOVE_MEMBER_ROLE));
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

    protected String showTime()
    {
	Date d = new Date();
	if (parseTime)
	    {
		SimpleDateFormat formatter = new SimpleDateFormat ("HH:mm:ss SSSS");
		return formatter.format(d);
	    }
	else
	    return Long.toString(d.getTime());
    }
    
  
  protected void handleMessage(KernelMessage m)
  {
    if (m.getOperation() == Kernel.CREATE_GROUP)
	{
	    AGRTrio agr = (AGRTrio) m.getArgument();
	    if (hasGUI())
		gui.addMessage(agr.getAgent().getName(),
				   "found_group",
				   agr.getGroup(),
				   "",
				   showTime());
	    else
		println("FOUND_GROUP <"+agr.getGroup()+"> by "+agr.getAgent().getName());	    
	}
    if (m.getOperation() == Kernel.LEAVE_GROUP)
	{
	    AGRTrio agr = (AGRTrio) m.getArgument();
	    if (hasGUI())
		gui.addMessage(agr.getAgent().getName(),
				   "leave_group",
				   agr.getGroup(),
				   "",
			       showTime());
	    else
		println("LEAVE_GROUP <"+agr.getGroup()+"> by "+agr.getAgent().getName());	    
	}
    /*if (m.getOperation() == Kernel.JOIN_GROUP)
	{
	    AGRTrio agr = (AGRTrio) m.getArgument();
	    if (hasGUI())
		gui.addMessage(agr.getAgent().getName(),
			       "join_group",
			       agr.getGroup(),
			       "",
			       showTime()
			       );
	    else
		println("JOIN_GROUP <"+agr.getGroup()+"> by "+agr.getAgent().getName());	    
	}*/
    if (m.getOperation() == Kernel.ADD_MEMBER_ROLE)
	{
	    AGRTrio agr = (AGRTrio) m.getArgument();
	    if (hasGUI())
		gui.addMessage(agr.getAgent().getName(),
				   "add_member_role",
				   agr.getGroup(),
				   agr.getRole(),
			       showTime());
	    else
		println("ADD_MEMBER_ROLE <"+agr.getGroup()+"> by "+agr.getAgent().getName());	    
	}
    if (m.getOperation() == Kernel.REMOVE_MEMBER_ROLE)
	{
	    AGRTrio agr = (AGRTrio) m.getArgument();
	    if (hasGUI())
		gui.addMessage(agr.getAgent().getName(),
				   "remove_member_role",
				   agr.getGroup(),
				   agr.getRole(),
				   showTime());
	    else
		println("REMOVE_MEMBER_ROLE <"+agr.getGroup()+"> by "+agr.getAgent().getName());	    
	}
  }
}












