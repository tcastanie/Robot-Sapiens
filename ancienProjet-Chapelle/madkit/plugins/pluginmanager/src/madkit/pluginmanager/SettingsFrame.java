/*
 * SettingsFrame.java - Created on Feb 21, 2004
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
 * Last Update: $Date: 2004/06/29 13:47:03 $
 */

package madkit.pluginmanager;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.TitledBorder;




/**Basic Settings Windows.
 * @author Sebastian Rodriguez - sebastian.rodriguez@utbm.fr
 *
 * @version $Revision: 1.3 $
 */
class SettingsFrame extends JPanel {
    
    public static final int OK_OPTION=0, CANCEL_OPTION=1;
    
    private int _selectedOption=CANCEL_OPTION;
    
    private Properties _props=new Properties();
    
    private JTextField _username;
    private JTextField _useremail;
    
    private JTextField _proxyhost;
    private JSpinner _proxyport;
    
    private JDialog _dialog;
    
    public SettingsFrame(){
    	this(true);
    }
    
    public SettingsFrame(boolean showButtons){
        //setTitle("Plugin Manager Settings");
        
        //getContentPane().
        setLayout(new BorderLayout());
        
        //addWindowListener(new Close());
        
        JPanel p=new JPanel();
        
        ///proxy settings
        JPanel proxyPanel=new JPanel();
        proxyPanel.setBorder(new TitledBorder("Proxy"));
        proxyPanel.setLayout(new BorderLayout());
        
        JLabel lhost=new JLabel("Host");
        JLabel lport=new JLabel("Port");
        p=new JPanel();
        p.setLayout(new GridLayout(2,1));
        p.add(lhost);
        p.add(lport);
        proxyPanel.add(p,BorderLayout.WEST);
        
        _proxyhost =new JTextField();
        _proxyport=new JSpinner(new SpinnerNumberModel(8080,1,65536,1));
        p=new JPanel();
        p.setLayout(new GridLayout(2,1));
        p.add(_proxyhost);
        p.add(_proxyport);
        proxyPanel.add(p,BorderLayout.CENTER);
        
        
        
        //User info
        JPanel userPanel=new JPanel();
        userPanel.setBorder(new TitledBorder("User Information"));
        userPanel.setLayout(new BorderLayout());
        
        JLabel username=new JLabel("Your Name ");
        JLabel useremail=new JLabel("Your E-Mail ");
        p=new JPanel();
        p.setLayout(new GridLayout(2,1));
        p.add(username);
        p.add(useremail);
        userPanel.add(p,BorderLayout.WEST);
        
        
        _username=new JTextField();
        _useremail=new JTextField();
        
        p=new JPanel();
        p.setLayout(new GridLayout(2,1));
        p.add(_username);
        p.add(_useremail);
        userPanel.add(p,BorderLayout.CENTER);
        
        String note="<html>" +
        		"Important:<br>" +
        		"Your information will be sent to the Downlaod Source as username (your name) and passaword(your email)<br>" +
        		"<html>";
        JLabel userInfoNote=new JLabel(note);
        
        userPanel.add(userInfoNote,BorderLayout.SOUTH);
        
        p=new JPanel();
        p.setLayout(new BoxLayout(p,BoxLayout.Y_AXIS));
        p.add(userPanel);
        p.add(proxyPanel);
        
        //getContentPane().
        add(p,BorderLayout.CENTER);
        
        //Buttons
        if(showButtons){
        	JPanel btnPanel=new JPanel();
            JButton okBtn=new JButton("Accept");
            okBtn.addActionListener(new Ok());
            
            JButton cancelBtn=new JButton("Cancel");
            cancelBtn.addActionListener(new Cancel());
            btnPanel.add(okBtn);
            btnPanel.add(cancelBtn);

            add(btnPanel,BorderLayout.SOUTH);        	
        }else{
        	_selectedOption=OK_OPTION;
        }
                
    }
    
    private class Ok implements ActionListener
    {
        public void actionPerformed(ActionEvent ae)
        {
            _selectedOption = OK_OPTION;
            //synchronized (SettingsFrame.this)
                //{ SettingsFrame.this.notifyAll(); }
            _dialog.dispose();
        }
    }

    private class Clear implements ActionListener
    {
        public void actionPerformed(ActionEvent ae)
        {
            _username.setText("");
            _useremail.setText("");
            
            _proxyhost.setText("");
            _proxyport.setValue(new Integer(8080));
            _username.requestFocus();
        }
    }

    private class Cancel implements ActionListener
    {
        public void actionPerformed(ActionEvent ae)
        {
            _selectedOption = CANCEL_OPTION;
            _dialog.dispose();
            //synchronized (SettingsFrame.this)
                //{ SettingsFrame.this.notifyAll(); }
        }
    }


    private class Close extends WindowAdapter
    {
        public void windowClosing(WindowEvent we)
        {
            new Cancel().actionPerformed(null);
        }
    }

    
    
    /**Shows a Settings Window.
     * 
     * @param config Used to set the values
     * @return the Selected Option, either OK_OPTION or CANCEL_OPTION
     */
    public synchronized int showSettings(Properties config){
        _props=config;
        _proxyhost.setText(config.getProperty("http.proxyHost"));
        
        _username.setText(config.getProperty("madkit.user.name"));
        _useremail.setText(config.getProperty("madkit.user.email"));
        String port=config.getProperty("http.proxyPort");
        if(port==null || port.equals("")){
            port="8080";
        }
        _proxyport.setValue(Integer.valueOf(port));
        
        setVisible(true);
        
        
        _dialog=new JDialog();
        _dialog.getContentPane().add(this);
        _dialog.setModal(true);
        //_dialog.setSize(300,200);
        _dialog.pack();
        _dialog.show();
        _dialog.dispose();
        return _selectedOption;

    }
    
    public Properties getSelectedConfig(){
        if(_selectedOption==OK_OPTION){//if the user accepted the config, change it and return
            _props.setProperty("http.proxyHost",_proxyhost.getText());
            _props.setProperty("http.proxyPort",((Integer)_proxyport.getValue()).toString());
            _props.setProperty("madkit.user.name",_username.getText());
            _props.setProperty("madkit.user.email",_useremail.getText());
        }
        return _props;
        
    }
    
    public static Properties showSettingsDialog(File configFile) throws FileNotFoundException, IOException{
        Properties props=new Properties();
        props.load(new FileInputStream(configFile));
        SettingsFrame sf=new SettingsFrame();
        int opt=sf.showSettings(props);
        if(opt ==OK_OPTION){
            props.store(new FileOutputStream(configFile),"PluginManager Configuration file.");
            return props;
        }else{
            return null;
        }
    }
}
