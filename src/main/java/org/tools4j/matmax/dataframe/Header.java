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
                if (!Objects.equals(header1.rowColumnLabel(rHdr, cHdr), header2.rowColumnLabel(rHdr, cHdr))) {
                    return false;
                }
            }
        }
        for (int rHdr = 0; rHdr < hdrRows; rHdr++) {
            for (int c = 0; c < cols; c++) {
                if (!Objects.equals(header1.columnLabel(rHdr, c), header2.columnLabel(rHdr, c))) {
                    return false;
                }
            }
        }
        for (int r = 0; r < rows; r++) {
            for (int cHdr = 0; cHdr < hdrCols; cHdr++) {
                if (!Objects.equals(header1.rowLabel(r, cHdr), header2.rowLabel(r, cHdr))) {
                    return false;
                }
            }
        }
        return true;
    }
}
