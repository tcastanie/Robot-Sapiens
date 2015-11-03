package madkit.share;
import madkit.kernel.Message;



public class SearchMessage extends Message
{
    String file;
    int sizeFile;
    
    public SearchMessage(String f,int s)
    {
	file=f;
	sizeFile=s;
    }
    
    public SearchMessage(String f)
    {
	file=f;
	sizeFile=0;
    }
    
    public String getFile(){return file;}
    public int getSizeFile() {return sizeFile;}

}
