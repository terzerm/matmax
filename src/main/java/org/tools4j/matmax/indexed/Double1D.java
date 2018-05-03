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
public interface Double1D extends Primitive1D<Double, Double1D>, Operand.DoubleOp<Double1D> {
    double valueAsDouble(int index);

    @Override
    default Double value(final int index) {
        return valueAsDouble(index);
    }

    @Override
    default Double1D apply(final Function<? super Double1D, ? extends Double1D> operator) {
        return operator.apply(this);
    }

    @Override
    default Double1D applyToEach(final DoubleUnaryOperator operator) {
        return index -> operator.applyAsDouble(valueAsDouble(index));
    }

    @Override
    default BinaryOperable<Double1D, ? extends Double1D> with(final Double1D secondOperand) {
        return operator -> operator.apply(this, secondOperand);
    }

    default Bool1D toBool1D(final DoublePredicate function) {
        return index -> function.test(valueAsDouble(index));
    }

    default Int1D toInt1D(final DoubleToIntFunction function) {
        return index -> function.applyAsInt(valueAsDouble(index));
    }

    default Long1D toLong1D(final DoubleToLongFunction function) {
        return index -> function.applyAsLong(valueAsDouble(index));
    }

    @Override
    default Obj1D<Double> toObj1D() {
        return toObj1D(Double::valueOf);
    }

    default <T> Obj1D<T> toObj1D(final DoubleFunction<? extends T> function) {
        return index -> function.apply(valueAsDouble(index));
    }

}
