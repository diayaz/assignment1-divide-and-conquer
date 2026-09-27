package daa;

// Deterministic Select (Median-of-Medians): k-th smallest element (k is 0-based).
// Groups of 5 -> median of medians as pivot -> in-place partition -> recurse into one side.
// T(n) <= T(n/5) + T(7n/10) + O(n) = O(n)
public class DeterministicSelector {
    private final Metrics m;

    public DeterministicSelector(Metrics m) {
        this.m = m;
    }

    // Note: changes the order of elements in a.
    public int select(int[] a, int k) {
        if (a == null || a.length == 0) throw new IllegalArgumentException("empty array");
        if (k < 0 || k >= a.length) throw new IllegalArgumentException("bad k: " + k);
        return select(a, 0, a.length - 1, k);
    }

    private int select(int[] a, int lo, int hi, int k) {
        m.enter();
        try {
            if (hi - lo + 1 <= 5) {
                insertionSort(a, lo, hi);
                return a[k];
            }

            int pivot = medianOfMedians(a, lo, hi);

            // 3-way partition: < pivot | == pivot | > pivot
            int lt = lo, gt = hi, i = lo;
            while (i <= gt) {
                m.comparisons++;
                if (a[i] < pivot) swap(a, lt++, i++);
                else if (a[i] > pivot) swap(a, i, gt--);
                else i++;
            }

            if (k < lt) return select(a, lo, lt - 1, k);
            if (k > gt) return select(a, gt + 1, hi, k);
            return pivot;
        } finally {
            m.exit();
        }
    }

    // Sort each group of 5, move its median to the front, then select the median of medians.
    private int medianOfMedians(int[] a, int lo, int hi) {
        int count = 0;
        for (int start = lo; start <= hi; start += 5) {
            int end = Math.min(start + 4, hi);
            insertionSort(a, start, end);
            swap(a, lo + count, (start + end) / 2);
            count++;
        }
        return select(a, lo, lo + count - 1, lo + (count - 1) / 2);
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

    private void swap(int[] a, int i, int j) {
        int t = a[i];
        a[i] = a[j];
        a[j] = t;
        m.swaps++;
    }
}
