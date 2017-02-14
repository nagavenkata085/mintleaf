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

package org.qamatic.mintleaf.oracle;

import org.qamatic.mintleaf.DbContext;

import org.qamatic.mintleaf.dbs.oracle.OracleDbContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class OracleDbAssert {


    public static void assertPackageBodyExists(OracleDbContext context, String pkgName) throws SQLException, IOException {
        assertTrue("PL/Sql package body Invalid or not found: " + pkgName, context.isPackageBodyExists(pkgName, false));
    }


    public static void assertPackageExists(OracleDbContext context, String pkgName) throws SQLException, IOException {

        assertTrue("PL/Sql package Invalid or not found: " + pkgName, context.isPackageExists(pkgName, false));
    }

    public static void assertSequenceExists(OracleDbContext context, String sequenceName) throws SQLException, IOException {

        assertTrue("Sequence Exists: " + sequenceName, context.isSequenceExists(sequenceName));
    }

  
    public static void assertPackageNotExists(OracleDbContext context, String pkgName) throws SQLException, IOException {

        assertFalse("PL/Sql package found: " + pkgName, context.isPackageExists(pkgName, false));
    }

    public static void assertPackageInterfaceExists(OracleDbContext context, String pkgName) throws SQLException, IOException {

        assertTrue("PL/Sql package found: " + pkgName, context.isPackageExists(pkgName, false));
    }


    public static void assertTypeBodyExists(OracleDbContext context, String typeName) {
        assertTrue("Schema Type Invalid or not found: " + typeName, context.isTypeBodyExists(typeName, false));
    }


    public static void assertTypeBodyNotExists(OracleDbContext context, String typeName) {

        assertFalse("Schema Type Invalid or not found: " + typeName, context.isTypeBodyExists(typeName, false));
    }


    public static void assertTypeExists(OracleDbContext context, String typeName) {
        assertTrue("Schema Type Invalid or not found: " + typeName, context.isTypeExists(typeName, false));
    }


    public static void assertTypeNotExists(OracleDbContext context, String typeName) {

        assertFalse("Schema Type found: " + typeName, context.isTypeExists(typeName, false));
    }

    public static void assertSynonymExists(OracleDbContext context, String SynonymName) {
        assertTrue("Schema Synonym Invalid or not found: " + SynonymName, context.isSynonymExists(SynonymName, false));
    }


    public static void assertSynonymNotExists(OracleDbContext context, String SynonymName) {

        assertFalse("Schema Synonym found: " + SynonymName, context.isSynonymExists(SynonymName, false));
    }


    public static void assertTableExists(DbContext context, String tableName) throws SQLException {
        assertTrue("Table Invalid or not found: " + tableName, context.isTableExists(tableName));
    }

    public static void assertMViewExists(DbContext context, String mvName) {
        assertTrue("Materialized View Not Found: " + mvName, context.isSqlObjectExists(mvName, "MATERIALIZED VIEW", false));
    }

    public static void assertIndexExists(DbContext context, String indexName) {
        assertTrue("Index Not Found: " + indexName, context.isSqlObjectExists(indexName, "INDEX", false));
    }

    public static void assertColumnExists(DbContext context, String tableName, String columnName) {
        assertTrue("Column Invalid or not found: " + tableName + ":" + columnName, context.isColumnExists(tableName, columnName));
    }

    public static void assertTableNotExists(DbContext context, String tableName) {
        assertFalse("Table  found: " + tableName, context.isSqlObjectExists(tableName, "TABLE", false));
    }

    public static void assertCountEquals(int expectedValue, DbContext context, String tableName, String whereClause, Object... whereClauseValues) {
        int actual = context.getCount(tableName, whereClause, whereClauseValues);
        assertEquals(expectedValue, actual);

    }

    public static void assertCountEquals(int expectedValue, DbContext context, String tableName) {
        int actual = context.getCount(tableName, null, null);
        assertEquals(expectedValue, actual);

    }


    public static void assertTriggerExists(OracleDbContext context, String triggerName) {
        assertTrue("Trigger Invalid or not found: " + triggerName, context.isTriggerExists(triggerName, false));
    }

    public static void assertTriggerNotExists(OracleDbContext context, String triggerName) {
        assertFalse("Trigger found: " + triggerName, context.isTriggerExists(triggerName, false));
    }


    public static void assertViewExists(DbContext context, String viewName) {

        assertTrue("View Invalid or not found: " + viewName, context.isSqlObjectExists(viewName, "VIEW", false));
    }

    public static void assertViewNotExists(DbContext context, String viewName) {

        assertFalse("View found: " + viewName, context.isSqlObjectExists(viewName, "VIEW", false));
    }

    public static void assertIndexNotExists(DbContext context, String indexName) {

        assertFalse("Index found: " + indexName, context.isSqlObjectExists(indexName, "INDEX", false));
    }

    public static void assertPrivilegeExists(DbContext context, String granteeName, String privilegeName, String objectName) {

        assertTrue("Privilege not granted on " + privilegeName, context.isPrivilegeExists(granteeName, privilegeName, objectName));
    }

    public static void assertPrivilegeExists(DbContext context, String granteeName, String[] privileges, String objectName) {

        StringBuilder notFound = new StringBuilder();

        for (String privilege : privileges) {
            if (!context.isPrivilegeExists(granteeName, privilege, objectName)) {
                notFound.append(privilege);
            }
        }

        if (notFound.length() == 0) {
            assertTrue(true);
            return;
        }

        assertFalse("Privilege not granted on: " + notFound.toString(), true);
    }

    @SuppressWarnings("unchecked")
    public static void assertQuery(DbContext context, String sqlQuery, @SuppressWarnings("rawtypes") RowMapper rowMapper) {
        JdbcTemplate template = new JdbcTemplate(context.getDataSource());
        template.query(sqlQuery, rowMapper);
    }
}
