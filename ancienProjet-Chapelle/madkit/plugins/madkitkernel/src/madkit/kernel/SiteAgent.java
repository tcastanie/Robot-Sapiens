/*
* SiteAgent.java - Kernel: the kernel of MadKit
* Copyright (C) 1998-2002 Olivier Gutknecht, Fabien Michel, Jacques Ferber
*
* This library is free software; you can redistribute it and/or
* modify it under the terms of the GNU Lesser General Public
* License as published by the Free Software Foundation; either
* version 2.1 of the License, or (at your option) any later version.

* This library is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
* Lesser General Public License for more details.

* You should have received a copy of the GNU Lesser General Public
* License along with this library; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
*/
package madkit.kernel;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

/** The SiteAgent represents the Kernel for communities and organizations synchronization management...
 @author Fabien Michel
  @version 1.0
  @since MadKit 3.0
 */

final class SiteAgent extends Agent
{
    public static final String PUBLIC=Kernel.DEFAULT_COMMUNITY;
    public static final String COMMUNITIES="communities";
    public static final String SITE="site";

	private AgentAddress myCommunicator=null;
	private Map organizations;
	private Organization communities;
	private KernelAgent kernelAgent;
	private Collection distantKernels;
	private siteAgentGUI gui;

SiteAgent(Map organizations, KernelAgent agt)
{
	this.organizations = organizations;
	communities = (Organization) organizations.get(COMMUNITIES);
	kernelAgent = agt;
	distantKernels = new HashSet();
}

////////////////////////////////////////////////////	LIFE CYCLE
public void initGUI()
{
	gui = new siteAgentGUI(this);
	setGUIObject(gui);
}

final public void activate()
{

	createCommunity(PUBLIC);
	/*createGroup(true,"communities","public",null,null);
	requestRole("communities","public","site",null);*/
	createGroup(false,"communications",null,null);
	//createGroup(false,"local",null,null);
	createGroup(false,"system",null,null);
        requestRole("system",SITE,null);
	requestRole("communications",SITE,null);
	kernelAgent.localOrg = (Organization) organizations.get(PUBLIC);
	getCurrentKernel().launchAgent(kernelAgent,"KernelAgent",getCurrentKernel(),false);
        this.setName("SITE:"+this.getAddress().getKernel().getHost());
}

final public void live()
{   try{
	while(true)
	{
		Message m = waitNextMessage();
		if(m instanceof SynchroMessage)
			handleSynchroMessage((SynchroMessage) m);
		else if(m instanceof ConnectionMessage)
			handleConnectionMessage((ConnectionMessage) m);
		else if(m instanceof NetworkRequest)
			handleNetworkRequest((NetworkRequest) m);
		else if(m instanceof StringMessage){
                    handleMessage((StringMessage) m);
		} else
			debug("receive an unknown message type :"+m);
	}
      } catch(Exception ex){
          System.err.println("Error in SiteAgent: "+ex);
      }
}

protected void handleMessage(StringMessage msg){
       // System.out.println("::: handling message :"+msg);
        String content = msg.getString();
        StringTokenizer st = new StringTokenizer(content," ");
        String token = st.nextToken();
        if (token.equalsIgnoreCase("$url")){
            token = st.nextToken();
            AgentAddress ag = getAgentWithRole("system","browser");
            if (ag == null){
                AbstractAgent a = AbstractMadkitBooter.getBooter().makeJavaAgent(this,"agents.system.WebBrowserAgent",true);
                ag = a.getAddress();
                if (ag == null){
                  System.err.println("Error: cannot create WebBrowserAgent upon reception of a $url request");
                }
            }
            sendMessage(ag,new StringMessage("$goto "+token));
        } else if (token.equalsIgnoreCase("$message")){
          try {
            String s = content.substring("$message".length()+1,content.length());
            AgentAddress ag = getAgentWithRole("system","pager");
            if (ag == null){
                AbstractAgent a = AbstractMadkitBooter.getBooter().makeJavaAgent(this,"madkit.system.Pager",true);
                ag = a.getAddress();
                if (ag == null){
                  System.err.println("Error: cannot create a Pager upon reception of a $message request");
                }
            }
            sendMessage(ag,new StringMessage("$display "+"Received a remote message from "
                        +msg.getSender()+": \n"+s));
           } catch(Exception e){}
        }
}

public void end()
{
	System.err.println("SITE AGENT KILLED !!!!!!!!!!!!!");
	System.err.println("UNSTABLE SYSTEM ....");
}

///////////////////////////////////////////////////////// Handling messages

final void handleSynchroMessage(SynchroMessage m)
{
	if(connectedWith(m.community))
	{
		if(! organizations.containsKey(m.community))
		{
			kernelAgent.callHooks(Kernel.NEW_COMMUNITY,m.community);
			organizations.put(m.community,new Organization());
		}
		Organization localOrg = (Organization) organizations.get(m.community);
		switch(m.code)
		{
			case Kernel.CREATE_GROUP:
				localOrg.createGroup(m.initiator, true, m.groupName, m.newGroup.getDescription(), m.newGroup.getGroupIdentifier());
				kernelAgent.callHooks(Kernel.CREATE_GROUP,  new AGRTrio(m.initiator,m.community,m.groupName,null));
				break;
			case Kernel.ADD_MEMBER_ROLE:
				localOrg.requestRole(m.initiator, m.groupName, m.roleName, m.memberCard);
				kernelAgent.callHooks(Kernel.ADD_MEMBER_ROLE,  new AGRTrio(m.initiator,m.community,m.groupName,m.roleName));
				break;
			case Kernel.LEAVE_GROUP:
				if(localOrg.leaveGroup(m.initiator, m.groupName))
					removeCommunity(m.community);
				kernelAgent.callHooks(Kernel.LEAVE_GROUP,  new AGRTrio(m.initiator,m.community,m.groupName,null));
				break;
			case Kernel.NEW_COMMUNITY:
				establishConnectionWith(m.getSender(), true);
				break;
			case Kernel.DELETE_COMMUNITY:
				localOrg.removeAgentsFromKernel(m.getSender().getKernel());
				if(localOrg.isEmpty())
				{
					removeCommunity(m.community);
					kernelAgent.callHooks(Kernel.DELETE_COMMUNITY,m.community);
				}
				break;
			case Kernel.REMOVE_MEMBER_ROLE:
				if(localOrg.leaveRole(m.initiator, m.groupName, m.roleName))
				{
					removeCommunity(m.community);
				}
				kernelAgent.callHooks(Kernel.REMOVE_MEMBER_ROLE,  new AGRTrio(m.initiator,m.community,m.groupName,m.roleName));
				break;
			case Kernel.MIGRATION:
				if(getDebug())
					System.err.println("receiving a migration "+m.ref.toString());
				getCurrentKernel().receiveAgent(m.ref);
				System.gc();
				System.runFinalization();
				break;
		}
	}
	else
		switch(m.code)
		{
			case Kernel.NEW_COMMUNITY:
				kernelAgent.callHooks(Kernel.NEW_COMMUNITY,m.community);
				System.err.println("new community detected");
				if (gui != null)
                                  gui.refreshCommunities();//establishConnectionWith(m.getSender(), true);
				break;
			case Kernel.DELETE_COMMUNITY:
				kernelAgent.callHooks(Kernel.DELETE_COMMUNITY,m.community);
				System.err.println("a community has been deleted ");//+organizations);
				if (gui != null)
                                  gui.refreshCommunities();//establishConnectionWith(m.getSender(), true);
				break;
		}
}

final void handleConnectionMessage(ConnectionMessage m)
{
	if(m.isTheFirstMessage())
	{
		getCurrentKernel().synchronizeKernel( ((ConnectionMessage) m).getOrgs(), true);
		establishConnectionWith(m.getSender(),false);
	}
	else
		getCurrentKernel().synchronizeKernel( ((ConnectionMessage) m).getOrgs(), false);
	if(distantKernels.add(m.getSender())){
                //System.out.println("Connecting to "+m.getSender());
		kernelAgent.callHooks(Kernel.CONNECTED_TO, m.getSender().getKernel());
		kernelAgent.callHooks(Kernel.ADD_MEMBER_ROLE, m.getSender(), COMMUNITIES, PUBLIC, SITE);
        }
	System.gc();
	System.runFinalization();
}

synchronized final void handleNetworkRequest(NetworkRequest m)
{
	switch(m.getRequestCode())
	{
		case NetworkRequest.INJECT_MESSAGE:
			try
			{
				Message m2 = (Message) m.getArgument();
				getCurrentKernel().sendLocalMessage(m2);
				if(! (m2 instanceof PrivateMessage))
					kernelAgent.callHooks(Kernel.SEND_MESSAGE, m2.clone());
			}
			catch (MessageException me) {if (getDebug()) System.err.println(me);}
			break;
		case NetworkRequest.CONNECTION_REQUEST:
			if(m.getSender().equals(myCommunicator))
			{
		  		establishConnectionWith((AgentAddress) m.getArgument(),true);
		  		//distantKernels.add(m.getArgument());
		  	}
		  	break;
		case NetworkRequest.DECONNECTED_FROM:
	  		distantKernels.remove(m.getArgument());
		  	deconnection((String) m.getArgument());
		  	break;
		case NetworkRequest.BE_COMMUNICATOR:
			if(myCommunicator == null)
			{
				redisplayMyGUI();
				myCommunicator = m.getSender();
			}
			break;
		case NetworkRequest.STOP_COMMUNICATOR:
			if(myCommunicator!= null && myCommunicator.equals(m.getSender()))
			{
				disposeMyGUI();
				myCommunicator = null;
				for(Iterator i = organizations.entrySet().iterator();i.hasNext();)
				{
					Map.Entry e = (Map.Entry) i.next();
					Organization org = (Organization) e.getValue();
					if(org.removeDistantAgents())
					{
						//communities.leaveGroup(getAddress(), (String) e.getKey());
						leaveGroup("communities", (String) e.getKey());
						i.remove();
					}
				}
				distantKernels.clear();
				System.gc();
				System.runFinalization();
			}
			break;
		case NetworkRequest.GET_AVAILABLE_DESTINATIONS:
			KernelAddress[] destinations = new KernelAddress[distantKernels.size()];
			int j=0;
			for (Iterator i = distantKernels.iterator();i.hasNext();j++)
				destinations[j] = ((AgentAddress) i.next()).getKernel();
			sendMessage(m.getSender(), new NetworkRequest(NetworkRequest.GET_AVAILABLE_DESTINATIONS, destinations));
			break;
		case NetworkRequest.REQUEST_MIGRATION:
			if(getDebug())
				println("receiving a migration request of "+m.getSender());
			// kernelAgent.callsHook(Kernel.MIGRATION...	à rajouter
			tryMigration((KernelAddress) m.getArgument(),m.getSender());
			break;
	}
}


////////////////////////////////////////////////////////////////////////////////////////

///////////////////////////////////////////////////	CONNECTION

//////////////////////////////////////////////////////////////////////////////////////////
final synchronized void establishConnectionWith(AgentAddress distantKernel, boolean first)
{
	Map orgs = new HashMap();
	for(Iterator i = organizations.entrySet().iterator();i.hasNext();)
	{
		Map.Entry e = (Map.Entry) i.next();
		if(connectedWith((String)e.getKey()))
		{
			Organization org = ((Organization) e.getValue()).exportOrg();
			orgs.put(e.getKey(),org);
		}
	}
	sendMessage(distantKernel, new ConnectionMessage(orgs,first));
	System.err.println("sending connection message");
}

final synchronized void deconnection(String id)
{
	for(Iterator i = distantKernels.iterator();i.hasNext();)
	{
		AgentAddress distantK = (AgentAddress) i.next();
		if(distantK.getKernel().getID().equals(id))
		{
			debug("disconnected from "+id);
			for(Iterator j = organizations.entrySet().iterator();j.hasNext();)
			{
				Map.Entry e = (Map.Entry) j.next();
				Organization org = (Organization) e.getValue();
				if(org.removeAgentsFromKernel(distantK.getKernel()))
				{
					leaveGroup("communities", (String) e.getKey());
					sendAll(new SynchroMessage(Kernel.LEAVE_GROUP,getAddress(), COMMUNITIES, (String) e.getKey(),null, null));
					sendAll(new SynchroMessage(Kernel.DELETE_COMMUNITY, (String) e.getKey()));
					j.remove();
				}
			}
			kernelAgent.callHooks(Kernel.DISCONNECTED_FROM, distantK.getKernel());
			kernelAgent.callHooks(Kernel.REMOVE_MEMBER_ROLE, distantK, COMMUNITIES, PUBLIC, SITE);
			i.remove();
			System.gc();
			System.runFinalization();
			break;
		}
	}
	if (gui != null)
          gui.refreshCommunities();
}

////////////////////////////////////////////////////////////////////////////////////////

///////////////////////////////////////////////////	SYNCHRONIZATION

//////////////////////////////////////////////////////////////////////////////////////////
final synchronized void updateDistantOrgs(AgentAddress initiator, String community, String groupName)
{
	AgentAddress[] receivers = communities.getRolePlayers(community,SITE);
	Organization orgOfTheGroup = (Organization) organizations.get(community);
	for (int i = 0;i<receivers.length;i++)
   	{
   		if(! receivers[i].equals(getAddress()))
   		{
	   		Message m = new SynchroMessage(initiator, community, orgOfTheGroup.getGroup(groupName),groupName);
	   		m.setSender(getAddress());
	   		m.setReceiver(receivers[i]);
	   		sendDistantMessage(m);
	   	}
  	}
}

final synchronized void updateDistantOrgs(int code,AgentAddress initiator, String community, String groupName,String roleName, Object memberCard)
{
	AgentAddress[] receivers = communities.getRolePlayers(community,SITE);
	for (int i = 0;i<receivers.length;i++)
   	{
   		if(! receivers[i].equals(getAddress()))
   		{
	   		Message m = new SynchroMessage(code,initiator, community, groupName,roleName, memberCard);
	   		m.setSender(getAddress());
	   		m.setReceiver(receivers[i]);
	   		sendDistantMessage(m);
	   	}
   	}
}

final synchronized void tryMigration(KernelAddress destination, AgentAddress traveler)
{
	AbstractAgent ref = Kernel.getReference(traveler);
	Message message = new SynchroMessage(ref);
	for(Iterator i = distantKernels.iterator();i.hasNext();)
	{
		AgentAddress potentialReceiver = (AgentAddress) i.next();
		if(potentialReceiver.getKernel().equals(destination))
		{
			message.setReceiver(potentialReceiver);
			break;
		}
	}
	if(message.getReceiver() != null && ref != null)
	{
		//kernelAgent.callshook 	...  updateDistantOrgs(Kernel.MIGRATION,traveler,null,null,null);
		if (ref instanceof Agent)
		{
			((Agent)ref).getAgentThread().stop();
			ref.setCurrentKernel(null);
			//ref.messageBox=null;
		}
		getCurrentKernel().removeAgentFromOrganizations(traveler);
		getCurrentKernel().removeReferenceOf(traveler);
		message.setSender(getAddress());
		sendDistantMessage(message);
	}
}


////////////////////////////////////////////////////////////////////////////////////////

///////////////////////////////////////////////////	DISTANT MESSAGING MANAGEMENT

//////////////////////////////////////////////////////////////////////////////////////////

final synchronized boolean sendDistantMessage(Message m)
{
	if(myCommunicator != null)
	{
		Message m2 = new KernelMessage(KernelMessage.NO_REQUEST,Kernel.SEND_MESSAGE,m);
		m2.setSender(getAddress());
   		m2.setReceiver(myCommunicator);
   		try
   		{
			getCurrentKernel().sendLocalMessage(m2);
		}
		catch(MessageException mexc)
		{
			if (getDebug())
				System.err.println("Unable to send distant message !!"+mexc);
			return false;
		}
		return true;
	}
	return false;
}

private void sendAll(Message m)
{
	m.setSender(getAddress());
	for(Iterator i = distantKernels.iterator();i.hasNext();)
	{
		Message m2 = (Message) m.clone();
		AgentAddress distantK = (AgentAddress) i.next();
		m2.setReceiver(distantK);
		sendDistantMessage(m2);
	}
}
////////////////////////////////////////////////////////////////////////////////////////

///////////////////////////////////////////////////	COMMUNITY MANAGEMENT

//////////////////////////////////////////////////////////////////////////////////////////
synchronized void joinCommunity(String communityName)
{
	if(! connectedWith(communityName))
	{
		if(createGroup(true,COMMUNITIES,communityName,null,null) > 0)
			sendAll(new SynchroMessage(getAddress(), COMMUNITIES, communities.getGroup(communityName),communityName));
		requestRole(COMMUNITIES,communityName,SITE,null);		//must be optimized
		sendAll(new SynchroMessage(Kernel.ADD_MEMBER_ROLE,getAddress(), COMMUNITIES, communityName,SITE, null));
		sendAll(new SynchroMessage(Kernel.NEW_COMMUNITY,communityName));
	}
}
synchronized void leaveCommunity(String communityName)
{
	Organization organization = (Organization) organizations.get(communityName);
	organization.removeDistantAgents();
	if(organization.isEmpty())
	{

		leaveGroup("communities",communityName);
		sendAll(new SynchroMessage(Kernel.LEAVE_GROUP, getAddress(), COMMUNITIES, communityName, null, null));
		organizations.remove(communityName);
		kernelAgent.callHooks(Kernel.DELETE_COMMUNITY,communityName);
	}
	else
	{
		leaveRole(COMMUNITIES,communityName,SITE);
		sendAll(new SynchroMessage(Kernel.REMOVE_MEMBER_ROLE,getAddress(), COMMUNITIES, communityName, "site", null));
		sendAll(new SynchroMessage(Kernel.DELETE_COMMUNITY, communityName));
	}
	if (gui != null) gui.refreshCommunities();
}

synchronized void removeCommunity(String communityName)
{
	System.err.println("removing community : "+communityName);
	organizations.remove(communityName);
	kernelAgent.callHooks(Kernel.DELETE_COMMUNITY,communityName);
	leaveGroup(COMMUNITIES,communityName);
	sendAll(new SynchroMessage(Kernel.LEAVE_GROUP,getAddress(), COMMUNITIES, communityName,null, null));
	sendAll(new SynchroMessage(Kernel.DELETE_COMMUNITY, communityName));
	if (gui != null)
          gui.refreshCommunities();
}

synchronized boolean connectedWith(String community)
{
	if(community.equals(COMMUNITIES) || communities.isPlayingRole(getAddress(),community,SITE))
		return true;
	return false;
}

synchronized Organization createCommunity(String communityName)
{
	if(! organizations.containsKey(communityName))
	{
		Organization org = new Organization();
		organizations.put(communityName,org);
		createGroup(true,COMMUNITIES,communityName,null,null);
		requestRole(COMMUNITIES,communityName,SITE,null);
		sendAll(new SynchroMessage(getAddress(), COMMUNITIES, communities.getGroup(communityName),communityName));
		sendAll(new SynchroMessage(Kernel.ADD_MEMBER_ROLE,getAddress(), COMMUNITIES, communityName,SITE, null));
		sendAll(new SynchroMessage(Kernel.NEW_COMMUNITY,communityName));
		kernelAgent.callHooks(Kernel.NEW_COMMUNITY,communityName);
		if(hasGUI())
			gui.refreshCommunities();
		return org;
	}
	return null;
}

synchronized void refreshCommunities()
{
	if (gui != null)
          gui.refreshCommunities();
}

synchronized String[] getCommunities()
{
	Collection c = new HashSet();
	String[] s = communities.getGroups();
	for(int i = 0;i<s.length;i++)
		if(communities.isPlayingRole(getAddress(),s[i],"member") || communities.getRolePlayer(s[i],SITE) != null)
			c.add(s[i]);
	return (String[]) c.toArray(new String[0]);
}

// do not kill the agent on window closing
    public void windowClosing(AWTEvent we){

    }


}

