/*
* ComponentDesc.java - SEdit, a tool to design and animate graphs in MadKit
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

import java.util.Enumeration;
import java.util.Hashtable;


public class ComponentDesc extends NodeDesc
{
    public final static int MODULE_FREE     = 1;
    public final static int MODULE_FIXED    = 2;
    public final static int MODULE_TEMPLATE = 3;

    public final static int MODULE_PLACEMENT_AUTO      = 1;
    public final static int MODULE_PLACEMENT_JUSTIFIED = 2;
    public final static int MODULE_PLACEMENT_MANUAL    = 3;
    
    int module = MODULE_FREE;//NO_MODULE;
    Hashtable connectors = null;
    Hashtable placement  = null;
    
    int layout;
    
    /**
       * Get the value of layout.
       * @return Value of layout.
       */
    public int getLayout() {return layout;}
    
    /**
       * Set the value of layout.
       * @param v  Value to assign to layout.
       */
    public void setLayout(int  v) {this.layout = v;}
    
    /**
     * Get the value of module.
     * @return Value of module.
     */
    public int getModule() {return module;}
    
    /**
     * Set the value of module.
     * @param v  Value to assign to module.
     */
    public void setModule(int  v) 
    {
	connectors = new Hashtable();
	placement = new Hashtable();
	//	System.err.println("Module"+v+NO_MODULE+(module!=NO_MODULE)+" "+connectors);
	//	System.err.println("Module"+v+" "+connectors);
	
	this.module = v;
    }
    
    
    public void addModuleConnector(String name, ConnectorDesc cnx, ConnectorPlacement cp)
    {
	//	if ((module==MODULE_FIXED) || (module==MODULE_TEMPLATE))
	//{
	if ((name==null) || (name.equals("")))
	    name = cnx.getName()+connectors.size();
	
	connectors.put(name, cnx);
	placement.put(name,cp);
    
	if (module==MODULE_FREE)
	    {
		//		connectors.put(name, cnx);
		//		placement.put(name,cp);
		ActionDesc ad = new ActionDesc("addConnector","Add "+name+" connector");
		ad.setParam(name);
		addActionDesc(ad);
	    }
		
	    //System.err.println("ADDMODEULES"+name+cnx+module+connectors);*/
    }

    public ComponentDesc(String name, String desc, String classname, int module, int lay)
    {
	this(name, desc, classname);
	setModule(module);
	setLayout(lay);
    }    		
    public ComponentDesc(String name, String desc, String classname)
    {
	super(name, desc, classname);
	setGraphicClass("SEdit.Graphics.GRectangle");
	if ((classname == null) || classname.equals(""))
	    setElementClass("SEdit.SComponent");
    }    		

    /** A factory method that builds a new element according to the description */
    public Hashtable makeConnectors()
    {
	Hashtable v = new Hashtable();
	if ((module==MODULE_FIXED) || (module==MODULE_TEMPLATE))
	    {
		for (Enumeration e = connectors.keys(); e.hasMoreElements() ;) 
		    {
			String name = (String)e.nextElement();
			v.put(name,makeConnector(name));
		    }
	    }
	return v;
    }


    /** A factory method that builds a new element according to the description */
    public SConnect makeConnector(String name)
    {
	ConnectorDesc cd =(ConnectorDesc)connectors.get(name);
	SConnect c = (SConnect)cd.makeElement(null);
	c.setConnectorPlacement((ConnectorPlacement)placement.get(name));
	c.setLabel(name);
	return c;
    }
    
    public ConnectorPlacement getConnectorPlacement(String name)
	{
	    return (ConnectorPlacement)placement.get(name);
	}
    

    public String toString() 
    {   	
	return super.toString()+"|,"+iconName;
    }



}









