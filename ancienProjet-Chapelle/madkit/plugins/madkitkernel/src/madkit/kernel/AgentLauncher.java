/*
* AgentLauncher.java - How to launch an agent from its description or its file
* Copyright (C) 2003 Jacques Ferber
*
* This library is free software; you can redistribute it and/or
* modify it under the terms of the GNU Lesser General Public
* License as published by the Free Software Foundation; either
* version 2.1 of the License, or (at your option) any later version.

* This library is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
* Lesser General Public License for more details.

* You should have received a copy of the GNU Lesser General Public
* License along with this library; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
*/


package madkit.kernel;

import java.awt.Point;
import java.awt.Dimension;

/**
 * 
 * @author J.Ferber
 *
 * An abstract class which is used to launch agents from their description. Used in Desktops
 * and Explorer to launch agents of the right kind
 */
public abstract class AgentLauncher {
    protected String className;
	protected String label;
	protected Boolean gui;
	protected AbstractMadkitBooter booter;
	protected Point position;
	protected Dimension dim;
	protected String type;
	protected String typeArg;
	protected Object arg;
	protected AbstractAgent ag;
	
	public void setAgent(AbstractAgent ag){
		this.ag = ag;
	}
    
    public static AgentLauncher makeAgentLauncher(String launcherClass, AbstractMadkitBooter booter, String type, String className, 
    					String label, String typeArg, Object arg, boolean gui, Point p, Dimension d){
	try {
            Class cl = Utils.loadClass(launcherClass);
            AgentLauncher agentLauncher = (AgentLauncher) cl.newInstance();
            agentLauncher.init(booter,type,className,label,typeArg,arg,new Boolean(gui),p,d);

            return agentLauncher;
        } catch (ClassNotFoundException ex){
              System.err.println("Agent class does not exist"+ ex.getMessage());
        } catch (Exception ccex){
              System.err.println("Agent launch exception:"+ccex);
              // ccex.printStackTrace();
        }	
        return null;
    }
    
    public void init(AbstractMadkitBooter booter, String type, String className, 
    		String label, String typeArg, Object arg, Boolean gui, Point p, Dimension d){
        this.booter=booter;
        this.className = className;
        this.label=label;
        this.gui = gui;
        this.position=p; 
        if (position == null){position = new Point(-1,-1);
        }
        this.dim=d;        
		if (dim == null){dim = new Dimension(-1,-1);
		}
        this.type = type;
        this.typeArg = typeArg;
        this.arg = arg;
    }
    
    public abstract void launch();
}