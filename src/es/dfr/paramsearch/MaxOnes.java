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
	private String[] basicModelArr = { "be", "d", "g", "if", "in", "ine", "p" };
	private String[] afterEffectArr = { "no", "b", "l" };
	private String[] dfrNormalizationArr = { "no", "h1", "h2", "h3", "z" };

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
		
		String basicModel = basicModelArr[ind2.genome[6]];
		String afterEffect = afterEffectArr[ind2.genome[7]];
		String dfrNormalization = dfrNormalizationArr[ind2.genome[8]];
		int sizeOfN = ind2.genome[9] + 1;
		
		System.out.print("[" + ind2.genome[0] + "," + ind2.genome[1] + "," 
				+ ind2.genome[2] + "," + ind2.genome[3] + "," + ind2.genome[4] + "," 
				+ ind2.genome[5] + "|" + ind2.genome[6] + "|" + ind2.genome[7] + "|" 
				+ ind2.genome[8]  + "|" + ind2.genome[9] + "] = ");
		System.out.println(sizeOfN + "," + normMode + "," + basicModel + "," + afterEffect + "," + dfrNormalization);

		String output = "";
//		Process p;
		try {
//			p = Runtime.getRuntime()
//					.exec("/Users/Chaiyong/Documents/es_exp/scripts/eval_dfr.sh /Users/Chaiyong/Documents/es_exp/tests_extracted/ eval_dfr " + sizeOfN
//							+ " " + normMode + " /Users/Chaiyong/Documents/es_exp/eval_dfr/ " + basicModel + " " + afterEffect + " " + dfrNormalization);
//			p.waitFor();
			
			ProcessBuilder pb = new ProcessBuilder("./scripts/eval_dfr.sh", "tests_extracted/", "eval_dfr",
					String.valueOf(sizeOfN), normMode, "eval_dfr/", basicModel, afterEffect, dfrNormalization);
			pb.directory(new File("/Users/Chaiyong/Documents/es_exp/"));
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
