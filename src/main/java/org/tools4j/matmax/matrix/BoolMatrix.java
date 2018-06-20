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

import org.tools4j.matmax.indexed.Bool1D;
import org.tools4j.matmax.indexed.Bool2D;
import org.tools4j.matmax.vector.BoolVector;
import org.tools4j.matmax.vector.ObjVector;

import java.util.Objects;
import java.util.function.Function;

public interface BoolMatrix extends Matrix<Boolean, Bool2D>, Bool2D {

    HashFunction<BoolMatrix> HASH_FUNCTION = (m, r, c) -> Boolean.hashCode(m.valueAsBoolean(r,c));
    ValueEquality<BoolMatrix> VALUE_EQUALITY = (m1, m2, r, c) -> m1.valueAsBoolean(r,c) == m2.valueAsBoolean(r,c);

    default BoolVector row(final int row) {
        final int cols = nColumns();
        return BoolVector.create(cols, col -> valueAsBoolean(row, col));
    }

    default BoolVector column(final int col) {
        final int rows = nColumns();
        return BoolVector.create(rows, row -> valueAsBoolean(row, col));
    }

    @Override
    default ObjVector<? extends BoolVector> rows() {
        return ObjVector.create(nRows(), this::row);
    }

    @Override
    default ObjVector<? extends BoolVector> columns() {
        return ObjVector.create(nColumns(), this::column);
    }

    @Override
    default BoolMatrix negate() {
        return BoolMatrix.create(nRows(), nColumns(), Bool2D.super.negate());
    }

    @Override
    default BoolMatrix or(final Bool2D other) {
        return BoolMatrix.create(nRows(), nColumns(), Bool2D.super.or(other));
    }

    @Override
    default BoolMatrix and(final Bool2D other) {
        return BoolMatrix.create(nRows(), nColumns(), Bool2D.super.and(other));
    }

    @Override
    default BoolMatrix xor(final Bool2D other) {
        return BoolMatrix.create(nRows(), nColumns(), Bool2D.super.xor(other));
    }

    @Override
    default BoolMatrix andNot(final Bool2D other) {
        return BoolMatrix.create(nRows(), nColumns(), Bool2D.super.andNot(other));
    }

    @Override
    default BoolMatrix apply(final Function<? super Bool2D, ? extends Bool2D> operator) {
        return BoolMatrix.create(nRows(), nColumns(), Bool2D.super.apply(operator));
    }

    @Override
    default BinaryOperable<Bool2D, ? extends BoolMatrix> with(final Bool2D secondOperand) {
        return operator -> operator.apply(this, secondOperand);
    }

    default IntMatrix toInt2D(final int trueValue, final int falseValue) {
        return IntMatrix.create(nRows(), nColumns(), Bool2D.super.toInt2D(trueValue, falseValue));
    }

    default LongMatrix toLong2D(final long trueValue, final long falseValue) {
        return LongMatrix.create(nRows(), nColumns(), Bool2D.super.toLong2D(trueValue, falseValue));
    }

    default DoubleMatrix toDouble2D(final double trueValue, final double falseValue) {
        return DoubleMatrix.create(nRows(), nColumns(), Bool2D.super.toDouble2D(trueValue, falseValue));
    }

    @Override
    default ObjMatrix<Boolean> toObj2D() {
        return ObjMatrix.create(nRows(), nColumns(), Bool2D.super.toObj2D());
    }

    default <T> ObjMatrix<T> toObj2D(final T trueValue, final T falseValue) {
        return ObjMatrix.create(nRows(), nColumns(), Bool2D.super.toObj2D(trueValue, falseValue));
    }

    @Override
    default ObjMatrix<String> toStr2D() {
        return ObjMatrix.create(nRows(), nColumns(), Bool2D.super.toStr2D());
    }

    static BoolMatrix create(final boolean[][] values) {
        final int rows = values.length;
        final int cols = rows == 0 ? 0 : values[0].length;
        return create(rows, cols, (r,c) -> (r >= 0 & r < rows & c >= 0 & c < cols)
                && c < values[r].length && values[r][c]);
    }

    static BoolMatrix createFromRows(final BoolVector... rowData) {
        return createFromRows(rowData.length == 0 ? 0 : rowData[0].nElements(), rowData);
    }
    static BoolMatrix createFromRows(final int cols, final Bool1D... rowData) {
        final int rows = rowData.length;
        return create(rows, cols, (r,c) -> (r >= 0 & r < rows & c >= 0 & c < cols) && rowData[r].valueAsBoolean(c));
    }

    static BoolMatrix createFromColumns(final BoolVector... columnData) {
        return createFromColumns(columnData.length == 0 ? 0 : columnData[0].nElements(), columnData);
    }
    static BoolMatrix createFromColumns(final int rows, final Bool1D... columnData) {
        final int cols = columnData.length;
        return create(rows, cols, (r,c) -> (r >= 0 & r < rows & c >= 0 & c < cols) && columnData[c].valueAsBoolean(r));
    }

    static BoolMatrix create(final Matrix<?, ?> meta, Bool2D data) {
        return create(meta.nRows(), meta.nColumns(), data);
    }

    static BoolMatrix create(final int rows, final int cols, final Bool2D data) {
        if (rows < 0) throw new IllegalArgumentException("rows must not be negative: " + rows);
        if (cols < 0) throw new IllegalArgumentException("cols must not be negative: " + cols);
        Objects.requireNonNull(data);
        return new BoolMatrix() {
            @Override
            public int nRows() {
                return rows;
            }

            @Override
            public int nColumns() {
                return cols;
            }

            @Override
            public boolean valueAsBoolean(final int row, final int column) {
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
                if (obj instanceof BoolMatrix) {
                    return Matrix.equals(this, (BoolMatrix)obj, VALUE_EQUALITY);
                }
                return false;
            }

            @Override
            public String toString() {
                return "BoolMatrix:" + nRows() + "x" + nColumns();
            }
        };
    }

    static BoolMatrix identity(final int n) {
        return diagonal(n, n);
    }

    static BoolMatrix diagonal(final int nRows, final int nColumns) {
        return create(nRows, nColumns, (row, column) -> row >= 0 & row < nRows & column >= 0 & column < nColumns & row == column);
    }

    static BoolMatrix constant(final int nRows, final int nColumns, final boolean value) {
        if (!value) {
            return create(nRows, nColumns, Bool2D.FALSE);
        }
        return create(nRows, nColumns, (row, column) -> row >= 0 & row < nRows & column >= 0 & column <= nColumns);
    }

}
