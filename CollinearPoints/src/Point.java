/******************************************************************************
 *  Compilation:  javac Point.java
 *  Execution:    java Point
 *  Dependencies: edu.princeton.cs.algs4.StdDraw.java
 *  
 *  An immutable data type for points in the plane.
 *
 ******************************************************************************/

import java.util.Comparator;

import edu.princeton.cs.algs4.StdDraw;

public class Point implements Comparable<Point> {
    
    public static final Comparator<Point> BY_NATURAL = new NaturalOrder();

    private static final int AXIS_MIN_VALUE = 0; // axis min value
    
    private final int x; // x-coordinate of this point
    private final int y; // y-coordinate of this point
    
    /**
     * constructs the point (x, y)
     * 
     * @param x the <em>x</em>-coordinate of the point
     * @param y the <em>y</em>-coordinate of the point
     */
    public Point(int x, int y) {
        assertAxesRange(new String[] {"x", "y"}, new int[] {x, y});
        
        this.x = x;
        this.y = y;
    }

    /**
     * draws this point
     */
    public void draw() {
        StdDraw.point(x, y);
    }

    /**
     * draws the line segment from this point to that point
     * 
     * @param that the other point
     */
    public void drawTo(Point that) {
        if (that == null) throw new NullPointerException();
        assertPoint(that);
        
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    /**
     * string representation
     */
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    /**
     * Compares two points by y-coordinate, breaking ties by x-coordinate.
     * Formally, the invoking point (x0, y0) is less than the argument point
     * (x1, y1) if and only if either y0 < y1 or if y0 = y1 and x0 < x1.
     *
     * @param  that the other point
     * @return the value <tt>0</tt> if this point is equal to the argument
     *         point (x0 = x1 and y0 = y1);
     *         a negative integer if this point is less than the argument
     *         point; and a positive integer if this point is greater than the
     *         argument point
     */
    public int compareTo(Point that) {
        if (that == null) throw new NullPointerException();
        assertPoint(that);
        
        if (y < that.y) return -1;
        if (y > that.y) return 1;
        if (x < that.x) return -1;
        else if (x > that.x) return 1;
        return 0;
    }

    /**
     * Returns the slope between this point and the specified point.
     * Formally, if the two points are (x0, y0) and (x1, y1), then the slope
     * is (y1 - y0) / (x1 - x0). For completeness, the slope is defined to be
     * +0.0 if the line segment connecting the two points is horizontal;
     * Double.POSITIVE_INFINITY if the line segment is vertical;
     * and Double.NEGATIVE_INFINITY if (x0, y0) and (x1, y1) are equal.
     *
     * @param  that the other point
     * @return the slope between this point and the specified point
     */
    public double slopeTo(Point that) {
        if (that == null) throw new NullPointerException();
        assertPoint(that);
        
        final int horizontalLineSegment = that.x - x;
        final int verticalLineSegment = that.y - y;
        
        if (horizontalLineSegment == 0 && verticalLineSegment == 0) return Double.NEGATIVE_INFINITY;
        if (verticalLineSegment == 0) return +0.0;
        if (horizontalLineSegment == 0) return Double.POSITIVE_INFINITY;
        
        return (double) verticalLineSegment / horizontalLineSegment;
    }

    /**
     * Compares two points by the slope they make with this point.
     * The slope is defined as in the slopeTo() method.
     *
     * @return the Comparator that defines this ordering on points
     */
    public Comparator<Point> slopeOrder() {
        return new SlopeOrder();
    }

    /**
     * Asserts if axis value is in range
     * @param axis axis discriminant
     * @param value value of axis
     */
    private void assertAxisRange(final String axis, final int value) {
        assert (value >= Point.AXIS_MIN_VALUE) : "Value of " + axis + " is not in [" + Point.AXIS_MIN_VALUE + ", 32767] range";
    }
    
    /**
     * Asserts if all axes values are in range
     * @param axes list of axes discriminants
     * @param values list of values for axes
     */
    private void assertAxesRange(final String[] axes, final int[] values) {
        assert (axes.length == values.length) : "Length of axes is not equal to values of axes length";
        
        for (int i = 0; i < axes.length; i++) {
            assertAxisRange(axes[i], values[i]);
        }
    }
    
    /**
     * Asserts if point axes are in range
     * @param p point
     */
    private void assertPoint(final Point p) {
        assertAxesRange(new String[] {"x", "y"},  new int[] {p.x, p.y});
    }
    
    private class SlopeOrder implements Comparator<Point> {

        @Override
        public int compare(Point o1, Point o2) {
            final double slope1 = slopeTo(o1),
                         slope2 = slopeTo(o2);
            return Double.compare(slope1, slope2);
        }
        
    }
    
    private static class NaturalOrder implements Comparator<Point> {

        @Override
        public int compare(Point o1, Point o2) {
            return o1.compareTo(o2);
        }
        
    }
}
