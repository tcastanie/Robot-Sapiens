package warbot.GGG;

import warbot.kernel.*;
import madkit.kernel.*;

public class GGG_Target {
	double x = 0;
	double y = 0;
	int type = 0;
	AgentAddress id = null;

	public GGG_Target() {
	}

	/*public GGG_Target(double newX, double newY){
		x = newX;
		y = newY;
	}*/

	public GGG_Target(double angle, double dist){
		x = (Math.cos(angleWarbotToRadian(angle))*dist);
		y = (-Math.sin(angleWarbotToRadian(angle))*dist);
	}

	public GGG_Target(Percept p) {
		x = p.getX();
		y = p.getY();
		id = p.getAgent();
	}

	public void somme(GGG_Target t) {
		x = x+t.x;
		y = y+t.y;
	}
	//********************************************************
	// Fonction qui prend un angle Warbot et retourne son équivalent en Radians
	private double angleWarbotToRadian(double angle) {
		return (((-1.0 * Math.PI * angle)/180.0) + 2.0*Math.PI);
	}
	
	//********************************************************
	// Fonction qui prend un angle en Radians et retourne son équivalent en angle Warbot
	private double radianToAngleWarbot(double angle) {
		return (((-180.0 * angle)/Math.PI) + 360.0);
	}
}