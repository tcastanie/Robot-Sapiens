package madkit.TreeTools;

import java.util.*;
import javax.swing.*;
import java.net.URL;
import java.io.*;

class FileDescriptor {

    String imageAddress;
    ImageIcon icon;

    FileDescriptor(String im){
        imageAddress = im;
    }
    
    ImageIcon getImage(){
        if (icon == null){
            URL url = this.getClass().getResource(imageAddress); 
            if (url != null){
                icon = new ImageIcon(url);
	    }
	}
        return icon;
    }
}

public class FileIconDescriptor extends GenericIconDescriptor {

    Map fileDescriptors;


    public FileIconDescriptor(String defaultImage, String[][] lst, String[][] coms){
		super(defaultImage);
		for(int i=0;i<lst.length;i++){
			addFileDescriptor(lst[i][0],lst[i][1]);
		}
		for(int i=0;i<coms.length;i++){
			addCommand(coms[i][0],coms[i][1]);
		}
	}

    public FileIconDescriptor(String[][] lst, String[][] coms){
        this();
        for(int i=0;i<lst.length;i++){
            addFileDescriptor(lst[i][0],lst[i][1]);
        }
        for(int i=0;i<coms.length;i++){
            addCommand(coms[i][0],coms[i][1]);
        }
    }

    public FileIconDescriptor(){
        //super("/images/window/File.gif");
        super("/images/mime_ascii.png");
  /*      addFileDescriptor("pdf","/images/window/PdfFile.gif");
        addFileDescriptor("txt","/images/kde/txt.png");
        addFileDescriptor("htm","/images/kde/html.png");
        addFileDescriptor("html","/images/kde/html.png");
        addFileDescriptor("xml","/images/window/XMLFile.gif");
        addFileDescriptor("xsl","/images/window/XSLFile.gif");
        addFileDescriptor("jpg","/images/kde/jpg.png");
        addFileDescriptor("jpeg","/images/kde/jpg.png");
        addFileDescriptor("gif","/images/kde/gif.png");
		addFileDescriptor("png","/images/kde/png.png");
        addFileDescriptor("ps","/images/window/PsFile.gif");
        addFileDescriptor("ppt","/images/window/PPTFile.gif");
        addFileDescriptor("zip","/images/kde/tgz.png");
        addFileDescriptor("tar","/images/kde/tar.png");
        addFileDescriptor("gz","/images/kde/tgz.png");
        addFileDescriptor("ini","/images/kde/binary.png");
        addFileDescriptor("bmp","/images/kde/image.png");
        addFileDescriptor("dll","/images/kde/binary.png");
        addFileDescriptor("bat","/images/window/BatFile.gif");
        addFileDescriptor("com","/images/window/BatFile.gif");
        addFileDescriptor("wav","/images/kde/sound_wav.png");
        addFileDescriptor("mp2","/images/window/MpegFile.gif");
        addFileDescriptor("mpg","/images/window/MpegFile.gif");
        addFileDescriptor("mpeg","/images/window/MpegFile.gif");
        addFileDescriptor("mp3","/images/kde/sound_mp3.png");
        addCommand("open","execute"); */

		// addFileDescriptor("doc","/images/kde/document.png");
		addFileDescriptor("xsl","/images/window/XSLFile.gif");
		addFileDescriptor("avi","/images/avi.png");
		addFileDescriptor("ppt","/images/window/PPTFile.gif");
			addFileDescriptor("bat","/images/fileicons/BatFile.gif");
			addFileDescriptor("bmp","/images/kde/image.png");
			addFileDescriptor("cda","/images/cdtrack.png");
			addFileDescriptor("com","/images/exe.png");
			addFileDescriptor("dll","/images/binary.png");
			addFileDescriptor("doc","/images/doc.png");
			addFileDescriptor("exe","/images/exe.png");
			addFileDescriptor("gif","/images/gif.png");
			addFileDescriptor("gz","/images/tgz.png");
			addFileDescriptor("htm","/images/html.png");
			addFileDescriptor("html","/images/html.png");
			addFileDescriptor("ini","/images/binary.png");
			addFileDescriptor("jpg","/images/jpg.png");
			addFileDescriptor("jpeg","/images/jpg.png");
			addFileDescriptor("mp2","/images/mpg.png");
			addFileDescriptor("mp3","/images/mp3.png");
			addFileDescriptor("mpeg","/images/mpg.png");
			addFileDescriptor("mpg","/images/mpg.png");
			addFileDescriptor("pdf","/images/pdf.png");
			addFileDescriptor("png","/images/image.png");
			addFileDescriptor("tar","/images/tar.png");
			addFileDescriptor("txt","/images/text.png");
			addFileDescriptor("java","/images/source_java.png");
			addFileDescriptor("wav","/images/mp3.png");
			addFileDescriptor("jar","/images/tar.png");
			addFileDescriptor("xml","/images/fileicons/XMLFile.gif");
			// addFileDescriptor("xsl","/images/fileicons/XSLFile.gif");
			addFileDescriptor("zip","/images/fileicons/ZipFile.gif");
			addCommand("open","execute");
			addCommand("rename file", "rename");
			addCommand("delete file", "delete");
    }


    public static String getPathExtens(String s){
        int k = s.lastIndexOf('.');
        if (k != -1){
            return s.substring(k+1).toLowerCase();
        }
        else {
            return null;
        }
    }

    public void addFileDescriptor(String ext, String im){
        if (fileDescriptors == null) fileDescriptors = new HashMap();
        fileDescriptors.put(ext,new FileDescriptor(im));
    }

/*	public ImageIcon getImage(){
		File file = ((FileIcon)getArg()).getFile();
		FileDescriptor desc =  (FileDescriptor) fileDescriptors.get(getPathExtens(file.getName()));
		if (desc != null) return desc.getImage();
		else return super.getImage();
	} */
	
   public ImageIcon getImage(File file){
        return getImage(file.getName());
    }
    
    public ImageIcon getImage(String fileName){
        FileDescriptor desc = (FileDescriptor) fileDescriptors.get(getPathExtens(fileName));
        if (desc != null) return desc.getImage();
        else return super.getImage();
    }
    

    public ImageIcon getImage(Entry entry){
        return getImage(entry.getName());
    }
}
