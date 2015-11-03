/*
* Broker.java - Travel, a simple demo application
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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JPanel;

import madkit.kernel.Agent;
import madkit.kernel.AgentAddress;
import madkit.kernel.Message;
import madkit.kernel.OPanel;
import madkit.messages.ACLMessage;

/** Contains the bid of a provider. This class is used by the Broker to choose
	the best offer among the bids of the providers */
class BidAnswer {
	AgentAddress bidder;
	int value;
	BidAnswer(AgentAddress b, int v){bidder = b; value = v;}
	int getValue(){return value;}
	AgentAddress getBidder(){return bidder;}
}

public class Broker extends Agent implements ActionListener
{
  Vector bidders = new Vector();
  BidAnswer[] answers;
  int contract = 0;
  JButton b;
  int cpt=0;
  AgentAddress client;

  boolean pause = false;

  public void enterPause()
    {
      pause = true;
      b.setBackground(Color.red);
      b.repaint();

      while (pause)
	pause(200);
    }

  public void initGUI()
    {
      JPanel p = new JPanel(new BorderLayout());
      OPanel o = new OPanel();
      p.add("Center",o);
       b = new JButton("Next step");
      b.addActionListener(this);
      p.add("South",b);
      setGUIObject(p);
      setOutputWriter(o.getOut());//new GUIPrintWriter(o.ge.outField));
    }

  public void actionPerformed(ActionEvent e)
    {
      pause=false;
      b.setBackground(Color.gray);
    }


  public void activate()
    {
      println("Broker ready");

      createGroup(true,"travel","travel-providers",null,null);
      createGroup(true,"travel","travel-clients",null,null);
      requestRole("travel","travel-providers", "broker",null);
      requestRole("travel","travel-clients", "broker",null);
    }


  public void handleMessage(ACLMessage m)
  { String product=null;

    if (m.getAct().equalsIgnoreCase("REQUEST")){

    	println("Receiving client request");
    	enterPause();

        product = m.getContent().toString();
        client = m.getSender();

        AgentAddress[] bidders=getAgentsWithRole("travel","travel-providers", product+"-provider");
        if (bidders.length==0){
                println("Not found providers of " + product);
                return;
        }
    	println ("Found providers of "+product);
    	cpt = bidders.length;
    	answers = new BidAnswer[cpt];

        println ("Transmitting requests...");

        ACLMessage req=new ACLMessage("REQUEST-FOR-BID",product);
    	broadcastMessage("travel","travel-providers", product+"-provider",req);

        println ("Waiting for offers..");
    }
	// reception des propositions
    else if (m.getAct().equalsIgnoreCase("BID")){
		receiveBid(m);
    }
}



  public void live()
  {
    while (true)
	{
	  Message m = waitNextMessage();

	  if (m instanceof ACLMessage)
	    handleMessage((ACLMessage)m);
	  else
	  	System.err.println("ERROR: invalid message type: "+ m);
	}
  }

  protected void receiveBid(ACLMessage m){
  	println("Received an offer of " + m.getContent() + " from " + m.getSender());
  	cpt--;
  	answers[cpt]=new BidAnswer(m.getSender(),((Integer) new Integer(m.getContent().toString())).intValue());
  	if (cpt <= 0)
  		bestContract();
  }


  void bestContract()
    {
      AgentAddress best = null;
      int bestoffer = 9999;
      println("Selecting best offer from " + answers.length + " proposals");
      enterPause();
      for (int i=0;i<answers.length;i++){
		  println(":: best: " + bestoffer+", value: " + answers[i].getValue());
		  if (answers[i].getValue() < bestoffer) {
		      best=answers[i].getBidder();
		      bestoffer=answers[i].getValue();
		  }
		}

      if (best != null)
	{
	  println("Chosen provider:"+best);
	  println("  avec "+bestoffer+" F");
	  contract++;
	  println("Sending provider confirmation");

	  enterPause();
	  sendMessage(best,
		      new ACLMessage("MAKE-CONTRACT","contract-"+contract));
	  pause(100);

	  println("Sending client confirmation");
	  enterPause();

	  sendMessage(client,
		      new ACLMessage("MAKE-CONTRACT","contract-"+contract));
	}
    }
}







