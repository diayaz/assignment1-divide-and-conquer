package daa;

import java.util.Random;

// Randomized QuickSort with in-place 3-way partition (handles duplicates well).
// Recurses into the smaller part and loops over the larger one -> depth O(log n).
// Expected O(n log n), worst case O(n^2).
public class QuickSorter {
    private final Metrics m;
    private final Random rnd = new Random();

    public QuickSorter(Metrics m) {
        this.m = m;
    }

    public void sort(int[] a) {
        if (a == null || a.length < 2) return;
        quickSort(a, 0, a.length - 1);
    }

    private void quickSort(int[] a, int lo, int hi) {
        m.enter();
        while (lo < hi) {
            int pivot = a[lo + rnd.nextInt(hi - lo + 1)];

            // a[lo..lt-1] < pivot, a[lt..gt] == pivot, a[gt+1..hi] > pivot
            int lt = lo, gt = hi, i = lo;
            while (i <= gt) {
                m.comparisons++;
                if (a[i] < pivot) {
                    swap(a, lt++, i++);
                } else if (a[i] > pivot) {
                    swap(a, i, gt--);
                } else {
                    i++;
                }
            }

            if (lt - lo < hi - gt) {
                quickSort(a, lo, lt - 1); // smaller part -> recursion
                lo = gt + 1;              // larger part -> loop
            } else {
                quickSort(a, gt + 1, hi);
                hi = lt - 1;
            }
        }
        m.exit();
    }

    private void swap(int[] a, int i, int j) {
        int t = a[i];
        a[i] = a[j];
        a[j] = t;
        m.swaps++;
    }
}
