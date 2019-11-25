package land.lucas.simplelinalg;

import java.util.AbstractCollection;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * 2D Matrix containing double data
 */
public class DoubleMatrix2D extends AbstractCollection implements Matrix2D {
    /**
     * Underlying data implementation
     */
    public double[][] data;

    /**
     * Shape of matrix (rows, columns)
     */
    public int[] shape = {0, 0};

    /**
     * @return Number of elements in matrix
     */
    @Override
    public int size() {
        return shape[0] * shape[1];
    }

    /**
     * Iterates over the matrix row-majorly
     *
     * @return Row-major Iterator
     */
    @Override
    public Iterator iterator() {
        return new Iterator() {
            int cnt = 0;

            @Override
            public boolean hasNext() {
                return DoubleMatrix2D.this.shape[0] > this.cnt;
            }

            @Override
            public double[] next() {
                if (hasNext()) {
                    return DoubleMatrix2D.this.data[cnt++];
                } else {
                    throw new NoSuchElementException();
                }
            }
        };
    }

    /**
     * Construct from 2D double data
     *
     * @param mat Matrix to construct data from
     * @throws ArithmeticException Exception for non-rectangular matrices
     */
    public DoubleMatrix2D(double[][] mat) throws ArithmeticException {
        if (!verifyMatrix(mat)) {
            throw new ArithmeticException("Invalid matrix");
        }

        this.data = mat;
        this.shape[0] = mat.length;
        this.shape[1] = mat[0].length;
    }

    /**
     * Construct zero matrix
     *
     * @param i Number of rows
     * @param j Number of columns
     */
    public DoubleMatrix2D(int i, int j) {
        this.data = generateZeros(i, j);

        this.shape[0] = i;
        this.shape[1] = j;
    }

    /**
     * Get data at position by index
     *
     * @param i Row
     * @param j Column
     * @return Value at row and column
     */
    public double get(int i, int j) {
        return this.data[i][j];
    }

    /**
     * Set data at position by index
     *
     * @param i   Row
     * @param j   Column
     * @param val Value to set
     * @return Whether set operation succeeded
     */
    public boolean set(int i, int j, double val) {
        try {
            this.data[i][j] = val;
            return true;
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
    }

    /**
     * Verify whether matrix is valid (rectangular)
     *
     * @param mat Matrix to check
     * @return Whether matrix is valid
     */
    private boolean verifyMatrix(double[][] mat) {
        boolean valid = true;
        int columns = mat[0].length;

        for (double[] row : mat) {
            if (row.length != columns) {
                valid = false;
            }
        }

        return valid;
    }

    /**
     * Adds two DoubleMatrix2Ds together
     *
     * @param m First matrix
     * @param n Second matrix
     * @return Element-wise sum of matrices
     * @throws ArithmeticException Exception for matrices of different shapes
     */
    public static DoubleMatrix2D add(DoubleMatrix2D m, DoubleMatrix2D n) throws ArithmeticException {
        DoubleMatrix2D result = new DoubleMatrix2D(m.shape[0], m.shape[1]);

        if (!Arrays.equals(m.shape, n.shape)) {
            throw new ArithmeticException("Matrix shapes do not match");
        }


        for (int i = 0; i < m.shape[0]; i++) {
            for (int j = 0; j < m.shape[1]; j++) {
                result.set(i, j, m.get(i, j) + n.get(i, j));
            }
        }

        return result;
    }

    /**
     * Multiply matrix by scalar
     *
     * @param m Matrix to multiply
     * @param k Scalar to scale by
     * @return Scaled matrix
     */
    public static DoubleMatrix2D mul(DoubleMatrix2D m, int k) {
        DoubleMatrix2D result = new DoubleMatrix2D(m.shape[0], m.shape[1]);

        for (int i = 0; i < m.shape[0]; i++) {
            for (int j = 0; j < m.shape[1]; j++) {
                result.set(i, j, m.get(i, j) * k);
            }
        }

        return result;
    }

    /**
     * Multiply matrix by scalar
     *
     * @param k Scalar to scale by
     * @return Scaled matrix
     */
    public DoubleMatrix2D mul(int k) {
        return mul(this, k);
    }

    /**
     * Subtract two matrices
     *
     * @param m First matrix
     * @param n Second matrix
     * @return Subtracted matrix
     */
    public static DoubleMatrix2D sub(DoubleMatrix2D m, DoubleMatrix2D n) {
        return add(m, n.mul(-1));
    }

    /**
     * Subtract two matrices
     *
     * @param mat Matrix to subtract by
     * @return Subtracted matrix
     */
    public DoubleMatrix2D sub(DoubleMatrix2D mat) {
        return sub(this, mat);
    }

    /**
     * Add two matrices
     *
     * @param mat Matrix to add by
     * @return Summed matrix
     */
    public DoubleMatrix2D add(DoubleMatrix2D mat) {
        return add(this, mat);
    }

    /**
     * Generate 2D zero double array
     *
     * @param i Rows
     * @param j Columns
     * @return Zero array
     */
    private static double[][] generateZeros(int i, int j) {
        double[][] zeroArray = new double[i][j];

        for (int k = 0; k < i; k++) {
            for (int l = 0; l < j; l++) {
                zeroArray[k][l] = 0.;
            }
        }

        return zeroArray;
    }

    /**
     * Generate zero matrix
     *
     * @param i Rows
     * @param j Columns
     * @return Zero matrix
     */
    public static DoubleMatrix2D zeros(int i, int j) {
        double[][] zeroArray = generateZeros(i, j);

        return new DoubleMatrix2D(zeroArray);
    }

    /**
     * Print matrix
     */
    public void print() {
        for (double[] row : this.data) {
            for (double element : row) {
                System.out.print(element + " ");
            }
            System.out.println();
        }
    }

    /**
     * Convolve matrix with kernel
     *
     * @param kernel Kernel to convolve by
     * @return Convolved matrix
     */
    public DoubleMatrix2D convolve(DoubleMatrix2D kernel) {
        if ((kernel.shape[0] & 1) == 0 || (kernel.shape[1] & 1) == 0) {
            throw new IllegalArgumentException("Kernel must have odd size");
        }

        int padHeight = kernel.shape[0] >>> 1;
        int padWidth = kernel.shape[1] >>> 1;

        DoubleMatrix2D result = new DoubleMatrix2D(this.shape[0], this.shape[1]);

        for (int i = this.shape[0] - 1; i >= 0; i--) {

            for (int j = this.shape[1] - 1; j >= 0; j--) {
                double accumulator = 0.;

                for (int k = kernel.shape[0] - 1; k >= 0; k--) {

                    for (int l = kernel.shape[1] - 1; l >= 0; l--) {

                        accumulator += kernel.get(k, l) * this.get(
                                boundary(i + k - padHeight, this.shape[0]),
                                boundary(j + l - padWidth, this.shape[1])
                        );
                    }
                }

                result.set(i, j, accumulator);
            }
        }

        return result;
    }


    /**
     * Helper function to calculate boundary
     *
     * @param value         Value to compare
     * @param boundaryIndex Index of boundary
     * @return Value by range
     */
    private static int boundary(int value, int boundaryIndex) {
        if (value < 0) {
            return 0;
        }
        if (value < boundaryIndex) {
            return value;
        }

        return boundaryIndex - 1;
    }
}
