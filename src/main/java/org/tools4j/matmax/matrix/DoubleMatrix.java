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

import org.tools4j.matmax.indexed.Double1D;
import org.tools4j.matmax.indexed.Double2D;
import org.tools4j.matmax.vector.DoubleVector;
import org.tools4j.matmax.vector.ObjVector;

import java.util.Objects;
import java.util.function.*;

public interface DoubleMatrix extends Matrix<Double, Double2D>, Double2D {

    HashFunction<DoubleMatrix> HASH_FUNCTION = (m,r,c) -> Double.hashCode(m.valueAsDouble(r,c));
    ValueEquality<DoubleMatrix> VALUE_EQUALITY = (m1,m2,r,c) -> Double.compare(
            m1.valueAsDouble(r,c), m2.valueAsDouble(r,c)) == 0;//NOTE: Double.compare handles NaN values

    default DoubleVector row(final int row) {
        final int cols = nColumns();
        return DoubleVector.create(cols, col -> valueAsDouble(row, col));
    }

    default DoubleVector column(final int col) {
        final int rows = nColumns();
        return DoubleVector.create(rows, row -> valueAsDouble(row, col));
    }

    @Override
    default ObjVector<? extends DoubleVector> rows() {
        return ObjVector.create(nRows(), this::row);
    }

    @Override
    default ObjVector<? extends DoubleVector> columns() {
        return ObjVector.create(nColumns(), this::column);
    }

    @Override
    default DoubleMatrix apply(final Function<? super Double2D, ? extends Double2D> operator) {
        return create(this, Double2D.super.apply(operator));
    }

    @Override
    default DoubleMatrix applyToEach(final DoubleUnaryOperator operator) {
        return DoubleMatrix.create(this, Double2D.super.applyToEach(operator));
    }

    @Override
    default BinaryOperable<Double2D, ? extends DoubleMatrix> with(final Double2D secondOperand) {
        return operator -> operator.apply(this, secondOperand);
    }

    @Override
    default BoolMatrix toBool2D(final DoublePredicate function) {
        return BoolMatrix.create(nRows(), nColumns(), Double2D.super.toBool2D(function));
    }

    @Override
    default IntMatrix toInt2D(final DoubleToIntFunction function) {
        return IntMatrix.create(nRows(), nColumns(), Double2D.super.toInt2D(function));
    }

    @Override
    default LongMatrix toLong2D(final DoubleToLongFunction function) {
        return LongMatrix.create(nRows(), nColumns(), Double2D.super.toLong2D(function));
    }

    @Override
    default ObjMatrix<Double> toObj2D() {
        return ObjMatrix.create(this, Double2D.super.toObj2D());
    }

    @Override
    default <T> ObjMatrix<T> toObj2D(final DoubleFunction<? extends T> function) {
        return ObjMatrix.create(this, Double2D.super.toObj2D(function));
    }

    @Override
    default ObjMatrix<String> toStr2D() {
        return ObjMatrix.create(nRows(), nColumns(), Double2D.super.toStr2D());
    }

    static DoubleMatrix create(final double[][] values) {
        final int rows = values.length;
        final int cols = rows == 0 ? 0 : values[0].length;
        return create(rows, cols, (r,c) -> (r >= 0 & r < rows & c >= 0 & c < cols) && c < values[r].length ?
                values[r][c] : Double.NaN);
    }

    static DoubleMatrix createFromRows(final DoubleVector... rowData) {
        return createFromRows(rowData.length == 0 ? 0 : rowData[0].nElements(), rowData);
    }
    static DoubleMatrix createFromRows(final int cols, final Double1D... rowData) {
        final int rows = rowData.length;
        return create(rows, cols, (r,c) -> r >= 0 & r < rows & c >= 0 & c < cols ?
                rowData[r].valueAsDouble(c) : Double.NaN);
    }

    static DoubleMatrix createFromColumns(final DoubleVector... columnData) {
        return createFromColumns(columnData.length == 0 ? 0 : columnData[0].nElements(), columnData);
    }
    static DoubleMatrix createFromColumns(final int rows, final Double1D... columnData) {
        final int cols = columnData.length;
        return create(rows, cols, (r,c) -> r >= 0 & r < rows & c >= 0 & c < cols ?
                columnData[c].valueAsDouble(r) : Double.NaN);
    }

    static DoubleMatrix create(final Matrix<?,?> meta, Double2D data) {
        return create(meta.nRows(), meta.nColumns(), data);
    }

    static DoubleMatrix create(final int rows, final int cols, final Double2D data) {
        if (rows < 0) throw new IllegalArgumentException("rows must not be negative: " + rows);
        if (cols < 0) throw new IllegalArgumentException("cols must not be negative: " + cols);
        Objects.requireNonNull(data);
        return new DoubleMatrix() {
            @Override
            public int nRows() {
                return rows;
            }

            @Override
            public int nColumns() {
                return cols;
            }

            @Override
            public double valueAsDouble(final int row, final int column) {
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
                if (obj instanceof DoubleMatrix) {
                    return Matrix.equals(this, (DoubleMatrix)obj, VALUE_EQUALITY);
                }
                return false;
            }

            @Override
            public String toString() {
                return "DoubleMatrix:" + nRows() + "x" + nColumns();
            }
        };
    }

    static DoubleMatrix identity(final int n) {
        return diagonal(n, 1d);
    }

    static DoubleMatrix diagonal(final int n, final double value) {
        return diagonal(n, n, value);
    }

    static DoubleMatrix diagonal(final int nRows, final int nColumns, final double value) {
        return create(nRows, nColumns, (row, column) -> row >= 0 & row < nRows & column >= 0 & column < nColumns ?
                (row == column ? value : 0d) : Double.NaN);
    }

    static DoubleMatrix diagonal(final double... values) {
        final int n = values.length;
        return create(n, n, (row, column) -> row >= 0 & row < n & column >= 0 & column < n ?
                (row == column ? values[row] : 0d) : Double.NaN);
    }

    static DoubleMatrix constant(final int nRows, final int nColumns, final double value) {
        if (Double.isNaN(value)) {
            return create(nRows, nColumns, Double2D.NAN);
        }
        return create(nRows, nColumns, (row, column) -> row >= 0 & row < nRows & column >= 0 & column <= nColumns ? value : Double.NaN);
    }

}
