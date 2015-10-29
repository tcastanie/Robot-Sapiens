// Copyright (C) 1997 by MadKit Team
package kernelsim;

import madkit.kernel.*;
//import smaapp.*;
//import java.io.*;
//import java.util.*;
import java.awt.*;
//import madkit.simulation.activators.*;

import java.awt.event.*;
/** A DriveViewer is used to monitor the drive values<br>
* (fr: Classe qui est utilisée pour observer les valeurs des motivations (drives))
* @author Jérôme Chapelle 2004
*/
public class DriveViewer extends Watcher  implements ReferenceableAgent
{
    /* propre à DriveViewer */
    //int Rnum=1;
    //int oldRnum;
	final static long serialVersionUID=0; // Warning du compilo
	
    int nbdrive=10;
    int viewerwidth,viewerheight;
    int sensorwidth,sensorheight;
    boolean running=true;
    NeuralAgent[] theDrives; // tableau des actionneurs
    int xmargin,ymargin,ylegendmargin;
    
    Font StandardFont,SmallFont=null;
    
    
    
    //AgentAddress[] v=null;
    VueMeterMemory[] VueMeterMemories;
    
    DriveCanvas onScreen;
        
    public DriveProbeApp detector=null;
    RobotSchedulerApp sch;
    String neuronGroup;

    //** var pour affichages dans simu **/
	
    boolean flash = false;
    boolean show = true;
    
    boolean bardraw = false;
    boolean graphdraw = true;

    
    boolean RainbowColorModel = false;
    boolean GreenMajentaColorModel = true;    
    boolean GrayColorModel = false;
    boolean Smiley = true;
    /*
    public void setRobot (int r){this.Rnum=r;}
    public int getRobot(){return Rnum;}
	*/
    
    public void setSmiley (boolean add){Smiley = add;}
    public boolean getSmiley(){return Smiley;}
    
    public void setBar (boolean add){bardraw = add;graphdraw=!add;}
    public boolean getBar(){return bardraw;}
    public void setGraph (boolean add){bardraw = !add;graphdraw=add;}
    public boolean getGraph(){return graphdraw;}
    
    public void setRainbow(boolean add)
    { RainbowColorModel=true;
      GreenMajentaColorModel = false;
      GrayColorModel = false;}
    public boolean getRainbow()
    { return RainbowColorModel;}
    public void setGreenMajenta(boolean add)
    { RainbowColorModel=false;
      GreenMajentaColorModel = true;
      GrayColorModel = false;}
    public boolean getGreenMajenta()
    { return GreenMajentaColorModel;}
    public void setGray(boolean add)
    { RainbowColorModel=false;
      GreenMajentaColorModel = false;
      GrayColorModel = true;}    
    public boolean getGray()
    { return GrayColorModel;}


    //public int bord,h,l;
    
    /** Choisis une couleur en fonction de la valeur value. La couleur choisie dépends du modèle de couleur (Gray/MagentaGreen/Rainbow) **/
    public Color getColor(double value)
    {	Color c;
    	if (value<0) value=0;
    	if (value>1) value=1;
    	if (RainbowColorModel==true)
    	{
    		c = new Color (Color.HSBtoRGB((float)value,(float)1,(float)1));
    	}
    	else
    	{
    		if (GreenMajentaColorModel == true)
    	     	{
    	     		c = new Color((int)(value*255),255-(int)(value*255),140);
    	     	}
    	     	else // if (GrayColorModel == true)
    	     	{
    	     		c = new Color ((int)(255-(value*255)),(int)(255-(value*255)),(int)(255-(value*255)));
    	     	}
    	}
    	return c;
      
    //new Color (Color.HSBtoRGB((float)valutemp,(float)1,(float)0.9));
    }
    
    
    public void initValues()
    {
    	viewerwidth=400;
	viewerheight=100;
	
	//if (detector!=null) {removeProbe(detector);}
	//detector2 = new ActorProbeApp("NeuronGroup"+Rnum,"actor");
	//addProbe(detector2);
	if (detector!=null)
	{
	 System.out.println("InitValues() = group:role = "+detector.getGroup()+":"+detector.getRole());
	 
	}
    	
    	xmargin=10;
    	ymargin=10;
    	ylegendmargin=40;
    	if (nbdrive!=0) {
    		sensorwidth=(viewerwidth-(2*xmargin))/nbdrive;
    		VueMeterMemories= new VueMeterMemory[nbdrive];
    		for (int i=0;i<nbdrive;i++)
    		{	// En mode graphe, on mets autant de mémoire que de pixels à afficher
    			if (graphdraw == true) VueMeterMemories[i]=new VueMeterMemory(sensorwidth);
    			// Sinon, en mode "barres" on a besoin de mem une seule valeur
    			else VueMeterMemories[i]=new VueMeterMemory(1);
    		}
    		
    	 	}
    	else { sensorwidth=1;}
    	sensorheight=(viewerheight-(2*ymargin)-ylegendmargin);
    }
    
