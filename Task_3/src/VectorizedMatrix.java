public class VectorizedMatrix {

    // Manual SIMD-style vectorization (processes 4 elements per loop)
    public static double[][] multiply(double[][] A, double[][] B) {

        int n = A.length;
        double[][] C = new double[n][n];

        int block = 4;  // SIMD block size

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {

                double sum = 0;
                int k = 0;

                // Vectorized loop processing 4 values per iteration
                for (; k <= n - block; k += block) {
                    sum += A[i][k]     * B[k][j];
                    sum += A[i][k + 1] * B[k + 1][j];
                    sum += A[i][k + 2] * B[k + 2][j];
                    sum += A[i][k + 3] * B[k + 3][j];
                }

                // Remaining values when n is not a multiple of 4
                for (; k < n; k++) {
                    sum += A[i][k] * B[k][j];
                }

                C[i][j] = sum;
            }
        }

        return C;
    }
}
