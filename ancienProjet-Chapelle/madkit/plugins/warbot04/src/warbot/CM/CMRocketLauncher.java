package warbot.CM;

import warbot.kernel.*;

public class CMRocketLauncher extends Brain
{
	String groupName = "warbot-";
	String roleName  = "Launcher";

	int tempsMax		= 10;
	int temps			= tempsMax;	// variable permettant de garder la meme direction pendant tempsMax itérations

	public CMRocketLauncher(){}

	public void activate()
	{
		groupName=groupName+getTeam();  // -> warbot-CM
		randomHeading();  				// direction aléatoire
		println("RocketLauncher CM opérationnel");
		createGroup(false,groupName,null,null);
		requestRole(groupName,roleName,null);
		requestRole(groupName,"mobile",null);
	}
	
	public void desobstination()
	{
		//
		if (!isMoving())	// blocage
		{
			if(temps != tempsMax )
			{
				temps = tempsMax;	
			}
			randomHeading();
			temps --;
			move();
			return;
		}
		if(temps < tempsMax && temps > 0)	// direction aléatoire a déjà été prise	
		{
			temps --;
			move();
			return;
		}
		if(temps == 0)	// temps de désobstination écoulé
		{
			temps = tempsMax;
		}		
	}

	public void gestionTir(double directionTir,int tailleTabAmis,double[][] tabAmis)	// pour éviter de tirer sur ses amis
	{
		println("gestion du tir");
		println("direction du tir :"+Double.toString(directionTir));
		for(int i=0;i<tailleTabAmis;i++)
		{
			println("entrée dans le tableau");
			println("direction de l'ami : "+Double.toString(towards(tabAmis[0][i],tabAmis[1][i])));
			if((directionTir-towards(tabAmis[0][i],tabAmis[1][i])>0 && directionTir-towards(tabAmis[0][i],tabAmis[1][i])<20) || (towards(tabAmis[0][i],tabAmis[1][i])-directionTir>0 && towards(tabAmis[0][i],tabAmis[1][i])-directionTir<20) || (directionTir-towards(tabAmis[0][i],tabAmis[1][i])>340) || (towards(tabAmis[0][i],tabAmis[1][i])-directionTir>340))
			{
				println("desobstination");
				desobstination();
				return;
			}
		}
		println("pas desobstination");
		launchRocket(directionTir);	
	}

