import java.io.BufferedReader;
import java.io.FileReader;

/**
 * Utility class to read Matrix Market (.mtx) files and convert them into CSR format.
 * Supports real-valued sparse matrices in coordinate (triplet) format.
 */
public class MatrixMarketReader {

    public static CSRMatrix readCSR(String filename) throws Exception {

        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line;

        // Skip comments starting with '%'
        do {
            line = br.readLine();
        } while (line != null && line.startsWith("%"));

        if (line == null)
            throw new Exception("Invalid Matrix Market file: missing header.");

        // Read rows, cols, and number of non-zero entries
        String[] header = line.trim().split("\\s+");
        int rows = Integer.parseInt(header[0]);
        int cols = Integer.parseInt(header[1]);
        int nnz  = Integer.parseInt(header[2]);

        // Arrays to hold triplets
        int[] I = new int[nnz];
        int[] J = new int[nnz];
        double[] V = new double[nnz];

        // Count non-zero entries per row
        int[] rowCount = new int[rows];

        for (int k = 0; k < nnz; k++) {
            line = br.readLine();
            String[] entry = line.split("\\s+");

            int i = Integer.parseInt(entry[0]) - 1;  // convert to 0-based
            int j = Integer.parseInt(entry[1]) - 1;
            double val = Double.parseDouble(entry[2]);

            I[k] = i;
            J[k] = j;
            V[k] = val;

            rowCount[i]++;
        }

        br.close();

        // Build rowPtr array
        int[] rowPtr = new int[rows + 1];
        for (int i = 1; i <= rows; i++) {
            rowPtr[i] = rowPtr[i - 1] + rowCount[i - 1];
        }

        // Allocate CSR arrays
        double[] values = new double[nnz];
        int[] colIndex = new int[nnz];
        int[] filled = new int[rows];

        // Fill CSR structure
        for (int k = 0; k < nnz; k++) {
            int r = I[k];
            int dest = rowPtr[r] + filled[r];

            values[dest] = V[k];
            colIndex[dest] = J[k];

            filled[r]++;
        }

        // Create CSRMatrix instance
        CSRMatrix M = new CSRMatrix();
        M.rows = rows;
        M.cols = cols;
        M.values = values;
        M.colIndex = colIndex;
        M.rowPtr = rowPtr;

        return M;
    }
}
