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
public interface Int2D extends Primitive2D<Integer, Int2D>, Operand.IntOp<Int2D> {
    Int2D ZERO = (row, column) -> 0;
    Int2D ONE = (row, column) -> 1;

    int valueAsInt(int row, int column);

    @Override
    default Integer value(final int row, final int column) {
        return valueAsInt(row, column);
    }

    @Override
    default Int1D row(final int row) {
        return col -> valueAsInt(row, col);
    }

    @Override
    default Int1D column(final int col) {
        return row -> valueAsInt(row, col);
    }

    @Override
    default Obj1D<? extends Int1D> rows() {
        return this::row;
    }

    @Override
    default Obj1D<? extends Int1D> columns() {
        return this::row;
    }

    @Override
    default Int2D apply(final Function<? super Int2D, ? extends Int2D> operator) {
        return operator.apply(this);
    }

    @Override
    default BinaryOperable<Int2D, ? extends Int2D> with(final Int2D secondOperand) {
        return operator -> operator.apply(this, secondOperand);
    }

    @Override
    default Int2D applyToEach(final IntUnaryOperator operator) {
        return (row, column) -> operator.applyAsInt(valueAsInt(row, column));
    }

    default Bool2D toBool2D(final IntPredicate function) {
        return (row, column) -> function.test(valueAsInt(row, column));
    }

    default Long2D toLong2D() {
        return toLong2D(v -> (long)v);
    }

    default Long2D toLong2D(final IntToLongFunction function) {
        return (row, column) -> function.applyAsLong(valueAsInt(row, column));
    }

    default Double2D toDouble2D() {
        return toDouble2D(v -> (double)v);
    }

    default Double2D toDouble2D(final IntToDoubleFunction function) {
        return (row, column) -> function.applyAsDouble(valueAsInt(row, column));
    }

    @Override
    default Obj2D<Integer> toObj2D() {
        return toObj2D(Integer::valueOf);
    }

    default <T> Obj2D<T> toObj2D(final IntFunction<? extends T> function) {
        return (row, column) -> function.apply(valueAsInt(row, column));
    }

    @Override
    default Obj2D<String> toStr2D() {
        return (row, column) -> String.valueOf(valueAsInt(row, column));
    }

    static Int2D constant(final int value) {
        if (value == 0) return ZERO;
        if (value == 1) return ONE;
        return (row, column) -> value;
    }
}
