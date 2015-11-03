package enveditor;

import madkit.kernel.Agent;
import javax.swing.*;
import java.util.*;

import madkit.kernel.*;
import java.awt.*;
import java.awt.event.*;

public class TextIn extends Dialog {

    TextField t;
    protected Button button;

    public TextIn(JPanel parent,String txt,String defaut)
    {
    	super((JFrame)null,txt,true);
	//super(parent,txt,true);
	this.setSize(400,150);
        this.enableEvents(AWTEvent.MOUSE_EVENT_MASK);
	this.requestFocus();

	ActionListener listener = new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		TextIn.this.dispose();
		if (listeners != null)
		 listeners.actionPerformed(new ActionEvent(TextIn.this,e.getID(),e.getActionCommand()));
	    }
	};
	
	this.setLayout(new BorderLayout(15,15));
        t= new TextField(defaut,20); //14
	Panel p0 = new Panel();
	p0.setLayout(new FlowLayout(FlowLayout.CENTER,15,15));
	p0.add(t);
	this.add("Center",t);

	button = new Button("Ok");
	button.setActionCommand("Ok");
	button.addActionListener(listener);

	Panel p =new Panel();
	p.setLayout(new FlowLayout(FlowLayout.CENTER,15,15));
	p.add(button);
	this.add("South",p);

	this.pack();
     
    }

   protected ActionListener listeners = null;

    public void addActionListener(ActionListener l) {
	listeners = AWTEventMulticaster.add(listeners,l);
    }
   
    public void removeActionListener(ActionListener l) {
	listeners = AWTEventMulticaster.remove(listeners,l);
    }

    public String getS()
    {
     String txt=t.getText();
     return txt;
    }
}
