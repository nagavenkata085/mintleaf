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

package org.qamatic.mintleaf.oracle;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.qamatic.mintleaf.core.*;
import org.qamatic.mintleaf.dbsupportimpls.oracle.OracleDbAssert;
import org.qamatic.mintleaf.interfaces.DbContext;
import org.qamatic.mintleaf.oracle.core.SqlObjectInfo;
import org.qamatic.mintleaf.oracle.core.SqlScriptObject;
import org.qamatic.mintleaf.interfaces.DbMetaData;
import org.qamatic.mintleaf.dbsupportimpls.oracle.OracleDbContext;
import org.qamatic.mintleaf.oracle.core.BaseSqlScriptObject;
import org.qamatic.mintleaf.oracle.junitsupport.OracleTestCase;
import org.qamatic.mintleaf.oracle.junitsupport.OracleTestDatabase;
import org.qamatic.mintleaf.oracle.mocks.CreateTable1;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.*;

public class OracleDbHelperTest extends OracleTestCase {


    private MockTestPackage2 mvtestPackage;

    @Before
    public void init() throws SQLException, IOException {
        OracleHelperScript utils = new OracleHelperScript(getSchemaOwnerContext());
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
        OracleHelperScript utils = new OracleHelperScript(getSchemaOwnerContext());
        PopulateTestDataForTable1 table1 = new PopulateTestDataForTable1(getSchemaOwnerContext());
        try {
            table1.dropAll();
            table1.createAll();

            OracleDbAssert.assertCountEquals(2, getSchemaOwnerContext(), "table1");
            getSchemaOwnerContext().truncateTable("TABLE1");
            assertEquals(0, getSchemaOwnerContext().getCount("TABLE1"));

        } finally {

            table1.dropAll();

        }
    }

    protected OracleDbContext getOracleDbContext() {
        return (OracleDbContext) getSchemaOwnerContext();
    }


    @Test
    public void testIsPackageExists() {
        assertTrue("MockTestPackage2 package not found: ", getOracleDbContext().isPackageExists("MockTestPackage2", false));
    }

    @SuppressWarnings("boxing")
    @Test
    public void testIsColumnExists() throws SQLException, IOException {
        OracleHelperScript utils = new OracleHelperScript(getSchemaOwnerContext());
        PopulateTestDataForTable1 table1 = new PopulateTestDataForTable1(getSchemaOwnerContext());
        try {
            table1.dropAll();
            table1.createAll();

            OracleDbAssert.assertCountEquals(1, getSchemaOwnerContext(), "table1", "id=?", 2);
            assertTrue("Column Not Found: ", getSchemaOwnerContext().isColumnExists("TABLE1", "ID"));

        } finally {

            table1.dropAll();

        }
    }

    @Test
    public void testCreateType() throws SQLException, IOException {
        OracleHelperScript utils = new OracleHelperScript(getSchemaOwnerContext());
        PopulateTestDataForTable1 table1 = new PopulateTestDataForTable1(getSchemaOwnerContext());
        try {
            table1.dropAll();
            table1.createAll();
            utils.createTypeFromTable("TE_TABLE1", null, "TABLE1");
            OracleDbAssert.assertTypeExists(getOracleDbContext(), "TE_TABLE1");

        } finally {

            //      table1.dropAll();

        }
    }

    @Test
    public void testCreateTypeFromSynonym() throws SQLException, IOException {
        OracleHelperScript utils = new OracleHelperScript(getSchemaOwnerContext());
        PopulateTestDataForTable1 table1 = new PopulateTestDataForTable1(getSchemaOwnerContext());
        JdbcTemplate template = new JdbcTemplate();
        template.setDataSource(getSchemaOwnerContext().getDataSource());
        try {
            table1.dropAll();
            table1.createAll();
            template.execute("CREATE SYNONYM SYN_TABLE1 FOR TABLE1");
            utils.createTypeFromTable("TE_TABLE1", null, "SYN_TABLE1");
            OracleDbAssert.assertTypeExists(getOracleDbContext(), "TE_TABLE1");

        } finally {

            table1.dropAll();

        }
    }

    @Test
    public void testgetobjectcolumnsdef() throws SQLException, IOException {
        OracleHelperScript utils = new OracleHelperScript(getSchemaOwnerContext());
        PopulateTestDataForTable1 table1 = new PopulateTestDataForTable1(getSchemaOwnerContext());
        JdbcTemplate template = new JdbcTemplate();
        template.setDataSource(getSchemaOwnerContext().getDataSource());
        try {
            table1.dropAll();
            table1.createAll();
            DbMetaData metadata = getSchemaOwnerContext().getMetaData("TABLE1");
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
            OracleDbAssert.assertIndexExists(getSchemaOwnerContext(), "TEST_IDX");

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
            OracleDbAssert.assertIndexNotExists(getSchemaOwnerContext(), "TEST_IDX");

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
            OracleDbAssert.assertIndexExists(getSchemaOwnerContext(), "MV_TABLE1");

        } catch (Exception e) {

        } finally {

            table1.dropAll();

        }
    }

