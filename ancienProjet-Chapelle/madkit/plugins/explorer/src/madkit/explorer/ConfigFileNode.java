/*
 * Created on 29 nov. 2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package madkit.explorer;

import java.io.File;

import madkit.TreeTools.GenericIconDescriptor;
import madkit.kernel.*;

/**
 * @author ferber
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ConfigFileNode extends EditableFileNode {

	static AgentNodeDescriptor configFileNodeDescriptor =
			   new AgentNodeDescriptor("/images/fileicons/config2.gif",
				   new String[][]{ // commands
						   {"execute","execute"},
						   {"edit with AgentEditor","edit"},
						   {"edit with JSynEdit","JSynEdit"},
						   {"rename file", "rename"},
						   {"delete file", "delete"},
						   {"properties", "info"}
					   });
	/**
	 * @param ag
	 * @param file
	 * @param desc
	 * @param iconSize
	 * @param iconPanel
	 */
	
	public ConfigFileNode(
		AbstractAgent ag,
		File file,
		GenericIconDescriptor desc,
		int iconSize,
		IconPanel iconPanel) {
		super(ag, file, desc, iconSize, iconPanel);
		this.setDescriptor(configFileNodeDescriptor);
	}
	
	public void execute(){
		AbstractMadkitBooter.getBooter().loadFile(ag,file);
	}

}