////////////////////////////////////////////////////////////////////////////////////////

///////////////////////////////////////////////////	PRIVATE MESSAGE CLASSES

//////////////////////////////////////////////////////////////////////////////////////////

final class ConnectionMessage extends Message  implements PrivateMessage
{
	Object org;
	boolean first;

	ConnectionMessage(Object org, boolean first)
	{
		this.org = org;
		this.first=first;
	}
	final Map getOrgs(){return (Map) org;}
	final boolean isTheFirstMessage(){return first;}

}


final class SynchroMessage extends Message  implements PrivateMessage
{
	int code;
	AgentAddress initiator=null;
	String groupName=null,roleName=null, community=null;
	Group newGroup=null;
	Object memberCard=null;
	AbstractAgent ref=null;

	SynchroMessage(AgentAddress initiator, String community, Group theNewGroup,String groupName)
	{
		code = Kernel.CREATE_GROUP;
		newGroup = theNewGroup;
		this.community = community;
		this.groupName = groupName;
		this.initiator = initiator;
	}

	SynchroMessage(AbstractAgent initiator)
	{
		this.ref = initiator;
		this.community = SiteAgent.PUBLIC;
		code = Kernel.MIGRATION;
	}

	SynchroMessage(int code, AgentAddress initiator, String community, String groupName,String roleName, Object memberCard)
	{
		this.code = code;
		this.groupName = groupName;
		this.community = community;
		this.roleName = roleName;
		this.initiator = initiator;
		this.memberCard = memberCard;
	}

