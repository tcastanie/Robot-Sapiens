/*
 * 03/01/2002 - 19:53:59
 *
 * FindReplace.java - The Jext's find dialog
 * Copyright (C) 1998-2001 Romain Guy
 * romain.guy@jext.org
 * www.jext.org
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

//package org.jext.search;



package gnu.search;

import gnu.*;
import gnu.gui.*;
import gnu.regexp.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


//import org.jext.*;
//import org.jext.gui.*;

//$$ add [[
import jedit.textarea.JEditTextArea;
import jsynedit.AbstractEditorPanel;
import gnu.gui.AbstractDisposer;
//$$ add ]] 

/**
 * The <code>FindReplace</code> class is a component which displays
 * a dialog for either finding either replacing text. It provides
 * two combo lists, which holds latest patterns, and many buttons
 * or check boxes for options.
 * @author Romain Guy
 */

public class FindReplace extends JDialog implements ActionListener
{
  // Constant declarations
  /** Defines a search only dialog */
  public static final int SEARCH = 1;
  /** Defines a search and replace dialog */
  public static final int REPLACE = 2;

  // Private declarations
  private int type;
  private /*JextFrame*/AbstractEditorPanel parent;
  private JComboBox fieldSearch;
  private JComboBox fieldReplace;
  private JTextField fieldSearchEditor, fieldReplaceEditor, script;
  private JextHighlightButton btnFind, btnReplace, btnReplaceAll, btnCancel;
  private JextCheckBox checkIgnoreCase, saveStates, useRegexp, allFiles, scripted;

    //$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
    // public void setType(int t){ type=t;} //$$$$
    // $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$


  // This method is used to easily build the GridBagLayout

  private void buildConstraints(GridBagConstraints agbc, int agx, int agy, int agw, int agh,
                                int awx, int awy)
  {
    agbc.gridx = agx;
    agbc.gridy = agy;
    agbc.gridwidth = agw;
    agbc.gridheight = agh;
    agbc.weightx = awx;
    agbc.weighty = awy;
    agbc.insets = new Insets(2, 2, 2, 2);
  }

  /**
   * Constructs a new find dialog according to the specified
   * type of dialog requested. The dialog can be either a 
   * FIND dialog, either a REPLACE dialog. In both cases, components
   * displayed remain the sames, but the ones specific to replace
   * feature are grayed out.
   * @param parent The window holder
   * @param type The type of the dialog: <code>FindReplace.FIND</code>
   *             or <code>FindReplace.REPLACE</code>
   * @param modal Displays dialog as a modal window if true
   */

