


package madkit.kernel;

class JavaAgentLauncher extends AgentLauncher {


    public void launch(){
        AbstractAgent a=null;
        try {
            Class cl;

            cl = Utils.loadClass(className);
            a= (AbstractAgent) cl.newInstance();

            if ((a != null) && (a instanceof AbstractAgent)){
              if (label == null)
                label = a.getClass().getName();
                if (gui == null)
                    booter.launchAgent((AbstractAgent) a,label,booter.isGraphics,position,dim);
                else
                    booter.launchAgent((AbstractAgent) a,label,gui.booleanValue(),position,dim);
            }
        } catch (ClassNotFoundException ex){
              System.err.println("Agent class does not exist"+ ex.getMessage());
        } catch (Exception ccex){
              System.err.println("Agent launch exception:"+ccex);
              // ccex.printStackTrace();
        }
    }
}