/*
* SchemeModule.java - SEdit, a tool to design and animate graphs in MadKit
* Copyright (C) 1998-2002 Jacques Ferber, Olivier Gutknecht
*
* This program is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License
* as published by the Free Software Foundation; either version 2
* of the License, or any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
*/
package SEdit.Scheme;

import gnu.expr.Interpreter;
import gnu.lists.FString;
import gnu.lists.LList;
import gnu.lists.Pair;
import gnu.mapping.CharArrayInPort;
import gnu.mapping.Environment;
import gnu.mapping.InPort;
import gnu.mapping.OutPort;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Hashtable;

import kawa.Shell;
import kawa.lang.AutoloadProcedure;
import kawa.standard.Scheme;
import SEdit.Structure;


public class SchemeModule // implements ActionListener
{

    public String madkitDirectory;

    protected Environment environment;
    public static Scheme interpreter = null;
    public static int envcount;

    Structure structure;
    Structure getStructure(){return(structure);}
    void setStructure(Structure str){structure = str;}


	// OutPort out_p, err_p;

	/*
    public void newParseEnv() {
	//Environment env = Environment.getCurrent();
	environment.define(parseEnvSymb,new Hashtable());
    }

    public Hashtable getParseEnv() {
	//Environment env = Environment.getCurrent();
	return (Hashtable) environment.lookup(parseEnvSymb).get();
    }

    public void setCurrentModel(Structure struc) {
	//Environment.getCurrent().
	environment.define(currentModelSymb,struc);
    }

    public Formalism getCurrentFormalism() {
	//Environment env = Environment.getCurrent();
		Object r = environment.lookup(currentFormalismSymb).get();
		if (r instanceof Formalism)
			return((Formalism) r);
		else
			return(null);
    } */

  /*  public void setCurrentAgent(AbstractSEditAgent ag) {
		// System.out.println("setCurrentAgent "+ag+" "+this);

		environment.define(currentAgentSymb,ag);
    } */

    // JF Ajouts
    public void setAsCurrentEnv(){
    	Environment.setCurrent(environment);
    }

	public Object readExpr(InPort in) throws java.io.IOException, gnu.text.SyntaxException {
		return(interpreter.read(in));
	}

	public Object eval(Object sexpr){
		return(interpreter.eval(sexpr, environment));
	}

/*	public void evalString(String s, boolean loadingMode){
		boolean b= isInsertMode();

		try {
     		// System.out.println(":: evaluation de : " + s);
     		if (loadingMode)
     			setInsertMode(false);
     		else
     			setInsertMode(true);
     		setLoadingMode(loadingMode);
     		newParseEnv();
     		runOutString(s,interpreter,environment,OutPort.outDefault());
     		setInsertMode(b);
		}
		catch(IllegalArgumentException e) {
     		setInsertMode(b);
			System.err.println("ERROR: in " + this +" : expression not understood : " + s);
		}
	} */

	public void evalString(String s){
		try {
  			Object e = STools.readFromString(s);

 			System.out.println(":: test de : " + e + " : "+ STools.prinToString(s));
  			Object r = interpreter.eval(e,environment);
  			System.out.println(">> " + r + " : " + STools.prinToString(r));
  		} catch(java.io.IOException ex){}
  		catch(gnu.text.SyntaxException ex){
  			System.out.println("Erreur de syntaxe: " + ex);
  		}
  	}

	public static void runOutString (String str, Scheme interp, Environment env, OutPort out) {
    	if (out == null)
    		out = OutPort.outDefault();
    	Shell.run (interp, env, new CharArrayInPort(str), out, OutPort.errDefault());
    }

   Object evalit1(Object expr, Hashtable env) {
 	if (expr instanceof Pair) {
	  	return(new Pair(evalit1(((Pair) expr).car, env),
	  						evalit1(((Pair) expr).cdr, env)));

 	}else {
	  		// si c'est une variable
	  	if ((expr instanceof String) && (((String)expr).charAt(0) == '?')) {
	  		if (env.containsKey((String) expr))
	  				return(new Pair(Interpreter.quote_sym,new Pair(env.get(expr),LList.Empty)));
	 	}
	}
  	return(expr);
 }

 Object evalit(Object expr, Hashtable env) {
 	Object newExpr = evalit1(expr,env);
 	Object r=null;

 	System.out.println(":: evaluation de : " + newExpr + " : "+ STools.prinToString(newExpr));

	r = interpreter.eval(newExpr, environment);
	return(r);
 }


    /* Define a procedure to be autoloaded.
       Taken from kawa.standard.scheme */
    void define_proc (String name, String className)
    {
		interpreter.define (name, new AutoloadProcedure (name, className));
    }



