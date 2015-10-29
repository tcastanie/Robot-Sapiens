package kernelsim;

import java.awt.*;

public class CirAgent {

    public int xc,yc;
    public int R;
    public Color color;

    int dimy=0;


    public CirAgent(int xc, int yc, int R, Color color)
    {
     this.xc=xc;
     this.yc=yc;
     this.R=R;
     this.color=color;
    }

    public boolean isInside(int x, int y)
    {
     return (Math.sqrt((x-xc)*(x-xc)+(y-yc)*(y-yc)) <= R);
    }

    public boolean isInter(CirAgent a)
    {
     return (Math.sqrt((this.xc-a.xc)*(this.xc-a.xc)+(this.yc-a.yc)*(this.yc-a.yc)) <= (R+a.R));
    }

    public void draw(int x0,int y0,Graphics g)
    {
     g.setColor(color);
     //g.fillOval(x0+xc-R,y0+yc-R,R*2,R*2);
     switch (R) {
     case 7 :
     {
     // System.out.println("trace"+xc+" "+yc+" "+color);
      g.fillRect(x0+xc-4,y0+yc-4,9,9);
      g.drawLine(x0+xc-3,y0+yc-5,x0+xc+3,y0+yc-5);
      g.drawLine(x0+xc-3,y0+yc+5,x0+xc+3,y0+yc+5);
      g.drawLine(x0+xc-5,y0+yc-3,x0+xc-5,y0+yc+3);
      g.drawLine(x0+xc+5,y0+yc-3,x0+xc+5,y0+yc+3);
      g.drawLine(x0+xc-1,y0+yc-6,x0+xc+1,y0+yc-6);
      g.drawLine(x0+xc-1,y0+yc+6,x0+xc+1,y0+yc+6);
      g.drawLine(x0+xc-6,y0+yc-1,x0+xc-6,y0+yc+1);
      g.drawLine(x0+xc+6,y0+yc-1,x0+xc+6,y0+yc+1);
     }
     }
    }


   public int transfo(int a,int b)
    {
	return (dimy+a-b);
    }


    public void draw3D(int dim,int x0,int y0,Graphics g)
    {
	dimy=dim;
	g.setColor(color);
	//g.fillOval(x0+xc-R,y0+yc-R,R*2,R*2);
	switch (R) {
	case 7 :
	    {
		// System.out.println("trace"+xc+" "+yc+" "+color);
		int[] xp= new int[4];
		int[] yp= new int[4];

		xp[0] = transfo(x0+xc-4,y0+yc-4);
		xp[1] = transfo(x0+xc-4+9,y0+yc-4);
		xp[2] = transfo(x0+xc-4+9,y0+yc-4+9);
		xp[3] = transfo(x0+xc-4,y0+yc-4+9);
		yp[0]=y0+yc-4;
		yp[1]=y0+yc-4;
		yp[2]=y0+yc-4+9;
		yp[3]=y0+yc-4+9;

		g.fillPolygon(xp,yp,4);

		g.drawLine(transfo(x0+xc-3,y0+yc-5),yc-5,transfo(x0+xc+3,y0+yc-5),y0+yc-5);
		g.drawLine(transfo(x0+xc-3,y0+yc+5),yc+5,transfo(x0+xc+3,y0+yc+5),y0+yc+5);
		g.drawLine(transfo(x0+xc-5,y0+yc-3),yc-3,transfo(x0+xc-5,y0+yc+3),y0+yc+3);
		g.drawLine(transfo(x0+xc+5,y0+yc-3),yc-3,transfo(x0+xc+5,y0+yc+3),y0+yc+3);
		g.drawLine(transfo(x0+xc-1,y0+yc-6),yc-6,transfo(x0+xc+1,y0+yc-6),y0+yc-6);
		g.drawLine(transfo(x0+xc-1,y0+yc+6),yc+6,transfo(x0+xc+1,y0+yc+6),y0+yc+6);
		g.drawLine(transfo(x0+xc-6,y0+yc-1),yc-1,transfo(x0+xc-6,y0+yc+1),y0+yc+1);
		g.drawLine(transfo(x0+xc+6,y0+yc-1),yc-1,transfo(x0+xc+6,y0+yc+1),y0+yc+1);
	    }
     }
    }

    public boolean isInter(Rect r)
    {
     boolean ans=false;
     if (r.isInside(xc,yc)==true) ans=true;
     else
      {
       if ((r.x1<=xc+R) && (r.x1>=xc-R))
	if ((r.y1<=yc) && (r.y2>=yc)) ans=true;
         else if ((isInside(r.x1,r.y1)==true) || (isInside(r.x1,r.y2))) ans=true;
       if ((r.x2<=xc+R) && (r.x2>=xc-R))
	if ((r.y1<=yc) && (r.y2>=yc)) ans=true;
         else if ((isInside(r.x2,r.y1)==true) || (isInside(r.x2,r.y2))) ans=true;
       if ((r.y1<=yc+R) && (r.y1>=yc-R))
	if ((r.x1<=xc) && (r.x2>=xc)) ans=true;
         else if ((isInside(r.x1,r.y1)==true) || (isInside(r.x2,r.y1))) ans=true;
       if ((r.y2<=yc+R) && (r.y2>=yc-R))
	if ((r.x1<=xc) && (r.x2>=xc)) ans=true;
         else if ((isInside(r.x1,r.y2)==true) || (isInside(r.x2,r.y2))) ans=true;
      }
     return ans;
    }

}






