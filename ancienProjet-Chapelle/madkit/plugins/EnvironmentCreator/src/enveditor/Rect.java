package enveditor;

import madkit.kernel.*;
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
     return (isInside(b.x1,b.y1) || isInside(b.x2,b.y1) || isInside(b.x1,b.y2) || isInside(b.x2,b.y2));
    }

    public void drawRect(int x0,int y0,Graphics g)
    {
     g.setColor(color);
     g.fillRect(x0+x1,y0+y1,x2-x1+1,y2-y1+1);

    }
}
