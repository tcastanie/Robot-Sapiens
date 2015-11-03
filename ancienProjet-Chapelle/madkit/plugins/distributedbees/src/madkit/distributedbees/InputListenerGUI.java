/*
* InputListenerGUI.java - DistributedBees demo program
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
import java.util.Enumeration;

/** this class defines the Graphic User InterFace: a control board allowing the players to manipulate 
the simulation and then to choose :
   the view of the simulation (Red bees, Blue bees or/and Green bees),
   the color of the queenBee they want to control and
   the kind of control upon the choosen queenBee (Alea, square, moveTo).

  @author Pierre BOMMEL, Fabien MICHEL
  @version 1.0 4/7/2000 
 */

public class InputListenerGUI extends JPanel implements ActionListener
{
    private GridBagLayout gridbag;
    private GridBagConstraints constraints;
    private JButton redViewButton , blueViewButton, greenViewButton, redPlayerButton, bluePlayerButton, greenPlayerButton; 
    private ButtonGroup group;
    private boolean blueView=false, redView=false, greenView=false;
    private JRadioButton aleaRadioButton, carreRadioButton, sourisRadioButton;
    private JPanel viewButtonsPanel, playButtonsPanel;
    private OPanel textDisplay;

    /**
     * @label myListener 
     */
    private InputListener myListener;
    ////////////////////////////////////////////////////////////////////

    public InputListenerGUI (InputListener l) 
    {
        myListener = l;
        setSize(380,160);
        initialisation();
    } 
    ////////////////////////////////////////////////////////////////////

    public void makebutton(JButton b,JPanel p) {
        gridbag.setConstraints(b, constraints);
        p.add(b);
    }


    public JRadioButton addRadioButton(JPanel buttonPanel, ButtonGroup g, String buttonName, boolean v)
    {  
        JRadioButton button = new JRadioButton(buttonName, v);
        button.addActionListener(this);
        gridbag.setConstraints(button, constraints);
        g.add(button);
        buttonPanel.add(button);
        return button;
    }

