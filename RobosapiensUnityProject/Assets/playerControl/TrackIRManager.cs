using UnityEngine;
using System.Collections;
using System;
using TrackIRUnity;

public class TrackIRManager : MonoBehaviour{

    TrackIRUnity.TrackIRClient trackIRclient;
    public bool running = false;
    public bool active = false;
    string status, data;
    private Quaternion m_CharacterTargetRot;
    private Quaternion m_CameraTargetRot;
    public float XSensitivity = 0.01f;
    public float YSensitivity = 0.01f;
    public bool clampVerticalRotation = true;
    public float MinimumX = -90F;
    public float MaximumX = 90F;
    public bool smooth;
    public float smoothTime = 5f;
    private long lastSig = -1;
    private int counter = 0;
    private int timeout = 20;
    TrackIRClient.LPTRACKIRDATA tid;
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
            if(status != null)
                running = true;
        }
    }

    private void Update()
    {
        if (running)
        {
            TrackIRClient.LPTRACKIRDATA temptid = trackIRclient.client_HandleTrackIRData(); // Data for head tracking
            if (lastSig == temptid.wPFrameSignature)
            {
                if (counter < timeout)
                    counter++;
                else
                    active = false;
            }
            else
            {
                tid = temptid;
                counter = 0;
                active = true;
                lastSig = tid.wPFrameSignature;
            }
        }
        else
            active = false;
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

    internal void LookRotation(Transform character, Transform camera)
    {
        if (running)
        {
            //data = trackIRclient.client_TestTrackIRData();          // Data for debugging output, can be removed if not debugging/testing
            //TrackIRClient.LPTRACKIRDATA tid = trackIRclient.client_HandleTrackIRData(); // Data for head tracking
            //Vector3 rot = camera.transform.localRotation.eulerAngles;
            Vector3 rot = new Vector3();
            rot.y = -tid.fNPYaw * YSensitivity;
            rot.x = tid.fNPPitch *XSensitivity;
            rot.z = 0;
            camera.transform.localRotation = Quaternion.Euler(rot);
            rot.x = 0;
            rot.y += character.localRotation.eulerAngles.y;
            character.localRotation = Quaternion.Slerp(character.localRotation, Quaternion.Euler(rot), smoothTime * Time.deltaTime);
        }
    }

    public void Init(Transform character, Transform camera)
    {
        m_CharacterTargetRot = character.localRotation;
        m_CameraTargetRot = camera.localRotation;
        //Start();
    }

    Quaternion ClampRotationAroundXAxis(Quaternion q)
    {
        q.x /= q.w;
        q.y /= q.w;
        q.z /= q.w;
        q.w = 1.0f;

        float angleX = 2.0f * Mathf.Rad2Deg * Mathf.Atan(q.x);

        angleX = Mathf.Clamp(angleX, MinimumX, MaximumX);

        q.x = Mathf.Tan(0.5f * Mathf.Deg2Rad * angleX);

        return q;
    }
}
