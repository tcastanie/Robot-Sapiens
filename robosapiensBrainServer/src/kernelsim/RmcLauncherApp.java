package kernelsim;

import madkit.kernel.*;
//-------
import java.awt.*;
import java.awt.BorderLayout;
//import java.awt.Color;
import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.awt.event.*;

import java.util.Vector;
import java.lang.Integer;

/*import javax.swing.JButton;
import javax.swing.JPanel;*/
import javax.swing.*;

/** Main class that handles the simulation<br>
* (fr: Classe principale qui s'occupe de la simulation)
*/
public abstract class RmcLauncherApp extends Agent
{
	public static final long serialVersionUID = 42L; //RobotAppEnv.serialVersionUID;
    protected RobotAppEnv playGround;
    protected RobotWorldViewer2DApp rwv;
    
    boolean run = false;
    boolean robotViewer = false;
    boolean sensorViewer = false;
    boolean reset = false;
    boolean end = false;
    public static boolean MS=true;
    RobotSchedulerApp sch;
    
    public static String simNameCopy;
    protected String simulationName=("? NAME ?");
    protected String simulationFile=("? FILE ?");
    protected String dir;
    int cycleDelay = 10; //100
    public static int NumberRobots;
    int NumberDecoup = 0;
    int currentSViewer=1;
    int currentAViewer=1;
    int currentDViewer=1;
    SensorViewer[] SViewers=null;
    ActorViewer[] AViewers=null;
    DriveViewer[] DViewers=null;    
    
    public static boolean is_applet=false;

    public static int NRobCopy;
    public static int NDecCopy;

    static int x_prem=100;
    static int y_prem=45;
    public static int depotSize = 300;

	// Relatif au menu
	//GraphicMenu cenv;
	MenuSim menu;
  	JButton b, runb, playpause, LoadBrains, SaveBrains, bmnr,bpnr,bmnd,bpnd;
  	JPanel all,p,m;
  	JLabel jl;
  	GridBagConstraints c;
  	int default_width=640; int default_height=480; // Taille de toute la fenetre
  	JTextField tf_filename,tf_numberrobots,tf_numberdecoup,tf_cycledelay,tf_sensorviewer,tf_actorviewer,tf_driveviewer;
  	
  	//JTabbedPane enved,robed;
	//String filename;		// Nom de la map (ouvrir/sauver)
	//int map_dimx,map_dimy;		// dimensions de l'envt
	// ---------------

    boolean stop=false;

