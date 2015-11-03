package madkit.desktop2;

import java.awt.*;
import java.io.*;

import madkit.kernel.*;

//import org.w3c.dom.Document;
//import org.w3c.dom.Element;
//import org.w3c.dom.NodeList;
//
//import org.apache.xerces.jaxp.*;			// Xerces

public class DesktopBooter extends AbstractMadkitBooter{
	static DesktopAgent desktop = null;
	Point location;
	Dimension size;

	static public void setDesktopAgent(DesktopAgent a){
		desktop = a;
	}
	
	static public void main(String argv[]){
		bootProcess(argv);
		DesktopBooter boot = new DesktopBooter(graphics, ipnumeric, initFile, ipaddress,network);
	}
    
	protected DesktopBooter(boolean isg, boolean ipnumeric, String initFile, String ipaddress, boolean network){
		super(isg,ipnumeric,initFile,ipaddress,network);
	}
	
	protected void init(boolean isg, boolean ipnumeric, String initFile, String ipaddress, boolean network){
		initialAgentClass="madkit.desktop2.DesktopAgent";
		initialAgentName="MadkitDesktop";
		// readDimensions();

		super.init(isg,ipnumeric,initFile,ipaddress,network);

		theKernel.setOutputStream(new OutputStreamWriter(System.err));
   }
   
	public void setupGUI(AbstractAgent a, Point p, Dimension d){
		if (desktop == null){
			a.initGUI();
			guis.put(a, a.getGUIObject());
		}
		else {
			a.initGUI();
			Object gui = a.getGUIObject();
			if (gui != null){
				desktop.addAgent(a,(Component)gui,p,d);
			}/* else
				desktop.addDefaultGUI(a);*/
		}
	}
	
	public void disposeGUI(AbstractAgent a){
		Object f = guis.get(a);
		if (f != null){
			if (f instanceof DesktopAgentGUI){
				((DesktopAgentGUI)f).quitMadkit();
			}
			else if (f instanceof Component){
				desktop.removeGUI((Component) f);
				guis.remove(a);
			}
		}
	}
	public void disposeGUIImmediatly(AbstractAgent a){
		disposeGUI(a);
	}
	
	protected void registerAgentGUI(AbstractAgent a, Component c){
		guis.put(a,c);
	}
	
}