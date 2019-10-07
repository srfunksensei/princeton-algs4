
/******************************************************************************
 *  Compilation:  javac Percolation.java
 *  Dependencies: WeightedQuickUnionUF.java
 *
 *  Percolation.
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/**
 * 
 * Model of the system as an n-by-n grid of sites. Each site is either blocked
 * or open; open sites are initially empty. A full site is an open site that can
 * be connected to an open site in the top row via a chain of neighboring (left,
 * right, up, down) open sites. If there is a full site in the bottom row, then
 * we say that the system percolates.
 * 
 * @author mb
 *
 */
public class Percolation {

    private static final int NUMBER_OF_VIRTUAL_SITES = 2; // number of virtual sites
    private static final int INDEX_OF_TOP_SITE = 0; // index of top virtual site

    private final int indexOfBottomSite; // index of bottom virtual site
    private final WeightedQuickUnionUF weightedQuickUnionUF; // graph representation of system
    private final int n; // dimension of grid

    private boolean[] sites; // 1d representation of sites. false indicates closed and true open site
    private int numberOfOpenSites = 0; // number of open sites

    /**
     * create n-by-n grid, with all sites blocked
     * 
     * @param n dimension of grid
     */
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("cannot construct grid");
        }
        this.n = n;

        int numOfSites = n * n + Percolation.NUMBER_OF_VIRTUAL_SITES; // number of sites including virtual top and
                                                                      // bottom

        this.sites = new boolean[numOfSites];
        this.weightedQuickUnionUF = new WeightedQuickUnionUF(numOfSites);
        this.indexOfBottomSite = numOfSites - 1;
    }

    /**
     * open site (row, col) if it is not open already
     * 
     * @param row row of site in the grid
     * @param col column of site in the grid
     */
    public void open(int row, int col) {
        if (!isOpen(row, col)) {
            int index = xyTo1D(row, col);
            sites[index] = true;
            numberOfOpenSites++;

            if (row == 1) { // connect to top if in first row
                weightedQuickUnionUF.union(Percolation.INDEX_OF_TOP_SITE, index);
            } 
            if (row == n) { // connect to bottom if in last row
                weightedQuickUnionUF.union(indexOfBottomSite, index);
            }

            // connect to neighbours if not connected
            if (row - 1 > 0 && isOpen(row - 1, col) && !weightedQuickUnionUF.connected(index, xyTo1D(row - 1, col))) {
                weightedQuickUnionUF.union(index, xyTo1D(row - 1, col));
            }
            if (row + 1 <= n && isOpen(row + 1, col) && !weightedQuickUnionUF.connected(index, xyTo1D(row + 1, col))) {
                weightedQuickUnionUF.union(index, xyTo1D(row + 1, col));
            }
            if (col - 1 > 0 && isOpen(row, col - 1) && !weightedQuickUnionUF.connected(index, xyTo1D(row, col - 1))) {
                weightedQuickUnionUF.union(index, xyTo1D(row, col - 1));
            }
            if (col + 1 <= n && isOpen(row, col + 1) && !weightedQuickUnionUF.connected(index, xyTo1D(row, col + 1))) {
                weightedQuickUnionUF.union(index, xyTo1D(row, col + 1));
            }
        }
    }

    /**
     * is site (row, col) open?
     * 
     * @param row row of site in the grid
     * @param col column of site in the grid
     * @return
     */
    public boolean isOpen(int row, int col) {
        validateIfSiteIsInsideBounds(row, col);

        final int index = xyTo1D(row, col);
        return sites[index];
    }

    /**
     * is site (row, col) full?
     * 
     * @param row row of site in the grid
     * @param col column of site in the grid
     * @return
     */
    public boolean isFull(int row, int col) {
        return isOpen(row, col) && weightedQuickUnionUF.connected(Percolation.INDEX_OF_TOP_SITE, xyTo1D(row, col));
    }

    /**
     * number of open sites
     * 
     * @return
     */
    public int numberOfOpenSites() {
        return numberOfOpenSites;
    }

    /**
     * does the system percolate?
     * 
     * @return
     */
    public boolean percolates() {
        return weightedQuickUnionUF.connected(Percolation.INDEX_OF_TOP_SITE, indexOfBottomSite);
    }

    /**
     * 
     * map from a 2-dimensional (row, column) pair to a 1-dimensional index
     * 
     * @param row row of site in the grid
     * @param col column of site in the grid
     * @return
     */
    private int xyTo1D(final int row, final int col) {
        return n * (row - 1) + col;
    }

    /**
     * checks if site specified with row and column is inside bounds
     * 
     * @param row row of site in the grid
     * @param col column of site in the grid
     * @throws IllegalArgumentException if one of the values does not satisfy condition
     *             {@code 1 <= val <= n}
     */
    private void validateIfSiteIsInsideBounds(final int row, final int col) {
        validate(row);
        validate(col);
    }

    /**
     * 
     * validate that value is in a range [1, n]
     * 
     * @param val value to be validated
     * @throws IllegalArgumentException unless {@code 1 <= val <= n}
     */
    private void validate(final int val) {
        if (val < 1 || val > n) {
            throw new IllegalArgumentException("index " + val + " is not between 1 and " + n);
        }
    }
}
