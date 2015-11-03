package kernelsim;
// ************************************************* //
// *** Classe VecteurR - Olivier Simonin - v 1.0 *** //
// ************************************************* //
import java.awt.*;

/** Class that handles geometrical vector using real values<br>
* (fr: Vecteur avec composantes réelles)
* @author Olivier Simonin, Jérôme Chapelle
*/
public class VecteurR {
    /** X and Y values
    * <br>(fr: composantes X et Y).
    */
    public double vx,vy; // composantes X, Y
    
    /** Vector angle, in trigo radian unit
    ** <br>(fr:  Angle du vecteur (radians trigo)). */
    public double a;


    // *** CONSTRUCTEURS *** //
    // ********************* //

    /** Constructs a new vector using vx and vy, computes the angle according to this values<br>
    * (fr: Construit un nouveau vecteur avec les variables vx et vy, et calcule l'angle correspondant).
    */
    public VecteurR(double vx, double vy)   // Comp X, Y
    {
     this.vx=vx;
     this.vy=vy;
     a=this.angle();
    }

	/* Redéfinition (types identiques avec l'autre constructeur depuis
	passage des float,int ... aux double) donc pas bon
    public VecteurR(double l, double a)    // Norme, Angle
    {
     this.a=normalise(a);
     vx=(l*Math.cos(a));
     vy=(l*Math.sin(a));
    }
	*/
    /** Constructs a new vector with the same composants vx and vy of the given vector <i>v</i> Given vector uses integer values for vx and vy<br>
    * (fr: Construit un nouveau vecteur en copiant les composantes vx et vy du vecteur donné <i>v</i>. Les composantes du vecteur donné exprimées avec des entiers).
    */
    public VecteurR(Vecteur v)            // Vecteur (var. int)
    {
     vx=v.vx;
     vy=v.vy;
     a=angle();
    }
  
  /** Constructs a new vector by copying the given vector <i>v</i><br>
  * (fr: Construit un nouveau vecteur copie du vecteur donné <i>v</i>).
  */
  public VecteurR(VecteurR v)            // Vecteur (var. int)
    {
     vx=v.vx;
     vy=v.vy;
     a=v.a;
    }

    // *** INFO sur le vecteur *** //
    // *************************** //

    /** Computes the "length" of the vector<br>
    * (fr: Calcule la Norme du vecteur).
    */
    public double norme()
    {
     return(Math.sqrt(vx*vx+vy*vy));
    }

    /** Computes angle (radians) of this vector using vx and vy<br>
    * (fr: Renvoi l'Angle du vecteur (radians))*/
    public double angle()                 
    {
     double t;
     double l=this.norme();
     if (l==0) return 0;
     if (vy>=0) t=Math.acos(vx/l);
      else t=(-Math.acos(vx/l));
     return(normalise(t));
    }
    
     /** Computes angle (degrees) of this vector using vx and vy<br>
    * (fr: Renvoi l'Angle du vecteur (degres))*/
    public double angleDegres()
    {
    		// // Convert a 45 degree angle to radians
		//    double angle = 45.0 * 2.0 * Math.PI/360.0;
		double tmpval=0;
		tmpval = this.angle()*360;
		tmpval = tmpval / 2;
		tmpval = tmpval / (Math.PI);
		while ((tmpval<0)||(tmpval>=360))
		{	System.out.print("Conv(VR):"+tmpval);
			if (tmpval<0) {tmpval+=360;}
			if (tmpval>=360) {tmpval-=360;}
			System.out.println("=>"+tmpval);
		}
		
		/*
		tmpval = tmpval*360;
		tmpval = tmpval / 2;
		tmpval = tmpval / (Math.PI);*/
		
    	return tmpval; //(360*angle()/2/ (float)(Math.PI)));
    }
    
     /** Computes angle (value between 0 and 1) of this vector using vx and vy<br>
    * (fr: Renvoi l'Angle du vecteur entre 0 et 1) */
    public double angleProp()
    {
    	return (normalise(a)/(2*Math.PI));
    }
    


    // *** OPERATIONS SUR le vect. *** //
    // ******************************* //

    public void addV(double nx, double ny)  // var. des comp. X,Y
    {
     vx=vx+nx;
     vy=vy+ny;
     a=this.angle();
    }

