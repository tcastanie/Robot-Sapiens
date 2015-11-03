/*
* GPongGUI.java - the interface of a Graphical PingPong agent in Java
* Author Jacques Ferber (November 2002)
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
/*
* GPongGUI.java - the class that shows the PingPong and moves the Paddle..
* Author Jacques Ferber (November 2002)
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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JComponent;

/**
 * The class that shows the PingPong and moves the paddle..
 * @author J. Ferber
 * @version 1.0
 */

public class GPongGUI extends JComponent implements Runnable {
    GPongAgent ag;
    PongBall ball;
    ComputerPlayer computer;
    Thread thread;
    boolean running = true;

    final int default_width=280;
    final int default_height=240;

    boolean side=false;   // true = left, false = right
    boolean getSide(){return side;}

    public GPongGUI(GPongAgent _ag) {
        ag = _ag;
        setBackground (Color.black);
        this.setPreferredSize(new Dimension(default_width,default_height));
        side = false;
    }

    void setLeftPlayer(){
        side = true;
        computer = new ComputerPlayer (this,15,(getHeight()/2)+20, null,true);
        repaint();
    }

    void setRightPlayer(){
        side = false;
        computer = new ComputerPlayer (this,getWidth()-35,(getHeight()/2)-20, null,false);
        repaint();
    }

    void leaveBall(int y_pos,int x_speed, int y_speed){
        computer.setBall(null);
        ball = null;
        ag.sendBall(y_pos,x_speed,y_speed);
    }

    void createInitBall(){
        createBall((getHeight()/3),2,2);
    }

    void createBall(int y_pos,int x_speed, int y_speed){
        if (side){ // if left side
            ball = new PongBall(this,getWidth()-10,y_pos);
            if (x_speed > 0) x_speed = - x_speed;
        } else {
            ball = new PongBall(this,10,y_pos);
            if (x_speed < 0) x_speed = - x_speed;
        }
        ball.setSpeed(x_speed,y_speed);
        computer.setBall(ball);
    }

    public void start(){
        if (thread == null){
            thread = new Thread (this);
            thread.start();
        }
    }

    public void stop(){
        if (thread != null)
            thread.stop();
    }

    public synchronized void setGoOn(boolean b){
        running=b;
    }

    public void run(){
        while (running)
        {
            repaint();

            if (ball != null) ball.move();
            if (computer != null) computer.move();

            if (ball != null) ball.testForCollisionComputer(computer);

            try
            {
                Thread.sleep (10);
            }
            catch (InterruptedException ex)
            {
                break;
            }
        }
    }

    public void paintComponent(Graphics g){
        if (ball != null) ball.display(g);
        if (computer != null) computer.display (g);
    }
}
