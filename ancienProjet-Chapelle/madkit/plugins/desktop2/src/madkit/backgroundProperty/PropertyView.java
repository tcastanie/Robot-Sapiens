package madkit.backgroundProperty;

import java.awt.*;

public interface PropertyView{
	public Color getBackgroundColor();
	public String getWallpaperPath();
	public String getWallpaperPos();
	
	public void setBackgroundColor(Color color);
	public void setWallpaperPath(String path);
	public void setWallpaperPos(String pos);
	
	public void closeProperty();
}
