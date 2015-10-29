package kernelsim;

import java.awt.*;
import java.io.*;

public class MatEnv {

  public int dimx,dimy;
  public byte[] mat; // val. de -128 a 127
  public static Point[] Disk7ext;

  public void init()
    {
	  /* Disk7ext Utilisé par une seule fonction
	   * Laquelle n'est plus utilisée 050919
     Disk7ext= new Point[36];
     Disk7ext[0]=new Point(0,-7);
     Disk7ext[1]=new Point(-1,-7);
     Disk7ext[2]=new Point(1,-7);
     Disk7ext[3]=new Point(-2,-6);
     Disk7ext[4]=new Point(2,-6);
     Disk7ext[5]=new Point(-3,-6);
     Disk7ext[6]=new Point(3,-6);
     Disk7ext[7]=new Point(-4,-5);
     Disk7ext[8]=new Point(4,-5);
     Disk7ext[9]=new Point(-5,-4);
     Disk7ext[10]=new Point(5,-4);
     Disk7ext[11]=new Point(-6,-3);
     Disk7ext[12]=new Point(6,-3);
     Disk7ext[13]=new Point(-6,-2);
     Disk7ext[14]=new Point(6,-2);
     Disk7ext[15]=new Point(-7,-1);
     Disk7ext[16]=new Point(7,-1);
     Disk7ext[17]=new Point(-7,0);
     Disk7ext[18]=new Point(7,0);
     Disk7ext[19]=new Point(0,7);
     Disk7ext[20]=new Point(-1,7);
     Disk7ext[21]=new Point(1,7);
     Disk7ext[22]=new Point(-2,6);
     Disk7ext[23]=new Point(2,6);
     Disk7ext[24]=new Point(-3,6);
     Disk7ext[25]=new Point(3,6);
     Disk7ext[26]=new Point(-4,5);
     Disk7ext[27]=new Point(4,5);
     Disk7ext[28]=new Point(-5,4);
     Disk7ext[29]=new Point(5,4);
     Disk7ext[30]=new Point(-6,3);
     Disk7ext[31]=new Point(6,3);
     Disk7ext[32]=new Point(-6,2);
     Disk7ext[33]=new Point(6,2);
     Disk7ext[34]=new Point(-7,1);
     Disk7ext[35]=new Point(7,1);*/
    }

  public MatEnv(int dimx, int dimy)
    {
        init();
	this.dimx = dimx;
	this.dimy = dimy;
	mat = new byte[dimx*dimy];
    }

  public MatEnv(String filename)
    {
     init();
     loadfond(filename);
    }

  public MatEnv() { this(0,0); }

  
  public byte get_val(int x, int y)
    {
     if ((x>=0) && (x<dimx) && (y>=0) && (y<dimy))
        return mat[y*dimx+x];
      //else
	return -1;
    }
  
  /*
  public void put_val(int x, int y, byte v)
    {
     mat[y*dimx+x]=v;
    }
  */
  /*
  public void horiz(int x1,int x2,int y,byte v)
    {
     for(int i=x1;i<=x2;i++) mat[y*dimx+i]=v;
    }

  public void vert(int y1,int y2,int x,byte v)
    {
     for(int j=y1;j<=y2;j++) mat[j*dimx+x]=v;
    }
  */
  /*
  public void move_box(int x1,int y1,int l,int h,byte dir)
    {
      switch (dir) {
      case 1 : { // up
	        horiz(x1,x1+l,y1-1,get_val(x1,y1));
	        horiz(x1,x1+l,y1+h,(byte)0);
		break;
               }
      case 2 : { // down
	        horiz(x1,x1+l,y1+h+1,get_val(x1,y1));
	        horiz(x1,x1+l,y1,(byte)0);
	        break; }
      case 3 : { // right
	        vert(y1,y1+h,x1+l+1,get_val(x1,y1));
	        vert(y1,y1+h,x1,(byte)0);
	        break; }
      case 4 : { // left
	        vert(y1,y1+h,x1-1,get_val(x1,y1));
	        vert(y1,y1+h,x1+l,(byte)0);
               }
      }
    }
  */
  
