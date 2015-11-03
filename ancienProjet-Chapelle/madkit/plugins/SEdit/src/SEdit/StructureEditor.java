/*
* StructureEditor.java - SEdit, a tool to design and animate graphs in MadKit
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
package SEdit;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.PrintJob;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import SEdit.Graphics.GNode;
import SEdit.Graphics.GObject;

import madkit.utils.graphics.*;



/******************************************************************

			CLASS StructureEditor

******************************************************************/



/** Canvas dans lequel les interactions avec l'utilisateur sont réalisés */

public class StructureEditor extends JDesktopPane
			implements MouseListener, MouseMotionListener

{
    // Methodes et variables de classe : Symboles ...

    /** la gestion du presse-papier.  Ol: bien plus logique ici que dans
	SEditAgent (feu SEditApp) qui n'est pas forcément lancé sur le même
	kernel */
    public StructureAgent agent;

    static String clipBoard = "";

    // les pop-up menus
    protected JPopupMenu globalPopup;
    Hashtable globalActions;

    public static String getClipBoard(){return clipBoard;}
    public static void setClipBoard(String s) {clipBoard = s;}

    GObject popupTarget;	// l'element selectionne par le popup menu
    protected Hashtable popupTable = new Hashtable();

    boolean firstTime = true;

    Color backgroundColor=Color.white;
    int color;

    /** pour changer les couleurs
	*/
	public void setColor(int c) {
        color = c;
  		switch(c) {
  			case 0: setBackground(Color.white);break;
  			case 1: setBackground(Color.blue);break;
  			case 2: setBackground(Color.cyan);break;
  			case 3: setBackground(Color.darkGray);break;
  			case 4: setBackground(Color.red);break;
  			case 5: setBackground(Color.green);break;
  			case 6: setBackground(Color.lightGray);break;
  			case 7: setBackground(Color.magenta);break;
  			case 8: setBackground(Color.orange);break;
  			case 9: setBackground(Color.gray);break;
  			case 10: setBackground(Color.pink);break;
  			case 11: setBackground(Color.yellow);break;
  			default : setBackground(Color.white);
		}
  	}

	public int getColor() {
        return(color);
    }

    public static final int SELECT_FLECHE=0;
    public static final int SELECT_RECT=1;
    public static final int SELECT_POLY=2;
    public static final int COUPER=3;
    public static final int COPIER=4;
    public static final int COLLER=5;
    public static final int DETACHER=6;
    public static final int DEPLACER=7;
    public static final int AFFICHER_TEXTE=8;
    public static final int RAFRAICHIR=9;

    public static final int ADD_NODE=10;	// ajoute un node a la structure...
    public static final int ADD_ARROW=11;	// ajoute un arrow a la structure...

    public static final int AJOUTER_NODE=10;	// ajoute un node a la structure...
    public static final int AJOUTER_ARROW=11;	// ajoute un arrow a la structure...
    public static final int DEPLACEMENT=12;

    private Vector listeObjets=new Vector(); 	// contient la liste des objets graphiques
    private Vector listeFleches=new Vector();
    private Vector listeNoeuds=new Vector();
    private Vector listeConnecteurs=new Vector();

    // Cursor insertCursor = new Cursor(Cursor.MOVE_CURSOR);
    Cursor insertCursor = new Cursor(Cursor.CROSSHAIR_CURSOR);
    Cursor defaultCursor = Cursor.getDefaultCursor();

    // a afficher
    private int         mode;           //mode Selectionner/Place/...
    private Vector      vSelected;      //Plusieurs Composants selectionnes
    private Vector      vRefreshed;     //Composants a refraichir si deplacement
    private Vector      translateList = new Vector();     //Composants a refraichir si deplacement

    //Ol: FIXME not in StructureEditor probably
   private ElementDesc  typeElement;   // contient le descripteur de l'element a ajouter

	private StructureBean structureBean; 	// the bean where the editor resides
	public StructureBean getStructureBean(){return structureBean;}
	public void setStructureBean(StructureBean bean){structureBean=bean;}


    private int     xd,yd,xold,yold,xnew,ynew;  //Selection rectangle

    public Graphics GC; // le contexte graphique

    // grid management: grid values is managed by the structure
    // due to the simplicity for saving properties
    // but in the future, this will be done through properties
    // of the editor itself.
 	public void setDisplayGrid(boolean b){
 		getStructure().setDisplayGrid(b);
 	}
 	public boolean getDisplayGrid(){
 		return getStructure().getDisplayGrid();
 	}
 	public void toggleShowGrid(){
 		getStructure().toggleShowGrid();
 		repaint();
 	}

 	// JF Fix me, pas tres beau tout cela...
 	// en montant (vers le bouton)
 	public void setSnapToGrid(boolean b){
 		getStructureBean().setSnapToGrid(b);
 	}

 	// en descendant (vers la structure)
 	public void setSnapToGrid1(boolean b){
 		getStructure().setSnapToGrid1(b);
 	}

 	public int getGridSize(){return getStructure().getGridSize();}

  	// used to open and save files
    //protected String fileName;
    //protected String dirName;

    protected boolean displayCnxLabels  =true;
    protected boolean displayNodeLabels =true;
    protected boolean displayArrowLabels=true;

    public void switchConn()
    {
	displayCnxLabels=!displayCnxLabels;
	repaint();
    }
    public void switchNodes()
    {
	displayNodeLabels=!displayNodeLabels;
	repaint();
    }
    public void switchArrows()
    {
	displayArrowLabels=!displayArrowLabels;
	repaint();
    }

   // public String getFilename() {return(fileName);}




	/** Constructor */
    public StructureEditor(StructureAgent _agent)
    {
    	super();

	setBackground(Color.white);
     	setForeground(Color.black);
	//     	setDoubleBuffered(true);
      	setFont( new Font("Courier",Font.PLAIN,12) );

	mode=SELECT_FLECHE;
	vSelected=new Vector();

	//	globalActions = new Hashtable();
	//	globalActions.put("charger",   "load");
	//	globalActions.put("sauver",    "save");
	// globalActions.put("propriétés","globalProperties");
	// addGlobalPopUp();

	addMouseMotionListener(this);
	addMouseListener(this);
	agent = _agent;
	getStructure().setEditor(this);
      	super.setSize(1200,800);

     // to handle JInternalFrames inside the Editor
     // setDesktopManager(new SEditDesktopManager());
     String javaVersion=System.getProperty("java.version");
     // System.out.print("Java Version: " + javaVersion);
     if (javaVersion.compareTo("1.3") >= 0){
        // System.out.println(", OK");
        setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);
     } else {
        System.out.println(", this Java Machine is a little bit old, but it's all right");
        putClientProperty("JDesktopPane.dragMode","outline");
     }
    }



    public void installActions(ElementDesc ed)
    {
	ActionListener al = new ActionListener()
	    {
		public void actionPerformed(ActionEvent e) {
		    ActionDesc a=null;
		    Object target = popupTarget;

		    if (target instanceof GObject)
			{
			    target = ((GObject)target).getSElement();
			    a = ((SElement)target).getDescriptor().getAction(e.getActionCommand());
			}
		    if (target instanceof Structure)
			a = ((Structure)target).getFormalism().getAction(e.getActionCommand());

		    //    doElementAction(popupTarget,e.getActionE.GetActionCommand()());
		    a.execute(target);
		    translateList.removeAllElements();
		    computeGlobalList(translateList, vSelected);
		    repaint();
		}
	    };
	JPopupMenu p = new JPopupMenu();
	Utils.addPopupItems(p, ed.getActions(),al);
	add(p);
	popupTable.put(ed,p);
    }

    public void processElementPopUp(int x,int y)
    {
	JPopupMenu p = (JPopupMenu)popupTable.get(popupTarget.getSElement().getDescriptor());
	p.show(this,x,y);
    }


    public Dimension getSize()
    {
	return super.getSize();
    }

    public void setSize(Dimension d)
    {
	int x = getSize().width;
	int y = getSize().height;

	if (d.width > x)
	    x=d.width ;

	if (d.height > y)
	    y=d.height;

	super.setSize(x,y);
    }

    public Structure getStructure(){ return agent.getStructure();}
    //    public void setStructure(Structure s){ structure = s;}

    public void addGObject(GObject o){
	  listeObjets.addElement(o);
	  if (o.getSElement() instanceof SArrow)
		  listeFleches.addElement(o);
	  else
		  if (o.getSElement() instanceof SConnect)
		    listeConnecteurs.addElement(o);
		  else
		    listeNoeuds.addElement(o);
    }


    public void addGObjectFirst(GObject o){
	listeObjets.insertElementAt(o,0);
	if (o.getSElement() instanceof SArrow)
	    listeFleches.addElement(o);
	else
	    if (o.getSElement() instanceof SConnect)
		listeConnecteurs.addElement(o);
	    else
		listeNoeuds.addElement(o);
    }

    public void removeGObject(GObject o) {
		listeObjets.removeElement(o);
		listeFleches.removeElement(o);
		listeConnecteurs.removeElement(o);
		listeNoeuds.removeElement(o);
    }

    public void deleteSelection()
    {
	for (Enumeration e = vSelected.elements() ; e.hasMoreElements() ;)
	    ((GObject)e.nextElement()).getSElement().delete();

	//        for(int n=0; n<vSelected.size(); n++)
	//  ((GObject) vSelected.elementAt(n)).delete();
        repaint();
    }

    public Vector getSelection() {return(vSelected);};
    public void setSelection(Vector v)
    {
	vSelected=v;
	for(int n=0; n<vSelected.size(); n++)
	    ((GObject) vSelected.elementAt(n)).select(true);

	computeGlobalList(translateList, vSelected);
    }
    public void addSelection(Vector v)
    {
	for (Enumeration e=v.elements(); e.hasMoreElements();)
	    addSelected((GObject)e.nextElement());

	computeGlobalList(translateList, vSelected);
    }

    public void updateTranslateList()
    {
     	computeGlobalList(translateList, vSelected);
    }

    public void addSelected(GObject o) {
		if (vSelected.isEmpty())
			getStructureBean().inspectElement(o.getSElement());
		if (!vSelected.contains(o)){
			vSelected.addElement(o);
			o.select(true);
			computeGlobalList(translateList,vSelected);
		}
    }

    public void removeSelected(GObject o) {
	  vSelected.removeElement(o);
	  o.select(false);
	  computeGlobalList(translateList, vSelected);//	computeTranslateList();
    }

    /** supprime tous les elements selectionnes */
    public void clearSelection(){
	  for (Enumeration e = vSelected.elements() ; e.hasMoreElements() ;)
		  ((GObject)e.nextElement()).select(false);
	  vSelected.removeAllElements();
	  translateList.removeAllElements();

	  getStructureBean().inspectElement(null);
    }



  /* public void setFileNames(String dName, String fName) {
	  dirName = dName;
      fileName = fName;
   } */

    public void clearAll(){
	  vSelected.removeAllElements();
	  listeObjets.removeAllElements();
	  listeNoeuds.removeAllElements();
	  listeFleches.removeAllElements();
	  listeConnecteurs.removeAllElements();
	  getStructure().clearAll();
	  repaint();
   }

