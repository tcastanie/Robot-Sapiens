package kernelsim;
//import madkit.kernel.*;
//import java.awt.*;
//import java.lang.reflect.Method;
//import java.util.*;
import java.io.*;

// ***********************************
// *** LearnModule.java            ***
// *** Creation 23 / 07 / 2003     ***
// *** Jérôme Chapelle@lirmm.fr    ***
// ***********************************

// Fonctionnement: 030901 madkit 3.1b2\madkit 3.1b2\src\rmc\ ... NeuronAgent.java


/**
 * <p>Titre : LearnModule.java </p>
 * <p>Description : Implémentation du module d'apprentissage</p>
 * @author Jérôme Chapelle@lirmm.fr
 * @version 1.0
 */

public class LearnModule {
     // Matrice d'apprentissage
     // perceptions(t-1), actions, evolutions(t+1), 2 valeurs
     /** perceptions(t-1), actions, evolutions(t+1), 2 valeurs */
     private int m_p_prev,m_action/*,m_p_next*/,m_nb_val;
     private int m_p_prev_def=27;
     private int m_action_def=6;
     //private int m_p_next_def=1;
     private int m_nb_val_def=2;
     private float MAct[][][]; //private float MAct[][][][];
     private float max_value=1;
     private float min_value=0;
     //@SuppressWarnings("unused")
     //051018nl private float med_value=(min_value+max_value)/2;
     //private float def_max_value=1; private float def_min_value=0;
     //private float def_med_value=0.5;
     
     /** This string is used for debug needs (may contains the name of the older)*/
     private String label=null;

     // temp
     public int variable_evolution=0;
     // Pour l'apprentissage
     public float Beta=(float)0.9;
     public boolean exp=false; // On tente une expérience ?
     public int exp_tps=0;     // Tps d'execution de l'expérience
     public int exp_min=15;    // Tps min pour expérimenter
     public int exp_max=50;   // Tps max pour expérimenter
     public boolean exp_fin=false; // Experience terminee
     public int exp_deb=0;
     public float exp_moy=0; // Moyenne d'une expérience
     public int exp_sit;    // Situation au debut de l'expérience
     public float coef_exp=(float)0.5; // coef d'exploration

  /** Constructor that uses default parameters to initialize the matrix (calls SetMActDefValues();) */
  public LearnModule() {
     // Matrice d'apprentissage
     // perceptions(t-1), actions, evolutions(t+1), 2 valeurs
     SetMActDefValues();
     //MAct=makeMatrix(m_p_prev,m_action,m_p_next,m_nb_val);
     MAct=makeMatrix(m_p_prev,m_action,m_nb_val);
  }
  /** Constructor that uses given parameters to initialize the matrix. */
  /*public LearnModule(int m_p_prev,int m_action,int m_p_next,int m_nb_val) {
     // Matrice d'apprentissage
     // perceptions(t-1), actions, evolutions(t+1), 2 valeurs
     if ((m_p_prev<1)||(m_action<1)||(m_p_next<1)||(m_nb_val<1))
        { SetMActDefValues(); }
     MAct=makeMatrix(m_p_prev,m_action,m_p_next,m_nb_val);
  }*/
  /** Constructor that uses given parameters to initialize the matrix. */
  public LearnModule(int m_p_prev,int m_action,int m_nb_val) {
     // Matrice d'apprentissage
     // perceptions(t-1), actions, 2 valeurs
     if ((m_p_prev<1)||(m_action<1)||(m_nb_val<1))
        { SetMActDefValues(); }
     MAct=makeMatrix(m_p_prev,m_action,m_nb_val);
  }
  
  public int m_p_prev()
  {   return m_p_prev;  }
  public int m_action()
  {	  return m_action;  }
  public int m_nb_val()
  {	  return m_nb_val;  }
  
