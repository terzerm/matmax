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

import org.tools4j.matmax.indexed.Int1D;
import org.tools4j.matmax.indexed.Int2D;
import org.tools4j.matmax.vector.IntVector;
import org.tools4j.matmax.vector.ObjVector;

import java.util.Objects;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.function.IntToDoubleFunction;
import java.util.function.IntToLongFunction;

public interface IntMatrix extends Matrix<Integer, Int2D>, Int2D {

    HashFunction<IntMatrix> HASH_FUNCTION = (m, r, c) -> Integer.hashCode(m.valueAsInt(r,c));
    ValueEquality<IntMatrix> VALUE_EQUALITY = (m1, m2, r, c) -> m1.valueAsInt(r,c) == m2.valueAsInt(r,c);

    @Override
    default IntVector row(final int row) {
        final int cols = nColumns();
        return IntVector.create(cols, col -> valueAsInt(row, col));
    }

    @Override
    default IntVector column(final int col) {
        final int rows = nColumns();
        return IntVector.create(rows, row -> valueAsInt(row, col));
    }

    @Override
    default ObjVector<? extends IntVector> rows() {
        return ObjVector.create(nRows(), this::row);
    }

    @Override
    default ObjVector<? extends IntVector> columns() {
        return ObjVector.create(nColumns(), this::column);
    }

    @Override
    default BoolMatrix toBool2D(final IntPredicate function) {
        return BoolMatrix.create(nRows(), nColumns(), Int2D.super.toBool2D(function));
    }

    @Override
    default LongMatrix toLong2D() {
        return LongMatrix.create(nRows(), nColumns(), Int2D.super.toLong2D());
    }

    @Override
    default LongMatrix toLong2D(final IntToLongFunction function) {
        return LongMatrix.create(nRows(), nColumns(), Int2D.super.toLong2D(function));
    }

    @Override
    default DoubleMatrix toDouble2D() {
        return DoubleMatrix.create(nRows(), nColumns(), Int2D.super.toDouble2D());
    }

    @Override
    default DoubleMatrix toDouble2D(final IntToDoubleFunction function) {
        return DoubleMatrix.create(nRows(), nColumns(), Int2D.super.toDouble2D(function));
    }

    @Override
    default ObjMatrix<Integer> toObj2D() {
        return ObjMatrix.create(nRows(), nColumns(), Int2D.super.toObj2D());
    }

    @Override
    default <T> ObjMatrix<T> toObj2D(final IntFunction<? extends T> function) {
        return ObjMatrix.create(nRows(), nColumns(), Int2D.super.toObj2D(function));
    }

    @Override
    default ObjMatrix<String> toStr2D() {
        return ObjMatrix.create(nRows(), nColumns(), Int2D.super.toStr2D());
    }

    static IntMatrix create(final int[][] values) {
        final int rows = values.length;
        final int cols = rows == 0 ? 0 : values[0].length;
        return create(rows, cols, (r,c) -> (r >= 0 & r < rows & c >= 0 & c < cols) && c < values[r].length ?
                values[r][c] : 0);
    }

    static IntMatrix createFromRows(final int cols, final Int1D... rowData) {
        final int rows = rowData.length;
        return create(rows, cols, (r,c) -> r >= 0 & r < rows & c >= 0 & c < cols ?
                rowData[r].valueAsInt(c) : 0);
    }

    static IntMatrix createFromColumns(final int rows, final Int1D... columnData) {
        final int cols = columnData.length;
        return create(rows, cols, (r,c) -> r >= 0 & r < rows & c >= 0 & c < cols ?
                columnData[c].valueAsInt(r) : 0);
    }

    static IntMatrix create(final int rows, final int cols, final Int2D data) {
        if (rows < 0) throw new IllegalArgumentException("rows must not be negative: " + rows);
        if (cols < 0) throw new IllegalArgumentException("cols must not be negative: " + cols);
        Objects.requireNonNull(data);
        return new IntMatrix() {
            @Override
            public int nRows() {
                return rows;
            }

            @Override
            public int nColumns() {
                return cols;
            }

            @Override
            public int valueAsInt(final int row, final int column) {
                return data.value(row, column);
            }

            @Override
            public int hashCode() {
                return Matrix.hashCode(this, HASH_FUNCTION);
            }

            @Override
            public boolean equals(final Object obj) {
                if (this == obj) return true;
                if (obj == null) return false;
                if (obj instanceof IntMatrix) {
                    return Matrix.equals(this, (IntMatrix)obj, VALUE_EQUALITY);
                }
                return false;
            }

            @Override
            public String toString() {
                return "IntMatrix:" + nRows() + "x" + nColumns();
            }
        };
    }

    static IntMatrix identity(final int n) {
        return diagonal(n, 1);
    }

    static IntMatrix diagonal(final int n, final int value) {
        return diagonal(n, n, value);
    }

    static IntMatrix diagonal(final int nRows, final int nColumns, final int value) {
        return create(nRows, nColumns, (row, column) ->
                row >= 0 & row < nRows & column >= 0 & column < nColumns & row == column ? value : 0);
    }

    static IntMatrix diagonal(final int... values) {
        final int n = values.length;
        return create(n, n, (row, column) -> row >= 0 & row < n & column >= 0 & column < n & row == column ?
                values[row] : 0);
    }

    static IntMatrix diagonal(final IntVector values) {
        return diagonal(values.nElements(), values);
    }

    static IntMatrix diagonal(final int n, final Int1D values) {
        return create(n, n, (row, column) -> row >= 0 & row < n & column >= 0 & column < n & row == column ?
                values.valueAsInt(row) : 0);
    }

    static IntMatrix constant(final int nRows, final int nColumns, final int value) {
        if (value == 0) {
            return create(nRows, nColumns, Int2D.ZERO);
        }
        return create(nRows, nColumns, (row, column) -> row >= 0 & row < nRows & column >= 0 & column <= nColumns ? value : 0);
    }
}
