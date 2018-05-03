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
public interface Obj2D<V> extends Indexed2D<V, Obj2D<V>>, Operand.ObjOp<V, Obj2D<V>> {
    @Override
    V value(int row, int column);

    @Override
    default Obj1D<V> row(final int row) {
        return col -> value(row, col);
    }

    @Override
    default Obj1D<V> column(final int col) {
        return row -> value(row, col);
    }

    @Override
    default Obj2D<V> apply(final Function<? super Obj2D<V>, ? extends Obj2D<V>> operator) {
        return operator.apply(this);
    }

    @Override
    default Obj2D<V> applyToEach(final Function<? super V, ? extends V> operator) {
        return (row, column) -> operator.apply(value(row, column));
    }

    @Override
    default BinaryOperable<Obj2D<V>, ? extends Obj2D<V>> with(final Obj2D<V> secondOperand) {
        return operator -> operator.apply(this, secondOperand);
    }

    default Bool2D toBool2D(final Predicate<? super V> function) {
        return (row, column) -> function.test(value(row, column));
    }

    default Int2D toInt2D(final ToIntFunction<? super V> function) {
        return (row, column) -> function.applyAsInt(value(row, column));
    }

    default Long2D toLong2D(final ToLongFunction<? super V> function) {
        return (row, column) -> function.applyAsLong(value(row, column));
    }

    default Double2D toDouble2D(final ToDoubleFunction<? super V> function) {
        return (row, column) -> function.applyAsDouble(value(row, column));
    }
}
