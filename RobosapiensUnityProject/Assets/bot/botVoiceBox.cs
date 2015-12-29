using UnityEngine;
using System.Collections;
using System;
using FMOD;
using System.Runtime.InteropServices;

public class botVoiceBox : MonoBehaviour {

    //dsp
    DSP sine;
    double frequency;
    float minDist = 5.0f;
    float maxDist = 30.0f;
    // Use this for initialization
    int offsetWave;
    FMOD.Channel channel;
    FMOD.System lowlevel = null;
    private const float DISTANCEFACTOR = 1.0f;

    FMOD.VECTOR lastPos;
    public float[] etab;

    void Start () {
        etab = ((botControl)gameObject.GetComponent<botControl>()).emoTab;
        FMOD.VECTOR pos1 = new FMOD.VECTOR();
        pos1.x = gameObject.transform.position.x; pos1.y = gameObject.transform.position.y; pos1.z = gameObject.transform.position.z;
        lastPos = pos1;
        FMOD.VECTOR vel1 = new FMOD.VECTOR();
        vel1.x = 0.0f; vel1.y = 0.0f; vel1.z = 0.0f;
        
        FMODUnity.RuntimeManager.StudioSystem.getLowLevelSystem(out lowlevel);
        System.Random random = new System.Random();

        offsetWave = random.Next(0, 5);
        switch (random.Next(0, 5))
        {
            case 0:
                {
                    frequency = 220f;
                    break;
                }
            case 1:
                {
                    frequency = 440f;
                    break;
                }
            case 2:
                {
                    frequency = 880f;
                    break;
                }
            case 3:
                {
                    frequency = 1760f;
                    break;
                }
            case 4:
                {
                    frequency = 110f;
                    break;
                }
            case 5:
                {
                    frequency = 3520f;
                    break;
                }
            default:
                {
                    break;
                }
        }

      
        lowlevel.createDSPByType(DSP_TYPE.OSCILLATOR, out sine);
        sine.setParameterFloat(1, (float)frequency);

        lowlevel.playDSP(sine, null, true, out channel);
        channel.setMode(MODE._3D |MODE._3D_LINEARROLLOFF);
        channel.set3DMinMaxDistance(minDist * DISTANCEFACTOR, maxDist * DISTANCEFACTOR);
        channel.set3DAttributes(ref pos1, ref vel1, ref vel1);
        channel.setVolume(0.3f);
        channel.setPaused(false);
    }


    void Update () {
        channel.setPaused(true);
        FMOD.VECTOR pos1 = new FMOD.VECTOR();
        pos1.x = gameObject.transform.position.x; pos1.y = gameObject.transform.position.y; pos1.z = gameObject.transform.position.z;
        FMOD.VECTOR vel1 = new FMOD.VECTOR();
        FMOD.VECTOR v2 = new FMOD.VECTOR();
        v2.x = 0.0f; v2.y = 0.0f; v2.z = 0.0f;
        vel1.x = pos1.x - lastPos.x; vel1.y = pos1.y - lastPos.y; vel1.z = pos1.z - lastPos.z;

        lastPos = pos1;
        //UnityEngine.Debug.Log(etab);

        double myFreq = frequency;
        myFreq += frequency * etab[0] + frequency * etab[1];
        myFreq = (myFreq % 1000.0f) + 80.0f;
        sine.setParameterFloat(1, (float)myFreq);
        sine.setParameterInt(0, (int)((Math.Round(5*etab[0])+offsetWave))%5);
        channel.set3DMinMaxDistance(minDist * DISTANCEFACTOR, maxDist * DISTANCEFACTOR);
        channel.set3DAttributes(ref pos1, ref vel1, ref vel1);
        channel.setVolume(0.4f*etab[3]);
        channel.setPaused(false);
        //lowlevel.update();
    }

    internal void doVoice()
    {
      
    
    }
 
}

