package warbot.gecko;

import warbot.kernel.*;
import java.util.Vector;

public class gecko_team_spy extends Brain
{
  boolean goalReached=false;
  int compt=0;
public gecko_team_spy(){	}

public void activate()
{
 randomHeading();
// println("I am an ant like robot");
// println("I am heading " + getHeading());
this.showUserMessage(true);
}

// simple procs that may be used
void dropAll(){
  for (int i=0;i<bagSize();i++)
          drop(i);
}

void takeFood(Food p){
  if(distanceTo(p) < 2){
     if(Math.random()<.1) eat((Food)p);
         else  take((Food) p);
  } else {
     setHeading(towards(p.getX(),p.getY()));
         move();
  }
}

public void doIt()
{
  double direction;


 if(! isMoving())
 {
         randomHeading();
         move();
         return;		//exit doIt to avoid doing something else
 }
 if(isBagFull())
         goalReached=true;
 if(isBagEmpty())
         goalReached=false;
 if(goalReached)
 {
         drop(0);
         return;
 }
 Percept[] percepts = getPercepts();
 Percept[] myBag=null;

 if (isBagEmpty())
 setUserMessage(null);
else
 setUserMessage("bag: "+ bagSize());

Percept perceptFood=null;
Percept perceptHome=null;

 if(percepts.length > 0){
// compute indexes from percepts
   for(int i=0;i<percepts.length;i++){
         Percept p = percepts[i];
         if (p.getTeam().equals("gecko team")){
           direction = towards(-p.getX() + (Math.random()*15),-p.getY() + (Math.random()*15));
           setHeading(direction);
           move();
           return;
         }

         if ((!p.getTeam().equals("gecko team")) && ((p.getPerceptType().equals("Home")))){
           if(compt == 0)
           {
             broadcast(getTeam(),"warrior","DirectionBaseEnnemie",Double.toString(getHeading()),Double.toString(towards(p.getX(),p.getY())));
             compt++;
           }
           else if(compt < 60)
             {compt++;
             println(Integer.toString(compt));}
           else
             compt=0;

           //move();
           //return;
         }


         String pType= p.getPerceptType();
         if (pType.equals("Home") && (perceptHome == null))
    perceptHome = p;
 else if (pType.equals("Food") && (perceptFood == null))
            perceptFood = p;
     }
 // use indexes to control behavior
 if ((perceptHome !=null) && (perceptHome.getTeam().equals(getTeam())) && isBagEmpty()){
     setHeading(towards(-perceptHome.getX(),-perceptHome.getY()));
     setUserMessage("leaving base");
     move();
     return;
 }
 if ((perceptFood !=null) && (perceptHome == null)){
     setUserMessage("miam");
     takeFood((Food) perceptFood);
     return;
 }
 if ((perceptHome !=null) && (perceptHome.getTeam().equals(getTeam())) && !isBagEmpty()){
     if(distanceTo(perceptHome) < 2){
             setUserMessage("drop all");
             dropAll();
             return;
     } else {
             setHeading(towards(perceptHome.getX(),perceptHome.getY()));
             setUserMessage("going to base");
             move();
             return;
     }
 }
}
move();
}


}
