using UnityEngine;
using System.Collections;
using Leap;
using System;
using UnityEngine.UI;

public class leapStatesManagement : MonoBehaviour {

    public HandController myLeap;
    public static bool leapIndex = false;
    public static bool leapPalm = false;
    public static bool leapFist = false;
    public static bool onMenu = false;
    public static bool onPlacement = false;
    public static bool inhibitGhost = false;
    public spawnCubes spCubeScript;

    public static GameObject grabbedObj = null;

    private GameObject ghostObject = null;
    private GameObject menu;
    private int GrabForce = 10;
    private Quaternion prevRot = new Quaternion();
    private int prevSwipeId = -1;
    private int timer = 0;
    private int leapWait = 40;
    public static int timerInactive = 0;
    public static int leapWaitGeneral = 20;

    // Use this for initialization
    void Start () {
        menu = GameObject.Find("menuDeSelection");
        myLeap.GetLeapController().EnableGesture(Gesture.GestureType.TYPE_SWIPE, true);
        myLeap.GetLeapController().EnableGesture(Gesture.GestureType.TYPE_KEY_TAP, true);
        myLeap.GetLeapController().Config.SetFloat("Gesture.Swipe.MinLength", 60.0f);
        myLeap.GetLeapController().Config.SetFloat("Gesture.Swipe.MinVelocity", 900.0f);
        myLeap.GetLeapController().Config.SetFloat("Gesture.KeyTap.MinDownVelocity", 60.0f);
        myLeap.GetLeapController().Config.SetFloat("Gesture.KeyTap.HistorySeconds", .2f);
        myLeap.GetLeapController().Config.SetFloat("Gesture.KeyTap.MinDistance", 5.0f);
        myLeap.GetLeapController().Config.Save();

    }

