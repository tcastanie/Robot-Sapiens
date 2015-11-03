package kernelsim;

import java.awt.*;
import java.util.Vector; // Pour les Vecteurs d'objets

// ************************************************* //
// *** Classe Plaque - Jérôme CHAPELLE           *** //
// ************************************************* //


public class Plaque {

    public int VERTICAL=1;
    public int HORIZONTAL=2;
    public int HAUT=1;
    public int BAS=2;
    public int DROITE=3;
    public int GAUCHE=2;

    private Color thing= Color.green;
    public int x1,x2,y1,y2; // coordonnées (entieres)
    public float dx,dy; // correction des coord entieres

    // Pour le UndoMove
    public int sx1,sx2,sy1,sy2; // coordonnées (entieres)
    public float sdx,sdy; // correction des coord entieres

    // Coordonnées réelles: (x1+dx,y1+dy)-(x2+dx,y2+dy)

    public Vector<Agisseur> Rob_Pouss;  // Les robots qui pousssent ou tirent
    public Vector<Agisseur> Rob_Maint;    // Les robots qui maintiennent la plaque

    public int Rob_Decoup=-1;     // Le robot qui découpe
                                  // Pas de découpeur à l'initialisation
    public float dec_prog=0;     // Avancement de la découpe
    public int dec_x1,dec_x2,dec_y1,dec_y2; // Ligne de découpe
    public float dec_dx,dec_dy; // correction des coord entieres
    //public int NbPix=0;
    public boolean do_decoup=false;
    // Pour le UndoMove
    private int sdec_x1,sdec_x2,sdec_y1,sdec_y2; // coordonnées (entieres)
    private float sdec_dx,sdec_dy; // correction des coord entieres

    public int Id;            // Identifiant de la plaque, -1 si anonyme (?)
    public int ParentId;      // Identifiant du père de la plaque
                              // (-1 si orphelin)

    public int worldDimx=0,worldDimy=0;
    // Variables à rajouter
      // Somme des forces ...
      // Coefficient de frottement
      public int coef_frot=1;
    // Méthodes à rajouter:
      // Autorisation de déplacement
      // Negociation de déplacement

    public Plaque(int x1, int y1, int x2, int y2)
    {
        Id=-1;
        ParentId=-1;
        this.x1=x1; this.x2=x2;
        this.y1=y1; this.y2=y2;

        dx=dy=0;
        // Pour le UndoMove
        sx1=x1;sx2=x2;sy1=y1;sy2=y2;
        sdx=dx;sdy=dy; // correction des coord entieres

        sdec_x1=dec_x1;sdec_x2=dec_x2;sdec_y1=dec_y1;sdec_y2=dec_y2;
        sdec_dx=dec_dx;sdec_dy=dec_dy; // correction des coord entieres
        Rob_Pouss=new Vector<Agisseur>();
        Rob_Maint=new Vector<Agisseur>();
        Rob_Decoup=-1;
    }

    public void SetWorldDim(int dimx,int dimy)
    {
        worldDimx=dimx; worldDimy=dimy;
    }

    public Plaque(Rect R)
    {
        this(R.x1,R.y1,R.x2,R.y2);
    }

    public Rect GetRect()
    {
      return new Rect(x1,y1,x2,y2,thing);
    }

    public int Largeur() {return (x2-x1);}
    public int Hauteur() {return (y2-y1);}
    public int Surface() {return (Largeur()*Hauteur());}
    public int ForceRequise() {return ((int)(coef_frot*Surface()));}

    public void SetId(int Id)
    {
      this.Id=Id;
    }

    public void SetParentId(int pId)
    {
      this.ParentId=pId;
    }

    public int GetId() {return Id;}
    public int GetParentId() {return ParentId;}

    public Rect GetPosition()
    {
      return new Rect(x1,y1,x2,y2,thing);
    }

    public Rect SetPosition(int x, int y)
    { // Déplace une plaque de sa position (x1,y1) à (x,y)
      // Ne pas changer l'ordre des instructions ...
      this.x2=this.x2+x-this.x1;
      this.x1=x;
      this.y2=this.y2+y-this.y1;
      this.y1=y;
      return GetPosition();
    }
    
