package es.dfr.paramsearch;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import ec.*;
import ec.simple.*;
import ec.util.*;
import ec.vector.*;

public class MaxOnes extends Problem implements SimpleProblemForm {
	// ind is the individual to be evaluated.
	// We're given state and threadnum primarily so we
	// have access to a random number generator
	// (in the form: state.random[threadnum] )
	// and to the output facility

	// DFR parameters
    private String[] basicModelArr = { "be", "d", "g", "if", "in", "ine", "p" };
	private String[] afterEffectArr = { "no", "b", "l" };
	private String[] dfrNormalizationArr = { "no", "h1", "h2", "h3", "z" };
    // BM25 parameters
    // default k1 is 1.2
    private float[] k1 = { 0.0, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0, 1.1, 1.2, 1.3, 1.4, 1.5, 1.6, 1.7, 1.8, 1.9, 2.0, 2.1, 2.2, 2.3, 2.4};
	private float[] b = { 0.0, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.75, 0.8, 0.9, 1.0};
	private String[] discoutOverlap = { "true", "false" };
	
    private static String HOMEDIR="/home/cragkhit/es_exp/";
	private static String SCRIPT="scripts/eval_bm25.sh";
	private static String INPUTDIR="methods";
	private static String INDEXNAME="bm25";
	private static String OUTPUTDIR="eval_bm25";

	public void evaluate(final EvolutionState state, final Individual ind, final int subpopulation,
			final int threadnum) {
		if (ind.evaluated)
			return; // don't evaluate the individual if it's already evaluated
		if (!(ind instanceof IntegerVectorIndividual))
			state.output.fatal("Whoa!  It's not an IntegerVectorIndividual!!!", null);
		IntegerVectorIndividual ind2 = (IntegerVectorIndividual) ind;
		/***
		 * Call ES here
		 */
		double fitness = 0.0;
		
		String normMode = "";
		if (ind2.genome[0]==1) normMode += "d"; 
		if (ind2.genome[1]==1) normMode += "j";
		if (ind2.genome[2]==1) normMode += "k";
		if (ind2.genome[3]==1) normMode += "p";
		if (ind2.genome[4]==1) normMode += "s";
		if (ind2.genome[5]==1) normMode += "w";
        if (ind2.genome[0]==0 && ind2.genome[1]==0 && ind2.genome[2]==0
            && ind2.genome[3]==0 && ind2.genome[4]==0 && ind2.genome[5]==0)
            normMode = "x";
		
		String k1Val = k1[ind2.genome[6]];
		String bVal = afterEffectArr[ind2.genome[7]];
		String dfrNormalization = dfrNormalizationArr[ind2.genome[8]];
		int sizeOfN = ind2.genome[9] + 2;
		
		System.out.print("[" + ind2.genome[0] + "," + ind2.genome[1] + "," 
				+ ind2.genome[2] + "," + ind2.genome[3] + "," + ind2.genome[4] + "," 
				+ ind2.genome[5] + "|" + ind2.genome[6] + "|" + ind2.genome[7] + "|" 
				+ ind2.genome[8]  + "|" + ind2.genome[9] + "] = ");
		System.out.println(sizeOfN + "," + normMode + "," + basicModel + "," + afterEffect + "," + dfrNormalization);

		try {
			ProcessBuilder pb = new ProcessBuilder(SCRIPT, INPUTDIR, INDEXNAME,
					String.valueOf(sizeOfN), normMode, OUTPUTDIR, basicModel, 
					afterEffect, dfrNormalization, HOMEDIR);
			pb.directory(new File(HOMEDIR));
			Process p = pb.start();

			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			StringBuilder builder = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				builder.append(line);
				builder.append(System.getProperty("line.separator"));
			}
			String result = builder.toString();
			
			System.out.println(result);
			// parse the result to get fitness value
			fitness = Double.parseDouble(result.trim());
		} catch (IOException e) {
			e.printStackTrace();
		} 

		if (!(ind2.fitness instanceof SimpleFitness))
			state.output.fatal("Whoa!  It's not a SimpleFitness!!!", null);

		((SimpleFitness) ind2.fitness).setFitness(state, fitness, fitness == 1.0);
		ind2.evaluated = true;
	}
}
