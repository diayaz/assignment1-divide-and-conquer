package daa;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class SelectTest {

    @Test
    void hundredRandomTests() {
        Random rnd = new Random(2);
        for (int t = 0; t < 100; t++) {
            int n = 1 + rnd.nextInt(1000);
            int[] a = new int[n];
            for (int i = 0; i < n; i++) a[i] = rnd.nextInt(t % 2 == 0 ? 1000000 : 10);
            int k = rnd.nextInt(n);

            int[] sorted = a.clone();
            Arrays.sort(sorted);
            assertEquals(sorted[k], new DeterministicSelector(new Metrics()).select(a.clone(), k));
        }
    }

    @Test
    void sortedAndReverse() {
        int n = 10001;
        int[] a = new int[n];
        int[] b = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = i;
            b[i] = n - 1 - i;
        }
        assertEquals(n / 2, new DeterministicSelector(new Metrics()).select(a, n / 2));
        assertEquals(n / 2, new DeterministicSelector(new Metrics()).select(b, n / 2));
    }

    @Test
    void singleElement() {
        assertEquals(7, new DeterministicSelector(new Metrics()).select(new int[]{7}, 0));
    }

    @Test
    void badInput() {
        DeterministicSelector s = new DeterministicSelector(new Metrics());
        assertThrows(IllegalArgumentException.class, () -> s.select(new int[0], 0));
        assertThrows(IllegalArgumentException.class, () -> s.select(new int[]{1, 2}, 5));
    }
}
