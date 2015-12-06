using UnityEngine;
using System.Collections;

public class IRDetectorScript : abstractSensorScript{
    public float maxDistance = 100.0f;
    public float distance = 50.0f;
    private RaycastHit hit;
    private Transform emitter;

    // Use this for initialization
    void Start () {
        emitter = transform.Find("tip").transform;
//        Debug.Log(emitter.name);
	}
	
	// Update is called once per frame
	void Update () {

        //Debug.Log(emitter.TransformDirection(Vector3.up));
        Debug.DrawRay(emitter.position, emitter.TransformDirection(Vector3.up), Color.red, 1.5f);
        if (Physics.Raycast(emitter.position, emitter.TransformDirection(Vector3.up),out hit, maxDistance))
        {
            //Debug.Log("hit");
            distance = hit.distance;
        }
        else
        {
            distance = maxDistance;
        }
        normalizedValue = distance / maxDistance;
//        Debug.Log(distance);
    }
}
