package smaapp;

import java.util.Vector;

public class FuzzyFilter {

	int nb_intervales; //Default =3;
	final static int nb_intervales_def=3;
	final static int nb_intervales_max=100;
	
	int look_fonction; //Default =FuzzyFunc.LINE;
	final static int look_def=FuzzyFunc.LINE;
		
	public Vector<FuzzyPair> intervales=new Vector<FuzzyPair>(); // nb_intervales + 2 objets

	public FuzzyFilter(int look, int interv)
	{	
		initFilter(look, interv);
	}
	
	public FuzzyFilter(int look)
	{
		initFilter(look, nb_intervales_def);
	}
	
	public FuzzyFilter()
	{
		initFilter(look_def,nb_intervales_def);
	}
	
	
	public void initFilter(int look, int interv)
	{	
		if (! FuzzyFunc.iflookexist(look))
			{look_fonction=FuzzyFunc.LINE;}
		else {look_fonction=look;}
		
		if ((interv<2)||(interv>nb_intervales_max))
			{nb_intervales=nb_intervales_def;}
		else {nb_intervales=interv;}
	
		FuzzyPair p ;
		float an,as; // Point "a noyau", Point "a seuil"
		float pente=(((float)1)/nb_intervales)-(((float)1)/(nb_intervales+1));
		for (int i=0;i<=nb_intervales;i++)
		{
			
			if (i==0)
			{
				an=0;as=0;
			}
			else
			{
				if (i==nb_intervales)
				{
					an=1;as=1;
				}
				else
				{
					as=((float) i)/nb_intervales;
					an=as-pente;
				}
			}
			p = new FuzzyPair(an,as);
			intervales.addElement(p);
		}
	}
	
	public FuzzyQuad intervale(int i)
	{
		// Retourne les 4 coordonnées du i-eme intervale
		// Il y a n intervales de 1 à n
		FuzzyQuad fq=null;
		if ((i<1)||(i>nb_intervales))
		{
			System.out.println("FuzzyFilter.intervale: exception !(1<="+i+"<="+nb_intervales+")");
		}
		else
		{
			fq = new FuzzyQuad(intervales.elementAt(i-1),intervales.elementAt(i));
		}
		return fq;
	}
	
	public float appartenance(float value,int i)
	{	// Détermine le pourcentage d'appartenance
		// de x au sous ensemble i
		float x=FuzzyFunc.getXfromValue(look_fonction,value);
		FuzzyQuad fq=intervale(i);
		float ap=0; // appartenance, résultat
		//float b_pre_noyau=(2*fq.a.seuil-fq.a.noyau);
		//float c_pre_noyau=(2*fq.b.seuil-fq.b.noyau);
		if ((x<fq.a.noyau)||(x>fq.c_pre_noyau()))
		{
			ap=0;
		}
		else // if ((x<interv.a_noyau)||(x>c_pre_noyau()))
		{
			if (x<fq.b_pre_noyau())
			{	// Cas 1
				ap=(((float)x)-fq.a.noyau)/(fq.b_pre_noyau()-fq.a.noyau);
			}
			else // if (x<b_pre_noyau)
			{	// x>=b_pre_noyau
				if (x<=fq.b.noyau)
				{
					ap=1;
				}
				else // if (x<interv.b_noyau)
				{
					ap=(((float)fq.c_pre_noyau())-x)/(fq.c_pre_noyau()-fq.b.noyau);
				} // else // if (x<interv.b_noyau)
			} // else // if (x<b_pre_noyau)
		} // else // if ((x<interv.a_noyau)||(x>c_pre_noyau))
		
		return ap;
	}
	
	public int appartenance(float value)
	{	// Détermine quel sous ensemble supporte
		// x avec le plus fort pourcentage
		FuzzyQuad fq;
		float x=FuzzyFunc.getXfromValue(look_fonction,value);
		int result=nb_intervales; // petite protection au cas où value=1 ...
		for (int i=1;i<=nb_intervales;i++)
		{
			fq=intervale(i);
			if (x<fq.b.noyau)
			{
				//float b_pre_noyau=(2*fq.a.seuil-fq.a.noyau);
				
				if (x>=fq.b_pre_noyau())
				{	// Cas 1
					result=i;
					//break;
				}
				else
				{
					if (x>=fq.a.seuil)
					{
						result=i;
						//break;
					}
					else
					{
						// Cas 1
						result=i-1;
						//break;
					}
				}
				break;
			}
			
		}
		return result;
	}
	
	public String toString()
	{	
		String s=new String();
		s="(look:"+FuzzyFunc.nameof(this.look_fonction)+",nbi:"+this.nb_intervales+")=";
		for (int i=0;i<this.intervales.size();i++)
		{
			s=s+"("+intervales.get(i).noyau+","+intervales.get(i).seuil+")";
		}
		return s;
	}

	
	
	
}
