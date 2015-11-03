/*
* SoccerObserver.java -TurtleKit - A 'star logo' in MadKit
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
package turtlekit.simulations.soccer;



import java.awt.Color;

import turtlekit.kernel.Observer;



/** Observer for the playground initialization, and (in the future) it will be the refere

 

  @author Fabien MICHEL

  @version 1.2 6/12/1999 */



 public class SoccerObserver extends Observer

 {

     boolean holden = false,animation=true;

     public int status=-1;



     public SoccerObserver(boolean b)

     {

		 animation=b;

     }



	 public void initTurtleTables(){}



  void peopleAnimation()

  {

	  for (int i =0;i<200;i++)

		 for(int j=0;j<20;j++)

			if (Math.random()<.1)

			patchGrid[i][j].setColor(new Color((int) (Math.random()*256),(int) (Math.random()*256),(int) (Math.random()*256)));



	 for (int i =0;i<200;i++)

		 for(int j=130;j<150;j++)

			if (Math.random()<.1)

			patchGrid[i][j].setColor(new Color((int) (Math.random()*256),(int) (Math.random()*256),(int) (Math.random()*256)));



	 for (int i =0;i<15;i++)

		 for(int j=0;j<150;j++)

			if (Math.random()<.1)

			patchGrid[i][j].setColor(new Color((int) (Math.random()*256),(int) (Math.random()*256),(int) (Math.random()*256)));



	 for (int i =185;i<200;i++)

		 for(int j=0;j<150;j++)

			if (Math.random()<.1)

			patchGrid[i][j].setColor(new Color((int) (Math.random()*256),(int) (Math.random()*256),(int) (Math.random()*256)));

  }



 public void setup()

  {

	 for (int i =0;i<200;i++)

		 for(int j=0;j<20;j++)

			patchGrid[i][j].setColor(new Color((int) (Math.random()*256),(int) (Math.random()*256),(int) (Math.random()*256)));



	 for (int i =0;i<200;i++)

		 for(int j=130;j<150;j++)

			patchGrid[i][j].setColor(new Color((int) (Math.random()*256),(int) (Math.random()*256),(int) (Math.random()*256)));



	 for (int i =0;i<15;i++)

		 for(int j=0;j<150;j++)

			patchGrid[i][j].setColor(new Color((int) (Math.random()*256),(int) (Math.random()*256),(int) (Math.random()*256)));



	 for (int i =185;i<200;i++)

		 for(int j=0;j<150;j++)

			patchGrid[i][j].setColor(new Color((int) (Math.random()*256),(int) (Math.random()*256),(int) (Math.random()*256)));



	 for (int i =20;i<180;i++)					//lignes horizontales

	 {

		 patchGrid[i][24].setColor(Color.white);

		 patchGrid[i][124].setColor(Color.white);

	 }



 	 for (int i =20;i<179;i++)					//sol

		 for(int j=25;j<124;j++)

			patchGrid[i][j].setColor(Color.green);



 	 for (int i =24;i<125;i++)					   //ligne verticlaes

	 {

		 patchGrid[19][i].setColor(Color.white);

		 patchGrid[99][i].setColor(Color.white);

		 patchGrid[179][i].setColor(Color.white);

	 }



	 for (int i =20;i<46;i++)		  //surfaces de réparation

	 {

		 patchGrid[i][49].setColor(Color.white);

		 patchGrid[i][99].setColor(Color.white);

	 }



	 for (int i =155;i<180;i++)

	 {

		 patchGrid[i][49].setColor(Color.white);

		 patchGrid[i][99].setColor(Color.white);

	 }



 	 for (int i =49;i<100;i++)

	 {

		 patchGrid[45][i].setColor(Color.white);

		 patchGrid[155][i].setColor(Color.white);

	 }



	 for (int i =20;i<30;i++)		  //surfaces de but

	 {

		 patchGrid[i][65].setColor(Color.white);

		 patchGrid[i][85].setColor(Color.white);

	 }



	 for (int i =169;i<180;i++)

	 {

		 patchGrid[i][65].setColor(Color.white);

		 patchGrid[i][85].setColor(Color.white);

	 }



 	 for (int i =65;i<86;i++)

	 {

		 patchGrid[29][i].setColor(Color.white);

		 patchGrid[169][i].setColor(Color.white);

	 }



	 patchGrid[35][74].setColor(Color.white);  //points de penalty

	 patchGrid[164][74].setColor(Color.white);



	 patchGrid[19][80].setColor(Color.darkGray);   // poteaux

	 patchGrid[19][69].setColor(Color.darkGray);

	 patchGrid[179][80].setColor(Color.darkGray);

	 patchGrid[179][69].setColor(Color.darkGray);



 	 for (int i =70;i<80;i++)

	 {

		 patchGrid[19][i].setColor(Color.lightGray);

		 patchGrid[179][i].setColor(Color.lightGray);

	 }

 }

public void watch()

{

	if (animation) peopleAnimation();

		//System.out.println("0 0"); //score

}



}





















