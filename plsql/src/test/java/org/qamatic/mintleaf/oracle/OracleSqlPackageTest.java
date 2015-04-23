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
import org.qamatic.mintleaf.core.SqlObjectDependsOn;
import org.qamatic.mintleaf.core.SqlObjectInfo;
import org.qamatic.mintleaf.interfaces.DbContext;
import org.qamatic.mintleaf.interfaces.SqlPackage;
import org.qamatic.mintleaf.interfaces.SqlProcedure;
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
        SqlPackage pkg = new ATestPackage1(getSchemaOwnerContext());
        assertNotNull(pkg.getDbContext());
    }

    @Test
    public void TestBasicPackageCreate() throws SQLException, IOException {
        ATestPackage2 pkg = new ATestPackage2(getSchemaOwnerContext());
        pkg.create();
        DbAssert.assertPackageExists(pkg);
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
        DbAssert.assertPackageExists(pkg);

        pkg.drop();
        DbAssert.assertPackageNotExists(pkg);
    }

    @Test
    public void TestDependencyPackageCreate1() throws SQLException, IOException {
        DependencyPackageCreate1 pkg = new DependencyPackageCreate1(getSchemaOwnerContext());
        pkg.createDependencies();
        DbAssert.assertPackageExists(getSchemaOwnerContext(), "TESTLOG");
        pkg.create();
        DbAssert.assertPackageExists(pkg);
    }

    @Test
    public void TestDependencyPackageCreate2() throws SQLException, IOException {
        DependencyPackageCreate1 pkg = new DependencyPackageCreate1(getSchemaOwnerContext());
        pkg.create();
        DbAssert.assertPackageExists(getSchemaOwnerContext(), "TESTLOG");
        DbAssert.assertPackageExists(pkg);
    }

    @Test
    public void TestDependencyPackageCreate3() throws SQLException, IOException {
        DependencyPackageCreate2 pkg = new DependencyPackageCreate2(getSchemaOwnerContext());
        pkg.createDependencies();
        pkg.create();
        DbAssert.assertPackageExists(getSchemaOwnerContext(), "TESTLOG");
        DbAssert.assertPackageExists(getSchemaOwnerContext(), "TESTCONST");
        DbAssert.assertPackageExists(pkg);
    }

    @Test
    public void TestDependencyPackageDrop1() throws SQLException, IOException {
        DependencyPackageCreate2 pkg = new DependencyPackageCreate2(getSchemaOwnerContext());
        pkg.createDependencies();
        pkg.create();
        DbAssert.assertPackageExists(getSchemaOwnerContext(), "TESTLOG");
        DbAssert.assertPackageExists(getSchemaOwnerContext(), "TESTCONST");
        DbAssert.assertPackageExists(pkg);
        pkg.dropDependencies();
        DbAssert.assertPackageNotExists(getSchemaOwnerContext(), "TESTLOG");
        DbAssert.assertPackageNotExists(getSchemaOwnerContext(), "TESTCONST");
    }

    @Test
    public void testgetConstant() throws SQLException, IOException {
        ATestPackage2 pkg = new ATestPackage2(getSchemaOwnerContext());
        pkg.createAll();
        DbAssert.assertPackageExists(pkg);

        SqlProcedure constValue = (SqlProcedure) pkg.getConstant("TEST_CONSTANT_VALUE", Types.VARCHAR);
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
            SqlProcedure proc = getFunction("FINDMAX", Types.INTEGER);
            proc.createParameter("value1", Types.INTEGER);
            proc.createParameter("value2", Types.INTEGER);
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
