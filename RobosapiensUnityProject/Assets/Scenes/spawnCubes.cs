using UnityEngine;
using System.Collections;
using Leap;

public class spawnCubes : MonoBehaviour {

	public GameObject cube;
	public GameObject cubeFantome;
    public HandController myLeap;

    Quaternion rotation;

    private bool hover=false;

	// Use this for initialization
	void Start () {
//        myLeap.EnableGesture(Gesture.GestureType.TYPE_SCREEN_TAP);
	}

    void onHover()
    {
        Debug.Log("hover");
        hover = true;
    }



    // Update is called once per frame
    void Update () {

        rotation = Quaternion.Euler(0, GameObject.Find("cubeSpawner").transform.rotation.eulerAngles.y, 0);

        if (Input.GetMouseButtonDown(0)){
			Instantiate(cubeFantome,transform.position, rotation);
            hover = false;
		}

		if(Input.GetMouseButtonUp(0)){
			Instantiate(cube,transform.position, rotation);
		}
	}
}
