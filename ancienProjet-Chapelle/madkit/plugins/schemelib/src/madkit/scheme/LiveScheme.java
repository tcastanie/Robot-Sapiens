/*
* LiveScheme.java - an evaluator for Scheme (Kawa) expressions
* Copyright (C) 1998-2002 Olivier Gutknecht
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
package madkit.scheme;

import gnu.mapping.Binding;
import gnu.mapping.Environment;
import gnu.mapping.Future;
import gnu.mapping.OutPort;
import gnu.mapping.Procedure;
import gnu.mapping.TtyInPort;

import java.io.Reader;

import kawa.Shell;
import madkit.utils.graphics.IOPanel;

/** A simple agent that extends the SchemeAgent to launch a interactive
    read-eval-print loop

    @author Ol. Gutknehct */

public class LiveScheme extends SchemeAgent
{
    Reader in_r;
    IOPanel message;


    Future thread;

    public void initGUI()
    {
	message = new IOPanel(true);
	in_r = message.getIn();
	setGUIObject(message);
	setOutputWriter(message.getOut());
    }

    public void activate()
    {
    }

    public void live()
    {

    out_p = new OutPort(message.getOut(), true, "<msg_stdout>");
    err_p = new OutPort(message.getErr(), true, "<msg_stderr>");
    //OutPort.setOutDefault(new OutPort(message.getOut(),"<stdout>"));
    //OutPort.setErrDefault(new OutPort(message.getErr(),"<stderr>"));
    //out_p = OutPort.outDefault();
    //err_p = OutPort.errDefault();
	TtyInPort in_p = new TtyInPort(message.getIn(), "<msg_stdin>", out_p);
	Binding pr = Environment.getCurrentBinding("default-prompter");
	if (pr != null)
	    {
		Procedure prompter = pr.getProcedure();
		((TtyInPort)in_p).setPrompter(prompter);
	    }
	Shell.run(interp, env, in_p, out_p, err_p);
//
    }
}





