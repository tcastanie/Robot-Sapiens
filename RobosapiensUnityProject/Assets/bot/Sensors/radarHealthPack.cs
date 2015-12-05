using UnityEngine;
using System.Collections;
using System;

public class radarHealthPack : MonoBehaviour {

    public float distanceMax;//la distance max de détection

    float xPlayer, yPlayer;//la position x et z du robot

    // Use this for initialization
    void Start () {
    }

    float distCart(float xA, float yA, float xB, float yB) {
        //retourne la distance cartésienne entre deux objets A(xA, yA) et B(xB, yB)
        float result = (float)Math.Sqrt(Math.Pow(xB - xA, 2) + Math.Pow(yB - yA, 2));
        if(result < 0) {
            result *= -1;
        }
        return result;
    }

    float radar(float distMax) {
        //return -1 si pas de health_pack en dessous de distanceMax
        //return float de 0 en faisant face, à 0.5 en tournant le dos au health_pack
        float retour;
        GameObject[] healthPacks = GameObject.FindGameObjectsWithTag("health_pack");
        float smallestDist = distMax;
        int idPlusProche = 0;
        for(int i = 0; i<healthPacks.Length; i++) {
            float xHP = healthPacks[i].transform.position.x;
            float yHP = healthPacks[i].transform.position.z;
            //Debug.Log(distCart(xPlayer, yPlayer, xHP, yHP));
            if(distCart(xPlayer, yPlayer, xHP, yHP) < smallestDist) {
                smallestDist = distCart(xPlayer, yPlayer, xHP, yHP);
                idPlusProche = i;
            }
        }
        //Debug.Log(idPlusProche);
        if (smallestDist >= distanceMax) {
            retour = 0;
        } else {
            Vector3 targetDir = healthPacks[idPlusProche].transform.position - transform.position;
            Vector3 forward = transform.forward;
            Vector2 targetDir2 = new Vector2(targetDir.x, targetDir.z);
            Vector2 forward2 = new Vector2(forward.x, forward.z);
            float angle = Vector2.Angle(targetDir2, forward2);
            //Debug.Log(targetDir + " " + forward + " " + angle);
            retour = angle/360.0f;
        }

        return retour;
    }
	
	// Update is called once per frame
	void Update () {
        //affiche le résultat de radar dans la console
        xPlayer = GetComponent<Transform>().position.x;//récupère les coordonnées x et z du robot
        yPlayer = GetComponent<Transform>().position.z;
        float rep = radar( distanceMax );
        Debug.Log("réponse radar HP :" + rep);
	}
}
