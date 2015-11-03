/*
 * PluginInstaller.java - Created on Feb 8, 2004
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
 * Last Update: $Date: 2004/06/29 13:44:51 $
 */

package madkit.pluginmanager;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.TitledBorder;
import javax.swing.text.html.HTMLEditorKit;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import madkit.kernel.AbstractAgent;
import madkit.kernel.GraphicShell;
import madkit.kernel.Kernel;

/**Its a simple installer for Madkit. It will show the lincense
 * and lets the user select the installation directory.
 *  
 * @author Sebastian Rodriguez - sebastian.rodriguez@utbm.fr
 *
 * @version $Revision: 1.5 $
 */
public class PluginInstaller extends  JFrame implements GraphicShell{
	
	private Kernel _kernel;
	private HelpViewer _help=null;
	private JPanel cards;
	//
	private JButton _btnNext;
	private InstallIndex _index;
	private Hashtable _context;
	
	private Hashtable _guis=null;
	
	public PluginInstaller(File madkitInstallDir){
		
		setTitle("Plugin Installer");
		addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent evt) {
				askAndExit();
			}
		});
		_context=new Hashtable();
		
		getContentPane().setLayout(new BorderLayout(2,2));
		//_madkitDir=madkitInstallDir;
		
		_kernel=new Kernel("Plugin Installer");
		_kernel.registerGUI(this);
		
		JPanel head=new JPanel();
		head.setBackground(new Color(246,247,233));//set the background color to the jpg's bg color
		JLabel licon=new JLabel();
		URL url=getClass().getResource("/images/title.jpg");
		if(url!=null){
			licon.setIcon(new ImageIcon(url));
		}
		
		head.add(licon);
		getContentPane().add(head,BorderLayout.NORTH);
		
		_index=new InstallIndex();
		createComponets();
		getContentPane().add(_index,BorderLayout.WEST);
		
		cards=new JPanel();
		cards.setLayout(new CardLayout());
		
		for (Iterator iter = _index.getSteps().iterator(); iter.hasNext();) {
			InstallStep element = (InstallStep) iter.next();
			element.initPanel();
			cards.add(element.getPanel(),element.getName());
		}
		
		
		
		getContentPane().add(cards,BorderLayout.CENTER);
		
		//Next back buttons
		JPanel btnPanel=new JPanel();
		_btnNext=new JButton("Next >");
		_btnNext.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				next();
				
			}
		});
		
		JButton btnBack=new JButton("< Back");
		btnBack.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				back();
				
			}
		});
		JButton btnExit=new JButton("Exit");
		btnExit.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				askAndExit();
				
			}

			
		});
		
		JButton btnHelp=new JButton("Help");
		btnHelp.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				if(_help==null){
					_help=new HelpViewer();
				}
				_help.setPage(_index.getCurrentStep().getHelpURL());
				_help.show();
				
			}

			
		});
		
		
		btnPanel.add(btnBack);
		btnPanel.add(_btnNext);
		btnPanel.add(btnExit);
		btnPanel.add(btnHelp);
		
		getContentPane().add(btnPanel,BorderLayout.SOUTH);
		
		setSize(700,450);
		//setResizable(false);
		
		
		show();
		
	}
	
	private void askAndExit() {
		int op=JOptionPane.showConfirmDialog(this,"Do you really want to Exit ?","Exit",JOptionPane.YES_NO_OPTION);
		if(op==JOptionPane.YES_OPTION){
			System.exit(0);
		}
	}
	
	private void createComponets(){
		//intro
		_index.addInstallStep(new InstallStep(0,"intro","Introduction"){

			public void initPanel() {
					//JPanel introPanel=new JPanel();
					JLabel introLable=new JLabel();
					//introPanel.setLayout(new BorderLayout());
					introLable.setText("<html>"+
"<body>"+
"<h1>MadKit -<i> Multi Agent Development Kit</i></h1>"+
"<h2>Version 4.0 </h2>"+
    
"<p>This installer will guide you through the installation of MadKit.</p>"+
"<p>Click <b>Help</b> for instructions on any of the Installation Steps</p>"+ 
"</body>"+
"</html>");
					//introPanel.add(introLable,BorderLayout.NORTH);
					//ImageIcon ii = new ImageIcon(getClass().getResource("/images/neomadkit_small.jpg"));
					//introPanel.add(new JLabel(ii),BorderLayout.CENTER);
					_panel.setLayout(new BorderLayout());
					_panel.add(introLable,BorderLayout.CENTER);
				}
			
				public URL getHelpURL(){
					return getClass().getResource("help-intro.html");
				}
			});
		
		//lincense
		_index.addInstallStep(new InstallStep(1,"license","Madkit's License"){

			public void initPanel() {
				JPanel lincensePanel=new JPanel();
				lincensePanel.setLayout(new BorderLayout());
				JTextPane licenseText=new JTextPane();
				licenseText.setEditable(false);
						
				try {
					licenseText.setPage(getClass().getResource("/madkit/pluginmanager/gpl.html"));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
				JScrollPane sc=new JScrollPane();
				sc.setViewportView(licenseText);		
				lincensePanel.add(sc,BorderLayout.CENTER);
				_panel=lincensePanel;
				
			}
			public URL getHelpURL(){
				return getClass().getResource("help-license.html");
			}
		});

		//		Install Dir
		_index.addInstallStep(new InstallStep(2,"installdir","Select Install Directory"){
			File dir=null;
			private JTextField _installDir;
			public void initPanel() {
				
				JPanel installDirPanel=new JPanel();
				installDirPanel.setLayout(new BorderLayout());
				installDirPanel.add(new JLabel("Select the Directory where you want to install Madkit"),BorderLayout.NORTH);
				JPanel dirPanel=new JPanel();
				dirPanel.setLayout(new GridBagLayout());
				
				GridBagConstraints constraints=new GridBagConstraints();
				constraints.fill=GridBagConstraints.BOTH;
				constraints.gridwidth=3;
				constraints.weightx=0.1;
				_installDir=new JTextField();
				_installDir.setEditable(false);
				dirPanel.add(_installDir,constraints);
				
				JButton choose=new JButton("Browse");
				choose.addActionListener(new ActionListener(){

					public void actionPerformed(ActionEvent e) {

						JFileChooser fc=new JFileChooser();
				
						fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
						int res=fc.showOpenDialog(new JFrame());
						if(res==JFileChooser.APPROVE_OPTION){
							dir= fc.getSelectedFile();
							if(dir!=null){
								if(!dir.exists()){
									int op=JOptionPane.showConfirmDialog(PluginInstaller.this,"The Selected Directory does not exists. Do you want to create it?","Create Directory",JOptionPane.YES_NO_OPTION);
									if(op==JOptionPane.YES_OPTION){
										dir.mkdirs();
									}else{
										dir=null;
										return;
									}
								}
								if(!dir.canWrite()){
									JOptionPane.showMessageDialog(PluginInstaller.this,"Can't write in the selected directory","Can not Write",JOptionPane.ERROR_MESSAGE);
									dir=null;
								}else{	
									_installDir.setText(dir.getAbsolutePath());
									_installDir.setEditable(false);
									_context.put("madkitDir",dir.getAbsoluteFile());
								}
							}
							
							
						}
						checkNextEnable();
					}

				});
				
				dirPanel.add(choose);
				
				installDirPanel.add(dirPanel,BorderLayout.CENTER);
				_panel=installDirPanel;
			}
			public boolean isStepFinished(){
				return dir!=null;
			}
			
			public URL getHelpURL(){
				return getClass().getResource("help-madkitdir.html");
			}
		});
		
		
		_index.addInstallStep(new InstallStep(3,"installfrom","Select Install Source"){
			public void initPanel() {
				_panel=new JPanel();
				_panel.setLayout(new FlowLayout(FlowLayout.LEFT));
				_context.put("installSource",new Boolean(true));
				JRadioButton webSource=new JRadioButton("Install from Internet (Recommended)",true);
				webSource.addActionListener(new ActionListener(){

					public void actionPerformed(ActionEvent e) {
						_context.put("installSource",new Boolean(true));//if installSource is true then install from the web
						
					}
				});
				JRadioButton localSource=new JRadioButton("Install from Local File",false);
				localSource.addActionListener(new ActionListener(){

					public void actionPerformed(ActionEvent e) {
						_context.put("installSource",new Boolean(false));//if installSource is true then install from the web
						
					}
					
				});
				ButtonGroup grp=new ButtonGroup();
				grp.add(webSource);
				grp.add(localSource);
				_panel.add(webSource);
				_panel.add(new JLabel("<html>Downloads the latests version from http://www.madkit.net/plugins <br> Requires an Internet Connection</html>"));
				_panel.add(localSource);
				_panel.add(new JLabel("<html>Installs the plugins from a local file<br>You have to download the zip bundle from http://www.madkit.org</html>"));
			}
			public URL getHelpURL(){
				return getClass().getResource("help-source.html");
			}
			
		
		});
		
		_index.addInstallStep(new InstallStep(4,"installconfig","Configure Installation"){
			JPanel config=new JPanel();
			boolean inet;
			Properties _props=new Properties();
			JTextField _username;
			JTextField _email;
			public void initPanel() {
				_panel=new JPanel();
				_panel.setLayout(new GridLayout(2,1));
				JPanel p=new JPanel();
				p.setLayout(new FlowLayout(FlowLayout.LEFT));
				p.setBorder(new TitledBorder("Install Type"));
				_context.put("installType",new Boolean(true));
				p.setLayout(new FlowLayout(FlowLayout.LEFT));
				
				JRadioButton full=new JRadioButton("Full Install",true);
				full.addActionListener(new ActionListener(){

					public void actionPerformed(ActionEvent e) {
						_context.put("installType",new Boolean(true));
						
					}
					
				});
				JRadioButton custom=new JRadioButton("Custom Install");
				custom.addActionListener(new ActionListener(){

					public void actionPerformed(ActionEvent e) {
						_context.put("installType",new Boolean(false));
						
					}
					
				});
				
				ButtonGroup grp=new ButtonGroup();
				grp.add(full);
				grp.add(custom);
				p.add(full);
				p.add(new JLabel("Installs all Madkit Plugins, including sources and Documentation"));
				p.add(custom);
				p.add(new JLabel("Lets you select the plugins to install"));
				_panel.add(p);
				
				
				config.setBorder(new TitledBorder("Install Source"));
				_panel.add(config);
			}
		
			public void activate(){
				inet=((Boolean)_context.get("installSource")).booleanValue();
				config.removeAll();
				if(inet){
					config.setLayout(new GridLayout(4,2));
					JTextField t=new JTextField("http://www.madkit.net/plugins");
					t.setEditable(false);//TODO This should be editable when more donwload server are available
					_context.put("inetSource","http://www.madkit.net/plugins");
					config.add(new JLabel("Download Server"));
					config.add(t);
					final JLabel userl=new JLabel("Name");
					config.add(userl);
					_username=new JTextField();
					_username.setText(_props.getProperty("madkit.user.name",""));
					_username.addKeyListener(new KeyListener(){

						public void keyPressed(KeyEvent e) {}

						public void keyReleased(KeyEvent e) {
							_props.put("madkit.user.name",_username.getText());
							checkNextEnable();
						}

						public void keyTyped(KeyEvent e) {}
					});
					
										
					config.add(_username);
					config.add(new JLabel("Email"));
					_email=new JTextField();
					_email.setText(_props.getProperty("madkit.user.email",""));
					_email.addKeyListener(new KeyListener(){

						public void keyPressed(KeyEvent e) {}

						public void keyReleased(KeyEvent e) {
							_props.put("madkit.user.email",_email.getText());
							checkNextEnable();
						}

						public void keyTyped(KeyEvent e) {}
						
					});
					config.add(_email);
					JButton set=new JButton("Set Proxy");
					set.addActionListener(new ActionListener(){

						public void actionPerformed(ActionEvent e) {
							SettingsFrame stf=new SettingsFrame();
							int res=stf.showSettings(_props);
							
							if(res==SettingsFrame.OK_OPTION){
								_props=stf.getSelectedConfig();
							}
							checkNextEnable();
						}
						
					});
					
					config.add(set);
				}else{
					
					config.setLayout(new GridBagLayout());
					
					GridBagConstraints constraints=new GridBagConstraints();
					
					final JTextField t=new JTextField();
					t.setEditable(false);
					if(_context.get("localFile")!=null){
						t.setText(_context.get("localFile").toString());
					}
					config.add(new JLabel("Select Madkit's bundle file"));
					constraints.fill=GridBagConstraints.BOTH;
					constraints.gridwidth=3;
					constraints.weightx=0.1;
					config.add(t,constraints);	
					
					
					
					
					JButton choose=new JButton("Browse");
					choose.addActionListener(new ActionListener(){

						public void actionPerformed(ActionEvent e) {

							JFileChooser fc=new JFileChooser();
							File dir;
							fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
							int res=fc.showOpenDialog(new JFrame());
							if(res==JFileChooser.APPROVE_OPTION){
								dir= fc.getSelectedFile();
								if(dir!=null){
									
									if(!dir.canRead()){
										JOptionPane.showMessageDialog(PluginInstaller.this,"Can't read in the selected File","Can not Read",JOptionPane.ERROR_MESSAGE);
										dir=null;
									}else{	
										t.setText(dir.getAbsolutePath());
										t.setEditable(false);
										_context.put("localFile",dir.getAbsoluteFile());
									}
								}
								
								
							}
							checkNextEnable();
						}

					});
					
					config.add(choose);
					
				}
			}
			public URL getHelpURL(){
				return getClass().getResource("help-type.html");
			}
			
			public void end(){
				_context.put("config",_props);
			}
			public boolean isStepFinished(){
				if(inet){
					if(_props==null)return false;
					String name=_props.getProperty("madkit.user.name");
					if(name==null || name.equals("")) return false;
					String email=_props.getProperty("madkit.user.email");
					if(email==null || email.equals("")){
						return false;
					}
					if(email.indexOf('@')<0){
						setWarningMessage("A valide email address is required");
						return false;
					}
					setWarningMessage(null);
					return true;
				}else{
					File sourceFile=(File) _context.get("localFile");
					if(sourceFile!=null && sourceFile.canRead()){
						return true;
					}else{
						return false;
					}
				}
			}
		});
		
		_index.addInstallStep(new InstallStep(5,"done","Install"){
			JLabel label=new JLabel();
			URL help;
			public void initPanel() {
				_panel=new JPanel();
				_panel.add(label);
				
			}
			
			public void activate(){
				boolean inet=((Boolean)_context.get("installSource")).booleanValue();
				boolean full=((Boolean)_context.get("installType")).booleanValue();
				String text="<html>Summary <br><hr>";
				text+="Install Directory: <b>"+_context.get("madkitDir")+"</b><br>";
				text+="Install Source: <b>"+(inet?"Internet</b> ("+_context.get("inetSource")+")":"Local File</b>")+"<br>";
				text+="Install: <b>"+(full?"Full":"Custom")+"</b><br>";
				text+="<br><br>";
				if(!full){
					text+="<hr>You selected a custom install, to select the plugins click <b>Launch Plugin Manager</b><br>Click <b>Help</b> for futher information";
					help=getClass().getResource("help-finish-custom.html");
				}else{
					text+="<hr>You selected a Full Madkit Install, click <b>Install</b>";
					help=getClass().getResource("help-finish-full.html");
				}
				
				text+="</html>";
				label.setFont(new Font("Serial",Font.PLAIN,12));
				label.setText(text);
				
			}
			
			public URL getHelpURL(){
				return help;
			}
			
			public void end(){
				System.out.println("Launching Manager");
				File dir=(File) _context.get("madkitDir");
				boolean inet=((Boolean)_context.get("installSource")).booleanValue();
				boolean full=((Boolean)_context.get("installType")).booleanValue();
				File localfile=(File) _context.get("localFile");
				String downServer=_context.get("inetSource").toString();
				
				
				ManagerAgent agent=new ManagerAgent(dir,(Properties) _context.get("config"));
				
				
				
							
				if(inet){					
					agent.setDownloadSource(downServer);
				}else{
					try {
						//TODO Handle exceptions properly
						agent.installFromBigLocalDistribution(localfile);
						
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					
					
				}
				
				if(full){
					agent.setFullInstallRequest();
				}
				
				_kernel.launchAgent(agent,"PluginManagerAgent",_kernel,true);
				
				agent.refreshPluginsFromSources();
				
				_btnNext.setEnabled(false);
			}
		});
		
		
		
		//init the index
		_index.init();
	}
	 
	private void next(){
		InstallStep step=_index.getCurrentStep();
		
		if(step.isStepFinished()){
			step.end();
			_index.next();
			if(_index.isLastStep()){
				if(((Boolean)_context.get("installType")).booleanValue()){//means a full install
					_btnNext.setText("Install");
				}else{
					_btnNext.setText("Launch Plugin Manager");
				}
				
			}
			step=_index.getCurrentStep();
			checkNextEnable();
			CardLayout l=(CardLayout) cards.getLayout();
			l.show(cards,step.getName());
			if(_help!=null){
				_help.setPage(step.getHelpURL());
			}
		}
		
	}
	

	

	private void checkNextEnable() {
		InstallStep step=_index.getCurrentStep();
		if(step.isStepFinished()){
			_btnNext.setEnabled(true);
		}else{
			_btnNext.setEnabled(false);
		}
	}

	private void back(){
		InstallStep step=_index.getCurrentStep();
		if(_index.isLastStep()){
			_btnNext.setText("Next >");
		}
		_index.back();
		
		step=_index.getCurrentStep();
		if(step.isStepFinished()){
			_btnNext.setEnabled(true);
		}else{
			_btnNext.setEnabled(false);
		}
		CardLayout l=(CardLayout) cards.getLayout();
		l.show(cards,step.getName());
		if(_help!=null){
			_help.setPage(step.getHelpURL());
		}
	}
	/**
	 * 
	 */
	private void informFailureAndExit() {
		JOptionPane.showMessageDialog(new JFrame(),"Starting the Plugin Installer failed","Error",JOptionPane.ERROR_MESSAGE);
		System.exit(-1);
	}


	private File askInstallDir(){
		JFileChooser fc=new JFileChooser();
		
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int res=fc.showOpenDialog(new JFrame());
		if(res==JFileChooser.APPROVE_OPTION){
			return fc.getSelectedFile();
		}
		return null;
	}
	
	public static void main(String[] args) {
		try {
			javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e){}
		new PluginInstaller(null);
	}

	
	////////////////////////////////////////////////////////////////////////////
	/// Graphic shell
	////////////////////////////////////////////////////////////////////////////
	/* (non-Javadoc)
	 * @see madkit.kernel.GraphicShell#setupGUI(madkit.kernel.AbstractAgent)
	 */
	public void setupGUI(AbstractAgent arg0) {
		arg0.initGUI();
		Object agui=arg0.getGUIObject();
		if(agui!=null && agui instanceof Component){
			if(_guis==null){
				_guis=new Hashtable();
			}
			
			JFrame af=new JFrame();
			af.setSize(400,500);
			af.setTitle(arg0.getName());
			af.getContentPane().add((Component)agui );
			_guis.put(arg0,af);
			af.show();
		}
		
	}

	/* (non-Javadoc)
	 * @see madkit.kernel.GraphicShell#setupGUI(madkit.kernel.AbstractAgent, java.awt.Point, java.awt.Dimension)
	 */
	public void setupGUI(AbstractAgent arg0, Point arg1, Dimension arg2) {
		setupGUI(arg0);
		
	}

	/* (non-Javadoc)
	 * @see madkit.kernel.GraphicShell#disposeGUI(madkit.kernel.AbstractAgent)
	 */
	public void disposeGUI(AbstractAgent arg0) {
		disposeGUIImmediatly(arg0);
		
	}

	/* (non-Javadoc)
	 * @see madkit.kernel.GraphicShell#disposeGUIImmediatly(madkit.kernel.AbstractAgent)
	 */
	public void disposeGUIImmediatly(AbstractAgent arg0) {
		JFrame af=(JFrame) _guis.get(arg0);
		if(af!=null){
			af.dispose();	
		}
		
	}

	/* (non-Javadoc)
	 * @see madkit.kernel.GraphicShell#getDefaultGUIObject(madkit.kernel.AbstractAgent)
	 */
	public Object getDefaultGUIObject(AbstractAgent arg0) {
		return System.out;
	}
}
/**
 * General Install Index.<br>
 * @author Sebastian Rodriguez - sebastian.rodriguez@utbm.fr
 *
 * @version $Revision: 1.5 $
 */
class InstallIndex extends JPanel{
	
	Collection _steps;
	int _currentStep;
	Hashtable _labels;
	Font _currentFont;
	Font _normalFont;
	ImageIcon doneIcon;
	ImageIcon togoIcon;
	
	public InstallIndex(){
		_steps=new Vector();
		_currentStep=0;
		_labels=new Hashtable();
		_currentFont=new Font("Serial",Font.BOLD,12);
		_normalFont=new Font("Serial",Font.ITALIC,10);
		doneIcon=new ImageIcon(getClass().getResource("/images/stepfinished.png"));
		togoIcon=new ImageIcon(getClass().getResource("/images/togo.png"));
	}
	
	
	/**
	 * 
	 */
	public void next() {
		if(_currentStep<(_steps.size()-1)){
			setFont(_currentStep,_normalFont);
			setDoneStep(_currentStep,true);
			_currentStep++;
			setFont(_currentStep,_currentFont);
			getStep(_currentStep).activate();
		}
	}
	
	private InstallStep getStep(int s){
		return (InstallStep)_steps.toArray()[s];
	}
	
	/**
	 * @param step
	 */
	private void setDoneStep(int step,boolean done) {
		if(done){
			((JLabel)_labels.get(((InstallStep)_steps.toArray()[step]).getName())).setIcon(doneIcon);	
		}else{
			((JLabel)_labels.get(((InstallStep)_steps.toArray()[step]).getName())).setIcon(togoIcon);
		}
		
	}

	public void back(){
		if(_currentStep>0){
			setFont(_currentStep,_normalFont);
			setDoneStep(_currentStep,false);
			_currentStep--;
			setFont(_currentStep,_currentFont);
		}
	}
	
	private void setFont(int label, Font font){
		((JLabel)_labels.get(((InstallStep)_steps.toArray()[label]).getName())).setFont(font);
	}
	
	public boolean isLastStep(){
		return _currentStep==(_steps.size()-1);
	}

	/**
	 * 
	 */
	void init() {
		setLayout(new GridLayout(_steps.size(),1));
		for (Iterator iter = _steps.iterator(); iter.hasNext();) {
			InstallStep step = (InstallStep) iter.next();
			JLabel label=new JLabel(step.getDisplayString());
			label.setToolTipText(step.getShortDescription());
			label.setFont(_normalFont);
			label.setIcon(togoIcon);
			add(label);
			_labels.put(step.getName(),label);
		}
		
		((JLabel)_labels.get(((InstallStep)_steps.toArray()[_currentStep]).getName())).setFont(_currentFont);
	}

	public void addInstallStep(InstallStep step){
		_steps.add(step);
	}
	
	public InstallStep getCurrentStep(){
		return (InstallStep) _steps.toArray()[_currentStep];
	}
	
	public Collection getSteps(){
		return _steps;
	}
}

	
/**
 * Represents a Single install step.<br>
 * Each step is in charge of its panel and conditions.
 * 
 * @author Sebastian Rodriguez - sebastian.rodriguez@utbm.fr
 *
 * @version $Revision: 1.5 $
 */
abstract class InstallStep{
	String _name;
	int _order=0;
	String _shortDescription;
	String _display;
	JPanel _panel=null;
	private JPanel _realPanel;
	private JLabel _warn;
	
	
	public InstallStep(int order, String name, String displayString){
		_order=order;
		_name=name;
		_display=displayString;
		_panel=new JPanel();
		_realPanel=new JPanel();
		_realPanel.setLayout(new BorderLayout());
		_warn=new JLabel();
		_realPanel.add(_warn,BorderLayout.NORTH);
	}
	
	/**
	 * Called When the user clicks on next and the step is going to change
	 */
	public void end() {
		
	}

	protected void setWarningMessage(String msg){
		if(msg==null){
			_warn.setText("");
			_warn.setIcon(null);
			return;
		}
		_warn.setText(msg);
		_warn.setIcon(new ImageIcon(getClass().getResource("/images/error.png")));
	}
	/**
	 * @return
	 */
	public String getName() {
		return _name;
	}

	public abstract void initPanel();
	/**Gets the help url for this Install Step.
	 * @return the help url, or null is not available
	 */
	public URL getHelpURL() {
		return null;
	}

	
	/**Called by the Index where this install Step becomes the current one.
	 */
	public void activate(){}
	
	public final JPanel getPanel(){
		_realPanel.add(_panel,BorderLayout.CENTER);
		return _realPanel;
	}
	
	public boolean isStepFinished(){
		return true;
	}
	
	public String getDisplayString(){
		return _display	;
	}
	
	public String getShortDescription(){
		return _shortDescription;
	}
}

class HelpViewer extends JDialog{
	JEditorPane editor;

	public HelpViewer(){
		super();
		getContentPane().setLayout(new BorderLayout());
		editor=new JEditorPane();
		editor.setEditorKit(new HTMLEditorKit());
		editor.setEditable(false);
		setTitle("Installer Help");
		setSize(new Dimension(300,400));
		JScrollPane scr=new JScrollPane();
		scr.setViewportView(editor);
		getContentPane().add(scr,BorderLayout.CENTER);
		JButton close=new JButton("Close");
		close.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				dispose();
				
			}
			
		});
		getContentPane().add(close,BorderLayout.SOUTH);
		
	}
	
	public void setPage(URL helpURL){
		try {
			editor.setPage(helpURL);
		} catch (IOException e) {
			editor.setText("<html> <h1>No Help Available</h1></html>");
		}
		
	}
}