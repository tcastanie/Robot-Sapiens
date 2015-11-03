package madkit.TreeTools;

import javax.swing.*;
import java.io.*;
import java.awt.*;




public class FileNode extends AbstractFileNode{

    static FileIconDescriptor fileNodeDescriptor;

    { // class initialization ...
        fileNodeDescriptor =
            new FileIconDescriptor("/images/kde/document.png",
                new String[][]{ // new extensions, you just have to complete...
                    {"jar","/images/window/JarFile.gif"},
                    {"scm","/images/window/ScmFile.gif"},
                    {"ss","/images/window/ScmFile.gif"},
//                    {"exe","/images/window/BatFile.gif"},
					{"exe","/images/icons/exe.png"},
					{"doc","/images/kde/document.png"},
						{"pdf","/images/window/PdfFile.gif"},
						{"txt","/images/kde/txt.png"},
						{"htm","/images/kde/html.png"},
						{"html","/images/kde/html.png"},
						{"xml","/images/window/XMLFile.gif"},
						{"xsl","/images/window/XSLFile.gif"},
						{"jpg","/images/kde/jpg.png"},
						{"jpeg","/images/kde/jpg.png"},
						{"gif","/images/kde/gif.png"},
					{"png","/images/kde/png.png"},
						{"ps","/images/window/PsFile.gif"},
						{"ppt","/images/window/PPTFile.gif"},
						{"zip","/images/kde/tgz.png"},
						{"tar","/images/kde/tar.png"},
						{"gz","/images/kde/tgz.png"},
						{"ini","/images/kde/binary.png"},
						{"bmp","/images/kde/image.png"},
						{"dll","/images/kde/binary.png"},
						{"bat","/images/window/BatFile.gif"},
						{"com","/images/window/BatFile.gif"},
						{"wav","/images/kde/sound_wav.png"},
						{"mp2","/images/window/MpegFile.gif"},
						{"mpg","/images/window/MpegFile.gif"},
						{"mpeg","/images/window/MpegFile.gif"},
						{"mp3","/images/kde/sound_mp3.png"},
//					{"bat","/images/icons/exe.png"},
//					{"jpg","/images/icons/jpg.png"},
//					{"jpeg","/images/icons/jpg.png"},
//					{"exe","/images/icons/exe.png"},
//					{"txt","/images/icons/text.png"}
                },
                new String[][]{ // new commands
                    {"delete","delete"},
                    {"execute","execute"}
                }
            );
    }

    public static FileIconDescriptor getFileNodeDescriptor(){
        return fileNodeDescriptor;
    }


    public FileNode(File file){
        super(file);
        this.setDescriptor(fileNodeDescriptor);
    }

    public FileNode(Entry entry){
		super(entry);
		this.setDescriptor(fileNodeDescriptor);
    }

    public ImageIcon getLeafIcon(){
    	ImageIcon im=null;
		if (file != null) 
			im= fileNodeDescriptor.getImage(file);
		else 
			im= fileNodeDescriptor.getImage(entry);
		return im;
    }

    public void execute(){
        boolean windows = isWindowsPlatform();
        String cmd = null;
        try {
            //String url = "file://"+file.getAbsolutePath();
            String url=file.getAbsolutePath();
            if (windows) {
                  // cmd = 'rundll32 url.dll,FileProtocolHandler http://...'
                  cmd = WIN_PATH + " " + WIN_FLAG + " " + url;
                  //System.out.println("execute: " + cmd);
                  Process p = Runtime.getRuntime().exec(cmd);
            }
        } catch(IOException x) {
              // couldn't exec browser
              System.err.println("Could not invoke browser, command=" + cmd);
              System.err.println("Caught: " + x);
        }
      }

      static boolean isWindowsPlatform()
      {
          String os = System.getProperty("os.name");
          if ( os != null && os.startsWith(WIN_ID))
              return true;
          else
              return false;
      }
      // Used to identify the windows platform.
      private static final String WIN_ID = "Windows";
      // The default system browser under windows.
      private static final String WIN_PATH = "rundll32";
      // The flag to display a url.
      private static final String WIN_FLAG = "url.dll,FileProtocolHandler";
      // The default browser under unix.
      private static final String UNIX_PATH = "netscape";
      // The flag to display a url.
      private static final String UNIX_FLAG = "-remote openURL";
}
