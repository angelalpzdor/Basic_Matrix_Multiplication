public class OptimizedMatrix {

    /**
     * Performs the standard O(n^3) matrix multiplication C = A * B.
     * Naive implementation with three nested loops in order i-j-k.
     */
    public static double[][] multiplyBasic(double[][] A, double[][] B) {
        int n = A.length;
        double[][] C = new double[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                double sum = 0.0;
                for (int k = 0; k < n; k++) {
                    sum += A[i][k] * B[k][j];
                }
                C[i][j] = sum;
            }
        }

        return C;
    }

    /**
     * Performs a cache-friendly version of matrix multiplication
     * by using the transposed version of B and reordering loops (i - k - j).
     */
    public static double[][] multiplyCacheFriendly(double[][] A, double[][] B) {
        int n = A.length;
        double[][] C = new double[n][n];

        // Precompute the transpose of B for sequential memory access
        double[][] B_T = transpose(B);

        for (int i = 0; i < n; i++) {
            for (int k = 0; k < n; k++) {
                double a = A[i][k];
                for (int j = 0; j < n; j++) {
                    C[i][j] += a * B_T[j][k];
                }
            }
        }

        return C;
    }

    /**
     * Performs blocked (tiled) matrix multiplication.
     * This improves cache locality for large matrices.
     */
    public static double[][] multiplyTiled(double[][] A, double[][] B, int blockSize) {
        int n = A.length;
        double[][] C = new double[n][n];

        // Again, transpose B to optimize memory access
        double[][] B_T = transpose(B);

        for (int ii = 0; ii < n; ii += blockSize) {
            for (int kk = 0; kk < n; kk += blockSize) {
                for (int jj = 0; jj < n; jj += blockSize) {

                    int iMax = Math.min(ii + blockSize, n);
                    int kMax = Math.min(kk + blockSize, n);
                    int jMax = Math.min(jj + blockSize, n);

                    for (int i = ii; i < iMax; i++) {
                        for (int k = kk; k < kMax; k++) {
                            double a = A[i][k];
                            for (int j = jj; j < jMax; j++) {
                                C[i][j] += a * B_T[j][k];
                            }
                        }
                    }

                }
            }
        }

        return C;
    }

    /**
     *returns the transpose of a square matrix.
     */
    public static double[][] transpose(double[][] M) {
        int n = M.length;
        double[][] T = new double[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                T[j][i] = M[i][j];
            }
        }

        return T;
    }
}
