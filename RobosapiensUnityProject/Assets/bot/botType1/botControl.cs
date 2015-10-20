using UnityEngine;
using System.Collections;
using System;
using System.Net.Sockets;

public class botControl : MonoBehaviour {

    //Sensors Effectors
    public int maxTorque = 30;
    [SerializeField]private WheelCollider[] Wheels = new WheelCollider[2];
    private float[] currentTorque;
    [SerializeField]private GameObject[] sensors = new GameObject[8];
    
    //network
    public string serverIP = "127.0.0.0";
    public Int32 port = 28001;
    private TcpClient client;
    private NetworkStream stream;
    private int BUFF_SIZE = 512;
    //Settings
    private Rigidbody rb;

    //Debug
    public float speedRight = 0.5f;
    public float speedLeft = 0.5f;

    void Start()
    {
        rb = GetComponent<Rigidbody>();
        rb.centerOfMass = new Vector3(0.0f,0.0f,0.0f);
        //Connect();
        currentTorque = new float[Wheels.Length];
    }
	
	void Update () {
        //sendSensorData(sensors);
        getEffectorData(currentTorque);
        for(int i = 0; i < Wheels.Length; i++)
            Wheels[i].motorTorque = currentTorque[i];
    }

    private void getEffectorData(float[] currentTorque)
    {
        currentTorque[0] = speedLeft;
        currentTorque[1] = speedRight;
        /*
        Byte[] Msg = new Byte[BUFF_SIZE];
        String responseData = String.Empty;
        Int32 bytes;
        //while((bytes = stream.Read(Msg, 0, Msg.Length)) != 0);
        bytes = stream.Read(Msg, 0, Msg.Length);
        responseData = System.Text.Encoding.ASCII.GetString(Msg, 0, bytes);
        Debug.Log("server said : " + responseData);
        string[] respLines = responseData.Split('\n');
        if(respLines[0].Contains("EFFECTORS"))
        {
            for(int i = 1; i < int.Parse(respLines[0].Split(' ')[1])+1;i++)
            {
                currentTorque[int.Parse(respLines[i].Split(' ')[0])] = float.Parse(respLines[i].Split(' ')[1]);
            }
        }
        */
    }

    private void sendSensorData(GameObject[] sensors)
    {
        throw new NotImplementedException();
    }

    string makeServerRegisterMessage()
    {
        string ret = "REGISTERING " + name + "\n";
        ret += "SENSORS " + sensors.Length + "\n";
        for (int i = 0; i < sensors.Length; i++)
            ret += i + " FLOAT\n";
        ret += "EFFECTORS " + Wheels.Length + "\n";
        for (int i = 0; i < Wheels.Length; i++)
            ret += i + " FLOAT\n";

        return ret;
    }

    void Connect()
    {
        try
        {
            client = new TcpClient(serverIP, port);
            stream = client.GetStream();
            Byte[] rMsg = System.Text.Encoding.ASCII.GetBytes(makeServerRegisterMessage());
            stream.Write(rMsg, 0, rMsg.Length);
            // Buffer to store the response bytes.
            rMsg = new Byte[BUFF_SIZE];
            // String to store the response ASCII representation.
            String responseData = String.Empty;
            // Read the first batch of the TcpServer response bytes.
            Int32 bytes = stream.Read(rMsg, 0, rMsg.Length);
            responseData = System.Text.Encoding.ASCII.GetString(rMsg, 0, bytes);
            Debug.Log("bot server said : " + responseData);
        }
        catch (ArgumentNullException e)
        {
            Debug.LogError("ArgumentNullException: " + e);
        }
        catch (SocketException e)
        {
            Debug.LogError("SocketException: " + e.ToString());
        }
    }
    
    void OnApplicationQuit()
    {
        stream.Close();
        client.Close();
    }

}
