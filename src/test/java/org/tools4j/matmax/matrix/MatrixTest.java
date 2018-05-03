/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2018 matmax (tools4j.org) Marco Terzer
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.tools4j.matmax.matrix;

import org.junit.AfterClass;
import org.junit.Test;
import org.tools4j.matmax.indexed.Int2D;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Double.NaN;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class MatrixTest {

    private static final int N_ROWS = 10;
    private static final int N_COLS = 10;
    private static final AtomicInteger TEST_COUNT = new AtomicInteger();
    private static final Set<Integer> ALL_HASHES = new HashSet<>();

    @AfterClass
    public static void testHashQualityBetweenTests() {
        final long count = TEST_COUNT.get();
        assertNotEquals("at least one test should have been run", 0, count);
        final long expected = count * N_ROWS * N_COLS - (N_ROWS+N_COLS-1) * (count-1);//subtraction cause all zero row/col matrices have same hash
        assertEquals("all hashes should be different", expected, ALL_HASHES.size());
    }

    @Test
    public void hashZeroes() {
        hash(IntMatrix.create(new int[][] {
                {0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0},
        }));
    }
    @Test
    public void hashNaN() {
        hash(DoubleMatrix.create(new double[][] {
                {NaN,NaN,NaN,NaN,NaN,NaN,NaN,NaN,NaN,NaN},
                {NaN,NaN,NaN,NaN,NaN,NaN,NaN,NaN,NaN,NaN},
                {NaN,NaN,NaN,NaN,NaN,NaN,NaN,NaN,NaN,NaN},
                {NaN,NaN,NaN,NaN,NaN,NaN,NaN,NaN,NaN,NaN},
                {NaN,NaN,NaN,NaN,NaN,NaN,NaN,NaN,NaN,NaN},
                {NaN,NaN,NaN,NaN,NaN,NaN,NaN,NaN,NaN,NaN},
                {NaN,NaN,NaN,NaN,NaN,NaN,NaN,NaN,NaN,NaN},
                {NaN,NaN,NaN,NaN,NaN,NaN,NaN,NaN,NaN,NaN},
                {NaN,NaN,NaN,NaN,NaN,NaN,NaN,NaN,NaN,NaN},
                {NaN,NaN,NaN,NaN,NaN,NaN,NaN,NaN,NaN,NaN},
        }));
    }
    @Test
    public void hashSameValues() {
        hash(IntMatrix.create(new int[][] {
                {1,1,1,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,1,1,1},
        }));
    }
    @Test
    public void hashSameRows() {
        hash(IntMatrix.create(new int[][] {
                {11,12,13,14,15,16,17,18,19,20},
                {11,12,13,14,15,16,17,18,19,20},
                {11,12,13,14,15,16,17,18,19,20},
                {11,12,13,14,15,16,17,18,19,20},
                {11,12,13,14,15,16,17,18,19,20},
                {11,12,13,14,15,16,17,18,19,20},
                {11,12,13,14,15,16,17,18,19,20},
                {11,12,13,14,15,16,17,18,19,20},
                {11,12,13,14,15,16,17,18,19,20},
                {11,12,13,14,15,16,17,18,19,20},
        }));
    }
    @Test
    public void hashSameCols() {
        hash(IntMatrix.create(new int[][] {
                { 2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
                { 3, 3, 3, 3, 3, 3, 3, 3, 3, 3},
                { 4, 4, 4, 4, 4, 4, 4, 4, 4, 4},
                { 5, 5, 5, 5, 5, 5, 5, 5, 5, 5},
                { 6, 6, 6, 6, 6, 6, 6, 6, 6, 6},
                { 7, 7, 7, 7, 7, 7, 7, 7, 7, 7},
                { 8, 8, 8, 8, 8, 8, 8, 8, 8, 8},
                { 9, 9, 9, 9, 9, 9, 9, 9, 9, 9},
                {10,10,10,10,10,10,10,10,10,10},
                {11,11,11,11,11,11,11,11,11,11},
        }));
    }

    private void hash(final Matrix<?,?> matrix) {
        assertEquals("unexpected row count", N_ROWS, matrix.nRows());
        assertEquals("unexpected column count", N_COLS, matrix.nColumns());
        final Int2D hashFunction = matrix.toObj2D().toInt2D(Objects::hashCode);
        final Set<Integer> hashes = new HashSet<>();
        for (int r = 0; r < matrix.nRows(); r++) {
            for (int c = 0; c < matrix.nColumns(); c++) {
                final IntMatrix hashSubMatrix = IntMatrix.create(r, c, hashFunction);
                final int hash1 = Matrix.hashCode(hashSubMatrix, IntMatrix.HASH_FUNCTION);
                final int hash2 = hashSubMatrix.hashCode();
                assertEquals("both hashes should be the same", hash1, hash2);
                hashes.add(hash1);
                ALL_HASHES.add(hash1);
                //System.out.println(r + "x" + c + "=" + hash1);
            }
        }
        TEST_COUNT.incrementAndGet();
        assertEquals("all hashes should be different", matrix.nRows() * matrix.nColumns(), hashes.size());
    }

}