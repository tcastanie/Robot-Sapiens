/*
 * 01/20/2003 - 23:11:56
 *
 * EditorOptions.java - The editor options pane
 * Copyright (C) 2000 Romain Guy
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

//package org.jext.options;
package gnu.options;

import java.awt.*;
import javax.swing.*;
/*
import org.jext.*;
import org.jext.gui.*;
*/
import gnu.*;
import gnu.gui.*;
import jsynedit.AbstractEditorPanel;

public class EditorOptions extends AbstractOptionPane //JPanel implements OptionPane
{
  private FontSelector fonts;
  private JTextField autoScroll, linesInterval, wrapGuide;
  private JComboBox newline, tabSize, modes, encoding, orientation;
  private JextCheckBox enterIndent, tabIndent, softTabs, blockCaret, selection, smartHomeEnd,
                       splitArea, fullFileName, lineHighlight, eolMarkers, blinkCaret, tabStop,
                       linesIntervalEnabled, wrapGuideEnabled, dirDefaultDialog, overSpace,
                       addExtraLineFeed, preserveLineTerm;
  private String modeNames[];

  public EditorOptions()
  {
    super("editor");

    addComponent(/*Jext.getProperty("options.autoscroll.label")*/"Auto scroll", autoScroll = new JTextField(4));
    autoScroll.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));

    addComponent(/*Jext.getProperty("options.linesinterval.label")*/"Interval highlight", linesInterval = new JTextField(4));
    linesInterval.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));

    addComponent(/*Jext.getProperty("options.wrapguide.label")*/"Wrap guide offset", wrapGuide = new JTextField(4));
    wrapGuide.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));

    String[] encodings = { "ASCII", "Cp850", "Cp1252", "iso-8859-1", "iso-8859-2", "KOI8_R","MacRoman",
                           "UTF8", "UTF16", "Unicode" };
    encoding = new JComboBox(encodings);
    encoding.setRenderer(new ModifiedCellRenderer());
    encoding.setEditable(true);
    addComponent(/*Jext.getProperty("options.encoding.label")*/"File encoding", encoding);

    fonts = new FontSelector("editor");
    addComponent(/*Jext.getProperty("options.fonts.label")*/"Font", fonts);

    String sizes[] = { "2", "4", "8", "16" };
    tabSize = new JComboBox(sizes);
    tabSize.setEditable(true);
    addComponent(/*Jext.getProperty("options.tabsize.label")*/"Tabs size", tabSize);
    tabSize.setRenderer(new ModifiedCellRenderer());
    //$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
    int nModes = 0;  /// $$$$$$$$$$$Jext.modes.size();
	//$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
    String modeUserNames[] = new String[nModes];
    modeNames = new String[nModes];
    

    /*$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
    for (int i = 0; i < nModes; i++)
    {
      Mode syntaxMode = (Mode) Jext.modes.get(i);
      modeNames[i] = syntaxMode.getModeName();
      modeUserNames[i] = syntaxMode.getUserModeName();
    }
    */ //$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$

    modes = new JComboBox(modeUserNames);
    modes.setRenderer(new ModifiedCellRenderer());
    addComponent(/*Jext.getProperty("options.syntax.mode.label")*/"Colorizing mode", modes);
	       
	     String newlines[] = { "MacOS (\\r)", "Unix (\\n)", "Windows (\\r\\n)" };
    newline = new JComboBox(newlines);
    newline.setRenderer(new ModifiedCellRenderer());
    addComponent(/*Jext.getProperty("options.newline.label")*/"Line separator", newline);

    String _or[] = { "Vertical", "Horizontal" };
    orientation = new JComboBox(_or);
    orientation.setRenderer(new ModifiedCellRenderer());
    addComponent(/*Jext.getProperty("options.orientation.label")*/"Split orientation", orientation);

    addComponent(linesIntervalEnabled = new JextCheckBox(/*Jext.getProperty("options.linesintervalenabled.label")*/"Enable interval highlighting"));
    addComponent(wrapGuideEnabled = new JextCheckBox(/*Jext.getProperty("options.wrapguideenabled.label")*/"Enable wrap guide"));
    addComponent(splitArea = new JextCheckBox(/*Jext.getProperty("options.splitarea.label")*/"Splits editor"));
    addComponent(blockCaret = new JextCheckBox(/*Jext.getProperty("options.blockcaret.label")*/"Block caret"));
    addComponent(blinkCaret = new JextCheckBox(/*Jext.getProperty("options.blinkingcaret.label")*/"Blinking caret"));
    addComponent(lineHighlight = new JextCheckBox(/*Jext.getProperty("options.linehighlight.label")*/"Line highlighting"));
    addComponent(eolMarkers = new JextCheckBox(/*Jext.getProperty("options.eolmarkers.label")*/"End of line markers"));
    addComponent(softTabs = new JextCheckBox(/*Jext.getProperty("options.softtabs.label")*/"Soft tabs"));
    addComponent(tabIndent = new JextCheckBox(/*Jext.getProperty("options.tabindent.label")*/"Indent on TAB"));
    addComponent(enterIndent = new JextCheckBox(/*Jext.getProperty("options.enterindent.label")*/"Indent on ENTER"));
    addComponent(tabStop = new JextCheckBox(/*Jext.getProperty("options.tabstop.label")*/"Tab stops"));
    addComponent(overSpace = new JextCheckBox(/*Jext.getProperty("options.wordmove.go_over_space.label")*/"Word jumping goes over spaces"));
    addComponent(smartHomeEnd = new JextCheckBox(/*Jext.getProperty("options.smartHomeEnd.label")*/"Smart HOME/END"));
    addComponent(dirDefaultDialog = new JextCheckBox(/*Jext.getProperty("options.defaultdirloaddialog.label")*/"Default dir from active file"));
    addComponent(selection = new JextCheckBox(/*Jext.getProperty("options.selection.label")*/"Use selection as search pattern"));
    addComponent(addExtraLineFeed = new JextCheckBox(/*Jext.getProperty("options.extra_line_feed.label")*/"Append an extra line feed on save"));
    addComponent(preserveLineTerm = new JextCheckBox(/*Jext.getProperty("options.line_end_preserved.label")*/"Keep existing line separator on open"));

    load();
  }

  public void load()
  {
  /*  
  autoScroll.setText(Jext.getProperty("editor.autoScroll"));
  linesInterval.setText(Jext.getProperty("editor.linesInterval"));
    wrapGuide.setText(Jext.getProperty("editor.wrapGuideOffset"));
 
    encoding.setSelectedItem(Jext.getProperty("editor.encoding", System.getProperty("file.encoding")));

     tabSize.setSelectedItem(Jext.getProperty("editor.tabSize"));

    int selMode = 0;
    String currMode = Jext.getProperty("editor.colorize.mode");
    for ( ; selMode < modeNames.length; selMode++)
      if (currMode.equals(modeNames[selMode])) break;
    modes.setSelectedIndex(selMode);

    int i = 0;
    String currNewLine = Jext.getProperty("editor.newLine");
    for ( ; i < Jext.NEW_LINE.length; i++)
      if (Jext.NEW_LINE[i].equals(currNewLine)) break;
    newline.setSelectedIndex(i);

    orientation.setSelectedItem(Jext.getProperty("editor.splitted.orientation"));

    linesIntervalEnabled.setSelected(Jext.getBooleanProperty("editor.linesIntervalEnabled"));
    wrapGuideEnabled.setSelected(Jext.getBooleanProperty("editor.wrapGuideEnabled"));
    splitArea.setSelected(Jext.getBooleanProperty("editor.splitted"));
    blockCaret.setSelected(Jext.getBooleanProperty("editor.blockCaret"));
    blinkCaret.setSelected(Jext.getBooleanProperty("editor.blinkingCaret"));
    lineHighlight.setSelected(Jext.getBooleanProperty("editor.lineHighlight"));
    eolMarkers.setSelected(Jext.getBooleanProperty("editor.eolMarkers"));
    tabIndent.setSelected(Jext.getBooleanProperty("editor.tabIndent"));
    enterIndent.setSelected(Jext.getBooleanProperty("editor.enterIndent"));
    softTabs.setSelected(Jext.getBooleanProperty("editor.softTab"));
    tabStop.setSelected(Jext.getBooleanProperty("editor.tabStop"));
    smartHomeEnd.setSelected(Jext.getBooleanProperty("editor.smartHomeEnd"));
    dirDefaultDialog.setSelected(Jext.getBooleanProperty("editor.dirDefaultDialog"));
    selection.setSelected(Jext.getBooleanProperty("use.selection"));
    overSpace.setSelected(Jext.getBooleanProperty("editor.wordmove.go_over_space"));
    addExtraLineFeed.setSelected(Jext.getBooleanProperty("editor.extra_line_feed"));
    preserveLineTerm.setSelected(Jext.getBooleanProperty("editor.line_end.preserve"));

  */    fonts.load();
  }

  public Component getComponent()
  {
    JScrollPane scroller = new JScrollPane(this);
    Dimension _dim = this.getPreferredSize();
    scroller.setPreferredSize(new Dimension((int) _dim.width, 410));
    scroller.setBorder(javax.swing.border.LineBorder.createBlackLineBorder());
    return scroller;
  }

  public void save()
  {
    /*Jext.setProperty("editor.colorize.mode",
                     ((Mode) Jext.modes.get(modes.getSelectedIndex())).getModeName());*/

      /*  $$$$$$$$$$$
	  Jext.setProperty("editor.colorize.mode", modeNames[modes.getSelectedIndex()]);
	  Jext.setProperty("editor.tabIndent", tabIndent.isSelected() ? "on" : "off");
	  Jext.setProperty("editor.enterIndent", enterIndent.isSelected() ? "on" : "off");
	  Jext.setProperty("editor.softTab", softTabs.isSelected() ? "on" : "off");
	  Jext.setProperty("editor.tabStop", tabStop.isSelected() ? "on" : "off");
	  Jext.setProperty("editor.tabSize", (String) tabSize.getSelectedItem());
      */
      System.out.println("--------- encoding == "+(String) encoding.getSelectedItem()+"  --- file encoding : "+
			 System.getProperty("file.encoding")+"------------");
      //setProperty("encoding", (String) encoding.getSelectedItem());

 
      AbstractEditorPanel.setProperty("blockCaret", blockCaret.isSelected() ? "on" : "off");
      AbstractEditorPanel.setProperty("blinkingCaret", blinkCaret.isSelected() ? "on" : "off");
      AbstractEditorPanel.setProperty("lineHighlight", lineHighlight.isSelected() ? "on" : "off");
      //    Jext.setProperty("editor.newLine", (String) Jext.NEW_LINE[newline.getSelectedIndex()]);
      AbstractEditorPanel.setProperty("eolMarkers", eolMarkers.isSelected() ? "on" : "off");
 /*
    Jext.setProperty("editor.smartHomeEnd", smartHomeEnd.isSelected() ? "on" : "off");
    Jext.setProperty("editor.dirDefaultDialog", dirDefaultDialog.isSelected() ? "on" : "off");
    Jext.setProperty("editor.splitted", splitArea.isSelected() ? "on" : "off");
    Jext.setProperty("editor.autoScroll", autoScroll.getText());
    Jext.setProperty("editor.linesInterval", linesInterval.getText());
    Jext.setProperty("editor.linesIntervalEnabled", linesIntervalEnabled.isSelected() ? "on" : "off");
    Jext.setProperty("editor.wrapGuideOffset", wrapGuide.getText());
    Jext.setProperty("editor.wrapGuideEnabled", wrapGuideEnabled.isSelected() ? "on" : "off");
    Jext.setProperty("editor.splitted.orientation", (String) orientation.getSelectedItem());
    Jext.setProperty("use.selection", selection.isSelected() ? "on" : "off");
    Jext.setProperty("editor.wordmove.go_over_space", overSpace.isSelected() ? "on" : "off");
    Jext.setProperty("editor.extra_line_feed", addExtraLineFeed.isSelected() ? "on" : "off");
    Jext.setProperty("editor.line_end.preserve", preserveLineTerm.isSelected() ? "on" : "off");
      */
      fonts.save();
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

    fonts = null;

    autoScroll = null;
    linesInterval = null;
    wrapGuide = null;

    newline = null;
    tabSize = null;
    modes = null;
    encoding = null;
    orientation = null;

    enterIndent = null;
    tabIndent = null;
    softTabs = null;
    blockCaret = null;
    selection = null;
    smartHomeEnd = null;
    splitArea = null;
    fullFileName = null;
    lineHighlight = null;
    eolMarkers = null;
    blinkCaret = null;
    tabStop = null;
    linesIntervalEnabled = null;
    wrapGuideEnabled = null;
    dirDefaultDialog = null;
    overSpace = null;
    modeNames = null;
    addExtraLineFeed = null;
    preserveLineTerm = null;
  }
  // End of patch
}

// End of EditorOptions.java
