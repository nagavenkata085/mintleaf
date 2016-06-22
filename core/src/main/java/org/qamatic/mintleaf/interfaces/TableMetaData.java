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

import java.util.ArrayList;
import java.util.List;

public class TableMetaData {
    private final List<Column> mvcolumnsMetaData = new ArrayList<Column>();
    private String mvobjectName;

    public TableMetaData() {

    }

    public TableMetaData(Column... columns) {
        for (Column column : columns) {

            mvcolumnsMetaData.add(column);
        }
    }


    public void add(int idx, Column column) {
        mvcolumnsMetaData.add(idx, column);
    }

    public void add(Column column) {
        mvcolumnsMetaData.add(column);
    }

    public List<Column> getColumns() {
        return mvcolumnsMetaData;
    }

    public int getIndex(String columnName) {
        columnName = columnName.toUpperCase();
        // not important about performance for now, let it be sequential
        for (int i = 0; i < mvcolumnsMetaData.size(); i++) {
            if (mvcolumnsMetaData.get(i).getColumnName().equalsIgnoreCase(columnName)) {
                return i;
            }
        }
        return -1;
    }

    public Column findColumn(String columnName) {
        int idx = getIndex(columnName);
        if (idx != -1) {
            return mvcolumnsMetaData.get(idx);
        }
        return null;
    }

    public int size() {
        return mvcolumnsMetaData.size();
    }

    public String getObjectName() {
        return mvobjectName;
    }

    public void setObjectName(String objectName) {
        mvobjectName = objectName;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Column mdata : mvcolumnsMetaData) {
            if (sb.length() != 0) {
                sb.append(", ");
            }
            sb.append(mdata.getColumnName());
        }
        return sb.toString();
    }
}
