/*
* JessEditorPanel.java - JessEditor agents, a simple editor to evaluate Jess rules
* Copyright (C) 2000-2002 Jacques Ferber
*
* This program is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License
* as published by the Free Software Foundation; either version 2
* of the License, or any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for moJessSetWatchDialogre details.
*
* You should have received a copy of the GNU General Public License
* along with this program; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
*/
package madkit.jess;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import jess.JessException;
import jess.Rete;
import madkit.utils.agents.AbstractEditorPanel;


class JessSetWatchDialog extends JDialog implements ItemListener, ActionListener
{
	// order: facts, rules, activations, compilations, outMessages, inMessages, restartEngine, controlMessages
    /* **************** Programming notes ***
     * JF: well, I am not very proud of my programming style for all this. This is really
     * a mess... Exactly the style I recommend my students NOT to use... Well, well...
     */
	boolean[] watchItems;
	JessEditorPanel editor;

	JCheckBox factsButton;
	JCheckBox rulesButton;
	JCheckBox activButton;
	JCheckBox compilButton;
	JCheckBox outButton;
	JCheckBox inButton;
	JCheckBox restartButton;
	JCheckBox controlButton;

	public JessSetWatchDialog(JessEditorPanel _editor, boolean[] _witems){

		super();
		setTitle("Watch settings");
	  	// setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		editor = _editor;
		watchItems = _witems;

	// Create the check boxes
        factsButton = new JCheckBox("facts : ");
        factsButton.setSelected(watchItems[0]);
        factsButton.setToolTipText("prints a message whenever a fact is asserted or retracted");
        factsButton.addItemListener(this);

        rulesButton = new JCheckBox("rules : ");
        rulesButton.setSelected(watchItems[1]);
        rulesButton.setToolTipText("prints a message when any rule fires");
        rulesButton.addItemListener(this);

        activButton = new JCheckBox("activations : ");
        activButton.setSelected(watchItems[2]);
        activButton.setToolTipText("prints a message when any rule is activated, or deactivated");
        activButton.addItemListener(this);

        compilButton = new JCheckBox("compilations : ");
        compilButton.setSelected(watchItems[3]);
        compilButton.setToolTipText("prints a message when any rule is compiled");
        compilButton.addItemListener(this);

        outButton = new JCheckBox("out messages : ");
        outButton.setSelected(watchItems[4]);
        outButton.setToolTipText("prints a message when a Madkit message is sent");
        outButton.addItemListener(this);

        inButton = new JCheckBox("in messages : ");
        inButton.setSelected(watchItems[5]);
        inButton.setToolTipText("prints a message when a Madkit message is received");
        inButton.addItemListener(this);

        restartButton = new JCheckBox("restart engine : ");
        restartButton.setSelected(watchItems[6]);
        restartButton.setToolTipText("prints a message when the Jess engine is restarted (usually upon reception of a Madkit message)");
        restartButton.addItemListener(this);

        controlButton = new JCheckBox("control messages : ");
        controlButton.setSelected(watchItems[6]);
        controlButton.setToolTipText("prints a message when a Madkit control message is received");
        controlButton.addItemListener(this);


        JPanel checkPanel = new JPanel(new GridLayout(0,1));

	  	getContentPane().setLayout(new BorderLayout());
        getContentPane().add(checkPanel,"Center");

        checkPanel.add(factsButton);
        checkPanel.add(rulesButton);
        checkPanel.add(activButton);
        checkPanel.add(compilButton);
        checkPanel.add(outButton);
        checkPanel.add(inButton);
        checkPanel.add(restartButton);
        checkPanel.add(controlButton);
		checkPanel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

		JPanel buttonPanel = new JPanel();
		getContentPane().add(buttonPanel,"South");

		JButton okButton = new JButton("OK");
		buttonPanel.add(okButton);
		okButton.addActionListener(this);

		JButton cancelButton = new JButton("Cancel");
		buttonPanel.add(okButton);
		cancelButton.addActionListener(this);


		pack();
		show();
    }


	/** Listens to the check boxes. */
    public void itemStateChanged(ItemEvent e) {
        int index = 0;
        Object source = e.getItemSelectable();

        if (source == factsButton) {
            index = 0;
        } else if (source == rulesButton) {
            index = 1;
        } else if (source == activButton) {
            index = 2;
        } else if (source == compilButton) {
            index = 3;
        } else if (source == outButton) {
            index = 4;
        } else if (source == inButton) {
            index = 5;
        } else if (source == restartButton) {
            index = 6;
        }  else if (source == controlButton) {
            index = 7;
        }

        if (e.getStateChange() == ItemEvent.SELECTED){
        	watchItems[index] = true;
        } else if (e.getStateChange() == ItemEvent.DESELECTED){
        	watchItems[index] = false;
        }

    }

    public void actionPerformed(ActionEvent e) {
			String s = e.getActionCommand();
            if (s.equals("OK")) {
            	editor.setWatchItems(watchItems);
            	dispose();
            } else
            	dispose();
	}
}


class JessEditorPanel extends AbstractEditorPanel {

 	boolean[] watchItems=new boolean[]{false,false,false,false,
                                        true,   // out messages
                                        true,   // in messages
                                        false, // restart engine
                                        false};  // control messages

    protected JPanel commandPanel;


