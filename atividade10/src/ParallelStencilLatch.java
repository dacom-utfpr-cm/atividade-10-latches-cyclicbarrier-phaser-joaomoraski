import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ParallelStencilLatch {

    // Solução Paralela com Latch
    static void parallelStencilWithLatch(double[] input, double[] output, int iterations, int tasks) throws InterruptedException {
        int n = input.length;
        ExecutorService executor = Executors.newFixedThreadPool(tasks);

        for (int iter = 0; iter < iterations; iter++) {
            CountDownLatch latch = new CountDownLatch(tasks);
            int chunkSize = n / tasks;

            for (int t = 0; t < tasks; t++) {
                int start = t * chunkSize;
                int end = (t == tasks - 1) ? n : start + chunkSize;

                executor.submit(() -> {
                    for (int i = Math.max(1, start); i < Math.min(n - 1, end); i++) {
                        output[i] = (input[i - 1] + input[i] + input[i + 1]) / 3.0;
                    }
                    latch.countDown();
                });
            }

            latch.await();
            System.arraycopy(output, 0, input, 0, n);
        }

        executor.shutdown();
    }
}
