package madkit.backgroundProperty;

import java.awt.*;
import java.awt.event.*;
// import java.awt.image.*;
import java.io.File;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

// import madkit.desktop2.*;

public class PropertyPane extends JPanel implements ActionListener{
	JButton apply, ok, cancel;
	PropertyView propertyView;
	PropertyEdit propertyEdit;
	PropertyScreen propertyScreen;
	
	public PropertyPane(PropertyView propertyView, int screenWidth, int screenHeight){
		super(new BorderLayout());
		this.propertyView = propertyView;
		
		JPanel south = new JPanel();
		ok = new JButton("Ok");
		ok.addActionListener(this);
		south.add(ok);
		cancel = new JButton("Cancel");
		cancel.addActionListener(this);
		south.add(cancel);
		apply = new JButton("Apply");
		apply.addActionListener(this);
		apply.setEnabled(false);
		south.add(apply);
		
		add(south, BorderLayout.SOUTH);
		
		JPanel pane = new JPanel(new BorderLayout());
		propertyEdit = new PropertyEdit(this);
		pane.setBorder(BorderFactory.createLineBorder(Color.black, 2));
		if(propertyView.getWallpaperPath() == null || propertyView.getWallpaperPath().equals(""))
			propertyScreen =  new PropertyScreen(this, screenWidth, screenHeight, false);
		else
			propertyScreen =  new PropertyScreen(this, screenWidth, screenHeight, true);
		pane.add(propertyScreen, BorderLayout.CENTER);
		pane.add(propertyEdit, BorderLayout.SOUTH);
		add(pane, BorderLayout.CENTER);
		
		add(new JPanel(), BorderLayout.NORTH);
		add(new JPanel(), BorderLayout.EAST);
		add(new JPanel(), BorderLayout.WEST);
	}
	public Dimension getPreferredSize(){
		return new Dimension(350, 350);
	}
	public void setScreenSize(int width, int height){
		propertyScreen.setScreenSize(width, height);
	}
	protected Color getBackgroundColor(){
		return propertyView.getBackgroundColor();
	}
	protected String getWallpaperPath(){
		return propertyView.getWallpaperPath();
	}
	protected String getWallpaperPos(){
		return propertyView.getWallpaperPos();
	}
	protected void apply(){
		propertyView.setBackgroundColor(propertyEdit.getColor());
		propertyView.setWallpaperPath(propertyEdit.getPath());
		propertyView.setWallpaperPos(propertyEdit.getPos());
	}
	protected void enableApply(){
		if (propertyView.getBackgroundColor().equals(propertyEdit.getColor()) &&
			propertyView.getWallpaperPath().equals(propertyEdit.getPath()) &&
			propertyView.getWallpaperPos().equals(propertyEdit.getPos()))
			apply.setEnabled(false);
		else
			apply.setEnabled(true);
	}
	protected void setColor(Color color){
		propertyEdit.setColor(color);
		propertyScreen.setColor(color);
		enableApply();
	}
	protected void setWallpaper(File f){
		propertyEdit.setWallpaper(f.getPath());
		propertyScreen.setWallpaper(f.getPath());
		enableApply();
	}
	protected void setWallpaperPos(String pos){
		propertyScreen.setWallpaperPos(pos);
		enableApply();
	}
	protected void setUseWallpaper(boolean bool){
		propertyScreen.setUseWallpaper(bool);
		enableApply();
	}
	
	public void actionPerformed(ActionEvent e){
		Object o = e.getSource();
		if (o == ok){
			apply();
			propertyView.closeProperty();
		}
		else if (o == apply){ apply(); enableApply(); }
		else if (o == cancel) propertyView.closeProperty();
	}
}

class PropertyEdit extends JPanel implements ActionListener, ChangeListener{
	PropertyPane propertyPane;
	JCheckBox useWallpaper;
	JTextField wallpaperPath;
	JComboBox wallpaperPos;
	JButton browse, selectColor;
	JPanel pColor;
	
