package madkit.imageviewer;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

import madkit.resbrowser.*;


public class ImageViewer extends JPanel{
	Images images;
	
	public ImageViewer(){
		super(new BorderLayout());
		images = new Images(this);
		JScrollPane sp = new JScrollPane(images);
		add(sp, "Center");
	}
	public Dimension getPreferredSize(){return new Dimension(500, 400);}
	public RessourceInfo getSelectedRessource(){ return images.getSelectedRessource(); }
	public Dimension getIconDimension(){ return images.getIconDimension(); }
	public void addChangeListener(ChangeListener e){ images.addChangeListener(e); }
	public void removeChangeListener(ChangeListener e){ images.removeChangeListener(e); }
	public void setSelectedIcon(String icone){ images.setSelectedIcon(icone); }
	public void scrollToCurrentMiniature(){ images.scrollToCurrentMiniature(); }
}

class Images extends JPanel implements MouseListener, KeyListener{
	protected LinkedList listMiniature, changeListeners;
	protected Miniature currentMiniature;
	protected int margeX, margeY, iconSize;
	protected ImageViewer imageViewer;
	
	public Images(ImageViewer imageViewer){
		super(new BorderLayout());
		this.addMouseListener(this);
		this.imageViewer = imageViewer;
		LinkedList list = new LinkedList();
		list.add(".png");
		list.add(".gif");
		list.add(".jpg");
		init(list, 96);
	}
	
	public Images(ImageViewer imageViewer, LinkedList ext, int iconSize){
		super(new BorderLayout());
		this.imageViewer = imageViewer;
		init(ext, iconSize);
	}
	
	public boolean isFocusable(){return true;}
	
	protected void init(LinkedList ext, int iconSize){
		this.setBackground(Color.white);
		this.iconSize = iconSize;
		this.addKeyListener(this);
		
		listMiniature = new LinkedList();
		changeListeners = new LinkedList();
		margeX = 10;
		margeY = 10;
		
		RessourceBrowser ressourceBrowser = new RessourceBrowser();
		LinkedList ressource = ressourceBrowser.browseForRessourceEndWith(ext);
		
		Iterator it = ressource.iterator();
	    while (it.hasNext()){
	    	RessourceInfo ressourceInfo = (RessourceInfo) it.next();
	    	listMiniature.add(new Miniature(ressourceInfo, iconSize));
	    }
	}
	public Dimension getPreferredSize(){
		int nbIconW = (int)(getSize().getWidth()/(iconSize+2*margeX));
		if (nbIconW < 1) nbIconW = 1;
		
		return new Dimension(iconSize+ 2*margeX, (int)(Math.ceil((float)listMiniature.size() / (float)nbIconW) * (iconSize+ 2*margeY)));
	}
	public void paint(Graphics g){
		super.paint(g);
		
		int nbIconW = (int)(getSize().getWidth()/(iconSize+2*margeX));
		if (nbIconW < 1) nbIconW = 1;
		
		int x = margeX;
		int y = margeY;
		int i = 0;
		
		Iterator it = listMiniature.iterator();
		while (it.hasNext()){
			Miniature miniature = (Miniature) it.next();
			boolean bool;
			if (miniature == currentMiniature)
				bool = g.hitClip(x-1, y-1, iconSize+2, iconSize+2);
			else
				bool = g.hitClip(x, y, iconSize, iconSize);
			if(bool){
				miniature.paint(g, x, y);
			}
			if ((i + 1) % nbIconW == 0){
				x = margeX;
				y += (int)(margeY*2 + iconSize);
			}
			else
				x += (int)(margeX*2 + iconSize);
			i++;
		}
	}
	
	public RessourceInfo getSelectedRessource(){
		if (currentMiniature != null)
			return currentMiniature.getRessource();
		else return null;
	}
	public Dimension getIconDimension(){ return currentMiniature.getIconDimension(); }
	
