/*
 * BrowserGUI.java - Created on Feb 25, 2004
 * 
 * Copyright (C) 2003-2004 Sebastian Rodriguez
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.

 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * Last Update: $Date: 2004/05/05 09:50:22 $
 */

package madkit.docbrowser;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import madkit.kernel.AbstractAgent;
//import madkit.pluginmanager.Utils;


/**
 * @author Sebastian Rodriguez - sebastian.rodriguez@utbm.fr
 *
 * @version $Revision: 1.1 $
 */
class BrowserGUI extends JPanel implements HyperlinkListener {

    
    
    private JTextField _urlField;
    private JEditorPane _htmlPane;
    private NavigationBar _navBar;
    private File _docXMLFile;
    private DocTree tree;
    AbstractAgent _agent;
    
    public BrowserGUI(File docXMLFile, AbstractAgent agent){
        _agent=agent;
        _docXMLFile=docXMLFile;
        setLayout(new BorderLayout());
        //create navigation bar
        _urlField=new JTextField();
        _urlField.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent e) {
                setPage(_urlField.getText());
                
            }
        });

        JPanel urlPanel=new JPanel();
        urlPanel.setLayout(new BorderLayout());
        urlPanel.add(_urlField,BorderLayout.CENTER);
        urlPanel.add(new JLabel("Location :"),BorderLayout.WEST);
        JButton goBtn=new JButton("Go");
        goBtn.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent e) {
                setPage(_urlField.getText());
                
            }
        });
        
        urlPanel.add(goBtn,BorderLayout.EAST);
        _navBar=new NavigationBar(this);
        
        JPanel topPanel=new JPanel();
        topPanel.setLayout(new BorderLayout());
        JToolBar navtool=new JToolBar();
        navtool.add(_navBar);
        
        topPanel.add(navtool,BorderLayout.WEST);
        
        JToolBar urltool=new JToolBar();
        urltool.add(urlPanel);
        
        topPanel.add(urltool,BorderLayout.SOUTH);

        JMenuBar helptool=new JMenuBar();
        helptool.add(new MadkitHelpMenu(_agent,"docbrowser","DocBrowser Plugin"));
        
        topPanel.add(helptool,BorderLayout.NORTH);

        //create Html pane
        _htmlPane=new JEditorPane();
        _htmlPane.addHyperlinkListener(this);
        
        _htmlPane.setEditable(false);
        
        
        tree=new DocTree(_docXMLFile,this);
        
        
        //put in place
        
        add(topPanel,BorderLayout.NORTH);
        
        JSplitPane split=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        
        JScrollPane sc1=new JScrollPane();
        sc1.setViewportView(_htmlPane);
        
        split.setRightComponent(sc1);
        split.setLeftComponent(tree);
        
        add(split,BorderLayout.CENTER);
        setPreferredSize(new Dimension(600,400));
    }
    
    
    void setPage(String url){
        try {
            _htmlPane.setPage(url);
            _urlField.setText(url);
            _navBar.setPage(url);
        } catch (IOException e) {
            setErrorPage(url,e);
        }
        
    }
    
    /**
     * @param e
     */
    private void setErrorPage(String url,IOException e) {
        String htmlError="<html><head><title>Error</title></head><body>";
        htmlError+="<h1>ERROR </h1> Impossible to load "+url+" <br> "+e.getMessage()+"</body></html>";
        
        
        _htmlPane.setText(htmlError);
        
        
    }


    /* (non-Javadoc)
     * @see javax.swing.event.HyperlinkListener#hyperlinkUpdate(javax.swing.event.HyperlinkEvent)
     */
    public void hyperlinkUpdate(HyperlinkEvent e) {
        if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
            setPage(e.getURL().toExternalForm());
        }
    }


    /**
     * @param pluginName
     * @param section
     */
    void displayDoc(String pluginName, String section) {
        if(section.equalsIgnoreCase("all")){
            String html=tree.getHtmlForPlugin(pluginName);
            String targetURL=_docXMLFile.getParentFile().getAbsolutePath()+File.separatorChar+pluginName+"-all-sections.html";
            File target=new File(targetURL);
            try {
                writeToFile(html,new FileOutputStream(target));
                
            } catch (FileNotFoundException e) {
                
                debug("FileNotFoundException caught "+e.getMessage());
            }
            setPage("file://"+targetURL);
            
        }else{
            String url=tree.getDocURL(pluginName,section);
            if(url!=null){
                setPage(url);
            }else{
                debug("no such plugin and section"+pluginName+" "+section);
            }
        }
    }
        
		/**
			* @param pluginName
			* @param section
			*/
	   void displayLink(String name, DocLink link) {
	   		tree.addLink(name,link);
	   }

	public static boolean writeToFile(String string, FileOutputStream out){
			PrintWriter pw=new PrintWriter(out);
			pw.println(string);
			pw.flush();
			pw.close();
			return true;
		}
    /**
     * @param string
     */
    private void debug(String string) {
        System.err.println("**debug BrowserGUI** : "+string);
        
    }

}

class NavigationBar extends JPanel{
    private BrowserGUI _parent;
    private JBrowseButton _back;
    private JBrowseButton _forward;
    private JBrowseButton _reload;
    
    private final static int BROWSER=99;
    
    public NavigationBar (BrowserGUI parent){
        _parent=parent;
        _back=new JBrowseButton(this,JBrowseButton.BACK_BROWSE);
        _forward=new JBrowseButton(this,JBrowseButton.FORWARD_BROWSE);
        _reload=new JBrowseButton(this,JBrowseButton.RELOAD_BROWSE);
        
        _back.setForwardButton(_forward);
        add(_back);
        add(_forward);
        add(_reload);

    }
    
    boolean propagate=true;
    int lastsender=BROWSER;
    
    public void setPage(String url){
        if(propagate){
            lastsender=BROWSER;
        }
        doSetPage(url);
        
    }
    
    private void doSetPage(String url){
        switch (lastsender) {
        case BROWSER:
            _back.pageChanged(url);
            _reload.setCurrentPage(url);
            _forward.reset();
            break;
        case JBrowseButton.FORWARD_BROWSE:
            _back.pageChanged(url);
        	_reload.setCurrentPage(url);
            break;
        case JBrowseButton.BACK_BROWSE:
        	_reload.setCurrentPage(url);
        	_back.setCurrentPage(url);
            break;
          
        default:
            break;
        }
        
        propagate=true;
    }
    
    public void changePage(String url,int sender){
        propagate=false;
        lastsender=sender;
        _parent.setPage(url);
        
    }
}