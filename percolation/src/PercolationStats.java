/******************************************************************************
 * Compilation: javac PercolationStats.java  
 * Dependencies: Percolation.java StdRandom.java StdStats.java 
 *
 * PercolationStats.
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

/**
 * 
 * Simple class used to determine treshold of percolation by executing
 * {@code trails} experiments. By repeating this computation experiment T times
 * and averaging the results, we obtain a more accurate estimate of the
 * percolation threshold.
 * 
 * @author mb
 *
 */
public class PercolationStats {

    private static final double CONFIDENCE_TRESHOLD_CONSTANT = 1.96;

    private final int trials; // number of iterations
    private final double[] fractions; // iteration fractions
    
    private double mean = Double.MIN_VALUE; // sample mean of percolation threshold
    private double stddev = Double.MIN_VALUE; // sample standard deviation of percolation threshold

    /**
     * 
     * perform trials independent experiments on an n-by-n grid
     * 
     * @param n dimension of grid
     * @param trials number of iteration for experiment
     * @throws IllegalArgumentException if either trails or n is less then 1
     */
    public PercolationStats(int n, int trials) {
        if (n < 1 || trials < 1)
            throw new IllegalArgumentException("n or trails is less then 1");

        this.trials = trials;
        this.fractions = new double[trials];

        for (int i = 0; i < trials; i++) {
            final Percolation p = new Percolation(n);
            while (!p.percolates()) {
                final int row = StdRandom.uniform(1, n + 1);
                final int col = StdRandom.uniform(1, n + 1);
                p.open(row, col);
            }

            fractions[i] = (double) p.numberOfOpenSites() / (n * n);
        }
    }

    /**
     * sample mean of percolation threshold
     * 
     * @return
     */
    public double mean() {
        if (mean == Double.MIN_VALUE) {
            mean = StdStats.mean(fractions);
        }
        return mean;
    }

    /**
     * sample standard deviation of percolation threshold
     * 
     * @return
     */
    public double stddev() {
        if (trials == 1) return Double.NaN;
        
        if(stddev == Double.MIN_VALUE) {
            stddev = StdStats.stddev(fractions);
        }
        return stddev;
    }

    /**
     * low endpoint of 95% confidence interval
     * 
     * @return
     */
    public double confidenceLo() {
        return mean - confidence();
    }

    /**
     * high endpoint of 95% confidence interval
     * 
     * @return
     */
    public double confidenceHi() {
        return mean + confidence();
    }

    /**
     * 95% confidence interval
     * 
     * @return
     */
    private double confidence() {
        return PercolationStats.CONFIDENCE_TRESHOLD_CONSTANT * stddev / Math.sqrt(trials);
    }

    public static void main(String[] args) // test client, described below
    {
        final int n = Integer.parseInt(args[0]);
        final int trials = Integer.parseInt(args[1]);
        final PercolationStats ps = new PercolationStats(n, trials);
        System.out.println(ps.mean());
        System.out.println(ps.stddev());
        System.out.println(ps.confidenceLo());
        System.out.println(ps.confidenceHi());
    }
}