package madkit.desktop2;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import javax.swing.*;
import java.net.URL;

import org.w3c.dom.*;
import org.apache.xml.serialize.*;			// Xerces
import org.apache.xerces.jaxp.*;			// Xerces


import madkit.kernel.*;
import madkit.boot.*;

import SEdit.Formalism;
import SEdit.FormalismAgent;
import SEdit.SEditMessage;
import SEdit.StructureAgent;
import SEdit.XMLFormalism;
import SEdit.XMLStructureLoader;

import com.l2fprod.gui.plaf.skin.SkinLookAndFeel;


public class DesktopAgentGUI implements ActionListener{
	
	public static final String DESKTOPINITFILE = "desktop.ini";
	
	protected AgentInfo ai;
	protected HashMap components;
	protected LinkedList iconInfos, menuInfos, desktopMenuItem;
	protected Desktop desktop;
	protected JMenu menuSelectDesktop;
	protected JMenuItem stop, exec, about;
	protected JMenuItemDesktop currentDesktop;
	protected PropertyDesktop propertyDesktop;
	protected boolean saveIni, saveConfig;
	final protected String defaultConfigPath = System.getProperties().get("madkit.dir")+File.separator+"bin"+File.separator+"configs"+File.separator;
	
	public DesktopAgentGUI(DesktopAgent ag){
		ai = new AgentInfo(ag);
		components = new HashMap();
		iconInfos = new LinkedList();
		menuInfos = new LinkedList();
		desktopMenuItem = new LinkedList();
		propertyDesktop = new PropertyDesktop();
		
		readIniFile();
		createMenuSystem();
		
		if (currentDesktop != null){
			if (!createDesktop(currentDesktop.getClassName()))
				System.exit(0);
		}
		else{
			if (!createDesktop("madkit.desktop2.DesktopWithContainer"))
				System.exit(0);
				
			for (int i = 0 ; i<menuSelectDesktop.getMenuComponentCount() ; i++){
				Component c = menuSelectDesktop.getMenuComponent(i);
				if (c instanceof JMenuItemDesktop){
					if (((JMenuItemDesktop)c).getClassName().equals("madkit.desktop2.DesktopWithContainer"))
						currentDesktop = (JMenuItemDesktop) c;
						currentDesktop.setSelected(true);
				}
			}
		}
	}
	public void init(){
		desktop.show();
//		if (saveConfig)
//		{
			((AbstractMadkitBooter) DesktopBooter.getBooter()).loadConfigFile(new File(defaultConfigPath+"Desktop2Config.cfg"));
//		}
	}
	
	protected void createMenuSystem(){
		menuSelectDesktop = new JMenu("Select your desktop");
		Iterator it = desktopMenuItem.iterator();
		while (it.hasNext()){
			DesktopInfo desktopInfo = (DesktopInfo)it.next();
			JMenuItemDesktop desktopItem;
			
			if (desktopInfo.getIcon() != null && !desktopInfo.getIcon().equals(""))
				desktopItem = new JMenuItemDesktop(desktopInfo.getName(), new ImageIcon(desktopInfo.getIcon()));
			else
				desktopItem = new JMenuItemDesktop(desktopInfo.getName());
			
			desktopItem.setClassName(desktopInfo.getCode());
			desktopItem.setToolTipText(desktopInfo.getDescription());
			desktopItem.addActionListener(this);
			
			if (currentDesktop == null && desktopInfo.getDefault() != null && desktopInfo.getDefault().equalsIgnoreCase("true"))
				currentDesktop = desktopItem;
			
			menuSelectDesktop.add(desktopItem);
		}
		if (currentDesktop != null)
			currentDesktop.setSelected(true);
		
		
		exec = new JMenuItem("Execute script file...");
		exec.addActionListener(this);
		
		about = new JMenuItem("About...");
		about.addActionListener(this);
		
		stop = new JMenuItem("Stop Madkit");
		stop.addActionListener(this);	
	}
	
