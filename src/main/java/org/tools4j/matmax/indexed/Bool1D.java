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

import java.util.function.Function;

@FunctionalInterface
public interface Bool1D extends Primitive1D<Boolean, Bool1D>, Operand.BoolOp<Bool1D> {
    Bool1D TRUE = index -> true;
    Bool1D FALSE = index -> false;

    boolean valueAsBoolean(int index);

    @Override
    default Boolean value(final int index) {
        return valueAsBoolean(index);
    }

    @Override
    default Bool1D negate() {
        return index -> !valueAsBoolean(index);
    }

    @Override
    default Bool1D or(final Bool1D other) {
        return (index) -> valueAsBoolean(index) || other.valueAsBoolean(index);
    }

    @Override
    default Bool1D and(final Bool1D other) {
        return (index) -> valueAsBoolean(index) && other.valueAsBoolean(index);
    }

    @Override
    default Bool1D xor(final Bool1D other) {
        return (index) -> valueAsBoolean(index) ^ other.valueAsBoolean(index);
    }

    @Override
    default Bool1D andNot(final Bool1D other) {
        return (index) -> valueAsBoolean(index) && !other.valueAsBoolean(index);
    }

    @Override
    default Bool1D apply(final Function<? super Bool1D, ? extends Bool1D> operator) {
        return operator.apply(this);
    }

    @Override
    default BinaryOperable<Bool1D, ? extends Bool1D> with(final Bool1D secondOperand) {
        return operator -> operator.apply(this, secondOperand);
    }

    default Int1D toInt1D(final int trueValue, final int falseValue) {
        return index -> valueAsBoolean(index) ? trueValue : falseValue;
    }

    default Long1D toLong1D(final long trueValue, final long falseValue) {
        return index -> valueAsBoolean(index) ? trueValue : falseValue;
    }

    default Double1D toDouble1D(final double trueValue, final double falseValue) {
        return index -> valueAsBoolean(index) ? trueValue : falseValue;
    }

    @Override
    default Obj1D<Boolean> toObj1D() {
        return toObj1D(Boolean.TRUE, Boolean.FALSE);
    }

    default <T> Obj1D<T> toObj1D(final T trueValue, final T falseValue) {
        return index -> valueAsBoolean(index) ? trueValue : falseValue;
    }

    @Override
    default Obj1D<String> toStr1D() {
        return index -> String.valueOf(valueAsBoolean(index));
    }

    static Bool1D constant(final boolean value) {
        return value ? TRUE : FALSE;
    }

}
