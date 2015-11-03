package warbot.CM;

import warbot.kernel.*;

public class CMExplorer extends Brain
{
	String groupName	="warbot-";
	String roleName		="Explorer";
	
	int memeDirection	=30;
	int tempsMax		=8;
	int temps			=tempsMax;	// variable permettant de garder la meme direction pendant tempsMax itérations

	public CMExplorer(){}

	public void activate()
	{
		groupName		=groupName+getTeam();  // -> warbot-CM
		randomHeading();  // direction aléatoire
		println("Explorateur CM opérationnel");
		createGroup(false,groupName,null,null);
		requestRole(groupName,roleName,null);
		requestRole(groupName,"mobile",null);
		
	}
	
	public void decompteTemps()
	{
		temps--;	
	}

	public void doIt()
	{		
		String chaineAide		="HELP-E";
		String chaineAtak		="ATAQ";
		double positionEnnemiX 	= 0;
		double positionEnnemiY 	= 0;
		String ennemiX 			= "";
		String ennemiY 			= "";
				
		if (!isMoving())	// si bloqué : direction aléatoire
			randomHeading();
		
		
		// 1. Si base ennemie trouvée : broadcast
		Percept[]objetsPercus = getPercepts();  // entités dans le périmètre de perception
		for(int i=0;i<objetsPercus.length;i++)  // pour toutes les entités perçues...
		{
			Percept objetCourant = objetsPercus[i];
			if (!objetCourant.getTeam().equals(getTeam()) && objetCourant.getPerceptType().equals("Home")) // si objet courant = base ennemie
			{
				ennemiX = Double.toString(objetCourant.getX());	
				ennemiY = Double.toString(objetCourant.getY());				
				broadcast(groupName,"Launcher",chaineAtak,ennemiX,ennemiY);
			}
		}		
			
		// 2. Si attaqué : demande d'aide sans argument
		if (getShot())
		{
			println("explorer attaqué");
			broadcast(groupName,"Launcher",chaineAide,"0","0");			
		}
			
		// 3. si rocketlauncher ennemi repéré
		for(int i=0;i<objetsPercus.length;i++)  // pour toutes les entités perçues...
		{
			Percept objetCourant = objetsPercus[i];
			if (!objetCourant.getTeam().equals(getTeam()) && objetCourant.getPerceptType().equals("RocketLauncher")) // si RocketLauncher ennemi
			{
				// stratégie d'évitement
				positionEnnemiX = objetCourant.getX();	// abscisse de l'ennemi
				positionEnnemiY = objetCourant.getY();	// ordonnée de l'ennemi
				ennemiX = Double.toString(objetCourant.getX());	
				ennemiY = Double.toString(objetCourant.getY());				
//				println("launcher ennemi repéré");
//				println(ennemiX);
//				println(ennemiY);	
				broadcast(groupName,"Launcher",chaineAide,ennemiX,ennemiY);
				for(int j=0;j<objetsPercus.length;j++)  // pour toutes les entités perçues...
				{
					Percept objetCourant2 = objetsPercus[j];
					if ((getHeading()-(towards(objetCourant2.getX(),objetCourant.getY()))>=20) && (objetCourant2.getPerceptType().equals("Obstacle")))
					// si détecte un obstacle et que cet obstacle est dans le champ de la direction de l'agent	
					{
						if (temps==tempsMax)	// si on vient de repérer l'obstacle
						{
							decompteTemps();
							setHeading(towards(-positionEnnemiX/2,-positionEnnemiY)); // on évite ET l'ennemi, Et l'obstacle
						}
						else	
						{
							if (temps==0)
								{temps=tempsMax;}
							else	// temps entre 8 et 0
								{decompteTemps();}		
						}
						move();
						return;
					}					
				}	
				setHeading(towards(-positionEnnemiX,-positionEnnemiY)); // direction opposée à l'ennemi repéré
			}
		}
		
		// 4. déplacement aléatoire (randomHeading au début)
		temps=tempsMax;	//remise à 0 du compteur : on est hors de risque...
		move();
		return;			
	}
}