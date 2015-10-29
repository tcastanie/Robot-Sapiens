package kernelsim;

//import java.util.*;


public class Event
{

     public Bits[][] tr;
     public char[] val;

  public Event()
     {
	 Bits tr[][] = new Bits[8][2];
	 for (int i=0;i<8;i++)
	     { tr[i][0] = new Bits(); tr[i][1] = new Bits();}

	 //@SuppressWarnings("unused")
	 //051018 char val[] = new char[8];
     }



}

