// Copyright (C) 1997 by MadKit Team
package kernelsim;

import madkit.kernel.*;
//import smaapp.*;
//import java.io.*;
//import java.util.*;
import java.awt.*;
//import madkit.simulation.activators.*;

import java.awt.event.*;

/** A SensorViewer is used to monitor the sensor values<br>
* (fr: Classe qui est utilisée pour observer les valeurs des capteurs (sensor))
* @author Jérôme Chapelle 2004
*/
public class SensorViewer extends Watcher  implements ReferenceableAgent
{
    /* propre à SensorViewer */
    //int Rnum=1;
    //int oldRnum;
	final static long serialVersionUID=0; // Warning du compilo
	
    int nbsensor=10;
    int viewerwidth,viewerheight;
    int sensorwidth,sensorheight;
    boolean running=true;
    
    NeuralAgent[] theSensors; // tableau des capteurs
    int xmargin,ymargin,ylegendmargin;
    
    Font StandardFont,SmallFont=null;
    
    
    
    //AgentAddress[] v=null;
    VueMeterMemory[] VueMeterMemories;
    
    //RobotAppEnv world;
    SensorCanvas onScreen;
        
    public SensorProbeApp detector=null;
    RobotSchedulerApp sch;
    String neuronGroup;

    //** var pour affichages dans simu **/
	
    boolean flash = false;
    boolean show = true;
    
    boolean bardraw = false;
    boolean graphdraw = true;

    
    boolean RainbowColorModel = true;
    boolean GreenMajentaColorModel = false;    
    boolean GrayColorModel = false;
    /*
    public void setRobot (int r){this.Rnum=r;}
    public int getRobot(){return Rnum;}
	*/
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


    public int bord,h,l;
    
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
	//detector2 = new SensorProbeApp("NeuronGroup"+Rnum,"sensor");
	//addProbe(detector2);
	if (detector!=null)
	{
	 System.out.println("InitValues() = group:role = "+detector.getGroup()+":"+detector.getRole());
	 
	}
    	//nbsensor=10;
    	xmargin=10;
    	ymargin=10;
    	ylegendmargin=40;
    	
    	/*
    	theSensors= detector.getSensors();
	nbsensor = Trier(theSensors); //theSensors.length;
    	*/
    	
    	if (nbsensor!=0) {
    		sensorwidth=(viewerwidth-(2*xmargin))/nbsensor;
    		VueMeterMemories= new VueMeterMemory[nbsensor];
    		for (int i=0;i<nbsensor;i++)
    		{	// En mode graphe, on mets autant de mémoire que de pixels à afficher
    			if (graphdraw == true) VueMeterMemories[i]=new VueMeterMemory(sensorwidth);
    			// Sinon, en mode "barres" on a besoin de mem une seule valeur
    			else VueMeterMemories[i]=new VueMeterMemory(1);
    		}
    		
    	 	}
    	else { sensorwidth=1;}
    	sensorheight=(viewerheight-(2*ymargin)-ylegendmargin);
    }
    
    public SensorViewer(String ngroup, RobotSchedulerApp s)
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
	
	//String lacommande=e.getActionCommand();
	
