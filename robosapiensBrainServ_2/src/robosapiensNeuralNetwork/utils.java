/*
 * based on code by Matthew Robbins
 * 
 * https://github.com/matthewrdev/Neural-Network
 */ 

package robosapiensNeuralNetwork;

public class utils {
	public static double BIAS = 0.1;
	public static int hiddenLayerSize = 10;
	
	public static double RandomClamped()
	{
		return (Math.random() * 2.0)-1.0;
	}
	
	public static double Sigmoid(double a, double p)
	{
		double ap = (-a)/p;
		return (1 / (1 + Math.exp(ap)));
	}
}
