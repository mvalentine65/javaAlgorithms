/* *****************************************************************************
 *  Name: Michael Valentine
 *  Date: 1/25/2023
 *  Description:
 * public class FastCollinearPoints {
   public FastCollinearPoints(Point[] points)     // finds all line segments containing 4 or more points
   public           int numberOfSegments()        // the number of line segments
   public LineSegment[] segments()                // the line segments
}
* The method segments() should include each maximal line segment containing 4
* (or more) points exactly once. For example, if 5 points appear on a line
* segment in the order p→q→r→s→t, then do not include the subsegments
* p→s or q→t.
*
* Corner cases.
* Throw an IllegalArgumentException if the argument to the constructor is null,
* if any point in the array is null, or if the argument to the constructor
* contains a repeated point.
*
* Performance requirement. The order of growth of the running time of your
* program should be n2 log n in the worst case and it should use space
* proportional to n plus the number of line segments returned.
* FastCollinearPoints should work properly even if the input has 5 or more
* collinear points.
**************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {
    private LineSegment[] lineSegments = new LineSegment[0];

    public FastCollinearPoints(Point[] points) {
        // null checks
        if (points == null) {
            throw new IllegalArgumentException("null points array");
        }
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException("null point found in array");
            }
        }

        // clone to stop side effects
        Point[] working = points.clone();

        // coordinate equality check
        Arrays.sort(working);
        for (int i = 0; i < working.length - 1; i++) {
            if (working[i].compareTo(working[i + 1]) == 0) {
                throw new IllegalArgumentException("equal points detected");
            }
        }
        if (working.length < 4) {
            return;
        }
        ArrayList<LineSegment> output = new ArrayList<LineSegment>();
        Point[] working2 = working.clone();
        // find line segments
        for (Point origin : working) {
            Arrays.sort(working2, origin.slopeOrder());
            int start = 1;
            double lastSlope = origin.slopeTo(working2[1]);
            // origin is always [0], [1] is accounted for
            // start at [2]
            for (int i = 2; i < points.length; i++) {
                double currentSlope = origin.slopeTo(working2[i]);
                // if slopes are unequal, new line segment has been found
                if (currentSlope != lastSlope) {
                    if (i - start >= 3) {
                        Point[] found = sortCollinear(working2, start, i, origin);
                        // dupe prevention
                        if (found[0].compareTo(origin) == 0) {
                            output.add(new LineSegment(found[0], found[1]));
                        }
                    }
                    start = i;
                    lastSlope = currentSlope;
                }
            }
            if (points.length - start >= 3) {
                Point[] found = sortCollinear(working2, start, working2.length, origin);
                if (found[0].compareTo(origin) == 0) {
                    output.add(new LineSegment(found[0], found[1]));
                }
            }
        }
        lineSegments = output.toArray(new LineSegment[output.size()]);
    }

    /*
        Loads the a length of collinear points into an array, then sorts them
        to find the first and last points in the line segment. This way the
        array sits on the stack instead of the whole Array. Should reduce
        (hopefully) memory.
     */
    private Point[] sortCollinear(Point[] points, int startIndex, int endIndex, Point origin) {
        ArrayList<Point> working = new ArrayList<>();
        working.addAll(Arrays.asList(points).subList(startIndex, endIndex));
        working.add(origin);
        // end-index is already exclusive, no need to adjust
        // lets give the autocorrected code a chance
        working.sort(null);
        return new Point[] { working.get(0), working.get(working.size() - 1) };
    }


    public int numberOfSegments() {
        return lineSegments.length;
    }


    public LineSegment[] segments() {
        return lineSegments.clone();
    }


    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
