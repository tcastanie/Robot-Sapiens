package kernelsim;

import java.awt.*;

// Box est une classe pour l'objet caisse
// on y trouve ses coordonnees initiales, courentes et finales

public class Box {

    public int xi,yi,x,y;
    public boolean fin;
    public int dimx,dimy;
    public Color color;
    public int way[][] = new int[2][20];
    public int longway;
    public boolean trans = false;
    public boolean finish = false;

    public Box(int xi, int yi, int dimx, int dimy, Color color)
    {
     this.xi=xi;
     this.yi=yi;
     x=xi; y=yi;
     fin=false;
     this.dimx=dimx;
     this.dimy=dimy;
     this.color=color;
     longway=0;
    }

    public void addPoint(int xp, int yp)
    {
     way[0][longway]=xp;
     way[1][longway]=yp;
     longway++;
    }

    public void remPoint(int q)
    {
     if ((q>=0) && (q<longway))
       {
	for (int i=q;i<(longway-1);i++)
	    {
	     way[0][i]=way[0][i+1];
	     way[1][i]=way[1][i+1];
	    }
	longway--;
       }
    }

    public boolean isInside(int x, int y)
    {
     return ((x >= xi) && (x <= xi+dimx) && (y >= yi) && (y <= yi+dimy));
    }

    public void drawBox(int x0,int y0,Graphics g)
    {
     g.setColor(color);
     g.fillRect(x0+x,y0+y,dimx,dimy);
    }

    public void drawBoxF(int x0,int y0,Graphics g)
    {
     g.setColor(color);
     g.drawRect(x0+x,y0+y,dimx,dimy);
    }

    public void drawWay(int x0, int y0, Graphics g, Color c)
    {
     int xd=xi+dimx/2;
     int yd=yi+dimy/2;
     int xs,ys;
     g.setColor(c);
     for(int i=0;i<longway;i++)
	 {
	  xs=way[0][i]; ys=way[1][i];
	  g.drawLine(x0+xd,y0+yd,x0+xs,y0+ys);
	  xd=xs; yd=ys;
	 }
     if (fin==true)
	 { g.setColor(color);
	   g.drawRect(x0+xd-dimx/2,y0+yd-dimy/2,dimx,dimy);
	 }
    }

}
