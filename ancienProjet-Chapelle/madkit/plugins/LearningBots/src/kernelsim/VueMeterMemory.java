// Copyright (C) 1997 by MadKit Team
package kernelsim;

/*import madkit.kernel.*;
import smaapp.*;
import java.io.*;
import java.util.*;
import java.awt.*;
import madkit.simulation.activators.*;
*/

public class VueMeterMemory 
{
	int nsize; // Nombre de mémoire
	int nlast; // Index sur la dernière mémoire
	/** Booleen qui determine si on enregistre
	toutes les valeurs, mêmes si identiques, ou pas*/
	boolean remembersamevalues=true;
	double values[];
	
	public VueMeterMemory(int nmem)
	{
		nsize=nmem;
		nlast=-1;
		values = new double[nsize];
		for (int i=0;i<nsize;i++) {values[i]=0;}
	}
	
	public void RememberSameValues()
	{ remembersamevalues=true;}
	
	public void DontRememberSameValues()
	{ remembersamevalues=false;}
	
	public int getSize()
	{ return nsize;
	}

	public double getLastValue()
	{ if (nlast==-1) {return 0;}
	  //else {
		  return values[nlast];
		  //}
	}
	
	public double getLastValue(int n)
	{ if (nlast==-1) {return 0;}
	  /*else {*/
	  	int indextemp=(nsize+nlast-n)%nsize;
	  	return values[indextemp];
	  	//}
	  	
	}
	
	
	
	public void putValue(double value)
	{	if (remembersamevalues)
		{
		nlast++;
		nlast=nlast%nsize;
		values[nlast]=value;
		}
		else if (value!=getLastValue())
			{
			nlast++;
			nlast=nlast%nsize;
			values[nlast]=value;
			}
	}
	


	

	/*public void setSize()
	{ return nsize;
	}*/
	
	

}