/*
 * Copyright {2011-2015} Senthil Maruthaiappan
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package org.qamatic.mintleaf.interfaces;

import java.sql.Date;
import java.sql.Types;

public class SqlColumn {
    private String mvColumnName;
    private String mvTypeName;
    private int mvDatatype;
    private boolean mvNullable;
    private int mvColumnSize;
    private int mvDecimalDigits;
    private boolean mvCalculated;
    private boolean mvIgnoreForTypeObjectCreation;

    public SqlColumn() {

    }

    public SqlColumn(String column) {
        mvColumnName = column;
    }

    public SqlColumn(String column, String typeName) {
        mvColumnName = column;
        mvTypeName = typeName;

    }

    public String getColumnName() {
        return mvColumnName;
    }

    public void setColumnName(String columnName) {
        mvColumnName = columnName;
    }

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

    public void setTypeName(String dataType) {
        mvTypeName = dataType;
    }

    public int getDatatype() {
        return mvDatatype;
    }

    public void setDatatype(int dataType) {
        mvDatatype = dataType;
    }

    public boolean isNullable() {
        return mvNullable;
    }

    public void setNullable(boolean nullable) {
        mvNullable = nullable;
    }

    public void setNullable(int nullable) {
        mvNullable = nullable == 1;
    }

    public int getColumnSize() {
        return mvColumnSize;
    }

    public void setColumnSize(int columnSize) {
        mvColumnSize = columnSize;
    }

    public int getDecimalDigits() {
        return mvDecimalDigits;
    }

    public void setDecimalDigits(int decimalDigits) {
        mvDecimalDigits = decimalDigits;
    }

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

    public boolean isCalculated() {

        return mvCalculated;
    }

    public void setCalculated(boolean calculated) {
        mvCalculated = calculated;
    }

    public boolean isIgnoreForTypeObjectCreation() {
        return mvIgnoreForTypeObjectCreation;
    }

    public void setIgnoreForTypeObjectCreation(boolean ignoreForTypeObjectCreation) {
        mvIgnoreForTypeObjectCreation = ignoreForTypeObjectCreation;
    }

}
