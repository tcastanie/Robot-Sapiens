package madkit.share;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.Vector;

import org.apache.xerces.jaxp.DocumentBuilderFactoryImpl;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


public class FileMaj  {

	String majPath;
	
	public FileMaj(String mp)
	{
	    majPath = mp;
	    File f = new File(majPath);
	    try 
	    {
	    	if (!f.exists()){
	    		f.createNewFile();
	    		Document doc = DocumentBuilderFactoryImpl.newInstance().newDocumentBuilder().newDocument();
			Element racine = doc.createElement("update");
			doc.appendChild(racine);
			OutputFormat of = new OutputFormat(doc);
			FileWriter writer = new FileWriter(f);
			of.setIndenting(true);
			of.setLineSeparator(System.getProperties().get("line.separator").toString());
			XMLSerializer serial = new XMLSerializer(writer, of);
			serial.asDOMSerializer();
			serial.serialize(doc);
		}
		else{f.createNewFile();}
	    }
	    catch(Exception e){e.printStackTrace(System.err);}
	}

	
	public Vector load()
	{
		String path;
		String sName;
		String lastModified;
		
		Vector res = new Vector();
		try
		{
			File f = new File(majPath);
			if (f.exists()){
				FileInputStream from = new FileInputStream(f);
				Document doc = DocumentBuilderFactoryImpl.newInstance().newDocumentBuilder().parse(from);
				Element racine;
				NodeList nodeName;
	
				racine=doc.getDocumentElement();
				
				nodeName=racine.getElementsByTagName("updated-file");
				for(int i=0; i<nodeName.getLength(); i++){
					Element te = (Element) nodeName.item(i);
					path = te.getAttribute("file-name");
					sName = te.getAttribute("server-name");
					lastModified = te.getAttribute("last-modified");
					UpdatedFile uf = new UpdatedFile(path,sName,Long.parseLong(lastModified));
					res.addElement(uf);
				}
			}
		}
		catch(Exception e){ e.printStackTrace(System.err);}
		return res;
	}

	public void lastUpdate(Vector suf)
	{
		Vector stockUpdatedFile = new Vector();
		for(int i=0; i<suf.size(); i++){
			stockUpdatedFile.add((UpdatedFile)(suf.elementAt(i)));
		}
		raz_MajFile(suf);
		try
		{
			File f = new File(majPath);
			for(int i=0; i<stockUpdatedFile.size(); i++){
				String p = ((UpdatedFile)stockUpdatedFile.elementAt(i)).getPath();
				long lastModified = ((UpdatedFile)stockUpdatedFile.elementAt(i)).getLastModified();
				String serverName = ((UpdatedFile)stockUpdatedFile.elementAt(i)).getServerName();
				
				Document doc = DocumentBuilderFactoryImpl.newInstance().newDocumentBuilder().parse(f);
				Element racine;
			
				racine=doc.getDocumentElement();
				    
				Element elt = doc.createElement("updated-file");
				elt.setAttribute("file-name", p);
				elt.setAttribute("server-name", serverName);
				elt.setAttribute("last-modified", ""+lastModified);
				racine.appendChild(elt);
				    
				OutputFormat of = new OutputFormat(doc);
				FileWriter writer = new FileWriter(f);
				of.setIndenting(true);
				of.setLineSeparator(System.getProperties().get("line.separator").toString());
				XMLSerializer serial = new XMLSerializer(writer, of);
				serial.asDOMSerializer();
				serial.serialize(doc);
				
			}
		}
		catch(Exception e){e.printStackTrace(System.err);}
	}
	
	public void raz_MajFile(Vector suf)
	{
		Vector stockUpdatedFile = new Vector();
		for(int i=0; i<suf.size(); i++){
			stockUpdatedFile.add((UpdatedFile)(suf.elementAt(i)));
		}
		try
		{
			File f = new File(majPath);
			boolean b = f.delete();
			while(!b) {b = f.delete();}
	    		Document doc = DocumentBuilderFactoryImpl.newInstance().newDocumentBuilder().newDocument();
			Element racine = doc.createElement("update");
			doc.appendChild(racine);
			OutputFormat of = new OutputFormat(doc);
			FileWriter writer = new FileWriter(f);
			of.setIndenting(true);
			of.setLineSeparator(System.getProperties().get("line.separator").toString());
			XMLSerializer serial = new XMLSerializer(writer, of);
			serial.asDOMSerializer();
			serial.serialize(doc);
		}
		catch(Exception e){e.printStackTrace(System.err);}
	}	
	

	public boolean existFic(String virtual_Path,String serverName)
	{
		try
		{
			String path;
			String sName;
			File f = new File(majPath);
			if (f.exists()){
				FileInputStream from = new FileInputStream(f);
				Document doc = DocumentBuilderFactoryImpl.newInstance().newDocumentBuilder().parse(from);
				Element racine;
				NodeList nodeName;
	
				racine=doc.getDocumentElement();
				
				nodeName=racine.getElementsByTagName("updated-file");
				for(int i=0; i<nodeName.getLength(); i++){
					Element te = (Element) nodeName.item(i);
					path = te.getAttribute("file-name");
					sName = te.getAttribute("server-name");
					if( path.equals(virtual_Path) && sName.equals(serverName) )
					{
						return true;
					}
				}
			}
		}
		catch(Exception e){ e.printStackTrace(System.err); }
		return false;
	}		

}
