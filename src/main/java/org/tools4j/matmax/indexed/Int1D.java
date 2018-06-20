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
public interface Int1D extends Primitive1D<Integer, Int1D>, Operand.IntOp<Int1D> {
    Int1D ZERO = index -> 0;
    Int1D ONE = index -> 1;

    int valueAsInt(int index);

    @Override
    default Integer value(final int index) {
        return valueAsInt(index);
    }

    @Override
    default Int1D applyToEach(final IntUnaryOperator operator) {
        return index -> operator.applyAsInt(valueAsInt(index));
    }

    @Override
    default Int1D apply(final Function<? super Int1D, ? extends Int1D> operator) {
        return operator.apply(this);
    }

    @Override
    default BinaryOperable<Int1D, ? extends Int1D> with(final Int1D secondOperand) {
        return operator -> operator.apply(this, secondOperand);
    }

    default Bool1D toBool1D(final IntPredicate function) {
        return index -> function.test(valueAsInt(index));
    }

    default Long1D toLong1D() {
        return toLong1D(v -> (long)v);
    }

    default Long1D toLong1D(final IntToLongFunction function) {
        return index -> function.applyAsLong(valueAsInt(index));
    }

    default Double1D toDouble1D() {
        return toDouble1D(v -> (double)v);
    }

    default Double1D toDouble1D(final IntToDoubleFunction function) {
        return index -> function.applyAsDouble(valueAsInt(index));
    }
    
    @Override
    default Obj1D<Integer> toObj1D() {
        return toObj1D(Integer::valueOf);
    }

    default <T> Obj1D<T> toObj1D(final IntFunction<? extends T> function) {
        return index -> function.apply(valueAsInt(index));
    }

    @Override
    default Obj1D<String> toStr1D() {
        return index -> String.valueOf(valueAsInt(index));
    }

    static Int1D constant(final int value) {
        if (value == 0) return ZERO;
        if (value == 1) return ONE;
        return index -> value;
    }
}
