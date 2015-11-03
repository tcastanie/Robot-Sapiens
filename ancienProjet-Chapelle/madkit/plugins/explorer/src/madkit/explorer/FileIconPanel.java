/*
 * FileIconPanel.java - a component of the MadKit Explorer Copyright (C)
 * 2000-2003 ABDALLAH WAEL, KHAMMAL Amine, LORCA Xavier, MARTY Christophe,
 * Jacques Ferber
 * 
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 */

package madkit.explorer;

import java.awt.*;
import java.io.*;
import madkit.TreeTools.*;
import madkit.kernel.*;
import madkit.boot.*;
import madkit.utils.common.*;
import java.util.*;
import java.util.jar.*;
import java.lang.reflect.*;

public class FileIconPanel extends IconPanel {

	Explorer explorer;
	ExplorerActionDrag explorerActionDrag;

	public FileIconPanel(AbstractAgent ag, int iconSize, Explorer explorer) {
		super(ag, iconSize);
		this.explorer = explorer;
		explorerActionDrag = new ExplorerActionDrag();
	}

	public void addItem(File file) {
		GenericIconDescriptor desc;
				
		FileIcon icon = null;
		if (file.isDirectory()) {
			if (file.listFiles() == null)
				desc = new DirIconDescriptor(true);
			else
				desc = new DirIconDescriptor(false);
			icon = new FileIcon(ag, file, desc, iconSize, this);
		} else {
			desc = new FileIconDescriptor();
			String extens = FileIconDescriptor.getPathExtens(file.getName());
			if (extens != null) {
				if ((extens.equalsIgnoreCase("class"))
					|| (extens.equalsIgnoreCase("bak"))
					|| (file.getName().endsWith("~"))
					|| (file.getName().endsWith("#")))
					icon = null; // DO NOTHING...
				else if (extens.equalsIgnoreCase("py"))
					icon = new PythonAgentNode(ag, file, desc, iconSize, this);
				else if (extens.equalsIgnoreCase("bsh"))
					icon =
						new BeanShellAgentNode(ag, file, desc, iconSize, this);
				else if (extens.equalsIgnoreCase("scm"))
					icon = new SchemeAgentNode(ag, file, desc, iconSize, this);
				else if (extens.equalsIgnoreCase("clp"))
					icon = new JessAgentNode(ag, file, desc, iconSize, this);
				else if (extens.equalsIgnoreCase("fml"))
					icon = new FormalismNode(ag, file, desc, iconSize, this);
				else if (extens.equalsIgnoreCase("cfg"))
					icon = new ConfigFileNode(ag, file, desc, iconSize, this);
				else if (extens.equalsIgnoreCase("sed"))
					icon = new SEditFileNode(ag, file, desc, iconSize, this);
				else if (extens.equalsIgnoreCase("html") || extens.equalsIgnoreCase("htm"))
					icon = new HTMLFileNode(ag, file, desc, iconSize, this);
				else if (file.getName().endsWith("build.xml"))
					icon = new AntFileNode(ag, file, desc, iconSize, this);
				else if (extens.equalsIgnoreCase("xml") || extens.equalsIgnoreCase("txt") 
							|| extens.equalsIgnoreCase("java"))
					icon = new EditableFileNode(ag, file, desc, iconSize, this);
				else if (extens.equalsIgnoreCase("properties")) {
					//icon = new PropertyFileNode(ag, file, desc, iconSize,
					// this);
					//icon = new FileIcon(ag, file, new
					// DirIconDescriptor(false), iconSize, this);
					//this.add(icon);
					if (!processPropertyFileNode(ag,file,desc,iconSize,this)) {
						icon = new PropertyFileNode(ag,file,desc,iconSize,this);
					} else
						icon = null;
				}

				//				else if (extens.equals("cfg"))
				//					icon = new ConfigFileNode(ag, file,desc,iconSize,this);
				//				else if (extens.equals("jar")){
				//					AbstractFileNode jar = new DirNode(file);
				//					parent.add(jar);
				//					processJarFile(jar,file,gui.getAgent());
				else
					icon = new FileIcon(ag, file, desc, iconSize, this);
			} else
				icon = new FileIcon(ag, file, desc, iconSize, this);
		}
		if (icon != null) {
			this.add(icon);
			this.Nb_Icons++;
			this.setPreferredSize(new Dimension(iconSize, Nb_Icons * iconSize));
			this.repaint();
		}
	}

	public boolean processPropertyFileNode(
		AbstractAgent ag,
		File file,
		GenericIconDescriptor desc,
		int iconSize,
		IconPanel iconPanel) {
		PropertyFile pf = new PropertyFile();
		pf.loadFrom(file);
		String jarName = pf.getProperty("madkit.plugin.name");
		if (jarName != null) {
			jarName = jarName + ".jar";
			String fullJarName = Madkit.libDirectory + File.separator + jarName;
			File jarFile = new File(fullJarName);
			if (!jarFile.exists()) {
				return (false);
			}
			String agentNames = pf.getProperty("madkit.plugin.agents");
			String res=null;
			if (agentNames == null)
				res = null;
			else {
				if (agentNames.equalsIgnoreCase("${agents}") || agentNames.equalsIgnoreCase("all"))
					res = null;
				else if ((agentNames.equalsIgnoreCase("none")) || agentNames.equals(""))
					res = "";
				else res = agentNames;
			}
			processJarFile(ag, jarFile, iconSize, iconPanel, res);
			return true;
		}
		return false;
	}

