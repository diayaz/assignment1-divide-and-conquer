package daa;

import java.util.Arrays;
import java.util.Random;

public class Main {
    public static void main(String[] args) throws Exception {
        Random rnd = new Random();
        int[] a = new int[15];
        for (int i = 0; i < a.length; i++) a[i] = rnd.nextInt(100);
        int[] expected = a.clone();
        Arrays.sort(expected);
        System.out.println("Array:      " + Arrays.toString(a));

        Metrics m1 = new Metrics();
        int[] b = a.clone();
        new MergeSorter(m1).sort(b);
        System.out.println("MergeSort:  " + Arrays.toString(b) + " ok=" + Arrays.equals(b, expected) + " | " + m1);

        Metrics m2 = new Metrics();
        int[] c = a.clone();
        new QuickSorter(m2).sort(c);
        System.out.println("QuickSort:  " + Arrays.toString(c) + " ok=" + Arrays.equals(c, expected) + " | " + m2);

        Metrics m3 = new Metrics();
        int k = a.length / 2;
        int kth = new DeterministicSelector(m3).select(a.clone(), k);
        System.out.println("Select k=" + k + ": " + kth + " ok=" + (kth == expected[k]) + " | " + m3);

        Point[] pts = new Point[1000];
        for (int i = 0; i < pts.length; i++) pts[i] = new Point(rnd.nextDouble() * 1000, rnd.nextDouble() * 1000);
        Metrics m4 = new Metrics();
        double fast = new ClosestPairSolver(m4).solve(pts);
        double slow = new ClosestPairSolver(new Metrics()).bruteForce(pts);
        System.out.println("ClosestPair (n=1000): " + fast + " ok=" + (fast == slow) + " | " + m4);

        System.out.println();
        System.out.println("Running experiments...");
        new Experiment().run("results/results.csv");
        System.out.println("Saved to results/results.csv");
    }
}
