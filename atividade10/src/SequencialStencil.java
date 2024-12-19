public class SequencialStencil {

    static void sequentialStencil(double[] input, double[] output, int iterations) {
        int n = input.length;
        for (int iter = 0; iter < iterations; iter++) {
            for (int i = 1; i < n - 1; i++) {
                output[i] = (input[i - 1] + input[i] + input[i + 1]) / 3.0;
            }
            System.arraycopy(output, 0, input, 0, n);
        }
    }
}
