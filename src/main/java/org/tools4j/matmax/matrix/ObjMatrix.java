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
package org.tools4j.matmax.matrix;

import org.tools4j.matmax.indexed.Obj2D;
import org.tools4j.matmax.vector.ObjVector;

import java.util.Objects;

public interface ObjMatrix<V> extends Matrix<V, Obj2D<V>>, Obj2D<V> {

    HashFunction<ObjMatrix<?>> HASH_FUNCTION = (m,r,c) -> Objects.hashCode(m.value(r, c));
    ValueEquality<ObjMatrix<?>> VALUE_EQUALITY = (m1,m2,r,c) -> Objects.equals(m1.value(r,c), m2.value(r,c));

    @Override
    default ObjVector<V> row(final int row) {
        final int cols = nColumns();
        return ObjVector.create(cols, col -> value(row, col));
    }

    @Override
    default ObjVector<V> column(final int col) {
        final int rows = nRows();
        return ObjVector.create(rows, row -> value(row, col));
    }

    static <V> ObjMatrix<V> create(final Matrix<?,?> meta, Obj2D<V> data) {
        return create(meta.nRows(), meta.nColumns(), data);
    }

    static <V> ObjMatrix<V> create(final int rows, final int cols, final Obj2D<V> data) {
        return new ObjMatrix<V>() {
            @Override
            public int nRows() {
                return rows;
            }

            @Override
            public int nColumns() {
                return cols;
            }

            @Override
            public V value(final int row, final int column) {
                return data.value(row, column);
            }

            @Override
            public int hashCode() {
                return Matrix.hashCode(this, HASH_FUNCTION);
            }

            @Override
            public boolean equals(final Object obj) {
                if (this == obj) return true;
                if (obj == null) return false;
                if (obj instanceof ObjMatrix) {
                    return Matrix.equals(this, (ObjMatrix<?>)obj, VALUE_EQUALITY);
                }
                return false;
            }
            @Override
            public String toString() {
                return "ObjMatrix:" + nRows() + "x" + nColumns();
            }
        };
    }

}
