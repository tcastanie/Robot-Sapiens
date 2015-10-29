package kernelsim;

//import java.awt.*;

// Olivier Simonin v1.0
//

public class tache {

    public boolean a; // tache active ou non
    public float i; // intensite d'activation
    public float iref; // val initiale de i

    public tache()
    {
     a=false;
     i=0;
    }

    public void init(double v)
    {
	i=(float)v;
	iref=(float)v;
    }

}

