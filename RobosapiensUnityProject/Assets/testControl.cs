using UnityEngine;
using System.Collections;

public class testControl : MonoBehaviour {
    [SerializeField]
    private WheelCollider[] Wheels = new WheelCollider[2];
    float coef = 30.5f;
    // Use this for initialization
    void Start () {
	
	}
	
	// Update is called once per frame
	void Update ()
    {

        Wheels[0].motorTorque = (coef * Input.GetAxis("botL"));
        Wheels[1].motorTorque = coef * Input.GetAxis("botR");
        //Debug.Log(Wheels[0].motorTorque); Debug.Log(Wheels[1].motorTorque);
    }
}
