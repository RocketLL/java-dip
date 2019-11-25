package land.lucas.simplelinalg;

import java.util.AbstractCollection;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

// class, inheritance, polymorphism, collection


public class DoubleMatrix2D extends AbstractCollection implements Matrix2D {
    public double[][] data;
    public int[] shape = {0, 0};

    @Override
    public int size() {
        return shape[0] * shape[1];
    }

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

    public DoubleMatrix2D(double[][] mat) throws ArithmeticException {
        if (!verifyMatrix(mat)) {
            throw new ArithmeticException("Invalid matrix");
        }

        this.data = mat;
        this.shape[0] = mat.length;
        this.shape[1] = mat[0].length;
    }

    public DoubleMatrix2D(int i, int j) {
        this.data = generateZeros(i, j);

        this.shape[0] = i;
        this.shape[1] = j;
    }

    public double get(int i, int j) {
        return this.data[i][j];
    }

    public boolean set(int i, int j, double val) {
        try {
            this.data[i][j] = val;
            return true;
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
    }

    boolean verifyMatrix(double[][] mat) {
        boolean valid = true;
        int columns = mat[0].length;

        for (double[] row : mat) {
            if (row.length != columns) {
                valid = false;
            }
        }

        return valid;
    }

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

    public static DoubleMatrix2D mul(DoubleMatrix2D m, int k) {
        DoubleMatrix2D result = new DoubleMatrix2D(m.shape[0], m.shape[1]);

        for (int i = 0; i < m.shape[0]; i++) {
            for (int j = 0; j < m.shape[1]; j++) {
                result.set(i, j, m.get(i, j) * k);
            }
        }

        return result;
    }

    public DoubleMatrix2D mul(int k) {
        return mul(this, k);
    }

    public static DoubleMatrix2D sub(DoubleMatrix2D m, DoubleMatrix2D n) {
        return add(m, n.mul(-1));
    }

    public DoubleMatrix2D sub(DoubleMatrix2D mat) {
        return sub(this, mat);
    }

    public DoubleMatrix2D add(DoubleMatrix2D mat) {
        return add(this, mat);
    }

    private static double[][] generateZeros(int i, int j) {
        double[][] zeroArray = new double[i][j];

        for (int k = 0; k < i; k++) {
            for (int l = 0; l < j; l++) {
                zeroArray[k][l] = 0.;
            }
        }

        return zeroArray;
    }

    public static DoubleMatrix2D zeros(int i, int j) {
        double[][] zeroArray = generateZeros(i, j);

        return new DoubleMatrix2D(zeroArray);
    }

    public void print() {
        for (double[] row : this.data) {
            for (double element : row) {
                System.out.print(element + " ");
            }
            System.out.println();
        }
    }

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