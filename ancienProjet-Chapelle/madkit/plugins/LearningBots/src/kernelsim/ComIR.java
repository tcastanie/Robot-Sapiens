package kernelsim;

//import java.awt.*;

// Olivier Simonin v1.0
//

public class ComIR {

    public char[] trame; // 2 octets
    public float dist,Ddist,Dsat,sat;
    public boolean c,known;
    public int signSat;

    public ComIR()
    {
	char[] trame= new char[2];
	trame[0]=0;
	trame[1]=0;
    }

    public void seto1(char v)
    {
	trame[0]=v;
    }

    public void seto2(char v)
    {
	trame[1]=v;
    }

    public char geto1(char v)
    {
	return trame[0];
    }

    public char geto2(char v)
    {
	return trame[1];
    }
}