	// this method should be split into several methods...
	void processJarFile(AbstractAgent ag, File f, int iconSize, IconPanel iconPanel, String agentNames) {
		//System.out.println(":: processing " + f);
		String str = null;
		if (agentNames != null){
			StringTokenizer st = new StringTokenizer(agentNames, " ,:;&\t\n\r\f");
			while (st.hasMoreTokens()) {
				str = st.nextToken();
				int r = str.lastIndexOf(".");
				String smallstr = str;
				if (r != -1)
					smallstr = str.substring(r + 1);
				installAgent(smallstr,str,iconPanel,iconSize,ag);
			}
		} else {
		try {
			JarFile jf = new JarFile(f);
			Manifest mft = jf.getManifest();
			if ((mft != null) && (mft.getEntries().size() > 0)) {
				boolean foundAgent = false;
				Map entries = mft.getEntries();
				//System.out.println("manifest in : " + f + ", size: "+entries.size());
				for (Iterator it = entries.keySet().iterator();it.hasNext();) {
					String key = (String) it.next();
					Attributes attr = (Attributes) entries.get(key);
					String res = attr.getValue("Agent");
					if (res == null)
						res = attr.getValue("Bean");
					//System.out.println(" agent : " + res);
					if ((res != null) && (res.equalsIgnoreCase("true"))) {
						foundAgent = true;
						int k = key.lastIndexOf('.');
						str = key.substring(0, k).replace('/', '.');
						int r = str.lastIndexOf(".");
						String smallstr = str;
						if (r != -1)
							smallstr = str.substring(r + 1);
						installAgent(smallstr,str,iconPanel,iconSize,ag);
					}
				}
				if (foundAgent)
					return;
			}
			// check entries to get AgentClasses
			else {
				for (Enumeration e = jf.entries(); e.hasMoreElements();) {
					JarEntry je = (JarEntry) e.nextElement();
					String jName = je.getName();
					//println(je.toString());
					if (!je.isDirectory()) {
						if (jName.endsWith(".class")) {
							int k = jName.lastIndexOf('.');
							str = jName.substring(0, k).replace('/', '.');
							int r = str.lastIndexOf(".");
							String smallstr = str;
							if (r != -1)
								smallstr = str.substring(r + 1);

							// Class c = Class.forName(str);
							//System.out.println("loading: " + str);
							installAgent(smallstr,str,iconPanel,iconSize,ag);
						}
					}
				}
			}
		} catch (IOException ex) {
			System.err.println("Not a good jar file : " + f.getName());
		} catch (Exception ex) {
			System.err.println(
				"Error while loading class : "
					+ str
					+ " check its default constructor");
		}
	  }
	}

	void installAgent(String smallstr,String str,IconPanel parent,int iconSize,AbstractAgent ag) {
		// System.out.println("installing?: "+str);
		try {
			Class c = Utils.loadClass(str);
			if (Utils.loadClass("madkit.kernel.AbstractAgent").isAssignableFrom(c)) {
				Class[] params = new Class[0];
				Constructor def = c.getConstructor(params);
				if (def == null)
					System.err.println("WARNING: class "+ str+ " does not have default constructor");
				else {
					parent.add(new JavaAgentNode(ag, iconSize, parent, smallstr, str));
					//	 parent.add(new JavaAgentNode(smallstr,str));
				}
			}
		} catch (ClassNotFoundException ex) {
			System.err.println("Error : class not found : " + str);
		} catch (NoSuchMethodException ex) {
			System.err.println("Error : default constructor of class "+ str + " not found : ");
		} catch (Exception ex) {
			System.err.print("Error while loading class : "+ str + " error: "+ex);
		} catch (Throwable ex) {
			System.err.println("Error : class "+ str + " contains dependencies which are not in the classpath");
		}
	}

	public void doubleClick(AWTEvent e) {

		FileIcon icon = (FileIcon) (e.getSource());
		File file = icon.getFile();
		String iconName = icon.getName();
		//System.out.println("FileIconPanel : "+file+" "+iconName);
		if (file.isDirectory()) {
			if (file.listFiles() == null)
				return;
			String absolutePath = explorer.getAbsolutePath();
			explorer.setAbsolutePath(absolutePath + File.separator + iconName);
			if (explorer.readDown() == false) {
				explorer.setAbsolutePath(absolutePath);
			}
			//System.out.println("ABSOLUTE PATH :
			// "+explorer.getAbsolutePath());
			//System.out.println("REPERTOIRE");
		} else {
			icon.execute();
			//System.out.println("FICHIER");
		}
	}

//	rajout brd

		public void simpleClick(AWTEvent e)
		{
			FileIcon icon = (FileIcon)e.getSource();
			explorerActionDrag.execIcon(explorer, icon);
		
		}
//fin	rajout brd
}
