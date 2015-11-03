/*
* BasicBody.java -Warbot: robots battles in MadKit
* Copyright (C) 2000-2002 Fabien Michel, Jacques Ferber
*
* This program is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License
* as published by the Free Software Foundation; either version 2
* of the License, or any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
*/
package warbot.kernel;

import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.Hashtable;

import javax.swing.ImageIcon;

import madkit.kernel.AbstractAgent;
import madkit.kernel.Utils;

/** The abstract body class. All brain are linked to a subclass of it
  @version 0.1
  @author Fabien Michel
  */

public abstract class BasicBody extends MovableEntity implements Body //, InvocationHandler
{


/////////////////////////////////////////////////////////////// VARIABLES

// The linked brain
Brain myBrain;
transient Hashtable methods=new Hashtable(20);

//physic variables
protected int maximumEnergy;

//private variables
final protected static int EAT=2;	//ACTION DESCRIPTION
Entity eatWhat=null;

final protected static int TAKE=3;	//ACTION DESCRIPTION
final protected static int DROP=4;	//ACTION DESCRIPTION

public static final String[] ACTIONS={"Nothing","Move","Eat","Take","Drop","Nothing","Nothing","Nothing","Nothing","Nothing",
                                    "Build rocket","Launch rocket"};

 Bag myBag;
private int dropIndex=-1;
private Entity takeWhat;

//"what's up" variables
//boolean eating=false;

//gif location
String gifLocation="default";
void setGifLocation(String location){gifLocation=location;}



BasicBody(WarbotEnvironment theWorld,Brain b,String name,String team,int radius,int nrg,int detectingRange)
{
	super(theWorld,name,team,radius,nrg,detectingRange);
	maximumEnergy = nrg;
	myBag=new Bag(1000);
	myBrain=b;
}

BasicBody(){
	myBag=new Bag(1000);
}

public Percept makePercept(double dx, double dy, double d){
	   Percept p = super.makePercept(dx,dy,d);
	   p.setPerceptType("Robot");
	   return p;
}

public void  createDefaultImage()
{
	if (gifLocation==null || gifLocation.equals("default"))
		super.createDefaultImage();
	else
	{
		java.net.URL url = null;
		try
		{
			url = new java.net.URL(gifLocation);
			myGif = new ImageIcon(Toolkit.getDefaultToolkit().createImage(url).getScaledInstance(radius*2,radius*2,Image.SCALE_SMOOTH));
		}
		catch(Exception e)
		{
			System.err.println("invalid gif url");
			super.createDefaultImage();
		}
	}
}

  /* show messages as a line */
  void showLine(double x, double y, double fromX, double fromY, Color c){
          /// raah, the terrible hack!! accessing a graphics from a Body!! Sorry dears...
          ((GBasicBody)getGObject()).showLine((int)x,(int)y,(int)fromX,(int)fromY,c);
  }

  boolean showMessages=true;
  public void setShowMessages(boolean b){showMessages=b;}
  public boolean isShowMessages(){return showMessages;}
  public void toggleShowMessages(){
        showMessages=!showMessages;
  }

/** Displays the graphic representation of a basic body. Displays also its detection range if asked.
 */
/*public void draw(Graphics g, JPanel pane){
    super.draw(g,pane);
    if(showDetect) {
		g.setColor(Color.black);
    	g.drawOval(xdisplay,ydisplay,displaySize,displaySize);
        g.setColor(Color.red);
        g.drawOval(xcor()-getDetectingRange()-getRadius(),ycor()-getDetectingRange()-getRadius(),getDetectingRange()*2+getRadius()*2,getDetectingRange()*2+getRadius()*2);
    }
    if (showEnergyLevel){
        if (initialEnergy == 0)
            initialEnergy = energy;
        drawEnergyLevel(g, energy, initialEnergy);
    }
} */

////////////////////////////////////// access methods
final void setBrain(Brain theBrain){myBrain=theBrain;}
final Brain getBrain(){return myBrain;}

final String getBrainClassName(){
    if (getBrain() != null){
        return(getBrain().getClass().getName());
    } else
        return null;
}

  protected String brainClass;