	public RmcLauncherApp(String s)
	{//C:\madkit\plugins\LearningBots\src\smaapp
	
		try
		{
			dir = s;
			dir = System.getProperties().getProperty("user.dir")+"/bin/";
		}
		catch (Exception e)
		{
			is_applet=true;
			System.out.println(e);
		}
		
		
		// /plugins/LearningBots/src/smaapp/
	}

// --------- Partie concernant le menu -------------------------------------------
    private void MyActionPerformed(java.awt.event.ActionEvent e){
       		//filename=e.getActionCommand();
       		
       		/*
       		  String TmpFilename=tf_filename.getText();
	    if (!TmpFilename.endsWith(".env")) { TmpFilename=TmpFilename.concat(".env"); tf_filename.setText(TmpFilename);}
	    */
       		System.out.println("Lancer cette commandeRmcLA:MAP:");
		System.out.println(e.getActionCommand());
		}
    	/*b1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt){
                 MyActionPerformed(evt);
            }
	});*/
    public void sendEvent(ActionEvent e)
    {
	System.out.println("Lancer cette commandeRmcLA:sE:");
	System.out.println(e.getActionCommand());
	
	String lacommande=e.getActionCommand();
	
// --------------------- Ligne 1 -----------------------------------------------
	if (lacommande.equals("runsimulation"))
	{ //ClearMap();
		
		simulationFile=simNameCopy=simulationName=tf_filename.getText();
		System.out.println("simulationFile=simNameCopy=simulationName="+simulationName);
		System.out.println("Version du 15-06-05@16:01");
		setRun(true);
		

		
	}
	if (lacommande.equals("playsimulation"))
	{ 
		if (getRun()==false) {setRun(true);}
		
		if (getStop()==true)
		{ //System.out.println ("Ancien nom :"+playpause.getText());
		  playpause.setText("Play");
		  playpause.updateUI();
		  //sendMessage(sch.getAddress(), new StringMessage("Play")); Null pointer exception
		  LoadBrains.setEnabled(false);
		  LoadBrains.setOpaque(false);
		  SaveBrains.setEnabled(false);
		  SaveBrains.setOpaque(false);
		}
		else
		{ //System.out.println ("Ancien nom :"+playpause.getText());
		  playpause.setText("Pause");
		  playpause.updateUI();
		  //sendMessage(sch.getAddress(), new StringMessage("Pause"));
		  LoadBrains.setEnabled(true);
		  LoadBrains.setOpaque(true);
		  SaveBrains.setEnabled(true);
		  SaveBrains.setOpaque(true);		  
		}
		setStop(!getStop());
	}
	if (lacommande.equals("loadbrains"))
	{ 
		if(sch != null)
			sendMessage(sch.getAddress(), new StringMessage("LoadBrains"));
	}
	if (lacommande.equals("savebrains"))
	{ 
		if(sch != null)
			sendMessage(sch.getAddress(), new StringMessage("SaveBrains"));
	}

	if (lacommande.equals("resetSimulation")) 
	{ setReset(true); }
// --------------------- Ligne 2 -----------------------------------------------
	
	if (lacommande.equals("MinusNumberRobots")) 
	{ 	int nbrobtemp=getNumberRob()-1;
		if (nbrobtemp<=1) {nbrobtemp=1;}
		if (nbrobtemp<getNumberDecoup())
		{	setNumberDecoup(nbrobtemp);
			tf_numberdecoup.setText((new Integer(nbrobtemp)).toString());
			tf_numberdecoup.updateUI();
		}
		setNumberRob(nbrobtemp);
		tf_numberrobots.setText((new Integer(nbrobtemp)).toString());
		tf_numberrobots.updateUI();
	}
	if (lacommande.equals("PlusNumberRobots")) 
	{ 
		int nbrobtemp=getNumberRob()+1;
		if (nbrobtemp<=1) {nbrobtemp=1;}
		setNumberRob(nbrobtemp);
		
		tf_numberrobots.setText((new Integer(nbrobtemp)).toString());
		tf_numberrobots.updateUI();
	}
	
// --------------------- Ligne 3 -----------------------------------------------
	
	if (lacommande.equals("MinusNumberDecoup")) 
	{ 	int nbrobtemp=getNumberDecoup()-1;
		if (nbrobtemp<=0) {nbrobtemp=0;}
		setNumberDecoup(nbrobtemp);
		tf_numberdecoup.setText((new Integer(nbrobtemp)).toString());
		tf_numberdecoup.updateUI();
	}
	if (lacommande.equals("PlusNumberDecoup")) 
	{ 
		int nbrobtemp=getNumberDecoup()+1;
		if (nbrobtemp>getNumberRob())
		{
			setNumberRob(nbrobtemp);
			tf_numberrobots.setText((new Integer(nbrobtemp)).toString());
			tf_numberrobots.updateUI();
			
		}
		setNumberDecoup(nbrobtemp);
		tf_numberdecoup.setText((new Integer(nbrobtemp)).toString());
		tf_numberdecoup.updateUI();
	}

// --------------------- Ligne 4 -----------------------------------------------
	
	if (lacommande.equals("MinusCycleDelay")) 
	{ 	Double valtmp=new Double((double)(getCycleDelay()*0.75));
		System.out.println("getCycleDelay()"+getCycleDelay());
		System.out.println("valtmp"+valtmp);
		int cycdel=(valtmp.intValue());
		System.out.println("cycdel"+cycdel);
		//int cycdel = (int)(getCycleDelay()/1.5);
		if (cycdel<=1) {cycdel=1;}
		setCycleDelay((int)cycdel);
		tf_cycledelay.setText((new Integer(cycdel)).toString());
		tf_cycledelay.updateUI();
	}
	if (lacommande.equals("PlusCycleDelay")) 
	{ 
		Double valtmp=new Double((double)(getCycleDelay()*1.5));
		System.out.println("getCycleDelay()"+getCycleDelay());
		System.out.println("valtmp"+valtmp);
		int cycdel=(valtmp.intValue());
		System.out.println("cycdel"+cycdel);
		//int cycdel = (int)(getCycleDelay()/1.5);
		if (cycdel<=1) {cycdel=1;}
		setCycleDelay((int)cycdel);
		tf_cycledelay.setText((new Integer(cycdel)).toString());
		tf_cycledelay.updateUI();
	}
	
// --------------------- Ligne 5 -----------------------------------------------
	
	if (lacommande.equals("MinusCurrentSViewer")) 
	{ 	int sviewer=currentSViewer-1;
		if (sviewer<=1) {sviewer=1;}
		//setCycleDelay(sviewer);
		currentSViewer=sviewer;
		tf_sensorviewer.setText((new Integer(currentSViewer)).toString());
		tf_sensorviewer.updateUI();
	}
	if (lacommande.equals("PlusCurrentSViewer")) 
	{ 	int sviewer=currentSViewer+1;
		if (sviewer>getNumberRob()) {sviewer=getNumberRob();}
		//setCycleDelay(sviewer);
		currentSViewer=sviewer;
		tf_sensorviewer.setText((new Integer(currentSViewer)).toString());
		tf_sensorviewer.updateUI();
	}
	if (lacommande.equals("ShowCurrentSViewer")) 
	{ 	addSensorViewer(currentSViewer);
		
	}
// --------------------- Ligne 6 -----------------------------------------------
	if (lacommande.equals("MinusCurrentAViewer")) 
	{ 	int aviewer=currentAViewer-1;
		if (aviewer<=1) {aviewer=1;}
		//setCycleDelay(aviewer);
		currentAViewer=aviewer;
		tf_actorviewer.setText((new Integer(currentAViewer)).toString());
		tf_actorviewer.updateUI();
	}
	if (lacommande.equals("PlusCurrentAViewer")) 
	{ 	int aviewer=currentAViewer+1;
		if (aviewer>getNumberRob()) {aviewer=getNumberRob();}
		//setCycleDelay(sviewer);
		currentAViewer=aviewer;
		tf_actorviewer.setText((new Integer(currentAViewer)).toString());
		tf_actorviewer.updateUI();
	}
	if (lacommande.equals("ShowCurrentAViewer")) 
	{ 	addActorViewer(currentAViewer);
		
	}
	
// --------------------- Ligne 7 -----------------------------------------------
	if (lacommande.equals("MinusCurrentDViewer")) 
	{ 	int dviewer=currentDViewer-1;
		if (dviewer<=1) {dviewer=1;}
		currentDViewer=dviewer;
		tf_driveviewer.setText((new Integer(currentDViewer)).toString());
		tf_driveviewer.updateUI();
	}
	if (lacommande.equals("PlusCurrentDViewer")) 
	{ 	int dviewer=currentDViewer+1;
		if (dviewer>getNumberRob()) {dviewer=getNumberRob();}
		//setCycleDelay(sviewer);
		currentDViewer=dviewer;
		tf_driveviewer.setText((new Integer(currentDViewer)).toString());
		tf_driveviewer.updateUI();
	}
	if (lacommande.equals("ShowCurrentDViewer")) 
	{ 	addDriveViewer(currentDViewer);
		
	}
/*
	if (lacommande.equals("")) 
	{ 
	}
	if (lacommande.equals("")) 
	{ 
	}
	if (lacommande.equals("")) 
	{ 
	}
	if (lacommande.equals("")) 
	{ 
	}
	if (lacommande.equals("")) 
	{ 
	}
	if (lacommande.equals("")) 
	{ 
	}
	if (lacommande.equals("")) 
	{ 
	}
	if (lacommande.equals("")) 
	{ 
	}
	if (lacommande.equals("")) 
	{ 
	}
	if (lacommande.equals("")) 
	{ 
	}
	*/
	
    }
  public JButton IconButton(String iconFileName)
  {	return IconButton(iconFileName,null);  }    
  public JButton IconButton(String iconFileName,String altText)
  {
	ImageIcon ii = null;
    	

	try
	      { 
		java.net.URL url;
		url = this.getClass().getResource(iconFileName);
		//System.out.println(""+this.getClass());
		//System.out.println("image url:"+url);
		ii = new ImageIcon(url);
	      } catch (Exception e) { e.printStackTrace();}
      	if (ii!=null)
      	{      	//Icon ico=new ImageIcon(ii);
      		if (altText==null)
      		{ b = new JButton(ii); }
      		else
      		{ b = new JButton(altText,ii);}
	}
	else
	{	b = new JButton(altText);
	}
	return b;
  }
  
