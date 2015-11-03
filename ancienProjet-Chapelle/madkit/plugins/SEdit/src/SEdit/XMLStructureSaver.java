/*
* XMLStructureSaver.java - SEdit, a tool to design and animate graphs in MadKit
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

import java.io.File;
import java.io.FileWriter;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.xerces.dom.DocumentImpl;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import SEdit.Graphics.GObject;



 class XMLStructureSaver
{
    protected String name = "example";

    protected Structure structure;
    protected Formalism formalism;
    protected StructureAgent master;

    /** Parses the resulting document tree. */
    public void write(File uri, Structure s, Formalism f)
    {

        try {
	    structure = s;
	    formalism = f;

	    //	    DOMParser parser = new DOMParser();
	    FileWriter writer = new FileWriter(uri);
	    OutputFormat of = new OutputFormat();
	    of.setIndenting(true);

	    XMLSerializer maker = new XMLSerializer(writer, of);
	    maker.startDocument();

	    Document doc = new DocumentImpl();

	    writeStructure(doc);
	    maker.serialize(doc);


	    maker.endDocument();
	    //	    parser.parse(uri);
	    //	    return structure;
	}
        catch (Exception e) {
            e.printStackTrace(System.err);
	    //turn null;
	}
    }

    /** Parses the resulting document tree. */
    public Document generateDOM(Structure s, Formalism f, Vector objects)
    {

        try {
	    structure = s;
	    formalism = f;

	    Document doc = new DocumentImpl();
	    writeStructure(doc,objects);
	    return doc;

	    //	    parser.parse(uri);
	    //	    return structure;
	}
        catch (Exception e) {
            e.printStackTrace(System.err);
	    return null;
	}
    }

    public void writeStructure(Document doc, Vector objects)
    {
	// Create the fragment we'll return
	DocumentFragment dfResult =  doc.createDocumentFragment();
	Element eBean = doc.createElement("structure");
	dfResult.appendChild(eBean);
	eBean.setAttribute("type", formalism.getName());
	eBean.setAttribute("name", name);
	if (!structure.getDescription().equals(""))
	    eBean.setAttribute("description", structure.getDescription());

	Vector nodes = new Vector();
	Vector arrows = new Vector();
	for (Enumeration e = objects.elements(); e.hasMoreElements();)
	    {
		SElement s = (SElement)e.nextElement();
		if (s instanceof SNode)
		    nodes.addElement(s);
		if (s instanceof SArrow)
		    arrows.addElement(s);
	    }

	if (nodes.size()>0)
	    eBean.appendChild(writeNodes(doc,nodes));
	if (arrows.size()>0)
	    eBean.appendChild(writeArrows(doc,arrows));

	//	eBean.appendChild(writeProperties(doc,structure));
	doc.appendChild(dfResult);
    }


    public void writeStructure(Document doc)
    {
	// Create the fragment we'll return
	DocumentFragment dfResult =  doc.createDocumentFragment();

	Element eBean = doc.createElement("structure");
	dfResult.appendChild(eBean);
	eBean.setAttribute("type", formalism.getName());
	eBean.setAttribute("name", name);
	if (!structure.getDescription().equals(""))
	    eBean.setAttribute("description", structure.getDescription());

	if (structure.getNodes().size()>0)
	    eBean.appendChild(writeNodes(doc, structure.getNodes()));
	if (structure.getArrows().size()>0)
	    eBean.appendChild(writeArrows(doc, structure.getArrows()));
	eBean.appendChild(writeProperties(doc,structure,null,0));
	doc.appendChild(dfResult);
    }

    public DocumentFragment writeProperties(Document doc, Object o,
										    ElementDesc desc,int type)
    {
	DocumentFragment dfResult = doc.createDocumentFragment();
	// System.out.println(":: getProperties: " + o);
	Hashtable t = ReflectorUtil.getProperties(o);
	//System.out.println(":: properties: " + t + " of " + o);
	if (t.size()>0)
	    {
		for (Enumeration e = t.keys(); e.hasMoreElements();)
		    {
			Element eProp = doc.createElement("property");
			String name = (String)e.nextElement();
			String value = (String)t.get(name);
			if ((!value.equals("")) && (!name.equals("id")) &&
			   (!name.equals("label")) &&
			   ((desc == null) || (!desc.isDefaultValue(name,value,type)))) {
					eProp.setAttribute("name", name);
					Text textValue = doc.createTextNode(value);
					eProp.appendChild(textValue);
					dfResult.appendChild(eProp);
			    }
		    }
	    }
	return dfResult;
    }


    public DocumentFragment writeGraphic(Document doc, SElement elem)
    {
	DocumentFragment dfResult = doc.createDocumentFragment();
	GObject o = elem.getGObject();

	Element eBean = doc.createElement("graphic");

	eBean.setAttribute("x", Integer.toString(o.getLocation().x));
	eBean.setAttribute("y", Integer.toString(o.getLocation().y));
	dfResult.appendChild(eBean);

	eBean.appendChild(writeProperties(doc,o,elem.getDescriptor(),ElementDesc.GRAPHICPROPERTIES));

	return dfResult;
    }


    public DocumentFragment writeConnector(Document doc, String name, SConnect s)
    {
	DocumentFragment dfResult = doc.createDocumentFragment();
	ConnectorPlacement p = s.getConnectorPlacement();
	Element eBean = doc.createElement("connector");

	eBean.setAttribute("ref", s.getID());
	eBean.setAttribute("name", name);
	if (p.getSide()!=ConnectorPlacement.UNSPECIFIED)
	    switch(p.getSide())
		{
		case ConnectorPlacement.RIGHT:
		    eBean.setAttribute("side", "Right");
		    break;
		case ConnectorPlacement.LEFT:
		    eBean.setAttribute("side", "Left");
		    break;
		case ConnectorPlacement.TOP:
		    eBean.setAttribute("side", "Top");
		    break;
		case ConnectorPlacement.BOTTOM:
		    eBean.setAttribute("side", "Bottom");
		    break;
		}
	if (p.getRatio()!=-1)
	    eBean.setAttribute("ratio", Double.toString(p.getRatio()));

	dfResult.appendChild(eBean);

	return dfResult;
    }

    public DocumentFragment writeNode(Document doc, SNode node)
    {
	DocumentFragment dfResult = doc.createDocumentFragment();

	Element eBean = doc.createElement("node");

	eBean.setAttribute("type", node.getDescriptor().getName());
	eBean.setAttribute("id", node.getID());
	if (node.getLabel()!=null)
	    //	    if (!node.getLabel().equals(""))
		eBean.setAttribute("label", node.getLabel());

	if (node instanceof SComponent)
	    {
		SComponent comp = (SComponent)node;

		for (Enumeration e = comp.getInNames(); e.hasMoreElements();)
		    {
			String name = (String)e.nextElement();
			SConnect s = comp.getInConnect(name);
			eBean.appendChild(writeConnector(doc, name, s));
		    }
		for (Enumeration e = comp.getOutNames(); e.hasMoreElements();)
		    {
			String name = (String)e.nextElement();
			SConnect s = comp.getOutConnect(name);
			eBean.appendChild(writeConnector(doc, name, s));
		    }
	    }

	eBean.appendChild(writeGraphic(doc,node));
	eBean.appendChild(writeProperties(doc,node,node.getDescriptor(),ElementDesc.PROPERTIES));
	dfResult.appendChild(eBean);
	return dfResult;
    }


    public DocumentFragment writeNodes(Document doc, Vector nodes)
    {
	DocumentFragment dfResult =  doc.createDocumentFragment();
	Vector v = new Vector();

	Element eBean = doc.createElement("nodes");
	dfResult.appendChild(eBean);
	for (Enumeration e = nodes.elements(); e.hasMoreElements();)
	    {
		// We write first connectors, so that module will have them
		// for instanciation
		SNode node = (SNode)e.nextElement();
		if (node.persistent){
			if (node instanceof SConnect)
				eBean.appendChild(writeNode(doc,node));
			else
				v.addElement(node);
			}
	    }
	for (Enumeration e = v.elements(); e.hasMoreElements();)
	    {
		SNode node = (SNode)e.nextElement();
		eBean.appendChild(writeNode(doc,node));
	    }

	return dfResult;
    }
    public DocumentFragment writeArrow(Document doc, SArrow arrow)
    {
	DocumentFragment dfResult = doc.createDocumentFragment();

	Element eBean = doc.createElement("arrow");

	eBean.setAttribute("type", arrow.getDescriptor().getName());
	eBean.setAttribute("id", arrow.getID());
	eBean.setAttribute("from", arrow.getOrigin().getID());
	eBean.setAttribute("to", arrow.getTarget().getID());
	if (arrow.getLabel()!=null)
	    //	    if (!arrow.getLabel().equals(""))
	    eBean.setAttribute("label", arrow.getLabel());
	eBean.appendChild(writeGraphic(doc,arrow));
	eBean.appendChild(writeProperties(doc,arrow,arrow.getDescriptor(),ElementDesc.PROPERTIES));
	dfResult.appendChild(eBean);

	return dfResult;
    }


    public DocumentFragment writeArrows(Document doc, Vector arrows)
    {
	DocumentFragment dfResult =  doc.createDocumentFragment();

	Element eBean = doc.createElement("arrows");
	dfResult.appendChild(eBean);
	for (Enumeration e = arrows.elements(); e.hasMoreElements();)
	    {
		SArrow arrow = (SArrow)e.nextElement();
		if (arrow.persistent)
		   eBean.appendChild(writeArrow(doc,arrow));
	    }
	return dfResult;
    }


    public XMLStructureSaver(StructureAgent s)
    {
	master = s;
    }


    /* Test method */
    public static void main(String argv[])
    {
	//	System.err.println(xf.parseFormalismName(argv[0]));
	//	System.err.println(xf.parse(argv[0]));

    } // main(String[])
}






