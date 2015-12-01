using UnityEngine;
using System;
using System.Collections.Generic;

namespace FMODUnity
{
    [AddComponentMenu("FMOD Studio/FMOD Studio Bank Loader")]
    public class StudioBankLoader : MonoBehaviour
    {

        public LoaderGameEvent LoadEvent;
        public LoaderGameEvent UnloadEvent;
        [BankRef]
        public List<string> Banks;
        public String CollisionTag;
        public bool PreloadSamples;
        
        bool loaded;

        void HandleGameEvent(LoaderGameEvent gameEvent)
        {
            if (LoadEvent == gameEvent)
            {
                Load();
            }
            if (UnloadEvent == gameEvent)
            {
                Unload();
            }
        }

        void OnEnable()
        {
            HandleGameEvent(LoaderGameEvent.LevelStart);
        }

        void OnApplicationQuit()
        {
            loaded = false;
        }

        void OnDisable()
        {
            HandleGameEvent(LoaderGameEvent.LevelEnd);
        }

        void OnTriggerEnter(Collider other)
        {
            if (String.IsNullOrEmpty(CollisionTag) || other.CompareTag(CollisionTag))
            {
                HandleGameEvent(LoaderGameEvent.TriggerEnter);
            }
        }

        void OnTriggerExit(Collider other)
        {
            if (String.IsNullOrEmpty(CollisionTag) || other.CompareTag(CollisionTag))
            {
                HandleGameEvent(LoaderGameEvent.TriggerExit);
            }
        }

        public void Load()
        {
            foreach (var bankRef in Banks)
            {
                try
                {
                    RuntimeManager.LoadBank(bankRef, PreloadSamples);
                }
                catch (BankLoadException e)
                {
                    UnityEngine.Debug.LogException(e);
                }
            }

            loaded = true;
        }

        public void Unload()
        {
            foreach (var bankRef in Banks)
            {
                RuntimeManager.UnloadBank(bankRef);
            }
            loaded = false;
        }
    }
}
