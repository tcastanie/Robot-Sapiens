package kernelsim;
import madkit.kernel.*;

//import java.awt.*;
import java.util.Vector; // Pour les Vecteurs d'objets

//import java.io.*;

// ************************************************* //
// *** Classe Plaques - Jérôme CHAPELLE          *** //
// ************************************************* //


public class Plaques extends AbstractAgent{

	static final long serialVersionUID = RmcLauncherApp.serialVersionUID; //42L;
    public Vector<Plaque> TabPlaques;  // Les robots qui pousssent ou tirent
    private static int IdNum=0;

    //private Rect[] PlqTabR;
    //private int PlqNbR;
    //051018nl private Vector AllRobots;
    public int worldDimX,worldDimY;
    public Plaques()
    {
        TabPlaques=new Vector<Plaque>();
        worldDimX=worldDimY=0;
    }

    public void ResetAll()
    {
      ResetIdNum();
      TabPlaques=new Vector<Plaque>();
    }

    public void ResetIdNum()
    {
      if (TabPlaques.size()==0)
        IdNum=(IdNum+1)%2;
        // Pour que d'une simulation à l'autre on ne puisse avoir
        // des Id identiques.
    }

    public void SetWorldDim(int dimx,int dimy)
    {
        worldDimX=dimx; worldDimY=dimy;
        for (int i=0;i<GetSize();i++)
        { GetPlaque(i).SetWorldDim(dimx,dimy);  }
    }
    public int GetSize()
    {return TabPlaques.size();}

    /*public void SetInfos(Rect[] TabR,int nbR,Vector Robots)
    {
      this.PlqTabR=TabR;
      this.PlqNbR=nbR;
      this.AllRobots=Robots;
    }*/

    public Rect GetRect(int i)
    {
      //Renvoie le rectangle qui représente la i-ieme plaque
      return ((Plaque)TabPlaques.elementAt(i)).GetRect();
    }

    public Plaque GetPlaque(int i)
    {
      //Renvoie la i-ieme plaque
      return ((Plaque)TabPlaques.elementAt(i));
    }
    
    /** Deplace la Plaque d'indice i aux coordonnes (mx,my) */
    public int movePlaque(int i,int mx,int my)
    {
    	if ((i<0)||(i>GetSize())) return -1;
    	((Plaque)TabPlaques.elementAt(i)).SetPosition(mx,my);
    	return i;
    }
    
    public int movePlaqueC(int i,int mx,int my)
    {
    	if ((i<0)||(i>GetSize())) return -1;
    	((Plaque)TabPlaques.elementAt(i)).SetPositionC(mx,my);
    	return i;
    }
    

    public int AddPlaque(Rect R)
    { // Ajoute une plaque et renvoie son identifiant
      return AddPlaque(R.x1,R.y1,R.x2,R.y2);
    }

    public int AddPlaque(int x1,int y1,int x2,int y2)
    {
      return AddPlaque(x1,y1,x2,y2,-1);
    }

    public int AddPlaque(int x1,int y1,int x2,int y2,int pId)
    {
      (TabPlaques).addElement(new Plaque(x1,y1,x2,y2));
      ((Plaque)(TabPlaques.lastElement())).SetWorldDim(worldDimX,worldDimY);
      ((Plaque)(TabPlaques.lastElement())).SetId(getNewId());
      ((Plaque)(TabPlaques.lastElement())).SetParentId(pId);
      return ((Plaque)(TabPlaques.lastElement())).GetId();
    }

    public int AddPlaque(Rect R, int pId)
    { // Ajoute une plaque et renvoie son identifiant
      // Trace la parenté (pere: pId)
      return AddPlaque(R.x1,R.y1,R.x2,R.y2,pId);
    }

    public int IndicePlaque(int id)
    {
      // Recherche l'indice d'un l'élt pour un id donné
      // -1 sinon
      int i=0;

      while (i<TabPlaques.size())
      {
        if (((Plaque)(TabPlaques.elementAt(i))).GetId()==id)
          return i;
        if (i>=TabPlaques.size()) return -1;
        i++;
      }
      return -1;
    }

    public boolean Exist(int id)
    { // Dit si la plaque if existe toujours
      if (IndicePlaque(id)==-1) return false;
      //else 
    	  return true;
    }

    public int Separer(int IdPlaque)
    {
      // Sépare une plaque en deux nouvelles
      // dont le pId est celui de l'ancienne
      // et l'id de la première plaque est renvoyé
      // (l'id de la seconde = id première + 2)
      // -1 si erreur

      int oldInd,newIdA,newIdB;
      // Resp.: ancien indice, id nvels plaques A & B
      oldInd=IndicePlaque(IdPlaque);
      if (oldInd==-1) return -1;
      Plaque oldPlq=GetPlaque(oldInd);
      if (oldPlq.dec_prog<1) return -1;
      if (oldPlq.dec_x1==oldPlq.dec_x2)
      {
        // Decoupe verticale
        // Plaque gauche:
        newIdA=this.AddPlaque(oldPlq.x1,oldPlq.y1,oldPlq.dec_x1,oldPlq.y2,IdPlaque);
        // Plaque droite
        newIdB=this.AddPlaque(oldPlq.dec_x1+1,oldPlq.y1,oldPlq.x2,oldPlq.y2,IdPlaque);
        //newIdB=newIdA+2;
      }
      else
        if (oldPlq.dec_y1==oldPlq.dec_y2)
        {
          // Decoupe horizontale
          // Plaque du haut
          newIdA=AddPlaque(oldPlq.x1,oldPlq.y1,oldPlq.x2,oldPlq.dec_y1,IdPlaque);
          // Plaque du bas
          newIdB=this.AddPlaque(oldPlq.x1,oldPlq.dec_y1+1,oldPlq.x2,oldPlq.y2,IdPlaque);
          //newIdB=newIdA+2;
        }
        else
        { // Si on arrive ici, c'est que les infos de decoupe dec_x1 ... dec_2
          // Ne représentent pas une ligne mais un rectangle (Bizarre)
          System.out.println("Bug Plaques.Separer: ligne de decoupe erronnee");
          return -1;
        }


      // Les numéros doivent se suivre (cohérence valeur de retour):
      if (newIdA+2!=newIdB)
        {System.out.println("Bug separation: Id non-consecutifs");return -1;}
      oldInd=IndicePlaque(IdPlaque);
      TabPlaques.removeElementAt(oldInd);
      //TabPlaques.addElement(new Plaque(x1,x2,y1,y2));

      // Reviendre
      return newIdA;
    }

    public int getNewId()
    { // PS la valeur d'increment
      // doit etre la meme que dans la fonction Separer if (newIdA+2!=newIdB)
      IdNum+=2;
      return (IdNum);
    }

    public int DegagementZoneDepot(Rect Zone_Depot,int Taille_Zone_Depot)
    { // Fait disparaitre les Plaques qui sont dans la zone de Dépot
      // Renvoie le nombre de plaques supprimées
      int i=0;
      int n=0;
      while (i<TabPlaques.size())
        {
          if (((Plaque)TabPlaques.elementAt(i)).EstSurDepotZC(Zone_Depot.x1+(Taille_Zone_Depot/2),
                                                              Zone_Depot.y1+(Taille_Zone_Depot/2),
                                                              Taille_Zone_Depot/2))
            { n++;
              // ### Notification Robot(s) ? ###
              System.out.println("Enlevement plaque i="+i+" / id="+((Plaque)TabPlaques.elementAt(i)).GetId() );
              TabPlaques.removeElementAt(i);

            }
          else i++;
        }
      return n;
    }
}

