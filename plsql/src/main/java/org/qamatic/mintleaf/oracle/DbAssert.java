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

package org.qamatic.mintleaf.oracle;

import org.qamatic.mintleaf.interfaces.DbContext;
import org.qamatic.mintleaf.interfaces.SqlPackage;
import org.qamatic.mintleaf.interfaces.SqlTrigger;
import org.qamatic.mintleaf.interfaces.SqlTypeObject;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;

public class DbAssert {

    public static void assertPackageBodyExists(SqlPackage pkg) throws SQLException, IOException {
        assertPackageBodyExists(pkg.getDbContext(), pkg.getName());
    }

    public static void assertPackageBodyExists(DbContext context, String pkgName) throws SQLException, IOException {
        DbUtility utils = new DbUtility(context);
        utils.create();
        assertTrue("PL/Sql package body Invalid or not found: " + pkgName, utils.isPackageBodyExists(pkgName));
    }

    public static void assertPackageExists(SqlPackage pkg) throws SQLException, IOException {
        assertPackageExists(pkg.getDbContext(), pkg.getName());
    }

    public static void assertPackageExists(DbContext context, String pkgName) throws SQLException, IOException {
        DbUtility utils = new DbUtility(context);
        utils.create();
        assertTrue("PL/Sql package Invalid or not found: " + pkgName, utils.isPackageInterfaceExists(pkgName));
    }

    public static void assertSequenceExists(DbContext context, String sequenceName) throws SQLException, IOException {
        DbUtility utils = new DbUtility(context);
        utils.create();
        assertTrue("Sequence Exists: " + sequenceName, utils.isSequenceExists(sequenceName));
    }

    public static void assertPackageNotExists(SqlPackage pkg) throws SQLException, IOException {
        assertPackageNotExists(pkg.getDbContext(), pkg.getName());
    }

    public static void assertPackageNotExists(DbContext context, String pkgName) throws SQLException, IOException {
        DbUtility utils = new DbUtility(context);
        utils.create();
        assertFalse("PL/Sql package found: " + pkgName, utils.isPackageInterfaceExists(pkgName));
    }

    public static void assertPackageInterfaceExists(DbContext context, String pkgName) throws SQLException, IOException {
        DbUtility utils = new DbUtility(context);
        utils.create();
        assertTrue("PL/Sql package found: " + pkgName, utils.isPackageInterfaceExists(pkgName));
    }

    public static void assertTypeBodyExists(DbContext context, String typeName) {
        DbUtility utils = new DbUtility(context);
        assertTrue("Schema Type Invalid or not found: " + typeName, utils.isTypeBodyExists(typeName));
    }

    public static void assertTypeBodyExists(SqlTypeObject aTypeObject) {
        assertTypeBodyExists(aTypeObject.getDbContext(), aTypeObject.getName());

    }

    public static void assertTypeBodyNotExists(DbContext context, String typeName) {
        DbUtility utils = new DbUtility(context);
        assertFalse("Schema Type Invalid or not found: " + typeName, utils.isTypeBodyExists(typeName));
    }

    public static void assertTypeBodyNotExists(SqlTypeObject aTypeObject) {
        assertTypeBodyNotExists(aTypeObject.getDbContext(), aTypeObject.getName());

    }

    public static void assertTypeExists(DbContext context, String typeName) {
        DbUtility utils = new DbUtility(context);
        assertTrue("Schema Type Invalid or not found: " + typeName, utils.isTypeExists(typeName));
    }

    public static void assertTypeExists(SqlTypeObject aTypeObject) {
        assertTypeExists(aTypeObject.getDbContext(), aTypeObject.getName());

    }

    public static void assertTypeNotExists(DbContext context, String typeName) {
        DbUtility utils = new DbUtility(context);
        assertFalse("Schema Type found: " + typeName, utils.isTypeExists(typeName));
    }

    public static void assertTypeNotExists(SqlTypeObject aTypeObject) {
        assertTypeNotExists(aTypeObject.getDbContext(), aTypeObject.getName());
    }

    public static void assertSynonymExists(DbContext context, String SynonymName) {
        DbUtility utils = new DbUtility(context);
        assertTrue("Schema Synonym Invalid or not found: " + SynonymName, utils.isSynonymExists(SynonymName));
    }

    public static void assertSynonymExists(SqlTypeObject aSynonymObject) {
        assertSynonymExists(aSynonymObject.getDbContext(), aSynonymObject.getName());

    }

    public static void assertSynonymNotExists(DbContext context, String SynonymName) {
        DbUtility utils = new DbUtility(context);
        assertFalse("Schema Synonym found: " + SynonymName, utils.isSynonymExists(SynonymName));
    }

