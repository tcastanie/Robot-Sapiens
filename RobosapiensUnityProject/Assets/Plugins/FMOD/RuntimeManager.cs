using System;
using System.Collections.Generic;
using UnityEngine;

namespace FMODUnity
{
    [AddComponentMenu("")]
    public class RuntimeManager : MonoBehaviour
    {

        static SystemNotInitializedException initException = null;
        static RuntimeManager instance;
        static RuntimeManager Instance
        {
            get
            {
                if (initException != null)
                {
                    throw initException;
                }

                if (instance == null)
                {
                    var gameObject = new GameObject("FMOD.UnityItegration.RuntimeManager");
                    instance = gameObject.AddComponent<RuntimeManager>();
                    DontDestroyOnLoad(gameObject);
                    gameObject.hideFlags = HideFlags.HideInHierarchy;

                    try
                    {
                        RuntimeUtils.EnforceLibraryOrder();
                        instance.Initialiase(false);
                    }
                    catch (Exception e)
                    {
                        initException = e as SystemNotInitializedException;
                        if (initException == null)
                        {
                            initException = new SystemNotInitializedException(e);
                        }
                        throw initException;
                    }
                }

                return instance;
            }
       
        }

        public static FMOD.Studio.System StudioSystem
        {
            get { return instance.studioSystem; }
        }


        public static FMOD.System LowlevelSystem
        {
            get { return instance.lowlevelSystem; }
        }

        FMOD.Studio.System studioSystem;
        FMOD.System lowlevelSystem;

        struct LoadedBank
        {
            public FMOD.Studio.Bank Bank;
            public int RefCount;
        }

        Dictionary<string, LoadedBank> loadedBanks = new Dictionary<string, LoadedBank>();
        Dictionary<string, uint> loadedPlugins = new Dictionary<string, uint>();
        Dictionary<Guid, FMOD.Studio.EventDescription> cachedDescriptions = new Dictionary<Guid, FMOD.Studio.EventDescription>();

        void CheckInitResult(FMOD.RESULT result, string cause)
        {
            if (result != FMOD.RESULT.OK)
            {
                if (studioSystem != null)
                {
                    studioSystem.release();
                    studioSystem = null;
                }
                throw new SystemNotInitializedException(result, cause);
            }
        }

