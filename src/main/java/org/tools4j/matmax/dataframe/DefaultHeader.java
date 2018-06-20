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

import org.tools4j.matmax.indexed.Obj2D;
import org.tools4j.matmax.matrix.ObjMatrix;
import org.tools4j.matmax.vector.ObjVector;

import java.util.Objects;

public class DefaultHeader implements Header {

    private final Obj2D<?> rowColumnLabels;
    private final ObjMatrix<?> columnLabels;
    private final ObjMatrix<?> rowLabels;

    public DefaultHeader(final Object[] columnLabels, final Object[] rowLabels) {
        this(ObjVector.create(columnLabels), ObjVector.create(rowLabels));
    }

    public DefaultHeader(final ObjVector<?> columnLabels, final ObjVector<?> rowLabels) {
        this(columnLabels.toRow(), rowLabels.toColumn());
    }

    public DefaultHeader(final ObjMatrix<?> columnLabels, final ObjMatrix<?> rowLabels) {
        this(Obj2D.NULL, columnLabels, rowLabels);
    }

    public DefaultHeader(final Obj2D<?> rowColumnLabels, final ObjMatrix<?> columnLabels, final ObjMatrix<?> rowLabels) {
        this.rowColumnLabels = Objects.requireNonNull(rowColumnLabels);
        this.columnLabels = Objects.requireNonNull(columnLabels);
        this.rowLabels = Objects.requireNonNull(rowLabels);
    }

    @Override
    public Object rowColumnLabel(final int headerRow, final int headerColumn) {
        return rowColumnLabels.value(headerRow, headerColumn);
    }

    @Override
    public Object columnLabel(final int headerRow, final int column) {
        return columnLabels.value(headerRow, column);
    }

    @Override
    public Object rowLabel(final int row, final int headerColumn) {
        return rowLabels.value(row, headerColumn);
    }

    @Override
    public int nHeaderRows() {
        return columnLabels.nRows();
    }

    @Override
    public int nHeaderColumns() {
        return rowLabels.nColumns();
    }

    @Override
    public int nRows() {
        return rowLabels.nRows();
    }

    @Override
    public int nColumns() {
        return columnLabels.nColumns();
    }

    @Override
    public int hashCode() {
        return Header.hashCode(this);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o instanceof Header) {
            return Header.equals(this, (Header) o);
        }
        return false;
    }

    @Override
    public String toString() {
        return "DefaultHeader{columnLabels:" + nHeaderRows() + "x" + nColumns() + ", rowLabels:" + nRows() + "x" + nHeaderColumns() + "}";
    }
}
