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

import java.util.*;
import java.util.function.*;
import java.util.stream.DoubleStream;
import java.util.stream.StreamSupport;

public interface DoubleVector extends Vector<Double, Double1D>, Double1D {

    HashFunction<DoubleVector> HASH_FUNCTION = (vec, ind) -> Double.hashCode(vec.valueAsDouble(ind));
    ValueEquality<DoubleVector> VALUE_EQUALITY = (vec1, vec2, ind) -> Double.compare(
            vec1.valueAsDouble(ind), vec2.valueAsDouble(ind)) == 0;//NOTE: Double.compare handles NaN values

    @Override
    default DoubleVector apply(final Function<? super Double1D, ? extends Double1D> operator) {
        return create(this, Double1D.super.apply(operator));
    }

    @Override
    default DoubleVector applyToEach(final DoubleUnaryOperator operator) {
        return DoubleVector.create(this, Double1D.super.applyToEach(operator));
    }

    @Override
    default BinaryOperable<Double1D, ? extends DoubleVector> with(final Double1D secondOperand) {
        return operator -> operator.apply(this, secondOperand);
    }

    @Override
    default DoubleVector materialize() {
        return DoubleVector.create(toArray());
    }

    @Override
    default PrimitiveIterator.OfDouble iterator() {
        return new PrimitiveIterator.OfDouble() {
            private int next = 0;
            @Override
            public boolean hasNext() {
                return next < nElements();
            }

            @Override
            public double nextDouble() {
                final int nextAfter = next + 1;
                if (nextAfter > nElements()) {
                    throw new NoSuchElementException();
                }
                final int current = nextAfter - 1;
                next = nextAfter;
                return valueAsDouble(current);
            }
        };
    }

    @Override
    default Spliterator.OfDouble spliterator() {
        return Spliterators.spliterator(iterator(), nElements(), Spliterator.ORDERED | Spliterator.IMMUTABLE);
    }

    @Override
    default DoubleStream stream() {
        return StreamSupport.doubleStream(spliterator(), false);
    }

    default double[] toArray() {
        final int n = nElements();
        final double[] array = new double[n];
        for (int i = 0; i < n; i++) {
            array[i] = valueAsDouble(i);
        }
        return array;
    }

    @Override
    default BoolVector toBool1D(final DoublePredicate function) {
        return BoolVector.create(nElements(), Double1D.super.toBool1D(function));
    }

    @Override
    default IntVector toInt1D(final DoubleToIntFunction function) {
        return IntVector.create(nElements(), Double1D.super.toInt1D(function));
    }

    @Override
    default LongVector toLong1D(final DoubleToLongFunction function) {
        return LongVector.create(nElements(), Double1D.super.toLong1D(function));
    }

    @Override
    default ObjVector<Double> toObj1D() {
        return toObj1D(Double::valueOf);
    }

    @Override
    default <T> ObjVector<T> toObj1D(final DoubleFunction<? extends T> function) {
        return ObjVector.create(nElements(), Double1D.super.toObj1D(function));
    }

    static DoubleVector create(final double... values) {
        Objects.requireNonNull(values);
        return new DoubleVector() {
            @Override
            public int nElements() {
                return values.length;
            }

            @Override
            public double valueAsDouble(final int index) {
                return index >= 0 & index < values.length ? values[index] : Double.NaN;
            }

            @Override
            public int hashCode() {
                return Vector.hashCode(this, HASH_FUNCTION);
            }

            @Override
            public boolean equals(final Object obj) {
                return Vector.equals(this, obj, DoubleVector.class, VALUE_EQUALITY);
            }

            @Override
            public double[] toArray() {
                return values.clone();
            }

            @Override
            public DoubleVector materialize() {
                return this;
            }

            @Override
            public String toString() {
                return "DoubleVector:" + nElements();
            }
        };
    }

    static DoubleVector create(final Vector<?, ?> meta, final Double1D data) {
        return create(meta.nElements(), data);
    }

    static DoubleVector create(final int n, final Double1D data) {
        if (n < 0) throw new IllegalArgumentException("Negative vector length: " + n);
        Objects.requireNonNull(data);
        return new DoubleVector() {
            @Override
            public int nElements() {
                return n;
            }

            @Override
            public double valueAsDouble(final int index) {
                return index >= 0 & index < n ? data.value(index) : Double.NaN;
            }

            @Override
            public int hashCode() {
                return Vector.hashCode(this, HASH_FUNCTION);
            }

            @Override
            public boolean equals(final Object obj) {
                return Vector.equals(this, obj, DoubleVector.class, VALUE_EQUALITY);
            }

            @Override
            public String toString() {
                return "DoubleVector:" + nElements();
            }
        };
    }

    static DoubleVector constant(final int n, final double value) {
        return create(n, index -> index >= 0 & index < n ? value : Double.NaN);
    }

    static DoubleVector nan(final int n) {
        return create(n, index -> Double.NaN);
    }
}
