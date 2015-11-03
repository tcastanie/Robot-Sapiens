/*
* HelpBox.java - SEdit, a tool to design and animate graphs in MadKit
* Copyright (C) 1998-2002 Jacques Ferber, Olivier Gutknecht
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
package  SEdit;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.Document;


 class HelpBox extends JFrame {
    Formalism formalism;
    
    public HelpBox(String filename)
    {
	this(filename,null);
    }

    public HelpBox(String filename, Formalism f) {
	super("Help");
	formalism=f;
	
	setBounds( 200, 25, 400, 400);
	JPanel p = new JPanel();

	//	p.setOpaque(false); 
       	p.setOpaque(true);
       	p.setBackground(UIManager.getColor("window"));
	p.setLayout(new BorderLayout() );
	HtmlPane html = new HtmlPane(filename,formalism);
	

	p.add(html, BorderLayout.CENTER);
	//		p.add(buildToolBar(), BorderLayout.NORTH);

	getContentPane().add(p);
	show();
	
    }

    protected JToolBar buildToolBar() {
        JToolBar bar = new JToolBar();
	bar.add(new JButton("<<"));
	bar.add(new JButton(">>"));
	return bar;
    }
    public void pack() {
       Dimension size = getPreferredSize();
       setSize(size.width, size.height);
    }

}


class HtmlPane extends JPanel implements HyperlinkListener {
    JEditorPane html;
    Object base;
    
        
    public HtmlPane(String filename, Object b) {
        setLayout(new BorderLayout());
	base = b;
	
	try {
	    URL url;
	    
	    if (filename.startsWith("http"))
		url = new URL(filename);
	    else
		url = new URL ("file:"+(new File(filename)).getCanonicalPath());
	    
	    html = new JEditorPane(url);
	    html.setEditable(false);
	    html.addHyperlinkListener(this);
	    	    
	    JScrollPane scroller = new JScrollPane();
	    JViewport vp = scroller.getViewport();
	    vp.add(html);
	    add(scroller, BorderLayout.CENTER);
	} catch (MalformedURLException e) {
	    System.out.println("Malformed URL: " + e);
	} catch (IOException e) {
	    System.out.println("IOException: " + e);
	}	
    }

    /**
     * Notification of a change relative to a 
     * hyperlink.
     */
    public void hyperlinkUpdate(HyperlinkEvent e) {
	if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
	    linkActivated(e.getURL());
	}
    }

    /**
     * Follows the reference in an
     * link.  The given url is the requested reference.
     * By default this calls <a href="#setPage">setPage</a>,
     * and if an exception is thrown the original previous
     * document is restored and a beep sounded.  If an 
     * attempt was made to follow a link, but it represented
     * a malformed url, this method will be called with a
     * null argument.
     *
     * @param u the URL to follow
     */
    protected void linkActivated(URL u) {
	Cursor c = html.getCursor();
	Cursor waitCursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
	html.setCursor(waitCursor);
	SwingUtilities.invokeLater(new PageLoader(u, c));
    }

    /**
     * temporary class that loads synchronously (although
     * later than the request so that a cursor change
     * can be done).
     */
    class PageLoader implements Runnable {
	
	PageLoader(URL u, Cursor c) {
	    url = u;
	    cursor = c;
	}

        public void run() {
	    if (url == null) {
		// restore the original cursor
		html.setCursor(cursor);

		// PENDING(prinz) remove this hack when 
		// automatic validation is activated.
		Container parent = html.getParent();
		parent.repaint();
	    } else {
		Document doc = html.getDocument();
		try {
		    html.setPage(url);
		} catch (IOException ioe) {
		    html.setDocument(doc);
		    getToolkit().beep();
		} finally {
		    // schedule the cursor to revert after
		    // the paint has happended.
		    url = null;
		    SwingUtilities.invokeLater(this);
		}
	    }
	}

	URL url;
	Cursor cursor;
    }

}
