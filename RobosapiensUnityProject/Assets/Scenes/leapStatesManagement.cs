using UnityEngine;
using System.Collections;
using Leap;
using System;

public class leapStatesManagement : MonoBehaviour {

    public HandController myLeap;
    public static bool leapIndex = false;
    public static bool leapPalm = false;
    public static bool leapFist = false;

    public static GameObject grabbedObj = null;

    private int GrabForce = 10;

    // Use this for initialization
    void Start () {
	
	}
	
	// Update is called once per frame
	void Update () {
        Frame f = myLeap.GetFrame();
        HandList hands = f.Hands;
        Hand Hand = hands.GetEnumerator().Current;
        if(Hand != null && Hand.IsValid)
        {
            leapIndex = false;
            leapPalm = false;
            leapFist = false;
            Finger index = null;
            FingerList flist = Hand.Fingers.Extended();
            foreach(Finger p in flist)
            {
                if(p.IsFinger && (p).Type().Equals(Finger.FingerType.TYPE_INDEX))
                {
                    index = p;
                }
            }

            if (Hand.Pointables.Extended().Count < 3 && index != null && index.IsValid){
                leapIndex = true;
                Debug.Log("index");
            }
            else if(Hand.Pointables.Extended().Count >= 3)
            {
                leapPalm = true;
                Debug.Log("palm");
            }
            else
            {
                leapFist = true;
                Debug.Log("fist");
            }
            if (grabbedObj != null)
            {
                HandModel[] PHands = myLeap.GetAllPhysicsHands();
                if(PHands != null && PHands.Length > 0)
                {
                    //grabbedObj.GetComponent<Rigidbody>().Sleep();
                    //grabbedObj.transform.parent = GameObject.FindGameObjectsWithTag("MainCamera")[0].transform;
                    grabbedObj.GetComponent<Rigidbody>().isKinematic = false;
                    grabbedObj.transform.parent.position = PHands[0].palm.transform.position;// - grabbedObj.GetComponentsInParent ;// + PHands[0].transform.position;
                    grabbedObj.transform.parent.rotation = PHands[0].palm.transform.rotation;
                    grabbedObj.GetComponent<Rigidbody>().isKinematic = true;

                    /*   
                    Vector3 distance = PHands[0].palm.position - grabbedObj.transform.position;
                    //distance.Normalize();
                    grabbedObj.GetComponent<Rigidbody>().AddForce(distance * GrabForce);*/
                }
            }
        }
        
    }

    internal static void detachOject(GameObject obj)
    {
        obj.transform.parent.position = new Vector3(obj.transform.parent.position);
        obj.transform.parent.rotation = new Vector3(obj.transform.parent.rotation);
    }
}
