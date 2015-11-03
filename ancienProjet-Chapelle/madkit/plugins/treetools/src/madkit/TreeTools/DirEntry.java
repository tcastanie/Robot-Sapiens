package madkit.TreeTools;

import java.util.*;
import java.io.*;

public class DirEntry extends Entry{

    protected Vector vect;

    public DirEntry() {
	super();
	setDir(true);
	setName("Shared Directory");
	vect = new Vector();
    }
		
    public DirEntry(String path) {
	super(path,true);
	vect = new Vector();
	initialise(path);
    }

    public void initialise(String path) {

 	File f;
	File file = new File(path);
	File[] liste = file.listFiles();
	if (liste != null){
	    int i = 0;
	    int j = 0;
	    while (i<liste.length) { 	
		f = liste[i];
		if (f.isDirectory()) {	
		    setDir(true);
		    vect.addElement(new DirEntry(path+File.separator+f.getName()));
		    ((Entry)vect.elementAt(j)).setDate(f.lastModified());
		    j++; 
		}
		else if (f.canRead()) {
		    vect.addElement(new FilEntry(path+File.separator+f.getName()));
		    ((Entry)vect.elementAt(j)).setDate(f.lastModified());
		    j++; 
		}
		i++;
	    }
	}
    }
    
    public Vector getVect() {	
	return vect;	
    }

    public void setVect(Vector _vect) {	
	vect = _vect;	
    }

    public void addDirEntry(DirEntry dir) {
	vect.addElement(dir);
    }

    public void remDirEntry(int index) {
	vect.removeElementAt(index);
    }
    
    public Object getEntry(int index) {
	return vect.elementAt(index);
    }

    public int getVectSize(){
	return vect.size();
    }   

    public long isThereFile(String path,String s) {
	long res = 0;
	for(int i=0;i<vect.size();i++){
	    if( ((Entry)vect.elementAt(i)).isDir() )
		res = ((DirEntry)vect.elementAt(i)).isThereFile(path,s+((Entry)vect.elementAt(i)).getName()+File.separator);
	    else {
		if(path.equals(s+((Entry)vect.elementAt(i)).getName())){
		    return ((Entry)vect.elementAt(i)).getDate();
		}
	    }
	}
	return res;
    }
	
    public void display(String t) {
	System.out.println(t+"*"+getName());
	t = t+"\t";
	for(int i=0;i< vect.size();i++){
	    if( ((Entry)vect.elementAt(i)).isDir() ){
		((DirEntry)vect.elementAt(i)).display(t);
	    }
	    else {
		System.out.println( t+((Entry)vect.elementAt(i)).getName() );
	    }
	}
    }
}

	
	
	
	
	
	
	
	
	
	
	
		
					
				
	