  /*
  public void addDisk(int xc,int yc,int r,byte v)
    {
    switch (r) {
     case 7 :
     {
      Rect re=new Rect(xc-4,yc-4,xc+4,yc+4,null);
      addRect(re,v);
      horiz(xc-3,xc+3,yc-5,v);
      horiz(xc-3,xc+3,yc+5,v);
      vert(yc-3,yc+3,xc-5,v);
      vert(yc-3,yc+3,xc+5,v);
      horiz(xc-1,xc+1,yc-6,v);
      horiz(xc-1,xc+1,yc+6,v);
      vert(yc-1,yc+1,xc-6,v);
      vert(yc-1,yc+1,xc+6,v);
     }
     }
    }
    */
  /*
  public void move_disk(int xc,int yc,int xn,int yn,int r)
    {
     byte oldv=get_val(xc,yc);
     switch(r){
     case 1: {
	 put_val(xc,yc,(byte)0);
	 put_val(xn,yn,oldv);
	 break;
         }
     case 7: {
	 addDisk(xc,yc,r,(byte)0);
	 addDisk(xn,yn,r,oldv);
	 break;
         }
     }
    }
    */
  
  public boolean test_horiz(int x1,int x2,int y)
    {
      boolean rep=true;
      if (y<0 || y>=dimy || x1<0 || x2>=dimx) rep=false;
      else
       for(int i=x1;i<=x2;i++) if (mat[y*dimx+i]!=0) rep=false;
      return rep;
    }

  public boolean test_vert(int y1,int y2,int x)
    {
      boolean rep=true;
      if (x<0 || x>=dimx || y1<0 || y2>=dimy) rep=false;
      else
       for(int i=y1;i<=y2;i++) if (mat[i*dimx+x]!=0) rep=false;
      return rep;
    }

  /*
   * 051019 Je ne sais pas à quoi devait servir cette fonction ?
   * Peut etre pour débug ou test ???
  public Point testD7_v(int xc,int yc,byte v1,byte v2)
    {
    boolean f=false;
    int i=0;
    byte v=0;
    while(i<36 && f==false)
     {
      v=get_val(xc+Disk7ext[i].x,yc+Disk7ext[i].y);
      if (v>=v1 && v<=v2) f=true;
      else i++;
     }
    if (f==true) return Disk7ext[i]; else return null;
    }
  */
  public boolean test_moveD(int xc,int yc,int r,int dir)
    {
     boolean rep=false;
     switch(r)
     {
       case 1: switch(dir) //============== rayon 1 ===============
	{
	case 1 : if (get_val(xc,yc-1)==0) rep=true; break;
	case 2 : if (get_val(xc,yc+1)==0) rep=true; break;
	case 3 : if (get_val(xc+1,yc)==0) rep=true; break;
	case 4 : if (get_val(xc-1,yc)==0) rep=true; break;
	} break;
       case 7: switch(dir) //============= rayon 7 =================
	{
      	case 1 : if (test_horiz(xc-1,xc+1,yc-7)==true && test_horiz(xc-3,xc-2,yc-6)==true  && test_horiz(xc+2,xc+3,yc-6)==true && get_val(xc-4,yc-5)==0 && get_val(xc+4,yc-5)==0 && get_val(xc-5,yc-4)==0 && get_val(xc+5,yc-4)==0 && get_val(xc-6,yc-2)==0 && get_val(xc+6,yc-2)==0) rep=true; break;
        case 3 : if (test_vert(yc-1,yc+1,xc+7)==true && test_vert(yc-3,yc-2,xc+6)==true  && test_vert(yc+2,yc+3,xc+6)==true && get_val(xc+5,yc-4)==0 && get_val(xc+5,yc+4)==0 && get_val(xc+4,yc-5)==0 && get_val(xc+4,yc+5)==0 && get_val(xc+2,yc-6)==0 && get_val(xc+2,yc+6)==0) rep=true; break;
	case 2 : if (test_horiz(xc-1,xc+1,yc+7)==true && test_horiz(xc-3,xc-2,yc+6)==true  && test_horiz(xc+2,xc+3,yc+6)==true && get_val(xc-4,yc+5)==0 && get_val(xc+4,yc+5)==0 && get_val(xc-5,yc+4)==0 && get_val(xc+5,yc+4)==0 && get_val(xc-6,yc+2)==0 && get_val(xc+6,yc+2)==0) rep=true; break;
	case 4 : if (test_vert(yc-1,yc+1,xc-7)==true && test_vert(yc-3,yc-2,xc-6)==true  && test_vert(yc+2,yc+3,xc-6)==true && get_val(xc-5,yc-4)==0 && get_val(xc-5,yc+4)==0 && get_val(xc-4,yc-5)==0 && get_val(xc-4,yc+5)==0 && get_val(xc-2,yc-6)==0 && get_val(xc-2,yc+6)==0) rep=true; break;
	} break;
        //=======================================================
     }
     return rep;
    }

