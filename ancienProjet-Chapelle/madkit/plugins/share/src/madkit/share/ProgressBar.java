package madkit.share;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.Timer;

import madkit.kernel.*;


class ProgressBarGui extends JPanel {
	
    public final static int ONE_SECOND = 1000;
    
    private JProgressBar progressBar;
    JPanel centerPane;
//    JPanel mainPane;
    JLabel text;
    JLabel time;
    JLabel download;
    JPanel pan;
    int second;
    int minute;
    int hour;

    private Timer timer;
	
	ProgressBar ag;
	SmallShareAgent smallShareAgent;
    
    public JProgressBar getBar() { return progressBar;}	
    public void setMessage(String n){ progressBar.setString(n);}	
    public void setText(String n){text.setText(n);}
    public void setDownloadSize(String n){download.setText(n);}
    public void setMax(int max) {progressBar.setMaximum(max);timer.start();}	
    
    public ProgressBarGui(ProgressBar _ag, int max, String title,String s,SmallShareAgent ssa) {
	
	ag = _ag;
	smallShareAgent = ssa;
	second=0;
	minute=0;
	hour=0;
	
//	setResizable(false);
	//setTitle(title);
	this.setPreferredSize(new Dimension(500,90));	
	setLocation(300,300);

	text = new JLabel("waiting...");
	download = new JLabel("waiting...");
	time = new JLabel("waiting...");
	
	progressBar = new JProgressBar(0,(int)max);
	progressBar.setValue(0);
	progressBar.setStringPainted(true);
	progressBar.setString(s);
		
	setLayout(new BorderLayout());
	centerPane = new JPanel(new GridLayout(2,1));

	centerPane.add(time);
	centerPane.add(download);
	add(text,"North");
	add(centerPane,"Center");
	add(progressBar,"South");
		
//	setVisible(true);
	
//	addWindowListener(new WindowAdapter() {
//		public void windowClosing(WindowEvent e) {
//		smallShareAgent.sendMessage(smallShareAgent.getAddress(),new KillMessage());
//		dispose();	
//		}	
//	});

	timer = new Timer(ONE_SECOND, new ActionListener() {
		public void actionPerformed(ActionEvent evt) {
		    second++;
		    if(second>59){
				second=0;
				minute++;
		    }
		    if(minute>59){
				minute=0;
				hour++;
		    }
		    removeAll();
		    centerPane.removeAll();
		    time = new JLabel("elapsed time : "+hour+"h "+minute+"min "+second+"s");
		    centerPane.add(time);
		    centerPane.add(download);
		    add(text,"North");
		    add(centerPane,"Center");
		    add(progressBar,"South");
		    validate();
		}
	    });
    }
}

public class ProgressBar extends Agent {
	ProgressBarGui gui;
	SmallShareAgent smallShareAgent;
	String groupName="share";
	
	int max=0;
	String title="Agent";
	String aString;
	
	boolean living=true;
	

	public JProgressBar getBar() { return gui.getBar();}	
	public void setMessage(String n){ gui.setMessage(n);}	
	public void setText(String n){gui.setText(n);}
	public void setDownloadSize(String n){gui.setDownloadSize(n);}
	public void setMax(int max) {gui.setMax(max);}
	public void validate(){gui.validate();}
	public void setVisible(boolean b){gui.setVisible(b);}
	
	public ProgressBar(int max, String title,String s,SmallShareAgent ssa){
		this.max = max;
		this.title = title;
		this.aString = s;
		smallShareAgent = ssa;
	}
	
	public void initGUI(){
		gui = new ProgressBarGui(this, max,title, aString ,smallShareAgent);
		setGUIObject(gui);
	}
	
	/*======================= Activation de l'agent==========================*/
   public void activate(){
	   groupName = smallShareAgent.getGroupName();
		
	   createGroup(true,groupName,null,null);
	   requestRole(groupName,"ProgressBar",null);
   }


   /*========================================================================*/
   /*======================= Boucle principale de l'agent ===================*/
   public void live()		
   {
   while(living){
	   exitImmediatlyOnKill();
		Message m = waitNextMessage();
	   }

   }
	
   /*========================================================================*/
   /*============================ Mort de l'agent ===========================*/
   public void end(){
		smallShareAgent.sendMessage(smallShareAgent.getAddress(),new KillMessage());
   //System.out.println("(client) SmallShareAgent killed");	
   }
}
