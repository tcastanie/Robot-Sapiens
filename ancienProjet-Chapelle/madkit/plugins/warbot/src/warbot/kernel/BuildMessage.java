/*
* BuildMessage.java -Warbot: robots battles in MadKit
* Copyright (C) 2000-2002 Fabien Michel, Jacques Ferber
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
package warbot.kernel;

final class BuildMessage extends madkit.kernel.Message
{
	int type;
	String team;
	int x,y,radius=20;

BuildMessage(int type,String color,int x,int y)
{
	this.type=type;
	team=color;
	this.x=x;
	this.y=y;
}

BuildMessage(int type,String color,int radius,int x,int y)
{
	this.type=type;
	team=color;
	this.x=x;
	this.y=y;
	this.radius=radius;
}
}
