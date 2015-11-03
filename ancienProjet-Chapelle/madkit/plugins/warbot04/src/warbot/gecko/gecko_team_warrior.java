package warbot.gecko;

import warbot.kernel.*;
import java.util.Vector;



public class gecko_team_warrior extends Brain
{
/* Debut des constantes */

  int intervalle_entre_warriors = 40;



/* Fin des constantes */

//----------------------------------------------------------------------------//

/* Debut des variables */

  // Indique si le warrior est dans le cercle
  boolean DeplacementsCercleFinis=false;

  // Indique si le warrior est positionné en formation dans le cercle
  boolean DeplacementsFormationFinis=false;

  boolean attaque_termine = false;

  boolean bool_ini_etat_3;

  // Liste des rocket launchers
  Vector warriors = new Vector();
  int NbreWarrior=1;

  // Liste des explorateurs
  Vector spys = new Vector();
  int NbreSpy=0;

  // Liste des bases
  Vector fortresses = new Vector();
  int NbreFortress=0;

  // 0 = Création
  // 1 = Déplacement pour formation alignement
  // 2 = Déplacement pour formation à droite
  // 3 = Déplacement pour formation diagonal
  // 4 = Déplacement pour formation tout droit
  // 5 = Bloqué deplacement formation

  // 6 = Position formation atteinte
  // 7 = Déplacement pour recherche

  int etat=0;

  int compt = 0;

  // Indique le numero unique que contient le warrior apres la formation
  // en demi cercle (de 1 à NbreWarrior, en partant du "bout droit" de la formation)
  int classement=0;

  // Indique le nombre de warriors ayant fini leur deplacement
  // (On sait qu'ils sont à l'intérieur du cercle, il ne reste plus
  // qu'à les reorienter vers une trajection en formant un demi cercle)
  int NbreDeplacementsCercleFinis=0;

  // Indique le nombre de warriors ayant fini leur deplacement
  // pour la formation (derniere etape de la formation)
  int NbreDeplacementsFormationFinis=0;

  int NbreCycleBlocage=0;

  // Coordonnées relatives ideales par rapport au point moyen "du bout droit"
  // de la formation du demi cercle
  double destinationXCoinDroitFormation=0;
  double destinationYCoinDroitFormation=0;


  // Deplacement court terme
  double destinationXCourtTerme=0;
  double destinationYCourtTerme=0;

  // Centre du cercle de la formation
  double destinationXPointMoyen=0;
  double destinationYPointMoyen=0;

  // Indique vers où ira la meute une fois la 1ere formation effectué au debut de la partie
  // (destinationXPointMoyenFortress,destinationYPointMoyenFortress) -> (destinationXPointMoyen,destinationYPointMoyen)
  double destinationXPointMoyenFortress=0;
  double destinationYPointMoyenFortress=0;

  // Direction actuelle du warrior
  double direction=0;

  // Direction de la meute par rapport au centre du cercle de la formation
  double directionCentreFormation=0;

  // Rayon du demi cercle de la formation de la meute
  double rayon_formation=0;


/* Fin des variables */

//----------------------------------------------------------------------------//

/* Debut du code */

  public gecko_team_warrior(){
  }

  public void activate()
  {
    this.showUserMessage(true);
    createGroup(false,getTeam(),null,null);
    broadcast(getTeam(),"warrior","ExisteWarrior?","warrior");
    broadcast(getTeam(),"spy","ExisteSpy?","warrior");
    broadcast(getTeam(),"fortress","ExisteFortress?","warrior");
    requestRole(getTeam(),"warrior",null);

  }

  public void end()
  {
    broadcast(getTeam(),"warrior","WarriorMort");
    broadcast(getTeam(),"spy","WarriorMort");
    broadcast(getTeam(),"fortress","WarriorMort");
    println("ID Dead (me-warrior) : "+getAddress().getLocalID());
  }

// retourne un vecteur d'ennemis
      public Vector analyse(){
        double x;
        double y;
        double direction;
        Vector V = new Vector();
        Percept [] percepts = getPercepts();
        Percept p;
        for(int i=0 ; i< percepts.length ; i++){
          p = percepts[i];
          x = p.getX();
          y = p.getY();
          direction = towards(x,y);
          if(suis_je_un_ennemis(p) && verif_cible(x,y,p.getDistance())) V.add(p);
        }
        return V;
      }