    // Update is called once per frame
    void Update () {
        timer = timer - 1;
        if (timer < 0)
            timer = 0;
        timerInactive = timerInactive - 1;
        if (timerInactive < 0)
            timerInactive = 0;
        Frame f = myLeap.GetFrame();
        HandList hands = f.Hands;
        Hand Hand = hands.GetEnumerator().Current;
        if(Hand != null && Hand.IsValid && timerInactive <=0)
        {
            
            Finger index = null;
            FingerList flist = Hand.Fingers.Extended();
            foreach(Finger p in flist)
            {
                if(p.IsFinger && (p).Type().Equals(Finger.FingerType.TYPE_INDEX))
                {
                    index = p;
                }
            }

            if (Hand.Pointables.Extended().Count < 3 && index != null && index.IsValid)
            {
                leapIndex = false;
                leapPalm = false;
                leapFist = false;
                leapIndex = true;
                Debug.Log("index");
            }
            else if (Hand.Pointables.Extended().Count >= 3)
            {
                leapIndex = false;
                leapPalm = false;
                leapFist = false;
                leapPalm = true;
                Debug.Log("palm");
            }
            else if(Hand.Pointables.Extended().Count == 0)
            {
                Debug.Log("fist");
                if (grabbedObj != null)
                {
                    Rigidbody rb = grabbedObj.GetComponent<Rigidbody>();
                    if (rb != null)
                        rb.isKinematic = false;
                    grabbedObj.SetActive(true);
                    //detachOject(grabbedObj);
                    grabbedObj = null;
                }
                else if(!leapFist)
                {
                    //toggle menu
                    if(onMenu)
                    {
                        menu.GetComponent<CanvasScaler>().scaleFactor = 1.0f;
                        //myLeap.GetLeapController().EnableGesture(Gesture.GestureType.TYPE_SWIPE,false);
                        timer = 0;
                    }
                    else if(!onPlacement)
                    {
                        menu.GetComponent<CanvasScaler>().scaleFactor = 2.0f;
                        timer = leapWait;
                    }
                    onMenu = !onMenu;
                }
                leapIndex = false;
                leapPalm = false;
                leapFist = false;
                Destroy(ghostObject);
                onPlacement = false;
                leapFist = true;
            }
            if(onMenu)
            {
                foreach(Gesture g in f.Gestures())
                {
                    SwipeGesture swipeGesture = new SwipeGesture(g);
                    if (g.Type == Gesture.GestureType.TYPE_SWIPE && g.Duration > 30 && prevSwipeId != g.Id && timer <= 0 && Math.Abs(swipeGesture.Direction.x) > 0.6)
                    {
                        //Debug.Log("swiper no swiping");
                       
                        //Debug.Log(swipeGesture.Direction);
                        if(swipeGesture.Direction.x>0)
                        {
                            spCubeScript.cycleObjectsForward();
                        }
                        else if(swipeGesture.Direction.x<0)
                        {
                            spCubeScript.cycleObjectsBackwards();        
                        }
                        prevSwipeId = g.Id;
                        timer = leapWait;
                    }
                }
            }
            if (grabbedObj != null)
            {
                HandModel[] PHands = myLeap.GetAllPhysicsHands();
                if (ghostObject == null)
                {
                    Rigidbody rb = grabbedObj.GetComponent<Rigidbody>();
                    if (rb != null)
                        rb.isKinematic = true;
                    grabbedObj.SetActive(false);
                    
                    if (PHands != null && PHands.Length > 0)
                    {
                        prevRot = new Quaternion();
                        //onPlacement = true;
                        inhibitGhost = true;
                        if (grabbedObj.name.Contains("Mur"))
                        {
                            ghostObject = ((GameObject)Instantiate(spCubeScript.murFantome, PHands[0].palm.transform.position, PHands[0].palm.transform.rotation));
                            ghostObject.transform.Translate(new Vector3(0.0f, 0.0f, 0.0f));
                        }
                        else if (grabbedObj.name.Contains("Cube"))
                        {
                            ghostObject = ((GameObject)Instantiate(spCubeScript.cubeFantome, PHands[0].palm.transform.position, PHands[0].palm.transform.rotation));
                            ghostObject.transform.Translate(new Vector3(0.0f, 0.0f, 0.0f));
                        }
                        else if (grabbedObj.name.Contains("Pack"))
                        {
                            ghostObject = ((GameObject)Instantiate(spCubeScript.healthPackFantome, PHands[0].palm.transform.position, PHands[0].palm.transform.rotation));
                            ghostObject.transform.Translate(new Vector3(0.0f, 0.0f, 0.0f));
                        }
                     
                    }
                }
                if (PHands != null && PHands.Length > 0)
                {
                    Debug.Log("moving ghost");
                    // Debug.Log(grabbedObj.transform.Find("attachPoint").transform.localPosition);

                    ghostObject.transform.position = PHands[0].palm.transform.position;//grabbedObj.transform.Find("attachPoint").transform.localPosition;// - grabbedObj.GetComponentsInParent ;// + PHands[0].transform.position;
                    //grabbedObj.transform.RotateAround(PHands[0].palm.transform.position - new Vector3(0.0f, 0.45f, 0.0f), grabbedObj.transform.up, PHands[0].palm.transform.rotation.eulerAngles.y- prevRot.eulerAngles.y);
                    //grabbedObj.transform.RotateAround(PHands[0].palm.transform.position - new Vector3(0.0f, 0.45f, 0.0f), grabbedObj.transform.forward, PHands[0].palm.transform.rotation.eulerAngles.x - prevRot.eulerAngles.x);
                    //grabbedObj.transform.RotateAround(PHands[0].palm.transform.position - new Vector3(0.0f, 0.45f, 0.0f), grabbedObj.transform.right,-( PHands[0].palm.transform.rotation.eulerAngles.z - prevRot.eulerAngles.z));
                    ghostObject.transform.rotation = PHands[0].palm.transform.rotation;
                    ghostObject.transform.Translate(new Vector3(0.0f, -1.6f, 0.0f));
                    prevRot = PHands[0].palm.transform.rotation;
                    /*   
                    Vector3 distance = PHands[0].palm.position - grabbedObj.transform.position;
                    //distance.Normalize();
                    grabbedObj.GetComponent<Rigidbody>().AddForce(distance * GrabForce);*/
                }
                foreach (Gesture g in f.Gestures())
                {
                    KeyTapGesture keytapGesture = new KeyTapGesture(g);
                    if (g.Type == Gesture.GestureType.TYPE_KEY_TAP && keytapGesture.Pointable.IsFinger && (new Finger(keytapGesture.Pointable)).Type().Equals(Finger.FingerType.TYPE_INDEX))
                    {
                        detachOject(grabbedObj);
                        grabbedObj = null;
                        return;
                    }
                }
                        /*
                        HandModel[] PHands = myLeap.GetAllPhysicsHands();
                        if(PHands != null && PHands.Length > 0)
                        {
                            // Debug.Log(grabbedObj.transform.Find("attachPoint").transform.localPosition);
                            Rigidbody rb = grabbedObj.GetComponentInChildren<Rigidbody>();
                            if(rb != null)
                                rb.isKinematic = false;
                            grabbedObj.transform.position = PHands[0].palm.transform.position ;//grabbedObj.transform.Find("attachPoint").transform.localPosition;// - grabbedObj.GetComponentsInParent ;// + PHands[0].transform.position;
                            //grabbedObj.transform.RotateAround(PHands[0].palm.transform.position - new Vector3(0.0f, 0.45f, 0.0f), grabbedObj.transform.up, PHands[0].palm.transform.rotation.eulerAngles.y- prevRot.eulerAngles.y);
                            //grabbedObj.transform.RotateAround(PHands[0].palm.transform.position - new Vector3(0.0f, 0.45f, 0.0f), grabbedObj.transform.forward, PHands[0].palm.transform.rotation.eulerAngles.x - prevRot.eulerAngles.x);
                            //grabbedObj.transform.RotateAround(PHands[0].palm.transform.position - new Vector3(0.0f, 0.45f, 0.0f), grabbedObj.transform.right,-( PHands[0].palm.transform.rotation.eulerAngles.z - prevRot.eulerAngles.z));
                            grabbedObj.transform.rotation = PHands[0].palm.transform.rotation;
                            if(grabbedObj.name.Contains("Mur"))
                            {
                                Debug.Log("picked up mur");
                                grabbedObj.transform.Translate(new Vector3(0.0f, +0.45f, 0.0f));
                            }
                            else if (grabbedObj.name.Contains("Cube"))
                            {
                                Debug.Log("picked up cube");
                                grabbedObj.transform.Translate(new Vector3(0.0f, -0.45f, 0.0f));
                            }
                            else if (grabbedObj.name.Contains("Pack"))
                            {
                                Debug.Log("picked up pack");
                                grabbedObj.transform.Translate(new Vector3(0.0f, -0.45f, 0.0f));
                            }
                            if (rb != null)
                                rb.isKinematic = true;
                            prevRot = PHands[0].palm.transform.rotation;
                            /*   
                            Vector3 distance = PHands[0].palm.position - grabbedObj.transform.position;
                            //distance.Normalize();
                            grabbedObj.GetComponent<Rigidbody>().AddForce(distance * GrabForce);*/

                        //}
                    }
            if (grabbedObj == null && !onMenu)
            {
                foreach (Gesture g in f.Gestures())
                {
                    KeyTapGesture keytapGesture = new KeyTapGesture(g);
                    if (g.Type == Gesture.GestureType.TYPE_KEY_TAP && keytapGesture.Pointable.IsFinger && (new Finger(keytapGesture.Pointable)).Type().Equals(Finger.FingerType.TYPE_INDEX))
                    {
                        if (onPlacement)
                        {
                            Destroy(ghostObject);
                            HandModel[] PHands = myLeap.GetAllPhysicsHands();
                            if (PHands != null && PHands.Length > 0)
                            {
                                switch (varGlobales.idObjet)
                                {
                                    case 0:
                                        ((GameObject)Instantiate(spCubeScript.cube, PHands[0].palm.transform.position, PHands[0].palm.transform.rotation)).transform.Translate(new Vector3(0.0f, -1.6f, 0.0f));
                                        break;
                                    case 1:
                                        ((GameObject)Instantiate(spCubeScript.mur, PHands[0].palm.transform.position, PHands[0].palm.transform.rotation)).transform.Translate(new Vector3(0.0f, -1.6f, 0.0f));
                                        break;
                                    case 2:
                                        ((GameObject)Instantiate(spCubeScript.healthPack, PHands[0].palm.transform.position, PHands[0].palm.transform.rotation)).transform.Translate(new Vector3(0.0f, -1.6f, 0.0f));
                                        break;
                                    default:
                                        Debug.Log("Erreur : Objet inconnu");
                                        break;
                                }
                                prevRot = new Quaternion();
                                onPlacement = false;
                                timerInactive = leapWaitGeneral;
                            }
                        }
                        else
                        {
                            HandModel[] PHands = myLeap.GetAllPhysicsHands();
                            if (PHands != null && PHands.Length > 0)
                            {
                                prevRot = new Quaternion();
                                onPlacement = true;
                                switch (varGlobales.idObjet)
                                {
                                    case 0:
                                        ghostObject = ((GameObject)Instantiate(spCubeScript.cubeFantome, PHands[0].palm.transform.position, PHands[0].palm.transform.rotation));
                                        ghostObject.transform.Translate(new Vector3(0.0f, 0.0f, 0.0f));
                                        break;
                                    case 1:
                                        ghostObject = ((GameObject)Instantiate(spCubeScript.murFantome, PHands[0].palm.transform.position, PHands[0].palm.transform.rotation));
                                        ghostObject.transform.Translate(new Vector3(0.0f, 0.0f, 0.0f));
                                        break;
                                    case 2:
                                        ghostObject = ((GameObject)Instantiate(spCubeScript.healthPackFantome, PHands[0].palm.transform.position, PHands[0].palm.transform.rotation));
                                        ghostObject.transform.Translate(new Vector3(0.0f, 0.0f, 0.0f));
                                        break;
                                    default:
                                        Debug.Log("Erreur : Objet inconnu");
                                        break;
                                }
                                onPlacement = true;
                            }
                        }
                        //Debug.Log("tappety tap tap");                        
                    }
                }
            }
            if (onPlacement)
            {
                HandModel[] PHands = myLeap.GetAllPhysicsHands();
                if (PHands != null && PHands.Length > 0)
                {
                    // Debug.Log(grabbedObj.transform.Find("attachPoint").transform.localPosition);
                   
                    ghostObject.transform.position = PHands[0].palm.transform.position;//grabbedObj.transform.Find("attachPoint").transform.localPosition;// - grabbedObj.GetComponentsInParent ;// + PHands[0].transform.position;
                    //grabbedObj.transform.RotateAround(PHands[0].palm.transform.position - new Vector3(0.0f, 0.45f, 0.0f), grabbedObj.transform.up, PHands[0].palm.transform.rotation.eulerAngles.y- prevRot.eulerAngles.y);
                    //grabbedObj.transform.RotateAround(PHands[0].palm.transform.position - new Vector3(0.0f, 0.45f, 0.0f), grabbedObj.transform.forward, PHands[0].palm.transform.rotation.eulerAngles.x - prevRot.eulerAngles.x);
                    //grabbedObj.transform.RotateAround(PHands[0].palm.transform.position - new Vector3(0.0f, 0.45f, 0.0f), grabbedObj.transform.right,-( PHands[0].palm.transform.rotation.eulerAngles.z - prevRot.eulerAngles.z));
                    ghostObject.transform.rotation = PHands[0].palm.transform.rotation;
                    ghostObject.transform.Translate(new Vector3(0.0f, -1.6f, 0.0f));
                    prevRot = PHands[0].palm.transform.rotation;
                    /*   
                    Vector3 distance = PHands[0].palm.position - grabbedObj.transform.position;
                    //distance.Normalize();
                    grabbedObj.GetComponent<Rigidbody>().AddForce(distance * GrabForce);*/
                }
            }
        }
        
    }