	SynchroMessage(int code, String community)
	{
		this.community = community;
		this.code = code;
	}

	int getCode(){return code;}
}




////////////////////////////////////////////////////////////////////////////////////////

///////////////////////////////////////////////////	GUI

//////////////////////////////////////////////////////////////////////////////////////////

final class siteAgentGUI extends JPanel implements ItemListener
{
	SiteAgent myAgent;

	siteAgentGUI(SiteAgent agt){
		setSize(200,300);
		myAgent = agt;
		//setTitle(myAgent.getName());
		refreshCommunities();
	}



	void refreshCommunities(){
		removeAll();
		setLayout(new BorderLayout());
		String[] communityNames = myAgent.getCommunities();
		Object[][] data=new Object[communityNames.length][2];
		for(int i = 0; i < communityNames.length; i++)
		{
			data[i][0]=communityNames[i];
			data[i][1]=new Boolean(myAgent.connectedWith(communityNames[i]));
			//JCheckBox c = new JCheckBox(communityNames[i],myAgent.connectedWith(communityNames[i]));
			//c.addItemListener(this);
			//tmp.add(c);
		}
		//JPanel tmp = new JPanel();
		//tmp.setLayout(new GridLayout(communityNames.length,1,5,5));
		//tmp.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

		SitesTableModel sitesModel = new SitesTableModel(this,data);
        JTable table = new JTable(sitesModel);
        table.setPreferredScrollableViewportSize(new Dimension(200, 100));

        //Create the scroll pane and add the table to it.
        JScrollPane scrollPane = new JScrollPane(table);

        //Add the scroll pane to this window.
        add(scrollPane, BorderLayout.CENTER);

		add("North",new JLabel("Available communities"));
		//add("Center",tmp);
		validate();
	}

