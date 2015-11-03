/*
 * Created on 27 nov. 2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package madkit.explorer;

import java.io.*;

import madkit.TreeTools.GenericIconDescriptor;
import madkit.kernel.AbstractAgent;
import madkit.boot.Madkit;
import org.apache.tools.ant.*;
import java.util.Set;
import java.util.Vector;
import javax.swing.JOptionPane;


/**
 * @author ferber
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class AntFileNode extends EditableFileNode {

	/**
	 * @param ag
	 * @param file
	 * @param desc
	 * @param iconSize
	 * @param iconPanel
	 */
	Project project;
	
	public AntFileNode(
		AbstractAgent ag,
		File file,
		GenericIconDescriptor desc,
		int iconSize,
		IconPanel iconPanel) {
		super(ag, file, desc, iconSize, iconPanel);

		BuildLogger logger = new DefaultLogger();
	   logger.setMessageOutputLevel(Project.MSG_INFO);
	   logger.setOutputPrintStream(System.out);
	   logger.setErrorPrintStream(System.err);
	   //logger.setEmacsMode(emacsMode);
//		Main.main(new String[]{"-file",file.getAbsolutePath()});
		// Runtime.getRuntime().exec("ant",null,file.getParentFile());
		project = new Project();
		project.setCoreLoader(Madkit.getClassLoader());
	    project.addBuildListener(logger);
		project.init();
		project.setUserProperty("ant.version",Main.getAntVersion());
		project.setUserProperty("ant.file",file.getAbsolutePath());
		ProjectHelper.configureProject(project,file);
		Set keys = project.getTargets().keySet();
		String madkitProject = project.getProperty("string.madkit");
		String systemProject = project.getProperty("system.plugin");
		Vector vKeys=new Vector(keys);
		int nKeys=0; 
		String[][] commands = null;
		if ((madkitProject == null) || (!"true".equalsIgnoreCase(systemProject))){
			nKeys= keys.size(); 
			commands = new String[4+nKeys][2];
			commands[0][0]="ant build (default)"; commands[0][1]="execute";
			for (int i=0;i<nKeys;i++){
				commands[i+4][0]="Ant target: "+(String) vKeys.get(i);
				commands[i+4][1]=(String) vKeys.get(i);
			}
		} else {
			commands = new String[4][2];
			commands[0][0]="ant build (default)"; commands[0][1]="execute";
		}
		commands[1][0]="edit with AgentEditor"; commands[1][1]="edit";
		commands[2][0]="edit with JSynEdit"; commands[2][1]="JSynEdit";
		commands[3][0]="properties"; commands[3][1]="info";
		if (madkitProject == null)
			this.setDescriptor(new AgentNodeDescriptor("/images/ant_buildfile.gif",commands));
		else if ("true".equalsIgnoreCase(systemProject))
	//		this.setDescriptor(new AgentNodeDescriptor("/images/agents/agent_ant_systembuild.gif",commands));
			this.setDescriptor(new AgentNodeDescriptor("/images/ant_nobuildfile.gif",commands));
		else
	//		this.setDescriptor(new AgentNodeDescriptor("/images/agents/agent_ant_buildfile.gif",commands));
			this.setDescriptor(new AgentNodeDescriptor("/images/ant_buildfile.gif",commands));
		
	}
	
//	static AgentNodeDescriptor antFileNodeDescriptor =
//			new AgentNodeDescriptor("/images/ant_buildfile.gif",
//				new String[][]{ // commands
//						{"ant build (default)","execute"},
//						{"edit with AgentEditor","edit"},
//						{"properties", "info"}
//					});
	
	public void execute(){

		String madkitProject = project.getProperty("string.madkit");
		String systemProject = project.getProperty("system.plugin");
		if ((madkitProject == null) || (!"true".equalsIgnoreCase(systemProject))){
			try
				{
					System.out.println("Running Ant..");
					project.executeTarget(project.getDefaultTarget());
					reloadJar();
					System.out.println("BUILD SUCCESSFUL");
				}
				catch(Exception e)
				{
					System.out.println("BUILD FAILED: \n"+e);
				}
		}
		else {
			JOptionPane.showMessageDialog(null,  "For security reasons, MadKit cannot execute this buildfile");
		}
	 }
	
	public void command(String command){
		try
		{
			project.executeTarget(command);
			if (project.getDefaultTarget().equalsIgnoreCase(command))
				reloadJar();
			System.out.println("BUILD SUCCESSFUL");
		}
		catch(Exception e)
		{
			System.out.println("BUILD FAILED: \n"+e);
		}
	}
	
	void reloadJar(){
		String madkitProject = project.getProperty("string.madkit");
		if (madkitProject != null){
			String jarName = project.getProperty("jar.name");
			//System.out.println("Reloading jar: "+jarName);
			String jarPath = Madkit.libDirectory+File.separator+jarName;
			File jarFile = new File(jarPath);
			if (jarFile.isFile()){
				File[] fileLst = new File[1];
				fileLst[0]=jarFile;
				Madkit.newMadkitClassLoader(fileLst);
			}
		}
	}
	
}


