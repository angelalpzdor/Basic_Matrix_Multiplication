import java.io.*;
import java.util.*;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

public class Benchmark {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter matrix size: ");
        int n = sc.nextInt();
        System.out.print("Enter number of runs: ");
        int runs = sc.nextInt();

        List<Double> times = new ArrayList<>();
        List<Double> memoryUsages = new ArrayList<>();

        for (int i = 0; i < runs; i++) {
            double memoryBefore = getMemoryMB();
            long start = System.nanoTime();
            Matrix.multiplyMatrices(n);
            long end = System.nanoTime();
            double duration = (end - start) / 1e9;
            double memoryAfter = getMemoryMB();
            double usedMemory = Math.max(0.0, memoryAfter - memoryBefore);

            times.add(duration);
            memoryUsages.add(usedMemory);

            System.out.printf("Running %d/%d: %.4f seconds, Memory used: %.2f MB%n",
                    i + 1, runs, duration, usedMemory);
        }

        double mean = mean(times);
        double stdev = stdev(times, mean);
        double min = Collections.min(times);
        double max = Collections.max(times);
        double realMemory = mean(memoryUsages);
        double theoreticalMemory = estimateMemoryMB(n);

        System.out.println("\nResults:");
        System.out.printf("Matrix size: %dx%d%n", n, n);
        System.out.printf("Number of runs: %d%n", runs);
        System.out.printf("Average time: %.4f s%n", mean);
        System.out.printf("Standard deviation: %.4f s%n", stdev);
        System.out.printf("Best time: %.4f s%n", min);
        System.out.printf("Worst time: %.4f s%n", max);
        System.out.printf("Average real memory used: %.2f MB%n", realMemory);
        System.out.printf("Theoretical memory usage: %.2f MB%n", theoreticalMemory);

        saveResult("java", n, runs, mean, stdev, min, max, realMemory, theoreticalMemory);
    }

    public static double getMemoryMB() {
        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage heapUsage = memoryBean.getHeapMemoryUsage();
        return heapUsage.getUsed() / (1024.0 * 1024.0);
    }

    public static double estimateMemoryMB(int n) {
        return 3.0 * n * n * 8 / (1024.0 * 1024.0);
    }

    public static double mean(List<Double> list) {
        double sum = 0.0;
        for (double v : list) sum += v;
        return sum / list.size();
    }

    public static double stdev(List<Double> list, double mean) {
        if (list.size() <= 1) return 0.0;
        double sum = 0.0;
        for (double v : list) sum += Math.pow(v - mean, 2);
        return Math.sqrt(sum / (list.size() - 1));
    }

    public static void saveResult(String language, int n, int runs, double mean,
                                  double stdev, double min, double max,
                                  double realMemory, double theoreticalMemory) {
        try {
            File folder = new File("results");
            if (!folder.exists()) folder.mkdirs();

            File file = new File("results/benchmark_results.csv");
            boolean exists = file.exists();

            FileWriter fw = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter writer = new PrintWriter(bw);

            if (!exists) {
                writer.println("language,size,runs,mean (s),stdev,best,worst,real memory (MB),theoretical memory (MB)");
            }

            writer.printf(Locale.US,
                    "%s,%d,%d,%.4f,%.4f,%.4f,%.4f,%.2f,%.2f%n",
                    language, n, runs, mean, stdev, min, max, realMemory, theoreticalMemory);

            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving results: " + e.getMessage());
        }
    }
}
