/*
 * Created on 13 juin 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package madkit.explorer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Vector;


/**
 * @author B_ROUIFED
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ExplorerDirectoryFile {
	
	private Explorer explorer;
	private File oldFile;
	private File newFile;
	private File fileTocopy;
	private File fileCopied;
	private String arg1;
	private String arg2;
	private String oldPath;
	private String newPath;
	private boolean result;
	private File [] files;
	

	public ExplorerDirectoryFile (Explorer _explorer, File _oldFile, File _newFile)
	{
		
		this.explorer = _explorer;
		this.oldFile = _oldFile;
		this.newFile = _newFile;
		exec();
	}
	
	public  void exec()
	{
// File exists
		if (newFile.equals(oldFile))
			return;
		if (newFile.exists())
		{
			arg1 = "This File " + newFile + " already exists ";
			arg2 = "COPY FILE " + newFile; 
			ExplorerOptionPane.showMessage(arg1,arg2);
			
		}else
		{
		// file is file?
			if (oldFile.isFile())
			{
				result = transferFile(oldFile,newFile);
				if (result)
				{
					displayFile(newFile);	
				}else
				{
				}
			}else   // file is a directory
			{
			// first create Directorie
				result = createDirectory(newFile);
				if (result)
				{
					displayFile(newFile);	
					transferDirectory();
				}
				else
					System.out.println("result DIRECTORIE " + result );
			}
		}
		
	}
	
	
	public  boolean createDirectory(File _file)
	{
		// Copy the directory itself
		boolean creat = false;
		File file = _file;
		if(!file.exists())
		{
			creat = file.mkdir();
			System.out.println("directory created = " + file.toString());
		}
		
		return creat;
		
	}
	

	public void transferDirectory()
	{
			
		//second create contents (directories and files
		oldPath = (String)oldFile.toString();
		newPath = (String)newFile.toString();
		ExplorerFindFileModel model  = new ExplorerFindFileModel();
		Vector vPathFile = new Vector();
		vPathFile = model.findDirectories(oldPath,"");
		String nameToCopy, nameCopied;
		int s,j,t;
		boolean boolCreated;
		while(vPathFile.size() > 0)
		{
			for(int i=0; i< vPathFile.size(); i++)
			{

				s = oldPath.length();

				nameToCopy  = (String) (vPathFile.elementAt(i));
				t = nameToCopy.length();
			

				nameCopied = newPath + nameToCopy.substring(s,t);

//				System.out.println(",nameTocopy = " +nameToCopy + " namcopied " + nameCopied);
			
				fileTocopy  = new File (nameToCopy);
				fileCopied = new File (nameCopied);
				if(fileTocopy.isDirectory())
					boolCreated = createDirectory(fileCopied);
				else
				{
					boolCreated = transferFile(fileTocopy,fileCopied);
				}
				if  (boolCreated)
					vPathFile.removeElementAt(i);
			}
		}
			
		
	}

	public boolean transferFile(File _inputFile, File _outputFile)
	{
		result = false;
		File inputFile = _inputFile;
		File outPutFile = _outputFile;
		FileInputStream sourceFileStream=null;
		FileOutputStream destinationFileStream=null;
		if(!outPutFile.exists()){
			try
			{
				outPutFile.createNewFile();
				sourceFileStream      = new FileInputStream(inputFile);
				destinationFileStream = new FileOutputStream(outPutFile);

			/* Lecture par segment de 0.5Mo */
				byte buffer[]=new byte[512*1024];
				int nbLecture = 0;

				while( (nbLecture = sourceFileStream.read(buffer)) != -1 )
				{
				
					destinationFileStream.write(buffer, 0, nbLecture);
				} 

			/* Copie réussie */
			
				result = true;
				}catch( FileNotFoundException f )
					{System.out.println ("Can't find this file");}
				catch( IOException e ){System.out.println(e);} finally{
				/* Quoi qu'il arrive, on ferme les flux */
				try{
					sourceFileStream.close();
				} catch(Exception e) {System.out.println("in close " + e); }
				try{
					destinationFileStream.close();
				} catch(Exception e) {System.out.println("out close " + e); }
				}
			}
			return result;

	}
	

	public void displayFile(File _f)
	{
		File fileToDisplay = _f;
		explorer.icons.addItem(fileToDisplay);
		if (result)
		{
			System.out.println("File Pasted = " + fileToDisplay.toString());
			if (ExplorerActions.actionBefore.equals ("cut"))
			{
				boolean delet = oldFile.delete();
				if(delet)
				{		
					System.out.println ("Source " + oldFile.toString() + " deleted");
				}
			}
				
		}


	}

}
