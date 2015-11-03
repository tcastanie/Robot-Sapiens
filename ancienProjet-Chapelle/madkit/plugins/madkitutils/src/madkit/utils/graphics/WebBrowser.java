/*
* WebBrowser.java - Graphics utilities for MadKit agents
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
import java.awt.event.KeyEvent;

import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JSeparator;
import javax.swing.JToolBar;

import madkit.kernel.Agent;

public class WebBrowser extends AbstractWebBrowser
{



    public WebBrowser() {
        super();                 // Chain to JFrame constructor
    }

    public WebBrowser(Agent ag){
        super(ag);
    }

    protected void init(){
        super.init();
        JMenuBar menubar = new JMenuBar();
        this.setJMenuBar(menubar);

        JMenu menuFile=new JMenu("File");
        menubar.add(menuFile);
        //addMenuItem(menuFile, "New", "New",  -1);
        addMenuItem(menuFile, "Open...", "Open",  -1);
        menuFile.add(new JSeparator());
        addMenuItem(menuFile, "Close", "Close", KeyEvent.VK_W);
        addMenuItem(menuFile, "Exit", "Exit", KeyEvent.VK_Q);


        // menu "Edition"
        JMenu menuGo=new JMenu("Go");
        menubar.add(menuGo);
        addMenuItem(menuGo, "Forward", "Forward", KeyEvent.VK_X);
        addMenuItem(menuGo, "Backward", "Backward", KeyEvent.VK_C);
        addMenuItem(menuGo, "Reload", "Reload", KeyEvent.VK_V);
        addMenuItem(menuGo, "Home", "Clear", KeyEvent.VK_DELETE);

        JToolBar toolbar = new JToolBar();
        addButton(toolbar,"Open","Open file",null);
        addButton(toolbar,"Backward","Go backward",null);
        addButton(toolbar,"Forward","Go forward",null);
        addButton(toolbar,"Reload","Reload",null);

        // Add the URL field and a label for it to the end of the toolbar
        toolbar.add(new JLabel("         URL:"));
        toolbar.add(urlField);
        // And add the toolbar to the top of the window
        this.getContentPane().add(toolbar, BorderLayout.NORTH);

    }


}
