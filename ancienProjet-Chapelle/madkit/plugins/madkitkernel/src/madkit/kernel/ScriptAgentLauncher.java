
package madkit.kernel;

import java.lang.reflect.*;

public class ScriptAgentLauncher extends AgentLauncher  {
    

    public void launch(){
        //System.out.println(":: type: " + type + ", className: " + className + ", label: "
        //                    +label+", typeArg: "+typeArg+", arg: "+arg);
        Object a=null;
        try {
            Class cl;

            cl = Utils.loadClass(className);
            Class[] cp0 = new Class[1];
            Object[] co0 = new Object[1];

            //cp0[0]=Class.forName(typeArg);
            cp0[0]=Utils.loadClass(typeArg);
            co0[0]=arg;

            Constructor s = cl.getConstructor(cp0);
            a= s.newInstance(co0);
            //System.out.println(":: a class: " + a.getClass());

        } catch (ClassNotFoundException ex){
              System.err.println("Agent class does not exist"+ ex.getMessage());
        } catch (Exception ccex){
              System.err.println("Agent launch exception:"+ccex);
              // ccex.printStackTrace();
        }
         if ((a != null) && (a instanceof AbstractAgent)){
              // System.out.println("Launching "+ type + " agent");
              if (label == null)
					label = AbstractMadkitBooter.getAgentLabelFromClassName(a.getClass().getName());
              if (gui == null)
                    booter.launchAgent((AbstractAgent) a,label,booter.isGraphics,position,dim);
              else
                    booter.launchAgent((AbstractAgent) a,label,gui.booleanValue(),position,dim);
         }
    }
}
