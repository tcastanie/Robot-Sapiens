package madkit.desktop2;

import java.awt.*;
import java.util.*;
import javax.swing.*;


public class DesktopFullscreen extends DesktopMDI{
	protected JDialog dialog;
	
	public DesktopFullscreen(){
		super();
	}
	
	public void init(AgentInfo ai, LinkedList menuInfos, DesktopAgentGUI desktopAgentGUI){
		super.init(ai, menuInfos, desktopAgentGUI);
		
		dialog = new JDialog();
		dialog.setModal(true);
		dialog.setUndecorated(true);
		
		dialog.getContentPane().setLayout(new BorderLayout());
		dialog.getContentPane().add(desktop, BorderLayout.CENTER);
		
		dialog.getContentPane().add(bar, BorderLayout.SOUTH);
		dBar.setOrientation(startMenu.getOrientation());
		
		dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		dialog.addWindowListener(this);
	}

	public void dispose(){
		super.dispose();
		dialog.dispose();
	}
	public Component getTopComponent(){ return dialog; }
	
	public boolean setBarOrientation(String orientation){
		startMenu.setOrientation(orientation);
		dialog.remove(bar);
		dialog.getContentPane().add(bar, orientation);
		return true;
	}
	public void show(){
		dialog.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		dialog.setVisible(true);
	}
}
