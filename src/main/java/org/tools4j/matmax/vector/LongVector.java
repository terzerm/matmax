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

import org.tools4j.matmax.function.LongBiPredicate;
import org.tools4j.matmax.indexed.Double1D;
import org.tools4j.matmax.indexed.Long1D;
import org.tools4j.matmax.matrix.LongMatrix;

import java.util.*;
import java.util.function.*;
import java.util.stream.LongStream;
import java.util.stream.StreamSupport;

public interface LongVector extends Vector<Long, Long1D>, Long1D {

    HashFunction<LongVector> HASH_FUNCTION = (vec, ind) -> Long.hashCode(vec.valueAsLong(ind));
    ValueEquality<LongVector> VALUE_EQUALITY = (vec1, vec2, ind) -> Long.compare(
            vec1.valueAsLong(ind), vec2.valueAsLong(ind)) == 0;//NOTE: Long.compare handles NaN values

    @Override
    default LongVector apply(final Function<? super Long1D, ? extends Long1D> operator) {
        return create(this, Long1D.super.apply(operator));
    }

    @Override
    default LongVector applyToEach(final LongUnaryOperator operator) {
        return LongVector.create(this, Long1D.super.applyToEach(operator));
    }

    @Override
    default BinaryOperable<Long1D, ? extends LongVector> with(final Long1D secondOperand) {
        return operator -> operator.apply(this, secondOperand);
    }

    @Override
    default LongMatrix toRow() {
        return LongMatrix.create(1, nElements(), (row, column) -> row >= 0 & row < 1 ? value(column) : 0L);
    }

    @Override
    default LongMatrix toColumn() {
        return LongMatrix.create(nElements(), 1, (row, column) -> column >= 0 & column < 1 ? value(row) : 0L);
    }

    @Override
    default int indexOf(final Long value, final int start) {
        return indexOf(value.longValue(), start);
    }

    default int indexOf(final long value, final int start) {
        return indexOf(value, start, (d1, d2) -> Double.compare(d1, d2) == 0);
    }

    default int indexOf(final long value, final int start, final LongBiPredicate matcher) {
        final int n = nElements();
        for (int i = start; i < n; i++) {
            if (matcher.test(value, valueAsLong(i))) {
                return i;
            }
        }
        return -1;
    }

    default int indexMatching(final LongPredicate predicate) {
        final int n = nElements();
        for (int i = 0; i < n; i++) {
            if (predicate.test(valueAsLong(i))) {
                return i;
            }
        }
        return -1;
    }

    @Override
    default PrimitiveIterator.OfLong iterator() {
        return new PrimitiveIterator.OfLong() {
            private int next = 0;
            @Override
            public boolean hasNext() {
                return next < nElements();
            }

            @Override
            public long nextLong() {
                final int nextAfter = next + 1;
                if (nextAfter > nElements()) {
                    throw new NoSuchElementException();
                }
                final int current = nextAfter - 1;
                next = nextAfter;
                return valueAsLong(current);
            }
        };
    }

    @Override
    default Spliterator.OfLong spliterator() {
        return Spliterators.spliterator(iterator(), nElements(), Spliterator.ORDERED | Spliterator.IMMUTABLE);
    }

    @Override
    default LongStream stream() {
        return StreamSupport.longStream(spliterator(), false);
    }

    default long[] toArray() {
        final int n = nElements();
        final long[] array = new long[n];
        for (int i = 0; i < n; i++) {
            array[i] = valueAsLong(i);
        }
        return array;
    }

    @Override
    default LongVector materialize() {
        return LongVector.create(toArray());
    }

    @Override
    default BoolVector toBool1D(final LongPredicate function) {
        return BoolVector.create(nElements(), Long1D.super.toBool1D(function));
    }

    @Override
    default IntVector toInt1D(final LongToIntFunction function) {
        return IntVector.create(nElements(), Long1D.super.toInt1D(function));
    }

    @Override
    default Double1D toDouble1D(final LongToDoubleFunction function) {
        return DoubleVector.create(nElements(), Long1D.super.toDouble1D(function));
    }

    @Override
    default ObjVector<Long> toObj1D() {
        return toObj1D(Long::valueOf);
    }

    @Override
    default <T> ObjVector<T> toObj1D(final LongFunction<? extends T> function) {
        return ObjVector.create(nElements(), Long1D.super.toObj1D(function));
    }

    @Override
    default ObjVector<String> toStr1D() {
        return ObjVector.create(nElements(), Long1D.super.toStr1D());
    }

    static LongVector create(final long... values) {
        Objects.requireNonNull(values);
        return new LongVector() {
            @Override
            public int nElements() {
                return values.length;
            }

            @Override
            public long valueAsLong(final int index) {
                return index >= 0 & index < values.length ? values[index] : 0L;
            }

            @Override
            public long[] toArray() {
                return values.clone();
            }

            @Override
            public LongVector materialize() {
                return this;
            }

            @Override
            public int hashCode() {
                return Vector.hashCode(this, HASH_FUNCTION);
            }

            @Override
            public boolean equals(final Object obj) {
                return Vector.equals(this, obj, LongVector.class, VALUE_EQUALITY);
            }

            @Override
            public String toString() {
                return "LongVector:" + nElements();
            }
        };
    }

    static LongVector create(final Vector<?, ?> meta, final Long1D data) {
        return create(meta.nElements(), data);
    }

    static LongVector create(final int n, final Long1D data) {
        if (n < 0) throw new IllegalArgumentException("Negative vector length: " + n);
        Objects.requireNonNull(data);
        return new LongVector() {
            @Override
            public int nElements() {
                return n;
            }

            @Override
            public long valueAsLong(final int index) {
                return index >= 0 & index < n ? data.value(index) : 0L;
            }

            @Override
            public int hashCode() {
                return Vector.hashCode(this, HASH_FUNCTION);
            }

            @Override
            public boolean equals(final Object obj) {
                return Vector.equals(this, obj, LongVector.class, VALUE_EQUALITY);
            }

            @Override
            public String toString() {
                return "LongVector:" + nElements();
            }
        };
    }

    static LongVector constant(final int n, final long value) {
        return value == 0L ? zero(n) : create(n, index -> index >= 0 & index < n ? value : 0L);
    }

    static LongVector zero(final int n) {
        return create(n, Long1D.ZERO);//no index check needed as zero is default
    }
}
