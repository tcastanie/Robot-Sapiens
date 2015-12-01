using System;
using System.Collections.Generic;
using System.Text;
using UnityEngine;
using UnityEditor;

namespace FMODUnity
{
    [InitializeOnLoad]
    public class MigrationUtil : MonoBehaviour
    {
        [MenuItem("FMOD/Migration From Legacy Integration")]
        public static void ShowMigrationDialog()
        {
            if (EditorUtility.DisplayDialog("FMOD Studio Integration Migration", "Are you sure you wish to migrate.\nPlease backup your Unity project first.", "OK", "Cancel"))
            {
                Migrate();
            }
        }

        public static void Migrate()
        {
            Undo.IncrementCurrentGroup();
            Undo.SetCurrentGroupName("FMOD Studio Integration Migration");

            Settings settings = Settings.Instance;

            var prefKey = "FMODStudioProjectPath_" + Application.dataPath;
            var prefValue = EditorPrefs.GetString(prefKey);
            if (prefValue != null)
            {
                settings.SourceBankPath = prefValue as string;
                settings.SourceBankPath += "/Build";
                settings.HasSourceProject = false;
            }

            // for each level
            EditorApplication.SaveCurrentSceneIfUserWantsTo();

            var scenes = AssetDatabase.FindAssets("*.scene");
            foreach (string scene in scenes)
            {
                if (!EditorUtility.DisplayDialog("FMOD Studio Integration Migration", String.Format("Migrate scene {0}", AssetDatabase.GUIDToAssetPath(scene)), "OK", "Cancel"))
                {
                    continue;
                }

                EditorApplication.OpenScene(AssetDatabase.GUIDToAssetPath(scene));

                var emitters = FindObjectsOfType<FMOD_StudioEventEmitter>();
                foreach (var emitter in emitters)
                {
                    GameObject parent = emitter.gameObject;
                    bool startOnAwake = emitter.startEventOnAwake;
                    string path = null;
                    if (emitter.asset != null)
                    {
                        path = emitter.asset.path;
                    }
                    else if (!String.IsNullOrEmpty(emitter.path))
                    {
                        path = emitter.path;
                    }
                    else
                    {
                        continue;
                    }

                    Undo.DestroyObjectImmediate(emitter);

                    var newEmitter = Undo.AddComponent<StudioEventEmitter>(parent);
                    newEmitter.Event = path;
                    newEmitter.PlayEvent = startOnAwake ? EmitterGameEvent.LevelStart : EmitterGameEvent.None;
                    newEmitter.PlayEvent = startOnAwake ? EmitterGameEvent.LevelEnd : EmitterGameEvent.None;
                }


                var listeners = FindObjectsOfType<FMOD_Listener>();

                foreach (var listener in listeners)
                {
                    GameObject parent = listener.gameObject;

                    foreach (var plugin in listener.pluginPaths)
                    {
                        if (!settings.Plugins.Contains(plugin))
                        {
                            settings.Plugins.Add(plugin);
                        }
                    }

                    Undo.DestroyObjectImmediate(listener);
                    Undo.AddComponent<StudioListener>(parent);
                }

                EditorApplication.SaveCurrentSceneIfUserWantsTo();
            }

            EditorUtility.SetDirty(settings);

            AssetDatabase.DeleteAsset("Assets/FMODAssets");
            AssetDatabase.Refresh();
        }
    }
}
