/*
* WebBrowserAgent.java -an agent that browse Web pages
* Copyright (C) 2000-2002 Jacques Ferber
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
package madkit.system;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.StringTokenizer;

import madkit.kernel.Agent;
import madkit.kernel.Message;
import madkit.kernel.StringMessage;
import madkit.utils.graphics.WebBrowser;

public class WebBrowserAgent extends Agent {

    WebBrowser display;
    String homeAddress;

    public WebBrowserAgent() {
    }

    public WebBrowserAgent(String s) {
        homeAddress=s;
    }




    public void initGUI(){
        display = new WebBrowser(this);
	    setGUIObject(display);
        if (homeAddress != null){
            display.setHomeAddress(homeAddress);
        }
        display.displayHomeURL();
    }

    public void activate(){
        //createGroup(false,"system",null,null);
        requestRole("system","browser",null);
    }

    public void live()
    {
		while(true){
			Message m =  waitNextMessage();
	      	handleMessage(m);
		}
	}

    void handleMessage(Message m){
        if (m instanceof StringMessage){
            StringMessage msg = (StringMessage) m;
            String content = msg.getString();
            StringTokenizer st = new StringTokenizer(msg.getString()," ");
            String token = st.nextToken();
            if (token.equalsIgnoreCase("$goto")){
              gotoUrl(st.nextToken());
            } else
              gotoUrl(msg.getString());
        }
    }

    void gotoUrl(String st){
      try {
        URL url = new URL(st);
        display.displayURL(url);
      } catch(MalformedURLException e){
            display.printMsgBox("Malformed URL : " + st);
      }
    }

}