        void Initialiase(bool forceNoNetwork)
        {
            FMOD.RESULT result;
            result = FMOD.Studio.System.create(out studioSystem);
            CheckInitResult(result, "Creating System Object");
            studioSystem.getLowLevelSystem(out lowlevelSystem);

            Settings fmodSettings = Settings.Instance;
            FMODPlatform fmodPlatform = RuntimeUtils.GetCurrentPlatform();


            #if UNITY_EDITOR || ((UNITY_STANDALONE_WIN || UNITY_STANDALONE_OSX) && DEVELOPMENT_BUILD)
                result = FMOD.Debug.Initialize(FMOD.DEBUG_FLAGS.LOG, FMOD.DEBUG_MODE.FILE, null, RuntimeUtils.LogFileName);
                CheckInitResult(result, "Applying debug settings");
            #endif

            int realChannels = fmodSettings.GetRealChannels(fmodPlatform);
            result = lowlevelSystem.setSoftwareChannels(realChannels);
            CheckInitResult(result, "Set software channels");
            result = lowlevelSystem.setSoftwareFormat(
                fmodSettings.GetSampleRate(fmodPlatform),
                (FMOD.SPEAKERMODE)fmodSettings.GetSpeakerMode(fmodPlatform),
                0 // raw not supported
                );
            CheckInitResult(result, "Set software format");

            // Setup up the platforms recommended codec to match the real channel count
            FMOD.ADVANCEDSETTINGS advancedsettings = new FMOD.ADVANCEDSETTINGS();
            #if UNITY_EDITOR || UNITY_STANDALONE
            advancedsettings.maxVorbisCodecs = realChannels;
            #elif UNITY_IOS || UNITY_ANDROID || UNITY_WP8_1 || UNITY_PSP2 || UNITY_WII
            advancedsettings.maxFADPCMCodecs = realChannels;
            #elif UNITY_XBOXONE
            advancedsettings.maxXMACodecs = realChannels;
            #elif UNITY_PS4
            advancedsettings.maxAT9Codecs = realChannels;
            #endif

            #if UNITY_5_0 || UNITY_5_1
            if (fmodSettings.IsLiveUpdateEnabled(fmodPlatform) && !forceNoNetwork)
            {
                UnityEngine.Debug.LogWarning("FMOD Studio: Detected Unity 5, running on port 9265");
                advancedsettings.profilePort = 9265;
            }
            #endif

            advancedsettings.randomSeed = (uint) DateTime.Now.Ticks;
            result = lowlevelSystem.setAdvancedSettings(ref advancedsettings);
            CheckInitResult(result, "Set advanced settings");

            FMOD.INITFLAGS lowlevelInitFlags = FMOD.INITFLAGS.NORMAL;
            FMOD.Studio.INITFLAGS studioInitFlags = FMOD.Studio.INITFLAGS.NORMAL | FMOD.Studio.INITFLAGS.DEFERRED_CALLBACKS;

            if (fmodSettings.IsLiveUpdateEnabled(fmodPlatform) && !forceNoNetwork)
            {
                studioInitFlags |= FMOD.Studio.INITFLAGS.LIVEUPDATE;
            }                       

            FMOD.RESULT initResult = studioSystem.initialize(
                fmodSettings.GetVirtualChannels(fmodPlatform),
                studioInitFlags,
                lowlevelInitFlags,
                IntPtr.Zero
                );

            CheckInitResult(initResult, "Calling initialize");

            // Dummy flush and update to get network state
            studioSystem.flushCommands();
            FMOD.RESULT updateResult = studioSystem.update();

            // Restart without liveupdate if there was a socket error
            if (updateResult == FMOD.RESULT.ERR_NET_SOCKET_ERROR)
            {
                studioSystem.release();
                UnityEngine.Debug.LogWarning("FMOD Studio: Cannot network port for Live Update, restarting with Live Update disabled. Check for other applications that are running FMOD Studio");
                Initialiase(true);
            }
            else
            {
                try
                {
                    // Always load strings bank
                    LoadBank(fmodSettings.MasterBank + ".strings", fmodSettings.AutomaticSampleLoading);

                    if (fmodSettings.AutomaticEventLoading)
                    {
                        LoadBank(fmodSettings.MasterBank, fmodSettings.AutomaticSampleLoading);

                        foreach (var bank in fmodSettings.Banks)
                        {
                            LoadBank(bank, fmodSettings.AutomaticSampleLoading);
                        }
                    }
                }
                catch (BankLoadException e)
                {
                    throw new SystemNotInitializedException(e.Result, String.Format("Loading Bank '{0}'", e.Path));
                }

                foreach (var pluginName in fmodSettings.Plugins)
                {
                    string pluginPath = RuntimeUtils.GetPluginPath(pluginName);
                    uint handle;
                    result = lowlevelSystem.loadPlugin(pluginPath, out handle);
                    CheckInitResult(result, String.Format("Loading plugin '{0}' from '{1}'", pluginName, pluginPath));
                    loadedPlugins.Add(pluginName, handle);
                }
            };
        }

        bool listenerWarningIssued = false;
        void Update()
        {
            if (studioSystem != null)
            {
                studioSystem.update();
                if (!hasListener && !listenerWarningIssued)
                {
                    listenerWarningIssued = true;
                    UnityEngine.Debug.LogWarning("FMOD Studio Integration: Please add an 'FMOD Studio Listener' component to your a camera in the scene for correct 3D positioning of sounds");
                }
            }
        }

        void OnDestroy()
        {
            if (studioSystem != null)
            {
                studioSystem.release();
                studioSystem = null;
            }
            initException = null;
            instance = null;
        }

        void OnApplicationPause(bool pauseStatus)
        {
            if (studioSystem != null && studioSystem.isValid())
			{
				if (pauseStatus)
				{
					lowlevelSystem.mixerSuspend();
				}
				else
				{
					lowlevelSystem.mixerResume();
				}
			}
        }

        public static void LoadBank(string bankName, bool loadSamples = false)
        {
            if (Instance.loadedBanks.ContainsKey(bankName))
            {
                LoadedBank loadedBank = Instance.loadedBanks[bankName];
                loadedBank.RefCount++;

                if (loadSamples)
                {
                    loadedBank.Bank.loadSampleData();
                }
            }
            else
            {
                LoadedBank loadedBank = new LoadedBank();
                string bankPath = RuntimeUtils.GetBankPath(bankName);
                var loadResult = Instance.studioSystem.loadBankFile(bankPath, FMOD.Studio.LOAD_BANK_FLAGS.NORMAL, out loadedBank.Bank);
                if (loadResult == FMOD.RESULT.OK)
                {
                    loadedBank.RefCount = 1;
                    Instance.loadedBanks.Add(bankName, loadedBank);

                    if (loadSamples)
                    {
                        loadedBank.Bank.loadSampleData();
                    }
                }
                else if (loadResult == FMOD.RESULT.ERR_EVENT_ALREADY_LOADED)
                {
                    // someone loaded this bank directly using the studio API
                    // TODO: will the null bank handle be an issue
                    loadedBank.RefCount = 2;
                    Instance.loadedBanks.Add(bankName, loadedBank);                    
                }
                else
                {
                    throw new BankLoadException(bankPath, loadResult);
                }
            }
        }