    public FindReplace(/*JextFrame*/AbstractEditorPanel parent, int type, boolean modal)
  {
      //  super(parent, type == REPLACE ? /*Jext.getProperty("replace.title")*/"Replace" :
      //    /*Jext.getProperty("find.title")*/"Find", modal);

      // $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
      /* PROBLEME: comment fixer le dialogue au dessus de l'interface ??? */
      this.parent = parent;
      this.type = type;
      
      setTitle(type==REPLACE?"Replace":"Find"); 
      //setRootPane(parent);

	 // $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
    fieldSearch = new JComboBox();
    fieldSearch.setRenderer(new ModifiedCellRenderer());
    fieldSearch.setEditable(true);
    fieldReplace = new JComboBox();
    fieldReplace.setRenderer(new ModifiedCellRenderer());
    fieldReplace.setEditable(true);
    KeyHandler handler = new KeyHandler();
    fieldSearchEditor = (JTextField) fieldSearch.getEditor().getEditorComponent();
    fieldSearchEditor.addKeyListener(handler);
    fieldReplaceEditor = (JTextField) fieldReplace.getEditor().getEditorComponent();
    fieldReplaceEditor.addKeyListener(handler);

    GridBagLayout gridbag = new GridBagLayout();
    GridBagConstraints constraints = new GridBagConstraints();
    getContentPane().setLayout(gridbag);
    ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

    JLabel findLabel = new JLabel(/*Jext.getProperty("find.label")*/"Find label");
    buildConstraints(constraints, 0, 0, 1, 1, 25, 50);
    constraints.anchor = GridBagConstraints.WEST;
    gridbag.setConstraints(findLabel, constraints);
    getContentPane().add(findLabel);

    buildConstraints(constraints, 1, 0, 1, 1, 25, 50);
    constraints.fill = GridBagConstraints.HORIZONTAL;
    constraints.anchor = GridBagConstraints.CENTER;
    gridbag.setConstraints(fieldSearch, constraints);
    getContentPane().add(fieldSearch);

    btnFind = new JextHighlightButton(/*Jext.getProperty("find.button")*/"Find");
    btnFind.setToolTipText(/*Jext.getProperty("find.tip")*/"find.tip");
    //    btnFind.setMnemonic(Jext.getProperty("find.mnemonic").charAt(0));
    btnFind.addActionListener(this);
    buildConstraints(constraints, 2, 0, 1, 1, 25, 50);
    constraints.anchor = GridBagConstraints.CENTER;
    gridbag.setConstraints(btnFind, constraints);
    getContentPane().add(btnFind);
    getRootPane().setDefaultButton(btnFind);

    btnCancel = new JextHighlightButton(/*Jext.getProperty("general.cancel.button")*/"Cancel");
    //    btnCancel.setMnemonic(Jext.getProperty("general.cancel.mnemonic").charAt(0));
    btnCancel.addActionListener(this);
    buildConstraints(constraints, 3, 0, 1, 1, 25, 50);
    constraints.anchor = GridBagConstraints.CENTER;
    gridbag.setConstraints(btnCancel, constraints);
    getContentPane().add(btnCancel);

    JLabel replaceLabel = new JLabel(/*Jext.getProperty("replace.label")*/"Replace with");
    buildConstraints(constraints, 0, 1, 1, 1, 25, 50);
    constraints.anchor = GridBagConstraints.WEST;
    gridbag.setConstraints(replaceLabel, constraints);
    getContentPane().add(replaceLabel);

// patch added by gandalf march 25 2003
    if (type != REPLACE)
      replaceLabel.setEnabled(false);
// patch added by gandalf march 25 2003

    buildConstraints(constraints, 1, 1, 1, 1, 25, 50);
    constraints.fill = GridBagConstraints.HORIZONTAL;
    constraints.anchor = GridBagConstraints.CENTER;
    gridbag.setConstraints(fieldReplace, constraints);
    getContentPane().add(fieldReplace);

    if (type != REPLACE)
      fieldReplace.setEnabled(false);
    btnReplace = new JextHighlightButton(/*Jext.getProperty("replace.button")*/"Replace");
    btnReplace.setToolTipText(/*Jext.getProperty("replace.tip")*/"replace.tip");
//    btnReplace.setMnemonic(Jext.getProperty("replace.mnemonic").charAt(0));
    if (type != REPLACE)
      btnReplace.setEnabled(false);
    btnReplace.addActionListener(this);
    buildConstraints(constraints, 2, 1, 1, 1, 25, 50);
    constraints.anchor = GridBagConstraints.CENTER;
    gridbag.setConstraints(btnReplace, constraints);
    getContentPane().add(btnReplace);

    btnReplaceAll = new JextHighlightButton(/*Jext.getProperty("replace.all.button")*/"Replace All");
    btnReplaceAll.setToolTipText(/*Jext.getProperty("replace.all.tip")*/"replace.all.tip");
//    btnReplaceAll.setMnemonic(Jext.getProperty("replace.all.mnemonic").charAt(0));
    if (type != REPLACE)
      btnReplaceAll.setEnabled(false);
    btnReplaceAll.addActionListener(this);
    buildConstraints(constraints, 3, 1, 1, 1, 25, 50);
    constraints.anchor = GridBagConstraints.CENTER;
    gridbag.setConstraints(btnReplaceAll, constraints);
    getContentPane().add(btnReplaceAll);

    scripted = new JextCheckBox(/*Jext.getProperty("replace.script")*/"replace.script", Search.getPythonScript());
    if (type != REPLACE)
      scripted.setEnabled(false);
    else
    {
      fieldReplace.setEnabled(!scripted.isSelected());
      scripted.addActionListener(this);
    }
    buildConstraints(constraints, 0, 2, 1, 1, 50, 50);
    constraints.anchor = GridBagConstraints.WEST;
    gridbag.setConstraints(scripted, constraints);
    getContentPane().add(scripted);

    script = new JTextField();
    if (type != REPLACE)
      script.setEnabled(false);
    else
      script.setEnabled(scripted.isSelected());
    script.setText(Search.getPythonScriptString());
    buildConstraints(constraints, 1, 2, 1, 1, 50, 50);
    constraints.anchor = GridBagConstraints.CENTER;
    gridbag.setConstraints(script, constraints);
    getContentPane().add(script);

    checkIgnoreCase = new JextCheckBox(/*Jext.getProperty("find.ignorecase.label")*/"Ignore Case", Search.getIgnoreCase());
    buildConstraints(constraints, 0, 3, 1, 1, 25, 50);
    constraints.anchor = GridBagConstraints.WEST;
    gridbag.setConstraints(checkIgnoreCase, constraints);
    getContentPane().add(checkIgnoreCase);

    JPanel cPane = new JPanel();
    saveStates = new JextCheckBox(/*Jext.getProperty("find.savevalues.label")*/"Save Values",
				  /* Jext.getBooleanProperty("savestates")*/true);
    allFiles = new JextCheckBox(/*Jext.getProperty("find.allFiles.label")*/"In All Files",
				/*Jext.getBooleanProperty("allfiles")*/false);
    cPane.add(saveStates);
    cPane.add(allFiles);

    buildConstraints(constraints, 1, 3, 1, 1, 25, 50);
    constraints.anchor = GridBagConstraints.WEST;
    gridbag.setConstraints(cPane, constraints);
    getContentPane().add(cPane);

    useRegexp = new JextCheckBox(/*Jext.getProperty("find.useregexp.label")*/"Use Extended RegExp", Search.getRegexp());
    buildConstraints(constraints, 2, 3, 2, 1, 50, 50);
    constraints.anchor = GridBagConstraints.WEST;
    gridbag.setConstraints(useRegexp, constraints);
    getContentPane().add(useRegexp);

    load();

    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    addKeyListener(new AbstractDisposer(this));
    addWindowListener(new WindowAdapter()
    {
      public void windowClosing(WindowEvent e)
      {
	  exit();
      }
    });

    /*   $$$$$$$$$$$$$$$$$$$$$$$$
     FontMetrics fm = getFontMetrics(getFont());
    fieldSearch.setPreferredSize(new Dimension(18 * fm.charWidth('m'),
                                 (int) fieldSearch.getPreferredSize().height));
    fieldReplace.setPreferredSize(new Dimension(18 * fm.charWidth('m'),
                                  (int) fieldReplace.getPreferredSize().height));
    */


    pack();
    setResizable(false);
    UUtilities.centerComponentChild(parent, this);

//patch by MJB 8/1/2002		
        btnFind.addKeyListener(handler);
	btnReplace.addKeyListener(handler);
	btnReplaceAll.addKeyListener(handler);
	btnCancel.addKeyListener(handler);
	checkIgnoreCase.addKeyListener(handler);
	saveStates.addKeyListener(handler);
	useRegexp.addKeyListener(handler);
	allFiles.addKeyListener(handler);
	scripted.addKeyListener(handler);
	script.addKeyListener(handler);
//end MJB patch
    

/*   $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
     $$$*/  setLocation (50,50);   //$$$$$$$$
/*   $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
 */
	//       show();
  }

