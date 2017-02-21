/*
 * <!--
 *   ~
 *   ~ The MIT License (MIT)
 *   ~
 *   ~ Copyright (c) 2010-2017 Senthil Maruthaiappan
 *   ~
 *   ~ Permission is hereby granted, free of charge, to any person obtaining a copy
 *   ~ of this software and associated documentation files (the "Software"), to deal
 *   ~ in the Software without restriction, including without limitation the rights
 *   ~ to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   ~ copies of the Software, and to permit persons to whom the Software is
 *   ~ furnished to do so, subject to the following conditions:
 *   ~
 *   ~ The above copyright notice and this permission notice shall be included in all
 *   ~ copies or substantial portions of the Software.
 *   ~
 *   ~ THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   ~ IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   ~ FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *   ~ AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *   ~ LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   ~ OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *   ~ SOFTWARE.
 *   ~
 *   ~
 *   -->
 */

package org.qamatic.mintleaf.core;

import org.qamatic.mintleaf.*;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class BaseDbContext implements DbContext {

    private static final MintLeafLogger logger = MintLeafLogger.getLogger(BaseDbContext.class);
    private final DataSource dataSource;
    private DbSettings dbSettings;

    public BaseDbContext(DataSource datasource) {
        dataSource = datasource;
    }

    @Override
    public DataSource getDataSource() {
        return dataSource;
    }

    @Override
    public Connection getConnection() throws SQLException {

        return dataSource.getConnection();
    }

    public DbSettings getDbSettings() {
        return dbSettings;
    }

    public FluentJdbc newQuery() {
        return new FluentJdbc(dataSource);
    }


    public void setDbSettings(DbSettings dbSettings) {
        this.dbSettings = dbSettings;
    }

    public <T> List<T> query(String sql, final RowListener<T> listener) throws SQLException {

        final List<T> rows = new ArrayList<T>();

        FluentJdbc fluentJdbc = newQuery().createStatement(sql).query(new RowListener<T>() {
            @Override
            public T eachRow(int row, ResultSet resultSet) throws SQLException {
                rows.add(listener.eachRow(row, resultSet));
                return null;
            }
        }).close();
        return rows;
    }

    public int queryInt(String sql, Object[] whereClauseValues) {
        FluentJdbc fluentJdbc = null;
        try {
            fluentJdbc = newQuery().createStatement(sql).withParamValues(whereClauseValues).first();
            return fluentJdbc.getResultSet().getInt(1);

        } catch (SQLException e) {
            logger.error("getCount()", e);

        } finally {
            if (fluentJdbc != null) {
                fluentJdbc.close();
            }
        }
        return -1;
    }

    @Override
    public int getCount(String tableName, String whereClause, Object[] whereClauseValues) {

        String sql = "";
        if (whereClause != null) {
            sql = String.format("select count(*) from %s where %s", tableName, whereClause);
        } else {
            sql = String.format("select count(*) from %s", tableName);
        }

        return queryInt(sql, whereClauseValues);
    }

    @Override
    public boolean isSqlObjectExists(String objectName, String objectType, boolean ignoreValidity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getCount(String tableName) {
        return getCount(tableName, null, null);

    }

    @Override
    public boolean isTableExists(String tableName) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isdbFeatureExists(String featureName) {
        throw new UnsupportedOperationException();
    }


    @Override
    public void truncateTable(String tableName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isUserExists(String userName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<String> getSqlObjects(String objectType) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<String> getPrimaryKeys(String ownerName, String tableName) throws SQLException {
        throw new UnsupportedOperationException();
    }

    public DbMetaData getMetaData(String objectName) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isPrivilegeExists(String granteeName, String privilegeName, String objectName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isColumnExists(String tableName, String columnName) {
        throw new UnsupportedOperationException();
    }


}
