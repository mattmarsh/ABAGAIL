package assignment2;

import dist.*;
import opt.*;
import opt.example.*;
import opt.ga.*;
import shared.*;
import func.nn.backprop.*;
import func.nn.activation.*;

import shared.DataSet;
import shared.DataSetDescription;
import shared.reader.CSVDataSetReader;
import shared.reader.DataSetReader;

import java.util.*;
import java.io.*;
import java.nio.file.Paths;
import java.text.*;

/**
 * Implementation of randomized hill climbing, simulated annealing, and genetic algorithm to
 * find optimal weights to a neural network that is classifying breast tissues instances as either 
 * benign or malignant. 
 *
 * @author Adam Acosta
 * @version 1.0
 */
public class NNoptimization {

    private static Instance[] trainInstances = initializeInstances("bcTrainData.csv", "bcTrainLabels.csv");
    private static Instance[] testInstances = initializeInstances("bcTestData.csv", "bcTestLabels.csv");

    private static int inputLayer = 30, hiddenLayer = 8, outputLayer = 1;
    private static BackPropagationNetworkFactory factory = new BackPropagationNetworkFactory();
    
    private static ErrorMeasure measure = new SumOfSquaresError();

    private static DataSet set = new DataSet(trainInstances);

    private static BackPropagationNetwork networks[] = new BackPropagationNetwork[3];
    private static NeuralNetworkOptimizationProblem[] nnop = new NeuralNetworkOptimizationProblem[3];

    private static OptimizationAlgorithm[] oa = new OptimizationAlgorithm[3];
    private static String[] oaNames = {"RHC", "SA", "GA"};
    private static String results = "";

    private static DecimalFormat df = new DecimalFormat("0.000");

    private static Instance[] initializeInstances(String dataFile, String labelFile) {

        DataSetReader dsr = new CSVDataSetReader(new File("src/assignment2/" + dataFile).getAbsolutePath());
        DataSetReader lsr = new CSVDataSetReader(new File("src/assignment2/" + labelFile).getAbsolutePath());
        DataSet ds;
        DataSet labs;

        try {
            ds = dsr.read();
            labs = lsr.read();
            Instance[] instances = ds.getInstances();
            Instance[] labels = labs.getInstances();

            for(int i = 0; i < instances.length; i++) {
                instances[i].setLabel(new Instance(labels[i].getData().get(0)));
            }

            return instances;
        } catch (Exception e) {
            System.out.println("Failed to read input file");
            return null;
        }
    }

    public static void main(String[] args) {

        int it = args.length > 0 ? Integer.parseInt(args[0]): 100;

        for(int i = 0; i < oa.length; i++) {
            networks[i] = factory.createClassificationNetwork(
                new int[] {inputLayer, hiddenLayer, outputLayer},
                new LogisticSigmoid());
            nnop[i] = new NeuralNetworkOptimizationProblem(set, networks[i], measure);
        }

        oa[0] = new RandomizedHillClimbing(nnop[0]);
        oa[1] = new SimulatedAnnealing(1E11, .95, nnop[1]);
        oa[2] = new StandardGeneticAlgorithm(200, 100, 10, nnop[2]);
        
        //PrintWriter writer;
        //try
        //{
        //	writer = new PrintWriter(new File("output/NNoptResults.csv").getAbsolutePath());
	    //} catch (Exception e) {
	    //    System.out.println("Failed to open output file");
	    //    return;
	    //}     
        
        for(int i = 0; i < oa.length; i++) {
            double start = System.nanoTime(), end, trainingTime, testingTime, correct = 0, incorrect = 0;
            for(int j = 0; j < it; j++) {
                oa[i].train();
            }
            end = System.nanoTime();

            trainingTime = end - start;
            trainingTime /= Math.pow(10,9);

            Instance optimalInstance = oa[i].getOptimal();
            networks[i].setWeights(optimalInstance.getData());

            double predicted, actual;
            start = System.nanoTime();
            for(int j = 0; j < trainInstances.length; j++) {
                networks[i].setInputValues(trainInstances[j].getData());
                networks[i].run();

                predicted = Double.parseDouble(trainInstances[j].getLabel().toString());
                actual = Double.parseDouble(networks[i].getOutputValues().toString());

                double trash = Math.abs(predicted - actual) < 0.5 ? correct++ : incorrect++;

            }
            end = System.nanoTime();
            testingTime = end - start;

            results +=  oaNames[i] + "," + it + "," + df.format(incorrect / (correct + incorrect)) + ","; 

            correct = 0;
            incorrect = 0;
            start = System.nanoTime();
            for(int j = 0; j < testInstances.length; j++) {
                networks[i].setInputValues(testInstances[j].getData());
                networks[i].run();

                predicted = Double.parseDouble(testInstances[j].getLabel().toString());
                actual = Double.parseDouble(networks[i].getOutputValues().toString());

                double trash = Math.abs(predicted - actual) < 0.5 ? correct++ : incorrect++;

            }
            end = System.nanoTime();
            testingTime += end - start;
            testingTime /= 2.0;
            testingTime /= Math.pow(10, 9);

            results += df.format(incorrect / (correct + incorrect)) + ","
                     + df.format(trainingTime) + "," + df.format(testingTime) + "\n";
            //writer.print(results);
        }
        System.out.print(results);
        //writer.close();
    }

}