  public void initGUI()
    {
  		setNumberRob(1);
  		
    	//String tmpstring;
	//int tempvalue;
	
	System.out.println("GUI Initialisé");
    	//map_dimx=320;map_dimy=240;

// ------------------------------------- Conteneurs ------------------------------------
	all= new JPanel(new BorderLayout());
      	p = new JPanel(new BorderLayout());
      	m = new JPanel(new GridBagLayout());
      	all.add("Center",p);
      	all.add("West",m);
      	
	//cenv=new GraphicMenu(map_dimx,map_dimy);
      	//p.add("Center",cenv);
        
      	//MenuSim menu;
	c = new GridBagConstraints();
	c.fill = GridBagConstraints.NONE; // BOTH => maximise la taille des elts
	c.weightx = 1.0;
	c.weighty = 1.0;
	c.gridwidth=GridBagConstraints.REMAINDER; // REMAINDER = ce qu'il reste

	c.weighty = 0;
	menu=new MenuSim(200,100,this);
	
	//c.gridheight = GridBagConstraints.REMAINDER;
	//c.weighty=2;
	m.add(menu,c);
	c.weighty = 1.0;

// --------------------------- Première ligne ---------------------------
	/*
	c.weighty = 1.0;
	c.gridwidth=3;
	b = new JButton("Nouveau");
	b.setActionCommand("newmap");
	b.addActionListener(menu);	
	m.add(b,c);
	
	tmpstring = (new Integer(320)).toString();
	tf_dimx = new JTextField(tmpstring, 4);
	tf_dimx.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt){
                 MyActionPerformed(evt);
            }
	});
	m.add(tf_dimx,c);
	
	tmpstring = (new Integer(240)).toString();
	c.gridwidth=GridBagConstraints.REMAINDER;
	tf_dimy = new JTextField(tmpstring, 4);
	tf_dimy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt){
                 MyActionPerformed(evt);
            }
	});
	m.add(tf_dimy,c);
	*/
	
	c.gridwidth=5;
	
	c.weightx =1;
	//c.gridx = 1;
	//c.gridy = 0;
	//c.fill = GridBagConstraints.HORIZONTAL;
	runb = IconButton("/images/run.gif","Run");
	//b = new JButton(ico);
	runb.setActionCommand("runsimulation");
	runb.addActionListener(menu);	
	m.add(runb,c);
	
	//c.gridx = 2;
	//c.gridy = 0;
	playpause = IconButton("/images/step.gif","Play"); //new JButton("Play");
	playpause.setActionCommand("playsimulation");
	playpause.addActionListener(menu);	
	m.add(playpause,c);
	
	//c.gridx = 3;
	//c.gridy = 0;
	LoadBrains = IconButton("/images/open.gif","Load"); //new JButton("Load");
	LoadBrains.setActionCommand("loadbrains");
	LoadBrains.addActionListener(menu);	
	m.add(LoadBrains,c);
	
	//c.gridx = 4;
	//c.gridy = 0;
	SaveBrains = IconButton("/images/save.gif","Save"); //new JButton("Save");
	SaveBrains.setActionCommand("savebrains");
	SaveBrains.addActionListener(menu);	
	m.add(SaveBrains,c);	

	playpause.setEnabled(false);
	playpause.setOpaque(false);	
	LoadBrains.setEnabled(false);
	LoadBrains.setOpaque(false);
	SaveBrains.setEnabled(false);
	SaveBrains.setOpaque(false);

	//c.gridx = 5;
	//c.gridy = 0;
	//c.gridwidth=GridBagConstraints.REMAINDER;
	b = IconButton("/images/stop.gif","Reset"); //new JButton("Pause");
	b.setActionCommand("resetSimulation");
	b.addActionListener(menu);	
	c.gridwidth=GridBagConstraints.REMAINDER;
	m.add(b,c);

// --------------------------- Deuxième ligne ---------------------------
	c.gridwidth=2;
	jl = new JLabel("Simulation Name");
	m.add(jl,c);
	c.gridwidth=GridBagConstraints.REMAINDER;
	tf_filename = new JTextField(simulationName, 16);
	tf_filename.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt){
                 MyActionPerformed(evt);
            }
	});
	m.add(tf_filename,c);
