package daa;

// MergeSort: linear merge, one reusable buffer, insertion sort for small parts.
// T(n) = 2T(n/2) + O(n) = O(n log n)
public class MergeSorter {
    private static final int CUTOFF = 16;
    private final Metrics m;

    public MergeSorter(Metrics m) {
        this.m = m;
    }

    public void sort(int[] a) {
        if (a == null || a.length < 2) return;
        int[] buf = new int[a.length]; // allocated once, reused in every merge
        sort(a, buf, 0, a.length - 1);
    }

    private void sort(int[] a, int[] buf, int lo, int hi) {
        m.enter();
        if (hi - lo + 1 <= CUTOFF) {
            insertionSort(a, lo, hi);
        } else {
            int mid = (lo + hi) >>> 1;
            sort(a, buf, lo, mid);
            sort(a, buf, mid + 1, hi);
            merge(a, buf, lo, mid, hi);
        }
        m.exit();
    }

    private void merge(int[] a, int[] buf, int lo, int mid, int hi) {
        for (int k = lo; k <= hi; k++) buf[k] = a[k];
        int i = lo, j = mid + 1;
        for (int k = lo; k <= hi; k++) {
            if (i > mid) a[k] = buf[j++];
            else if (j > hi) a[k] = buf[i++];
            else {
                m.comparisons++;
                if (buf[j] < buf[i]) a[k] = buf[j++];
                else a[k] = buf[i++];
            }
        }
    }

    private void insertionSort(int[] a, int lo, int hi) {
        for (int i = lo + 1; i <= hi; i++) {
            int x = a[i];
            int j = i - 1;
            while (j >= lo) {
                m.comparisons++;
                if (a[j] <= x) break;
                a[j + 1] = a[j];
                j--;
            }
            a[j + 1] = x;
        }
    }
}
