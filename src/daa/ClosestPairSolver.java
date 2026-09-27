package daa;

import java.util.Arrays;
import java.util.Comparator;

// Closest pair of points: sort by x, split, solve halves, check the strip in y order.
// Each call returns its points sorted by y (merge like in MergeSort),
// so T(n) = 2T(n/2) + O(n) = O(n log n).
public class ClosestPairSolver {
    private final Metrics m;

    public ClosestPairSolver(Metrics m) {
        this.m = m;
    }

    public double solve(Point[] points) {
        if (points == null || points.length < 2) throw new IllegalArgumentException("need at least 2 points");
        Point[] p = points.clone();
        Arrays.sort(p, Comparator.comparingDouble((Point q) -> q.x));
        Point[] buf = new Point[p.length];
        return solve(p, buf, 0, p.length - 1);
    }

    private double solve(Point[] p, Point[] buf, int lo, int hi) {
        m.enter();
        try {
            if (hi - lo + 1 <= 3) {
                double best = bruteForce(p, lo, hi);
                Arrays.sort(p, lo, hi + 1, Comparator.comparingDouble((Point q) -> q.y));
                return best;
            }

            int mid = (lo + hi) >>> 1;
            double midX = p[mid].x;
            double d = Math.min(solve(p, buf, lo, mid), solve(p, buf, mid + 1, hi));

            mergeByY(p, buf, lo, mid, hi);

            // strip: points closer than d to the middle line (already sorted by y)
            int s = 0;
            for (int i = lo; i <= hi; i++) {
                if (Math.abs(p[i].x - midX) < d) buf[s++] = p[i];
            }
            for (int i = 0; i < s; i++) {
                for (int j = i + 1; j < s && buf[j].y - buf[i].y < d; j++) {
                    m.comparisons++;
                    d = Math.min(d, buf[i].dist(buf[j]));
                }
            }
            return d;
        } finally {
            m.exit();
        }
    }

    private void mergeByY(Point[] p, Point[] buf, int lo, int mid, int hi) {
        for (int k = lo; k <= hi; k++) buf[k] = p[k];
        int i = lo, j = mid + 1;
        for (int k = lo; k <= hi; k++) {
            if (i > mid) p[k] = buf[j++];
            else if (j > hi) p[k] = buf[i++];
            else if (buf[j].y < buf[i].y) p[k] = buf[j++];
            else p[k] = buf[i++];
        }
    }

    private double bruteForce(Point[] p, int lo, int hi) {
        double best = Double.POSITIVE_INFINITY;
        for (int i = lo; i <= hi; i++) {
            for (int j = i + 1; j <= hi; j++) {
                m.comparisons++;
                best = Math.min(best, p[i].dist(p[j]));
            }
        }
        return best;
    }

    // O(n^2) version, used in tests and for comparison.
    public double bruteForce(Point[] points) {
        if (points == null || points.length < 2) throw new IllegalArgumentException("need at least 2 points");
        return bruteForce(points, 0, points.length - 1);
    }
}
