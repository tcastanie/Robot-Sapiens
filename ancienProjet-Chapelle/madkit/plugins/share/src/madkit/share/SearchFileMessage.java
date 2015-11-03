package madkit.share;
import madkit.kernel.Message;


public class SearchFileMessage extends Message
{
    String file;
    String virtualPath;
    float sizeFile;
    boolean isDir;
    
    public SearchFileMessage(String f,String vp,float s,boolean id){
	file=f;
	virtualPath=vp;
	sizeFile=(float)s;
	isDir=id;
    } 
    
    public String getFile(){return file;}
    public String getVirtualPath() {return virtualPath;}
    public float getSize(){
	return sizeFile;
    }
    public boolean isDirectory() {return isDir;}
}
