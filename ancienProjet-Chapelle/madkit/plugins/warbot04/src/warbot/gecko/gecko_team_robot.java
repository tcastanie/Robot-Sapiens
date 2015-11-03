package warbot.gecko;

/**
 * <p>Titre : Projet warbot</p>
 * <p>Description : </p>
 * <p>Copyright : Copyright gecko team (c) 2004</p>
 * <p>Société : </p>
 * @author non attribuable
 * @version 1.0
 */

public class gecko_team_robot
{
  // Type de robot
  // 0=fortress
  // 1=spy
  // 2=warrior
  int TypeRobot=0;

  // Position relative
  double positionX=0;
  double positionY=0;

  // Points de vie
  int PointsDeVie=0;

  String ID;

  boolean obstacleH=false;
  boolean obstacleB=false;
  boolean obstacleG=false;
  boolean obstacleD=false;

  int id_attaque = 0;

  public int get_id_attaque(){
    return id_attaque;
  }

  public String get_ID(){
    return ID;
  }

  public void set_id_attaque(int i){
    id_attaque = i;
  }

  public gecko_team_robot() {}

}