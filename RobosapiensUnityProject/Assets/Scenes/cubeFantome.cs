﻿using UnityEngine;
using System.Collections;

public class cubeFantome : MonoBehaviour {

    // Use this for initialization
    void Start () {
	
	}
	
	// Update is called once per frame
	void Update () {
        transform.position = GameObject.Find("cubeSpawner").transform.position;
        transform.rotation = Quaternion.Euler(0, GameObject.Find("cubeSpawner").transform.rotation.eulerAngles.y, 0);
        if (Input.GetMouseButtonUp(0)){
            Destroy(gameObject);
        }
    }
}
