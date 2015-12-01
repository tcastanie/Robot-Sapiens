using System;
using UnityEngine;

namespace FMODUnity
{
    public class EventNotFoundException : Exception
    {
        public Guid Guid;
        public string Path;
        public EventNotFoundException(string path)
            : base("FMOD Studio event not found '" + path + "'")
        {           
            Path = path;
        }

        public EventNotFoundException(Guid guid)
            : base("FMOD Studio event not found " + guid.ToString("b") + "")
        {
            Guid = guid;
        }
    }
        
    public class BusNotFoundException : Exception
    {
        public string Path;
        public BusNotFoundException(string path)
            : base("FMOD Studio bus not found '" + path + "'")
        {           
            Path = path;
        }
    }

    public class VCANotFoundException : Exception
    {
        public string Path;
        public VCANotFoundException(string path)
            : base("FMOD Studio VCA not found '" + path + "'")
        {
            Path = path;
        }
    }

    public class BankLoadException : Exception
    {
        public string Path;
        public FMOD.RESULT Result;

        public BankLoadException(string path, FMOD.RESULT result)
            : base(String.Format("FMOD Studio could not load bank '{0}' : {1} : {2}", path, result.ToString(), FMOD.Error.String(result)))
        {
            Path = path;
            Result = result;
        }
    }

    public class SystemNotInitializedException : Exception
    {
        public FMOD.RESULT Result;
        public string Location;

        public SystemNotInitializedException(FMOD.RESULT result, string location)
            : base(String.Format("FMOD Studio initialization failed : {2} : {0} : {1}", result.ToString(), FMOD.Error.String(result), location))
        {
            Result = result;
            Location = location;
        }

        public SystemNotInitializedException(Exception inner)
            : base("FMOD Studio initialization failed", inner)
        {
        }
    }

    public enum EmitterGameEvent
    {
        None,
        LevelStart,
        LevelEnd,
        TriggerEnter,
        TriggerExit,
        CollisionEnter,
        CollisionExit,
    }

    public enum LoaderGameEvent
    {
        None,
        LevelStart,
        LevelEnd,
        TriggerEnter,
        TriggerExit,
    }
    
    public static class RuntimeUtils
    {
        public const string LogFileName = "fmod.log";

        public static void EnforceLibraryOrder()
        {
            #if UNITY_ANDROID && !UNITY_EDITOR
            
            // First, obtain the current activity context
            AndroidJavaObject activity = null;
            using (var activityClass = new AndroidJavaClass("com.unity3d.player.UnityPlayer"))
            {
                activity = activityClass.GetStatic<AndroidJavaObject>("currentActivity");
            }

            using (var fmodJava = new AndroidJavaClass("org.fmod.FMOD"))
            {
                if (fmodJava != null)
                {
                    fmodJava.CallStatic("init", activity);
                }
                else
                {
                    UnityEngine.Debug.LogWarning("FMOD Studio: Cannot initialiaze Java wrapper");
                }
            }

			AndroidJavaClass jSystem = new AndroidJavaClass("java.lang.System");
			jSystem.CallStatic("loadLibrary", FMOD.VERSION.dll);
			jSystem.CallStatic("loadLibrary", FMOD.Studio.STUDIO_VERSION.dll);
            
            #endif

            #if !UNITY_IPHONE // iOS is statically linked

            // Call a function in fmod.dll to make sure it's loaded before fmodstudio.dll
            int temp1, temp2;
            FMOD.Memory.GetStats(out temp1, out temp2);

            Guid temp3;
            FMOD.Studio.Util.ParseID("", out temp3);
            
            #endif

        }

        public static FMOD.VECTOR ToFMODVector(this Vector3 vec)
        {
            FMOD.VECTOR temp;
            temp.x = vec.x;
            temp.y = vec.y;
            temp.z = vec.z;

            return temp;
        }

        public static FMOD.ATTRIBUTES_3D To3DAttributes(this Vector3 pos)
        {
            FMOD.ATTRIBUTES_3D attributes = new FMOD.ATTRIBUTES_3D();
            attributes.forward = ToFMODVector(Vector3.forward);
            attributes.up = ToFMODVector(Vector3.up);
            attributes.position = ToFMODVector(pos);

            return attributes;
        }

        public static FMOD.ATTRIBUTES_3D To3DAttributes(this Transform transform)
        {
            FMOD.ATTRIBUTES_3D attributes = new FMOD.ATTRIBUTES_3D();
            attributes.forward = transform.forward.ToFMODVector();
            attributes.up = transform.up.ToFMODVector();
            attributes.position = transform.position.ToFMODVector();

            return attributes;
        }

        public static FMOD.ATTRIBUTES_3D To3DAttributes(GameObject go, Rigidbody rigidbody = null)
        {
            FMOD.ATTRIBUTES_3D attributes = go.transform.To3DAttributes();

            if (rigidbody)
            {
                attributes.velocity = rigidbody.velocity.ToFMODVector();
            }

            return attributes;
        }