  // load the search and replace histories from user
  // properties. It also selects latest pattern from
  // the list.

    

    private void load()
    {
	String s;
	/*	for (int i = 0; i < 25; i++)
	    {
	    s = /*Jext.getProperty("search.history." + i/"search.history." + i;
		if (s != null)
		    fieldSearch.addItem(s);
		else
		    break;
	    }
	*/
	JEditTextArea textArea = parent.getTextArea();
	if (true/*!Jext.getBooleanProperty("use.selection")*/)
	    {
		s = Search.getFindPattern();
		if (s != null)
		    {
			addSearchHistory(s);
			fieldSearch.setSelectedItem(s);
		    }
	    } else if ((s = textArea.getSelectedText()) != null) {

		char c = '\0';
		StringBuffer buf = new StringBuffer(s.length());
		out:  for (int i = 0; i < s.length(); i++)
		    {
			switch (c = s.charAt(i))
			    {
			    case '\n':
				break out;
			    default:
				buf.append(c);
			    }
		    }
		
		s = buf.toString();
		addSearchHistory(s);
		fieldSearch.setSelectedItem(s);
	    }
	
	if (type == REPLACE)
	    {
		/*	for (int i = 0; i < 25; i++)
		    {
			s = /*Jext.getProperty("replace.history." + i)/"replace.history."+i;
			if (s != null)
			    fieldReplace.addItem(s);
			else
			    break;
		    }
		*/
		s = Search.getReplacePattern();
		if (s != null)
		    {
			addReplaceHistory(s);
			fieldReplace.setSelectedItem(s);
		    }
	    }
	
	// selects contents
	fieldSearchEditor.selectAll();
    }
    
