package madkit.share;


public class FileInfo implements java.io.Serializable
{
	String path;
	int sizeFic;	

	public FileInfo(String p ,int s)
	{
		path = p;
		sizeFic = s;
	}


	public String getPath() {return path;}
	public int getSizeFic() {return sizeFic;}

}
