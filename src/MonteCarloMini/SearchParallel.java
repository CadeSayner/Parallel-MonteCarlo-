package MonteCarloMini;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
/**
 * SearchParallel
 */
public class SearchParallel extends RecursiveAction {
    // Static constant defining the threshold for switching to sequential processing
    static int SEQUENTIAL_CUTOFF = 1000;

    // Array of Search objects to be processed in parallel
    Search[] arr;

    // Starting index of the subrange to be processed by this task
    int lo;

    // Ending index (exclusive) of the subrange to be processed by this task
    int hi;

    // Local minimum found within the subrange processed by this task
    int localMin;

    // Index of the search that found the local minimum within this subrange
    int finder;
    
    public SearchParallel(Search[] a, int l, int h){
        arr = a;
        lo = l;
        hi = h;
    }
    protected void compute() {
        // Check if the range is small enough for sequential processing
        if (hi - lo < SEQUENTIAL_CUTOFF) {
            int min = Integer.MAX_VALUE;
            int local_min = Integer.MAX_VALUE;
            int finder = -1;
    
            // Iterate over the range to find valleys and determine minimum
            for (int i = lo; i < hi; i++) {
                local_min = arr[i].find_valleys();
                if ((!arr[i].isStopped()) && (local_min < min)) {
                    min = local_min;
                    finder = i; // Keep track of the index where the minimum was found
                }
            }
    
            // Set the minimum and associated index for this subrange
            localMin = min;
            this.finder = finder;
        } else {
            // Divide the range into two halves and create subtasks for each half
            SearchParallel left = new SearchParallel(arr, lo, (hi + lo) / 2);
            SearchParallel right = new SearchParallel(arr, (hi + lo) / 2, hi);
    
            left.fork(); // Start the computation of the left subtask in parallel
            right.compute(); // Compute the right subtask sequentially
            left.join(); // Wait for the left subtask to complete
    
            // Determine the minimum and associated index from the subtasks
            if (left.localMin < right.localMin) {
                localMin = left.localMin;
                finder = left.finder;
            } else {
                localMin = right.localMin;
                finder = right.finder;
            }
        }
    }
}