    public DriveViewer(String ngroup, RobotSchedulerApp s)
    {
	sch=s;
	neuronGroup=ngroup;
	//oldRnum=Rnum;
	initValues();

	//visibleSize = new Dimension(dimx,dimy);
    }

    public void sendEvent(ActionEvent e)
    {
	System.out.println("Lancer cette commandeSV:sE:");
	System.out.println(e.getActionCommand());
	
	String lacommande=e.getActionCommand();
	
// --------------------- Ligne 1 -----------------------------------------------
	if (lacommande.equals("runsimulation"))
	{ //ClearMap(); 
		//setRun(true);
	}
	
	/*
	if (lacommande.equals("")) 
	{ 
	}
	*/
	
    }

    public void initGUI()
    {
	//setBean(onScreen = new GridCanvas2DApp(dimx,dimy,this)); < deprecated
	onScreen = new DriveCanvas(viewerwidth,viewerheight,this);
	setGUIObject(onScreen);
    }

	/*
    public Dimension getVisibleSize()
    {
	return visibleSize;
    }*/


    public void activate()
    {
	detector = new DriveProbeApp(neuronGroup,"drive");
	addProbe(detector);
	
	theDrives= detector.getDrives();
	nbdrive = Trier(theDrives); //theDrives.length;
	//NeuralAgent[] theDrivesBis=
	
	requestRole(neuronGroup,"sensor observer",null);
	//requestRole(neuronGroup, "member", null);
	
    }
	
	/** Trie les differents capteurs en les rangeant par ordre
	alphabetique sur leur labels, renvoie le nb de capteurs**/
	public int Trier(NeuralAgent[] vin)
	{	
		// Tri naïf (par inversion)
		int len=vin.length;
		if ((vin==null)||(len<1)) return 0;
		
		NeuralAgent agtTemp= null;
		for (int i=0;i<len;i++)
		{
			agtTemp=vin[i];
				
			for (int j=i+1;j<len;j++)
			{
				int tempvalue=((vin[i]).getLabel()).compareTo((vin[j]).getLabel());
				//System.out.println("Trie : "+((vin[i]).getLabel())+" comp "+(vin[j]).getLabel()+" = "+tempvalue);	
				if (tempvalue>0)
				{
					//System.out.println("Inversion !");
					vin[i]=vin[j];
					vin[j]=agtTemp;
					agtTemp=vin[i];
					//System.out.println("==> "+((vin[i]).getLabel())+" <= "+(vin[j]).getLabel());	
				} // if (tempvalue>0)
			} // for (int j=i+1;j<len;j++)
		} // for (int i=0;i<len;i++)
		return len;
	}
	
