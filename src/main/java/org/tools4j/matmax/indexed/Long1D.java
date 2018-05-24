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
public interface Long1D extends Primitive1D<Long, Long1D>, Operand.LongOp<Long1D> {
    long valueAsLong(int index);

    @Override
    default Long value(final int index) {
        return valueAsLong(index);
    }

    @Override
    default Long1D apply(final Function<? super Long1D, ? extends Long1D> operator) {
        return operator.apply(this);
    }

    @Override
    default Long1D applyToEach(final LongUnaryOperator operator) {
        return index -> operator.applyAsLong(valueAsLong(index));
    }

    @Override
    default BinaryOperable<Long1D, ? extends Long1D> with(final Long1D secondOperand) {
        return operator -> operator.apply(this, secondOperand);
    }

    default Bool1D toBool1D(final LongPredicate function) {
        return index -> function.test(valueAsLong(index));
    }

    default Int1D toInt1D(final LongToIntFunction function) {
        return index -> function.applyAsInt(valueAsLong(index));
    }

    default Double1D toDouble1D(final LongToDoubleFunction function) {
        return index -> function.applyAsDouble(valueAsLong(index));
    }

    @Override
    default Obj1D<Long> toObj1D() {
        return toObj1D(Long::valueOf);
    }

    default <T> Obj1D<T> toObj1D(final LongFunction<? extends T> function) {
        return index -> function.apply(valueAsLong(index));
    }

    @Override
    default Obj1D<String> toStr1D() {
        return index -> String.valueOf(valueAsLong(index));
    }
}
