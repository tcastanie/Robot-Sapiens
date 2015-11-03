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
//package editor;

package jsynedit;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.event.*;
import javax.swing.*;


//$$$$ modif
import java.util.*;
import javax.swing.JTabbedPane;

//$$ add [[
import java.util.Properties;
import jsynedit.search.FindReplace;
import jedit.lang.*;
import jedit.DefaultInputHandler;
import jedit.InputHandler;
//$$ add ]]

// FROM OURY'S
import gnu.gui.*;
import gnu.options.*;
import gnu.java.Beautifier;
import gnu.jbrowser.*;

import java.text.MessageFormat;
import gnu.actions.*;
//$$ oury
import gnu.search.FindAll;
// FROM OURY'S end

// #############
import madkit.kernel.*;
import madkit.utils.agents.AbstractEditorAgent;

public class AbstractEditorPanel
	extends JRootPane
	implements ActionListener, AncestorListener, ChangeListener {

    protected AbstractDoc inputArea;
    protected JTextArea outputArea;

    protected PrintWriter out;

    protected JToolBar toolBar;
    protected JMenuBar menubar;
    private static int cpt=1;//compteur pr lsnveuax documents
    protected JPanel commandPanel;
    
    // from Oury [[
    private Beautifier beautifier;
    private JSplitPane split;
	private InsertClassName jbrowser;
	boolean jbrowserEnabled = false;
	private static Properties props;
	private FindAll findAll;

	private BoxComment boxComment;
	private BoxUncomment boxUncomment;  // from Seb
	private ToUpperCase upperCase;
	private ToLowerCase lowerCase;
	private SimpleComment simpleComment;
	private SimpleUnComment simpleUnComment;
	private RemoveWhitespace rmWhiteSpace;
	
	private JFileChooser fileChooser;
	private Print printer;
	private CreateTemplate templates;
    // from Oury ]]
    
    // from PG [[
	private LeftIndent left_indent;
	private RightIndent right_indent;
	
	private String iconeNewFile,iconeOpenFile,iconeSaveFile,iconeSaveAllFile,iconeCutFile,
			iconeCopyFile,iconePasteFile,iconeUndoFile,iconeRedoFile,iconePropFile,
			iconeSearchFile,iconeCloseFile,iconePrintFile;
	// from PG ]]
	
    JMenu menuOptions;

    public PrintWriter stdout() { return out;}
    public PrintWriter stderr() { return out;}
    public InputStream stdin()  { return System.in; } // other?

    // $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
    protected FindReplace findReplace;
    protected JTabbedPane pane, vTabbedPane;
    protected JComboBox languages ;
	protected OptionsDialog optionsDialog;
    private AbstractDoc[] textAreas;
    private static int textAreasCurrentSize=0;
    private String currentFile;

    public AbstractDoc getTextArea(){ return inputArea;}
    public AbstractDoc[] getTextAreas(){ return textAreas;}

    public JTabbedPane getTabbedPane(){ return pane;}
	public JFileChooser getFileChooser(int mode){return fileChooser;}
    // $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
    public JTextArea getOutputArea(){ return(outputArea);}

    public JToolBar getToolbar(){ return(toolBar);}
    public JMenuBar getMenubar(){ return(menubar);}
    
	public static final boolean LOAD = true;
	public static final boolean SAVE = false;

    //#######################"
	AbstractDoc getInputArea(){
		return inputArea;
	}
	
    AbstractEditorAgent ag;
    public AbstractEditorPanel(AbstractEditorAgent ag){
		this();
    	this.ag = ag;
    }
    

    public AbstractEditorPanel (){


		// les menus attaches a la fenetre
		menubar=new JMenuBar();
		this.setJMenuBar(menubar);	// on installe le menu bar

		// menu "Fichier"
		JMenu menuFile=new JMenu("File");
		menubar.add(menuFile);
		addMenuItem(this, menuFile, "New file", "new",KeyEvent.VK_N, KeyEvent.VK_N);
		// from Oury
		JMenu menuTemplates=new JMenu("Templates");

		JMenu menuJavaTemplates=new JMenu("Java");
		JMenu menuMadkitTemplates=new JMenu("Madkit");
		//JMenu menuJspTemplates=new JMenu("JavaScipt");
		//JMenu menuPhpTemplates=new JMenu("PHP");

		menuTemplates.add(menuJavaTemplates);
		menuTemplates.add(menuMadkitTemplates);
		//menuTemplates.add(menuJspTemplates);
		//menuTemplates.add(menuPhpTemplates);

		addMenuItem(this, menuJavaTemplates, "Class", getUserDirectory()+"/templates/Java/Class.java", -1);
		addMenuItem(this, menuJavaTemplates, "Interface", getUserDirectory()+"/templates/Java/Interface.java", -1);
		addMenuItem(this, menuJavaTemplates, "Main", getUserDirectory()+"/templates/Java/Main.java", -1);


		addMenuItem(this, menuMadkitTemplates, "Agent", getUserDirectory()+"/templates/Madkit/Agent.java", -1);

		//addMenuItem(this, menuJspTemplates, "JSP", /*getUserDirectory()+*/"/gnu/templates/JSP/JSP.jsp", -1);
		//addMenuItem(this, menuJspTemplates, "Use bean", /*getUserDirectory()+*/"/gnu/templates/JSP/UseBean.jsp", -1);

		//addMenuItem(this, menuPhpTemplates, "php", /*getUserDirectory()+*/"/gnu/templates/PHP/File.php", -1);
		//addMenuItem(this, menuPhpTemplates, "pear to pear", /*getUserDirectory()+*/"/gnu/templates/PHP/pear.php", -1);


		// from Oury ]]
		addMenuItem(this, menuFile, "Open", "open", KeyEvent.VK_O, KeyEvent.VK_O);
		menuFile.addSeparator();
		addMenuItem(this, menuFile, "Save", "save", KeyEvent.VK_S, KeyEvent.VK_S);
		addMenuItem(this, menuFile, "Save as", "saveAs", -1);
		addMenuItem(this, menuFile, "Save All", "saveAll", -1);
		menuFile.addSeparator();
		//addMenuItem(this, menuFile, "Print", "print", KeyEvent.VK_P, KeyEvent.VK_P);
		addMenuItem(this, menuFile, "Print Document", "print", KeyEvent.VK_P,
					KeyEvent.VK_P);
		menuFile.addSeparator();
		addMenuItem(this, menuFile, "Close", "close", KeyEvent.VK_W, KeyEvent.VK_W);
		addMenuItem(this, menuFile, "Quit", "quit", KeyEvent.VK_Q, KeyEvent.VK_Q);


		// menu "Edition"
		JMenu menuEdit=new JMenu("Edit");
	
		addMenuItem(this, menuEdit, "Undo", "undo", KeyEvent.VK_Z, KeyEvent.VK_Z);
		addMenuItem(this, menuEdit, "Redo", "redo", KeyEvent.VK_Y, KeyEvent.VK_Y);
		menuEdit.addSeparator();
		addMenuItem(this, menuEdit, "Cut", "cut", KeyEvent.VK_X, KeyEvent.VK_X);
		addMenuItem(this, menuEdit, "Copy", "copy", KeyEvent.VK_C, KeyEvent.VK_C);
		addMenuItem(this, menuEdit, "Paste", "paste", KeyEvent.VK_V, KeyEvent.VK_V);
		menuEdit.addSeparator();
		addMenuItem(this, menuEdit, "Find", "find", KeyEvent.VK_F, KeyEvent.VK_F);
		addMenuItem(this, menuEdit, "Replace", "replace", KeyEvent.VK_R, KeyEvent.VK_R);
		menuEdit.addSeparator();
		// from PG [[
		addMenuItem(this, menuEdit, "Left Indent", "left_indent", KeyEvent.VK_LEFT, KeyEvent.VK_LEFT);
		addMenuItem(this, menuEdit, "Right Indent", "right_indent", KeyEvent.VK_RIGHT, KeyEvent.VK_RIGHT);
		menuEdit.addSeparator();
		// from PG ]]
		//$$ oury
		addMenuItem(this, menuEdit, "Box Comment", "boxcomment", -1); // -1 pr le moment
		addMenuItem(this, menuEdit, "Box Uncomment", "boxuncomment", -1);  // from Seb
    		addMenuItem(this, menuEdit, "Upper Case", "uppercase", KeyEvent.VK_UP, KeyEvent.VK_UP);
    		addMenuItem(this, menuEdit, "Lower Case", "lowercase", KeyEvent.VK_DOWN, KeyEvent.VK_DOWN);
    		addMenuItem(this, menuEdit, "Simple Comment", "simplecomment", -1); // -1 pr le moment
    		addMenuItem(this, menuEdit, "Simple Uncomment", "simpleuncomment", -1); // -1 pr le moment
    		addMenuItem(this, menuEdit, "remove Space", "removewhitespace", -1); // -1 pr le moment

		menubar.add(menuEdit);
		// $ modif 
		//	addMenuItem(this, menuEdit, "Clear output", "clear",0, 0);


		menubar.add(menuTemplates);

		menuOptions=new JMenu("Options");
		menubar.add(menuOptions);
		
		// from Oury [[
		JMenu java=new JMenu("Java");
		addMenuItem(this, java, "Beautifier", "beautifier", -1);
		addMenuItem(this, java, "Browser", "browser", -1);
		menuOptions.add(java);
	
		addMenuItem(this, menuOptions,"Find All", "findall",-1);
		addMenuItem(this, menuOptions,"Preferences...", "preferences",-1);
		//	from Oury ]]
		
		addMenuItem(this, menuOptions,"General","optGen", -1);
		addMenuItem(this, menuOptions,"Police","optPol", -1);
		addMenuItem(this, menuOptions,"Style & Color", "optS&C",-1);
	
		/* choix eventuels à placer
		   addMenuItem(this, menuOption,  );
		   addMenuItem(this, menuOption,  );
		   addMenuItem(this, menuOption,  );
		   addMenuItem(this, menuOption,  );
	
		   JMenu menuLangage=new JMenu("Language");
		   menubar.add(menuLangage);
		*/
	

		JMenu menuAide=new JMenu("Help");
		menubar.add(menuAide);


		getContentPane().setLayout(new BorderLayout(1,1));


		commandPanel = new JPanel();
		commandPanel.setAlignmentX(LEFT_ALIGNMENT);
		commandPanel.setAlignmentY(TOP_ALIGNMENT);
		commandPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
       	//AbstractEditorPanelcommandPanel.setSize(300,300);
	
		toolBar = new JToolBar();
		
		//applyTheme("metal");
		//applyTheme("purple");
		//applyTheme("blue");
		//applyTheme("gold");
		applyTheme("transpa");
		//applyTheme("red");

		addTool(toolBar, "new", "new file", "/images/"+iconeNewFile);
		addTool(toolBar, "open", "Open file", "/images/"+iconeOpenFile);
		addTool(toolBar, "save", "Save file", "/images/"+iconeSaveFile);
		addTool(toolBar, "saveAll", "Save all files","/images/"+iconeSaveAllFile);
		toolBar.addSeparator();
		addTool(toolBar, "cut", "Cut", "/images/"+iconeCutFile);
		addTool(toolBar, "copy", "Copy","/images/"+iconeCopyFile);
		addTool(toolBar, "paste", "Paste","/images/"+iconePasteFile);
		toolBar.addSeparator();
		addTool(toolBar, "undo", "Undo", "/images/"+iconeUndoFile);
		addTool(toolBar, "redo", "Redo","/images/"+iconeRedoFile);
		//DAVID
		//toolBar.addSeparator();
		//addTool(toolBar, "find", "Search","/images/"+iconeSearchFile);
		//addTool(toolBar, "print", "Print","/images/"+iconePrintFile);
		//addTool(toolBar, "optGen", "General Options","/images/"+iconePropFile);
		// END
		//toolBar.addSeparator();
		//addTool(toolBar, "close", "Close the current tabbedpane", "/images/close.png");
		//toolBar.addSeparator();


		// DAVID
		//languages.setRenderer(new ModifiedCellRenderer());
		// remplacée par
		//languages.setRenderer(new jsynedit.search.ModifiedCellRenderer());
		// raison : "The type ModifiedCellRenderer is ambiguous"
	
		//languages.setEditable(true);

		//languages.addActionListener( this ) ;

		//$$ from Seb [[
		//String repTemp;
		//repTemp =  getUserDirectory() + "/build/jedit/lang/" ;
		//java.net.URL repTemp = this.getClass().getResource("/jedit/lang/");
		//String filesTemp [] = getWildCardMatches( repTemp.getPath(), "TokenMarker.class" , true );
		languages = new JComboBox();
		languages.addItem("None");
		languages.addItem("BatchFile");
		languages.addItem("C");
		languages.addItem("CC");
		languages.addItem("Eiffel");
		languages.addItem("HTML");
		languages.addItem("IDL");
		languages.addItem("Java");
		languages.addItem("JavaScript");
		languages.addItem("Jess");
		languages.addItem("Kawa");
		languages.addItem("Lisp");
		languages.addItem("Patch");
		languages.addItem("Perl");
		languages.addItem("PHP");
		languages.addItem("Props");
		languages.addItem("Python");
		languages.addItem("Scheme");
		languages.addItem("ShellScript");
		languages.addItem("TeX");
		languages.addItem("XML");
		//$$ from Seb ]]

		/*if(filesTemp==null){
		  languages=new JComboBox();	
		  System.out.println("## Warning: No Syntax loaded");	
		  }
		  else	{
			languages = new JComboBox(filesTemp);
			languages.insertItemAt("None", 0);
			languages.setRenderer(new ModifiedCellRenderer());
		  }*/
		//	language.setBackground(Color.white);
		languages.addActionListener( this ) ;
		toolBar.addSeparator(new Dimension(50, 5));
		
		toolBar.add(languages);
		commandPanel.add(toolBar);
		getContentPane().add("North",commandPanel);

		// $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
		findReplace=new FindReplace(this ,2,true); // ou false à revoir & 0 pr initialiser (1==Find) (2==Replace)
		textAreas=new AbstractDoc[10];
       		textAreasCurrentSize=0;
		pane= new JTabbedPane();	
	
		// from PG [[
		left_indent = new LeftIndent();
		right_indent = new RightIndent();
		// from PG ]]
	
		//FROM OURY'S
		printer=new Print();
		templates = new CreateTemplate();
		
		boxComment = new BoxComment();
		boxUncomment = new BoxUncomment();  // from Seb
		upperCase = new ToUpperCase();
		lowerCase = new ToLowerCase();
		simpleComment = new SimpleComment();
		simpleUnComment = new SimpleUnComment();
		rmWhiteSpace = new RemoveWhitespace();
		
		split=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

		getContentPane().add(split, BorderLayout.CENTER);
		//split.setSize(600,520);
		split.setOneTouchExpandable(true);
		//split.setDividerLocation(100);
		pane.setPreferredSize(new Dimension(400,400));
 
		vTabbedPane =new JTabbedPane();
		split.setLeftComponent(vTabbedPane);
		
//		########################## MadKit	
		
	   outputArea = new JTextArea(";; EditorAgent 2.0 (c) Madkit team\n",10,40);
	   JScrollPane outscroller = new JScrollPane();
	   outscroller.setSize(400,100);
	   outscroller.getViewport().add(outputArea);
	   out = new PrintWriter(new JTextAreaWriter(outputArea), true);
	   
	   JSplitPane splitMainPanel=new JSplitPane(JSplitPane.VERTICAL_SPLIT);

	   splitMainPanel.setOneTouchExpandable(true);
	   //splitMainPanel.setDividerLocation(0.8);
	   splitMainPanel.setTopComponent(pane);
	   splitMainPanel.setBottomComponent(outscroller);
		
//		split.setRightComponent(pane);
	   split.setRightComponent(splitMainPanel);
		
//      ############################ MadKit
		
//		##############################
//		fileChooser = new JFileChooser(System.getProperty("user.dir",null));
		fileChooser = new JFileChooser(System.getProperty("madkit.dir",null));
		
		initProperties();
		
		optionsDialog = new OptionsDialog(this);
		findReplace = new FindReplace(this, 2, true); // ou false à revoir & 0 pr initialiser (1==Find) (2==Replace)
		findAll = new FindAll(this);

		jbrowser = new InsertClassName();
		beautifier = new Beautifier();

		vTabbedPane.addTab("FindAll", findAll);
		
		currentFile = null;

		//optionsDialog.show();
		//FROM OURY'S end

		
		
       
	
		// from Seb [[
		addAncestorListener(this);
		
		pane.addChangeListener( this );
		languages.setSelectedIndex(0);
		// from seb ]]
		
		show();
    }
    
    
	//[[PG
		public void applyTheme(String theme)
		{
			if(theme=="red")
			{	
				applyThemeIcon("red");return;	
			}
			if(theme=="blue")
			{	
				applyThemeIcon("blue");return;	
			}
			if(theme=="purple")
			{	
				applyThemeIcon("violet");return;	
			}
			if(theme=="gold")
			{	
				applyThemeIcon("yellow");return;	
			}
			if(theme=="metal")
			{	
				applyThemeIcon("metal");return;	
			}
			if(theme=="transpa")
			{	
				applyThemeIcon("transpa");return;	
			}
    			
		}
    	
    	
		public void applyThemeIcon(String theme)
		{
			iconeNewFile=theme+"/b_"+theme+"_new.gif";
			iconeOpenFile=theme+"/b_"+theme+"_open.gif";
			iconeSaveFile=theme+"/b_"+theme+"_save.gif";
			iconeSaveAllFile=theme+"/b_"+theme+"_saveAll.gif";
			iconeCutFile=theme+"/b_"+theme+"_cut.gif";
			iconeCopyFile=theme+"/b_"+theme+"_copy.gif";
			iconePasteFile=theme+"/b_"+theme+"_paste.gif";
			iconeUndoFile=theme+"/b_"+theme+"_undo.gif";
			iconeRedoFile=theme+"/b_"+theme+"_redo.gif";
			iconeSearchFile=theme+"/b_"+theme+"_search.gif";
			iconePropFile=theme+"/b_"+theme+"_props.gif";
			iconeCloseFile="close.gif";
			iconePrintFile=theme+"/b_"+theme+"_print02.gif";		
		}
		
    
    // from Oury [[
	/**
	 * Set a property.
	 * @param name Property's name
	 * @param value The value to store as <code>name</code>
	 */
   
	public static  void setProperty(String name, String value)
	{
	if (name == null || value == null)
		return;
	props.put(name, value);
	}
	
	/**
	  * If we store properties, we need to read them, too !
	  * @param name The name of the property to read
	  * @return The value of the specified property
	  */

	   public static String getProperty(String name)
	   {
			return props.getProperty(name);
	   }
	   
	/**
	 * Fetches a property, returning the default value if it's not
	 * defined.
	 * @param name The property
	 * @param def The default value
	 */

	public final static String getProperty(String name, String def) {
	  return props.getProperty(name, def);
	}
	
	
	public void propertiesChanged()
	{
		AbstractDoc doc;
		// we send the event to all the listeners available
		for (int i = 0; i < pane.getTabCount(); i++)
		{
			doc = (AbstractDoc) textAreas[i];
			doc.loadTextAreaProperties();
			doc.loadUndoProperties();
		}
	}
	
	
	/**
	 * Returns the property with the specified name, formatting it with
	 * the <code>java.text.MessageFormat.format()</code> method.
	 * @param name The property
	 * @param args The positional parameters
	 */

	public static final String getProperty(String name, Object[] args) {
	  if (name == null) {
		return null;
	  }

	  if (args == null) {
		return props.getProperty(name, name);
	  }
	  else {
		return MessageFormat.format(props.getProperty(name, name), args);
	  }
	}
	
	
	/**
	 * Returns true if the property value equals to "on" or "true"
	 * @param name The name of the property to read
	 */
	public static boolean getBooleanProperty(String name) {
	  String p = getProperty(name);
	  if (p == null) {
		return false;
	  }
	  else {
		return p.equals("on") || p.equals("true");
	  }
	}
	   
	   
	public void initProperties(){
	//		from Oury ]]
	props=new Properties();
	
	// comments properties...
	initCommentProperties("", "", "", "");

	// printOptions properies   valeurs à revoir...
	props.setProperty("print.pageOrientation","1");
	props.setProperty("print.pageWidth","10");
	props.setProperty("print.pageHeight","10");
	props.setProperty("print.pageImgX","10");
	props.setProperty("print.pageImgY","10");
	props.setProperty("print.pageImgWidth","10");
	props.setProperty("print.pageImgHeight","10");

	props.setProperty("print.font","10");
	props.setProperty("print.fontSize","10");
	// end

	// create templates properties
	setProperty("templates.input","Enter the value of {0}");
	setProperty("templates.title","Template input");
	//end

	//printing properties
	props.setProperty("print.printLineNumbers.label","Print line numbers");
	props.setProperty("print.wrapText.label","Wrap text");
	props.setProperty("print.printHeader.label","Print header");
	props.setProperty("print.printFooter.label","Print footer");
	props.setProperty("print.printSyntax.label","Print colors and styles");
	props.setProperty("print.pageLayout.label","Page Layout");
	// end 

	//EditorOption properties...
	props.setProperty("options.orientation.label","Split orientation");
	props.setProperty("options.linesintervalenabled.label","Enable interval highlighting");
	props.setProperty("options.wrapguideenabled.label","Enable wrap guide");
	props.setProperty("options.splitarea.label","Splits editor");
	props.setProperty("options.blockcaret.label","Block caret");
	props.setProperty("options.blinkingcaret.label","Blinking caret");
	props.setProperty("options.linehighlight.label","Line highlighting");
	props.setProperty("options.eolmarkers.label","End of line markers");
	props.setProperty("options.softtabs.label","Soft tabs");
	props.setProperty("options.tabindent.label","Indent on TAB");
	props.setProperty("options.enterindent.label","Indent on ENTER");
	props.setProperty("options.tabstop.label","Tab stops");
	props.setProperty("options.wordmove.go_over_space.label","Word jumping goes over spaces");
	props.setProperty("options.smartHomeEnd.label","Smart HOME/END");
	props.setProperty("options.defaultdirloaddialog.label","Default dir from active file");
	props.setProperty("options.selection.label","Use selection as search pattern");
	props.setProperty("options.extra_line_feed.label","Append an extra line feed on save");
	props.setProperty("options.line_end_preserved.label","Keep existing line separator on open");
	//end

	//StylesOptions...
 //    mod"Editor background", /*"editor.bgColor"*/"#ffffff");
//	   model.addColorChoice("Editor foreground", /*"editor.fgColor"*/"#000000");
//	   model.addColorChoice("Caret", /*"editor.caretColor"*/"#ff0000");
//	   model.addColorChoice("Selection", /*"editor.selectionColor"*/"#ccccff");
//	   model.addColorChoice("Highlight", /*"editor.highlightColor"*/"#ffff00");
//	   model.addColorChoice("Line highlight", /*"editor.lineHighlightColor"*/"#cdffcd");//e0ffe0");
//	   model.addColorChoice("Interval highlight", /*"editor.linesHighlightColor"*/"#e6e6ff");
//	   model.addColorChoice("Bracket highlight", /*"editor.bracketHighlightColor"*/"#00ff00");
//	   model.addColorChoice("Wrap guide", /*"editor.wrapGuideColor"*/"#ff0000");
//	   model.addColorChoice("EOL markers", /*"editor.eolMarkerColor"*/"#009999");
   
	props.setProperty("options.styles.bgColor", "Editor background");
	props.setProperty("options.styles.fgColor", "Editor foreground");
		props.setProperty("options.styles.caretColor", "Caret");
		props.setProperty("options.styles.selectionColor", "Selection");
	props.setProperty("options.styles.highlightColor", "Highlight");
	props.setProperty("options.styles.lineHighlightColor", "Line highlight");
	props.setProperty("options.styles.linesHighlightColor", "Interval highlight");
		props.setProperty("options.styles.bracketHighlightColor", "Bracket Highlight");
	props.setProperty("options.styles.wrapGuideColor", "Wrap guide");
		props.setProperty("options.styles.eolMarkerColor", "EOL markers");

//	   model.addStyleChoice("Comment 1", "color:#00ff00 style:i");///*"editor.style.comment1"*/"Comment 1");
//	   model.addStyleChoice("Comment 2", "color:#00ff00 style:i");///*"editor.style.comment2"*/"Comment 2");
//	   model.addStyleChoice("Literal 1 (chaîne)", "color:#00ffff");///*"editor.style.literal1"*/"String literal");
//	   model.addStyleChoice("Literal 2 (object)", "color:#650099");///*"editor.style.literal2"*/"Object literal");
//	   model.addStyleChoice("Label", "color:#cc00cc style:b");///*"editor.style.label"*/"Label");
//	   model.addStyleChoice("Keyword 1", "color:#0000ff style:b");///*"editor.style.keyword1"*/"Keyword 1");
//	   model.addStyleChoice("Keyword 2", "color:#ff9900");///*"editor.style.keyword2"*/"Keyword 2");
//	   model.addStyleChoice("Keyword 3", "color:#ff9900");///*"editor.style.keyword3"*/"Keyword 3");
//	   model.addStyleChoice("Operator", "color:#ffc800 style:b");///*"editor.style.operator"*/"Operator");
//	   model.addStyleChoice("Invalid token", "color:#666666 style:b");///*"editor.style.invalid"*/"Invalid token");
//	   model.addStyleChoice("Method", "color:#000000 style:b");///*"editor.style.method"*/"Method");

	props.setProperty("options.styles.comment1Style", "editor.style.comment1");
		props.setProperty("options.styles.comment2Style", "editor.style.comment2");
		props.setProperty("options.styles.literal1Style", "editor.style.literal1");
	props.setProperty("options.styles.literal2Style", "editor.style.literal2");
	props.setProperty("options.styles.labelStyle", "editor.style.label");
	props.setProperty("options.styles.keyword1Style", "editor.style.keyword1");
		props.setProperty("options.styles.keyword2Style", "editor.style.keyword2");
	props.setProperty("options.styles.keyword3Style", "editor.style.keyword3");
	props.setProperty("options.styles.operatorStyle", "editor.style.operator");
	props.setProperty("options.styles.invalidStyle", "editor.style.invalid");
	props.setProperty("options.styles.methodStyle", "editor.style.method");
	//end

	//beautifier properties...
	props.setProperty("beautifier.breakBracket", "true");
	props.setProperty("beautifier.indentSwitch", "true");
	props.setProperty("beautifier.preferredLineLength","30");
	props.setProperty("editor.softTab", "on");
	//ends

	//jbrowse properties JBrowseOptionPane.java
	props.setProperty("options.jbrowse.panel_label","Default Options for Future Sessions");
	//	props.setProperty("options.jbrowse.showStatusBar","Display status bar");
	//	props.setProperty("options.jbrowse.filterOptions","What to include");
	props.setProperty("options.jbrowse.showAttr","Attributes");
	props.setProperty("options.jbrowse.showPrimAttr","include");
	props.setProperty("options.jbrowse.showGeneralizations","extends/implements");
	props.setProperty("options.jbrowse.showThrows","Method throws type(s)");
	props.setProperty("options.jbrowse.visLevelLabel","Lowest Visibility Level to Show" );
	props.setProperty("options.jbrowse.topLevelVis","Top-Level:");
	props.setProperty("options.jbrowse.memberVis","Member:");
	props.setProperty("options.jbrowse.displayOptions","How to display");
	props.setProperty("options.jbrowse.showArgs","Arguments");
	props.setProperty("options.jbrowse.showArgNames","formal names");
	props.setProperty("options.jbrowse.showNestedName","Qualify nested");
	props.setProperty("options.jbrowse.showIconKeywords","Keywords specified by icons");
	props.setProperty("options.jbrowse.showMiscMod","Implementation detail modifiers");
	props.setProperty("options.jbrowse.alphaSort","Alpha Method Sort");
	props.setProperty("options.jbrowse.showLineNums","Line Numbers");
	props.setProperty("options.jbrowse.umlStyle","UML");
	props.setProperty("options.jbrowse.javaStyle","Java");
	props.setProperty("options.jbrowse.customStyle","Custom");
	props.setProperty("options.jbrowse.displayStyle","Display style:");
	
	/* Custom Display Style Options */
	props.setProperty("options.jbrowse.customOptions","Custom Display");
	props.setProperty("options.jbrowse.custVisAsSymbol","Use Visibility");
	props.setProperty("options.jbrowse.custAbsAsItalic","Abstract in italics");
	props.setProperty("options.jbrowse.custStaAsUlined","Static as underlined");
	props.setProperty("options.jbrowse.custTypeIsSuffixed","Type identifier as a suffix");
	// from Oury ]]
	
	//$$ from Seb [[
	// Open and Save dialogs
	props.setProperty("filechooser.lastdirectory." + LOAD, getHomeDirectory());
	props.setProperty("filechooser.lastdirectory." + SAVE, getHomeDirectory());
	
	// Undo component
	props.setProperty("options.undo.limit", "" + UndoComponent.MAX_UNDOLIMIT);
	props.setProperty("options.undo.sequence", "" + UndoComponent.SEQUENCE_DELAY);
	
	initPropertiesStylesOptions();
	// from Seb ]]
	}
	
	// from Seb [[
	
	// for fonts
private void initPropertiesStylesOptions()
{		
	// from jext-defs.props.xml file
	AbstractEditorPanel.setProperty("editor.wordmove.go_over_space", "on");
	AbstractEditorPanel.setProperty("editor.splitted.orientation", "Vertical");
	AbstractEditorPanel.setProperty("editor.autoScroll", "3");
	AbstractEditorPanel.setProperty("editor.antiAliasing", "off");
	AbstractEditorPanel.setProperty("editor.dirDefaultDialog", "on");
	AbstractEditorPanel.setProperty("editor.linesInterval", "5");
	AbstractEditorPanel.setProperty("editor.linesIntervalEnabled", "off");
	AbstractEditorPanel.setProperty("editor.linesHighlightColor", "#e6e6ff");
	AbstractEditorPanel.setProperty("editor.tabStop", "on");
	AbstractEditorPanel.setProperty("editor.font", "Monospaced");
	AbstractEditorPanel.setProperty("editor.fontSize", "12");
	AbstractEditorPanel.setProperty("editor.tabSize", "8");
	AbstractEditorPanel.setProperty("editor.tabIndent", "off");
	AbstractEditorPanel.setProperty("editor.enterIndent", "on");
	AbstractEditorPanel.setProperty("editor.blinkingCaret", "on");
	AbstractEditorPanel.setProperty("editor.softTab", "on");
	AbstractEditorPanel.setProperty("editor.saveSession", "on");
	AbstractEditorPanel.setProperty("editor.colorize.mode", "plain");
	AbstractEditorPanel.setProperty("editor.bgColor", "#ffffff");
	AbstractEditorPanel.setProperty("editor.fgColor", "#000000");
	AbstractEditorPanel.setProperty("editor.lineHighlight", "on");
	AbstractEditorPanel.setProperty("editor.lineHighlightColor", "#e0e0e0");
	AbstractEditorPanel.setProperty("editor.highlightColor", "#ffff00");
	AbstractEditorPanel.setProperty("editor.wrapGuideColor", "#ff0000");
	AbstractEditorPanel.setProperty("editor.wrapGuideOffset", "0");
	AbstractEditorPanel.setProperty("editor.wrapGuideEnabled", "off");
	AbstractEditorPanel.setProperty("editor.bracketHighlight", "on");
	AbstractEditorPanel.setProperty("editor.bracketHighlightColor", "#00ff00");
	AbstractEditorPanel.setProperty("editor.eolMarkers", "off");
	AbstractEditorPanel.setProperty("editor.eolMarkerColor", "#009999");
	AbstractEditorPanel.setProperty("editor.caretColor", "#ff0000");
	AbstractEditorPanel.setProperty("editor.selectionColor", "#ccccff");
	AbstractEditorPanel.setProperty("editor.blockCaret", "off");
	AbstractEditorPanel.setProperty("editor.line_end.preserve", "on");

	AbstractEditorPanel.setProperty("editor.style.comment1", "color:#009900 style:i");
	AbstractEditorPanel.setProperty("editor.style.comment2", "color:#009900 style:i");
	AbstractEditorPanel.setProperty("editor.style.keyword1", "color:#0000ff style:b");
	AbstractEditorPanel.setProperty("editor.style.keyword2", "color:#ff9900");
	AbstractEditorPanel.setProperty("editor.style.keyword3", "color:#ff0000");
	AbstractEditorPanel.setProperty("editor.style.label", "color:#cc00cc style:b");
	AbstractEditorPanel.setProperty("editor.style.literal1", "color:#9999ff");
	AbstractEditorPanel.setProperty("editor.style.literal2", "color:#650099 style:b");
	AbstractEditorPanel.setProperty("editor.style.operator", "color:#ffc800 style:b");
	AbstractEditorPanel.setProperty("editor.style.invalid", "color:#ff9900 style:b");
	AbstractEditorPanel.setProperty("editor.style.method", "color:#000000 style:b");
		
	AbstractEditorPanel.setProperty("buttons.highlightColor", "#c0c0d2");
}

	// for comments
	public void initCommentProperties( String block, String start, String end, String box )
	{		
		props.setProperty("blockComment", block);
		props.setProperty("commentStart", start);
		props.setProperty("commentEnd", end);
		props.setProperty("boxComment", box);
	}
// from Seb ]]

	
	
	/**
	 * Unsets (clears) a property.
	 * @param name The property
	 */
	public static void unsetProperty(String name) {
	   if (props.get(name) != null)
		 props.put(name, "");
	   else
		 props.remove(name);
	 }
	 
	 
	public JTabbedPane getVerticalTabbedPane() {
	  return vTabbedPane;
	}
	
	
	public void triggerTabbedPanes() {
	  vTabbedPane.addTab("Find All", findAll);
	}

	// from Oury ]]
    
    // from Seb [[
	public void ancestorAdded(AncestorEvent event)
	{
		/*System.out.println("ancestor");*/
		// récupère la fermeture
		//System.out.println(Utils.getRealFrameParent(this));
		Utils.getRealFrameParent(this).addWindowListener( new WindowListener() {
				public void windowActivated( WindowEvent e ) {}
				public void windowDeactivated( WindowEvent e ) {}
				public void windowClosed( WindowEvent e ) {/*System.out.println("closed");*/}
				public void windowIconified( WindowEvent e ) {/*System.out.println("icon");*/}
				public void windowDeiconified( WindowEvent e ) {}
				public void windowOpened( WindowEvent e) {}
				public void windowClosing( WindowEvent e )
				{
					//System.out.println("closing");
					AbstractDoc[] doc = getTextAreas();
					int i;
		
					for ( i = 0; i < textAreasCurrentSize; i++ )
						closeTabbedPane( i );
				}
			} );
	}
	public void ancestorMoved(AncestorEvent event) {}
	public void ancestorRemoved(AncestorEvent event) {/*System.out.println("removed");*/}
	
	
	/**
	 * Update the current textArea corresponding to current TabbedPane.
	 * @param e the event informing changes on the TabbedPane
	 */
	public void stateChanged( ChangeEvent e )
	{
		inputArea = (AbstractDoc)pane.getSelectedComponent();
		
		if (inputArea != null)
		{
			currentFile = inputArea.getCurrentFile();
			if ( currentFile != null )
				selectCorrectLanguage(currentFile);
			Utils.setFrameParentTitle(this, pane.getTitleAt(pane.getSelectedIndex()));
		}				
		else
		{
			languages.setSelectedIndex(0);
			currentFile = null;
			Utils.setFrameParentTitle(this, "");
		}
			
		if ( jbrowserEnabled )
		{
			jbrowser.reparse(inputArea);
		}
	}
	// from Seb ]]

	/**
	 * This method can determine if a String matches a pattern of wildcards
	 * @param pattern The pattern used for comparison
	 * @param string The String to be checked
	 * @return true if <code>string</code> matches <code>pattern</code>
	 */

	public static boolean match(String pattern, String string)
	{

		if ( string.endsWith ( pattern) ) {
			return true;
		}
		else return false;
  
	}

	/**
	 * Returns user directory.
	 */

	public static String getUserDirectory()
	{
		//#############################
		//return System.getProperty("user.dir");		
		return System.getProperty("madkit.dir")+File.separator+"plugins/jsynedit";
	}
	

	/*public static String[] getWildCardMatches(String path, String s, boolean sort)
	{
		if (s == null)
			return null;

		String files[];
		String filesThatMatch[];
		String args = new String(s.trim());
		ArrayList filesThatMatchVector = new ArrayList();
		int s_len = s.length();

		if (path == null)
			path = getUserDirectory();

		files = (new File(path)).list();
		if (files == null)
			return null;

		for (int i = 0; i < files.length; i++)
			{
			if (match(args, files[i]))
				{
					File temp = new File(getUserDirectory(), files[i]);

					filesThatMatchVector.add(new String(   temp.getName().substring(0,  temp.getName().length() - s_len) ) );
				}
			}


		Object[] o = filesThatMatchVector.toArray();
		filesThatMatch = new String[o.length];
		for (int i = 0; i < o.length; i++)
			filesThatMatch[i] = o[i].toString();
		o = null;
		filesThatMatchVector = null;

		if (sort)
			Arrays.sort(filesThatMatch);

		return filesThatMatch;
	}*/





	/// $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
    private int waitCount;
    /**
     * Shows the wait cursor.
     */
    
    public void showWaitCursor()
    {
		if (waitCount++ == 0)
			{
				Cursor cursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
				setCursor(cursor);
				AbstractDoc[] textAreas = getTextAreas();
				// DAVID : A CAUSE DE BUG A L'EXECUTION
				//for (int i = 0; i < textAreas.length; i++)
				//textAreas[i].getPainter().setCursor(cursor);
				// DAVID end
			}
    }
    
    /**
     * Hides the wait cursor.
     */
    public void hideWaitCursor()
    {
		if (waitCount > 0)
			waitCount--;
	
		if (waitCount == 0)
			{
				Cursor cursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
				setCursor(cursor);
				cursor = Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR);
				AbstractDoc[] textAreas = getTextAreas();
				// DAVID : A CAUSE DE BUG A L'EXECUTION
				//for(int i = 0; i < textAreas.length; i++)
				//    textAreas[i].getPainter().setCursor(cursor);
				// DAVID end
			}
	    
    }
	//  $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$   
    


    public void actionPerformed(ActionEvent e)
    {
		String c = e.getActionCommand();

		if ( c == null )
			return;
		
		command(c);
		// from Oury [[
		if (c.equals("browser")) {
		  if (!jbrowserEnabled) {
			split.setDividerLocation(150);
			split.setOneTouchExpandable(true);
			jbrowser.actionPerformed(e);
			jbrowserEnabled = !jbrowserEnabled;
			//  repaint();
		  }
		  else {
			split.setOneTouchExpandable(true);
			split.setDividerLocation(0);
			jbrowserEnabled = !jbrowserEnabled;
		  }
		}
		else if (c.equals("print")){
			printer.actionPerformed(e);
		}
		//##########################################
/*		else if (c.equals(getUserDirectory()+"/gnu/templates/Java/Class.java") ||
					c.equals(getUserDirectory()+"/gnu/templates/Java/Interface.java")||
					 c.equals(getUserDirectory()+"/gnu/templates/Java/Main.java") ||
					 c.equals(getUserDirectory()+"/gnu/templates/JSP/JSP.jsp") || 
					 c.equals(getUserDirectory()+"/gnu/templates/JSP/UseBean.jsp") ||
					 c.equals(getUserDirectory()+"/gnu/templates/PHP/File.php") ||
					 c.equals(getUserDirectory()+"/gnu/templates/PHP/pear.php") )
			templates.actionPerformed(e); */
		else if (c.equals(getUserDirectory()+"/templates/Java/Class.java") ||
					c.equals(getUserDirectory()+"/templates/Java/Interface.java")||
					 c.equals(getUserDirectory()+"/templates/Java/Main.java") ||
					 c.equals(getUserDirectory()+"/templates/Madkit/Agent.java"))
			templates.actionPerformed(e);
		else if (c.equals("uppercase")) {
		  upperCase.actionPerformed(e);
		}
		else if (c.equals("lowercase")) {
		  lowerCase.actionPerformed(e);
		}
		else if (c.equals("simplecomment")) {
		  simpleComment.actionPerformed(e);
		}
		else if (c.equals("simpleuncomment")) {
		  simpleUnComment.actionPerformed(e);
		}
		else if (c.equals("left_indent")) {
			left_indent.actionPerformed(e);
		}
		else if (c.equals("right_indent")) {
			right_indent.actionPerformed(e);
		}
		else if (c.equals("boxcomment")) {
		  boxComment.actionPerformed(e);
		}
		else if (c.equals("boxuncomment")) {    // from Seb
		  boxUncomment.actionPerformed(e);
		}
		else if (c.equals("removewhitespace")) {
		  rmWhiteSpace.actionPerformed(e);
		}
		else if (c.equals("beautifier")) {
		  beautifier.actionPerformed(e);
		}
		// from Oury ]]
    }

    public void command(String c){
		if (c.equals("open")) openFile();
        else if (c.equals("new")) newFile();
		else if (c.equals("save")) save();
		else if (c.equals("saveAll")) saveAll();
		else if (c.equals("copy")) inputArea.copy();
		else if (c.equals("cut")) inputArea.cut();
		else if (c.equals("paste")) inputArea.paste();
		else if (c.equals("saveAs")) saveAs();
		else if (c.equals("clear")) clear();
		else if (c.equals("close")) closeCurentTabbedPane();
		else if (c.equals("undo")) inputArea.undo();
		else if (c.equals("redo")) inputArea.redo();
		// DAVID
		else if (c.equals("optGen")) optionsDialog.show();
		else if (c.equals("optPol")) optionsDialog.show();
		else if (c.equals("optS&C")) optionsDialog.show();
		// DAVID end
		else if(c.equals("find"))findReplace.show();
		else if(c.equals("quit")) quit();
		else if ( c.equals("comboBoxChanged"))
		{
			if ( inputArea !=null )
			{
				chooseTokenMarker( (String)languages.getSelectedItem() );
				inputArea.repaint();
			}
		}
		else if (c.equals("find")) {
		  //setEnabled(false);
		  findReplace.show();
		}
		else if (c.equals("replace")) {
		  //  findReplace.setType(2);
		  findReplace.show();
		}
		else if (c.equals("preferences")) {
		  optionsDialog.show();
		}
		else if (c.equals("findall")) {
		  split.setDividerLocation(150);
		  split.setOneTouchExpandable(true);
		  vTabbedPane.setSelectedComponent(findAll);
		}				  
    }

	public void closeCurentTabbedPane()
	{
		int c_index;
		c_index=pane.getSelectedIndex();
		closeTabbedPane( c_index );
	}
	
	public void closeTabbedPane( int id )
	{
		if(inputArea.undo.getTextModified()==true)
			{
				int  res;
				String title=pane.getTitleAt(id);
				res = JOptionPane.showConfirmDialog(this,
													"Save changes to "+title+" ?",
													"Warning",
													JOptionPane.YES_NO_CANCEL_OPTION,
													JOptionPane.QUESTION_MESSAGE);
				if ( res == JOptionPane.CANCEL_OPTION )
					return;
				else	if ( res == JOptionPane.YES_OPTION )
					save();
			}

		pane.remove(id);
		textAreasCurrentSize--;
		stateChanged( new ChangeEvent(pane) );
	}

    public void clearOutputArea(){
    	outputArea.setText("");
    }
    
	public void newFile()
	{
		String tabPaneTittle="Untitled - "+cpt;
		JComponent panel= new JPanel(false); 
		inputArea=textAreas[textAreasCurrentSize]=new AbstractDoc();
		inputArea.loadTextAreaProperties();
		inputArea.loadUndoProperties();
		enableShortcuts(inputArea);
		// inputArea.setTokenMarker(new JavaTokenMarker());  // A AMELIORER  String s=Utils.getExtension(f);
		/*inputArea.setTokenMarker(new JavaTokenMarker());
		inputArea.setTokenMarker(new CCTokenMarker());*/
		inputArea.setEditor(this); // the actions need to know the editor
		pane.addTab(tabPaneTittle,inputArea);
		pane.setSelectedIndex(textAreasCurrentSize);
		textAreasCurrentSize++;	 	   
		cpt++;
	}
    //String dirName;
    //String fileName;

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
    
    
    // from Oury [[
	public static String getHomeDirectory()
	{
		return System.getProperty("user.dir");
	}
	// from Oury ]]
	
	
    protected String extens=null;
    public void setExtens(String ext){extens = ext;}

		
	//$$ SEB : créer inputArea ici : pas propre
    protected boolean getFileDialog(boolean direction, String title, String extens){
    	//$$ modif from Seb [[
		JFileChooser fd;
		//JFileChooser fd = new JFileChooser(System.getProperty("user.dir",null));
		fd = new JFileChooser( getProperty( "filechooser.lastdirectory." + direction ) );
		//$$ modif from Seb ]]
		if (extens != null){
			fd.setFileFilter(new SwingFileFilter(extens));
		}
		fd.setDialogTitle(title);
		
		int retval=0;
		if (direction == LOAD){
			//fd.setDialogType(JFileChooser.OPEN_DIALOG);
			retval = fd.showOpenDialog(this);
		} else {
			//fd.setDialogType(JFileChooser.SAVE_DIALOG);
			if ( currentFile == null )
				fd.setSelectedFile(new File( pane.getTitleAt(pane.getSelectedIndex()) ));
			else fd.setSelectedFile(new File(inputArea.getCurrentFile()));
			retval = fd.showSaveDialog(this);
		}
		if (retval == JFileChooser.APPROVE_OPTION)	{
			if (fd.getSelectedFile()!=null)
                {
                    currentFile = fd.getSelectedFile().getPath();
                    // from Seb [[
					setProperty( "filechooser.lastdirectory." + direction, fd.getSelectedFile().getParent() );
                    // from Seb ]]
                    return(true);
                }
			else
			{
				return false;
			}
		} else
			return(false);
    }

    
    // from Seb [[
    public void openFile( String filename )
    {
    	currentFile = filename;
    	openFile();
    }
    // from Seb ]]
    
    public void openFile()
    {		
		if ( !getFileDialog(LOAD,"Open file",extens) )
			return;

		if ( readFile(currentFile) )
			inputArea.undo.reset();
    }

	public String getExtension(String s) {
        String ext = null;
		// String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }

        return ext;
    }

    public String chooseTokenMarker(String ext) {
		if (ext == null)
			return "";
			
		initCommentProperties("", "", "", "");
		
		if (ext.equalsIgnoreCase("bat") || ext.equalsIgnoreCase("BatchFile")) {
			((AbstractDoc) pane.getSelectedComponent()).setTokenMarker(new BatchFileTokenMarker());
			initCommentProperties("rem", "", "", "");
			return "BatchFile";
		}
		else if (ext.equalsIgnoreCase("c") || ext.equalsIgnoreCase("h")) {
			((AbstractDoc) pane.getSelectedComponent()).setTokenMarker(new CTokenMarker());
			initCommentProperties("//", "/*", "*/", "*");
			return "C";
		}
		else if (ext.equalsIgnoreCase("cc") || ext.equalsIgnoreCase("cpp") || ext.equalsIgnoreCase("h")) {
			((AbstractDoc) pane.getSelectedComponent()).setTokenMarker(new CCTokenMarker());
			initCommentProperties("//", "/*", "*/", "*");
			return "CC";
		}

		else if (ext.equalsIgnoreCase("cc") || ext.equalsIgnoreCase("Eiffel")) {
			((AbstractDoc) pane.getSelectedComponent()).setTokenMarker(new EiffelTokenMarker());
			initCommentProperties("--", "", "", "");
			return "Eiffel";
		}
		else if (ext.equalsIgnoreCase("html") || ext.equalsIgnoreCase("htm")) {
			((AbstractDoc) pane.getSelectedComponent()).setTokenMarker(new HTMLTokenMarker());
			return "HTML";
		}
		else if (ext.equalsIgnoreCase("idl")) {
			/*
			  .IDL - Devdocs dans Snappy
			  - Interface Definition Library - source Visual C++
			*/
			((AbstractDoc) pane.getSelectedComponent()).setTokenMarker(new IDLTokenMarker());
			initCommentProperties("//", "/*", "*/", "*");
			return "IDL";
		}

		else if (ext.equalsIgnoreCase("java") || ext.equalsIgnoreCase("bsh")) {
			/*((AbstractDoc) pane.getSelectedComponent())*/inputArea.setTokenMarker(new JavaTokenMarker());
			initCommentProperties("//", "/*", "*/", "*");
			return "Java";
		}
		else if (ext.equalsIgnoreCase("js") || ext.equalsIgnoreCase("JavaScript")) {
			((AbstractDoc) pane.getSelectedComponent()).setTokenMarker(new JavaScriptTokenMarker());
			initCommentProperties("//", "/*", "*/", "*");
			return "JavaScript";
		}
		else if (ext.equalsIgnoreCase("lsp") || ext.equalsIgnoreCase("lisp")) {
			((AbstractDoc) pane.getSelectedComponent()).setTokenMarker(new LispTokenMarker());
			initCommentProperties(";", "#|", "|#", "#");
			return "Lisp";
		}
		else if (ext.equalsIgnoreCase("p56") || ext.equalsIgnoreCase("Patch")) { // .P56 Patch
			((AbstractDoc) pane.getSelectedComponent()).setTokenMarker(new PatchTokenMarker());
			return "Patch";
		}
		else if (ext.equalsIgnoreCase("pbp") || ext.equalsIgnoreCase("Perl")) { // .PBP Fichier Perl Builder
			((AbstractDoc) pane.getSelectedComponent()).setTokenMarker(new PerlTokenMarker());
			initCommentProperties("#", "", "", "");
			return "Perl";
		}
		else if (ext.equalsIgnoreCase("php") || ext.equalsIgnoreCase("php3")) { // .Php
			((AbstractDoc) pane.getSelectedComponent()).setTokenMarker(new PHPTokenMarker());
			initCommentProperties("//", "/*", "*/", "*");
			return "PHP";
		}
		else if (ext.equalsIgnoreCase("prop") || ext.equalsIgnoreCase("props")) {
			((AbstractDoc) pane.getSelectedComponent()).setTokenMarker(new PropsTokenMarker());
			initCommentProperties("#", "", "", "");
			return "Props";
		}
		else if (ext.equalsIgnoreCase("py") || ext.equalsIgnoreCase("Python")) { // Python
			((AbstractDoc) pane.getSelectedComponent()).setTokenMarker(new PythonTokenMarker());
			initCommentProperties("#", "", "", "");
			return "Python";
		}
		else if (ext.equalsIgnoreCase("ss") || ext.equalsIgnoreCase("sch") ||
					ext.equalsIgnoreCase("Scheme")) { //  Fichier SCHEME
			((AbstractDoc) pane.getSelectedComponent()).setTokenMarker(new SchemeTokenMarker());
			// Scheme files (*.ss,*.scm)|*.ss;*.scm
			initCommentProperties(";", "#|", "|#", "#");
			return "Scheme";
		}
		else if (ext.equalsIgnoreCase("sh") || ext.equalsIgnoreCase("ShellScript")) { // ShellScript
			((AbstractDoc) pane.getSelectedComponent()).setTokenMarker(new ShellScriptTokenMarker());
			initCommentProperties("#", "", "", "");
			return "ShellScipt";
		}
		//else if (ext.equalsIgnoreCase("sql") || ext.equalsIgnoreCase("TSQL")) { // TSQL
		  //((AbstractDoc) pane.getSelectedComponent()).setTokenMarker(new TSQLTokenMarker());
		  //initCommentProperties("--", "/*", "*/", "*");
		  //return "TSQL";
		 //}
		else if (ext.equalsIgnoreCase("tex")) { // TeX 
			((AbstractDoc) pane.getSelectedComponent()).setTokenMarker(new TeXTokenMarker());
			initCommentProperties("%", "", "", "");
			return "TeX";
		}
		else if (ext.equalsIgnoreCase("xml") || ext.equalsIgnoreCase("cfg") 
					|| ext.equalsIgnoreCase("ini") || ext.equalsIgnoreCase("fml")) { // XML
			((AbstractDoc) pane.getSelectedComponent()).setTokenMarker(new XMLTokenMarker());
			initCommentProperties("", "<!--", "-->", "");
			return "XML";
		}
		else if (ext.equalsIgnoreCase("scm")) {
			((AbstractDoc) pane.getSelectedComponent()).setTokenMarker(new KawaTokenMarker());
			initCommentProperties(";", "#|", "|#", "#");
			return "Kawa";
		} // MODIF KENDY
		else if (ext.equalsIgnoreCase("clp") || ext.equalsIgnoreCase("jess")) {
			((AbstractDoc) pane.getSelectedComponent()).setTokenMarker(new JessTokenMarker());
			initCommentProperties(";", "#|", "|#", "#");
			return "Jess";
		} // MODIF KENDY	
		else {
			// ((AbstractDoc) pane.getSelectedComponent()).setTokenMarker(null);
			((AbstractDoc) pane.getSelectedComponent()).setTokenMarker(null);
			return ext;
		}
    }
    
    
    public void selectCorrectLanguage(String filename)
    {
		String ext, lang;
		
		ext = getExtension(filename);
		lang = chooseTokenMarker(ext);
		
		for ( int i = 1; i < languages.getItemCount(); i++ )
			if ( ((String)languages.getItemAt(i)).equals(lang) )
			{
				languages.setSelectedIndex(i);
				break;
			}
			else languages.setSelectedIndex(0);
    }


    public boolean readFile(String s){
        return readFile(new File(s));
    }

    public boolean readFile(File f){
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
			// $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
			/* Si le fichier n'est pas déjà ouvert ,si c est le cas pas besoin de le 
			 * reinserrer dans le tableau pane
			 */
			String fname=f.getName();
			for (int i=0;i<textAreasCurrentSize;i++){
				if(fname.equals(pane.getTitleAt(i))){
					pane.setSelectedIndex(i);
					ds.close();
					return false;
				}
			}
			/******************************************/
			inputArea=textAreas[textAreasCurrentSize]=new AbstractDoc();
			inputArea.loadTextAreaProperties();
			inputArea.loadUndoProperties();
			enableShortcuts(inputArea);
			inputArea.setName(fname); // jbrowser needs to know the filename
			inputArea.setEditor(this); // the actions need to know the editor
			// $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$

			inputArea.setCurrentFile( f.getPath() );
			inputArea.setText(text); // supprime l'ancien texte
			inputArea.setCaretPosition(0);
			//    Utils.setFrameParentTitle(this,f.getName());
			
			pane.addTab(fname,inputArea);
	    
			pane.setSelectedIndex(textAreasCurrentSize);
			textAreasCurrentSize++;
			
			ds.close();
		}
		catch (Exception err) {
			System.out.println("Cannot open file " + f.getName() );
		}
		
		return true;
    }

    void saveIfModified(){
		if (!(inputArea.getText().equals(""))){
			int  result;
			result = JOptionPane.showConfirmDialog(this,
												   "Buffer is modified, do you want to save it ?",
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
    	if (inputArea == null)
    		return;
    		
		if (getFileDialog(SAVE,"Save file",extens))
		{
			// from Seb [[
			// save1(true);
			inputArea.setCurrentFile( currentFile );
			save1(true);
			pane.setTitleAt( pane.getSelectedIndex(), new File(currentFile).getName() );
			selectCorrectLanguage(currentFile);
			// from Seb ]]
		}
    }

	public void saveAll()
	{
		int i;
		int n;
		
		n = pane.getComponentCount();
		
		for (i = 0; i < n; i++ )
		{
			pane.setSelectedIndex(i);
			save();
		}
	}

    public void save() {
    	if (inputArea == null)
    		return;
		if (inputArea.getCurrentFile() == null)
			saveAs();
		else
			save1(true);
    }


    void save1(boolean ok) {
		if (ok) {
			try{
				PrintWriter outFile = new PrintWriter(new FileWriter(inputArea.getCurrentFile()));
				String content = inputArea.getText();
				outFile.print(content);
				outFile.flush();
				outFile.close();
			} catch (FileNotFoundException e) {
				System.err.println("saveContent: " + e);
			} catch (IOException e) {
				System.err.println("saveContent: " + e);
			}
			//on met la variable TextModified a false
			inputArea.undo.setTextModified(false);
		}
    }


    public void clear(){
     	clearOutputArea();
    }
    
    
    public void quit()
	{
    	System.exit(0);
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
		b.addActionListener(this/*
							new ActionListener(){
								public void actionPerformed(ActionEvent e) {
									command(e.getActionCommand());
								}
							}*/);
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


	//$$ from Seb for Shortcuts [[
	public void enableShortcuts( AbstractDoc doc )
	{
		InputHandler ih;
		
		ih = doc.getInputHandler();
		
		// File menu
		ih.addKeyBinding("C+N", this, "new");
		ih.addKeyBinding("C+O", this, "open");
		ih.addKeyBinding("C+S", this, "save");
		ih.addKeyBinding("C+P", this, "print");
		ih.addKeyBinding("C+W", this, "close");
			ih.addKeyBinding("C+F4", this, "close");
		ih.addKeyBinding("C+Q", this, "quit");
			ih.addKeyBinding("A+X", this, "quit");
			ih.addKeyBinding("A+F4", this, "quit");
			
		// Edit menu
		ih.addKeyBinding("C+Z", this, "undo");
		ih.addKeyBinding("C+Y", this, "redo");
		ih.addKeyBinding("C+X", this, "cut");
			ih.addKeyBinding("S+DELETE", this, "cut");
		ih.addKeyBinding("C+C", this, "copy");
			ih.addKeyBinding("C+INSERT", this, "copy");
		ih.addKeyBinding("C+V", this, "paste");
			ih.addKeyBinding("S+INSERT", this, "paste");
		ih.addKeyBinding("C+F", this, "find");
			ih.addKeyBinding("F3", this, "find");
		ih.addKeyBinding("C+R", this, "replace");
		ih.addKeyBinding("C+LEFT", this, "left_indent");
		ih.addKeyBinding("C+RIGHT", this, "right_indent");
		ih.addKeyBinding("C+UP", this, "uppercase");
		ih.addKeyBinding("C+DOWN", this, "lowercase");
		
		// Options menu
		ih.addKeyBinding("C+O", this, "optGen");
			ih.addKeyBinding("F10", this, "optGen");
	}
	//$$ from Seb for Shortcuts ]]
	
	//$$ from Seb [[
	public String getCurrentFile()
	{
		return currentFile;
	}
	
	public void newBuffer( String text )
	{
		System.out.println("buffer");
		newFile();
		inputArea.setText( text );
	}
	//$$ from Seb ]]
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
