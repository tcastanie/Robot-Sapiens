package madkit.resbrowser;

import java.util.*;
import java.util.jar.*;
import java.net.URL;

import madkit.boot.*;

public class RessourceBrowser{
	
	public RessourceBrowser(){
	}
	
	public LinkedList browseForRessourceEndWith(String ext){
		if (ext == null) return new LinkedList();
		LinkedList linked = new LinkedList();
		linked.add(ext);
		return browseForRessourceEndWith(linked);
	}
	
	public LinkedList browseForRessourceEndWith(Collection ext){
		LinkedList ressource = new LinkedList();
		if (ext == null) return ressource;

		MadkitClassLoader ucl = Madkit.getClassLoader();
		URL url[] = ucl.getURLs();
		
		for (int i = 0; i < url.length; i ++){
			try{
				JarFile jarFile = new JarFile(url[i].getFile());
				for (Enumeration enum = jarFile.entries() ; enum.hasMoreElements() ;){
					JarEntry entry = (JarEntry) enum.nextElement();
					if (!entry.isDirectory() && endsWith(ext, entry.getName())){
						RessourceInfo ressourceInfo = new RessourceInfo();
						ressourceInfo.setJarName(jarFile.getName());
						ressourceInfo.setResourceName(entry.getName());
						ressource.add(ressourceInfo);
					}
				}
			}catch(Exception e){ e.printStackTrace(System.err); }
		}
		return ressource;
	}
	protected void browseJar(){
		
	}
	protected boolean endsWith(Collection ext, String str){
		Iterator it = ext.iterator();
	    while (it.hasNext()){
	    	Object o = it.next();
	    	if (str.endsWith(o.toString())) return true;
	    }
		return false;
	}
}
