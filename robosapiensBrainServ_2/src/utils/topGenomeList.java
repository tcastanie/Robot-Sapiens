package utils;

import robosapiensNeuralNetwork.*;
import java.util.ArrayList;

public class topGenomeList extends ArrayList<Genome> {
	int maxSize = 1;
	
	public topGenomeList(int maxSizein)
	{
		maxSize = maxSizein;
	}
	
	public boolean add(Genome g)
	{
		int i = 0;

		if((!containsId(g.ID)))
		{
			while(i < this.size() &&  g.fitness < get(i).fitness)
				i++;
			super.add(i, g);
			i = this.size()-1;
			while(i >= maxSize)
			{
				this.remove(i);
				i--;
			}			
		}
		return true;
	}

	private boolean containsId(int iD) {
		for(Genome g : this)
			if(g.ID == iD)
				return true;
		return false;
	}
}