// --------------------------- Troisième ligne ---------------------------	
	c.gridwidth=4;
	jl = new JLabel("Robots number");
	m.add(jl,c);
	
	tf_numberrobots = new JTextField((new Integer(NumberRobots)).toString(), 16);
	tf_numberrobots.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt){
                 MyActionPerformed(evt);
            }
	});
	m.add(tf_numberrobots,c);
	
	bmnr = IconButton("/images/moins.gif"); //new JButton("Nouveau chose3");
	bmnr.setActionCommand("MinusNumberRobots");
	bmnr.addActionListener(menu);
	m.add(bmnr,c);
	
	c.gridwidth=GridBagConstraints.REMAINDER;
	bpnr = IconButton("/images/plus.gif"); 
	bpnr.setActionCommand("PlusNumberRobots");
	bpnr.addActionListener(menu);
	m.add(bpnr,c);


// --------------------------- Quatrième ligne ---------------------------

	c.gridwidth=4;
	jl = new JLabel("Cutters number");
	m.add(jl,c);

	tf_numberdecoup = new JTextField((new Integer(NumberDecoup)).toString(), 16);
	tf_numberdecoup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt){
                 MyActionPerformed(evt);
            }
	});
	m.add(tf_numberdecoup,c);
	
	bmnd = IconButton("/images/moins.gif"); //new JButton("Nouveau chose3");
	bmnd.setActionCommand("MinusNumberDecoup");
	bmnd.addActionListener(menu);
	m.add(bmnd,c);

	c.gridwidth=GridBagConstraints.REMAINDER;
	bpnd = IconButton("/images/plus.gif"); 
	bpnd.setActionCommand("PlusNumberDecoup");
	bpnd.addActionListener(menu);
	m.add(bpnd,c);
	
