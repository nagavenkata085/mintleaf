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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.qamatic.mintleaf.core.*;
import org.qamatic.mintleaf.interfaces.DbContext;
import org.qamatic.mintleaf.interfaces.SqlObject;
import org.qamatic.mintleaf.interfaces.SqlObjectMetaData;
import org.qamatic.mintleaf.oracle.junitsupport.OracleTestCase;
import org.qamatic.mintleaf.oracle.junitsupport.TestDatabase;
import org.qamatic.mintleaf.oracle.mocks.CreateTable1;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.*;

public class DbUtilityTest extends OracleTestCase {


    private MockTestPackage2 mvtestPackage;

    @Before
    public void init() throws SQLException, IOException {
        DbUtility utils = new DbUtility(getSchemaOwnerContext());
        utils.create();
        mvtestPackage = new MockTestPackage2(getSchemaOwnerContext());
        mvtestPackage.create();
    }

    @After
    public void cleanUp() {
        mvtestPackage.drop();
    }

    @Test
    public void testtruncateTable() throws SQLException, IOException {
        DbUtility utils = new DbUtility(getSchemaOwnerContext());
        PopulateTestDataForTable1 table1 = new PopulateTestDataForTable1(getSchemaOwnerContext());
        try {
            table1.dropAll();
            table1.createAll();

            DbAssert.assertCountEquals(2, getSchemaOwnerContext(), "table1");
            utils.truncateTable("TABLE1");
            assertEquals(0, getSchemaOwnerContext().getCount("TABLE1"));

        } finally {

            table1.dropAll();

        }
    }

    @Test
    public void testTriggerDrop() {

        BaseSqlTrigger trigger = new BaseSqlTrigger(getSchemaOwnerContext()) {
            @Override
            public String getName() {
                return "UFO";
            }
        };
        trigger.drop();
        assertTrue("exception must have thrown while dropping", true);

    }


    @Test
    public void testIsPackageExists() {
        DbUtility utils = new DbUtility(getSchemaOwnerContext());
        assertTrue("MockTestPackage2 package not found: ", utils.isPackageExists("MockTestPackage2"));
        assertTrue("MockTestPackage2 package not found: ", utils.isPackageExistsByName("MockTestPackage2"));
    }

    @SuppressWarnings("boxing")
    @Test
    public void testIsColumnExists() throws SQLException, IOException {
        DbUtility utils = new DbUtility(getSchemaOwnerContext());
        PopulateTestDataForTable1 table1 = new PopulateTestDataForTable1(getSchemaOwnerContext());
        try {
            table1.dropAll();
            table1.createAll();

            DbAssert.assertCountEquals(1, getSchemaOwnerContext(), "table1", "id=?", 2);
            assertTrue("Column Not Found: ", utils.isColumnExists("TABLE1", "ID"));

        } finally {

            table1.dropAll();

        }
    }

    @Test
    public void testCreateType() throws SQLException, IOException {
        DbUtility utils = new DbUtility(getSchemaOwnerContext());
        PopulateTestDataForTable1 table1 = new PopulateTestDataForTable1(getSchemaOwnerContext());
        try {
            table1.dropAll();
            table1.createAll();
            utils.createTypeFromTable("TE_TABLE1", null, "TABLE1");
            DbAssert.assertTypeExists(getSchemaOwnerContext(), "TE_TABLE1");

        } finally {

            //      table1.dropAll();

        }
    }

    @Test
    public void testCreateTypeFromSynonym() throws SQLException, IOException {
        DbUtility utils = new DbUtility(getSchemaOwnerContext());
        PopulateTestDataForTable1 table1 = new PopulateTestDataForTable1(getSchemaOwnerContext());
        JdbcTemplate template = new JdbcTemplate();
        template.setDataSource(getSchemaOwnerContext().getDataSource());
        try {
            table1.dropAll();
            table1.createAll();
            template.execute("CREATE SYNONYM SYN_TABLE1 FOR TABLE1");
            utils.createTypeFromTable("TE_TABLE1", null, "SYN_TABLE1");
            DbAssert.assertTypeExists(getSchemaOwnerContext(), "TE_TABLE1");

        } finally {

            table1.dropAll();

        }
    }

    @Test
    public void testgetobjectcolumnsdef() throws SQLException, IOException {
        DbUtility utils = new DbUtility(getSchemaOwnerContext());
        PopulateTestDataForTable1 table1 = new PopulateTestDataForTable1(getSchemaOwnerContext());
        JdbcTemplate template = new JdbcTemplate();
        template.setDataSource(getSchemaOwnerContext().getDataSource());
        try {
            table1.dropAll();
            table1.createAll();
            UtilityCommon utilityCommon = new UtilityCommon(getSchemaOwnerContext());
            SqlObjectMetaData metadata = utilityCommon.getObjectMetaData("TABLE1", true);
            assertEquals(0, metadata.getIndex("ID"));
            assertEquals(1, metadata.getIndex("NAME"));

        } finally {

            table1.dropAll();

        }
    }

