/*
* MemoryMonitorAgent.java -an agent monitoring JVM resources
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

import java.awt.BorderLayout;import java.awt.event.ActionEvent;import java.awt.event.ActionListener;import javax.swing.JButton;import javax.swing.JLabel;import javax.swing.JPanel;import madkit.kernel.Agent;import madkit.kernel.Message;import madkit.kernel.StringMessage;

public class MemoryMonitorAgent extends Agent implements ActionListener {

    Monitor monitor;
    Runtime runtime = Runtime.getRuntime();
    boolean working = true;

    JLabel threadsinfo;

    public MemoryMonitorAgent() {
    }


    public void initGUI(){
        JPanel p = new JPanel(new BorderLayout());
        JPanel buttonPanel = new JPanel();

        threadsinfo = new JLabel("                 ");

        JButton bgc=new JButton("GC");
        JButton bstart=new JButton("Start");
        JButton bstop=new JButton("Stop");

        bgc.addActionListener(this);
        bstart.addActionListener(this);
        bstop.addActionListener(this);
        buttonPanel.add(bstart);
        buttonPanel.add(bstop);
        buttonPanel.add(bgc);

        p.add(threadsinfo,BorderLayout.NORTH);
        p.add(buttonPanel,BorderLayout.SOUTH);
        monitor = new Monitor();
        p.add(monitor,BorderLayout.CENTER);
        monitor.setTopString("K allocated");
        monitor.setBottomString("K used");
        this.setGUIObject(p);

    }

    public void activate(){
        requestRole("system","memory monitor",null);
        float freeMem = runtime.freeMemory();
        float totalMem = runtime.totalMemory();
            float alloc=totalMem-freeMem;
            monitor.setValues(totalMem/1024,alloc/1024);
    }

    void showThreads(){
        ThreadGroup currentGroup = Thread.currentThread().getThreadGroup();
        String groupName = currentGroup.getName();
        int numThreads = currentGroup.activeCount();
        threadsinfo.setText("ThreadGroup: "+groupName+", num: "+numThreads);

    }

    public void live(){
        Message m;
        while (true) {
            if (!working){
                m = waitNextMessage();
                handleMessage(m);
            } else {
                pause(1000);
                float freeMem = runtime.freeMemory();
                float totalMem = runtime.totalMemory();
                float alloc=totalMem-freeMem;
                monitor.setValues(totalMem/1024,alloc/1024);
                m = nextMessage();
                if (m != null)
                    handleMessage(m);
                showThreads();
	    	}
        }
    }

    void handleMessage(Message m){
        if (m instanceof StringMessage){
            String s = ((StringMessage)m).getString();
            if (s.equals("start"))
                working=true;
            else if (s.equals("stop"))
                working=false;
        }
    }

    public void actionPerformed(ActionEvent e){
        String c = e.getActionCommand();
        if (c.equals("GC")){
            System.out.println("GC...");
            runtime.gc();
        } else if (c.equals("Start"))
            sendMessage(this.getAddress(),new StringMessage("start"));
        else if (c.equals("Stop"))
            sendMessage(this.getAddress(),new StringMessage("stop"));
    }
}