/*   public void close() {
   	int option;
   	if (getStructure().modified) {
       	option = JOptionPane.showConfirmDialog(null,
					       "Structure was modified, do you want to save it ?",
                "Saving", JOptionPane.YES_NO_OPTION);
           if (option == JOptionPane.YES_OPTION);
           	save();
       }
	//Ol: FIXME       structure.close();
   }

   void saveAs() {
       JFileChooser fd = new JFileChooser(System.getProperty("user.dir",null));
       fd.setDialogTitle("Saving structure");
       int retval = fd.showOpenDialog(null);
       if (retval != -1)
	   {
	       dirName = fd.getSelectedFile().getParent()+File.separator;
	       fileName = fd.getSelectedFile().getName();
	       save1();
	   }
    }


   void save() {
       JFileChooser fd = new JFileChooser(System.getProperty("user.dir",null));
       fd.setDialogTitle("Saving structure");
       int retval = fd.showOpenDialog(null);
       if (retval != -1)
	   {
	       dirName = fd.getSelectedFile().getParent()+File.separator;
	       fileName = fd.getSelectedFile().getName();
	       save1();
	   }
    }


   void save1() {
	System.out.println("saving standard file : " + fileName);
   	if (fileName != null) {
       	try{
	    // System.out.println(":: sauvegarde de " + dirName + fileName);
	    PrintWriter outFile = new PrintWriter(new FileOutputStream(dirName+fileName));
	    // structure.save(outFile,false);
            outFile.close();
	    //Ol: FIXME should settitle on editor
	    // fr.setTitle(fr.getFormalism().getName()+ " - " + fileName);
	} catch (FileNotFoundException e) {
	    System.err.println("saveContent: " + e);
	} catch (IOException e) {
	    System.err.println("saveContent: " + e);
	}
	// System.out.println(":: fichier " + fileName + " sauve");
	}
   } */

    void pasteClipBoard(){
	  clearSelection();
	  agent.insertClipBoard();
	  updateTranslateList();
    }


   void copyToClipBoard()
    {
	Vector v1 = new Vector();
	computeGlobalList(v1, vSelected);
	Vector v = new Vector();
	for (Enumeration e = v1.elements() ; e.hasMoreElements() ;)
	    {
		SElement s = ((GObject)e.nextElement()).getSElement();
		v.addElement(s);
	    }

       if (v.size()>0)
	   {
	       XMLStructureSaver xf =  new XMLStructureSaver(agent);
	       xf.generateDOM(getStructure(),getStructure().getFormalism(),v);

	       agent.setClipBoard(xf.generateDOM(getStructure(),
						  getStructure().getFormalism(),
						  v));
	   }
    }

   void paste()
    {
	clearSelection();

	String s = StructureEditor.getClipBoard();
	if (!(s.equals(""))) {
	    // FIXME	Shell.runString(s,schemeModule.interpreter);
	    // or SEditKawaShell.runOutString(s,schemeModule.interpret,OutPort.outDefault()); if we need a trace
     	}
	repaint();
   }

   void cut() {
   		copyToClipBoard();
   		deleteSelection();
   }

   public String askForNewName(String name) {

   		String newname;
       newname = JOptionPane.showInputDialog(this,
                                      "Modification de : " + name,
                                      "Nouveau nom",
                                      JOptionPane.PLAIN_MESSAGE);
       if (name.equals(newname))
       	return(null);
       else
       	return(newname);
	}

	public String askForString(String header) {
		return askForString(header, "");
	}

	public String askForString(String header, String message) {
		String s;
        s = JOptionPane.showInputDialog(this, header, message , JOptionPane.PLAIN_MESSAGE);
       if ((s == null) || (s.equals("")))
       	return(null);
       else
       	return(s);
	}



    /** Find the object at a given point. The method tries to be a bit
	smart so that connectors or arrows have priority on nodes or
	components so that we can easily grab them */
    public GObject find(int x, int y)
	{
	    int n = listeObjets.size();
	    Point p = new Point(x,y);
	    GObject c;
	    Vector v = new Vector();


	    for (Enumeration e = listeObjets.elements() ; e.hasMoreElements() ;)
		{
		    c=(GObject)e.nextElement();
		    if (c.contains(p))
			v.addElement(c);//return calignho;
		}

	    if (v.size()==0)
		return null;
	    if (v.size()==1)
		return (GObject)v.elementAt(0);
	    if (v.size()>1)
		{
		    Enumeration ec = v.elements();
		    GObject first = (GObject)ec.nextElement();
		    if (first.getSElement() instanceof SComponent)
			{
			    for (; ec.hasMoreElements() ;)
				{
				    c=(GObject)ec.nextElement();
				    if ((c.getSElement() instanceof SConnect) &&
					(((SConnect)c.getSElement()).getComponent()
					 == first.getSElement()))
					return c;
				    if (c.getSElement() instanceof SArrow)
					return c;
				}
			}
		    return first;
		}
	    return null;
	}


    /** Find all elements that are contained in a given rectangle */
    public Vector find(Rectangle r)
    {
	Vector      v=new Vector();
	GObject   c;

	for (Enumeration e = listeObjets.elements() ; e.hasMoreElements() ;)
	    {
		c = (GObject)e.nextElement();
		if(c.isContainedIn(r) && c.isSelectable())
		    v.addElement(c);
	    }
        return v;
    }

    void computeGlobalList(Vector v, Vector orig)
    {
	GObject tmp;
	//System.err.println("--1-COMPUTEDGL"+v+" "+orig);

	for (Enumeration e = orig.elements() ; e.hasMoreElements() ;)
	    {
		tmp = (GObject)e.nextElement();
		if (!v.contains(tmp))
		    {
			if ((tmp.getSElement() instanceof SConnect)||
			    (tmp.getSElement() instanceof SComponent))
			    {
				SComponent c;
				if (tmp.getSElement() instanceof SConnect)
				    c = ((SConnect)tmp.getSElement()).getComponent();
				else
				    c = (SComponent)tmp.getSElement();

				if (c!=null)
				    if (!v.contains(c.getGObject()))
					{
					    v.addElement(c.getGObject());
					    Enumeration ei;
					    ei = c.getInConnectors();
					    for (; ei.hasMoreElements();)
						v.addElement(((SConnect)ei.nextElement()).getGObject());

					    ei = c.getOutConnectors();
					    for (; ei.hasMoreElements();)
						v.addElement(((SConnect)ei.nextElement()).getGObject());
					}

			    }

			else
			    v.addElement(tmp);
			}
		}
    }

    /** Translate a complete selection */
    void translateSelection(int dx, int dy ){
	  int minx=Integer.MAX_VALUE;
	  int miny=Integer.MAX_VALUE;
	  Rectangle r = null;
	  GObject tmp ;

	  r = ((GObject)vSelected.elementAt(0)).getBounds();
	  computeBounds(r,vSelected,false);
	  tmp = (GObject) translateList.firstElement();
	  if ((vSelected.size() == 1) && (tmp instanceof GNode)
		  && (((GNode) tmp).getResizable())
		  && (((GNode) tmp).getSelectedHandle()!=0)){
		((GNode) tmp).doDrag(dx,dy);
	  }
	  else {
		for (Enumeration e = translateList.elements() ; e.hasMoreElements() ;)
			{
			tmp = (GObject)e.nextElement();
			tmp.translate(dx,dy);
			r.add(tmp.getBounds());//translate(dx,dy);
			}
		r.setSize(r.getSize().width+2, r.getSize().height+2);
	  }
	  repaint(r);
    }

  void snapToGridSelection(int s){
  	GObject tmp;
  	for (Enumeration e = translateList.elements() ; e.hasMoreElements() ;)  {
		tmp = (GObject)e.nextElement();
		tmp.snapToGrid(s);
	}
  }

  void alignVertical()
    {
	if (vSelected.size()>1)
	    {
		Vector v = new Vector();
		Vector modules = new Vector();

		Enumeration e = vSelected.elements() ;
		GObject ref = (GObject)e.nextElement();
		for ( ; e.hasMoreElements() ;)
		    {
			GObject o = ((GObject)e.nextElement());
			if (o.getSElement() instanceof SConnect)
			    {
				GObject m=((SConnect)(o.getSElement())).getComponent().getGObject();
				if (!v.contains(m))
				    v.addElement(m);
			    }
			else
			    if (!v.contains(o))
				v.addElement(o);
		    }
		if (ref.getSElement() instanceof SConnect)
		    {
			GObject m=((SConnect)(ref.getSElement())).getComponent().getGObject();
			if (v.contains(m))
			{
			    ref=m;
			    v.removeElement(m);
			}
		    }
		if (v.size()>0)
		    {
			e = v.elements() ;
			for ( ; e.hasMoreElements() ;)
			    {
				GObject o = ((GObject)e.nextElement());

				if (o.getSElement() instanceof SComponent)
				    {
					Point orig = o.getLocation();
					o.alignVerticalTo(ref);
					Point end = o.getLocation();
					Enumeration ei;
					ei = ((SComponent)o.getSElement()).getInConnectors();
					for (; ei.hasMoreElements();)
						((SConnect)ei.nextElement()).getGObject().translate(end.x-orig.x,end.y-orig.y);
					ei = ((SComponent)o.getSElement()).getOutConnectors();
					for (; ei.hasMoreElements();)
					    ((SConnect)ei.nextElement()).getGObject().translate(end.x-orig.x,end.y-orig.y);
				    }
				else
				    o.alignVerticalTo(ref);
			    }
			//clearSelection();
			repaint();
		    }
	    }
    }

  void alignHorizontal()
    {
	if (vSelected.size()>1)
	    {
		Vector v = new Vector();
		Vector modules = new Vector();

		Enumeration e = vSelected.elements() ;
		GObject ref = (GObject)e.nextElement();
		for ( ; e.hasMoreElements() ;)
		    {
			GObject o = ((GObject)e.nextElement());
			if (o.getSElement() instanceof SConnect)
			    {
				GObject m=((SConnect)(o.getSElement())).getComponent().getGObject();
				if (!v.contains(m))
				    v.addElement(m);
			    }
			else
			    if (!v.contains(o))
				v.addElement(o);
		    }
		if (ref.getSElement() instanceof SConnect)
		    {
			GObject m=((SConnect)(ref.getSElement())).getComponent().getGObject();
			if (v.contains(m))
			{
			    ref=m;
			    v.removeElement(m);
			}
		    }
		if (v.size()>0)
		    {
			e = v.elements() ;
			for ( ; e.hasMoreElements() ;)
			    {
				GObject o = ((GObject)e.nextElement());

				if (o.getSElement() instanceof SComponent)
				    {
					Point orig = o.getLocation();
					o.alignHorizontalTo(ref);
					Point end = o.getLocation();
 					Enumeration ei;

					ei = ((SComponent)o.getSElement()).getInConnectors();
					for (; ei.hasMoreElements();)
						((SConnect)ei.nextElement()).getGObject().translate(end.x-orig.x,end.y-orig.y);
					ei = ((SComponent)o.getSElement()).getOutConnectors();
					for (; ei.hasMoreElements();)
					    ((SConnect)ei.nextElement()).getGObject().translate(end.x-orig.x,end.y-orig.y);
				    }
				else
				    o.alignHorizontalTo(ref);
			    }
			//clearSelection();
			repaint();
		    }
	    }
    }


    void setInsertMode(ElementDesc c)
    {
	  if (c instanceof ArrowDesc)
		  setMode(ADD_ARROW);
	  else
		  setMode(ADD_NODE);
	  typeElement = c;
    }

    void setInsertMode(int mode, ElementDesc c)
    {
	setMode(mode);
	typeElement =c;
	//	System.err.println(mode+" "+c);
    }


    void setMode(int m)
    {
  		mode = m;
		if ((mode == ADD_ARROW) || (mode == ADD_NODE))
			setCursor(insertCursor);
		else
			setCursor(defaultCursor);
    }

  /** cree un element de la classe passee en argument
  	  doit etre une instance d'une sous-classe de SElement */
    void installNode(GObject c, boolean selected)
    {
	addGObject(c);
	c.setEditor(this);
	installActions(c.getSElement().getDescriptor());
	if (selected)
	    addSelected(c);
	repaint();
    }

    void installArrow(GObject c, boolean selected)
    {
	addGObject(c);
	c.setEditor(this);
	installActions(c.getSElement().getDescriptor());
	if (selected)
	    addSelected(c);
	repaint();
    }

  void prepareNewArrow()
  {
      //Recherche des Composants de debut et de fin de l'arc
      GObject from,to;
      from=find(xd,yd);
      to=find(xnew,ynew);

      if (from==null || to == null)
	  return;


      SElement origin = from.getSElement();
      SElement target = to.getSElement();
      if(!(from.getSElement() instanceof SNode && to.getSElement() instanceof SNode)){
          // System.beep();
          return;
      }

      Point
	  p=from.getLocation(),
	  q=to.getLocation();

      Dimension
	  dp = from.getDimension(),
	  dq = to.getDimension();

      int cx = (p.x+dp.width/2+q.x+dq.width/2)/2;
      int cy = (p.y+dp.height/2+q.y+dq.height/2)/2;
      // System.err.println("fleche"+p+q+cx+cy);

      agent.doCommand(new NewArrowCommand(typeElement, new Point(cx,cy), origin, target));
  }




  /** insertion du composant dans un container */

  	public void addNotify() {
		super.addNotify();
	}

  	public void setGraphics(Graphics g) {
  	GC =g;

 	}


    public void computeBounds(Rectangle r, Object obj, boolean fromSConnect){
		if ((obj instanceof Vector) || (obj instanceof Enumeration)){
			Enumeration e;
			if (obj instanceof Vector)
				e = ((Vector)obj).elements();
			else
				e = (Enumeration)obj;

			for(; e.hasMoreElements() ;)
				computeBounds(r, e.nextElement(),fromSConnect);
			}
		else  if ((obj instanceof GObject) || (obj instanceof SElement)){
		    GObject o = null;
		    SElement s = null;

		    if (obj instanceof GObject){
			    o = (GObject)obj;
			    s = o.getSElement();
			}
		    if (obj instanceof SElement){
			    s = (SElement)obj;
			    o = s.getGObject();
			}
		    r.add(o.getBounds());

		    if (s instanceof SNode){
			    Vector v;
			    v = ((SNode)s).getInArrows();

			    if (v!=null)
				computeBounds(r,v,fromSConnect);
			    v = ((SNode)s).getOutArrows();
			    if (v!=null)
				computeBounds(r,v,fromSConnect);
			}
		    if (s instanceof SComponent)
			{
			    Enumeration ei;
			    ei = ((SComponent)s).getInConnectors();
			    if (ei!=null)
				computeBounds(r,ei,fromSConnect);
			    ei = ((SComponent)s).getOutConnectors();
			    if (ei!=null)
				computeBounds(r,ei,fromSConnect);
			}
		    if (s instanceof SConnect)
			{
			    if (!fromSConnect)
				computeBounds(r,((SConnect)s).getComponent(),true);
			}
		}
   }


    public Dimension getDimension()
    {
	int maxx = 0;
	int maxy = 0;

	for (Enumeration e = getStructure().getNodes().elements() ; e.hasMoreElements() ;)
	    {
        SNode node=(SNode)e.nextElement();
		GObject obj = node.getGObject();
		Point p = obj.getLocation();
		if (p.x > maxx)
		    maxx=p.x+obj.getDimension().width;
		if (p.y > maxy)
		    maxy=p.y+obj.getDimension().height;
	    }
	for (Enumeration e = getStructure().getArrows().elements() ; e.hasMoreElements() ;) {
	    Point p = ((SElement)(e.nextElement())).getGObject().getLocation();
	    if (p.x > maxx)
		maxx=p.x;
	    if (p.y > maxy)
		maxy=p.y;
	}
	return new Dimension(maxx, maxy);
    }

   public void setCanvasSize() {
   	  Dimension d = getDimension();
	  if (d.width != 0)
	  	setSize(d.width*2,d.height*2);
   }


    public void addGlobalPopUp()
    {
	ActionListener al = new ActionListener(){
	    public void actionPerformed(ActionEvent e) {
           	System.out.println("commande globale: " +e.getActionCommand());
	    }};

	globalPopup = new JPopupMenu();
	Utils.addPopupItems(globalPopup, globalActions, al);
	add(globalPopup);
    }

    // la lecture du popup menu
    public void processMouseEvent(MouseEvent ev)
    {
	if (GraphicUtils.isPopupTrigger(ev)){
	    popupTarget = find(ev.getX(),ev.getY());
	    if (popupTarget != null)
           	processElementPopUp(ev.getX(),ev.getY());
	    // else	// pas de globalPopUp pour l'instant...
	    //		globalPopup.show(this,ev.getX(),ev.getY());
	} else
	    super.processMouseEvent(ev);
    }


