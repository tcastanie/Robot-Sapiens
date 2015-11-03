/*
* DynamicCommBean.java - Communicator: the connection module of MadKit
* Copyright (C) 1998-2002 Olivier Gutknecht, Pierre Bommel, Fabien Michel, Thomas Cahuzac
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
package madkit.communicator;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;


/**
  @author Olivier Gutknecht
  @review by Pierre Bommel
  @version d1
  */

public class DynamicCommBean extends JPanel implements TextListener
{
  JLabel message;
  JLabel titre = new JLabel("Socket Communicator (inactive)");
  JTextField nameField, portField, hostField;
  JComponent led = new JLabel("  ");

  DefaultListModel kv1 = new DefaultListModel();
  JList kernelList = new JList(kv1);

  public DynamicCommBean(final DynamicTwoChannelsCommunicator c)
  {
     setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
    JPanel high = new JPanel();
    high.setLayout(new BorderLayout());

    JPanel p = new JPanel(new FlowLayout());
    //  led.setSize(10,10);
    setActive(false);
    titre.setOpaque(true);
    led.setOpaque(true);
    p.add(titre);
    p.add(led);

    high.add("North",p);
    message = new JLabel("");
    high.add("South",message);

    JPanel hostinfo = new JPanel();
    hostinfo.setLayout(new FlowLayout());

    hostField = new JTextField (20);
    hostField.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
      	c.addHost(hostField.getText(),
			     Integer.parseInt(portField.getText()));
      }});

    portField = new JTextField (5);
    portField.setText(Integer.toString(DynamicTwoChannelsCommunicator.DEFAULT_PORT));

    hostinfo.add(hostField);
    hostinfo.add(portField);

    JButton b = new JButton("Add");
    b.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
	c.addHost(hostField.getText().trim(),
			     Integer.parseInt(portField.getText()));
      }});

    hostinfo.add(b);

/*
    JButton deconnect = new JButton("Down");
    deconnect.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
	c.selfDisconnect();
      }});

    hostinfo.add(deconnect);
*/
    high.add("Center", hostinfo);


     JPanel low = new JPanel(new GridLayout(1,1));
     low.add(new JScrollPane(kernelList));

     add(high);
     add(low);
 }


public void resetGUIObject()
  {
    nameField.setText("");
  }
  public String getName()
  {
    return nameField.getText();
  }
  public void setMessage(String s)
  {
    message.setText(s);
  }
  public  void textValueChanged(TextEvent e)
  {
  }

  public void setStatus(String s)
    {
      titre.setText("SocketCommunicator ("+s+")");
    }

  public void setActive(boolean b)
  {
    if (b)
      titre.setText("SocketCommunicator (active)  ");
    else
      titre.setText("SocketCommunicator (inactive)");
    if (b)
      led.setBackground(Color.green);
    else
      led.setBackground(Color.red);
    repaint();

}

  public synchronized void setText(String msg)
  {
    kv1.addElement(msg);
    revalidate();
    repaint();
  }

  public synchronized void reset()
  {
    kv1.removeAllElements();
    revalidate();
    repaint();
  }

  public synchronized void removeText(String s)
  {
    kv1.removeElement(s);
    revalidate();
    repaint();
  }
}






