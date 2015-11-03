/*
* SchemeAbstractEditorPanel.java - SchemeEditor, a simple editor to evaluate Scheme expressions
* Copyright (C) 2000-2002 Jacques Ferber
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
package madkit.scheme;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;

import madkit.kernel.Agent;
import madkit.kernel.JTextAreaWriter;
import madkit.utils.graphics.GraphicUtils;
import madkit.utils.graphics.LoadDialog;



public abstract class SchemeAbstractEditorPanel extends JRootPane implements ActionListener{

 	protected JTextArea inputArea;
 	protected JTextArea outputArea;

 	protected PrintWriter out;

 	protected Agent ag;

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


    public SchemeAbstractEditorPanel (Agent _ag){
      		ag = _ag;


      		// les menus attaches a la fenetre
        	menubar=new JMenuBar();
        	this.setJMenuBar(menubar);	// on installe le menu bar

	        // menu "Fichier"
	        JMenu menuFile=new JMenu("File");
	        menubar.add(menuFile);
	       // addMenuItem(menuAl, menuFile, "nouvelle structure", "new",KeyEvent.VK_N);
	        GraphicUtils.addMenuItem(this, menuFile, "Open", "openfile", -1);
	        GraphicUtils.addMenuItem(this, menuFile, "Save", "save", KeyEvent.VK_S, KeyEvent.VK_S);
	        GraphicUtils.addMenuItem(this, menuFile, "Save as", "saveAs", -1);
			//addMenuItem(this, menuFile, "Print", "print", KeyEvent.VK_P, KeyEvent.VK_P);
	        //addMenuItem(this, menuFile, "Close", "close", KeyEvent.VK_W, KeyEvent.VK_W);
	        //addMenuItem(this, menuFile, "Quit", "quit", KeyEvent.VK_Q, KeyEvent.VK_Q);


	        // menu "Edition"
	        JMenu menuEdit=new JMenu("Edit");
	        menubar.add(menuEdit);
	        GraphicUtils.addMenuItem(this, menuEdit, "Cut", "cut", KeyEvent.VK_X, KeyEvent.VK_X);
	        GraphicUtils.addMenuItem(this, menuEdit, "Copy", "copy", KeyEvent.VK_C, KeyEvent.VK_C);
	        GraphicUtils.addMenuItem(this, menuEdit, "Paste", "paste", KeyEvent.VK_V, KeyEvent.VK_V);
	        GraphicUtils.addMenuItem(this, menuEdit, "Delete", "delete", KeyEvent.VK_DELETE, KeyEvent.VK_DELETE);
	        GraphicUtils.addMenuItem(this, menuEdit, "Clear output", "clear",0, 0);


			getContentPane().setLayout(new BorderLayout(10,10));


			commandPanel = new JPanel();
	        commandPanel.setAlignmentX(LEFT_ALIGNMENT);
	        commandPanel.setAlignmentY(TOP_ALIGNMENT);
	        commandPanel.setLayout(new FlowLayout(FlowLayout.LEFT));


	        toolBar = new JToolBar();
	        // addTool(toolBar, "new", "new");
	        GraphicUtils.addTool(this,toolBar, "openfile", "Open file",
			"/toolbarButtonGraphics/general/Open24.gif");

		GraphicUtils.addTool(this,toolBar, "save", "Save file",
			"/toolbarButtonGraphics/general/Save24.gif");
			toolBar.addSeparator();
	        GraphicUtils.addTool(this,toolBar, "cut", "Cut",
			"/toolbarButtonGraphics/general/Cut24.gif");
	        GraphicUtils.addTool(this,toolBar, "copy", "Copy",
			"/toolbarButtonGraphics/general/Copy24.gif");
	        GraphicUtils.addTool(this,toolBar, "paste", "Paste",
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


	String dirName;
	String fileName;

	public Frame getFrameParent(){
		Component c = this;
		while (!(c instanceof Frame))
			if (c == null)
				return(null);
			else
				c = c.getParent();
		return((Frame) c);
	}

  public static final boolean LOAD = true;
  public static final boolean SAVE = false;

    public boolean getFileDialog(boolean direction, String title,String extens){
         LoadDialog ld = new LoadDialog(this,direction,title,extens);
         if (ld.isFileChoosed()) {
			 dirName = ld.getDirName();
			 fileName = ld.getFileName();
             return true;
        } else
            return false;
  }


 public void openFile() {
	Object result;
	if (getFileDialog(LOAD,"Open file","scm")) {
		if (fileName != null){
			try{
				String text = new String();
	    		String str = new String();
      			FileInputStream fs = new FileInputStream(dirName+fileName);
				BufferedReader ds = new BufferedReader(new InputStreamReader(fs));

      			while (str != null) {
  	  				str = ds.readLine();

	  				if (str != null)
	    				text = text + str + "\n";
	    		}
				inputArea.setText(text); // supprime l'ancien texte
			}
			catch (Exception err) {
      			System.out.println("Cannot open file " + fileName);
    		}
		}
	}
  }



	public void saveAs() {
		if (getFileDialog(SAVE,"Save file","scm"))
	       	save1(true);
	}

   public void save() {
		if (fileName == null)
			saveAs();
		else
			save1(true);
    }


   void save1(boolean ok) {
   	if (ok) {
       try{
	    PrintWriter outFile = new PrintWriter(new FileWriter(dirName+fileName));
	    String content = inputArea.getText();
	    outFile.print(content);
	    outFile.flush();

		} catch (FileNotFoundException e) {
		    System.err.println("saveContent: " + e);
		} catch (IOException e) {
		    System.err.println("saveContent: " + e);
		}
		System.out.println(":: file " + fileName + " saved");
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

}
