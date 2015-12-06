using UnityEngine;
using System.Collections;
using System;

public class collectorScript : MonoBehaviour {

    public float distPickUp = 0.5f;
    public string targetTag;
    public GameObject bot;
    private botControl scr;
    private Transform pos;

	// Use this for initialization
	void Start () {
        scr = bot.GetComponent<botControl>();
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
        float xPlayer = pos.position.x;
        float yPlayer = pos.position.z;

        GameObject[] healthPacks = GameObject.FindGameObjectsWithTag(targetTag);
        for (int i = 0; i < healthPacks.Length; i++)
        {
            float xHP = healthPacks[i].transform.position.x;
            float yHP = healthPacks[i].transform.position.z;
            //Debug.Log(distCart(xPlayer, yPlayer, xHP, yHP));
            if (distCart(xPlayer, yPlayer, xHP, yHP) < distPickUp)
            {
                Destroy(healthPacks[i]);
                scr.addReward(50.0);
            }
        }
    }
}
