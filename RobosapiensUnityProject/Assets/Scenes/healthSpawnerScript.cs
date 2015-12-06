using UnityEngine;
using System.Collections;
using System;

public class healthSpawnerScript : MonoBehaviour {
    public int timeDelay = 2000;
    private int count = 0;
    private double dist = 0.5;
    private Transform pos;
    public GameObject healthPack;

    // Use this for initialization
    void Start()
    {
        pos = GetComponent<Transform>();
    }

    float distCart(float xA, float yA, float xB, float yB)
    {
        //retourne la distance cartésienne entre deux objets A(xA, yA) et B(xB, yB)
        float result = (float)Math.Sqrt(Math.Pow(xB - xA, 2) + Math.Pow(yB - yA, 2));
        if (result < 0)
        {
            result *= -1;
        }
        return result;
    }

	
	// Update is called once per frame
	void Update () {
	    if(count == 0)
        {
            Boolean found = false;
            float xPlayer = pos.position.x;
            float yPlayer = pos.position.z;

            GameObject[] healthPacks = GameObject.FindGameObjectsWithTag("health_pack");
            for (int i = 0; i < healthPacks.Length; i++)
            {
                float xHP = healthPacks[i].transform.position.x;
                float yHP = healthPacks[i].transform.position.z;
                //Debug.Log(distCart(xPlayer, yPlayer, xHP, yHP));
                if (distCart(xPlayer, yPlayer, xHP, yHP) < dist)
                {
                    found = true;
                }
            }
            if(!found)
            {
                count = timeDelay;
            }
        }
        else if(count == 1)
        {
            Instantiate(healthPack, transform.position, transform.rotation);
            count--;
        }
        else
        {
            count--;
        }
	}
}
