/*
 * based on code by Matthew Robbins
 * 
 * https://github.com/matthewrdev/Neural-Network
 */

package robosapiensNeuralNetwork;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

public class NeuralNetwork {
	private int inputAmount;
	private int outputAmount;
	private ArrayList<Double> inputs = new ArrayList<Double>();
	private NLayer inputLayer;
	private ArrayList<NLayer> hiddenLayers = new ArrayList<NLayer>();
	private NLayer outputLayer;
	private ArrayList<Double> outputs = new ArrayList<Double>();
	
	
	public NeuralNetwork()
	{
//		: inputLayer(NULL)
//		, outputLayer(NULL)
		this.ExportNet("lol.txt");
		
	}


	void Update()
	{
		outputs.clear();
	
		for (int i = 0; i < hiddenLayers.size(); i++)
		{
			if (i > 0)
			{
				inputs = outputs;
			}
	
			hiddenLayers.get(i).Evaluate(inputs, outputs);
		}
	
		inputs = outputs;
		// Process the layeroutputs through the output llayer to 
		outputLayer.Evaluate(inputs, outputs);
	}

	void SetInput(ArrayList<Double> in)
	{
		inputs = in;
		//System.out.println(inputs);
	}

	double GetOutput(int ID)
	{
		if (ID >= outputAmount)
			return 0.0f;
		return outputs.get(ID);
	}

	int GetTotalOutputs()
	{
		return outputAmount;
	}
	
/*
	void ExportNet(String filename)
	{
		char buff[128] = {0};
		sprintf(buff, "ExportedNNs/%s", filename);
		std::ofstream file;
		file.open(buff);
	
		file << "<NeuralNetwork>" << endl;
		file <<"TotalOuputs=" << this->outputAmount << endl;
		file <<"TotalInputs=" << this->inputAmount << endl;
		// Export hidden layerss.
		for (unsigned int i = 0; i < hiddenLayers.size(); i++)
		{
			hiddenLayers[i]->SaveLayer(file, "Hidden");
		}
		// Export output layer.
		outputLayer->SaveLayer(file, "Output");
		file << "</NeuralNetwork>" << endl;
	
		file.close();
	} */
	
