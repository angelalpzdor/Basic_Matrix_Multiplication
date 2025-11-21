import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class BenchmarkTask2 {

    // Used to generate random matrices
    private static double[][] generateRandomMatrix(int n) {
        Random rand = new Random();
        double[][] M = new double[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                M[i][j] = rand.nextDouble();
            }
        }
        return M;
    }

    // Random dense vector for sparse multiply
    private static double[] generateRandomVector(int n) {
        Random rand = new Random();
        double[] v = new double[n];
        for (int i = 0; i < n; i++) v[i] = rand.nextDouble();
        return v;
    }

    // Measure memory in MB
    private static double getMemoryUsageMB() {
        Runtime rt = Runtime.getRuntime();
        return (rt.totalMemory() - rt.freeMemory()) / (1024.0 * 1024.0);
    }

    // Append row to CSV
    private static void appendCSV(String row) {
        try (FileWriter fw = new FileWriter("benchmark_task2_results.csv", true)) {
            fw.write(row + "\n");
        } catch (IOException e) {
            System.out.println("Error writing CSV: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        try {
            // User input
            java.util.Scanner sc = new java.util.Scanner(System.in);
            System.out.print("Enter matrix size n (dense): ");
            int n = sc.nextInt();
            System.out.print("Enter number of repetitions: ");
            int runs = sc.nextInt();

            // Generate two dense matrices
            double[][] A = generateRandomMatrix(n);
            double[][] B = generateRandomMatrix(n);

            System.out.println("\n===== DENSE MULTIPLICATION TESTS =====");

            // Benchmark: basic, cache and tiled
            benchmarkDense("basic", n, runs, A, B);
            benchmarkDense("cache", n, runs, A, B);
            benchmarkDense("tiled16", n, runs, A, B, 16);
            benchmarkDense("tiled32", n, runs, A, B, 32);
            benchmarkDense("tiled64", n, runs, A, B, 64);

            System.out.println("\n===== SPARSE MULTIPLICATION TEST =====");

            // Benchmark Sparse CSR
            benchmarkSparse();

            System.out.println("\nDone. Results saved to benchmark_task2_results.csv");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Benchmark dense version without tiling
    private static void benchmarkDense(String algorithm, int n, int runs,
                                       double[][] A, double[][] B) {

        double totalTime = 0;
        double best = Double.MAX_VALUE;
        double worst = Double.MIN_VALUE;

        for (int r = 0; r < runs; r++) {
            double memBefore = getMemoryUsageMB();
            long start = System.nanoTime();

            double[][] C;
            if (algorithm.equals("basic")) {
                C = OptimizedMatrix.multiplyBasic(A, B);
            } else {
                C = OptimizedMatrix.multiplyCacheFriendly(A, B);
            }

            long end = System.nanoTime();
            double memAfter = getMemoryUsageMB();

            double elapsedSec = (end - start) / 1e9;
            double memUsed = Math.max(0, memAfter - memBefore);

            totalTime += elapsedSec;
            best = Math.min(best, elapsedSec);
            worst = Math.max(worst, elapsedSec);

            System.out.printf("[%s] Run %d/%d: %.6f s, %.2f MB\n",
                    algorithm, r + 1, runs, elapsedSec, memUsed);
        }

        double mean = totalTime / runs;

        appendCSV("java," + algorithm + "," + n + "," + runs + "," +
                mean + "," + best + "," + worst);
    }

    // Benchmark dense tiled version
    private static void benchmarkDense(String algorithm, int n, int runs,
                                       double[][] A, double[][] B, int blockSize) {

        double totalTime = 0;
        double best = Double.MAX_VALUE;
        double worst = Double.MIN_VALUE;

        for (int r = 0; r < runs; r++) {

            double memBefore = getMemoryUsageMB();
            long start = System.nanoTime();

            double[][] C = OptimizedMatrix.multiplyTiled(A, B, blockSize);

            long end = System.nanoTime();
            double memAfter = getMemoryUsageMB();

            double elapsedSec = (end - start) / 1e9;
            double memUsed = Math.max(0, memAfter - memBefore);

            totalTime += elapsedSec;
            best = Math.min(best, elapsedSec);
            worst = Math.max(worst, elapsedSec);

            System.out.printf("[%s] Run %d/%d: %.6f s, %.2f MB\n",
                    algorithm, r + 1, runs, elapsedSec, memUsed);
        }

        double mean = totalTime / runs;

        appendCSV("java," + algorithm + "," + n + "," + runs + "," +
                mean + "," + best + "," + worst);
    }

    // Sparse CSR benchmark using mc2depi.mtx
    private static void benchmarkSparse() {
        try {
            // Load sparse matrix
            CSRMatrix M = MatrixMarketReader.readCSR("mc2depi.mtx");

            System.out.println("Loaded sparse matrix: " + M.rows + " x " + M.cols);
            System.out.println("Non-zeros: " + M.values.length);

            double[] x = generateRandomVector(M.cols);

            int runs = 10;
            double total = 0, best = Double.MAX_VALUE, worst = Double.MIN_VALUE;

            for (int r = 0; r < runs; r++) {
                double memBefore = getMemoryUsageMB();
                long start = System.nanoTime();

                double[] y = M.multiply(x);

                long end = System.nanoTime();
                double memAfter = getMemoryUsageMB();

                double elapsed = (end - start) / 1e9;
                double memUsed = Math.max(0, memAfter - memBefore);

                total += elapsed;
                best = Math.min(best, elapsed);
                worst = Math.max(worst, elapsed);

                System.out.printf("[sparse] Run %d/%d: %.6f s, %.2f MB\n",
                        r + 1, runs, elapsed, memUsed);
            }

            double mean = total / runs;

            appendCSV("java,sparseCSR,matrix_market," + runs + "," +
                    mean + "," + best + "," + worst);

        } catch (Exception e) {
            System.out.println("Sparse benchmark failed: " + e.getMessage());
        }
    }
}
