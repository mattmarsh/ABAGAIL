package assignment2;

import java.text.DecimalFormat;

import assignment2.NNoptimization;
import opt.SimulatedAnnealing;

public class NNoptSA {
	
	private static DecimalFormat df = new DecimalFormat("0.000");

	public static void main(String[] args) {
		int it = args.length > 0 ? Integer.parseInt(args[0]): 100;
		double T = args.length > 1 ? Double.parseDouble(args[1]): 1E11;
		double cooling = args.length > 2 ? Double.parseDouble(args[2]): 0.95;

		String result = NNoptimization.runSA(it, T, cooling);
		
		System.out.print(result);

	}

}
