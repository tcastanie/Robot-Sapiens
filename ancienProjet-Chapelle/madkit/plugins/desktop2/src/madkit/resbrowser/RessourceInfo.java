package madkit.resbrowser;

public class RessourceInfo{
	String jarName, resourceName;

	public RessourceInfo(){}
	
	public String getJarName(){ return (this.jarName); }
	public void setJarName(String jarName){ this.jarName = jarName; }

	public String getResourceName(){ return (this.resourceName); }
	public void setResourceName(String resourceName) { this.resourceName = resourceName; }
}