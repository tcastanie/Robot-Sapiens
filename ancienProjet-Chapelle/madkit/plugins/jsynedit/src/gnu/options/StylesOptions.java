/*
 * StylesOptions.java - Color/style option pane
 * Copyright (C) 1999 Slava Pestov
 * Portions copyright (C) 1999 mike dillon
 * Portions copyright (C) 1999-2001 Romain Guy
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

package gnu.options;

import java.awt.*;
import java.awt.event.*;

import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.border.EmptyBorder;

/*
  import org.jext.*;
  import org.jext.gui.*;
  import jedit.syntax.SyntaxStyle;
*/

//import gnu.*;
import jsynedit.AbstractDoc; //a enlever...
import gnu.gui.AbstractOptionPane;
//import jedit.syntax.SyntaxStyle;


public class StylesOptions extends AbstractOptionPane
{
  public static final EmptyBorder noFocusBorder = new EmptyBorder(1, 1, 1, 1);

  public StylesOptions()
  {
    super("styles ");

    setLayout(new GridLayout(2, 1));

    JPanel panel = new JPanel(new BorderLayout());
    panel.add(BorderLayout.NORTH, new JLabel(/*Jext.getProperty("options.styles.colors")*/"Colors:"));
    panel.add(BorderLayout.CENTER, createColorTableScroller());
    add(panel);

    setLayout(new GridLayout(2, 1));

    panel = new JPanel(new BorderLayout());
    panel.add(BorderLayout.NORTH, new JLabel(/*Jext.getProperty("options.styles.styles")*/"Styles:"));
    panel.add(BorderLayout.CENTER, createStyleTableScroller());
    add(panel);
  }

    public void save()
    {
      colorModel.save();
      styleModel.save();
    }
  public void load()
  {
    colorModel.load();
    styleModel.load();
  }

  // private members
  private ColorTable.ColorTableModel colorModel;
  private ColorTable colorTable;
  private StyleTable.StyleTableModel styleModel;
  private StyleTable styleTable;
  
  private JScrollPane createColorTableScroller()
  {
    colorModel = createColorTableModel();
    colorTable = new ColorTable(colorModel);
    Dimension d = colorTable.getPreferredSize();
    d.height = Math.min(d.height, 100);
    JScrollPane scroller = new JScrollPane(colorTable);
    scroller.setPreferredSize(d);
    return scroller;
  }

  private ColorTable.ColorTableModel createColorTableModel()
  {
     
    ColorTable.ColorTableModel model = new ColorTable.ColorTableModel();
    model.addColorChoice("Editor background", jsynedit.AbstractEditorPanel.getProperty("editor.bgColor")/*"#ffffff"*/);
    model.addColorChoice("Editor foreground", jsynedit.AbstractEditorPanel.getProperty("editor.fgColor")/*"#000000"*/);
    model.addColorChoice("Caret", jsynedit.AbstractEditorPanel.getProperty("editor.caretColor")/*"#ff0000"*/);
    model.addColorChoice("Selection", jsynedit.AbstractEditorPanel.getProperty("editor.selectionColor")/*"#ccccff"*/);
    model.addColorChoice("Highlight", jsynedit.AbstractEditorPanel.getProperty("editor.highlightColor")/*"#ffff00"*/);
    model.addColorChoice("Line highlight", jsynedit.AbstractEditorPanel.getProperty("editor.lineHighlightColor")/*"#cdffcd"*/);//e0ffe0"*/);
    model.addColorChoice("Interval highlight", jsynedit.AbstractEditorPanel.getProperty("editor.linesHighlightColor")/*"#e6e6ff"*/);
    model.addColorChoice("Bracket highlight", jsynedit.AbstractEditorPanel.getProperty("editor.bracketHighlightColor")/*"#00ff00"*/);
    model.addColorChoice("Wrap guide", jsynedit.AbstractEditorPanel.getProperty("editor.wrapGuideColor")/*"#ff0000"*/);
    model.addColorChoice("EOL markers", jsynedit.AbstractEditorPanel.getProperty("editor.eolMarkerColor")/*"#009999"*/);
      /*
      model.addColorChoice("options.styles.gutterBgColor", "textArea.gutter.bgColor");
      model.addColorChoice("options.styles.gutterFgColor", "textArea.gutter.fgColor");
      model.addColorChoice("options.styles.gutterHighlightColor", "textArea.gutter.highlightColor");
      model.addColorChoice("options.styles.gutterBorderColor", "textArea.gutter.borderColor");
      model.addColorChoice("options.styles.gutterAnchorMarkColor", "textArea.gutter.anchorMarkColor");
      model.addColorChoice("options.styles.gutterCaretMarkColor", "textArea.gutter.caretMarkColor");
      model.addColorChoice("options.styles.gutterSelectionMarkColor", "textArea.gutter.selectionMarkColor");
      model.addColorChoice("options.styles.consoleBgColor", "console.bgColor");
      model.addColorChoice("options.styles.consoleOutputColor", "console.outputColor");
      model.addColorChoice("options.styles.consolePromptColor", "console.promptColor");
      model.addColorChoice("options.styles.consoleErrorColor", "console.errorColor");
      model.addColorChoice("options.styles.consoleInfoColor", "console.infoColor");
      model.addColorChoice("options.styles.consoleSelectionColor", "console.selectionColor");
      model.addColorChoice("options.styles.vfSelectionColor", "vf.selectionColor");
    */
    model.addColorChoice("Buttons highlight", "buttons.highlightColor"/*"#c0c0d2"*/);
    return model;
  }

  private JScrollPane createStyleTableScroller()
  {
    styleModel = createStyleTableModel();
    styleTable = new StyleTable(styleModel);
    Dimension d = styleTable.getPreferredSize();
    d.height = Math.min(d.height, 100);
    JScrollPane scroller = new JScrollPane(styleTable);
    scroller.setPreferredSize(d);
    return scroller;
  }

  private StyleTable.StyleTableModel createStyleTableModel()
  {
    StyleTable.StyleTableModel model = new StyleTable.StyleTableModel();
    model.addStyleChoice("Comment 1", "color:#00ff00 style:i");///*"editor.style.comment1"*/"Comment 1");
    model.addStyleChoice("Comment 2", "color:#00ff00 style:i");///*"editor.style.comment2"*/"Comment 2");
    model.addStyleChoice("Literal 1 (chaîne)", "color:#00ffff");///*"editor.style.literal1"*/"String literal");
    model.addStyleChoice("Literal 2 (object)", "color:#650099");///*"editor.style.literal2"*/"Object literal");
    model.addStyleChoice("Label", "color:#cc00cc style:b");///*"editor.style.label"*/"Label");
    model.addStyleChoice("Keyword 1", "color:#0000ff style:b");///*"editor.style.keyword1"*/"Keyword 1");
    model.addStyleChoice("Keyword 2", "color:#ff9900");///*"editor.style.keyword2"*/"Keyword 2");
    model.addStyleChoice("Keyword 3", "color:#ff9900");///*"editor.style.keyword3"*/"Keyword 3");
    model.addStyleChoice("Operator", "color:#ffc800 style:b");///*"editor.style.operator"*/"Operator");
    model.addStyleChoice("Invalid token", "color:#666666 style:b");///*"editor.style.invalid"*/"Invalid token");
    model.addStyleChoice("Method", "color:#000000 style:b");///*"editor.style.method"*/"Method");
    return model;
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
    
    colorModel = null;
    colorTable = null;
    styleModel = null;
    styleTable = null;
  }
  // End of patch

}//end class StylesOptions
