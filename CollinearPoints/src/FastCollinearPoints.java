/*************************************************************************
 *  Compilation:  javac LineSegment.java
 *  Execution:    none
 *  Dependencies: Point.java
 *
 * Simple class that examines 4 points at a time and checks whether they all lie on the same line segment, returning all such line segments.
 * This class uses sorting for faster calculation.
 * 
 *************************************************************************/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FastCollinearPoints {

    private final List<LineSegment> lineSegments = new ArrayList<>(); // line segments that can be constructed
    private final List<Point> segmentPoints = new ArrayList<>(); // starting & ending points of segments
    
    /**
     * finds all line segments containing 4 points
     * 
     * @param points
     */
    public FastCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException();
        }
        
        Point[] copy = new Point[points.length];
        for (int i = 0; i < points.length; i++) {
            copy[i] = points[i];
        }
            
        if (points.length >= 4) {
            final List<Point> pointsOnLine = new ArrayList<>();
            
            for (int i = 0; i < points.length && copy.length > 3; i++) {
                final Point p = points[i];
                Arrays.sort(copy, p.slopeOrder());
            
                pointsOnLine.clear();
                pointsOnLine.add(p);
                for (int j = 1; j < copy.length - 2; j++) {
                    final Point  q = copy[j],
                                r = copy[j + 1],
                                s = copy[j + 2];
                    
                    if (p.compareTo(q) == 0 || p.compareTo(r) == 0 || p.compareTo(s) == 0 ||
                            q.compareTo(r) == 0 || q.compareTo(s) == 0 || r.compareTo(s) == 0) { 
                        throw new IllegalArgumentException();
                    }
                    
                    if (p.slopeOrder().compare(q, r) == 0 && q.slopeOrder().compare(r, s) == 0) {
                        pointsOnLine.add(q);
                        pointsOnLine.add(r);
                        pointsOnLine.add(s);
                    }
                }
                
                if (pointsOnLine.size() > 3) {
                    final Point[] array = new Point[pointsOnLine.size()];
                    for (int j = 0; j < pointsOnLine.size(); j++) {
                        array[j] = pointsOnLine.get(j);
                    }
                    Arrays.sort(array, Point.BY_NATURAL);

                    final Point start = array[0], end = array[array.length - 1];
                    boolean isSameSegment = false;
                    for (int j = 0; j < segmentPoints.size(); j += 2) {
                        if (segmentPoints.get(j).compareTo(start) == 0 && segmentPoints.get(j + 1).compareTo(end) == 0) {
                            isSameSegment = true;
                            break;
                        }
                    }
                    if (!isSameSegment) {
                        segmentPoints.add(start);
                        segmentPoints.add(end);
                        lineSegments.add(new LineSegment(start, end));
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
                    
                    if (p.compareTo(q) == 0) { 
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
