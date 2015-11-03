/*
* BeeLauncherGUI.java - DistributedBees demo program
* Copyright (C) 1998-2004 P. Bommel, F. Michel
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

package madkit.distributedbees;

import madkit.kernel.OPanel;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;

/** this class defines the Graphic User InterFace of the BeeLauncher: a control board allowing the players to manipulate the BeeLauncher and then to choose :
   to launch the simulation,
   to launch a various number of bees by swarm (Red bees, Blue bees or/and Green bees),
   to kill the colored swarms and
   to kill the local simulation

  @author Pierre BOMMEL, Fabien MICHEL
  @version 1.0 01/09/2000 
 */

public class BeeLauncherGUI extends JPanel implements ActionListener
{    
    /**
     * @label myListener 
     */
    private BeeLauncher myListener;
    JLabel label;
    private OPanel textDisplay;
    Panneau redJPanel;
    Panneau greenJPanel;
    Panneau blueJPanel;
    JButton redLaunchButton;
    JButton blueLaunchButton;
    JButton greenLaunchButton;
    JButton redKillButton;
    JButton blueKillButton;
    JButton greenKillButton;
    
    ////////////////////////////////////////////////////////////////////
    
    public BeeLauncherGUI (BeeLauncher l) 
    {
        myListener = l;
        setSize(380,280);
        initialisation();
    } 
    ////////////////////////////////////////////////////////////////////
       
    
    public void initialisation()
    {
        setLayout(new GridLayout(2,1));
        ////// PANEL1 ///////
        JPanel panel1 = new JPanel();
        label = new JLabel("BeeLauncher : "+myListener.group+"  ",  SwingConstants.CENTER );
        label.setFont(new Font("Dialog",Font.BOLD, 14));
        label.setMaximumSize(new Dimension(350, 70) );

        textDisplay = new OPanel();
        textDisplay.jscrollpane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); 
        textDisplay.jscrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED); 
        myListener.setOutputWriter(textDisplay.getOut());

        panel1.add(label);
        panel1.add(textDisplay, -1);
        add(panel1,"North");
        
        ////// PANEL2 ///////
        redJPanel = new Panneau(Color.red, this);
        greenJPanel = new Panneau(Color.green, this);
        blueJPanel = new Panneau(Color.blue, this);
        JPanel panel2 = new JPanel();
        panel2.add("Center",redJPanel); 
        panel2.add("Center",blueJPanel);        
        panel2.add("Center",greenJPanel);       
        add(panel2,"South");
        
        redLaunchButton = redJPanel.launchButton;
        blueLaunchButton = blueJPanel.launchButton;
        greenLaunchButton = greenJPanel.launchButton;
        redKillButton = redJPanel.killButton;
        blueKillButton = blueJPanel.killButton;
        greenKillButton = greenJPanel.killButton;
    }
    
    public Dimension getPreferredSize(){return getSize();}
    
    public void actionPerformed(ActionEvent e)
     {
        Object s=e.getSource();
        if(s==redLaunchButton) myListener.setLaunchTheRed(true);
        if(s==redKillButton) myListener.killTheReds = true;
        if(s==blueLaunchButton) myListener.setLaunchTheBlue(true);
        if(s==blueKillButton) myListener.killTheBlues = true;
        if(s==greenLaunchButton) myListener.setLaunchTheGreen(true);
        if(s==greenKillButton) myListener.killTheGreens = true;
    }
    public void updateName() 
    {
        label.setText("Launcher : "+myListener.group.toUpperCase()+"  ");
    }
    
     
    ///////////////////////////////////////////////////////////////////
    class Panneau extends JPanel implements DocumentListener
    {
        JButton launchButton;
        JButton killButton;
        IntTextField nbBeesField;
        String couleur;
        BeeLauncher myLauncher;

        public Panneau(Color color, ActionListener listener)
        {
            setLayout(new GridLayout(3,1));
            setBackground(color);
            
            nbBeesField = new IntTextField(0, 4);
            
            launchButton = new JButton ("Launch");
            killButton = new JButton ("Kill");
            add(nbBeesField);
            add(launchButton);
            add(killButton);

            launchButton.addActionListener(listener);
            killButton.addActionListener(listener);
            (nbBeesField.getDocument()).addDocumentListener(this);
            Border bordGrave = BorderFactory.createEtchedBorder();
            if (color == Color.green) couleur = "green";            
            if (color == Color.red) couleur = "red";
            if (color == Color.blue) couleur = "blue";
            Border bordVue = BorderFactory.createTitledBorder(bordGrave, couleur+" swarm");
            this.setBorder(bordVue);
            myLauncher = ((BeeLauncherGUI)listener).myListener;
        }

    public void insertUpdate(DocumentEvent e)
    {
        Document s=e.getDocument();
        
        try{
            if (couleur.equals("red")) 
                {
                    myLauncher.setNumberOfRedBees(nbBeesField.getValue());
                }
            if (couleur.equals("green")) 
                {
                    myLauncher.setNumberOfGreenBees(nbBeesField.getValue());
                }
            if (couleur.equals("blue")) 
                {
                    myLauncher.setNumberOfBlueBees(nbBeesField.getValue());
                }
            
        }
        catch (IllegalArgumentException ex) {
            // Quietly ignore.
        }
    }
    

    public void removeUpdate(DocumentEvent e)
    {
    }
    

    public void changedUpdate(DocumentEvent e)
    {
    }
    }
    ////////////////////////////////////////////////////////////

    class IntTextField extends JTextField 
    {  
        public IntTextField(int defval, int size)
        {  
            super("" + defval, size);
            setHorizontalAlignment(JTextField.RIGHT);
        }
        
        public boolean isValid()
        {  
            try
            {  
                Integer.parseInt(getText());
                return true;
            }
            catch(NumberFormatException e)
                {  return false; 
                }
        }
        
        public int getValue()
        {  try
            {  return Integer.parseInt(getText());
            }
        catch(NumberFormatException e)
            {  return 0; 
            }
        }
    }

}//fin classe


