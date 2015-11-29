using UnityEngine;
using System.Collections;

public class selecteurBehavior : MonoBehaviour {
    

    void Start() {
    }

    // Update is called once per frame
    void Update() {
        Vector2 currentPosition = GetComponent<RectTransform>().localPosition;
        switch (varGlobales.idObjet) {
            case 0:
                currentPosition.x = 50;
                break;
            case 1:
                currentPosition.x = 150;
                break;
            case 2:
                currentPosition.x = 250;
                break;
            default:
                Debug.Log("Erreur cadre selecteur : mauvais id");
                break;
        }
        GetComponent<RectTransform>().localPosition = currentPosition;
    }
}
