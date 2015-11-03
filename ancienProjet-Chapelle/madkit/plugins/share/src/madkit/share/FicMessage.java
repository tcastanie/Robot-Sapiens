package madkit.share;

import madkit.kernel.Message;


/* SenderAgent: An agent which is able to share data 
 * with other agents, 
 * @author G.Hoarau , S.Lara, C.Menichetti, J.Pequignot
 * @version 1.0
 */
 
public class FicMessage extends Message {

    String nom ;
    boolean updatePlugin;

    String getPath(){return nom;}
    boolean getUpdatePlugin() {return updatePlugin;} 

    public FicMessage(String t) 
    {
    	nom = t;
	updatePlugin=false;
    }

    public FicMessage(String t,boolean u) 
    {
    	nom = t;
	updatePlugin=u;
    }
    
}// fin classe
