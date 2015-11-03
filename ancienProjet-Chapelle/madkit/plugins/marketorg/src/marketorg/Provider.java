/*
* Provider.java - Travel, a simple demo application
* Copyright (C) 1998-2002 Olivier Gutknecht, Jacques Ferber
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
package marketorg;

import java.awt.Color;
import java.net.URL;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import madkit.kernel.Agent;
import madkit.kernel.Message;
import madkit.messages.ACLMessage;

public class Provider extends Agent
{
  int prix = 0;
  String competence;
  JLabel message;


  public Provider()
  {
    if (Math.random() > 0.5)
      competence = "train";
    else
      competence = "plane";

    if (prix == 0)
      prix = (int)(Math.random() * 500 )+100;
    if (competence.equals("plane"))
      prix=prix*2;
  }

  public void initGUI()
  {
    ImageIcon ii = null;
    String fileName = "";

    if (competence.equals("plane"))
      fileName = "/images/plane.jpg";
    else
      fileName = "/images/train.jpg";
    try
      {
	URL url;
	url = this.getClass().getResource(fileName);
	ii = new ImageIcon(url);
      } catch (Exception e) { e.printStackTrace(); }

    JPanel p= new JPanel();
    p.setLayout(new BoxLayout(p,BoxLayout.Y_AXIS));

    p.add( new JLabel("Price: "+prix));
    if (ii!=null)
      p.add( new JLabel(ii));

    p.add(message = new JLabel("                             "));
    message.setOpaque(true);
    p.setSize(170,100);
    message.setBackground(Color.lightGray);

    setGUIObject(p);
  }


  public void activate()
    {
      createGroup(true,"travel","travel-providers",null,null);
      requestRole("travel","travel-providers",competence+"-provider",null);
      display("Ticket: "+competence);
    }

  public void display(String s)
  {
    if (hasGUI())
      message.setText(s);
    else
      println(s);
  }


  protected void handleMessage(ACLMessage m)
  {
    Color bkg=null;
    if (hasGUI())
        bkg=message.getBackground();

    if (m.getAct().equalsIgnoreCase("REQUEST-FOR-BID"))
    {
		debug("s"+m.getSender());
		debug("r"+m.getReceiver());
      	display("Sending bid");
      	if (hasGUI()) message.setBackground(Color.yellow);

      	sendMessage(m.getSender(),
		  new ACLMessage("BID",String.valueOf(prix)));
      	display("Bid sent");
      	if (hasGUI()) {
      	    pause(4000);
            message.setBackground(Color.lightGray);
      	}
        display("Ticket: "+competence);
    }

    else if (m.getAct().equalsIgnoreCase("MAKE-CONTRACT"))
    {
      String group = m.getContent().toString();
      display("Received offer: "+group);
      if (hasGUI()) message.setBackground(Color.green);
      createGroup(true,"travel",group,null,null);
      requestRole("travel",group,"service",null);
    }
    else if (m.getAct().equalsIgnoreCase("VALIDATE")){
		display("Validating contract OK");
		if (hasGUI()) message.setBackground(Color.magenta);
		sendMessage(m.getSender(),
		    new ACLMessage("ACCEPT-CONTRACT"));
      	if (hasGUI()) {
            pause(6000);
      	    message.setBackground(Color.lightGray);
        }
      	display("Ticket: "+competence);
    }
  }


  public void live()
    {
      while (true) {
		Message m = waitNextMessage();
	  	if (m instanceof ACLMessage)
	    	handleMessage((ACLMessage)m);

		}
    }
}









