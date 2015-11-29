using UnityEngine;
using System.Collections;
using Leap;

public class spawnCubes : MonoBehaviour {

    public GameObject cube;
    public GameObject cubeFantome;
    public HandController myLeap;
    public GameObject mur;
    public GameObject murFantome;
    public GameObject healthPack;
    public GameObject healthPackFantome;

    int idObjet;            //0 = cube, 1 = mur, 2 = healthPack
    Quaternion rotation;
    private bool hover = false;

    void Start() {
        idObjet = 0;
        //myLeap.EnableGesture(Gesture.GestureType.TYPE_SCREEN_TAP);
    }

    void onHover() {
        Debug.Log("hover");
        hover = true;
    }



    // Update is called once per frame



    void Update() {

        if (Input.GetKeyDown(KeyCode.C)) {
            if (idObjet == 0 || idObjet == 1) {
                idObjet += 1;
            }
            else {
                idObjet = 0;
            }
        }

        rotation = Quaternion.Euler(0, GameObject.Find("cubeSpawner").transform.rotation.eulerAngles.y, 0);

        if (Input.GetMouseButtonDown(0)) {
            switch (idObjet) {
                case 0:
                    Instantiate(cubeFantome, transform.position, rotation);
                    hover = false;
                    break;
                case 1:
                    Instantiate(murFantome, transform.position, rotation);
                    break;
                case 2:
                    Instantiate(healthPackFantome, transform.position, rotation);
                    break;
                default:
                    Debug.Log("Erreur : Objet inconnu");
                    break;
            }
            //Instantiate(cubeFantome,transform.position, rotation);
        }

        if (Input.GetMouseButtonUp(0)) {
            switch (idObjet) {
                case 0:
                    Instantiate(cube, transform.position, rotation);
                    break;
                case 1:
                    Instantiate(mur, transform.position, rotation);
                    break;
                case 2:
                    Instantiate(healthPack, transform.position, rotation);
                    break;
                default:
                    Debug.Log("Erreur : Objet inconnu");
                    break;
            }
            //Instantiate(cube,transform.position, rotation);
        }
    }
}
