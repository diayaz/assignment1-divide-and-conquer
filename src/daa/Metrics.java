package daa;

// Counts comparisons, swaps, recursive calls and max recursion depth.
public class Metrics {
    public long comparisons;
    public long swaps;
    public long calls;
    public int depth;
    public int maxDepth;

    public void enter() {
        calls++;
        depth++;
        if (depth > maxDepth) maxDepth = depth;
    }

    public void exit() {
        depth--;
    }

    @Override
    public String toString() {
        return "comparisons=" + comparisons + ", swaps=" + swaps
                + ", calls=" + calls + ", maxDepth=" + maxDepth;
    }
}
