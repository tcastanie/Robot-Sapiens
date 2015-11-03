package madkit.share;
import java.awt.Color;
import java.awt.Event;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Panel;
import java.awt.Toolkit;
import java.net.URL;

public class Icon extends Panel {
	
    Image Icone;
    private String NameIcon, PathIcon;
    private int posx, posy, sizex, sizey;
    private boolean selected;
    URL url;

	
	
    Icon(String text, String textpath, int taille_x, int taille_y, boolean selec) {
		
	NameIcon=text;        // Nom du fichier
	PathIcon=textpath;    // Chemin (path) pour acceder au fichier
	posx=0;               // Coordonnee x du coin superieur gauche de l'image : 0 par defaut
	// Modifiable par la fonction move
	posy=0;               // Coordonnee y du coin superieur gauche de l'image : 0 par defaut
	// Modifiable par la fonction move
	sizex=taille_x;       // Taille en x du pannel dans lequel est affichee l'image
	sizey=taille_y;       // Taille en y du pannel dans lequel est affichee l'image
	selected=selec;       // Indique si l'objet a ete selectionne par un click souris
 
	//super.setSize(sizex,sizey);
 		
 	//On associe une image à l'extension du fichier	
	int Long = NameIcon.length(), ind = NameIcon.lastIndexOf('.');
	String ext = (ind == -1)?"":NameIcon.substring(ind, Long).toLowerCase();
 		
	if (NameIcon.equals("..")) {
	    url = this.getClass().getResource("/images/share/left.png");
	    if (url != null) Icone = Toolkit.getDefaultToolkit().getImage(url);
	    return;
  	}
  	
	if (NameIcon.charAt(Long-1) == java.io.File.separatorChar) {
	    url = this.getClass().getResource("/images/share/folder.png");
	    if (url != null) Icone = Toolkit.getDefaultToolkit().getImage(url);
	    return ;
  	}
  	
  	if (ext.equals(".jpg") || ext.equals(".jpeg") || ext.equals(".gif") || ext.equals(".png") ) {
	    url = this.getClass().getResource("/images/share/image.png");
	    if (url != null) Icone = Toolkit.getDefaultToolkit().getImage(url);
	    return ;
    	}
	if (ext.equals(".exec") || ext.equals(".exe") ) {
	    url = this.getClass().getResource("/images/share/windows.png");
	    if (url != null) Icone = Toolkit.getDefaultToolkit().getImage(url);
	    return ;
    	}
    	if (ext.equals(".java") ) {
	     url = this.getClass().getResource("/images/share/text.png");
	    if (url != null) Icone = Toolkit.getDefaultToolkit().getImage(url);
	    return ;
    	}
    		
	if (ext.equals(".bmp") || ext.equals(".tif") || ext.equals(".tiff")) {
	    url = this.getClass().getResource("/images/share/image.png");
	    if (url != null) Icone = Toolkit.getDefaultToolkit().getImage(url);
	    return;
	}	
  
	if (ext.equals(".doc")) {
	    url = this.getClass().getResource("/images/share/doc.png");
	    if (url != null) Icone = Toolkit.getDefaultToolkit().getImage(url);
	    return;
	}
	if (ext.equals(".txt")) {
	    url = this.getClass().getResource("/images/share/text.png");
	    if (url != null) Icone = Toolkit.getDefaultToolkit().getImage(url);
	    return;
	}

	if (ext.equals(".zip") || ext.equals(".tar") || ext.equals(".gz") || ext.equals(".tgz")) {
	     url = this.getClass().getResource("/images/share/zip.png");
	    if (url != null) Icone = Toolkit.getDefaultToolkit().getImage(url);
	    return;
	}	
  
	if (ext.equals(".mp3")) {
	    url = this.getClass().getResource("/images/share/mp3.png");
	    if (url != null) Icone = Toolkit.getDefaultToolkit().getImage(url);
	    return;
	}
  
	if (ext.equals(".mpeg") || ext.equals(".avi") ) {
	    url = this.getClass().getResource("/images/share/movie.png");
	    if (url != null) Icone = Toolkit.getDefaultToolkit().getImage(url);
	    return;
	}
  
	 url = this.getClass().getResource("/images/share/binary.png");
	 if (url != null) Icone = Toolkit.getDefaultToolkit().getImage(url);
    }

    static Icon LastSelected = null;


    void AfficheName(String name, Graphics g)
    {
	// Methode AfficheName:
	// Cette fonction affiche le nom du fichier sous son icone. 
	// Si le nom est trop long et qu'il ne rentre pas dans les dimension sizex et sizey
	// la fin du nom est tronquee et on lui rajoute 3 points de suspension
	int taille;
	taille=(g.getFontMetrics()).stringWidth(name);

	if (taille > sizex) {
	    if (this.selected == true) {
		g.fillRect((sizex-taille-2)/2, sizey-45, taille+2, 14);
		g.setColor(Color.white);
	    }
	    else
		g.setColor(Color.black);
	    g.drawString( (name.substring(0,(sizey/8-1))+"...") , 3, sizey-35); 
	} else {
	    if (this.selected == true) {
		g.fillRect((sizex-taille-2)/2, sizey-45, taille+2, 14);
		g.setColor(Color.white);
	    }
	    else
		g.setColor(Color.black);
	    g.drawString(name, (sizex-taille)/2, sizey-35);
	}
  
    }


    public void paint(Graphics g)
    {
   
	g.drawImage(Icone, (sizex-30)/2, 5, 30, 30, this);
	AfficheName(NameIcon,g);
      

    }
  
  
    public String getName(){
	return NameIcon;
    }


    public boolean mouseDown(Event e, int x, int y) {
	// Methode mouseDown :
	// Lorsque on clique, on repere a quel element cela correspond dans la liste
  
	e.target=Icone;
	e.arg = NameIcon;
	if (this.selected == false) {
	    if (LastSelected != null) {
		LastSelected.selected=false;
		LastSelected.repaint();
	    }
	    LastSelected=this;
	    LastSelected.selected=true;
	    LastSelected.repaint();
	} else {
	    this.selected = false;
	    this.repaint();
	    LastSelected = null;
	    e.arg = null;
	}
	return super.mouseDown(e,x,y);
    }




}
