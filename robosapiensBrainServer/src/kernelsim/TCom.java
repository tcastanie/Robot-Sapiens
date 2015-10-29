package kernelsim;

//import java.awt.*;

// Olivier Simonin v1.0
//

public class TCom {

    public int t[][];
    int c; // cote du triangle

    public TCom(int c)
    {
     this.c=c;
     t = new int[c][];
     for(int i=0;i<c;i++)
	  t[i]=new int[c-i];
    }

    public void fill(int v)
    {
     for(int i=0;i<c;i++)
	for(int j=0;j<(c-i);j++)
	   t[i][j]=v;
    }

    // suppose a,b compris entre 1 et c
    public void put(int a, int b, int v)
    {
      if (a<b) {int tempo=a; a=b; b=tempo;}
      t[c-a+1][b-1]=v;
    }

    public int get(int a, int b)
    {
      if (a<b) {int tempo=a; a=b; b=tempo;}
      return(t[c-a+1][b-1]);
    }
}

