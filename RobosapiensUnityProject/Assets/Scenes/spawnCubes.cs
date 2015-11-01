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
			// print(Input.mousePosition);
			// print(GetComponent<Transform>().position);
			// Vector3 p = Camera.main.ScreenToWorldPoint(new Vector3(Input.mousePosition.x, Input.mousePosition.y,1.0f));
			Instantiate(cube,GetComponent<Transform>().position,Quaternion.identity);
		}
	}
}