	public void mouseClicked(MouseEvent e){}
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
	public void mousePressed(MouseEvent e){
		if (currentMiniature != null){
			currentMiniature.setSelected(false);
			currentMiniature = null;
		}
		int x = e.getX();
		int T1 = 2*margeX + iconSize;
		int CX = (int)((float)x/(float)T1);
		if ( ((CX*T1 + margeX) < x) && (x < ((CX+1)*T1 - margeX))){
			int y = e.getY();
			int T2 = 2*margeY + iconSize;
			int CY = (int)((float)y/(float)T2);
			if ( ((CY*T2 + margeY) < y) && (y < ((CY+1)*T2 - margeY))){
				int nbIconW = (int)(getSize().getWidth()/(iconSize+2*margeX));
				if (nbIconW < 1) nbIconW = 1;
				int i = (CY)*(nbIconW) + CX;
				if (i < listMiniature.size()){
					currentMiniature = ((Miniature)listMiniature.get(i));
					currentMiniature.setSelected(true);
				}
			}
		}
		repaint();
		onChange();
	}
	protected void onChange(){
		Iterator it = changeListeners.iterator();
	    while (it.hasNext()){
	    	((ChangeListener) it.next()).stateChanged(new ChangeEvent(imageViewer));
	    }
	}
	public void mouseReleased(MouseEvent e){}
	
	public void addChangeListener(ChangeListener e){ changeListeners.add(e); }
	public void removeChangeListener(ChangeListener e){ changeListeners.remove(e); }
	
	public void setSelectedIcon(String icone){
		Iterator it = listMiniature.iterator();
		while (it.hasNext()){
			Miniature miniature = (Miniature) it.next();
			String str = miniature.getRessource().getResourceName();
			if (!str.startsWith("/")) str = "/" + str;
			if (str.equals(icone)){
				if (currentMiniature != null){
					currentMiniature.setSelected(false);
					currentMiniature = null;
				}
				currentMiniature = miniature;
				miniature.setSelected(true);
				if (getSize().getWidth() > 0){
					scrollToCurrentMiniature();
				}
				onChange();
				return;
			}
		}
	}
	public void scrollToCurrentMiniature(){
		if (currentMiniature != null){
			int y, h;
			int nbIconW = (int)(getSize().getWidth()/(iconSize+2*margeX));
			if (nbIconW < 1) nbIconW = 1;
			
			h = 2*margeY + iconSize;
			y = (int)((float)listMiniature.indexOf(currentMiniature)/(float)nbIconW)*h;
			
			this.scrollRectToVisible(new Rectangle(0, y, 1, h));
		}
	}
	public void keyPressed(KeyEvent e){
		int code = e.getKeyCode();
		int i = listMiniature.indexOf(currentMiniature);
		int nbIconW = (int)(getSize().getWidth()/(iconSize+2*margeX));
		if (nbIconW < 1) nbIconW = 1;
		
		if (currentMiniature == null && (code == KeyEvent.VK_DOWN || code == KeyEvent.VK_LEFT || code == KeyEvent.VK_RIGHT || code == KeyEvent.VK_UP)){
			currentMiniature = (Miniature) listMiniature.getFirst();
			if (currentMiniature != null){
				currentMiniature.setSelected(true);
				scrollToCurrentMiniature();
				repaint();
				onChange();
			}
		}
		else if (currentMiniature != null){
			if (code == KeyEvent.VK_DOWN){
				currentMiniature.setSelected(false);
				if (i + nbIconW < listMiniature.size() - 1){
					currentMiniature = (Miniature) listMiniature.get(i + nbIconW);
				}
				else{
					currentMiniature = (Miniature) listMiniature.get(listMiniature.size() - 1);
				}
				currentMiniature.setSelected(true);
				scrollToCurrentMiniature();
				repaint();
				onChange();
			}
			else if (code == KeyEvent.VK_UP){
				currentMiniature.setSelected(false);
				if (i - nbIconW > 0){
					currentMiniature = (Miniature) listMiniature.get(i - nbIconW);
				}
				else{
					currentMiniature = (Miniature) listMiniature.get(0);
				}
				currentMiniature.setSelected(true);
				scrollToCurrentMiniature();
				repaint();
				onChange();
			}
			else if (code == KeyEvent.VK_LEFT){
				if (i > 0){
					currentMiniature.setSelected(false);
					currentMiniature = (Miniature) listMiniature.get(i - 1);
					currentMiniature.setSelected(true);
					scrollToCurrentMiniature();
					repaint();
					onChange();
				}
			}
			else if (code == KeyEvent.VK_RIGHT){
				if (i < listMiniature.size() - 1){
					currentMiniature.setSelected(false);
					currentMiniature = (Miniature) listMiniature.get(i + 1);
					currentMiniature.setSelected(true);
					scrollToCurrentMiniature();
					repaint();
					onChange();
				}
			}
		}
	}
	public void keyReleased(KeyEvent e){}
	public void keyTyped(KeyEvent e){}
}

