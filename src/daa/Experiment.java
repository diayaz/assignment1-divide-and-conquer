package daa;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Locale;
import java.util.Random;

// Runs all algorithms on different sizes and input types and saves results to CSV.
public class Experiment {
    private static final int[] SIZES = {1000, 2000, 5000, 10000, 20000, 50000, 100000, 200000, 500000, 1000000};
    private static final String[] TYPES = {"random", "sorted", "reverse", "duplicates"};
    private static final int WARMUP = 10;
    private static final int RUNS = 5;

    private final Random rnd = new Random(42);
    private PrintWriter out;

    public void run(String csvFile) throws IOException {
        new java.io.File(csvFile).getParentFile().mkdirs();
        out = new PrintWriter(csvFile);
        out.println("algorithm,input,n,time_ms,max_depth,comparisons,swaps,calls");

        for (int n : SIZES) {
            for (String type : TYPES) {
                int[] a = generate(type, n);
                measure("MergeSort", type, n, m -> new MergeSorter(m).sort(a.clone()));
                measure("QuickSort", type, n, m -> new QuickSorter(m).sort(a.clone()));
                measure("Select", type, n, m -> new DeterministicSelector(m).select(a.clone(), n / 2));
            }
            Point[] pts = randomPoints(n);
            measure("ClosestPair", "random", n, m -> new ClosestPairSolver(m).solve(pts));
            if (n <= 20000) {
                measure("BruteForce", "random", n, m -> new ClosestPairSolver(m).bruteForce(pts));
            }
            System.out.println("done n = " + n);
        }
        out.close();
    }

    interface Task {
        void run(Metrics m);
    }

    // a few warm-up runs for the JIT, then median time of RUNS runs
    private void measure(String algorithm, String type, int n, Task task) {
        for (int i = 0; i < WARMUP; i++) task.run(new Metrics());

        long[] times = new long[RUNS];
        Metrics m = null;
        for (int i = 0; i < RUNS; i++) {
            m = new Metrics();
            long start = System.nanoTime();
            task.run(m);
            times[i] = System.nanoTime() - start;
        }
        Arrays.sort(times);
        double ms = times[RUNS / 2] / 1e6;

        out.printf(Locale.US, "%s,%s,%d,%.3f,%d,%d,%d,%d%n",
                algorithm, type, n, ms, m.maxDepth, m.comparisons, m.swaps, m.calls);
    }

    private int[] generate(String type, int n) {
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            switch (type) {
                case "sorted": a[i] = i; break;
                case "reverse": a[i] = n - i; break;
                case "duplicates": a[i] = rnd.nextInt(10); break;
                default: a[i] = rnd.nextInt();
            }
        }
        return a;
    }

    private Point[] randomPoints(int n) {
        Point[] p = new Point[n];
        for (int i = 0; i < n; i++) p[i] = new Point(rnd.nextDouble() * n, rnd.nextDouble() * n);
        return p;
    }
}
