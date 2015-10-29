package smaapp;

abstract class FuzzyFunc {

	static final int LINE=1; // Linéaire
	static final int LOGA=2; // Logarithmique
	static final int TRIG=4; // Trigger : Seuil
	
	
	public static String nameof(int look_fonction)
	{
		if (look_fonction==LINE) return new String("Linéaire");
		else if (look_fonction==LOGA) return new String("Logarithmique");
		else if (look_fonction==TRIG) return new String("Seuillée");
		return "Unknown";
	}
	
	public static boolean xor(boolean a, boolean b)
	{
		return ((a||b)&&(!(a&&b)));
	}
	
	public static boolean iflookexist(int look_fonction)
	{
		return xor(look_fonction==LINE,xor(look_fonction==LOGA,look_fonction==TRIG));
	}
	
	public static float getXfromValue(int look_fonction,float value)
	{
		if (look_fonction==LINE) return LineXfromValue(value);
		else if (look_fonction==LOGA) return LogaXfromValue(value);
		else if (look_fonction==TRIG) return TrigXfromValue(value);
		else
		{
			System.out.println("FuzzyFunc.getXfromValue : bad look_function");
			return -1;
		}
	}
	
	protected static float LineXfromValue(float value)
	{
		return value;
	}
	
	protected static float LogaXfromValue(float value)
	{
		System.out.println("FuzzyFunc.LogaXfromValue : unimplemented");
		return value;
	}
	
	protected static float TrigXfromValue(float value)
	{
		System.out.println("FuzzyFunc.TrigXfromValue : unimplemented");
		return value;
	}
	
}
