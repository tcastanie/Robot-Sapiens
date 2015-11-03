/*
* PropertyAgent.java -an agent which allow for the edition of properties of
* other agents
* Copyright (C) 2002 Jacques Ferber
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
package madkit.system.property;

import madkit.kernel.*;

import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

//import madkit.lib.messages.*;
//import madkit.lib.tools.*;
//import madkit.platform.desktop.property.*;
//import madkit.platform.desktop.PropertyComponent;

import madkit.messages.*;

class PropertyGUI extends JPanel {
   PropertyComponent properties;
   PropertyAgent myAgent;
   JPanel topPanel = new JPanel(new BorderLayout());

   JLabel nameLabel= new JLabel("Name  : ");
   JLabel idLabel =  new JLabel("Id    : ");
   JLabel classLabel=new JLabel("Class : ");
   JLabel nameValue= new JLabel("             ");
   JLabel idValue =  new JLabel("             ");
   JLabel classValue=new JLabel("             ");

  public PropertyGUI(PropertyAgent _ag) {
      setLayout(new BorderLayout());
      myAgent = _ag;
      JPanel labelPanel =new JPanel(new GridLayout(3,1));
      JPanel valuePanel = new JPanel(new GridLayout(3,1));
      topPanel.add(labelPanel,BorderLayout.WEST);
      topPanel.add(valuePanel,BorderLayout.CENTER);
      labelPanel.add(nameLabel);
      labelPanel.add(idLabel);
      labelPanel.add(classLabel);

      valuePanel.add(nameValue);
      valuePanel.add(idValue);
      valuePanel.add(classValue);

      nameLabel.setFont(PropertyComponent.boldFont);
      idLabel.setFont(PropertyComponent.boldFont);
      classLabel.setFont(PropertyComponent.boldFont);
      nameValue.setFont(PropertyComponent.defaultFont);
      idValue.setFont(PropertyComponent.defaultFont);
      classValue.setFont(PropertyComponent.defaultFont);

      add(topPanel,BorderLayout.NORTH);
      Border border1 = BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140)),BorderFactory.createEmptyBorder(10,10,10,10));
      topPanel.setBorder(border1);

      nameValue.setText(myAgent.getName());
      classValue.setText(myAgent.getClass().toString());
      idValue.setText(myAgent.getAddress().toString());

      Vector v = new Vector();
      v.addElement("id");
      properties = new PropertyComponent(myAgent,v);
      add(properties,BorderLayout.CENTER);
      setMinimumSize(new Dimension(200,200));
  }


  void editObject(AbstractAgent elt){
        Vector v = new Vector();

        nameValue.setText(elt.getName());
        classValue.setText(elt.getClass().toString());
        idValue.setText(myAgent.getAddress().toString());

        this.remove(properties);
        v.addElement("id");
        properties = new PropertyComponent(elt,v);
        add(properties,BorderLayout.CENTER);
        validate();
  }

}

public class PropertyAgent extends Agent {
    PropertyGUI gui;
    AbstractAgent currentAgent;

	public PropertyAgent(){
		super();
		currentAgent = null;
	}
	
	public PropertyAgent(AbstractAgent ag){
		super();
		currentAgent = ag;
	}
	
    public void initGUI(){
        gui = new PropertyGUI(this);
        this.setGUIObject(gui);
    }

    public void activate(){
        int b = this.createGroup(false,"public","system",null,null);
        requestRole("public","system", "propertyEditor",null);
        if (currentAgent != null)
        	gui.editObject(currentAgent);
    }

    public void live(){
        while (true){
            this.exitImmediatlyOnKill();

            Message m=waitNextMessage();
            if (m instanceof ActMessage){
                ActMessage msg = (ActMessage) m;
                if (msg.getAction().equals("edit")){
                    Object o = msg.getObject();
                    //System.out.println("$$ editing "+o);
                    if (o != this)
                        gui.editObject((AbstractAgent) o);
                }
            }
        }
    }
}