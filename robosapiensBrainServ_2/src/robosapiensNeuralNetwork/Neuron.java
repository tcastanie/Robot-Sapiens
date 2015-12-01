/*
 * based on code by Matthew Robbins
 * 
 * https://github.com/matthewrdev/Neural-Network
 */

package robosapiensNeuralNetwork;

import java.util.ArrayList;

public class Neuron {

	int numInputs;
	ArrayList<Double> weights;
	
	public void Populate(int numOfInputs)
	{
		this.numInputs = numOfInputs;
		weights = new ArrayList<Double>();
		// Initilise the weights
		for (int i = 0; i < numOfInputs; i++)
		{
			weights.add(utils.RandomClamped());
		}

		// Add an extra weight as the bias (the value that acts as a threshold in a step activation).
		weights.add(utils.RandomClamped());
	}

	public void Initilise(ArrayList<Double> weightsIn, int numOfInputs) {
		// The number of inputs should not be equal or exceed the weights.
				// If so, something is not right in the exported NN file or you've 
				// just done something odd to fuck this up.
				//assert(numOfInputs < weightsIn.size());

				this.numInputs = numOfInputs;
				weights = weightsIn;
	}


}
