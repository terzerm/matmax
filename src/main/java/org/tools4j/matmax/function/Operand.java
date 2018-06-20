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
package org.tools4j.matmax.function;

import java.util.function.*;

public interface Operand<T extends Operand<T>> {
    T apply(Function<? super T, ? extends T> operator);

    BinaryOperable<T,? extends T> with(T secondOperand);

    @FunctionalInterface
    interface BinaryOperable<T,R> extends Function<BiFunction<? super R, ? super T, ? extends T>, T> {
        //nothing to add
    }

    interface BoolOp<T extends BoolOp<T>> extends Operand<T> {
        T negate();
        T or(T other);
        T and(T other);
        T xor(T other);
        T andNot(T other);
    }

    interface IntOp<T extends IntOp<T>> extends Operand<T> {
        T applyToEach(IntUnaryOperator operator);
    }

    interface LongOp<T extends LongOp<T>> extends Operand<T> {
        T applyToEach(LongUnaryOperator operator);
    }

    interface DoubleOp<T extends DoubleOp<T>> extends Operand<T> {
        T applyToEach(DoubleUnaryOperator operator);
    }

    interface ObjOp<V, T extends ObjOp<V, T>> extends Operand<T> {
        T applyToEach(Function<? super V, ? extends V> operator);
    }
}
