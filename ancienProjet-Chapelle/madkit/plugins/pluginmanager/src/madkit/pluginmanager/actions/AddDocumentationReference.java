/*
 * AddDocumentationReference.java - Created on Feb 24, 2004
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
 * Last Update: $Date: 2004/06/02 10:12:48 $
 */

package madkit.pluginmanager.actions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import madkit.pluginmanager.Action;
import madkit.pluginmanager.Utils;
import madkit.pluginmanager.Version;
import madkit.pluginmanager.XMLWriter;

/**Adds a link in the documentation index of Madkit.<br>
 * The documentation index is divided by sections. Sections can be
 * added at will, however we encourage you
 * to use on of the following sections:<br>
 * <ul>
 * <li><b>api</b>: used when adding a link to an agent or MAS api </li>
 * <li><b>manual</b>: when adding a Manual</li>
 * <li><b>demo</b>:when adding a demostration</li>
 * <li><b>tech</b>: when adding specifications or technical reports for Madkit</li> 
 * </ul>
 * 
 * <br>
 * <br>
 * The required arguments for this actions are:
 * <ul>
 * <li><b>section</b>: defines the documentation section.</li>
 * <li><b>url</b>: link's url </li>
 * <li><b>name</b>: the displayed link name </li>
 * <li><b>absolute</b>: defines whether the url is absolute or relative to the madkit installation directory </li>
 * 
 * </ul>
 * <br>
 * This action is recognized as:
 * adddocumentationreference<br>
 * adddocref<br>
 * docref<br>
 * 
 * @author Sebastian Rodriguez - sebastian.rodriguez@utbm.fr
 *
 * @version $Revision: 1.3 $
 */
public class AddDocumentationReference extends AbstractAction implements Action{
    
    private String _section;//section of this docref
    private String _url;//url
    private String _name;//displayed link name
    private boolean _absoluteLink=false;//if true, _url will be writen as is in the index.html file. Otherwise, the madkit Dir will be added
    private String _reason="ok";
    
    
    private File _docsFile;
    private Document _docs;
    
    //utilities
    private static final String html_start="<html>" +
    										"<head>" +
    										"<title>Madkit Documentation</title>" +
											"</head>" +
											"<body>" +
											"<h1>Madkit Documentation<h1>";
    
    /**
     *
     */
    public AddDocumentationReference(File madkitDirectory,String plugin, Version pluginVersion) {
        super("adddocumentationreference",madkitDirectory, plugin, pluginVersion);
        _docsFile=new File(madkitDirectory.getAbsolutePath()+File.separatorChar+"cache"+File.separatorChar+"docs.xml");
        
    }

    /* (non-Javadoc)
     * @see madkit.pluginmanager.Action#requiredPlugins()
     */
    public Collection requiredPlugins() {
        return new Vector();
    }

    /* (non-Javadoc)
     * @see madkit.pluginmanager.Action#getMandatoryParameters()
     */
    public Collection getMandatoryParameters() {
        Vector v=new Vector();
        v.add("section");
        v.add("url");
        v.add("name");
        return v;
    }

    /* (non-Javadoc)
     * @see madkit.pluginmanager.Action#getOptionalParameters()
     */
    public Collection getOptionalParameters() {
        Vector v=new Vector();
        v.add("absolute");
        return v;
    }

    /* (non-Javadoc)
     * @see madkit.pluginmanager.Action#setParamenters(java.util.Properties)
     */
    public void setParamenters(Properties params) {
        _section=params.getProperty("section");
        _url=params.getProperty("url");
        _name=params.getProperty("name");
        _absoluteLink=Boolean.valueOf(params.getProperty("absolute")).booleanValue();
    }

    /* (non-Javadoc)
     * @see madkit.pluginmanager.Action#execute()
     */
    public boolean execute() {
        if(_section==null || _url==null || _name==null){
            _reason="Insufficient Parameters";
            return false;
        }
        
        //check if docs.xml exists
        if(!_docsFile.exists()){
            createDocumentationRef();
        }
        
        try {
            _docs=openXMLDocument(_docsFile);
            String displayName=null;
            StringTokenizer stk=new StringTokenizer(_section,"::");
            
            _section=stk.nextToken();
            if(stk.hasMoreTokens()){
                displayName=stk.nextToken();
            }else{
                displayName=_section;
            }
            
            NodeList sections=_docs.getElementsByTagName("section");
            
            for(int i=0;i<sections.getLength();i++){
                Element sec= (Element) sections.item(i);
                if(sec.getAttribute("name").equals(_section)){
                    Element ref=_docs.createElement("docref");
                    ref.setAttribute("plugin",getOwnerPlugin());
                    ref.setAttribute("version",getOwnerPluginVersion().toString());
                    ref.setAttribute("url",_url);
                    ref.setAttribute("name",_name);
                    ref.setAttribute("absolute",String.valueOf(_absoluteLink));
                    sec.appendChild(ref);
                }
            }
            
            return XMLWriter.saveDocument(_docsFile,_docs);
            
        } catch (SAXException e) {
            debug("SAXException caught "+e.getMessage());
        } catch (IOException e) {
            debug("IOException caught "+e.getMessage());
        } catch (ParserConfigurationException e) {
            debug("ParserConfigurationException caught "+e.getMessage());
        } catch (FactoryConfigurationError e) {
            debug("FactoryConfigurationError caught "+e.getMessage());
        }
        return false;
    }

    /* (non-Javadoc)
     * @see madkit.pluginmanager.Action#getFailureReason()
     */
    public String getFailureReason() {
        return _reason;
    }
    
