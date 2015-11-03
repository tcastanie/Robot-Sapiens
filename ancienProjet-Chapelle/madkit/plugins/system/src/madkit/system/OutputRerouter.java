/*
* OutputRerouter.java - OutputRerouter, a way to reroute all System.out to an agent
* Copyright (C) 2002 Jean-Gabriel Bertrand, Omar Mohsine
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

package madkit.system;

import java.io.OutputStream;
import java.io.PrintStream;

import madkit.kernel.Agent;
import madkit.kernel.Message;
import madkit.kernel.StringMessage;


class MessageOutputStream extends OutputStream {
    OutputRerouter ag;

    MessageOutputStream(OutputRerouter _ag){
        ag = _ag;
    }

    String str="";

  public void write(int x)
  {
    str = str + (char) x;
    if (x == '\n')
      flush();
  }


  public void write(String s)
  {   str = str + s;
      flush();
  }

  public synchronized void write (byte[] data, int off, int len){
    //flush();
    write(new String(data, off, len));
  }

  public synchronized void flush() {
    if (! str.equals("")) {
	    ag.sendString(str);
	    str = "";
      }
  }

  public void close () {
    flush();
  }
}

public class OutputRerouter extends Agent {

  String groupName;
  MessageOutputStream outStream;
  PrintStream out;

  public void activate(){
      groupName = "output-"+this.getAddress().getKernel().getHost();
      createGroup(true,groupName,null,null);
      requestRole(groupName,"source",null);

      outStream = new MessageOutputStream(this);
      out = new PrintStream(outStream, true);

      System.setOut(out);
      System.setErr(out);
  }

  public void live(){
    while(true){
      Message m = waitNextMessage();
    }
  }

  void sendString(String s){
    this.broadcastMessage(groupName,"listener",new StringMessage(s));
    //print(s);
  }

}