    JessEditorPanel (EditJessAgent _ag, Rete r) {
      		super(_ag);

      		JMenuBar myMenubar = getMenubar();
	        // menu "Control"
	        JMenu menuControl=new JMenu("Actions");
	        myMenubar.add(menuControl);
	        addMenuItem(this, menuControl, "Run", "run", KeyEvent.VK_J, KeyEvent.VK_J);
	        addMenuItem(this, menuControl, "Halt", "halt", KeyEvent.VK_T, KeyEvent.VK_T);
	        addMenuItem(this, menuControl, "Facts", "facts", KeyEvent.VK_F, KeyEvent.VK_F);
	        addMenuItem(this, menuControl, "Rules", "rules", KeyEvent.VK_R, KeyEvent.VK_R);
	        addMenuItem(this, menuControl, "Reset", "reset", KeyEvent.VK_D, KeyEvent.VK_D);
	        addMenuItem(this, menuControl, "Reset", "reset", KeyEvent.VK_D, KeyEvent.VK_D);
	        addMenuItem(this, menuControl, "Re-init", "reinit", KeyEvent.VK_Y, KeyEvent.VK_Y);
	        addMenuItem(this, menuControl, "Eval buffer", "evalBuffer", KeyEvent.VK_B, KeyEvent.VK_B);


	        JToolBar mytoolBar = getToolbar();
	        mytoolBar.addSeparator();
		//			/addTool(toolBar, "print", "demo/agents/system/print.gif");
	        addTool(mytoolBar, "run", "Run","/images/toolbars/run.gif");
	        addTool(mytoolBar, "halt", "Halt", "/images/toolbars/halt.gif");
	        addTool(mytoolBar, "facts", "Facts","/images/toolbars/facts.gif");
	        addTool(mytoolBar, "rules", "Rules","/images/toolbars/rules.gif");
	        addTool(mytoolBar, "reset", "Reset","/images/toolbars/reset.gif");
	        addTool(mytoolBar, "reinit", "Re-init","/images/toolbars/reinit.gif");
	        addTool(mytoolBar, "watch", "Watch all","/images/toolbars/watch.gif");
	        // addTool(mytoolBar, "unwatch", "reset","/images/madkit/jess/unwatch.gif");
			mytoolBar.addSeparator();
	        addTool(mytoolBar, "evalBuffer", "eval buffer", "/images/toolbars/sendbuf.gif");
	        addTool(mytoolBar, "evalSelection", "eval selection", "/images/toolbars/sendsel.gif");


          // Configure the Rete object
    		r.addOutputRouter("t", stdout());
    		//r.addInputRouter("WSTDIN", in, true);
    		r.addOutputRouter("WSTDOUT", stdout());
    		r.addOutputRouter("WSTDERR", stdout());
            setExtens("clp");

    }


 	public void command(String c){
        JessController co = (JessController) ag.getController();
 		if (c.equals("evalBuffer")) evalBuffer();
 		else if (c.equals("evalSelection")) evalSelection();
   		else if(c.equals("run"))
  	 		co.doSendControlMessage("run");
   		else if(c.equals("reset"))
  	 		co.doSendControlMessage("reset");
   		else if(c.equals("halt")){
            try {
                println("halting");
                co.getRete().halt();
            } catch(JessException e){
                System.err.println("Error in halting Jess with agent: " + ag);
            }
   		} else if(c.equals("facts"))
  	 		co.doSendControlMessage("facts");
   		else if(c.equals("rules"))
  	 		co.doSendControlMessage("rules");
   		else if(c.equals("reinit"))
  	 		co.doSendControlMessage("reinit");
   		else if(c.equals("watch")){
            watchItems[4] = co.getWatchOutMessages();
            watchItems[5] = co.getWatchInMessages();
            watchItems[6] = co.getWatchRestartEngine();
            watchItems[7] = co.getWatchControlMessages();
   			new JessSetWatchDialog(this, watchItems);
 		} else super.command(c);
 	}

   void setWatchItems(boolean[] witems){
   		watchItems = witems;
        JessController co = (JessController) ag.getController();
  		co.doSendControlMessage("unwatch","all");
  		if (watchItems[0] && watchItems[1] & watchItems[2] && watchItems[3])
  			co.doSendControlMessage("watch", "all");
  		else {
  			if (watchItems[0]) co.doSendControlMessage("watch", "facts");
  			if (watchItems[1]) co.doSendControlMessage("watch", "rules");
  			if (watchItems[2]) co.doSendControlMessage("watch", "activations");
  			if (watchItems[3]) co.doSendControlMessage("watch", "compilations");
        }
        if (watchItems[4] != co.getWatchOutMessages ())
            co.doSendControlMessage("watchOutMessages");
        if (watchItems[5] != co.getWatchInMessages ())
            co.doSendControlMessage("watchInMessages");
        if (watchItems[6] != co.getWatchRestartEngine ())
            co.doSendControlMessage("watchRestartEngine");
        if (watchItems[7] != co.getWatchRestartEngine ())
            co.doSendControlMessage("watchControlMessages");
   }

     void evalBuffer() {
        String s = inputArea.getText();
        JessController co = (JessController) ag.getController();
     	co.doSendControlMessage("eval",s);
   }

   void evalSelection() {
     	String s = inputArea.getSelectedText();
        JessController co = (JessController) ag.getController();
     	co.doSendControlMessage("eval",s);
   }

   public void println(String s){
   		stdout().println(s);
   }


}