    /** Creates an empty Docmentation xml file
    */
    private void createDocumentationRef(){
        String contents="<?xml version=\"1.0\"?>" +
        		"<madkitdocs>" +
        		"<section name=\"manual\" display=\"Manuals\">" +
					"<docref absolute=\"false\" name=\"Content\" url=\"docs/index.html\"/>" +
					"<docref absolute=\"false\" name=\"User's guide\" url=\"docs/userguide/userguide.html\" />" +
					"<docref absolute=\"false\" name=\"Developper's guide\" url=\"docs/devguide/devguide.html\" />" +
				"</section>" +
				"<section name=\"api\" display=\"API\"></section>" +
        	//	"<section name=\"tech\" display=\"Technical Reports\"></section>" +
        	//	"<section name=\"demo\" display=\"Demos\"></section>" +
        		"</madkitdocs>";
        try {
            Utils.writeToFile(contents,new FileOutputStream(_docsFile));
        } catch (FileNotFoundException e) {
            debug("FileNotFoundException caught "+e.getMessage());
        }
    }
    
    /**Generates the Madkit Documentation Index
     * @throws SAXException
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws FactoryConfigurationError
     */
    private void generateHtmlIndex() throws SAXException, IOException, ParserConfigurationException, FactoryConfigurationError{
        //check if docs.xml exists
        if(!_docsFile.exists()){
            createDocumentationRef();
        }
        
        if(_docs==null){
            _docs=openXMLDocument(_docsFile);
        }
        
        File target=new File(_madkitDir.getAbsolutePath()+File.separatorChar+"docs"+File.separatorChar+"plugins.html");
        target.getParentFile().mkdirs();//make the path dirs
        
        String htmlContents=html_start.toString();
        NodeList sections=_docs.getElementsByTagName("section");
        
        for(int i=0;i<sections.getLength() ; i++){
            Element sec=(Element) sections.item(i);
            htmlContents+="<h2>"+sec.getAttribute("display")+"</h2><ul>";
            NodeList docslist=sec.getElementsByTagName("docref");
            for(int j=0;j<docslist.getLength();j++){
                Element docref=(Element) docslist.item(j);
                htmlContents+=getHtmlDocLink(docref);
            }
            htmlContents+="</ul><br>";
        }
        htmlContents+="</body><html>";
        Utils.writeToFile(htmlContents,new FileOutputStream(target));
    }

    /**Creates a string with the html anchor representing the link to the documentation file.
     * @param docref Element of the reference
     * @return html anchor to the Doc file.
     */
    private String getHtmlDocLink(Element docref) {
        String link="<li>" +
        		"<a href=\"";
        if(Boolean.valueOf(docref.getAttribute("absolute")).booleanValue()){
            link+=docref.getAttribute("url");    
        }else{
            link+="file://"+_madkitDir.getAbsolutePath()+"/"+docref.getAttribute("url");
        }
        link+="\">"+docref.getAttribute("name")+"</a></li>";
        return link;
    }
    
    private void removePlugin() throws SAXException, IOException, ParserConfigurationException, FactoryConfigurationError{
        if(!_docsFile.exists()){
            return;
        }
        if(_docs==null){
            _docs=openXMLDocument(_docsFile);
        }
        NodeList sections=_docs.getElementsByTagName("section");
        
        for(int i=0;i<sections.getLength();i++){
            Element sec= (Element) sections.item(i);
            NodeList docrefs=sec.getElementsByTagName("docref");
            for(int j=0 ; j<docrefs.getLength() ; j++){
                Element dr=(Element) docrefs.item(j);
                if(dr.getAttribute("plugin").equals(getOwnerPlugin()) && dr.getAttribute("version").equals(getOwnerPluginVersion().toString())){
                    sec.removeChild(dr);
                }
            }
            
        }
        
        XMLWriter.saveDocument(_docsFile,_docs);
        
    }
    
    /**Creates the Madkit Html Documentation index.
     * @param madkitDirectory
     * @throws IOException
     */
    public static final void generateHtmlDocumentation(File madkitDirectory) throws IOException{
        AddDocumentationReference ac=new AddDocumentationReference(madkitDirectory,null,null);
        try {
            ac.generateHtmlIndex();
        } catch (SAXException e) {
            ac.debug("SAXException caught "+e.getMessage());
        } catch (IOException e) {
            ac.debug("IOException caught "+e.getMessage());
        } catch (ParserConfigurationException e) {
            ac.debug("ParserConfigurationException caught "+e.getMessage());
        } catch (FactoryConfigurationError e) {
            ac.debug("FactoryConfigurationError caught "+e.getMessage());
        }
    }
    
    public static final void removePlugin(File madkitDirectory,String plugin, Version pluginVersion) throws IOException{
        AddDocumentationReference ac=new AddDocumentationReference(madkitDirectory,plugin , pluginVersion);
        
        try {
            ac.removePlugin();
        } catch (SAXException e) {
            ac.debug("SAXException caught "+e.getMessage());
        } catch (ParserConfigurationException e) {
            ac.debug("ParserConfigurationException caught "+e.getMessage());
        } catch (FactoryConfigurationError e) {
            ac.debug("FactoryConfigurationError caught "+e.getMessage());
        }
    }

    /* (non-Javadoc)
     * @see madkit.pluginmanager.Action#shouldAskUser()
     */
    public boolean shouldAskUser() {
        return false;
    }

    /* (non-Javadoc)
     * @see madkit.pluginmanager.Action#getQuestionToUser()
     */
    public String getQuestionToUser() {
        return " ";
    }
}