	public void saveConfigFile(boolean bool){
		if (bool){
			try{
				Document doc = DocumentBuilderFactoryImpl.newInstance().newDocumentBuilder().newDocument();
				Element racine = doc.createElement("config");
				
				Iterator it = components.values().iterator();
			    while (it.hasNext()){
			    	AgentInfo agentInfo = (AgentInfo)it.next();
			    	if (agentInfo != null && agentInfo.getAgent() != null){
			    		Element elt = doc.createElement("launch-agent");
						elt.setAttribute("name", agentInfo.getAgent().getName());
						elt.setAttribute("type", "Java"/*agentInfo.getAgent().getType()*/);
						elt.setAttribute("code", agentInfo.getAgent().getClass().getName());
						elt.setAttribute("X", String.valueOf((int)agentInfo.getLocation().getX()));
						elt.setAttribute("Y", String.valueOf((int)agentInfo.getLocation().getY()));
						elt.setAttribute("height", String.valueOf((int)agentInfo.getSize().getHeight()));
						elt.setAttribute("width", String.valueOf((int)agentInfo.getSize().getWidth()));
						
						racine.appendChild(elt);
			    	}
			    }
				doc.appendChild(racine);
	
				OutputFormat of = new OutputFormat(doc);
				FileWriter writer = new FileWriter(defaultConfigPath+"Desktop2Config.cfg");
				of.setIndenting(true);
				of.setLineSeparator(System.getProperties().get("line.separator").toString());
				XMLSerializer serial = new XMLSerializer(writer, of);
				serial.asDOMSerializer();
				serial.serialize(doc);
	
			}catch(Exception e){ e.printStackTrace(System.err); }
		}
	}
	public void saveIniFile(boolean bool){
		try{
			if (!bool){
				Document doc = DocumentBuilderFactoryImpl.newInstance().newDocumentBuilder().parse(new File(DESKTOPINITFILE));
				Element racine;
				NodeList propertyList;
	
				racine=doc.getDocumentElement();
									
				propertyList = racine.getElementsByTagName("PropertyDesktop");
				if (propertyList.getLength() > 0){
					Element elt = (Element) propertyList.item(0);
					elt.setAttribute("saveIni", Boolean.toString(saveIni));
					elt.setAttribute("saveConfig", Boolean.toString(saveConfig));
				}
				else{
					Element elt = doc.createElement("PropertyDesktop");
					elt.setAttribute("saveIni", Boolean.toString(saveIni));
					elt.setAttribute("saveConfig", Boolean.toString(saveConfig));
					racine.appendChild(elt);
				}
				
				OutputFormat of = new OutputFormat(doc);
				FileWriter writer = new FileWriter(DESKTOPINITFILE);
				of.setIndenting(true);
				of.setLineSeparator(System.getProperties().get("line.separator").toString());
				XMLSerializer serial = new XMLSerializer(writer, of);
				serial.asDOMSerializer();
				serial.serialize(doc);
			}
			else{
				Document doc = DocumentBuilderFactoryImpl.newInstance().newDocumentBuilder().newDocument();
				Element racine = doc.createElement("desktop");
	
				Iterator it = menuInfos.iterator();
				while (it.hasNext()){
					MenuInfo menu = (MenuInfo) it.next();
					Element elt = doc.createElement("StartMenu");
					elt.setAttribute("name", menu.getName());
					elt.setAttribute("icon", menu.getIcon());
	
					Iterator it1 = menu.getMenuItemInfo().iterator();
					while (it1.hasNext()){
						MenuItemInfo mii = (MenuItemInfo) it1.next();
						Element subElt = doc.createElement("button");
						subElt.setAttribute("name", mii.getName());
						subElt.setAttribute("icon", mii.getIcon());
						subElt.setAttribute("description", mii.getDescription());
						subElt.setAttribute("type", mii.getType());
						subElt.setAttribute("code", mii.getCode());
						elt.appendChild(subElt);
					}
					racine.appendChild(elt);
				}
	
				it = iconInfos.iterator();
				while (it.hasNext()){
					IconInfo icon = (IconInfo)it.next();
					Element elt = doc.createElement("DesktopIcon");
					elt.setAttribute("name", icon.getLabel());
					elt.setAttribute("description", icon.getDescription());
					elt.setAttribute("icon", icon.getIcon());
					elt.setAttribute("type", icon.getType());
					elt.setAttribute("code", icon.getCode());
					elt.setAttribute("X", Integer.toString(icon.getX()));
					elt.setAttribute("Y", Integer.toString(icon.getY()));
					racine.appendChild(elt);
				}
				
				Element elt = doc.createElement("PropertyDesktop");
				elt.setAttribute("saveIni", Boolean.toString(saveIni));
				elt.setAttribute("saveConfig", Boolean.toString(saveConfig));
				if (propertyDesktop.getDesktopColor() != null){
					Element color = doc.createElement("BackgroundColor");
					color.setAttribute("R", String.valueOf(propertyDesktop.getDesktopColor().getRed()));
					color.setAttribute("G", String.valueOf(propertyDesktop.getDesktopColor().getGreen()));
					color.setAttribute("B", String.valueOf(propertyDesktop.getDesktopColor().getBlue()));
					elt.appendChild(color);
				}
				if (propertyDesktop.getWallpaperPath() != null && !propertyDesktop.getWallpaperPath().equals("")){
					Element wallpaper = doc.createElement("Wallpaper");
					wallpaper.setAttribute("Path", propertyDesktop.getWallpaperPath());
					wallpaper.setAttribute("Position", propertyDesktop.getWallpaperPos());
					elt.appendChild(wallpaper);
				}	
				Point p = ai.getLocation();
				Dimension d = ai.getSize();
				if ((p.x >= 0) || (p.y >= 0) || (d.width >= 0) && (d.height >= 0)){
					Element dim = doc.createElement("Dimension");
					if (p.x >= 0) dim.setAttribute("X",""+p.x);
					if (p.y >= 0) dim.setAttribute("Y",""+p.y);
					if (d.width >= 0) dim.setAttribute("width",""+d.width);
					if (d.height >= 0) dim.setAttribute("height",""+d.height);
					elt.appendChild(dim);
				}
				
				if (propertyDesktop.getSkinUse()){
					Element skin = doc.createElement("skin");
					skin.setAttribute("use",Boolean.toString(propertyDesktop.getSkinUse()));
					String theme = propertyDesktop.getSkinTheme();
					if (theme == null) theme = "themepack.zip";
					skin.setAttribute("name",theme);
					elt.appendChild(skin);
				}
				
				racine.appendChild(elt);
				
				String defaut = "";
				for (int i = 0 ; i<menuSelectDesktop.getMenuComponentCount() ; i++){
					Component c = menuSelectDesktop.getMenuComponent(i);
					if (currentDesktop == c){
						defaut = currentDesktop.getClassName();
					}
				}
				Element desk = doc.createElement("DesktopType");
				it = desktopMenuItem.iterator();
				while (it.hasNext()){
					DesktopInfo desktopInfo = (DesktopInfo)it.next();
					Element elem = doc.createElement("DesktopClass");
					elem.setAttribute("name", desktopInfo.getName());
					elem.setAttribute("description", desktopInfo.getDescription());
					elem.setAttribute("icon", desktopInfo.getIcon());
					elem.setAttribute("code", desktopInfo.getCode());
				System.out.println(desktopInfo.getCode() + "  " + defaut);
					if (desktopInfo.getCode().equals(defaut))
						elem.setAttribute("default", Boolean.toString(true));
					else
						elem.setAttribute("default", Boolean.toString(false));
					
					desk.appendChild(elem);
				}
				racine.appendChild(desk);
	
				doc.appendChild(racine);
	
				OutputFormat of = new OutputFormat(doc);
				FileWriter writer = new FileWriter(DESKTOPINITFILE);
				of.setIndenting(true);
				of.setLineSeparator(System.getProperties().get("line.separator").toString());
				XMLSerializer serial = new XMLSerializer(writer, of);
				serial.asDOMSerializer();
				serial.serialize(doc);
			}
		}catch(Exception e){ e.printStackTrace(System.err); }
	}
	
