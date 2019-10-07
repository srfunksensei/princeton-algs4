/******************************************************************************
 *  Compilation:  javac BruteCollinearPoints.java
 *  Execution:    java BruteCollinearPoints
 *  Dependencies: java.util.ArrayList.java, java.util.List.java, Point.java
 *  
 *  Simple class that examines 4 points at a time and checks whether they all lie on the same line segment, returning all such line segments.
 *
 ******************************************************************************/
import java.util.ArrayList;
import java.util.List;

public class BruteCollinearPoints {

    private final List<LineSegment> lineSegments = new ArrayList<>(); // line segments that can be constructed
    
    /**
     * finds all line segments containing 4 points
     * 
     * @param points
     */
    public BruteCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException();
        }

        if (points.length >= 4) {
            for (int i = 0; i < points.length - 3; i++) {
                for (int j = i + 1; j < points.length - 2; j++) {
                    for (int k = j + 1; k < points.length - 1; k++) {
                        for (int m = k + 1; m < points.length; m++) {
                            final Point p = points[i], 
                                    q = points[j],
                                    r = points[k],
                                    s = points[m];
                            
                            if (p == null || q == null || r == null || s == null ||  
                                p.compareTo(q) == 0 || p.compareTo(r) == 0 || p.compareTo(s) == 0 ||
                                q.compareTo(r) == 0 || q.compareTo(s) == 0 || r.compareTo(s) == 0) { 
                                throw new IllegalArgumentException();
                            }
            
                            if (p.slopeOrder().compare(q, r) == 0 && q.slopeOrder().compare(r, s) == 0) {
                                Point start = p, end = p;
                                // find starting point
                                if (start.compareTo(q) > 0) {
                                    start = q;
                                } 
                                if (start.compareTo(r) > 0) {
                                    start = r;
                                }
                                if (start.compareTo(s) > 0) {
                                    start = s;
                                }
                                
                                // find ending point
                                if (end.compareTo(q) < 0) {
                                    end = q;
                                } 
                                if (end.compareTo(r) < 0) {
                                    end = r;
                                }
                                if (end.compareTo(s) < 0) {
                                    end = s;
                                }
                                
                                lineSegments.add(new LineSegment(start, end));
                            }
                        }
                    }
                }
            }
        } else {
            if (points.length == 1 && points[0] == null) {
                throw new IllegalArgumentException();
            }
            
            for (int i = 0; i < points.length - 1; i++) {
                for (int j = i + 1; j < points.length; j++) {
                    final Point p = points[i],
                                q = points[j];
                    
                    if (p == null || q == null || p.compareTo(q) == 0) { 
                        throw new IllegalArgumentException();
                    }
                }
            }
        }
    }

    /**
     * the number of line segments
     * 
     * @return
     */
    public int numberOfSegments() {
        return lineSegments.size();
    }

    /**
     * the line segments
     * 
     * @return
     */
    public LineSegment[] segments() {
        final LineSegment[] copy = new LineSegment[lineSegments.size()];
        for (int i = 0; i < lineSegments.size(); i++) {
            copy[i] = lineSegments.get(i);
        }
        
        return copy;
    }
}
