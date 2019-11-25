package land.lucas.simplelinalg;

import java.util.*;


public class IntegerMatrix2D extends AbstractCollection implements Matrix2D {
    public int[][] data;
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
                return IntegerMatrix2D.this.shape[0] > this.cnt;
            }

            @Override
            public int[] next() {
                if (hasNext()) {
                    return IntegerMatrix2D.this.data[cnt++];
                } else {
                    throw new NoSuchElementException();
                }
            }
        };
    }

    public IntegerMatrix2D(int[][] mat) throws ArithmeticException {
        if (!verifyMatrix(mat)) {
            throw new ArithmeticException("Invalid matrix");
        }

        this.data = mat;
        this.shape[0] = mat.length;
        this.shape[1] = mat[0].length;
    }

    public IntegerMatrix2D(int i, int j) {
        this.data = generateZeros(i, j);

        this.shape[0] = i;
        this.shape[1] = j;
    }

    public int get(int i, int j) {
        return this.data[i][j];
    }

    boolean verifyMatrix(int[][] mat) {
        boolean valid = true;
        int columns = mat[0].length;

        for (int[] row : mat) {
            if (row.length != columns) {
                valid = false;
            }
        }

        return valid;
    }

    public static IntegerMatrix2D add(IntegerMatrix2D m, IntegerMatrix2D n) throws ArithmeticException {
        IntegerMatrix2D result = new IntegerMatrix2D(m.shape[0], m.shape[1]);

        if (!Arrays.equals(m.shape, n.shape)) {
            throw new ArithmeticException("Matrix shapes do not match");
        }


        for (int i = 0; i < m.shape[0]; i++) {
            for (int j = 0; j < m.shape[1]; j++) {
                result.data[i][j] = m.data[i][j] + n.data[i][j];
            }
        }

        return result;
    }

    public static IntegerMatrix2D mul(IntegerMatrix2D m, int k) {
        IntegerMatrix2D result = new IntegerMatrix2D(m.shape[0], m.shape[1]);

        for (int i = 0; i < m.shape[0]; i++) {
            for (int j = 0; j < m.shape[1]; j++) {
                result.data[i][j] = m.data[i][j] * k;
            }
        }

        return result;
    }

    public IntegerMatrix2D mul(int k) {
        return mul(this, k);
    }

    public static IntegerMatrix2D sub(IntegerMatrix2D m, IntegerMatrix2D n) {
        return add(m, n.mul(-1));
    }

    public IntegerMatrix2D sub(IntegerMatrix2D mat) {
        return sub(this, mat);
    }

    public IntegerMatrix2D add(IntegerMatrix2D mat) {
        return add(this, mat);
    }

    private static int[][] generateZeros(int i, int j) {
        int[][] zeroArray = new int[i][j];

        for (int k = 0; k < i; k++) {
            for (int l = 0; l < j; l++) {
                zeroArray[k][l] = 0;
            }
        }

        return zeroArray;
    }

    public static IntegerMatrix2D zeros(int i, int j) {
        int[][] zeroArray = generateZeros(i, j);

        return new IntegerMatrix2D(zeroArray);
    }

    public void print() {
        for (int[] row : this.data) {
            for (int element : row) {
                System.out.print(element + " ");
            }
            System.out.println();
        }
    }
}