	protected void readIniFile(){
		File file = new File(DESKTOPINITFILE);
		//System.err.println(System.getProperties().get("user.dir"));
		//System.err.println(System.getProperties().get("lib.dir"));
		if (! file.exists())
			file = new File(defaultConfigPath+"defaultDesktop.ini");
		if (file.exists()){
			try {
				FileInputStream from = new FileInputStream(file);
				Document doc = DocumentBuilderFactoryImpl.newInstance().newDocumentBuilder().parse(from);
				
				Element racine,e;
				NodeList agentList,buttonlist, iconlist, propertyList, property, desktopList, desktopCList;
	
				racine=doc.getDocumentElement();
				
				agentList=racine.getElementsByTagName("StartMenu");
				for(int i=0; i<agentList.getLength(); i++){
					Element te = (Element) agentList.item(i);
					
					MenuInfo menuInfo = new MenuInfo();
					menuInfo.setIcon(te.getAttribute("icon"));
					menuInfo.setName(te.getAttribute("name"));
					
					buttonlist=te.getElementsByTagName("button");
					for (int j=0;j<buttonlist.getLength(); j++){
						Element elt = (Element) buttonlist.item(j);
						MenuItemInfo menuItemInfo = new MenuItemInfo();
						menuItemInfo.setCode(elt.getAttribute("code"));
						menuItemInfo.setDescription(elt.getAttribute("description"));
						menuItemInfo.setIcon(elt.getAttribute("icon"));
						menuItemInfo.setName(elt.getAttribute("name"));
						menuItemInfo.setType(elt.getAttribute("type"));
						menuItemInfo.setGUI(elt.getAttribute("gui"));
						menuInfo.addMenuItemInfo(menuItemInfo);
					}
					menuInfos.add(menuInfo);
				}
				
				iconlist=racine.getElementsByTagName("DesktopIcon");
				for(int i=0; i<iconlist.getLength(); i++){
					Element elt = (Element) iconlist.item(i);
					
					IconInfo icon = new IconInfo();
					icon.setIcon(elt.getAttribute("icon"));
					icon.setCode(elt.getAttribute("code"));
					icon.setLabel(elt.getAttribute("name"));
					icon.setDescription(elt.getAttribute("description"));
					icon.setType(elt.getAttribute("type"));
					icon.setGUI(elt.getAttribute("gui"));
					
					String str1 = elt.getAttribute("X");
					String str2 = elt.getAttribute("Y");
					int x = 0, y = 0;
					if(str1 != null)
						x = Integer.parseInt(str1);
					if(str2 != null)
						y = Integer.parseInt(str2);
						
					icon.setX(x);
					icon.setY(y);
					
					iconInfos.add(icon);
				}
				
				desktopList = racine.getElementsByTagName("DesktopType");
				if (desktopList.getLength() > 0){
					desktopCList = racine.getElementsByTagName("DesktopClass");
					for(int i=0; i<desktopCList.getLength(); i++){
						Element elt = (Element) desktopCList.item(i);
						
						DesktopInfo desktopInfo = new DesktopInfo();
						desktopInfo.setIcon(elt.getAttribute("icon"));
						desktopInfo.setCode(elt.getAttribute("code"));
						desktopInfo.setName(elt.getAttribute("name"));
						desktopInfo.setDescription(elt.getAttribute("description"));
						desktopInfo.setDefault(elt.getAttribute("default"));
						
						desktopMenuItem.add(desktopInfo);
					}
				}
				
				propertyList = racine.getElementsByTagName("PropertyDesktop");
				if (propertyList.getLength() > 0){
					Element elt = (Element) propertyList.item(0);
					String str;
					str = elt.getAttribute("saveIni");
					if (str == null || str.equals(""))
						saveIni = true;
					else
						saveIni = Boolean.valueOf(str).booleanValue();
					
					str = elt.getAttribute("saveConfig");
					if (str == null || str.equals(""))
						saveConfig = false;
					else
						saveConfig = Boolean.valueOf(str).booleanValue();
					
					
					property = elt.getElementsByTagName("BackgroundColor");
					if (property.getLength() > 0){
						Element color = (Element) property.item(0);
						String r, g, b;
						r = color.getAttribute("R");
						g = color.getAttribute("G");
						b = color.getAttribute("B");
						if (r != null && g != null && b != null)
							propertyDesktop.setDesktopColor(new Color(Integer.parseInt(r), Integer.parseInt(g), Integer.parseInt(b)));
					}
					property = elt.getElementsByTagName("Wallpaper");
					if (property.getLength() > 0){
						Element wallpaper = (Element) property.item(0);
						String path, pos;
						path = wallpaper.getAttribute("Path");
						pos = wallpaper.getAttribute("Position");
						if (pos != null){
							if (pos.equalsIgnoreCase(PropertyDesktop.CENTER))
								propertyDesktop.setWallpaperPos(PropertyDesktop.CENTER);
							else if (pos.equalsIgnoreCase(PropertyDesktop.STRETCH))
								propertyDesktop.setWallpaperPos(PropertyDesktop.STRETCH);
						}
						if (path != null)
							propertyDesktop.setWallpaperPath(path);
					}
					property = elt.getElementsByTagName("Dimension");
					if (property.getLength() > 0){
						int x=-1,y=-1,h=-1,w=-1;
						Element dim = (Element) property.item(0);

						String descX = dim.getAttribute("X");
						String descY = dim.getAttribute("Y");
						String descH = dim.getAttribute("height");
						String descW = dim.getAttribute("width");
						try { x = Integer.parseInt(descX);} catch(NumberFormatException ex){}
						try { y = Integer.parseInt(descY);} catch(NumberFormatException ex){}
						try { h = Integer.parseInt(descH);} catch(NumberFormatException ex){}
						try { w = Integer.parseInt(descW);} catch(NumberFormatException ex){}
						ai.setLocation(new Point(x,y));
						ai.setSize(new Dimension(w,h));
					}// skin management
                    property = racine.getElementsByTagName("skin");
                    if (property.getLength() > 0) {
                        Element skinTe = (Element) property.item(0);
                        boolean skinUse=Boolean.valueOf(skinTe.getAttribute("use")).booleanValue();
                        propertyDesktop.setSkinUse(skinUse);
                        if (skinUse){
                        	String skinTheme = skinTe.getAttribute("name");
                        	propertyDesktop.setSkinTheme(skinTheme);
                        	processSkin(skinTheme);
                        }
                    }
				}
				from.close();
			}
			catch(IOException e){
				System.err.println("File read error with !\n"+file.getName());
			}
			catch(Exception e){
				System.err.println("XML problem !\n"+file.getName());
			}
		}
	}
        
