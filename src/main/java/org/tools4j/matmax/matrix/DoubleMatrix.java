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

import org.tools4j.matmax.indexed.*;

import java.util.function.*;

public interface DoubleMatrix extends Matrix<Double, Double2D>, Double2D {

    HashFunction<DoubleMatrix> HASH_FUNCTION = (m,r,c) -> Double.hashCode(m.valueAsDouble(r,c));
    ValueEquality<DoubleMatrix> VALUE_EQUALITY = (m1,m2,r,c) -> Double.compare(
            m1.valueAsDouble(r,c), m2.valueAsDouble(r,c)) == 0;//NOTE: Double.compare handles NaN values

    @Override//FIXME return Vector1D
    default Double1D row(final int row) {
        return col -> valueAsDouble(row, col);
    }

    @Override//FIXME return Vector1D
    default Double1D column(final int col) {
        return row -> valueAsDouble(row, col);
    }

    @Override
    default DoubleMatrix apply(final Function<? super Double2D, ? extends Double2D> operator) {
        return create(this, Double2D.super.apply(operator));
    }

    @Override
    default DoubleMatrix applyToEach(final DoubleUnaryOperator operator) {
        return DoubleMatrix.create(this, (row, column) -> operator.applyAsDouble(valueAsDouble(row, column)));
    }

    @Override
    default BinaryOperable<Double2D, ? extends DoubleMatrix> with(final Double2D secondOperand) {
        return operator -> operator.apply(this, secondOperand);
    }

    @Override//FIXME return BoolMatrix
    default Bool2D toBool2D(final DoublePredicate function) {
        return (row, column) -> function.test(valueAsDouble(row, column));
    }

    @Override//FIXME return IntMatrix
    default Int2D toInt2D(final DoubleToIntFunction function) {
        return (row, column) -> function.applyAsInt(valueAsDouble(row, column));
    }

    @Override//FIXME return LongMatrix
    default Long2D toLong2D(final DoubleToLongFunction function) {
        return (row, column) -> function.applyAsLong(valueAsDouble(row, column));
    }

    @Override
    default ObjMatrix<Double> toObj2D() {
        return toObj2D(Double::valueOf);
    }

    @Override
    default <T> ObjMatrix<T> toObj2D(final DoubleFunction<? extends T> function) {
        return ObjMatrix.create(this, (row, column) -> function.apply(valueAsDouble(row, column)));
    }

    static DoubleMatrix create(final double[][] values) {
        final int rows = values.length;
        final int cols = rows == 0 ? 0 : values[0].length;
        return create(rows, cols, (r,c) ->
                r >= 0 & r < rows & c >= 0 & c < cols & c < values[r].length ? values[r][c] : Double.NaN);
    }

    static DoubleMatrix createFromRows(final int cols, final Double1D... rowData) {
        final int rows = rowData.length;
        return create(rows, cols, (r,c) ->
                r >= 0 & r < rows & c >= 0 & c < cols ? rowData[r].valueAsDouble(c) : Double.NaN);
    }

    static DoubleMatrix createFromColumns(final int rows, final Double1D... columnData) {
        final int cols = columnData.length;
        return create(rows, cols, (r,c) ->
                r >= 0 & r < rows & c >= 0 & c < cols ? columnData[c].valueAsDouble(r) : Double.NaN);
    }

    static DoubleMatrix create(final Matrix<?,?> meta, Double2D data) {
        return create(meta.nRows(), meta.nColumns(), data);
    }

    static DoubleMatrix create(final int rows, final int cols, final Double2D data) {
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
}
