package robosapiensNeuralNetwork;
import java.util.ArrayList;

public class NeuralNetwork {
	private int inputAmount;
	private int outputAmount;
	private ArrayList<Double> inputs;
	private NLayer inputLayer;
	private ArrayList<NLayer> hiddenLayers;
	private NLayer outputLayer;
	private ArrayList<Double> outputs;
	
	
	public NeuralNetwork()
	{
//		: inputLayer(NULL)
//		, outputLayer(NULL)
		
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
	}

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
	void CreateNet(int numOfHiddenLayers, int numOfInputs, int neuronsPerHidden, int numOfOutputs)
	{
		inputAmount = numOfInputs;
		outputAmount = numOfOutputs;
	
		for (int i = 0; i < numOfHiddenLayers; i++)
		{
			NLayer layer = new NLayer();
			layer.PopulateLayer(neuronsPerHidden, numOfInputs);
			hiddenLayers.add(layer);
		}
	
		outputLayer = new NLayer();
		outputLayer.PopulateLayer(numOfOutputs, neuronsPerHidden);		
	}
	

	void ReleaseNet()
	{
		if (inputLayer != null)
		{
			
			inputLayer = null;
		}
		if (outputLayer != null)
		{
			
			outputLayer = null;
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
		hiddenLayers.clear();
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

	void FromGenome(Genome genome, int numOfInputs, int neuronsPerHidden, int numOfOutputs)
	{		
		ReleaseNet();
	
		outputAmount = numOfOutputs;
		inputAmount = numOfInputs;
		int weightsForHidden = numOfInputs * neuronsPerHidden;
		NLayer hidden = new NLayer();
		ArrayList<Neuron> neurons = new ArrayList<Neuron>((neuronsPerHidden));
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
	
		// Clear weights and reasign the weights to the output.
		int weightsForOutput = neuronsPerHidden * numOfOutputs;
		neurons.clear();
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
		this.outputLayer.LoadLayer(neurons);
	}

}