// la gestion des événements
  public void mousePressed(MouseEvent e) {
	  e.consume();
	  int x=e.getX();
	  int y=e.getY();

	  //Mises a jours
	  xd=xold=xnew=x;
	  yd=yold=ynew=y;

	  switch(mode){
			//---Debut des differents cas---
		case AJOUTER_NODE : {
			clearSelection();
			if (getStructure().getSnapToGrid()){
				int s = getGridSize();
				x = s*(int)(Math.round(x/s));
				y = s*(int)(Math.round(y/s));
			}

			agent.doCommand(new NewNodeCommand((NodeDesc)typeElement, new Point(x,y)));
		    break;
		}

		case AJOUTER_ARROW:{
		    clearSelection();
		  break;
		}

		case SELECT_FLECHE : {
			GObject c=find(x,y);
			if(c==null ) {
			  //Aucun composant designe
			  //Suppression de la selection precedente
			  if ((e.getModifiers() & InputEvent.SHIFT_MASK) == 0)
				  clearSelection();
			//Il ne s'agit pas du deplacement de composants
			} else if(vSelected.contains(c)){
				if ((e.getModifiers() & InputEvent.SHIFT_MASK) != 0){
					removeSelected(c);
				}
				else {
				  // System.out.println("look for handle");
				  if ((c instanceof GNode) && (((GNode) c).getResizable()))
					  ((GNode) c).doReSelect(x,y);
				  setMode(DEPLACEMENT);
				}
			} else if (c.isSelectable()){
				if ((e.getModifiers() & InputEvent.SHIFT_MASK) == 0)
				clearSelection();

				addSelected(c);
				//Suppression de la selection precedente
				//clearSelection();
				//Ajout de la nouvelle selection
				// c.select(true);
				//deplacement du composant selectionne
				setMode(DEPLACEMENT);
			  }
			  break;
		  }

		case SELECT_RECT :{
		  clearSelection();
		}
    }
    repaint();
  }





  public void mouseDragged(MouseEvent e)
    {
	int x=e.getX();
	int y=e.getY();

        //Mises a jours
	xold=xnew;
	yold=ynew;
	xnew=x;
	ynew=y;

	switch(mode){
	    //---Debut des differents cas---
    	case AJOUTER_NODE:
	    //    setMode(SELECT_FLECHE);
	    break;
	case AJOUTER_ARROW:{
	    //Positionne la couleur de trace en noir
	    GC.setColor(Color.black);
	    //Effacement de la ligne precedente
	    if( xd!=xold || yd!=yold )
		GC.drawLine(xd,yd,xold,yold);
	    //Trace de la nouvelle ligne
	    if( xd!=xnew || yd!=ynew )
		GC.drawLine(xd,yd,xnew,ynew);
	    break;
	}
	case DEPLACEMENT:
	    //	    if(vSelected.size()==1){
	    //((GObject) vSelected.firstElement()).drag(xold,xnew,yold,ynew);
	    //}
	    //else
	    translateSelection((xnew-xold),(ynew-yold));
	    break;
	case SELECT_FLECHE:{
	    setMode(SELECT_RECT);
	    // break;
	} // fin case SELECT_FLECHE
	case SELECT_RECT:{
	    GC.setColor(Color.red);
	    //Effacement du rectangle precedent
	    if(xd!=xold){
			GC.drawLine(xd,yd,xold,yd);
			GC.drawLine(xd,yold,xold,yold);
	    }
	    if(yd!=yold){
			GC.drawLine(xd,yd,xd,yold);
			GC.drawLine(xold,yd,xold,yold);
	    }

	    //Trace du nouveau rectangle
	    if(xd!=xnew){
		    GC.drawLine(xd,yd,xnew,yd);
		    GC.drawLine(xd,ynew,xnew,ynew);
	    }
	    if(yd!=ynew){
		    GC.drawLine(xd,yd,xd,ynew);
		    GC.drawLine(xnew,yd,xnew,ynew);
	    }
	} // case SELECT_RECT
	break;
	} // fin switch
    } // fin mouseDragged

  public void mouseReleased(MouseEvent e) {
      e.consume();
      int x =e.getX();
      int y =e.getY();
      //Mises a jours
      xold=xnew;
      yold=ynew;
      xnew=x;
      ynew=y;

      switch(mode){
	  //---Debut des differents cas---
      case AJOUTER_ARROW : {
	  GC.drawLine(xd,yd,xold,yold);
	  GC.setColor(Color.white);
	  //Effacement de la ligne precedente
	  if( xd!=xold || yd!=yold )
	      GC.drawLine(xd,yd,xold,yold);
	  prepareNewArrow();
	  break;
      }
      case DEPLACEMENT:
	  setMode(SELECT_FLECHE);
	  if (getStructure().getSnapToGrid()){
		int s = getGridSize();
		snapToGridSelection(s);
	  }
	  repaint();
	  break;

      case SELECT_RECT:{
	  //Positionne la couleur de trace en rouge
	  GC.setColor( Color.red );
	  //Effacement du rectangle precedent
	  if( xd!=xold ){
	      GC.drawLine(xd,yd,xold,yd);
	      GC.drawLine(xd,yold,xold,yold);
	  }
	  if( yd!=yold ){
	      GC.drawLine(xd,yd,xd,yold);
	      GC.drawLine(xold,yd,xold,yold);
	  }
	  //Recherche des Objets selectionnes
	  Rectangle r = new Rectangle(Math.min(xd,xnew),
				      Math.min(yd,ynew),
				      Math.abs(xnew-xd),
				      Math.abs(ynew-yd));
	  if ((e.getModifiers() & InputEvent.SHIFT_MASK) == 0)
	      setSelection(find(r));
	  else
	      addSelection(find(r));
	  //Les objets selectionnes sont colores en rouge

	  //Fin de selection : Retour au mode SELECT_FLECHE
	  setMode(SELECT_FLECHE);
	  repaint();
	  break;
      } // fin case SELECT_RECT
      } // fin switch
  } // fin mouseReleased


  void drawGrid(Graphics g){
  	 Dimension d = getSize();
  	 int s = getGridSize();
  	 int nX = (int) Math.round(d.width/s);
  	 int nY = (int) Math.round(d.height/s);
	 g.setColor(Color.lightGray);
	 // verticales
  	 for(int i=0;i< nX; i++){
  	 	g.drawLine(i*s,0,i*s,d.height);
  	 }
  	 // horizontales
  	 for(int i=0;i< nY; i++){
  	 	g.drawLine(0,i*s,d.width,i*s);
  	 }
  }

  public void paintComponent(Graphics g)
    {
	g.setColor(getBackground());
	g.fillRect(0, 0, getWidth(), getHeight());
	doUpdate(g);
    }

    public void doUpdate(Graphics g)
    {
	//Sauvegarde du contexte graphique des GObject
	GC=getGraphics();
	GC.setXORMode(getBackground());

	g.setPaintMode();

	// Ask the structure to draw the background, if any.
	// Do we have to put it here?
	getStructure().drawBackground(g);

	// display the grid if necessary
    if (getDisplayGrid()) drawGrid(g);
	for (Enumeration e = listeNoeuds.elements() ; e.hasMoreElements() ;)
	    {
		GObject o = (GObject)e.nextElement();
		if(o.selected)
		    g.setColor(Color.red );
		else if (mode==DEPLACEMENT)
		    g.setColor(Color.gray);
		else
		    g.setColor(o.getForeground());
		o.paint(g);
	    }

	for (Enumeration e = listeFleches.elements() ; e.hasMoreElements() ;)
	    {
		GObject o = (GObject)e.nextElement();
		if(o.selected)
		    g.setColor(Color.red );
		else if (mode==DEPLACEMENT)
		    g.setColor(Color.gray);
		else
		    g.setColor(o.getForeground());
		o.paint(g);
	}
	for (Enumeration e = listeConnecteurs.elements() ; e.hasMoreElements() ;)
	    {
		GObject o = (GObject)e.nextElement();
		if(o.selected)
		    g.setColor(Color.red );
		else if (mode==DEPLACEMENT)
		    g.setColor(Color.gray);
		else
		    g.setColor(o.getForeground());
		o.paint(g);
	    }

	int n = listeObjets.size();

	Font f = g.getFont();
	Font fconn = new Font(f.getName(),f.getStyle(),f.getSize()-4);

	if (mode!=DEPLACEMENT)
	    {
		if (displayNodeLabels)
		    for (Enumeration e = listeNoeuds.elements() ; e.hasMoreElements() ;)
			{
			    GObject o =(GObject)e.nextElement();
			    if(o.selected)
				g.setColor(Color.red );
			    else
				g.setColor(Color.black);
			    o.displayName(g);
			}
		if (displayArrowLabels)
		    for (Enumeration e = listeFleches.elements() ; e.hasMoreElements() ;)
			{
			    GObject o =(GObject)e.nextElement();
			    if(o.selected)
				g.setColor(Color.red );
			    else
				g.setColor(Color.black);
			    o.displayName(g);
			}
		g.setFont(fconn);
		if (displayCnxLabels)
		    for (Enumeration e = listeConnecteurs.elements() ; e.hasMoreElements() ;)
			{
			    GObject o =(GObject)e.nextElement();
			    if(o.selected)
				g.setColor(Color.red );
			    else
				g.setColor(Color.black);
			    o.displayName(g);
			}
		g.setFont(f);
	    }
		if ((vSelected.size() == 1)&&
		    (vSelected.firstElement() instanceof GNode)){
		  ((GNode) vSelected.firstElement()).displayHandles(g);
		}

    }


    // Impression

    public void print(Frame _f)
    {
		printEditor();
    }

    public void printEditor()
    {
	Frame f = new Frame(" ");
	f.setSize(1,1);
	f.show();

	PrintJob pjob = getToolkit().getPrintJob(f,
						 "Printed with SEdit", null);

	if (pjob != null) {
	    Graphics pg = pjob.getGraphics();
	    Dimension size=getSize();

	    if (pg != null) {
		pg.drawRect(0,0,size.width,size.height);
		this.printAll(pg);
		//	this.print(pg);

		pg.dispose(); // flush page
	    }
	    pjob.end();
	}
	f.dispose();

    }

  public void mouseEntered(MouseEvent e) {}
  public void mouseExited(MouseEvent e) {}
  public void mouseClicked(MouseEvent e) {}
  public void mouseMoved(MouseEvent e) {}

}













