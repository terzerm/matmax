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

import org.tools4j.matmax.indexed.Indexed2D;
import org.tools4j.matmax.vector.Vector;

public interface Matrix<V, T extends Indexed2D<V, T>> extends Indexed2D<V, T> {
    int nRows();
    int nColumns();

    @Override
    Vector<V, ?> row(int row);
    @Override
    Vector<V, ?> column(int col);

    interface HashFunction<M extends Matrix<?,?>> {
        int hashCode(M matrix, int row, int col);
    }

    interface ValueEquality<M extends Matrix<?,?>> {
        boolean equalValues(M matrix1, M matrix2, int row, int col);
    }

    static <M extends Matrix<?,?>> int hashCode(final M matrix, final HashFunction<? super M> hashFunction) {
        final int n = 10;
        final int rows = matrix.nRows();
        final int cols = matrix.nColumns();
        int hash = 31 * rows + cols;
        if (rows == 0 | cols == 0) return hash;
        if (rows * cols <= n) {
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    hash = (31 * hash) + hashFunction.hashCode(matrix, r, c);
                }
            }
        } else {
            int p = 2147483647;//largest int prime
            final int len = Math.min(rows * cols, n);
            for (int i = 0; i < len; i++) {
                final int r = p % rows;
                final int c = (p / rows) % cols;
                hash = (31 * hash) + hashFunction.hashCode(matrix, r, c);
                p *= 2147483647;
                p &= Integer.MAX_VALUE;//makes it positive, or zero for Integer.MIN_VALUE
            }
        }
        return hash;
    }

    static <M extends Matrix<?,?>> boolean equals(final M m1, final M m2, final ValueEquality<? super M> valueEquality) {
        if (m1 == m2) return true;
        if (m1 == null || m2 == null) return false;//cannot be both null due to first line
        final int rows = m1.nRows();
        if (rows != m2.nRows()) return false;
        final int cols = m1.nColumns();
        if (cols != m2.nColumns()) return false;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (!valueEquality.equalValues(m1, m2, r, c)) return false;
            }
        }
        return true;
    }
}
