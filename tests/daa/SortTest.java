package daa;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class SortTest {
    private final Random rnd = new Random(1);

    private void check(int[] a) {
        int[] expected = a.clone();
        Arrays.sort(expected);

        int[] b = a.clone();
        new MergeSorter(new Metrics()).sort(b);
        assertArrayEquals(expected, b, "MergeSort");

        int[] c = a.clone();
        new QuickSorter(new Metrics()).sort(c);
        assertArrayEquals(expected, c, "QuickSort");
    }

    @Test
    void empty() {
        check(new int[0]);
    }

    @Test
    void singleElement() {
        check(new int[]{5});
    }

    @Test
    void random() {
        for (int n = 1; n <= 200; n++) {
            int[] a = new int[n];
            for (int i = 0; i < n; i++) a[i] = rnd.nextInt();
            check(a);
        }
        int[] big = new int[100000];
        for (int i = 0; i < big.length; i++) big[i] = rnd.nextInt();
        check(big);
    }

    @Test
    void sorted() {
        int[] a = new int[10000];
        for (int i = 0; i < a.length; i++) a[i] = i;
        check(a);
    }

    @Test
    void reverseSorted() {
        int[] a = new int[10000];
        for (int i = 0; i < a.length; i++) a[i] = a.length - i;
        check(a);
    }

    @Test
    void duplicates() {
        int[] a = new int[10000];
        for (int i = 0; i < a.length; i++) a[i] = rnd.nextInt(5);
        check(a);
        check(new int[1000]); // all zeros
    }

    @Test
    void quickSortDepthIsLogarithmic() {
        int n = 100000;
        int[] a = new int[n];
        for (int i = 0; i < n; i++) a[i] = i;
        Metrics m = new Metrics();
        new QuickSorter(m).sort(a);
        // recursion goes into the smaller part, so depth <= log2(n) + 1
        assertTrue(m.maxDepth <= 18, "depth = " + m.maxDepth);
    }
}
