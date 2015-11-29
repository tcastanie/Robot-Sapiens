using UnityEngine;
using System.Collections;

public class IRDetectorScript : MonoBehaviour {
    public float maxDistance = 100.0F;
    public float distance = 0.0f;
    private RaycastHit hit;
    private Transform emitter;

    // Use this for initialization
    void Start () {
        emitter = transform.Find("tip").transform;
        Debug.Log(emitter.name);
	}
	
	// Update is called once per frame
	void Update () {

        //Debug.Log(emitter.TransformDirection(Vector3.up));
        if (Physics.Raycast(emitter.position, emitter.TransformDirection(Vector3.up),out hit, maxDistance))
        {
            Debug.Log("hit");
            distance = hit.distance;
        }
        else
        {
            distance = maxDistance;
        }
        Debug.Log(distance);
    }
}
