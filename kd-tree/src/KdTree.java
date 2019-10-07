import java.util.ArrayList;
import java.util.List;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

/**
 * 
 * 2d-tree implementation. A mutable data type that uses a 2d-tree. A 2d-tree is
 * a generalization of a BST to two-dimensional keys. The idea is to build a BST
 * with points in the nodes, using the x- and y-coordinates of the points as
 * keys in strictly alternating sequence.
 * 
 * @author mb
 *
 */
public class KdTree {

    private KdNode root;
    private int size;

    /**
     * construct an empty set of points
     */
    public KdTree() {
        root = null;
        size = 0;
    }

    /**
     * is the set empty?
     * 
     * @return
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * number of points in the set
     * 
     * @return
     */
    public int size() {
        return size;
    }

    /**
     * add the point to the set (if it is not already in the set)
     * 
     * @param p
     */
    public void insert(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();

        root = insert(root, p, true);
    }

    private KdNode insert(final KdNode parent, final Point2D point, final boolean isVertical) {
        if (parent == null) {
            size++;
            return new KdNode(point, isVertical);
        }

        if (point.equals(parent.point)) {
            return parent;
        }

        if (parent.isVertical && point.x() < parent.x() || !parent.isVertical && point.y() < parent.y()) {
            parent.left = insert(parent.left, point, !parent.isVertical);
        } else {
            parent.right = insert(parent.right, point, !parent.isVertical);
        }

        return parent;
    }

    /**
     * does the set contain point p?
     * 
     * @param p
     * @return
     */
    public boolean contains(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();

        return contains(root, p);
    }

    private boolean contains(final KdNode node, final Point2D point) {
        if (node == null) {
            return false;
        }

        if (node.point.equals(point)) {
            return true;
        }

        if (isLeftOrTop(node, point)) {
            return contains(node.left, point);
        } else {
            return contains(node.right, point);
        }
    }

    /**
     * draw all points to standard draw
     */
    public void draw() {
        final Point2D min = new Point2D(0.0, 0.0), max = new Point2D(1.0, 1.0);

        StdDraw.setScale(min.x(), max.y());

        draw(root, min, max);
    }

    private void draw(final KdNode node, final Point2D min, final Point2D max) {
        if (node == null)
            return;

        final Point2D point = node.point;

        StdDraw.setPenColor();
        StdDraw.filledCircle(point.x(), point.y(), StdDraw.getPenRadius() * 2);

        if (node.isVertical) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(point.x(), min.y(), point.x(), max.y());
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(min.x(), point.y(), max.x(), point.y());
        }

        final Point2D maxLeft = node.isVertical ? new Point2D(node.x(), max.y()) : new Point2D(max.x(), node.y()),
                minRight = node.isVertical ? new Point2D(node.x(), min.y()) : new Point2D(min.x(), node.y());