	/** Dessine le visualiseur de capteur **/
	public void paintEnv(Graphics g)
	{	
		//pause(100);
		g.setColor(Color.white);
		g.fillRect(0,0,viewerwidth,viewerheight); // /2 => pour test
		g.setColor(Color.black);
		g.drawString("n "+neuronGroup+" ("+nbdrive+")", 10,10);
		//paintImmediatly(0,0,dimx,dimy);
		//system.out.println("SW.paintEnv(g)");
		double vtemp; // valeur temp
		String ltemp=null; // label temporaire
		if (theDrives!=null)
		{
			NeuralAgent agtAddrTemp= null;
			//System.out.println("Il y a "+theDrives.length+" drive");
			for (int i=0; (i < theDrives.length) ; i++)
			{	//System.out.print(i+" ");
				ltemp=null;
				agtAddrTemp = theDrives[i];
				if (i<theDrives.length)
					{vtemp=(agtAddrTemp).getLastValue();
					 ltemp=(agtAddrTemp).getLabel();
					}
				else
					{vtemp=((double)i)/theDrives.length;}
				VueMeterMemories[i].putValue(vtemp);
				drawVueMeter(g,vtemp,i,sensorwidth,sensorheight,ltemp);
				if (Smiley==true)
				{
					if ((ltemp!=null) &&(ltemp.compareTo("DrvMan")==0))
					{	int xstart = xmargin+(i*sensorwidth);
						int ystart=ymargin+((sensorheight-sensorwidth)/2);
						g.setColor(Color.yellow);
						g.fillOval(xstart,ystart,sensorwidth,sensorwidth);
						g.setColor(Color.black);
						g.drawOval(xstart,ystart,sensorwidth,sensorwidth);
						g.fillOval(xstart+(sensorwidth/4),ystart+(sensorwidth/4),3,3);
						g.fillOval(xstart+(sensorwidth*3/4),ystart+(sensorwidth/4),3,3);
						
						double coef; int y,decaly;
						Point coingauche,coindroite,millieubouche;
						decaly=(int)(vtemp*(sensorwidth*1/4));
						millieubouche=new Point(xstart+(sensorwidth/2),ystart+(sensorwidth/2)+decaly);
						coingauche=new Point(xstart+(sensorwidth*2/8),ystart+(sensorwidth*3/4)-decaly);
						coindroite=new Point(xstart+(sensorwidth*6/8),ystart+(sensorwidth*3/4)-decaly);
						
						for (int x=coingauche.x;x<=coindroite.x;x++)
						{
							if (x<millieubouche.x)
							{	coef=				 	
								((double)( x - coingauche.x )) /   // Distance du coin gche au point courrant
								((double)( millieubouche.x - coingauche.x ));    // Distance du coin gche au milieu 
								coef=coef*(1+(1-coef));
								y=(int)(((1-coef)*coingauche.y)+((coef)*millieubouche.y));
								g.fillOval(x-1,y-1,2,2);
							}
							else
							{	coef=				 	
								((double)( coindroite.x - x )) /   // Distance du coin drte au point courrant
								((double)( coindroite.x - millieubouche.x));    // Distance du coin drte au milieu 
								coef=coef*(1+(1-coef));
								y=(int)(((1-coef)*coindroite.y)+((coef)*millieubouche.y));
								g.fillOval(x-1,y-1,2,2);
							}
						} // for (int x=coingauche.x;x<=coindroite.x;x++)
					} // if ((ltemp!=null) &&(ltemp.compareTo("DrvMan")==0))
				} // if (Smiley==true)
				
			}
		}
		if (theDrives.length==0)
		{
			g.setColor(Color.red);
			g.drawString("No drive detected for NeuronGroup \""+neuronGroup+"\" !", 10,30);
		}
	}

