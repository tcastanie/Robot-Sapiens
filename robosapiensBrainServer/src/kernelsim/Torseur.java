package kernelsim;

//import java.awt.*;
/**
// ************************************************* //
// *** Classe Torseur - Jérôme Chapelle - v 1.0 *** //
// ************************************************* //
*/

public class Torseur {

    public double rx,ry,rz,mox,moy,moz;
    // resultantes x, y, z
    // moments x, y, z
    
    public Torseur(double x, double y, double z, double mx, double my, double mz)
    {
    	rx=x;ry=y;rz=z;
    	mox=mx;moy=my;moz=mz;
    }
    
    public Torseur()
    {
    	rx=0;ry=0;rz=0;
    	mox=0;moy=0;moz=0;
    }
    
    public Torseur(Torseur T)
    {
    	this.rx=T.rx;this.ry=T.ry;this.rz=T.rz;
    	this.mox=T.mox;this.moy=T.moy;this.moz=T.moz;
    }
    
    public Torseur(VecteurR V)
    {
    	this.rx=V.vx;this.ry=V.vy;this.rz=0;
    	this.mox=this.moy=this.moz=0;
    }
    
    public void setResultante(double x, double y, double z)
    {
    	rx=x;ry=y;rz=z;
    }
    
    public void setResultante(Vecteur3D V)
    {
    	rx=V.x;ry=V.y;rz=V.z;
    }
    
    public Vecteur3D getResultante()
    {
    	return new Vecteur3D(rx,ry,rz);
    }

    
    public void setMoment(double mx, double my, double mz)
    {
    	mox=mx;moy=my;moz=mz;
    }
    public void setMoment(Vecteur3D V)
    {
    	mox=V.x;moy=V.y;moz=V.z;
    }
    
    public Vecteur3D getMoment()
    {
    	return new Vecteur3D(mox,moy,moz);
    }

    public boolean isEqualTo(Torseur T)
    {
    	if ((this.rx==T.rx)&&(this.ry==T.ry)&&(this.rz==T.rz)
    	  &&(this.mox==T.mox)&&(this.moy==T.moy)&&(this.moz==T.moz))
    	  {return true;}
    	//else
    	return false;
    }
    
    /** Ajouter deux torseurs */
    public static Torseur add(Torseur TA, Torseur TB)
    {
    	return (new Torseur(TA.rx+TB.rx,TA.ry+TB.ry,TA.rz+TB.rz,TA.mox+TB.mox,TA.moy+TB.moy,TA.moz+TB.moz));
    	//res.setResultante(TA.rx+TB.rx,TA.ry+TB.ry,TA.rz+TB.rz);
    	//res.setMoment(TA.mox+TB.mox,TA.moy+TB.moy,TA.moz+TB.moz);
    }
    
    /** Ajouter un torseur */
    public void add(Torseur TB)
    {
    	this.rx+=TB.rx; this.ry+=TB.ry; this.rz+=TB.rz;
    	this.mox+=TB.mox; this.moy+=TB.moy; this.moz+=TB.moz;
    }
    
    /** Multiplication par un scalaire r */
    public void mult(double r)
    {
    	this.rx*=r; this.ry*=r; this.rz*=r;
    	this.mox*=r; this.moy*=r; this.moz*=r;
    }
    
    /* public Torseur mult(Torseur TA, Torseur TB)  {    }*/
    
    public void reduction(double x,double y, double z)
    { reduction(new Point3D(x,y,z)); }
    
    /** reduire le torseur en un point P */
    public void reduction(Point3D P)
    {
    	//Torseur temp=new Torseur(this);
    	Vecteur3D AO=new Vecteur3D(P,new Point3D());
    	Vecteur3D APpvR = Vecteur3D.produitVectoriel(AO,this.getResultante());
    	Vecteur3D M=Vecteur3D.add(APpvR,this.getMoment());
    	this.setMoment(M);
    	
    	
    }
    
    public String toString()
    {
    	String res=new String(rx+","+ry+","+rz+";"+mox+","+moy+","+moz);
    	return res;
    }












}