    @Test
    public void testIsIndexExists() throws SQLException, IOException {
        PopulateTestDataForTable1 table1 = new PopulateTestDataForTable1(getSchemaOwnerContext());
        JdbcTemplate template = new JdbcTemplate();
        template.setDataSource(getSchemaOwnerContext().getDataSource());

        try {
            table1.dropAll();
            table1.createAll();
            template.execute("DROP INDEX TEST_IDX");
            template.execute("CREATE INDEX TEST_IDX ON TABLE1(ID)");
            DbAssert.assertIndexExists(getSchemaOwnerContext(), "TEST_IDX");

        } catch (Exception e) {

        } finally {

            table1.dropAll();

        }
    }

    @Test
    public void testIsIndexNotExists() throws SQLException, IOException {
        PopulateTestDataForTable1 table1 = new PopulateTestDataForTable1(getSchemaOwnerContext());
        JdbcTemplate template = new JdbcTemplate();
        template.setDataSource(getSchemaOwnerContext().getDataSource());

        try {
            table1.dropAll();
            table1.createAll();
            template.execute("DROP INDEX TEST_IDX");
            template.execute("CREATE INDEX TEST_IDX ON TABLE1(ID)");
            template.execute("DROP INDEX TEST_IDX");
            DbAssert.assertIndexNotExists(getSchemaOwnerContext(), "TEST_IDX");

        } catch (Exception e) {

        } finally {

            table1.dropAll();

        }
    }

    @Test
    public void testIsMViewExists() throws SQLException, IOException {
        PopulateTestDataForTable1 table1 = new PopulateTestDataForTable1(getSchemaOwnerContext());
        JdbcTemplate template = new JdbcTemplate();
        template.setDataSource(getSchemaOwnerContext().getDataSource());

        try {
            table1.dropAll();
            table1.createAll();
            template.execute("DROP MATERIALIZED VIEW MV_TABLE1");
            template.execute("CREATE OR REPLACE MATERIALZED VIEW MV_TABLE1 AS SELECT * FROM TABLE1");
            DbAssert.assertIndexExists(getSchemaOwnerContext(), "MV_TABLE1");

        } catch (Exception e) {

        } finally {

            table1.dropAll();

        }
    }

    @Test
    public void testIsDbFeatureValidFalse() {
        DbUtility utils = new DbUtility(TestDatabase.getSysDbaContext());
        assertFalse(utils.isdbFeatureValid("NONEXISTENT"));

    }

    @Test
    public void testgetContextParamValue() {
        DbUtility utils = new DbUtility(getSchemaOwnerContext());
        utils.getContextParamValue("USERENV", "SESSION_USER");
        assertEquals(getSchemaOwnerContext().getDbSettings().getUsername().toUpperCase(), utils.getContextParamValue("USERENV", "SESSION_USER"));
    }

    @Test
    public void testIsDbFeatureValidTrue() {

        DbUtility utils = new DbUtility(TestDatabase.getSysDbaContext());

        assertTrue(utils.isdbFeatureValid("CATALOG"));

    }

    @Test
    public void testIsPackageInterfaceExists() {
        DbUtility utils = new DbUtility(getSchemaOwnerContext());
        assertTrue("MockTestPackage2 package not found: ", utils.isPackageInterfaceExists("MockTestPackage2"));
    }

    @Test
    public void testIsDatabaseUserExists() {
        DbUtility utils = new DbUtility(getSchemaOwnerContext());
        assertTrue("DBWORKSUser user not found: ", utils.isDatabaseUserExists(getSchemaOwnerContext().getDbSettings().getUsername()));
    }

    @Test
    public void testIsPackageBodyExists() {
        DbUtility utils = new DbUtility(getSchemaOwnerContext());
        assertTrue("MockTestPackage2 package body not found: ", utils.isPackageBodyExists("MockTestPackage2"));
    }

    @Test
    public void testIsUserObjectExists() {
        DbUtility utils = new DbUtility(getSchemaOwnerContext());
        assertTrue("MockTestPackage2 package not found: ", utils.isUserObjectExists("MockTestPackage2", "PACKAGE"));
    }

