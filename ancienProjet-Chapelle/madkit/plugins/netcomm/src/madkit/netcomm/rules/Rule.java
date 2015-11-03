/*
 * Rule.javaCreated on Dec 4, 2003
 *
 * Copyright (C) 2003 Sebastian Rodriguez
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.

 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 *Last Update $Date: 2003/12/17 16:33:14 $

 */
package madkit.netcomm.rules;

import java.util.Collection;
import java.util.regex.Pattern;

import madkit.netcomm.KnownProtocols;
import madkit.netcomm.ProtocolInformationImpl;
import madkit.netcomm.Protocollnformation;



/**
 * @author Sebastian Rodriguez - sebastian.rodriguez@utbm.fr
 *	@version $Revision: 1.1 $
 */
public final  class Rule {
	/**Accept the connection*/
	public static final int ACCEPT=0;
	/**Reject the connection. No reason for the rejection is sent*/
	public static final int REJECT=1;
	/**Refuse the connection. A reason must be given.*/
	public static final int REFUSE=2;

	/**Unicode dot. Provided only as utility.*/
	public static final String DOT="\\u002E";
	
	private final Pattern pattern;
	private final int action;
	private final Object _argument;
	
	public Rule(String regexp,int action,Object arg){
		
		//check the Rule
		switch (action) {
			case Rule.ACCEPT :
				if(arg!=null && !(arg instanceof Collection)){
					throw new IllegalArgumentException("to Accept a connection arg must be either a collection of protocols to use or null to use the default protocol order");
				}
				if(arg==null){
					arg=new ProtocolInformationImpl();
				}else if(arg instanceof Collection){
					arg=new ProtocolInformationImpl((Collection)arg);
				}
				
				break;

			case Rule.REFUSE:
				if(!(arg instanceof String && arg!=null)){
					throw new IllegalArgumentException("A reason must ");
				}
				break;
				
			case Rule.REJECT :
				
				break;
				
			default :
				throw new IllegalArgumentException("Unknown action");
		}
		this.action=action;
		this._argument=arg;
		pattern=Pattern.compile(regexp);
		
	}
	
	
	/**Gets the associated action to the hosts matching this rule.
	 * @return Returns the action.
	 */
	public int getAction() {
		return action;
	}
	
	/**Checks if the host <code>host</code> matches this rule
	 * @param host host to check
	 * @return
	 */
	public boolean matches(String host){
		return pattern.matcher(host).matches();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		String str="pattern : "+pattern.pattern();
		switch (action) {
			case Rule.ACCEPT:
				str+=" ACCEPT - Protocols : "+getProtocolInformation().getProtocols().toString() ;
				break;

			case Rule.REJECT:
				str+=" REJECT";
				break;

			case Rule.REFUSE:
				str+=" REFUSE reason: "+_argument;
				break;

			default :
				break;
		}
		return str;
	}
	
	/**Checks if the Protocol <code>protocol</code> is present in this rule.
	 * @param protocol
	 * @return 
	 */
	public boolean contains(String protocol){
		
		if(action == Rule.REJECT || action==Rule.REFUSE){
			return false;
		}
		
		if(_argument==null){//_argument null for ACCEPT means use the default order
			return KnownProtocols.getProtocols().contains(protocol);
		}else{
			return ((Protocollnformation)_argument).getProtocols().contains(protocol);
		}
	}
	
	/**Gets the protocol information of this rule, or creates a new one containning the refuse reason
	 * @return
	 */
	public Protocollnformation getProtocolInformation(){
		if(action == Rule.REJECT || action==Rule.REFUSE){
			return new ProtocolInformationImpl((String) _argument);
		}
		return (Protocollnformation) _argument;
		
	}
	
	/**Returns the Argument.
	 * @return
	 */
	public Object getArgument(){
		return _argument;
	}
}
