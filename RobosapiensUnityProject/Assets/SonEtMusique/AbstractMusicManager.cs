using UnityEngine;
using System.Collections;
using FMODUnity;

public class AbstractMusicManager : MonoBehaviour {

    public FMODUnity.StudioEventEmitter emmiter;
    public string tagOfSources;
    private GameObject[] objects;

    // Use this for initialization
    void Start () {
        objects = GameObject.FindGameObjectsWithTag(tagOfSources);
	}
	
	// Update is called once per frame
	void Update () {
        foreach(GameObject o in objects)
        {
            probeObject(o);
        }
        applyParameters();
	}

    void probeObject(GameObject obj)
    {

    }

    void applyParameters()
    {

    }
}
