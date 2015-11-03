package madkit.TreeTools;
import java.io.*;

//Classe générique permettant la reconstitution d'une arborescence.
public class Entry implements java.io.Serializable {
  
    protected String name;
    protected String path;
    protected boolean dir;
    protected long tmp;
    protected long size;
	
    public Entry(){
    }
	
    public Entry(String _path){
	path = _path;
	name = getNameFromPath(_path);
	dir=false;
    }

    public Entry(String _path,boolean _dir){
	path = _path;
	name = getNameFromPath(_path);
	dir = _dir;
    }

    public long getDate(){
	return tmp;
    }
	
    public void setDate(long _tmp){
	tmp = _tmp;
    }
	
    public boolean isDir(){	
	return dir;
    }
	
    public void setDir(boolean _dir){
	dir = _dir;
    }
	
    public String getName() {	
	return name;	
    }
    
    public void setName(String _name){	
	name = _name;	
    }
	
    public String getPath() {
	return path;
    }

    public void setPath(String _path){
	path = _path;
    }
    public long getSize() {
	return size;
    }

    public void setSize(long _size) {
	size = _size;
    }

     public static String getNameFromPath(String path){
        int k = path.lastIndexOf(File.separator);
        if (k != -1){
            return path.substring(k+1);
        }
        else return path;
    }
}