    public static void assertSynonymNotExists(SqlTypeObject aSynonymObject) {
        assertSynonymNotExists(aSynonymObject.getDbContext(), aSynonymObject.getName());
    }

    public static void assertTableExists(DbContext context, String tableName) {
        DbUtility utils = new DbUtility(context);
        assertTrue("Table Invalid or not found: " + tableName, utils.isTableExists(tableName));
    }

    public static void assertMViewExists(DbContext context, String mvName) {
        DbUtility utils = new DbUtility(context);
        assertTrue("Materialized View Not Found: " + mvName, utils.isUserObjectExists(mvName, "MATERIALIZED VIEW"));
    }

    public static void assertIndexExists(DbContext context, String indexName) {
        DbUtility utils = new DbUtility(context);
        assertTrue("Index Not Found: " + indexName, utils.isUserObjectExists(indexName, "INDEX"));
    }

    public static void assertColumnExists(DbContext context, String tableName, String columnName) {
        DbUtility utils = new DbUtility(context);
        assertTrue("Column Invalid or not found: " + tableName + ":" + columnName, utils.isColumnExists(tableName, columnName));
    }

    public static void assertTableNotExists(DbContext context, String tableName) {
        DbUtility utils = new DbUtility(context);
        assertFalse("Table  found: " + tableName, utils.isUserObjectExists(tableName, "TABLE"));
    }

    public static void assertCountEquals(int expectedValue, DbContext context, String tableName, String whereClause, Object... whereClauseValues) {
        int actual = context.getCount(tableName, whereClause, whereClauseValues);
        assertEquals(expectedValue, actual);

    }

    public static void assertCountEquals(int expectedValue, DbContext context, String tableName) {
        int actual = context.getCount(tableName, null, null);
        assertEquals(expectedValue, actual);

    }

    public static void assertParamValueEquals(String expectedValue, DbContext context, String namespace, String paramName) {
        DbUtility utils = new DbUtility(context);
        String actual = utils.getContextParamValue(namespace, paramName);
        assertEquals(expectedValue, actual);
    }

    public static void assertTriggerExists(DbContext context, String triggerName) {
        DbUtility utils = new DbUtility(context);
        assertTrue("Trigger Invalid or not found: " + triggerName, utils.isTriggerExists(triggerName));
    }

    public static void assertTriggerExists(SqlTrigger trigger) {
        assertTriggerExists(trigger.getDbContext(), trigger.getName());
    }

    public static void assertTriggerNotExists(DbContext context, String triggerName) {
        DbUtility utils = new DbUtility(context);
        assertFalse("Trigger found: " + triggerName, utils.isTriggerExists(triggerName));
    }

    public static void assertTriggerNotExists(SqlTrigger trigger) {
        assertTriggerNotExists(trigger.getDbContext(), trigger.getName());
    }

    public static void assertViewExists(DbContext context, String viewName) {
        DbUtility utils = new DbUtility(context);
        assertTrue("View Invalid or not found: " + viewName, utils.isUserObjectExists(viewName, "VIEW"));
    }

    public static void assertViewNotExists(DbContext context, String viewName) {
        DbUtility utils = new DbUtility(context);
        assertFalse("View found: " + viewName, utils.isUserObjectExists(viewName, "VIEW"));
    }

    public static void assertDateDifference(Date beforeDate, Date afterDate, int difference) {
        Calendar seedCalander = Calendar.getInstance();
        seedCalander.setTime(beforeDate);
        Calendar resultCalander = Calendar.getInstance();
        resultCalander.setTime(afterDate);
        int noOfDays = 0;
        while (seedCalander.before(resultCalander)) {
            seedCalander.add(Calendar.DAY_OF_MONTH, 1);
            ++noOfDays;
        }
        assertTrue(difference == noOfDays);
    }

    public static void assertIndexNotExists(DbContext context, String indexName) {
        DbUtility utils = new DbUtility(context);
        assertFalse("Index found: " + indexName, utils.isUserObjectExists(indexName, "INDEX"));
    }

    public static void assertPrivilegeExists(DbContext context, String granteeName, String privilegeName, String objectName) {
        DbUtility utils = new DbUtility(context);
        assertTrue("Privilege not granted on " + privilegeName, utils.isPrivilegeExists(granteeName, privilegeName, objectName));
    }

    public static void assertPrivilegeExists(DbContext context, String granteeName, String[] privileges, String objectName) {
        DbUtility utils = new DbUtility(context);
        StringBuilder notFound = new StringBuilder();

        for (String privilege : privileges) {
            if (!utils.isPrivilegeExists(granteeName, privilege, objectName)) {
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
