/*
* PetriLink.java - SEdit, a tool to design and animate graphs in MadKit
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
package SEdit.Formalisms.Petri;

import java.awt.*;
import java.awt.image.*;
import java.lang.Math;
import javax.swing.JOptionPane;

import java.util.*;
import java.io.*;

import SEdit.*; 
//import SEdit.Graphics.*;
import SEdit.Scheme.*;

// pour avoir des jetons colorés...
import gnu.mapping.*;
import gnu.math.IntNum;
import gnu.kawa.util.*;
import kawa.standard.*;
import kawa.lang.*;
import kawa.*;

/*****************************************************************

            CLASSE PetriLink

/*****************************************************************/

/** la classe des arcs Petri.

**/

public abstract class PetriLink extends SArrow
{  
	protected boolean activated = false;
	protected boolean validated = false;
	
	protected int weight=1; // le poids de la liaison (en consommation et en production)
	public void setWeight(int w){weight = w;}
	public int getWeight(){return(weight);}
	
	protected Object filter; // le filtre de la liaison
	public void setFilter(Object f){
		filter = f;
	}
	
	public Object getFilter() {return(filter);}
	
	protected String filterString=null;
	public void setFilterString(String s){
		try {
			setFilter(STools.readFromString(s));
			filterString = s;
		}	
		catch (Exception e) {
		    System.out.println(":: Erreur de saisie de filtre");
		}
	}
	
	public PetriLink(){
		super();
	}
	
	public String getFilterString(){
		return filterString;
	}
	
    public static String askForNewString(String oldvalue) {

   		String newname;
       newname = JOptionPane.showInputDialog(null,
                                      "Modification de : " + oldvalue,
                                      "Nouvelle valeur ",
                                      JOptionPane.PLAIN_MESSAGE);
       if (oldvalue.equals(newname))
       	return(null);
       else
       	return(newname);
	}
   
   protected void active(boolean flag)
   {
   	if (activated != flag)  {
		activated = flag;
		if(activated)
		    getGObject().setForeground(Color.magenta);
		else
		    getGObject().setForeground(Color.black);
		getGObject().getEditor().repaint();
	    }
   }
		
   // still useful??
   protected void validate(boolean flag)
   { if (validated != flag) {
			  // display();
				validated = flag;
			  // display();
   	}
   }
   
   
	
	
	public void modifyWeight() {
     	String newname = askForNewString(String.valueOf(weight));
       	if (newname != null){
       	  try {
           	setWeight(Integer.parseInt(newname));
         //   update();
           }
           catch(NumberFormatException e){}
       	}
	}
	
	public void modifyFilter() {
      	String filterDescr;
      	if (filter != null) {
	    	filterDescr = STools.writeToString(getFilter());
      	} else filterDescr = "";
   		// String s = editor.askForString("Modification du filtre :" + filterDescr,"Entrez le nouveau filtre");
   		
		String s = SEditTools.editText(getStructure().getAgent().gui, "Editing filter", filterDescr);
		// String s = askForNewString(filterDescr);
  			if (s != null) {
           		setFilterString(s);
   //        		update();
   				setLabel(s);
   				getGObject().setDisplayLabel(true);
           	}
	}
    
/*   public void doAction(String command)
   {
   	if (command.equals("modifyWeight"))    {
       	modifyWeight();
       }
    else if (command.equals("modifyFilter"))
       		modifyFilter();
       else super.doAction(command);
   } */
   
   /*
   public String bodyString()
    {   
    	if (getFilter() != null) {
    		String str = "'" + STools.writeToString(getFilter());
    		return(super.bodyString()+" "+1+" "+str);
    	}
    	else  if (weight == 1)
    		return(super.bodyString());
    	else
		  return (super.bodyString()+" "+ String.valueOf(weight));
	}
	
	public void setOptions(Vector v) {
   		if ((v != null) && (v.size() >0)) {
   			Object o = v.firstElement();
   			if (o instanceof IntNum) {
   				int w = ((IntNum)o).intValue();
   				setWeight(w);
   			}
   			else
   				System.err.println("SEdit warning: invalid weight " 
   					+ o + " in transition " + this);
   			if (v.size() > 1)
   				setFilter(v.elementAt(1));
   		}
   }
*/

}

