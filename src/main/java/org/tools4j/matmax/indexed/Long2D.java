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
public interface Long2D extends Primitive2D<Long, Long2D>, Operand.LongOp<Long2D> {
    long valueAsLong(int row, int column);

    @Override
    default Long value(final int row, final int column) {
        return valueAsLong(row, column);
    }

    @Override
    default Long2D apply(final Function<? super Long2D, ? extends Long2D> operator) {
        return operator.apply(this);
    }

    @Override
    default Long2D applyToEach(final LongUnaryOperator operator) {
        return (row, column) -> operator.applyAsLong(valueAsLong(row, column));
    }

    @Override
    default Function<BinaryOperator<Long2D>, Long2D> with(final Long2D secondOperand) {
        return operator -> operator.apply(this, secondOperand);
    }

    default Bool2D toBool2D(final LongPredicate function) {
        return (row, column) -> function.test(valueAsLong(row, column));
    }

    default Int2D toInt2D(final LongToIntFunction function) {
        return (row, column) -> function.applyAsInt(valueAsLong(row, column));
    }

    default Double2D toDouble2D(final LongToDoubleFunction function) {
        return (row, column) -> function.applyAsDouble(valueAsLong(row, column));
    }

    @Override
    default Obj2D<Long> toObj2D() {
        return toObj2D(Long::valueOf);
    }

    default <T> Obj2D<T> toObj2D(final LongFunction<? extends T> function) {
        return (row, column) -> function.apply(valueAsLong(row, column));
    }
}
