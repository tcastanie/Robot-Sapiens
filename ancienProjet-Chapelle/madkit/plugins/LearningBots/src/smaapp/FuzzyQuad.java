package smaapp;
import java.lang.String;

public class FuzzyQuad {

	//public float a_noyau,a_seuil,b_noyau,b_seuil;
	public FuzzyPair a,b;
	
	private float b_pre_noyau=-1; //(2*fq.a.seuil-fq.a.noyau);
	private float c_pre_noyau=-1; //(2*fq.b.seuil-fq.b.noyau);
	
	public FuzzyQuad(float a_n,float a_s,float b_n, float b_s)
	{
		a = new FuzzyPair(a_n,a_s);
		b = new FuzzyPair(b_n,b_s);
		//a_noyau=a_n; a_seuil=a_s;b_noyau=b_n;b_seuil=b_s;
	}
	
	public FuzzyQuad(FuzzyPair fpa, FuzzyPair fpb)
	{	a = new FuzzyPair(fpa.noyau,fpa.seuil);
		b = new FuzzyPair(fpb.noyau,fpb.seuil);
		//a.noyau=fpa.noyau; a.seuil=fpa.seuil;b.noyau=fpb.noyau;b.seuil=fpb.seuil;
	}
	/*
	public FuzzyQuad()
	{
		a=-1;b=-1;c=-1;d=-1;
	}*/

	public float b_pre_noyau()
	{
		if (b_pre_noyau==-1)
		{
			b_pre_noyau=(2*a.seuil-a.noyau);
		}
		return b_pre_noyau;
	}
	
	public float c_pre_noyau()
	{
		if (c_pre_noyau==-1)
		{
			c_pre_noyau=(2*b.seuil-b.noyau);
		}
		return c_pre_noyau;
	}
	
	public String toString()
	{	
		return "("+a.noyau+","+a.seuil+","+b.noyau+","+b.seuil+","+")";
	}
}
