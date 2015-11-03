package madkit.demos.gpong;

import java.awt.Color;import java.awt.Graphics;

public class PongBall
{
	private int x_pos;
	private int y_pos;
	private int x_speed;
	private int y_speed;

    GPongGUI display;

	private int right_out = 390;
	private int left_out = 10;
	private int down_out =290;
	private int up_out = 10;

	static final int radius = 10;

    public int getXSpeed ()
    {
        return x_speed;
    }

    public int getYPos ()
    {
        return y_pos;
    }


	public PongBall (GPongGUI disp, int xp, int yp)
	{
		this.x_pos = xp;
		this.y_pos = yp;
		x_speed = 3;
		y_speed = 3;
        display = disp;
	}

    void setSpeed(int sx, int sy){
        x_speed = sx;
        y_speed = sy;
    }
	public void move ()
	{
		x_pos += x_speed;
		y_pos += y_speed;

		isBallOut();
	}

    public void isBallOut (){
        down_out = display.getHeight()-radius;
        right_out = display.getWidth()-radius;
        if ((x_speed < 0)&& (x_pos < left_out)){
                if (!display.getSide())
                    display.leaveBall(y_pos,x_speed,y_speed);
                else
                    x_speed = - x_speed;
        } else if ((x_speed > 0) && (x_pos > right_out)){
                if (display.getSide())
                    display.leaveBall(y_pos,x_speed,y_speed);
                else
                    x_speed = - x_speed;
        }

        if (y_speed < 0) {
            if (y_pos < up_out){
                y_speed = - y_speed;
            }
        } else if (y_speed > 0){
            if (y_pos > down_out){
                y_speed = - y_speed;
            }
        }
    }


	public void testForCollisionComputer (ComputerPlayer computer)
    {
        boolean side=display.getSide();
        if ((side && x_speed<0) || (!side && x_speed>0)){
            int ball_top = y_pos - radius;
            int ball_bottom = y_pos + radius;
            int ball_left = x_pos - radius;
            int ball_right = x_pos + radius;

            int comp_top = computer.getYPos();
            int comp_bottom = computer.getYPos() + computer.getYSize();
            int comp_right = computer.getXPos() + computer.getXSize();
            int comp_left = computer.getXPos();

            if ((ball_top >= comp_top - radius) && (ball_bottom <= comp_bottom + radius)){
                if (side){
                    if (ball_left <= comp_right)
                        x_speed = - x_speed;
                } else
                    if (ball_right >= comp_left)
                        x_speed = - x_speed;
            }
        }
	}


	public void display (Graphics g)
	{
		g.setColor (Color.red);
		g.fillOval (x_pos - radius, y_pos - radius, 2 * radius, 2 * radius);
	}
}
