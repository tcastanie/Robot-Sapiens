/*
* MethodCallCommand.java - SEdit, a tool to design and animate graphs in MadKit
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

public class MethodCallCommand extends Command
{
    protected String method;
    protected Object parameters;
    protected Object obj;
    
    /**
       * Get the value of target.
       * @return Value of target.
       */
    public Object getTarget() {return obj;}
    
    /**
       * Set the value of target.
       * @param v  Value to assign to target.
       */
    public void setTarget(Object  v) {this.obj = v;}
    
    /**
       * Get the value of parameters.
       * @return Value of parameters.
       */
    public Object getParameters() {return parameters;}
    
    /**
       * Set the value of parameters.
       * @param v  Value to assign to parameters.
       */
    public void setParameters(Object v) {this.parameters = v;}
    

    public MethodCallCommand(String methodname)
    {
	method=methodname;
    }

    public MethodCallCommand(Object target, String methodname)
    {
	this(methodname);
	obj=target;
    }   

    public MethodCallCommand(Object target, String methodname, Object params)
    {
	this(target, methodname);
	parameters = params;
    }   

    public String getMethodName() 
    {
	return action;
    }

    public String toString()
    {
	return method+" "+parameters+" "+obj;
    }

    public void execute()
    {
	if (obj!=null)
	    execute(obj);
    }

    void execute(Object target)
    {
	try
	    {
		Class c = target.getClass();
		Method m;
		if (parameters!=null)
		    {
			Object[] params; 
			Class[] paramTypes;
			
			if (parameters instanceof Vector)
			    {
				Vector v = (Vector)parameters;
				
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
				params[0]=parameters;
				paramTypes[0]=parameters.getClass();
			    }
			m = c.getMethod(method,paramTypes);
			m.invoke(target, params);
		    }
		else
		    {
			m = c.getMethod(method,new Class[0]);
			m.invoke(target, new Object[0]);
		    }
	    }
	
	catch (Exception e) {
	    System.err.println("Can't invoke:"+e);
	}
    }
}