  public Vecteur radar(int x0,int y0,int d0,int dR,int density,int RA)
    {
     double alpha=0;
     double dalpha=Math.PI/RA;
     double d=d0;
     int vx,vy,k=0;
     byte val;
     Vecteur v= new Vecteur(0,0);
     while(alpha<2*Math.PI)
      {
       for(d=d0;d<=dR;d+=density)
        {
         vx=(int)Math.round(d*Math.cos(alpha));
         vy=(int)Math.round(d*Math.sin(alpha));
         val=get_val(x0+vx,y0+vy);
         if ((val!=0) && (val>-2))
           { v.addV((int)(vx*(1+1/d)),(int)(-vy*(1+1/d))); k++; }
        }
       alpha+=dalpha;
      }
      if (k>0) { v.vx=v.vx/k; v.vy=v.vy/k;}
      return v;
    }

  /*
 public Vecteur radarVal(int x0,int y0,int d0,int dR,int density,int RA,byte v1,byte v2)
    {
     double alpha=0;
     double dalpha=0;;
     double d=d0;
     //@SuppressWarnings("unused")
     int vx,vy; //051018 ,k=0;
     byte val=0;
     boolean first=false;
     Vecteur v= new Vecteur(0,0);
     while((d<=dR) && (first==false))
     {
     dalpha=2/d;
     for(alpha=0;alpha<(2*Math.PI);alpha+=dalpha)
      {
         vx=(int)Math.round(d*Math.cos(alpha));
         vy=(int)Math.round(d*Math.sin(alpha));
         val=get_val(x0+vx,y0-vy);
	 // if (val!=0) System.out.print(val);
         if ((val>=v1) && (val<=v2))
           { v=new Vecteur(vx,vy);
 	     first=true;
	   }
        }
       d+=density;
      }
      return v;
    }
*/
  public Vecteur radarV(int x0,int y0,float dir,float ouv,float r0,float rm,float density,byte except,byte agt)
   {
     double alpha=0;
     double dalpha=0;
     @SuppressWarnings("unused")
     float d=r0,l,dmin=rm;
     @SuppressWarnings("unused")
     float rr=rm/3;
     float vx,vy;
     int k=0;
     byte sign=(byte)1;
     byte val;
     Vecteur v= new Vecteur(0,0);
     while(d<=rm)
     {
     dalpha=2/d;
     for(alpha=dir-ouv;alpha<(dir+ouv);alpha+=dalpha)
        {
         vx=(float)Math.cos(alpha);
         vy=(float)Math.sin(alpha);
         val=get_val(x0+Math.round(vx*d),y0-Math.round(vy*d));
	 if (val==agt) val=0;
	 //   if ((val!=0) && (val>except)) // test matiere mur ou rob
	 if ((val!=0) && (val>except))
           {
	       if (d<dmin) dmin=d;
	       v.addV(Math.round(vx*(rm-d)),Math.round(vy*(rm-d))); k++;
	       if (val>0) sign=(byte)-1;
	   }
        }
       d=d+density;
      }
     v.fixN((float)dmin);
     v.s=sign;
     return v;
   }
  /*
  public VecteurR radOBS(int x0,int y0,float dir,float ouv,float r0,float rm,float density,byte except,byte agt)
   {
     double alpha=0;
     double dalpha=0;
     @SuppressWarnings("unused")
     float d=r0,l,dmin=rm;
     @SuppressWarnings("unused")
     float rr=rm/3;
     float vx,vy;
     int k=0;
     //051018 byte sign=(byte)1;
     byte val;
     VecteurR v= new VecteurR(0,0);
     while(d<=rm)
     {
     dalpha=2/(d+2);
     for(alpha=dir-ouv;alpha<(dir+ouv);alpha+=dalpha)
        {
         vx=(float)Math.cos(alpha);
         vy=(float)Math.sin(alpha);
         val=get_val(x0+Math.round(vx*d),y0-Math.round(vy*d));
	 if (val==agt) val=0;
	 //   if ((val!=0) && (val>except)) // test matiere mur ou rob
	 if ((val!=0) && (val>except))
           {
	       if (d<dmin) dmin=d;
	       v.addV(vx*(rm-d),vy*(rm-d)); k++;
	       //       if (val>0) sign=(byte)-1;
	   }
        }
       d=d+density;
      }
     v.fixN((float)dmin);
     // v.s=sign;
     return v;
   }
  */
  