    void communityChange(boolean b, String name){
        if (b)
            myAgent.joinCommunity(name);
        else
            myAgent.leaveCommunity(name);
    }

	public void itemStateChanged(ItemEvent e){
		Object source = e.getItemSelectable();
		if (e.getStateChange() == ItemEvent.DESELECTED)
		{
			myAgent.leaveCommunity( ((JCheckBox)source).getText());
			}
			else
		{
			myAgent.joinCommunity( ((JCheckBox)source).getText());
			}

	}

}

class SitesTableModel extends AbstractTableModel {
        final String[] columnNames = {"Sites",
                                      "connected"};
        Object[][] data;
        siteAgentGUI gui;

		SitesTableModel(siteAgentGUI g,Object[][] obj){
			data = obj;
            gui = g;
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

        public Object getValueAt(int row, int col) {
            return data[row][col];
        }

        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        public boolean isCellEditable(int row, int col) {
            if ((data[row][0].equals(SiteAgent.PUBLIC)) || (col < 1)) {
                return false;
            } else {
                return true;
            }
        }

        public void setValueAt(Object value, int row, int col) {
            data[row][col] = value;
            gui.communityChange(((Boolean)value).booleanValue(),(String)data[row][0]);
            fireTableCellUpdated(row, col);
        }
    }
