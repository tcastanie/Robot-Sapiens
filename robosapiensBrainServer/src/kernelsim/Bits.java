package kernelsim;

import java.util.*;


 public class Bits
 {

  public BitSet o;

  public Bits()
     {
	 //@SuppressWarnings("unused")
	  //051018 BitSet
	  o=new BitSet(8);
	  
     }

 public void init8(char v1,char v2,char v3,char v4,char v5,char v6,char v7,char v8)
 {
  if (v1==1) o.set(Bits8.c1); else o.clear(Bits8.c1);
  if (v2==1) o.set(Bits8.c2); else o.clear(Bits8.c2);
  if (v3==1) o.set(Bits8.c3); else o.clear(Bits8.c3);
  if (v4==1) o.set(Bits8.c4); else o.clear(Bits8.c4);
  if (v5==1) o.set(Bits8.c5); else o.clear(Bits8.c5);
  if (v6==1) o.set(Bits8.c6); else o.clear(Bits8.c6);
  if (v7==1) o.set(Bits8.c7); else o.clear(Bits8.c7);
  if (v8==1) o.set(Bits8.c8); else o.clear(Bits8.c8);
 }

public void initc(char car)
{

  if ((car & 1)==1) o.set(Bits8.c1); else o.clear(Bits8.c1);
  if ((car & 2)==1) o.set(Bits8.c2); else o.clear(Bits8.c2);
  if ((car & 4)==1) o.set(Bits8.c3); else o.clear(Bits8.c3);
  if ((car & 8)==1) o.set(Bits8.c4); else o.clear(Bits8.c4);
  if ((car & 16)==1) o.set(Bits8.c5); else o.clear(Bits8.c5);
  if ((car & 32)==1) o.set(Bits8.c6); else o.clear(Bits8.c6);
  if ((car & 64)==1) o.set(Bits8.c7); else o.clear(Bits8.c7);
  if ((car & 128)==1) o.set(Bits8.c8); else o.clear(Bits8.c8);

}

 public char bitp(int p)
     {
	 char r=0;
	 switch(p){
	 case 1: if (o.get(Bits8.c1)) r=1; break;
	 case 2: if (o.get(Bits8.c2)) r=1; break;
	 case 3: if (o.get(Bits8.c3)) r=1; break;
	 case 4: if (o.get(Bits8.c4)) r=1; break;
	 case 5: if (o.get(Bits8.c5)) r=1; break;
	 case 6: if (o.get(Bits8.c6)) r=1; break;
	 case 7: if (o.get(Bits8.c7)) r=1; break;
	 case 8: if (o.get(Bits8.c8)) r=1; break;
	 }
       return r;
     }

public char b8toc()
{
  return (char)(bitp(1)+2*bitp(2)+4*bitp(3)+8*bitp(4)+16*bitp(5)+32*bitp(6)+64*bitp(7)+128*bitp(8));
}

public void initbit(char p,char v)
{
   switch(p){
   case 1: if (v==1) o.set(Bits8.c1); else o.clear(Bits8.c1); break;
   case 2: if (v==1) o.set(Bits8.c2); else o.clear(Bits8.c2); break;
   case 3: if (v==1) o.set(Bits8.c3); else o.clear(Bits8.c3); break;
   case 4: if (v==1) o.set(Bits8.c4); else o.clear(Bits8.c4); break;
   case 5: if (v==1) o.set(Bits8.c5); else o.clear(Bits8.c5); break;
   case 6: if (v==1) o.set(Bits8.c6); else o.clear(Bits8.c6); break;
   case 7: if (v==1) o.set(Bits8.c7); else o.clear(Bits8.c7); break;
   case 8: if (v==1) o.set(Bits8.c8); else o.clear(Bits8.c8); break;
  }
}


public char bit(char car, char p)
{
  char r=0;

  switch(p){
   case 1: if ((car & 1)==1) r=1; break;
   case 2: if ((car & 2)==1) r=1; break;
   case 3: if ((car & 4)==1) r=1; break;
   case 4: if ((car & 8)==1) r=1; break;
   case 5: if ((car & 16)==1) r=1; break;
   case 6: if ((car & 32)==1) r=1; break;
   case 7: if ((car & 64)==1) r=1; break;
   case 8: if ((car & 128)==1) r=1; break;

  }

  return r;
}



}

