/*
 * JBrowseButton.java - Created on Feb 25, 2004
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

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;


/**
 * @author Sebastian Rodriguez - sebastian.rodriguez@utbm.fr
 *
 * @version $Revision: 1.1 $
 */
class JBrowseButton extends JPanel implements ActionListener{
    public static final int BACK_BROWSE=0; 
    public static final int FORWARD_BROWSE=1;
    public static final int RELOAD_BROWSE=2;
    
    public static final String BIG_ICON="big";
    
    private int _type;
    private String _icon_size=BIG_ICON;
    private boolean _showText=true;
    private String _buttonText="?";
    
    private Vector _stack=new Vector();
    
    private JButton _button;
    private String _currentURL;
    private NavigationBar _browser;
    private JBrowseButton _forward;
    
    
    public JBrowseButton(NavigationBar bar,int type){
        this(bar,type,BIG_ICON);
    }
    
    public JBrowseButton(NavigationBar bar,int type, String iconSize){
        _type=type;
        _icon_size=iconSize;
        _browser=bar;
        String iconImage="leftarrow_";
        
        switch (_type) {
        case BACK_BROWSE:
            _buttonText="Back";
            iconImage="leftarrow_";
            break;
        case FORWARD_BROWSE:
            _buttonText="Forward";
            iconImage="rightarrow_";
            break;
        case RELOAD_BROWSE:
            _buttonText="Reload";
            iconImage="reload_";
            break;
        default:
            break;
        }
        
        //create the button
        ImageIcon icon=new ImageIcon(getClass().getResource("/images/"+iconImage+_icon_size+".png"));
        _button=new JButton(icon);
        _button.setToolTipText(_buttonText);
        _button.addActionListener(this);
        
        setLayout(new BorderLayout());
        add(_button,BorderLayout.CENTER);
        checkEnable();
    }

    /**
     * 
     */
    private void checkEnable() {
        if(_type==BACK_BROWSE || _type==FORWARD_BROWSE){
            _button.setEnabled(!_stack.isEmpty());
        }else{
            _button.setEnabled(true);
        }
        
    }

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        switch (_type) {
        case BACK_BROWSE:
            String url=(String) _stack.remove(0);
            _forward.addToStack(_currentURL);
            _browser.changePage(url,_type);
            break;
        case FORWARD_BROWSE:
            _browser.changePage((String) _stack.remove(0),_type);
            break;
        case RELOAD_BROWSE:
            _browser.changePage(_currentURL,_type);
            break;

        default:
            break;
        }
        checkEnable();
    }
    
    /**
     * @param url
     */
    void addToStack(String url) {
        _stack.add(0,url);
        checkEnable();
        
    }

    public void pageChanged(String url){
        
        if(_type==BACK_BROWSE){
            if(_currentURL!=null)
                _stack.add(0,_currentURL);
        }
        _currentURL=url.toString();
        checkEnable();
    }
    
    /**
     * 
     */
    void reset() {
        _stack=new Vector();
        checkEnable();
    }

    void setCurrentPage(String url){
        _currentURL=url.toString();
    }

    /**
     * @param _forward
     */
    public void setForwardButton(JBrowseButton forward) {
        _forward=forward;
        
    }
    
    
}
