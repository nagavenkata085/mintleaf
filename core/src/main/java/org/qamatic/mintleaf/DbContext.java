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

package org.qamatic.mintleaf;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface DbContext {
    DataSource getDataSource();

    Connection getConnection() throws SQLException;

    DbSettings getDbSettings();

    void setDbSettings(DbSettings dbSettings);

    int getCount(String tableName, String whereClause, Object[] whereClauseValues);

    boolean isSqlObjectExists(String objectName, String objectType, boolean ignoreValidity);

    int getCount(String tableName);

    boolean isTableExists(String tableName);

    boolean isdbFeatureExists(String featureName);

    int getNextSequenceNumber(String sequenceName);

    void truncateTable(String tableName);

    boolean isUserExists(String userName);

    List<String> getSqlObjects(String objectType);

    List<String> getPrimaryKeys(String ownerName, String tableName);

    DbMetaData getMetaData(String objectName) throws SQLException;

    boolean isPrivilegeExists(String granteeName, String privilegeName, String objectName);

    boolean isColumnExists(String tableName, String columnName);

    boolean isSequenceExists(String sequenceName);
}
