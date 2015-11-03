/*
 * 10/19/2001 - 16:01:25
 *
 * PrintOptions.java - Options for printing.
 * Copyright (C) 2000 Scot Bellamy, (C) 2001 Romain Guy
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
import java.awt.event.*;
import java.awt.print.*;
import javax.swing.*;
import javax.swing.event.*;
/*
import org.jext.*;
import org.jext.gui.*;
*/
import jsynedit.AbstractEditorPanel;
import gnu.gui.*;

public class PrintOptions extends AbstractOptionPane implements ActionListener
{
  private FontSelector fonts;
  private JextHighlightButton pageLayout;
  private PageFormat pgfmt = new PageFormat();
  private JextCheckBox lineNumbers, wrap, syntax, header, footer;

  public PrintOptions()
  {
    super("print");

    fonts = new FontSelector("print");
    addComponent(/*Jext*/AbstractEditorPanel.getProperty("options.fonts.label"), fonts);

    addComponent(lineNumbers = new JextCheckBox(/*Jext*/AbstractEditorPanel.getProperty("print.printLineNumbers.label")));
    addComponent(wrap = new JextCheckBox(/*Jext*/AbstractEditorPanel.getProperty("print.wrapText.label")));
    addComponent(header = new JextCheckBox(/*Jext*/AbstractEditorPanel.getProperty("print.printHeader.label")));
    addComponent(footer = new JextCheckBox(/*Jext*/AbstractEditorPanel.getProperty("print.printFooter.label")));
    addComponent(syntax = new JextCheckBox(/*Jext*/AbstractEditorPanel.getProperty("print.printSyntax.label")));
    syntax.addActionListener(this);

    pageLayout = new JextHighlightButton(/*Jext*/AbstractEditorPanel.getProperty("print.pageLayout.label"));
    pageLayout.addActionListener(this);
    this.add(pageLayout);
    load();
  }
  
  public void load()
  {
    fonts.load();
    lineNumbers.setSelected(/*Jext*/AbstractEditorPanel.getBooleanProperty("print.lineNumbers"));
    wrap.setSelected(/*Jext*/AbstractEditorPanel.getBooleanProperty("print.wrapText"));
    header.setSelected(/*Jext*/AbstractEditorPanel.getBooleanProperty("print.header"));
    footer.setSelected(/*Jext*/AbstractEditorPanel.getBooleanProperty("print.footer"));
    syntax.setSelected(/*Jext*/AbstractEditorPanel.getBooleanProperty("print.syntax"));

    Paper paper = pgfmt.getPaper();
   
    pgfmt.setOrientation(Integer.parseInt(/*Jext*/AbstractEditorPanel.getProperty("print.pageOrientation")));
    double width = Double.parseDouble(/*Jext*/AbstractEditorPanel.getProperty("print.pageWidth"));
    double height = Double.parseDouble(/*Jext*/AbstractEditorPanel.getProperty("print.pageHeight"));
    double imgX = Double.parseDouble(/*Jext*/AbstractEditorPanel.getProperty("print.pageImgX"));
    double imgY = Double.parseDouble(/*Jext*/AbstractEditorPanel.getProperty("print.pageImgY"));
    double imgWidth = Double.parseDouble(/*Jext*/AbstractEditorPanel.getProperty("print.pageImgWidth"));
    double imgHeight = Double.parseDouble(/*Jext*/AbstractEditorPanel.getProperty("print.pageImgHeight"));
   
    paper.setSize(width, height);
    paper.setImageableArea(imgX, imgY, imgWidth, imgHeight);
    pgfmt.setPaper(paper);

    handleComponents();
  }

  private void handleComponents()
  {
    if (syntax.isSelected())
    {
      footer.setEnabled(true);
      pageLayout.setEnabled(false);
      wrap.setEnabled(false);
    } else {
      footer.setEnabled(false);
      pageLayout.setEnabled(true);
      wrap.setEnabled(true);
    }
  }

  public void save()
  {
    /*Jext*/AbstractEditorPanel.setProperty("print.lineNumbers", lineNumbers.isSelected() ? "on" : "off");
    /*Jext*/AbstractEditorPanel.setProperty("print.wrapText", wrap.isSelected() ? "on" : "off");
    /*Jext*/AbstractEditorPanel.setProperty("print.header", header.isSelected() ? "on" : "off");
    /*Jext*/AbstractEditorPanel.setProperty("print.footer", footer.isSelected() ? "on" : "off");
    /*Jext*/AbstractEditorPanel.setProperty("print.syntax", syntax.isSelected() ? "on" : "off");

    Paper paper = pgfmt.getPaper();

    /*Jext*/AbstractEditorPanel.setProperty("print.pageOrientation", Integer.toString(pgfmt.getOrientation()) );
    /*Jext*/AbstractEditorPanel.setProperty("print.pageWidth", Double.toString(paper.getWidth()));
    /*Jext*/AbstractEditorPanel.setProperty("print.pageHeight", Double.toString(paper.getHeight()));
    /*Jext*/AbstractEditorPanel.setProperty("print.pageImgX", Double.toString(paper.getImageableX()));
    /*Jext*/AbstractEditorPanel.setProperty("print.pageImgY", Double.toString(paper.getImageableY()));
    /*Jext*/AbstractEditorPanel.setProperty("print.pageImgWidth", Double.toString(paper.getImageableWidth()));
    /*Jext*/AbstractEditorPanel.setProperty("print.pageImgHeight", Double.toString(paper.getImageableHeight()));

    fonts.save();
  }

  public void pageLayout()
  {
    PrinterJob job = PrinterJob.getPrinterJob();
    pgfmt = job.pageDialog(pgfmt);
    pgfmt = job.validatePage(pgfmt); // make sure the pageformat is ok
  }

  public void actionPerformed(ActionEvent evt)
  {
    Object source = evt.getSource();

    if (source == pageLayout)
      pageLayout();
    else if (source == syntax)
      handleComponents();
  }
}

// End of PrintOptions.java