    void checkInitFile ()
    {
    // Set homeDirectory;  if first time called, run ~/.kawarc.scm.
	if (madkitDirectory == null) {
		String initfileName = "scheme/init.scm";
	    Object scmHomeDirectory;
	    madkitDirectory = System.getProperty ("madkit.dir");
	    if (madkitDirectory != null)
			scmHomeDirectory = new FString (madkitDirectory);
	    else
			scmHomeDirectory = Scheme.falseObject;
	    environment.define("user-directory", scmHomeDirectory);

	    File initFile = new File(initfileName);
	    if (initFile.exists()) {
	    	System.err.println(">> chargement de " + initFile.getPath());
	     try
		 {
		     // InPort fstream = InPort.openFile(initFile.getPath());
		     InPort fstream = InPort.openFile(initFile.getPath());
		     kawa.standard.load.loadSource(fstream, environment);
		     fstream.close();
		 }
	  /*   catch (FileNotFoundException e)
		 {
		     System.err.println("Cannot open file");
		 } */
	     catch (Exception e)
		 {
		     e.printStackTrace(System.err);
		 }
         catch (Throwable e)
		 {
		     e.printStackTrace(System.err);
		 }
	    }
    }
  }

    public Environment getEnvironment()
    {
	return interpreter.getEnvironment();
    }



  public SchemeModule(Structure str)
  {

    setStructure(str);
    if (Interpreter.defaultInterpreter == null)
	  {
	      Interpreter.defaultInterpreter = new Scheme();
	      Environment.setCurrent(Interpreter.defaultInterpreter.getEnvironment());
	  }
      if (interpreter==null)
	  interpreter = new Scheme();

      envcount++;
      environment = interpreter.getNewEnvironment();
      System.err.println("initialisation de Scheme :" + envcount + " " + environment);

  }

  public void init(){

      Object x = interpreter.eval("(define %scheme-agent-count% "+envcount+")",environment);
      environment.define("%current-agent%", getStructure().getAgent());
      environment.define("self", getStructure().getAgent());


	  // out_p = new OutPort(System.out,"<msg_stdout>");
	  // err_p = new OutPort(System.err,"<msg_stderr>");

      StringBuffer libsource = new StringBuffer();
      String rsrc="SEditSchemeAgentLib.scm";
      try
	  {
	      InputStream defs = madkit.kernel.Utils.loadClass("SEdit.Scheme.SchemeModule").getResourceAsStream(rsrc);
	      BufferedReader dip = new BufferedReader (new InputStreamReader(defs));
	      String s=null;

	      while((s=dip.readLine()) != null)
		  {
		      libsource.append('\n');
		      libsource.append(s);
		  }
	  }
      catch (Exception eofe)
	  {
	      System.err.println("Cannot load :"+ rsrc);
	      eofe.printStackTrace();
	  }

      interpreter.eval(libsource.toString(),environment);
  }

 /* 	void testModule(String s){
  		try {
  			Object e = STools.readFromString(s);

 			System.out.println(":: test de : " + e + " : "+ STools.prinToString(s));
  			Object r = interpreter.eval(e,environment);
  			System.out.println(">> " + r + " : " + STools.prinToString(r));
  		} catch(java.io.IOException ex){}
  		catch(gnu.text.SyntaxException ex){System.out.println("Erreur de syntaxe: " + ex);}
  	}

    String fileDir;
    String fileName;

    boolean getFileDialog(String title)
    {
	JFileChooser fd = new JFileChooser(System.getProperty("user.dir",null));
	fd.setDialogTitle(title);
	int retval = fd.showOpenDialog(null);
	if (retval != -1)	{
	    fileDir = fd.getSelectedFile().getParent()+File.separator;
	    fileName = fd.getSelectedFile().getName();
	    return(true);
	} else  return(false);
    }

    // Ol: FIXME remove SEditApp dependancies
    void loadFile() {
	//	SEditApp.setInstallFileName(false);
	if (getFileDialog("Evaluer fichier")) {
	    if (fileName != null){
	    	try {
				kawa.standard.load.loadSource(fileDir+fileName,environment);
			} catch(Exception e){
				System.out.println("Cannot load file - Scheme error..");
			}

		//if (SEditApp.getInstallFileName())
		//  SEditApp.getCurrentStructureFrame().installFileName(fileDir, fileName);
	    }
	}
    }


    void editFile() {
	Object result;
    // Ol: FIXME remove SEditApp dependancies
	//SEditApp.setInstallFileName(false);
	if (getFileDialog("Editer fichier")) {
	    if (fileName != null){
		try{
		    String text = new String();
		    String str = new String();
		    FileInputStream fs = new FileInputStream(fileDir+fileName);
		    BufferedReader ds = new BufferedReader(new InputStreamReader(fs));

		    while (str != null) {
			str = ds.readLine();

			if (str != null)
			    text = text + str + "\n";
		    }
		    //			inputArea.setText(text); // supprime l'ancien texte
		}
		catch (Exception err) {
		    System.out.println("Cannot open file " + fileName);
    		}
	    }
	}
    } */

}