// --------------------------- Cinquième ligne ---------------------------

	c.gridwidth=4;
	jl = new JLabel("Cycle Delay");
	m.add(jl,c);

	tf_cycledelay = new JTextField((new Integer(cycleDelay)).toString(), 16);
	tf_cycledelay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt){
                 MyActionPerformed(evt);
            }
	});
	m.add(tf_cycledelay,c);
	
	b = IconButton("/images/moins.gif"); //new JButton("Nouveau chose3");
	b.setActionCommand("MinusCycleDelay");
	b.addActionListener(menu);
	m.add(b,c);
	b = IconButton("/images/plus.gif"); 
	b.setActionCommand("PlusCycleDelay");
	b.addActionListener(menu);
	m.add(b,c);	

	c.gridwidth=GridBagConstraints.REMAINDER;
	b = IconButton("/images/redo.gif"); 
	b.setActionCommand("UpdateCycleDelay");
	b.addActionListener(menu);
	m.add(b,c);
		
// --------------------------- Sixième ligne ---------------------------

	c.gridwidth=5;
	jl = new JLabel("SensorViewer");
	m.add(jl,c);
	
	tf_sensorviewer = new JTextField((new Integer(currentSViewer)).toString(), 16);
	tf_sensorviewer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt){
                 MyActionPerformed(evt);
            }
	});
	m.add(tf_sensorviewer,c);
	
	b = IconButton("/images/moins.gif"); //new JButton("Nouveau chose3");
	b.setActionCommand("MinusCurrentSViewer");
	b.addActionListener(menu);
	m.add(b,c);
	b = IconButton("/images/plus.gif"); 
	b.setActionCommand("PlusCurrentSViewer");
	b.addActionListener(menu);
	m.add(b,c);

	c.gridwidth=GridBagConstraints.REMAINDER;
	b = IconButton("/images/eye.gif"); 
	b.setActionCommand("ShowCurrentSViewer");
	b.addActionListener(menu);
	m.add(b,c);
	
	
// --------------------------- Septième ligne ---------------------------

	c.gridwidth=5;
	jl = new JLabel("ActorViewer");
	m.add(jl,c);
	
	tf_actorviewer = new JTextField((new Integer(currentAViewer)).toString(), 16);
	tf_actorviewer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt){
                 MyActionPerformed(evt);
            }
	});
	m.add(tf_actorviewer,c);
	
	b = IconButton("/images/moins.gif"); //new JButton("Nouveau chose3");
	b.setActionCommand("MinusCurrentAViewer");
	b.addActionListener(menu);
	m.add(b,c);
	b = IconButton("/images/plus.gif"); 
	b.setActionCommand("PlusCurrentAViewer");
	b.addActionListener(menu);
	m.add(b,c);

	c.gridwidth=GridBagConstraints.REMAINDER;
	b = IconButton("/images/eye.gif"); 
	b.setActionCommand("ShowCurrentAViewer");
	b.addActionListener(menu);
	m.add(b,c);

