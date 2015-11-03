/*
* STools.java - SEdit, a tool to design and animate graphs in MadKit
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
import gnu.kawa.lispexpr.ScmRead;
import gnu.lists.LList;
import gnu.lists.Pair;
import gnu.mapping.CharArrayInPort;
import gnu.mapping.Environment;
import gnu.mapping.OutPort;
import gnu.mapping.SFormat;
import gnu.mapping.UnboundSymbol;
import gnu.mapping.Values;
import gnu.mapping.WrongArguments;
import gnu.mapping.WrongType;

import java.io.StringWriter;
import java.util.Hashtable;

import kawa.lang.Eval;
import kawa.lang.GenericError;
import kawa.standard.Scheme;


/** une classe qui contient un ensemble de procedures
effectuant l'interface entre SEdit et Kawa */
public class STools {


    // Ol: FIXME: gueuh ? C'est pas complètement redondant avec (vector->list ...) ?
  /** transforme un vecteur java.util.Vector en liste Scheme */
  public static LList javaVectorToList (java.util.Vector v)
  {
  	 int len = v.size();

     LList result = LList.Empty;
     for (int t=len-1; t>=0; t--) {
        result = new Pair(v.elementAt(t),result);
     }
     return result;
  }

  public static Object readFromString(String str)
	throws java.io.IOException, gnu.text.SyntaxException {
		CharArrayInPort inp = new CharArrayInPort(str);
		return(ScmRead.readObject(inp));
  }

  /** print an expression to a String in a human-readable way
  (i.e. without quotes around strings)
  */
  public static String prinToString(Object o) {
  	StringWriter sw = new StringWriter();
  	OutPort outp = new OutPort(sw);
  	prin(o,outp);
  	return(sw.toString());
  }

  /** print an expression to a String in a machine-readable way
  (i.e. with quotes around strings)
  */
  public static String writeToString(Object o) {
  	StringWriter sw = new StringWriter();
  	// OutPort outp = new OutPort(sw);
  	OutPort outp = new OutPort(sw, true, true);
  	// outp.writeSchemeObject(o, true);
  	return(sw.toString());
  }


  // identique à la methode 'print' dans la classe Scheme de Kawa..
  public static void print (Object value, OutPort out)
  {
    if (value == Scheme.voidObject) {
	  out.println("void");out.flush();
      return;
    }
    if (value instanceof Values) {
		Object[] values = ((Values) value).getValues();
		for (int i = 0;  i < values.length;  i++) {
	    	SFormat.print (values[i], out);
	    	out.println();
	  	}
    } else {
		SFormat.print (value, out);
		out.println();
	}
    out.flush();
  }

    public static void prin (Object value, OutPort out)
  {
    if (value == Scheme.voidObject) {
	  out.print("void");out.flush();
      return;
    }
    if (value instanceof Values) {
		Object[] values = ((Values) value).getValues();
		for (int i = 0;  i < values.length;  i++) {
	    	SFormat.print (values[i], out);
	 //   	out.println();
	  	}
    } else {
		SFormat.print(value, out);
		// out.println();
	}
    out.flush();
  }

  /** transforme une liste en un vecteur java */
  public static java.util.Vector listToJavaVector (LList lst)
  {
    int len = lst.size();
    java.util.Vector v = new java.util.Vector();

    Object list = lst;
    for (int i=0; i < len; i++) {
		Pair pair = (Pair) list;

		v.addElement(pair.car);
		list = pair.cdr;
    }
    return v;
  }

  /** la fonction de filtrage d'appel */
  public static Hashtable match(Object pat, Object fact) {
  	return(match(pat,fact,new Hashtable()));
  }

  // pour simplement faire les vérifications nécessaires...
  // et traiter le cas des filtres qui ne sont pas des listes...
  public static Hashtable match(Object pat, Object fact, Hashtable subst) {
  	if (!(pat instanceof LList)){
  		if (pat instanceof String) { // si c'est un symbole
			if (((String)pat).charAt(0) == '?') { // si c'est une variable
				subst = varSubst((String) pat, fact,subst);
				return(subst);
			} else { // on teste les symboles
				if (!(fact instanceof String)) return(null);
				if (pat.equals(fact)) // MODIF JF 19.11.00
					return(subst);
					return(null);
			}
		}
  		return(null);
  	} else if (!(pat instanceof LList) || !(pat instanceof LList))
  		return(null);
  	 else
  		return(match((LList) pat, (LList) fact, subst));
  }

