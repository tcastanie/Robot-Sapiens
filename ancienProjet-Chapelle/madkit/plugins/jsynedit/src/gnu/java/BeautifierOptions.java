/*
 * 05/08/2000 - 23:26:27
 *
 * BeautifierOptions.java - The Beautifier options pane
 * Copyright (C) 2000 Aaron Miller
 * aaronhmiller@mac.com
 * www.chez.com/powerteam
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
package gnu.java;

import java.awt.*;
import javax.swing.*;
/*
 import org.jext.*;
 import org.jext.gui.*;
 */
import gnu.*;
import gnu.gui.*;
import jsynedit.AbstractEditorPanel;

public class BeautifierOptions
    extends AbstractOptionPane {
  private JextCheckBox breakBracket;
  private JextCheckBox indentSwitch;
  private JTextField prefLineLength;
  private JTextArea settingsInfo;

  public BeautifierOptions() {
    super("beautifier");

    addComponent(breakBracket = new JextCheckBox("New line for open curly '{'"
        /*Jext.getProperty("beautifier.breakBracket.label")*/));
    breakBracket.setSelected("true".equals( /*Jext*/AbstractEditorPanel.getProperty("beautifier.breakBracket")));

    addComponent(indentSwitch = new JextCheckBox(
        "Indent case blocks of switch statements"
        /*Jext.getProperty("beautifier.indentSwitch.label")*/));
    indentSwitch.setSelected("true".equals( /*Jext*/AbstractEditorPanel.getProperty("beautifier.indentSwitch")));

    addComponent("Preferred line length:"
                 /*Jext.getProperty("beautifier.preferredLineLength.label")*/,
                 prefLineLength = new JTextField(4));
    prefLineLength.setText("70"
        /*Jext.getProperty("beautifier.preferredLineLength")*/);
    prefLineLength.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));

    settingsInfo = new JTextArea(
        "Use tab settings in Editor options to set indent type (tab/space) and size"
        /*Jext.getProperty("beautifier.indentSettings.label")*/, 2, 35);
    settingsInfo.setBackground(getBackground()); //new Color(205, 205, 205));
    settingsInfo.setEditable(false);
    settingsInfo.setLineWrap(true);
    settingsInfo.setWrapStyleWord(true);
    addComponent(settingsInfo);
  }

  public void save() {
    /*Jext*/AbstractEditorPanel.setProperty("beautifier.breakBracket",
                                            breakBracket.isSelected() ? "true" :
                                            "false");
    /*Jext*/AbstractEditorPanel.setProperty("beautifier.indentSwitch",
                                            indentSwitch.isSelected() ? "true" :
                                            "false");
    /*Jext*/AbstractEditorPanel.setProperty("beautifier.preferredLineLength",
                                            prefLineLength.getText());
  }
}
// End of BeautifierOptions.java
