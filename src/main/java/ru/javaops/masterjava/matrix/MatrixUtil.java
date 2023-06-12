package ru.javaops.masterjava.matrix;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

public class MatrixUtil {

    public static int[][] concurrentMultiply(int[][] matrixA, int[][] matrixB, ExecutorService executor) throws InterruptedException, ExecutionException {
        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];

        CountDownLatch latch = new CountDownLatch(matrixSize);
        for (int col = 0; col < matrixSize; col++) {
            final int fCol = col;
            executor.submit(() -> {
                int[] cashB = new int[matrixSize];
                for (int k = 0; k < matrixSize; k++) {
                    cashB[k] = matrixB[k][fCol];
                }
                int[] cashA;
                for (int row = 0; row < matrixSize; row++) {
                    cashA = matrixA[row];
                    int sum = 0;
                    for (int k = 0; k < matrixSize; k++) {
                        sum += cashA[k] * cashB[k];
                    }
                    matrixC[row][fCol] = sum;
                }
                latch.countDown();
            });
        }
        latch.await();

        return matrixC;
    }

    public static int[][] singleThreadMultiply(int[][] matrixA, int[][] matrixB) {
        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];

        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                int sum = 0;
                for (int k = 0; k < matrixSize; k++) {
                    sum += matrixA[i][k] * matrixB[k][j];
                }
                matrixC[i][j] = sum;
            }
        }
        return matrixC;
    }

    // optimized by https://habrahabr.ru/post/114797/
    public static int[][] singleThreadMultiplyQuick(int[][] matrixA, int[][] matrixB) {
        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];

        int[][] transB = new int[matrixSize][matrixSize];
        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                transB[j][i] = matrixB[i][j];
            }
        }

        int[] cashA;
        int[] cashB;
        int[] cashC;
        for (int i = 0; i < matrixSize; i++) {
            cashA = matrixA[i];
            cashC = matrixC[i];
            for (int j = 0; j < matrixSize; j++) {
                int sum = 0;
                cashB = transB[j];
                for (int k = 0; k < matrixSize; k++) {
                    sum += cashA[k] * cashB[k];
                }
                cashC[j] = sum;
            }
        }
        return matrixC;
    }

    public static int[][] singleThreadMultiplyQuick2(int[][] matrixA, int[][] matrixB) {
        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];

        int[] cashA;
        int[] cashB = new int[matrixSize];
        for (int col = 0; col < matrixSize; col++) {
            for (int k = 0; k < matrixSize; k++) {
                cashB[k] = matrixB[k][col];
            }
            for (int row = 0; row < matrixSize; row++) {
                cashA = matrixA[row];
                int sum = 0;
                for (int k = 0; k < matrixSize; k++) {
                    sum += cashA[k] * cashB[k];
                }
                matrixC[row][col] = sum;
            }
        }
        return matrixC;
    }

    public static int[][] create(int size) {
        int[][] matrix = new int[size][size];
        Random rn = new Random();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][j] = rn.nextInt(10);
            }
        }
        return matrix;
    }

    public static boolean compare(int[][] matrixA, int[][] matrixB) {
        final int matrixSize = matrixA.length;
        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                if (matrixA[i][j] != matrixB[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }
}
