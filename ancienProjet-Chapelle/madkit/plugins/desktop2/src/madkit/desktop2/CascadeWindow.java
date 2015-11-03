package madkit.desktop2;

import java.awt.*;
public class CascadeWindow extends WindowManager{
	int dx, dy, x, y;
	
	public CascadeWindow(Desktop desktop){
		super(desktop);
		dx = 20;
		dy = 20;
		x = 0;
		y = 0;
	}
	public CascadeWindow(Desktop desktop, int dx, int dy){
		super(desktop);
		this.dx = dx;
		this.dy = dy;
		x = 0;
		y = 0;
	}
	protected void orderComponent(Component c){
	    c.setLocation(x, y);
	    desktop.toFront(c);
	    if (desktop != null){
		    x += dx;
		    y += dy;
		    if ((y + c.getSize().getHeight() > desktop.getHeight()) || (x + c.getSize().getWidth() > desktop.getWidth())){
		    	x = 0;
		    	y = 0;
		    }
		}
	}
	protected void reset(){
		x = 0;
	    y = 0;
	}
}
