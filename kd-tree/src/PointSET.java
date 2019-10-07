/******************************************************************************
 * Compilation: javac Board.java 
 * Dependencies: java.util.ArrayList, java.util.Arrays, java.util.List,
 *              edu.princeton.cs.algs4.StdRandom
 * 
 *
 ******************************************************************************/

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

/**
 * 
 * Mutable data type that represents a set of points in the unit square. 
 * Implemented using a red–black BST.
 *  
 * @author mb
 *
 */
public class PointSET {
    
    private final TreeSet<Point2D> points;

    /**
     * construct an empty set of points
     */
    public PointSET() {
        points = new TreeSet<>();
    }

    /**
     * is the set empty?
     * 
     * @return
     */
    public boolean isEmpty() {
        return points.isEmpty();
    }

    /**
     * number of points in the set
     * 
     * @return
     */
    public int size() {
        return points.size();
    }

    /**
     * add the point to the set (if it is not already in the set)
     * 
     * @param p
     */
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        
        points.add(p);
    }

    /**
     * does the set contain point p?
     * 
     * @param p
     * @return
     */
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return points.contains(p);
    }

    /**
     * draw all points to standard draw
     */
    public void draw() {
        StdDraw.setPenColor();
        StdDraw.setPenRadius();
        
        for (Point2D point : points) {
            point.draw();
        }
    }

    /**
     * all points that are inside the rectangle (or on the boundary)
     * 
     * @param rect
     * @return
     */
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        
        List<Point2D> list = new ArrayList<>();
        for (Point2D point : points) {
            if (rect.contains(point)) list.add(point);
        }
        return list;
    }

    /**
     * a nearest neighbor in the set to point p; null if the set is empty
     * 
     * @param p
     * @return
     */
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        
        double distance = Double.POSITIVE_INFINITY;
        Point2D nearest = null;
        for (Point2D point : points) {
            if (p.distanceSquaredTo(point) < distance) {
                nearest = point;
                distance = p.distanceSquaredTo(point);
            }
        }
        return nearest;
    }
}