package MonteCarloMini;


import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;

public class Experiment {
    static long startTime = 0;   // Stores the start time of a timer
    static long endTime = 0;     // Stores the end time of a timer

    static int Iterations = 10;  // Number of iterations for each experiment
    static int SequentialCutoffFactor = 64;  // Factor for determining sequential cutoff

    // Custom timer start function
    private static void tick() {
        startTime = System.currentTimeMillis();
    }

    // Custom timer stop function
    private static void tock() {
        endTime = System.currentTimeMillis();
    }

    public static void main(String[] args) throws IOException {
        FileWriter f = new FileWriter("results.csv");
        int[] grid_sizes = {100, 250, 500, 750, 1000, 2000, 3000, 3500, 4000, 4500, 5000};
        double[] search_densities = {0.025, 0.05, 0.1, 0.25, 0.5};

        // Loop through different search densities and grid sizes
        for(int i = 0; i < search_densities.length; i++) {
            for(int j = 0; j < grid_sizes.length; j++) {
                // Perform parallel and sequential searches and calculate speedup
                double parallel_search_time = searchParallel(grid_sizes[j], grid_sizes[j], search_densities[i]);
                double sequential_search_time = searchSequential(grid_sizes[j], grid_sizes[j], search_densities[i]);
                double speed_up = sequential_search_time / parallel_search_time;

                // Write results to a CSV file
                f.write(String.format("%f.%s.%f \n", search_densities[i], grid_sizes[j], speed_up));
            }
        }
        f.close();
    }

     public static double searchParallel(int rows, int columns, double searches_density){
        double xmin = -1000;
        double xmax = 1000;
        double ymin = -1000;
        double ymax = 1000;
    	TerrainArea terrain;  //object to store the heights and grid points visited by searches

    	Search [] searches;		// Array of searches
        terrain = new TerrainArea(rows, columns, xmin,xmax,ymin,ymax);
    	int num_searches = (int)( rows * columns * searches_density );
    	searches= new Search [num_searches];
        double total_time = 0;
        double minimum = Double.MAX_VALUE;
        for (int j = 0; j < Iterations; j++) {
            Random rand = new Random();  //the random number generator
            for (int i=0;i<num_searches;i++) 
                searches[i]=new Search(i+1, rand.nextInt(rows),rand.nextInt(columns),terrain);
    
            SearchParallel.SEQUENTIAL_CUTOFF = rows*rows/SequentialCutoffFactor; // Set the sequential cutoff
    
            SearchParallel t = new SearchParallel(searches,0,searches.length);  
            tick();
            ForkJoinPool.commonPool().invoke(t);
            tock();
            double time = (endTime - startTime);
            total_time += time;
            if(time < minimum){
                minimum = time;
            }
        }
        return (total_time-minimum)/(Iterations-1);
    }

    public static double searchSequential(int rows, int columns, double searches_density){
        double xmin = -1000;
        double xmax = 1000;
        double ymin = -1000;
        double ymax = 1000;
    	TerrainArea terrain;  //object to store the heights and grid points visited by searches

    	Search [] searches;		// Array of searches
        terrain = new TerrainArea(rows, columns, xmin,xmax,ymin,ymax);
    	int num_searches = (int)( rows * columns * searches_density );
    	searches= new Search [num_searches];

        double total_time = 0;
        double minimum = Double.MAX_VALUE;
        for (int j = 0; j < Iterations; j++) {
            Random rand = new Random();  //the random number generator
            for (int i=0;i < num_searches;i++) 
                searches[i] = new Search(i+1, rand.nextInt(rows),rand.nextInt(columns),terrain);
    
            tick();
            //all searches
            int min=Integer.MAX_VALUE;
            int local_min=Integer.MAX_VALUE;
            for  (int i=0;i<num_searches;i++) {
                local_min=searches[i].find_valleys();
                if((!searches[i].isStopped())&&(local_min<min)) { //don't look at  those who stopped because hit exisiting path
                    min=local_min;
                }
            }
               //end timer
            tock();
            double time = (endTime - startTime);
            total_time += time;
            if(time < minimum){
                minimum = time;
            }
        }
        return (total_time-minimum)/(Iterations-1);
    }
}
