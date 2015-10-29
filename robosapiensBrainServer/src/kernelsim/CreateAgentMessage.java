/*
* CreateAgentMessage.java 
* @author Jérôme Chapelle 2006
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

import java.util.Vector;

import smaapp.NeuronAgent;
import madkit.kernel.Message;
/** A CreateAgentMessage is used between NeuralAgent(s) and the NeuronScheduler.
* @author Jérôme Chapelle 2006
*/
public class CreateAgentMessage extends Message {
	static final long serialVersionUID = RmcLauncherApp.serialVersionUID; //42L;
	String _neuronGroup;
	String _label;
	Vector<NeuronAgent> _MotorGroup;
    
/** Constructs a new CreateAgentMessage 
*/
  public CreateAgentMessage(String neuronGroup, String label,Vector<NeuronAgent> MotorGroup )
  {
    super();
    _neuronGroup=neuronGroup;
    _label=label;
    _MotorGroup=MotorGroup;
  }

/** Return the neuronGroup of the agent to be created
@return The value of the message (0 - 1)*/
  public String getneuronGroup()
  {
    return _neuronGroup;
  }

/** Return the neuronGroup of the agent to be created
@return The value of the message (0 - 1)*/
  public String getlabel()
  {
    return _label;
  }
  
/** Return the debugString of the message */
  public Vector<NeuronAgent> getMotorGroup()
  {
    return _MotorGroup;
  }
  
/** Prints a string that represents the Message
*/
  public String toString()
  {
    String ret="("+getneuronGroup()+","+getlabel()+",Vec_nbelts:"+getMotorGroup().size()+")";
    return ret;
  }
  
}
