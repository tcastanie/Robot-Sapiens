// Copyright (C) 1997 by MadKit Team
package kernelsim;

import madkit.kernel.*;
//import java.io.*;
//import java.util.*;
import java.awt.*;
//import madkit.simulation.activators.*;


public class RobotWorldViewer extends Watcher implements ReferenceableAgent
{
    public RobotProbeApp detector;
    protected Dimension gridSize;
    protected Dimension visibleSize;

    RobotSchedulerApp sch;
    static final long serialVersionUID = RobotSchedulerApp.serialVersionUID; //42L;
    String group;
    int cellSize = 6;

    RobotAppEnv world;
    GridCanvas onScreen;
    MatEnv grid;
    Shaft[] tabS;
    Rect[] tabR;
    RobotAppPhy[] theRobots; // 040427 Reviendre ?
    Shaft Base;
    Color colbase=Color.red;

	int cpt = 0;
    boolean flash = false;
    boolean show = true;
    boolean VectB = true;
    boolean RadarB = false;
    boolean ComB = false;
    boolean Field = true;

    public void setFlash (boolean add){flash = add;}
    public boolean getFlash(){return flash;}
    public void setShow (boolean add){show = add;}
    public boolean getShow(){return show;}
    public boolean getVectAff(){return VectB;}
    public void setVectAff(boolean b) {VectB=b;}

    public boolean getRadarAff(){return RadarB;}
    public void setRadarAff(boolean b) {RadarB=b;}

    public boolean getCommunicationAff(){return ComB;}
    public void setCommunicationAff(boolean b) {ComB=b;}

    public boolean getField(){return Field;}
    public void setField(boolean b) {Field=b;}

    public RobotWorldViewer(String g,RobotAppEnv re,RobotSchedulerApp s)
    {
	world = re;
	sch=s;
	tabS = world.TabS;
	tabR = world.TabR;
	Base = world.Base;
	grid = world.mat;   //040427 Reviendre
	group = g;
	visibleSize = new Dimension(grid.dimx,grid.dimy);
    }

    public void initGUI()
    {
	//setBean(onScreen = new GridCanvas(grid.dimx,grid.dimy,this)); < deprecated
	onScreen = new GridCanvas(grid.dimx,grid.dimy,this);
	setGUIObject(onScreen);
    }


    public Dimension getVisibleSize()
    {
	return visibleSize;
    }


    public void activate()
    {
	detector = new RobotProbeApp(group,"robot");
	addProbe(detector);
	// update(); < deprecated
	theRobots = detector.getRobots();
	System.err.println(theRobots.length);
	if (!isGroup(group)) { createGroup(true, group, null, null); }
        else { requestRole(group, "member", null); }
	//joinGroup(group);
	requestRole(group,"robot observer",null);
    }

   public int max10(int v)
    {
	int r=v;
	if (v>10) r= 10;
	if (v<-10) r= -10;
	return r;
    }

 public double max10(VecteurR v)
    {
	double r=1;
	if (v.norme()>8)  r=8/v.norme();
	return r;
    }

 public void paintCircle(int x,int y,float r,Graphics g)
    {
	g.setColor(Color.black);
	for(float a=0;a<6.29;a=a+3/r)
	  {
	   int yc=(int)Math.round(r*Math.sin(a));
	   int xc=(int)Math.round(r*Math.cos(a));
	   g.drawLine(x+xc,y+yc,x+xc,y+yc);
	  }
    }