  // exits the dialog after having saved the search and
  // replace histories.

   
    private void exit(){
	/*$$$$$$$$$$$$$$$$$$$$$$$$$
	  
	  if (saveStates.isSelected())
	    {
	    for (int i = 0; i < fieldSearch.getItemCount(); i++)
	    Jext.setProperty("search.history." + i, (String) fieldSearch.getItemAt(i));
	    for (int i = fieldSearch.getItemCount(); i < 25; i++)
	    Jext.unsetProperty("search.history." + i);
	    
	    if (type == REPLACE)
	    {
	    for (int i = 0; i < fieldReplace.getItemCount(); i++)
	    Jext.setProperty("replace.history." + i, (String) fieldReplace.getItemAt(i));
	    for (int i = fieldReplace.getItemCount(); i < 25; i++)
	    Jext.unsetProperty("replace.history." + i);
	    }
	    }
	    
	    Jext.setProperty("savestates", (saveStates.isSelected() ? "on" : "off"));
	    Jext.setProperty("allfiles", (allFiles.isSelected() ? "on" : "off"));
	    
	*/
	// patch added by gandalf march 25 2003
	Search.setIgnoreCase(checkIgnoreCase.isSelected() ? true : false);
	Search.setRegexp(useRegexp.isSelected() ? true : false);
	// patch added by gandalf march 25 2003
       	
	dispose();
    }


  // adds current search pattern in the search history list

  private void addSearchHistory()
  {
    addSearchHistory(fieldSearchEditor.getText());
  }

  // adds a pattern in the search history list
  // the pattern to be added is specified by the param c

  private void addSearchHistory(String c)
  {
    if (c == null)
      return;

    for (int i = 0; i < fieldSearch.getItemCount(); i++)
    {
      if (((String) fieldSearch.getItemAt(i)).equals(c))
        return;
    }

    fieldSearch.insertItemAt(c, 0);
    if (fieldSearch.getItemCount() > 25)
    {
      //for (int i = 24; i < fieldSearch.getItemCount(); i++)

// patch added by gandalf march 25 2003
      for (int i = 25; i < fieldSearch.getItemCount();)
// patch added by gandalf march 25 2003
          fieldSearch.removeItemAt(i);
    }

    //Search.setFindPattern(fieldSearchEditor.getText());
    fieldSearchEditor.setText((String) fieldSearch.getItemAt(0));
  }

  // adds current replace pattern in the replace history list

  private void addReplaceHistory()
  {
    addReplaceHistory(fieldReplaceEditor.getText());
  }

  // adds a pattern in the replace history list
  // the pattern to be added is given by the param c

  private void addReplaceHistory(String c)
  {
    if (c == null)
      return;

    for (int i = 0; i < fieldReplace.getItemCount(); i++)
    {
      if (((String) fieldReplace.getItemAt(i)).equals(c))
        return;
    }

    fieldReplace.insertItemAt(c, 0);
    if (fieldReplace.getItemCount() > 25)
    {
      //for (int i = 24; i < fieldReplace.getItemCount(); i++)

// patch added by gandalf march 25 2003
      for (int i = 25; i < fieldReplace.getItemCount();)
// patch added by gandalf march 25 2003
        fieldReplace.removeItemAt(i);
    }

    //Search.setReplacePattern(fieldReplaceEditor.getText());
    fieldReplaceEditor.setText((String) fieldReplace.getItemAt(0));
  }

  // Catch the action performed and then look for its source
  // According to the source object we call appropriate methods

  public void actionPerformed(ActionEvent evt)
  {
    Object source = evt.getSource();
    if (source == btnCancel)
      exit();
    else if (source == btnFind)
	doFind();
    else if (source == btnReplace)
      doReplace();
    else if (source == btnReplaceAll)
      doReplaceAll();
    else if (source == scripted)
    {
      script.setEnabled(scripted.isSelected());
      fieldReplace.setEnabled(!scripted.isSelected());
    }
  }

  private void setSettings()
  {
    Search.setFindPattern(fieldSearchEditor.getText());
    Search.setIgnoreCase(checkIgnoreCase.isSelected());
    Search.setRegexp(useRegexp.isSelected());
    if (type == REPLACE)
    {
      Search.setReplacePattern(fieldReplaceEditor.getText());
      Search.setPythonScript(scripted.isSelected());
      Search.setPythonScriptString(script.getText());
    }
  }

  // replace all the occurences of search pattern by
  // the replace one. If 'All Files' is checked, this is
  // done in all the opened file in the component 'parent'

