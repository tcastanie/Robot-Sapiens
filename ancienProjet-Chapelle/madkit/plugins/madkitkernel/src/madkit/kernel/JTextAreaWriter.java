/*
* JTextAreaWriter.java - Graphics utilities for MadKit agents
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
package madkit.kernel;

import java.io.Writer;

import javax.swing.JTextArea;

/** A Writer that appends its output to a TextArea.
  * Based on code from Albert L. Ting" <alt@artisan.com>.
  J'ai déjà placé ce texte dans SEdit. Il faudra eviter
  de le dupliquer plusieurs fois...
  */

public class JTextAreaWriter extends Writer
{
  JTextArea area;
  String str="";

  public JTextAreaWriter (JTextArea area)
  {
    this.area = area;
  }

  JTextAreaWriter (){}

  public synchronized void clear()
  {
    area.setText("");
  }

  public synchronized void write(int x)
  {
    str = str + (char) x;
    if (x == '\n')
      flush();
  }

  public void write (String str)
  {
  	int end = area.getDocument().getLength();
      area.append(str);
      area.setCaretPosition(end+str.length());
  }

  public synchronized void write (char[] data, int off, int len)
  {
    flush();
    write(new String(data, off, len));
  }

  public synchronized void flush()
  {
    if (! str.equals(""))
      {
	write(str);
	str = "";
      }
  }

  public void close ()
  {
    flush();
  }
}

