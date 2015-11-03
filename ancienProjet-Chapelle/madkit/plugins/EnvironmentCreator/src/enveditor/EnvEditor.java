package enveditor;

import madkit.kernel.*;
import java.io.*;
import java.util.*;
import java.awt.*;
import madkit.simulation.activators.*;

//-------
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.*;
import java.util.Vector;

import java.lang.Integer;

/*import javax.swing.JButton;
import javax.swing.JPanel;*/
import javax.swing.*;
//import javax.swing.JTextField;



import madkit.kernel.Agent;
import madkit.kernel.AgentAddress;
import madkit.kernel.Message;
import madkit.kernel.OPanel;
import madkit.messages.ACLMessage;
//-------

public class EnvEditor extends Agent implements ReferenceableAgent
{
	public EnvEditor()
	{

		//monenv=new CreateEnv(640,480);
	}
	
	CreateEnv cenv;
	MenuEnv menv;
  	JButton b;
  	JPanel all,p,m;
  	JLabel jl;
  	GridBagConstraints c;
  	int default_width=640; int default_height=480; // Taille de toute la fenetre
  	JTextField tf_filename,tf_dimx,tf_dimy;	// TextFields : dimensions de l'envt
  	JTabbedPane enved,robed;
	//String filename;		// Nom de la map (ouvrir/sauver)
	int map_dimx,map_dimy;		// dimensions de l'envt
	
    private void MyActionPerformed(java.awt.event.ActionEvent e){
       		//filename=e.getActionCommand();
       		String TmpFilename=tf_filename.getText();
	    if (!TmpFilename.endsWith(".env")) { TmpFilename=TmpFilename.concat(".env"); tf_filename.setText(TmpFilename);}
       		System.out.println("Lancer cette commandeEE:MAP:");
		System.out.println(e.getActionCommand());
    }
    	/*b1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt){
                 MyActionPerformed(evt);
            }
	});*/
      