        // must include the Skinlf lib to be compiled...
        void processSkin(String path){
            if ((path == null) || ("".equals(path)))
                return;
            MadkitClassLoader ucl = Madkit.getClassLoader();
            try {
                Class cl = ucl.loadClass("com.l2fprod.gui.plaf.skin.SkinLookAndFeel");
                if (cl != null){
                    System.out.println("Setting skin name: "+ path);
                    String skinDir = Madkit.libDirectory+File.separator+"skins"+File.separator;
                    SkinLookAndFeel.setSkin(SkinLookAndFeel.loadThemePack(skinDir+path));
                    SkinLookAndFeel.enable();
                }
            } catch(ClassNotFoundException ex){
                System.out.println("Cannot change the look and feel of the Desktop. Need the skinlf.jar in libs/support directory");
            }catch(Exception ex){
                System.out.println("Invalid look & feel theme : "+path);
            }
        }

	
	public boolean addListenerTo(MenuItemInfo mii, AbstractButton abstractButton){
		if(mii.getType() != null && mii.getCode() != null){
			if (mii.getType().equalsIgnoreCase("Java")){
				abstractButton.addActionListener(new JavaAgentButton(ai.getAgent(), mii.getCode(), mii.getGUI()));
				return true;
			}
			else if (mii.getType().equalsIgnoreCase("Scheme")){
				abstractButton.addActionListener(new SchemeAgentButton(ai.getAgent(), mii.getCode(), mii.getGUI()));
				return true;
			}
			else if (mii.getType().equalsIgnoreCase("Jess")){
				abstractButton.addActionListener(new JessAgentButton(ai.getAgent(), mii.getCode(), mii.getGUI()));
				return true;
			}
			else if (mii.getType().equalsIgnoreCase("Python")){
				abstractButton.addActionListener(new PythonAgentButton(ai.getAgent(), mii.getCode(), mii.getGUI()));
				return true;
			}
			else if (mii.getType().equalsIgnoreCase("BeanShell")){
				abstractButton.addActionListener(new BeanShellAgentButton(ai.getAgent(), mii.getCode(), mii.getGUI()));
				return true;
			}
			else if (mii.getType().equalsIgnoreCase("SEditFormalism")){
				abstractButton.addActionListener(new SEditFormalismButton(ai.getAgent(), mii.getCode(), mii.getGUI()));
				return true;
			}
			else if (mii.getType().equalsIgnoreCase("SEditFile")){
				abstractButton.addActionListener(new SEditFileButton(ai.getAgent(), mii.getCode(), mii.getGUI()));
				return true;
			}
		}
		return false;
	}
	
	protected void processButton(JMenu menu, Element elt){
		if (elt.getAttribute("type").equalsIgnoreCase("Java")){
			menu.add(
				new JMenuItemForAgent(
					ai.getAgent(),
					elt,
					new JavaAgentButton(ai.getAgent(),elt.getAttribute("code"),elt.getAttribute("gui"))
				));
		}
		else if (elt.getAttribute("type").equalsIgnoreCase("Scheme")){
			menu.add(
				new JMenuItemForAgent(
					ai.getAgent(),
					elt,
					new SchemeAgentButton(ai.getAgent(),elt.getAttribute("code"),elt.getAttribute("gui"))
				));
		}
		else if (elt.getAttribute("type").equalsIgnoreCase("Jess")){
			menu.add(
				new JMenuItemForAgent(
					ai.getAgent(),
					elt,
					new JessAgentButton(ai.getAgent(),elt.getAttribute("code"),elt.getAttribute("gui"))
				));
		}
		else if (elt.getAttribute("type").equalsIgnoreCase("Python")){
			menu.add(
				new JMenuItemForAgent(
					ai.getAgent(),
					elt,
					new PythonAgentButton(ai.getAgent(),elt.getAttribute("code"),elt.getAttribute("gui"))
				));
		}
		else if (elt.getAttribute("type").equalsIgnoreCase("BeanShell")){
			menu.add(
				new JMenuItemForAgent(
					ai.getAgent(),
					elt,
					new BeanShellAgentButton(ai.getAgent(),elt.getAttribute("code"),elt.getAttribute("gui"))
				));
		}
		else if (elt.getAttribute("type").equalsIgnoreCase("SEditFormalism")){
			menu.add(
				new JMenuItemForAgent(
					ai.getAgent(),
					elt,
					new SEditFormalismButton(ai.getAgent(),elt.getAttribute("code"),elt.getAttribute("gui"))
				));
		}
		else if (elt.getAttribute("type").equalsIgnoreCase("SEditFile")){
			menu.add(
				new JMenuItemForAgent(
					ai.getAgent(),
					elt,
					new SEditFileButton(ai.getAgent(),elt.getAttribute("code"),elt.getAttribute("gui"))
				));
		}
	}
	public void addAgent(AbstractAgent ag, Component c, Point p, Dimension d){
		AgentInfo ai = new AgentInfo(ag);
		ai.setLocation(p);
		ai.setSize(d);
		components.put(c, ai);
		desktop.addComponent(ai, c);
		((DesktopBooter)DesktopBooter.getBooter()).registerAgentGUI(ag,c);
	}
	public void removeGUI(Component c){
		desktop.removeComponent(c);
		components.remove(c);
	}
	public boolean quitMadkit(){
		QuitDialog quit;
		quit = new QuitDialog(desktop.getTopComponent(), this);
		
		if (quit.result()==0)
			System.exit(0);
		else if (quit.result() > 0){
			desktop.dispose();
			saveIniFile(saveIni);
			saveConfigFile(saveConfig);
			System.exit(0);
		}
		return false;
	}
	