        draw(node.left, min, maxLeft);
        draw(node.right, minRight, max);
    }

    /**
     * all points that are inside the rectangle (or on the boundary)
     * 
     * @param rect
     * @return
     */
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new IllegalArgumentException();

        final List<Point2D> result = new ArrayList<>();
        return range(rect, root, new RectHV(0.0, 0.0, 1.0, 1.0), result);
    }

    private List<Point2D> range(final RectHV rect, final KdNode node, final RectHV origin, final List<Point2D> result) {
        if (rect == null)
            throw new IllegalArgumentException();

        if (node == null)
            return result;

        if (rect.contains(node.point)) {
            result.add(node.point);
        }

        if (node.left != null) {
            final RectHV rectLeft = getIntersectionRectLeftSubtree(node, origin);
            if (rectLeft.intersects(rect)) {
                range(rect, node.left, rectLeft, result);
            }
        }

        if (node.right != null) {
            final RectHV rectRight = getIntersectionRectRightSubtree(node, origin);
            if (rectRight.intersects(rect)) {
                range(rect, node.right, rectRight, result);
            }
        }

        return result;
    }

    private RectHV getIntersectionRectLeftSubtree(final KdNode node, final RectHV origin) {
        if (node.isVertical) {
            return new RectHV(origin.xmin(), origin.ymin(), node.x(), origin.ymax());
        } else {
            return new RectHV(origin.xmin(), origin.ymin(), origin.xmax(), node.y());
        }
    }

    private RectHV getIntersectionRectRightSubtree(final KdNode node, final RectHV origin) {
        if (node.isVertical) {
            return new RectHV(node.x(), origin.ymin(), origin.xmax(), origin.ymax());
        } else {
            return new RectHV(origin.xmin(), node.y(), origin.xmax(), origin.ymax());
        }
    }

    /**
     * a nearest neighbor in the set to point p; null if the set is empty
     * 
     * @param p
     * @return
     */
    public Point2D nearest(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();

        if (root == null)
            return null;

        return nearest(root, new RectHV(0.0, 0.0, 1.0, 1.0), p, root.point);
    }
    
    private Point2D nearest(final KdNode node, final RectHV origin, final Point2D point, final Point2D currentChampion) {
        if (node == null) return currentChampion;
        
        final KdNode candidateNodeFirst, candidateNodeSecond;
        final boolean isLeftorTop = isLeftOrTop(node, point);
        if (isLeftorTop) {
            candidateNodeFirst = node.left;
            candidateNodeSecond = node.right;
        } else {
            candidateNodeFirst = node.right;
            candidateNodeSecond = node.left;
        }
        
        Point2D candidateChampion = currentChampion;
        double distanceNearest = point.distanceSquaredTo(candidateChampion);
        
        if (candidateNodeFirst != null) {
            final RectHV rect = isLeftorTop ? getIntersectionRectLeftSubtree(node, origin) : getIntersectionRectRightSubtree(node, origin);
            
            final double distanceToRect = rect.distanceSquaredTo(point);
            if (distanceToRect <= distanceNearest) {
                final double distanceToPoint = point.distanceSquaredTo(candidateNodeFirst.point);
                        
                final Point2D champ = distanceToPoint < distanceNearest ? candidateNodeFirst.point : candidateChampion;
                candidateChampion = nearest(candidateNodeFirst, rect, point, champ);
                distanceNearest = point.distanceSquaredTo(candidateChampion);
            }
        }
        
        if (candidateNodeSecond != null) {
            final RectHV rect = isLeftorTop ? getIntersectionRectRightSubtree(node, origin) : getIntersectionRectLeftSubtree(node, origin);
            
            final double distanceToRect = rect.distanceSquaredTo(point);
            if (distanceToRect <= distanceNearest) {
                final double distanceToPoint = point.distanceSquaredTo(candidateNodeSecond.point);
                
                final Point2D champ = distanceToPoint < distanceNearest ? candidateNodeSecond.point : candidateChampion;
                candidateChampion = nearest(candidateNodeSecond, rect, point, champ);
            }
        }
        
        return candidateChampion;
    }

    private boolean isLeftOrTop(final KdNode node, final Point2D point) {
        if (node == null)
            throw new IllegalArgumentException();

        return node.isVertical && point.x() < node.x() || !node.isVertical && point.y() < node.y();
    }

    private static class KdNode {

        KdNode left;
        KdNode right;

        boolean isVertical;
        private final Point2D point;

        public KdNode(final Point2D point, final boolean isVertical, final KdNode left, final KdNode right) {
            super();
            this.point = point;
            this.isVertical = isVertical;
            this.left = left;
            this.right = right;
        }

        public KdNode(final Point2D point, final boolean isVertical) {
            this(point, isVertical, null, null);
        }

        public double x() {
            return this.point.x();
        }

        public double y() {
            return this.point.y();
        }
    }

    public static void main(String[] args) {
        final KdTree tree = new KdTree();
         
         tree.insert(new Point2D(0.7, 0.2));
         tree.insert(new Point2D(0.5, 0.4));
         tree.insert(new Point2D(0.2, 0.3));
         tree.insert(new Point2D(0.4, 0.7));
         tree.insert(new Point2D(0.9, 0.6));

           final Point2D p = new Point2D(0.5, 0.843);
        System.out.println(tree.nearest(p));

    }
}
