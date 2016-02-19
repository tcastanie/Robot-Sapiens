using UnityEngine;
using System.Collections;

public class IRDetectorScript : abstractSensorScript{
    public float maxDistance = 30.0f;
    public float distance = 15.0f;
    private RaycastHit hit;
    private Transform emitter;
    int layerMask = 1 << 8;
   

    // public double normalizedValue = 0.5;

    // Use this for initialization
    void Start () {
        emitter = transform.Find("tip").transform;
        //Debug.Log(emitter.name);
        //Debug.DrawRay(emitter.position, emitter.TransformDirection(Vector3.up) * 200, Color.red, 200.0f);
        layerMask = ~layerMask;
    }

    // Update is called once per frame
    void Update () {

        //Debug.Log(emitter.TransformDirection(Vector3.up));
        if (Physics.Raycast(emitter.position, emitter.TransformDirection(Vector3.up),out hit, maxDistance,layerMask))
        {
            Debug.DrawRay(emitter.position, emitter.TransformDirection(Vector3.up) * maxDistance, Color.yellow);
            //Debug.Log("hit");
            distance = hit.distance;
            //Debug.DrawLine(emitter.position, hit.point);
        }
        else
        {
            Debug.DrawRay(emitter.position, emitter.TransformDirection(Vector3.up) * maxDistance, Color.white);
            distance = maxDistance;
        }
        normalizedValue = distance / maxDistance;
       
    }
}
