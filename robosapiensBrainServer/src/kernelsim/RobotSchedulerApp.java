package kernelsim;

import madkit.kernel.*;
import madkit.simulation.activators.TurboMethodActivator;

//import java.awt.*;
import java.io.*;
//import java.util.*;
//import smaapp.*;

/** version pour Shaft Problem ***********************************
 */

public class RobotSchedulerApp extends Scheduler
{


 String group;
 RobotAppEnv world;
 static final long serialVersionUID = RobotAppEnv.serialVersionUID; //42L;
    // RobotActivator t1;
 TurboMethodActivator /*sensorObserver,*/robotObserver,robotRunMe,robotSignals,majEnv,robotWatcher,verifPlaques,majDepRob; //,neuronRunMe;
 public int iteration = 0;
 public static boolean stop_and_no_save=false;
 
 //int delay = 100;
 public static int nfic=0;
 StringMessage lastmessage=null;
 public boolean messageread=false;
 
 public int SimulationState=0;
 final static int SIMSTATE_STOP=0;
 final static int SIMSTATE_PLAY=1;
 final static int SIMSTATE_PAUSE=2;
 int delay = 10; //100

 public RobotSchedulerApp(String group,RobotAppEnv w)
	{
	world=w;
	this.group=group;
	setDebug(true);
	//t1 = new RobotActivator(group);
	//addActivator(t1);

	}

 public void activate()
 {
        robotObserver = new TurboMethodActivator("observe",group,"robot observer");
	addActivator(robotObserver);
	
	//sensorObserver = new TurboMethodActivator("observe",group,"sensor observer");
	
	// Depuis le 050630 Fait dans le neuronScheduler
	/*sensorObserver = new TurboMethodActivator("observe","NeuronGroup1","sensor observer");
	addActivator(sensorObserver);*/
	
	robotRunMe = new TurboMethodActivator("runMe",group,"robot");
	addActivator(robotRunMe);
	
	/*
	neuronRunMe = new TurboMethodActivator("runMe","NeuronGroup1","neuron"); // REVIENDRE Crade
	addActivator(neuronRunMe);
	*/
	
        robotSignals = new TurboMethodActivator("signals",group,"robot");
	addActivator(robotSignals);
	majEnv = new TurboMethodActivator("MajEnv",group,"robot world");
	addActivator(majEnv);
        // Simulation des plaques
	verifPlaques = new TurboMethodActivator("verifPlaques",group,"robot world");
	addActivator(verifPlaques);
        majDepRob = new TurboMethodActivator("majDepRob",group,"robot");
	addActivator(majDepRob);
	// 040505
        // ***********************
	robotWatcher = new TurboMethodActivator("watch",group,"robot watcher");
	addActivator(robotWatcher);
        println("Robot scheduler activated");
        if (!isGroup(group)) { createGroup(true, group, null, null); }
        else { requestRole(group, "member", null); }
	//joinGroup(group);
	requestRole(group, "scheduler",null);
 }


 /*void updateObserverActivator()
    {
	try{robotObserver.update();}

	catch (Exception e)
	    {
		System.err.println("t1, simple Activator not initialised"+e);
		e.printStackTrace();
	    }
    }*/


