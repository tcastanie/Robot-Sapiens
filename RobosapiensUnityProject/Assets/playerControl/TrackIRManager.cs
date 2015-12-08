using UnityEngine;
using System.Collections;

public class TrackIRManager : MonoBehaviour {

    TrackIRUnity.TrackIRClient trackIRclient;
    bool running = false;
    string status, data;

    // Use this for initialization
    void Start () {
        trackIRclient = new TrackIRUnity.TrackIRClient();
        StartCamera();
    }

    void StartCamera()
    {
        if (trackIRclient != null && !running)
        {                        // Start tracking
            status = trackIRclient.TrackIR_Enhanced_Init();
            running = true;
        }
    }

    void StopCamera()
    {
        if (trackIRclient != null && running)
        {                         // Stop tracking
            status = trackIRclient.TrackIR_Shutdown();
            running = false;
        }
    }

    void OnEnable()
    {
        StartCamera();
    }

    void OnDisable()
    {
        StopCamera();
    }

    void OnApplicationQuit()
    {                              
        StopCamera();
    }

    // Update is called once per frame
    void Update()
    {
        if (trackIRclient != null && running)
        {


        }
    }
}