 /** Dans cette classe, on décide de choisir son apparence:
    affichage classique du disque ROBOT*/
    public void paintRobots(Graphics g)
    {
    	if(theRobots != null)

	{
			//System.out.println("nb de robots : "+theRobots.length);
		int x2,y2;
	        int Rc,R0;

		double reduc;

		g.setColor(Color.black);
		for (int i=0;i<theRobots.length;i++)
		    {
		    	// Avant on calculait avec des entiers:
		    	// x et y de RobotSat, alors que RobotAppPhy
		    	// utilise des réels : xr et yr 040428
			// int y=theRobots[i].y;
			// int x=theRobots[i].x;
			int y=Math.round(theRobots[i].yr);
			int x=Math.round(theRobots[i].xr);
			Rc=theRobots[i].Rc;
			R0=theRobots[i].R0;
			g.drawLine(x,y,x,y);

			if (RadarB) paintCircle(x,y,R0,g); // g.drawOval(x-R0,y-R0,2*R0,2*R0);
			if (ComB &&  (theRobots[i].emit==true)) paintCircle(x,y,Rc,g); // g.drawOval(x-Rc,y-Rc,2*Rc,2*Rc);

			if (VectB==true)
			    {
				//float ouv=(float)Math.PI/4;
				int xv,yv;
				/*
				 g.setColor(Color.red);
				 xv=(int)theRobots[i].Vrep.vx;
				 yv=(int)-theRobots[i].Vrep.vy;

				 g.drawLine(x,y,x+xv,y+yv);
				*/
				if (theRobots[i].alt==false) g.setColor(Color.black);
				else // if (theRobots[i].rep==false)
				     g.setColor(Color.blue);
				// else g.setColor(Color.red);

				reduc=max10(theRobots[i].Vtask);
				xv=(int)(theRobots[i].Vtask.vx*reduc);
				yv=(int)(-theRobots[i].Vtask.vy*reduc);

				g.drawLine(x,y,x+xv,y+yv);

				/* <<<<<<<<<<<
				g.setColor(Color.green); // vect signaux rep
				xv=(int)theRobots[i].Vsli.vx;
				yv=(int)-theRobots[i].Vsli.vy;
				*/

				//g.drawLine(x,y,x+xv,y+yv);

			    }
			//  System.out.println(disk.R);

		    g.setColor(Color.green);
		    for (int j=0;j<theRobots.length;j++)
			{
			    if ((i!=j) && (theRobots[i].emit==true)
				&& (theRobots[i].com[j].c==true))
			 {

	                 //y2=theRobots[j].y-y;
			 //x2=theRobots[j].x-x;
			 y2 = Math.round(theRobots[j].yr)-y;
			 x2 = Math.round(theRobots[j].xr)-x;
			 // Math.round(theRobots[i].yr)
			 g.drawLine(x,y,x+x2,y+y2);
			 }
			}
		    }
	}
    }


    public void paintEnv(Graphics g)
    {
	//    if (first_paint==true)
	//      {
	//     world.draw(0,0,g,false);
   	// ========================= affichage env ===========
	//System.out.println("dans paintEnv");

        g.setColor(Color.white);
	g.fillRect(0,0,grid.dimx,grid.dimy);

	Font font=new Font("sanserif",Font.PLAIN,9);
        g.setFont(font);

	g.setColor(world.color_wall);
	for(int i=1;i<world.nbR;i++)
	    {
		tabR[i].drawRect(0,0,g);
	    }
	for(int i=0;i<world.nbS;i++)
	    {
	      tabS[i].drawShaftE(0,0,g);

		 String s= String.valueOf(tabS[i].vol);
		 g.drawString(s,tabS[i].x-2,tabS[i].y+20);
	    }
	g.setColor(colbase);
	Base.drawShaft(0,0,g);

           String s= String.valueOf(Base.vol);
	   g.drawString(s,Base.x-2,Base.y+20);
        //


        if // ((cpt==0) ||
           ((Field==true))
	    {
		int val;
		Vecteur vR=new Vecteur(0,0);
		VecteurR vrep=new VecteurR((float)0,(float)0);
		VecteurR vslide=new VecteurR((float)0,(float)0);
		for(int i=3;i<grid.dimx;i+=5)
		    for(int j=3;j<grid.dimy;j+=5)
			{
			    val=grid.get_val(i,j);
			    if (val!=-1)
				{
				    vR=grid.radar(i,j,1,10,3,20);
				    vrep.fix((float)vR.vx,(float)vR.vy);
				    vrep.mult(0.4);
				    vslide.fix((float)vR.vx,(float)vR.vy);
				    vslide.mult(0.8);

				    vslide.var((float)(Math.PI/2));
				    if ((cpt%2)==0) vrep.draw(i,j,g,Color.blue);
				    else if ((cpt%2)==1) vslide.draw(i,j,g,Color.blue);
				}
			}

	    }
	if (Field==true) cpt++;

	 font=new Font("sanserif",Font.PLAIN,10);
         g.setFont(font);

	 g.setColor(Color.blue);
	 s= String.valueOf(sch.iteration);
	 g.drawString(s,10,10);

	//	first_paint=false;
//      }
    }


    public void paintInfo(Graphics g)
    {
	paintEnv(g);
	paintRobots(g);
    }


    public void observe()
    {

	if (show)
	    {
		if (! flash) onScreen.repaint();
		else onScreen.flash();
	    }
    }
}













