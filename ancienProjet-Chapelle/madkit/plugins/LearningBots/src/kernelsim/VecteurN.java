package kernelsim;

/**
************************************************* 
*** Classe VecteurN Jérôme Chapelle - v 1.0   
* Permet de représenter des Vecteurs (au sens géométrique)
* de taille n, et de réaliser des calculs dessus: produit
* scalaire / vectoriel, calcul de l'angle formé par deux
* vecteurs ... Dernière mise à jour : 20 sept 2005
* (Notifier Greg en cas de changement)
 ************************************************* 
*/

public class VecteurN
{
    public double V[];
    int n=0;
    // composantes x, y, z
    
    /** Crée un vecteur à nComposantes dimensions, composantes données par Vn[] */
    public VecteurN(double Vn[],int nComposantes)
    {
    	this.n=nComposantes;
    	V=new double[n];
    	for (int i=0;i<n;i++)
    	{
    		V[i]=Vn[i];
    	}
    }
    
    /** Crée un vecteur à nComposantes dimensions , chaque composante = 0 */
    public VecteurN(int nComposantes)
    {
    	this.n=nComposantes;
    	V=new double[n];
    	for (int i=0;i<n;i++)
    	{
    		V[i]=0;
    	}
    }
    
    /** Crée un vecteur à nComposantes dimensions , chaque composante = a */
    public VecteurN(int nComposantes,double a)
    {
    	this.n=nComposantes;
    	V=new double[n];
    	for (int i=0;i<n;i++)
    	{
    		V[i]=a;
    	}
    }
    
    /** Crée une copie du vecteur P */ 
    public VecteurN(VecteurN P)
    {
    	this.n=P.n;
    	V=new double[n];
    	for (int i=0;i<n;i++)
    	{
    		V[i]=P.V[i];
    	}
    }
    
    /** Renvoie un vecteur somme des deux vecteurs en paramètres */
    public static VecteurN add(VecteurN VA, VecteurN VB)
    {
    	if (VA.n!=VB.n)
    	{
    		System.out.println("!!!VecteurN.add(VA,VB) : VA&VB pas meme nombre composantes!!!");
    		return null;
    	}
    	VecteurN vresult=new VecteurN(VA.n);
    	for (int i=0;i<VA.n;i++)
    	{
    		vresult.V[i]=VA.V[i]+VB.V[i];
    	}
    	return (vresult);
    }
    
    /** Renvoie un vecteur somme de this et VB */
    public VecteurN add(VecteurN VB)
    {
    	return add(this,VB);
    }
    
    /** Renvoie un vecteur différence des deux vecteurs en paramètres: VA-VB*/
    public static VecteurN sub(VecteurN VA, VecteurN VB)
    {
    	if (VA.n!=VB.n)
    	{
    		System.out.println("!!!VecteurN.add(VA,VB) : VA&VB pas meme nombre composantes!!!");
    		return null;
    	}
    	VecteurN vresult=new VecteurN(VA.n);
    	for (int i=0;i<VA.n;i++)
    	{
    		vresult.V[i]=VA.V[i]-VB.V[i];
    	}
    	return (vresult);
    }
    /** Renvoie un vecteur différence de this et VB: this-VB */
    public VecteurN sub(VecteurN VB)
    {
    	return sub(this,VB);
    }
    
    /** Renvoie la distance euclidienne entre VA et VB */
    public static double distance(VecteurN VA,VecteurN VB)
    {
    	return (sub(VA,VB)).norme();
    }
    /** Renvoie la distance euclidienne entre this et VB */
    public double distance(VecteurN VB)
    {
    	return (sub(this,VB)).norme();
    }
    
    /** Renvoie un vecteur produit vectoriel des deux vecteurs en paramètres */
    public static VecteurN produitVectoriel(VecteurN VA, VecteurN VB)
    {
    	VecteurN vresult=new VecteurN(VA.n);
    	for (int i=0;i<vresult.n;i++)
    	{
    		if (i<vresult.n-2)
    		{
    			vresult.V[i]=(VA.V[i+1]*VB.V[i+2])-(VA.V[i+2]*VB.V[i+1]);
    		}
    		else
    		{
    			if (i<vresult.n-1)
    			{
    				vresult.V[i]=(VA.V[i+1]*VB.V[0])-(VA.V[0]*VB.V[i+1]);
    			}
    			else
    			{
    				vresult.V[i]=(VA.V[0]*VB.V[1])-(VA.V[1]*VB.V[0]);
    			}
    		}
    	}
    	return vresult;
    }
    
    /** Renvoie la norme du vecteur VA*/
    public static double norme(VecteurN VA)
    {
    	double sum=0;
    	for (int i=0;i<VA.n;i++)
    	{
    		sum=sum+Math.pow(VA.V[i],2);
    	}
    	return Math.sqrt(sum);
    }
    
