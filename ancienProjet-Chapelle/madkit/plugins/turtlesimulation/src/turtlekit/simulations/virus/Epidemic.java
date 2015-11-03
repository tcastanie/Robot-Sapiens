/*
* Epidemic.java -TurtleKit - A 'star logo' in MadKit
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
package turtlekit.simulations.virus;





import turtlekit.kernel.Launcher;





/** Virus transmission simulation 


 


  @author Fabien MICHEL


  @version 1.2 31/01/2000 */





public class Epidemic extends Launcher


{


    


    int nbOfInfected=1;


    int nbOfnonInfected=300;


    boolean virusType1=true;


    public void setNbOfInfected(int add){nbOfInfected = add;}


    public int getNbOfInfected(){return nbOfInfected;}


    public void setNbOfnonInfected(int add){nbOfnonInfected = add;}


    public int getNbOfnonInfected(){return nbOfnonInfected;}


    /**with this accessor you choose the kind of simulation you want for the the transmission of


       the virus : messages (type1) or direct interaction (type2 faster)*/


    public void setVirusType1(boolean b){virusType1 = b;}


    public boolean getVirusType1(){return virusType1;}


    


    public Epidemic()


    {


	setSimulationName("VIRUS TRANSMISSION");


	setWidth(60);


	setHeight(60);


    }


    


    public void addSimulationAgents()


    {


	for (int i = 0; i < nbOfnonInfected; i++)


	    if (virusType1)


		addTurtle(new Virus("green"));


	    else


		addTurtle(new Virus2("green"));


	for (int i = 0; i < nbOfInfected; i++)


	    if (virusType1)


		addTurtle(new Virus("red"));


	    else


		addTurtle(new Virus2("red"));


	


	addObserver(new VirusObserver(nbOfnonInfected),true);


	


	addViewer();


    }


}














