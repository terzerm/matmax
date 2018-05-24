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
package org.tools4j.matmax.dataframe;

import org.tools4j.matmax.indexed.Indexed2D;
import org.tools4j.matmax.matrix.Matrix;
import org.tools4j.matmax.vector.ObjVector;

public interface DataFrame<V, T extends Indexed2D<V, T>> extends Matrix<V, T> {

    Header header();

    default ObjVector<?> headerRow() {
        return header().row(0);
    }

    default ObjVector<?> headerColumn() {
        return header().column(0);
    }

    default DataFrame<V, T> reshapeTo(final DataFrame<?, ?> template) {
        return reshapeTo(template.header());
    }

    default DataFrame<V, T> reshapeTo(final Header header) {
        if (hasSameShapeAs(header)) return this;
        return null;//FIXME
    }

    @Override
    default int nRows() {
        return header().nRows();
    }

    @Override
    default int nColumns() {
        return header().nColumns();
    }

    default boolean hasSameShapeAs(final DataFrame<?,?> other) {
        return hasSameShapeAs(other.header());
    }

    default boolean hasSameShapeAs(final Header header) {
        return Header.equals(header(), header);
    }

}