	public void ExportNet(String filename){
		FileWriter fw = null;
		try {
			fw = new FileWriter(filename);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedWriter bw = new BufferedWriter (fw);
		PrintWriter fout = new PrintWriter (bw); 
		fout.println("<NeuralNetwork>"); 
		fout.println("TotalOuputs="+this.outputAmount); 
		fout.println("TotalInputs="+this.inputAmount); 
		for(int i = 0; i < this.hiddenLayers.size(); i++){
			this.hiddenLayers.get(i).SaveLayer(fout, "Hidden");
		}
		this.outputLayer.SaveLayer(fout, "Output");
		fout.println("</NeuralNetwork>");
		fout.close();
	}
	
	public void loadNet(String filename){
		InputStream ips = null;
		try {
			ips = new FileInputStream(filename);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		InputStreamReader ipsr=new InputStreamReader(ips);
		BufferedReader br=new BufferedReader(ipsr);
		String ligne;
		float weight = 0.0f;
		int totalNeurons = 0;
		int totalWeights = 0;
		int totalInputs = 0;
		int currentNeuron = 0;
		ArrayList<Neuron> neurons = new ArrayList<Neuron>();
		ArrayList<Double> weights = new ArrayList<Double>();
		String type = "HIDDEN";
		try {
			while ((ligne=br.readLine())!=null){
				if(ligne == "<NeuralNetwork>"){
					//DEBUT PROG					
				}else if(ligne == "</NeuralNetwork>"){
					//FIN PROG
					break;
				}else if(ligne == "<NLayer>"){
					//DEBUT LAYER
					//on réinit tout
					weight = 0.0f;
					totalNeurons = 0;
					totalWeights = 0;
					totalInputs = 0;
					currentNeuron = 0;
					neurons = new ArrayList<Neuron>();
					type = "HIDDEN";
				}else if(ligne == "</NLayer>"){
					//FIN LAYER
					//On add le layer
					NLayer layer = new NLayer();
					layer.SetNeurons(neurons, neurons.size(), totalInputs);
					if(type == "HIDDEN"){
						this.hiddenLayers.add(layer);
					}else if(type == "OUTPUT"){
						this.outputLayer = layer;
					}
				}else if(ligne == "<Neuron>"){
					//DEBUT NEURONE
					//On réinit les poids
					weights = new ArrayList<Double>();
				}else if(ligne == "</Neuron>"){
					//FIN NEURONE
					Neuron n = new Neuron();
					n.Initilise(weights, totalInputs);
					neurons.add(n);		
					currentNeuron++; //?
				}else{
					//SINON ON RECUPERE LES VALEURS
					String[] parts = ligne.split("=");
					if(parts.length > 1){					
						if(parts[0] == "Type"){
							if(parts[1] == "Hidden"){
								type = "HIDDEN";
							}else if(parts[1] == "Output"){
								type = "OUTPUT";
							}
						}else if(parts[0] == "Inputs"){
							totalInputs = Integer.parseInt(parts[1]);
						}else if(parts[0] == "Neurons"){
							totalNeurons = Integer.parseInt(parts[1]);
						}else if(parts[0] == "Weights"){
							totalWeights = Integer.parseInt(parts[1]);
						}else if(parts[0] == "W"){
							weight = Float.parseFloat(parts[1]);
						}else if(parts[0] == "TotalOuputs"){
							outputAmount = Integer.parseInt(parts[1]);
						}else if(parts[0] == "TotalInputs"){
							inputAmount = Integer.parseInt(parts[1]);
						}
					}
				}
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			ips.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*
	void NeuralNet::LoadNet(char* filename)
	{
		FILE* file = fopen(filename,"rt");
	
		if(file!=NULL)
		{
			enum LayerType
			{
				HIDDEN,
				OUTPUT,
			};
	
			float weight = 0.0f;
			int totalNeurons = 0;
			int totalWeights = 0;
			int totalInputs = 0;
			int currentNeuron = 0;
			std::vector<Neuron> neurons;
			std::vector<float> weights;
			LayerType type = HIDDEN;
	
	
			char buffComp[1024] ={0};
	
			while(fgets(buffComp,1024,file))
			{
				char buff[1024] = {0};
	
				if(buffComp[strlen(buffComp)-1]=='\n')
				{
					for(unsigned int i = 0; i<strlen(buffComp)-1;i++)
					{
						buff[i] = buffComp[i];
					}
				}
	
				if(0 == strcmp(buff, "<NeuralNetwork>"))
				{
				}
				else if (0 == strcmp(buff,"</NeuralNetwork>"))
				{
					break;
				}
				else if (0 == strcmp(buff,"<NLayer>"))
				{
					weight = 0.0f;
					totalNeurons = 0;
					totalWeights = 0;
					totalInputs = 0;
					currentNeuron = 0;
					neurons.clear();
					type = HIDDEN;
				}
				else if (0 == strcmp(buff,"</NLayer>"))
				{
					NLayer* layer = new NLayer();
					layer->SetNeurons(neurons, neurons.size(), totalInputs);
					switch (type)
					{
					case HIDDEN:
						this->hiddenLayers.push_back(layer);
						layer = NULL;
						break;
					case OUTPUT:
						this->outputLayer = layer;
						layer = NULL;
						break;
					};
				}
				else if (0 == strcmp(buff,"<Neuron>"))
				{
					weights.clear();
				}
				else if (0 == strcmp(buff,"</Neuron>"))
				{
					neurons[currentNeuron].Initilise(weights, totalInputs);
					currentNeuron++;
				}
			
				else
				{
					char* token = strtok(buff, "=");
					if(token != NULL)
					{
						char* value = strtok(NULL,"=");
	
	
						if (0 == strcmp(token,"Type"))
						{
							if (0 == strcmp("Hidden", value))
							{
								type = HIDDEN;
							}
							else if (0 == strcmp("Output", value))
							{
								type = OUTPUT;
							}
						}
						else if (0 == strcmp(token,"Inputs"))
						{
							totalInputs = atoi(value);
						} 
						else if (0 == strcmp(token,"Neurons"))
						{
							totalNeurons = atoi(value);
						} 
						else if (0 == strcmp(token,"Weights"))
						{
							totalWeights = atoi(value);
						} 
						else if (0 == strcmp(token,"W"))
						{
							weight = (float)atof(value);
						} 
						else if (0 == strcmp(token,"TotalOuputs"))
						{
							outputAmount = atoi(value);
						} 
						else if (0 == strcmp(token,"TotalInputs"))
						{
							inputAmount = atoi(value);
						} 
					}
				}
			}
			fclose(file);
		}
	}
*/


	void ReleaseNet()
	{
		if (inputLayer != null)
		{
			
			inputLayer=new NLayer();
		}
		if (outputLayer != null)
		{
			
			outputLayer=new NLayer();
		}
		/*
		for (int i = 0; i < hiddenLayers.size(); i++)
		{
			if (hiddenLayers.get(i) != null)
			{
				delete hiddenLayers[i];
				hiddenLayers[i] = NULL;
			}
		}*/
		if (hiddenLayers != null)
		{
			hiddenLayers= new ArrayList<NLayer>();
		}
	}

	int GetNumOfHiddenLayers()
	{
		return hiddenLayers.size();
	}

	Genome ToGenome()
	{
		Genome genome = new Genome();
		for (int i = 0; i < this.hiddenLayers.size(); i++)
		{
			ArrayList<Double> weights = new ArrayList<Double>();
			hiddenLayers.get(i).GetWeights(weights);
			for (int j = 0; j < weights.size(); j++)
			{
				genome.weights.add(weights.get(j));
			}
		}
			
		ArrayList<Double> weights= new ArrayList<Double>();
		outputLayer.GetWeights(weights);
		for (int i = 0; i < weights.size(); i++)
		{
			genome.weights.add(weights.get(i));
		}
		
		return genome;
	}

	void CreateNet(int numOfHiddenLayers, int numOfInputs, int neuronsPerHidden, int numOfOutputs)
	{
		System.out.println("new net with : \n "+numOfHiddenLayers+" hiddenlayers of "+neuronsPerHidden + " neurons");
		inputAmount = numOfInputs;
		outputAmount = numOfOutputs;
	
		for (int i = 0; i < numOfHiddenLayers; i++)
		{
			NLayer layer = new NLayer();
			layer.PopulateLayer(neuronsPerHidden, numOfInputs);
			hiddenLayers.add(layer);
		}
	
		outputLayer = new NLayer();
		outputLayer.PopulateLayer(neuronsPerHidden, numOfOutputs);		
	}
	
	
	void FromGenome(Genome genome, int numOfInputs, int neuronsPerHidden,int numOfHiddenLayers ,int numOfOutputs)
	{		
		ReleaseNet();
		ArrayList<Neuron> neurons;
		outputAmount = numOfOutputs;
		inputAmount = numOfInputs;
		int weightsForHidden = numOfInputs * neuronsPerHidden;
		for (int x = 0; x < numOfHiddenLayers; x++)
		{
		NLayer hidden = new NLayer();
		hidden.totalInputs = numOfInputs;
		neurons = new ArrayList<Neuron>((neuronsPerHidden));
//		neurons.resize(neuronsPerHidden);
		for (int i = 0; i < neuronsPerHidden; i++)
		{
			neurons.add(new Neuron());
			ArrayList<Double> weights = new ArrayList<Double>((numOfInputs + 1));
			//weights.resize(numOfInputs + 1);
			for (int j = 0; j < numOfInputs + 1; j++)
			{
				weights.add(genome.weights.get(i * neuronsPerHidden + j));
			}
			neurons.get(i).Initilise(weights, numOfInputs);
		}
		hidden.LoadLayer(neurons);
		this.hiddenLayers.add(hidden);
		}	
		// Clear weights and reasign the weights to the output.
		int weightsForOutput = neuronsPerHidden * numOfOutputs;
		neurons= new ArrayList<Neuron>(numOfOutputs);
		//neurons.resize(numOfOutputs);
		for (int i = 0; i < numOfOutputs; i++)
		{
			neurons.add(new Neuron());
			ArrayList<Double> weights = new ArrayList<Double>((numOfInputs + 1));
			//weights.resize(neuronsPerHidden + 1);
			for (int j = 0; j < neuronsPerHidden + 1; j++)
			{
				weights.add(genome.weights.get(i * neuronsPerHidden + j));
			}
			neurons.get(i).Initilise(weights, neuronsPerHidden);
		}
		outputLayer = new NLayer();
		outputLayer.totalInputs = numOfOutputs;
		this.outputLayer.LoadLayer(neurons);
	}

}
