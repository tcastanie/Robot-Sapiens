/*
* BallMessage.java - the interface of a Graphical PingPong agent in Java
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

import madkit.kernel.Message;

public class BallMessage extends Message {
    int posy=0;
    int speedx=1;
    int speedy=1;

    public BallMessage(int y, int sx, int sy){
        posy=y;
        speedx=sx;
        speedy=sy;
    }

    public int getPosY(){return posy;}
    public int getSpeedX(){return speedx;}
    public int getSpeedY(){return speedy;}

}
