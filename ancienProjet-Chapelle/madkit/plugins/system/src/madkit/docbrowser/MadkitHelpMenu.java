/*
 * MadkitHelpMenu.java - Created on Feb 26, 2004
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
 * Last Update: $Date: 2004/05/05 09:50:22 $
 */

package madkit.docbrowser;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;

import madkit.kernel.AbstractAgent;
import madkit.kernel.StringMessage;


/**Provides a Standard Help Menu.
 * 
 * @author Sebastian Rodriguez - sebastian.rodriguez@utbm.fr
 *
 * @version $Revision: 1.1 $
 */
public class MadkitHelpMenu extends JMenu {
    private String _plugin;
    private AbstractAgent _agent;
    
    public MadkitHelpMenu(AbstractAgent agent,String plugin, String displayString){
        _plugin=plugin;
        _agent=agent;
        setText("Help");
        //
        ImageIcon icon=new ImageIcon(getClass().getResource("/images/manual_small.png"));
        JMenuItem item=new JMenuItem(displayString+" Doc",icon);
        
        item.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent e) {
                requestDoc("all");                
            }
        });
        
        add(item);
        add(new JSeparator());

        item=new JMenuItem("About "+plugin);
        item.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent e) {
                //requestDoc("all");
                
            }
        });

        add(item);
        icon=new ImageIcon(getClass().getResource("/images/toolbars/org_group16.gif"));
        item=new JMenuItem("About Madkit",icon);
        item.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent e) {
                //requestDoc("all");
                
            }
        });        
        add(item);

    }
    
    
    
    private void requestDoc(String section){
        if(section==null){
            section="all";
        }
        String req="displayDoc::plugin::"+_plugin+"-doc::section::"+section;
        _agent.sendMessage(DocBrowserAgent.community,DocBrowserAgent.group,DocBrowserAgent.DOC_BROWSER_ROLE,new StringMessage(req));
    }
}
