/*
* AbstractWebBrowser.java - Graphics utilities for MadKit agents
* Copyright (C) 2000-2002  Jacques Ferber
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
package madkit.utils.graphics;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import madkit.kernel.Agent;

public class AbstractWebBrowser extends JRootPane
    implements HyperlinkListener,  ActionListener
{

    Agent agent;

    JEditorPane textPane;
    JLabel msgbox;
    JTextField urlField;
    JFileChooser fileChooser;

    java.util.List listURL = new ArrayList(20);
    int currentLocation = -1;

    String homeAddress = "file:"+System.getProperty("madkit.dir",null)+File.separator+"docs"+File.separator+"index.html";  // A default value

	public void setHomeAddress(String h){homeAddress = h;}
	public String getHomeAddress(){return homeAddress;}

    public AbstractWebBrowser() {
        super();
        init();
    }

    public AbstractWebBrowser(Agent ag){
        super();
        agent = ag;
        init();
    }

    public AbstractWebBrowser(Agent ag, String ha){
        super();
        agent = ag;
        setHomeAddress(ha);
        init();
    }

    protected void init(){
        textPane = new JEditorPane();
        textPane.setEditable(false);

        textPane.addHyperlinkListener(this);
        textPane.setPreferredSize(new Dimension(500,400));

        getContentPane().add(new JScrollPane(textPane),BorderLayout.CENTER);

        msgbox = new JLabel(" ");
        getContentPane().add(msgbox, BorderLayout.SOUTH);

        urlField = new JTextField();
        urlField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayURL(urlField.getText());
            }
            });
    }


    boolean goToLocation(URL url) {
		try {
		    String href = url.toString();
		    urlField.setText(href);
            printMsgBox("Loading page...");
		    textPane.setPage(url);
		    return true;
		}
		catch (IOException ex) {
		    printMsgBox("Cannot load the page: " + ex.getMessage());
		    return false;
		}
    }

    public void printMsgBox(String s){
        msgbox.setText(s);
    }

    public void displayHomeURL() {
        displayURL(getHomeAddress());
    }

    public void displayURL(URL url) {
		if (goToLocation(url)) {
	        currentLocation++;
	        if (currentLocation >= listURL.size()){
	            listURL.add(url);
	        } else {
	            listURL.set(currentLocation,url);
	        }
	     }
    }

    public void displayURL(String href) {
	try {
	    displayURL(new URL(href));
	}
	catch (MalformedURLException ex) {
	    printMsgBox("Malformed URL: " + href);
	}
    }

    public void openPage() {
	    fileChooser = new JFileChooser(System.getProperty("madkit.dir",null));

	    javax.swing.filechooser.FileFilter filter = new javax.swing.filechooser.FileFilter() {
			    public boolean accept(File f) {
				String fn = f.getName();
				if (f.isDirectory() || fn.endsWith(".html") || fn.endsWith(".htm"))
				    return true;
				else return false;
			    }
			    public String getDescription() { return "HTML Files"; }
			};
	    fileChooser.setFileFilter(filter);
	    fileChooser.addChoosableFileFilter(filter);

		int result = fileChooser.showOpenDialog(this);
		if (result == JFileChooser.APPROVE_OPTION) {

		    File selectedFile = fileChooser.getSelectedFile();
	        String url = "file:"+selectedFile.getPath();
		    displayURL(url);
		}
    }

    public void back() {
	    if (currentLocation > 0){
            --currentLocation;
	        goToLocation((URL)listURL.get(currentLocation));
        }
    }

    public void forward() {
	if (currentLocation < listURL.size()-1){
        ++currentLocation;
	    goToLocation((URL)listURL.get(currentLocation));
        }
    }

    public void reload() {
	if (currentLocation != -1)
	    goToLocation((URL)listURL.get(currentLocation));
    }



    public void close() {
	    this.setVisible(false);
		if (agent != null)
            agent.killAgent(agent);
    }

    public void quit(boolean confirm) {
        if (agent != null)
            agent.killAgent(agent);
    }


    public void hyperlinkUpdate(HyperlinkEvent e) {
		HyperlinkEvent.EventType type = e.getEventType();
		if (type == HyperlinkEvent.EventType.ACTIVATED) {
		    displayURL(e.getURL());
		}
		else if (type == HyperlinkEvent.EventType.ENTERED) {
		    printMsgBox(e.getURL().toString());
		}
		else if (type == HyperlinkEvent.EventType.EXITED) {
		    printMsgBox(" ");
		}
	}

    public void actionPerformed(ActionEvent e){
	    printMsgBox("");
        command(e.getActionCommand());
    }

    protected void command(String c){
        if (c.equals("Open"))
        	openPage();
        else if (c.equals("Backward"))
            back();
        else if (c.equals("Forward"))
            forward();
        else if (c.equals("Reload"))
            reload();
        else if (c.equals("Close"))
            close();
        else if (c.equals("Exit"))
            quit(true);
    }

    /// tool methods to create menubars and toolbars
    void addMenuItem(JMenu m, String command, String descr, int key) {
        addMenuItem(m, command, command, descr, true, key, this);
    }

	void addMenuItem(JMenu m, String name, String command, String descr, boolean disp, int key, ActionListener listener) {
        JMenuItem menuItem;
	    menuItem = new JMenuItem(name);
        m.add(menuItem);

        menuItem.setActionCommand(command);
        menuItem.setToolTipText(descr);
        menuItem.addActionListener(listener);
        if (!disp)
            menuItem.setEnabled(false);
		if (key > 0) {
			if (key != KeyEvent.VK_DELETE)
				menuItem.setAccelerator(KeyStroke.getKeyStroke(key, Event.CTRL_MASK, false));
			else
				menuItem.setAccelerator(KeyStroke.getKeyStroke(key, 0, false));
		}
	}


	void addButton(JComponent p, String name, String tooltiptext, String imageName) {
        addButton(p, name, tooltiptext, imageName, this );
    }

    void addButton(JComponent p, String name, String tooltiptext, String imageName, ActionListener listener) {
	  JButton b;
	  if ((imageName == null) || (imageName.equals(""))) {
              b = (JButton) p.add(new JButton(name));
	  }
	  else {
		   java.net.URL u = this.getClass().getResource(imageName);
	       if (u != null){
			  ImageIcon im = new ImageIcon (u);

			  b = (JButton) p.add(new JButton(im));
		   } else
				  b = (JButton) p.add(new JButton(name));
		   b.setActionCommand(name);
	  }

	  b.setToolTipText(tooltiptext);
	  b.setBorder(BorderFactory.createRaisedBevelBorder());
	  b.setMargin(new Insets(0,0,0,0));
	  b.addActionListener(listener);
	}


}
