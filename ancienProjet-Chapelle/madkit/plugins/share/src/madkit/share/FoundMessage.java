package madkit.share;
import madkit.kernel.Message;



public class FoundMessage extends Message
{
	String file;
	int sizeFic;

	public FoundMessage(String f,int s)
	{
		file=f;
		sizeFic=s;
	}

	public String getByte(){return file;}
	public int getPath() {return sizeFic;}

}