  /** set the brain class name of an entity. The brain
      generally derives from AbstractAgent. */
  public void setBrainClass(String s){brainClass = s;}

  /** Get the brain class name of an entity which
      generally derives from AbstractAgent. */
  public String getBrainClass(){return brainClass;}

  protected String behaviorFileName=null;

  /** Set the behavior file name attached to the brain class
   *  used for scripting the behavior.
   */
  public void setBehaviorFileName(String s){
	  behaviorFileName=s;
  }

  /** Return the behavior file name attached to the brain class
   *  used for scripting.
   */
  public String getBehaviorFileName(){return behaviorFileName;}

  /** 	Creates the brain and launches if it is an agent.
  *		The brain class is given as a String. The name argument is used to instantiate
  *		the name of the corresponding agent. If the gui flag is true, a bean is created
  *		and associated to this agent.
  */
  public void makeBrain(String className, String name, boolean gui,
                        String behaviorFileName){
  	try {
  		Class c;
  		c = Utils.loadClass(className);
	    myBrain = (Brain) c.newInstance();
	    myBrain.setBody(this);
	    if (myBrain instanceof AbstractAgent){
	    	String n=name;
	    	if (n == null){
	    		n = getLabel();
	    	}
	    	if (n == null){
	    		n = getID();
	    	}
			if (behaviorFileName != null)
			  setBehaviorFileName(behaviorFileName);
			myBrain.init();
	    	getStructure().getAgent().doLaunchAgent((AbstractAgent) myBrain,n,gui);
	    }
	}
	catch(ClassNotFoundException ev){
	    System.err.println("Class not found :" + className + " "+ev);
	    ev.printStackTrace();
	}
	catch(InstantiationException e){
	    System.err.println("Can't instanciate ! " + className +" "+e);
	    e.printStackTrace();
	}
	catch(IllegalAccessException e){
	    System.err.println("illegal access! " + className+" "+e);
	    e.printStackTrace();
	}
 }

 /* Initialization of a mobile entity.
  *	 Takes its brain class and tries to instantiate an
  *	 agent with this brain class if it is not null.
  *	 Sets its speed = speedMax.
  */
  public void init(){
  		//setSpeed(getMaxSpeed());
        super.init();
  		String s = getBrainClass();
  		if (s != null){
  			makeBrain(s, null, false,getBehaviorFileName());
            myBrain.requestRole(this.getTeam(),this.getName(),null);
            int r = myBrain.requestRole(simulationGroup,"brain",WarbotIdentifier.password);
  		}
  }

  //// User message

  boolean showUserMessage=false;
  public void setShowUserMessage(boolean b){showUserMessage=b;}
  public boolean isShowUserMessage(){return showUserMessage;}
  public void toggleShowUserMessage(){
    showUserMessage=!showUserMessage;
  }

