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

import org.tools4j.matmax.indexed.Long1D;
import org.tools4j.matmax.indexed.Long2D;
import org.tools4j.matmax.vector.LongVector;
import org.tools4j.matmax.vector.ObjVector;

import java.util.Objects;
import java.util.function.*;

public interface LongMatrix extends Matrix<Long, Long2D>, Long2D {

    HashFunction<LongMatrix> HASH_FUNCTION = (m, r, c) -> Long.hashCode(m.valueAsLong(r,c));
    ValueEquality<LongMatrix> VALUE_EQUALITY = (m1, m2, r, c) -> m1.valueAsLong(r,c) == m2.valueAsLong(r,c);

    default LongVector row(final int row) {
        final int cols = nColumns();
        return LongVector.create(cols, col -> valueAsLong(row, col));
    }

    default LongVector column(final int col) {
        final int rows = nColumns();
        return LongVector.create(rows, row -> valueAsLong(row, col));
    }

    @Override
    default ObjVector<? extends LongVector> rows() {
        return ObjVector.create(nRows(), this::row);
    }

    @Override
    default ObjVector<? extends LongVector> columns() {
        return ObjVector.create(nColumns(), this::column);
    }

    @Override
    default LongMatrix apply(final Function<? super Long2D, ? extends Long2D> operator) {
        return create(this, Long2D.super.apply(operator));
    }

    @Override
    default LongMatrix applyToEach(final LongUnaryOperator operator) {
        return LongMatrix.create(this, Long2D.super.applyToEach(operator));
    }

    @Override
    default BinaryOperable<Long2D, ? extends LongMatrix> with(final Long2D secondOperand) {
        return operator -> operator.apply(this, secondOperand);
    }

    @Override
    default BoolMatrix toBool2D(final LongPredicate function) {
        return BoolMatrix.create(nRows(), nColumns(), Long2D.super.toBool2D(function));
    }

    @Override
    default IntMatrix toInt2D(final LongToIntFunction function) {
        return IntMatrix.create(nRows(), nColumns(), Long2D.super.toInt2D(function));
    }

    @Override
    default DoubleMatrix toDouble2D(final LongToDoubleFunction function) {
        return DoubleMatrix.create(nRows(), nColumns(), Long2D.super.toDouble2D(function));
    }

    @Override
    default ObjMatrix<Long> toObj2D() {
        return ObjMatrix.create(this, Long2D.super.toObj2D());
    }

    @Override
    default <T> ObjMatrix<T> toObj2D(final LongFunction<? extends T> function) {
        return ObjMatrix.create(this, Long2D.super.toObj2D(function));
    }

    @Override
    default ObjMatrix<String> toStr2D() {
        return ObjMatrix.create(nRows(), nColumns(), Long2D.super.toStr2D());
    }

    static LongMatrix create(final long[][] values) {
        final int rows = values.length;
        final int cols = rows == 0 ? 0 : values[0].length;
        return create(rows, cols, (r,c) -> (r >= 0 & r < rows & c >= 0 & c < cols) && c < values[r].length ?
                values[r][c] : 0L);
    }

    static LongMatrix createFromRows(final LongVector... rowData) {
        return createFromRows(rowData.length == 0 ? 0 : rowData[0].nElements(), rowData);
    }
    static LongMatrix createFromRows(final int cols, final Long1D... rowData) {
        final int rows = rowData.length;
        return create(rows, cols, (r,c) -> r >= 0 & r < rows & c >= 0 & c < cols ? rowData[r].valueAsLong(c) : 0L);
    }

    static LongMatrix createFromColumns(final LongVector... columnData) {
        return createFromColumns(columnData.length == 0 ? 0 : columnData[0].nElements(), columnData);
    }
    static LongMatrix createFromColumns(final int rows, final Long1D... columnData) {
        final int cols = columnData.length;
        return create(rows, cols, (r,c) -> r >= 0 & r < rows & c >= 0 & c < cols ? columnData[c].valueAsLong(r) : 0L);
    }

    static LongMatrix create(final Matrix<?, ?> meta, Long2D data) {
        return create(meta.nRows(), meta.nColumns(), data);
    }

    static LongMatrix create(final int rows, final int cols, final Long2D data) {
        if (rows < 0) throw new IllegalArgumentException("rows must not be negative: " + rows);
        if (cols < 0) throw new IllegalArgumentException("cols must not be negative: " + cols);
        Objects.requireNonNull(data);
        return new LongMatrix() {
            @Override
            public int nRows() {
                return rows;
            }

            @Override
            public int nColumns() {
                return cols;
            }

            @Override
            public long valueAsLong(final int row, final int column) {
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
                if (obj instanceof LongMatrix) {
                    return Matrix.equals(this, (LongMatrix)obj, VALUE_EQUALITY);
                }
                return false;
            }

            @Override
            public String toString() {
                return "LongMatrix:" + nRows() + "x" + nColumns();
            }
        };
    }

    static LongMatrix identity(final int n) {
        return diagonal(n, 1L);
    }

    static LongMatrix diagonal(final int n, final long value) {
        return diagonal(n, n, value);
    }

    static LongMatrix diagonal(final int nRows, final int nColumns, final long value) {
        return create(nRows, nColumns, (row, column) ->
                row >= 0 & row < nRows & column >= 0 & column < nColumns & row == column ? value : 0L);
    }

    static LongMatrix diagonal(final long... values) {
        final int n = values.length;
        return create(n, n, (row, column) -> row >= 0 & row < n & column >= 0 & column < n & row == column ?
                values[row] : 0L);
    }

    static LongMatrix diagonal(final LongVector values) {
        return diagonal(values.nElements(), values);
    }

    static LongMatrix diagonal(final int n, final Long1D values) {
        return create(n, n, (row, column) -> row >= 0 & row < n & column >= 0 & column < n & row == column ?
                values.valueAsLong(row) : 0L);
    }

    static LongMatrix constant(final int nRows, final int nColumns, final long value) {
        if (value == 0L) {
            return create(nRows, nColumns, Long2D.ZERO);
        }
        return create(nRows, nColumns, (row, column) -> row >= 0 & row < nRows & column >= 0 & column <= nColumns ? value : 0L);
    }

}
