/*
* PropertyComponent.java - SEdit, a tool to design and animate graphs in MadKit
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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import SEdit.property.PropertyEvent;
import SEdit.property.PropertyEventListener;

public class PropertyComponent extends JPanel {

    Object element;
    Vector propertyEventListeners=new Vector();

    public Font defaultFont = new Font("Dialog", Font.PLAIN, 12);
    public Font boldFont = new Font("Dialog", Font.BOLD, 12);

    public synchronized void addPropertyEventListener(PropertyEventListener p){
        if (!propertyEventListeners.contains(p))
            propertyEventListeners.addElement(p);
    }

    public synchronized void removePropertyEventListener(PropertyEventListener p){
        if (propertyEventListeners.contains(p))
            propertyEventListeners.removeElement(p);
    }

    public PropertyComponent(Object elt, Vector forbidden){
	//super(frame, "Property Editor", false);
		element = elt;
		Hashtable namevalues=null;
		Hashtable nametypes=null;

		if (element != null){
			namevalues = ReflectorUtil.getProperties(elt);
			nametypes = ReflectorUtil.getPropertiesTypes(elt);

			setPreferredSize(new Dimension(400,100+(24*namevalues.size())));//length));
		} else
		    setPreferredSize(new Dimension(400,24*4));

     	setLayout(new BorderLayout());

        if (element != null){
     	    JPanel genPanel = new JPanel(new BorderLayout());

      /*  JPanel namePanel = new JPanel();
	    namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.Y_AXIS));
        JPanel valuePanel = new JPanel();
	    valuePanel.setLayout(new BoxLayout(valuePanel, BoxLayout.Y_AXIS));
        genPanel.add(namePanel,BorderLayout.WEST); */

            JPanel namePanel = new JPanel(new GridLayout(namevalues.size(),1));
            JPanel valuePanel = new JPanel(new GridLayout(namevalues.size(),1));
            genPanel.add(namePanel,BorderLayout.WEST);

            genPanel.add(valuePanel,BorderLayout.CENTER);

     	    JScrollPane scrollPanel = new JScrollPane(genPanel);

		    genPanel.setAlignmentX(LEFT_ALIGNMENT);
		    genPanel.setAlignmentY(TOP_ALIGNMENT);

		    Border emptyBorder = new EmptyBorder(5,5,5,5);
		    Border buttonBorder = new TitledBorder(null, "Properties",
							   TitledBorder.LEFT, TitledBorder.TOP);
	    // swing.boldFont);
		    Border compoundBorder = new CompoundBorder(buttonBorder, emptyBorder);
		    genPanel.setBorder(compoundBorder);

			for (Enumeration e = namevalues.keys(); e.hasMoreElements();){
				String key = (String)e.nextElement();
				if (!forbidden.contains(key)){
                    Component[] comps=showProperty(key,
								(String)namevalues.get(key),
								(Class)nametypes.get(key));
                    namePanel.add(comps[0]);
                    valuePanel.add(comps[1]);
                }
			}
		    //add(genPanel,BorderLayout.CENTER);
		    add(scrollPanel,BorderLayout.CENTER);
		}
    }


    Component[] showProperty(String label, String value, Class type){
            Dimension dim = new Dimension(24,24);
            Dimension dimLabel = new Dimension(120,20);
            Dimension dimValue = new Dimension(200,20);

            Component[] comps=new Component[2];
            //JPanel p = new JPanel();
            //p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));

            //JPanel fields = new JPanel(new GridLayout());

            JLabel lab = new JLabel(label+" : ");
            lab.setMinimumSize(dimLabel);
            lab.setPreferredSize(dimLabel);
            lab.setMaximumSize(dimLabel);
            //fields.add(lab);
            comps[0]=lab;
            lab.setFont(boldFont);
            Component o=null;

            if (type==new String().getClass()) {
                JTextField t = new JTextField();
                t.setText(value);
                o=t;
                t.addActionListener(new Actioner(this,o,label,value,type));
                //fields.add(t);
                comps[1]=t;
            }
            else if (type== Boolean.TYPE) {
                boolean bool;
                if (value.equals("true")) bool = true; else bool = false;
                JPanel radioButtons = new JPanel();
                radioButtons.setLayout(new BoxLayout(radioButtons, BoxLayout.X_AXIS));
                ButtonGroup group = new ButtonGroup();
                JRadioButton button;

                button = new JRadioButton("vrai",bool);
                button.setToolTipText("choix vrai");
                group.add(button);
                button.setAlignmentY(CENTER_ALIGNMENT);
                //swing.radioButtons.addElement(button);
                radioButtons.add(button);
                o=button;
                button.addActionListener(new Actioner(this,o,label,value,type));
                //radioButtons.add(Box.createRigidArea(swing.hpad10));

                button = new JRadioButton("faux",(!bool));
                button.setToolTipText("choix faux");
                group.add(button);
                button.setAlignmentY(CENTER_ALIGNMENT);
                //swing.radioButtons.addElement(button);
                radioButtons.add(button);

                /* radioButtons.setMinimumSize(dimValue);
                   radioButtons.setPreferredSize(dimValue);
                   radioButtons.setMaximumSize(dimValue); */
                //fields.add(radioButtons);
                comps[1]=radioButtons;
                o=button;
                button.addActionListener(new Actioner(this,o,label,value,type));
            }
            else if ((type == Integer.TYPE) || (type == Character.TYPE) || (type == Double.TYPE))
                {

                JTextField t = new JTextField();
                        //JLabel t = new JLabel();
                        //t.setBackground(Color.white);
                t.setText(value);
                /*	t.setMinimumSize(dimValue);
                    t.setPreferredSize(dimValue);
                    t.setMaximumSize(dimValue); */
                //fields.add(t);
                comps[1]=t;
                o=t;
                t.addActionListener(new Actioner(this,o,label,value,type));
                }
            else if ((type == Float.TYPE)  ||
                     (type == Long.TYPE) || (type == Short.TYPE) || (type == Byte.TYPE))
                     comps[1]=new JLabel(value);
            else
                System.err.println("ERR: not an editable field:"+label+", "+value+" : "+type);

            //p.add(fields);
            //return(p);
            return(comps);
		}

		void editField(Component t, String label, String value, Class type) {
		try {
			String newValue = "";

			if (t instanceof JTextField)
			newValue = ((JTextField)t).getText();
			if (t instanceof JRadioButton)
			{
				newValue="true";
				if (((JRadioButton)t).getText().equals("faux"))
				if (((JRadioButton)t).isSelected())
					newValue="false";
			}
			//System.err.println("editfield"+label+" "+newValue+" "+value+" "+type+t);

			ReflectorUtil.setProperty(element, label, newValue);
            notifyPropertyChange(new PropertyEvent(this, element, label, newValue,type));
			}
		catch(
            Exception e){System.err.println("ExceptionPropDial"+e);
		}


    }

    protected void notifyPropertyChange(PropertyEvent e){
        Vector v;
        synchronized(this){
            v = (Vector) propertyEventListeners.clone();
        }
        int cnt = v.size();
        for(int i=0;i<cnt;i++)
            ((PropertyEventListener) v.elementAt(i)).updateProperty(e);
    }


	public String askForNewValue(String name,String type) {
		String newname = JOptionPane.showInputDialog(null,  // this
                                      "Modification de : " + name,
                                      "Nouveau nom (" + type + ")",
                                      JOptionPane.PLAIN_MESSAGE);
		if (name.equals(newname))
			return(null);
		else
			return(newname);
	}

}

class Actioner implements ActionListener {
	PropertyComponent d;
	String l;
	String v;
	Class t;
	Component text;
	public void actionPerformed(ActionEvent e) {
	    d.editField(text,l,v,t);
	}

	Actioner(PropertyComponent dial, Component tx, String label, String value, Class type) {
		d = dial;
		text = tx;
		l = label;
		v = value;
		t = type;
	}
}
