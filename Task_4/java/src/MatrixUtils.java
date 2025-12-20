import java.util.Random;

/**
 * Utility class for matrix operations.
 */
public class MatrixUtils {

    /**
     * Generates a square matrix of size rows x cols filled with random doubles.
     */
    public static double[][] generateRandomMatrix(int rows, int cols) {
        double[][] matrix = new double[rows][cols];
        Random rand = new Random();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = rand.nextDouble();
            }
        }
        return matrix;
    }
}