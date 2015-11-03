/*
* WarbotMessage.java -Warbot: robots battles in MadKit
* Copyright (C) 2000-2002 Fabien Michel, Jacques Ferber
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
package warbot.kernel;

import madkit.kernel.Message;

final public class WarbotMessage extends Message {
	  String act;

	  String[] content;
	  double fromX=0.0;
	  double fromY=0.0;

	  public double getFromX(){return fromX;}
	  public double getFromY(){return fromY;}

	  void setFromX(double x){fromX=x;}
	  void setFromY(double y){fromY=y;}

	  public String getAct(){return act;}
	  public String[] getContent(){return content;}
	  public int getLength(){return content.length;}

	  public String getArg1(){
	      if (content != null && content.length>0)
		      return content[0];
		  else
		      return null;
	  }

	  public String getArg2(){
	      if (content != null && content.length>1)
		      return content[1];
		  else
		      return null;
	  }

	  public String getArgN(int n){
	      if (content != null && content.length>=n)
		      return content[n-1];
		  else
		      return null;
	  }

	  WarbotMessage(String what){
	      act=what;
	  }

	  WarbotMessage(String what, String[] cont){
	      this(what);
		  content = cont;
	  }

	  WarbotMessage(String what, String arg){
	      this(what);
		  content = new String[1];
		  content[0]=arg;
	  }

	  WarbotMessage(String what, String arg1, String arg2){
	      this(what);
		  content = new String[2];
		  content[0]=arg1;
		  content[1]=arg2;
	  }

}