  /** Returns the string which is associated to this LearnModule. (fr) Renvoie le label associé à ce Module d'apprentissage (pour debug). */
  public String getLabel()
  {	 return label;  }
  /** Sets the string which is associated to this LearnModule. (fr) Attribue un label au Module d'apprentissage (pour debug). */
  public void setLabel(String l)
  {	// protected String last_ds;
	 label = l;
  }
  /** Generates the learning matrix which has 4 dimensions: perceptions(t-1), actions, perceptions(t+1), 2 values. */
  /*public float[][][][] makeMatrix(int m_p_prev,int m_action,int m_p_next,int m_nb_val) {
     // Matrice d'apprentissage
     // perceptions(t-1), actions, evolutions(t+1), 2 valeurs
      if ((m_p_prev<1)||(m_action<1)||(m_p_next<1)||(m_nb_val<1))
        { SetMActDefValues(); }
      this.m_p_prev=m_p_prev;
      this.m_action=m_action;
      this.m_p_next=m_p_next;
      this.m_nb_val=m_nb_val;
      return (new float[m_p_prev][m_action][m_p_next][m_nb_val]);
  }*/
  /** Generates the learning matrix which has 4 dimensions: perceptions(t-1), actions, perceptions(t+1), 2 values. */
  public float[][][] makeMatrix(int m_p_prev,int m_action,int m_nb_val) {
     // Matrice d'apprentissage
     // perceptions(t-1), actions, evolutions(t+1), 2 valeurs
      if ((m_p_prev<1)||(m_action<1)||(m_nb_val<1))
        { SetMActDefValues(); }
      this.m_p_prev=m_p_prev;
      this.m_action=m_action;
      
      this.m_nb_val=m_nb_val;
      return (new float[m_p_prev][m_action][m_nb_val]);
  }

  /** Sets default values to the matrix bounds / parameters */
  public void SetMActDefValues () {
     // Matrice d'apprentissage
     // perceptions(t-1), actions, evolutions(t+1), 2 valeurs
     m_p_prev=m_p_prev_def;
     m_action=m_action_def;
     //m_p_next=m_p_next_def;
     m_nb_val=m_nb_val_def;
  }

  /** Returns true if the values <i>n_p_prev, n_action, n_nb_val</i> don't exceed the matrix bounds */ 
  //Returns true if the values <i>n_p_prev, n_action, n_p_next, n_nb_val</i> don't exceed the matrix bounds
  private boolean isValuesConform(int n_p_prev,int n_action,/*int n_p_next,*/int n_nb_val)
  { if ((n_p_prev>=m_p_prev)||(n_action>=m_action)||/*(n_p_next>=m_p_next)||*/(n_nb_val>=m_nb_val)
      ||(n_p_prev<0)||(n_action<0)||/*(n_p_next<0)||*/(n_nb_val<0))
         {return false;}
    //else {
    return true; //}
  }
  
  /** Returns the value which is in MAct[n_p_prev][n_action][n_nb_val] */
  //Returns the value which is in MAct[n_p_prev][n_action][n_p_next][n_nb_val]
  public float GetValue(int n_p_prev,int n_action,/*int n_p_next,*/int n_nb_val)
  { if (isValuesConform( n_p_prev, n_action, /*n_p_next,*/ n_nb_val))
      {
        return MAct[n_p_prev][n_action]/*[n_p_next]*/[n_nb_val];
      }
    /*else
        {*/ System.out.println("LearnModule::GetValue=>mauvais paramètres");
          return -1;
        //}
  }
  /** Sets the value of MAct[n_p_prev][n_action][n_nb_val] to <i>value</i>*/
  // Sets the value of MAct[n_p_prev][n_action][n_p_next][n_nb_val] to <i>value</i>
  public boolean SetValue(int n_p_prev,int n_action,/*int n_p_next,*/int n_nb_val, float value)
  { if ((isValuesConform( n_p_prev, n_action, /*n_p_next,*/ n_nb_val))
        &&((n_nb_val==1)||(value<=max_value)&&(value>=min_value)))
      { MAct[n_p_prev][n_action]/*[n_p_next]*/[n_nb_val]=value;
        return true;
      }
    /*else
        {*/ System.out.println("LearnModule::SetValue=> mauvais paramètres("+n_p_prev+","+n_action+","/*+n_p_next+","*/+n_nb_val+","+value+")("+m_p_prev+","+m_action+/*","+m_p_next+*/","+m_nb_val+","+max_value+")");
          return false;
        //}
  }
  /* Originale (DEA) avec valeurs i<6 j<27
  public void AfficheMAct()
  {
        for (int k=0; k<3; k++)
        {
             for (int i=0;i<6;i++)
              for (int j=0;j<9;j++)
                {
                  if (j==0) System.out.print(i+" ");
                  System.out.print( ((double)((int)(MAct[i][j+(k*9)][0]*1000))) /1000);
                  System.out.print("/"+(int)MAct[i][j+(k*9)][1]+" ");
                  if (j==8) System.out.println();
                }
              System.out.println();
        }
  }*/
  
