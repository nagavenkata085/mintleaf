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
import org.qamatic.mintleaf.core.SqlObjectDependsOn;
import org.qamatic.mintleaf.core.SqlObjectInfo;
import org.qamatic.mintleaf.dbsupportimpls.oracle.OracleDbAssert;
import org.qamatic.mintleaf.interfaces.DbContext;
import org.qamatic.mintleaf.interfaces.SqlStoredProcedure;
import org.qamatic.mintleaf.interfaces.db.OracleDbContext;
import org.qamatic.mintleaf.oracle.junitsupport.OracleTestCase;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Types;

import static org.junit.Assert.*;

public final class OracleSqlPackageTest extends OracleTestCase {


    @Before
    public void init() {

    }

    @After
    public void cleanUp() throws SQLException, IOException {
        ATestPackage2 pkg = new ATestPackage2(getSchemaOwnerContext());
        pkg.dropAll();
    }

    @Test
    public void TestPackageTest() {
        SqlStoredProcedureModule pkg = new ATestPackage1(getSchemaOwnerContext());
        assertNotNull(pkg.getDbContext());
    }

    @Test
    public void TestBasicPackageCreate() throws SQLException, IOException {
        ATestPackage2 pkg = new ATestPackage2(getSchemaOwnerContext());
        pkg.create();
        OracleDbAssert.assertPackageExists((OracleDbContext) pkg.getDbContext(), pkg.getName());
        assertTrue(pkg.isExists());
    }

    @Test
    public void TestaFunctionFindMax() throws SQLException, IOException {
        ATestPackage4 pkg = new ATestPackage4(getSchemaOwnerContext());
        pkg.create();
        assertEquals(20, pkg.FindMax(10, 20));
    }

    @Test
    public void TestDrop() throws SQLException, IOException {
        ATestPackage2 pkg = new ATestPackage2(getSchemaOwnerContext());
        pkg.create();

        OracleDbAssert.assertPackageExists((OracleDbContext) pkg.getDbContext(), pkg.getName());

        pkg.drop();

        OracleDbAssert.assertPackageNotExists((OracleDbContext) pkg.getDbContext(), pkg.getName());
    }

    @Test
    public void TestDependencyPackageCreate1() throws SQLException, IOException {
        DependencyPackageCreate1 pkg = new DependencyPackageCreate1(getSchemaOwnerContext());
        pkg.createDependencies();
        OracleDbAssert.assertPackageExists((OracleDbContext) getSchemaOwnerContext(), "TESTLOG");
        pkg.create();

        OracleDbAssert.assertPackageExists((OracleDbContext) pkg.getDbContext(), pkg.getName());
    }

    @Test
    public void TestDependencyPackageCreate2() throws SQLException, IOException {
        DependencyPackageCreate1 pkg = new DependencyPackageCreate1(getSchemaOwnerContext());
        pkg.create();
        OracleDbAssert.assertPackageExists(getSchemaOwnerContext(), "TESTLOG");
        OracleDbAssert.assertPackageExists((OracleDbContext) pkg.getDbContext(), pkg.getName());
    }

    @Test
    public void TestDependencyPackageCreate3() throws SQLException, IOException {
        DependencyPackageCreate2 pkg = new DependencyPackageCreate2(getSchemaOwnerContext());
        pkg.createDependencies();
        pkg.create();
        OracleDbAssert.assertPackageExists(getSchemaOwnerContext(), "TESTLOG");
        OracleDbAssert.assertPackageExists(getSchemaOwnerContext(), "TESTCONST");
        OracleDbAssert.assertPackageExists((OracleDbContext) pkg.getDbContext(), pkg.getName());
    }

    @Test
    public void TestDependencyPackageDrop1() throws SQLException, IOException {
        DependencyPackageCreate2 pkg = new DependencyPackageCreate2(getSchemaOwnerContext());
        pkg.createDependencies();
        pkg.create();
        OracleDbAssert.assertPackageExists(getSchemaOwnerContext(), "TESTLOG");
        OracleDbAssert.assertPackageExists(getSchemaOwnerContext(), "TESTCONST");
        OracleDbAssert.assertPackageExists((OracleDbContext) pkg.getDbContext(), pkg.getName());
        pkg.dropDependencies();
        OracleDbAssert.assertPackageNotExists(getSchemaOwnerContext(), "TESTLOG");
        OracleDbAssert.assertPackageNotExists(getSchemaOwnerContext(), "TESTCONST");
    }

    @Test
    public void testgetConstant() throws SQLException, IOException {
        ATestPackage2 pkg = new ATestPackage2(getSchemaOwnerContext());
        pkg.createAll();
        OracleDbAssert.assertPackageExists((OracleDbContext) pkg.getDbContext(), pkg.getName());

        SqlStoredProcedure constValue = (SqlStoredProcedure) pkg.getConstant("TEST_CONSTANT_VALUE", Types.VARCHAR);
        assertNotNull(constValue);

        assertEquals("declare\nbegin\n? := ATestPackage2.TEST_CONSTANT_VALUE;\nend;\n", constValue.getCallString());

    }

    @SqlObjectInfo(name = "ATestPackage2", source = "/ATestPackage2.sql")
    @SqlObjectDependsOn(Using = {TestConst.class, DependencyPackageCreate1.class})
    public class DependencyPackageCreate2 extends OraclePackage {

        public DependencyPackageCreate2(DbContext context) {
            super(context);
        }
    }

    @SqlObjectInfo(name = "ATestPackage2", source = "/ATestPackage2.sql")
    @SqlObjectDependsOn(Using = {TestLog.class})
    public class DependencyPackageCreate1 extends OraclePackage {

        public DependencyPackageCreate1(DbContext context) {
            super(context);
        }
    }

    @SqlObjectInfo(name = "TestLog", source = "/TestLog.sql")
    public class TestLog extends OraclePackage {

        public TestLog(DbContext context) {
            super(context);
        }
    }

    @SqlObjectInfo(name = "", source = "/ATestPackage1.sql")
    public class ATestPackage4 extends OraclePackage {

        public ATestPackage4(DbContext context) {
            super(context);
        }

        @SuppressWarnings("boxing")
        public int FindMax(int value1, int value2) {
            SqlStoredProcedure proc = getFunction("FINDMAX", Types.INTEGER);
            proc.createInParameter("value1", Types.INTEGER);
            proc.createInParameter("value2", Types.INTEGER);
            proc.compile();
            proc.setValue("value1", value1);
            proc.setValue("value2", value2);
            proc.execute();

            return proc.getIntValue("result");
        }

    }

    @SqlObjectInfo(name = "ATestPackage2", source = "/ATestPackage2.sql")
    public class ATestPackage2 extends OraclePackage {

        public ATestPackage2(DbContext context) {
            super(context);
        }
    }

    @SqlObjectInfo(name = "atest_pkg1", source = "/ATestPackage1.sql")
    public class ATestPackage1 extends OraclePackage {

        public ATestPackage1(DbContext context) {
            super(context);
        }
    }

    @SqlObjectInfo(name = "TESTCONST", source = "/TestConst.sql")
    public class TestConst extends OraclePackage {

        public TestConst(DbContext context) {
            super(context);
        }
    }

}
