/*
 * ProcessMonitor.java - Created on Apr 7, 2004
 * 
 * Copyright (C) 2003-2004 Sebastian Rodriguez
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.

 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * Last Update: $Date: 2004/04/14 11:55:47 $
 */

package madkit.pluginmanager;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.Timer;


/**Monitors a Process. The process must implement the <code>ObservableProcess</code> Interface 
 * @author Sebastian Rodriguez - sebastian.rodriguez@utbm.fr
 *
 * @version $Revision: 1.1 $
 */
class ProcessMonitor extends JDialog {

	private JProgressBar _currentProcess;
	private JProgressBar _globalProcess;
	private JLabel _currNote;
	private Timer _timer;
	private ObservableProcess _process;
	
	public ProcessMonitor(Component parent,ObservableProcess process){
		super(JOptionPane.getFrameForComponent(parent),false);
		setLocationRelativeTo(parent);
		_process=process;
		
		
		getContentPane().setLayout(new BorderLayout());
		
		JLabel lglob=new JLabel(_process.getGlobalProcessNote());
		_currNote=new JLabel();
		
		_currentProcess=new JProgressBar(0,_process.getCurrentProcessMax());
		_globalProcess=new JProgressBar(0,_process.getGlobalProcessMax());
		
		JPanel p=new JPanel();
		p.setLayout(new BoxLayout(p,BoxLayout.Y_AXIS));
		
		p.add(lglob);
		p.add(_globalProcess);
		p.add(_currNote);
		p.add(_currentProcess);
		
		getContentPane().add(p,BorderLayout.CENTER);
		
		//icon
		JLabel licon=new JLabel();
		URL url=getClass().getResource("/images/update.gif");
		if(url!=null){
			licon.setIcon(new ImageIcon(url));
		}
		getContentPane().add(licon,BorderLayout.WEST);
		pack();
	}
	
	
	/**Shows a Progress Window. 
     */
    public synchronized void showProgressDialog(){
        setSize(300,100);
        _timer=new Timer(100,new TimerListener());
        _timer.start();
        setVisible(true);
    }

    
    class TimerListener implements ActionListener{

		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			if(_process.isGlobalProcessFinished()){
				dispose();
				_timer.stop();
				_timer=null;
			}else{
				_currNote.setText(_process.getCurrentProcessNote());
				_currentProcess.setValue(_process.getCurrentProcessStatus());
				_globalProcess.setValue(_process.getGlobalProcessStatus());
			}
			
		}

    }
}


