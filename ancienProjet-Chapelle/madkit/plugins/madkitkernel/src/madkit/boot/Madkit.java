/*
* Madkit.java - The starter class of Madkit
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

import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

/**
 * @author Jaco
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
/**
 * @author Jaco
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
/**
 * @author Jaco
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
/** The starter class of MadKit.
 * This class contains only static information and is only used for the ClassLoader and
 * managing CLASSPATH informations...
 */


public class Madkit {


    static boolean debug=false;

    public static final String madkitdirname = "$MADKIT";
    public static final String homedirname = "$HOME";

    public static final String libDirName= "lib";
    public static final String autoloadDir = "autoload";
    public static final String pluginsDirName = "plugins";
    public static final String javaToolsDir = new File(System.getProperty("java.home")).getParent()+File.separator+"lib"+File.separator;
    
    public static final String libDirectory=lookForDirectory(libDirName).getPath();
    public static final String madkitDirectory=new File(libDirectory).getParent();
    
    
    //public final static String libDir=libDirectory;
	//public final static String autoloadDir=madkitDirectory+File.separator+autoloadDir;
	
	
    
    public static MadkitClassLoader madkitClassLoader;
    public static MadkitClassLoader getClassLoader(){return madkitClassLoader;}

public static File lookForDirectory(String dirname)
{
	File currentdir = new File(System.getProperty("user.dir"));
	do 
	{
		File f = new File(currentdir+File.separator+dirname);
		if(f.exists() && f.isDirectory())
			return f;
		currentdir = currentdir.getParentFile();
	}
	while(currentdir != null);
	return null;
}

public static void newMadkitClassLoader(File[] jars){
	MadkitClassLoader oldClassLoader = madkitClassLoader;
	madkitClassLoader = MadkitClassLoader.newMadkitClassLoader(jars,oldClassLoader);
}

/**
 * Used at init time to get all jars that must be in the classpath
 * @return an array of all the URLs (which are normally jar files)
 */
public static URL[] getAllURL()
{
	/*System.err.println("lib.dir= "+libDirectory);
	System.err.println("madkit.dir= "+madkitDirectory);*/
	System.setProperty("madkit.dir",madkitDirectory);
	Vector res = new Vector();
	String pythonPath="";
	addMadkitClassLoader(res, lookForDirectory(libDirName));
	addMadkitClassLoader(res,lookForDirectory(pluginsDirName));
	addMadkitClassLoader(res,lookForDirectory(autoloadDir));
	addMadkitClassLoader(res,new File(javaToolsDir));
	//System.err.println("madkit.dir= "+javaToolsDir);
	System.setProperty("user.dir",madkitDirectory);

	URL[] urls = new URL[res.size()];
	//System.out.println("urls : "+res);
	for (int i=0;i<urls.length;i++){
		urls[i]=(URL) res.elementAt(i);
		pythonPath=pythonPath+File.pathSeparator+((URL) res.elementAt(i)).getFile();
	}
	//System.out.println("pythonPath: " + pythonPath);
	System.setProperty("python.path",pythonPath);
	//System.out.println("classpath: "+System.getProperty("java.class.path"));
	System.setProperty("java.class.path",System.getProperty("java.class.path")+File.pathSeparator+pythonPath);

	return urls;
}

  public static void addMadkitClassLoader(Vector urlList,File dir){
        if (dir != null && dir.isDirectory()){
            String[] contenu=dir.list();
            //System.out.println("dir : "+file.getName());
            for (int i=0;i<contenu.length;i++) {
                File f = new File(dir.getAbsolutePath()+File.separator+contenu[i]);
                if (f.isDirectory()){
                    addMadkitClassLoader(urlList,f);
                } else if (f.getPath().endsWith(".jar")){
                    try {
                        urlList.add(f.toURL());
                    } catch (MalformedURLException e){
                        System.err.println("Error: bad file " + f.getPath());
                    }
                }
            }
        }
  }

    public static void main(String[] args) {
        if (args.length < 1){
            System.err.println("Error: no boot class for booting MadKit");
            System.exit(0);
        }
        String bootClassName=args[0];
        URL[] urls = Madkit.getAllURL();
        madkitClassLoader = new MadkitClassLoader(urls);
        try {
            String[] nargs=new String[args.length-1];
            for (int i=1;i<args.length;i++)
                nargs[i-1]=args[i];
            Class cl = madkitClassLoader.loadClass(bootClassName);
            Class[] types=new Class[]{String[].class};
            //types[0]= Class.forName("java.lang.String[]");
            //types[0]= java.lang.String;
            Object[] arglist=new Object[1];
            arglist[0]=nargs;
            Method mainMeth = cl.getMethod("main",types);
            mainMeth.invoke(cl,arglist);

        }catch(Exception ex){
            System.err.println("Error while booting Madkit " + args[0]);
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }
    }
}