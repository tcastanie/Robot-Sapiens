import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

import jsynedit.*;


public class Main
	extends JFrame
	implements WindowListener//, ActionListener
{
	private AbstractEditorPanel memo;
	//private AbstractDoc memo;
	
	public static void main( String[] args )
	{
		Main win = new Main(args);
	}
	
	public Main(String[] args)
	{

	memo = new AbstractEditorPanel();
	
	
for(int i = 0; i < args.length; i++)
{
	try {
		memo.readFile(args[i]);
		String ext=memo.getExtension(args[i]);
	  memo.chooseTokenMarker(ext);
} catch (Exception e) {
}
}
		// dav déb
		//memo.setName("MadKit Advanced Text Editor");
		// dav fin
		
		/*memo = new AbstractDoc();
		
		JButton undo = new JButton("undo");
		JButton redo = new JButton("redo");
		JButton cut = new JButton("cut");
		JButton copy = new JButton("copy");
		JButton paste = new JButton("paste");
		
		undo.addActionListener( this );
		redo.addActionListener( this );
		cut.addActionListener( this );
		copy.addActionListener( this );
		paste.addActionListener( this );
		
		
		getContentPane().setLayout( new BorderLayout() );
		
		JPanel north = new JPanel();
		JPanel center = new JPanel();
		
		north.add(undo);
		north.add(redo);
		north.add(cut);
		north.add(copy);
		north.add(paste);
		
		center.add(memo);
		
		getContentPane().add(BorderLayout.NORTH, north);
		getContentPane().add(BorderLayout.CENTER, center);
		*/
		getContentPane().add(memo);
		setSize(800,500);
		
		addWindowListener(this);
		
		show();
	}
	

	// Implémentation du WindowListener
	public void windowActivated( WindowEvent e ) {}
	public void windowDeactivated( WindowEvent e ) {}
	public void windowClosed( WindowEvent e ) {/*System.out.println("main closed");*/}
	public void windowIconified( WindowEvent e ) {/*System.out.println("main icon");*/}
	public void windowDeiconified( WindowEvent e ) {}
	public void windowOpened( WindowEvent e) {}
	public void windowClosing( WindowEvent e )
	{
		/*System.out.println("main closing");*/
		System.exit(0);
	}
/*	
	public void actionPerformed( ActionEvent e )
		{
			if ( e.getActionCommand().equals( "undo") )
				memo.undo();
			else if ( e.getActionCommand().equals("redo") )
				memo.redo();
			else if ( e.getActionCommand().equals("cut") )
				memo.cut();
			else if ( e.getActionCommand().equals("copy") )
				memo.copy();
			else if ( e.getActionCommand().equals("paste") )
				memo.paste();
		}
		*/
}