    void detachOject(GameObject obj)
    {
        Destroy(ghostObject);
        Rigidbody rb = obj.GetComponent<Rigidbody>();
        if (rb != null)
            rb.isKinematic = false;

        HandModel[] PHands = myLeap.GetAllPhysicsHands();
        if (PHands != null && PHands.Length > 0)
        {
            obj.transform.position = new Vector3(PHands[0].palm.transform.position.x, PHands[0].palm.transform.position.y, PHands[0].palm.transform.position.z);
            obj.transform.rotation = new Quaternion(PHands[0].palm.transform.rotation.x, PHands[0].palm.transform.rotation.y, PHands[0].palm.transform.rotation.z, PHands[0].palm.transform.rotation.w);

            if (grabbedObj.name.Contains("Mur"))
                obj.transform.Translate(new Vector3(0.0f, -1.6f, 0.0f));
            else if (grabbedObj.name.Contains("Cube"))
                obj.transform.Translate(new Vector3(0.0f, -1.6f, 0.0f));
            else if (grabbedObj.name.Contains("Pack"))
                obj.transform.Translate(new Vector3(0.0f, -1.6f, 0.0f));
            
            timerInactive = leapWaitGeneral;
        }
        obj.SetActive(true);
        prevRot = new Quaternion();
        inhibitGhost = false;   
    }
}
