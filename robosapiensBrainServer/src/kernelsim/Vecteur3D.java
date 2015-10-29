package kernelsim;

//import java.awt.*;
/**
// ************************************************* //
// *** Classe Vecteur3D Jérôme Chapelle - v 1.0 *** //
// ************************************************* //
*/

public class Vecteur3D
{
    public double x,y,z;
    // composantes x, y, z
    
    public Vecteur3D(double vx, double vy, double vz)
    {
    	x=vx;y=vy;z=vz;
    }
    
    public Vecteur3D()
    {
    	x=y=z=0;
    }
    
    public Vecteur3D(Vecteur3D P)
    {
    	this.x=P.x;this.y=P.y;this.z=P.z;
    }
    
    /** Crée un vecteur à partir de deux points */
    public Vecteur3D(Point3D PA,Point3D PB)
    {
    	this.x=PB.x-PA.x;
    	this.y=PB.y-PA.y;
    	this.z=PB.z-PA.z;
    }
    
    /** Renvoie un vecteur somme des deux vecteurs en paramètres */
    public static Vecteur3D add(Vecteur3D VA, Vecteur3D VB)
    {
    	return (new Vecteur3D(VA.x+VB.x,VA.y+VB.y,VA.z+VB.z));
    }
    
    /** Renvoie un vecteur produit vectoriel des deux vecteurs en paramètres */
    public static Vecteur3D produitVectoriel(Vecteur3D VA, Vecteur3D VB)
    {
    	Vecteur3D vresult=new Vecteur3D();
    	vresult.x=(VA.y*VB.z)-(VA.z*VB.y);
    	vresult.y=(VA.z*VB.x)-(VA.x*VB.z);
    	vresult.z=(VA.x*VB.y)-(VA.y*VB.x);
    	return vresult;
    }

    public String toString()
    {
    	String res=new String(x+","+y+","+z);
    	return res;
    }

}

