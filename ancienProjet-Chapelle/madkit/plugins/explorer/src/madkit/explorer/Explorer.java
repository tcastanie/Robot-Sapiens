/* Explorer.java: a generic File Explorer
* 
* Copyright (C) 2003 ABDALLAH WAEL, KHAMMAL Amine,
*		     LORCA Xavier, MARTY Christophe, FERBER Jacques
*
* This program is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License
* as published by the Free Software Foundation; either version 2
* of the License, or any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
*/
package madkit.explorer;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import madkit.kernel.AbstractAgent;

public class Explorer extends JPanel {

	JPanel contentPane;
	FileIconPanel icons;
	String path, absolutePath;
	boolean hidden;
	AbstractAgent ag;
	JTextField display;

	public Explorer(AbstractAgent ag, String path, int iconSize, JTextField display) {
		super(new BorderLayout());
		this.ag = ag;
		this.path = path;
		this.hidden = false;
		this.icons = new FileIconPanel(ag, iconSize, this);
		this.display = display;
		JScrollPane scroller = new JScrollPane(icons);
//

		add(scroller, BorderLayout.CENTER);
		path();
	}

	public Dimension getPreferredSize() {
		return new Dimension(380, 180);
	}

	public void path() {
		File tmp = new File(path);
		try {
			tmp = tmp.getCanonicalFile();
		} catch (IOException e) {
			System.out.println("Couldn't get Canonical File");
		}
		absolutePath = tmp.getAbsolutePath();
		//System.out.println("ABSOLUTEPATH : "+absolutePath);
		read();
	}

	public boolean read() {
//		System.out.println("ABSOLUTEPATH : "+absolutePath);
		File file = new File(absolutePath);
		if (display != null){
			display.setText(absolutePath);
		}
		if (file == null)
			return false;
		if (file.isDirectory()) {
			icons.clear();
			Object[] files =
				file.listFiles(new Explorer_hiddenFiles_filterAdapter(this));
			if (files == null)
				return false;
			Arrays.sort(files, new Explorer_name_sortAdapter(this));
			Arrays.sort(files, new Explorer_directory_sortAdapter(this));
			for (int i = 0; i < files.length; i++) {
				//System.out.println("METHODE READ dans EXPLORER.JAVA : "+files[i]);
				icons.addItem((File) files[i]);
			}
			this.validate();

			return true;
		} else
			return false;
	}


	public int directorySort(Object o1, Object o2) {
		boolean b1 = ((File) o1).isDirectory();
		boolean b2 = ((File) o2).isDirectory();
		if (b1 && b2)
			return 0;
		if (!b1 && !b2)
			return 0;
		if (b1)
			return -1;
		return 1;
	}

	public int nameSort(Object o1, Object o2) {
		String s1 = ((File) o1).getName();
		String s2 = ((File) o2).getName();
		return s1.compareToIgnoreCase(s2);
	}

	public boolean hiddenFilesFilter(File file) {
		if (hidden)
			return true;
		return !file.isHidden();
	}

	public void setHidden(boolean bool) {
		hidden = bool;
	}
	public boolean getHidden() {
		return hidden;
	}

	public boolean readUp() {
		File file = new File(absolutePath);
//		System.out.println(" explorer readup " + absolutePath);
		String parent = file.getParent();
//		System.out.println("Explorer parent = " + parent);
		if (parent != null) {
			absolutePath = parent;
			return read();
		}
		return false;
	}

	public boolean readDown() {
		return read();
	}

	public String getParent(String file) {
		String tmp = file.substring(0, file.lastIndexOf(File.separatorChar));
		return tmp.substring(0, tmp.lastIndexOf(File.separator) + 1);
	}

	public String getAbsolutePath() {
		return absolutePath;
	}

	public void setAbsolutePath(String absolutePath) {
		this.absolutePath = absolutePath;
	}
}

class Explorer_directory_sortAdapter implements Comparator {
	Explorer adaptee;
	Explorer_directory_sortAdapter(Explorer adaptee) {
		this.adaptee = adaptee;
	}
	public int compare(Object o1, Object o2) {
		return adaptee.directorySort(o1, o2);
	}
}

class Explorer_name_sortAdapter implements Comparator {
	Explorer adaptee;
	Explorer_name_sortAdapter(Explorer adaptee) {
		this.adaptee = adaptee;
	}
	public int compare(Object o1, Object o2) {
		return adaptee.nameSort(o1, o2);
	}
}

class Explorer_hiddenFiles_filterAdapter implements FileFilter {
	Explorer adaptee;
	Explorer_hiddenFiles_filterAdapter(Explorer adaptee) {
		this.adaptee = adaptee;
	}
	public boolean accept(File file) {
		return adaptee.hiddenFilesFilter(file);
	}
}