        public static FMODPlatform GetCurrentPlatform()
        {
            #if UNITY_EDITOR
            return FMODPlatform.PlayInEditor;
            #elif UNITY_STANDALONE_WIN
            return FMODPlatform.Windows;
            #elif UNITY_STANDALONE_OSX
            return FMODPlatform.Mac;
            #elif UNITY_STANDALONE_LINUX
            return FMODPlatform.Linux;
            #elif UNITY_IOS
            FMODPlatform result;
            switch (UnityEngine.iOS.Device.generation)
            {
			case UnityEngine.iOS.DeviceGeneration.iPhone5:
			case UnityEngine.iOS.DeviceGeneration.iPhone5C:
			case UnityEngine.iOS.DeviceGeneration.iPhone5S:
			case UnityEngine.iOS.DeviceGeneration.iPadAir1:
			case UnityEngine.iOS.DeviceGeneration.iPadMini2Gen:
			case UnityEngine.iOS.DeviceGeneration.iPhone6:
			case UnityEngine.iOS.DeviceGeneration.iPhone6Plus:
			case UnityEngine.iOS.DeviceGeneration.iPadMini3Gen:
			case UnityEngine.iOS.DeviceGeneration.iPadAir2:
                result = FMODPlatform.MobileHigh;
				break;
            default:
                result = FMODPlatform.MobileLow;
				break;
            }

            UnityEngine.Debug.Log(String.Format("FMOD Studio: Device {0} classed as {1}", SystemInfo.deviceModel, result.ToString()));
            return result;
            #elif UNITY_ANDROID
            FMODPlatform result;
            if (SystemInfo.processorCount <= 2)
            {
                result = FMODPlatform.MobileLow;
            }
            else if (SystemInfo.processorCount >= 8)
            {
                result = FMODPlatform.MobileHigh;
            }
            else
            {
                // check the clock rate on quad core systems            
                string freqinfo = "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq";
                using(global::System.IO.TextReader reader = new global::System.IO.StreamReader(freqinfo))
                {
                    try
                    {
                        string line = reader.ReadLine();
                        int khz = Int32.Parse(line) / 1000;
                        if (khz >= 1600)
                        {
                            result = FMODPlatform.MobileHigh;
                        }
                        else
                        {
                            result = FMODPlatform.MobileLow;
                        }
                    }
                    catch
                    {
                        // Assume worst case
                        result = FMODPlatform.MobileLow;
                    }
                }
            }
            
            UnityEngine.Debug.Log(String.Format("FMOD Studio: Device {0} classed as {1}", SystemInfo.deviceModel, result.ToString()));
            return result;
            #elif UNITY_WINRT_8_1
            FMODPlatform result;
            if (SystemInfo.processorCount <= 2)
            {
                result = FMODPlatform.MobileLow;
            }
            else
            {
                result = FMODPlatform.MobileHigh;
            }

            UnityEngine.Debug.Log(String.Format("FMOD Studio: Device {0} classed as {1}", SystemInfo.deviceModel, result.ToString()));
            return result;

            #elif UNITY_PS4
            return FMODPlatform.PS4;
            #elif UNITY_XBOXONE
            return FMODPlatform.XboxOne;
            #elif UNITY_PSP2
            return FMODPlatform.PSVita;
            #elif UNITY_WIIU
            return FMODPlatform.WiiU;
            #endif
        }

        const string BankExtension = ".bank";
        public static string GetBankPath(string bankName)
        {
            #if UNITY_EDITOR
            // For play in editor use original asset location because streaming asset folder will contain platform specific banks
			string bankFolder = Settings.Instance.SourceBankPath;
			if (Settings.Instance.HasPlatforms)
			{
				bankFolder = global::System.IO.Path.Combine(bankFolder, Settings.Instance.GetBankPlatform(FMODPlatform.PlayInEditor));
			}
            #elif UNITY_ANDROID
            string bankFolder = "file:///android_asset";
            #elif UNITY_WINRT_8_1
            string bankFolder = "ms-appx:///Data/StreamingAssets";
            #else
            string bankFolder = Application.streamingAssetsPath;
            #endif

            if (!bankName.Contains(BankExtension))
            {
                bankName += BankExtension;
            }

            return bankFolder + "/" + bankName;
        }

        public static string GetPluginPath(string pluginName)
        {
            #if UNITY_IOS
				return "";
			#else
	            #if UNITY_EDITOR_WIN || UNITY_STANDALONE_WIN || UNITY_XBOXONE || UNITY_WINRT_8_1
	                string pluginFileName = pluginName + ".dll";
	            #elif UNITY_STANDALONE_OSX || UNITY_EDITOR_OSX
					string pluginFileName = pluginName + ".bundle";
	            #elif UNITY_PS4
	                string pluginFileName = pluginName + ".prx"
	            #elif UNITY_ANDROID || UNITY_STANDALONE_LINUX
	                string pluginFileName = "lib" + pluginName + ".so";
	            #endif

	            #if UNITY_EDITOR_WIN && UNITY_EDITOR_64
	                string pluginFolder = Application.dataPath + "/Plugins/X86_64/";
	            #elif UNITY_EDITOR_WIN
	                string pluginFolder = Application.dataPath + "/Plugins/X86/";
	            #elif UNITY_STANDALONE_WIN || UNITY_PS4 || UNITY_XBOXONE || UNITY_STANDALONE_OSX || UNITY_EDITOR_OSX || UNITY_STANDALONE_LINUX
	                string pluginFolder = Application.dataPath + "/Plugins/";
	            #elif UNITY_WINRT_8_1
	                string pluginFolder = "";
	            #elif UNITY_ANDROID            
					var dirInfo = new global::System.IO.DirectoryInfo(Application.persistentDataPath);
					string packageName = dirInfo.Parent.Name;
	                string pluginFolder = "/data/data/" + packageName + "/lib/";
	            #else
	                string pluginFolder = "";
	            #endif

	            return pluginFolder + pluginFileName;
			#endif
        }
    }
}
