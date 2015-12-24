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
    }

    public override void probeObject(GameObject obj)
    {
       
    }

    public override void applyParameters()
    {
        percParam.setValue(1.0f);
    }

}
