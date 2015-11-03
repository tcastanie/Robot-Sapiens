// Copyright (C) 1997 by MadKit Team
package kernelsim;

import madkit.kernel.*;
//import java.io.*;
//import java.util.*;
import java.awt.*;
//import java.lang.*;
//import madkit.simulation.activators.*;


public class RobotWorldViewer3D extends Watcher  implements ReferenceableAgent
{
	static final long serialVersionUID = RobotAppEnv.serialVersionUID; //42L;
    public RobotProbeApp detector;
    protected Dimension gridSize;
    protected Dimension visibleSize;

    public static float fluidity=1;


    public int HVECT;
    int P=25,dx,dy,h;
    int Rc;
    int R0;
    double dd;
    int vm=50;
    int vmax=50;

    int premier=0;

    Color coltxt=Color.blue;
    Color colbase=Color.red;

    String group;
    int cellSize = 6;
    RobotAppEnv world;
    GridCanvas3D onScreen;
    MatEnv grid;
    Shaft[] tabS;
    Rect[] tabR;
    Shaft Base;
    RobotAppPhy[] theRobots;

    int Npoints=20;
    int d=50;
    int[][] Surface;
    int[][] SO;
    int HM;

	int cpt = 0;
    boolean flash = false;
    boolean show = true;
    boolean RadarB = false;
    boolean ComB = false;
    boolean SatB = false;
    boolean VectB = true;
    boolean SurfB = true;
    boolean PersoB = false;
    RobotSchedulerApp sch;

    public void setFlash (boolean add){flash = add;}
    public boolean getFlash(){return flash;}
    public void setShow (boolean add){show = add;}
    public boolean getShow(){return show;}

    public boolean getRadarAff(){return RadarB;}
    public void setRadarAff(boolean b) {RadarB=b;}

    public boolean getCommunicationAff(){return ComB;}
    public void setCommunicationAff(boolean b) {ComB=b;}

    public boolean getSatIAff(){return SatB;}
    public void setSatIAff(boolean b) {SatB=b;}

    public boolean getVectAff(){return VectB;}
    public void setVectAff(boolean b) {VectB=b;}

    public boolean getSurface(){return SurfB;}
    public void setSurface(boolean b) {SurfB=b;}

    public boolean getS_P_I(){return PersoB;}
    public void setS_P_I(boolean b) {PersoB=b;}


    public RobotWorldViewer3D(String g,RobotAppEnv re,RobotSchedulerApp s)
    {
	world = re;
	sch=s;
	tabS = world.TabS;
	tabR = world.TabR;
	Base = world.Base;
	grid = world.mat;
	group = g;
	HVECT=(max(grid.dimy,grid.dimx))/2;
	h=P;
	dx=grid.dimx/(Npoints-1)+1;
	dy=grid.dimy/(Npoints-1)+1;
	dd=Math.sqrt(dx*dx+dy*dy);
	visibleSize = new Dimension(grid.dimx+grid.dimy,grid.dimy+2*HVECT);
	Surface=new int[Npoints][Npoints];
	SO=new int[Npoints][Npoints];
    }

    public void initGUI()
    {
	//setBean(onScreen = new GridCanvas3D(grid.dimx+grid.dimy,grid.dimy+2*HVECT,this)); < deprecated
	onScreen = new GridCanvas3D(grid.dimx+grid.dimy,grid.dimy+2*HVECT,this);
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
	//update();
	theRobots = detector.getRobots();
	System.err.println(""+this+": nb of robots = "+theRobots.length);
	if (!isGroup(group)) { createGroup(true, group, null, null); }
	else { requestRole(group, "member", null); }
	//joinGroup(group);
	requestRole(group,"robot observer",null);
    }

    public void paintCircle(int x,int y,float r,Graphics g)
    {
	g.setColor(Color.black);
	for(float a=0;a<6.29;a=a+3/r)
	  {
	   int yc=y+(int)Math.round(r*Math.sin(a));
	   int xc=transfo(x+(int)Math.round(r*Math.cos(a)),yc);
	   g.drawLine(xc,yc+2*HVECT,xc,yc+2*HVECT);
	  }
    }