    public Rect SetPositionC(int x, int y)
    { // Déplace une plaque de sa position en mettant le centre en (x,y)
      int largdem = Math.round(Math.abs(this.x2-this.x1)/2);
      int hautdem = Math.round(Math.abs(this.y2-this.y1)/2);
      return SetPosition(x-largdem,y-hautdem);
    }
    /*
    public Rect Move(int dx,int dy)
    { // Deplace une plaque de (dx,dy)
      x1+=dx;x2+=dx;y1+=dy;y2+=dy;
      return GetPosition();
    }*/

    public Rect Move(float x,float y)
    { // Deplace une plaque de (x,y)
      int rnum;
      float xtemp,ytemp;
      xtemp=ytemp=0;
      //System.out.print("Plaque="+GetId()+" pous:");
      /*if (Rob_Pouss.size()>0)
      for (int i=0;i<Rob_Pouss.size();i++)
        System.out.print(((Agisseur)Rob_Pouss.elementAt(i)).RobotNum+"-");

      System.out.print(" dec:");
      if (Rob_Decoup!=-1) System.out.print(Rob_Decoup);

      System.out.print(" maint:");
      if (Rob_Maint.size()>0)
      for (int i=0;i<Rob_Pouss.size();i++)
        System.out.print(((Agisseur)Rob_Maint.elementAt(i)).RobotNum+"-");

      System.out.println();
      */
      if (y1-1<=0)
      { int decal=y1-1;
        y1-=decal;y2-=decal; sy1-=decal;sy2-=decal;
        dec_y1-=decal;dec_y2-=decal; sdec_y1-=decal;sdec_y2-=decal;
        //y2-=y1;sy2-=y1;dec_y1-=y1;dec_y2-=y1;sdec_y1-=y1;sdec_y2-=y1;
        //y1=0;sy1=0;
      }

      if (x1-1<=0)
      { int decal=x1-1;
        x1-=decal;x2-=decal; sx1-=decal;sx2-=decal;
        dec_x1-=decal;dec_x2-=decal; sdec_x1-=decal;sdec_x2-=decal;
      }

      if (y2+1>=worldDimy)
      {
        int decal=y2-worldDimy+1;
        y1-=decal;y2-=decal; sy1-=decal;sy2-=decal;
        dec_y1-=decal;dec_y2-=decal; sdec_y1-=decal;sdec_y2-=decal;
      }

      if (x2+1>=worldDimx)
      {
        int decal=x2-worldDimx+1;
        x1-=decal;x2-=decal; sx1-=decal;sx2-=decal;
        dec_x1-=decal;dec_x2-=decal; sdec_x1-=decal;sdec_x2-=decal;
      }

      xtemp=x1+x+dx;
      ytemp=y1+y+dy;

      sx1=x1;  sx2=x2;
      sy1=y1;  sy2=y2;
      sdx=dx;  sdy=dy;

      x1+=(x+dx);
      x2+=(x+dx);
      y1+=(y+dy);
      y2+=(y+dy);
      dx=xtemp-x1;
      dy=ytemp-y1;

      xtemp=dec_x1+x+dec_dx;
      ytemp=dec_y1+y+dec_dy;

      sdec_x1=dec_x1;  sdec_x2=dec_x2;
      sdec_y1=dec_y1;  sdec_y2=dec_y2;
      sdec_dx=dec_dx;  sdec_dy=dec_dy;

      dec_x1+=(x+dec_dx);
      dec_x2+=(x+dec_dx);
      dec_y1+=(y+dec_dy);
      dec_y2+=(y+dec_dy);
      dec_dx=xtemp-dec_x1;
      dec_dy=ytemp-dec_y1;

      for (int i=0;i<this.Rob_Pouss.size();i++)
      {
        rnum=(((Agisseur)Rob_Pouss.elementAt(i)).RobotNum);
        for (int j=0;j<RobotAppPhy.otherRobots.size();j++)
          {
            if ( ((RobotAppPhy)RobotAppPhy.otherRobots.elementAt(j)).N_agent==rnum)
              { // 010616
                ((RobotAppPhy)RobotAppPhy.otherRobots.elementAt(j)).pouss_ok=false;
                /*((RobotAppPhy)RobotAppPhy.otherRobots.elementAt(j)).dep_ok=false;
                System.out.println("Depok a false de robot="+((RobotAppPhy)RobotAppPhy.otherRobots.elementAt(j)).N_agent);
                */
              }
          }
      }

      return GetPosition();
    }
/*
    public RobotAppPhy GetRobotByIndice(int indice)
    {
      return ((RobotAppPhy)RobotAppPhy.otherRobots.elementAt(indice));
    }

    public RobotAppPhy GetPousseurById(int id)
    {
      int i=0; boolean go=true;
      while ((i<RobotAppPhy.otherRobots.size()) && go)
      {

      }
    }
*/
    public Rect UndoMove()
    { // Deplace une plaque de (x,y)
      x1=sx1;  x2=sx2;
      y1=sy1;  y2=sy2;
      dx=sdx;  dy=sdy;

      dec_x1=sdec_x1;  dec_x2=sdec_x2;
      dec_y1=sdec_y1;  dec_y2=sdec_y2;
      dec_dx=sdec_dx;  dec_dy=sdec_dy;

      int rnum;
      //RobotAppPhy rtemp;
      for (int i=0;i<this.Rob_Pouss.size();i++)
      {
        rnum=(((Agisseur)Rob_Pouss.elementAt(i)).RobotNum);
        for (int j=0;j<RobotAppPhy.otherRobots.size();j++)
          {
            if ( ((RobotAppPhy)RobotAppPhy.otherRobots.elementAt(j)).N_agent==rnum)
              { // 010616
                ((RobotAppPhy)RobotAppPhy.otherRobots.elementAt(j)).pouss_ok=false;
                /*((RobotAppPhy)RobotAppPhy.otherRobots.elementAt(j)).dep_ok=false;
                System.out.println("Depok a false de robot="+((RobotAppPhy)RobotAppPhy.otherRobots.elementAt(j)).N_agent);
                */
              }
          }
      }
      return GetPosition();
    }