	public PropertyEdit(PropertyPane propertyPane){
		super(new GridLayout(0,1));
		this.propertyPane = propertyPane;
		
		JPanel pane = new JPanel(new BorderLayout());
		JPanel pane2 = new JPanel();
		if (propertyPane.getWallpaperPath() != null && !propertyPane.getWallpaperPath().equals(""))
			useWallpaper = new JCheckBox("Use wallpaper", true);
		else
			useWallpaper = new JCheckBox("Use wallpaper", false);
		useWallpaper.addChangeListener(this);
		pane2.add(useWallpaper);
		String[] strs = {"Center", "Stretch"};
		wallpaperPos = new JComboBox(strs);
		wallpaperPos.setSelectedItem(propertyPane.getWallpaperPos());
		wallpaperPos.addActionListener(this);
		pane2.add(wallpaperPos);
		pane.add(pane2, BorderLayout.CENTER);
		add(pane);
		
		pane = new JPanel(new BorderLayout());
		pane2 = new JPanel();
		wallpaperPath = new JTextField(20);
		if (propertyPane.getWallpaperPath() != null)
			wallpaperPath.setText(propertyPane.getWallpaperPath());
		wallpaperPath.setEditable(false);
		pane2.add(wallpaperPath);
		browse = new JButton("Browse");
		browse.addActionListener(this);
		pane2.add(browse);
		pane.add(pane2, BorderLayout.CENTER);
		add(pane);
		
		pane = new JPanel(new BorderLayout());
		pane2 = new JPanel();
		pane2.add(new JLabel("Backgroud Color :"));
		pColor = new JPanel();
		pColor.setBackground(propertyPane.getBackgroundColor());
		pColor.setPreferredSize(new Dimension(20, 20));
		pColor.setBorder(BorderFactory.createLineBorder(Color.black));
		pane2.add(pColor);
		selectColor = new JButton("Select color");
		selectColor.addActionListener(this);
		pane2.add(selectColor);
		pane.add(pane2, BorderLayout.CENTER);
		add(pane);
	}
	public void setColor(Color color){ pColor.setBackground(color); }
	public void setPos(String pos){ wallpaperPos.setSelectedItem(pos); }
	public void setWallpaper(String path){ wallpaperPath.setText(path); }
	public Color getColor(){ return pColor.getBackground(); }
	public String getPos(){ return wallpaperPos.getSelectedItem().toString(); }
	public String getPath(){
		if (useWallpaper.isSelected())
			return wallpaperPath.getText();
		else return "";
	}
	public void actionPerformed(ActionEvent e){
		Object o = e.getSource();
		if (o == selectColor){
			JColorChooser colorChooser = new JColorChooser();
			
			Color color = colorChooser.showDialog(this, "Please choose the background color", pColor.getBackground());
			if (color != null)
				propertyPane.setColor(color);
		}
		if (o == browse){
			File file = openFile(false, "png,jpg", "Supported image files (*.png;*.jpg)");
			if (file != null){
				propertyPane.setWallpaper(file);
			}
		}
		if (o == wallpaperPos){
			propertyPane.setWallpaperPos(wallpaperPos.getSelectedItem().toString());
		}
	}
	public void stateChanged(ChangeEvent e){
		Object o = e.getSource();
		if (o == useWallpaper){
			propertyPane.setUseWallpaper(useWallpaper.isSelected());
		}
	}
	
	protected File openFile(boolean b,final String extens, final String description){
		Vector ext=new Vector();
		StringTokenizer st=new StringTokenizer(extens,",");
		while (st.hasMoreTokens()){
			ext.add(st.nextToken());
		}
		final Object[] tabext = ext.toArray();
		JFileChooser fc = new JFileChooser(System.getProperty("user.dir"));
		if (b) fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		if (extens != null)
			fc.setFileFilter(new javax.swing.filechooser.FileFilter() {
				public boolean accept(File f) {
					String fn = f.getName();
					if (f.isDirectory()) return true;
					for (int i=0; i< tabext.length;i++)
						if (fn.endsWith("."+(String) tabext[i]))
							return true;
						return false;
				}
				public String getDescription() { return description; }
			});
		File file=null;
		if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			file = fc.getSelectedFile();
			return file;
		} else
		return null;
	}
}