    public int max(int v1,int v2,int v3,int v4)
    { int v5,v6;
      if (v1>v2) {v5=v1;} else {v5=v2;}
      if (v3>v4) {v6=v3;} else {v6=v4;}
      if (v5>v6)
      	{return v5;} 
      //else
      return v6;
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

    public int max (int v1,int v2)
    {
	if (Math.abs(v1)>Math.abs(v2))
		{return v1;} 
		//else
		return v2;
    }

    public void paintRobots(Graphics g)
    {
		//System.out.println("nb de robots : "+theRobots.length);
	int x=0,y=0,xv=0,yv=0,xn=0,v;
	//int R;
	vmax=0;
        double reduc;
	if(theRobots == null && detector != null )	theRobots = detector.getRobots();
	if(theRobots != null)
	{
		for (int i=0;i<theRobots.length;i++)
		    {
			//y=theRobots[i].y;
			y = Math.round(theRobots[i].yr);
			//xn=theRobots[i].x;
			xn= Math.round(theRobots[i].xr);
			x=transfo(xn,y);
			Rc=theRobots[i].Rc;
			R0=theRobots[i].R0;
			if (RadarB) paintCircle(xn,y,(float)R0,g);
			if (ComB &&  (theRobots[i].emit==true)) paintCircle(xn,y,(float)Rc,g); // Rc/2

			g.setColor(Color.blue);

			if (SurfB)
			{
			 int c=Rc/dx;
			 if (c==0) c=1;
			 int x1=xn/dx;
			 int y1=y/dy;
			 int ds,xs1,ys1,xp,yp;

			 float sat;
			 if (PersoB) { sat=theRobots[i].Satp; c=1;}
		 	 else if (theRobots[i].emit==true)  sat=-theRobots[i].Sati; else sat=0;

			 for (int pi=1;pi<=(2*c);pi++)
			  for (int pj=1;pj<=(2*c);pj++)
			  {
			      xs1=x1-c+pi; xp=xs1*dx;
			      ys1=y1-c+pj; yp=ys1*dy;
			      ds=(int)Math.sqrt((xn-xp)*(xn-xp)+(y-yp)*(y-yp));
			      if (ds<Rc)
			       {
				try{

				    if ((int)Math.abs(sat*((Rc*Rc)-(ds*ds)))>Math.abs(Surface[ys1][xs1]))
					Surface[ys1][xs1]=(int)(sat*((Rc*Rc)-(ds*ds)));
				    v=(int)Math.abs(Surface[ys1][xs1]);
				}
				catch (ArrayIndexOutOfBoundsException e1) { v=0; }
			       if (v>vmax) vmax=v;
			       }
			  }
			}


				g.drawLine(x,y+2*HVECT,x,y+2*HVECT);


			      {

			       //String s= String.valueOf((int)(theRobots[i].N_agent));
			       Font font=new Font("sanserif",Font.PLAIN,8);
			       g.setFont(font);
			       //g.drawString(s,x-2,y+2*HVECT-20);
			       g.setColor(coltxt);
			       // g.drawString(theRobots[i].txt,x-4,y+2*HVECT-8);
			      }


			if (VectB==true)
			    {
				//float ouv=(float)Math.PI/4;
				/*
				if (RmcLauncher.MS==true)
				{

				 g.setColor(Color.red);
				 xv=(int)theRobots[i].Vsa.vx;
				 yv=(int)-theRobots[i].Vsa.vy;
				 yv=y+yv;
				 xv=transfo(xn+xv,yv);
				 g.drawLine(x,y+2*HVECT,xv,yv+2*HVECT);

				}


				 g.setColor(Color.red);
				 xv=(int)theRobots[i].Vrep.vx;
				 yv=(int)-theRobots[i].Vrep.vy;
				 yv=y+yv;
				 xv=transfo(xn+xv,yv);
				 g.drawLine(x,y+2*HVECT,xv,yv+2*HVECT);
				*/
				g.setColor(Color.blue);
				reduc=max10(theRobots[i].Vtask);
				xv=(int)(theRobots[i].Vtask.vx*reduc);
				yv=(int)(-theRobots[i].Vtask.vy*reduc);
				yv=y+yv;
				xv=transfo(xn+xv,yv);
				g.drawLine(x,y+2*HVECT,xv,yv+2*HVECT);
				/*
				g.setColor(Color.black);
				xv=(int)theRobots[i].Vsli.vx;
				yv=(int)-theRobots[i].Vsli.vy;
				yv=y+yv;
				xv=transfo(xn+xv,yv);
				g.drawLine(x,y+2*HVECT,xv,yv+2*HVECT);
				*/
			    }

		}
	}
    }



    public int transfo(int a,int b)
    {
	return (grid.dimy+a-b);
    }


    public void paintEnv(Graphics g)
    {
	//    if (first_paint==true)
	//      {
	//     world.draw(0,0,g,false);
   	// ========================= affichage env ===========
	//System.out.println("dans paintEnv");

    	//051018 int v;


        g.setColor(Color.white);
	g.fillRect(0,0,grid.dimx+grid.dimy,grid.dimy+2*HVECT);

	g.setColor(coltxt);
	fluidity=(float)theRobots[0].fluid/(float)theRobots[0].mvt;

	g.drawString("f = "+String.valueOf(fluidity),grid.dimx+25,grid.dimy+2*HVECT-10);

	g.setColor(world.color_wall);
	int yr=2*HVECT;

	//g.drawLine(transfo(0,0),HVECT,transfo(0,0),yr);
	//g.drawLine(transfo(0,0)-5,3*HVECT/2,transfo(0,0)+5,3*HVECT/2);
	g.drawLine(transfo(0,0),yr,transfo(0,grid.dimy),yr+grid.dimy);
	g.drawLine(transfo(0,0),yr,transfo(grid.dimx,0),yr);
	g.drawLine(transfo(grid.dimx,0),yr,transfo(grid.dimx,grid.dimy),yr+grid.dimy);
	g.drawLine(transfo(0,grid.dimy),yr+grid.dimy,transfo(grid.dimx,grid.dimy),yr+grid.dimy);

	int[] xp= new int[4];
	int[] yp= new int[4];

	for(int i=1;i<world.nbR;i++)
	    {
		xp[0] = transfo(tabR[i].x1, tabR[i].y1);
		xp[1] = transfo(tabR[i].x2, tabR[i].y1);
		xp[2] = transfo(tabR[i].x2, tabR[i].y2);
		xp[3] = transfo(tabR[i].x1, tabR[i].y2);
		yp[0]= tabR[i].y1+yr;
		yp[1]= tabR[i].y1+yr;
		yp[2]= tabR[i].y2+yr;
		yp[3]= tabR[i].y2+yr;

		g.fillPolygon(xp,yp,4);

                if (SurfB && (premier==0))
		{
              	 int x1=(tabR[i].x1+4)/dx;
                 int x2=(tabR[i].x2-4)/dx;
		 int y1=(tabR[i].y1+4)/dy;
                 int y2=(tabR[i].y2-4)/dy;
		 int xs1,ys1;

		 for (int pi=x1;pi<=x2;pi++)
		  for (int pj=y1;pj<=y2;pj++)
		  {
		      xs1=pi;
		      ys1=pj;

			try{
				SO[ys1][xs1]=1;
			}
			catch (ArrayIndexOutOfBoundsException e1) { //051018 v=0;
			System.out.println("WV3D:erreur de tableau");
			}

		  }
		}

	    }


	Font font=new Font("sanserif",Font.PLAIN,8);
        g.setFont(font);

	for(int i=0;i<world.nbS;i++)
	    {
		g.setColor(world.box);

		xp[0] = transfo(tabS[i].x, tabS[i].y);
		xp[1] = transfo(tabS[i].x+tabS[i].dimx, tabS[i].y);
		xp[2] = transfo(tabS[i].x+tabS[i].dimx, tabS[i].y+tabS[i].dimy);
		xp[3] = transfo(tabS[i].x, tabS[i].y+tabS[i].dimy);
		yp[0]= tabS[i].y+yr;
		yp[1]= tabS[i].y+yr;
		yp[2]= tabS[i].y+tabS[i].dimy+yr;
		yp[3]= tabS[i].y+tabS[i].dimy+yr;

		 g.drawPolygon(xp,yp,4);

		  g.setColor(coltxt);
		 String s= String.valueOf(tabS[i].vol);
		 g.drawString(s,xp[0],yp[0]-5);
	    }

	       g.setColor(colbase);

		xp[0] = transfo(Base.x, Base.y);
		xp[1] = transfo(Base.x+Base.dimx, Base.y);
		xp[2] = transfo(Base.x+Base.dimx, Base.y+Base.dimy);
		xp[3] = transfo(Base.x, Base.y+Base.dimy);
		yp[0]= Base.y+yr;
		yp[1]= Base.y+yr;
		yp[2]= Base.y+Base.dimy+yr;
		yp[3]= Base.y+Base.dimy+yr;
		g.fillPolygon(xp,yp,4);

		 g.setColor(coltxt);
		 String s= String.valueOf(Base.vol);
		 g.drawString(s,xp[0],yp[0]-5);


	 font=new Font("sanserif",Font.PLAIN,10);
         g.setFont(font);

	 g.setColor(coltxt);
	 s= String.valueOf(sch.iteration);
	 g.drawString("step "+s,20,60+d);


	 if (premier==0) premier=1;
    }

  public int sign(int v)
    {
      if (v<0) {return -1;}
      //else
      return 1;
    }

  public void paintSurf(Graphics g)
    {
	  // boucle dessin surface et remise a zero

	Color line;
	int y=0,yc,x1=0,y1=0,x2=0,y2=0,x3=0,y3=0,s1,s2,s3;

        int r=vmax/h;  // vmax*vmax/h;
        double a=1.5;

        if (r==0) r=1;

        Font font=new Font("sanserif",Font.PLAIN,10);
         g.setFont(font);

	g.setColor(coltxt);

	if (PersoB)
	g.drawString("Sat P",20,20+d);
	else g.drawString("Surf. =  - Sat I",20,40+d);

	HM=12;

    // obs.

      g.setColor(Color.lightGray);


      /*
	       for(int j=0;j<Npoints;j++)
	       for(int i=0;i<Npoints-1;i++)
		  {
		   if (i>0)
		       { x1=x2; y1=y2;}
		   else
		       {
			y=j*dy;
			x1=transfo(i*dx,y);
			s1=(int)(Surface[j][i]*a/r);

			if (SO[j][i]==1) s1+=HM;

			y1=y-(int)(s1);
		       }

		      x2=x1+dx;
		      s2=(int)(Surface[j][i+1]*a/r);

		      if (SO[j][i+1]==1) s2+=HM;

		      y2=y-(int)(s2);
		      yc=max(y1-y,y2-y)/2;
		      //      System.out.print(yc+" ");



		     if ((Surface[j][i]!=0) || (Surface[j][i+1]!=0))
			 {
			  line = new Color(140-2*yc,150+yc,200); //0
			  g.setColor(line);
			 }
		         else  g.setColor(Color.lightGray);

		      g.drawLine(x1,y1+h+d,x2,y2+h+d);


		  }



	         for(int i=0;i<Npoints;i++)
	         for(int j=0;j<Npoints-1;j++)
		  {
		   if (j>0)
		       { x1=x3; y1=y3;}
		   else
		       {
			y=j*dy;
			x1=transfo(i*dx,y);
	 	        s1=(int)(Surface[j][i]*a/r);

			if (SO[j][i]==1) s1+=HM;

			y1=y-(int)(s1);
		       }

		   y=j*dy;
		   x3=transfo(i*dx,y+dy);

		    s3=(int)(Surface[j+1][i]*a/r);

		      if (SO[j+1][i]==1) s3+=HM;

		      y3=y+(int)(dy-s3);
		      yc=max(y1-y,y3-y-dy)/2;
		      // System.out.println(yc);


		      if ((Surface[j][i]!=0) || (Surface[j][i+1]!=0))
			 {
			  line = new Color(140-2*yc,150+yc,200); //0
			  g.setColor(line);
			 }
		         else  g.setColor(Color.lightGray);

		       g.drawLine(x1,y1+h+d,x3,y3+h+d);


		  }

	  */
	       for(int j=0;j<Npoints;j++)
	       for(int i=0;i<Npoints-1;i++)
		  {
		   if (i>0)
		       { x1=x2; y1=y2;}
		   else
		       {
			y=j*dy;
			x1=transfo(i*dx,y);

			s1=(int)(Surface[j][i]*a/r);

			if (SO[j][i]==1) s1+=HM;

			y1=y-(int)(s1);

		       }

		      x2=x1+dx;
		      s2=(int)(Surface[j][i+1]*a/r);

		      if (SO[j][i+1]==1) s2+=HM;

		      y2=y-(int)(s2);

		      yc=max(y1-y,y2-y)/2;
		      //      System.out.print(yc+" ");

		      if ((Surface[j][i]!=0) || (Surface[j][i+1]!=0))
		      {
			   line = new Color(140-2*yc,50+yc,0); //10
		       g.setColor(line);
		       }
		       else  g.setColor(Color.lightGray);

		       g.drawLine(x1,y1+h+d,x2,y2+h+d);
		  }


	         for(int i=0;i<Npoints;i++)
	         for(int j=0;j<Npoints-1;j++)
		  {
		   if (j>0)
		       { x1=x3; y1=y3;}
		   else
		       {
			y=j*dy;
			x1=transfo(i*dx,y);

			  s1=(int)(Surface[j][i]*a/r);

			if (SO[j][i]==1) s1+=HM;

			y1=y-(int)(s1);
		       }

		   y=j*dy;
		   x3=transfo(i*dx,y+dy);


		    s3=(int)(Surface[j+1][i]*a/r);

		      if (SO[j+1][i]==1) s3+=HM;

		      y3=y+(int)(dy-s3);

		      yc=max(y1-y,y3-y-dy)/2;
		      // System.out.println(yc);

		      if ((Surface[j][i]!=0) || (Surface[j+1][i]!=0))
		      {

			  line = new Color(140-2*yc,50+yc,0); // 150+yc
		       g.setColor(line);
		      }
		      else   g.setColor(Color.lightGray);

		      g.drawLine(x1,y1+h+d,x3,y3+h+d);


		       Surface[j+1][i]=0;
		       Surface[j][i]=0;
		  }


    }

    public void paintInfo(Graphics g)
    {
	paintEnv(g);
	paintRobots(g);
        if (SurfB) paintSurf(g);
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
