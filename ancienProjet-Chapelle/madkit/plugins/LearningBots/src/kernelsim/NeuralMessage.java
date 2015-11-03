/*
* NeuralMessage.java 
* @author Jérôme Chapelle 2004
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
package kernelsim;

import madkit.kernel.Message;

/** A NeuralMessage is used between NeuralAgent(s).
* @author Jérôme Chapelle 2004
*/

public class NeuralMessage extends Message
{
	static final long serialVersionUID = RmcLauncherApp.serialVersionUID; //42L;
    double value;
    String debugString;
/** Constructs a new NeuralMessage that has:<br>
* value : double value, may be between 0 and 1<br>
* debugString : a string used to debug
*/
  public NeuralMessage(double v, String dS)
  {
    super();
    this.value=v;
    this.debugString=dS;
  }

/** Same as NeuralMessage(double v, String dS), but debugString is set to null
* v : double value, may be between 0 and 1
*/
  public NeuralMessage(double v)
  {
    super();
    this.value=v;
    this.debugString=null;
  }

/** Return the value of the message
@return The value of the message (0 - 1)*/
  public double getValue()
  {
    return value;
  }
  
/** Return the debugString of the message */
  public String getDebugString()
  {
    return debugString;
  }
  
/** Prints a string that represents the NeuralMessage
 should looks like this:<br> value=0.5 -[it's a test]
*/
  public String toString()
  {
    String ret="value="+getValue()+" -["+getDebugString()+"]";
    return ret;
  }
  
}