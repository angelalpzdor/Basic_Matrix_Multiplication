/**
 * Class representing a sparse matrix stored in CSR format.
 * CSR = Compressed Sparse Row:
 *
 */
public class CSRMatrix {

    public int rows;
    public int cols;

    public double[] values;
    public int[] colIndex;
    public int[] rowPtr;

    /**
     * Multiplies this CSR matrix with a dense vector x.
     * Performs y = A * x.
     */
    public double[] multiply(double[] x) {
        double[] y = new double[rows];

        for (int i = 0; i < rows; i++) {
            double sum = 0.0;
            for (int k = rowPtr[i]; k < rowPtr[i + 1]; k++) {
                sum += values[k] * x[colIndex[k]];
            }
            y[i] = sum;
        }

        return y;
    }
}
