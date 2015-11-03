/*
* JTextAreaOutputStream.java - Graphics utilities for MadKit agents
* Copyright (C) 2000-2002  Jacques Ferber, Olivier Gutknecht
*
* This library is free software; you can redistribute it and/or
* modify it under the terms of the GNU Lesser General Public
* License as published by the Free Software Foundation; either
* version 2.1 of the License, or (at your option) any later version.

* This library is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
* Lesser General Public License for more details.

* You should have received a copy of the GNU Lesser General Public
* License along with this library; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
*/
package madkit.utils.graphics;

import java.io.OutputStream;

import javax.swing.JTextArea;


/** An outputstream that appends its output to a TextArea.
  * Based on code from Albert L. Ting" <alt@artisan.com>.
  */

public class JTextAreaOutputStream extends OutputStream {

    public JTextAreaOutputStream() {
    }

  JTextArea area;
  String str="";


  public JTextAreaOutputStream(JTextArea area){
    this.area = area;
  }


  public synchronized void clear()
  {
    area.setText("");
  }

  public void write(int x)
  {
    str = str + (char) x;
    if (x == '\n')
      flush();
  }



  public void write(String str)
  {	  int end = area.getDocument().getLength();
      area.append(str);
      area.setCaretPosition(end+str.length());
      flush();
  }



  public synchronized void write (byte[] data, int off, int len){
    flush();
    write(new String(data, off, len));
  }



  public synchronized void flush() {
    if (! str.equals("")) {
	    write(str);
	    str = "";
      }
  }


  public void close () {
    flush();
  }

}