  public void initGUI()
    {
    	
    	String tmpstring;
	int tempvalue;
	
	System.out.println("GUI Initialisé");
    	map_dimx=320;map_dimy=240;

	all= new JPanel(new BorderLayout());
      	p = new JPanel(new BorderLayout());
      	m = new JPanel(new GridBagLayout());
      	all.add("Center",p);
      	all.add("West",m);
      	
      	//CreateEnv cenv;
	cenv=new CreateEnv(map_dimx,map_dimy);
      	p.add("Center",cenv);
        /*b = new JButton("Clic dessus");
      	b.addActionListener(cenv);
      	p.add("South",b);*/
      	
      	//MenuEnv menv;
	c = new GridBagConstraints();
	c.fill = GridBagConstraints.NONE; // BOTH => maximise la taille des elts
	c.weightx = 1.0;
	c.weighty = 1.0;
	c.gridwidth=GridBagConstraints.REMAINDER; // REMAINDER = ce qu'il reste

	c.weighty = 0;
	menv=new MenuEnv(100,48,this);
	
	//c.gridheight = GridBagConstraints.REMAINDER;
	//c.weighty=2;
	m.add(menv,c);
	
	c.weighty = 1.0;
	c.gridwidth=3;
	b = new JButton("Nouveau");
	b.setActionCommand("newmap");
	b.addActionListener(menv);	
	m.add(b,c);
	
	tmpstring = (new Integer(map_dimx)).toString();
	tf_dimx = new JTextField(tmpstring, 4);
	tf_dimx.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt){
                 MyActionPerformed(evt);
            }
	});
	m.add(tf_dimx,c);
	
	tmpstring = (new Integer(map_dimy)).toString();
	c.gridwidth=GridBagConstraints.REMAINDER;
	tf_dimy = new JTextField(tmpstring, 4);
	tf_dimy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt){
                 MyActionPerformed(evt);
            }
	});
	m.add(tf_dimy,c);
	
	c.gridwidth=3;
	b = new JButton("Effacer");
	b.setActionCommand("clearmap");
	b.addActionListener(menv);	
	m.add(b,c);
	b = new JButton("Mur");
	b.setActionCommand("setpenwall");
	b.addActionListener(menv);	
	m.add(b,c);
	c.gridwidth=GridBagConstraints.REMAINDER;
	b = new JButton("Objet");
	b.setActionCommand("setpenplate");
	b.addActionListener(menv);	
	m.add(b,c);

	c.gridwidth=2;
	b = new JButton("Ouvrir");
	b.setActionCommand("openmap");
	b.addActionListener(menv);	
	m.add(b,c);
	c.gridwidth=GridBagConstraints.REMAINDER;
	b = new JButton("Sauver");
	b.setActionCommand("savemap");
	b.addActionListener(menv);	
	m.add(b,c);
	
	tf_filename = new JTextField("nom de la simu", 16);
	tf_filename.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt){
                 MyActionPerformed(evt);
            }
	});
	m.add(tf_filename,c);
	
	jl = new JLabel("Texte");
	m.add(jl,c);

	b = new JButton("Nouveau chose3");
	b.setActionCommand("nvchose3");
	b.addActionListener(menv);
	m.add(b,c);

	//c.weightx = 1.0;
	c.weighty = 1000.0;
	c.gridheight=GridBagConstraints.REMAINDER; // REMAINDER = ce qu'il reste
	jl = new JLabel("");
	m.add(jl,c);m.add(jl,c);m.add(jl,c);m.add(jl,c);
	
      	//all.setSize(calculatePreferredSize());
      	all.setPreferredSize(new Dimension(640,480));
      	
    	enved = new JTabbedPane();
    	//robed = new JTabbedPane();
    	//ImageIcon icon = createImageIcon("images/open.bmp");
	
	//JComponent panel1 = makeTextPanel("Panel #1");
	enved.addTab("Editer une map", all);
	enved.addTab("Editer un robot", jl);
	//tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
      	
      	setGUIObject(enved);
    }
    
    Dimension calculatePreferredSize()
    {
    	Dimension tmp= new Dimension();
    	int largeur, hauteur, cenvlargeur, cenvhauteur, mlargeur, mhauteur;
    	
    	cenvlargeur = cenv.DIMX+2*cenv.bord+2*cenv.l;
    	cenvhauteur = cenv.DIMY+2*cenv.bord+cenv.h+cenv.l;
    	
    	tmp = m.getSize(tmp);
    	mlargeur = tmp.width;
    	mhauteur = tmp.height;
    	/*largeur = Math.max(m.getPreferredSize().width,cenvlargeur);
    	hauteur = Math.max(m.getPreferredSize().height,cenvhauteur);*/
    	System.out.println("cenv="+cenvlargeur+","+cenvhauteur+" - m="+mlargeur+","+mhauteur);
    	largeur = mlargeur+cenvlargeur;
    	hauteur = mhauteur+cenvhauteur;
    	
    	tmp = new Dimension (largeur,hauteur);
    	return tmp;
    }
    
    public void sendEvent(ActionEvent e)
    {
	System.out.println("Lancer cette commandeEE:");
	System.out.println(e.getActionCommand());
	
	String lacommande=e.getActionCommand();
	if (lacommande.equals("clearmap")) { ClearMap(); }
	if (lacommande.equals("newmap")) { NewMap();}
	if (lacommande.equals("setpenplate")) { setPenPlate();}
	if (lacommande.equals("setpenwall")) { setPenWall();}
	if (lacommande.equals("openmap")) { openMap();}
	if (lacommande.equals("savemap")) { saveMap();}
	
    }
    
    void openMap()
    	{
    	    //TextIn thename= new TextIn(m,"File name","hello.txt");
	    //thename.show();
	    //filename=thename.getS();
	    //thename.dispose();
	    cenv.filename=tf_filename.getText();
	    cenv.load();
	    cenv.repaint();
	}
    void saveMap()
    	{
    	    //TextIn thename= new TextIn(m,"File name","hello.txt");
	    //thename.show();
	    //filename=thename.getS();
	    //thename.dispose();
	    String TmpFilename=tf_filename.getText();
	    if (!TmpFilename.endsWith(".env")) { TmpFilename=TmpFilename.concat(".env"); tf_filename.setText(TmpFilename);}
	    
	    cenv.filename=TmpFilename;
	    cenv.save();
	}
	
    public void setPenPlate()
    {
    	cenv.objectType=cenv.TYPE_PLAQUE;
    /*	            if (objectType==TYPE_OBSTACLE) objectType=TYPE_PLAQUE;
            else if (objectType==TYPE_PLAQUE) objectType=TYPE_OBSTACLE;*/
        System.out.println("Mode dessin = "+ cenv.objectType+"/"+cenv.TYPE_PLAQUE);
        cenv.repaint();
    }

    public void setPenWall()
    {
    	cenv.objectType=cenv.TYPE_OBSTACLE;
    /*	            if (objectType==TYPE_OBSTACLE) objectType=TYPE_PLAQUE;
            else if (objectType==TYPE_PLAQUE) objectType=TYPE_OBSTACLE;*/
        System.out.println("Mode dessin = "+ cenv.objectType+"/"+cenv.TYPE_OBSTACLE);
        cenv.repaint();
    }
    
    public void NewMap(int largeur, int hauteur)
    {
    	Dimension tmp;
    	System.out.println("NewMap ==========");
	System.out.println(tf_dimx.getText());
	System.out.println(tf_dimy.getText());
	
	
	//Object tmp = getGUIObject();
	p.remove(cenv);
	cenv=new CreateEnv(largeur,hauteur);
	p.add("Center",cenv);
	tmp = calculatePreferredSize();
	
	//all.setBoundingRect(0,0,largeur, haut
	//all.reshape(10,10,tmp.width+500,tmp.height+500);
	//p.setSize(new Dimension(400,300));
	//m.setSize(new Dimension(600,600));
	//all.setPreferredSize(new Dimension(800,600));
	//all.setBounds(0,0,800,600);
	cenv.repaint();
	all.repaint();
	//all.setPreferredSize(new Dimension(800,600));
	
    }
    
    public void NewMap(Dimension d)
    { NewMap(d.width,d.height); }

    public void NewMap()
    {
    	Integer tempvalue;
    	int largeur,hauteur;
    	
    	tempvalue = new Integer(tf_dimx.getText());
    	largeur = tempvalue.intValue();
    	
    	tempvalue = new Integer(tf_dimy.getText());
    	hauteur = tempvalue.intValue();
    	
    	map_dimx=largeur;
    	map_dimy=hauteur;
    	NewMap(largeur,hauteur);
    }

    public void ClearMap()
    {
    	int largeur = cenv.DIMX;
    	int hauteur = cenv.DIMY;
    	String tmpstring;
    	Integer tempvalue;
    	
    	map_dimx=largeur;
    	map_dimy=hauteur;
    	
    	tmpstring = (new Integer(largeur)).toString();
    	tf_dimx.setText(tmpstring);
    	tmpstring = (new Integer(hauteur)).toString();
    	tf_dimy.setText(tmpstring);
    	NewMap(largeur,hauteur);
    }

/*
  public void actionPerformed(ActionEvent e)
    {
      //pause=false;
      b.setBackground(Color.gray);
    }
*/

  public void activate()
    {
      println("ec ready");

      createGroup(true,"LBots","Env-Editor",null,null);
      requestRole("LBots","Env-Editor", "broker",null);
    }


  public void live()
  {
        while (true)
	{	Message m = waitNextMessage();
		 int i=0;
		if (i==1) {i=2;}
	}
  }

}