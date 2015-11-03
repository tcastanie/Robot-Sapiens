/*
* LineChartMessage.java - LineCharts for MadKit
* Copyright (C) 2000-2002 Fabien MICHEL
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

package madkit.linechart;

import java.awt.Color;

import madkit.kernel.Message;

/**Message class to communicate with the LineChartAgent
    @author Fabien MICHEL
    @version 1.0 26/06/2002 */

public class LineChartMessage extends Message
{
	final public static int NEW_LINECHART = 0;
	final public static int ADD_POINT = 1;
	
	public int x, y, command; 
	public String linechartName="uname";
	public Color color=Color.black;


/**To create a new linechart*/
public LineChartMessage(String linechartName)
{
	command = NEW_LINECHART;
	if(linechartName != null)
		this.linechartName = linechartName;
}

/**To create a new linechart with a specified color*/
public LineChartMessage(String linechartName, Color linechartColor)
{
	this(linechartName);
	if(linechartColor != null)
		color = linechartColor;
}

/**to add a point to a specified linechart*/
public LineChartMessage(String linechartName, int x, int y)
{
	command = ADD_POINT;
	this.linechartName = linechartName;
	this.x=x;
	this.y=y;
}

}
