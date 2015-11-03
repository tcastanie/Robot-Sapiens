/*
 * 00:49:52 09/09/00
 *
 * FontSelector.java - Font selector
 * Copyright (C) 2000 Romain Guy
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

//package org.jext.gui;
package gnu.gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;
/*
  import org.jext.*;
  import org.jext.gui.*;
*/
import jsynedit.AbstractEditorPanel; //a enlever...

import gnu.UUtilities;
 

public class FontSelector extends JextHighlightButton
{
  private String key;
  private String[] styles = {"Plain","Bold","Italic","Bold Italic"/* Jext.getProperty("options.editor.plain"),
				Jext.getProperty("options.editor.bold"),
				Jext.getProperty("options.editor.italic"),
				Jext.getProperty("options.editor.boldItalic")*/ };

  /**Constructs the FontSelector, a clickable button to select fonts.
   * @param key The name of the property to use to save the fonts(the values are
   * actually stored in keys whose names depend on this).
   */
  public FontSelector(String key)
  {
    super();
    this.key = key;

    addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent evt)
      {
        changeFont(new SelectorFrame(getFont()).getSelectedFont());
      }
    });
    load();
  }
  /** It reloads the fonts properties, when for instance a you want to undo a unsaved change
   * made by the user(most notably, you must call this in your option pane's load() method).*/
  public void load()
    {
	int fontSize, fontStyle;
	String fontFamily;
	
    try {
	//	System.out.println("try FontSelector 11111 "+ key);
	fontSize = Integer.parseInt(/*Jext.getProperty(key + ".fontSize")*/"12");
    } catch (Exception e) {
	fontSize = 10;
    }
    try {
	//	System.out.println("try FontSelector 22222 key = "+ key);
 	fontStyle = Integer.parseInt(/*Jext.getProperty(key + ".fontStyle")*/"0");
    }catch (Exception e) {
	fontStyle = 0;
    }
    
    fontFamily = "Monospaced";
    
    changeFont(new Font(fontFamily, fontStyle, fontSize));
  }

  /** It saves the fonts properties, when you want to save changes made by the user
   * (most notably, you must call this in your option pane's save() method).*/
    public void save()
    {
	
	//      System.out.println(" fffffffffselector - save de FontSelector !!!!!! "+ key);
      Font font = getFont();
      
    /*
    Jext.setProperty(key + ".font", font.getFamily());
    Jext.setProperty(key + ".fontSize", String.valueOf(font.getSize()));
    Jext.setProperty(key + ".fontStyle", String.valueOf(font.getStyle()));
        */

      /*Jext*/AbstractEditorPanel.setProperty("fontName", font.getFamily());
      /*Jext*/AbstractEditorPanel.setProperty("fontSize", String.valueOf(font.getSize()));
      /* Jext*/AbstractEditorPanel.setProperty("fontStyle", String.valueOf(font.getStyle()));
    //loadTextAreaProperties();
}

  private void changeFont(Font font)
  {
    if (font != null)
      setFont(font);
    setFontLabel();
  }

  private void setFontLabel()
  {
    Font font = getFont();
    StringBuffer buf = new StringBuffer();
    buf.append(font.getName()).append(':').append(font.getSize()).append(':');
    buf.append(styles[font.getStyle()]);
    setText(buf.toString());
  }

  class SelectorFrame extends JDialog implements ListSelectionListener, ActionListener
  {
    private String[] sizes = { "9", "10", "12", "14", "16", "18", "24" };

    private boolean fontSelected = false;

    private JLabel example;
    private JextHighlightButton ok, cancel;
    private JList fontsList, sizesList, stylesList;
    private JTextField fontsField, sizesField, stylesField;

    SelectorFrame(Font font)
    {
      super(JOptionPane.getFrameForComponent(FontSelector.this),"Font Selector"
	    /* Jext.getProperty("font.selector.title")*/, true);

      getContentPane().setLayout(new BorderLayout());

      JPanel panel_1 = createTextFieldAndListPanel(/*"font.selector.family"*/"Font family:",
                                                   fontsField = new JTextField(),
                                                   fontsList = new JList(getAvailableFontFamilyNames()));
      fontsField.setText(font.getName());
      fontsField.setEnabled(false);
      fontsList.setCellRenderer(new ModifiedCellRenderer());

      JPanel panel_2 = createTextFieldAndListPanel(/*"font.selector.size"*/"Font size:",
                                                   sizesField = new JTextField(10),
                                                   sizesList = new JList(sizes));
      sizesList.setSelectedValue(String.valueOf(font.getSize()), true);
      sizesField.setText(String.valueOf(font.getSize()));
      sizesList.setCellRenderer(new ModifiedCellRenderer());

      JPanel panel_3 = createTextFieldAndListPanel(/*"font.selector.style"*/"Font style:",
                                                   stylesField = new JTextField(10),
                                                   stylesList = new JList(styles));
      stylesList.setSelectedIndex(font.getStyle());
      stylesField.setText((String) stylesList.getSelectedValue());
      stylesField.setEnabled(false);
      stylesList.setCellRenderer(new ModifiedCellRenderer());

      fontsList.addListSelectionListener(this);
      sizesList.addListSelectionListener(this);
      stylesList.addListSelectionListener(this);

      JPanel listsPanel = new JPanel(new GridLayout(1, 3, 6, 6));
      listsPanel.add(panel_1);
      listsPanel.add(panel_2);
      listsPanel.add(panel_3);

      JPanel examplePanel = new JPanel();
      examplePanel.setBorder(new javax.swing.border.TitledBorder(/*Jext.getProperty("font.selector.preview")*/"Preview"));
      examplePanel.add(example = new JLabel(/*Jext.getProperty("font.selector.example")*/"\"You mean the kind from up there ?\" -- Plan 9"));

      JPanel buttonsPanel = new JPanel();
      buttonsPanel.add(ok = new JextHighlightButton(/*Jext.getProperty("general.ok.button")*/"Ok"));
      ok.setMnemonic(/*Jext.getProperty("general.ok.mnemonic")*/"O".charAt(0));
      buttonsPanel.add(cancel = new JextHighlightButton(/*Jext.getProperty("general.cancel.button")*/"Cancel"));
      cancel.setMnemonic(/*Jext.getProperty("general.cancel.mnemonic")*/"C".charAt(0));

      ok.addActionListener(this);
      cancel.addActionListener(this);

      preview();

      getContentPane().add(listsPanel, BorderLayout.NORTH);
      getContentPane().add(examplePanel, BorderLayout.CENTER);
      getContentPane().add(buttonsPanel, BorderLayout.SOUTH);

      Dimension prefSize = example.getPreferredSize();
      prefSize.height = 30;
      example.setPreferredSize(prefSize);

      getRootPane().setDefaultButton(ok);

      addKeyListener(new AbstractDisposer(this));
      setDefaultCloseOperation(DISPOSE_ON_CLOSE);
      // setIconImage(GUIUtilities.getJextIconImage());

      fontsList.setSelectedValue(font.getName(), true);
 
      pack();

      UUtilities.centerComponent(this);
      setResizable(false);
      setVisible(true);
    }

    public void actionPerformed(ActionEvent evt)
    {
      Object o = evt.getSource();
      if (o == cancel)
        dispose();
      else if (o == ok)
      {
        fontSelected = true;
        dispose();
      }
    }

    public Font getSelectedFont()
    {
      if (!fontSelected)
        return null;
      else
        return buildFont();
    }

    private Font buildFont()
    {
      int fontSize;
      try
      {
        fontSize = Integer.parseInt(sizesField.getText());
      } catch (Exception e) {
        fontSize = 12;
      }

      return new Font(fontsField.getText(), stylesList.getSelectedIndex(), fontSize);
    }

    private void preview()
    {
      example.setFont(buildFont());
    }

    public void valueChanged(ListSelectionEvent evt)
    {
      Object source = evt.getSource();
      if (source == fontsList)
      {
        String family = (String) fontsList.getSelectedValue();
        if (family != null)
          fontsField.setText(family);
      } else if (source == sizesList) {
        String size = (String) sizesList.getSelectedValue();
        if (size != null)
          sizesField.setText(size);
      } else if(source == stylesList) {
        String style = (String) stylesList.getSelectedValue();
        if (style != null)
          stylesField.setText(style);
      }

      preview();
    }

    private JPanel createTextFieldAndListPanel(String label, JTextField textField, JList list)
    {
      GridBagLayout layout = new GridBagLayout();
      JPanel panel = new JPanel(layout);

      GridBagConstraints cons = new GridBagConstraints();
      cons.gridx = cons.gridy = 0;
      cons.gridwidth = cons.gridheight = 1;
      cons.fill = GridBagConstraints.BOTH;
      cons.weightx = 1.0f;

      JLabel _label = new JLabel(/*Jext.getProperty(label)*/label);
      layout.setConstraints(_label, cons);
      panel.add(_label);

      cons.gridy = 1;
      Component vs = Box.createVerticalStrut(6);
      layout.setConstraints(vs, cons);
      panel.add(vs);

      cons.gridy = 2;
      layout.setConstraints(textField, cons);
      panel.add(textField);

      cons.gridy = 3;
      vs = Box.createVerticalStrut(6);
      layout.setConstraints(vs, cons);
      panel.add(vs);

      cons.gridy = 4;
      cons.gridheight = GridBagConstraints.REMAINDER;
      JScrollPane scroller = new JScrollPane(list);
      layout.setConstraints(scroller, cons);
      panel.add(scroller);

      return panel;
    }
  }

  /**
   * For some reason the default Java fonts show up in the
   * list with .bold, .bolditalic, and .italic extensions.
   */

  private static final String[] HIDEFONTS = { ".bold", ".italic" };

  /**
   * Gets a list of all available font family names.
   * When possible (in JDK 1.2 or later), this will
   * return GraphicsEnvironment.getAvailableFontFamilyNames().
   * However this method gracefully degrades to returning
   * Toolkit.getFontList() in JDK 1.1.
   */

  public static String[] getAvailableFontFamilyNames()
  {
    String nameArray[] = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
    java.util.Vector nameVector = new java.util.Vector(nameArray.length);
    for (int i = 0, j; i < nameArray.length; i++)
    {
      for (j = 0; j < HIDEFONTS.length; j++)
        if (nameArray[i].indexOf(HIDEFONTS[j]) >= 0)
          break;
      if (j == HIDEFONTS.length)
        nameVector.addElement(nameArray[i]);
    }

    String[] array = new String[nameVector.size()];
    nameVector.copyInto(array);
    nameVector = null;
    return array;
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
    
    key = null;
    styles = null;
  }
  // End of patch
}

// End of FontListBox.java
