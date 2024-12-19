import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;

public class ParallelStencilPhaser {

    static void parallelStencilWithPhaser(double[] input, double[] output, int iterations, int tasks) {
        int n = input.length;
        Phaser[] phasers = new Phaser[tasks + 1];
        for (int i = 0; i <= tasks; i++) {
            phasers[i] = new Phaser(1);
        }

        ExecutorService executor = Executors.newFixedThreadPool(tasks);
        int chunkSize = n / tasks;

        for (int t = 0; t < tasks; t++) {
            int start = t * chunkSize;
            int end = (t == tasks - 1) ? n : start + chunkSize;
            final Phaser leftPhaser = phasers[t];
            final Phaser rightPhaser = phasers[t + 1];

            executor.submit(() -> {
                for (int iter = 0; iter < iterations; iter++) {
                    for (int i = Math.max(1, start); i < Math.min(n - 1, end); i++) {
                        output[i] = (input[i - 1] + input[i] + input[i + 1]) / 3.0;
                    }
                    leftPhaser.arriveAndAwaitAdvance();
                    rightPhaser.arriveAndAwaitAdvance();
                }
            });
        }

        for (int iter = 0; iter < iterations; iter++) {
            phasers[0].arriveAndAwaitAdvance();
            phasers[tasks].arriveAndAwaitAdvance();
            System.arraycopy(output, 0, input, 0, n);
        }

        executor.shutdown();
    }
}
