package warbot.XO;


public class Obstacle {
	
	double x,y;
	double power;
	int life,maxlife;
	String type;
	
	
	Obstacle(double x1, double y1, String type1) {
		x = x1;
		y = y1;
		type = type1;
	}

	Obstacle(double x1, double y1, int life1, String type1) {
		x = x1;
		y = y1;
		life = life1;
		type = type1;
	}

	Obstacle(double x1, double y1, int life1, double p, String type1) {
		x = x1;
		y = y1;
		life = life1;
		power = p;
		type = type1;
	}

	Obstacle(double x1, double y1, int life1, int maxlife1, double p, String type1) {
		x = x1;
		y = y1;
		life = life1;
		maxlife = maxlife1;
		power = p;
		type = type1;
	}


	public void setX(double x1) {
		x = x1;
	}
	
	public void setY(double y1) {
		y = y1;
	}

	public void setLife(int l) {
		life = l;
	}

	public void setMaxLife(int l) {
		maxlife = l;
	}

	public void setPower(double p) {
		power = p;
	}
	
	public void setType(String type1) {
		type = type1;
	}
	
	public int getLife() {
		return life;
	}

	public int getMaxLife() {
		return maxlife;
	}

	public double getPower() {
		return power;
	}

	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public String getType() {
		return type;
	}
	
}