    public void initialisation()
    {
        redViewButton = new JButton("RedViewer");
        blueViewButton = new JButton("BlueViewer");
        greenViewButton = new JButton("greenViewer");
        redPlayerButton = new JButton("redPlayer ");
        bluePlayerButton=new JButton("bluePlayer");     
        greenPlayerButton=new JButton("greenPlayer");   
        redViewButton.addActionListener(this);
        redPlayerButton.addActionListener(this);
        blueViewButton.addActionListener(this);
        greenViewButton.addActionListener(this);
        bluePlayerButton.addActionListener(this);
        greenPlayerButton.addActionListener(this);
        // GridBagLayout !!! le plus difficile   (p.492) 
// 1- Definition d'un GridBagLayout, sans indication nb de lignes ni colonnes. Cet objet est le gestionnaire de mise en forme pour les composants ex: buton.setLayout(gridbag)
        gridbag = new GridBagLayout();
// 2- Definition d'un GridBagConstraints. Il specifiera de quelle façon les composants sont disposés à l'interieur du GridBag
        constraints = new GridBagConstraints();

        //////   Panel des boutons de VUE   ///////
        viewButtonsPanel = new JPanel(gridbag);
        //viewButtonsPanel.setLayout(new GridLayout(1,3));
        Border bordGrave = BorderFactory.createEtchedBorder();
        Border bordVue = BorderFactory.createTitledBorder(bordGrave, "VIEW");
        viewButtonsPanel.setBorder(bordVue);

//fill = Pour que le composant ne remplisse pas toute la zone. 4 options: NONE, HORIZONTAL, VERTICAL et BOTH (Resize the component both horizontally and vertically).
        constraints.fill = GridBagConstraints.BOTH;

//anchor = si le composant ne remplit pas toute la zone, on specifie sa situation ds la zone avec ce champs.= CENTER (defaut), NORTH, NORTHEAST,...
        constraints.anchor = GridBagConstraints.CENTER;

        //Champs de poids: =0 -> la zone ne bouge pas ds la direction x.
        //                 = 100 -> 
        constraints.weightx = 100;

//gridx,gridy,gridwidth,gridheight: ces contraintes définissent l'emplacement du composant ds la grille.
//      gridx,gridy= position de colonne et ligne du coin sup gauche
//          des valeurs ou RELATIVE = on ajoute les elements successivement de gauche a droite
//                         REMAINDER= le composant est le dernier de la ligne
//      gridwidth,gridheight= nbre de colonnes et lignes que le composant occuppe
        constraints.gridwidth = 3;
        
        //constraints.gridwidth = GridBagConstraints.RELATIVE; 
        // On fabrique un button qu'on met ds un panel selon la "constraints"
        makebutton(redViewButton,viewButtonsPanel);
        makebutton(blueViewButton,viewButtonsPanel);
        constraints.gridwidth = GridBagConstraints.REMAINDER; //end row
        makebutton(greenViewButton,viewButtonsPanel);

        constraints.weightx = 0.0;                  //reset to the default

        /////    Panel des boutons de PLAYER   /////////////
        playButtonsPanel = new JPanel(gridbag);
        Border bordGrave2 = BorderFactory.createEtchedBorder();
        Border bordPlayer = BorderFactory.createTitledBorder(bordGrave2, "PLAYER");
        playButtonsPanel.setBorder(bordPlayer);
        playButtonsPanel.setLayout(gridbag);

        constraints.gridwidth = 3;
        //constraints.gridwidth = GridBagConstraints.RELATIVE; 
        makebutton(redPlayerButton,playButtonsPanel);
        makebutton(bluePlayerButton,playButtonsPanel); 
        constraints.gridwidth = GridBagConstraints.REMAINDER; //end row
        makebutton(greenPlayerButton,playButtonsPanel); 
        
        group = new ButtonGroup();
        constraints.gridwidth = 3;
        constraints.fill = GridBagConstraints.NONE;
        //constraints.gridwidth = GridBagConstraints.RELATIVE;
        aleaRadioButton = addRadioButton(playButtonsPanel, group, "Random", false);
        aleaRadioButton.setEnabled(false);    
        carreRadioButton = addRadioButton(playButtonsPanel, group, "Square", false);  
        carreRadioButton.setEnabled(false);    
        constraints.gridwidth = GridBagConstraints.REMAINDER;  
        sourisRadioButton = addRadioButton(playButtonsPanel, group, "Mouse", false);
        sourisRadioButton.setEnabled(false);    




        textDisplay = new OPanel();//JTextArea();
        textDisplay.jscrollpane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); 
        textDisplay.jscrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED); 
        myListener.setOutputWriter(textDisplay.getOut());

        //////////////////////////////////////////////////
        constraints.fill = GridBagConstraints.NONE;
        constraints.gridwidth = 3; //end row
        constraints.weightx = 0.0;                  
        gridbag.setConstraints(viewButtonsPanel, constraints);
        add(viewButtonsPanel, constraints);
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        gridbag.setConstraints(playButtonsPanel, constraints);
        add(playButtonsPanel, constraints);
        //gridbag.setConstraints(textDisplay, constraints);
        //add("South",textDisplay);
        doLayout();
        viewButtonsPanel.doLayout();
        playButtonsPanel.doLayout();
        //textDisplay.doLayout();
    }
    
    public Dimension getPreferredSize(){return getSize();}
    
    public void actionPerformed(ActionEvent e)
    {
        Object s=e.getSource();
        if(s==redViewButton)
            {
                if(redView)
                    {
                        redViewButton.setBackground(viewButtonsPanel.getBackground());
                        redView=false;
                    }
                else
                    {
                        redViewButton.setBackground(Color.red);
                        redView=true;
                    }
                //action 
                myListener.changeColoredView(Color.red, redView);
                return;
            }
        if (s==greenViewButton)
            {
                if(greenView)
                    {
                        greenViewButton.setBackground(viewButtonsPanel.getBackground());
                        greenView=false;
                    }
                else
                    {
                        greenViewButton.setBackground(Color.green);
                        greenView=true;
                    }
                //action 
                myListener.changeColoredView(Color.green, greenView);
                return;
            }

        if (s== blueViewButton)
            {
                if(blueView)
                    {
                        blueViewButton.setBackground(viewButtonsPanel.getBackground());
                        blueView=false;
                    }
                else
                    {
                        blueViewButton.setBackground(Color.cyan);
                        blueView=true;
                    }
                //action 
                myListener.changeColoredView(Color.blue, blueView);
                return;
            }

        if (s== bluePlayerButton)
            {
                if(bluePlayerButton.getBackground() != Color.cyan)
                    allJRadioButtonsAtFalse();
                bluePlayerButton.setBackground(Color.cyan);
                redPlayerButton.setBackground(playButtonsPanel.getBackground());
                greenPlayerButton.setBackground(playButtonsPanel.getBackground());
                //action
                myListener.changeColoredRole(Color.blue);
            }

        if (s== redPlayerButton)
            {
                if(redPlayerButton.getBackground() != Color.red)
                    allJRadioButtonsAtFalse();
                redPlayerButton.setBackground(Color.red);
                bluePlayerButton.setBackground(playButtonsPanel.getBackground());
                greenPlayerButton.setBackground(playButtonsPanel.getBackground());
                //action
                myListener.changeColoredRole(Color.red);
            }   

        if (s== greenPlayerButton)
            {
                if(greenPlayerButton.getBackground() != Color.green)
                    allJRadioButtonsAtFalse();
                greenPlayerButton.setBackground(Color.green);
                redPlayerButton.setBackground(playButtonsPanel.getBackground());
                bluePlayerButton.setBackground(playButtonsPanel.getBackground());
                //action
                myListener.changeColoredRole(Color.green);
            }   

        if (s== aleaRadioButton) 
            {
                //action
                myListener.purposeMessage("buzz");
            }    
        if (s== carreRadioButton)
            {
                //action
                myListener.purposeMessage("carre");
            }    
        if (s== sourisRadioButton)
            {
                //action
                myListener.purposeMessage("moveTo");
            }    
    }

    /**Sets the state of the radioButton at false. Note that this method does not trigger an actionEvent.*/
    public void allJRadioButtonsAtFalse()
    {
        for(Enumeration e = group.getElements();e.hasMoreElements();)//Return all the buttons that are participating in the group.
            {
                JRadioButton b = (JRadioButton) e.nextElement();
                b.setEnabled(true);
                b.setSelected(false);
            }
    }    
}



