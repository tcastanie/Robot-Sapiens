package madkit.desktop2;

import java.awt.*;
import java.util.*;
import javax.swing.*;

//import madkit.desktopicon.*;

public interface Desktop{
	public void addIcon(IconInfo iconInfo);
	public void addComponent(AgentInfo _ai, Component c);
	public void addMenuSystem(JMenuItem menuSystem);
	public void dispose();
	public int getHeight();
	public Component getTopComponent();
	public int getWidth();
	public void init(AgentInfo ai, LinkedList menuInfos, DesktopAgentGUI desktopAgentGUI);
	public void removeComponent(Component c);
	public void removeIcon(IconInfo iconInfo);
	public void setProperty(PropertyDesktop propertyDesktop);
	public void show();
	public void toFront(Component c);
	public void toBack(Component c);
}