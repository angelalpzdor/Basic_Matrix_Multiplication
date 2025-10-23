import java.util.Random;

public class Matrix {

    public static double[][] multiplyMatrices(int n) {
        Random rand = new Random();
        double[][] A = new double[n][n];
        double[][] B = new double[n][n];
        double[][] B_T = new double[n][n];
        double[][] C = new double[n][n];

        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++) {
                A[i][j] = rand.nextDouble();
                B[i][j] = rand.nextDouble();
            }

        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                B_T[i][j] = B[j][i];

        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++) {
                double sum = 0.0;
                for (int k = 0; k < n; k++)
                    sum += A[i][k] * B_T[j][k];
                C[i][j] = sum;
            }

        return C;
    }
}
