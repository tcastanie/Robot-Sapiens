using UnityEngine;
using System.Collections;
using System.Collections.Generic;

public class motivScript : abstractMotivatorScript{

    //L'algo retourne 2 quand le robot va sur une nouvelle case -> très très très gratifiant !!!
    //L'algo reoutrne 1 quand le robot reste sur sa case -> peu gratifiant
    //L'algo retourne 0 quand le robot retourne sur une case précedemment visitée -> pénalisant

    float xRobot, yRobot;
    int xCase, yCase;
    Vector2 coordCase;
    List<Vector2> list = new List<Vector2>();

    // Use this for initialization
    void Start () {
        state = "REWARD_0";
	}

    new public void sendMYControlMessage(string msg)
    {
     
        switch (msg)
        {
            case "RUN":
                {
                    
                    break;
                }
            case "STOP":
                {
                    break;
                }
            case "REINIT":
                {
                 
                    list.Clear();
                    break;
                }
            default:
                {
                    break;
                }
        }
    }

    int explo(float x, float y) {
        int r;
        xCase = (int)x / 5;
        yCase = (int)y / 5;
        coordCase = new Vector2(xCase, yCase);
        if (!list.Contains(coordCase)) {
            r = 2;
            list.Add(coordCase);
        }
        else if (list[list.Count - 1] == coordCase) {
            r = 0;
        }
        else {
            r = 1;
        }
        return r;
    }
	
	// Update is called once per frame
	void Update () {
        xRobot = GetComponent<Transform>().position.x;//récupère les coordonnées x et z du robot
        yRobot = GetComponent<Transform>().position.z;;
        switch(explo(xRobot, yRobot))
        {
            case 0:
                {
                    state = "REWARD_0";
                    break;
                }
            case 1:
                {
                    state = "REWARD_1";
                    break;
                }
            case 2:
                {
                    state = "REWARD_2";
                    break;
                }
            default:
                {
                    break;
                }
        }


        /*
        int prout = explo(xRobot, yRobot);
        Debug.Log(prout);
        */
    }
}
