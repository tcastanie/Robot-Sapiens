using UnityEngine;
using System.Collections;
using FMODUnity;
using FMOD;

public class RobotSapiensMusicManager : AbstractMusicManager {
    FMOD.Studio.EventInstance evInst;
    FMOD.Studio.ParameterInstance percParam;
    FMOD.Studio.ParameterInstance bassParam;
    FMOD.Studio.ParameterInstance padParam;
    FMOD.Studio.ParameterInstance sweepParam;

    private
        double objCount = 0;
    float[] etab = new float[4];
    float[] temptab = new float[4];

    public new void Start()
    {
        base.Start();
        FMOD.Studio.EventDescription eventDesc;       
        FMODUnity.RuntimeManager.StudioSystem.getEvent(emmiter.Event, out eventDesc);
        FMOD.Studio.EventInstance[] tabEv;
        eventDesc.getInstanceList(out tabEv);
        evInst = tabEv[0];
        evInst.getParameter("pad", out padParam);
        evInst.getParameter("sweep", out sweepParam);
        evInst.getParameter("bass", out bassParam);
        evInst.getParameter("perc", out percParam);
        etab[0] = 0.5f; etab[1] = 0.5f; etab[2] = 0.5f; etab[3] = 0.5f;
        temptab[0] = 0.0f; temptab[1] = 0.0f; temptab[2] = 0.0f; temptab[3] = 0.0f;
    }

    public override void probeObject(GameObject obj)
    {
        float[] emTab = ((botControl)obj.GetComponent<botControl>()).emoTab;
        temptab[0] += emTab[0];
        temptab[1] += emTab[1];
        temptab[2] += emTab[2];
        temptab[3] += emTab[3];
        objCount++;
    }

    public override void applyParameters()
    {

        temptab[0] /= (float)objCount; temptab[1] /= (float)objCount; temptab[2] /= (float)objCount; temptab[3] /=(float)objCount;
        percParam.setValue(temptab[2]);
        bassParam.setValue(temptab[0]);
        padParam.setValue(temptab[1]);
        sweepParam.setValue(temptab[3]);
        objCount = 0;
        temptab[0] = 0.0f; temptab[1] = 0.0f; temptab[2] = 0.0f; temptab[3] = 0.0f;
    }

}