class PropertyScreen extends JPanel{
	PropertyPane propertyPane;
	PreviewScreen previewScreen;
	
	public PropertyScreen(PropertyPane propertyPane, int screenWidth, int screenHeight, boolean useWallpaper){
		super(new BorderLayout());
		this.propertyPane = propertyPane;
		
		previewScreen = new PreviewScreen(propertyPane.getBackgroundColor(), propertyPane.getWallpaperPath(), propertyPane.getWallpaperPos(), screenWidth, screenHeight, useWallpaper);
		add(previewScreen, BorderLayout.CENTER);
	}
	public void setColor(Color color){
		previewScreen.setColor(color);
	}
	public void setWallpaper(String path){
		previewScreen.setWallpaper(path);
	}
	public void setWallpaperPos(String pos){
		previewScreen.setWallpaperPos(pos);
	}
	public void setUseWallpaper(boolean bool){
		previewScreen.setUseWallpaper(bool);
	}
	public void setScreenSize(int width, int height){
		previewScreen.setScreenSize(width, height);
	}
}

class PreviewScreen extends JPanel{
	ImageIcon screen, wallpaperPath;
	Color backgroundColor;
	int screenWidth, screenHeight;
	boolean useWallpaper;
	String wallpaperPos;
	
	public PreviewScreen(Color backgroundColor, String wallpaperPath, String wallpaperPos, int screenWidth, int screenHeight, boolean useWallpaper){
		super();
		screen = new ImageIcon(this.getClass().getResource("/images/icones/screen.png"));
		this.backgroundColor = backgroundColor;
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		this.useWallpaper = useWallpaper;
		this.wallpaperPos = wallpaperPos;
		setWallpaper(wallpaperPath);
	}
	public void setColor(Color color){
		backgroundColor = color;
		repaint();
	}
	public void setUseWallpaper(boolean useWallpaper){
		this.useWallpaper = useWallpaper;
		repaint();
	}
	public void setWallpaper(String path){
		if (path == null || path.equals(""))
			wallpaperPath = null;
		else
			wallpaperPath = new ImageIcon(path);
		repaint();
	}
	public void setWallpaperPos(String wallpaperPos){
		this.wallpaperPos = wallpaperPos;
		repaint();
	}
	public void setScreenSize(int screenWidth, int screenHeight){
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		repaint();
	}
	public void paint(Graphics g){
		super.paint(g);
		Graphics2D g2D = (Graphics2D)g;
		int x, y;
		x = (int)((getSize().getWidth() - screen.getIconWidth()) /2);
		y = (int)((getSize().getHeight() - screen.getIconHeight()) /2);
		g2D.drawImage(screen.getImage(), x, y, null);
		g2D.setColor(backgroundColor);
		g2D.fill(new Rectangle(12 + x, 16 + y, 152, 112));
		
		if (wallpaperPath != null && useWallpaper){
			int X, Y, W, H;
			if (wallpaperPos.equalsIgnoreCase("Center")){
				int i, j;
				i = ((screenWidth - wallpaperPath.getIconWidth()) /2);
				j = ((screenHeight - wallpaperPath.getIconWidth()) /2);
				
				X = 12 + x + (int)(152 * i/screenWidth);
				Y = 16 + y + (int)(112 * j/screenHeight);
				W = (int)(152 * wallpaperPath.getIconWidth()/screenWidth);
				H = (int)(112 * wallpaperPath.getIconWidth()/screenHeight);
			}
			else{
				X = 12 + x;
				Y = 16 + y;
				W = 152;
				H = 112;
			}
			Rectangle rect = g2D.getClipBounds();
			g2D.clipRect(12 + x, 16 + y, 152, 112);
			
			g2D.drawImage(wallpaperPath.getImage(), X, Y, W, H, null);
			
			if (rect != null) g2D.setClip(rect);
			else g2D.setClip(null);
		}
	}
}
