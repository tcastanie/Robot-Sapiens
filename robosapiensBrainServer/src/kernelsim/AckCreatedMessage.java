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

	import smaapp.NeuronAgent;
	import madkit.kernel.Message;
	/** A AckCreatedMessage is used to notify that a new neuron has been created.
	* @author Jérôme Chapelle 2006
	*/

	public class AckCreatedMessage extends Message {
		static final long serialVersionUID = RmcLauncherApp.serialVersionUID; //42L;
		NeuronAgent _neuronAgentCreated;
	    
	/** Constructs a new AckCreatedMessage 
	*/
	  public AckCreatedMessage(NeuronAgent neuronAgentCreated)
	  {
	    super();
	    _neuronAgentCreated=neuronAgentCreated;
	  }

	/** Return the neuronAgentCreated that has been created
	*/
	  public NeuronAgent getneuronAgent()
	  {
	    return _neuronAgentCreated;
	  }

	/** Prints a string that represents the Message
	*/
	  public String toString()
	  {
	    String ret="("+getneuronAgent().getLabel()+")";
	    return ret;
	  }
	  
	}
