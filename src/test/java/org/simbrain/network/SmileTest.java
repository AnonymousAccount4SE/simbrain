package org.simbrain.network;

import org.junit.Test;
import org.opencv.core.Mat;
import smile.math.matrix.Matrix;
import static org.junit.Assert.assertEquals;

/**
 * @return
 */
public class SmileTest {

    @Test
    public void basics() {

        var a = Matrix.eye(3);
        var b = Matrix.diag(new double[]{1, 4, 5});
        // System.out.println(a);
        var c = b.mm(b);
        //System.out.println(c);

        //Todo: Just put this through the paces.
        //  Try as much matrix stuff possible

        // Initial code to create a 2d matrix of floats


        // Create a 2x3 zero matrix

        var matrix_a = Matrix.eye(2,3);

        // Get its shape

        var rows = matrix_a.nrows();
        var cols = matrix_a.ncols();
        var result = "Shape: "+rows+" x "+cols;
        System.out.println("Shape: "+rows+" x "+cols);
        assertEquals("Shape: 2 x 3", result);

        // Create a 2x3 matrix of ones

        double[][] ones = {{1.0,1.0,1.0},{1.0,1.0,1.0}};
        var matrix_ones = new Matrix(2,3,ones);
        System.out.println(matrix_ones);


        // Confirm sum of entries is 6

        var sums = matrix_ones.sum();
        System.out.println("Sums: "+sums);
        assertEquals(6.0, sums,0.0);
        // Set the value of entry 1,3 to 3

        matrix_ones.set(0,2,3);

        // Retrieve the value of that entry and confirm it is 3

        System.out.println("Value at 1,3: "+matrix_ones.get(0,2));
        assertEquals(3,matrix_ones.get(0,2),0.0);

        // Perform a matrix multiplication using simple values and confirm correct outputs

        double[][] twos = {{2.0,2.0,2.0},{2.0,2.0,2.0}};
        var matrix_twos = new Matrix(2,3,twos);
        double[][] threes = {{3.0,3.0},{3.0,3.0},{3.0,3.0}};
        var matrix_threes = new Matrix(3,2,threes);
        var result_matrix = matrix_twos.mm(matrix_threes);
        System.out.println(result_matrix);

        //TODO: Perform a dot product and confirm correct outputs

    }
}