	/** Dessine un VueMeter **/
	public void drawVueMeter(Graphics g,double vumeter_value,int vumeter_nb,int vumeter_larg,int vumeter_haut, String label)
	{
		int xstart,ystart,xend,yend,hauteur;
		double dhauteur=vumeter_haut*vumeter_value;
		hauteur = (int)dhauteur;
		if (hauteur>vumeter_haut) {hauteur=vumeter_haut;}
		
		xstart=xmargin+(vumeter_larg*vumeter_nb);
		ystart=ymargin+(vumeter_haut-hauteur);
		xend=xmargin+(vumeter_larg*(vumeter_nb+1));
		yend=ymargin+vumeter_haut;
		Color vuecolor;
		if (bardraw==true)
		{	vuecolor = getColor (vumeter_value);
			//new Color((int)(vumeter_value*255),255-(int)(vumeter_value*255),140);
			g.setColor(vuecolor);
			g.fillRect(xstart,ystart,xend-xstart,yend-ystart);
		}
		else // <=> if (graphdraw==true)
		{	double moyenne=0,maxvalue=0; int nbeltmoy=0; 
		
			for (int xind=xstart;xind<=xend;xind++)
			{	
				double valutemp=VueMeterMemories[vumeter_nb].getLastValue(xind-xstart);
				nbeltmoy++;moyenne+=(valutemp/nbeltmoy);
				if (valutemp>maxvalue) {maxvalue=valutemp;}
				dhauteur=vumeter_haut*valutemp;
				hauteur = (int)dhauteur;
				if (hauteur>vumeter_haut) {hauteur=vumeter_haut;}
				yend=ymargin+vumeter_haut;
				ystart=ymargin+(vumeter_haut-hauteur);
				
				vuecolor = getColor(valutemp);
				//new Color((int)(valutemp*255),255-(int)(valutemp*255),140);
				g.setColor(vuecolor);
				g.drawLine(xind,ystart,xind,yend);
				if (xind==xend)
				{	// Une ligne pour indiquer la derniere valeur
					g.drawLine(xstart,ystart,xend,ystart);
					// Une Ligne pour indiquer la moyenne
					vuecolor = getColor(moyenne);
					g.setColor(vuecolor);
					dhauteur=vumeter_haut*moyenne;
					hauteur = (int)dhauteur;
					ystart=ymargin+(vumeter_haut-hauteur);
					g.drawLine(xstart,ystart,xend,ystart);
					// Une Ligne pour indiquer le maximum
					vuecolor = getColor(maxvalue);
					g.setColor(vuecolor);
					dhauteur=vumeter_haut*maxvalue;
					hauteur = (int)dhauteur;
					ystart=ymargin+(vumeter_haut-hauteur);
					g.drawLine(xstart,ystart,xend,ystart);
					
				} // if (xind==xend)
			} // for (int xind=xstart;xind<=xend;xind++)
		} // else <=> if (graphdraw==true)
		
		g.setColor(Color.red);
		if (label!=null)
		{			
			g.setFont(SmallFont);
			g.drawString(label, xstart,yend+10);
			g.setFont(StandardFont);
		}
		
		//g.drawString(""+vumeter_nb, xstart,yend+10);
		//g.drawString(label, xstart,yend+10);
		int vumeter_value_arrondie=(int)(vumeter_value*100);
		g.setColor(Color.green);
		g.drawString(""+vumeter_value_arrondie, xstart,yend+20);
		
		g.setColor(Color.blue);
		g.drawString(""+hauteur, xstart,yend+30);
		
		if ((onScreen.drvnum!=-1)&&((onScreen.drvnum-1)==vumeter_nb))
		{
			g.setColor(Color.gray);
			g.drawRect(xstart,ystart-(sensorheight/10),xend-xstart,(2*sensorheight/10));
			g.setColor(Color.black);
			g.drawRect(xstart+1,ystart-(sensorheight/10)+1,xend-xstart-2,(2*sensorheight/10)-2);
			
			//g.setColor(Color.green);
			//g.fillOval(((xend+xstart)/2)-(sensorwidth/4), sensorheight- (yend-ystart)+(sensorwidth/4),(sensorwidth/4), (sensorwidth/4));
			

		}
		
		g.setColor(Color.black);
		g.drawRect(xend-vumeter_larg,yend-vumeter_haut,vumeter_larg,vumeter_haut);
		
	}
 
    
    public int getDriveNum(int x,int y)
    {
    	if ((y>viewerheight)||(y<0)||(x>viewerwidth)||(x<0))
    	{	System.out.println("(AVW)Coordonnées hors limites");
    		return -1;
    	}
    	int indice=-1;
    	if ((y>=ymargin)&&(y<=ymargin+sensorheight))
    	{ 	// Hauteur y valable
    		if ((x>=xmargin)&&(x<=(xmargin+sensorwidth*nbdrive)))
    		{	// Largeur x valable
    			indice=1+Math.round((x-xmargin)/sensorwidth);
    			/*System.out.println("x:y-xmargin:ymargin-sensorwidth:sensorheight-nbdrive="
    				+x+":"+y+"-"
    				+xmargin+":"+ymargin+"-"
    				+sensorwidth+":"+sensorheight+"-"
    				+nbdrive);*/
    		}
	}
	return indice;
    }
    
    public double estimateValue(int y)
    {
    	double retvalue;
    	retvalue =(((double)(sensorheight-y+ymargin))/((double)sensorheight));
    	if (retvalue>1) retvalue=1;
    	if (retvalue<0) retvalue=0;
	//System.out.println("y:ymargin:sensorheight:retvalue="+y+":"+ymargin+":"+sensorheight+":"+retvalue);
    	return retvalue;
    }



    public void paintInfo(Graphics g)
    {
	paintEnv(g);
	StandardFont=g.getFont();
    	SmallFont=StandardFont.deriveFont(StandardFont.getSize2D()/4*3);
	//paintRobots(g);
    }


    public void observe()
    {	
		if (! flash) 
		{ 
			
			onScreen.repaint();
		//System.out.println("Koinkoin!!!");	
		}
		else onScreen.flash();
    }

    public void pleaseSendMessage(AgentAddress agtAddrTemp,Message msg)
    {
    	sendMessage(agtAddrTemp,msg);
    }
    
    public void end()
    {
    	running=false;
    }
    

}
