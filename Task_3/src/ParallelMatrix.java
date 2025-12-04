import java.util.concurrent.*;

public class ParallelMatrix {

    // Parallel multiplication using a fixed thread pool
    public static double[][] multiply(double[][] A, double[][] B, int threads)
            throws InterruptedException, ExecutionException {

        int n = A.length;
        double[][] C = new double[n][n];

        ExecutorService executor = Executors.newFixedThreadPool(threads);
        Future<?>[] futures = new Future<?>[threads];

        int chunk = (int) Math.ceil(n * 1.0 / threads);

        for (int t = 0; t < threads; t++) {
            int start = t * chunk;
            int end = Math.min(start + chunk, n);

            // Each thread computes a block of rows
            futures[t] = executor.submit(() -> {
                for (int i = start; i < end; i++) {
                    for (int j = 0; j < n; j++) {
                        double sum = 0;
                        for (int k = 0; k < n; k++) {
                            sum += A[i][k] * B[k][j];
                        }
                        C[i][j] = sum;
                    }
                }
            });
        }

        // Wait for all threads
        for (Future<?> f : futures) f.get();
        executor.shutdown();

        return C;
    }
}
