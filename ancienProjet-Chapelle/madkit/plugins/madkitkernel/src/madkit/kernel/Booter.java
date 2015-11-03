// Copyright (C) 1999 by MadKit Team

package madkit.kernel;

/** A minimalist console booter for MadKit.

    It starts the kernel, and then launch agents defined in a config
    file, with or without GUI.

  @version 2.0 02/2002
  @author Olivier Gutknecht (initial version), J. Ferber (rev 3.0  5/02/2002) */
public class Booter extends AbstractMadkitBooter implements GraphicShell
{

  protected Booter(boolean isg, boolean ipnumeric, String initFile, String ipaddress, boolean network){
        super(isg,ipnumeric,initFile,ipaddress,network);
  }


     /** Booting from command line */
    static public void main(String argv[]){
        bootProcess(argv);
        if (initFile != null) {
              Booter boot = new Booter(graphics, ipnumeric, initFile, ipaddress, network);
        } else {
            System.out.println("Sorry config file not found... bye");
        }
    }


}