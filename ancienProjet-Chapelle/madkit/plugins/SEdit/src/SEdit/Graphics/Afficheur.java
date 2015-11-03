/*
* Afficheur.java - SEdit, a tool to design and animate graphs in MadKit
* Copyright (C) 1998-2002 Jacques Ferber, Olivier Gutknecht
*
* This program is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License
* as published by the Free Software Foundation; either version 2
* of the License, or any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
*/
package SEdit.Graphics;

import java.awt.Graphics;


/**************************************************************************

CLASSE Afficheur

**************************************************************************/
public class Afficheur extends Object
{   static final boolean[]  ZERO=   {true,true,true,false,true,true,true};
    static final boolean[]  UN=     {false,false,true,false,false,true,false};
    static final boolean[]  DEUX=   {true,true,false,true,false,true,true};
    static final boolean[]  TROIS=  {true,false,true,true,false,true,true};
    static final boolean[]  QUATRE= {false,false,true,true,true,true,false};
    static final boolean[]  CINQ=   {true,false,true,true,true,false,true};
    static final boolean[]  SIX=    {true,true,true,true,true,false,true};
    static final boolean[]  SEPT=   {false,false,true,false,false,true,true};
    static final boolean[]  HUIT=   {true,true,true,true,true,true,true};
    static final boolean[]  NEUF=   {true,false,true,true,true,true,true};

    static boolean[] Descriptif(int chiffre)
    {   if(chiffre==0)
            return ZERO;
        else if(chiffre==1)
            return UN;
        else if(chiffre==2)
            return DEUX;
        else if(chiffre==3)
            return TROIS;
        else if(chiffre==4)
            return QUATRE;
        else if(chiffre==5)
            return CINQ;
        else if(chiffre==6)
            return SIX;
        else if(chiffre==7)
            return SEPT;
        else if(chiffre==8)
            return HUIT;
        else if(chiffre==9)
            return NEUF;
        else
            return ZERO;    }

    static public void Afficher_chiffre(Graphics g, int a, int x, int y,
                                            int chiffre)
    {   boolean Tab[]=Descriptif(chiffre);
        int b=a/2;

        if(Tab[0])  g.drawLine(x-b,y+a,x+b,y+a);
        if(Tab[1])  g.drawLine(x-b,y+a,x-b,y);
        if(Tab[2])  g.drawLine(x+b,y+a,x+b,y);
        if(Tab[3])  g.drawLine(x-b,y,x+b,y);
        if(Tab[4])  g.drawLine(x-b,y,x-b,y-a);
        if(Tab[5])  g.drawLine(x+b,y,x+b,y-a);
        if(Tab[6])  g.drawLine(x-b,y-a,x+b,y-a);    }

    static public void Afficher_nombre(Graphics g, int a, int x, int y, int nombre)
    //  ! (x,y) : Coordonnes du centre de la chaine ...
    {   String  StrNbr=String.valueOf(nombre);
        int     chiffre;

        int xc=x - (StrNbr.length()-1) * a * 3 / 4;
        int yc=y;
        for(int n=0;n<StrNbr.length();n++)
        {   chiffre=Character.digit( StrNbr.charAt(n) , 10 );
            Afficher_chiffre(g,a,xc,yc,chiffre);
            xc+=3*a/2;
        }   }
}


