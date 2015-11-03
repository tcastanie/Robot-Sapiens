/*
 * SSLConfigurator.javaCreated on Dec 5, 2003
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
package madkit.netcomm;

//import org.apache.log4j.Logger;

/**Configures the SSL keyStore and trustStore.
 * There are to ways to configure these stores:
 * <ol>
 * <li>Use madkit's default: a default keystore and truststore is provided. We do
 * not advise to use these store because is provided with the distribution and they are NOT unique.<br>
 * To enable you must set <code>madkit.netcomm.ssl.config</code> property to $MADKIT_HOME/lib/support</li>
 * <li>Create your own: follow the instruction given in jsse to create your own keystore and trustStore.
 * Then set javax.net.ssl.keyStore and javax.net.ssl.trustStore properly.</li>
 * </ol>
 * @author Sebastian Rodriguez - sebastian.rodriguez@utbm.fr
 *	@version $Revision: 1.1 $
 */
final class SSLConfigurator {
	//private static Logger logger=Logger.getLogger(SSLConfigurator.class);
	
	public static final boolean configure(){
		String keyStore=System.getProperty("javax.net.ssl.keyStore");
		String trustStore=System.getProperty("javax.net.ssl.trustStore");
		String config=System.getProperty("madkit.netcomm.ssl.config");
		if(keyStore==null && config==null){
			return false;
		}
		
		if(keyStore==null){
			System.setProperty("javax.net.ssl.keyStore",config+"/madkitkeystore");
			System.setProperty("javax.net.ssl.keyStorePassword","madkit");
		}
		if(trustStore==null){
			System.setProperty("javax.net.ssl.trustStore",config+"/madkittruststore");
			System.setProperty("javax.net.ssl.trustStorePassword","madkit");
		}
		
		return true;
	}
	
}
