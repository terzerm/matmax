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
package org.tools4j.matmax.indexed;

import org.tools4j.matmax.function.Operand;

import java.util.function.*;

@FunctionalInterface
public interface Double2D extends Primitive2D<Double, Double2D>, Operand.DoubleOp<Double2D> {
    double valueAsDouble(int row, int column);

    @Override
    default Double value(final int row, final int column) {
        return valueAsDouble(row, column);
    }

    @Override
    default Double1D row(final int row) {
        return col -> valueAsDouble(row, col);
    }

    @Override
    default Double1D column(final int col) {
        return row -> valueAsDouble(row, col);
    }

    @Override
    default Double2D apply(final Function<? super Double2D, ? extends Double2D> operator) {
        return operator.apply(this);
    }

    @Override
    default Double2D applyToEach(final DoubleUnaryOperator operator) {
        return (row, column) -> operator.applyAsDouble(valueAsDouble(row, column));
    }

    @Override
    default BinaryOperable<Double2D, ? extends Double2D> with(final Double2D secondOperand) {
        return operator -> operator.apply(this, secondOperand);
    }

    default Bool2D toBool2D(final DoublePredicate function) {
        return (row, column) -> function.test(valueAsDouble(row, column));
    }

    default Int2D toInt2D(final DoubleToIntFunction function) {
        return (row, column) -> function.applyAsInt(valueAsDouble(row, column));
    }

    default Long2D toLong2D(final DoubleToLongFunction function) {
        return (row, column) -> function.applyAsLong(valueAsDouble(row, column));
    }

    @Override
    default Obj2D<Double> toObj2D() {
        return toObj2D(Double::valueOf);
    }

    default <T> Obj2D<T> toObj2D(final DoubleFunction<? extends T> function) {
        return (row, column) -> function.apply(valueAsDouble(row, column));
    }

    @Override
    default Obj2D<String> toStr2D() {
        return (row, column) -> String.valueOf(valueAsDouble(row, column));
    }
}
