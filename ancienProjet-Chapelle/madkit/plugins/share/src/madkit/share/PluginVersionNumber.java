package madkit.share;

public class PluginVersionNumber implements java.io.Serializable {
    
    String versionNumber;

    //A compléter ou modifier si numéro de version n'est plus du type float

    public PluginVersionNumber(String version){
	versionNumber=version;
    }

    public float parse(){
	return Float.parseFloat(versionNumber);
    }

    public boolean isGreater(PluginVersionNumber versionBis){
	if(this.parse()>versionBis.parse())
	    return true;
	else return false;
    }

    public boolean isSmaller(PluginVersionNumber versionBis){
	if(this.parse()<versionBis.parse())
	    return true;
	else return false;
    }

    public String toString() {return versionNumber;}

}
    
