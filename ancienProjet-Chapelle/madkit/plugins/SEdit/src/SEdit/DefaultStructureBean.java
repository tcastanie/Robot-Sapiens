/*
* DefaultStructureBean.java - SEdit, a tool to design and animate graphs in MadKit
* Copyright (C) 1998-2002 Jacques Ferber, Olivier Gutknecht
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
package SEdit;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.util.Enumeration;

import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;


/**
 *  The standard structure bean class which is used as a default
 *  for SEdit structures.
 */

public class DefaultStructureBean extends StructureBean {

   public JScrollPane scrollPane;
    protected JPanel commandPanel;

    //protected JPanel elementPanel;
    protected JToolBar elementPanel;
    // protected JSplitPane splitElementProperty;
    // protected JSplitPane splitEditorElements;

    //protected JLabel propertyPanel;

    public DefaultStructureBean(StructureAgent a)
    {
        super(a);
	    setBackground(SystemColor.control);

        // les menus attaches a la fenetre
        JMenuBar menubar=new JMenuBar();
        this.setJMenuBar(menubar);	// on installe le menu bar

        ActionListener menuAl = new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        				command(e.getActionCommand());
           }};

        // menu "Fichier"
        JMenu menuFile=new JMenu("File");
        menubar.add(menuFile);
	    //Utils.addMenuItem(menuAl, menuFile, "New", "newFile", KeyEvent.VK_N);
	    //Utils.addMenuItem(menuAl, menuFile, "Load File", "loadFile", KeyEvent.VK_N);
        Utils.addMenuItem(menuAl, menuFile, "Insert file", "insertXMLFile", -1);
        Utils.addMenuItem(menuAl, menuFile, "Save", "saveXMLFile", KeyEvent.VK_S, KeyEvent.VK_S);
        Utils.addMenuItem(menuAl, menuFile, "Save as.. ", "saveAsXMLFile", -1);
		Utils.addMenuItem(menuAl, menuFile, "Print", "print", KeyEvent.VK_P, KeyEvent.VK_P);
        Utils.addMenuItem(menuAl, menuFile, "Close", "close", KeyEvent.VK_W, KeyEvent.VK_W);
	// Utils.addMenuItem(menuAl, menuFile, "Dump formalism", "dumpFormalism", KeyEvent.VK_Q, KeyEvent.VK_Q);
	Utils.addMenuItem(menuAl, menuFile, "Dump structure", "dumpStructure", -1, -1);


        // menu "Edition"
        JMenu menuEdit=new JMenu("Edit");
        menubar.add(menuEdit);
        Utils.addMenuItem(menuAl, menuEdit, "Cut", "cut", KeyEvent.VK_X, KeyEvent.VK_X);
        Utils.addMenuItem(menuAl, menuEdit, "Copy", "copy", KeyEvent.VK_C, KeyEvent.VK_C);
        Utils.addMenuItem(menuAl, menuEdit, "Paste", "paste", KeyEvent.VK_V, KeyEvent.VK_V);
        Utils.addMenuItem(menuAl, menuEdit, "Clear", "delete", KeyEvent.VK_DELETE, KeyEvent.VK_DELETE);


        JMenu optionsEdit=new JMenu("Options");
        menubar.add(optionsEdit);
        Utils.addMenuItem(menuAl, optionsEdit,
			  "Toggle connectors display", "switchConn", -1);
        Utils.addMenuItem(menuAl, optionsEdit,
			  "Toggle nodes display", "switchNodes", -1);
        Utils.addMenuItem(menuAl, optionsEdit,
			  "Toggle arrows display", "switchArrows", -1);
        Utils.addMenuItem(menuAl, optionsEdit, "settings", "properties", -1);

	if ((getFormalism().authors != null) ||
	    (getFormalism().docURL != null)){
		JMenu menuHelp=new JMenu("Help");
		menubar.add(menuHelp);

		if (getFormalism().docURL != null)
		    Utils.addMenuItem(menuAl, menuHelp, "Documentation", "doc", -1);

		if (getFormalism().authors != null)
		    Utils.addMenuItem(menuAl, menuHelp, "About", "about", -1);
	    }

        setSize(600, 450);
	    getContentPane().setLayout(new BorderLayout(10,10));
        // le toolbar
	    commandPanel = (JPanel)getContentPane().add(new JPanel());
        commandPanel.setAlignmentX(LEFT_ALIGNMENT);
        commandPanel.setAlignmentY(TOP_ALIGNMENT);
        //commandPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        //commandPanel.setLayout(new BoxLayout(commandPanel,BoxLayout.Y_AXIS));

		// snap to grid
		//--------------
     	java.net.URL u = this.getClass().getResource("/toolbarButtonGraphics/general/snaptogrid.gif");
        ImageIcon snapToGridIcon = new ImageIcon(u);
        if (snapToGridIcon.getImage() != null)
        	buttonSnapToGrid = new JToggleButton(snapToGridIcon, false);
        else
        	buttonSnapToGrid = new JToggleButton("Snap to grid", false);
		buttonSnapToGrid.setMargin(new Insets(1,1,1,1));
		buttonSnapToGrid.addItemListener(new ItemListener(){
        	public void itemStateChanged(ItemEvent e) {
            	if (e.getStateChange() == ItemEvent.SELECTED) {
                	editor.setSnapToGrid1(true);
            	} else {
                	editor.setSnapToGrid1(false);
            	}
        	}
          }
		);

		// property editor
		//-----------------
		java.net.URL u2 = this.getClass().getResource("/toolbarButtonGraphics/general/loupe.gif");
        if (u2 != null){
		  ImageIcon propertyEditorIcon = new ImageIcon(u2);
		  buttonPropertyEditor = new JToggleButton(propertyEditorIcon, false);
        } else
        	buttonPropertyEditor = new JToggleButton("prop ed.", false);
		buttonPropertyEditor.setMargin(new Insets(1,1,1,1));
		buttonPropertyEditor.addItemListener(new ItemListener(){
        	public void itemStateChanged(ItemEvent e) {
            	if (e.getStateChange() == ItemEvent.SELECTED) {
                	showPropertyEditor(true);
            	} else {
                	showPropertyEditor(false);
            	}
        	}}
		);


        JToolBar toolBar = new JToolBar();
        //addTool(toolBar, "loadFile", "/toolbarButtonGraphics/general/Load24.gif");
        addTool(toolBar, "insertXMLFile", "/toolbarButtonGraphics/general/Inserte4.gif");
		addTool(toolBar, "saveXMLFile", "/toolbarButtonGraphics/general/Save24.gif");
        toolBar.addSeparator();
        addTool(toolBar, "Cut", "/toolbarButtonGraphics/general/Cut24.gif");
        addTool(toolBar, "Copy", "/toolbarButtonGraphics/general/Copy24.gif");
        addTool(toolBar, "Paste", "/toolbarButtonGraphics/general/Paste24.gif");
        toolBar.addSeparator();
		addTool(toolBar, "Print", "/toolbarButtonGraphics/general/Print24.gif");
		toolBar.addSeparator();
        addTool(toolBar, "Align horizontally", "/toolbarButtonGraphics/general/alignHorizontal24.gif");
        addTool(toolBar, "Align vertically", "/toolbarButtonGraphics/general/alignVertical24.gif");
        addTool(toolBar, "Show grid", "/toolbarButtonGraphics/general/showgrid.gif");
        toolBar.add(buttonSnapToGrid);
		toolBar.add(buttonPropertyEditor);
        //addTool(toolBar, "Snap to grid", "/toolbarButtonGraphics/general/snaptogrid.gif");
        toolBar.addSeparator();
        addTool(toolBar, "Select", "/toolbarButtonGraphics/general/Select24.gif");

	//    addTool(toolBar, "capturer", "images/select.gif");
    //    addTool(toolBar, "Clear", "/toolbarButtonGraphics/general/Delete24.gif");
        toolBar.addSeparator();

        if (getFormalism().hasActions()) {
            for(Enumeration i = getFormalism().getActions().keys(); i.hasMoreElements();) {
              ActionDesc d  = (ActionDesc) getFormalism().getActions().get((i.nextElement()));
              if (d == null)
                toolBar.addSeparator();
              else {
                addTool(toolBar, d);//.getAction(), d.getIcon()); // 0 = commande, 1 = icon
              }
            }
        }

		getContentPane().add("North",commandPanel);


        // Element Panels (Nodes and Arrows buttons)
        //---------------------------------------------

	    elementPanel = new JToolBar();
        elementPanel.setFloatable(true);

		elementPanel.add(makeElements());

       editor = new StructureEditor(myAgent);

       scrollPane = new JScrollPane(22,32);
       scrollPane.setPreferredSize(new Dimension(600,300));

       scrollPane.getViewport().add(editor);
       getContentPane().add("North",toolBar);
       if (myAgent.getShowElementPanel()){
           getContentPane().add("South",elementPanel);
           getContentPane().add("Center",scrollPane);
        }
        else {
           getContentPane().add("Center",scrollPane);
        }

       editor.setGraphics(scrollPane.getViewport().getGraphics());
	   editor.setStructureBean(this);

       setLocation(200,100);

       this.setVisible(true);

    }
}
