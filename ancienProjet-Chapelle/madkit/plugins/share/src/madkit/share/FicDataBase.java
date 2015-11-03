package madkit.share;
import java.io.File;
import java.util.Vector;



//========================================================================================================//
//      Classe permettant de faire correspondre un path reel et un path virtuel
//========================================================================================================//

public class FicDataBase{
    Vector dataBase;

    public FicDataBase(){
	dataBase = new Vector();
    }


    public Vector getVector() {return dataBase;}

/*==================================================================================================*/    

    public void addDir(String path){
	File fic = new File(path);
	int cut = path.lastIndexOf(File.separator);
	path = path.replace('\\','/');
	AssocPath a = new AssocPath(path,path.substring(cut+1),fic.getName());
	dataBase.add(a);

	File[] liste = fic.listFiles();
	File ficn;

	for(int i=0; i<liste.length; i++){
	    ficn=liste[i];
	    if(ficn.isDirectory()){
		addDirAux(ficn.getAbsolutePath(),cut);
	    }
	    else{
		path = ficn.getAbsolutePath();
		path = path.replace('\\','/');
		a = new AssocPath(path,path.substring(cut+1),ficn.getName(),(float)(ficn.length()));
		dataBase.add(a);
	    }
	}
    }

    public void addDirAux(String path,int cut){	
	File fic = new File(path);
	path = path.replace('\\','/');
	AssocPath a = new AssocPath(path,path.substring(cut+1),fic.getName());
	dataBase.add(a);
	
	File[] liste = fic.listFiles();
	File ficn;
	if (liste != null) {
	    for(int i=0; i<liste.length; i++){
		
		ficn=liste[i];
		if(ficn.isDirectory()){
		    addDirAux(ficn.getAbsolutePath(),cut);
		}
		else{
		    path = ficn.getAbsolutePath();
		    path = path.replace( '\\','/');
		    a = new AssocPath(path,path.substring(cut+1),ficn.getName(),(float)(ficn.length()));
		    dataBase.add(a);
		}
	    }
	}
    }

/*============================================================================================*/
    public String searchRealPath(String virtualPath){
	for(int i=0;i<dataBase.size();i++){
	    if( (((AssocPath)dataBase.elementAt(i)).getVirtualPath()).equals(virtualPath) ){
		return ((AssocPath)dataBase.elementAt(i)).getRealPath();
	    }
	}
	return "error";
    }
    
    public Vector searchFile(String file){
	Vector v = new Vector();
	for(int i=0;i<dataBase.size();i++){
	    if( (((AssocPath)dataBase.elementAt(i)).getVirtualPath()).equalsIgnoreCase(file)
		|| (((AssocPath)dataBase.elementAt(i)).getNameFile()).equalsIgnoreCase(file) 
		|| ( (((AssocPath)dataBase.elementAt(i)).getNameFile()).indexOf(file) != -1)
		)
		v.add((AssocPath)dataBase.elementAt(i));
	}
	return v;
    }
    
    public void display(){
		for(int i=0;i<dataBase.size();i++){
		    System.out.println(i+": "+
				       ((AssocPath)dataBase.elementAt(i)).getNameFile()+
				       " , "+
				       ((AssocPath)dataBase.elementAt(i)).getRealPath()+
				       " ----> "+
				       ((AssocPath)dataBase.elementAt(i)).getVirtualPath());
		}
    }
    
    public String displayToString(){
    	String res="";		
    	for(int i=0;i<dataBase.size();i++){
		    res = res+i+": "+
				   ((AssocPath)dataBase.elementAt(i)).getNameFile()+
				   ", "+
				   ((AssocPath)dataBase.elementAt(i)).getRealPath()+
				   " ----> "+
				   ((AssocPath)dataBase.elementAt(i)).getVirtualPath();
			res = res + "/n";
		}
		return res;
    }


}
