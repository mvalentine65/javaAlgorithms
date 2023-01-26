/* *****************************************************************************
 *  Name: Michael Valentine
 *  Date: 1/24/2023
 *  Description:
 * public BruteCollinearPoints(Point[] points)    // finds all line segments containing 4 points
 * public           int numberOfSegments()        // the number of line segments
 * public LineSegment[] segments()                // the line segments
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {
    private LineSegment[] lineSegments = new LineSegment[0];

    public BruteCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("Points array is null.");
        }
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException("null point found in array");
            }
        }

        Point[] working = points.clone();
        Arrays.sort(working);
        // equality check
        for (int i = 0; i < working.length - 1; i++) {
            if (working[i].compareTo(working[i + 1]) == 0) {
                throw new IllegalArgumentException("repeated points");
            }
        }
        ArrayList<LineSegment> output = new ArrayList<>();
        if (working.length < 4) {
            return;
        }
        for (int p = 0; p < working.length - 3; p++) {
            for (int q = p + 1; q < working.length - 2; q++) {
                for (int r = q + 1; r < working.length - 1; r++) {
                    for (int s = r + 1; s < working.length; s++) {
                        double slope1 = working[p].slopeTo(working[q]);
                        double slope2 = working[p].slopeTo(working[r]);
                        double slope3 = working[p].slopeTo(working[s]);
                        if (Double.compare(slope1, slope2) == 0
                                && Double.compare(slope2, slope3) == 0) {
                            output.add(new LineSegment(working[p], working[s]));
                        }
                    }
                }
            }
        }
        lineSegments = output.toArray(new LineSegment[output.size()]);
    }

    public int numberOfSegments() {
        return lineSegments.length;
    }

    public LineSegment[] segments() {
        return lineSegments.clone();
    }

    // unit tests here //
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);


        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}

