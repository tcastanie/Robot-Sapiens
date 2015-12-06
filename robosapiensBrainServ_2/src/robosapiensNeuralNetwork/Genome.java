/*
 * based on code by Matthew Robbins
 * 
 * https://github.com/matthewrdev/Neural-Network
 */

package robosapiensNeuralNetwork;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

import robosapiensBrainServer.RobotServerGlobals;

public class Genome {

	public String name = "nameless";
	public double fitness;
	public int ID;
	ArrayList<Double> weights = new ArrayList<Double>();
	public int index=-1;
	public int generation=0;

	public void save(String path)
	{
		FileWriter fw = null;
		try {
			fw = new FileWriter(path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedWriter bw = new BufferedWriter (fw);
		PrintWriter fout = new PrintWriter (bw); 
		fout.print(name);fout.print(" ");
		fout.println(ID); 
		//fout.println("TotalWeights="+this.weights.size()); 
		for(int i = 0; i < this.weights.size(); i++){
			fout.println(weights.get(i));
		}
		fout.close();
	}

	public boolean load(String path)
	{
		File  f = new File(path);
		if(f.exists())
		{
			InputStream ips = null;
			try {
				ips = new FileInputStream(path);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			} 
			InputStreamReader ipsr=new InputStreamReader(ips);
			BufferedReader br=new BufferedReader(ipsr);
			String ligne;
			weights = new ArrayList<Double>();
			try {
				ligne=br.readLine();
				name = ligne.split(" ")[0];
				ID = Integer.parseInt(ligne.split(" ")[1]);
				while ((ligne=br.readLine())!=null){
					weights.add(Double.parseDouble(ligne));
				}
			}catch(IOException e)
			{
				e.printStackTrace();
				return false;
			}
			try {
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
		}
		else
		{
			return false;
		}
	}


}
