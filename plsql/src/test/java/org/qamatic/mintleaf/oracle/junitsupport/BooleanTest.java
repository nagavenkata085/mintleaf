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

package org.qamatic.mintleaf.oracle.junitsupport;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.qamatic.mintleaf.core.SqlObjectInfo;
import org.qamatic.mintleaf.interfaces.DbContext;
import org.qamatic.mintleaf.interfaces.SqlProcedure;
import org.qamatic.mintleaf.oracle.DbAssert;
import org.qamatic.mintleaf.oracle.OraclePackage;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BooleanTest extends OracleTestCase {


    private BooleanTestPackage mvbooleanPackage;

    @Before
    public void init() throws SQLException, IOException {
        mvbooleanPackage = new BooleanTestPackage(getSchemaOwnerContext());
        mvbooleanPackage.createAll();
    }

    @After
    public void cleanUp() throws SQLException, IOException {
        mvbooleanPackage.dropAll();
    }

    @Test
    public void testIsPackageExists() throws SQLException, IOException {
        DbAssert.assertPackageExists(mvbooleanPackage);
    }

    @Test
    public void testBooleanTrueCaseLegacyCall() throws SQLException {
        assertTrue(mvbooleanPackage.isGreaterThan100LegacyCall(101));
        assertTrue(mvbooleanPackage.isGreaterThan100LegacyCall(3191));
    }

    @Test
    public void testBooleanFalseCaseLegacyCall() throws SQLException {
        assertFalse(mvbooleanPackage.isGreaterThan100LegacyCall(50));
        assertFalse(mvbooleanPackage.isGreaterThan100LegacyCall(100));
    }

    @Test
    public void testBooleanFalseCaseSpringCall() {
        assertFalse(mvbooleanPackage.isGreaterThan100SpringCall(50));
        assertFalse(mvbooleanPackage.isGreaterThan100SpringCall(100));
    }

    @Test
    public void testBooleanTrueCaseSpringCall() throws SQLException {
        assertTrue(mvbooleanPackage.isGreaterThan100LegacyCall(101));
        assertTrue(mvbooleanPackage.isGreaterThan100LegacyCall(3191));
    }

    @Test
    public void testCallAnonymous_GetBoolean() {

        assertTrue(mvbooleanPackage.getBoolean());
    }

    @Test
    public void testBooleanCaseProcedureLogicTest() {
        assertTrue(mvbooleanPackage.isGreaterThan100TestingProcedureLogic(101));
        assertTrue(mvbooleanPackage.isGreaterThan100TestingProcedureLogic(3191));
    }

    @Test
    public void testBooleanRecompileMissing() {
        try {
            mvbooleanPackage.isGreaterThan100SpringCallNoRecompileException(101);
        } catch (Throwable e1) {
            assertTrue(true);
            return;
        }

        assertTrue(false);
    }

    @SqlObjectInfo(name = "BooleanTest", source = "/BooleanTest.sql")
    private class BooleanTestPackage extends OraclePackage {
        public BooleanTestPackage(DbContext context) {
            super(context);
        }

        @SuppressWarnings("boxing")
        public boolean getBoolean() {

            SqlProcedure proc = getProcedure(getAnonymousCode2());
            proc.createParameter("value1", Types.INTEGER);
            proc.createOutParameter("result", Types.INTEGER);
            proc.compile();
            proc.setValue("value1", 500);
            proc.execute();
            return proc.getBooleanValue("result");
        }

        private String getAnonymousCode2() {
            StringBuilder builder = new StringBuilder();

            builder.append("declare\n");
            builder.append("result_INTEGER INTEGER;\n");
            builder.append("result_BOOLEAN BOOLEAN;\n");
            builder.append("\n");
            builder.append("\n");
            builder.append("  FUNCTION bool2int (b BOOLEAN) RETURN INTEGER IS  BEGIN IF b IS NULL THEN RETURN NULL; ELSIF b THEN RETURN 1; ELSE RETURN 0; END IF; END bool2int;  FUNCTION int2bool (i INTEGER) RETURN BOOLEAN IS  BEGIN IF i IS NULL THEN RETURN NULL; ELSE RETURN i <> 0; END IF; END int2bool;\n");
            builder.append("\n");

            builder.append("\n");
            builder.append("begin\n");
            builder.append("result_BOOLEAN := BooleanTest.isGreaterThan100(?);\n");
            builder.append("? := bool2int(result_BOOLEAN);\n");

            builder.append("\n");
            builder.append("end;\n");

            return builder.toString();
        }

        @SuppressWarnings("boxing")
        public boolean isGreaterThan100SpringCall(int value1) {

            SqlProcedure proc = getFunction("isGreaterThan100");
            proc.createBooleanOutParameter("result");
            proc.createParameter("value1", Types.INTEGER);
            proc = proc.recompile();
            proc.setValue("value1", value1);
            proc.execute();
            int i = proc.getIntValue("result");
            return i == 1;

        }

        @SuppressWarnings("boxing")
        public boolean isGreaterThan100SpringCallNoRecompileException(int value1) {

            SqlProcedure proc = getFunction("isGreaterThan100");
            proc.createBooleanOutParameter("result");
            proc.createParameter("value1", Types.INTEGER);

            proc.setValue("value1", value1);
            proc.execute();
            int i = proc.getIntValue("result");
            return i == 1;

        }

        @SuppressWarnings("boxing")
        public boolean isGreaterThan100TestingProcedureLogic(int value1) {

            SqlProcedure proc = getProcedure("isGreaterThan100Proc");
            proc.createBooleanOutParameter("result");
            proc.createParameter("value1", Types.INTEGER);
            proc.createBooleanParameter("value2");
            proc = proc.recompile();

            proc.setValue("value1", value1);
            proc.setValue("value2", 1);
            proc.execute();
            int i = proc.getIntValue("result");
            return i == 1;

        }

        public boolean isGreaterThan100LegacyCall(int value1) throws SQLException {
            String proc = "{ ? = call BooleanTest.bool2int(BooleanTest.isGreaterThan100(?)) }";
            CallableStatement cs = getSchemaOwnerContext().getConnection().prepareCall(proc);
            cs.setInt(2, value1);
            cs.registerOutParameter(1, Types.INTEGER);
            cs.execute();
            return cs.getInt(1) == 1;
        }

    }

}