	public void doIt()
	{		
		// variables pour gestion des messages
		double[][] tabAtaq			= new double[4][100];	// tableau des message d'attaque : première colonne contient l'X, deuxième colonne contient l'Y de la base
		double[][] tabHelpBL		= new double[4][100];	// tableau des message d'aide venant des bases concernant des launchers : première colonne X de l'ennemi si non vide, deuxième colonne Y de l'ennemi si non vide, troisième colonne X envoyeur, quatrième colonne Y envoyeur
		double[][] tabHelpBE		= new double[4][100];	// tableau des message d'aide venant des bases concernant des explorers : première colonne X de l'ennemi si non vide, deuxième colonne Y de l'ennemi si non vide, troisième colonne X envoyeur, quatrième colonne Y envoyeur
		double[][] tabHelpE			= new double[4][100];	// tableau des message d'aide venant des explorers : première colonne X de l'ennemi si non vide, deuxième colonne Y de l'ennemi si non vide, troisième colonne X envoyeur, quatrième colonne Y envoyeur
		double[][] tabHelpL			= new double[4][100];	// tableau des message d'aide venant des launchers : première colonne X de l'ennemi si non vide, deuxième colonne Y de l'ennemi si non vide, troisième colonne X envoyeur, quatrième colonne Y envoyeur
		int comptAtaq 				= 0;					// compteur du tableau tabAtaq
		int comptHelpBL				= 0;					// compteur du tableau tabHelpBL
		int comptHelpBE				= 0;					// compteur du tableau tabHelpBE
		int comptHelpE 				= 0;					// compteur du tableau tabHelpE
		int comptHelpL 				= 0;					// compteur du tableau tabHelpL
		int tailleAtaq				= 0;					// taille finale des différents tableaux
		int tailleHelpBL			= 0;					// taille finale des différents tableaux
		int tailleHelpBE			= 0;					// taille finale des différents tableaux
		int tailleHelpE				= 0;					// taille finale des différents tableaux
		int tailleHelpL				= 0;					// taille finale des différents tableaux
		// variable pour gestion des objets perçus
		double[][] tabMyTeam		= new double[2][50];	// tableau contenant les coordonnées relatives des membres de notre équipe
		double[] tabLauncher		= new double[4];		// tableau contenant les coordonnées relatives, l'énergie et la distance du launcher ennemi ayant le moins d'énergie tout en étant le plus proche
		double[] tabExplorer		= new double[4];		// tableau contenant les coordonnées relatives, l'énergie et la distance de l'explorer ennemi ayant le moins d'énergie tout en étant le plus proche
		double[] tabBase			= new double[4];		// tableau contenant les coordonnées relatives, l'énergie et la distance de la base ennemie ayant le moins d'énergie tout en étant la plus proche
		int comptMyTeam				= 0;					// compteur du tableau tabMyTeam
		int tailleMyTeam			= 0;					// taille finale du tableau tabMyTeam
		// initialisation de l'énergie et de la distance dans les tableaux des ennemis perçus
		tabLauncher[2]				= 0;
		tabLauncher[3]				= 0;
		tabExplorer[2]				= 0;
		tabExplorer[3]				= 0;
		tabBase[2]					= 0;
		tabBase[3]					= 0;
		// variables de gestion des distances
		int distanceEnnemi			= 80;
		int distanceAmis 			= 80;				// distance à respecter pour éviter obstination rejoindre amis (cas Y)
		int distanceMinAmis 		= 30;				// distance au delà de laquelle il ne faut pas s'approcher des amis (cas Y)
		// variables pour l'envoi de messages
		String actMessageAide 		= "HELP-L";			// cas où risque d'attaque ennemie
		String actMessageAttaque	= "ATAQ";			// cas de détection de la base ennemie
		String argMessageX 			= "";				// pour envoyer position relative en X de l'ennemi
		String argMessageY 			= "";				// pour envoyer position relative en Y de l'ennemi
		WarbotMessage messCourant	= null;
		// variables diverses
		double directionX			= 0;
		double directionY			= 0;
		int seuilEnergieBase		= 6000;				// énergie seuil pour "se défendre" vs "se sacrifier"
		
		// récupération et classement des messages
		while((messCourant = readMessage())!= null)
		{
			// message d'attaque de base ennemie : les coordonnées sont toujours présentes en argument du message
			if(messCourant.getAct() != null && messCourant.getAct() == "ATAQ")
			{
				tabAtaq[0][comptAtaq] = Double.valueOf(messCourant.getArg1()).doubleValue();
				tabAtaq[1][comptAtaq] = Double.valueOf(messCourant.getArg2()).doubleValue();
				tabAtaq[2][comptAtaq] = messCourant.getFromX();
				tabAtaq[3][comptAtaq] = messCourant.getFromY();
				comptAtaq++;
			}
			// message d'aide d'une base concernant les launchers : les coordonnées de l'ennemi sont toujours présentes en argument du message
			if(messCourant.getAct() != null && messCourant.getAct() == "HELP-BL")
			{
				tabHelpBL[0][comptHelpBL] = Double.valueOf(messCourant.getArg1()).doubleValue();
				tabHelpBL[1][comptHelpBL] = Double.valueOf(messCourant.getArg2()).doubleValue();	
				tabHelpBL[2][comptHelpBL] = messCourant.getFromX();
				tabHelpBL[3][comptHelpBL] = messCourant.getFromY();
				comptHelpBL++;
			}
			// message d'aide d'une base concernant les explorers: les coordonnées de l'ennemi sont toujours présentes en argument du message
			if(messCourant.getAct() != null && messCourant.getAct() == "HELP-BE")
			{
				tabHelpBE[0][comptHelpBE] = Double.valueOf(messCourant.getArg1()).doubleValue();
				tabHelpBE[1][comptHelpBE] = Double.valueOf(messCourant.getArg2()).doubleValue();	
				tabHelpBE[2][comptHelpBE] = messCourant.getFromX();
				tabHelpBE[3][comptHelpBE] = messCourant.getFromY();
				comptHelpBE++;
			}
			// message d'aide d'un launcher : les coordonnées de l'ennemi sont toujours présentes en argument du message
			if(messCourant.getAct() != null && messCourant.getAct() == "HELP-L")
			{
				// un launcher ne prend pas en compte ses propres messages
				if(messCourant.getFromX() != 0 && messCourant.getFromY() != 0)
				{
					tabHelpL[0][comptHelpL] = Double.valueOf(messCourant.getArg1()).doubleValue();
					tabHelpL[1][comptHelpL] = Double.valueOf(messCourant.getArg2()).doubleValue();	
					tabHelpL[2][comptHelpL] = messCourant.getFromX();
					tabHelpL[3][comptHelpL] = messCourant.getFromY();
					comptHelpL++;
				}
			}
			// message d'aide d'un explorer : pas de coordonnées de l'ennemi en argument si message suite à tir
			if(messCourant.getAct() != null && messCourant.getAct() == "HELP-E")
			{
				tabHelpE[0][comptHelpE] = Double.valueOf(messCourant.getArg1()).doubleValue();
				tabHelpE[1][comptHelpE] = Double.valueOf(messCourant.getArg2()).doubleValue();	
				tabHelpE[2][comptHelpE] = messCourant.getFromX();
				tabHelpE[3][comptHelpE] = messCourant.getFromY();
				comptHelpE++;
			}
		}
		tailleAtaq 	= comptAtaq;
		tailleHelpBL = comptHelpBL;
		tailleHelpBE = comptHelpBE;
		tailleHelpE = comptHelpE;
		tailleHelpL = comptHelpL;
		comptAtaq	= 0;
		comptHelpBL	= 0;
		comptHelpBE	= 0;
		comptHelpE 	= 0;
		comptHelpL	= 0;
		// fin récupération et classement des messages
		
		// récupération et classement des objets perçus
		Percept[] objetsPercus = getPercepts();  		// entités dans le périmètre de perception
		for(int i=0;i<objetsPercus.length;i++)  // pour toutes les entités perçues...
		{
			Percept objetCourant = objetsPercus[i];
			if(objetCourant.getTeam().equals(getTeam()))	// objet de mon équipe
			{
				tabMyTeam[0][comptMyTeam] = objetCourant.getX();
				tabMyTeam[1][comptMyTeam] = objetCourant.getY();
				comptMyTeam++;	
			}
			else	// on ne garde que les ennemis d'énergie minimum les plus près de chaque type	
			{
				// ennemi de type base
				if(objetCourant.getPerceptType().equals("Home"))			// objet "ennemi" de type base
				{
					if(objetCourant.getEnergy()<tabBase[2] || tabBase[2] == 0)
					{
						tabBase[0] = objetCourant.getX();
						tabBase[1] = objetCourant.getY();
						tabBase[2] = objetCourant.getEnergy();
						tabBase[3] = objetCourant.getDistance();
					}
					else
						if(objetCourant.getEnergy()==tabBase[2] && objetCourant.getDistance() < tabBase[3])
						{
							tabBase[0] = objetCourant.getX();
							tabBase[1] = objetCourant.getY();
							tabBase[2] = objetCourant.getEnergy();
							tabBase[3] = objetCourant.getDistance();	
						}
				}
				// ennemi de type explorer
				if(objetCourant.getPerceptType().equals("Explorer"))		// objet "ennemi" de type explorer
				{
					if(objetCourant.getEnergy()<tabExplorer[2] || tabExplorer[2] == 0)
					{
						tabExplorer[0] = objetCourant.getX();
						tabExplorer[1] = objetCourant.getY();
						tabExplorer[2] = objetCourant.getEnergy();
						tabExplorer[3] = objetCourant.getDistance();
					}
					else
						if(objetCourant.getEnergy()==tabExplorer[2] && objetCourant.getDistance() < tabExplorer[3])
						{
							tabExplorer[0] = objetCourant.getX();
							tabExplorer[1] = objetCourant.getY();
							tabExplorer[2] = objetCourant.getEnergy();
							tabExplorer[3] = objetCourant.getDistance();	
						}
				}
				// ennemi de type launcher
				if(objetCourant.getPerceptType().equals("RocketLauncher"))	// objet "ennemi" de type launcher
				{
					if(objetCourant.getEnergy()<tabLauncher[2] || tabLauncher[2] == 0)
					{
						tabLauncher[0] = objetCourant.getX();
						tabLauncher[1] = objetCourant.getY();
						tabLauncher[2] = objetCourant.getEnergy();
						tabLauncher[3] = objetCourant.getDistance();
					}
					else
						if(objetCourant.getEnergy()==tabLauncher[2] && objetCourant.getDistance() < tabLauncher[3])
						{
							tabLauncher[0] = objetCourant.getX();
							tabLauncher[1] = objetCourant.getY();
							tabLauncher[2] = objetCourant.getEnergy();
							tabLauncher[3] = objetCourant.getDistance();	
						}
				}
			}			
		}
		tailleMyTeam 	= comptMyTeam;
		comptMyTeam 	= 0;
		// fin récupération et classement des objets perçus
		
		// on ordonne les actions principalement en fonction des objets perçus et des messages reçus
		// A : une base ennemie est à portée et le launcher est en position de tirer et/ou de se sacrifier
		if(tabBase[2]!=0 && tabBase[3]!=0)
		{
			// prévient les autres
			argMessageX = Double.toString(tabBase[0]);	
			argMessageY = Double.toString(tabBase[1]);
			broadcast (groupName,"Launcher",actMessageAttaque,argMessageX,argMessageY);
			if((!getShot()) || (getShot() && tabBase[2] < seuilEnergieBase))	
			{
				// tire
				gestionTir(towards(tabBase[0],tabBase[1]),tailleMyTeam,tabMyTeam);
				//launchRocket(towards(tabBase[0],tabBase[1]));
				return;
			}	
		}
		
		// B : un launcher ennemi est à portée
		if(tabLauncher[2]!=0 && tabLauncher[3]!=0)
		{
			// demande aide
			argMessageX = Double.toString(tabLauncher[0]);	
			argMessageY = Double.toString(tabLauncher[1]);
			broadcast (groupName,"Launcher",actMessageAide,argMessageX,argMessageY);
			// tire
			gestionTir(towards(tabLauncher[0],tabLauncher[1]),tailleMyTeam,tabMyTeam);
			//launchRocket(towards(tabLauncher[0],tabLauncher[1]));
			return;
		}
		
		// C : réception de demande d'aide de la base car launchers ennemis repérés dans zone de sécurité
		if (tailleHelpBL > 0)
		{
			desobstination();
			if(temps != tempsMax ) return;
			/* il faudrait sélectionner la base la plus rapprochée */
			// arbitrairement on va vers le dernier launcher ayant envoyé un message
			directionX = tabHelpBL[0][tailleHelpBL-1] + tabHelpBL[2][tailleHelpBL-1];
			directionY = tabHelpBL[1][tailleHelpBL-1] + tabHelpBL[3][tailleHelpBL-1];
			setHeading(towards(directionX,directionY));
			move();
			return;	
		}
			
		// D : réception de demande d'aide d'un launcher attaqué
		if (tailleHelpL > 0)
		{
			desobstination();
			if(temps != tempsMax ) return;
			/* il faudrait sélectionner le launcher le plus rapproché */
			// arbitrairement on va vers le dernier launcher ayant envoyé un message
			directionX = tabHelpL[0][tailleHelpL-1] + tabHelpL[2][tailleHelpL-1];
			directionY = tabHelpL[1][tailleHelpL-1] + tabHelpL[3][tailleHelpL-1];
			setHeading(towards(directionX,directionY));
			move();
			return;	
		}
		
		// E : réception d'un message d'attaque de la base ennemie
		if (tailleAtaq > 0)
		{
			desobstination();
			if(temps != tempsMax ) return;
			directionX = tabAtaq[0][tailleAtaq-1] + tabAtaq[2][tailleAtaq-1];
			directionY = tabAtaq[1][tailleAtaq-1] + tabAtaq[3][tailleAtaq-1];
			/* a voir : on ne bouge vers la base uniquement si on n'est pas déjà dessus */
			setHeading(towards(directionX,directionY));
			move();
			return;
		}
		
		// W : on a pour objet perçu un explorer
		if(tabExplorer[2]!=0 && tabExplorer[3]!=0)
		{
			// tire
			gestionTir(towards(tabExplorer[0],tabExplorer[1]),tailleMyTeam,tabMyTeam);
			//launchRocket(towards(tabExplorer[0],tabExplorer[1]));
			return;
		}
		
		// X : réception de demande d'aide de la base car explorers ennemis repérés dans zone de sécurité
		if (tailleHelpBE > 0)
		{
			desobstination();
			if(temps != tempsMax ) return;
			/* il faudrait sélectionner la base la plus rapprochée */
			// arbitrairement on va vers le dernier launcher ayant envoyé un message
			directionX = tabHelpBE[0][tailleHelpBE-1] + tabHelpBE[2][tailleHelpBE-1];
			directionY = tabHelpBE[1][tailleHelpBE-1] + tabHelpBE[3][tailleHelpBE-1];
			setHeading(towards(directionX,directionY));
			move();
			return;	
		}
			
		// Y : rejoindre ami perçu
		for(int i=0;i<objetsPercus.length;i++)  // pour toutes les entités perçues...
		{
			Percept objetCourant = objetsPercus[i];
			if (objetCourant.getTeam().equals(getTeam()) && objetCourant.getPerceptType().equals("RocketLauncher") && distanceTo(objetCourant) > distanceAmis && distanceTo(objetCourant) < distanceMinAmis)
			{
				if (!isMoving())	// cas où il a blocage : changement de direction
				{
					randomHeading();
				}
				else	// on va vers l'autre
				{
					setHeading(towards(objetCourant.getX(),objetCourant.getY())); //  récupère la direction de l'ami
				}
				move();
				return;
			}
		}
		// Z : déplacement aléatoire
		if (!isMoving())
		{
			randomHeading();
		}	
		move();
		return;	
	}
}