    public Rect GetLastRect()
    { //Renvoie la dernière position du rectangle
      return new Rect(sx1,sy1,sx2,sy2,thing);
    }

    public boolean HasMoved()
    { // Dit si la plaque a bougé
      return !((dx==sdx)&&(dy==sdy)&&(x1==sx1)&&(y1==sy1)&&(x2==sx2)&&(y2==sy2));
    }

    public int carre(int x) {return x*x;}

    public int getDistanceTo(int xfrom,int yfrom)
    {
      return (int)(Math.sqrt((double)(carre(xfrom-((x2+x1)/2))+carre(yfrom-((y2+y1)/2)))));
    }

    public VecteurR GetLastDeltaPos()
    { //Renvoie la dernière position du rectangle
      return new VecteurR(sdx,sdy);
    }

    public boolean AddPousseur(int numRob)
    {
      // Ajoute un Pousseur à la plaque
      // Si Pas possible: renvoie FALSE (ex. plus de place ...) (rare?)
      if (HasPousseur(numRob))
        { System.out.println("Doublon pousseur("+numRob+")");
          return false; }
      Rob_Pouss.addElement(new Agisseur(numRob));
      //System.out.println("Plaque.AddPousseur (id="+numRob+")");
      return true;
    }

    public boolean HasPousseur(int numRob)
    {
      // Détermine si la plaque a numRob comme pousseur
      int i;
      if (Rob_Pouss.size()<=0) return false;
      i=0;
      while (i<Rob_Pouss.size())
      {
        if (((Agisseur)Rob_Pouss.elementAt(i)).RobotNum==numRob)
        { return true;}
        //else
        	i++;
      }
      return false;
    }

    public boolean HasMainteneur(int numRob)
    {
      // Détermine si la plaque a numRob comme mainteneur
      int i;
      if (Rob_Maint.size()<=0) return false;
      i=0;
      while (i<Rob_Pouss.size())
      {
        if (((Agisseur)Rob_Maint.elementAt(i)).RobotNum==numRob)
          {return true;}
        //else
        i++;
      }
      return false;
    }

    public boolean HasDecoupeur(int numRob)
    {
      // Détermine si la plaque a numRob comme mainteneur
      if (Rob_Decoup==-1) return false;
      return (Rob_Decoup==numRob);
    }