    public void addV(VecteurR v)          // Addition d'un vecteur
    {
     vx=vx+v.vx;
     vy=vy+v.vy;
     a=angle();
    }
    
    public static VecteurR addV(VecteurR va,VecteurR vb)          // Addition de deux vecteurs
    {
    	VecteurR Vresult=new VecteurR(va);
    	Vresult.addV(vb);
    	return Vresult;
    }

    public void addV(Vecteur v)           // Addition d'un Vecteur (int)
    {
     vx=vx+v.vx;
     vy=vy+v.vy;
     a=angle();
    }

    public void fix(double vx,double vy)    // Fixe les comp. X, Y (INIT)
    {
     this.vx=vx;
     this.vy=vy;
     a=angle();
    }

    public void fixA(double na)            // Fixe un nouvel Angle
    {
     double l=this.norme();
     a=normalise(na);
     vx=(l*Math.cos(a));
     vy=(l*Math.sin(a));
    }

    public void fixN(double l)            // Fixe une nouvelle Norme
    {
     vx=(l*Math.cos(a));
     vy=(l*Math.sin(a));
      a=angle();
    }
	/*
    public void mult(double c)           // Mult. les comp. X, Y par c
    {
     vx=(vx*c);
     vy=(vy*c);
     a=angle();
    }
    */
    
    /** Multiplie chaque comp. X, Y par c */
    public void mult(double c)           // Mult. les comp. X, Y par c
    {
     vx=(vx*c);
     vy=(vy*c);
     a=angle();
    }
    /** Div. chaque comp. X, Y par c */
    public void div(double c)           
    {
     vx=(vx/c);
     vy=(vy/c);
     a=angle();
    }

   public void var(double v)              // Ajouter valeur v a l'angle
    {
     double l=this.norme();
     a=normalise(a+v);
     vx=(l*Math.cos(a));
     vy=(l*Math.sin(a));
    }

   public double normalise(double angle)   // Normaliser l'Angle entre 0 et 2*PI
    {
     int cpt=0;
     while((angle<0) || (angle>2*Math.PI))
     {
      if (angle<0) angle+=2*Math.PI;
      if (angle>2*Math.PI) angle-=2*Math.PI;
      cpt++;
      if (cpt>2) System.out.println ("angle="+angle);
     }
     return angle;
    }


    // *** Operations ENTRE vecteurs *** //
    // ********************************* //

    public double Dangle(VecteurR v)      // renvoi l'angle entre le vecteur et un vecteur passe
    {                                    // valeur comprise entre -PI et PI
     double dif=this.a-v.a;
     if (dif>Math.PI) dif=(2*Math.PI-dif);
     return dif;
    }

    public double DanglePP(VecteurR v)    // renvoi le plus petit des 2 angles entre le vecteur
    {                                    // et un vecteur passe en param.
     double dif1=normalise(this.a-v.a);
     double dif2=normalise(v.a-this.a);
     if (dif1<dif2)
     	{return dif1;}
     	//else
     return dif2;
    }

    public int signe(VecteurR vref)      // renvoi le signe de la difference des angles entre le vect.
                                         // et un vecteur passe an param.
    {
     if (vref.a>Math.PI)
    	 {
    	 	if ((a>vref.a) ||(a<(vref.a-Math.PI)))
    	 		{return 1;}
    	 	//else
    	 		return -1;
    	 }
     else
     if ((a<vref.a) ||(a>(vref.a+Math.PI)))
    	 {return -1;}
     	//else
     	return 1;
    }

    public double ps(VecteurR v2)         // renvoi le produit scalaire entre le vecteur un vecteur
                                         // passe en param.
    {
     return((this.norme()*v2.norme()*Math.cos(Dangle(v2))));
    }


    // *** AFFICHAGES vecteur *** //
    // ************************** //

    public void print()
    {
     System.out.println(vx+" "+vy+" "+a);
    }

    public void draw(int x,int y,Graphics g,Color c)
    {
     g.setColor(c);
     int xv=(int)(x-Math.round(vx));
     int yv=(int)(y+Math.round(vy));
     g.drawLine(x,y,xv,yv);
    }

    public String toString()
    {
    	String res=new String(vx+","+vy+"-"+a);
    	return res;
    }
}

