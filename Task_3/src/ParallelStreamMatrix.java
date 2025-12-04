import java.util.stream.IntStream;

public class ParallelStreamMatrix {

    // Parallel multiplication using Java parallel streams
    public static double[][] multiply(double[][] A, double[][] B) {

        int n = A.length;
        double[][] C = new double[n][n];

        IntStream.range(0, n).parallel().forEach(i -> {
            for (int j = 0; j < n; j++) {
                double sum = 0;
                for (int k = 0; k < n; k++) {
                    sum += A[i][k] * B[k][j];
                }
                C[i][j] = sum;
            }
        });

        return C;
    }
}
