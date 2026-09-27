package daa;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class ClosestPairTest {

    @Test
    void compareWithBruteForce() {
        Random rnd = new Random(3);
        for (int t = 0; t < 50; t++) {
            int n = 2 + rnd.nextInt(1999); // n <= 2000
            Point[] p = new Point[n];
            for (int i = 0; i < n; i++) p[i] = new Point(rnd.nextDouble() * 1000, rnd.nextDouble() * 1000);

            double fast = new ClosestPairSolver(new Metrics()).solve(p);
            double slow = new ClosestPairSolver(new Metrics()).bruteForce(p);
            assertEquals(slow, fast, 1e-9);
        }
    }

    @Test
    void duplicatePoints() {
        Point[] p = {new Point(1, 1), new Point(5, 5), new Point(1, 1)};
        assertEquals(0.0, new ClosestPairSolver(new Metrics()).solve(p));
    }

    @Test
    void twoPoints() {
        Point[] p = {new Point(0, 0), new Point(3, 4)};
        assertEquals(5.0, new ClosestPairSolver(new Metrics()).solve(p), 1e-9);
    }

    @Test
    void largeInput() {
        Random rnd = new Random(4);
        Point[] p = new Point[200000];
        for (int i = 0; i < p.length; i++) p[i] = new Point(rnd.nextDouble() * 1e6, rnd.nextDouble() * 1e6);
        double d = new ClosestPairSolver(new Metrics()).solve(p);
        assertTrue(d >= 0);
    }

    @Test
    void badInput() {
        assertThrows(IllegalArgumentException.class,
                () -> new ClosestPairSolver(new Metrics()).solve(new Point[]{new Point(0, 0)}));
    }
}
