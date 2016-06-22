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

package org.qamatic.mintleaf.interfaces;

public class Column {
    protected String mvColumnName;
    protected String mvTypeName;
    protected int mvDatatype;
    protected boolean mvNullable;
    protected int mvColumnSize;
    protected int mvDecimalDigits;
    protected boolean mvCalculated;
    protected boolean mvIgnoreForTypeObjectCreation;


    public String getColumnName() {
        return mvColumnName;
    }

    public void setColumnName(String columnName) {
        mvColumnName = columnName;
    }

    public String getTypeName() {
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

        return String.class;

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