    public boolean HasRobot(int numRob)
    {
      return (HasPousseur(numRob)||HasDecoupeur(numRob)||HasMainteneur(numRob));
    }

    public boolean AppliquerForce(int numRob,int fx,int fy)
    {
      // Memorise la force appliquée par un robot à la plaque
      int IndRob=GetIndice_with_id(numRob,Rob_Pouss);
      if (IndRob==-1)
          return false;
      ((Agisseur)Rob_Pouss.elementAt(IndRob)).SetForce(fx,fy);
      //if (numRob==4) System.out.println("Force appliquee par "+numRob+" fx="+fx+" / fy="+fy);
      return true;
    }
    /*
    public boolean SpecifieDecoupe(int numRob,int direction,int morceau,int epaisseur)
    {
      // Prepare la decoupe
      // return false si la Plaque a un autre decoupeur
      if ((numRob!=Rob_Decoup)&&(numRob!=-1))
      return false;
      // Specifie le sens
      if (direction==HORIZONTAL)
      { // Test decoupe horizontale:
        if (morceau==HAUT)
          {
            if (y1+epaisseur>=y2)
             {dec_x1=x1;dec_y1=(y2+y1)/2; dec_x2=x2; dec_y2=dec_y1;}
            else
             {dec_x1=x1;dec_y1=y1+epaisseur; dec_x2=x2; dec_y2=dec_y1;}
          }
        else // morceau==BAS ou pas specifié
          {
            if (y2-epaisseur<=y2)
             {dec_x1=x1;dec_y1=(y2+y1)/2; dec_x2=x2; dec_y2=dec_y1;}
            else
             {dec_x1=x1;dec_y1=y2-epaisseur; dec_x2=x2; dec_y2=dec_y1;}
          }
        return true;
      }
      else
        if (direction==VERTICAL)
        {
          // Test decoupe vertitale:
          if (morceau==GAUCHE)
          {
            if (x1+epaisseur>=x2)
             {dec_x1=(x2+x1)/2; dec_y1=y1; dec_x2=dec_x1; dec_y2=y2;}
            else
             {dec_x1=x1+epaisseur;dec_y1=y1; dec_x2=dec_x1; dec_y2=dec_y1;}
          }
          else // morceau==DROITE ou pas spécifié
          {
            if (x2-epaisseur<=x1)
             {dec_x1=(x2+x1)/2; dec_y1=y1; dec_x2=dec_x1; dec_y2=y2;}
            else
             {dec_x1=x2-epaisseur;dec_y1=y1; dec_x2=dec_x1; dec_y2=dec_y1;}
          }
          return true;
        }
        else return false;
    } // Specifie decoupe
    */

    public boolean AppliquerDecoupe(int numRob)
    {
      // Memorise la volonté de decoupe
      // return false si la Plaque a un autre decoupeur

      if ((numRob!=Rob_Decoup)&&(numRob!=-1))
      return false;
      this.do_decoup=true;
      return true;
    }

    public int ExecuterDecoupe()
    { // Appelée par le Scheduler
      // Execute une découpe si possible
      // Si decoupe exécutée, renvoyer le nb de pixels decoupés
      int nb_pix=0;
      if (this.do_decoup==true)
      {
        // Si la plaque a été déplacée:
        if (this.HasMoved()) nb_pix=0;
        // Si il y a plus de mainteneur que de pousseurs:
        else if (Rob_Maint.size()>Rob_Pouss.size())
                nb_pix=2;
             else
              if (Rob_Maint.size()==Rob_Pouss.size())
                nb_pix=1;
                  else nb_pix=0;
        if (evalDecoupe()>=tailleDecoupe())
          {
            dec_prog=1;
          }
        else
        { float realpix=evalDecoupe()+nb_pix;
          dec_prog= realpix /((float)tailleDecoupe());
        }
        //System.out.println("Decoupe de "+nb_pix+" pixels");
      }
      this.do_decoup=false;
      return nb_pix;
    }

    public float evalDecoupe()
    { // Retourne le nb de pixels decoupés
      return (dec_prog*((float)tailleDecoupe()));
    }

      public int tailleDecoupe()
      { // Retourne le nb de pixels à decouper
        if (dec_y1==dec_y2)
          {
            return x2-x1;
          }
        /*else
          {*/
            return y2-y1;
          //}
      }

