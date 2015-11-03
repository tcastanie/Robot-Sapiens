/*
* AbstractEditorPanel.java - a NotePad editor in MadKit
* Copyright (C) 1998-2002  Jacques Ferber
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
package madkit.utils.agents;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Event;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;

import madkit.kernel.AbstractAgent;
import madkit.kernel.JTextAreaWriter;
import madkit.kernel.Utils;



public abstract class AbstractEditorPanel extends JRootPane implements ActionListener{

 	protected JTextArea inputArea;
 	protected JTextArea outputArea;

 	protected PrintWriter out;

 	protected AbstractAgent ag;

 	protected JToolBar toolBar;
 	protected JMenuBar menubar;

    protected JPanel commandPanel;


    public PrintWriter stdout() { return out;}
	public PrintWriter stderr() { return out;}
	public InputStream stdin()  { return System.in; } // other?

	public JTextArea getInputArea(){ return(inputArea);}
	public JTextArea getOutputArea(){ return(outputArea);}


	public JToolBar getToolbar(){ return(toolBar);}
	public JMenuBar getMenubar(){ return(menubar);}


    public AbstractEditorPanel (AbstractAgent  _ag){
      		ag = _ag;


      		// les menus attaches a la fenetre
        	menubar=new JMenuBar();
        	this.setJMenuBar(menubar);	// on installe le menu bar

	        // menu "Fichier"
	        JMenu menuFile=new JMenu("File");
	        menubar.add(menuFile);
	        addMenuItem(this, menuFile, "New file", "new",KeyEvent.VK_N);
	        addMenuItem(this, menuFile, "Open", "openfile", -1);
	        addMenuItem(this, menuFile, "Save", "save", KeyEvent.VK_S, KeyEvent.VK_S);
	        addMenuItem(this, menuFile, "Save as", "saveAs", -1);
			//addMenuItem(this, menuFile, "Print", "print", KeyEvent.VK_P, KeyEvent.VK_P);
	        //addMenuItem(this, menuFile, "Close", "close", KeyEvent.VK_W, KeyEvent.VK_W);
	        //addMenuItem(this, menuFile, "Quit", "quit", KeyEvent.VK_Q, KeyEvent.VK_Q);


	        // menu "Edition"
	        JMenu menuEdit=new JMenu("Edit");
	        menubar.add(menuEdit);
	        addMenuItem(this, menuEdit, "Cut", "cut", KeyEvent.VK_X, KeyEvent.VK_X);
	        addMenuItem(this, menuEdit, "Copy", "copy", KeyEvent.VK_C, KeyEvent.VK_C);
	        addMenuItem(this, menuEdit, "Paste", "paste", KeyEvent.VK_V, KeyEvent.VK_V);
	        addMenuItem(this, menuEdit, "Delete", "delete", KeyEvent.VK_DELETE, KeyEvent.VK_DELETE);
	        addMenuItem(this, menuEdit, "Clear output", "clear",0, 0);


			getContentPane().setLayout(new BorderLayout(10,10));


			commandPanel = new JPanel();
	        commandPanel.setAlignmentX(LEFT_ALIGNMENT);
	        commandPanel.setAlignmentY(TOP_ALIGNMENT);
	        commandPanel.setLayout(new FlowLayout(FlowLayout.LEFT));


	        toolBar = new JToolBar();
	        addTool(toolBar, "new", "new file",
			    "/toolbarButtonGraphics/general/New24.gif");
	        addTool(toolBar, "openfile", "Open file",
			"/toolbarButtonGraphics/general/Open24.gif");

		addTool(toolBar, "save", "Save file",
			"/toolbarButtonGraphics/general/Save24.gif");
		toolBar.addSeparator();
	        addTool(toolBar, "cut", "Cut",
			"/toolbarButtonGraphics/general/Cut24.gif");
	        addTool(toolBar, "copy", "Copy",
			"/toolbarButtonGraphics/general/Copy24.gif");
	        addTool(toolBar, "paste", "Paste",
			"/toolbarButtonGraphics/general/Paste24.gif");

			commandPanel.add(toolBar);

			getContentPane().add("North",commandPanel);

			inputArea = new JTextArea(10,40);
			JScrollPane inscroller = new JScrollPane();
			inscroller.setSize(300,200);
			inscroller.getViewport().add(inputArea);

			outputArea = new JTextArea(";; EditorAgent 1.1 (c) Madkit team\n",10,40);
			JScrollPane outscroller = new JScrollPane();
			outscroller.setSize(300,200);
			outscroller.getViewport().add(outputArea);

			//Create a split pane with the two scroll panes in it.
			JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
		                           inscroller, outscroller);
			splitPane.setOneTouchExpandable(true);
			splitPane.setDividerLocation(150);

			getContentPane().add(splitPane,"Center");

      		out = new PrintWriter(new JTextAreaWriter(outputArea), true);

    }


 	public void actionPerformed(ActionEvent e){
 		String c = e.getActionCommand();
 		command(c);
 	}

 	public void command(String c){
 		if (c.equals("openfile")) openFile();
        else if (c.equals("new")) newFile();
 		else if (c.equals("save")) save();
 		else if (c.equals("copy")) inputArea.copy();
 		else if (c.equals("cut")) inputArea.cut();
 		else if (c.equals("paste")) inputArea.paste();
 		else if (c.equals("saveAs")) saveAs();
 		else if (c.equals("clear")) clear();
 	}


    public void clearOutputArea(){
    	outputArea.setText("");
    }

    public void newFile(){
        saveIfModified();
        inputArea.setText("");
        setCurrentFile(null);
    }


	//String dirName;
	//String fileName;

    String currentFile=null;

    public void setCurrentFile(String s){currentFile=s;}
    public String getCurrentFile(){return currentFile;}


  // protected String getDirName(){return dirName;}
  // protected String getFileName(){return fileName;}

	public Frame getFrameParent(){
		Component c = this;
		while (!(c instanceof Frame))
			if (c == null)
				return(null);
			else
				c = c.getParent();
		return((Frame) c);
	}

  protected String extens=null;
  public void setExtens(String ext){extens = ext;}

  public static final boolean LOAD = true;
  public static final boolean SAVE = false;

  protected boolean getFileDialog(boolean direction, String title,String extens){
            JFileChooser fd = new JFileChooser(System.getProperty("user.dir",null));
            if (extens != null){
                    System.out.println("set filter: " + extens);
                fd.setFileFilter(new SwingFileFilter(extens));
            }
       		fd.setDialogTitle(title);
            int retval=0;
            if (direction == LOAD){
                //fd.setDialogType(JFileChooser.OPEN_DIALOG);
       		    retval = fd.showOpenDialog(this);
            } else {
                //fd.setDialogType(JFileChooser.SAVE_DIALOG);
       		    retval = fd.showSaveDialog(this);
            }
       		if (retval == JFileChooser.APPROVE_OPTION)	{
                if (fd.getSelectedFile()!=null)
                {
                    setCurrentFile(fd.getSelectedFile().getPath());
                    return(true);
                }
                else
                    return false;
		    } else
                    return(false);
 }

 public void openFile() {
	Object result;
	if (getFileDialog(LOAD,"Open file",extens)) {
		if (currentFile != null){
            readFile(getCurrentFile());
        }
    }
 }

  public void readFile(String s){
        readFile(new File(s));
  }

  public void readFile(File f){
            String text=new String();
            String str=new String();
            BufferedReader ds=null;
            try {
				ds = new BufferedReader(new FileReader(f));

      			while (str != null) {
  	  				str = ds.readLine();

	  				if (str != null)
	    				text = text + str + "\n";
	    		}
				inputArea.setText(text); // supprime l'ancien texte
                Utils.setFrameParentTitle(this,f.getName());
                ds.close();
			}
			catch (Exception err) {
      			System.out.println("Cannot open file " + getCurrentFile());
    		}
  }

  void saveIfModified(){
    if (!(inputArea.getText().equals(""))){
        int  result;
	    result = JOptionPane.showConfirmDialog(this,
					       "Buffer is not empty, do you want to save it?",
					       "Warning",
					       JOptionPane.YES_NO_OPTION,
					       JOptionPane.QUESTION_MESSAGE,
					       null);
	    if(result == JOptionPane.YES_OPTION){
	        save();
        }
    }
  }

	public void saveAs() {
		if (getFileDialog(SAVE,"Save file",extens))
	       	save1(true);
	}

   public void save() {
		if (getCurrentFile() == null)
			saveAs();
		else
			save1(true);
    }


   void save1(boolean ok) {
   	if (ok) {
       try{
	    PrintWriter outFile = new PrintWriter(new FileWriter(getCurrentFile()));
	    String content = inputArea.getText();
	    outFile.print(content);
	    outFile.flush();
        outFile.close();
		} catch (FileNotFoundException e) {
		    System.err.println("saveContent: " + e);
		} catch (IOException e) {
		    System.err.println("saveContent: " + e);
		}
		System.out.println(":: file " + getCurrentFile() + " saved");
        Utils.setFrameParentTitle(this,Utils.getFileNameFromPath(getCurrentFile()));
	}
   }


  public void clear(){
     	clearOutputArea();
  }

  public void print(String s){
     	stdout().print(s);
  }

  public void println(String s){
     	stdout().println(s);
  }

  public void addTool(JToolBar toolBar, String name, String descr, String imageName) {
	  JButton b;
	  if (imageName.equals("")) {
	  	b = (JButton) toolBar.add(new JButton(name));
	  	b.setActionCommand(name);
	  }
	  else {
	      java.net.URL u = this.getClass().getResource(imageName);
	      if (u!=null)
		  b = (JButton) toolBar.add(new JButton(new ImageIcon(u)));
	      else
		  b = (JButton) toolBar.add(new JButton(name));
	      b.setActionCommand(name);
	  }

	  b.setToolTipText(descr);
	  b.setMargin(new Insets(0,0,0,0));
	  b.addActionListener(
        		new ActionListener(){
        			public void actionPerformed(ActionEvent e) {
        				command(e.getActionCommand());
        		}
        });
  }


    // Devrait se trouver ensuite dans un "Utils" dans Madkit...
  public static void addMenuItem(ActionListener al, JMenu m, String label, String command, int key) {
  		addMenuItem(al, m, label, command, key, -1);
   }

  public  static  void addMenuItem(ActionListener al, JMenu m, String label, String command, int key, int ckey) {
        JMenuItem menuItem;
        if (key > 0)
	  		menuItem = new JMenuItem(label,key);//Ol. Was MenuShortCut
        else
        	 menuItem = new JMenuItem(label);
        m.add(menuItem);
	// System.err.println("added"+label);

        menuItem.setActionCommand(command);
        menuItem.addActionListener(al);
        if (ckey > 0) {
        	if (ckey != KeyEvent.VK_DELETE)
        		menuItem.setAccelerator(KeyStroke.getKeyStroke(ckey, Event.CTRL_MASK, false));
        	else
        		menuItem.setAccelerator(KeyStroke.getKeyStroke(ckey, 0, false));
        }
   }

}

// swing class
class SwingFileFilter extends javax.swing.filechooser.FileFilter {

    String extens="xml";

    SwingFileFilter(String ext){
        extens = ext;
    }

    // Accept all directories and extension file
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }
        if (extens == null)
            return true;
        else if (f.getName().endsWith("."+extens))
            return true;
        else
            return false;
    }

    // The description of this filter
    public String getDescription() {
        return extens+" files";
    }
}
