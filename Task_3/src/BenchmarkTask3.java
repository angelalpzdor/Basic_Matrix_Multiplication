import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class BenchmarkTask3 {

    // Create a random square matrix
    public static double[][] generateMatrix(int n) {
        double[][] M = new double[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                M[i][j] = Math.random();
        return M;
    }

    // Get used memory in bytes
    public static long measureMemory() {
        Runtime rt = Runtime.getRuntime();
        rt.gc();
        return rt.totalMemory() - rt.freeMemory();
    }

    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {

        int[] sizes = {200, 300, 400, 500};
        int[] threads = {2, 4, 8};

        FileWriter csv = new FileWriter("../results/task3_results.csv");
        csv.write("size,method,threads,time_ms,speedup,efficiency,memory_bytes\n");

        for (int n : sizes) {

            double[][] A = generateMatrix(n);
            double[][] B = generateMatrix(n);

            // Baseline
            long mem0 = measureMemory();
            long t0 = System.nanoTime();
            BaselineMatrix.multiply(A, B);
            long baseTime = (System.nanoTime() - t0) / 1_000_000;
            long memBase = measureMemory() - mem0;

            csv.write(n + ",baseline,1," + baseTime + ",1,1," + memBase + "\n");

            // ThreadPool
            for (int th : threads) {
                long mem1 = measureMemory();
                long t1 = System.nanoTime();
                ParallelMatrix.multiply(A, B, th);
                long parTime = (System.nanoTime() - t1) / 1_000_000;
                long memPar = measureMemory() - mem1;

                double speedup = (double) baseTime / parTime;
                double eff = speedup / th;

                csv.write(n + ",parallel_threads," + th + "," +
                        parTime + "," + speedup + "," + eff + "," + memPar + "\n");
            }

            // Parallel Streams
            long mem2 = measureMemory();
            long t2 = System.nanoTime();
            ParallelStreamMatrix.multiply(A, B);
            long psTime = (System.nanoTime() - t2) / 1_000_000;
            long memPS = measureMemory() - mem2;

            double speedupPS = (double) baseTime / psTime;

            csv.write(n + ",parallel_streams," +
                    Runtime.getRuntime().availableProcessors() + "," +
                    psTime + "," + speedupPS + ",-," + memPS + "\n");

            // Vectorized
            long mem3 = measureMemory();
            long t3 = System.nanoTime();
            VectorizedMatrix.multiply(A, B);
            long vecTime = (System.nanoTime() - t3) / 1_000_000;
            long memVec = measureMemory() - mem3;

            double speedupVec = (double) baseTime / vecTime;

            csv.write(n + ",vectorized,1," + vecTime + "," + speedupVec + ",1," + memVec + "\n");
        }

        csv.close();
        System.out.println("Done. File saved in Task_3/results/");
    }
}