    public Point SommeForces()
    {
      // Renvoie le vecteur somme des forces
      Point res,tmp;
      res=new Point(0,0);
      tmp=new Point(0,0);

      for (int i=0;i<Rob_Pouss.size();i++)
      {
        tmp=((Agisseur)Rob_Pouss.elementAt(i)).GetForce();
        res.x+=tmp.x;res.y+=tmp.y;
      }
      return res;
    }

    public boolean ForcesSuffisantes()
    { // Determine si la somme des forces appliquées est suffisante
      // pour permettre le déplacement de l'objet % frottements
      // Ex. Une plaque 32x32 pèse 1024
      Point tmp=SommeForces();
      int somf=(int)Math.sqrt(Math.pow((double)tmp.x,2)+Math.pow((double)tmp.y,2));
      if (somf>=ForceRequise()) {return true;}
      //else
    	  return false;
    }

    public boolean AddMainteneur(int numRob)
    {
      // Ajoute un Mainteneur à la plaque
      // Si Pas possible: renvoie FALSE (ex. plus de place ...) (rare?)
      if (HasMainteneur(numRob))
      { System.out.println("Doublon mainteneur("+numRob+")");
          return false; }
      Rob_Maint.addElement(new Agisseur(numRob,0,0));
      return true;
    }

    public boolean AddDecoupeur(int numRob,int xr, int yr)
    {
      // Assigne un Découpeur à la plaque
      // Si Pas possible: renvoie FALSE
      if ((Rob_Decoup==-1)&&(numRob!=-1)) {Rob_Decoup=numRob;}
      else {  return false;  }

      // Specifie le sens
      if ((x2-x1)<(y2-y1))
      { // Test decoupe horizontale:
        dec_x1=x1;dec_y1=(y2+y1)/2; dec_x2=x2; dec_y2=dec_y1;
        return true;
      }
      /*else
        // if (direction==VERTICAL)
        {*/
          // Test decoupe vertitale:
          dec_x1=(x2+x1)/2; dec_y1=y1; dec_x2=dec_x1; dec_y2=y2;
          return true;
        //}
    }

    private int GetIndice_with_id(int IdRob,Vector Tab)
    {
      // Recherche l'indice d'un l'élt pour un id donné
      // -1 sinon
      boolean cont=true;
      int i=0;

      while (cont && (i<Tab.size()))
      {
        if ( ((Agisseur)(Tab.elementAt(i))).RobotNum==IdRob)
          return i;
        if (i>=Tab.size()) return -1;
        i++;
      }
      return -1;
    }

    public boolean RemPousseur(int IdRob)
    {
      int IndRob=GetIndice_with_id(IdRob,Rob_Pouss);
      if (IndRob==-1) return false;
      Rob_Pouss.removeElementAt(IndRob);
      return true;
    }

    public boolean RemMainteneur(int IdRob)
    {
      int IndRob=GetIndice_with_id(IdRob,Rob_Pouss);
      if (IndRob==-1) return false;
      Rob_Maint.removeElementAt(IndRob);
      return true;
    }

    public boolean RemDecoupeur(int IdRob)
    {
      if (IdRob!=Rob_Decoup) return false;
      // else
      Rob_Decoup=-1;
      return true;
    }

    //public int carre(int x) {return x*x;}

    public boolean EstSurDepotPC(int xC,int yC, int R,int xP, int yP)
    {
      // Reponds true sir le point (xP,yP) appartient au cercle (xC,yC,R)
      if (Math.sqrt(carre(xP-xC)+carre(yP-yC))<R) {return true;}
      //else
      return false;
    }
    public boolean EstSurDepotZC(int x,int y, int R)
    {
      // Reponds true si la plaque est sur la zone de dépot
      // "sur" : quand les 4 coins sont dans la zone
      // Représentée par le Cercle (x,y) de rayon R
      if (EstSurDepotPC(x,y,R,x1,y1)
        &&EstSurDepotPC(x,y,R,x1,y2)
        &&EstSurDepotPC(x,y,R,x2,y1)
        &&EstSurDepotPC(x,y,R,x2,y2))
        return true;
      //else
      return false;
    }

}

