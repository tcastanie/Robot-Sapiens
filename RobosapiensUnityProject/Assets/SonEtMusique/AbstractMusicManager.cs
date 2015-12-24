using UnityEngine;
using System.Collections;
using FMODUnity;

public class AbstractMusicManager : MonoBehaviour {

    public FMODUnity.StudioEventEmitter emmiter;
    public string tagOfSources;
    private GameObject[] objects;

    // Use this for initialization
    public void Start () {
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

    public virtual void probeObject(GameObject obj)
    {

    }

    public virtual void applyParameters()
    {

    }
}