      public boolean existe_il_ennemis(){
        if (analyse().size() == 0) {return false;}
        else return true;
      }


// determine si un percept est un ennemis ou pas
      public boolean suis_je_un_ennemis(Percept p){
        String equipe;
        equipe = p.getTeam();
        if(! equipe.equals("") &&  ! equipe.equals("gecko team")) return true;
        return false;
      }

// verifie si la direction de tir est correcte !
// l'idee etant d'eviter de s'entretuer !!!
      public boolean verif_cible(double xx , double yy , double d2){
        double x;
        double y;
        double coef;
        double res;
        double d1;
        Percept [] percepts = getPercepts();
        Percept p;
        int acc;
        for(int i=0 ; i<percepts.length ; i++){
          p = percepts[i];
          x = p.getX();
          y = p.getY();
          d1 = p.getDistance();
          coef = y/x;
          res = yy/xx;
          acc = (int) (10*coef) - (int) (10*res);
          if(acc<0) acc = acc*(-1);
          System.out.println("x = " + x + " xx = " + xx + " " + p.getTeam());
          if(acc<5 && x>0){
            if(p.getTeam().equals("B2")){
              return false;
            }
          }
        }
        return true;
      }

// determine une cible si le vecteur passer en parametre n'est pas vide
// le vecteur passe en parametre est un vecteur d'ennemis
// si une BASE ennemis se trouve parmis les cibles potentielles alors on choisit la 1ere
// si non on determine la cible la plus faible
      public double donne_cible(Vector V){
        double direction = 0;
        int energie;
        Percept p;
        double x;
        double y;
        if (V.size() == 0) return 0;
        p = (Percept) V.elementAt(0);
        x = p.getX();
        y = p.getY();
        direction = towards(x, y);
        if (p.getPerceptType().equals("Home")) return direction;
        energie = p.getEnergy();
        for (int i=1 ; i<V.size() ; i++){
          p = (Percept) V.elementAt(i);
          if (p.getPerceptType().equals("Home")){
            x = p.getX();
            y = p.getY();
            direction = towards(x,y);
            return direction;
          }
          if(p.getEnergy() < energie){
            x = p.getX();
            y = p.getY();
            direction = towards(x, y);
            energie = p.getEnergy();
          }
        }
        return direction;
      }

// arme et tire dans le cas ou l'agent a une cible
      public void tirer(double direction){
        if(direction != 0) {buildRocket();launchRocket(direction);}
      }

      public boolean suis_je_en_position_potentielle_attaque(){
        if (etat != 0 && etat != 1 && etat != 2) return true;
        return false;
      }

      public void tache1(){
        if (suis_je_en_position_potentielle_attaque()) {
          double cible = donne_cible(analyse());
          tirer(cible);
        }
      }


      public void attaque(){
        //System.out.println("etat = " + etat);
        Vector V;
        V = analyse();
        ///System.out.println("ggggggggggggggggggggggggggggggggggggggggggggggggggggggggggg");
        if (V.size() > 0) {
          double cible = donne_cible(analyse());
           broadcast(getTeam(), "warrior", "fight", getName());
          // je passe en position d'attaque
          //if(! (get_id_attaque() == 1 && etat == 6)) set_id_attaque(1);
          //etat = 0;//etat = 6;
          System.out.println("cible = " + cible + "yyyyyhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhyyyyyyyyyyyyyyyyyyyy");
          tirer(cible);
          //move();
          //return;
        }
        else {
          etat = 7;
          // je ne suis plus en position d'attaque
          //if(get_id_attaque() != 0) set_id_attaque(0);
          //main();
          //}
        }
      }
/*
      public int get_id_attaque(){
        gecko_team_robot p;
        for(int i=0 ; i<warriors.size() ;i++){
          p = (gecko_team_robot) warriors.get(i);
          if(p.get_ID().equals(getName())) return p.get_id_attaque();
        }
        return 999;
      }
*/
    // va modifier l'identifiant d'attaque du gecko_team_robot correspondant
      public void set_id_attaque(int i){
        gecko_team_robot R = recupere_identite();
        if(R != null) R.set_id_attaque(i);
        broadcast(getTeam(),"warrior","mise a jour d' attaque",getName(),Integer.toString(i));
      }

    // permet de recuperer son gecko_team_robot correspondant dans le vector warriors
    public gecko_team_robot recupere_identite(){
      System.out.println("rrrrrrrrrrrrrrrrrr");
      gecko_team_robot p;
      String ident;
      System.out.println("rrrrrrmmmmmmmmmmrrrrrrrrrrrr");
      if (warriors.size() == 0) return null;
      for (int i = 0; i < warriors.size(); i++) {
        System.out.println("rrrrrrrrrrrrjjjjjjjjrrrrrr");
        p = (gecko_team_robot) warriors.get(i);
        System.out.println("rrrrrrrrrrrrreeeeeeeeeeeerrrrr");
        ident = p.get_ID();
        System.out.println(i);
        if (getName().equals(ident)) {
          return p;
        }
      }
      return null;
    }


