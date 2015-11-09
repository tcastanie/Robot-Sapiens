using UnityEngine;
using System.Collections;

public class spawnCubes : MonoBehaviour {

	public GameObject cube;

	// Use this for initialization
	void Start () {

	}

	// Update is called once per frame
	void Update () {
		if(Input.GetMouseButtonDown(0)){
			Instantiate(cube,GameObject.Find("cubeSpawner").transform.position,Quaternion.identity);
		}

		if(Input.GetMouseButtonUp(0)){
			//Instantiate(cube,GameObject.Find("cubeSpawner").transform.position,Quaternion.identity);
		}
	}
}
