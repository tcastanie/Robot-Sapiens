/*
 * Action.java - Created on Feb 10, 2004
 * 
 * Copyright (C) 2003-2004 Sebastian Rodriguez
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
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
 * Last Update: $Date: 2004/03/12 17:25:14 $
 */

package madkit.pluginmanager;

import java.util.Collection;
import java.util.Properties;

/**Defines the actions a plugin can request to execute after installation.
 * An action is defined in a plugin's xml file. The declaration is:
 * &lt;action id="actionname" set of parameters><br>
 * <br>
 * The only registered attribute name is <code>id</code>, used to identify the action. 
 * The set of parameters depends on each action type.
 *  
 * @author Sebastian Rodriguez - sebastian.rodriguez@utbm.fr
 *
 * @version $Revision: 1.2 $
 */
public interface Action {
	/**Gets the action name.
	 * @return Action name
	 */
	public String getName();
	
	/**Gets the required plugins to execute this action.
	 * 
	 * @return
	 */
	public Collection requiredPlugins();
	/**Get the <b>Madatory</b> parameters needed by the action to execute 
	 * @return a collection of Strings with the paraments names
	 * @see Action.getOptionalParameters()
	 */
	public Collection getMandatoryParameters();
	
	/**Get the <b>Optional/b> parameters needed by the action to execute 
	 * @return a collection of Strings with the paraments names
	 * @see Action.getMadatoryParameters()
	 */
	public Collection getOptionalParameters();
	
	/**Sets the parameters obtained from the plugin xml file.
	 * <code>params</code> will contain both, mandatory and optional parameters
	 * @param params set of parameters
	 */
	public void setParamenters(Properties params);
	
	/**Request the action to execute.
	 * @return true if and only if the action execution was successful
	 */
	public boolean execute();
	/**Gets the failure reason. This method will be called only if execute
	 * return <code>false</code>
	 * @return failure reason
	 */
	public String getFailureReason();

    /**Should the Plugin Manager ask for user confirmation before executing the action.
     * @return true if the action requires user confirmation.
     */
    public boolean shouldAskUser();

    /**If showAskUser() returns true, this method is called to get the Question. Please formulate your question so
     * that "yes" means execute the Action
     * @return
     */
    public String getQuestionToUser();
    
    /**Gets the name of the plugin owner of this action
     * @return
     */
    public String getOwnerPlugin();
	
	/**Gets the version of the plugin owner of this action
	 * @return
	 */
	public Version getOwnerPluginVersion();
}
