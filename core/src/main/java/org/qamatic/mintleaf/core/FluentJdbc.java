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

import org.qamatic.mintleaf.MintLeafLogger;
import org.qamatic.mintleaf.RowListener;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Calendar;

/**
 * Created by senips on 2/20/17.
 */
public class FluentJdbc {

    private static final MintLeafLogger logger = MintLeafLogger.getLogger(FluentJdbc.class);
    private DataSource dataSource;

    private Connection connection;
    private PreparedStatement statement;
    private ResultSet resultSet;
    private String sql;

    public FluentJdbc(DataSource dataSource) {

        this.dataSource = dataSource;
    }

    public FluentJdbc createStatement(final String sql) throws SQLException {
        if (statement != null) {
            close();
        }
        connection = dataSource.getConnection();
        statement = connection.prepareStatement(sql);
        this.sql = sql;
        return this;
    }

    public PreparedStatement getStatement() {
        return this.statement;
    }

    public ResultSet getResultSet() throws SQLException {
        if (this.resultSet == null) {
            executeQuery();
        }
        return this.resultSet;
    }


    public FluentJdbc close() {
        try {


            if (this.resultSet != null) {
                this.resultSet.close();
            }
            if (this.statement != null) {
                this.statement.close();
            }
            if (this.connection != null) {
                this.connection.close();
            }
            this.resultSet = null;
            this.connection = null;
            this.statement = null;
        } catch (SQLException se) {
            logger.error("FluentJdbc close()", se);
        } finally {
            return this;
        }
    }


    public FluentJdbc executeQuery() throws SQLException {
        this.resultSet = statement.executeQuery();
        return this;
    }


//    public int executeUpdate() throws SQLException {
//        return 0;
//    }

    public FluentJdbc setNull(int parameterIndex, int sqlType) throws SQLException {
        statement.setNull(parameterIndex, sqlType);
        return this;
    }


    public FluentJdbc setInt(int parameterIndex, int x) throws SQLException {
        statement.setInt(parameterIndex, x);
        return this;
    }


    public FluentJdbc setString(int parameterIndex, String x) throws SQLException {
        statement.setString(parameterIndex, x);
        return this;
    }


    public FluentJdbc clearParameters() throws SQLException {
        statement.clearParameters();
        return this;
    }


    public FluentJdbc setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException {
        statement.setObject(parameterIndex, x, targetSqlType);
        return this;
    }


    public FluentJdbc setObject(int parameterIndex, Object x) throws SQLException {
        statement.setObject(parameterIndex, x);
        return this;
    }


    public FluentJdbc execute() throws SQLException {
        logger.info(sql);
        statement.execute();
        return this;
    }


    public FluentJdbc setDate(int parameterIndex, Date x, Calendar cal) throws SQLException {
        statement.setDate(parameterIndex, x, cal);
        return this;
    }


    public FluentJdbc setTime(int parameterIndex, Time x, Calendar cal) throws SQLException {
        statement.setTime(parameterIndex, x, cal);
        return this;
    }


    public FluentJdbc setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException {
        statement.setTimestamp(parameterIndex, x, cal);
        return this;
    }

    public FluentJdbc withParamValues(Object[] bindingParams) throws SQLException {

        if (bindingParams == null)
            return this;
        for (int i = 0; i < bindingParams.length; i++) {
            if (bindingParams[i] instanceof Integer) {
                setInt(i + 1, (Integer) bindingParams[i]);
            } else {
                setString(i + 1, (String) bindingParams[i]);
            }
        }

        return this;
    }

//    public <T> FluentJdbc query(final String sql, final RowListener<T> listener) throws SQLException {
//        createStatement(sql);
//        return query(listener);
//    }

    public FluentJdbc first() throws SQLException {
        getResultSet().next();
        return this;
    }

    public <T> FluentJdbc query(final RowListener<T> listener) throws SQLException {

        try {
            int i = 0;
            while (getResultSet().next()) {
                if (i == 0) {
                    listener.firstRow(getResultSet());
                }
                listener.eachRow(i++, getResultSet());
            }
        } finally {

        }
        return this;
    }
}