    /** Renvoie la norme du vecteur */
    public double norme()
    {	
    	return norme(this);
    }
    
    /** Renvoie le Produit scalaire de deux vecteurs */
    public static double produitScalaire(VecteurN VA,VecteurN VB)
    {
    	if (VA.n!=VB.n)
    	{
    		System.out.println("!!!VecteurN.produitScalaire(VA,VB) : VA&VB pas meme nombre composantes!!!");
    		return -1;
    	}
    	double ps=0;
    	for (int i=0;i<VA.n;i++)
    	{
    		ps=ps+VA.V[i]*VB.V[i];
    	}
    	return ps;
    }
    
    /** Renvoie l'angle formé par les deux vecteurs VA et VB (0<angle<2*PI)*/
    public static double angle(VecteurN VA, VecteurN VB)
    {	/* asin => angle entre -PI/2 et PI/2 */
    	double asinus=Math.asin(norme(produitVectoriel(VA,VB))/(norme(VA)*norme(VB))); 
    	/* acos => angle entre 0 et PI */
    	double acosinus=Math.acos((produitScalaire(VA,VB))/(VA.norme()*VB.norme()));
    	/* si asin<0, alors alpha=-acosinus, soit alpha=2PI-acosinus*/
    	if (asinus<0)
    	{
    		return (2*Math.PI)-(acosinus);
    	}
    	/*else
    	{*/
    		return acosinus;
    	//}
    }
    /** Renvoie l'angle formé par le vecteur(this) et VB (0<angle<2*PI)*/
    public double angle(VecteurN VB)
    {
    	return angle(this,VB);
    }

    /** Renvoie l'angle formé par le vecteur this et le vecteur Vunitaire (0<angle<2*PI)*/
    public double angle()
    {
    	System.out.println("!!! VecteurN.angle() appel d'une fonction hasardeuse !!!");
    	VecteurN Vunit=new VecteurN(this.n,1);
    	Vunit.setComposante(this.n-1,0);
    	return angle(this,new VecteurN(this.n,1));
    }
 
    /** Renvoie le pourcentage de similarité (0 à 1) entre deux angles */
    public static double angleSimilaire(VecteurN VA,VecteurN VB)
    {
	    // Si l'angle entre les deux vecteurs est proche de 0 leur similarité est proche de 1
	    // Si l'angle entre les deux vecteurs est proche de PI leur similarité est proche de 0
	    // Si l'angle entre les deux vecteurs est proche de 2*PI leur similarité est proche de 1
	    double angle=VA.angle(VB);
	    double angleAbs;
	    if (angle>Math.PI)
	    {
	  	  angleAbs=(2*Math.PI)-angle;
	    }
	    else
	    {
	  	  angleAbs=angle;
	    }
	    double angleDAbs=1-(angleAbs/(Math.PI));
	    return angleDAbs;
	    //double distanceDAbs = (Math.min(V1.norme(),V2.norme())/Math.max(V1.norme(),V2.norme()));
    }
    
    /** Renvoie le pourcentage de similarité (0 à 1) entre deux distances 
     *  => min(distance(VA),distance(VB)) / min(distance(VA),distance(VB)) */
    public static double distanceSimilaire(VecteurN VA,VecteurN VB)
    {
	    double distanceDAbs = (Math.min(VA.norme(),VB.norme())/Math.max(VA.norme(),VB.norme()));
	    return distanceDAbs;
    }
    
    
    /** Renvoie la i-ieme composante du vecteur */
    public double getComposante(int i)
    {
    	if (i>n)
    	{
    		System.out.println("!!!VecteurN.getComposante(i) : i > nombre composantes!!!");
    		return -1;
    	}
    	return V[i];
    }
    /** Renvoie les composantes du vecteur dans un tableau de double */
    public double[] getComposantes()
    {
    	double result[]=new double[this.n];
    	for (int i=0;i<n;i++)
    	{
    		result[i]=V[i];
    	}
    	return result;
    }
    
    /** Attribue value à la i-ieme composante du vecteur */
    public void setComposante(int i,double value)
    {
    	if (i>n)
    	{
    		System.out.println("!!!VecteurN.setComposante(i) : i > nombre composantes!!!");
    		//return -1;
    	}
    	V[i]=value;
    }
    /** Attribue les n valeurs de values[] aux composantes du vecteur */
    public void setComposantes(double values[],int n)
    {
    	
    	if (this.n!=n)
    	{
    		System.out.println("!!!VecteurN.setComposantes(values[],n) : n != nombre composantes!!!");
    		//return -1;
    	}
    	V=new double[this.n];
    	for (int i=0;i<this.n;i++)
    	{
    		V[i]=values[i];
    	}
    }
    
    public String toString()
    {
    	String res=new String();
    	for (int i=0;i<n;i++)
    	{
    		res=res+ new String(V[i]+";");
    	}    	
    	return res;
    }

}

