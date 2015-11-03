package kernelsim;

//import java.awt.*;

// Olivier Simonin v1.0
//

public class Cap {

    static double PI=Math.PI;

    public char C[]=new char[8];

    public Cap()
    {

    }

    public Cap(Cap c)
    {

      for(int i=0;i<8;i++)
       C[i]=c.C[i];
    }

    public void init()
    {
      for(int i=0;i<8;i++)
       C[i]=0;
    }

    public void valid(double a)
    {
      if (a<=(-PI/2)) C[0]=1;
       else
       if (a<(-5*PI/14)) { C[0]=1; C[1]=1; }
        else
	 if (a<(-3*PI/14)) { C[1]=1; C[2]=1; }
         else
	  if (a<(-PI/14)) { C[2]=1; C[3]=1; }
          else
	   if (a<(PI/14)) { C[3]=1; C[4]=1; }
	   else
	    if (a<(3*PI/14)) { C[4]=1; C[5]=1; }
	    else
	     if (a<(5*PI/14)) { C[5]=1; C[6]=1; }
             else
	      if (a<=(PI/2)) { C[6]=1; C[7]=1; }
              else
	       C[7]=1;
    }

    public boolean exist()
    {
      boolean ex=false;

      for(int i=0;i<8;i++)
       if (C[i]==1) ex=true;

      return ex;
    }

    public char NC(char n)
    {
	if ((n<0) || (n>7)) return 0;
	/*else */return C[n];
    }

    public char Cgoal(float a)
    {
      char n;
      if (a<(-3*PI/7)) n=0;
       else
	if (a<(-2*PI/7)) n=1;
        else
	 if (a<(-PI/7)) n=2;
         else
	  if (a<0) n=3;
          else
	   if (a<(PI/7)) n=4;
	   else
	    if (a<(2*PI/7)) n=5;
	    else
	     if (a<(3*PI/7)) n=6;
             else
	      n=7;

      return n;
    }

    public int side()
    {
	int p1=C[0]+C[1]+C[2]+C[3];
	int p2=C[4]+C[5]+C[6]+C[7];
	if (p1>p2) return 1;
 	 else if (p2>p1) return -1;
	  else return 0;
    }

    public void print()
    {
      for(int i=0;i<8;i++)
        System.out.print(i+":"+(int)C[i]+" ");
      System.out.println();
    }
}

