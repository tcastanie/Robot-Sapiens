/*
* EditSchemeAgent.java - SchemeEditor, a simple editor to evaluate Scheme expressions
* Copyright (C) 2000-2002 Jacques Ferber
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
package madkit.scheme;

import gnu.lists.LList;
import gnu.lists.Pair;
import gnu.mapping.Binding;
import gnu.mapping.InPort;
import gnu.mapping.OutPort;
import gnu.mapping.WrongArguments;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import madkit.kernel.Message;
import madkit.messages.ControlMessage;


public class EditSchemeAgent extends SchemeAgent {

  // JessEditorPanel display;

  public SchemeEditorPanel display;

   public EditSchemeAgent(){
  	super();
  }

  public EditSchemeAgent(String s)
  {
    super(s);
  }

  public EditSchemeAgent(File s)
  {
    super(s);
  }


  public void initGUI()
  {
    display = new SchemeEditorPanel(this);
	setOutputWriter(display.stdout());
	//out_p = new OutPort(display.stdout(),"<msg_stdout>");
	//err_p = new OutPort(display.stdout(),"<msg_stderr>");
	out_p = new OutPort(display.stdout(),true,"<msg_stdout>");
	err_p = new OutPort(display.stdout(),"<msg_stderr>");
	setGUIObject(display);
  }


  public void println(String s){
  	display.stdout().println(s);
  }

  public void print(String s){
  	display.stdout().print(s);
  }

  public void activate(){
  	super.activate();

    println(";; EditSchemeAgent version 1.0");
    println(";; Scheme output appears in this window");
    println(";; Type and evaluate your input in the editor above");
  }

  public void doSendControlMessage(String act){
  		sendMessage(getAddress(),new ControlMessage(act));
  }

  public void doSendControlMessage(String act, String cont){
  		sendMessage(getAddress(),new ControlMessage(act,cont));
  }

  public void handleControlMessage(ControlMessage m){
  	String act = m.getAction();
  	// println("<< ControlMessage: " + act);
  	Object v = null;
  	try {
  		if (act.equals("eval")) {
	    	v = interp.eval(m.getContent(),env);
  		}
  		else if (act.equals("load")){
  			try {
	       		FileReader fis = new FileReader(m.getContent());
				kawa.standard.load.loadSource(new InPort(fis),env);

	    	   } catch(FileNotFoundException ex){
      				println("Error opening file " + m.getContent());
	    	   } catch(IOException ex){
      				println("Error reading file " + m.getContent());
	    	   } catch(Throwable ex){
                    println("Error with file "+ m.getContent());
               }
    	} else {
  			println("** Error: command unknown " + act);
  			return;
  		}
  		if (v != null){
  		//	println("> " + v.toString());
  		  print("> ");
	      interp.print(v, out_p);
  		} else
  			println("> null");
  	}
  	catch (WrongArguments e)
	  {
	    if (e.usage != null)
	      err_p.println("usage: "+e.usage);
	    e.printStackTrace(err_p);
	  }
	catch (java.lang.ClassCastException e)
	  {
	    err_p.println("Invalid parameter, was: "+ e.getMessage());
	    e.printStackTrace(err_p);
	  }
//	catch (gnu.text.SyntaxException e)
//	  {
//	    e.printAll(err_p, 20);
//	  }
  	catch (Exception re) {
        println("Scheme error doing " + act);
        re.printStackTrace(err_p);
	 }
  }

  public void live(){
	while(true) {
		Message m = waitNextMessage();
		if (m instanceof ControlMessage){
  			handleControlMessage((ControlMessage) m);
  		} else {
  			Binding b=env.lookup("handle-message");
			if (b!= null){
			  try {
				Pair r = new Pair(b.getValue(),new Pair(m,LList.Empty));
				// interp.print(r,out_p);
	    		interp.eval(r,env);
	    	  }
	    	  catch (Exception re) {
        		println("Scheme error " + re.getMessage());
        		re.printStackTrace(err_p);
	 		  }
    		}
  			else
  				println("cannot handle this message : " + m);
  		}
  	}
  }

  public void doPause(int n){
  	pause(n);
  }
}
