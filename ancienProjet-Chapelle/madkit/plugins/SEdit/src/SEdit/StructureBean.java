/*
* StructureBean.java - SEdit, a tool to design and animate graphs in MadKit
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

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JTabbedPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

import madkit.utils.graphics.LoadDialog;


public abstract class StructureBean extends JRootPane
{
    StructureAgent myAgent;
    StructureEditor editor;

	PropertyFrame propertyEditor;

    StructureDialog propertyDialog;

    /**
       * Get the value of editor.
       * @return Value of editor.
       */
    public StructureEditor getEditor() {return editor;}

    /**
       * Set the value of editor.
       * @param v  Value to assign to editor.
       */
    public void setEditor(StructureEditor  v) {this.editor = v;}





    JToggleButton buttonSnapToGrid;
	JToggleButton buttonPropertyEditor;


    public void setTitle(String s){
    	Container c = Utils.getFrameParent(this);
    	if (c != null){
    		if (c instanceof JFrame)
    			((JFrame) c).setTitle(s);
    		else if (c instanceof Frame)
    			((Frame) c).setTitle(s);
    		else if (c instanceof JInternalFrame)
    			((JInternalFrame) c).setTitle(s);
    	}
    }

    public Structure getStructure(){
	    return(myAgent.getStructure());
    }


    public Formalism getFormalism() {
    	return(myAgent.getFormalism());
    }

    public StructureBean(StructureAgent a)
    {
	    myAgent = a;

    }


    JComponent makeElements(){
        Hashtable categories = new Hashtable();
        //	categories.put("", new JToolBar());
        for(Enumeration e = getFormalism().nodeDescList.elements();e.hasMoreElements();) {
            NodeDesc d = (NodeDesc) e.nextElement();
            String cat = d.getCategory();

            if (!categories.containsKey(cat)){
                    JPanel j = new JPanel();
                    j.setLayout(new BoxLayout(j,BoxLayout.X_AXIS));
                    //JToolBar j = new JToolBar();
                    categories.put(cat, j);
                    //j.setFloatable(false);
                    //j.setOrientation(JToolBar.HORIZONTAL);
            }
            //addToolElement((JToolBar)categories.get(cat),d);
            addToolElement((JPanel)categories.get(cat),d);
        }
       // for(Enumeration e = categories.elements();e.hasMoreElements();){
       //     JToolBar tb = (JToolBar)e.nextElement();
       //     tb.addSeparator();
       // }
        for(Enumeration e = getFormalism().arrowDescList.elements();e.hasMoreElements();){
            ArrowDesc d = (ArrowDesc) e.nextElement();
            String cat = d.getCategory();
            if (!categories.containsKey(cat)){
                    JPanel j = new JPanel();
                    j.setLayout(new BoxLayout(j,BoxLayout.X_AXIS));
                //JToolBar j = new JToolBar();
                categories.put(cat, j);
                //j.setFloatable(false);
                //j.setOrientation(JToolBar.HORIZONTAL);
            }
            //addToolElement((JToolBar)categories.get(cat),d);
            addToolElement((JPanel)categories.get(cat),d);
        } if (categories.size()==1){
            //return (JToolBar)((categories.elements()).nextElement());
            return (JPanel)((categories.elements()).nextElement());
        } else {
            JTabbedPane tabbed = new JTabbedPane(JTabbedPane.TOP);
                for(Enumeration e = categories.keys();e.hasMoreElements();){
                    String key = (String)e.nextElement();
                    //JToolBar tb = (JToolBar)categories.get(key);
                    JPanel tb = (JPanel)categories.get(key);
                    if (key.equals(""))
                        tabbed.add("<untitled>",tb);
                    else
                        tabbed.add(key,tb);
                }
            tabbed.setPreferredSize(new Dimension(400,72));
            return tabbed;
        }
    }


    void addTool(JToolBar toolBar, final ActionDesc d)
    {
        JButton b;
        if ((d.getIcon() == null) || (d.getIcon().equals(""))) {
            b = (JButton) toolBar.add(new JButton(d.getDescription()));
            b.setActionCommand(d.getDescription());
        }
        else {
            ImageIcon i = new ImageIcon (d.getIcon());

            if (i.getImage()!=null)
            b = (JButton) toolBar.add(new JButton(new ImageIcon(getFormalism().getBase()+d.getIcon())));
            else
            b = (JButton) toolBar.add(new JButton(d.getDescription()));
            b.setActionCommand(d.getDescription());
        }

        b.setToolTipText(d.getDescription());
        b.setMargin(new Insets(0,0,0,0));

        b.addActionListener( new ActionListener(){

            public void actionPerformed(ActionEvent e) {
                d.execute(getStructure());
                editor.repaint();
            }}
                     );

    }


    void addTool(JToolBar toolBar, String name, String imageName) {
        JButton b;
        if ((imageName == null) || (imageName.equals(""))) {
            b = (JButton) toolBar.add(new JButton(name));
            b.setActionCommand(name);
        }
        else {
            java.net.URL u = this.getClass().getResource(imageName);
            ImageIcon i = new ImageIcon (u);

            if (i.getImage()!=null)
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


    //void addToolElement(JToolBar toolBar, ElementDesc d)
    void addToolElement(JPanel toolBar, ElementDesc d)
    {
	    JButton b;

        if (d.hasIcon())
            b = (JButton) toolBar.add(new JButton(new ImageIcon(getFormalism().getBase()+d.getIcon())));
        else
            b = (JButton) toolBar.add(new JButton(d.getName()));

        if (!d.getDescription().equals(""))
            b.setToolTipText(d.getDescription());

        b.setActionCommand(d.getName());

        b.setMargin(new Insets(0,0,0,0));
        b.addActionListener(
            new ActionListener(){
                    public void actionPerformed(ActionEvent e) {
                        NodeDesc nd = getFormalism().getNodeDesc(e.getActionCommand());
                        if (nd == null){
                          ArrowDesc ad = getFormalism().getArrowDesc(e.getActionCommand());
                          if (ad == null)
                              System.err.println("Erreur : j'ai pas trouve le descripteur de: " +
                                                 e.getActionCommand());
                          else
                              editor.setInsertMode(StructureEditor.AJOUTER_ARROW, ad);
                      }
                        else
                            editor.setInsertMode(StructureEditor.AJOUTER_NODE, nd);
                    }
                }
            );
    }





 /* void addElement(PanelListener p, String label,
  		  String className, String imageName, String tipText)
  {
  	JButton b;

 	if ((imageName == null) || imageName.equals("")) {
   	  b = new JButton(label);
   	  b.setSize(98,48);
   	}
  	else
 	    b = new JButton(new ImageIcon(imageName + ".gif"));
           // b.setLabel("");
	    b.setActionCommand(className);
            if ((tipText==null) || tipText.equals(""))
		b.setToolTipText(label);
            else
		b.setToolTipText(tipText);
       b.setMargin(new Insets(0,0,0,0));
       p.add(b);
       b.addActionListener(p);
	} */

    // JF: est en double avec celle de SEdit...  on utilise les
    // MenuItems et non les JMenuItem a cause des raccourcis
    // clavier...
    // Ol: tant pis, je repasse celle-ci en
    // JMenuItem. Trop de risques à faire du mélange heavy/lightweight
    void addMenuItem(ActionListener al, JMenu m, String label, String command, int key) {
        JMenuItem menuItem;
        if (key > 0)
	   menuItem = new JMenuItem(label,key); //new MenuShortcut(key));
        else
        	  menuItem = new JMenuItem(label);
        m.add(menuItem);
        menuItem.setActionCommand(command);
        menuItem.addActionListener(al);
   }


 	public void setSnapToGrid(boolean b){
     	buttonSnapToGrid.setSelected(b);
 	}

	void showPropertyEditor(boolean b){
		if (propertyEditor == null){
			if (b)
			  propertyEditor = new SElementPropertyFrame(this,null,4,true);
		} else
		  if (b){
			  propertyEditor.show();
		  } else
			  propertyEditor.hide();
	}

	void inspectElement(SElement elt){
		if (propertyEditor != null)
		  propertyEditor.editObject(elt);
		else
		  propertyEditor = new SElementPropertyFrame(this, elt,4,false);
	}


  /** les commandes standards qui ne dependent pas d'un type
   de structure
   */
  void command(String a)
  {
      if (a.equals("close"))	close();
      else if (a.equals("Select")) editor.setMode(0);
      //      else if (a.equals("capturer")) editor.setMode(1);
      else if (a.equals("Clear")) editor.clearAll();
      else if (a.equals("Cut")) editor.cut();
      else if (a.equals("Copy")) editor.copyToClipBoard();
      else if (a.equals("Paste")) {editor.clearSelection(); myAgent.insertClipBoard();
      editor.updateTranslateList();

      }

      else if (a.equals("Show grid")) editor.toggleShowGrid();
      // else if (a.equals("Snap to grid")) editor.toggleSnapToGrid();
      else if (a.equals("delete")) editor.deleteSelection();
 //     else if (a.equals("saveAs")) editor.saveAs();
 //     else if (a.equals("save")) editor.save();
      else if (a.equals("switchConn")) editor.switchConn();
      else if (a.equals("switchNodes")) editor.switchNodes();
      else if (a.equals("switchArrows")) editor.switchArrows();
      else if (a.equals("insertXMLFile")) {

         LoadDialog ld = new LoadDialog(this,LoadDialog.LOAD,"load a structure","sed");
         if (ld.isFileChoosed()) {
			 String fileDir = ld.getDirName();
			 String fileName = ld.getFileName();

			editor.clearSelection();

			myAgent.insertXMLFile(fileDir+fileName,true);
			editor.repaint();
	     }
      }
      else if (a.equals("properties")){
            propertyDialog = new StructureDialog(Utils.getRealFrameParent(this),getEditor());
      }
      else if (a.equals("saveXMLFile")) save();
      else if (a.equals("saveAsXMLFile")) saveAs();
      else if (a.equals("print")) editor.printEditor();
      else if (a.equals("Align horizontally")) editor.alignHorizontal();
      else if (a.equals("Align vertically")) editor.alignVertical();
      else if (a.equals("about"))
	  {
	      String s = "Formalism "+getFormalism().name+"\n";
	      for (Enumeration e=getFormalism().authors.elements(); e.hasMoreElements();)
		  s=s+"- "+(String) e.nextElement();
	      s=s+" -";

	      JOptionPane.showMessageDialog(null, s);
	  }
      else if (a.equals("doc")) {
	  HelpBox hb = new HelpBox(getFormalism().docURL,getFormalism());
      }
      else if (a.equals("dumpFormalism")) {
	  this.getStructure().getFormalism().dump();
      }
      else if (a.equals("dumpStructure")) {
	  this.getStructure().dump();
      }
      //Ol: FIXME else editor.command(a);
      // creation des objets dumpFormalism
  }

    boolean ending=false;

    synchronized void close(){
     if (!ending){
          ending=true;
          System.out.println("closing bean");
          myAgent.close();
          if (propertyDialog != null)
            propertyDialog.dispose();
          if (propertyEditor != null)
            propertyEditor.dispose();
        }
    }

    void saveAs() {


         LoadDialog ld = new LoadDialog(this,LoadDialog.SAVE,"save a structure","sed");
         if (ld.isFileChoosed()) {
			 String dirName = ld.getDirName();
			 String fileName = ld.getFileName();

			 System.out.println(":: saving file: "+dirName+fileName);
		   	 myAgent.saveXMLFile(dirName+fileName);
	     }

       /* JFileChooser fd = new JFileChooser(System.getProperty("user.dir",null));
       fd.setDialogTitle("Saving structure");
       int retval = fd.showSaveDialog(null);
       if (retval != -1) {
       	if (fd.getSelectedFile() != null) {
	       	String dirName = fd.getSelectedFile().getParent()+File.separator;
	       	String fileName = fd.getSelectedFile().getName();
			System.out.println(":: saving file: "+dirName+File.separator+fileName);
		   	myAgent.saveXMLFile(dirName+File.separator+fileName);
	   	}
	   } */
    }

    void save() {
	   String fName = myAgent.getFileName();
	   if (fName == null)
	   		saveAs();
	   else
		   	myAgent.saveXMLFile(fName);
	   }

}









