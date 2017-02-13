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

import org.qamatic.mintleaf.DbContext;
import org.qamatic.mintleaf.DbSettings;
import org.qamatic.mintleaf.DbMetaData;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;


public class AbstractDbContext implements DbContext {

    private final DataSource dataSource;
    private DbSettings dbSettings;

    public AbstractDbContext(DataSource datasource) {
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


    public void setDbSettings(DbSettings dbSettings) {
        this.dbSettings = dbSettings;
    }

    @Override
    public int getCount(String tableName, String whereClause, Object[] whereClauseValues) {
        JdbcTemplate template = new JdbcTemplate(getDataSource());

        String sql = "";
        if (whereClause != null) {
            sql = String.format("select count(*) from %s where %s", tableName, whereClause);
        } else {
            sql = String.format("select count(*) from %s", tableName);
        }
        return template.queryForInt(sql, whereClauseValues);
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
    public List<String> getPrimaryKeys(String ownerName, String tableName) {
        throw new UnsupportedOperationException();
    }

    public DbMetaData getMetaData(String objectName) throws SQLException{
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
