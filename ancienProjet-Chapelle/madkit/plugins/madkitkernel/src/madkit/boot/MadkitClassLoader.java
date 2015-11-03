/*
* MadkitClassLoader.java - Kernel: the kernel of MadKit
* Copyright (C) 1998-2002 Olivier Gutknecht, Fabien Michel, Jacques Ferber
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
package madkit.boot;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.zip.*;
import java.util.jar.*;

public class MadkitClassLoader extends URLClassLoader {

    public MadkitClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }

    public MadkitClassLoader(URL[] urls) {
        super(urls);
    }
    
    static public MadkitClassLoader newMadkitClassLoader(File[] jars, ClassLoader parent){
		MadkitClassLoader loader= new MadkitClassLoader(new URL[0],parent);
		for (int i=0;i<jars.length;i++) {
			File f = jars[i];
			if (f.getPath().endsWith(".jar")){ 
				try {
					// System.out.println("## MadkitLoader: (re)loading "+f);
					loader.addURL(f.toURL());
					JarFile jfile = new JarFile(f);
					for (Enumeration e=jfile.entries();e.hasMoreElements();){
						ZipEntry entry = (ZipEntry) e.nextElement();
						if (!entry.isDirectory()){
								String fileName = entry.getName();
								if (fileName.endsWith(".class")){
									fileName = fileName.replace('/', '.');
									fileName = fileName.substring(0,fileName.length()-6);
									// System.out.println("## adding new class: "+fileName);
									loader.addClassToReload(fileName);
								}
						}
					}
				} catch (MalformedURLException e){
					System.err.println("Error: bad jar file " + f.getPath());
				}	catch (IOException e){
					System.err.println("Error: not a jar file " + f.getPath());
				}
			}
		}
		return loader;
    }

    // add only a new URL
    public void addURL(URL url){
        URL[] urls=this.getURLs();
        for(int i=0;i<urls.length;i++){
            if (urls[i].equals(url))
                return;
        }
        super.addURL(url);
        System.out.println("adding "+url.getFile()+" to classpath");
    }

    public void addDir(File dir){
        Vector urlList = addDir0(new Vector(),dir);
        for(Iterator it=urlList.iterator();it.hasNext();){
            URL url=(URL) it.next();
            super.addURL(url);
        }
    }

    private Vector addDir0(Vector urlList, File dir){
        if (dir.isDirectory()){
            String[] contenu=dir.list();
            //System.out.println("dir : "+file.getName());
            for (int i=0;i<contenu.length;i++) {
                File f = new File(dir.getPath()+File.separator+contenu[i]);
                if (f.isDirectory()){
                    addDir0(urlList,f);
                } else if (f.getPath().endsWith(".jar")){
                    try {
                        urlList.add(f.toURL());
                    } catch (MalformedURLException e){
                        System.err.println("Error: bad jar file " + f.getPath());
                    }
                }
            }
        }
        return urlList;
    }
    
    void reinitizalize(){
    	URL[] urls = this.getURLs();
    	for(int i=0;i<urls.length;i++){
    		
    	}
    }


	/* (non-Javadoc)
	 * @see java.lang.ClassLoader#findClass(java.lang.String)
	 */
	protected Class findClass(String name) throws ClassNotFoundException {
	//	System.out.println("loadclass: "+name);
		return super.findClass(name);
	}
	
	/**
	 * Contains the list of the class which have been modified
	 */
	private Vector classes=new Vector();
	
	public void addClassToReload(String name){
		classes.add(name);
	}
	
	
	protected synchronized Class loadClass(String name, boolean resolve)
		throws ClassNotFoundException {
		
		if (classes.contains(name)){
			// System.out.println("## newly (re)loaded class: "+name);
			Class c = findClass(name);
			if (c == null)
				return(super.loadClass(name,resolve));
			else {
				if (resolve) {
					resolveClass(c);
				}
				classes.remove(name);
				return c;
			}
		} else
			return super.loadClass(name,resolve);
	}
}
