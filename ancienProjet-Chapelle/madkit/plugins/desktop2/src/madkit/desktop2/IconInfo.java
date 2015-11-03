package madkit.desktop2;

public class IconInfo{
	String icon, label, description, type, code, gui;
	int X, Y;
	
	public IconInfo(){
		X = 0;
		Y = 0;
	}

	public String getIcon(){ return (this.icon); }
	public void setIcon(String icon){ this.icon = icon; }

	public String getLabel(){ return (this.label); }
	public void setLabel(String label){ this.label = label; }

	public String getDescription(){ return (this.description); }
	public void setDescription(String description){ this.description = description; }

	public int getX(){ return (this.X); }
	public void setX(int X){ this.X = X; }

	public int getY(){ return (this.Y); }
	public void setY(int Y){ this.Y = Y; }

	public String getType(){ return (this.type); }
	public void setType(String type){ this.type = type; }

	public String getCode(){ return (this.code); }
	public void setCode(String code){ this.code = code; }
	
	public String getGUI(){ return (this.gui); }
	public void setGUI(String gui){ this.gui = gui; }
}
