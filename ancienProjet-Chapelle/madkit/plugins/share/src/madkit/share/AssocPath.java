package madkit.share;


public class AssocPath
{
    String realPath;
    String virtualPath;
    String nameFile;
    float sizeFic;
    boolean isDir;
    

    public AssocPath(String r, String v,String n,float s)
    {
	realPath=r;
	virtualPath=v;
	nameFile=n;
	
	//arrondi à 10-2
	sizeFic=s;
	sizeFic *= 100;
	sizeFic = (int)(sizeFic+.5);
	sizeFic /= 100;
	
	isDir=false;
    }
	
    public AssocPath(String r, String v, String nf)
    {
	realPath=r;
	virtualPath=v;
	nameFile = nf;
	sizeFic = (float)0;
	isDir=true;
    }


    public String getRealPath() {return realPath;}
    public String getVirtualPath() {return virtualPath;}
    public String getNameFile() {return nameFile;}
    public boolean isDirectory() {return isDir;}
    public float getSizeFile() {return sizeFic;}
}
