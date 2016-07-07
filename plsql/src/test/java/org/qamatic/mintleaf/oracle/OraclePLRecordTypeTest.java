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

import oracle.sql.STRUCT;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.qamatic.mintleaf.core.SqlObjectInfo;
import org.qamatic.mintleaf.dbsupportimpls.oracle.OracleDbAssert;
import org.qamatic.mintleaf.interfaces.ColumnMap;
import org.qamatic.mintleaf.interfaces.DbContext;
import org.qamatic.mintleaf.interfaces.SqlArgument;
import org.qamatic.mintleaf.interfaces.SqlStoredProcedure;
import org.qamatic.mintleaf.oracle.junitsupport.OracleTestCase;
import org.qamatic.mintleaf.oracle.spring.OraclePLProcedure;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Types;

import static org.junit.Assert.assertEquals;

public class OraclePLRecordTypeTest extends OracleTestCase {


    private PackageThanContainsRecord mvpkg;

    private static String getAnonymousCode() {
        StringBuilder builder = new StringBuilder();

        builder.append("declare\n");
        builder.append("result_sup EMPLOYEE_TYPE;\n");
        builder.append("result_unsup ORACLEPLRECORDTYPETEST.PLEMPLOYEE_RECORD;\n");
        builder.append("\n");
        builder.append("\n");
        builder.append("  \n");
        builder.append("function pl_to_type_result(rec ORACLEPLRECORDTYPETEST.PLEMPLOYEE_RECORD)\n");
        builder.append("         return EMPLOYEE_TYPE is\n");
        builder.append("begin\n");
        builder.append("   return EMPLOYEE_TYPE(rec.id, rec.name);\n");
        builder.append("end pl_to_type_result;\n");
        builder.append("\n");
        builder.append("function type_to_pl_result(t EMPLOYEE_TYPE)\n");
        builder.append("         return ORACLEPLRECORDTYPETEST.PLEMPLOYEE_RECORD is\n");
        builder.append(" rec ORACLEPLRECORDTYPETEST.PLEMPLOYEE_RECORD;\n");
        builder.append("begin\n");
        builder.append(" if t IS NOT NULL\n");
        builder.append(" then\n");
        builder.append("rec.id := t.id;\n");
        builder.append("rec.name := t.name;\n");
        builder.append("\n");
        builder.append(" end if;\n");
        builder.append(" return rec;\n");
        builder.append("end type_to_pl_result;\n");
        builder.append("\n");
        builder.append("\n");
        builder.append("\n");
        builder.append("begin\n");
        builder.append("result_unsup := OraclePLRecordTypeTest.GETEMPLOYEE();\n");
        builder.append("? := pl_to_type_result(result_unsup);\n");
        builder.append("\n");
        builder.append("end;\n");
        return builder.toString();
    }

    @Before
    public void init() throws SQLException, IOException {
        mvpkg = new PackageThanContainsRecord(getSchemaOwnerContext());
        mvpkg.create();
        new EmployeeTypeObject(getSchemaOwnerContext()).create();
    }

    @After
    public void cleanUp() {
        // new EmployeeTypeObject(getSchemaOwnerContext()).drop();
        // mvpkg.drop();
    }

    @Test
    public void testObjectExistance() throws SQLException, IOException {
        OracleDbAssert.assertPackageExists(mvpkg);
        OracleDbAssert.assertTypeExists(getSchemaOwnerContext(), new EmployeeTypeObject(getSchemaOwnerContext()).getName());
    }

    @Test
    public void testGetEmployee() throws SQLException {
        STRUCT actual = (STRUCT) mvpkg.getEmployeeUsingAnonymousCode();
        assertEquals("senthil maruthaiappan", actual.getOracleAttributes()[1].toString());
    }

    @Test
    public void testGetEmployeeAsObject() throws SQLException {
        STRUCT actual = (STRUCT) mvpkg.getEmployeeAsObject();
        assertEquals("senthil maruthaiappan", actual.getOracleAttributes()[1].toString());
    }


    @SqlObjectInfo(name = "OraclePLRecordTypeTest", source = "/OraclePLRecordTypeTest.sql")
    private class PackageThanContainsRecord extends OraclePackage {
        public PackageThanContainsRecord(DbContext context) {
            super(context);
        }

        public Object getEmployeeUsingAnonymousCode() {

            String sql = getAnonymousCode();

            SqlStoredProcedure proc = getFunction(sql, Types.STRUCT, "EMPLOYEE_TYPE");
            proc.execute();
            proc.getValue("result");

            return proc.getValue("result");
        }

        public Object getEmployeeAsObject() {
            SqlStoredProcedure proc = getFunction("GETEMPLOYEE");
            SqlArgument arg = ((OraclePLProcedure) proc).createRecordOutParameter("result", "EMPLOYEE_TYPE", "ORACLEPLRECORDTYPETEST.PLEMPLOYEE_RECORD");
            SqlArgumentRecordTypeExtension ext = (SqlArgumentRecordTypeExtension) arg.getTypeExtension();
            ext.addTypeMap(new ColumnMap("id", "id"));
            ext.addTypeMap(new ColumnMap("name", "name"));
            proc = ((OraclePLProcedure) proc).recompile();
            proc.execute();
            return proc.getValue("result");
        }

    }

    @SqlObjectInfo(name = "employee_type", source = "/EmployeeTypeObject.sql", sourceDelimiter = ";")
    private class EmployeeTypeObject extends OracleTypeObject {
        public EmployeeTypeObject(DbContext context) {
            super(context);
        }

    }

}