 public void live() //throws ThreadDeath
  {

	//lastmessage= (StringMessage)
	Message m = nextMessage();
	if (m==null)
	{
		waitNextMessage();
	}
	
	println("Scheduler agent living");
	//040708 update();

	while(true)
		    {
		    	    	//System.err.println("sch running");
	
			pause(delay);
			m = nextMessage();
			/*if (m==null)
			{
				m=waitNextMessage();
			}*/
			if (m!=null)
			{	
				lastmessage=(StringMessage)m;
			}
			
			if (lastmessage!=null)
			{	
				if (m!=null)
				{ // Si m!=null ça veut dire que lastmessage = m
				  // Dans ce cas on affiche l'ordre reçue
					messageread=false;
									
					if ((!messageread) &&(lastmessage.getString().compareTo("Pause")==0))
					{	System.out.println("Pause pause pause");
						SimulationState=SIMSTATE_PAUSE; //=2;
						PauseBrains();
						robotRunMe.execute(); // runMe des robots
						messageread=true;
					}
					if ((!messageread) &&(lastmessage.getString().compareTo("Stop")==0 ))
					{	System.out.println("Stop stop stop");
					 	SimulationState=SIMSTATE_STOP; //=0;
					 	PauseBrains();
						robotRunMe.execute(); // runMe des robots
						messageread=true;
					}
					if ((!messageread) &&(lastmessage.getString().compareTo("Play")==0 ))
					{	System.out.println("Play play play");
						SimulationState=SIMSTATE_PLAY; //=1;
						PlayBrains();
 						messageread=true;
					}
					if ((!messageread) &&(lastmessage.getString().compareTo("LoadBrains")==0))
					{
						System.out.println("RSched:Load load load");
						LoadBrain("sav");
						messageread=true;
					}
					if ((!messageread) &&(lastmessage.getString().compareTo("SaveBrains")==0))
					{
						System.out.println("RSched:Save save save");
						SaveBrain("sav");
						messageread=true;
					}
					if (!messageread)
					{ 	System.out.println("RSched:Message not understood: "+lastmessage.getString());
					}
				}
				
				if (SimulationState==SIMSTATE_PLAY)
				{	Play();
		      		} //(SimulationState==SIMSTATE_PLAY)
	                 } // if (lastmessage!=null)
	                } // while(true)
            //rtemp.initialisation(this.world.x,);
            //world.resetAgent(this.iteration);
    }
 	

public void Play()
{
    //if (lastmessage.getString().compareTo("Play")==0 )
	//Play();

    //t1.execute();
    robotRunMe.execute(); // runMe des robots
	/*
		neuronRunMe.execute(); // runMe des neurones
	*/
    robotSignals.execute();
    // Simulation des plaques
    // Les robots qui ont pu bouger, bougent.
    // On verifie que la plque peut bouger:
    verifPlaques.execute(); // RobotAppEnv.java - verifPlaques(...)
    // On bouge les robots n'ayant pu bouger:
    majDepRob.execute(); // RobotBrain.java - majDepRob()
    // ***********
    robotObserver.execute();
    
    majEnv.execute();
    //robotWatcher.execute();
    iteration++;
    /*if ((iteration%2)==0)
    {*/
    //	sensorObserver.execute();
    /*}*/
    //if (world.endSeq(iteration)) { world.resetAgent(iteration); iteration=0; }
    if (world.endSeq(iteration)) {
      if (!stop_and_no_save)
      {
        System.out.println("************************************");
        System.out.println(" Fin de simulation :");
        System.out.print(" - Reset ...");
        world.resetAgent(iteration);
        System.out.println(" Ok !");
        System.out.print(" - Sauvegarde ...");
        SauveMatrices("sav");
        System.out.println(" Ok !");
        iteration=0;
        System.out.println("************************************");
       }
      }
    if (stop_and_no_save)
      {
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        System.out.println(" STOP AND NO SAVE ");
        System.out.print(" - Reset ...");
        world.resetAgent(iteration);
        world.LesPlaques.ResetAll();
        iteration=0;
        stop_and_no_save=false;
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
      }
	   

}

    public void PauseBrains()
    {
            RobotAppPhy rtemp;
            for (int i=0;i<RobotAppPhy.otherRobots.size();i++)
            { rtemp=(RobotAppPhy)RobotAppPhy.otherRobots.elementAt(i);
              sendMessage(rtemp.getAddress(), new StringMessage("PauseBrain"));
              System.out.println("RmcLA:PauseBrain message sent to RAPhy");
              //rtemp.SaveBrain("brainc52");
            }
            //System.out.println("RobotAppPhy.otherRobots.size()="+RobotAppPhy.otherRobots.size());
    }

    public void PlayBrains()
    {
            RobotAppPhy rtemp;
            for (int i=0;i<RobotAppPhy.otherRobots.size();i++)
            { rtemp=(RobotAppPhy)RobotAppPhy.otherRobots.elementAt(i);
              sendMessage(rtemp.getAddress(), new StringMessage("PlayBrain"));
              System.out.println("RmcLA:PlayBrain message sent to RAPhy");
              //rtemp.SaveBrain("brainc52");
            }
            //System.out.println("RobotAppPhy.otherRobots.size()="+RobotAppPhy.otherRobots.size());
    }
    
