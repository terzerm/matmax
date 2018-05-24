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

import org.tools4j.matmax.indexed.Indexed1D;

import java.util.Iterator;
import java.util.Objects;
import java.util.Spliterator;
import java.util.function.BiPredicate;
import java.util.stream.BaseStream;

public interface Vector<V, T extends Indexed1D<V, T>> extends Indexed1D<V, T>, Iterable<V> {
    int nElements();

    default int indexOf(final V value) {
        return indexOf(value, Objects::equals);
    }

    default int indexOf(final V value, final BiPredicate<? super V, ? super V> matcher) {
        final int n = nElements();
        for (int i = 0; i < n; i++) {
            if (matcher.test(value, value(i))) {
                return i;
            }
        }
        return -1;
    }

    T materialize();

    Iterator<V> iterator();

    BaseStream<V,?> stream();

    Spliterator<V> spliterator();

    interface HashFunction<V extends Vector<?,?>> {
        int hashCode(V vector, int index);
    }

    interface ValueEquality<V extends Vector<?,?>> {
        boolean equalValues(V vector1, V vector2, int index);
    }

    static <V extends Vector<?,?>> int hashCode(final V vector, final HashFunction<? super V> hashFunction) {
        final int n = 10;
        final int nElements = vector.nElements();
        int hash = nElements;
        if (nElements == 0) return hash;
        if (nElements <= n) {
            for (int i = 0; i < nElements; i++) {
                hash = (31 * hash) + hashFunction.hashCode(vector, i);
            }
        } else {
            int p = 2147483647;//largest int prime
            final int len = Math.min(nElements, n);
            for (int i = 0; i < len; i++) {
                final int index = p % nElements;
                hash = (31 * hash) + hashFunction.hashCode(vector, index);
                p *= 2147483647;
                p &= Integer.MAX_VALUE;//makes it positive, or zero for Integer.MIN_VALUE
            }
        }
        return hash;
    }

    static <V extends Vector<?,?>> boolean equals(final Vector<?,?> v1, final Object v2, final Class<V> vectorType,
                                                  final ValueEquality<? super V> valueEquality) {
        if (v1 == v2) return true;
        if (v1 == null || v2 == null) return false;//cannot be both null due to first line
        if (vectorType.isInstance(v1) & vectorType.isInstance(v2)) {
            return equals(vectorType.cast(v1), vectorType.cast(v2), valueEquality);
        }
        return false;
    }

    static <V extends Vector<?,?>> boolean equals(final V v1, final V v2, final ValueEquality<? super V> valueEquality) {
        if (v1 == v2) return true;
        if (v1 == null || v2 == null) return false;//cannot be both null due to first line
        if (v1.nElements() != v2.nElements()) return false;
        final int n = v1.nElements();
        for (int i = 0; i < n; i++) {
            if (!valueEquality.equalValues(v1, v2, i)) return false;
        }
        return true;
    }
}
