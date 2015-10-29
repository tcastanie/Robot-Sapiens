package smaapp;
import java.lang.String;

public class FuzzyPair {

	public float noyau;
	public float seuil;
	
	public FuzzyPair(float noy,float seui)
	{
		noyau=noy;
		seuil=seui;
	}
	
	public FuzzyPair()
	{
		noyau=0;
		seuil=0;
	}
	public String toString()
	{	//String s="("+noyau+","+seuil+")";
		return "("+noyau+","+seuil+")";
	}
}
