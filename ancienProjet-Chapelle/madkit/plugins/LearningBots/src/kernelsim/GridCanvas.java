package kernelsim;

//import madkit.kernel.Agent;
import java.awt.*;
import javax.swing.*;
//import java.io.*;
//import java.util.*;


public class GridCanvas extends JComponent //Canvas
{
  int MAXX;
  int MAXY;

  // for double buffering to prevent flicker
    //     transient Image       offScreenImage;
    // transient Graphics	offScreen;
       //RobotEnvironment world;
       RobotAppEnv world;

    int i, j;

    RobotWorldViewer lwv;
    static final long serialVersionUID = RobotWorldViewer.serialVersionUID; //42L;


    public void flash()
    {
	paintImmediately(getVisibleRect());
    }

  public GridCanvas(int width,int height,RobotWorldViewer l)
    {
	setSize(new Dimension(width,height));
	lwv=l;

	MAXX=getSize().width;
	MAXY=getSize().height;
	/*	offScreenImage = createImage(MAXX, MAXY);
	offScreen = offScreenImage.getGraphics ();
	offScreen.setColor(Color.black);
	offScreen.fillRect(0,0,MAXX,MAXY);
	*/	//	repaint();
    }

    /** Routine de bas niveau affichant l'état du SMA réactif, avec
	double-buffering si disponible */

    public void paintComponent(Graphics g)
    {
	super.paintComponent(g);
	//g.drawImage (offScreenImage, 0, 0, this);
	g.setColor(Color.black);
	g.fillRect(0,0,MAXX,MAXY);
	lwv.paintInfo(g);
    }


/*    public void paintPatch(Graphics g, Patch p, int size)
    {

	int x = p.x;
	int y = p.y;

	try {
	    g.setColor(p.visibleColor);
	    g.fillRect(x*size,y*size,size,size);
	    if (! (p.RobotsHere).isEmpty())
		{
		    Robot t = (Robot) (p.RobotsHere).firstElement();
		    g.setColor(t.color);
		    g.fillOval(x*size,y*size,size,size);
		}
	}
	catch (Exception e)
	    {
		System.err.println("g fault:"+e+" "+g+" "+p);
		e.printStackTrace();
	    }

    } */
}









