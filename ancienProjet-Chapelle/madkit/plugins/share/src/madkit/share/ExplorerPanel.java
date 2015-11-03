package madkit.share;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;


public class ExplorerPanel extends JPanel implements ActionListener, KeyListener{
	
    Panel p1;
    Panel p2;
    private String location = null;
    static MenuBar menubar;
    private TextArea textArea;
    private boolean ModeVisual; 
    private TextField selection;
    private vList l;
    private String pathname, filename;
    public int ind;
	
	
    public ExplorerPanel(String p){
		
	JLabel path=new JLabel("PATH:");
	selection=new TextField(p);
	selection.addKeyListener(this);
		
	JPanel txt=new JPanel(new BorderLayout());
	txt.add(path,BorderLayout.WEST);
	txt.add(selection,BorderLayout.CENTER);
		
	p1=new Panel(new BorderLayout());
	p1.setBackground(Color.white);
			
	JScrollPane scrollPan = new JScrollPane(p1);  
	scrollPan.setPreferredSize(new Dimension(450,320));
	scrollPan.createHorizontalScrollBar();
	scrollPan.createVerticalScrollBar();
	scrollPan.setViewportView(p1);

	setLayout(new BorderLayout());
	add(txt, BorderLayout.NORTH);
	add(scrollPan, BorderLayout.CENTER);
	setBackground(Color.white);



	Menu menu;
	MenuItem icons,lists;

	menubar = new MenuBar();
    	
	menu = new Menu("Options", false);
	menubar.add(menu);

	lists = new MenuItem("List   ");
	menu.add(lists);
	lists.addActionListener(this);
   
	icons = new MenuItem("Icon   ");
	menu.add(icons);
	icons.addActionListener(this);
	l=new ListFile();
	p1.add((ListFile) l,BorderLayout.CENTER);
		
	path();
         
    }
    
    public void actionPerformed(ActionEvent e) { }
	    
    public void keyReleased(KeyEvent ev) {}
    public void keyTyped(KeyEvent ev) {}	  
    public void keyPressed(KeyEvent ev) 
    {
	if (ev.getSource() instanceof TextField){
	    int k=ev.getKeyCode();
	    switch (k) {
	    case KeyEvent.VK_ENTER:
		validTextField();
		break;
	    default:
		ind=-1;
        
	    }
	} 
    }
    
    public void path()
    {
	location=selection.getText();
	File tmp = new File(location);
	pathname = tmp.getAbsolutePath();
	if (pathname.endsWith("."))
	    pathname = pathname.substring(0, pathname.length() - 2);
	read();
    }
    
    
    public boolean read() {
 
	File dir= new File(pathname);

	if (dir.isDirectory() || pathname.length()<3) {
	    selection.setText(pathname);
	    l.clear(); 
	    if (pathname.length()>3)
		l.addItem("..", "..");
	    String [] Noms = dir.list(); 
	    for (int i = 0; i<Noms.length; i++) {
		dir = new File(pathname+File.separator+Noms[i]);
		l.addItem(Noms[i]+(dir.isDirectory()?File.separator:""), pathname);
	    }
	    return true;
	}
   
	return false; 
    }


    public String getFileName()
    {
	return (selection.getText() + "/" + filename);
    }

    private void readUp() {
	pathname = getParent(pathname);
	read();
    }

    private String getParent(String file) { 
	return file.substring(0, file.lastIndexOf(File.separatorChar));
    }
    
    
    private void validTextField() 
    {
	String tmp_name = pathname;
	pathname = selection.getText();

     
	if (pathname.charAt(pathname.length()-1) == File.separatorChar)
	    pathname = getParent(pathname);
	if (read() == false) 
	    {
		pathname=tmp_name;
		selection.setText(pathname);
	    }
    }
    
    public boolean  action(Event ev,Object what) {
    
	if (((String)what).equals("..")) 
	    readUp();
    
	else {
	    String tmp = (String)what;
      
	    if (tmp.charAt(tmp.length()-1) == File.separatorChar) 
		{
		    String tmp_name = pathname;
		    pathname += File.separator+getParent(tmp);
		    if (read() == false) 
			{
			    pathname=tmp_name;
			    selection.setText(pathname);
			}
		}
   
	}   
	return super.action(ev,what);
   
    }
}
		
		
		
