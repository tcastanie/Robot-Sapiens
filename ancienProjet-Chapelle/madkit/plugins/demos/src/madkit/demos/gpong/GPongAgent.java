/*
* GPongAgent.java - a Graphical PingPong agent in Java
* Copyright (C) 1998-2002 Jacques Ferber
* taken from the original PingPong from Olivier Gutknecht
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
package madkit.demos.gpong;

import madkit.kernel.Agent;
import madkit.kernel.AgentAddress;
import madkit.kernel.Message;
import madkit.kernel.StringMessage;
import madkit.messages.ActMessage;



/**
 * <p>Title: TicTacToe</p>
 * <p>Description: A simple distributed Ping Pong in MadKit</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: MadKit Team</p>
 * @author J. Ferber
 * @version 1.0
 */

public class GPongAgent extends Agent {

    AgentAddress other = null;
	boolean creator=false;
    String groupName = "graphic-ping-pong";

    GPongGUI gui;

    public GPongAgent() {
    }

    public void initGUI() {
       gui = new GPongGUI(this);
       this.setGUIObject(gui);
   }

   public void activate(){
       if (isGroup(groupName)){
           creator=false;
           gui.setLeftPlayer();
       } else {
           createGroup(true,groupName,null,null);
           creator=true;
           gui.setRightPlayer();
       }
       requestRole(groupName,"player",null);
   }

   public void live()
   {
       do
       {
           exitImmediatlyOnKill(); //to be sure the agent thread can be really stopped
           pause(100);
           AgentAddress[] v = getAgentsWithRole(groupName,"player");
           for (int i=0; i < v.length; i++)
           {
               AgentAddress agent = v[i];
               if (! agent.equals(getAddress()))
                   other = agent;
           }
       }
       while (other == null);
       println("Other is :"+other);

       // If I'm not the founder agent, I ask the other one to start..
       if (! creator){
           sendMessage(other, new StringMessage("start"));
           gui.start();
       }

       while(true){
           Message m = waitNextMessage();
           //println("received a message : " + m);
           if (m instanceof BallMessage){
               BallMessage bm = (BallMessage) m;
               gui.createBall(bm.getPosY(),bm.getSpeedX(),bm.getSpeedY());
           }
           else if (m instanceof ActMessage){
               ActMessage am = (ActMessage) m;
               if (am.getAction().equals("ball position")){
                   int[] arg = (int[]) am.getObject();
                   gui.createBall(arg[0], arg[1], arg[2]);
               }
           }
           else if(m instanceof StringMessage){
               StringMessage sm = (StringMessage) m;
               if (sm.getString().equals("start")){
                   if (!creator){
                       creator=true;
                       gui.setRightPlayer();
                   }
                   gui.createInitBall();
                   gui.start();
               }
           }
       }
   }

   public void end(){
       gui.setGoOn(false);
   }

   void sendBall(int y_pos, int x_speed, int y_speed){
       AgentAddress[] v = getAgentsWithRole(groupName,"player");
       for (int i=0; i < v.length; i++){
           AgentAddress agent = v[i];
           if (! agent.equals(getAddress())){
               other = agent;
               break;
           }
       }
       //println("I send a BallMessage to " + other);
       //sendMessage(other,new BallMessage(y_pos,x_speed,y_speed));
       int[] pos = new int[3];
       pos[0] = y_pos; pos[1] = x_speed; pos[2] = y_speed;
       sendMessage(other,new ActMessage("ball position", pos));
   }

}
