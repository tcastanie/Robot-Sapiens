package madkit.share;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Graphics;

import javax.swing.JPanel;

public class ListFile extends JPanel implements vList {

  private JPanel IconPanel;
  private int MaxItemAffichable = 0;
  private Color BackGround = Color.white;
  private Color TextColor = Color.black; 
  private int Nb_Y, Nb_X, SIZE_X=90, SIZE_Y=90;
  
 

  ListFile() {
  	
     
       	setLayout(new BorderLayout());
     	IconPanel = new JPanel();
	
	
        IconPanel.setBackground(BackGround);          		
      
    	
   	add(IconPanel,BorderLayout.CENTER);
	
      	
  }

  public void clear() {
    
    IconPanel.removeAll(); 
    
    
    repaint();
  }

  public void addItem(String item, String pathitem) {
    
    IconPanel.add(new Icon(item,pathitem,SIZE_X,SIZE_Y,false));
    repaint();
  }
  
  public int countItems() {
    
    return IconPanel.getComponentCount();
  }

  public boolean mouseDown(Event Ev, int x, int y) {
   
    if ((Ev.clickCount == 1) && (Ev.modifiers==0)) {
      if (Ev.arg != null)
	
	  postEvent(new Event(Ev.target, Event.ACTION_EVENT, Ev.arg));}
    repaint();
    return true;
  }

 

   public void paint(Graphics g)
    {

      update(g);
    }


    public void update(Graphics g) {
	

		Dimension tmp = getSize();
	

	Color bg = getBackground();
	Color fg = getForeground();

	Nb_Y= tmp.height/SIZE_Y;
	Nb_X= tmp.width/SIZE_X;


	g.setColor(TextColor);
    


	Component Tab[] = IconPanel.getComponents();
	for (int i=0; i<countItems(); i++) {
	    //System.out.println(Tab[i]);
	    int x=i%Nb_X;
	    int y=i/Nb_X;
	    Tab[i].setSize(SIZE_Y,SIZE_X);
	    Tab[i].setLocation(x*SIZE_X,y*SIZE_Y);
	    Tab[i].setVisible(true);
	}

		g.setColor(bg);
   
    
    }
}

