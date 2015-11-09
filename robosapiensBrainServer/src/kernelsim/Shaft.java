package kernelsim;

import java.awt.*;

// Shaft est une classe pour representer les puits
// on y trouve ses coordonnees, son debit, son volume total

public class Shaft {

    public int x,y;
    public int dimx,dimy;
    public Color color;
    public int deb;
    public int debR;
    public int nbr=0;
    public int vol,oldvol;

    public Shaft(int x, int y, int dimx, int dimy, int deb, int vol, Color color)
    {
     this.x=x;
     this.y=y;
     this.dimx=dimx;
     this.dimy=dimy;
     this.deb=deb;
     this.vol=vol;
     this.color=Color.red;
    }

    public boolean isInside(int xp, int yp)
    {
     return ((xp >= x) && (xp <= x+dimx) && (yp >= y) && (yp <= y+dimy));
    }

    public boolean isInter(Rect b)
    {
     return (isInside(b.x1,b.y1) || isInside(b.x2,b.y1) || isInside(b.x1,b.y2) || isInside(b.x2,b.y2));
    }

    public void drawShaft(int x0,int y0,Graphics g)
    {
     g.setColor(color);
     g.fillRect(x0+x,y0+y,dimx,dimy);
     /*   g.setColor(Color.black);
     g.drawRect(x0+x,y0+y,dimx,dimy);
     */
    }

    public void drawShaftE(int x0,int y0,Graphics g)
    {
     g.setColor(color);
     g.drawRect(x0+x,y0+y,dimx,dimy);

    }

   public void drawC(int x0,int y0,Graphics g)
    {
     g.setColor(Color.white);
     int xc=x+dimx/2;
     int yc=y+dimy/2;
     int R=vol;
     g.drawOval(x0+xc-R,y0+yc-R,2*R,2*R);
    }

}
