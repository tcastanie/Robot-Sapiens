using UnityEngine;
using System.Collections;
using System;
using FMOD;
using System.Runtime.InteropServices;

public class botVoiceBox : MonoBehaviour {
    int SampleFrequency = 44100;
    int SampleSize = 2;
    double amplitude = 2.0;
    double frequency = 440.0;
    double phase = 1.0;
    // Use this for initialization
    void Start () {
	
	}
	
	// Update is called once per frame
	void Update () {
	
	}

    internal void doVoice()
    {
        byte[] buffer = new byte[SampleFrequency * SampleSize];
        //Channel c = new Channel();
        CREATESOUNDEXINFO exinfo = new CREATESOUNDEXINFO();
   
        byte[] samplebuffer = new byte[SampleSize];

        for (int i = 0; i < SampleFrequency; i++)
        {
            samplebuffer = BitConverter.GetBytes(Convert.ToInt16(Math.Round(amplitude * (Math.Sin(2 * Math.PI * frequency / SampleFrequency * i + phase)) * Int16.MaxValue)));
            Array.Copy(samplebuffer, 0, buffer, i * SampleSize, SampleSize);
        }

        exinfo.cbsize = Marshal.SizeOf(exinfo);
        exinfo.length = (uint)buffer.Length;
        result = system.createSound(buffer, MODE.DEFAULT, ref exinfo, ref sound);
        ERRCHECK(result);
        result = system.playSound(CHANNELINDEX.FREE, sound, false, ref c);

        ERRCHECK(result);
    }
 
}
}
