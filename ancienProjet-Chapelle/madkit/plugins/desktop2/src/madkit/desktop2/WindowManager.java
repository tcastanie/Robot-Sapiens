package madkit.desktop2;

import java.awt.*;
import java.util.*;

abstract class WindowManager{
	protected LinkedList components;
	protected Dimension containerSize;
	protected Desktop desktop;
	
	public WindowManager(Desktop desktop){
		components = new LinkedList();
		this.desktop = desktop;
	}
	public void addComponent(Component c){
		components.add(c);
	}
	public void removeComponent(Component c){
		components.remove(c);
	}
	public void reOrderAllComponent(){
		reset();
		Iterator it = components.iterator();
	    while(it.hasNext()){
	    	Component c = (Component)it.next();
	    	orderComponent(c);
	    }
	}
	
	abstract protected void orderComponent(Component c);
	abstract protected void reset();
}
