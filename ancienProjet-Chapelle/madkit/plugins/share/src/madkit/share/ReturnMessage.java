package madkit.share;
import madkit.kernel.Message;



public class ReturnMessage extends Message
{
	byte[] b;
	String virtualPath;
	int offsetStart;
	int offsetEnd;
	long lastModified;               //last modified (date)

	public ReturnMessage(byte[] t, String v, int s,int e,long l)
	{
		b = t;
		virtualPath = v;
		offsetStart=s;
		offsetEnd=e;
		lastModified=l;
	}

	public byte[] getByte(){return b;}
	public String getPath() {return virtualPath;}
	public int getOffsetStart(){return offsetStart;}
	public int getOffsetEnd(){return offsetEnd;}
	public long getLastModified() {return lastModified;}

}
