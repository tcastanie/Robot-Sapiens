package warbot.gecko;

import warbot.kernel.*;
import java.util.Vector;

public class gecko_team_fortress extends Brain
{
  // Liste des rocket launchers
  Vector warriors = new Vector();
  int NbreWarrior=0;

  // Liste des explorateurs
  Vector spys = new Vector();
  int NbreSpy=0;

  // Liste des bases
  Vector fortresses = new Vector();
  int NbreFortress=1;

  boolean obstacleH=false;
  boolean obstacleB=false;
  boolean obstacleG=false;
  boolean obstacleD=false;

  public gecko_team_fortress(){}

  public void activate()
  {
    this.showUserMessage(true);
    createGroup(false,getTeam(),null,null);
    broadcast(getTeam(),"warrior","ExisteWarrior?","fortress");
    broadcast(getTeam(),"spy","ExisteSpy?","fortress");
    broadcast(getTeam(),"fortress","ExisteFortress?","fortress");
    requestRole(getTeam(),"fortress",null);
  }

  public void end()
  {
    broadcast(getTeam(),"warrior","FortressMort");
    broadcast(getTeam(),"fortress","FortressMort");
    broadcast(getTeam(),"spy","FortressMort");
    println("ID Dead (me-fortress) : "+getAddress().getLocalID());
  }

  private void gestion_messages()
  {
    WarbotMessage wm;
    gecko_team_robot r;

    while (!isMessageBoxEmpty())
    {
      wm=readMessage();

      if(wm.getAct().equals("RAZPositions"))
      {
	Vector warriors = new Vector();
	NbreWarrior=0;

	Vector spys = new Vector();
	NbreSpy=0;

	Vector fortresses = new Vector();
	NbreFortress=1;

	broadcast(getTeam(),"warrior","FortressExiste","fortress");
	broadcast(getTeam(),"spy","FortressExiste","fortress");
	broadcast(getTeam(),"fortress","FortressExiste","fortress");

	break;
      }

      if(wm.getAct().equals("WarriorExiste"))
      {
	NbreWarrior++;
	r=new gecko_team_robot();
	r.positionX=wm.getFromX();
	r.positionY=wm.getFromY();
	r.ID=wm.getSender().getLocalID();
	warriors.add(r);
      }
      else if(wm.getAct().equals("WarriorMort"))
      {
	NbreWarrior--;
	for(int i=0;i<spys.size();i++)
	{
	  r=(gecko_team_robot)spys.get(i);
	  if(r.ID.equals(wm.getSender().getLocalID()))
	  {
	    println("ID Dead (warrior) : "+r.ID);
	    warriors.remove(i);
	    break;
	  }
	}
      }
      else if(wm.getAct().equals("SpyExiste"))
      {
	NbreSpy++;
	r=new gecko_team_robot();
	r.positionX=wm.getFromX();
	r.positionY=wm.getFromY();
	r.ID=wm.getSender().getLocalID();
	spys.add(r);
      }
      else if(wm.getAct().equals("SpyMort"))
      {
	NbreSpy--;
	for(int i=0;i<spys.size();i++)
	{
	  r=(gecko_team_robot)spys.get(i);
	  if(r.ID.equals(wm.getSender().getLocalID()))
	  {
	    println("ID Dead (spy) : "+r.ID);
	    spys.remove(i);
	    break;
	  }
	}
      }
      else if(wm.getAct().equals("ExisteFortress?"))
      {
	send(wm.getSender(),"FortressExiste");
	if(wm.getArg1().equals("warrior"))
	{
	  NbreWarrior++;
	  r = new gecko_team_robot();
	  r.positionX = wm.getFromX();
	  r.positionY = wm.getFromY();
	  r.ID = wm.getSender().getLocalID();
	  warriors.add(r);
	}
	else if(wm.getArg1().equals("spy"))
	{
	  NbreSpy++;
	  r = new gecko_team_robot();
	  r.positionX = wm.getFromX();
	  r.positionY = wm.getFromY();
	  r.ID = wm.getSender().getLocalID();
	  spys.add(r);
	}
	else if(wm.getArg1().equals("fortress"))
	{
	  NbreFortress++;
	  r = new gecko_team_robot();
	  r.positionX = wm.getFromX();
	  r.positionY = wm.getFromY();
	  r.ID = wm.getSender().getLocalID();
	  fortresses.add(r);
	}
      }
      else if(wm.getAct().equals("FortressExiste"))
      {
	if(!wm.getSender().getLocalID().equals(getAddress().getLocalID()))
	{
	  NbreFortress++;
	  r = new gecko_team_robot();
	  r.positionX = wm.getFromX();
	  r.positionY = wm.getFromY();
	  r.ID = wm.getSender().getLocalID();
	  fortresses.add(r);
	}
      }
      else if(wm.getAct().equals("FortressMort"))
      {
	NbreFortress--;
	for(int i=0;i<fortresses.size();i++)
	{
	  r=(gecko_team_robot)spys.get(i);
	  if(r.ID.equals(wm.getSender().getLocalID()))
	  {
	    println("ID Dead (fortress) : "+r.ID);
	    fortresses.remove(i);
	    break;
	  }
	}
      }

    }

  }


  public void doIt()
  {

    double[][] positionsObstacles={{0},{0}};
    boolean obstacleH=false;
    boolean obstacleB=false;
    boolean obstacleG=false;
    boolean obstacleD=false;


    Percept[] percepts=getPercepts();
    for(int i=0;i<percepts.length;i++)
    {
      Percept percept=percepts[i];

      // getPerceptType()
      //
      // Obstacle        Wall (or PalmTree)
      // Food            Hamburger
      // Rocket          Rocket
      // Explorer        Explorer
      // RocketLauncher  RocketLauncher
      // Home            Home


      if (percept.getPerceptType().equals("Obstable"))
      {
	println(getAddress().getLocalID()+" ->"+percept.getPerceptType()+" at "+percept.getX()+" "+percept.getY());
	/*
			    if(!e.getTeam().equals(getTeam())){
		setUserMessage("destroying, Urg, Urg..");
				    launchRocket(towards(e.getX(),e.getY()));
				    setHeading(towards(e.getX(),e.getY()));
				    waitingForRocket=waitingMax;
				    return;
			    }
		    }
		    // tuer les autres... waouh!!!
		    if ((e.getPerceptType().equals("RocketLauncher") ||
			e.getPerceptType().equals("Explorer")) &&
			    (waitingForRocket<=0) &&
			    (!e.getTeam().equals(getTeam()))){
		setUserMessage("shooting, Urg, Urg..");
				    launchRocket(towards( e.getX(),e.getY()  ));
				    waitingForRocket=waitingMax;
				    return;
		    }
	    }
    }
    int min = 0;
    for(int i=0;i< detectedEntities.length;i++)
    {   Percept e=detectedEntities[i];
	    if (distanceTo(e) < distanceTo(detectedEntities[min]) && e.getPerceptType().equals("Food"))
		    min=i;
    }
    // bouffer, miam, miam
    if(detectedEntities.length > 0 && detectedEntities[min].getPerceptType().equals("Food")){
	    if(distanceTo(detectedEntities[min]) < 2)
	    {
		    eat((Food)detectedEntities[min]);
		    return;
	    }
	    else
	    {
		    setHeading(towards(detectedEntities[min].getX(),detectedEntities[min].getY()));
		    move();
		    return;
*/
      }
    }


    gestion_messages();

  }

}