  /** la fonction de filtrage proprement dite*/
  public static Hashtable match(LList pat, LList fact, Hashtable subst) {
	if (subst == null) return null;
  	Object lstPat = pat;
  	Object lstFact = fact;
  	int lenPat = pat.size();
  	int lenFact = fact.size();
  	if (lenPat != lenFact)
  		return null;
  	if (lenPat == 0) return(null);
    for (int i=0; i < lenPat; i++) {
		Pair pairPat = (Pair) lstPat;
		Pair pairFact = (Pair) lstFact;
		// DEBUG
		// System.out.println("match: " + pairPat.car + " avec " + pairFact.car);
		if (pairPat.car instanceof LList) { // on recurse pour les sous-patterns
			if (!(pairFact.car instanceof LList)) return null;
			subst = match((LList) pairPat.car,(LList) pairFact.car,subst);
			if (subst == null) return(null);
		} else
		if (pairPat.car instanceof String) { // si c'est un symbole
			if (((String)pairPat.car).charAt(0) == '?') { // si c'est une variable
				subst = varSubst((String) pairPat.car, pairFact.car,subst);
				if (subst == null) return(null);
			} else { // on teste les symboles
				if (!(pairFact.car instanceof String)) return(null);
				if (!(pairPat.car.equals(pairFact.car))) return(null);
			}
		}
		lstPat = pairPat.cdr;
		lstFact = pairFact.cdr;
    }
    // DEBUG
    // System.out.println("subst: " + subst);
    return subst;
  }

  /** Substitue une valeur à une variable */
  private static Hashtable varSubst(String var, Object val, Hashtable subst) {
  	if (subst.containsKey(var)) {// si la variable est deja associee à une valeur
  		if (subst.get(var).equals(val))
  			return(subst);
  		else return(null);
  	} else {
  		try {
  			subst.put(var,val);
  			return(subst);
  		}
  		catch(NullPointerException e) {
  			return(null);
  		}
  	}
  }

  // prend une expression et retourne une autre expression dans laquelle
  // toutes les variables ont été remplacées par leurs valeurs
  public static Object subst(Object expr, Hashtable env) {
	  	if (expr instanceof Pair) {
	  		Object car = ((Pair) expr).car;
	  		if ((car instanceof String) && (car.equals("$"))) {
	  			Object cdr = ((Pair) expr).cdr;
	  			if (cdr instanceof Pair) {
	  				Object cadr = ((Pair)cdr).car;
	  				Object cddr = ((Pair)cdr).cdr;
	  				// System.out.println(":: evaluation de : " + STools.prinToString(cadr));
	  				return(new Pair(STools.evalit(cadr,env),subst(cddr, env)));
	  			} else
	  				return(LList.Empty);
	  		}
	  		else
	  			return(new Pair(subst(((Pair) expr).car, env),
	  						subst(((Pair) expr).cdr, env)));
	  	} else {
	  		// si c'est une variable
	  		if ((expr instanceof String) && (((String)expr).charAt(0) == '?')) {
	  			if (env.containsKey((String) expr))
	  				return(env.get(expr));
	  		}
	  	}
  		return(expr);
	}

/** Evalue une expression */
/* (define (evalit exp env)
    (cond
       ((null? exp)())
       ((isvar? exp) (car (env-lookup exp env)))
       ((pair? exp)
           (let ((pred (car exp)))
             (if (and (symbol? pred)
                      (not (predicate? pred))
                      (bound? pred))
                 (apply (eval pred nil)
                        (evalit (cdr exp) env))
                 (cons (evalit (car exp) env)
                       (evalit (cdr exp) env)))))
       (else exp))
) */
 public static Object evalit1(Object expr, Hashtable env) {
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

 public static Object evalit(Object expr, Hashtable env) {
 	Object p = evalit1(expr,env);
 	Object r=null;
	try {
	  		System.out.println(":: evaluation de : " + p + " : "+ STools.prinToString(p));
	  		r = Eval.eval(p,Environment.getCurrent());
	}
	catch (WrongArguments e)
		  {
		    if (e.usage != null)
		      System.err.println(":: Usage: "+e.usage + "in predicate");
		    e.printStackTrace(System.out);
	}
	catch (WrongType e)
		  {
		    System.err.println(":: Argument "+e.number+" to "+e.procname
				 +" must be of type "+e.typeExpected + "in predicate");
		    e.printStackTrace(System.out);
	}
	catch(UnboundSymbol e){
		  	System.err.println(":: Unknown symbol "+e.symbol + "in predicate");
		    e.printStackTrace(System.out);
	}
	catch(GenericError e) {
            System.out.println(":: Erreur in predicate" + e);
	}
    catch(Throwable e){
            System.out.println(":: Definitive error in" + e);
    }
	return(r);
 }


  public static LList delete(Object elt, LList lst) {
  	LList l = lst;
  	LList prev;

  	if (((Pair)l).car.equals(elt)) {
  			return((LList)((Pair)l).cdr);
  	}
  	prev = l;
  	l = (LList)((Pair)l).cdr;
  	while(l instanceof Pair) {
  		if (((Pair)l).car.equals(elt)) {
  			((Pair) prev).cdr = ((Pair) l).cdr;
  			return(lst);
  		}
  		prev = l;
  		l = (LList)((Pair)l).cdr;
  	}
  	return(lst);
  }


}
