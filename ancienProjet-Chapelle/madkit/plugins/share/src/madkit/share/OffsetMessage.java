package madkit.share;
import madkit.kernel.Message;


public class OffsetMessage extends Message
{
	String namePaquet;
	int offsetStart;
	int offsetEnd;

	public OffsetMessage(String i,int s,int e)
	{
		namePaquet = i;
		offsetStart=s;
		offsetEnd=e;
	}

	public String getName(){return namePaquet;}
	public int getOffsetStart(){return offsetStart;}
	public int getOffsetEnd(){return offsetEnd;}

}
