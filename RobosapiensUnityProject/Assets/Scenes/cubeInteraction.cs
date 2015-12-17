using UnityEngine;
using System.Collections;

public class cubeInteraction : MonoBehaviour
{
    private Transform oldTrsf;

    void OnTriggerEnter(Collider c)
    {
        if (c.transform.parent && c.transform.parent.parent && c.transform.parent.parent.gameObject.tag.Equals("leap_hand") && !leapStatesManagement.onPlacement && !leapStatesManagement.onMenu && leapStatesManagement.timerInactive <= 0)
        {
            if (leapStatesManagement.leapIndex)
            {
                Destroy(gameObject.transform.parent.gameObject);
            }
            else if (leapStatesManagement.leapPalm)
            {
                Debug.Log("palm action");
                leapStatesManagement.grabbedObj = gameObject.transform.parent.gameObject;
                leapStatesManagement.timerInactive = leapStatesManagement.leapWaitGeneral;
            }
            else
            {
                /*
                GameObject obj = leapStatesManagement.grabbedObj;
                leapStatesManagement.grabbedObj = null;
                leapStatesManagement.detachOject(obj);*/
            }
        }

    }

    void OnTriggerExit(Collider c)
    {

    }
}