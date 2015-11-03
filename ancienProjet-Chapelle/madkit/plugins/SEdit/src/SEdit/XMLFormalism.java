/*
* XMLFormalism.java - SEdit, a tool to design and animate graphs in MadKit
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
package SEdit;

import java.util.Hashtable;
import java.util.Vector;

//import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.xml.parsers.*;

import org.apache.xerces.jaxp.*;			// Xerces
import java.io.IOException;


public class XMLFormalism
{
    protected Formalism formalism;

    /** Parses the resulting document tree. */
    public Formalism parse(String uri)
    {

        try {
//	    DOMParser parser = new DOMParser();
//	    parser.parse(uri);
//	    Document document = parser.getDocument();
		Document document = DocumentBuilderFactoryImpl.newInstance().newDocumentBuilder().parse(uri);
		System.out.println("Document well formed :" + uri);
            parseFormalism(document.getDocumentElement());
			System.out.println("Document validated :" + uri);
	    return formalism;
	}
        
    catch (IOException e){
    	System.err.println("** Error : file cannot be read "+uri);
    }
	catch (SAXException e){
		System.err.println("** Error : SAX error in " + uri);
		System.err.println(e.getMessage());
	}
	catch (ParserConfigurationException e){
		System.err.println("** Error : ParserConfiguration error in " + uri);
		System.err.println(e.getMessage());
	}
   catch (ArithmeticException e) {
			System.err.println("** Error : formalism parsing error in " + uri);
            //e.printStackTrace(System.err);
	}
        return null;
    }


    void parseFormalism(Element f)
    {
	NodeList list;
	Node head;

	Hashtable prophash = new Hashtable();

	formalism = new Formalism(f.getAttribute("name"));

	String description = f.getAttribute("description");
	if (!description.equals(""))
	    formalism.setDescription(description);

	String structure = f.getAttribute("class");
	if (!structure.equals(""))
	    formalism.setStructureClass(structure);

	NodeList childs = f.getChildNodes();
	for (int j = 0; j < childs.getLength(); j++)
	    if (childs.item(j).getNodeType()==Node.ELEMENT_NODE)
		{
		    Element child =(Element) childs.item(j);
		    if (child.getNodeName().equals("formalism-info"))
			parseFormalismInfo(child);

		    if (child.getNodeName().equals("connector-types"))
			parseConnectors(child.getChildNodes());
		    if (child.getNodeName().equals("node-types"))
			parseNodes(child.getChildNodes());
		    if (child.getNodeName().equals("arrow-types"))
			parseArrows(child.getChildNodes());
		    if (child.getNodeName().equals("action"))
			    formalism.addActionDesc(parseAction(child));
            if (child.getNodeName().equals("graphic-element")){
                String graphicClass = child.getAttribute("class");
                //if (!graphicClass.equals(""))
                //    nd.setGraphicClass(graphicClass);
                //System.err.println("nodeselement2"+name+" "+nd.getGraphicClass());
                Hashtable graphprop = new Hashtable();
                NodeList graphproplist = child.getChildNodes();
                for (int m = 0; m < graphproplist.getLength(); m++)
                    if (graphproplist.item(m).getNodeType()==Node.ELEMENT_NODE)
                        {
                        Element prop = (Element) graphproplist.item(m);
                        if (prop.getNodeName().equals("property"))
                            addProperty(graphprop, prop);
                        }
                formalism.setGraphicProperties(graphprop);
                //System.out.println("graphicProperties: " + graphprop);
			}
            if (child.getNodeName().equals("property")) {
				addProperty(prophash, child);
				formalism.setProperties(prophash);
			}
		}
    }


    ActionDesc parseAction(Element e)
    {
	NodeList nodes;

	int type = ActionDesc.UNKNOWN;
	String icon = null;
	String value = null;
	String description = e.getAttribute("description");

        nodes = e.getElementsByTagName("icon");
	if (nodes.getLength() > 0)
	    icon = ((Element)nodes.item(0)).getAttribute("url");

	nodes = e.getElementsByTagName("java-method");
	if (nodes.getLength() > 0)
	    {
		value = ((Element)nodes.item(0)).getAttribute("name");
		type = ActionDesc.METHOD_CALL;
	    }
	else
	    {
		nodes = e.getElementsByTagName("scheme-function");
		if (nodes.getLength() > 0)
		    {
			value = ((Element)nodes.item(0)).getAttribute("name");
			type = ActionDesc.FUNCTION_CALL;
		    }
	    }

	return new ActionDesc(type, value, description, icon);
    }



    Hashtable getProperties(NodeList list)
    {
	Hashtable h = new Hashtable();

	int len = list.getLength();
	for (int i = 0; i < len; i++)
	    {
		if(list.item(i).getNodeType()==Node.ELEMENT_NODE)
		    {
			Element prop = (Element) list.item(i);
			String name = prop.getAttribute("name");
			String  value = prop.getFirstChild().getNodeValue();
			//			String value = prop.getAttribute("value");
			//if (value == null)
			//value = prop.getFirstChild().getNodeValue();
			// System.err.println("n/v"+name+" "+value);
			h.put(name,value);
		    }
	    }
	// System.err.println("proplist:"+h);

	return h;
    }



    void addProperty(Hashtable t, Element prop)
    {
	String name = prop.getAttribute("name");
	String  value = prop.getFirstChild().getNodeValue();
	//			String value = prop.getAttribute("value");
	//if (value == null)
	//value = prop.getFirstChild().getNodeValue();
	// System.err.println("n/v"+name+" "+value);
	t.put(name,value);
    }



    void parseNodes(NodeList list)
    {
	int len = list.getLength();
	for (int i = 0; i < len; i++)
	    {
		Hashtable prophash = new Hashtable();
		NodeDesc nd = null;

		if(list.item(i).getNodeType()==Node.ELEMENT_NODE)
		    {
			Element node = (Element) list.item(i);
			String name = node.getAttribute("name");
			String catg = node.getAttribute("category");
			String desc = node.getAttribute("description");
			String cls = node.getAttribute("class");
			String icon = null;

			NodeList childs = node.getChildNodes();
			boolean component=false;
			for (int j = 0; j < childs.getLength(); j++)
			    if (childs.item(j).getNodeType()==Node.ELEMENT_NODE)
				{
				    Element child =(Element) childs.item(j);
				    if (child.getNodeName().equals("module"))
					component=true;
				}
			if (component)
			    nd = new ComponentDesc(name, desc, cls);
			else
			    nd = new NodeDesc(name, desc, cls);
			nd.setFormalism(formalism);

			for (int j = 0; j < childs.getLength(); j++)
			    if (childs.item(j).getNodeType()==Node.ELEMENT_NODE)
				{
				    Element child =(Element) childs.item(j);
				    if (child.getNodeName().equals("icon"))
					{
					    //NodeList icontag = node.getElementsByTagName("icon");
					    //	  if (icontag.getLength() > 0)
					    //(Element)
					    icon = child.getAttribute("url");
					    nd.setIcon(icon);
					}
				    if (child.getNodeName().equals("module"))
					{
					    String type = child.getAttribute("type");
					    //					    System.err.println("Module detected:"+type);
					    int codemodule=0;

					    if (type.toLowerCase().equals("free"))
						codemodule = ComponentDesc.MODULE_FREE;
					    if (type.toLowerCase().equals("template"))
						codemodule = ComponentDesc.MODULE_TEMPLATE;
					    if (type.toLowerCase().equals("fixed"))
						codemodule = ComponentDesc.MODULE_FIXED;

					    String layout = child.getAttribute("layout");
					    // System.err.println("Module layout detected:"+layout);
					    int codelayout=0;

					    if (layout.toLowerCase().equals("auto"))
						codelayout = ComponentDesc.MODULE_PLACEMENT_AUTO;
					    if (layout.toLowerCase().equals("justified"))
						codelayout = ComponentDesc.MODULE_PLACEMENT_JUSTIFIED;
					    if (layout.toLowerCase().equals("manual"))
						codelayout = ComponentDesc.MODULE_PLACEMENT_MANUAL;


					    //nd = new ComponentDesc(name, desc, cls, icon, codemodule, codelayout);
					    ((ComponentDesc)nd).setModule(codemodule);
					    ((ComponentDesc)nd).setLayout(codelayout);


					    NodeList connlist = child.getChildNodes();
					    for (int k = 0; k < connlist.getLength(); k++)
						if (connlist.item(k).getNodeType()==Node.ELEMENT_NODE)
						    {
							Element prop = (Element) connlist.item(k);
							if(connlist.item(k).getNodeType()==Node.ELEMENT_NODE)
							    {
								String cref   = prop.getAttribute("type");
								String cname  = prop.getAttribute("name");
								String pside   = prop.getAttribute("side");
								String pratio = prop.getAttribute("ratio");

								int codeside = ConnectorPlacement.UNSPECIFIED;
								if (pside.toLowerCase().equals("left"))
								    codeside = ConnectorPlacement.LEFT;
								if (pside.toLowerCase().equals("right"))
								    codeside = ConnectorPlacement.RIGHT;
								if (pside.toLowerCase().equals("top"))
								    codeside = ConnectorPlacement.TOP;
								if (pside.toLowerCase().equals("bottom"))
								    codeside = ConnectorPlacement.BOTTOM;
								double ratio = -1;
								if ((pratio!=null) && (!pratio.equals("")))
								    ratio = (new Double(pratio)).doubleValue();

								ConnectorPlacement cp;
								if ((ratio < 0) || (ratio > 1))
								    cp = new ConnectorPlacement(codeside);
								else
								    cp = new ConnectorPlacement(codeside, ratio);

								ConnectorDesc cnx;
								cnx = formalism.getConnectorDesc(cref);
								// System.err.println("got cnx+"+cref+"("+cname+")"+cnx+" Pl "+cp);

								((ComponentDesc)nd).addModuleConnector(cname, cnx, cp);
							    }
						    }
					}
				    if (child.getNodeName().equals("graphic-element"))
					{
					    String graphicClass = child.getAttribute("class");
					    if (!graphicClass.equals(""))
						nd.setGraphicClass(graphicClass);
					    //System.err.println("nodeselement2"+name+" "+nd.getGraphicClass());
					    Hashtable graphprop = new Hashtable();
					    NodeList graphproplist = child.getChildNodes();
					    for (int m = 0; m < graphproplist.getLength(); m++)
						if (graphproplist.item(m).getNodeType()==Node.ELEMENT_NODE)
						    {
							Element prop = (Element) graphproplist.item(m);
							if (prop.getNodeName().equals("property"))
							    addProperty(graphprop, prop);
						    }
					    nd.setGraphicProperties(graphprop);
					    // System.err.println(i+" "+j+" "+"IN PazeROPERTY NDGR"+child+
								  //proplist.getLength()+" "+
					    //		  nd.getGraphicProperties());
					}
				    if (child.getNodeName().equals("property"))
					{
					    addProperty(prophash, child);
					    nd.setProperties(prophash);
					}
				    if (child.getNodeName().equals("action"))
					nd.addActionDesc(parseAction(child));
				}
			if (nd!=null)
			    {
				nd.setCategory(catg);
				formalism.addNodeDesc(nd);
				try {
				    ReflectorUtil.setProperties(nd,prophash);
				}
				catch(Exception e) {
				    Utils.log("setProperties failed:"+e);
				}
			    }
		    }
	    }
    }



    void parseConnectors(NodeList list)
    {
	int len = list.getLength();
	for (int i = 0; i < len; i++)
	    {
		if(list.item(i).getNodeType()==Node.ELEMENT_NODE)
		    {
			Element node = (Element) list.item(i);
			String name = node.getAttribute("name");
			String desc = node.getAttribute("description");
			String cls = node.getAttribute("class");
			String mode = node.getAttribute("mode");
			String catg = node.getAttribute("category");
			String icon = null;

			NodeList icontag = node.getElementsByTagName("icon");
			if (icontag.getLength() > 0)
			    icon = ((Element)icontag.item(0)).getAttribute("url");

			ConnectorDesc nd = new ConnectorDesc(name, desc, cls, mode);
			nd.setIcon(icon);

			nd.setCategory(catg);
			formalism.addConnectorDesc(nd);

			NodeList graphic = node.getElementsByTagName("graphic-element");
			if (graphic.getLength() > 0)
			    {
				// System.err.println("nodeselement-"+name+" "+nd.getGraphicClass());
				String graphicClass = ((Element)graphic.item(0)).getAttribute("class");
				if (!graphicClass.equals(""))
				    nd.setGraphicClass(graphicClass);
				// System.err.println("nodeselement+"+name+" "+nd.getGraphicClass());
				NodeList proplist = ((Element)graphic.item(0)).getElementsByTagName("property");

				if (proplist.getLength() > 0)
				    nd.setGraphicProperties(getProperties(proplist));
				// System.err.println("IN PROPERTY NDGR"+proplist.getLength()+" "+nd.getGraphicProperties());
					node.removeChild(graphic.item(0));
			    }

			NodeList proplist = node.getElementsByTagName("property");
			if (proplist.getLength() > 0)
			    try {// System.err.println("IN PROPERTY ND"+proplist);
			    ReflectorUtil.setProperties(nd,getProperties(proplist));
			    }
			    catch(Exception e) {
				Utils.log("setProperties failed:"+e);
			    }


		    }
	    }
    }


    void parseArrows(NodeList list)
    {
	int len = list.getLength();
	for (int i = 0; i < len; i++)
	    {
		if(list.item(i).getNodeType()==Node.ELEMENT_NODE)
		    {
			Element node = (Element) list.item(i);
			String name = node.getAttribute("name");
			String desc = node.getAttribute("description");
			String cls = node.getAttribute("class");
			String catg = node.getAttribute("category");
			String from = null;
			String to = null;
			String icon = null;

			NodeDesc ndfrom, ndto;
			ndfrom = formalism.getNodeDesc(node.getAttribute("from"));
			//System.err.println(nd);
			if (ndfrom==null)
			ndfrom = formalism.getConnectorDesc(node.getAttribute("from"));
			if (ndfrom!=null)
			    from = ndfrom.getElementClass();

			ndto = formalism.getNodeDesc(node.getAttribute("to"));
			if (ndto==null)
			    ndto = formalism.getConnectorDesc(node.getAttribute("to"));
			if (ndto!=null)
			    to = ndto.getElementClass();


			NodeList icontag = node.getElementsByTagName("icon");
			if (icontag.getLength() > 0)
			    icon = ((Element)icontag.item(0)).getAttribute("url");

			ArrowDesc ad = new ArrowDesc(name, desc, cls, icon, from, to, ndfrom, ndto);//formalism.getNodeDesc(from), formalism.getNodeDesc(to));
			//			System.err.println("LOADXF"+from+to+"|"+ndfrom+
				//	   " /"+ ndto+" "+
			//   formalism.getNodeDescList());
			ad.setCategory(catg);
			formalism.addArrowDesc(ad);
			ad.setFormalism(formalism);

			NodeList graphic = node.getElementsByTagName("graphic-element");
			if (graphic.getLength() > 0)
			    {
				String graphicClass = ((Element)graphic.item(0)).getAttribute("class");
				if (!graphicClass.equals(""))
				    ad.setGraphicClass(graphicClass);
				// System.err.println("calsselementpost"+ad.getGraphicClass());
				NodeList proplist = ((Element)graphic.item(0)).getElementsByTagName("property");
				if (proplist.getLength() > 0)
				    ad.setGraphicProperties(getProperties(proplist));
				node.removeChild(graphic.item(0));
			    }

			NodeList proplist = node.getElementsByTagName("property");
			if (proplist.getLength() > 0)
			    try {ReflectorUtil.setProperties(ad,getProperties(proplist));
			    }
			    catch(Exception e) {
				Utils.log("setProperties failed:"+e);
			    }
			NodeList actionList = node.getElementsByTagName("action");
			for (int j = 0; j < actionList.getLength(); j++)
			    if (actionList.item(j).getNodeType()==Node.ELEMENT_NODE)
				ad.addActionDesc(parseAction((Element) actionList.item(j)));
		    }
	    }
    }

    void parseFormalismInfo(Element node)
    {
	NodeList authors = node.getElementsByTagName("author");
	if (authors.getLength() > 0)
	    {
		Vector v = new Vector();
		int len = authors.getLength();
		for (int i = 0; i < len; i++)
		    {
			String a =  ((Node)authors.item(i)).getFirstChild().getNodeValue();
			v.addElement(a);
		    }
		formalism.setAuthors(v);
	    }

	NodeList doc = node.getElementsByTagName("doc");
	if (doc.getLength() > 0)
	    {
		String url = ((Element)doc.item(0)).getAttribute("url");
		formalism.setDocURL(url);
	    }

	NodeList icontag = node.getElementsByTagName("icon");
	if (icontag.getLength() > 0)
	    {
		String url = ((Element)icontag.item(0)).getAttribute("url");
		formalism.setIcon(url);
	    }
    }


    /* Test method */
    public static void main(String argv[])
    {
	XMLFormalism xf = new XMLFormalism();

	System.err.println(xf.parse(argv[0]));

    } // main(String[])
}






