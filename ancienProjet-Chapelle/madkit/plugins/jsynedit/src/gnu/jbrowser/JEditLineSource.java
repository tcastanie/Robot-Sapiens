package gnu.jbrowser;

import javax.swing.text.Element;
import javax.swing.text.BadLocationException;
import javax.swing.text.Position;

//import gnu.MenuAction;
import jsynedit.*;

//import jedit.textarea.TextUtilities;

//=============================================================================
/**
 * Implements the functionality specified by the LineSource interface using
 * the set of lines made availble by a jEdit view and adds a method to return
 * the StartOffset.
 */
class JEditLineSource
    implements JBrowseParser.LineSource {
  private
  /*JextFrame*/ AbstractEditorPanel view; // jEdit specific
  private
  /*JextTextArea*/ AbstractDoc buffer; // jEdit specific
  private Element map, lineElement; // jEdit specific
  private String name;
  private int start;
  private int lastLine; // last line that was read

  //-------------------------------------------------------------------------
  JEditLineSource( /*JextFrame*/AbstractEditorPanel view) {
    this.view = view;
    reset();

  } // JEditLineSource(View): <init>

  //-------------------------------------------------------------------------
  /**
   * Setup to become a newly initialized LineSource for the current buffer.
   */
  public void reset() {
    buffer = view.getTextArea();
    map = buffer.getDocument().getDefaultRootElement();
    /*  $$$$
         System.out.println("JeditLineSource **** getDefaultRootElement() = "+ map);
         File temp = buffer.getFile();///////getFile non defini ds abstractDoc !!!!
          if (temp == null)
          {
         System.out.println(" reset() ds jbplugin JeditLineSource **** temp = "+ temp);
          name = new String();
          }//end if File == null
          else
          {
          name = buffer.getFile().getName();
          }//end else
     */

    start = 0;
    lastLine = -1;

    // $$$ sets the textArea's Name !
    name = buffer.getName();

  } // reset(): void

  //-------------------------------------------------------------------------
  public final String getName() {
    return name;
  }

  //-------------------------------------------------------------------------
  public final Object createPosition(int offs) {
    Position pos;

    try {
      pos = buffer.getDocument().createPosition(offs);
    }
    catch (BadLocationException e) {
//%			JBrowse.log(1, this, "BadLocationException thrown in exception handler of method createPosition().");
      pos = null;
    }
    return pos;
  }

  //-------------------------------------------------------------------------
  public final String getLine(int lineIndex) {
    // ??? Note this should be cleaned up. Currently rely on returning
    // null when source is exhausted. Should actually throw exception in
    // the second case. Probably should have an indexed line source vs. sequential
    // line source. The second would only allow calls to getNextLine and it
    // would keep track of the line number for the client.
    String lineString;

    // Sanity check
    if (lineIndex > map.getElementCount() - 1) {

//%			JBrowse.log(4, this, "Argument to getLine() is bad: " + lineIndex);
      System.out.println(this +" Argument to getLine() is bad: " + lineIndex);
      return null; // source has been exhaused
    }

    try {
      lineElement = map.getElement(lineIndex);

      // Sanity check
      if (lineElement == null) {
//%				JBrowse.log(1, this, "Element returned by getElement() is null");
        return null;
        //System.exit(0); // ??? should throw an exception here
      }
      lastLine = lineIndex;

      start = lineElement.getStartOffset();
      lineString = buffer.getDocument().getText(start,
                                                lineElement.getEndOffset() - start -
                                                1);

    }
    catch (BadLocationException ble) {
//%			JBrowse.log(1, this, "BadLocationException thrown in getLine(int) method.");
      lineString = "";
    }
    return lineString;

  } // getLine(int): String

  //-------------------------------------------------------------------------
  public final boolean isExhausted() {
    return (lastLine >= map.getElementCount() - 1);
  }

  // this is specific to a JEditLineSource, should get rid of it ???
  public final int getStartOffset() {
    return start;
  }

  public final int getTotalLines() {
    return map.getElementCount();
  }

  final
  /*JextTextArea*/ AbstractDoc getTextArea() {
    return buffer;
  }

} // class JEditLineSource
// End of JBrowsePlugin.java