  /** Outputs on the standard output all the values contained in this matrix, also prints the label given to this object if not null*/
  public void AfficheMAct()
  {	     //int m_p_next_0=0; // Pour remplacer la composante m_p_next à zéro ds notre cas
  	     if (this.label!=null) {System.out.println("MAct de l'agent :"+this.label+".");}
             for (int i=0;i<m_p_prev;i++)
              for (int j=0;j<m_action;j++)
                {
                  if (j==0) System.out.print(i+" ");
                  System.out.print( ((double)((int)(MAct[i][j]/*[m_p_next_0]*/[0]*(float)1000))) /1000);
                  System.out.print("/"+(int)MAct[i][j]/*[m_p_next_0]*/[1]+" ");
                  if (j==(m_action-1)) System.out.println();
                }
              System.out.println();
  }
  
  /** Initialises the matrix values: MAct[i][j][k][0]=0.5 ; MAct[i][j][k][1]=1*/
  public void InitMAct()
  {	      //int m_p_next_0=0; // Pour remplacer la composante m_p_next à zéro ds notre cas
              for (int i=0;i<m_p_prev;i++)
              for (int j=0;j<m_action;j++)
                  {
                    MAct[i][j]/*[m_p_next_0]*/[0]=(float)0.5;
                    MAct[i][j]/*[m_p_next_0]*/[1]=1;
                  }
  }

public String itos(int n)
{ return ((new Integer(n)).toString()); }
public String ftos(float n)
{ return ((new Float(n)).toString()); }
public String dtos(double n)
{ return ((new Float(n)).toString()); }

public String[] parse(String text)
{
	String[] temp=null;
	temp = text.split("\\,");
	return temp;
}

public void load(BufferedReader dip)
{
	try
	  {	String s=null;
	  	String[] tmp=null;
		
		s=dip.readLine();//System.out.println(s);
		
		tmp=parse(s);
		m_p_prev = Integer.valueOf(tmp[0]).intValue();
		m_action = Integer.valueOf(tmp[1]).intValue();
		System.out.println("m_p_prev="+m_p_prev+" / m_action="+m_action);
		MAct=makeMatrix(m_p_prev,m_action,2); //m_nb_val);
		for (int i=0;i<m_p_prev;i++)
	            {
	              s=dip.readLine();
	              tmp=parse(s);
	              float tmpa,tmpb;
	              for (int j=0;j<m_action;j++)
	                { tmpa=Float.valueOf(tmp[j*2]).floatValue();
	                  tmpb=Float.valueOf(tmp[j*2+1]).floatValue();
	                  MAct[i][j][0]=tmpa;
	                  MAct[i][j][1]=tmpb;
	                  //System.out.print(tmpa+","+tmpb+",");
	                }
	                //System.out.println("(m_p_prev="+i+")");
	            }
		//afficheMact();
	  }
      catch (Exception eofe)
	  {
	      System.err.println("Load:"+eofe.getMessage());
	      eofe.printStackTrace();
	  }
	//MAct=makeMatrix(m_p_prev,m_action,2); //m_nb_val);
}

public void save(FileWriter fw)
  {
      if (!RmcLauncherApp.is_applet)
      try{
      	//String tmp;
	//m_p_prev,m_action,m_nb_val
	   fw.write(itos(m_p_prev)+","+itos(m_action)+"\n");
           for (int i=0;i<m_p_prev;i++)
            {
              for (int j=0;j<m_action;j++)
                {
                  //if (j==0) fw.write(i+";");
                  //System.out.print(MAct[i][j][0]+","+MAct[i][j][1]+",");
                  fw.write(ftos(MAct[i][j][0])+","+ftos(MAct[i][j][1]));
                  if (j<(m_action-1))
                  fw.write(",");
                }
                System.out.println("(m_p_prev="+i+")");
              fw.write("\n");
            }
           
          fw.close();
         }
      catch (Exception e)
      {	RmcLauncherApp.is_applet=true;
      	e.printStackTrace();
      }
    }

public void afficheMact()
  {
           for (int i=0;i<m_p_prev;i++)
            {
              for (int j=0;j<m_action;j++)
                {
                  //if (j==0) fw.write(i+";");
                  System.out.print(MAct[i][j][0]+","+MAct[i][j][1]);
                  //System.out.println(itos(i)+","+itos(j)+"\n");
                  //fw.write(ftos(MAct[i][j][0])+","+ftos(MAct[i][j][1]));
                  if (j<(m_action-1))
                  System.out.print(",");
                }
                System.out.println("(m_p_prev="+i+")");
            }
    } // public void afficheMact()

}