  private void doReplaceAll()
  {
    UUtilities.setCursorOnWait(this, true);
    addReplaceHistory();
    addSearchHistory();

    try
    {

      if (allFiles.isSelected())
      {
	  //$$$$$$$$$$$$$$$$$$        parent.setBatchMode(true);

        JEditTextArea textArea;
        JEditTextArea[] areas = parent.getTextAreas();

        for (int i = 0; i < areas.length; i++)
        {
          textArea = areas[i];
          setSettings();
          Search.replaceAll(textArea, 0, parent.getTextArea().getDocument().getLength());
        }

	//$$$$$$$$$$$$$$$$$$$$$        parent.setBatchMode(false);
      } else  {
        JEditTextArea textArea = parent.getTextArea();
        setSettings();
        if (Search.replaceAll(textArea, 0, parent.getTextArea().getDocument().getLength()) == 0)
        {
          UUtilities.beep();
        }
      }

    } catch (Exception e) {
      // nothing
    } finally {
     UUtilities.setCursorOnWait(this, false);
    }
  }

  // replaces specified search pattern by the replace one.
  // this is done only if a match is found.

  private void doReplace()
  {
    UUtilities.setCursorOnWait(this, true);
    addReplaceHistory();
    addSearchHistory();

    try
    {

      JEditTextArea textArea = parent.getTextArea();
      setSettings();

      if (!Search.replace(textArea))
      {
        UUtilities.beep();
      } else
        find(textArea);

    } catch (Exception e) {
      // nothing
    } finally {
      UUtilities.setCursorOnWait(this, false);
    }
  }

  // finds the next occurence of current search pattern
  // the search is done in current text area

  private void doFind()
  {
    UUtilities.setCursorOnWait(this, true);

    addSearchHistory();
    find(parent.getTextArea());

    UUtilities.setCursorOnWait(this, false);
  }

  // finds the next occurence of the search pattern in a
  // a given text area. if match is not found, and if user
  // don't ask to start over from beginning, then the method
  // calls itself by specifying next opened text area.

    private void find(JEditTextArea textArea)
  {
    setSettings();

    try
    {
	if (!Search.find(textArea, textArea.getCaretPosition()))
      {
	  // String[] args = { textArea.getName() };
        int response = 1;/*JOptionPane.showConfirmDialog(null,
                       Jext.getProperty("find.matchnotfound", args),
                       Jext.getProperty("find.title"),
                       (allFiles.isSelected() ?
                        JOptionPane.YES_NO_CANCEL_OPTION : JOptionPane.YES_NO_OPTION),
                       JOptionPane.QUESTION_MESSAGE);
			 */

	response = JOptionPane.showConfirmDialog
	    (null,"Match not found in {0}.\nRestart from beginning ?",
	     "find", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
	
        switch (response)
        {
          case JOptionPane.YES_OPTION:
            textArea.setCaretPosition(0);
            find(textArea);
            break;
          case JOptionPane.NO_OPTION:
            if (allFiles.isSelected())
            {
		/*JextTabbedPane*/JTabbedPane pane = parent.getTabbedPane();
              int index = pane.indexOfComponent(textArea);

              Component c = null;
              while (c == null && !(c instanceof JEditTextArea))
              {
                index++;
                if (index == pane.getTabCount())
                  index = 0;
                c = pane.getComponentAt(index);
              }

              JEditTextArea area = (JEditTextArea) c;
              if (area != textArea)
                find(area);
            }
            break;
          case JOptionPane.CANCEL_OPTION:
            return;
        }
      }
    } catch (Exception e) { }
  }

  class KeyHandler extends KeyAdapter
  {
    public void keyPressed(KeyEvent evt)
    {
      switch (evt.getKeyCode())
      {
        case KeyEvent.VK_ENTER:
          if (evt.getSource() == fieldSearchEditor)
            doFind();
          else if (evt.getSource() == fieldReplaceEditor)
            doReplace();
          break;
        case KeyEvent.VK_ESCAPE:
          exit();
      }
    }
  }

  /***************************************************************************
  Patch
     -> Memory management improvements : it may help the garbage collector.
     -> Author : Julien Ponge (julien@izforge.com)
     -> Date : 23, May 2001
  ***************************************************************************/
  protected void finalize() throws Throwable
  {
    super.finalize();

    parent = null;
    fieldSearch = null;
    fieldReplace = null;
    fieldSearchEditor = null;
    fieldReplaceEditor = null;
    btnFind = null;
    btnReplace = null;
    btnReplaceAll = null;
    btnCancel = null;
    checkIgnoreCase = null;
    saveStates = null;
    useRegexp = null;
    allFiles = null;
  }
  // End of patch
}

// End of FindReplace.java
