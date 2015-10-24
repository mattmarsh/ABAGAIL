package assignment2;

import java.util.Arrays;
import java.util.Random;

import opt.ga.MaxKColorFitnessFunction;
import opt.ga.Vertex;

import dist.DiscreteDependencyTree;
import dist.DiscretePermutationDistribution;
import dist.DiscreteUniformDistribution;
import dist.Distribution;
import opt.DiscreteChangeOneNeighbor;
import opt.EvaluationFunction;
import opt.SwapNeighbor;
import opt.GenericHillClimbingProblem;
import opt.HillClimbingProblem;
import opt.NeighborFunction;
import opt.RandomizedHillClimbing;
import opt.SimulatedAnnealing;
import opt.ga.CrossoverFunction;
import opt.ga.DiscreteChangeOneMutation;
import opt.ga.SingleCrossOver;
import opt.ga.SwapMutation;
import opt.ga.GenericGeneticAlgorithmProblem;
import opt.ga.GeneticAlgorithmProblem;
import opt.ga.MutationFunction;
import opt.ga.StandardGeneticAlgorithm;
import opt.ga.UniformCrossOver;
import opt.prob.GenericProbabilisticOptimizationProblem;
import opt.prob.MIMIC;
import opt.prob.ProbabilisticOptimizationProblem;
import shared.FixedIterationTrainer;
import shared.Instance;

/**
 * 
 * @author kmandal
 * @version 1.0
 */
public class MaxKColoringTest {
    /** The n value */
    //private static final int N = 50; // number of vertices
    private static final int L =4; // L adjacent nodes per vertex
    private static final int K = 8; // K possible colors
    private static final int MIMIC_SAMPLES = 200;
    private static final int GA_POP = 200;
    private static final int PRINT_INTERVAL = 10;
    /**
     * The test main
     * @param args ignored
     */
    public static void main(String[] args) {
        int N = args.length > 0 ? Integer.parseInt(args[0]): 10;
        //int it = args.length > 1 ? Integer.parseInt(args[1]): 1000;
        int it = 20000;
        Random random = new Random(N*L);
        // create the random velocity
        Vertex[] vertices = new Vertex[N];
        for (int i = 0; i < N; i++) {
            Vertex vertex = new Vertex();
            vertices[i] = vertex;	
            vertex.setAdjMatrixSize(L);
            for(int j = 0; j <L; j++ ){
            	 vertex.getAadjacencyColorMatrix().add(random.nextInt(N*L));
            }
        }
        /*for (int i = 0; i < N; i++) {
            Vertex vertex = vertices[i];
            System.out.println(Arrays.toString(vertex.getAadjacencyColorMatrix().toArray()));
        }*/
        // for rhc, sa, and ga we use a permutation based encoding
        MaxKColorFitnessFunction ef = new MaxKColorFitnessFunction(vertices);
        Distribution odd = new DiscretePermutationDistribution(K);
        NeighborFunction nf = new SwapNeighbor();
        MutationFunction mf = new SwapMutation();
        CrossoverFunction cf = new SingleCrossOver();
        HillClimbingProblem hcp = new GenericHillClimbingProblem(ef, odd, nf);
        GeneticAlgorithmProblem gap = new GenericGeneticAlgorithmProblem(ef, odd, mf, cf);
        
        Distribution df = new DiscreteDependencyTree(.1); 
        ProbabilisticOptimizationProblem pop = new GenericProbabilisticOptimizationProblem(ef, odd, df);
        
        long starttime = System.currentTimeMillis();
        RandomizedHillClimbing rhc = new RandomizedHillClimbing(hcp);      
        for(int i=0; i<it; i++)
        {
        	rhc.train();
        	if(i%PRINT_INTERVAL==0)
        	{
	            System.out.println("RHC," + i + "," + N + "," + L + "," + K 
	 		           + "," + ef.value(rhc.getOptimal()) + "," + !ef.getConflict()
	                    + "," + (System.currentTimeMillis() - starttime));
        	}
        }
        
        starttime = System.currentTimeMillis();
        SimulatedAnnealing sa = new SimulatedAnnealing(1E12, .1, hcp);
        for(int i=0; i<it; i++)
        {
        	sa.train();
        	if(i%PRINT_INTERVAL==0)
        	{
	            System.out.println("SA," + i + "," + N + "," + L + "," + K 
	 		           + "," + ef.value(sa.getOptimal()) + "," + !ef.getConflict()
	                    + "," + (System.currentTimeMillis() - starttime));
        	}
        }
        
        starttime = System.currentTimeMillis();
        StandardGeneticAlgorithm ga = new StandardGeneticAlgorithm(GA_POP, 10, 60, gap);
        for(int i=0; i<it/GA_POP; i++)
        {
        	ga.train();
        	if(true)
        	{
	            System.out.println("GA," + i*GA_POP + "," + N + "," + L + "," + K 
	 		           + "," + ef.value(ga.getOptimal()) + "," + !ef.getConflict()
	                    + "," + (System.currentTimeMillis() - starttime));
        	}
        }
        
        starttime = System.currentTimeMillis();
        MIMIC mimic = new MIMIC(MIMIC_SAMPLES, 100, pop);
        for(int i=0; i<it/MIMIC_SAMPLES; i++)
        {
        	mimic.train();
        	if(true)
        	{
	            System.out.println("MIMIC," + i*MIMIC_SAMPLES + "," + N + "," + L + "," + K 
	 		           + "," + ef.value(mimic.getOptimal()) + "," + !ef.getConflict()
	                    + "," + (System.currentTimeMillis() - starttime));
        	}
        }
        
    }
}
