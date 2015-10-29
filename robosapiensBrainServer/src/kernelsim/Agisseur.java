package kernelsim;

import java.awt.*;
//import java.util.Vector; // Pour les Vecteurs d'objets

// ************************************************* //
// *** Classe Agisseur - Jérôme CHAPELLE         *** //
// ************************************************* //


public class Agisseur {

    public int RobotNum;
    public int xforce,yforce;
    public int puissance=1;

    // ********* Constructeurs *********
    public Agisseur(int RobotNum, int xforce, int yforce,int puissance)
    {
        this.RobotNum=RobotNum;
        this.xforce=xforce;
        this.yforce=yforce;
        this.puissance=puissance;
    }
    public Agisseur(int RobotNum, int xforce, int yforce)
    {        this(RobotNum,xforce, yforce,1);    }
    public Agisseur(int RobotNum)
    {        this(RobotNum,0,0);    }

    // ********* Acceseurs lecture *********
    public Point GetForce()
    {      return new Point(xforce,yforce);    }
    public int GetPuissance()
    {      return puissance;    }

    // ********* Acceseurs écriture *********
    public void SetForce(int x,int y)
    {      xforce=x;yforce=y;    }
    public void SetForce(Point p)
    {      xforce=p.x;yforce=p.y;    }
    public void SetPuissance(int p)
    {      puissance=p;    }

}

