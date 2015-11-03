/*
 * Created on 27 oct. 2003
 * 
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package madkit.utils.common;

import java.io.*;

import java.util.Properties;

/**
 * @author Jaco
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class PropertyFile extends Properties {

	/**
	 *  
	 */
	public PropertyFile() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void saveTo(File f) {
		try {
			FileOutputStream fich = new FileOutputStream(f);
			this.store(fich, "");
		} catch (IOException ex) {
			System.err.println("Error in saving property file: " + f);
		}
	}

	public void loadFrom(File f) {
		try {
			FileInputStream fich = new FileInputStream(f);
			this.load(fich);
		} catch (IOException ex) {
			System.err.println("Error in saving property file: " + f);
		}
	}

}