    @Test
    public void testGetCountCase1() throws SQLException, IOException {
        PopulateTestDataForTable1 table1 = new PopulateTestDataForTable1(getSchemaOwnerContext());

        try {
            table1.dropAll();
            table1.createAll();

            DbAssert.assertCountEquals(2, getSchemaOwnerContext(), "table1");

        } finally {

            table1.dropAll();

        }

    }

    @SuppressWarnings("boxing")
    @Test
    public void testGetCountCase2() throws SQLException, IOException {
        PopulateTestDataForTable1 table1 = new PopulateTestDataForTable1(getSchemaOwnerContext());

        try {
            table1.dropAll();
            table1.createAll();

            DbAssert.assertCountEquals(1, getSchemaOwnerContext(), "table1", "id=?", 2);

        } finally {

            table1.dropAll();

        }

    }

    @Test
    public void testGetNextSequenceNumber() throws SQLException, IOException {
        DbUtility utils = new DbUtility(getSchemaOwnerContext());
        JdbcTemplate template = new JdbcTemplate();
        template.setDataSource(getSchemaOwnerContext().getDataSource());
        try {
            template.execute("DROP SEQUENCE TESTSEQUENCE");
        } catch (Exception e) {

        }

        template.execute("CREATE SEQUENCE TESTSEQUENCE INCREMENT BY 1 START WITH 998");
        DbAssert.assertSequenceExists(getSchemaOwnerContext(), "TESTSEQUENCE");
        assertEquals(998, utils.getNextSequenceNumber("TESTSEQUENCE"));
    }

    @Test
    public void testIsDependencyPackageExists() throws SQLException, IOException {
        DbUtility utils = new DbUtility(getSchemaOwnerContext());

        DependencyExistsTestPackage pkg = new DependencyExistsTestPackage(getSchemaOwnerContext());
        pkg.dropAll();
        Class<? extends SqlObject>[] dependencies = SqlObjectHelper.getDependencyItems(pkg, OraclePackage.class);

        assertEquals(1, dependencies.length);
        assertEquals(TestLog.class, dependencies[0]);

        assertFalse(utils.isDependencyPackageExists(pkg));
        pkg.createAll();
        assertTrue(utils.isDependencyPackageExists(pkg));

    }

    @Test
    public void testGetUserObjectList() {
        DbUtility utils = new DbUtility(getSchemaOwnerContext());
        boolean dblibPackageFound = false;
        List<String> list = utils.getSqlObjects("PACKAGE");
        for (String string : list) {
            if (string.equals("DBUTILITY")) {
                dblibPackageFound = true;
                break;
            }
        }

        assertTrue("getSqlObjects does not return DbUtility", dblibPackageFound);
    }

    @Test
    public void testGetPrimaryKeys() throws SQLException, IOException {
        new ExecuteQuery().loadFromSectionalFile(getSchemaOwnerContext(), "/examples/typeobjectexample_usingtable_sec.sql", new String[]{"drop person table",
                "create person table"});
        try {
            DbUtility utils = new DbUtility(getSchemaOwnerContext());
            List<String> keys = utils.getPrimaryKeys("person");
            assertEquals("ID", keys.get(0));

        } finally {
            new ExecuteQuery()
                    .loadFromSectionalFile(getSchemaOwnerContext(), "/examples/typeobjectexample_usingtable_sec.sql", new String[]{"drop person table"});
        }

    }

    @SqlObjectInfo(name = "few test data", source = "/AddDataToTable2Count5.sql")
    @SqlObjectDependsOn(Using = CreateTable1.class)
    public static class PopulateTestDataForTable2 extends BaseSqlObject {

        public PopulateTestDataForTable2(DbContext context) {
            super(context);

        }
    }

    @SqlObjectInfo(name = "MockTestPackage2", source = "/MockTestPackage2.sql")
    @SqlObjectDependsOn(Using = {TestLog.class, CreateTable1.class})
    public static class DependencyExistsTestPackage extends OraclePackage {
        public DependencyExistsTestPackage(DbContext context) {
            super(context);

        }

    }

    @SqlObjectInfo(name = "few test data", source = "/AddDataToTable1Count2.sql", sourceDelimiter = ";")
    @SqlObjectDependsOn(Using = CreateTable1.class)
    public class PopulateTestDataForTable1 extends BaseSqlObject {

        public PopulateTestDataForTable1(DbContext context) {
            super(context);

        }
    }

    @SqlObjectInfo(name = "MockTestPackage2", source = "/MockTestPackage2.sql")
    public class MockTestPackage2 extends OraclePackage {
        public MockTestPackage2(DbContext context) {
            super(context);

        }

    }

    @SqlObjectInfo(name = "TestLog", source = "/TestLog.sql")
    public class TestLog extends OraclePackage {

        public TestLog(DbContext context) {
            super(context);
        }
    }

}
