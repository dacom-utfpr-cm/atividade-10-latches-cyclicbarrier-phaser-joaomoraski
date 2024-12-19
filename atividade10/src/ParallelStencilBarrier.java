import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ParallelStencilBarrier {

    static void parallelStencilWithBarrier(double[] input, double[] output, int iterations, int tasks) throws InterruptedException {
        int n = input.length;
        try (ExecutorService executor = Executors.newFixedThreadPool(tasks)) {
            CyclicBarrier barrier = new CyclicBarrier(tasks);

            for (int iter = 0; iter < iterations; iter++) {
                int chunkSize = n / tasks;

                for (int t = 0; t < tasks; t++) {
                    int start = t * chunkSize;
                    int end = (t == tasks - 1) ? n : start + chunkSize;

                    executor.submit(() -> {
                        try {
                            for (int i = Math.max(1, start); i < Math.min(n - 1, end); i++) {
                                output[i] = (input[i - 1] + input[i] + input[i + 1]) / 3.0;
                            }
                            barrier.await();
                        } catch (Exception e) {
                            Thread.currentThread().interrupt();
                        }
                    });
                }

                barrier.await();
                System.arraycopy(output, 0, input, 0, n);
            }

            executor.shutdown();
        } catch (BrokenBarrierException e) {
            throw new RuntimeException(e);
        }
    }

}
