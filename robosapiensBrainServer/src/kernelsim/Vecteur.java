package kernelsim;

import java.awt.*;

/** Vecteur est une petite classe pour gerer un vecteur par
 *  ses composantes X,Y entieres
 */

public class Vecteur {

    public int vx,vy;
    public byte s=1; // info signe angle

    public Vecteur(int vx, int vy)
    {
     this.vx=vx;
     this.vy=vy;
    }

    public Vecteur(Vecteur V)
    {
     this.vx=V.vx;
     this.vy=V.vy;
     this.s=V.s;
    }

    public void draw(int x,int y,Graphics g)
    {
     g.setColor(Color.blue);
     int xv=x+vx;
     int yv=y-vy;
     g.drawLine(x,y,xv,yv);
    }

    public void addV(int nx, int ny)
    {
     vx=vx+nx;
     vy=vy+ny;
    }

    public void addV(Vecteur v)
    {
     vx=vx+v.vx;
     vy=vy+v.vy;
    }

    public double norme()
    {
     return(Math.sqrt(vx*vx+vy*vy));
    }

    public void fix(int vx,int vy)
    {
     this.vx=vx;
     this.vy=vy;
    }

    public void fixN(float l)
    {
     mult(l/norme());
    }

    public void mult(double c)
    {
     vx=(int)Math.round(vx*c);
     vy=(int)Math.round(vy*c);
    }

    public void print()
    {
     System.out.println(vx+" "+vy);
    }

}

