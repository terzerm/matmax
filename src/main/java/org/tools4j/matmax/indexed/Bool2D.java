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
public interface Bool2D extends Primitive2D<Boolean, Bool2D>, Operand.BoolOp<Bool2D> {
    boolean valueAsBoolean(int row, int column);

    @Override
    default Boolean value(final int row, final int column) {
        return valueAsBoolean(row, column);
    }

    @Override
    default Bool1D row(final int row) {
        return column -> valueAsBoolean(row, column);
    }

    @Override
    default Bool1D column(final int col) {
        return row -> valueAsBoolean(row, col);
    }

    @Override
    default Bool2D negate() {
        return (row, column) -> !valueAsBoolean(row, column);
    }

    @Override
    default Bool2D or(final Bool2D other) {
        return (row, column) -> valueAsBoolean(row, column) || other.valueAsBoolean(row, column);
    }

    @Override
    default Bool2D and(final Bool2D other) {
        return (row, column) -> valueAsBoolean(row, column) && other.valueAsBoolean(row, column);
    }

    @Override
    default Bool2D xor(final Bool2D other) {
        return (row, column) -> valueAsBoolean(row, column) ^ other.valueAsBoolean(row, column);
    }

    @Override
    default Bool2D andNot(final Bool2D other) {
        return (row, column) -> valueAsBoolean(row, column) && !other.valueAsBoolean(row, column);
    }

    @Override
    default Bool2D apply(final Function<? super Bool2D, ? extends Bool2D> operator) {
        return operator.apply(this);
    }

    @Override
    default BinaryOperable<Bool2D, ? extends Bool2D> with(final Bool2D secondOperand) {
        return operator -> operator.apply(this, secondOperand);
    }

    default Int2D toInt2D(final int trueValue, final int falseValue) {
        return (row, column) -> valueAsBoolean(row, column) ? trueValue : falseValue;
    }

    default Long2D toLong2D(final long trueValue, final long falseValue) {
        return (row, column) -> valueAsBoolean(row, column) ? trueValue : falseValue;
    }

    default Double2D toDouble2D(final double trueValue, final double falseValue) {
        return (row, column) -> valueAsBoolean(row, column) ? trueValue : falseValue;
    }

    @Override
    default Obj2D<Boolean> toObj2D() {
        return toObj2D(Boolean.TRUE, Boolean.FALSE);
    }

    default <T> Obj2D<T> toObj2D(final T trueValue, final T falseValue) {
        return (row, column) -> valueAsBoolean(row, column) ? trueValue : falseValue;
    }

    @Override
    default Obj2D<String> toStr2D() {
        return (row, column) -> String.valueOf(valueAsBoolean(row, column));
    }
}