  public void draw(int x0,int y0,Graphics g,boolean aff)
    {
     byte c=0,cp=0;
     int xp;
     for(int j=0;j<dimy;j++)
      {
      int i=0;xp=0;
      while(i<dimx)
	  {
	   c=mat[j*dimx+i];
	   if ((c!=cp) || (i==dimx-1))
	       {
		if (i>0)
		 {
		  if (i==dimx-1) i++;
		  if (cp<0)
		  {
		   g.setColor(new Color((int)cp*100));
		   g.drawLine(x0+xp,y0+j,x0+i-1,y0+j);
		  }
		 }
		xp=i; cp=c;
	       }
	   i++;
	  }
      }
    }

  public void addRect(Rect r, byte v)
    {
     for(int j=r.y1;j<=r.y2;j++)
      for(int i=r.x1;i<=r.x2;i++)
       mat[j*dimx+i]=v;
    }

  /*
 public void addRect(int x1,int y1,int x2,int y2, byte v)
    {
     for(int j=y1;j<=y2;j++)
      for(int i=x1;i<=x2;i++)
       mat[j*dimx+i]=v;
    }
  */
  public void loadfond(String fileenv)
    {
     //File f;
     //FileReader in = null;
     int size=0,S=0;
     char[] data = null;

	/*
     try {
        f=new File(fileenv);
	in = new FileReader(f);
        size = (int) f.length();
	data = new char[size];
	int chars_read=0;
	while(chars_read<size)
	    chars_read += in.read(data, chars_read, size-chars_read);
     }
     catch (IOException e) {System.out.println(e);}
     finally { try { if (in != null) in.close();} catch (IOException e) {} }
     */
     //-----------------------------------------------------------------------------------------------------------
	String fichierenv=null;
	System.out.println("Fileenv="+fileenv);
      try
	  {
	  	String rsrc="/worlds/"+fileenv; //+".env";
          	InputStream defs=null;
           
	       defs = this.getClass().getResourceAsStream(rsrc);

	      BufferedReader dip = new BufferedReader (new InputStreamReader(defs));
	      String s=null;
	      
	      while((s=dip.readLine()) != null)
		  {
		  	if (fichierenv==null) {fichierenv=s+'\n';}
		  	else {fichierenv+=s+'\n';}
		      //System.out.println(s);
		      //libsource.append('\n');
		      //libsource.append(s);
		  }
	  }
      catch (Exception eofe)
	  {
	      System.err.println("Load:"+eofe.getMessage());
	      eofe.printStackTrace();
	  }
	
     if (fichierenv!=null)
	{
		data = fichierenv.toCharArray();
		size = fichierenv.length();
	}
     System.out.println("Lecture par nv fnct("+size+"):");
     for (int i=0;i<size;i++)
     { System.out.print(data[i]);}  
     // traitement du texte charge :
     int i=0,n=1,l=0,v;
     char c;
     char[] nbT= new char[12];
     boolean finNb=false,first=true;
     //
     Rect r = new Rect();
     while(i<size)
	 {
	  c=data[i];
	  if ((c>='0') && (c<='9'))
	      {
	       nbT[l]=c;
	       l++;
	       finNb=true;
	      }
	   else
	       {   if (finNb==true)
		   {
		    String s=String.valueOf(nbT,0,l);
		    v=Integer.parseInt(s);
		    switch (n) {
		    case 1: r.x1=v;break;
		    case 2: r.y1=v;break;
		    case 3: r.x2=v;break;
		    case 4: r.y2=v;
		   }
		   n++;
		   if (n==5)
		       { n=1;
		         if (first==true) {
			   dimx = r.x2;
			   dimy = r.y2;
			   mat = new byte[dimx*dimy];
			   first=false;
			   }
			 else { addRect(r,(byte)-1); S=S+Math.abs((r.x2-r.x1)*(r.y2-r.y1)); }
		       }
		   finNb=false; l=0;
		   }
	       }
	  i++;
	 }
     System.out.println("S="+S);
     System.out.println("%="+(100*S/(dimx*dimy)));
    }

}