// --------------------------- Septième ligne ---------------------------

	c.gridwidth=5;
	jl = new JLabel("DriveViewer");
	m.add(jl,c);
	
	tf_driveviewer = new JTextField((new Integer(currentDViewer)).toString(), 16);
	tf_driveviewer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt){
                 MyActionPerformed(evt);
            }
	});
	m.add(tf_driveviewer,c);
	
	b = IconButton("/images/moins.gif"); //new JButton("Nouveau chose3");
	b.setActionCommand("MinusCurrentDViewer");
	b.addActionListener(menu);
	m.add(b,c);
	b = IconButton("/images/plus.gif"); 
	b.setActionCommand("PlusCurrentDViewer");
	b.addActionListener(menu);
	m.add(b,c);

	c.gridwidth=GridBagConstraints.REMAINDER;
	b = IconButton("/images/eye.gif"); 
	b.setActionCommand("ShowCurrentDViewer");
	b.addActionListener(menu);
	m.add(b,c);
      	
      	setGUIObject(all);
    }

 public String getSimulationFile(){return simulationFile;}
 public void setSimulationFile(String n)
 {simulationFile = n;}
 public String getSimulationName(){return simulationName;}
 public void setSimulationName(String add){simulationName = add;}
 public int getCycleDelay(){return cycleDelay;}
 public int getDepotSize(){return depotSize;}
 public void setCycleDelay (int add)
	{	
		cycleDelay = add;
		if (sch!=null) {sch.delay = cycleDelay;}
	}
 public void setDepotSize(int dz){depotSize=dz;}
 public int getNumberRob(){return NumberRobots;}
 public void setNumberRob(int add)
 	{
 		NumberRobots=add;NRobCopy=add;
 		AViewers=new ActorViewer[add];
 		DViewers=new DriveViewer[add];
 		SViewers=new SensorViewer[add];
 	}

 public int getNumberDecoup(){return NumberDecoup;}
 public void setNumberDecoup(int add){NumberDecoup=add;NDecCopy=add;}

 public int getxp(){return x_prem;}
 public void setxp(int xp){x_prem=xp;}

 public int getyp(){return y_prem;}
 public void setyp(int yp){y_prem=yp;}

 public boolean getModelSat(){return MS;}
 public void setModelSat(boolean m){MS=m;}

 public boolean getReset(){return reset;}
 public void setRun (boolean add)
 {	
 	run = add;
  	NRobCopy=NumberRobots;
  	NDecCopy=NumberDecoup;
    	simNameCopy=simulationName;
    	
    	runb.setEnabled(false);
	runb.setOpaque(false);
	tf_filename.setEnabled(false);
	tf_filename.setOpaque(false);
	tf_numberrobots.setEnabled(false);
	tf_numberrobots.setOpaque(false);
	tf_numberdecoup.setEnabled(false);
	tf_numberdecoup.setOpaque(false);
	bmnr.setEnabled(false);
	bmnr.setOpaque(false);
	bpnr.setEnabled(false);
	bpnr.setOpaque(false);
	bmnd.setEnabled(false);
	bmnd.setOpaque(false);
	bpnd.setEnabled(false);
	bpnd.setOpaque(false);
	
	playpause.setEnabled(true);
	playpause.setOpaque(true);
	LoadBrains.setEnabled(true);
	LoadBrains.setOpaque(true);
	SaveBrains.setEnabled(true);
	SaveBrains.setOpaque(true);
	
    }
 public boolean getRun(){return run;}
 
 public void setSensorViewer (boolean b)
    {
	if (b)
	    {
	    	sensorViewer=true;
		/*robotViewer=true;
		*/
		//sendMessage(sch.getAddress(), new StringMessage("Pause"));
		println("adding sensorviewer");
		pause(500);
		addSensorViewer(currentSViewer);
		
		
		//sch.updateObserverActivator();
		
		pause(500);
		//sendMessage(sch.getAddress(), new StringMessage("Play"));
		
	    }
	// Must be false anyway.
	//robotViewer = false;
    }

 public boolean getSensorViewer() {return sensorViewer; } 
 
 public boolean getAddNewRobotViewer() {return robotViewer; }

 public void setReset (boolean b)
 {
	if (b)
	    {
		reset=true;
		sendMessage(sch.getAddress(), new StringMessage("Stop"));
		println("simulation reset");
		pause(1000);
		//playGround.reset();
		sch.iteration = 0;
		//040708 sch.update();
		init();
		pause(1000);
		//sendMessage(sch.getAddress(), new StringMessage("setReset")); // samedi
		sendMessage(sch.getAddress(), new StringMessage("Play"));
	    }
	// Must be false anyway.
    reset = false;
 }

    public void setAddNewRobotViewer (boolean b)
    {
	if (b)
	    {
		robotViewer=true;
		sendMessage(sch.getAddress(), new StringMessage("Pause"));
		println("adding viewer");
		pause(500);
		addRobotWorldViewer();
		//sch.updateObserverActivator();
		pause(500);
		//sendMessage(sch.getAddress(), new StringMessage("setAddNewRobotViewer")); // samedi
		sendMessage(sch.getAddress(), new StringMessage("Play"));
	    }
	// Must be false anyway.
	robotViewer = false;
    }
    public void setStop(boolean add)
    {
	if (add && stop != true)
	    {
		stop = true;
		if(sch != null)
			sendMessage(sch.getAddress(), new StringMessage("Stop"));
		println("simulation paused");
	    }
	if (!add && stop == true)
	    {
		stop = false;
		
		if(sch != null)
		{	//sendMessage(sch.getAddress(), new StringMessage("setStop")); // samedi
			sendMessage(sch.getAddress(), new StringMessage("Play"));
		}
		println("simulation running");
	    }
    }
    public boolean getStop(){return stop;}

    public final void activate()
	{
	println("RobotLauncher 2D activated !!");
	println("Launcher agent Activated");
	println("Wait for run instruction");
	
	/*
	print("Load GraphicMenu ... ");
	
	GraphicMenu GM = new GraphicMenu(320,400);
	GM.fond();
	GM.repaint();
	
	if (GM!=null) {println("Ok");} else {println("GM=null !!!");}
	*/
	 while (!end)
	  {
	      pause(10); // 050602
		if (run == true)
		{
		    if (! simulationName.equals("? NAME ?") && ! simulationName.equals("")
				 && ! simulationFile.equals("? FILE ?") && ! simulationFile.equals(""))
			{
			    println("Initialisation : please wait...");
			    break;
			}
			
			/*else{ // le else ici (est) était INDISPENSABLE
			 * 		// (car y avait pas les {} pour if(run==true))
			    */println("Set the simulationName before running it"+simulationName);
			    run = false;
			//} 
		} // if (run == true)

	  }
	}

