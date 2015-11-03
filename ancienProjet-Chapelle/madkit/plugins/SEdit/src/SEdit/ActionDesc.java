/*
* ActionDesc.java - SEdit, a tool to design and animate graphs in MadKit
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

import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Vector;


/** The description of an action that might be requested on a whole
    structure or on a single element (node or arrow). Usually defined
    in the formalism file */
public class ActionDesc
{
    public final static int UNKNOWN       = 0;
    /** The Java Method Call action type */
    public final static int METHOD_CALL   = 1;
    /** The Scheme Function Call action type */
    public final static int FUNCTION_CALL = 2;
    
    protected String action;
    protected String label;
    protected String icon;
    
    protected int lang = 0;
    

    Object parameters;
    
    /**
       * Get the value of parameters.
       * @return Value of parameters.
       */
    public Object getParam() {return parameters;}
    
    /**
       * Set the value of parameters.
       * @param v  Value to assign to parameters.
       */
    public void setParam(Object  v) {this.parameters = v;}
    

    public ActionDesc(int type, String value)
    {
	this(type, value, null, null);
    }

    public ActionDesc(String value, String description)
    {
	this(METHOD_CALL, value, description, null);
    }   

    public ActionDesc(String value, String description, String iconlocation)
    {
	this(METHOD_CALL, value, description, iconlocation);
    }    

    public ActionDesc(int type, String value, String description)
    {
	this(type, value, description, null);
    }

    public ActionDesc(int type, String value, String description, String iconlocation)
    {
	lang = type;
	action = value;
	label = description;
	icon = iconlocation;
    }    
    
    public int getType()
    {
	return lang;
    }
    public String getAction() 
    {
	return action;
    }
    public String getDescription()
    {
	return label;
    }
    public String getIcon() 
    {
	return icon;
    }
    
    public boolean hasDescription() 
    {
	return (label!=null);
    }
    
    public boolean hasIcon() 
    {
	if (icon==null)
	    return false;
	else if (icon.equals(""))
	    return false;
	else
	    return true;
    }

    public String toString()
    {
	return "("+lang+") "+action+" "+label+" "+icon+" "+parameters;
    }
    
    public void execute(Object target)
    {
	try
	    {
		if (getType()==ActionDesc.METHOD_CALL)
		    {
			Class c = target.getClass();
			Method m;
			if (getParam()!=null)
			    {
				Object p = getParam();  
				Object[] params; 
				Class[] paramTypes;
				
				if (p instanceof Vector)
				    {
					Vector v = (Vector)p;
					
					params = new Object[v.size()];
					paramTypes = new Class[v.size()];
					int i=0;
					
					for (Enumeration e=v.elements(); e.hasMoreElements();)
					    {
						Object o = e.nextElement();
						params[i]=o;
						paramTypes[i]=o.getClass();
						i++;
					    }
				    }
				else
				    {
					params=new Object[1];
					paramTypes=new Class[1];
					params[0]=p;
					paramTypes[0]=p.getClass();
				    }
				m = c.getMethod(getAction(),paramTypes);
				m.invoke(target, params);
			    }
			else
			    {
				m = c.getMethod(getAction(),new Class[0]);
				m.invoke(target, new Object[0]);
			    }
		    }
		
	    }
	catch (Exception e) {
	    System.err.println("Can't invoke:"+e);
	}
    }
}
