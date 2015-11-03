/*
 * Created on 18 juil. 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package SEdit;

import java.io.*;
import madkit.kernel.*;

/**
 * @author Jaco
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class SEditFileAgentLauncher extends AgentLauncher {

	/* (non-Javadoc)
	 * @see SEdit.FileLauncher#launch()
	 */
	public SEditFileAgentLauncher(){
		super();
	}
	public void launch() {
		String path = ((File) arg).getPath();
		AgentAddress fs = ag.getAgentWithRole("public","sedit","formalism-server");
		if (fs == null){
			Agent formServer = new FormalismAgent();
			ag.launchAgent(formServer,"Formalizator",false);
			fs = formServer.getAddress();
		}
		String form = XMLStructureLoader.parseFormalismName(path);
		ag.sendMessage(fs, new SEditMessage("get",form,path));
	}

}