	public void setSaveIni(boolean bool){ saveIni = bool; }
	public void setSaveConfig(boolean bool){ saveConfig = bool; }
	public boolean getSaveIni(){ return saveIni; }
	public boolean getSaveConfig(){ return saveConfig; }
	
	public void actionPerformed(ActionEvent e){
		Object o = e.getSource();
		if (o == stop){
			quitMadkit();
		}
		else if (o == exec){
			openScriptFile();
		}
		else if (o == about){
			new AboutDialog();
		}
		else if (o instanceof JMenuItemDesktop){
			JMenuItemDesktop itemDesktop = (JMenuItemDesktop)o;
			if (itemDesktop == currentDesktop) currentDesktop.setSelected(true);
			else{
				if (createDesktop(((JMenuItemDesktop)o).getClassName())){
					if (currentDesktop != null){
						currentDesktop.setSelected(false);
					}
					currentDesktop = itemDesktop;
					currentDesktop.setSelected(true);
				}
				else{
					itemDesktop.setSelected(false);
					itemDesktop.setEnabled(false);
				}
				desktop.show();
			}
		}
	}
	
	protected void addMenuSystem(){
		desktop.addMenuSystem(menuSelectDesktop);
		desktop.addMenuSystem(about);
		desktop.addMenuSystem(exec);
		desktop.addMenuSystem(stop);
	}
	
	public void addIcon(IconInfo iconInfo){
		if (iconInfo != null){
			iconInfos.add(iconInfo);
			desktop.addIcon(iconInfo);
		}
	}
	
	public void removeIcon(IconInfo iconInfo){
		if (iconInfo != null){
			iconInfos.remove(iconInfo);
			desktop.removeIcon(iconInfo);
		}
	}
	
	protected boolean createDesktop(String className){
		Object o = null;
		try {
			MadkitClassLoader ucl = Madkit.getClassLoader();
			Class cl = ucl.loadClass(className);
			o = cl.newInstance();
			
			if ((o != null) && (o instanceof Desktop)){
				if (desktop != null) desktop.dispose();
				desktop = (Desktop)o;
				desktop.init(ai, menuInfos /*startMenu*/, this);
				
				addMenuSystem();
				
				if (components != null){
					Iterator it = components.keySet().iterator();
					while (it.hasNext()){
						Component c = (Component)it.next();
						desktop.addComponent((AgentInfo)components.get(c), c);
					}
				}
				if (iconInfos != null){
					Iterator it = iconInfos.iterator();
				    while (it.hasNext()){
				    	Object o2 = it.next();
				    	desktop.addIcon((IconInfo)o2);
				    }
				}
				desktop.setProperty(propertyDesktop);
				return true;
			}
		} catch (ClassNotFoundException ex){
			System.err.println("Desktop class does not exist"+ ex.getMessage());
		} catch (Exception ccex){
			System.err.println("Desktop launch exception:"+ ccex);
		}
		return false;
	}

	protected File openFile(boolean b,final String extens){
		Vector ext=new Vector();
		StringTokenizer st=new StringTokenizer(extens,",");
		while (st.hasMoreTokens()){
			ext.add(st.nextToken());
		}
		final Object[] tabext = ext.toArray();
		JFileChooser fc = new JFileChooser(System.getProperty("user.dir"));
		if (b) fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		if (extens != null)
			fc.setFileFilter(new javax.swing.filechooser.FileFilter() {
				public boolean accept(File f) {
					String fn = f.getName();
					if (f.isDirectory()) return true;
					for (int i=0; i< tabext.length;i++)
						if (fn.endsWith("."+(String) tabext[i]))
							return true;
						return false;
				}
				public String getDescription() { return extens + "files"; }
			});
		File file=null;
		if (fc.showOpenDialog(desktop.getTopComponent()) == JFileChooser.APPROVE_OPTION) {
			file = fc.getSelectedFile();
			return file;
		} else
		return null;
	}

	protected void openScriptFile() {
		File file = openFile(false,AbstractMadkitBooter.ALL_FILES);
		if (file != null){
			((AbstractMadkitBooter) DesktopBooter.getBooter()).loadFile(ai.getAgent(),file);
		}
	}
}

class JMenuItemForAgent extends JMenuItem{
	AbstractAgent ag;
	boolean gui = true;
    
	public JMenuItemForAgent(AbstractAgent _ag, Element elt, ActionListener actionListener){
		super();
		String str;
		str = elt.getAttribute("name");
		if (str == null) str = "---";
		setText(str);
		str = elt.getAttribute("icon");
		try{
			if (str != null) setIcon(new ImageIcon(this.getClass().getResource(str)));
		}catch(Exception e){ System.out.println ("Icon: " + str + " not found"); }

		ag = _ag;
		str = elt.getAttribute("gui");
		if ((str != null) && (str.equalsIgnoreCase("false")))
			this.gui=false;

		addActionListener(actionListener);
    }
}

