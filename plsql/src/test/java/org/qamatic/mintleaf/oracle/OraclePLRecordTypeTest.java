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

import oracle.sql.STRUCT;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.qamatic.mintleaf.core.SqlObjectInfo;
import org.qamatic.mintleaf.interfaces.*;
import org.qamatic.mintleaf.oracle.junitsupport.OracleTestCase;

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
        DbAssert.assertPackageExists(mvpkg);
        DbAssert.assertTypeExists(new EmployeeTypeObject(getSchemaOwnerContext()));
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

            SqlProcedure proc = getFunction(sql, Types.STRUCT, "EMPLOYEE_TYPE");
            proc.execute();
            proc.getValue("result");

            return proc.getValue("result");
        }

        public Object getEmployeeAsObject() {
            SqlProcedure proc = getFunction("GETEMPLOYEE");
            SqlArgument arg = proc.createRecordOutParameter("result", "EMPLOYEE_TYPE", "ORACLEPLRECORDTYPETEST.PLEMPLOYEE_RECORD");
            SqlArgumentRecordTypeExtension ext = (SqlArgumentRecordTypeExtension) arg.getTypeExtension();
            ext.addTypeMap(new ArgumentTypeMap("id", "id"));
            ext.addTypeMap(new ArgumentTypeMap("name", "name"));
            proc = proc.recompile();
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
