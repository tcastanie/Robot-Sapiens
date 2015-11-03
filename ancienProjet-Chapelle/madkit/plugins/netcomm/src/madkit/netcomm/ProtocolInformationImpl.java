/*
 * ProtocolInformationImpl.java - Created on Nov 4, 2003
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
 * Last Update: $Date: 2003/12/17 16:33:14 $
 */

package madkit.netcomm;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

/**
 * 
 * @author Sebastian Rodriguez - sebastian.rodriguez@utbm.fr
 *
 * @version $Revision: 1.1 $
 */
public class ProtocolInformationImpl implements Protocollnformation,Serializable{
	private Vector protos;
	private boolean refused=false;
	private String reason;
	private Collection extraParams=new Vector();
	
	/**
	 * Creates a ProtcolInformationImpl with all the KnownProtocols
	 */
	public ProtocolInformationImpl(){
		protos=new Vector();
		protos.addAll(KnownProtocols.getProtocols());
		refused=false;
		reason=null;
	}
	
	/**Creates a ProtcolInformationImpl with refuse set to true.
	 * @param reason
	 */
	public ProtocolInformationImpl(String reason){
		this.reason=reason.toString();
		refused=true;
		protos=new Vector();
	}
	
	/**Creates a ProtcolInformationImpl with <code>protos</code> as the set of acceptable protocols.
	 * @param protos
	 */
	public ProtocolInformationImpl(Collection protocolsToUse){
		refused=false;
		this.protos=new Vector();
		this.protos.addAll(protocolsToUse);
	}
	/* (non-Javadoc)
	 * @see madkit.netcomm.Protocollnformation#getBestProtocol(madkit.netcomm.Protocollnformation)
	 */
	public String getBestProtocol(Protocollnformation info) {
		Iterator i=protos.iterator();//i use the protos vector to ensure that the choosen protocol order is the local one
		while(i.hasNext()){
			String p=(String) i.next();
			if(info.getProtocols().contains(p)){
				return p;
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see madkit.netcomm.Protocollnformation#getProtocols()
	 */
	public Collection getProtocols() {
		return (Collection) protos.clone();
	}

	/* (non-Javadoc)
	 * @see madkit.netcomm.Protocollnformation#refused()
	 */
	public boolean refused() {
		return refused;
	}
	
	public void setRefuse(boolean r){
		refused=r;
	}

	/* (non-Javadoc)
	 * @see madkit.netcomm.Protocollnformation#refuseReason()
	 */
	public String refuseReason() {
		return reason.toString();
	}

	/* (non-Javadoc)
	 * @see madkit.netcomm.Protocollnformation#getVersion()
	 */
	public String getVersion() {
		return "4.0";
	}

	/* (non-Javadoc)
	 * @see madkit.netcomm.Protocollnformation#getExtraParameters()
	 */
	public Collection getExtraParameters() {
		return extraParams;
	}
	
	/**Sets the extra Parameters.
	 * @param ep
	 */
	public void setExtraParameters(Collection ep){
		Vector v=new Vector();
		v.addAll(ep);
		extraParams=v;
	}
	

}
