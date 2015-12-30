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
		int totalWeights = NeuralNetGlobals.FEELER_COUNT * (NeuralNetGlobals.hiddenLayerSize+1) + (NeuralNetGlobals.hiddenLayerSize+1) * NeuralNetGlobals.NN_OUTPUT_COUNT + NeuralNetGlobals.hiddenLayerSize + NeuralNetGlobals.NN_OUTPUT_COUNT;
		System.out.println(NeuralNetGlobals.FEELER_COUNT * (NeuralNetGlobals.hiddenLayerSize+1) + (NeuralNetGlobals.hiddenLayerSize+1) * NeuralNetGlobals.NN_OUTPUT_COUNT + NeuralNetGlobals.hiddenLayerSize + NeuralNetGlobals.NN_OUTPUT_COUNT);
		NeuralNetGlobals.genAlg.GenerateNewPopulation(NeuralNetGlobals.MAX_GENOME_POPULATION, totalWeights);
		System.out.println(NeuralNetGlobals.FEELER_COUNT );System.out.println(NeuralNetGlobals.hiddenLayerSize );System.out.println(NeuralNetGlobals.NN_OUTPUT_COUNT );
	}

	@Override
	public void run() {
		while(true)
		{
		if (NeuralNetGlobals.genAlg.GetCurrentGenomeIndex() >= NeuralNetGlobals.genAlg.population.size()-1)
		{
			System.out.println("went through "+NeuralNetGlobals.genAlg.population.size()+" genomes");
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
