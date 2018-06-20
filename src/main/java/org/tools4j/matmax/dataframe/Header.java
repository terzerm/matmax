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

import org.tools4j.matmax.vector.ObjVector;

import java.util.Objects;

/**
 * <pre>
 *             (header columns)      (normal columns)
 *          o----------+----------o----------+----------+---~ / ~---o
 *          | (H0, H0) : (H0, H1) |  (H0, 0) :  (H0, 1) :           |
 * (header) |- - - - - + - - - - -|- - - - - + - - - - -:-   ...   -|
 *  (rows)  | (H1, H0) : (H1, H1) |  (H1, 0) :  (H1, 1) :           |
 *          o----------+----------o----------+----------+---~ / ~---o
 *          |  (0, H0) :  (0, H1) |  (0, 0)  :  (0, 1)  :           |
 * (normal) |- - - - - + - - - - -|- - - - - + - - - - -:    ...   -|
 *  (rows)  |  (1, H0) :  (1, H1) |  (1, 0)  :  (1, 1)  :           |
 *          |- - - - - + - - - - -|- - - - - + - - - - -:- -~ / ~- -o
 *          |                     |                                 |
 *          ~         ...         ~         ...              ...    ~
 *          |                     |                                 |
 *          o----------+----------o----------+----------+---~ / ~---o
 * </pre>
 */
public interface Header {
    Object rowColumnLabel(int headerRow, int headerColumn);
    Object columnLabel(int headerRow, int column);
    Object rowLabel(int row, int headerColumn);
    int nHeaderRows();
    int nHeaderColumns();
    int nRows();
    int nColumns();

    default ObjVector<?> row(final int headerRow) {
        return ObjVector.create(nColumns(), col -> columnLabel(headerRow, col));
    }

    default ObjVector<?> column(final int headerColumn) {
        return ObjVector.create(nRows(), row -> rowLabel(row, headerColumn));
    }

    static int hashCode(final Header header) {
        final int hdrRows = header.nHeaderRows();
        final int hdrCols = header.nHeaderColumns();
        final int rows = header.nRows();
        final int cols = header.nColumns();
        int code = 0;
        for (int rHdr = 0; rHdr < hdrRows; rHdr++) {
            for (int cHdr = 0; cHdr < hdrCols; cHdr++) {
                code = 31 * code + Objects.hashCode(header.rowColumnLabel(rHdr, cHdr));
            }
        }
        for (int rHdr = 0; rHdr < hdrRows; rHdr++) {
            for (int c = 0; c < cols; c++) {
                code = 31 * code + Objects.hashCode(header.columnLabel(rHdr, c));
            }
        }
        for (int r = 0; r < rows; r++) {
            for (int cHdr = 0; cHdr < hdrCols; cHdr++) {
                code = 31 * code + Objects.hashCode(header.rowLabel(r, cHdr));
            }
        }
        return code;
    }

    static boolean equals(final Header header1, final Header header2) {
        final int hdrRows = header1.nHeaderRows();
        final int hdrCols = header1.nHeaderColumns();
        final int rows = header1.nRows();
        final int cols = header1.nColumns();
        if (hdrRows != header2.nHeaderRows() | hdrCols != header2.nHeaderColumns() | rows != header2.nRows() | cols != header2.nColumns()) {
            return false;
        }
        for (int rHdr = 0; rHdr < hdrRows; rHdr++) {
            for (int cHdr = 0; cHdr < hdrCols; cHdr++) {
                if (!equalsRowColumnLabel(header1, header2, rHdr, cHdr)) {
                    return false;
                }
            }
        }
        for (int rHdr = 0; rHdr < hdrRows; rHdr++) {
            for (int c = 0; c < cols; c++) {
                if (!equalsColumnLabel(header1, header2, rHdr, c)) {
                    return false;
                }
            }
        }
        for (int r = 0; r < rows; r++) {
            for (int cHdr = 0; cHdr < hdrCols; cHdr++) {
                if (!equalsRowLabel(header1, header2, r, cHdr)) {
                    return false;
                }
            }
        }
        return true;
    }

    static boolean equalsRowColumnLabel(final Header header1, final Header header2, final int headerRow, final int headerColumn) {
        return Objects.equals(header1.rowColumnLabel(headerRow, headerColumn), header2.rowColumnLabel(headerRow, headerColumn));
    }

    static boolean equalsColumnLabel(final Header header1, final Header header2, final int headerRow, final int column) {
        return Objects.equals(header1.columnLabel(headerRow, column), header2.columnLabel(headerRow, column));
    }

    static boolean equalsRowLabel(final Header header1, final Header header2, final int row, final int headerColumn) {
        return Objects.equals(header1.rowLabel(row, headerColumn), header2.rowLabel(row, headerColumn));
    }
}
