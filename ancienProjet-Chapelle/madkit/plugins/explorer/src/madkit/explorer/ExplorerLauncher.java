package madkit.explorer;

import madkit.kernel.*;
import javax.swing.JRootPane;
import java.lang.reflect.*;

public class ExplorerLauncher extends Agent{

	boolean life;
	JRootPane display;

	public void activate(){
		life = true;
		println("Explorer Agent Activated");
		createGroup(true,"Landscape",null,null);
		requestRole("Landscape","Explorer",null);
	}

	public void live(){
		while(life){
			exitImmediatlyOnKill();
			Message m = waitNextMessage();
			handleMessage(m);
		}
	}

	public void initGUI(){
		display = new ExplorerGUI(this);
        	setGUIObject(display);
    	}

	public void killExplorerAgent(){
		life = false;
                this.killAgent(this);
	}

	public void end() {
		println("Bye Bye !!");
		println ("Explorer Agent Ended");
	}
	protected void handleMessage(Message ms){
		System.out.println("Handling message :"+ms);
		 if (ms.getClass().getName().equals("SEdit.SEditMessage")){
			try {
				Class c = Utils.loadClass("SEdit.SEditTools");
				Class[] args=new Class[2];
				args[0] = Utils.loadClass("madkit.kernel.AbstractAgent");
				args[1] = Utils.loadClass("madkit.kernel.Message");
				Method meth=c.getMethod("createStructure",args);
				meth.invoke(c,new Object[]{this,ms});
			} catch(ClassNotFoundException ex){
				System.err.println("Error: SEdit not installed");
			} catch(NoSuchMethodException ex){
				System.err.println("Internal error with SEdit "+ex);
			} catch(InvocationTargetException ex){
			 System.err.println("Internal error with SEdit "+ex);
			}catch(IllegalAccessException ex){
			System.err.println("Internal error with SEdit "+ex);
		   }
		 }
		 else if (ms instanceof StringMessage){
		   handleMessage((StringMessage) ms);
		 }
	   }
	
	protected void handleMessage(StringMessage str){
	}
	
}
