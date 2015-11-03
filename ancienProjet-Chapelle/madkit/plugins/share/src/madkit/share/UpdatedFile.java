package madkit.share;


public class UpdatedFile
{
	String path;
	String serverName;
	long lastModified;

	public UpdatedFile(String p,String s, long l)
	{
		path = p;
		serverName = s;
		lastModified = l;
	}


	public String getPath() {return path;}
	public String getServerName() {return serverName;}
	public long getLastModified() {return lastModified;}
}
