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

import org.tools4j.matmax.indexed.Double1D;
import org.tools4j.matmax.indexed.Int1D;
import org.tools4j.matmax.matrix.IntMatrix;

import java.util.*;
import java.util.function.*;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

public interface IntVector extends Vector<Integer, Int1D>, Int1D {

    HashFunction<IntVector> HASH_FUNCTION = (vec, ind) -> Integer.hashCode(vec.valueAsInt(ind));
    ValueEquality<IntVector> VALUE_EQUALITY = (vec1, vec2, ind) -> Integer.compare(
            vec1.valueAsInt(ind), vec2.valueAsInt(ind)) == 0;//NOTE: Int.compare handles NaN values

    @Override
    default IntVector apply(final Function<? super Int1D, ? extends Int1D> operator) {
        return create(this, Int1D.super.apply(operator));
    }

    @Override
    default IntVector applyToEach(final IntUnaryOperator operator) {
        return IntVector.create(this, Int1D.super.applyToEach(operator));
    }

    @Override
    default BinaryOperable<Int1D, ? extends IntVector> with(final Int1D secondOperand) {
        return operator -> operator.apply(this, secondOperand);
    }

    @Override
    default IntMatrix toRow() {
        return IntMatrix.create(1, nElements(), (row, column) -> row >= 0 & row < 1 ? value(column) : 0);
    }

    @Override
    default IntMatrix toColumn() {
        return IntMatrix.create(nElements(), 1, (row, column) -> column >= 0 & column < 1 ? value(row) : 0);
    }

    @Override
    default ObjVector<String> toStr1D() {
        return ObjVector.create(nElements(), Int1D.super.toStr1D());
    }

    @Override
    default PrimitiveIterator.OfInt iterator() {
        return new PrimitiveIterator.OfInt() {
            private int next = 0;
            @Override
            public boolean hasNext() {
                return next < nElements();
            }

            @Override
            public int nextInt() {
                final int nextAfter = next + 1;
                if (nextAfter > nElements()) {
                    throw new NoSuchElementException();
                }
                final int current = nextAfter - 1;
                next = nextAfter;
                return valueAsInt(current);
            }
        };
    }

    @Override
    default Spliterator.OfInt spliterator() {
        return Spliterators.spliterator(iterator(), nElements(), Spliterator.ORDERED | Spliterator.IMMUTABLE);
    }

    @Override
    default IntStream stream() {
        return StreamSupport.intStream(spliterator(), false);
    }

    default int[] toArray() {
        final int n = nElements();
        final int[] array = new int[n];
        for (int i = 0; i < n; i++) {
            array[i] = valueAsInt(i);
        }
        return array;
    }

    @Override
    default IntVector materialize() {
        return IntVector.create(toArray());
    }

    @Override
    default BoolVector toBool1D(final IntPredicate function) {
        return BoolVector.create(nElements(), Int1D.super.toBool1D(function));
    }

    @Override
    default Double1D toDouble1D() {
        return DoubleVector.create(nElements(), Int1D.super.toDouble1D());
    }

    @Override
    default Double1D toDouble1D(final IntToDoubleFunction function) {
        return DoubleVector.create(nElements(), Int1D.super.toDouble1D(function));
    }

    @Override
    default LongVector toLong1D(final IntToLongFunction function) {
        return LongVector.create(nElements(), Int1D.super.toLong1D(function));
    }

    @Override
    default ObjVector<Integer> toObj1D() {
        return toObj1D(Integer::valueOf);
    }

    @Override
    default <T> ObjVector<T> toObj1D(final IntFunction<? extends T> function) {
        return ObjVector.create(nElements(), Int1D.super.toObj1D(function));
    }

    static IntVector create(final int... values) {
        Objects.requireNonNull(values);
        return new IntVector() {
            @Override
            public int nElements() {
                return values.length;
            }

            @Override
            public int valueAsInt(final int index) {
                return index >= 0 & index < values.length ? values[index] : 0;
            }

            @Override
            public int[] toArray() {
                return values.clone();
            }

            @Override
            public IntVector materialize() {
                return this;
            }

            @Override
            public int hashCode() {
                return Vector.hashCode(this, HASH_FUNCTION);
            }

            @Override
            public boolean equals(final Object obj) {
                return Vector.equals(this, obj, IntVector.class, VALUE_EQUALITY);
            }

            @Override
            public String toString() {
                return "IntVector:" + nElements();
            }
        };
    }

    static IntVector create(final Vector<?, ?> meta, final Int1D data) {
        return create(meta.nElements(), data);
    }

    static IntVector create(final int n, final Int1D data) {
        if (n < 0) throw new IllegalArgumentException("Negative vector length: " + n);
        Objects.requireNonNull(data);
        return new IntVector() {
            @Override
            public int nElements() {
                return n;
            }

            @Override
            public int valueAsInt(final int index) {
                return index >= 0 & index < n ? data.value(index) : 0;
            }

            @Override
            public int hashCode() {
                return Vector.hashCode(this, HASH_FUNCTION);
            }

            @Override
            public boolean equals(final Object obj) {
                return Vector.equals(this, obj, IntVector.class, VALUE_EQUALITY);
            }

            @Override
            public String toString() {
                return "IntVector:" + nElements();
            }
        };
    }

    static IntVector constant(final int n, final int value) {
        return value == 0 ? zero(n) : create(n, index -> index >= 0 & index < n ? value : 0);
    }

    static IntVector zero(final int n) {
        return create(n, Int1D.ZERO);//no index check needed as zero is default
    }
}