class AgentInfo{
	AbstractAgent ag;
	Point winPos;
	Dimension winDim;
	
	public AgentInfo(AbstractAgent ag){
		this.ag = ag;
		winPos = new Point(-1,-1);
		winDim = new Dimension(-1,-1);
	}
	
	public AbstractAgent getAgent(){ return ag; }
	public void setAgent(AbstractAgent a){ ag = a; }
	
	public Point getLocation(){ return winPos; }
	public void setLocation(Point p){ winPos.setLocation(p); }
	
	public Dimension getSize(){ return winDim; }
	public void setSize(Dimension d){ winDim.setSize(d); }
	
	public String toString(){
		String str = "agent: ";
		if (ag != null) str += ag.toString();
		else str += "null";
		
		str += "\nwinPos: ";
		if (winPos != null) str += winPos.toString();
		else str += "null";
		
		str += "\nwinDim: ";
		if (winDim != null) str += winDim.toString();
		else str += "null";
		
		return str; 
	}
}

class MenuInfo{
	String icon, name;
	LinkedList menuItemInfo, menuInfo;
	
	public MenuInfo(){
		menuItemInfo = new LinkedList();
		menuInfo = new LinkedList();
	}

	public String getIcon(){ return (this.icon); }
	public void setIcon(String icon){ this.icon = icon; }

	public String getName(){ return (this.name); }
	public void setName(String name){ this.name = name; }
	
	public LinkedList getMenuItemInfo(){ return menuItemInfo; }
	public void addMenuItemInfo(MenuItemInfo mii){
		menuItemInfo.add(mii);
	}
	
	public LinkedList getMenuInfo(){ return menuInfo; }
	public void addMenuInfo(MenuInfo mii){
		menuInfo.add(mii);
	}
}

class MenuItemInfo{
	String icon, name, description, type, code, gui;
	
	public MenuItemInfo(){}

	public String getIcon(){ return (this.icon); }
	public void setIcon(String icon){ this.icon = icon; }

	public String getName(){ return (this.name); }
	public void setName(String name){ this.name = name; }

	public String getDescription(){ return (this.description); }
	public void setDescription(String description){ this.description = description; }

	public String getType(){ return (this.type); }
	public void setType(String type){ this.type = type; }

	public String getCode(){ return (this.code); }
	public void setCode(String code){ this.code = code; }
	
	public String getGUI(){ return (this.gui); }
	public void setGUI(String gui){ this.gui = gui; }
}

class DesktopInfo{
	String icon, name, description, code, defaultDesktop;
	
	public DesktopInfo(){}

	public String getIcon(){ return (this.icon); }
	public void setIcon(String icon){ this.icon = icon; }

	public String getName(){ return (this.name); }
	public void setName(String name){ this.name = name; }

	public String getDescription(){ return (this.description); }
	public void setDescription(String description){ this.description = description; }

	public String getCode(){ return (this.code); }
	public void setCode(String code){ this.code = code; }
	
	public String getDefault(){ return (this.defaultDesktop); }
	public void setDefault(String defaultDesktop){ this.defaultDesktop = defaultDesktop; }
}

class JMenuItemDesktop extends JCheckBoxMenuItem{
	String className;
	
	public JMenuItemDesktop(String text){
		super(text);
	}
	public JMenuItemDesktop(String text, Icon icon){
		super(text, icon);
	}
	
	public void setClassName(String className){ this.className = className; }
	public String getClassName(){ return this.className; }
}

class PropertyDesktop{
	public static final String CENTER = "Center";
	public static final String STRETCH = "Stretch";
	
	Color desktopColor;
	String wallpaperPath;
	String wallpaperPos;
	Point location;
	Dimension size;
        boolean skinUse=false;
        String skinTheme=null;
	
	public PropertyDesktop(){
		wallpaperPos = STRETCH;
		wallpaperPath = "";
		desktopColor = null;
	}

	public Color getDesktopColor(){ return (this.desktopColor); }
	public void setDesktopColor(Color desktopColor){ this.desktopColor = desktopColor; }
	
	public String getWallpaperPath(){ return (this.wallpaperPath); }
	public void setWallpaperPath(String wallpaperPath){ this.wallpaperPath = wallpaperPath; }

	public String getWallpaperPos(){ return (this.wallpaperPos); }
	public void setWallpaperPos(String wallpaperPos){ this.wallpaperPos = wallpaperPos; }	
	
        public void setSkinUse(boolean b){skinUse = b; }
        public boolean getSkinUse(){return skinUse;}
        public void setSkinTheme(String s){skinTheme = s;}
        public String getSkinTheme(){return skinTheme;}
	
}

class QuitDialog extends JDialog implements ActionListener{
	JCheckBox saveIni, saveConfig;
	JButton ok, no,quit;
	DesktopAgentGUI desktop;
	int valid; // -1 = cancel, 0 = quit, 1=save and quit
	
	public QuitDialog(Component owner, DesktopAgentGUI desktop){
		super();
		this.setTitle("Quit MadKit?");
		this.setModal(true);
		
		this.desktop = desktop;
		this.getContentPane().setLayout(new GridLayout(0,1));
		valid = -1;
		
		//JPanel pane = new JPanel();
		saveIni = new JCheckBox("Save the desktop properties");
		saveIni.setSelected(desktop.getSaveIni());
		//pane.add(saveIni);
		this.getContentPane().add(saveIni);
		
		//pane = new JPanel();
		saveConfig = new JCheckBox("Save the desktop configuration");
		saveConfig.setSelected(desktop.getSaveConfig());
		//pane.add(saveConfig);
		this.getContentPane().add(saveConfig);
		
		JPanel pane = new JPanel();
		pane.add(new JLabel("Do you really want to quit ?"));
		this.getContentPane().add(pane);
		
		pane = new JPanel();
		ok = new JButton("Save and quit");
		ok.addActionListener(this);
		pane.add(ok);
		quit = new JButton("Quit without saving");
		quit.addActionListener(this);
		pane.add(quit);
		no = new JButton("Cancel");
		no.addActionListener(this);
		pane.add(no);
		this.getContentPane().add(pane);
		
		pack();
		this.setLocation(owner.getX() + (owner.getWidth() - getWidth())/2 , owner.getY() + (owner.getHeight() - getHeight())/2);

		ok.requestFocus();
		show();
	}
	public int result(){
		if (valid>0){
			desktop.setSaveConfig(saveConfig.isSelected());
			desktop.setSaveIni(saveIni.isSelected());
		}
		return valid;
	}
	
