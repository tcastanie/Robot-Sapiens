package madkit.share;
import java.util.Vector;


public class ListFic implements java.io.Serializable
{
    Vector listFic;
	
    public ListFic(){listFic = new Vector();}
    
    public boolean isEmpty() {return listFic.isEmpty();}
	
    public Vector getVector () {return listFic;}

}
