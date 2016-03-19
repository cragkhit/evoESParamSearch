package es.dfr.paramsearch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
	private String[] norms = { "s", "w", "ws", "p", "ps", "pw", "pws", "k", "ks", "kw", "kws", "kp", "kps", "kpw",
			"kpws", "j", "js", "jw", "jws", "jp", "jps", "jpw", "jpws", "jk", "jks", "jkw", "jkws", "jkp", "jkps",
			"jkpw", "jkpws", "d", "ds", "dw", "dws", "dp", "dps", "dpw", "dpws", "dk", "dks", "dkw", "dkws", "dkp",
			"dkps", "dkpw", "dkpws", "dj", "djs", "djw", "djws", "djp", "djps", "djpw", "djpws", "djk", "djks", "djkw",
			"djkws", "djkp", "djkps", "djkpw", "djkpws" };
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
		/*
		 * int sum = 0; for (int x = 0; x < ind2.genome.length; x++) { //
		 * state.output.println(x + ": " + ind2.genome[x]); sum +=
		 * ind2.genome[x]; }
		 */
		/***
		 * Call ES here
		 */
		double fitness = 0.0;
		int sizeOfN = ind2.genome[4] + 1;
		String normMode = norms[ind2.genome[3]];
		String basicModel = basicModelArr[ind2.genome[0]];
		String afterEffect = afterEffectArr[ind2.genome[1]];
		String dfrNormalization = dfrNormalizationArr[ind2.genome[2]];
		state.output.print(sizeOfN + "," + normMode + "," + basicModel + "," + afterEffect + "," + dfrNormalization,
				Log.D_STDOUT);

		String output = "";
		Process p;
		try {
            String command = "/home/cragkhit/es_exp/scripts/eval_dfr.sh tests_extracted/ eval_dfr " + sizeOfN
                            + " " + normMode + " eval_dfr/ " + basicModel + " " + afterEffect + " " + dfrNormalization;
            // System.out.println(command);
			p = Runtime.getRuntime().exec(command);
			p.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

			String line = "";
			while ((line = reader.readLine()) != null) {
				output += line;
			}
			System.out.println(output);
			
			fitness = Double.parseDouble(output.trim());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		if (!(ind2.fitness instanceof SimpleFitness))
			state.output.fatal("Whoa!  It's not a SimpleFitness!!!", null);

		((SimpleFitness) ind2.fitness).setFitness(state, fitness, fitness == 1.0);
		ind2.evaluated = true;
	}
}
