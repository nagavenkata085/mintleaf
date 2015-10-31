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

import oracle.jdbc.OracleTypes;
import oracle.sql.NUMBER;
import org.junit.Assert;
import org.junit.Test;
import org.qamatic.mintleaf.core.SqlObjectInfo;
import org.qamatic.mintleaf.interfaces.*;
import org.qamatic.mintleaf.oracle.argextensions.OracleRowTypeExtension;
import org.qamatic.mintleaf.oracle.junitsupport.OracleTestCase;
import org.qamatic.mintleaf.oracle.spring.OracleSpringSqlProcedure;

import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class OracleSpringSqlProcedureTest extends OracleTestCase {

    private static List<Object> mvtestObjects = new ArrayList<Object>();


    @Test
    public void basicProcedureInvokingTest() {

    }

    @Test
    public void procedureNaming1() {
        MockPackage1 pkg = new MockPackage1(getSchemaOwnerContext());
        OracleSpringSqlProcedure p = new OracleSpringSqlProcedure(pkg);
        p.setSql("FindMax");
        assertEquals("declare\nbegin\nFindMax();\nend;\n", p.getCallString());
    }

    @Test
    public void testSetSql1() {
        MockPackage1 pkg = new MockPackage1(getSchemaOwnerContext());
        OracleSpringSqlProcedure p = (OracleSpringSqlProcedure) pkg.getFunction("FindMax", OracleTypes.INTEGER);
        assertEquals("declare\nbegin\n? := FindMax();\nend;\n", p.getCallString());
    }

    @Test
    public void testSetSql2() {
        MockPackage1 pkg = new MockPackage1(getSchemaOwnerContext());
        OracleSpringSqlProcedure p = (OracleSpringSqlProcedure) pkg.getProcedure("FindMax");
        assertEquals("declare\nbegin\nFindMax();\nend;\n", p.getCallString());
    }

    @Test
    public void testSetSql3CustomCall1() {
        MockPackage1 pkg = new MockPackage1(getSchemaOwnerContext());
        OracleSpringSqlProcedure p = (OracleSpringSqlProcedure) pkg.getProcedure("FindMax(?)");
        assertEquals("declare\nbegin\nFindMax(?);\nend;\n", p.getCallString());
    }

    @Test
    public void testSetSql3CustomCall3() {
        MockPackage1 pkg = new MockPackage1(getSchemaOwnerContext());
        OracleSpringSqlProcedure p = (OracleSpringSqlProcedure) pkg.getFunction("CO_SQLUTILS.bool2int(  BooleanTest.isGreaterThan100(?)  )", OracleTypes.INTEGER);
        assertEquals("declare\nbegin\n? := CO_SQLUTILS.bool2int(  BooleanTest.isGreaterThan100(?)  );\nend;\n", p.getCallString());
    }

    @Test
    public void testSetSql3CustomCall2() {
        MockPackage1 pkg = new MockPackage1(getSchemaOwnerContext());
        OracleSpringSqlProcedure p = (OracleSpringSqlProcedure) pkg.getFunction("FindMax", OracleTypes.INTEGER);
        assertEquals("declare\nbegin\n? := FindMax();\nend;\n", p.getCallString());
    }

    @Test
    public void procedureGetString() {
        MockPackage1 pkg = new MockPackage1(getSchemaOwnerContext());
        MockProcedureNoDb p = new MockProcedureNoDb(pkg, "FindMax");
        p.OutParamters.clear();
        p.OutParamters.put("result", "wbs1");
        p.execute();
        assertEquals("wbs1", p.getStringValue("result"));
    }

    @Test
    public void procedureGetValue() {
        MockPackage1 pkg = new MockPackage1(getSchemaOwnerContext());
        MockProcedureNoDb p = new MockProcedureNoDb(pkg, "FindMax");
        p.OutParamters.clear();
        p.OutParamters.put("result", "wbs1");
        p.execute();
        assertEquals("wbs1", p.getValue("result"));
    }

    @Test
    public void testgetDeclaredParam() {
        MockPackage1 pkg = new MockPackage1(getSchemaOwnerContext());
        MockProcedureNoDb p = new MockProcedureNoDb(pkg, "FindMax");

        p.createParameter("p1", Types.INTEGER);
        p.createParameter("p2", Types.INTEGER);
        p.createParameter("p3", Types.INTEGER);

        Assert.assertEquals(3, p.getDeclaredArguments().size());
    }

    @Test
    public void testgetTypeObjectValue() throws SQLException {
        MockPackage1 pkg = new MockPackage1(getSchemaOwnerContext());
        MockProcedureNoDb p = new MockProcedureNoDb(pkg, "FindMax") {
            @Override
            protected Object[] getOracleAttributes(String parameterName) throws SQLException {

                return new Object[]{new NUMBER(31)};
            }

            @Override
            protected OracleRowTypeExtension getRowTypeExtension(String rowTypeTableName, String supportedType) {
                OracleRowTypeExtension ext = new OracleRowTypeExtension();
                return ext;
            }
        };
        p.createRowTypeOutParameter("result", "EMPLOYEE");
        Assert.assertEquals(1, p.getDeclaredArguments().size());
        SqlTypeObjectValue value = p.getTypeObjectValue("result");
        assertNotNull(value);
        assertEquals("TE_EMPLOYEE", value.getTypeName());
        value.getMetaData().add(new SqlColumn("emp_id", "number"));
        assertEquals(31, value.getIntValue("emp_id"));
    }

    @Test
    public void testCreateTypeObjectParamMixed() {
        MockPackage1 pkg = new MockPackage1(getSchemaOwnerContext());
        MockProcedureNoDb p = new MockProcedureNoDb(pkg, "FindMax");
        SqlArgument arg1 = p.createTypeObjectParameter("student1", TStudentType.class);
        SqlArgument arg2 = p.createTypeObjectOutParameter("result", TStudentType.class);
        Assert.assertEquals(2, p.getDeclaredArguments().size());

        assertEquals("TSTUDENTTYPE", arg1.getTypeName());
        assertEquals("TSTUDENTTYPE", arg2.getTypeName());
        assertEquals("TSTUDENTTYPE", arg1.getTypeExtension().getSupportedType());
        assertEquals("TSTUDENTTYPE", arg2.getTypeExtension().getSupportedType());

        assertTrue(arg2.getTypeExtension().isOutParameter());
        assertFalse(arg2.getTypeExtension().isResultsParameter());

    }

    @Test
    public void testCreateTypeObjectOutParam() {
        MockPackage1 pkg = new MockPackage1(getSchemaOwnerContext());
        MockProcedureNoDb p = new MockProcedureNoDb(pkg, "FindMax");
        p.setFunction(true);
        SqlArgument arg1 = p.createTypeObjectOutParameter("result", TStudentType.class);
        Assert.assertEquals(1, p.getDeclaredArguments().size());

        assertEquals("TSTUDENTTYPE", arg1.getTypeName());
        assertEquals("TSTUDENTTYPE", arg1.getTypeExtension().getSupportedType());
        assertTrue(arg1.getTypeExtension().isOutParameter());
        assertTrue(arg1.getTypeExtension().isResultsParameter());

    }

    @Test
    public void testPackageGetFunctionAsTypeObject() {
        MockPackage1 pkg = new MockPackage1(getSchemaOwnerContext());
        SqlProcedure p = pkg.getFunction("getStudent", TStudentType.class);

        assertEquals(1, p.getDeclaredArguments().size());
        SqlArgument arg1 = p.getDeclaredArguments().get(0);
        assertEquals("result", arg1.getParameterName());
        assertEquals("TSTUDENTTYPE", arg1.getTypeName());
        assertEquals("TSTUDENTTYPE", arg1.getTypeExtension().getSupportedType());
        assertTrue(arg1.getTypeExtension().isOutParameter());
        assertTrue(arg1.getTypeExtension().isResultsParameter());

    }

    @Test
    public void testTypeObjectRegistry() {
        MockPackage1 pkg = new MockPackage1(getSchemaOwnerContext());
        MockProcedureNoDb p = new MockProcedureNoDb(pkg, "FindMax");
        p.createTypeObjectOutParameter("student1", TStudentType.class);
        assertEquals(1, p.getTypeObjectRegistry().size());

    }

    @Test
    public void testTypeObjectResultAccess() throws SQLException {
        MockPackage1 pkg = new MockPackage1(getSchemaOwnerContext());
        MockProcedureNoDb p = new MockProcedureNoDb(pkg, "FindMax");
        p.createTypeObjectOutParameter("result", TStudentType.class);
        p.createTypeObjectParameter("teacherA", TTeacherType.class);
        assertTrue(TStudentType.class.isInstance(p.getTypeObject("result")));
        TStudentType student = (TStudentType) p.getTypeObject("result");
        assertNotNull(student);
        TTeacherType teacherA = (TTeacherType) p.getTypeObject("teacherA");
        assertNotNull(teacherA);
    }

    @Test
    public void testTypeObjectResultFieldAccess() throws SQLException {
        MockPackage1 pkg = new MockPackage1(getSchemaOwnerContext());
        MockProcedureNoDb p = new MockProcedureNoDb(pkg, "FindMax") {
            @SuppressWarnings("boxing")
            @Override
            protected Object[] getOracleAttributes(String parameterName) throws SQLException {
                mvtestObjects.clear();
                mvtestObjects.add(123);
                return mvtestObjects.toArray();
            }
        };
        p.createTypeObjectOutParameter("result", TStudentType.class);
        p.getTypeObjectValue("result").getMetaData().add(new SqlColumn("student_id", "number"));

        TStudentType student = (TStudentType) p.getTypeObject("result");

        assertEquals(123, student.getStudentId());

    }

    @SqlObjectInfo(name = "TStudentType")
    public class TStudentType extends OracleTypeObject {

        @TypeObjectField(name = "student_id")
        private int mvstudentId;

        public TStudentType(DbContext context) {
            super(context);
        }

        public int getStudentId() {
            return mvstudentId;
        }

        public void setStudentId(int studentId) {
            mvstudentId = studentId;
        }

        @Override
        public SqlObjectMetaData getMetaData() throws SQLException {
            return null;
        }

    }

    @SqlObjectInfo(name = "TTeacherType")
    public class TTeacherType extends OracleTypeObject {

        public TTeacherType(DbContext context) {
            super(context);
        }

        @Override
        public SqlObjectMetaData getMetaData() throws SQLException {
            return null;
        }

    }

    @SqlObjectInfo(name = "", source = "")
    private class MockPackage1 extends OraclePackage {
        public MockPackage1(DbContext context) {
            super(context);
        }
    }

    private class MockProcedureNoDb extends OracleSpringSqlProcedure {
        public final HashMap<String, Object> OutParamters = new HashMap<String, Object>();

        public MockProcedureNoDb(SqlPackage pkg, String procedureName) {
            super(pkg);

        }

        @Override
        public Map<String, SqlTypeObject> getTypeObjectRegistry() {

            return super.getTypeObjectRegistry();
        }

        @Override
        protected Map<String, Object> executeInternal() {
            return OutParamters;
        }
    }

}