protected void createRobotWorld()
{
  createGroup(true,simulationName,null,null);
  //foundGroup(simulationName); // Deprecated
  System.out.println("simulationName:"+simulationName);
    System.out.println("simulationFile:"+simulationFile);
    System.out.println("dir:"+dir);
    System.out.println("new RobotAppEnv...");
  playGround = new RobotAppEnv(simulationName,simulationFile); // avant (chg local) ...,dir+simulationFile);
  System.out.println("launchAgent RobotAppEnv...");
  launchAgent(playGround,"ROBOT WORLD",false);
  System.out.println("RobotAppEnv created & launched !");
  
  System.out.println("new RobotSchedulerApp");
  sch = new RobotSchedulerApp(simulationName,playGround);
  System.out.println("launchAgent RobotScheduler...");
  launchAgent(sch,"RobotSchedulerApp",false);
  System.out.println("RobotSchedulerApp created & launched !");
  
  //sendMessage(sch.getAddress(), new StringMessage("CreateRobotWorld")); //samedi
  //sendMessage(sch.getAddress(), new StringMessage("Play"));
  //sendMessage(sch.getAddress(), new StringMessage("Stop"));
  
  println("initialisation ok");
}

protected void addRobot(RobotAppPhy t)
	{
	//playGround.addAgent(t,getNumberRob(),getxp(),getyp());
	playGround.addAgent(t,getNumberRob(),getxp(),getyp(),this.getNumberDecoup());
	}
    /*
protected void addRobotWatcher(RobotWatcher tw)
	{
	    	tw.simulationGroup = simulationName;
	    launchAgent(tw,"Robot Watcher",true);
	}
    */

