/*
 * Created on 7 f?vr. 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */

/**
 * @author sdaloz
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */

package jsynedit;



import jedit.textarea.JEditTextArea;
import gnu.search.SearchHighlight;
import java.io.File;
import jsynedit.AbstractEditorPanel;

// from Oury (for Fonts) [[
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Dimension;
import gnu.GUIUtilities;
import jedit.Token;
import jedit.syntax.SyntaxStyle;
// from Oury (for Fonts) ]]

public class AbstractDoc
	extends JEditTextArea
{
	/* Attributs */
	protected UndoComponent undo;			// d?faire / refaire	
	private boolean wrap;						        // retour ? la ligne automatique
	private String currentFile;
	private SearchHighlight searchHighlight;
	private AbstractEditorPanel editor;
	private JSyneditInputHandler inputHandler;
	
	// from Seb (for Fonts) [[
	private String fontName="Monospaced";
	private int fontStyle=Font.PLAIN;
	private int fontSize=12; 
	// from Seb (for Fonts) ]]
	
	/* Constructeurs */
	public AbstractDoc()
	{
		inputHandler = new JSyneditInputHandler();
		inputHandler.addDefaultKeyBindings();
		setInputHandler( inputHandler );
		
		undo = new UndoComponent();
		wrap = false;
		
		getDocument().addUndoableEditListener( undo );
		currentFile = null;
		
		setFont( new Font(fontName, fontStyle, fontSize) );
	}
	

	/**
	 * Undo last change on the text.
	 */
	public void undo()
	{
		undo.undo();
	}
	
	
	/**
	 * Cancel last undo.
	 */
	public void redo()
	{
		undo.redo();
	}
	
	
	// from Oury [[	
	/**
	 * Adds a search highlighter if none exists.
	 */
	public void initSearchHighlight() {
	  if (searchHighlight == null) {
		searchHighlight = new SearchHighlight();
		getPainter().addCustomHighlight(searchHighlight);
	  }
	}

	/**
	 * Returns the associated search highlighter.
	 */
	public SearchHighlight getSearchHighlight() {
	  return searchHighlight;
	}
	
	/**
	 * Return current opened file as a <code>File</code> object.
	 */
	public File getFile() {
	  return (currentFile == null ? null : new File(currentFile));
	}
	
	
	public AbstractEditorPanel getEditor() {
	  return editor;
	}
	
	public void setEditor(AbstractEditorPanel abs) {
	  editor = abs;
	}
	
	
	/**
	 * Gets the name of the current file opened.
	 * @return the filename
	 */
	public String getCurrentFile()
	{
		return currentFile;
	}
	
	
	/**
	 * Sets the name of the current file opened.
	 * @param s the filename
	 */
	public void setCurrentFile(String s)
	{
		currentFile=s;
	}	
	
	// from Oury, modified by Seb, for fonts [[
	/**
	  * Load  text area properties from the user settings.
	  */
	 public  void loadTextAreaProperties(/*JextTextArea textArea*/)
	 {
	   /*try
	   {
	   textArea.setTabSize(Integer.parseInt(Jext.getProperty("editor.tabSize")));
	   } catch (NumberFormatException nf) {
	   textArea.setTabSize(8);
	   Jext.AbstractEditorPanel.setProperty("editor.tabSize", "8");
	   }*/
    
	   try
	   {
	   setElectricScroll(Integer.parseInt(AbstractEditorPanel.getProperty("editor.autoScroll")));
	   } catch (NumberFormatException nf) {
	   /*textArea.*/setElectricScroll(0);
	   }
    
	   String newLine = /*Jext*/AbstractEditorPanel.getProperty("editor.newLine");
	   if (newLine == null)
	   /*Jext*/AbstractEditorPanel.setProperty("editor.newLine", System.getProperty("line.separator"));
	 
	 try
		{
		 //	System.out.println("je suis sage font="+getProperty("encoding"));
		 //	System.setProperty("file.encoding",AbstractEditorPanel.getProperty("encoding"));
		 setFontSize(Integer.parseInt(/*Jext*/AbstractEditorPanel.getProperty("editor.fontSize")/*getProperty("encoding")*/));
		 } catch (NumberFormatException nf) {
		 /*textArea.*/setFontSize(12);
		 /*Jext.*/AbstractEditorPanel.setProperty("editor.fontSize"/*encoding"*/, "12");
		 //	System.out.println("je suis sage 2");
		 }
	 try
		 {
		 setFontSize(Integer.parseInt(AbstractEditorPanel.getProperty("fontSize")));
		 } catch (NumberFormatException nf) {
		 setFontSize(12);
		 AbstractEditorPanel.setProperty("fontSize", "12");
		 }
	 try
		 {
		 setFontStyle(Integer.parseInt(AbstractEditorPanel.getProperty("fontStyle")));
		 } catch (NumberFormatException nf) {
		 setFontStyle(0);
		 AbstractEditorPanel.setProperty("fontStyle", "0");
		 }
	 setFontName(AbstractEditorPanel.getProperty("fontName"));
	 jedit.textarea.TextAreaPainter painter = getPainter();
	 
	   /*try
	   {
	   painter.setLinesInterval(Integer.parseInt(Jext.getProperty("editor.linesInterval")));
	   } catch (NumberFormatException nf) {
	   painter.setLinesInterval(0);
	   }

	   try
	   {
	   painter.setWrapGuideOffset(Integer.parseInt(Jext.getProperty("editor.wrapGuideOffset")));
	   } catch (NumberFormatException nf) {
	   painter.setWrapGuideOffset(0);
	   }
    
	   painter.setAntiAliasingEnabled(Jext.getBooleanProperty("editor.antiAliasing"));
	 */

	 painter.setLineHighlightEnabled(AbstractEditorPanel.getBooleanProperty("lineHighlight"));
	 painter.setEOLMarkersPainted(AbstractEditorPanel.getBooleanProperty("eolMarkers"));
	 painter.setBlockCaretEnabled(AbstractEditorPanel.getBooleanProperty("blockCaret"));

	 
	   //painter.setLinesIntervalHighlightEnabled(Jext.getBooleanProperty("editor.linesIntervalEnabled"));
	   //painter.setWrapGuideEnabled(Jext.getBooleanProperty("editor.wrapGuideEnabled"));
	 
	 painter.setBracketHighlightColor(GUIUtilities.parseColor(AbstractEditorPanel.getProperty(/*"Bracket highlight"*/"editor.bracketHighlightColor")));
	 painter.setLineHighlightColor(GUIUtilities.parseColor(/*Jext*/AbstractEditorPanel.getProperty(/*"Line highlight"*/"editor.lineHighlightColor")));
	 //painter.setHighlightColor(GUIUtilities.parseColor(/*Jext*/AbstractEditorPanel.getProperty(/*"Highlight"*/"editor.highlightColor")));
	 painter.setEOLMarkerColor(GUIUtilities.parseColor(/*Jext*/AbstractEditorPanel.getProperty(/*"EOL markers"*/"editor.eolMarkerColor")));
	 painter.setCaretColor(GUIUtilities.parseColor(/*Jext*/AbstractEditorPanel.getProperty(/*"Caret"*/"editor.caretColor")));
	 painter.setSelectionColor(GUIUtilities.parseColor(/*Jext*/AbstractEditorPanel.getProperty(/*"Selection"*/"editor.selectionColor")));
	 painter.setBackground(GUIUtilities.parseColor(/*Jext*/AbstractEditorPanel.getProperty(/*"Editor background"*/"editor.bgColor")));
	 painter.setForeground(GUIUtilities.parseColor(/*Jext*/AbstractEditorPanel.getProperty(/*"Editor foreground"*/"editor.fgColor")));
	 //painter.setLinesIntervalHighlightColor(GUIUtilities.parseColor(/*Jext*/AbstractEditorPanel.getProperty(/*"Interval highlight"*/"editor.linesHighlightColor")));
	 //painter.setWrapGuideColor(GUIUtilities.parseColor(/*Jext*/AbstractEditorPanel.getProperty(/*"Wrap guide"*/"editor.wrapGuideColor")));
    

	 /*
	   loadGutter(textArea.getGutter());
	 */
	 loadStyles(painter);
    
	 /*
	   if (textArea.isNew() && textArea.isEmpty())
	   textArea.setColorizing(Jext.getProperty("editor.colorize.mode"));
	   textArea.putClientProperty("InputHandler.homeEnd",
	   new Boolean(Jext.getBooleanProperty("editor.smartHomeEnd")));*/
	 setCaretBlinkEnabled(AbstractEditorPanel.getBooleanProperty("blinkingCaret"));
	 // textArea.setParentTitle();
	 /*textArea.*/repaint();
	 }
	 
	 
	 public void loadUndoProperties()
	 {
	 	int limit;
	 	long sequence;
	 	
	 	try
	 	{
	 		limit = Integer.parseInt(AbstractEditorPanel.getProperty("options.undo.limit"));
	 	}
	 	catch( Exception e )
	 	{
	 		limit = UndoComponent.MAX_UNDOLIMIT;
	 	}
	 	
	 	try
	 	{
	 		sequence = Long.parseLong(AbstractEditorPanel.getProperty("options.undo.sequence"));
	 	}
	 	catch( Exception e )
	 	{
			sequence = UndoComponent.SEQUENCE_DELAY;
	 	}
	 	
	 	undo.setLimit(limit);
	 	undo.setSequenceDelay(sequence);
	 	
		AbstractEditorPanel.setProperty("options.undo.limit", "" + limit);
		AbstractEditorPanel.setProperty("options.undo.sequence", "" + sequence);
	 }
	 
	 
	/**
	 * Loads the syntax colorizing styles properties. This method
	 * is called by loadTextArea() and exists only to separate the
	 * code because loadTextArea() was becoming confusing
	 */
	private void loadStyles( jedit.textarea.TextAreaPainter painter)
	{
	try
		{
		SyntaxStyle[] styles = new SyntaxStyle[Token.ID_COUNT];  // loads the syntax colorizing styles properties. This method
		// is called by loadTextArea() and exists only to separate the
		// code because loadTextArea() was becoming confusing

			styles[Token.COMMENT1] = GUIUtilities.parseStyle(/*Jext*/AbstractEditorPanel.getProperty("editor.style.comment1"));
			styles[Token.COMMENT2] = GUIUtilities.parseStyle(/*Jext*/AbstractEditorPanel.getProperty("editor.style.comment2"));
			styles[Token.KEYWORD1] = GUIUtilities.parseStyle(/*Jext*/AbstractEditorPanel.getProperty("editor.style.keyword1"));
			styles[Token.KEYWORD2] = GUIUtilities.parseStyle(/*Jext*/AbstractEditorPanel.getProperty("editor.style.keyword2"));
			styles[Token.KEYWORD3] = GUIUtilities.parseStyle(/*Jext*/AbstractEditorPanel.getProperty("editor.style.keyword3"));
			styles[Token.LITERAL1] = GUIUtilities.parseStyle(/*Jext*/AbstractEditorPanel.getProperty("editor.style.literal1"));
			styles[Token.LITERAL2] = GUIUtilities.parseStyle(/*Jext*/AbstractEditorPanel.getProperty("editor.style.literal2"));
			styles[Token.OPERATOR] = GUIUtilities.parseStyle(/*Jext*/AbstractEditorPanel.getProperty("editor.style.operator"));
			styles[Token.INVALID] = GUIUtilities.parseStyle(/*Jext*/AbstractEditorPanel.getProperty("editor.style.invalid"));
			styles[Token.LABEL] = GUIUtilities.parseStyle(/*Jext*/AbstractEditorPanel.getProperty("editor.style.label"));
//			styles[Token.METHOD] = GUIUtilities.parseStyle(/*Jext*/AbstractEditorPanel.getProperty("editor.style.method"));

//		styles[Token.COMMENT1] = GUIUtilities.parseStyle(/*Jext*/AbstractEditorPanel.getProperty("Comment 1"));
//		styles[Token.COMMENT2] = GUIUtilities.parseStyle(/*Jext*/AbstractEditorPanel.getProperty("Comment 2"));
//		styles[Token.KEYWORD1] = GUIUtilities.parseStyle(/*Jext*/AbstractEditorPanel.getProperty("Keyword 1"));
//		styles[Token.KEYWORD2] = GUIUtilities.parseStyle(/*Jext*/AbstractEditorPanel.getProperty("Keyword 2"));
//		styles[Token.KEYWORD3] = GUIUtilities.parseStyle(/*Jext*/AbstractEditorPanel.getProperty("Keyword 3"));
//		styles[Token.LITERAL1] = GUIUtilities.parseStyle(/*Jext*/AbstractEditorPanel.getProperty("Literal 1 (chaîne)"));
//		styles[Token.LITERAL2] = GUIUtilities.parseStyle(/*Jext*/AbstractEditorPanel.getProperty("Literal 2 (object)"));
//		styles[Token.OPERATOR] = GUIUtilities.parseStyle(/*Jext*/AbstractEditorPanel.getProperty("Operator"));
//		styles[Token.INVALID] = GUIUtilities.parseStyle(/*Jext*/AbstractEditorPanel.getProperty("Invalid token"));
//		styles[Token.LABEL] = GUIUtilities.parseStyle(/*Jext*/AbstractEditorPanel.getProperty("Label"));
		// $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
		//      styles[Token.METHOD] = GUIUtilities.parseStyle(/*Jext*/AbstractEditorPanel.getProperty(/*"Method"));
		//      il n'y a pas de style pr les methodes Token.METHOD n'existe pas dans cette version c'est a 
		//      dire Token.java;
      
		painter.setStyles(styles);
		} catch(Exception e) {}
	}
	
	/**
	 * Set the size of the font.
	 * @param size The new font's size
	 */
	public void setFontSize(int size)
	{
	  fontSize = size;
	  changeFont();
	  FontMetrics fm = getFontMetrics(getFont());
	  setMinimumSize(new Dimension(80 * fm.charWidth('m'), 5 * fm.getHeight()));
	  repaint();
	}
	
	/**
	 * Set the style of the font.
	 * @param style The new style to apply
	 */
	public void setFontStyle(int style)
	{
	fontStyle = style;
	changeFont();
	repaint();
	}
	
	
	/**
	 * Set the font which has to be used.
	 * @param name The name of the font
	 */
	public void setFontName(String name)
	{
	fontName = name;
	changeFont();
	}
	
	
	/**
	 * Set the new font.
	 */
	private  void changeFont()
	{
	getPainter().setFont(new Font(fontName, fontStyle, fontSize));
	}
	// from Oury ]]
	
	// from PG [[
	public  boolean getSoftTab()
	{
		return AbstractEditorPanel.getBooleanProperty("editor.softTab");
	}
     
     
	public int getTabSize()
	{
		String size = AbstractEditorPanel.getProperty("editor.tabSize");
		if (size == null)
			return 8;

		Integer i = new Integer(size);
		if (i != null)
			return i.intValue();
		else
			return 8;
	}
	// from PG ]]
}