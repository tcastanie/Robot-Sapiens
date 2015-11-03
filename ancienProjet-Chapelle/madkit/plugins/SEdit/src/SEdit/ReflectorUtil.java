/*
* ReflectorUtil.java - SEdit, a tool to design and animate graphs in MadKit
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

import java.beans.BeanInfo;
import java.beans.IndexedPropertyDescriptor;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Hashtable;

 class ReflectorUtil {

  // Use the Introspector to return an array of property descriptors for
  // bean class c.
  protected static PropertyDescriptor[] getPropertyDescriptors(Class c)
       throws IntrospectionException {

         BeanInfo	beaninfo = Introspector.getBeanInfo(c);
         return beaninfo.getPropertyDescriptors();
  }


    public static Hashtable getProperties(Object jb)
    {
	Hashtable values = new Hashtable();

try{	// Use the introspector to get the property descriptors
	// for this bean.
	PropertyDescriptor[] pds = getPropertyDescriptors(jb.getClass());
	Utils.log("Got " + pds.length + " property descriptors");

	// Create a hash table of property names and property descriptors
	Hashtable htpd = new Hashtable();
	for (int i = 0; i < pds.length; i++) {
	    htpd.put(pds[i].getName().toLowerCase(), pds[i]);
	    Utils.log("Hashing " + pds[i].getName().toLowerCase() + ": " + isNull(pds[i]));
	}
	Utils.log("Done hashing");

	for (Enumeration enm = htpd.keys(); enm.hasMoreElements(); )
	    {
		String sPropertyName = (String)enm.nextElement();
		Utils.log("Got property " + sPropertyName);
		PropertyDescriptor pd =
		    (PropertyDescriptor)htpd.get(sPropertyName.toLowerCase());
		Utils.log("PD1 is " + isNull(pd));
		if (pd == null) {
		    Utils.log("Couldn't get property descriptor for property " + sPropertyName);
		    continue;
		}
		else
		    {
			Object prop = getProperty(jb, sPropertyName, pd);
			if(prop!=null)
			    {
				values.put(sPropertyName,prop);
				// System.err.println(prop);
			    }
			else
			    if (pd.getPropertyType()==(new String()).getClass())
				{
				    // System.err.println("is stringclass");
				    values.put(sPropertyName,"");//pd.getPropertyType());
				}



		    }

	    }
	return values;

}


    	     catch (Exception e) {
		// Any problem that happens here, TOO BAD!
		// This could be changed to throw an exception
		Utils.debug("getPropertiesinstantiateBean: " + e.toString());
		e.printStackTrace();
		return null;

     }

    }

  public static Hashtable getPropertiesTypes(Object jb)
    {
	Hashtable types = new Hashtable();

try{	// Use the introspector to get the property descriptors
	// for this bean.
	PropertyDescriptor[] pds = getPropertyDescriptors(jb.getClass());
	Utils.log("Got " + pds.length + " property descriptors");

	// Create a hash table of property names and property descriptors
	Hashtable htpd = new Hashtable();
	for (int i = 0; i < pds.length; i++) {
	    htpd.put(pds[i].getName().toLowerCase(), pds[i]);
	    Utils.log("Hashing " + pds[i].getName().toLowerCase() + ": " + isNull(pds[i]));
	}
	Utils.log("Done hashing");

	for (Enumeration enm = htpd.keys(); enm.hasMoreElements(); )
	    {
		String sPropertyName = (String)enm.nextElement();
		Utils.log("Got property " + sPropertyName);
		PropertyDescriptor pd =
		    (PropertyDescriptor)htpd.get(sPropertyName.toLowerCase());
		Utils.log("PD is " + isNull(pd));
		if (pd == null) {
		    Utils.log("Couldn't get property descriptor for property " + sPropertyName);
		    continue;
		}
		else
		    {
			Object prop = getProperty(jb, sPropertyName, pd);
			if(prop!=null)
			    {
				types.put(sPropertyName,pd.getPropertyType());
				// System.err.println(prop);
			    }
			else
			    if (pd.getPropertyType()==(new String()).getClass())
				{
				    // System.err.println("is stringclass");
				    types.put(sPropertyName,pd.getPropertyType());
				}
		    }


	    }
	return types;

}


    	     catch (Exception e) {
		// Any problem that happens here, TOO BAD!
		// This could be changed to throw an exception
		Utils.debug("getPropertiesinstantiateBean: " + e.toString());
		e.printStackTrace();
		return null;

     }

    }



  // Getting a property of a bean
  public static String getProperty(Object jb, String sName,
                          PropertyDescriptor pd) {
    Utils.log("--- getProperty " + sName);

    // There are plenty of things that can go wrong here, so
    // let's just catch exceptions and report on them.
    try {

      // We permit property values that can be represented
      // as a String, or property values that are themselves
      // JavaBeans. Here we search all child nodes of this node,
      // looking for nonempty text nodes or a child node
      // of type "JavaBean".

      Utils.log("Value of " + sName + "'");

      // Get the set accessor for the property. If it's null, there's
      // nothing we can do. We'll use this method somewhere no matter
      // how we set the property.
      Utils.log("Getting getter for pd " + pd.toString());
      Method setter = pd.getWriteMethod();
      Utils.log("Setter is " + isNull(setter));
      if (setter == null)
        return null;
      Method getter = pd.getReadMethod();
      Utils.log("Getter is " + isNull(getter));
      if (getter == null)
        return null;

      // Handle non-primitive types.
      // 1. If it's a JavaBean, instantiate the JavaBean it
      // represents, use the new Bean to set the property.
      // 2. If the setter() method for the property takes
      // a single string as its argument, call that setter
      // method.
      // 3. If we can construct a value of the type corresponding
      // to the property's type, do so, and then call the
      // property's setter method.
      // Otherwise, we just can't set this property.
      // So, set the property based on one of these strategies:

      // [2] Does the "setter" function take a single string
      // as its argument? If so, use "value" to set it.
     Object  obj = getter.invoke(jb, new Object[0]);
     // System.err.println("via getter:"+ getter.invoke(jb, new Object[0]));
     //     System.err.println("via getter:"+ getter.invoke(jb, new Object[0]).toString());
     // System.err.println("via read:"+pd.getName());

     // Is this property of a primitive type? If so, convert the String
     // to that type as appropriate, and call "set" function. Simple.
     Class propType = pd.getPropertyType();
     Utils.log("Property type of " + sName + " == " + propType);
     if (propType == null)
	 return null;
     Utils.log("Property type of " + sName + " is " + propType.toString());

     if (propType.isPrimitive()) {
	 return obj.toString();
     }
     else
	 if (obj instanceof String)
	     return (String)obj;
     else
	 return null;


    } catch (Exception exc) {
      Utils.debug("setProperty: " + exc.toString());
      Utils.debug("target exception: " + exc.toString());
      exc.printStackTrace();

      if (exc instanceof InvocationTargetException) {
        Throwable te = ((InvocationTargetException)exc).getTargetException();
        Utils.debug("-->target exception: " + te.toString());
      }
    }

    Utils.log("Finished setProperty(" + sName + ")");
    return null;

  }



  // Setting a property of a bean
  public static void setProperty(Object jb, String sName, String value,
                          PropertyDescriptor pd) {
    Utils.log("--- SetProperty " + sName);
    //System.out.println("setProperty: " + sName + " : " + value + " on " + jb);

    // There are plenty of things that can go wrong here, so
    // let's just catch exceptions and report on them.
    try {

      // We permit property values that can be represented
      // as a String, or property values that are themselves
      // JavaBeans. Here we search all child nodes of this node,
      // looking for nonempty text nodes or a child node
      // of type "JavaBean".

      Utils.log("Value of " + sName + " is '" + value + "'");

      // Get the set accessor for the property. If it's null, there's
      // nothing we can do. We'll use this method somewhere no matter
      // how we set the property.
      Utils.log("Getting setter for pd " + pd.toString());
      Method setter = pd.getWriteMethod();
      Utils.log("Setter is " + isNull(setter));
      if (setter == null)
        return;

      // Handle indexed and non-indexed property descriptors separately
      if (!(pd instanceof IndexedPropertyDescriptor)) {

        // Is this property of a primitive type? If so, convert the String
        // to that type as appropriate, and call "set" function. Simple.
        Class propType = pd.getPropertyType();
        if (propType == null)
          return;
        Utils.log("Property type of " + sName + " is " + propType.toString());

        // This variable will hold the list of arguments to the
        // setter method.
        Object[] setterArgs = null;

        if (propType.isPrimitive()) {
          // All primitive types except for Character have a constructor
          // that uses "String" as an argument.

	    // Let's look for a constant
	    /*   Class c = setter.getDeclaredClass();
	    Field f = c.getDeclaredFiedl(value);
	    if (f.getModifiers().isStatic() && f.getModifiers().isFinal())
		{
		    setterArg = f.getValue(null);
		    setterArgs = new Object[] { setterArg };
		}
	    */

	    if (propType == char.class) {
		char c = (char)(value.charAt(0));//.intValue());
            setterArgs = new Object[] { new Character(c) };
          } else {
            // Select wrapper class
            Class wrapper;
            if (propType == boolean.class) wrapper = Boolean.class;
            else if (propType == byte.class) wrapper = Byte.class;
            else if (propType == int.class) wrapper = Integer.class;
            else if (propType == long.class) wrapper = Long.class;
            else if (propType == float.class) wrapper = Float.class;
            else if (propType == double.class) wrapper = Double.class;
            else {
              Utils.log("Can't find wrapper for " + propType.toString());
              return;
            }

            // Get the constructor for the type that takes a
            // String as an argument
            Utils.log("Getting ctor");
            Class[] argTypes = { String.class };
            Constructor ctor = wrapper.getConstructor(argTypes);
            Utils.log("ctor is " + isNull(ctor));

            // Create an instance of the object the setter is expecting
            Object[] ctorArgs = { value };
            Object setterArg = ctor.newInstance(ctorArgs);
            Utils.log("setterArg is " + isNull(setterArg));

            // System.err.println("Setting " + sName + " to '" + setterArg.toString() + "'" +
	    //              " via call to " + setter.toString());

            // Invoke the setter with that argument
            setterArgs = new Object[] { setterArg };

	    if (false)
		{
		    // We try to find a static field with the given name


		}


          }
        } else {

          // Handle non-primitive types.
          // 1. If it's a JavaBean, instantiate the JavaBean it
          // represents, use the new Bean to set the property.
          // 2. If the setter() method for the property takes
          // a single string as its argument, call that setter
          // method.
          // 3. If we can construct a value of the type corresponding
          // to the property's type, do so, and then call the
          // property's setter method.
          // Otherwise, we just can't set this property.
          // So, set the property based on one of these strategies:

          // [2] Does the "setter" function take a single string
          // as its argument? If so, use "value" to set it.
          if (setterArgs == null) {
            Class[] setterParameterTypes = setter.getParameterTypes();
            Utils.log("Setter has " + setterParameterTypes.length + " args");

            if (setterParameterTypes.length == 1 &&
                setterParameterTypes[0] == java.lang.String.class) {
                setterArgs = new Object[] { value };
            }
          }

          // [3] Can we create one of these by passing a String to
          // a ctor?
          Constructor ctor = null;
          try {
            ctor = propType.getConstructor(new Class[] { String.class } );
          } catch (Exception exc) {
            // Ignore exceptions
          }

          // If we got a constructor, use it to create the argument
          // to the setter, then call the setter
          if (ctor != null) {
            Object arg = ctor.newInstance(new Object[] { value });
            setterArgs = new Object[] { arg };
          } else {
            // We can't construct the object with a string
          }
        }

        // At this point we have either figured out what argument
        // to pass to the setter, or we simply don't know what
        // to do.
        if (setterArgs != null) {
          setter.invoke(jb, setterArgs);
        } else {
          Utils.log("Can't set property " + sName);
        }
      }

      // Handle indexed property types
      else {
      }
    } catch (Exception exc) {
      Utils.debug("setProperty: " + exc.toString());
      Utils.debug("target exception: " + exc.toString());
      exc.printStackTrace();

      if (exc instanceof InvocationTargetException) {
        Throwable te = ((InvocationTargetException)exc).getTargetException();
        Utils.debug("-->target exception: " + te.toString());
      }
    }

    Utils.log("Finished setProperty(" + sName + ")");
  }

  public static String isNull(Object o) {
    return ((o == null) ? "null" : "not null");
  }


  // Instantiate the JavaBean, and set all of its properties
  // The element must be of type "JavaBean"
    public static void setProperty(Object jb, String name, String value)
    throws ClassNotFoundException, IntrospectionException {
        //System.out.println("setProperty: " + name + " : " + value + " on " + jb);
        Hashtable t = new Hashtable();
        t.put(name,value);
        setProperties(jb, t);
    }



  // Instantiate the JavaBean, and set all of its properties
  // The element must be of type "JavaBean"
  public static void setProperties(Object jb, Hashtable properties)
    throws ClassNotFoundException, IntrospectionException {

    // Use the introspector to get the property descriptors
    // for this bean.
    PropertyDescriptor[] pds = getPropertyDescriptors(jb.getClass());
    Utils.log("Got " + pds.length + " property descriptors");


    // Create a hash table of property names and property descriptors
    Hashtable htpd = new Hashtable();
    for (int i = 0; i < pds.length; i++) {
      htpd.put(pds[i].getName().toLowerCase(), pds[i]);
      Utils.log("Hashing " + pds[i].getName().toLowerCase() + ": " + isNull(pds[i]));
    }
    Utils.log("Done hashing");

    // For each Property element in the nPropertyNodes list,
    // set the appropriate property by calling the
    // appropriate set function. Any properties in the XML for
    // which no PropertyDescriptors exist are ignored.
    // This handles elements like:
    // <Property NAME="size">3</Property>
    for (Enumeration enm = properties.keys(); enm.hasMoreElements(); )
      try {
        // Property name
	  String sPropertyName = (String)enm.nextElement();
	  Utils.log("Got property " + sPropertyName);

	  PropertyDescriptor pd =
	      (PropertyDescriptor)htpd.get(sPropertyName.toLowerCase());
	  Utils.log("PD2 is " + isNull(pd));
      //System.out.println("prop of "+jb.getClass().getName()+" is: "+sPropertyName+", pd: "+pd);
	  if (pd == null) {
	      Utils.log("Couldn't get property descriptor for property " + sPropertyName);
	      continue;
	  }

	  // set the property based on its type, etc.
	  setProperty(jb, sPropertyName, (String)properties.get(sPropertyName), pd);
      } catch (Exception e) {
	  // Any problem that happens here, TOO BAD!
	  // This could be changed to throw an exception
	  Utils.debug("instantiateBean: " + e.toString());
	  e.printStackTrace();
      }



    Utils.log("Set up JavaBean " + jb);

    // Return the newly-instantiated JavaBean.
  }
}

