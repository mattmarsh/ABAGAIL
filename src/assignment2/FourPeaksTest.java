package assignment2;

import java.util.Arrays;

import dist.DiscreteDependencyTree;
import dist.DiscreteUniformDistribution;
import dist.Distribution;

import opt.DiscreteChangeOneNeighbor;
import opt.EvaluationFunction;
import opt.GenericHillClimbingProblem;
import opt.HillClimbingProblem;
import opt.NeighborFunction;
import opt.RandomizedHillClimbing;
import opt.SimulatedAnnealing;
import opt.example.*;
import opt.ga.CrossoverFunction;
import opt.ga.DiscreteChangeOneMutation;
import opt.ga.SingleCrossOver;
import opt.ga.GenericGeneticAlgorithmProblem;
import opt.ga.GeneticAlgorithmProblem;
import opt.ga.MutationFunction;
import opt.ga.StandardGeneticAlgorithm;
import opt.prob.GenericProbabilisticOptimizationProblem;
import opt.prob.MIMIC;
import opt.prob.ProbabilisticOptimizationProblem;
import shared.FixedIterationTrainer;

/**
 * Copied from ContinuousPeaksTest
 * @version 1.0
 */
public class FourPeaksTest {
    /** The n value */
    //private static final int N = 150;
    /** The t value */
    //private static final int T = N / 5;
    
    private static final int PRINT_INTERVAL = 50;
    private static final int MAX_IT = 200000;
    private static final int MIMIC_SAMPLES = 200;
    private static final int GA_POP = 200;    
    
    public static void main(String[] args) {
    	int N = args.length > 0 ? Integer.parseInt(args[0]): 150;
    	int T = args.length > 1 ? N/Integer.parseInt(args[1]): N / 5;
    	int[] ranges = new int[N];
        Arrays.fill(ranges, 2);
        EvaluationFunction ef = new FourPeaksEvaluationFunction(T);
        Distribution odd = new DiscreteUniformDistribution(ranges);
        NeighborFunction nf = new DiscreteChangeOneNeighbor(ranges);
        MutationFunction mf = new DiscreteChangeOneMutation(ranges);
        CrossoverFunction cf = new SingleCrossOver();
        Distribution df = new DiscreteDependencyTree(.1, ranges); 
        HillClimbingProblem hcp = new GenericHillClimbingProblem(ef, odd, nf);
        GeneticAlgorithmProblem gap = new GenericGeneticAlgorithmProblem(ef, odd, mf, cf);
        ProbabilisticOptimizationProblem pop = new GenericProbabilisticOptimizationProblem(ef, odd, df);
        
        long starttime = System.currentTimeMillis();
        RandomizedHillClimbing rhc = new RandomizedHillClimbing(hcp);      
        for(int i=0; i<MAX_IT; i++)
        {
        	rhc.train();
        	if(i%PRINT_INTERVAL==0)
        	{
	            System.out.println("RHC," + i + "," + N + "," + T
		 		           + "," + ef.value(rhc.getOptimal())
	                       + "," + (System.currentTimeMillis() - starttime));
        	}
        }
        
        starttime = System.currentTimeMillis();
        SimulatedAnnealing sa = new SimulatedAnnealing(1E11, .95, hcp);
        for(int i=0; i<MAX_IT; i++)
        {
        	sa.train();
        	if(i%PRINT_INTERVAL==0)
        	{
	            System.out.println("SA," + i + "," + N + "," + T
	 		           + "," + ef.value(sa.getOptimal())
                       + "," + (System.currentTimeMillis() - starttime));
        	}
        }
        
        starttime = System.currentTimeMillis();
        StandardGeneticAlgorithm ga = new StandardGeneticAlgorithm(GA_POP, 100, 10, gap);
        for(int i=0; i<MAX_IT/GA_POP; i++)
        {
        	ga.train();
        	if(true)
        	{
	            System.out.println("GA," + i*GA_POP + "," + N + "," + T
	 		           + "," + ef.value(ga.getOptimal())
	                    + "," + (System.currentTimeMillis() - starttime));
        	}
        }
        
        starttime = System.currentTimeMillis();
        MIMIC mimic = new MIMIC(MIMIC_SAMPLES, 20, pop);
        for(int i=0; i<MAX_IT/MIMIC_SAMPLES; i++)
        {
        	mimic.train();
        	if(true)
        	{
	            System.out.println("MIMIC," + i*MIMIC_SAMPLES + "," + N + "," + T
	 		           + "," + ef.value(mimic.getOptimal())
	                    + "," + (System.currentTimeMillis() - starttime));
        	}
        }
    }
}