    // retourne vrai si aucun warrior n'est en position d'attaque, faux sinon
      public boolean foo_attaque(){
         gecko_team_robot R;
        for(int i=0 ; i<warriors.size() ; i++){
          R = (gecko_team_robot) warriors.get(i);
          if(R.get_id_attaque() == 1){
            return false;
          }
        }
        // ie personne n'est en position d'attaque
        return true;
      }

    // modifier son comportement si personne n'est en position d'attaque. ie retourner dans l'etat 0 !
      public void influer_comportement(){
        if (foo_attaque() == true) {
          etat = 0;
          //showUserMessage("yes");
        }
        //else main();
      }





        private void gestion_messages()

        {
          WarbotMessage wm;
          gecko_team_robot r;

          while (!isMessageBoxEmpty()) {
            wm = readMessage();
            if (wm.getAct().equals("RAZPositions")) {

              etat = 8;

              Vector warriors = new Vector();
              NbreWarrior = 1;

              Vector spys = new Vector();
              NbreSpy = 0;

              Vector fortresses = new Vector();
              NbreFortress = 0;

              break;

            }

            if (wm.getAct().equals("ExisteWarrior?")) {
              send(wm.getSender(), "WarriorExiste");
              if (wm.getArg1().equals("warrior")) {
                if (!wm.getSender().getLocalID().equals(getAddress().getLocalID())) {
                  NbreWarrior++;
                  r = new gecko_team_robot();
                  r.positionX = wm.getFromX();
                  r.positionY = wm.getFromY();
                  r.ID = wm.getSender().getLocalID();
                  warriors.add(r);
                }
              }
              else if (wm.getArg1().equals("spy")) {
                NbreSpy++;
                r = new gecko_team_robot();
                r.positionX = wm.getFromX();

                r.positionY = wm.getFromY();
                r.ID = wm.getSender().getLocalID();
                spys.add(r);
              }
              else if (wm.getArg1().equals("fortress")) {

                NbreFortress++;
                r = new gecko_team_robot();
                r.positionX = wm.getFromX();
                r.positionY = wm.getFromY();
                r.ID = wm.getSender().getLocalID();
                fortresses.add(r);
              }
            }
            else if (wm.getAct().equals("WarriorExiste")) {
              if (!wm.getSender().getLocalID().equals(getAddress().getLocalID())) {
                NbreWarrior++;
                r = new gecko_team_robot();
                r.positionX = wm.getFromX();
                r.positionY = wm.getFromY();
                r.ID = wm.getSender().getLocalID();
                warriors.add(r);
              }
            }
            else if (wm.getAct().equals("WarriorMort"))

            {
              NbreWarrior--;
              for (int i = 0; i < spys.size(); i++) {
                r = (gecko_team_robot) spys.get(i);
                if (r.ID.equals(wm.getSender().getLocalID())) {
                  println("ID Dead (warrior) : " + r.ID);
                  warriors.remove(i);
                  break;
                }
              }
            }
            else if (wm.getAct().equals("SpyExiste")) {
              NbreSpy++;
              r = new gecko_team_robot();
              r.positionX = wm.getFromX();
              r.positionY = wm.getFromY();
              r.ID = wm.getSender().getLocalID();
              spys.add(r);
            }
            else if (wm.getAct().equals("SpyMort")) {
              NbreSpy--;
              for (int i = 0; i < spys.size(); i++) {
                r = (gecko_team_robot) spys.get(i);
                if (r.ID.equals(wm.getSender().getLocalID())) {
                  println("ID Dead (spy) : " + r.ID);
                  spys.remove(i);
                  break;
                }
              }
            }
            else if (wm.getAct().equals("FortressExiste")) {
              NbreFortress++;
              r = new gecko_team_robot();
              r.positionX = wm.getFromX();
              r.positionY = wm.getFromY();
              r.ID = wm.getSender().getLocalID();
              fortresses.add(r);
            }
            else if (wm.getAct().equals("FortressMort")) {
              NbreFortress--;
              for (int i = 0; i < fortresses.size(); i++) {
                r = (gecko_team_robot) spys.get(i);
                if (r.ID.equals(wm.getSender().getLocalID())) {
                  println("ID Dead (fortress) : " + r.ID);
                  fortresses.remove(i);
                  break;
                }
              }
            }
            else if (wm.getAct().equals("DeplacementCercleFini")) {
              // modif Rémy */
             if (!wm.getSender().getLocalID().equals(getAddress().getLocalID()))
             // fin modif Rémy */
            {
              NbreDeplacementsCercleFinis++;
            }
            }
            else if (wm.getAct().equals("DeplacementFormationFini")) {
              /* modif Rémy */
              if (!wm.getSender().getLocalID().equals(getAddress().getLocalID()))
              /* fin modif Rémy */
              {
                NbreDeplacementsFormationFinis++;
              }
            }
            else if (wm.getAct().equals("DeplacementObstacle")) {
              /* modif Rémy */
              if (!wm.getSender().getLocalID().equals(getAddress().getLocalID()))
              /* fin modif Rémy */
              {
                NbreDeplacementsCercleFinis = 0;
                NbreDeplacementsFormationFinis = 0;
                setName("");
                DeplacementsCercleFinis = false;
                bool_ini_etat_3 = false;
                etat = 2;

                double ratio = rayon_formation /
                    Math.sqrt( (rayon_formation *
                                Math.cos(Math.toRadians(270 -
                                                        directionCentreFormation)) *
                                rayon_formation *
                                Math.cos(Math.toRadians(270 -
                                                        directionCentreFormation))) +
                              (rayon_formation *
                               Math.sin(Math.toRadians(270 - directionCentreFormation)) *
                               rayon_formation *
                               Math.sin(Math.toRadians(270 - directionCentreFormation))));
                directionCentreFormation = (directionCentreFormation + 90) % 360;

                // Coordonnées du point d'arrêt de la rotation par rapport au centre

                double tmpX = (rayon_formation *
                               Math.cos(Math.toRadians(270 - directionCentreFormation))) *
                    ratio;
                double tmpY = (rayon_formation *
                               Math.sin(Math.toRadians(270 - directionCentreFormation))) *
                    ratio;

                println("Rayon de la formation " + Double.toString(rayon_formation));

                println("tmpX" + Double.toString(tmpX));
                println("tmpY" + Double.toString(tmpY));

                double ratio2 = rayon_formation /
                    Math.sqrt( ( (tmpX + destinationXPointMoyen) *
                                (tmpX + destinationXPointMoyen)) +
                              ( (tmpY + destinationYPointMoyen) *
                               (tmpY + destinationYPointMoyen)));

                destinationXCoinDroitFormation = (tmpX + destinationXPointMoyen) *
                    ratio2;

                destinationYCoinDroitFormation = (tmpY + destinationYPointMoyen) *
                    ratio2;

                println("destinationXCoinDroitFormation " +
                        Double.toString(destinationXCoinDroitFormation));
                println("destinationYCoinDroitFormation " +
                        Double.toString(destinationYCoinDroitFormation));

              }
            }

	    else if(wm.getAct().equals("DirectionBaseEnnemie"))
		 {
		   //if(!wm.getSender().getLocalID().equals(getAddress().getLocalID()))

		   //{
		   //println("Dir base reçu");
		   if (etat == 5) {
		     NbreDeplacementsCercleFinis = 0;
		     NbreDeplacementsFormationFinis = 0;
		     setName("");
		     DeplacementsCercleFinis = false;
		     bool_ini_etat_3 = false;
		     etat = 2;

		     //directionCentreFormation=Double.parseDouble(wm.getArg2())+Double.parseDouble(wm.getArg1());
		     directionCentreFormation = towards(wm.getFromX(), wm.getFromY());
		     double ratio = rayon_formation /
			 Math.sqrt( (rayon_formation *
				     Math.cos(Math.toRadians(360 - directionCentreFormation)) *
				     rayon_formation *
				     Math.cos(Math.toRadians(360 - directionCentreFormation))) +
				   (rayon_formation *
				    Math.sin(Math.toRadians(360 - directionCentreFormation)) *
				    rayon_formation *
				    Math.sin(Math.toRadians(360 - directionCentreFormation))));

		     // Coordonnées du point d'arrêt de la rotation par rapport au centre

		     double tmpX = (rayon_formation *
				    Math.cos(Math.toRadians(360 - directionCentreFormation))) *
			 ratio;
		     double tmpY = (rayon_formation *
				    Math.sin(Math.toRadians(360 - directionCentreFormation))) *
			 ratio;

		     //println("Rayon de la formation "+Double.toString(rayon_formation));

		     //println("tmpX"+Double.toString(tmpX));
		     //println("tmpY"+Double.toString(tmpY));

		     double ratio2 = rayon_formation /
			 Math.sqrt( ( (tmpX + destinationXPointMoyen) *
				     (tmpX + destinationXPointMoyen)) +
				   ( (tmpY + destinationYPointMoyen) *
				    (tmpY + destinationYPointMoyen)));

		     destinationXCoinDroitFormation = (tmpX + destinationXPointMoyen) *
	   ratio2;

		     destinationYCoinDroitFormation = (tmpY + destinationYPointMoyen) *
	   ratio2;

		     //println("destinationXCoinDroitFormation " + Double.toString(destinationXCoinDroitFormation));
		     //println("destinationYCoinDroitFormation " + Double.toString(destinationYCoinDroitFormation));
		     //}
		     // }
		   }
		 }

/*
            else if (wm.getAct().equals("mise a jour d' attaque")) {
              if (!wm.getSender().getLocalID().equals(getAddress().getLocalID())) {
                String id = wm.getArg1();
                int acc = Integer.parseInt(wm.getArg2());
                for (int i = 0; i < warriors.size(); i++) {
                  if ( ( ( (gecko_team_robot) warriors.get(i)).get_ID()).equals(id)) {
                    ( (gecko_team_robot) warriors.get(i)).set_id_attaque(acc);
                  }
                }
              }
            }
*/
            else if (wm.getAct().equals("attaque termine")) {
              if (!wm.getSender().getLocalID().equals(getAddress().getLocalID())) {
                //if(etat != 7) {compt++;}
                compt++;
              }
            }

            else if (wm.getAct().equals("je revients")) {
              if (!wm.getSender().getLocalID().equals(getAddress().getLocalID())) {
               //if(etat == 7){ compt--;}
               compt--;
              }
            }
            else if (wm.getAct().equals("il y a une attaque")) {
              if (!wm.getSender().getLocalID().equals(getAddress().getLocalID())) {
                etat=6;
              }
            }
            /*
            else if (wm.getAct().equals("fight")) {
              println("fight           rrrrrrrrrrrrr");
              if (!wm.getSender().getLocalID().equals(getAddress().getLocalID())) {
                if (etat < 6) {
                  etat = 6;
                }
                //setHeading(towards(wm.getFromX(),wm.getFromY()));
                //move();
                return;
              }
            }
          */}
        }
























