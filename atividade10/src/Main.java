import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) {
        int n = 100;
        int iterations = 10;
        int tasks = 4;
        double[] input = IntStream.range(0, n).mapToDouble(i -> i).toArray();
        double[] output = new double[n];

        System.out.println("Initial Array:");
        System.out.println(java.util.Arrays.toString(input));

        // Implemente uma solução sequencial para o problema.
        SequencialStencil.sequentialStencil(input.clone(), output, iterations);
        System.out.println("Sequential Solution:");
        System.out.println(java.util.Arrays.toString(output));


        // Latch
        try {
            ParallelStencilLatch.parallelStencilWithLatch(input.clone(), output, iterations, tasks);
            System.out.println("Parallel Solution with Latch:");
            System.out.println(java.util.Arrays.toString(output));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


        // Barrier
        try {
            ParallelStencilBarrier.parallelStencilWithBarrier(input.clone(), output, iterations, tasks);
            System.out.println("Parallel Solution with Barrier:");
            System.out.println(java.util.Arrays.toString(output));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


        // Phaser
        ParallelStencilPhaser.parallelStencilWithPhaser(input.clone(), output, iterations, tasks);
        System.out.println("Parallel Solution with Phaser:");
        System.out.println(java.util.Arrays.toString(output));


    }
}