protected void addSensorViewer(int n)
    {
		if (SViewers==null)
		{
			SViewers=new SensorViewer[getNumberRob()];
		}
		if (SViewers[n-1]==null)
		{
			SensorViewer v = new SensorViewer("NeuronGroup"+n,sch);
			launchAgent(v,"SensorViewer",true);
			SViewers[n-1]=v;
		}
		else
		{
			if (SViewers[n-1].running==false)
			{
				SensorViewer v = new SensorViewer("NeuronGroup"+n,sch);
				launchAgent(v,"SensorViewer",true);
				SViewers[n-1]=v;
			}
		}
    }
    
protected void addActorViewer(int n)
    {
	if (AViewers==null)
	{
		AViewers=new ActorViewer[getNumberRob()];
	}
	
		if (AViewers[n-1]==null)
		{	
			ActorViewer v = new ActorViewer("NeuronGroup"+n,sch);
			launchAgent(v,"ActorViewer",true);
			AViewers[n-1]=v;
		}
		else
		{
			if (AViewers[n-1].running==false)
			{
				ActorViewer v = new ActorViewer("NeuronGroup"+n,sch);
				launchAgent(v,"ActorViewer",true);
				AViewers[n-1]=v;
			}
		}
    }
    
protected void addDriveViewer(int n)
    {
	if (DViewers==null)
	{
		DViewers=new DriveViewer[getNumberRob()];
	}
		if (DViewers[n-1]==null)
		{	
			DriveViewer v = new DriveViewer("NeuronGroup"+n,sch);
			launchAgent(v,"DriveViewer",true);
			DViewers[n-1]=v;
		}
		else
		{
			if (DViewers[n-1].running==false)
			{
				DriveViewer v = new DriveViewer("NeuronGroup"+n,sch);
				launchAgent(v,"DriveViewer",true);
				DViewers[n-1]=v;
			}
		}
    }

protected void addRobotWorldViewer()
    {
	//RobotWorldViewer2DApp
	 rwv = new RobotWorldViewer2DApp(simulationName,playGround,sch);
	rwv.setRmc(this);
	launchAgent(rwv,"Robot World Viewer App",true);
    }

abstract protected void init();

void launchSimulation() //throws ThreadDeath
{
	println("launching simulation");
	//sendMessage(sch.getAddress(), new StringMessage("launchSimulation")); // samedi
	//sendMessage(sch.getAddress(), new StringMessage("Play"));
	while (!end)
	{
		pause(10); // 050602 050706
    }
}


public final void end()
	{
	end=true;
	
	for (int i=0;i<this.getNumberRob();i++)
	{
		if ((DViewers[i]!=null)&&(DViewers[i].running==true))
		{ DViewers[i].end(); killAgent(DViewers[i]); DViewers[i]=null; }
		if ((AViewers[i]!=null)&&(AViewers[i].running==true))
		{ AViewers[i].end(); killAgent(AViewers[i]); AViewers[i]=null; }
		if ((SViewers[i]!=null)&&(SViewers[i].running==true))
		{ SViewers[i].end(); killAgent(SViewers[i]); SViewers[i]=null; }		
	}
	
	if (RobotAppPhy.otherRobots!=null)
	{
		Vector V=RobotAppPhy.otherRobots;
			
		if (V.size()>0)
		{
			RobotAppPhy tmpbrain=null;
			for (int i=0;i<V.size();i++)
			{
				tmpbrain=(RobotAppPhy)V.elementAt(i);
				tmpbrain.end();
				killAgent(tmpbrain);
				
			}
			
		}
	}
	
	    if (sch!=null)
	    {
	    	sendMessage(sch.getAddress(), new StringMessage("Stop"));
	    	pause(500);
	    }
	    
	    println("Closing simulation !");
	    if (playGround!=null)
	    {
	    	playGround.end();
	    	
	    	//playGround.finalReset();
	    	//println("No more World");
	    	killAgent(playGround);
	    	println("playGround agent killed !");
	    }
	    
	    if (sch!=null)
	    {
	    	sch.end();
	    	killAgent(sch);
	    	println("Robot scheduler killed !!");
	    }
	    if (rwv!=null)
	    {
	    	//playGround.finalReset();
	    	//println("No more World");
	    	killAgent(rwv);
	    	println("RobotWorldViewer agent killed !");
	    }
	}

public final void live()
    {
	createRobotWorld();
	init();
	//	addRobotWorldViewer();

	    addRobotWorldViewer();
	    //RobotWorldViewer2D v = new RobotWorldViewer2D(simulationName,playGround,sch);
	    //launchAgent(v,"Robot World Viewer",true);
        setStop(true);
	launchSimulation();
    }

}
