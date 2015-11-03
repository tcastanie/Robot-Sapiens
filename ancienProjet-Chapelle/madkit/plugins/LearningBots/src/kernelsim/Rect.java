package kernelsim;

import java.awt.*;

public class Rect {

  public int x1,y1,x2,y2;
  public Color color;

  public Rect(int x1, int y1, int x2, int y2,Color color)
    {
	this.x1 = x1;
	this.y1 = y1;
	this.x2 = x2;
	this.y2 = y2;
	this.color = color;
    }

    public Rect() { this(0,0,0,0,Color.blue); }

    public boolean isInside(int x, int y)
    {
     return ((x >= x1) && (x <= x2) && (y >= y1) && (y <= y2));
    }

    public boolean isInter(Rect b)
    {
      //System.out.println(this.x1+","+this.y1+","+this.x2+","+this.y2+"/"+
      //                    b.x1+","+b.y1+","+b.x2+","+b.y2);
     return (
              (isInside(b.x1,b.y1) || isInside(b.x2,b.y1) || isInside(b.x1,b.y2) || isInside(b.x2,b.y2))
            ||(b.isInside(this.x1,this.y1) || b.isInside(this.x2,this.y1) || b.isInside(this.x1,this.y2) || b.isInside(this.x2,this.y2))
              );
    }

    public void drawRect(int x0,int y0,Graphics g)
    {
     g.setColor(color);
     g.fillRect(x0+x1,y0+y1,x2-x1+1,y2-y1+1);
    }

   public void drawC(int x0,int y0,int R,Graphics g)
    {
     g.setColor(color);
     int xc=x1+(x2-x1)/2;
     int yc=y1+(y2-y1)/2;
     g.drawOval(x0+xc-R,y0+yc-R,2*R,2*R);
    }
}
