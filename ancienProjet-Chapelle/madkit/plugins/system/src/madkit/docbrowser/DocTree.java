/*
 * DocTree.java - Created on Feb 25, 2004
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
 * Last Update: $Date: 2004/06/02 10:15:31 $
 */

package madkit.docbrowser;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

import java.net.URL;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.ImageIcon;
import javax.swing.event.*;
import javax.swing.tree.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * @author Sebastian Rodriguez - sebastian.rodriguez@utbm.fr
 *
 * @version $Revision: 1.2 $
 */
class DocTree extends JPanel implements TreeSelectionListener{
    private File docFile; 
    private Document documentation=null;
    private JTree sectionTree;
    private BrowserGUI browser;
    private Hashtable pluginsDoc;
    private DefaultMutableTreeNode top;
    private DefaultTreeModel treemodel;
    
    public DocTree(File docFile,BrowserGUI browserGui){
        browser=browserGui;
        this.docFile=docFile;
        
        pluginsDoc=new Hashtable();
        
        setLayout(new BorderLayout());
        
        loadSections();
        
        JScrollPane sc=new JScrollPane();
        sc.setViewportView(sectionTree);
        
        add(sc,BorderLayout.CENTER);
    }
    
    private void loadSections(){
        String indexURL="file:"+docFile.getParentFile().getParentFile().getPath()+"/docs/index.html";
        top=new DefaultMutableTreeNode(new DocLink("Madkit","Madkit Documentation",indexURL,"madkit"));

		treemodel = new DefaultTreeModel(top);
        try {
            if(documentation==null){
                loadDoc();
            }
            
            
            createNodesBySections(top);
                
        
        } catch (SAXException e) {
            debug("SAXException caught "+e.getMessage());
        } catch (IOException e) {
            debug("IOException caught "+e.getMessage());
        } catch (ParserConfigurationException e) {
            debug("ParserConfigurationException caught "+e.getMessage());
        } catch (FactoryConfigurationError e) {
            debug("FactoryConfigurationError caught "+e.getMessage());
        }
        
        sectionTree=new JTree(treemodel);
        sectionTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

		sectionTree.setShowsRootHandles(true);
		sectionTree.setRootVisible(true);
		
		DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
		renderer.setLeafIcon(new ImageIcon(getClass().getResource("/images/window/HtmlFile.gif")));
		sectionTree.setCellRenderer(renderer);

        //Listen for when the selection changes.
        sectionTree.addTreeSelectionListener(this);

    }
    
    private static void debug(String string){
        System.err.println("**debug** DocTree : "+string);
    }
    
    private void loadDoc() throws SAXException, IOException, ParserConfigurationException, FactoryConfigurationError{
        documentation=DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(docFile);
    }
    
    private void createNodesBySections(DefaultMutableTreeNode top){
        DefaultMutableTreeNode section=null;
        DefaultMutableTreeNode docs=null;
        String urlPrefix="file:"+docFile.getParentFile().getParentFile().getPath()+File.separatorChar;
        
        NodeList sections=documentation.getElementsByTagName("section");
        
        for(int i=0;i<sections.getLength() ; i++){
            Element sec=(Element) sections.item(i);
            section=new DefaultMutableTreeNode(sec.getAttribute("display"));//create a top level node for this section
            NodeList docslist=sec.getElementsByTagName("docref");
            for(int j=0;j<docslist.getLength();j++){
                Element docref=(Element) docslist.item(j);
                String url;
                if(Boolean.valueOf(docref.getAttribute("absolute")).booleanValue()){
                    url=docref.getAttribute("url");
                }else{
                    url=urlPrefix+docref.getAttribute("url");
                }
                
                DocLink link=new DocLink(sec.getAttribute("name"),//section name
                        				docref.getAttribute("name"),//display's the DocRef name
                        				url,
                        				docref.getAttribute("plugin")
                        				);
                docs=new DefaultMutableTreeNode(link);
                section.add(docs);
                addPluginLnk(link._plugin,link);
            }
            top.add(section);
            
        }
        
    }
    
	DefaultMutableTreeNode findSection(String section){
		 for (Enumeration c = top.children(); c.hasMoreElements() ;) {
			DefaultMutableTreeNode sectionNode=(DefaultMutableTreeNode) c.nextElement();
			if (((String)sectionNode.getUserObject()).equals(section))
				return sectionNode;
		}
		return null;
	}
	
   void addLink(String plugin, DocLink link){
   		DefaultMutableTreeNode sectionNode = findSection(link.getSection());
   		if (sectionNode == null){
   			sectionNode = new DefaultMutableTreeNode(link.getSection());
			treemodel.insertNodeInto(sectionNode, top, treemodel.getChildCount(top));
   		}
   		DefaultMutableTreeNode docs=new DefaultMutableTreeNode(link);

   		treemodel.insertNodeInto(docs, sectionNode, treemodel.getChildCount(sectionNode));
   		sectionNode.add(docs);
   		addPluginLnk(plugin,link);
   }
   
   
   void addPluginLnk(String plugin, DocLink link){
        Collection v=(Collection) pluginsDoc.remove(plugin);
        if(v==null){
            v=new Vector();
        }
        v.add(link);
        pluginsDoc.put(plugin,v);
    }
    /* (non-Javadoc)
     * @see javax.swing.event.TreeSelectionListener#valueChanged(javax.swing.event.TreeSelectionEvent)
     */
    public void valueChanged(TreeSelectionEvent e) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)sectionTree.getLastSelectedPathComponent();
        
        if (node == null) return;

        Object nodeInfo = node.getUserObject();
        if (nodeInfo instanceof DocLink) {
            DocLink link = (DocLink)nodeInfo;
            displayURL(link._url);
            
        } 
        
    }

    /**
     * @param string
     */
    private void displayURL(String url) {
        browser.setPage(url);
        
    }

    /**
     * @param pluginName
     * @param section
     * @return
     */
    public String getDocURL(String pluginName, String section) {
        Collection secs=(Collection) pluginsDoc.get(pluginName);
        if(secs==null || secs.isEmpty()){
            return null;
        }
        for (Iterator it = secs.iterator(); it.hasNext();) {
                DocLink element = (DocLink) it.next();
                if(element._section.equals(section)){
                    return element._url;
                }
         }
        return null;
    }

    /**
     * @param pluginName
     * @return
     */
    public String getHtmlForPlugin(String pluginName) {
        String html="<html><body>";
        html+="<h1>"+pluginName+" Documentation<h1>";
        Collection c=(Collection) pluginsDoc.get(pluginName);
        if(c!=null){
            html+="<ul>";
            for (Iterator iter = c.iterator(); iter.hasNext();) {
                DocLink element = (DocLink) iter.next();
                html+="<li><a href=\""+element._url+"\">"+element._display+"</a></li>";
            }
            html+="</ul>";
        }
        html+="</body><html>";
        return html;
    }
}

class DocLink{
    String _url;
    String _display;
    String _section;
    String _plugin;
    
    public DocLink(String section, String display, String url, String plugin ){
        _section=section;
        _display=display;
        _url=url;
        _plugin=plugin;
    }
    
    public String toString(){
        return _display.toString();
    }
    
    String getSection(){return _section;}
    String getPluginName(){return _plugin;}
    String getDisplay(){return _display;}
    String getUrl() {return _url;}
}