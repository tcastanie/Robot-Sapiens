/*
* GridInitializer.java -TurtleKit - A 'star logo' in MadKit
* Copyright (C) 2000-2002 Fabien Michel
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
package turtlekit.simulations.diffusion;



import turtlekit.kernel.Observer;

/** This agent just initializes the center patch flavor and flavor2 (PatchVariable Objects)
  

    @author Fabien MICHEL
    @version 1.2 31/01/2000 */



public class GridInitializer extends Observer

{

    double val,val2;

    

    public GridInitializer (double v,double v2)

    {

	val = v;

	val2 = v2;

    }

    

    public void setup()

    {

	patchGrid[(int) (envWidth/2)][(int) (envHeight/2)].setPatchVariable("flavor",val);

	patchGrid[(int) (envWidth/2)][(int) (envHeight/2)].setPatchVariable("flavor2",val2);

    }	  

    

}







