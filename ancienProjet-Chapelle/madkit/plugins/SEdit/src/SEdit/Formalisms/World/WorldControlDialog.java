/*
* WorldControlDialog.java - A simple reactive agent library
* Copyright (C) 1998-2002 Jacques Ferber
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
package SEdit.Formalisms.World;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import javax.swing.text.*;


class WorldControlDialog extends JDialog  {

	JLabel tempoLabel;
	JSlider vjs;
	WorldStructure ms;

	WorldControlDialog(WorldStructure m) {
	
		ms = m;
		// top controls
		getContentPane().setLayout(new GridLayout(2,1));
		
		JPanel topControls = new JPanel(new FlowLayout());
		getContentPane().add(topControls);    	

		// bouton de start
		JButton runControl = new JButton(new ImageIcon(m.getFormalism().getBase()+"images/run.gif"));
		// runControl.setPressedIcon(resPressed);
		runControl.setBorderPainted(true);
		runControl.setFocusPainted(false);
		// runControl.setContentAreaFilled(false);
		runControl.setAlignmentY(CENTER_ALIGNMENT);
		topControls.add(runControl);
		runControl.addActionListener(new ActionListener(){
			public void  actionPerformed(ActionEvent e) {
				ms.start();
		}});
		
			// bouton de start
		JButton stopControl = new JButton(new ImageIcon(m.getFormalism().getBase()+"images/stop.gif"));
		// stopControl.setPressedIcon(resPressed);
		stopControl.setBorderPainted(true);
		stopControl.setFocusPainted(false);
		// stopControl.setContentAreaFilled(false);
		stopControl.setAlignmentY(CENTER_ALIGNMENT);
		topControls.add(stopControl);
		stopControl.addActionListener(new ActionListener(){
			public void  actionPerformed(ActionEvent e) {
				ms.stop();
		}});
		
		// bottom controls
		JPanel bottomControls = new JPanel(new FlowLayout());
		getContentPane().add(bottomControls);
		
		/** slider de delay */
    	vjs = new JSlider(JSlider.HORIZONTAL, 1, 200, ms.getDelay());
    	vjs.setPreferredSize(new Dimension(120,20));
		vjs.setAlignmentY(CENTER_ALIGNMENT);
    	bottomControls.add(vjs);
    	
    	vjs.addChangeListener(new ChangeListener() {
    		public void stateChanged(ChangeEvent e){
				int v = vjs.getValue();
				tempoLabel.setText(String.valueOf(v));
				ms.setDelay(v);
		}});
		
		// label de delay
		tempoLabel = new JLabel(String.valueOf(ms.getDelay()),SwingConstants.CENTER);
		tempoLabel.setAlignmentY(CENTER_ALIGNMENT);
		// labelControl.setBorderPainted(true);
		tempoLabel.setFont(new Font("Dialog", Font.BOLD, 12));
    	bottomControls.add(tempoLabel);

		
		
		pack();
		show();

	}
}
		
		
