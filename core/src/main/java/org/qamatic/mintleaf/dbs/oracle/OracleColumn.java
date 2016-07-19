/*
 *
 * The MIT License (MIT)
 *
 * Copyright (c) 2010-2015 Senthil Maruthaiappan
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
 *
 *
 */

package org.qamatic.mintleaf.dbs.oracle;

import org.qamatic.mintleaf.DbColumn;

import java.sql.Date;
import java.sql.Types;

public class OracleColumn extends DbColumn {

    public OracleColumn() {

    }

    public OracleColumn(String column) {
        mvColumnName = column;
    }

    public OracleColumn(String column, String typeName) {
        mvColumnName = column;
        mvTypeName = typeName;

    }


    @Override
    public String getTypeName() {
        if ((mvTypeName == null) || (mvTypeName.contains("("))) {
            return mvTypeName;
        }
        if (mvTypeName.equalsIgnoreCase("Date") || mvTypeName.equalsIgnoreCase("Blob") || mvTypeName.equalsIgnoreCase("SDO_GEOMETRY") || mvTypeName.toLowerCase().startsWith("timestamp")) {
            return mvTypeName;
        }

        if ((getColumnSize() != 0) && (getDecimalDigits() == 0)) {
            if (mvTypeName.equalsIgnoreCase("Varchar2")) {
                return mvTypeName + "(" + getColumnSize() + " CHAR)";
            }

            return mvTypeName + "(" + getColumnSize() + ")";
        }
        if ((getColumnSize() != 0) && (getDecimalDigits() != 0)) {
            return mvTypeName + "(" + getColumnSize() + "," + getDecimalDigits() + ")";
        }
        return mvTypeName;
    }


    @Override
    @SuppressWarnings("rawtypes")
    public Class getJavaDataType() {
        switch (getDatatype()) {
            case Types.DECIMAL:
            case Types.DOUBLE:
            case Types.FLOAT:
            case Types.NUMERIC:
                if (getDecimalDigits() != 0) {
                    return Double.class;
                }
                return Integer.class;

            case Types.DATE:
            case Types.TIMESTAMP:
                return Date.class;

            default:
                return String.class;

        }
    }


}
