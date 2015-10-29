package kernelsim;

//import java.awt.*;
/**
// ************************************************* //
// *** Classe Point3D Jérôme Chapelle - v 1.0 *** //
// ************************************************* //
*/

public class Point3D
{

    public double x,y,z;
    // composantes x, y, z
    
    public Point3D(double vx, double vy, double vz)
    {
    	x=vx;y=vy;z=vz;
    }
    
    public Point3D()
    {
    	x=y=z=0;
    }
    
    public Point3D(Point3D P)
    {
    	this.x=P.x;this.y=P.y;this.z=P.z;
    }
    
    /** Effectue une rotation d'angle alpha
     et de centre P pour le point this */
    public void RotateZ(Point3D P, double alpha)
    { // alpha => gradiants
    
    	// on ramene le point P au centre
    	double dx,dy; //,dz;
    	dx=this.x-P.x;
    	dy=this.y-P.y;
    	//dz=this.z-P.z;
    	double alphaZ=Math.atan(dx/dy);
    	double l=Math.sqrt(Math.pow(dx,2)+Math.pow(dy,2));
    	alphaZ+=alpha;
    	this.x=(l*Math.cos(alphaZ))+P.x;
    	this.y=(l*Math.sin(alphaZ))+P.y;  	
    }


}

