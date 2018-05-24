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
public interface Obj1D<V> extends Indexed1D<V, Obj1D<V>>, Operand.ObjOp<V, Obj1D<V>> {
    @Override
    V value(int index);

    @Override
    default Obj1D<V> apply(final Function<? super Obj1D<V>, ? extends Obj1D<V>> operator) {
        return operator.apply(this);
    }

    @Override
    default BinaryOperable<Obj1D<V>, ? extends Obj1D<V>> with(final Obj1D<V> secondOperand) {
        return operator -> operator.apply(this, secondOperand);
    }

    @Override
    default Obj1D<V> applyToEach(final Function<? super V, ? extends V> operator) {
        return index -> operator.apply(value(index));
    }

    default Bool1D toBool1D(final Predicate<? super V> function) {
        return index -> function.test(value(index));
    }

    default Int1D toInt1D(final ToIntFunction<? super V> function) {
        return index -> function.applyAsInt(value(index));
    }

    default Long1D toLong1D(final ToLongFunction<? super V> function) {
        return index -> function.applyAsLong(value(index));
    }

    default Double1D toDouble1D(final ToDoubleFunction<? super V> function) {
        return index -> function.applyAsDouble(value(index));
    }
}
