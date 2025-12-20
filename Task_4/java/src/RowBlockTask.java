import java.io.Serializable;
import java.util.concurrent.Callable;

/**
 * A distributed task that multiplies a single row of Matrix A 
 * with the entire Matrix B.
 * Implements Callable for returning a result and Serializable for network transmission.
 */
public class RowBlockTask implements Callable<double[]>, Serializable {
    private static final long serialVersionUID = 1L;
    
    private final double[] rowA;
    private final double[][] matrixB;

    public RowBlockTask(double[] rowA, double[][] matrixB) {
        this.rowA = rowA;
        this.matrixB = matrixB;
    }

    @Override
    public double[] call() {
        int columnsB = matrixB[0].length;
        double[] resultRow = new double[columnsB];

        // Perform dot product for the assigned row
        for (int j = 0; j < columnsB; j++) {
            for (int k = 0; k < rowA.length; k++) {
                resultRow[j] += rowA[k] * matrixB[k][j];
            }
        }
        return resultRow;
    }
}