/*
* Viewer.java -TurtleKit - A 'star logo' in MadKit
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
package turtlekit.kernel;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

/**Viewer is the simulation default world displayer agent (an specialized Observer,
	it can be extended to redefine the default representation of patches and
	turtles (a square fill with the color return by the getColor() method of them).

	@author Fabien MICHEL
	@version 2.0 22/08/2000 */

public class Viewer extends Observer
{
    int cellSize;
    GridCanvas onScreen;
    int cpt=10;
    int flashTime=10;
    boolean flash = false;
    boolean show = true;
    boolean redrawAll = false;
    TurtleProbe allTurtles;
    
    public void setFlash (boolean add)
    {
	if (add)
	    {
		redrawAll=true;
		onScreen.display();
		redrawAll=false;
	    }
	flash = add;
    }
    public boolean getRedrawAll(){return redrawAll;}
    public void setRedrawAll(boolean b){ redrawAll=b;}
    public boolean getFlash(){return flash;}
    public void setShow (boolean add)
	{
		if (add && (! show) && onScreen != null)
		{
		    redrawAll=true;
			onScreen.display();
		    redrawAll=false;
			show = true;
		}
		else show = add;
	}
    public boolean getShow(){return show;}
    public void setFlashStepSize (int add){flashTime = add;cpt=flashTime;}
    public int getFlashStepSize(){return flashTime;}
 
	/**MadKit usage, no redefinition*/
	final public void initGUI()
    {
	setGUIObject(onScreen = new GridCanvas(cellSize*envWidth,cellSize*envHeight,this));
    }

	/**init the GUI*/
    public void setup()  
    {
    	leaveRole(getSimulationGroup(),"observer");
    	requestRole(getSimulationGroup(),"viewer");
    	allTurtles = new TurtleProbe(getSimulationGroup(),"turtle");
    	addProbe(allTurtles);
	onScreen.initialisation();
    }

/**override this method if you want an other patch graphic representation
	giving an on screen location (x,y), a patch p to draw
	and a reserved on screen patch size: a square of pixels with a side of cellS.
	As the simulation display is optimized,
	be sure that you draw a figure that is contained in the reserved square or set
	the redrawAll variable to true (in the property box or in constructor
	so the patches are all repainted first,
	then the turtles (avoid to leave turtle trace on the floor,
	but realy slow down the simulation). 
	By example you can use the patch access methods to decide the color to display for this.
	default:
		g.setColor(p.getColor());
		g.fillRect(x,y,cellS,cellS);*/
    public void paintPatch(Graphics g, Patch p,int x,int y,int cellS)
    {
		g.setColor(p.color);
		g.fillRect(x,y,cellS,cellS);
	}

    /** In the same way, you can give a special graphic representation of your turtles.
		Default:
		g.setColor(t.getColor());
		g.fillRect(x,y,cellS,cellS);*/
	public void paintTurtle(Graphics g, Turtle t,int x,int y,int cellS)
    {
		g.setColor(t.color);
		g.fillRect(x,y,cellS,cellS);
	}

    final void paintInfo(Graphics g)
    {
	if (redrawAll)
	    for (int i=0; i < envWidth; i++)
		for (int j=0; j < envHeight; j++)
		    paintPatch(g, patchGrid[i][j],i*cellSize,(envHeight-j-1)*cellSize,cellSize);
	else
	    for (int i=0; i < envWidth; i++)
		for (int j=0; j < envHeight; j++)
		    if (patchGrid[i][j].change)
			paintPatch(g, patchGrid[i][j],i*cellSize,(envHeight-j-1)*cellSize,cellSize);
	Turtle[] turtles = allTurtles.getTurtles();		
	for(int i=0;i<turtles.length;i++)
	    {
		if (turtles[i] != null && ! turtles[i].hidden)
		    paintTurtle(g,turtles[i],turtles[i].xcor()*cellSize,(envHeight-turtles[i].ycor()-1)*cellSize,cellSize);
	    }
    }
    
/**the display itself*/
 public void display()
    {
	if (show)
	       if (flash) 
		   {
		       cpt--;
		       if (cpt < 0) 
			   {
			       redrawAll=true;
			       cpt=flashTime;
			       onScreen.display();
			       redrawAll=false;
			   }
		   }
	       else 
		   onScreen.display();
    }
}

/** this class defines the Graphics object (the agent's GUI) where the display is finally made
  @author Fabien MICHEL
  @version 1.2 20/3/2000 
 */

class GridCanvas extends Canvas
{
  Viewer simuViewer;
  Image buffer;
  Graphics bufferGraphics;

  public GridCanvas(int width,int height,Viewer l)
    {
    setBackground(Color.black);
    setForeground(Color.blue);
	setSize(width,height);
	simuViewer=l;
    }
  
 void initialisation()
  {
	Dimension d = getSize();
	buffer = createImage(d.width,d.height);
   	 bufferGraphics = buffer.getGraphics();
   	 bufferGraphics.setColor(Color.black);
	bufferGraphics.fillRect(0,0,d.width,d.height);
  }

  public Dimension getPreferredSize() {return getSize();}

  void display()
  {
	  if(getGraphics() != null)
	  {
		simuViewer.paintInfo(bufferGraphics);
	  	getGraphics().drawImage(buffer,0,0,this);
	  }
  }
}
