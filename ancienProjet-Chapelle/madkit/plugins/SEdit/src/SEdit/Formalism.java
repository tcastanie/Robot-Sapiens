/*
* Formalism.java - SEdit, a tool to design and animate graphs in MadKit
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
import java.util.Iterator;
import java.util.Vector;

/** A Formalism definition
    @version 2.0 */

public class Formalism extends Object
{
    /** Formalism name */
    String name;
    /** nom de la classe de la structure. Structure par defaut */
    String structureClass = "SEdit.Structure";
    String description = "";

    /** The (storage) origin for a formalism. Will be used if we need
        to load additional classes or various ressources (images, ...)  */
    Object base;

    public Vector authors;
    public String docURL;
    public String icon;

    Vector connectorDescList = new Vector();   	// la description des connecteurs
    Vector nodeDescList = new Vector();		// la description des noeuds
    Vector arrowDescList = new Vector();	// la description des flêches


    public String getName(){return(name);}

    public Object getBase(){return(base);}
    public void setBase(Object s){base=s;}

    public String getIcon(){return(icon);}
    public void setIcon(String s){icon=s;}

    public String getDescription(){return(description);}
    public void setDescription(String s){description=s;}

    public String getStructureClass(){return(structureClass);}
    public void setStructureClass(String s){structureClass=s;}


    /** Ol: JF Modif - allow properties set on the whole */
    Hashtable properties;

    /**
       * Get the value of properties.
       * @return Value of properties.
       */
    public Hashtable getProperties() {return properties;}

    /**
       * Set the value of properties.
       * @param v  Value to assign to properties.
       */
    public void setProperties(Hashtable  v) {this.properties = v;}


    Hashtable graphicProperties;

    /**
       * Get the value of graphic properties.
       * @return Value of graphic properties.
       */
    public Hashtable getGraphicProperties() {return graphicProperties;}

    /**
       * Set the value of graphic properties.
       * @param v  Value to assign to properties.
       */
    public void setGraphicProperties(Hashtable  v) {this.graphicProperties = v;}

    public Formalism()
    { }

    public Formalism(String _name){
	name = _name;
    }

    /** constructor used by the scheme function defFormalism */
    public Formalism(String _name, String _structClass, Object _authors,
		     String _htmldoc, String _icon) {
	name = _name;
	structureClass = _structClass;
	if (_authors instanceof Vector)
	    setAuthors((Vector)_authors);
	if (_authors instanceof String)
	    setAuthor((String)_authors);
	docURL = _htmldoc;
	icon = _icon;
    }

    public void addConnectorDesc(ConnectorDesc d){connectorDescList.addElement(d);	}
    public Vector getConnectorDescList(){  return(connectorDescList);  }

    public void addNodeDesc(NodeDesc d){nodeDescList.addElement(d);	}
    public Vector getNodeDescList(){  return(nodeDescList);  }

    public void addArrowDesc(ArrowDesc d){  arrowDescList.addElement(d);	}
    public Vector getArrowDescList(){  return(arrowDescList);  }

    public Hashtable actions;

    public boolean hasActions() { return (actions!=null); }
    public void setActions(Hashtable v) { actions = v; }
    public Hashtable getActions(){  return(actions);  }
    public ActionDesc getAction(String s){  return((ActionDesc)actions.get(s));  }
    public void addActionDesc(ActionDesc a)
    {
	if (actions == null)
	    actions = new Hashtable();
	actions.put(a.getDescription(),a);
    }

    public ElementDesc getDesc(String name)
    {
	ElementDesc ed;
	ed = getNodeDesc(name);
	if (ed!=null)
	    return ed;
	ed = getConnectorDesc(name);
	if (ed!=null)
	    return ed;
	ed = getArrowDesc(name);
	if (ed!=null)
	    return ed;
	return null;
    }


    public NodeDesc getNodeDesc(String name) {
	NodeDesc o;
	for (int i=0; i<nodeDescList.size();i++){
	    o = (NodeDesc)nodeDescList.elementAt(i);
	    if (o.getName().equals(name))
		return (o);
	}
	return(null);
    }

    public ConnectorDesc getConnectorDesc(String name) {
	ConnectorDesc o;
	for (int i=0; i<connectorDescList.size();i++){
	    o = (ConnectorDesc)connectorDescList.elementAt(i);
	    if (o.getName().equals(name))
		return (o);
	}
	return(null);
    }

    public ArrowDesc getArrowDesc(String name) {
	ArrowDesc o;
	for (int i=0; i<arrowDescList.size();i++){
	    o = (ArrowDesc)arrowDescList.elementAt(i);
	    if (o.getName().equals(name))
		return (o);
	}
	return(null);
    }


    public void setAuthors(Vector v)
    {
	authors = v;
    }
    /** A sugar method if you don't want to deal with vectors */
    public void setAuthor(String s)
    {
	authors = new Vector();
	authors.addElement(s);
    }

    public void setDocURL(String doc)
    {
	docURL = doc;
    }

    public String toString()
    {
	return  "Name:"+ name + ", Class:" + structureClass + ", Authors:" + authors + ", Doc:" + docURL + ", Icon:" + icon + ", Connectors:" +
	    connectorDescList + ", NodeDesc:" + nodeDescList + ", ArrowDesc:" + arrowDescList + ", Actions:" + actions;
    }

    public void dump(){
      System.out.println("Name:"+ name );
      System.out.println("Class:" + structureClass);
      System.out.println("Authors:" + authors);
      System.out.println("Doc:" + docURL);
      System.out.println("Icon:" + icon);
      if (nodeDescList.size() > 0){
        System.out.println("Nodes:");
        for(Iterator e = nodeDescList.iterator();e.hasNext();){
            System.out.println(((NodeDesc) e.next()).toString());
        }
      }
      if (arrowDescList.size() > 0){
        System.out.println("Arrows:");
        for(Iterator e = arrowDescList.iterator();e.hasNext();){
            System.out.println(((ArrowDesc) e.next()).toString());
        }
      }
      if (connectorDescList.size() > 0){
        System.out.println("Connectors:");
        for(Iterator e = connectorDescList.iterator();e.hasNext();){
            System.out.println(((ConnectorDesc) e.next()).toString());
        }
      }
    }

}