         public void doIt()
         {



           gecko_team_robot r;

           gestion_messages();

           setUserMessage("D "+Double.toString(direction)+" compt "+Integer.toString(compt)+",etat "+etat);

          if(etat==0)
          {

	    if(existe_il_ennemis()) {
	      etat = 6;
	      broadcast(getTeam(), "warrior", "il y a une attaque", getName());
	      return;
	    }

            destinationXCoinDroitFormation = 0;
            destinationYCoinDroitFormation = 0;

            destinationXPointMoyenFortress=0;
            destinationYPointMoyenFortress=0;

            destinationXPointMoyen=0;

            destinationYPointMoyen=0;

            rayon_formation=0;

            DeplacementsCercleFinis=false;
            DeplacementsFormationFinis=false;

            NbreDeplacementsCercleFinis=0;
            NbreDeplacementsFormationFinis=0;

            setName("");

            directionCentreFormation=0;

            bool_ini_etat_3=true;

            // Calcul du point moyen, centre de la formation
            for(int i = 0; i < warriors.size(); i++)

            {
              r = (gecko_team_robot) warriors.get(i);
              destinationXPointMoyen += r.positionX;
              destinationYPointMoyen += r.positionY;
            }

            destinationXPointMoyen/=NbreWarrior;
            destinationYPointMoyen/=NbreWarrior;

            // Calcul du rayon de la formation
            if(NbreWarrior<2)
            {
              rayon_formation=0;
            }
            else if (NbreWarrior<3)
            {
              if(intervalle_entre_warriors<40)
              {
                rayon_formation = 40;
              }
              else
              {
                rayon_formation = intervalle_entre_warriors;
              }
            }
            else
            {
              rayon_formation = (intervalle_entre_warriors/2) / (Math.sin(Math.toRadians(90.0 / (NbreWarrior-1))));
            }

            // Calcul du point moyen des Fortress qui nous servira au depart pour savoir vers où aller
            // (dans la direction opposée)
            for(int i = 0; i < fortresses.size(); i++)
            {

              r = (gecko_team_robot) fortresses.get(i);
              destinationXPointMoyenFortress += r.positionX;
              destinationYPointMoyenFortress += r.positionY;
            }

            if(NbreFortress>0)
            {
              destinationXPointMoyenFortress/=NbreFortress;
              destinationYPointMoyenFortress/=NbreFortress;
            }

            // Cas particulier où le point moyen des Fortresses et des Warriors est le meme (cas TRES rare, mais bon, on le gère...)

            if (destinationXPointMoyenFortress == destinationXPointMoyen && destinationYPointMoyenFortress == destinationYPointMoyen)
            {

              destinationXPointMoyenFortress = -rayon_formation;
              destinationYPointMoyenFortress = 0;
            }

            // Calcul de la destination ideale une fois tous les warriors dans le contour du cercle de formation
            // (orientation dans le cercle de façon à former un demi cercle)
            // Attention, il s'agit d'une destination relative par rapport au centre du cercle
            // mais de toute façon, les coordonnées du centre sont calculées à chaque tour
            // Ce point ideal est à 270 degrés (sens des aiguilles d'une montre) du point de deplacement
            // car les warriors vont se deplacer sur ce cercle dans le sens des aiguilles d'une montre
            // jusqu'à ce qu'un warrior rencontre ce point ideal et s'arrete. Les autres seront bloqués
            // par ce dernier et ainsi vont former le demi cercle.
            destinationXCoinDroitFormation=destinationYPointMoyen-destinationYPointMoyenFortress;
            destinationYCoinDroitFormation=destinationXPointMoyenFortress-destinationXPointMoyen;
            // On ramene cette destination au contour du cercle
            double ratio=rayon_formation/Math.sqrt(destinationXCoinDroitFormation*destinationXCoinDroitFormation+destinationYCoinDroitFormation*destinationYCoinDroitFormation);
            destinationXCoinDroitFormation*=ratio;
            destinationYCoinDroitFormation*=ratio;

            // Direction de déplacement une fois la formation terminée
            directionCentreFormation=towards(destinationXPointMoyen-destinationXPointMoyenFortress,destinationYPointMoyen-destinationYPointMoyenFortress);

            // Calcul de la distance entre le point moyen (centre du cercle de la formation) et le warrior
            // pour determiner la direction à prendre
            double distance=Math.sqrt(destinationXPointMoyen*destinationXPointMoyen+destinationYPointMoyen*destinationYPointMoyen);

            etat=1;

            direction=towards(destinationXPointMoyen,destinationYPointMoyen);

            // On determine si l'on doit s'eloigner ou se rapprocher du centre
            // (si on est à l'intérieur ou à l'exterieur du cercle de la formation)
            if(distance-1<rayon_formation)
            {
              direction+=180;

              direction=direction%360;
              setHeading(direction);
            }
            else if(distance+1>rayon_formation)
            {
              setHeading(direction);
            }
            else
            {
              return;
            }
          }//Fin de l'état 0

          else if (etat==1)
          {

	    if(existe_il_ennemis()) {
	      etat = 6;
	      broadcast(getTeam(), "warrior", "il y a une attaque", getName());
	      return;
	    }


	    boolean direction_vers_centre=true;
	    boolean direction_droite=true;
	    boolean direction_haut=true;
	    boolean direction_gauche=true;

            double direction_perimetre=0;

            int deplacement=getCoveredDistance();
            if(deplacement>0)
            {
              destinationXPointMoyen-=deplacement*Math.cos(Math.toRadians(direction));
              destinationYPointMoyen-=deplacement*Math.sin(Math.toRadians(direction));
            }

	    double distance=Math.sqrt(destinationXPointMoyen*destinationXPointMoyen+destinationYPointMoyen*destinationYPointMoyen);

	    if(distance-1<rayon_formation)
	    {
	       direction_perimetre=(360+towards(destinationXPointMoyen,destinationYPointMoyen)+180)%360;
	    }
	    else
	    {
	       direction_perimetre=towards(destinationXPointMoyen,destinationYPointMoyen);
	    }

/*	    if(Math.abs((direction%360)-((360+direction_perimetre+180)%360))<90)
	    {
	      direction_vers_centre=false;
	    }
*/

	    Percept[] perceptions=getPercepts();
	    if (perceptions.length>0)
	    {
	      for (int i = 0; i < perceptions.length; i++)
	      {
		Percept perception = perceptions[i];
		double distance_percept=Math.sqrt(perception.getX()*perception.getX()+perception.getY()*perception.getY());

		if (distance_percept-1<=intervalle_entre_warriors)
		{
		  if(Math.abs(360+direction_perimetre-towards(perception.getX(),perception.getY()))%360<60 || Math.abs(360+direction_perimetre-towards(perception.getX(),perception.getY()))%360>300)
		  {
		    direction_vers_centre=false;
		  }
		  else if(Math.abs(360+direction_perimetre+270-towards(perception.getX(),perception.getY()))%360<60  || Math.abs(360+direction_perimetre+270-towards(perception.getX(),perception.getY()))%360>300)
		  {
		    direction_droite=false;
		  }
		  else if(Math.abs(360+direction_perimetre+180-towards(perception.getX(),perception.getY()))%360<60  || Math.abs(360+direction_perimetre+180-towards(perception.getX(),perception.getY()))%360>300)
		  {
		    direction_haut=false;
		  }
		  else if(Math.abs(360+direction_perimetre+90-towards(perception.getX(),perception.getY()))%360<60  || Math.abs(360+direction_perimetre+90-towards(perception.getX(),perception.getY()))%360>300)
		  {
		    direction_gauche=false;
		  }
	        }

	      }

	    }



	    double distance2=Math.sqrt(destinationXPointMoyen*destinationXPointMoyen+destinationYPointMoyen*destinationYPointMoyen);
	    if(distance2-1<=rayon_formation && distance2+1>=rayon_formation)
	    {
	      etat=2;
	      return;
	    }

	    if(direction_vers_centre)
	    {
	      direction=direction_perimetre;
	    }
	    else if(direction_droite)
	    {
	      direction=(direction_perimetre+270)%360;
	    }
	    else if(direction_haut)
	    {
	      direction=(direction_perimetre+180)%360;
	    }
	    else if(direction_haut)
	    {
	      direction=(direction_perimetre+90)%360;
	    }
	    else
	    {
	      randomHeading();
	      direction=getHeading();
            }

            setHeading(direction);

          }
          else if(etat==2)
          {

	    if(existe_il_ennemis()) {
	      etat = 6;
	      broadcast(getTeam(), "warrior", "il y a une attaque", getName());
	      return;
	    }


            if(!DeplacementsCercleFinis)
            {
              DeplacementsCercleFinis=true;

              NbreDeplacementsCercleFinis++;

              println("DeplacementCercleFini");

              broadcast(getTeam(),"warrior","DeplacementCercleFini",getAddress().getLocalID());
              broadcast(getTeam(),"spy","DeplacementCercleFini",getAddress().getLocalID());
              broadcast(getTeam(),"fortress","DeplacementCercleFini",getAddress().getLocalID());

            }

            // Est ce que tout le monde est dans le cercle ?
            if(NbreDeplacementsCercleFinis>=NbreWarrior)
            {
              etat = 3;
            }

            return;
          }//Fin etat 2

          else if(etat==3)
          {
	    if(existe_il_ennemis()) {
	      etat = 6;
	      broadcast(getTeam(), "warrior", "il y a une attaque", getName());
	      return;
	    }

            //println("etat3");
            //println(getAddress().getLocalID());

            int deplacement=getCoveredDistance();
            if(deplacement>0)
            {
              destinationXPointMoyen-=deplacement*Math.cos(Math.toRadians(direction));
              destinationYPointMoyen-=deplacement*Math.sin(Math.toRadians(direction));
            }

            // Cas où le warrior a atteint le "bout droit" ideal de la formation en demi cercle
            if(destinationXPointMoyen+destinationXCoinDroitFormation<2 && destinationXPointMoyen+destinationXCoinDroitFormation>-2 && destinationYPointMoyen+destinationYCoinDroitFormation<2 && destinationYPointMoyen+destinationYCoinDroitFormation>-2)
            {
              println(getAddress().getLocalID() + " --> " + Double.toString(destinationXPointMoyen) + " " + Double.toString(destinationYPointMoyen));
              println( " --> " + Double.toString(destinationXCoinDroitFormation) + " " + Double.toString(destinationYCoinDroitFormation));
              DeplacementsFormationFinis=false;
              etat=4;
              return;
            }

            if(bool_ini_etat_3)
            {
              direction=towards(destinationXPointMoyen,destinationYPointMoyen)+Math.toDegrees(Math.acos(1/rayon_formation));
              setHeading(direction);
            }
            else
            {
              direction=towards(destinationXPointMoyen,destinationYPointMoyen)-Math.toDegrees(Math.acos(1/rayon_formation));
              setHeading(direction);
            }

            Percept[] perceptions=getPercepts();
            if (perceptions.length>0)
            {

              for (int i = 0; i < perceptions.length; i++)
              {
                Percept perception = perceptions[i];
                double distance_percept=Math.sqrt(perception.getX()*perception.getX()+perception.getY()*perception.getY());

                if (perception.getPerceptType().equals("RocketLauncher") && perception.getTeam().equals(getTeam()) && distance_percept-1<=intervalle_entre_warriors)
                {
      //modif Rémy
                  if(Math.abs(direction-towards(perception.getX(),perception.getY()))<=90 || Math.abs(direction-towards(perception.getX(),perception.getY()))>=270)// || direction-towards(perception.getX(),perception.getY())<=(-90))
      // fin modif Rémy
                  {

                    //println(getAddress().getLocalID()+"Amie dans le sens de la marche!");
                    //println("ma dir : "+direction);
                    //println("dir de mon amie :"+towards(perception.getX(),perception.getY()));

                    if (!perception.getAgent().getName().equals(""))
                    {
                      //Cas où il est à la place prévue!
                      //println("Devant moi, il y a " + perception.getAgent().getName() +
                             // " à une distance de " +
                              //Double.toString(perception.getDistance()));
                      DeplacementsFormationFinis = false;
                      etat = 4;
                      //println(getAddress().getLocalID()+"Mais il est positionné, je ne bouge plus, j'attends la synchro !");
                      return;
                    }
                    else
                    {
                      //println(getAddress().getLocalID()+"Mais il n'est pas positionner, je ne bouge pas!");
                      return;

                    }
                  }
                  else
                  {
                    //println(getAddress().getLocalID()+"Aucun amie dans le sens de marche ! Je bouge vers mon objectif !");
                    move();
                    return;
                  }

                }
                else
                {
                  //println(getAddress().getLocalID()+"Auncun amie assez proche en vue, je trace !");
                  move();
                  return;
                }
              }

            }
          }//Fin de l'état 3




          else if(etat==4)
          {
            //println("etat4");
            if(!DeplacementsFormationFinis)
            {
              DeplacementsFormationFinis=true;

              classement=++NbreDeplacementsFormationFinis;
              setName(Integer.toString(classement));

              //println("DeplacementsFormationFinis, je suis n°"+getName());

              broadcast(getTeam(),"warrior","DeplacementFormationFini",getAddress().getLocalID());
              broadcast(getTeam(),"spy","DeplacementFormationFini",getAddress().getLocalID());
              broadcast(getTeam(),"fortress","DeplacementFormationFini",getAddress().getLocalID());


            }

            if(NbreDeplacementsFormationFinis>=NbreWarrior)
            {
              etat = 5;
            }

            return;

          }//Fin de l'état 4

          else if(etat==5)
          {
            compt = 0;
            attaque_termine = false;

            if(existe_il_ennemis()) {
              etat = 6;
              broadcast(getTeam(), "warrior", "il y a une attaque", getName());
              return;
            }

            Percept[] perceptions=getPercepts();
            if (perceptions.length>0)
            {
              for (int i = 0; i < perceptions.length; i++) {
                Percept perception = perceptions[i];

                if ((perception.getPerceptType().equals("Obstacle")) || (perception.getPerceptType().equals("Home")))
                {
                  if(Math.abs(directionCentreFormation-towards(perception.getX(),perception.getY()))<=90 || Math.abs(directionCentreFormation-towards(perception.getX(),perception.getY()))>=270)
                  {
                  NbreDeplacementsCercleFinis=0;
                  NbreDeplacementsFormationFinis=0;
                  setName("");
                  DeplacementsCercleFinis=false;
                  bool_ini_etat_3=false;
                  etat=2;

                  double ratio=rayon_formation/Math.sqrt((rayon_formation*Math.cos(Math.toRadians(270-directionCentreFormation))*rayon_formation*Math.cos(Math.toRadians(270-directionCentreFormation)))+(rayon_formation*Math.sin(Math.toRadians(270-directionCentreFormation))*rayon_formation*Math.sin(Math.toRadians(270-directionCentreFormation))));

                  // Def la nouvelle dir
                  directionCentreFormation=(directionCentreFormation+90)%360;

                  // Def les coord du pt d'arrêt de la rotation par rapport au centre de la formation
                  double tmpX=(rayon_formation*Math.cos(Math.toRadians(270-directionCentreFormation)))*ratio;
                  double tmpY=(rayon_formation*Math.sin(Math.toRadians(270-directionCentreFormation)))*ratio;

                  double ratio2=rayon_formation/Math.sqrt(((tmpX+destinationXPointMoyen)*(tmpX+destinationXPointMoyen))+((tmpY+destinationYPointMoyen)*(tmpY+destinationYPointMoyen)));

                  //Calcul les coord du pt d'arrêt de la rotation en fonction de la position de l'agent
                  destinationXCoinDroitFormation=(tmpX+destinationXPointMoyen)*ratio2;
                  destinationYCoinDroitFormation=(tmpY+destinationYPointMoyen)*ratio2;

                 // Envoie les messages pour la réorientation
                  broadcast(getTeam(),"warrior","DeplacementObstacle",Double.toString(directionCentreFormation));
                  broadcast(getTeam(),"spy","DeplacementObstacle",Double.toString(directionCentreFormation));
                  broadcast(getTeam(),"fortress","DeplacementObstacle",Double.toString(directionCentreFormation));

                  return;
                  }
                }
              }
            }

            direction=directionCentreFormation;
            setHeading(direction);
            println("Go !");

          }//Fin de l'état 5


          else if(etat == 6) {
              attaque();
              return;
            } //Fin de l'etat 6


            else if(etat==7){
              if(existe_il_ennemis()) {
               etat = 6;
               attaque_termine = false;
               compt--;
               broadcast(getTeam(), "warrior", "je revients", getName());
               return;
             }

              if (!attaque_termine) {
                compt++;
                broadcast(getTeam(), "warrior", "attaque termine", getName());
                attaque_termine = true;
              }


              if (compt >= NbreWarrior){

                broadcast(getTeam(), "warrior", "RAZPositions", "warrior");

                return;
              }
            }
            else if(etat==8)
            {
              broadcast(getTeam(), "warrior", "WarriorExiste", "warrior");
              broadcast(getTeam(), "spy", "WarriorExiste", "warrior");
              broadcast(getTeam(), "fortress", "WarriorExiste", "warrior");

              etat=0;

            }

            move();

          }


      }







