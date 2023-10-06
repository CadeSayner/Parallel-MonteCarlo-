package MonteCarloMini;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
/**
 * MonteCarloSequentialCutoffOptimiser
 */
public class MonteCarloSequentialCutoffOptimiser {
    static long startTime = 0;
	static long endTime = 0;
    public static void main(String[] args) {
        // Optimise sequential cut off for best average performance
        long minimum = Long.MAX_VALUE;
        for (int i = 1000; i <= 500000 ; i += 10000){
            System.out.println("Trying:" + i);
            long v = 0;
            for(int j = 1; j <=20; j++){
                v += search(i);
            }
            v = v/20; // Get the average over the 20 tries

            if (v < minimum){
                minimum = v;
                System.out.println("New best sequential cutoff found:" + " seqcut = " + i + " took " + v + " miliseconds.");
            }
            else{
                System.out.println("Worse sequential cutoff found:" + " seqcut = " + i + " took " + v + " miliseconds.");
            }

        }

    }
    private static void tick(){
		startTime = System.currentTimeMillis();
	}
	private static void tock(){
		endTime=System.currentTimeMillis(); 
	}
    public static long search(int sequentialCutoff){
        // Performs the parallelised search with the specified sequnetial cutoff
        int rows = 2000;
        int columns = 2000;
        double xmin = -100;
        double xmax = 100;
        double ymin = -100;
        double ymax = 100;
    	TerrainArea terrain;  //object to store the heights and grid points visited by searches
    	double searches_density = 0.5;	// Density - number of Monte Carlo  searches per grid position - usually less than 1!

    	Search [] searches;		// Array of searches
    	Random rand = new Random();  //the random number generator
        terrain = new TerrainArea(rows, columns, xmin,xmax,ymin,ymax);
    	int num_searches = (int)( rows * columns * searches_density );
    	searches= new Search [num_searches];
        for (int i=0;i<num_searches;i++) 
    		searches[i]=new Search(i+1, rand.nextInt(rows),rand.nextInt(columns),terrain);

        SearchParallel.SEQUENTIAL_CUTOFF = sequentialCutoff; // Set the sequential cutoff

        SearchParallel t = new SearchParallel(searches,0,searches.length);  
		tick();
		ForkJoinPool.commonPool().invoke(t);
		tock();
        return endTime - startTime;

    }

    
}