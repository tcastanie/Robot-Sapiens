using UnityEngine;
using System.Collections;

public class spawnCubes : MonoBehaviour {

	public GameObject cube;
	public GameObject cubeFantome;

    Quaternion rotation;

	// Use this for initialization
	void Start () {

	}

	// Update is called once per frame
	void Update () {

        rotation = Quaternion.Euler(0, GameObject.Find("cubeSpawner").transform.rotation.eulerAngles.y, 0);

        if (Input.GetMouseButtonDown(0)){
			Instantiate(cubeFantome,transform.position, rotation);
		}

		if(Input.GetMouseButtonUp(0)){
			Instantiate(cube,transform.position, rotation);
		}
	}
}
