package madkit.share;
import madkit.kernel.Message;



/*
 * @author G.Hoarau , S.Lara, C.Menichetti, J.Pequignot
 * @version 1.0
 */



public class RequestTreeMessage extends Message {  
	
	String txt = null;
	
    public String getText() {
	
	return txt;
}


    public RequestTreeMessage(String t) {
    	txt = t;
	
}
}