// --------------------- Ligne 1 -----------------------------------------------
	
    }

    public void initGUI()
    {
	//setBean(onScreen = new GridCanvas2DApp(dimx,dimy,this)); < deprecated
	onScreen = new SensorCanvas(viewerwidth,viewerheight,this);
	setGUIObject(onScreen);
    }

	/*
    public Dimension getVisibleSize()
    {
	return visibleSize;
    }*/


    public void activate()
    {
	detector = new SensorProbeApp(neuronGroup,"sensor");
	addProbe(detector);
	
	theSensors= detector.getSensors();
	nbsensor = Trier(theSensors); //theSensors.length;
	//NeuralAgent[] theSensorsBis=
	
	
	//if (!isGroup(group)) { createGroup(true, group, null, null); }
        //else { requestRole(group, "member", null); }
	//requestRole(group,"sensor observer",null);
	
	requestRole(neuronGroup, "sensor observer", null);
	//requestRole(neuronGroup,"sensor observer",null);
	
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
			String vinlabel,vjnlabel;
			for (int j=i+1;j<len;j++)
			{
				vinlabel=(vin[i]).getLabel();
				vjnlabel=(vin[j]).getLabel();
				if ((vjnlabel!=null)&&(vinlabel!=null))
				{
					int tempvalue=vinlabel.compareTo(vjnlabel);
					//System.out.println("Trie : "+((vin[i]).getLabel())+" comp "+(vin[j]).getLabel()+" = "+tempvalue);	
					if (tempvalue>0)
					{
						//System.out.println("Inversion !");
						vin[i]=vin[j];
						vin[j]=agtTemp;
						agtTemp=vin[i];
						//System.out.println("==> "+((vin[i]).getLabel())+" <= "+(vin[j]).getLabel());	
					} // if (tempvalue>0)
				}
			} // for (int j=i+1;j<len;j++)
		} // for (int i=0;i<len;i++)
		return len;
	}
	
	/** Dessine le visualiseur de capteur **/
	public void paintEnv(Graphics g)
	{	
		/*if ((Rnum!=oldRnum)||(theSensors==null))
		{ // En théorie on ne passe plus ici car Rnum n'est plus modifié
		  //oldRnum=Rnum;

		  theSensors= detector.getSensors();
		  nbsensor = Trier(theSensors); //theSensors.length;
		  initValues();
		}*/
		g.setColor(Color.white);
		//if (Rnum<=1) {
		g.fillRect(0,0,viewerwidth,viewerheight); // /2 => pour test
		//}
		//Rnum++;
		g.setColor(Color.black);
		g.drawString("n "+neuronGroup+" ("+nbsensor+")", 10,10);
		//paintImmediatly(0,0,dimx,dimy);
		//system.out.println("SW.paintEnv(g)");
		double vtemp; // valeur temp
		String ltemp=null; // label temporaire
		if (theSensors!=null)
		{
			NeuralAgent agtAddrTemp= null;
			for (int i=0; (i < theSensors.length) ; i++)
			{	//nagenttrouve++;
				agtAddrTemp = theSensors[i];
				//System.out.println("Envoi msg à g="+NeuronGroup+" v="+valcapteursIR.NC(i));
				//sendMessage(NeuronGroup,"neuron",new NeuralMessage(valcapteursIR.NC(i),"CapIR"+i));
				if (i<theSensors.length)
					{vtemp=(agtAddrTemp).getLastValue();
					 ltemp=(agtAddrTemp).getLabel();
					}
				else
					{vtemp=((double)i)/theSensors.length;}
				VueMeterMemories[i].putValue(vtemp);
				drawVueMeter(g,vtemp,i,sensorwidth,sensorheight,ltemp);
				//sendMessage(agtAddrTemp,new NeuralMessage(valcapteursIR.NC(i),"CapIR"+i));
			}
		}
		if (theSensors.length==0)
		{
			g.setColor(Color.red);
			g.drawString("No sensor detected for NeuronGroup \""+neuronGroup+"\" !", 10,30);
		}
	}

	/** Dessine un VueMeter **/
	public void drawVueMeter(Graphics g,double vumeter_value,int vumeter_nb,int vumeter_larg,int vumeter_haut, String label)
	{
		//Color vuecolor = new Color(240,(int)(100+55*(satp+1)),140);
		
		int xstart,ystart,xend,yend,hauteur;
		double dhauteur=vumeter_haut*vumeter_value;
		hauteur = (int)dhauteur;
		if (hauteur>vumeter_haut) {hauteur=vumeter_haut;}
		
		xstart=xmargin+(vumeter_larg*vumeter_nb);
		ystart=ymargin+(vumeter_haut-hauteur);
		xend=xmargin+(vumeter_larg*(vumeter_nb+1));
		yend=ymargin+vumeter_haut;
		/*Color vuecolor = new Color((int)(vumeter_value*255),255-(int)(vumeter_value*255),140);
		g.setColor(vuecolor);*/
		//g.drawLine(xstart,ystart,xend,ystart);
		//g.drawLine(xstart,yend,xend,yend);
		Color vuecolor;
		if (bardraw==true)
		{	vuecolor = getColor (vumeter_value);
			//new Color((int)(vumeter_value*255),255-(int)(vumeter_value*255),140);
			g.setColor(vuecolor);
			g.fillRect(xstart,ystart,xend-xstart,yend-ystart);
		}
		else // if (graphdraw==true)
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
					
				}
			}
			
			
			
		}
		
		
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
		
		g.setColor(Color.black);
		g.drawRect(xend-vumeter_larg,yend-vumeter_haut,vumeter_larg,vumeter_haut);
	}

/*
    public void paintComponent(Graphics g)
    {
	super.paintComponent(g);
	paintInfo(g);
    }
*/    
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
			
		}
		else onScreen.flash();
			    	
		//System.out.println("Koinkoin!!!");
    }
    
    public void end()
    {
    	running=false;
    }
}
