/*
 * Created on 18 juil. 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package SEdit;

import java.io.File;

import madkit.kernel.*;
import java.io.*;

/**
 * @author Jaco
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class FormalismFileLauncher extends AgentLauncher {

	/**
	 * @param f
	 * @param ag
	 */
	public FormalismFileLauncher() {
		super();
	}

	/* (non-Javadoc)
	 * @see SEdit.FileLauncher#launch()
	 */
	public void launch() {
		// super.launch(f,ag);
		if (arg != null){
			String path = ((File)arg).getPath();
			File cwd = new File(System.getProperty("madkit.dir"));
			File formdir = new File(cwd, FormalismAgent.FORMALISM_FOLDER);
			Formalism form;
			XMLFormalism  xf = new XMLFormalism();
			System.out.println(">> loading formalism : " + path);
			try {
				form = xf.parse(path);
				form.setBase(formdir.getPath()+File.separator);
			// System.out.println("setbase: " + f.getBase());
				StructureAgent sa=new StructureAgent(form);
				ag.launchAgent(sa,form.getName()+" - Untitled",true);
			}catch (Exception ex){
				System.err.println(":: ERROR loading formalism : "+arg);
			}
		} else {
				System.err.println(":: ERROR loading null formalism ");
		}
	}

}
