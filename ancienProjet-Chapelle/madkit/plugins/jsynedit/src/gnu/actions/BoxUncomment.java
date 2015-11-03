/*
 * 11/19/2001 - 00:47:08
 *
 * BoxUncomment.java
 *
 * This	free software; you can redistribute it and/or
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

//package org.jext.actions;
package gnu.actions;

import javax.swing.text.Element;
//import org.jext.*;
import javax.swing.text.Document;
import java.awt.event.ActionEvent;
import javax.swing.text.BadLocationException;

import gnu.EditAction;
import gnu.MenuAction;
import jsynedit.AbstractDoc;
import gnu.UUtilities;

public class BoxUncomment
    extends MenuAction
    implements EditAction {
  public BoxUncomment() {
    super("box_comment");
  }

  public void actionPerformed(ActionEvent evt) {
    /*JextTextArea*/AbstractDoc textArea = getTextArea(evt);
    Document doc = textArea.getDocument();

    // $$$ getEditor() $$$ ???
    String commentStart = textArea.getEditor().getProperty("commentStart");
    String commentEnd = textArea.getEditor().getProperty("commentEnd");
    String boxComment = textArea.getEditor().getProperty("boxComment");

    if (commentStart == null || commentEnd == null || boxComment == null) {
      return;
    }

    commentStart = commentStart + ' ';
    commentEnd = ' ' + commentEnd;
    boxComment = boxComment + ' ';

    int selectionStart = textArea.getSelectionStart();
    int selectionEnd = textArea.getSelectionEnd();

    Element map = doc.getDefaultRootElement();
    int startLine = map.getElementIndex(selectionStart);
    int endLine = map.getElementIndex(selectionEnd);

    //$$ remove textArea.beginCompoundEdit();

    try {
      Element lineElement = map.getElement(startLine);
      int start = lineElement.getStartOffset();
      int indent = UUtilities.getLeadingWhiteSpace(doc.getText(start,
          lineElement.getEndOffset() - start));
      int boxCommentWhiteSpace = UUtilities.getLeadingWhiteSpace(boxComment);
      doc.remove(Math.max(start + indent, selectionStart), commentStart.length());

      for (int i = startLine + 1; i < endLine; i++) {
        lineElement = map.getElement(i);
        start = lineElement.getStartOffset();
        indent = UUtilities.getLeadingWhiteSpace(doc.getText(start,
            lineElement.getEndOffset() - start));
        doc.remove(start + indent - boxCommentWhiteSpace, boxComment.length());
      }

      lineElement = map.getElement(endLine);
      start = lineElement.getStartOffset();
      indent = UUtilities.getLeadingWhiteSpace(doc.getText(start,
          lineElement.getEndOffset() - start));

      if (startLine < endLine) {
      	System.out.println("start = " + startLine + "\nendl = " + endLine);
        doc.remove(start + indent - boxCommentWhiteSpace, boxComment.length());
      }
      doc.remove(Math.max(start + indent, textArea.getSelectionEnd()) - commentEnd.length(),
                       commentEnd.length());

      textArea.setCaretPosition(textArea.getCaretPosition());
    }
    catch (BadLocationException ble) {}
    //$$ remove textArea.endCompoundEdit();
  }
}
// End of BoxUncomment.java
