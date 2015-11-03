/*
* InputListener.java - DistributedBees demo program
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

import madkit.kernel.*;
import java.awt.event.*;
import java.awt.Color;
import madkit.kernel.StringMessage;

/** this class defines an InputListener that listen the inputs of a user : mouse events and keys events.
It send then to the Controler a PointMessage with the QueenBee new position.
=========================================================
        requestRole(BeeLauncher.BEE_COMMUNITY,  "simulation","inputListener");
        requestRole(BeeLauncher.BEE_COMMUNITY,  "simulation","java.awt.Color[r=...,g=...,b=...]InputListener");
=========================================================
* @see Controler
* @author Pierre BOMMEL, Fabien MICHEL
* @version 1.1 11/05/2000 
*/

public abstract class InputListener extends AbstractAgent implements MouseMotionListener, MouseListener, KeyListener, ReferenceableAgent
{
    ///////////  ATTRIBUTS  ///////////// 
    boolean mouseAction = false;

    /**
     * @label myControlBoard 
     */
    InputListenerGUI myControlBoard;
    protected Color myColor;    

    /**
     * @label positionMessage 
     */
    protected PointMessage positionMessage;

    /**
     * @label purposeMessage 
     */
    protected StringMessage purposeMsg;
    private int mouseMessageNumber = 0;
    private int destroyedMessagesRatio = 3;
    
    ///////////  Constructor  ///////////// 
    public InputListener()
    {
        positionMessage = new PointMessage();
        purposeMsg = new StringMessage("");
    }
    

    public void setMouseAction (boolean act) {mouseAction = act;}

    public boolean getMouseAction(){return mouseAction;}
        
    public void setDestroyedMessagesRatio(int nb) 
    {
        if (nb > 0) destroyedMessagesRatio = nb;
        else destroyedMessagesRatio = 1;
    }
    
    public int getDestroyedMessagesRatio() {return destroyedMessagesRatio;}
    
    /** ------------  evenements de MouseListener ------------ */
    public void mouseEntered(MouseEvent e)
    {
    }
    
    public void mouseExited(MouseEvent e)
    {
    }
        
    public void mouseReleased(MouseEvent e) 
    {
    }
    
    public void mousePressed(MouseEvent evt) 
            {  
                if (mouseAction) 
                    {
                        positionMessage.setPoint(evt.getPoint());
                        broadcastMessage(BeeLauncher.BEE_COMMUNITY, "simulation", myColor.toString()+"Controler", positionMessage);
                    }
            }
        
    public void mouseClicked(MouseEvent evt) 
            {
            }
         

    /** ------------  evenements de MouseMotionListener  ------------ */

    public void mouseMoved(MouseEvent evt) 
    {  
    }
    
    public void mouseDragged(MouseEvent evt) 
    { 
                if (mouseAction) 
                    {
                        if (mouseMessageNumber==0)
                        {
                                positionMessage.setPoint(evt.getPoint());
                                broadcastMessage(BeeLauncher.BEE_COMMUNITY, "simulation",myColor.toString()+"Controler", positionMessage);
                        }
                        mouseMessageNumber = (mouseMessageNumber + 1) % destroyedMessagesRatio;
                    }
    }

    /** ------------  evenements de KeyListener  ------------ */

    public void keyPressed(KeyEvent e) 
    {
        System.err.println("inputListener = ds keyPressed(KeyEvent e)");
    }
    
    public void keyReleased(KeyEvent e) 
    {
        System.err.println("inputListener = ds keyPressed(KeyEvent e)");
    }
      
    public void purposeMessage(String but)
    {
                purposeMsg.setString(but);
                broadcastMessage(BeeLauncher.BEE_COMMUNITY, "simulation", myColor.toString()+"Controler", purposeMsg);
                //sendMessage("master",myColor.toString()+"BeeEpiphyt", purposeMsg);
                //System.err.println("send purposeMsg :"+ but);
                if(but.equals("moveTo")) 
                    {
                        println("Click on the Interactive TV !");
                        mouseAction = true;
                    }
                else mouseAction = false;
    }
    
    public void keyTyped(KeyEvent e) 
    {
        System.err.println("inputListener = ds keyTyped(KeyEvent e)");
        char keyChar = e.getKeyChar();
        if (Character.isUpperCase(keyChar)) keyChar = Character.toLowerCase(keyChar);
        
        if (keyChar == 'c') 
            {
                purposeMessage("carre");
            }   
        if (keyChar == 'a') 
            {
                purposeMessage("buzz");
            }   
        if (keyChar == 'm') 
            {
                purposeMessage("moveTo");
            }
    }
    
    public void changeColoredRole(Color c)
    {
        if (myColor != null)
            leaveRole("simulation",myColor.toString()+"InputListener");
        myColor = c;
        requestRole(BeeLauncher.BEE_COMMUNITY,  "simulation",myColor.toString()+"InputListener",null);
    }
    
    public abstract void changeColoredView(Color c, boolean b);
    
    //////////////////////////////////////////////////////////////////////

    public String toString()
    {
        return "inputListener OKKKKKKKKKKKKKKK";
    }
    
    public void initGUI()
    {
        setGUIObject(myControlBoard = new InputListenerGUI (this));
    }

    public void activate()
    {
	createGroup(true, BeeLauncher.BEE_COMMUNITY, "simulation", null, null);
	requestRole(BeeLauncher.BEE_COMMUNITY,  "simulation", "inputListener",null);
       
        println("inputListener =  Activated"+"\n");
    } 
    public void end()
    {
        System.err.println("inputListener = DEAD");
    }
    
}




