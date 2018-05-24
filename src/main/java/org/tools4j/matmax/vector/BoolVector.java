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

import org.tools4j.matmax.indexed.Bool1D;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface BoolVector extends Vector<Boolean, Bool1D>, Bool1D {

    HashFunction<BoolVector> HASH_FUNCTION = (vec, ind) -> Boolean.hashCode(vec.valueAsBoolean(ind));
    ValueEquality<BoolVector> VALUE_EQUALITY = (vec1, vec2, ind) -> Boolean.compare(
            vec1.valueAsBoolean(ind), vec2.valueAsBoolean(ind)) == 0;

    @Override
    default BoolVector apply(final Function<? super Bool1D, ? extends Bool1D> operator) {
        return create(this, Bool1D.super.apply(operator));
    }

    @Override
    default BinaryOperable<Bool1D, ? extends BoolVector> with(final Bool1D secondOperand) {
        return operator -> operator.apply(this, secondOperand);
    }

    @Override
    default Iterator<Boolean> iterator() {
        return new Iterator<Boolean>() {
            private int next = 0;
            @Override
            public boolean hasNext() {
                return next < nElements();
            }

            @Override
            public Boolean next() {
                final int nextAfter = next + 1;
                if (nextAfter > nElements()) {
                    throw new NoSuchElementException();
                }
                final int current = nextAfter - 1;
                next = nextAfter;
                return valueAsBoolean(current);
            }
        };
    }

    @Override
    default Spliterator<Boolean> spliterator() {
        return Spliterators.spliterator(iterator(), nElements(), Spliterator.ORDERED | Spliterator.IMMUTABLE);
    }

    @Override
    default Stream<Boolean> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    default BitSet toBitSet() {
        final int n = nElements();
        final BitSet bitSet = new BitSet(n);
        for (int i = 0; i < n; i++) {
            bitSet.set(i, valueAsBoolean(i));
        }
        return bitSet;
    }

    default boolean[] toArray() {
        final int n = nElements();
        final boolean[] array = new boolean[n];
        for (int i = 0; i < n; i++) {
            array[i] = valueAsBoolean(i);
        }
        return array;
    }

    @Override
    default Bool1D materialize() {
        return create(nElements(), toBitSet());
    }

    @Override
    default IntVector toInt1D(final int trueValue, final int falseValue) {
        return IntVector.create(nElements(), Bool1D.super.toInt1D(trueValue, falseValue));
    }

    @Override
    default LongVector toLong1D(final long trueValue, final long falseValue) {
        return LongVector.create(nElements(), Bool1D.super.toLong1D(trueValue, falseValue));
    }

    @Override
    default DoubleVector toDouble1D(final double trueValue, final double falseValue) {
        return DoubleVector.create(nElements(), Bool1D.super.toDouble1D(trueValue, falseValue));
    }

    @Override
    default ObjVector<Boolean> toObj1D() {
        return toObj1D(Boolean.TRUE, Boolean.FALSE);
    }

    @Override
    default <T> ObjVector<T> toObj1D(final T trueValue, final T falseValue) {
        return ObjVector.create(nElements(), Bool1D.super.toObj1D(trueValue, falseValue));
    }

    static BoolVector create(final int n, final BitSet values) {
        if (n < 0) throw new IllegalArgumentException("Negative vector length: " + n);
        Objects.requireNonNull(values);
        return new BoolVector() {
            @Override
            public int nElements() {
                return n;
            }

            @Override
            public boolean valueAsBoolean(final int index) {
                return index >= 0 & index < n & values.get(index);
            }

            @Override
            public int hashCode() {
                return Vector.hashCode(this, HASH_FUNCTION);
            }

            @Override
            public BitSet toBitSet() {
                return (BitSet)values.clone();
            }

            @Override
            public Bool1D materialize() {
                return this;
            }

            @Override
            public boolean equals(final Object obj) {
                return Vector.equals(this, obj, BoolVector.class, VALUE_EQUALITY);
            }

            @Override
            public String toString() {
                return "BoolVector:" + nElements();
            }
        };
    }

    static BoolVector create(final boolean... values) {
        final int len = values.length;
        return create(len, index -> index >= 0 && index < len && values[index]);
    }

    static BoolVector create(final Vector<?, ?> meta, final Bool1D data) {
        return create(meta.nElements(), data);
    }

    static BoolVector create(final int n, final Bool1D data) {
        if (n < 0) throw new IllegalArgumentException("Negative vector length: " + n);
        Objects.requireNonNull(data);
        return new BoolVector() {
            @Override
            public int nElements() {
                return n;
            }

            @Override
            public boolean valueAsBoolean(final int index) {
                return index >= 0 && index < n && data.value(index);
            }

            @Override
            public int hashCode() {
                return Vector.hashCode(this, HASH_FUNCTION);
            }

            @Override
            public boolean equals(final Object obj) {
                return Vector.equals(this, obj, BoolVector.class, VALUE_EQUALITY);
            }

            @Override
            public String toString() {
                return "BoolVector:" + nElements();
            }
        };
    }

    static BoolVector constant(final int n, final boolean value) {
        return create(n, index -> index >= 0 & index < n & value);//NOTE: unconditional & is intentional!
    }

    static BoolVector trues(final int n) {
        return constant(n, true);
    }

    static BoolVector falses(final int n) {
        return create(n, index -> false);
    }
}