	public void actionPerformed(ActionEvent e){
		Object o = e.getSource();
		if (o == ok){
			valid = 1;
		} else if (o == quit)
			valid = 0;
		this.setVisible(false);
	}
}


class PropertiesDialog extends JDialog {
	PropertiesDialog(JDialog theDialog){
		super(theDialog,"System properties",true);
		String[] columnNames = {"Properties", "Values"};
		String[][] data;
		// getting the properties in order
		Vector props=new Vector();
		Properties p = System.getProperties();
		Iterator iterator = p.keySet().iterator();
		while (iterator.hasNext()) {
			props.add(iterator.next());
		}
		Collections.sort(props);
		data=new String[props.size()][2];
		for(int i=0;i<props.size();i++){
			String name=(String) props.get(i);
			data[i][0]=name;
			data[i][1]=System.getProperty(name);
		}
		// installing the JTable
		getContentPane().setLayout(new BorderLayout());
		JTable propTable = new JTable(data,columnNames);
		JScrollPane scrollPane = new JScrollPane(propTable);
		propTable.setPreferredScrollableViewportSize(new Dimension(400, 400));
		getContentPane().add(scrollPane,BorderLayout.CENTER);
		JButton ok     = new JButton("OK");
		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
		}});
		JPanel buttonPanel=new JPanel();
		buttonPanel.add(ok);
		getContentPane().add(buttonPanel,BorderLayout.SOUTH);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((int)(screenSize.width/2 - 200),(int)(screenSize.height/2 - 200));
		pack();
		show();
	}
}


class AboutDialog extends JDialog
{
  String image = "/images/neomadkit_small.jpg";

  public AboutDialog()
  {
	setTitle("About MadKit");
	this.setModal(true);
	getContentPane().setBackground(Color.gray);
	getContentPane().setLayout(new BoxLayout(getContentPane(),
						 BoxLayout.Y_AXIS));

	URL url = this.getClass().getResource(image);
	ImageIcon madkitPicture = new ImageIcon(url);
	JLabel ta = new JLabel(madkitPicture);
	ta.setAlignmentX(Box.CENTER_ALIGNMENT);

	getContentPane().add(Box.createVerticalStrut(10));
	getContentPane().add(ta);
	getContentPane().add(Box.createVerticalStrut(10));
	JLabel mld = new JLabel();

	mld.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
	mld.setText("<html>\n" +
				"<font color=red size=+1>The MadKit multi-agent platform</font>"+
				"<p><font color=blue>(c) 1997-2002 by Olivier Gutknecht, Jacques Ferber & Fabien Michel</font></p>"+
				"<ul>\n<li><b>Kernel version:</b> "+Kernel.VERSION+
				"<li><b>Desktop version:</b> "+DesktopAgent.VERSION+
				"</ul>\n" );

	mld.setAlignmentX(Box.CENTER_ALIGNMENT);
	getContentPane().add(mld);
	getContentPane().add(Box.createVerticalStrut(10));
	JPanel buttonPanel=new JPanel();
	buttonPanel.setBackground(Color.gray);
	buttonPanel.setAlignmentX(Box.CENTER_ALIGNMENT);
	JButton ok     = new JButton("OK");
	ok.addActionListener(new ActionListener() {
	  public void actionPerformed(ActionEvent e) {
			dispose();
	  }});

	buttonPanel.add(ok);
	JButton props     = new JButton("Properties");
	final AboutDialog about = this;
	props.addActionListener(new ActionListener() {
	  public void actionPerformed(ActionEvent e) {
			new PropertiesDialog(about);
	  }});

	buttonPanel.add(props);


	getContentPane().add(buttonPanel);
	getContentPane().add(Box.createVerticalStrut(10));
	getRootPane().setDefaultButton(ok);
	setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

   // int x = frame.getLocation().x + 30;
	//int y = frame.getLocation().y + 100;
	pack();
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	setLocation((int)(screenSize.width/2 - getSize().getWidth()/2),
		(int)(screenSize.height/2 - getSize().getHeight()/2));

	show();
  }

}


//////////////////////////////////////////////////////////
//Some ActionListener for launch different type of agent//
//////////////////////////////////////////////////////////

abstract class AgentButton implements ActionListener{
	AbstractAgent ag;
	String className;
	boolean gui=true; // by default!!
	AgentButton(AbstractAgent _ag, String className, String gui){
		ag = _ag;
		this.className = className;
		if ((gui != null) && (gui.equalsIgnoreCase("false")))
			this.gui=false;
	}
}

class JavaAgentButton extends AgentButton {

	JavaAgentButton(AbstractAgent _ag, String _className, String gui){
		super(_ag,_className, gui);
	}

	JavaAgentButton(AbstractAgent _ag, String _className){
		super(_ag,_className, null);
	}

