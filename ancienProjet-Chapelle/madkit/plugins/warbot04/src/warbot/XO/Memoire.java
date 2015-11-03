package warbot.XO;

import java.lang.*;
import java.util.*;
import java.io.*;
import warbot.kernel.*;
import madkit.kernel.*;

public class Memoire {
	
	boolean pB = false;
	double[][] mem = new double[10][2];
	String[] stockGoals = new String[4];
	double[][] stockBases = new double[20][2];
	String hG = null;
	int val1 = 0;
	int val2 = 1;
	int val3 = 2;
	int val4 = 3;
	
	/* dans l'ordre : (x1, y1)--->ma base
			  (x2, y2)--->base ennemie
			  (x3, y3)--->guerrier à aider
			  (x4, y4)--->guerrier à détruire
			  goals[4]--->tableau de buts
			  helpGuerrier--->addresse du guerrier à aider
			  perceptBase--->savoir si la base à attaquer est perçue
			  basesEnnemi--->position de toutes les bases ennemies perçues*/
	public Memoire(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4, String[] goals, String helpGuerrier, boolean perceptBase){
		mem[val1][0] = x1;
		mem[val1][1] = y1;
		mem[val2][0] = x2;
		mem[val2][1] = y2;
		mem[val3][0] = x3;
		mem[val3][1] = y3;
		mem[val4][0] = x4;
		mem[val4][1] = y4;
		
		for(int i=0; i<4; i++){stockGoals[i]=goals[i];}
		hG = helpGuerrier;
		pB = perceptBase;
	}
	
	public Memoire(double x2, double y2, double x3, double y3, double[][] basesEnnemi){
		mem[val2][0] = x2;
		mem[val2][1] = y2;
		mem[val3][0] = x3;
		mem[val3][1] = y3;
		for(int i=0; i<20; i++){
			for(int j=0; j<2; j++){
				stockBases[i][j]=basesEnnemi[i][j];
			}
		}
	}
	
	public Memoire(double x1, double y1, double x2, double y2){
		mem[val1][0] = x1;
		mem[val1][1] = y1;
		mem[val2][0] = x2;
		mem[val2][1] = y2;
	}
	
	public boolean getPerceptBase(){
		return pB;
	}
	
	public void setPerceptBase(boolean perceptBase){
		pB = perceptBase;
	}
	
	public String getHelpGuerrier(){
		return hG;
	}
	
	public void setHelpGuerrier(String helpGuerrier){
		hG = helpGuerrier;
	}
	
	public void saveGoals(String[] goals){
		for(int i=0; i<stockGoals.length; i++){stockGoals[i]=goals[i];}
	}
	
	public String[] getGoals(){
		return stockGoals;
	}
	
	public void saveBases(double[][] basesEnnemi){
		for(int i=0; i<stockBases.length; i++){
			for(int j=0; j<2; j++){
				stockBases[i][j]=basesEnnemi[i][j];
			}
		}
	}
	
	public double getXposbase(){
		double xpb = mem[val1][0];
		return xpb;
	}
	
	public double getYposbase(){
		double ypb = mem[val1][1];
		return ypb;
	}
	
	public void setXposbase(double x){
		mem[val1][0] = x;
	}
	
	public void setYposbase(double y){
		mem[val1][1] = y;
	}
	
	public double getXBaseEnnemie(){
		double x = mem[val2][0];
		return x;
	}
	
	public double getYBaseEnnemie(){
		double y = mem[val2][1];
		return y;
	}
	
	public void setXBaseEnnemie(double x){
		mem[val2][0] = x;
	}
	
	public void setYBaseEnnemie(double y){
		mem[val2][1] = y;
	}
	
	public double getHelpX(){
		double x = mem[val3][0];
		return x;
	}
	
	public double getHelpY(){
		double y = mem[val3][1];
		return y;
	}
	
	public void setHelpX(double x){
		mem[val3][0] = x;
	}
	
	public void setHelpY(double y){
		mem[val3][1] = y;
	}
	
	public double getXGuerrierEnnemi(){
		double x = mem[val4][0];
		return x;
	}
	
	public double getYGuerrierEnnemi(){
		double y = mem[val4][1];
		return y;
	}
	
	public void setXGuerrierEnnemi(double x){
		mem[val4][0] = x;
	}
	
	public void setYGuerrierEnnemi(double y){
		mem[val4][1] = y;
	}
}
