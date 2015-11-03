/*
* ElementDesc.java - SEdit, a tool to design and animate graphs in MadKit
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

import java.beans.IntrospectionException;
import java.util.Hashtable;


/**
    Abstract class of the meta-elements of the model elements.

    @author Jacques Ferber and Olivier Gutknecht
    @see SEdit.StructureEditor
    @see SEdit.StructureAgent
**/

public abstract class ElementDesc extends Object
{
    public static final int PROPERTIES=1;
	public static final int GRAPHICPROPERTIES=2;
    public static final int ACTIONPROPERTIES=3;

    public static boolean debug = false;

    String name;

    Formalism formalism;

    /**
       * Get the value of formalism.
       * @return Value of formalism.
       */
    public Formalism getFormalism() {return formalism;}
    /**
       * Set the value of formalism.
       * @param v  Value to assign to formalism.
       */
    public void setFormalism(Formalism  v) {this.formalism = v;}

    /**
       * Get the value of name.
       * @return Value of name.
       */
    public String getName() {return name;}

    /**
       * Set the value of name.
       * @param v  Value to assign to name.
       */
    public void setName(String  v) {this.name = v;}


    String defaultLabel;

    /**
       * Get the value of defaultLabel.
       * @return Value of defaultLabel.
       */
    public String getDefaultLabel() {return defaultLabel;}

    /**
       * Set the value of defaultLabel.
       * @param v  Value to assign to defaultLabel.
       */
    public void setDefaultLabel(String  v) {this.defaultLabel = v;}

    String description;
    /**
       * Get the value of description.
       * @return Value of description.
       */
    public String getDescription() {return description;}


    /** nom de l'icone dans la barre des elements a creer */
    public String icon = "";
    /** class name for a graphic element  */
    public String elementClass;
    public String category;

    public String graphicClass;
    public String getGraphicClass(){return graphicClass;}
    public void setGraphicClass(String i){graphicClass=i;}


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
       * Get the value of graphicProperties.
       * @return Value of graphicProperties.
       */
    public Hashtable getGraphicProperties() {return graphicProperties;}

    /**
       * Set the value of graphicProperties.
       * @param v  Value to assign to graphicProperties.
       */
    public void setGraphicProperties(Hashtable  v) {this.graphicProperties = v;}


	/**
	 * Test if a pair <key, value> is a default value of the table.
	 * @param name name of the key
	 * @param value value of the <key,value> pair
	 */
    public boolean isDefaultValue(String name, String value,int type){
	  Hashtable prop=null;
	  if (type == PROPERTIES) prop=getProperties();
	  else if (type == GRAPHICPROPERTIES) prop=getGraphicProperties();
	  else if (type == ACTIONPROPERTIES) prop=getActions();
	  if ((prop!=null) && (prop.containsKey(name)) && (value != null)
			  && value.equals(prop.get(name)))
		return true;
	  else
		return isStandardDefaultValue(name,value,type);
    }

	public boolean isStandardDefaultValue(String name, String value, int type){
		if ((type == GRAPHICPROPERTIES) &&
		  (((name.equalsIgnoreCase("labellocation")) &&
		   (value.equals("5"))) ||
		   (name.equalsIgnoreCase("displayLabel") &&
		   (value.equalsIgnoreCase("true"))) )
		  )
		  return true;
		else
		  return false;
	}

    public void setIcon(String location) {icon=location;}
    public String getIcon() {return icon;}

    public boolean hasIcon(){
		if (icon==null)
			return false;
		else if (icon.equals(""))
			return false;
		else
			return true;
    }

    public String getCategory() {return category;}
    public void setCategory(String s) {category=s;}

    public String getElementClass() {return elementClass;}
    public void setElementClass(String s) {elementClass=s;}



    public String toString() {
	  return(name+","+description+","+ elementClass + "," +
			 graphicClass + "," + icon+","+actions);};


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
    public void addActionDesc(String call, String name)
    {
	    actions.put(name,new ActionDesc(call,name));
    }

    public ElementDesc(){
	  actions = new Hashtable();
	  //addActionDesc("dump","Infos en vrac");
	  addActionDesc("inspect","Inspect");
	  //addActionDesc("inspectGraphics","Inspecter objet graphique");
	  //	addActionDesc("rename","Renommer");
	  //	addActionDesc("comment","Annoter");
	  addActionDesc("delete","Delete");
    }

    public ElementDesc(String n, String desc, String classname, String iconLocation){
        this();
        name = n;
        description = desc;
        elementClass = classname;
        icon=iconLocation;
    }



    /** A factory method that builds a new element according to the description */
    public SElement makeElement(Hashtable properties) {
	SElement o;
	try {
	    Class c;

	    // Ol: FIXED we don't impose classes in SEdit.Formalisms packages
	    /*	    if (formalism.getBase() instanceof URL)
		{
		    URL[] urls = new URL[1];
		    urls[0]=(URL)(formalism.getBase());
		    URLClassLoader u = new URLClassLoader(urls);
		    c = Class.forName(elementClass,true,u);
		}
		else*/
        // c = Class.forName(elementClass);
		c = madkit.kernel.Utils.loadClass(elementClass);
	    o = (SElement) c.newInstance();
	    o.setDescriptor(this);
	    // Ol: JF Modifs VRFY: Is that possible ? (getprops on a makeelement ?)

		if (getProperties() != null){
            //System.out.println("properties : " + getProperties());
			ReflectorUtil.setProperties(o,getProperties());
        }
        if (properties!=null)
			ReflectorUtil.setProperties(o,properties);
	    return o;
	}
	catch(ClassNotFoundException ev){
	    System.err.println("Class not found :" + elementClass + " "+ev);
	    ev.printStackTrace();
	    return(null);
	}
	catch(InstantiationException e){
	    System.err.println("Can't instanciate ! " + elementClass +" "+e);
	    e.printStackTrace();
	    return(null);
	}
	catch (IntrospectionException e)
	    {
		System.err.println("Can't introspect ! " + elementClass +" "+e);
		e.printStackTrace();
		return(null);
	    }
	catch(IllegalAccessException e){
	    System.err.println("il a pas aime l'acces dis donc! " + elementClass+" "+e);
	    e.printStackTrace();
	    return(null);
	}
    }
}









