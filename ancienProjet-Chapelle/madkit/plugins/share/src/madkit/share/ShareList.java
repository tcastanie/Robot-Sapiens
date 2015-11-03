package madkit.share;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import madkit.kernel.AgentAddress;


/*
 * @author G.Hoarau , S.Lara, C.Menichetti, J.Pequignot
 * @version 1.0
 */



public class ShareList extends JList implements MouseListener {
	
	JMenuItem menuItemc = new JMenuItem("connexion");
        JMenuItem menuItemi = new JMenuItem("infos");
        AbstractShareAgent agent;
        AgentAddress[] adresse;
        AgentAddress hum;
        JFrame f;
        JLabel l1;
        JLabel l2;
        GridLayout fs;
        
        public ShareList getList() {return this;}
  
  	public AgentAddress getAdr() {return hum;}
	
	ShareList(String []tab,AbstractShareAgent tu,AgentAddress [] adr) {
		
		super(tab);
		agent = tu;
		adresse = adr;
	
		this.addMouseListener(this);
		menuItemc.addActionListener(new ActionListener(){     // clic sur télécharger (bouton droit)
                	 public void actionPerformed(ActionEvent e){
                		connect();  
                }
            });
		menuItemi.addActionListener(new ActionListener(){      // clic sur éxécuter (bouton droit)
                	public void actionPerformed(ActionEvent e){
     					info();
                }
            }); 
	}     // fin constructeur
	
	
	
	public void connect() {
		String val=(String)getSelectedValue();
		for(int i=0;i<adresse.length;i++) {
                	String re=adresse[i].getKernel().toString();
				if(val.equals(re))
				{
				      	hum=adresse[i];
				    agent.executeServer(adresse[i]);
				}

		}
	}
	
	
	
	public void info() {
		
		String val=(String)getSelectedValue();
		String name,adr;
		for(int i=0;i<adresse.length;i++) {
                	String re=adresse[i].getKernel().toString();
			if(val.equals(re)){
				name=adresse[i].getName();
				adr=adresse[i].toString();
				f=new JFrame();
				f.setBounds(400,200,400,100);
				f.setTitle("infos");
				JDialog bd=new JDialog(f,"infos",true);
				Container c1=f.getContentPane();
				Container c2=bd.getContentPane();
				l1=new JLabel("        Nom :        "+name);
				l2=new JLabel("AgentAddress :  "+adr);
				fs=new GridLayout(3,1);
				c2.setLayout(fs);
				c2.add(l1);
				c2.add(l2);
				c1.add(c2);
				f.setVisible(true);
			}
		}
	}
	
	
	public void mousePressed(MouseEvent e) {                    // gere les double-clics et clics-droit dur le tree
                	String val=(String)getSelectedValue();
                if(e.getClickCount() == 2){                          
                   	for(int i=0;i<adresse.length;i++) {
                		String re=adresse[i].getKernel().toString();
                		 if(val.equals(re))
                		 {
					hum=adresse[i];
					//agent.sendMessage(adresse[i], new RequestTreeMessage("request-tree"));	
					agent.executeServer(adresse[i]);
				}	
					
			}
                    
                }
               else if((e.getClickCount() == 1) && // click droit de souris
                    ((e.getModifiers() & InputEvent.BUTTON3_MASK) == InputEvent.BUTTON3_MASK)){
                        JPopupMenu popup = new JPopupMenu();
                    	popup.add(menuItemc);
            		popup.add(menuItemi);
            		popup.show(e.getComponent(), e.getX(), e.getY());
                     
                 } 
             }
        
	
        public void mouseEntered(MouseEvent e){}
        public void mouseExited(MouseEvent e){}
        public void mouseClicked(MouseEvent e) {}
        public void mouseReleased(MouseEvent e) {}
	 


    	}
