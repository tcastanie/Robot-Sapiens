/*
 * based on code by Matthew Robbins
 * 
 * https://github.com/matthewrdev/Neural-Network
 */

package robosapiensNeuralNetwork;

import java.util.ArrayList;

public class NLayer {

	int totalNeurons;
	int totalInputs;
	ArrayList<Neuron> neurons = new ArrayList<Neuron>();
	
	public void Evaluate(ArrayList<Double> inputs, ArrayList<Double> outputs) {
		int inputIndex = 0;
		// Cycle over all the neurons and sum their weights against the inputs.
		for (int i = 0; i < totalNeurons; i++)
		{
			double activation = 0.0f;

			// Sum the weights to the activation value.
			// We do the sizeof the weights - 1 so that we can add in the bias to the 
			// activation afterwards.
			for (int j = 0; j < neurons.get(i).numInputs - 1; j++)
			{
				activation += inputs.get(inputIndex)* neurons.get(i).weights.get(j);
				inputIndex++;
			}

			// Add the bias.
			// The bias will act as a threshold value to 
			activation += neurons.get(i).weights.get(neurons.get(i).numInputs) * utils.BIAS;

			outputs.add(utils.Sigmoid(activation, 1.0));
			inputIndex = 0;
		}
	}

	public void PopulateLayer(int numOfNeurons, int numOfInputs) {
		
		totalInputs = numOfInputs;
		totalNeurons = numOfNeurons;
		this.neurons = new ArrayList<Neuron>(numOfNeurons);
		for (int i = 0; i < numOfNeurons; i++)
		{
			neurons.add(new Neuron());
			
			neurons.get(i).Populate(numOfInputs);
		}
	}

	public void GetWeights(ArrayList<Double> out) {
		// Calculate the size of the output vector by calculating the amount of weights in each neurons.
				// Avoids having to use a nasty push-back...
/*				int size = 0;
				for (int i = 0; i < this.totalNeurons; i++)
				{
					size += neurons.get(i).weights.size();
				}*/
				out.clear();

				// Iterate over each neuron and each connection weight and retrieve the weights
				for (int i = 0; i < this.totalNeurons; i++)
				{
					for (int j = 0; j < neurons.get(i).weights.size(); j++)
					{
						out.add(neurons.get(i).weights.get(j));
					}
				}
	}
	
	void SetWeights(ArrayList<Double> weights, int numOfNeurons, int numOfInputs)
	{
		int index = 0;
		totalInputs = numOfInputs;
		totalNeurons = numOfNeurons;
		this.neurons = new ArrayList<Neuron>(numOfNeurons);
		// Copy the weights into the neurons.
		for (int i = 0; i < numOfNeurons; i++)
		{
			neurons.add(new Neuron());
			neurons.get(i).weights = new ArrayList<Double>(numOfInputs);
			for (int j = 0; j < numOfInputs; j++)
			{
				neurons.get(i).weights.add(weights.get(index));
				index++;
			}
		}
	}

	
	void SetNeurons(ArrayList<Neuron> neurons, int numOfNeurons, int numOfInputs)
	{
		totalInputs = numOfInputs;
		totalNeurons = numOfNeurons;
		this.neurons = neurons;
	}

	/*
	void NLayer::SaveLayer(std::ofstream &fileOut, char* layerType)
	{
		fileOut << "<NLayer>" << std::endl;
		fileOut << "Type=" << layerType << std::endl;
		fileOut << "Inputs=" << this->totalInputs << std::endl;
		fileOut << "Neurons=" << this->neurons.size() << std::endl;
		fileOut << "-Build-" << std::endl;
		for (unsigned int i = 0; i < this->neurons.size(); i++)
		{
			fileOut << "<Neuron>" << std::endl;
			fileOut << "Weights=" << this->neurons[i].weights.size() << std::endl;
			for (unsigned int j = 0; j < neurons[i].weights.size(); j++)
			{
				fileOut << "W=" << neurons[i].weights[j] << std::endl; 
			}
			fileOut << "</Neuron>" << std::endl;
		}
			
		fileOut << "</NLayer>" << std::endl;
	}

	void NLayer::LoadLayer(std::vector<Neuron> in)
	{
		totalNeurons = in.size();
		neurons = in;
	}
	*/
	public void LoadLayer(ArrayList<Neuron> neurons) {
		totalNeurons = neurons.size();
		neurons = neurons;
	}


}
