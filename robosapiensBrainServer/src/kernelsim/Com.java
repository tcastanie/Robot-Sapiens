package kernelsim;

//import java.awt.*;

// Olivier Simonin v1.0
//

public class Com {

    public boolean c;        // com etablie
    public boolean known;    // com connu a t-1
    public float sat, Dsat;  // intensity of satisfaction indiv, var
    public float satd;       // intensity fn. dist
    public float signSat;    // last sign non null of sat variation
    public float angle;      // angle forme par le vecteur rob-robcom
    public float dist,Ddist; // dist and var. of dist between 2 agents
    public VecteurR alt;     // vecteur altruisme
    public VecteurR seg;     // vecteur reliant les 2 agents en comm
    public int si;

    public Com()
    {
     c=false;
     known=false;
     sat=0; si=0;
     dist=0;
     signSat=1;
     angle=0;
     alt=new VecteurR((float)0,(float)0);
     seg=new VecteurR((float)0,(float)0);
    }

  public void Alt(float d)
     {
      float l,norme;

      if (dist>0)
       {
	 if (dist>d) l=d; else l=d-(d-dist)*(d-dist)/d;
	 norme=-signSat*Math.abs(sat)*2*l; // k=2
	 alt.fixN(norme);
       }
     }

  public VecteurR Slide(double rr)
    {
      int s=-1; // toujours passer a g d
      VecteurR v = new VecteurR(alt.vx,alt.vy);

      if (signSat<0) v.var((float)(s*Math.PI/2));
      return v;
    }
}

