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
package org.tools4j.matmax.vector;

import org.tools4j.matmax.indexed.Obj1D;
import org.tools4j.matmax.matrix.ObjMatrix;

import java.util.*;
import java.util.function.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface ObjVector<V> extends Vector<V, Obj1D<V>>, Obj1D<V> {

    HashFunction<ObjVector<?>> HASH_FUNCTION = (vec, ind) -> Objects.hashCode(vec.value(ind));
    ValueEquality<ObjVector<?>> VALUE_EQUALITY = (vec1, vec2, ind) -> Objects.equals(vec1.value(ind), vec2.value(ind));

    @Override
    default ObjVector<V> apply(final Function<? super Obj1D<V>, ? extends Obj1D<V>> operator) {
        return create(this, Obj1D.super.apply(operator));
    }

    @Override
    default BinaryOperable<Obj1D<V>, ? extends ObjVector<V>> with(final Obj1D<V> secondOperand) {
        return operator -> operator.apply(this, secondOperand);
    }

    @Override
    default ObjMatrix<V> toRow() {
        return ObjMatrix.create(1, nElements(), (row, column) -> row >= 0 & row < 1 ? value(column) : null);
    }

    @Override
    default ObjMatrix<V> toColumn() {
        return ObjMatrix.create(nElements(), 1, (row, column) -> column >= 0 & column < 1 ? value(row) : null);
    }

    default int indexOf(final V value, final int start, final BiPredicate<? super V, ? super V> matcher) {
        final int n = nElements();
        for (int i = start; i < n; i++) {
            if (matcher.test(value, value(i))) {
                return i;
            }
        }
        return -1;
    }

    default int indexMatching(final Predicate<? super V> predicate) {
        final int n = nElements();
        for (int i = 0; i < n; i++) {
            if (predicate.test(value(i))) {
                return i;
            }
        }
        return -1;
    }

    @Override
    default Iterator<V> iterator() {
        return new Iterator<V>() {
            private int next = 0;
            @Override
            public boolean hasNext() {
                return next < nElements();
            }

            @Override
            public V next() {
                final int nextAfter = next + 1;
                if (nextAfter > nElements()) {
                    throw new NoSuchElementException();
                }
                final int current = nextAfter - 1;
                next = nextAfter;
                return value(current);
            }
        };
    }

    @Override
    default Spliterator<V> spliterator() {
        return Spliterators.spliterator(iterator(), nElements(), Spliterator.ORDERED | Spliterator.IMMUTABLE);
    }

    @Override
    default Stream<V> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    default Object[] toArray() {
        final int n = nElements();
        final Object[] array = new Object[n];
        for (int i = 0; i < n; i++) {
            array[i] = value(i);
        }
        return array;
    }

    default V[] toArray(final IntFunction<V[]> arrayFactory) {
        final int n = nElements();
        final V[] array = arrayFactory.apply(n);
        for (int i = 0; i < n; i++) {
            array[i] = value(i);
        }
        return array;
    }

    @Override
    default ObjVector<V> materialize() {
        @SuppressWarnings("unchecked")
        final V[] array = (V[])toArray();
        return ObjVector.create(array);
    }

    @Override
    default BoolVector toBool1D(final Predicate<? super V> function) {
        return BoolVector.create(nElements(), Obj1D.super.toBool1D(function));
    }

    @Override
    default IntVector toInt1D(final ToIntFunction<? super V> function) {
        return IntVector.create(nElements(), Obj1D.super.toInt1D(function));
    }

    @Override
    default LongVector toLong1D(final ToLongFunction<? super V> function) {
        return LongVector.create(nElements(), Obj1D.super.toLong1D(function));
    }

    @Override
    default DoubleVector toDouble1D(final ToDoubleFunction<? super V> function) {
        return DoubleVector.create(nElements(), Obj1D.super.toDouble1D(function));
    }

    @Override
    default ObjVector<String> toStr1D() {
        return ObjVector.create(nElements(), Obj1D.super.toStr1D());
    }

    @Override
    default ObjVector<String> toStr1D(final String nullDefault) {
        return ObjVector.create(nElements(), Obj1D.super.toStr1D(nullDefault));
    }

    @SafeVarargs
    static <V> ObjVector<V> create(final V... values) {
        Objects.requireNonNull(values);
        return new ObjVector<V>() {
            @Override
            public int nElements() {
                return values.length;
            }

            @Override
            public V value(final int index) {
                return index >= 0 & index < values.length ? values[index] : null;
            }
            @Override
            public ObjVector<V> materialize() {
                return this;
            }

            @Override
            public Object[] toArray() {
                final Object[] array = new Object[values.length];
                System.arraycopy(values, 0, array, 0, values.length);
                return array;
            }

            @Override
            public V[] toArray(final IntFunction<V[]> arrayFactory) {
                final V[] array = arrayFactory.apply(values.length);
                System.arraycopy(values, 0, array, 0, values.length);
                return array;
            }
            @Override
            public int hashCode() {
                return Vector.hashCode(this, HASH_FUNCTION);
            }

            @Override
            public boolean equals(final Object obj) {
                @SuppressWarnings("unchecked")
                final Class<ObjVector<V>> clazz = (Class<ObjVector<V>>)(Object)ObjVector.class;
                return Vector.equals(this, obj, clazz, VALUE_EQUALITY);
            }

            @Override
            public String toString() {
                return "ObjVector:" + nElements();
            }
        };
    }

    static <V> ObjVector<V> create(final Vector<?, ?> meta, final Obj1D<V> data) {
        return create(meta.nElements(), data);
    }

    static <V> ObjVector<V> create(final int n, final Obj1D<V> data) {
        if (n < 0) throw new IllegalArgumentException("Negative vector length: " + n);
        Objects.requireNonNull(data);
        return new ObjVector<V>() {
            @Override
            public int nElements() {
                return n;
            }

            @Override
            public V value(final int index) {
                return index >= 0 & index < n ? data.value(index) : null;
            }

            @Override
            public int hashCode() {
                return Vector.hashCode(this, HASH_FUNCTION);
            }

            @Override
            public boolean equals(final Object obj) {
                @SuppressWarnings("unchecked")
                final Class<ObjVector<V>> clazz = (Class<ObjVector<V>>)(Object)ObjVector.class;
                return Vector.equals(this, obj, clazz, VALUE_EQUALITY);
            }

            @Override
            public String toString() {
                return "ObjVector:" + nElements();
            }
        };
    }

    static <V> ObjVector<V> constant(final int n, final V value) {
        return value == null ? nulls(n) : create(n, index -> index >= 0 & index < n ? value : null);
    }

    static <V> ObjVector<V> nulls(final int n) {
        return create(n, Obj1D.nulls());//no index check needed as null is default
    }
}
