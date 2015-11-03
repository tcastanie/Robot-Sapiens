/*
* LineChartAgent.java - LineCharts for MadKit
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

import madkit.kernel.Agent;
import madkit.kernel.Message;
import madkit.linechart.linechartgui.LineChartGUI;


/**  This Agent is dedicated to create linecharts. Send a LineChartMessage to it to create a new linechart or add a point to an existing linechart.
	To DOs : implementation is not completed (modify scale, space between point, etc... with messages
    @author Fabien MICHEL
    @version 1.0 26/06/2002 */

public class LineChartAgent extends Agent
{
    public LineChartGUI myGUI;
    public int width=400,height=300;

    public LineChartAgent()
    {
    }

    public void initGUI()
    {
	setGUIObject(myGUI = new LineChartGUI(width,height));
    }

public void activate()
{
	createGroup(true,"analyse",null,null);
	requestRole("analyse","linechart agent");
	myGUI.init();
}

public void live()
{
	while (true)
	{
		Message m = waitNextMessage();
		if (m instanceof LineChartMessage)
		  	handleMessage((LineChartMessage) m);
		else
			System.err.println(this+" : I receive a message that I do not understand (not a LineChartMessage) :"+m);
	 }
}

void handleMessage(LineChartMessage m)
{
	switch(m.command)
	{
		case LineChartMessage.NEW_LINECHART :
		  	myGUI.addNewGraph( m.linechartName );
		  	myGUI.setGraphColor(m.linechartName, m.color);

		case LineChartMessage.ADD_POINT :
			myGUI.drawPoint(m.linechartName, m.x, m.y);
	}
}

public void setScaleX(int scale)
{
	myGUI.setScaleX(scale);
}

public void setScaleY(int scale)
{
	myGUI.setScaleY(scale);
}

public void setLineChartTitle(String title)
{
	myGUI.setTitleGraph(title);
}

public void setXAxisName(String xAxisName)
{
	myGUI.setTitleAxeX(xAxisName);
}

public void setYAxisName(String yAxisName)
{
	myGUI.setTitleAxeY(yAxisName);
}

}
