/*
* GUIReader.java - Graphics utilities for MadKit agents
* Copyright (C) 1998-2002  Olivier Gutknecht
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

import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import javax.swing.JTextField;

/**
 * A character stream whose source is a TextField or JTextField.
 */

public class GUIReader extends Reader implements ActionListener
{
  private Object tf;
  private String str = "";
  private int length;
  private int next = 0;
  private Writer out = null;  

    
    boolean duplicateOnOutput = true;
    
    /**
       * Get the value of duplicateOnOutput.
       * @return Value of duplicateOnOutput.
       */
    public boolean getDuplicateOnOutput() {return duplicateOnOutput;}
    
    /**
       * Set the value of duplicateOnOutput.
       * @param v  Value to assign to duplicateOnOutput.
       */
    public void setDuplicateOnOutput(boolean  v) {this.duplicateOnOutput = v;}
    
  
  /**
   * Create a new string reader.
   */
  public GUIReader(TextField s) 
  {
    tf=s;
    this.str = new String("");
    this.length = str.length();

    s.addActionListener(this);
    
  }
  /**
   * Create a new string reader.
   */
  public GUIReader(JTextField s) 
  {
    tf=s;
    this.str = new String("");
    this.length = str.length();

    s.addActionListener(this);    
  }

  /**
   * Create a new string reader.
   */
  public GUIReader(JTextField s, Writer w) 
  {
      this(s);
      out = w;
  }

  
  public void actionPerformed(ActionEvent e)
  {
    //    System.err.println("GUIReader actionPerformed:"+tf.getText()+'\n');
    if (tf instanceof TextField)
      {
	str = new String(((TextField)tf).getText()+'\n');
	((TextField)tf).setText("");
      }
    if (tf instanceof JTextField)
      {
	str = new String(((JTextField)tf).getText()+'\n');
	((JTextField)tf).setText("");
      }
    this.length = str.length();
    
    if (!str.equals(""))
	if (out!=null)
	    try 
		{
		    /*System.err.println("write");*/
		    if (duplicateOnOutput && out!=null)
			out.write(str); 
		}
	    catch (IOException ex) {
		System.err.println("GUIReader output exception: "+ex);
	    }
    next=0;
  }


    /** Check to make sure that the stream has not been closed */
    private void ensureOpen() throws IOException {
	if (str == null)
	    throw new IOException("Stream closed");
    }

    /**
     * Read a single character.
     *
     * @return     The character read, or -1 if the end of the stream has been
     *             reached
     *
     * @exception  IOException  If an I/O error occurs
     */
  public int read() throws IOException {
    synchronized (lock) {
      ensureOpen();
      /*      if (next >= length)
	return -1;*/


      //      System.err.println("GUIReader read():"+str+"/"+
      //			 str.length()+"/"+str.charAt(next+1));
      while (str.equals(""))
	{
	  //	  System.err.println("GUIReader read() wait:"+str);
	  
	  /* might be a writer waiting */
	  //notifyAll();
	  try {wait(500);}
	  catch (InterruptedException ex) {
	    throw new java.io.InterruptedIOException();
	  }
	}

      //      System.err.println("GUIReader read():"+str+"/"+str.charAt(next+1));
      return str.charAt(next++);
    }
  }

    /**
     * Read characters into a portion of an array.
     *
     * @param      cbuf  Destination buffer
     * @param      off   Offset at which to start writing characters
     * @param      len   Maximum number of characters to read
     *
     * @return     The number of characters read, or -1 if the end of the
     *             stream has been reached
     *
     * @exception  IOException  If an I/O error occurs
     */
  public int read(char cbuf[], int off, int len) throws IOException 
  {
    synchronized (lock) 
      {
	ensureOpen();
	//	System.err.println("GUIReader readcbuf():"+str+"/"+
	//	 str.length()+length+"/"+next);
	while (str.equals("") || (next>=length))
	  {
	    //  System.err.println("GUIReader readcbuf() wait:"+str);
	    try {wait(500);}
	    catch (InterruptedException ex) {
	      throw new java.io.InterruptedIOException();
	    }
	  }
	if (next >= length)
	  {
	    str = new String("");
	    length=0;
	    next=0;
	    
	    return -1;
	  }
	
	int n = Math.min(length - next, len);
	str.getChars(next, next + n, cbuf, off);
	next += n;
	//System.err.println("GUIReader readcbuf():"+str+"/"+n);
	return n;
      }
  }
  
    /**
     * Skip characters.
     *
     * @exception  IOException  If an I/O error occurs
     */
    public long skip(long ns) throws IOException {
	synchronized (lock) {
	    ensureOpen();
	    // System.err.println("GUIReader skip():"+str);
	    if (next >= length)
		return 0;
	    long n = Math.min(length - next, ns);
	    next += n;
	    return n;
	}
    }

    /**
     * Tell whether this stream is ready to be read.  String readers are
     * always ready to be read.
     */
    public boolean ready() {
      //   System.err.println("GUIReader ready():"+str);
	return ((!(str.equals(""))) && (next < length));
    }

    /**
     * Tell whether this stream supports the mark() operation, which it does.
     */
    public boolean markSupported() 
  {
    return false;
  }
  
    /**
     * Reset the stream to the most recent mark, or to the beginning of the
     * string if it has never been marked.
     *
     * @exception  IOException  If an I/O error occurs
     */
    public void reset() throws IOException {
	synchronized (lock) {
	    ensureOpen();
	    next = 0;
	}
    }

    /**
     * Close the stream.
     */
    public void close() 
  {
      str = null;
    }
}
