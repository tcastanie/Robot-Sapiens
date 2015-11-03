/*
* StatAgent.java - Communicator: the connection module of MadKit
* Copyright (C) 1998-2002 Olivier Gutknecht, Pierre Bommel, Fabien Michel, Thomas Cahuzac
*
* This library is free software; you can redistribute it and/or
* modify it under the terms of the GNU Lesser General Public
* License as published by the Free Software Foundation; either
* version 2.1 of the License, or (at your option) any later version.

* This library is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
* Lesser General Public License for more details.

* You should have received a copy of the GNU Lesser General Public
* License along with this library; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
*/
package madkit.communicator;

import madkit.kernel.*;
import java.util.*;

public class StatAgent extends Thread
{
    protected final int PERIOD=20; // s

    protected final int TOTAL_TYPE=0;
    protected final int SYSTEM_TYPE=1;
    protected final int OTHER_TYPE=2;
    protected final int TCP_TYPE=3;
    protected final int UDP_TYPE=4;
	
    protected final int NB_TYPE=5;

    protected final String[] TYPE_NAMES={"TOTAL","SYSTEM","OTHER","TCP","UDP"};

    protected final String[] ALL_MESSAGES={"madkit.kernel.KernelMessage","java.util.Vector"};          

    protected final int NB_MESSAGE_TYPES=ALL_MESSAGES.length;
					   
    protected long oldTime,time;

    protected int in[],out[];
    protected int nbIn[],nbOut[];

    public StatAgent()
    {
	in=new int[NB_TYPE];
	out=new int[NB_TYPE];
	nbIn=new int[NB_TYPE];
	nbOut=new int[NB_TYPE];
    }
		     
    private boolean isType(Object o,int type,boolean tcp)
    {
// 	boolean found=false;
// 	for (int i=0;!found && i<NB_MESSAGE_TYPES;i++)
// 	    if (o.getClass().getName().equals(ALL_MESSAGES[i]))
// 		found=true;

// 	if (!found)
// 	    {
// 		System.out.println("StatAgent: unknow message type: "+o.getClass().getName());
// 		System.exit(1);
// 	    }

	if (type==TOTAL_TYPE)
	    {
		return true;
	    }
	if (type==SYSTEM_TYPE)
	    {
		return (o instanceof KernelMessage || o instanceof Vector);
	    }
	if (type==OTHER_TYPE)
	    {
		return !(o instanceof KernelMessage || o instanceof Vector);
	    }
	if (type==TCP_TYPE)
	    return tcp;
	if (type==UDP_TYPE)
	    return !tcp;

	System.out.println("Unknown stat type: "+type);
	System.exit(1);
	return false;
    }

    public void onIn(int size,boolean tcp,Object o)
    {
	for (int i=0;i<NB_TYPE;i++)
	    if (isType(o,i,tcp))
		{
		    in[i]+=size;
		    nbIn[i]=nbIn[i]+1;
		}
    }

    public void onOut(int size,boolean tcp,Object o)
    {
	for (int i=0;i<NB_TYPE;i++)
	    if (isType(o,i,tcp))
		{
		    out[i]+=size;
		    nbOut[i]=nbOut[i]+1;
		}
    }

    public void run()
    {
	while (true)
	    {
		for (int i=0;i<NB_TYPE;i++)
		    {
			in[i]=0;   
			nbIn[i]=0;
			out[i]=0;  
			nbOut[i]=0;
		    }
	
		oldTime=System.currentTimeMillis();
		
		try
		    {
			sleep(PERIOD*1000);
		    }
		catch (Exception e)
		    {
		    }
		
		time=System.currentTimeMillis();

		//printStat((time-oldTime)/1000.0);
				
	    }
    }

    private void printAndFill(int n,int l)
    {
	printAndFill(n+"",l);
    }

    private void printAndFill(float n,int l)
    {
	printAndFill(n+"",l);
    }

    private void printAndFill(String s,int l)
    {
	l=l-1;
	int dec1=(l-s.length())/2;
	int dec2=(l-s.length())-dec1;

	for (int i=0;i<dec1;i++)
	    System.out.print(" ");

	System.out.print(s);

	for (int i=0;i<dec2;i++)
	    System.out.print(" ");
	System.out.print("|");
    }

    private void line(int l)
    {
	for (int i=0;i<l;i++)
	    System.out.print("-");
    }


    private void printStat(double dt)
    {
	printAndFill("",11);
	line(80);
	System.out.println();

	printAndFill("",11);
	printAndFill("IN",40);
	printAndFill("OUT",40);
	System.out.println();

	printAndFill("",11);
	line(80);
	System.out.println();

	printAndFill("",11);
	printAndFill("NB",10);
	printAndFill("SIZE (o)",10);
	printAndFill("AVERAGE",10);
	printAndFill("FLOW(o/s)",10);
	printAndFill("NB",10);
	printAndFill("SIZE (o)",10);
	printAndFill("AVERAGE",10);
	printAndFill("FLOW(o/s)",10);

	System.out.println();

	line(91);
	System.out.println();

	for (int i=0;i<NB_TYPE;i++)
	    lineValues(TYPE_NAMES[i],nbIn[i],in[i],nbOut[i],out[i],dt);
    }

    private void lineValues(String title,int nb1,int s1,int nb2,int s2,double dt)
    {
	System.out.print("|");

	printAndFill(title,10);
	printAndFill(nb1,10);
	printAndFill(s1,10);
	printAndFill(s1/(float) nb1,10);
	printAndFill((float) (s1/dt),10);

	printAndFill(nb2,10);
	printAndFill(s2,10);
	printAndFill(s2/(float) nb2,10);
	printAndFill((float) (s2/dt),10);

	System.out.println();

	line(91);
	System.out.println();
	
    }
	
}