	public void actionPerformed(ActionEvent e){
		AbstractAgent a=null;
		try {
			MadkitClassLoader ucl = Madkit.getClassLoader();
			// Class cl = Class.forName(className);
			Class cl = ucl.loadClass(className);
			a= (AbstractAgent) cl.newInstance();

			if ((a != null) && (a instanceof AbstractAgent)){
				String label = DesktopBooter.getAgentLabelFromClassName(a.getClass().getName());
				ag.launchAgent((AbstractAgent) a,label,gui);
			}
		} catch (ClassNotFoundException ex){
			System.err.println("Agent class does not exist"+ ex.getMessage());
		} catch (Exception ccex){
			System.err.println("Agent launch exception:"+ccex);
			// ccex.printStackTrace();
		}
	}
}

class ScriptAgentButton extends AgentButton  {
	String type;
	String typeArg;
	Object arg;
	ScriptAgentButton(AbstractAgent _ag, String type, String className, String typeArg, Object arg, String gui){
		super(_ag,className,gui);
		this.type = type;
		this.typeArg = typeArg;
		this.arg = arg;
	}

	ScriptAgentButton(AbstractAgent _ag, String type, String className, String typeArg, Object arg){
		super(_ag,className,null);
		this.type = type;
		this.typeArg = typeArg;
		this.arg = arg;
	}


	public void actionPerformed(ActionEvent e){
		//System.out.println(":: type: " + type + ", className: " + className + ", typeArg: "+typeArg);
		AbstractMadkitBooter.getBooter().makeScriptAgent(ag,className,typeArg,arg,gui);
	}
}

class PythonAgentButton extends ScriptAgentButton  {
	public PythonAgentButton(AbstractAgent _ag, String filePath, String gui){
		super(_ag,"PythonAgent","madkit.python.PythonAgent","java.lang.String",filePath,gui);
	}
	public PythonAgentButton(AbstractAgent _ag, String filePath){
		super(_ag,"PythonAgent","madkit.python.PythonAgent","java.lang.String",filePath);
	}
}

class BeanShellAgentButton extends ScriptAgentButton  {
	public BeanShellAgentButton(AbstractAgent _ag, String filePath, String gui){
		super(_ag,"BeanShellAgent","madkit.bsh.BeanShellAgent","java.lang.String",filePath,gui);
	}
	public BeanShellAgentButton(AbstractAgent _ag, String filePath){
		super(_ag,"BeanShellAgent","madkit.bsh.BeanShellAgent","java.lang.String",filePath);
	}
}

class SchemeAgentButton extends ScriptAgentButton  {
	SchemeAgentButton(AbstractAgent _ag, String filePath, String gui){
		super(_ag,"SchemeAgent","madkit.scheme.SchemeAgent","java.io.File",new File(System.getProperty("madkit.dir"),filePath),gui);
    }
	SchemeAgentButton(AbstractAgent _ag, String filePath){
		super(_ag,"SchemeAgent","madkit.scheme.SchemeAgent","java.io.File",new File(System.getProperty("madkit.dir"),filePath));
    }
}

class JessAgentButton extends ScriptAgentButton  {

	public void actionPerformed(ActionEvent e){
		try {
			Class cl = Madkit.getClassLoader().loadClass("jess.Rete");
			super.actionPerformed(e);
		} catch (ClassNotFoundException ex){
			System.err.println("WARNING: Jess has not been installed");
			System.err.println("Install the Jess plugin if you want to use it");
			return;
		} catch (Exception ccex){
			System.err.println("Error in instanciating a JessAgent"+ccex);
			// ccex.printStackTrace();
		}
	}
	JessAgentButton(AbstractAgent _ag, String filePath, String gui){
		super(_ag,"JessAgent","madkit.jess.JessAgent","java.lang.String",filePath, gui);
	}
	JessAgentButton(AbstractAgent _ag, String filePath){
		super(_ag,"JessAgent","madkit.jess.JessAgent","java.lang.String",filePath);
	}
}

class SEditFormalismButton extends AgentButton {
	String file;

	SEditFormalismButton(AbstractAgent _ag, String className, String gui){
		super(_ag,className,gui);
		file = className;
	}

	void createStructure(String fileName){
		try {
			Class cl = Class.forName("SEdit.SEditMessage");

			File cwd = new File(System.getProperty("madkit.dir"));
			File formdir = new File(cwd, FormalismAgent.FORMALISM_FOLDER);
			Formalism f;
			XMLFormalism  xf = new XMLFormalism();
			System.out.println(">> loading formalism : " + fileName);
			f = xf.parse(fileName);
			// System.out.println("setbase: " + f.getBase());
			if (f!=null) {
				f.setBase(formdir.getPath()+File.separator);
				StructureAgent sa=new StructureAgent(f);
				ag.launchAgent(sa,f.getName()+" - Untitled",true);
			} else {
				System.err.println(":: ERROR loading formalism : " + fileName);
			}
		}
		catch (ClassNotFoundException ex) {
			System.err.println(
			"SEdit is not properly installed, please install the SEdit plugin");
		}
	}


	public void actionPerformed(ActionEvent ev){
		try {
			createStructure(file);
		} catch (Exception ex){
			System.err.println(":: ERROR invalid formalism : " + file);
		}
	}
}

class SEditFileButton extends AgentButton {
	String file;
	SEditFileButton(AbstractAgent _ag, String className, String gui){
		super(_ag,className,gui);
		file = className;
	}

	public void actionPerformed(ActionEvent ev){
		try {
		Class cl = Class.forName("SEdit.SEditMessage");
		String form = XMLStructureLoader.parseFormalismName(file);

		AgentAddress fs = ag.getAgentWithRole("public","sedit","formalism-server");
		if (fs == null){
			Agent formServer = new FormalismAgent();
			ag.launchAgent(formServer,"Formalizator",false);
			fs = formServer.getAddress();
		}
		ag.sendMessage(fs, new SEditMessage("get", form, file));
		}
		catch (ClassNotFoundException ex) {
			System.err.println(
			"SEdit is not properly installed, please install the SEdit plugin");
		}
		catch (Exception ex) {
			System.err.println(
				":: ERROR invalid SEditFile (check its path and its content) : " +
				file);
		}
	}
}
