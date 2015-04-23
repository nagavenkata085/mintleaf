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

import java.util.ArrayList;
import java.util.List;

public class SqlObjectMetaData {
    private final List<SqlColumn> mvcolumnsMetaData = new ArrayList<SqlColumn>();
    private String mvobjectName;

    public SqlObjectMetaData() {

    }

    public SqlObjectMetaData(String... columns) {
        for (String column : columns) {
            mvcolumnsMetaData.add(new SqlColumn(column));
        }
    }

    public void add(int idx, SqlColumn column) {
        mvcolumnsMetaData.add(idx, column);
    }

    public void add(SqlColumn column) {
        mvcolumnsMetaData.add(column);
    }

    public List<SqlColumn> getColumns() {
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

    public SqlColumn findColumn(String columnName) {
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
        for (SqlColumn mdata : mvcolumnsMetaData) {
            if (sb.length() != 0) {
                sb.append(", ");
            }
            sb.append(mdata.getColumnName());
        }
        return sb.toString();
    }
}
