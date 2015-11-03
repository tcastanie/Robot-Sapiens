/*
* JessAgent.java - Jess agents: linking MadKit with Jess to describe agents behaviors in Jess
* Copyright (C) 1998-2002  Jacques Ferber
*
* This library is free software; you can redistribute it and/or
* modify it under the terms of the GNU Lesser General Public
* License as published by the Free Software Foundation; either
* version 2.1 of the License, or (at your option) any later version.

* This library is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
* Lesser General Public License for more details.

* You should have received a copy of the GNU Lesser General Public
* License along with this library; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
*/
package madkit.jess;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.PrintWriter;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import jess.Rete;
import madkit.kernel.Agent;
import madkit.kernel.OPanel;
import madkit.system.EditorAgent;
import madkit.utils.graphics.DefaultControlPanel;

// This class is a copy of the same class in the JessEditorPanel file
// It should be solved by an appropriate interface...

class JessAgentSetWatchDialog extends JDialog implements ItemListener, ActionListener
{
	// order: facts, rules, activations, compilations, outMessages, inMessages, restartEngine, controlMessages
    /* **************** Programming notes ***
     * JF: well, I am not very proud of my programming style for all this. This is really
     * a mess... Exactly the style I recommend my students NOT to use... Well, well...
     */
	boolean[] watchItems;
	JessAgentGUI editor;

	JCheckBox factsButton;
	JCheckBox rulesButton;
	JCheckBox activButton;
	JCheckBox compilButton;
	JCheckBox outButton;
	JCheckBox inButton;
	JCheckBox restartButton;
	JCheckBox controlButton;

	public JessAgentSetWatchDialog(JessAgentGUI _editor, boolean[] _witems){

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

class JessAgentGUI extends DefaultControlPanel implements ActionListener{

    OPanel outPanel;

    boolean[] watchItems=new boolean[]{false,false,false,false,
                                        true,   // out messages
                                        true,   // in messages
                                        false, // restart engine
                                        false};  // control messages

    JessAgentGUI(JessAgent _ag){
        super(_ag);
        setLayout(new BorderLayout());
        JToolBar mytoolBar = new JToolBar();
        mytoolBar.addSeparator();
    //			/addTool(toolBar, "print", "demo/agents/system/print.gif");
        addButton(mytoolBar, "run", "Run","/images/toolbars/run.gif");
        addButton(mytoolBar, "halt", "Halt", "/images/toolbars/halt.gif");
        addButton(mytoolBar, "facts", "Facts","/images/toolbars/facts.gif");
        addButton(mytoolBar, "rules", "Rules","/images/toolbars/rules.gif");
        addButton(mytoolBar, "reset", "Reset","/images/toolbars/reset.gif");
        addButton(mytoolBar, "reinit", "Re-init","/images/toolbars/reinit.gif");
        addButton(mytoolBar, "watch", "Watch all","/images/toolbars/watch.gif");
        addButton(mytoolBar, "notePad", "Edit script with NotePadAgent","/images/toolbars/agentEditor24.gif");
        addButton(mytoolBar, "jEdit", "Edit script with jEdit","/images/toolbars/jedit24.gif");

        add(mytoolBar,BorderLayout.NORTH);

        outPanel = new OPanel();
        add(outPanel,BorderLayout.CENTER);
    }

    OPanel getOutPanel(){
        return outPanel;
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
            co.halt();
   		} else if(c.equals("facts"))
  	 		co.doSendControlMessage("facts");
   		else if(c.equals("rules"))
  	 		co.doSendControlMessage("rules");
   		else if(c.equals("reinit"))
  	 		co.doSendControlMessage("reinit");
   		else if(c.equals("jEdit"))
  	 		jedit();
        else if (c.equals("notePad")){
            String s = co.getBehaviorFile();
            if (s == null)
                co.println("sorry no behavior file to edit");
            else {
                co.println("editing : " + s);
                EditorAgent ed = new EditorAgent(s);
                ag.launchAgent(ed,"Edit : " + s,true);
            }
        }else if(c.equals("watch")){
            watchItems[4] = co.getWatchOutMessages();
            watchItems[5] = co.getWatchInMessages();
            watchItems[6] = co.getWatchRestartEngine();
            watchItems[7] = co.getWatchControlMessages();
   			new JessAgentSetWatchDialog(this, watchItems);
 		}
       else super.command(c);
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
        /* String s = inputArea.getText();
        JessController co = (JessController) ag.getController();
     	co.doSendControlMessage("eval",s); */
   }

   void evalSelection() {
     /*	String s = inputArea.getSelectedText();
        JessController co = (JessController) ag.getController();
     	co.doSendControlMessage("eval",s); */
   }

   void println(String s){
        ((PrintWriter)outPanel.getOut()).println(s);
   }


}

public class JessAgent extends Agent {

  PrintWriter out;
  PrintWriter err;

  Rete rete;

  public JessAgent(){
  	this(null);
  }

  public JessAgent(String f){
  	super();

        setController(new JessController(this,f));
        rete = ((JessController)getController()).getRete();
  }

  public void initGUI()
  {
    JessAgentGUI o = new JessAgentGUI(this);
    setGUIObject(o);

    out = new PrintWriter(o.getOutPanel().getOut());
    err = new PrintWriter(o.getOutPanel().getOut());

    rete.addOutputRouter("t", out);
    rete.addOutputRouter("WSTDOUT", out);
    rete.addOutputRouter("WSTDERR", err);
  }

  public void println(String s){
    if (out != null)
  	out.println(s);
     else
        super.println(s);
  }



}
