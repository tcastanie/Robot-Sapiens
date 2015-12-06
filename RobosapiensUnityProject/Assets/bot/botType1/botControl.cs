using UnityEngine;
using System.Collections;
using System;
using System.Net.Sockets;

public class botControl : MonoBehaviour {

    //Sensors Effectors
    public string BotName = "robob";
    public float maxTorque = 30.0f;
    public float topSpeed = 10.0f;
    [SerializeField]private WheelCollider[] Wheels = new WheelCollider[2];
    private float[] currentTorque;
    [SerializeField]private GameObject[] sensors = new GameObject[8];
    [SerializeField]private GameObject[] motivators = new GameObject[1];

    //network
    public string serverIP = "127.0.0.1";
    public Int32 port = 28001;
    private TcpClient client;
    private NetworkStream stream;
    private int BUFF_SIZE = 512;
    //Settings
    private Rigidbody rb;

    double reward = 0;

    void Start()
    {
        rb = GetComponent<Rigidbody>();
        rb.centerOfMass = new Vector3(0.0f,0.0f,0.0f);
        Connect();
        currentTorque = new float[Wheels.Length];
    }
	
	void Update () {
        sendSensorData(sensors);
        sendMotivatorData(motivators);
        sendReward();
        getEffectorData(currentTorque);
        float speed = rb.velocity.sqrMagnitude;
        for (int i = 0; i < Wheels.Length; i++)
            Wheels[i].motorTorque = currentTorque[i] * maxTorque * (1.0f-(speed/topSpeed));
    }

    private void sendReward()
    {
        string msg = "REWARDS " + reward + "\n";
        Byte[] rMsg = System.Text.Encoding.ASCII.GetBytes(msg);
        stream.Write(rMsg, 0, rMsg.Length);
        reward = 0.0;
    }

    internal void addReward(double v)
    {
        reward += v;
    }

    private void getEffectorData(float[] currentTorque)
    {
        currentTorque[0] = 0.0f;
        currentTorque[1] = 0.0f;
        
        Byte[] Msg = new Byte[BUFF_SIZE];
        String responseData = String.Empty;
        Int32 bytes;
        //while((bytes = stream.Read(Msg, 0, Msg.Length)) != 0);
        bytes = stream.Read(Msg, 0, Msg.Length);
        responseData = System.Text.Encoding.ASCII.GetString(Msg, 0, bytes);
        //Debug.Log("server said : \n" + responseData);
        string[] respLines = responseData.Split('\n');
        int pos = 1;
        if (respLines[0].Contains("MOTIVATOR"))
        {
            for (int i = 1; i < int.Parse(respLines[0].Split(' ')[1]) + 1; i++)
            {
                //Debug.Log("motiv");
                ((motivators[int.Parse(respLines[i].Split(' ')[0])].GetComponent<abstractMotivatorScript>())).sendControlMessage(respLines[i].Split(' ')[1]);
                pos = i;
            }
            if (respLines[pos + 1].Contains("EFFECTORS"))
            {
                //            Debug.Log("found effect");

                for (int i = pos + 2; i < pos + 2 + int.Parse(respLines[pos + 1].Split(' ')[1]); i++)
                {
                    //              Debug.Log("effect");
                    //             Debug.Log(respLines[i].Split(' ')[1]);
                    currentTorque[int.Parse(respLines[i].Split(' ')[0])] = (float)Double.Parse(respLines[i].Split(' ')[1]);
                    //Debug.Log(currentTorque[int.Parse(respLines[i].Split(' ')[0])]);
                }
            }

        }

    }

    private void sendSensorData(GameObject[] sensors)
    {
        string msg = "SENSORS " + sensors.Length + "\n";

        for (int i = 0; i < sensors.Length; i++)
            msg += i + " " + getNormalisedSensorData(sensors[i]) + "\n";
        Byte[] rMsg = System.Text.Encoding.ASCII.GetBytes(msg);
        stream.Write(rMsg, 0, rMsg.Length);

        //Debug.Log(msg);
    }

    string getMotivatorMessage(GameObject motivator)
    {
        abstractMotivatorScript scr = motivator.GetComponent<abstractMotivatorScript>();
        return (scr.state);
    }

    private void sendMotivatorData(GameObject[] motivators)
    {
        string msg = "MOTIVATORS " + motivators.Length + "\n";

        for (int i = 0; i < motivators.Length; i++)
            msg += i + " " + getMotivatorMessage(motivators[i]) + "\n";
        Byte[] rMsg = System.Text.Encoding.ASCII.GetBytes(msg);
        stream.Write(rMsg, 0, rMsg.Length);

        //Debug.Log(msg);
    }

    private double getNormalisedSensorData(GameObject sensor)
    {
        abstractSensorScript scr = sensor.GetComponent<abstractSensorScript>();
        return  (scr.normalizedValue);

    }

    string makeServerRegisterMessage()
    {
        string ret = "REGISTERING " + BotName + "\n";
        ret += "SENSORS " + sensors.Length + "\n";
        for (int i = 0; i < sensors.Length; i++)
            ret += i + " FLOAT\n";
        ret += "MOTIVATORS " + motivators.Length + "\n";
        for (int i = 0; i < motivators.Length; i++)
            ret += i + " STRING\n";
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
            //Debug.Log("bot server said : " + responseData);
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