    @Test
    public void testIsDbFeatureValidFalse() {

        assertFalse(OracleTestDatabase.getSysDbaContext().isdbFeatureExists("NONEXISTENT"));

    }

    @Test
    public void testIsDbFeatureValidTrue() {


        assertTrue(OracleTestDatabase.getSysDbaContext().isdbFeatureExists("CATALOG"));

    }

    @Test
    public void testIsPackageInterfaceExists() {
        assertTrue("MockTestPackage2 package not found: ", getOracleDbContext().isPackageExists("MockTestPackage2", false));
    }

    @Test
    public void testIsDatabaseUserExists() {
        OracleHelperScript utils = new OracleHelperScript(getSchemaOwnerContext());
        assertTrue("DBWORKSUser user not found: ", getSchemaOwnerContext().isUserExists(getSchemaOwnerContext().getDbSettings().getUsername()));
    }

    @Test
    public void testIsPackageBodyExists() {
        OracleHelperScript utils = new OracleHelperScript(getSchemaOwnerContext());
        assertTrue("MockTestPackage2 package body not found: ", getOracleDbContext().isPackageBodyExists("MockTestPackage2", false));
    }

    @Test
    public void testIsUserObjectExists() {
        OracleHelperScript utils = new OracleHelperScript(getSchemaOwnerContext());
        assertTrue("MockTestPackage2 package not found: ", getSchemaOwnerContext().isSqlObjectExists("MockTestPackage2", "PACKAGE", false));
    }

    @Test
    public void testGetCountCase1() throws SQLException, IOException {
        PopulateTestDataForTable1 table1 = new PopulateTestDataForTable1(getSchemaOwnerContext());

        try {
            table1.dropAll();
            table1.createAll();

            OracleDbAssert.assertCountEquals(2, getSchemaOwnerContext(), "table1");

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

            OracleDbAssert.assertCountEquals(1, getSchemaOwnerContext(), "table1", "id=?", 2);

        } finally {

            table1.dropAll();

        }

    }

    @Test
    public void testGetNextSequenceNumber() throws SQLException, IOException {
        OracleHelperScript utils = new OracleHelperScript(getSchemaOwnerContext());
        JdbcTemplate template = new JdbcTemplate();
        template.setDataSource(getSchemaOwnerContext().getDataSource());
        try {
            template.execute("DROP SEQUENCE TESTSEQUENCE");
        } catch (Exception e) {

        }

        template.execute("CREATE SEQUENCE TESTSEQUENCE INCREMENT BY 1 START WITH 998");
        OracleDbAssert.assertSequenceExists((OracleDbContext) getSchemaOwnerContext(), "TESTSEQUENCE");
        assertEquals(998, getSchemaOwnerContext().getNextSequenceNumber("TESTSEQUENCE"));
    }

    @Test
    public void testIsDependencyPackageExists() throws SQLException, IOException {
        OracleHelperScript utils = new OracleHelperScript(getSchemaOwnerContext());

        DependencyExistsTestPackage pkg = new DependencyExistsTestPackage(getSchemaOwnerContext());
        pkg.dropAll();
        Class<? extends SqlScriptObject>[] dependencies = SqlObjectHelper.getDependencyItems(pkg, OraclePackage.class);

        assertEquals(1, dependencies.length);
        assertEquals(TestLog.class, dependencies[0]);

        assertFalse(utils.isDependencyPackageExists(pkg));
        pkg.createAll();
        assertTrue(utils.isDependencyPackageExists(pkg));

    }

    @Test
    public void testGetUserObjectList() {
        OracleHelperScript utils = new OracleHelperScript(getSchemaOwnerContext());
        List<String> list = getSchemaOwnerContext().getSqlObjects("PACKAGE");
        assertTrue("getSqlObjects does not return any one of the pacakge at least", list.size() != 0);
    }

    @Test
    public void testGetPrimaryKeys() throws SQLException, IOException {
        new ExecuteQuery().loadFromSectionalFile(getSchemaOwnerContext(), "/examples/typeobjectexample_usingtable_sec.sql", new String[]{"drop person table",
                "create person table"});
        try {
            OracleHelperScript utils = new OracleHelperScript(getSchemaOwnerContext());
            List<String> keys = getSchemaOwnerContext().getPrimaryKeys(null, "person");
            assertEquals("ID", keys.get(0));

        } finally {
            new ExecuteQuery()
                    .loadFromSectionalFile(getSchemaOwnerContext(), "/examples/typeobjectexample_usingtable_sec.sql", new String[]{"drop person table"});
        }

    }

    @SqlObjectInfo(name = "few test data", source = "/AddDataToTable2Count5.sql")
    @SqlObjectDependsOn(Using = CreateTable1.class)
    public static class PopulateTestDataForTable2 extends BaseSqlScriptObject {

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
    public class PopulateTestDataForTable1 extends BaseSqlScriptObject {

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
