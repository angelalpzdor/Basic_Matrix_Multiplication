import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IExecutorService;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;

/**
 * Main driver class for Distributed Matrix Multiplication using Hazelcast.
 * It measures both execution time (Scalability) and memory usage (Resource Utilization).
 */
public class DistributedMatrixMultiplication {

    public static void main(String[] args) {
        // 1. Ensure the output directory exists
        new File("results").mkdirs();

        // 2. Initialize Hazelcast Cluster Node
        HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance();
        IExecutorService executorService = hazelcastInstance.getExecutorService("matrixExecutor");

        // 3. Define matrix dimensions to test
        // Sizes are kept moderate to ensure stability on a single machine simulation
        int[] sizes = {200, 400, 600}; 

        System.out.println("=== Starting Distributed Matrix Multiplication (Benchmarks) ===");

        // Clear previous results to avoid duplicates in the report
        clearCsv("results/performance.csv");

        for (int size : sizes) {
            System.out.println("\nProcessing matrix size: " + size + "x" + size + "...");
            
            try {
                // Garbage collection to get a clean memory reading
                System.gc();
                long memoryBefore = getUsedMemory();

                // Generate random matrices
                double[][] matrixA = MatrixUtils.generateRandomMatrix(size, size);
                double[][] matrixB = MatrixUtils.generateRandomMatrix(size, size);
                double[][] resultC = new double[size][size];

                long startTime = System.currentTimeMillis();

                // 4. Distribute Tasks: Row-wise partitioning
                List<Future<double[]>> futures = new ArrayList<>();
                
                for (int i = 0; i < size; i++) {
                    // Each task computes one row of the resulting matrix
                    RowBlockTask task = new RowBlockTask(matrixA[i], matrixB);
                    futures.add(executorService.submit(task));
                }

                // 5. Aggregate Results (Reduce Phase)
                for (int i = 0; i < size; i++) {
                    resultC[i] = futures.get(i).get();
                }

                long endTime = System.currentTimeMillis();
                long duration = endTime - startTime;
                
                // Measure memory after execution
                long memoryAfter = getUsedMemory();
                long memoryUsed = Math.max(0, memoryAfter - memoryBefore);

                System.out.println(" -> Done. Time: " + duration + " ms | RAM Used: " + memoryUsed + " MB");
                
                // 6. Save metrics to CSV (Size, Time, Memory)
                saveResult("results/performance.csv", size, duration, memoryUsed);

            } catch (Exception e) {
                System.err.println("Error processing size " + size + ": " + e.getMessage());
                e.printStackTrace();
            }
        }
        
        System.out.println("\n=== All tests finished successfully. ===");
        System.out.println("Results saved to: results/performance.csv");
        
        // Terminate the process
        System.exit(0);
    }

    // --- Helper Methods ---
    
    /**
     * Calculates the currently used heap memory in Megabytes (MB).
     */
    private static long getUsedMemory() {
        Runtime runtime = Runtime.getRuntime();
        return (runtime.totalMemory() - runtime.freeMemory()) / (1024 * 1024);
    }

    /**
     * Appends a benchmark record to the CSV file.
     */
    private static void saveResult(String filename, int size, long time, long memory) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename, true))) {
            writer.println(size + "," + time + "," + memory);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Clears the CSV file at the start of the run.
     */
    private static void clearCsv(String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            // Write nothing to clear the file
        } catch (IOException e) {
            // File might not exist yet, which is fine
        }
    }
}