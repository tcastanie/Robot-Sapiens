/*
* SEditTools.java - SEdit, a tool to design and animate graphs in MadKit
* Copyright (C) 1998-2002 Jacques Ferber, Olivier Gutknecht
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
package SEdit;

//import kawa.*;
//import kawa.lang.*;
//import kawa.standard.*;

import java.awt.AWTError;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import madkit.kernel.*;


interface MiniTextListener {
	public void sendText(String text);
}

class MiniTextDialog extends JDialog implements ActionListener
{
	JTextArea textArea;
	MiniTextListener listener;
	
	public MiniTextDialog(Component comp, String title, String text, MiniTextListener lis){
		
		super(SEditTools.getFrameParent(comp), title, true);
	  	// setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	
		System.out.println(":: edition de " + text);
		listener = lis;
		
	  	getContentPane().setLayout(new BorderLayout(10,10)); 
	  	JPanel buttonPanel = new JPanel();
	  	
	    JButton okButton = new JButton("OK");
	    JButton applyButton = new JButton("Apply");
	    JButton cancelButton = new JButton("Cancel");
	   
	   	buttonPanel.add(okButton);
	   	okButton.addActionListener(this);
	   	buttonPanel.add(applyButton);
	   	applyButton.addActionListener(this);
	   	buttonPanel.add(cancelButton);
	   	cancelButton.addActionListener(this);
	   	
		getContentPane().add(buttonPanel,"South");
		
		textArea = new JTextArea(8,40);
		textArea.setText(text);
		JScrollPane outscroller = new JScrollPane();
		// outscroller.setSize(300,200);  
		outscroller.getViewport().add(textArea);
		getContentPane().add(outscroller,"Center");
		textArea.requestFocus();
		try {
			Toolkit toolkit = Toolkit.getDefaultToolkit();
			Dimension d=toolkit.getScreenSize();
			setLocation(d.width/2-100,d.height/2-100);
	    }catch(AWTError e){System.out.println("setlocation error");};
	    
	   pack();
	   show();
	}
	
	public void actionPerformed(ActionEvent e) {
				String s = e.getActionCommand();
                if (s.equals("OK")) {
                	listener.sendText(getText());
                	dispose();
                } if (s.equals("Apply"))
                	listener.sendText(getText());
                else 
                	dispose();
    }
	
	
	public void setText(String text){
		textArea.setText(text);
	}
	
	public String getText(){
		return textArea.getText();
	}
}

/**
	A class which contains some tools used by different parts of SEdit
	
*/


public class SEditTools
{
	static String result;

	public static Frame getFrameParent(Component _c){
		Component c = _c;
		while (!(c instanceof Frame))
			if (c == null)
				return(null);
			else
				c = c.getParent();
		return((Frame) c);
	}
	
	public static String editText(Component comp, String title, String textInit){
      	result = null;
      	
      	MiniTextListener miniListen = new MiniTextListener(){
      		public void sendText(String s){
      			result = s;
		    }};
		      	
   		// String s = editor.askForString("Modification de l'action :" + pred,"Entrez la nouvelle action:");
   		MiniTextDialog mini = new MiniTextDialog(comp,
   												title,
   												textInit,
   												miniListen);
   		return(result);
	}
	
	static public void createStructure(AbstractAgent ag, Message m){
			  SEditMessage fm = (SEditMessage) m;
			  System.out.println("receiving SEdit Message:"+fm.getRequest());
			  StructureAgent sa;
			  String fileName;
			  if (fm.getRequest() == "reply"){
				  Formalism f = (Formalism) fm.getParameter();
				  fileName = fm.getFileName();
				  if (f != null){
					  if (fileName != null){
						  sa = new StructureAgent(f,fileName);
						  ag.launchAgent(sa,f.getName()+" - "+fileName,true);
						  fileName = null;
					  } else {
						  sa = new StructureAgent(f);
						  ag.launchAgent(sa,f.getName()+" - Untitled",true);
					}
				  }
				 else
					   System.err.println("ERROR: no formalism found");
			  }
		}
	
}
