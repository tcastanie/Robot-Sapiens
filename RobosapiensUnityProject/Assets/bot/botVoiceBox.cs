using UnityEngine;
using System.Collections;
using System;
using FMOD;
using System.Runtime.InteropServices;

public class botVoiceBox : MonoBehaviour {

    float minDist = 5.0f;
    float maxDist = 50.0f;
    int SampleFrequency = 44100;
    int SampleSize = 2;
    double amplitude = 1.0;
    double frequency = 440.0;
    double phase = 1.0;
    int channels = 2;
    // Use this for initialization
 
    FMOD.Channel channel;
 
    private const float DISTANCEFACTOR = 1.0f;

    private FMOD.SOUND_PCMREADCALLBACK pcmreadcallback = new FMOD.SOUND_PCMREADCALLBACK(PCMREADCALLBACK);
    private FMOD.SOUND_PCMSETPOSCALLBACK pcmsetposcallback = new FMOD.SOUND_PCMSETPOSCALLBACK(PCMSETPOSCALLBACK);

    void Start () {
        FMOD.VECTOR pos1 = new FMOD.VECTOR();
        pos1.x = gameObject.transform.position.x; pos1.y = gameObject.transform.position.y; pos1.z = gameObject.transform.position.z;
        FMOD.VECTOR vel1 = new FMOD.VECTOR();
        vel1.x = 0.0f; vel1.y = 0.0f; vel1.z = 0.0f;
        FMOD.System lowlevel = null;
        FMODUnity.RuntimeManager.StudioSystem.getLowLevelSystem(out lowlevel);

        FMOD.CREATESOUNDEXINFO soundinfo = new FMOD.CREATESOUNDEXINFO();
       
        soundinfo.pcmsetposcallback = PCMSETPOSCALLBACK;
        soundinfo.pcmreadcallback = PCMREADCALLBACK;
        soundinfo.decodebuffersize = 3000;
        soundinfo.length =(uint) (SampleFrequency * channels * sizeof(short) * 5);
        soundinfo.format = FMOD.SOUND_FORMAT.PCM16;
        soundinfo.defaultfrequency = SampleFrequency;
        soundinfo.numchannels = channels;
        
        FMOD.RESULT result;
        FMOD.Sound sound;
        result = lowlevel.createSound("", FMOD.MODE.OPENUSER | FMOD.MODE._3D | FMOD.MODE._3D_LINEARROLLOFF, ref soundinfo, out sound);
        result = sound.setMode(FMOD.MODE.LOOP_NORMAL);
        result = lowlevel.playSound(sound, null, true, out channel);

        channel.set3DMinMaxDistance(minDist * DISTANCEFACTOR, maxDist * DISTANCEFACTOR);
        channel.set3DAttributes(ref pos1, ref vel1, ref vel1);
        channel.setPaused(false);
    }

    private static float t1 = 0, t2 = 0;        // time
    private static float v1 = 0, v2 = 0;        // velocity


    private static FMOD.RESULT PCMREADCALLBACK(IntPtr soundraw, IntPtr data, uint datalen)
    {
        unsafe
        {
            uint count;

            short* stereo16bitbuffer = (short*)data.ToPointer();

            for (count = 0; count < (datalen >> 2); count++)        // >>2 = 16bit stereo (4 bytes per sample)
            {
                *stereo16bitbuffer++ = (short)(Math.Sin(t1) * 32767.0f);    // left channel
                *stereo16bitbuffer++ = (short)(Math.Sin(t2) * 32767.0f);    // right channel

                t1 += 0.01f + v1;
                t2 += 0.0142f + v2;
                v1 += (float)(Math.Sin(t1) * 0.002f);
                v2 += (float)(Math.Sin(t2) * 0.002f);
            }
        }
        return FMOD.RESULT.OK;
    }

    private static FMOD.RESULT PCMSETPOSCALLBACK(IntPtr soundraw, int subsound, uint pcmoffset, FMOD.TIMEUNIT postype)
    {
        /*
            This is useful if the user calls Sound::setTime or Sound::setPosition and you want to seek your data accordingly.
        */

        return FMOD.RESULT.OK;
    }
    // Update is called once per frame
    void Update () {
	
	}

    internal void doVoice()
    {
      
    
    }
 
}

