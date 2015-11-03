/*
* ComputerPlayer.java - the interface of a Graphical PingPong agent in Java
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

import java.awt.Color;import java.awt.Graphics;import javax.swing.JComponent;

class ComputerPlayer
{
    private int y_pos;
	private int x_pos;
	private int y_speed;
	private int real_y_pos;

    boolean side=true; // true = left, false = right
	private final int size_x = 10;
	private final int size_y = 50;

	private PongBall ball;

    JComponent display;

    public int getXPos(){ return x_pos;}
    public int getYPos(){ return y_pos; }
    public int getXSize(){ return size_x;}
    public int getYSize(){ return size_y;}
    public boolean getSide(){ return side;}

	public ComputerPlayer (JComponent disp, int x_pos, int y_pos, PongBall ball, boolean s)
    {
		this.x_pos = x_pos;
		this.y_pos = y_pos;
		y_speed = 4;
		this.ball = ball;
        side = s;
        display = disp;
	}

    void setBall(PongBall b){
        ball=b;
    }

    boolean isTheBallComing(){
        if (ball == null)
            return false;

        boolean b=(ball.getXSpeed() <= 0);
        if (side)
            return(b);
        else
            return (!b);
    }

	/** Moves the paddles */
	public void move ()
    {
		real_y_pos = y_pos + (size_y / 2);
		if (!isTheBallComing())
		{
			if (real_y_pos < (display.getWidth()/2) - 2) {
				y_pos += y_speed;
			}
			else if (real_y_pos > (display.getWidth()/2) + 2){
				y_pos -= y_speed;
			}
		}
		else {
			if ( real_y_pos != ball.getYPos()){
				if (ball.getYPos() < real_y_pos){
					y_pos -= y_speed;
				}
				else if (ball.getYPos() > real_y_pos){
					y_pos += y_speed;
				}
			}
        }
	}


	/** Displays the paddle
     *  @param g a Graphics
     * */
	public void display (Graphics g)
	{
		g.setColor (Color.blue);
		g.fillRect (x_pos, y_pos, size_x, size_y);
	}
}