class Miniature{
	protected int size, imageX, imageY, imageWidth, imageHeight;
	protected ImageIcon image;
	protected Color rectColor, rectSelectedColor;
	protected RessourceInfo ressourceInfo;
	protected boolean selected, loaded;
	
	public Miniature(RessourceInfo res, int size){
		this.ressourceInfo = res;
		this.size = size;
		rectColor = Color.black;
		rectSelectedColor = Color.red;
		selected = false;
		loaded = false;
//		try{
//			String str = ressourceInfo.getResourceName();
//			if (!str.startsWith("/")) str = "/" + str;
//			image = new ImageIcon(this.getClass().getResource(str));
//		}catch(Exception e){ image = null; }
	}
	public void paint(Graphics g, int x, int y){
		Graphics2D g2D = (Graphics2D)g;
		
		if (!loaded){
			try{
				String str = ressourceInfo.getResourceName();
				if (!str.startsWith("/")) str = "/" + str;
				image = new ImageIcon(this.getClass().getResource(str));
			}catch(Exception e){ image = null; }
			setIconSize(size);
			loaded = true;
		}
		if (image != null) 
			g2D.drawImage(image.getImage(), x + imageX, y + imageY, imageWidth, imageHeight, null);
		
		if (selected){
			g2D.setColor(rectSelectedColor);
			g2D.draw(new Rectangle(x-1, y-1, size+1, size+1));
		}
		g2D.setColor(rectColor);
		g2D.draw(new Rectangle(x, y, size-1, size-1));
	}
	public void setIconSize(int s){
		if (image != null){
			if ((s < image.getIconWidth() - 2) || (s < image.getIconHeight() - 2)){
				if(image.getIconHeight() > image.getIconWidth()){
					imageWidth = (s-2) * image.getIconWidth() / image.getIconHeight();
					imageHeight = s-2;
					
					imageX = 1 + (s - imageWidth)/2;
					imageY = 1;
				}
				else{
					imageHeight = (s-2) * image.getIconHeight() / image.getIconWidth();
					imageWidth = s-2;
					
					imageX = 1;
					imageY = 1 + (s - imageHeight)/2;
				}
			}
			else{
				imageHeight = image.getIconHeight();
				imageWidth = image.getIconWidth();
					
				imageX = 1 + (s - imageWidth)/2;
				imageY = 1 + (s - imageHeight)/2;
			}
		}
		this.size = s;
	}
	public Dimension getIconDimension(){
		if (image != null)
			return new Dimension(image.getIconWidth(), image.getIconHeight());
		return null;
	}
	public int getIconSize(){ return size; }
	public RessourceInfo getRessource(){ return ressourceInfo; }
	public void setSelected(boolean bool){
		if (!loaded){
			try{
				String str = ressourceInfo.getResourceName();
				if (!str.startsWith("/")) str = "/" + str;
				image = new ImageIcon(this.getClass().getResource(str));
			}catch(Exception e){ image = null; }
			setIconSize(size);
			loaded = true;
		}
		selected = bool;
	}
}
