/*
* XMLStructureLoader.java - SEdit, a tool to design and animate graphs in MadKit
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

import java.awt.Point;
import java.util.Hashtable;

//import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.apache.xerces.jaxp.*;			// Xerces

public class XMLStructureLoader
{
    protected Structure structure;
    protected Formalism formalism;
    protected StructureAgent master;
    protected Namespace nm;


    boolean insertMode=false;

    /**
       * Get the value of insertMode.
       * @return Value of insertMode.
       */
    public boolean getInsertMode() {return insertMode;}

    /**
       * Set the value of insertMode.
       * @param v  Value to assign to insertMode.
       */
    public void setInsertMode(boolean  v) {this.insertMode = v;}

    /** Parses the resulting document tree. */
    public Structure parse(String uri, Structure s)
    {

        try {
	    structure = s;
	    nm = new Namespace(s);
            //Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(uri);

		Document document = DocumentBuilderFactoryImpl.newInstance().newDocumentBuilder().parse(uri);
//	    DOMParser parser = new DOMParser();
//	    parser.parse(uri);
//	    Document document = parser.getDocument();
            parseStructure(document.getDocumentElement());
	    return structure;
	}
        catch (Exception e) {
			System.err.println("** Error : structure parsing error in " + uri);
            e.printStackTrace(System.err);
	    return null;
	}
    }

    /** Integrate an existing DOM into a given structure */
    public Structure integrate(Structure s, Document document)
    {

        try {
	    	    insertMode=true;

	    structure = s;
	    nm = new Namespace(s);
            parseStructure(document.getDocumentElement());
	    return structure;
	}
        catch (Exception e) {
            e.printStackTrace(System.err);
	    return null;
	}
    }


    public static String parseFormalismName(String uri)
    {
	try
	    {
//		DOMParser parser = new DOMParser();
//		parser.parse(uri);
//		Document document = parser.getDocument();
		Document document = DocumentBuilderFactoryImpl.newInstance().newDocumentBuilder().parse(uri);
		return document.getDocumentElement().getAttribute("type");
	    }
        catch (Exception e) {
			System.err.println("** Error : parsing error in " + uri);
            // e.printStackTrace(System.err);
	    return null;
	}
    }

    void parseStructure(Element f)
    {
	NodeList list;
	Node head;

	String type = f.getAttribute("type");
	formalism = master.getFormalism(type);

	//	structure = master.getStructure();//new Structure();
	structure.setFormalism(formalism);

	String description = f.getAttribute("description");
	if (!description.equals(""))
	    structure.setDescription(type);

	list = f.getElementsByTagName("nodes");
	head = list.item( 0 );
	if (head!=null)
	    {
		list = head.getChildNodes();
		parseNodes(list);
	    }

	list = f.getElementsByTagName("arrows");
	head = list.item( 0 );
	if (head!=null)
	    {
		list = head.getChildNodes();
		parseArrows(list);
	    }

	// JF: ce n'est pas genial car on recupere toutes les properties du document, meme celles
	// des noeuds et des fleches. Mais bon, on modifiera cela par la suite.
	// De toutes facons, les properties qui n'ont pas de sens sont eliminees par la reflection.
	NodeList proplist = f.getElementsByTagName("property");
	if (proplist.getLength() > 0)
	    try
		{	// System.out.println("try to set properties: " + getProperties(proplist) + ", :" +proplist.getLength());
		    ReflectorUtil.setProperties(structure,getProperties(proplist));
		}
	catch (Exception e)
	    {
		Utils.debug(this, e);
	    }
    }


    void addProperty(Hashtable t, Element prop)
    {
	String name = prop.getAttribute("name");
	String value = prop.getFirstChild().getNodeValue();
	t.put(name,value);
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

			if (prop.getNodeName().equals("property"))
				addProperty(h, prop);

			/* String name = prop.getAttribute("name");
				String  value = prop.getFirstChild().getNodeValue();
				String value = prop.getAttribute("value"); */

			//if (value == null)
			//value = prop.getFirstChild().getNodeValue();
			// System.err.println("n/v"+name+" "+value);h.put(name,value);
		    }
	    }
	return h;
    }


    void parseNodes(NodeList list)
    {
	//	System.err.println("parsenodes:"+list);

	int len = list.getLength();
	for (int i = 0; i < len; i++)
	    {
		if(list.item(i).getNodeType()==Node.ELEMENT_NODE)
		    {
			Element node = (Element) list.item(i);

			Point p = null;
			String type = node.getAttribute("type");
			String id = node.getAttribute("id");
			String label = null;
			if (node.getAttributeNode("label")!=null)
			    label = node.getAttribute("label");
			//	System.err.println("---------------------READ"+id+type+label);

			Hashtable properties = new Hashtable();
			Hashtable graphicProperties = new Hashtable();
			Hashtable connectors = new Hashtable();
			Hashtable placement = new Hashtable();

			NodeList childs = node.getChildNodes();
			for (int j = 0; j < childs.getLength(); j++)
			    if (childs.item(j).getNodeType()==Node.ELEMENT_NODE)
				{
				    Element child =(Element) childs.item(j);
				    if (child.getNodeName().equals("graphic"))
					{
					    p = new Point(Integer.parseInt(child.getAttribute("x")),
							  Integer.parseInt(child.getAttribute("y")));
					    NodeList proplist = child.getChildNodes();
					    for (int k = 0; k < proplist.getLength(); k++)
						if (proplist.item(k).getNodeType()==Node.ELEMENT_NODE)
						    {
							Element prop =(Element) proplist.item(k);
							if (prop.getNodeName().equals("property"))
							    addProperty(graphicProperties, prop);
						    }
					}
				    if (child.getNodeName().equals("property"))
					addProperty(properties, child);
				    if (child.getNodeName().equals("connector"))
					{
					    String cref   = child.getAttribute("ref");
					    String cname  = child.getAttribute("name");
					    String pside   = child.getAttribute("side");
					    String pratio = child.getAttribute("ratio");

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

					    SConnect s = (SConnect)structure.getNode(nm.lookupID(cref));
					    //System.err.println("got cnx+"+cref+"("+cname+")"+s+" Pl "+cp);
					    connectors.put(cname, s);
					    placement.put(cname, cp);
					}
				}

			ElementDesc nd = formalism.getDesc(type);
			//System.err.print("NN"+type+id+label+nd);

			if (nd instanceof ComponentDesc)
			    master.newComponent(insertMode,(ComponentDesc)nd, nm.putID(id), label, properties, p, graphicProperties,
						connectors,placement);
			else if (nd instanceof NodeDesc)
			    master.newNode(insertMode,(NodeDesc)nd, nm.putID(id), label, properties, p, graphicProperties);
			//System.err.println("FROM FILE:"+properties+" "+graphicProperties);

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

			Point p = null;
			String type = node.getAttribute("type");
			String id = node.getAttribute("id");
			String label = null;
			if (node.getAttributeNode("label")!=null)
			    label = node.getAttribute("label");
			String to = node.getAttribute("to");
			String from = node.getAttribute("from");
			Hashtable properties = new Hashtable();
			Hashtable graphicProperties = new Hashtable();

			NodeList childs = node.getChildNodes();
			for (int j = 0; j < childs.getLength(); j++)
			    if (childs.item(j).getNodeType()==Node.ELEMENT_NODE)
				{
				    Element child =(Element) childs.item(j);
				    if (child.getNodeName().equals("graphic"))
					{
					    p = new Point(Integer.parseInt(child.getAttribute("x")),
							  Integer.parseInt(child.getAttribute("y")));
					    NodeList proplist = child.getChildNodes();
					    for (int k = 0; k < proplist.getLength(); k++)
						if (proplist.item(k).getNodeType()==Node.ELEMENT_NODE)
						    {
							Element prop =(Element) proplist.item(k);
							if (prop.getNodeName().equals("property"))
							    addProperty(graphicProperties, prop);
						    }
					}
				    if (child.getNodeName().equals("property"))
					addProperty(properties, child);
				}

			ArrowDesc nd = formalism.getArrowDesc(type);
			master.newArrow(insertMode, nd, nm.putID(id), nm.lookupID(from), nm.lookupID(to),
					label, properties, p, graphicProperties);
		    }
	    }
    }

    public XMLStructureLoader(StructureAgent s)
    {
	master = s;
    }


    /* Test method */
    public static void main(String argv[])
    {
	XMLStructureLoader xf = new XMLStructureLoader(null);
	System.err.println(xf.parseFormalismName(argv[0]));
    } // main(String[])
}