    public void SaveBrain(String name)
    {
            RobotAppPhy rtemp;
            for (int i=0;i<RobotAppPhy.otherRobots.size();i++)
            { rtemp=(RobotAppPhy)RobotAppPhy.otherRobots.elementAt(i);
              sendMessage(rtemp.getAddress(), new StringMessage("SaveBrain"));
              System.out.println("RmcLA:SaveBrain message sent to RAPhy");
              //rtemp.SaveBrain("brainc52");
            }
            //System.out.println("RobotAppPhy.otherRobots.size()="+RobotAppPhy.otherRobots.size());
    }

    public void LoadBrain(String name)
    {
            RobotAppPhy rtemp;
            for (int i=0;i<RobotAppPhy.otherRobots.size();i++)
            { rtemp=(RobotAppPhy)RobotAppPhy.otherRobots.elementAt(i);
              sendMessage(rtemp.getAddress(), new StringMessage("LoadBrain"));
              System.out.println("RmcLA:LoadBrain message sent to RAPhy");
              //rtemp.LoadBrain("brainc52");
            }
            
    }

    public void SauveMatrices(String name)
    {
            RobotAppPhy rtemp;
            nfic++;
            for (int i=0;i<RobotAppPhy.otherRobots.size();i++)
            {
              rtemp=(RobotAppPhy)RobotAppPhy.otherRobots.elementAt(i);
              SauveMatrice("MAct",rtemp);
            }
    }

    public void SauveMatrice(String name,RobotAppPhy r)
    {
      if (!RmcLauncherApp.is_applet)
      try{
          FileWriter fw;
          if (nfic/100>=1)
            fw=new FileWriter(nfic+"_"+name+"_"+RmcLauncherApp.simNameCopy+"_"+r.N_agent+".txt");
            else
              if (nfic/10>=1)
                fw=new FileWriter("0"+nfic+"_"+name+"_"+RmcLauncherApp.simNameCopy+"_"+r.N_agent+".txt");
                else
                  fw=new FileWriter("00"+nfic+"_"+name+"_"+RmcLauncherApp.simNameCopy+"_"+r.N_agent+".txt");
          //fw.write("Hello!");

           for (int i=0;i<27;i++)
           { if (i==0) fw.write("Agent("+r.N_agent+");");
             fw.write("v("+i+");");
           }
           fw.write("\n");
           for (int i=0;i<6;i++)
            {
              for (int j=0;j<27;j++)
                {
                  if (j==0) fw.write(i+";");
                  if ((r.MAct[i][j][1]*1000)!=0)
                    fw.write(""+((double)((int)(r.MAct[i][j][0]*1000))) /1000+";");
                  else fw.write(";");
                }
              fw.write("\n");
            }
           fw.write("\n");
           for (int i=0;i<27;i++)
           { if (i==0) fw.write("Agent("+r.N_agent+");");
             fw.write("n("+i+");");
           }
           fw.write("\n");
           for (int i=0;i<6;i++)
            {
              for (int j=0;j<27;j++)
                {
                  if (j==0) fw.write(i+";");
                  if ((r.MAct[i][j][1]*1000)!=0)
                    fw.write((int)r.MAct[i][j][1]+";");
                  else fw.write(";");
                }
              fw.write("\n");
            }

            // Ecriture d'infos
            fw.write("\n");
            fw.write("No Agent;"+r.N_agent+"\n");
            fw.write("Type:;");
            if (r.RobotType==r.TYPE_DECOUPEUR) fw.write("Decoupeur;\n");
            if (r.RobotType==r.TYPE_POUSSEUR) fw.write("Pousseur;\n");
            fw.write("SimName:;"+RmcLauncherApp.simNameCopy+";\n");
            fw.write("Nb dec:;"+RmcLauncherApp.NDecCopy+";\n");
            fw.write("Nb pouss:;"+(RmcLauncherApp.NRobCopy-RmcLauncherApp.NDecCopy)+";\n");
            fw.write("NbIter:;"+iteration+";\n");
          fw.close();
         }
      catch (Exception e)
      {	RmcLauncherApp.is_applet=true;
      	e.printStackTrace();
      }
    }
  
  public void end()
  {
	 //leaveRole(simulationGroup,"neuron");
	 leaveGroup(group);
  }
}

