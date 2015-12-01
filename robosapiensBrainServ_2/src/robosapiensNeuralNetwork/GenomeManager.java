/*
 * based on code by Matthew Robbins
 * 
 * https://github.com/matthewrdev/Neural-Network
 */

package robosapiensNeuralNetwork;

public class GenomeManager implements Runnable{
	public static GeneticAlgorithm genAlg;
	
	public GenomeManager()
	{
		genAlg = new GeneticAlgorithm();
		int totalWeights = NeuralNetGlobals.FEELER_COUNT * NeuralNetGlobals.hiddenLayerSize + NeuralNetGlobals.hiddenLayerSize * NeuralNetGlobals.NN_OUTPUT_COUNT + NeuralNetGlobals.hiddenLayerSize + NeuralNetGlobals.NN_OUTPUT_COUNT;
		genAlg.GenerateNewPopulation(NeuralNetGlobals.MAX_GENOME_POPULATION, totalWeights);
	}

	@Override
	public void run() {
		
	}
}