        public static void UnloadBank(string bankName)
        {
            LoadedBank loadedBank;
            if (Instance.loadedBanks.TryGetValue(bankName, out loadedBank))
            {
                loadedBank.RefCount--;
                if (loadedBank.RefCount == 0)
                {
                    loadedBank.Bank.unload();
                    Instance.loadedBanks.Remove(bankName);
                }
            }
        }

        public static Guid PathToGUID(string path)
        {
            Guid guid = Guid.Empty;
            if (path.StartsWith("{"))
            {
                FMOD.Studio.Util.ParseID(path, out guid);
            }
            else
            {
                var result = Instance.studioSystem.lookupID(path, out guid);
                if (result == FMOD.RESULT.ERR_EVENT_NOTFOUND)
                {
                    throw new EventNotFoundException(path);
                }
            }
            return guid;
        }
        
        public static FMOD.Studio.EventInstance CreateInstance(string path) 
        {
            try
            {
                return CreateInstance(PathToGUID(path)); 
            }
            catch(EventNotFoundException)
            {
                // Switch from exception with GUID to exception with path
                throw new EventNotFoundException(path);
            }
        }

        public static FMOD.Studio.EventInstance CreateInstance(Guid guid)
        {
            FMOD.Studio.EventDescription eventDesc = GetEventDescription(guid);
            FMOD.Studio.EventInstance newInstance;
            eventDesc.createInstance(out newInstance);
            return newInstance;
        }
        
        public static void PlayOneShot(string path, Vector3 position = new Vector3())
        {
            try
            {
                PlayOneShot(PathToGUID(path), position);
            }
            catch (EventNotFoundException)
            {
                // Switch from exception with GUID to exception with path
                throw new EventNotFoundException(path);
            }
        }

        public static void PlayOneShot(Guid guid, Vector3 position = new Vector3())
        {
            var instance = CreateInstance(guid);
            instance.set3DAttributes(RuntimeUtils.To3DAttributes(position));
            instance.start();
            instance.release();
        }
        
        public static FMOD.Studio.EventDescription GetEventDescription(string path)
        {
            try
            {
                return GetEventDescription(PathToGUID(path));
            }
            catch (EventNotFoundException)
            {
                throw new EventNotFoundException(path);
            }
        }

        public static FMOD.Studio.EventDescription GetEventDescription(Guid guid)
        {
            FMOD.Studio.EventDescription eventDesc = null;
            if (Instance.cachedDescriptions.ContainsKey(guid) && Instance.cachedDescriptions[guid].isValid())
            {
                eventDesc = Instance.cachedDescriptions[guid];
            }
            else
            {
                var result = Instance.studioSystem.getEventByID(guid, out eventDesc);

                if (result != FMOD.RESULT.OK)
                {
                    throw new EventNotFoundException(guid);
                }

                if (eventDesc != null && eventDesc.isValid())
                {
                    Instance.cachedDescriptions[guid] = eventDesc;
                }
            }
            return eventDesc;
        }

        static bool hasListener;
        public static bool HasListener
        {
            set { hasListener = value; }
            get { return hasListener;  }
        }

        public static void SetListenerLocation(GameObject gameObject, Rigidbody rigidBody = null)
        {
            Instance.studioSystem.setListenerAttributes(0, RuntimeUtils.To3DAttributes(gameObject, rigidBody));
        }

        public static void SetListenerLocation(Transform transform)
        {
            Instance.studioSystem.setListenerAttributes(0, transform.To3DAttributes());
        }

        public static FMOD.Studio.Bus GetBus(String path)
        {
            FMOD.RESULT result;
            FMOD.Studio.Bus bus;
            result = StudioSystem.getBus(path, out bus);
            if (result != FMOD.RESULT.OK)
            {

            }
            return bus;
        }

        public static FMOD.Studio.VCA GetVCA(String path)
        {
            FMOD.RESULT result;
            FMOD.Studio.VCA vca;
            result = StudioSystem.getVCA(path, out vca);
            if (result != FMOD.RESULT.OK)
            {

            }
            return vca;
        }
    }
}
