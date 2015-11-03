/*
* LauncherGui.java -TurtleKit - A 'star logo' in MadKit
* Copyright (C) 2000-2002 Fabien Michel
*
* This program is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License
* as published by the Free Software Foundation; either version 2
* of the License, or any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
*/
package turtlekit.kernel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import madkit.kernel.OPanel;

/** this class defines the Graphics object where the display is finally made
  @author Fabien MICHEL
  @version 1.1 4/1/2000 
 */

class LauncherGui extends JPanel implements ActionListener
{
    JTextField p,dD,pause,displayDelay; //zone de saisie
    JButton b1 , b2, b3, b4,step,python; //les boutons
    Launcher ll;
    JPanel buttons,allbuttons,cycle;
    //JTextArea textDisplay;
    OPanel textDisplay;
    //Image buffer;
    //Graphics bufferGraphics;

    public LauncherGui (Launcher l) {
	ll = l;
	setSize(220,210);
	//setLocation(200,200);
    } 

    private void makebutton(JButton b,JPanel p,GridBagLayout gridbag,GridBagConstraints c) {
	gridbag.setConstraints(b, c);
	p.add(b);
    }
    void initialisation()
    {
	b1 = new JButton("START");
	if (ll.wrap) b2 = new JButton("WRAP ON");
	else b2 = new JButton("WRAP OFF");
	b3 = new JButton("ADD VIEWER");
	b4 = new JButton("RESET");
	step=new JButton("STEP BY STEP");	
	python=new JButton("PYTHON COMMAND CENTER");	
	b1.addActionListener(this);
	b4.addActionListener(this);
	b2.addActionListener(this);
	b3.addActionListener(this);
	step.addActionListener(this);
	python.addActionListener(this);
      
	allbuttons = new JPanel();
	GridBagLayout gridbag = new GridBagLayout();
	GridBagConstraints c = new GridBagConstraints();
	allbuttons.setLayout(gridbag);
	c.fill = GridBagConstraints.BOTH;
	c.weightx = 1.0;
	makebutton(b1,allbuttons,gridbag, c);
	c.gridwidth = GridBagConstraints.REMAINDER; //end row
	makebutton(b4,allbuttons, gridbag, c);
	c.gridwidth = GridBagConstraints.RELATIVE; //next-to-last in row
	c.fill = GridBagConstraints.BOTH;
	c.weightx = 1.0;
	makebutton(b2,allbuttons, gridbag, c);
	c.gridwidth = GridBagConstraints.REMAINDER; //end row
	makebutton(b3,allbuttons, gridbag, c);
	c.weightx = 0.0;                  //reset to the default
	makebutton(step,allbuttons, gridbag, c); //another row
	makebutton(python,allbuttons, gridbag, c); //another row

        //Create the slider and its label
        JLabel sliderLabel = new JLabel("Simulation speed", JLabel.CENTER);
        sliderLabel.setAlignmentX(Component.CENTER_ALIGNMENT);


	
	JSlider simulationSpeed = new JSlider(JSlider.HORIZONTAL,
					      0, 500 , 490);
	simulationSpeed.addChangeListener(new SliderListener());
	simulationSpeed.setMajorTickSpacing(250);
	//  simulationSpeed.setMinorTickSpacing(10);
	simulationSpeed.setPaintTicks(true);
	simulationSpeed.setPaintLabels(false);
	simulationSpeed.setBorder(BorderFactory.createEmptyBorder(0,0,15,0));
	
	JPanel contentPane = new JPanel();
	
	
	//Create the label table
	/*Hashtable labelTable = new Hashtable();
	  labelTable.put( new Integer( 1000 ), new JLabel("Fast") );
	  labelTable.put( new Integer( 0 ), new JLabel("Slow") );
	  simulationSpeed.setTextTable( labelTable );
	  contentPane.add("North",sliderLabel);
	  contentPane.add("South",simulationSpeed);*/
	
	contentPane.add(sliderLabel);
	contentPane.add(simulationSpeed);
	setLayout(new BorderLayout());
	/* buttons = new JPanel();
	   buttons.setVisible(true);
	   buttons.setLayout(new GridLayout(2,2));*/
	
	textDisplay = new OPanel();//JTextArea();
	textDisplay.jscrollpane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); 
	textDisplay.jscrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED); 
	ll.setOutputWriter(textDisplay.getOut());
	//textDisplay.setBackground(Color.white);
	//textDisplay.setEditable(false);
	add("North",allbuttons);
	add("South",contentPane);
	add("Center",textDisplay);
	doLayout();
	allbuttons.doLayout();
	textDisplay.doLayout();
	contentPane.doLayout();
	//buttons.doLayout();
    }
    
    public Dimension getPreferredSize(){return getSize();}
    
    public void actionPerformed(ActionEvent e)
    {
	Object s=e.getSource();
	if(s==b1)
	    {
		if (b1.getText().equals("START"))
		    {
			b1.setBackground(Color.green);
			b1.setText("STOP");
			ll.start = true;
			return;
		    }
		if (ll.run && ll.start)
		    {
			b1.setBackground(Color.red);
			b1.setText("RUN");
			ll.setStop();
			return;
		    }
		else if (ll.start)
		    {
			b1.setBackground(Color.green);
			b1.setText("STOP");
			ll.setStop();
		    }
	    }
	else if (s==b3 && ll.start) ll.addViewer();
	else if (s==b4 && ll.start)
	    {
		textDisplay.clearOutput();
		ll.setReset();
		ll.run=true;
		b1.setBackground(Color.green);
		b1.setText("STOP");
	    }
	else if (s==b2 )
	    {
		if (b2.getText().equals("WRAP ON"))
		    {
			ll.setWrapModeOn(false);
			b2.setText("WRAP OFF");
		    }
		else 
		    {
			ll.setWrapModeOn(true);
			b2.setText("WRAP ON");
		    }
	    }
	//if (s==p) ll.setCyclePause(Integer.parseInt(p.getText()));
	//if (s==dD) ll.setCycleDisplayEvery(Integer.parseInt(dD.getText()));
	else if (s==step)
	    {
		if (ll.start && ll.run)
		    {
			b1.setBackground(Color.red);
			b1.setText("RUN");
			ll.setStop();
			ll.stepByStep();
			return;
		    }
		if (ll.start)
		    {
			ll.stepByStep();
			return;
		    }
	    }			
	
	else if (s==python)
	    {
		try
		{
			ll.println("launching python. Please wait...");
			ll.launchPython();
			if (ll.run)
			{
				b1.setBackground(Color.red);
				b1.setText("RUN");
				ll.setStop();
				ll.stepByStep();
				return;
			}
		}
		catch(NoClassDefFoundError ex)
		{
			ll.println("can't launch python in applet mode");
		}
		catch(Exception ex)
		{
			ll.println("can't launch python in applet mode");
		}
	    }			
	
    }
    
    class SliderListener implements ChangeListener {
	public void stateChanged(ChangeEvent e) {
	    JSlider source = (JSlider)e.getSource();
	    if (!source.getValueIsAdjusting()) {
		ll.setCyclePause( 500 - (int)source.getValue());
	    }
	}
    }
    
}



