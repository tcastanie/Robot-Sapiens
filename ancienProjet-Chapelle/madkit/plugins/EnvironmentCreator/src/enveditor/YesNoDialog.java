package enveditor;

import madkit.kernel.Agent;
import javax.swing.*;
import java.util.*;

import madkit.kernel.*;
import java.awt.*;
import java.awt.event.*;

public class YesNoDialog extends Dialog {

    public YesNoDialog(JPanel parent,String title,String message, String yes_label, String no_label, String cancel_label)
    {
    	super((JFrame)null,title,true);
	//super(parent,title,true);
        
	this.setLayout(new BorderLayout(15,15));
	this.add("Center", new Label(message));

	ActionListener listener = new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		YesNoDialog.this.dispose();
		if (listeners != null)
		 listeners.actionPerformed(new ActionEvent(YesNoDialog.this,e.getID(),e.getActionCommand()));
	    }
	};
	
	Panel bb = new Panel();
	bb.setLayout(new FlowLayout(FlowLayout.CENTER,25,15));
	this.add("South",bb);

	if (yes_label != null)
	    {
	     Button yes = new Button(yes_label);
	     yes.setActionCommand("yes");
	     yes.addActionListener(listener);
	     bb.add(yes);
	    }

	if (no_label != null)
	    {
	     Button no = new Button(no_label);
	     no.setActionCommand("no");
	     no.addActionListener(listener);
	     bb.add(no);
	    }

	if (cancel_label != null)
	    {
	     Button cancel = new Button(cancel_label);
	     cancel.setActionCommand("cancel");
	     cancel.addActionListener(listener);
	     bb.add(cancel);
	    }

	this.pack();
  }

   protected ActionListener listeners = null;

    public void addActionListener(ActionListener l) {
	listeners = AWTEventMulticaster.add(listeners,l);
    }
   
    public void removeActionListener(ActionListener l) {
	listeners = AWTEventMulticaster.remove(listeners,l);
    }

}
