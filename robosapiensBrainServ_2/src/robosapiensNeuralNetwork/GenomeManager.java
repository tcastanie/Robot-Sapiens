/*
 * based on code by Matthew Robbins
 * 
 * https://github.com/matthewrdev/Neural-Network
 */

package robosapiensNeuralNetwork;

public class GenomeManager implements Runnable{
	
	
	public GenomeManager()
	{
		NeuralNetGlobals.genAlg = new GeneticAlgorithm();
		int totalWeights = NeuralNetGlobals.FEELER_COUNT * NeuralNetGlobals.hiddenLayerSize + NeuralNetGlobals.hiddenLayerSize * NeuralNetGlobals.NN_OUTPUT_COUNT + NeuralNetGlobals.hiddenLayerSize + NeuralNetGlobals.NN_OUTPUT_COUNT + 10;
		System.out.println(NeuralNetGlobals.FEELER_COUNT * NeuralNetGlobals.hiddenLayerSize + NeuralNetGlobals.hiddenLayerSize * NeuralNetGlobals.NN_OUTPUT_COUNT + NeuralNetGlobals.hiddenLayerSize + NeuralNetGlobals.NN_OUTPUT_COUNT);
		NeuralNetGlobals.genAlg.GenerateNewPopulation(NeuralNetGlobals.MAX_GENOME_POPULATION, totalWeights);
	}

	@Override
	public void run() {
		while(true)
		{
		if (NeuralNetGlobals.genAlg.GetCurrentGenomeIndex() >= NeuralNetGlobals.MAX_GENOME_POPULATION - 1)
		{
			NeuralNetGlobals.genAlg.BreedPopulation();
		}
		else
		{
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		}
	}
}
