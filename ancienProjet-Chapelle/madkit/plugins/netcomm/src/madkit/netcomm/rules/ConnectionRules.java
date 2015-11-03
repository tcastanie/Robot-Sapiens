/*
 * ConnectionRules.javaCreated on Dec 4, 2003
 *
 * Copyright (C) 2003 Sebastian Rodriguez
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.

 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 *Last Update $Date: 2003/12/17 16:33:14 $

 */
package madkit.netcomm.rules;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import madkit.netcomm.KnownProtocols;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


/**Contains a set of rules to accept, reject or refuse incomming connections.
 * 
 * 
 * <b>Warnning</b>The Default Rule accepts all incomming connections.
 * <b>Warnning</b> If madkit.netcomm.rules is defined, the rules will be loaded from
 * the file and all parameters will be disregarded.
 * @author Sebastian Rodriguez - sebastian.rodriguez@utbm.fr
 *	@version $Revision: 1.1 $
 */
public final class ConnectionRules {
//	private static final Logger logger=Logger.getLogger(ConnectionRules.class);
	private Rule defaultRule=new Rule(".*",Rule.ACCEPT,null);
	
	private static ConnectionRules INSTANCE=new ConnectionRules();
	
	private Collection rules;
	
	/**Creates a new ConnectionRule with an empty rule set and Rule.ACCEPT as the default action
	 * 
	 */
	public ConnectionRules() {
		this(Rule.ACCEPT,null,new Vector());
	}
	
	/**Creates a new ConnectionRule with an empty rule set and <code>action</code> as the default action
	 * @param action
	 * @param arg
	 */
	public ConnectionRules(int action,Object arg){
		this(action,arg,new Vector());
	}
	
	/**Creates a new ConnectionRule with <code>rules</code> as the rule set and Rule.ACCEPT as the default action
	 * @param rules
	 */
	public ConnectionRules(Collection rules){
		this(Rule.ACCEPT,null,rules);
		
	}
	
	/**Creates a new Connectionrule with <code>action</code> as the default's rule action,
	 * and <code>rules</code> as the rules set.
	 * @param action default action
	 * @param arg	default rule argument
	 * @param rules rules set.
	 */
	public ConnectionRules(int action,Object arg,Collection rules){
		this.rules=rules;
		setDefaultConnectionRuleAction(action,arg);
		loadRules();
	}

	
	
	/**Sets the action for the default action rule.
	 * @param action action
	 * @param arg argument.
	 */
	public void setDefaultConnectionRuleAction(int action,Object arg){
		Rule r=new Rule(".*",action,arg);
		defaultRule=r;
	}
	
	/**gets the Default connection rule.
	 * @return default rule.
	 */
	public Rule getDefaultConnectionRule(){
		return defaultRule;
	}
	
	/**Finds the firts rule that matches <code>host</code>. If no rule is found, the defualt
	 * rule is returned.
	 * @param host
	 * @return the rule to apply.
	 */
	public Rule getRule(String host){
		for (Iterator iter = rules.iterator(); iter.hasNext();) {
			Rule element = (Rule) iter.next();
			if(element.matches(host)){
				return element;
			}
				
		}
		
		return defaultRule;	
	}
	
	private void loadRules(){
		
		String rFile=System.getProperty("madkit.netcomm.rules");
		if(rFile!=null){
			File f=new File(rFile);
			if(f.exists() && f.canRead()){
				debug("Loading Rules");
				if(!loadXMLRules(f)){
					debug("Impossible to load Rules");
				}
			}else{
				debug("File "+f+" could not be opened."+System.getProperty("line.separator")+
							" Please check that the file defined in madkit.netcomm.rules exists and has read permissions");
			}
		}
	}
	
	private boolean loadXMLRules(File xmlrule){
			try {
				DocumentBuilder df=DocumentBuilderFactory.newInstance().newDocumentBuilder();
				df.setEntityResolver(new NetCommResolver());
				Document doc=df.parse(xmlrule);
				
				NodeList rules=doc.getElementsByTagName("defaultrule");
				if(rules.getLength()>1){
					debug("The connection rule file must contain only one defaultrule");
					return false;
				}
				Element e=(Element) rules.item(0);
				int defAction=string2Action(e.getAttribute("action"));
				Object defArg=createProperArgument(e,defAction);
				
				rules=doc.getElementsByTagName("rule");
				Vector v=new Vector();

				for( int i=0; i<rules.getLength();i++){
					Rule r=handleNode((Element) rules.item(i));
					v.add(r);
				}
				this.setDefaultConnectionRuleAction(defAction,defArg);
				this.rules=(Collection) v.clone();
				return true;
			} catch (SAXException e) {
				
			} catch (IOException e) {
				
			} catch (ParserConfigurationException e) {
				
			} catch (FactoryConfigurationError e) {
				
			}
		
		return false;
	}
	
	/*----------------------------------------------------------------------------------------*/
	/**
	 * @param string
	 */
	private void debug(String string) {
		System.err.println(string);
		
	}

	/**
	 * @param e
	 * @return
	 */
	private Rule handleNode(Element node) {
		int act=string2Action(node.getAttribute("action"));
		String m=node.getAttribute("match");
		Object arg = createProperArgument(node, act);
		Rule r1=new Rule(m,act,arg);
		return r1;
	}

	private Object createProperArgument(Element node, int act) {
		Object arg=null;
		switch (act) {
			case Rule.REFUSE :
				arg=node.getAttribute("arg");
				break;
			case Rule.ACCEPT :
				arg=parseProtocols(node.getAttribute("arg"));
				break;
			default :
				break;
		}
		return arg;
	}

	/**
	 * @param string
	 * @return
	 */
	private Collection parseProtocols(String string) {
		if(string.equalsIgnoreCase("")) return null;
		Vector v=new Vector();
		StringTokenizer st=new StringTokenizer(string,":",false);

		while(st.hasMoreTokens()){
			Collection pts=KnownProtocols.getProtocols();
			String p=st.nextToken();

			if(pts.contains(p)){
				v.add(p);
			}
		}
		
		if(v.size()>0){
		return v;
			
		}
		
		return null;
	}

	public static  ConnectionRules getInstance(){
		return INSTANCE;
	}
	
	private int string2Action(String str){
		int r=-1;
		if(str.equalsIgnoreCase("accept")){
			r=Rule.ACCEPT;
		}else if(str.equalsIgnoreCase("refuse")){
			r=Rule.REFUSE;
		}else if(str.equalsIgnoreCase("reject")){
			r=Rule.REJECT;
		}
		return r;
	}
	
	public void printRules(){
		System.out.println("ConnectionRules");
		System.out.println("=================");
		System.out.println("Default :"+ defaultRule.toString());
		System.out.println("others:");
		int i=0;
		for (Iterator iter = rules.iterator(); iter.hasNext();) {
			Rule element = (Rule) iter.next();
			System.out.println("rule "+i+" "+ element.toString());
			i++;
		}
			
	}
}
