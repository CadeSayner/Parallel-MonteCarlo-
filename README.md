# Parallel-MonteCarlo-
A parallelized Monte Carlo algorithm for efficiently determining the minimum extrema of functions.

Monte Carlo methods use random selections in calculations to solve numerical or
physical problems. We deal with a Monte Carlo algorithm for
finding the minimum (the lowest point) of a two-dimensional mathematical function
f(x,y) within a specified range – this is an optimization problem. You can think of the
2D function as representing the height of a terrain, and the program’s task is to find
the lowest point in a specified rectangular area.

The project's parallelized algorithm showcases impressive speed-up gains, especially evident with higher search densities and grid sizes exceeding 1000x1000. The combination of a well-tuned search density and larger grid sizes ensures efficient workload distribution, minimizing overhead impact and load imbalance. The results indicate that this parallelized approach significantly outperforms the sequential implementation.
To run the parallelised algorithm:


1) Navigate to the Parallel Assignment Program repository in the terminal.

2) Execute the following command - make run ARGS="<command line arguments>" 

Example:

make run ARGS="1000 1000 -100 100 -100 100 0.05"

This command builds the project and runs the parallel algorithm using the given parameters. 

For completeness the ancillary files: Experiment.java and MonteCarloMinimizationOptimisation.java can be compiled using the make command and run in the bin repository. 
