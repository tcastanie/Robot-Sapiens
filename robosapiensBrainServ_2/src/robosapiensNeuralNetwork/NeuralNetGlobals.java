package robosapiensNeuralNetwork;

public class NeuralNetGlobals {
	public static String messInput = "inputMessage";
	public static String messOutput = "outputMessage";
	public static String messInit = "initMessage";
	public static String messReInit = "reinitMessage";
	public static String messReward = "rewardMessage";
	public static String messFailure = "failureMessage";
	public static String sensors = "sensors";
	public static String motivator = "motivator";
/*
	// TYPE 1
	public static int nHiddenLayer = 1;
	public static int hiddenLayerSize = 12;
	public static int FEELER_COUNT = 9;
*/
	
	
	///TYPE 2
	public static int nHiddenLayer = 1;
	public static int hiddenLayerSize = 25;
	public static int FEELER_COUNT = 21;
	
	
	public static int NN_OUTPUT_COUNT = 2;
	public static int MAX_GENOME_POPULATION = 20;
	
	
	
	
	public static double MUTATION_RATE = 0.15;
	public static double MAX_PERBETUATION = 0.3f;
	public static int INVALID_ID = -1;
	public static  int MAX_POPULATION = 15;

	public static double inputFailureThreshold = 0.98;
	public static double fitnessIncreseStep = 0.00001;
	public static double fitnessReward = 0.1;
	
	public static GeneticAlgorithm genAlg;
	
}
