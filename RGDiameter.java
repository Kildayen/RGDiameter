/**
 * Class RGDiameter measures the average diameter of a series of
 * random graphs.
 * This program can be set to perform parameter sweeps
 * for number of vertices and edge probability.
 * 
 * Usage: java pj2 DegreeDistRandomGraph seed V P T plotfile
 * 	seed = Random seed
 * 	V = Number of vertices
 * 	P = Edge probability
 * 	T = Number of trials for each configuration
 * 	Mode = Mode of operation (vertex sweep or probability sweep)
 *	I = Increment of increase for given parameter
 *	Stop = Final value for parameter sweep
 *
 * @author  Adam Warner
 * @modified 6/28/2018
 */

import edu.rit.pj2.LongLoop;
import edu.rit.pj2.Task;
import edu.rit.pj2.vbl.DoubleVbl;
import edu.rit.util.Random;

public class RGDiameter	extends Task
{
	// Command line arguments and trial parameters
	long seed;
	int V;
	double P;
	long T;
	String mode;
	double increment;
	double start, stop;

	// Pseudorandom number generator
	Random prng;
	
	// Average of graph diameters
	DoubleVbl avgDiameter = new DoubleVbl.Sum();
	
	// Main program.
	public void main (String[] args)
			throws Exception
	{
		// Parse command line arguments
		if (args.length != 7) usage();
		seed = Long.parseLong (args[0]);
		V = Integer.parseInt (args[1]);
		P = Double.parseDouble (args[2]);
		T = Long.parseLong (args[3]);
		mode = args[4];
		
		// Set start, increment, and stop according to mode
		if (mode.equalsIgnoreCase("p"))
		{
			start = P;
			increment = Double.parseDouble(args[5]);
			stop = Double.parseDouble(args[6]);
		} else
		{
			start = (double)V;
			increment = Double.parseDouble(args[5]);
			stop = Double.parseDouble(args[6]);
		}
		
		// Check initial parameters
		if (start > stop || increment <= 0)
		{
			System.err.println("Invalid parameters");
			throw new IllegalArgumentException();
		}
	
		System.out.printf("$ java pj2 RGDiameter");
		for (String arg : args) System.out.printf(" %s", arg);
		System.out.println();
		
		// Set up pseudorandom number generator
		prng = new Random (seed);

		// Setup parameter sweep
		for(double i = start; i <= stop; i += increment)
		{
			// Perform T trials
			parallelFor (0, T - 1).exec(new LongLoop()
			{
				// Per-thread variables
				Random prng;
				RGObject graph;
				DoubleVbl threadAvg;
				
				// Initialize Per-thread variables
				public void start()
				{
					prng = new Random (seed + rank());
					graph = new RGObject(V);
					threadAvg = threadLocal(avgDiameter); 
				}
				
				// Loop body
				public void run(long t)
				{	
					// Generate random graph and assign edges
					for (int a = 0; a < V - 1; ++ a)
					{
						for (int b = a + 1; b < V; ++ b)
						{
							if (prng.nextDouble() < P)
							{
								graph.setEdge(a, b);
							}
						}
					}
					threadAvg.item += graph.getDiameter();
				}
			});
		
		Double displayAvg = avgDiameter.item;
		System.out.println("Vertices: " + V + "   Edge Probability: " + P + "   Trials: " + T);
		System.out.println("Average Diameter: " + (displayAvg / (double)T));
		avgDiameter.item = 0;
		
		// Adjust parameters for next run
		if (mode.equalsIgnoreCase("p"))
		{
			P += increment;
		} else
		{
			V += (int)increment;
		}
	}
}
   
	// Print a usage message and throw an error, killing the program
	private static void usage()
	{
		System.err.println("Usage: java pj2 DegreeDistRandomGraph <seed> <V> <P> <T> <plotfile>");
		System.err.println("<seed> = Random seed");
		System.err.println("<V> = Number of vertices");
		System.err.println("<P> = Edge probability");
		System.err.println("<T> = Number of trials");
		System.err.println("<mode> = V or P, the parameter changed between trials");
		System.err.println("<increment> = Magnitude of parameter change between trials");
		System.err.println("<stop> = Stopping point for the parameter being changed");
		throw new IllegalArgumentException();
	}
}
