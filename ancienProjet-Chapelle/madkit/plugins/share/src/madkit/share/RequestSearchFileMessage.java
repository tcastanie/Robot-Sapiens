package madkit.share;
import madkit.kernel.Message;

public class RequestSearchFileMessage extends Message{
    String file;
    
    public RequestSearchFileMessage(String f){
	file=f;
    }
    
    public String getFile(){return file;}
}