  String userMessage;
  public void setUserMessage(String s){userMessage=s;}
  public String getUserMessage(){return userMessage;}


///////////////////////////////////////////////////////////////////////////////////////////////////////

//////////////////////////////////////  SYSTEM METHODS
final public void delete()
{
	System.out.println("I'm dead !!! ");
	if(myBrain != null)
		myBrain.delete();
    super.delete();
}

/*
////////////////////////////////////// PROXY MECHANISM

//interface InvocationHandler
final public Object invoke(Object proxy, Method method, Object[] args)
  {
  	Method m = null;
  	if(methods.contains(method))
  	{
		m = (Method) methods.get(method);
	}
	else
		try
		{
			m = getClass().getMethod(method.getName(),method.getParameterTypes());
			methods.put(method,m);
            //System.out.println(">> " + this + ", found method: " + method.getName());
		}
	  	catch(Exception e){
            //System.err.println("method unreachable");
            System.err.print(":: Body of " + myBrain.getName()+" don't know how to "+method.getName()+"( ");
        }

	try
	{
		return m.invoke(this,args);
	}
  	catch(Exception e)
  	{
 	  	Class[] param;
 		param=method.getParameterTypes();
  		System.err.print(":: Body of " + myBrain.getName()+" : error, while performing "+method.getName()+"( ");
 		for(int j=0; j<param.length; j++)
  			System.out.print(param[j].getName()+" ");
  		if(args!=null)
	 		for(int i=0;i<args.length;i++)
  				System.err.print(args[i].getClass().getName()+" ");
		System.err.println(")");
        e.printStackTrace();
  	}
  	return null;
  }
*/
/////////////////////////////////////	 ACTION MECHANISM
  void doAction()
  {
  	switch(action)
  	{
  		case MOVE:tryMove();break;
  		case EAT:tryEat();break;
		case TAKE:tryPutInMyBag();break;
  		case DROP:putOutOfMyBag(dropIndex);break;
	}
	takeWhat=null;
  }


////////////////////////////////////     PARTIAL BODY INTERFACE 5 + 3(super classes) = 7 functions
final public void die()
{
	delete();

}

private Percept[] percepts =new Percept[0]; // initially the percepts are empty

protected void setPercepts(Percept[] plst){percepts=plst;}

public Percept[] getPercepts()
{
	// return myWorld.getPerception(this);
	return percepts;
}

void createPerception(){
	 myWorld.makePerception(this);
}

public void move()
{
	action=MOVE;
}


public void eat(Food f)
{
 	action=EAT;
  	eatWhat=f.getEntity();
}

public int getMaximumEnergy(){ return maximumEnergy;}

////////////////////////////////////////////////internal methods
/*protected void tryMove()
{
	if (energy>0)
	{
		if (myWorld.authorizeMove(this,newX(),newY()))
  		{
  			doPhysicalMove();
  			moving=true;
  		}
   	}
}*/

protected void tryEat()
{
	/*System.err.println(this.toString());
	System.err.println(eatWhat.toString()); */
	//System.err.println(":: distance  "+distanceFrom(eatWhat));
	if (distanceFrom(eatWhat) < 3)
  	{
  		increaseEnergyLevel(eatWhat.getEnergy());
  		//eating=true;
        eatWhat.delete();
  	}
  	eatWhat=null;
  }

protected void increaseEnergyLevel(int v)
  {
  	energy += v;
  	if (energy>maximumEnergy)
  		energy=maximumEnergy;
  }

  //////////////////////////////////////// internal methods
void tryPutInMyBag()
{
	//System.err.println("dist reel"+distanceFrom(takeWhat));
	if(takeWhat!=null && myWorld.contains(takeWhat) && takeWhat instanceof Hamburger && distanceFrom(takeWhat) < 2 && myBag.put(takeWhat)){
		takeWhat.delete();
		myBag.put(takeWhat);
	}
}

private void putOutOfMyBag(int index)
{
	if(myBag.size()>=index)
	{
		Entity e=myBag.get(index);
		if(e != null && dropEntity(e,0))
			myBag.remove(index);
	}
}

public void take(Food p)
{
	takeWhat=p.getEntity();
	action=TAKE;
}
  ////////////////////////////////////     PARTIAL BODY INTERFACE 6 functions


public Percept[] inMyBag()
{
	Entity[] ents= myBag.returnContent();
	Percept[] percepts=new Percept[ents.length];
	for(int i=0;i<ents.length;i++){
			percepts[i]=ents[i].makePercept(0,0,0);
	}
/*		if (ents[i] instanceof Hamburger)
			percepts[i]=new Food(0,0,0,null,ents[i].getEnergy());
		else
			percepts[i]=null;
*/
	return percepts;
}

public boolean isMyBagEmpty(){return myBag.isEmpty();}

public boolean isMyBagFull(){
    return myBag.isFull();
}

public int getBagCapacity(){return myBag.capacity;}
public int nbOfObjectsInMyBag(){return myBag.size();}

public void drop(int index)
{
	action = DROP;
	dropIndex = index;
}

// for
public void launchRocket(double direction){
    System.out.println(":: " + "LaunchRocket" + " not accessible from " + this);
}


/**try to do the action of building a rocket for future launch*/
public void buildRocket(){
    System.out.println(":: " + "buildRocket" + " not accessible from " + this);
}

public int getRocketNb(){
    System.out.println(":: " + "getRocketNb" + " not accessible from " + this);
    return 0;
}

}
