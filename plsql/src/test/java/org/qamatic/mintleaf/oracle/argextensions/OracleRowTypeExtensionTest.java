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

package org.qamatic.mintleaf.oracle.argextensions;

import org.junit.Assert;
import org.junit.Test;
import org.qamatic.mintleaf.core.BaseProcedureCall;
import org.qamatic.mintleaf.interfaces.ArgumentTypeMap;
import org.qamatic.mintleaf.interfaces.SqlArgument;
import org.qamatic.mintleaf.interfaces.SqlPackage;
import org.qamatic.mintleaf.oracle.spring.OracleSpringSqlProcedure;

import java.sql.Types;

import static org.junit.Assert.assertEquals;

public class OracleRowTypeExtensionTest {

    @Test
    public void testRecordAssignmentMap() {
        OracleRowTypeExtension ext = new OracleRowTypeExtension();
        ArgumentTypeMap map = new ArgumentTypeMap("id", "id");
        assertEquals("r.id := t.id;", ext.getMappedTypeToRecAssignment(map, "r", "t"));
    }

    @Test
    public void testgetRecField() {
        OracleRowTypeExtension ext = new OracleRowTypeExtension();
        ArgumentTypeMap map = new ArgumentTypeMap("id", "id");
        assertEquals("r.id", ext.getRecField(map, "r"));
    }

    @Test
    public void testgetMappedTypeToRecAssignments() {
        OracleRowTypeExtension ext = new OracleRowTypeExtension();
        ext.addTypeMap(new ArgumentTypeMap("id", "id"));
        ext.addTypeMap(new ArgumentTypeMap("name", "name"));
        assertEquals("r.id := t.id;\nr.name := t.name;\n", ext.getMappedTypeToRecAssignments("r", "t"));
    }

    @Test
    public void testgetRecFields() {
        OracleRowTypeExtension ext = new OracleRowTypeExtension();
        ext.addTypeMap(new ArgumentTypeMap("id", "id"));
        ext.addTypeMap(new ArgumentTypeMap("name", "name"));
        assertEquals("r.id, r.name", ext.getRecFields("r"));
    }

    @Test
    public void testRecordTypeConversion() {
        OracleRowTypeExtension ext = new OracleRowTypeExtension();
        ext.setIdentifier("x");
        ext.setSupportedType("EMPLOYEE_TYPE");
        ext.setUnsupportedType("ORACLEPLRECORDTYPETEST.PLEMPLOYEE_RECORD");
        ext.addTypeMap(new ArgumentTypeMap("id", "id"));
        ext.addTypeMap(new ArgumentTypeMap("name", "name"));

        StringBuilder builder = new StringBuilder();

        builder.append("  \n");
        builder.append("function pl_to_type_x(rec ORACLEPLRECORDTYPETEST.PLEMPLOYEE_RECORD)\n");
        builder.append("         return EMPLOYEE_TYPE is\n");
        builder.append("begin\n");
        builder.append("   return EMPLOYEE_TYPE(rec.id, rec.name);\n");
        builder.append("end pl_to_type_x;\n");
        builder.append("\n");
        builder.append("function type_to_pl_x(t EMPLOYEE_TYPE)\n");
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
        builder.append("end type_to_pl_x;\n");
        builder.append("\n");

        assertEquals(builder.toString(), ext.getTypeConversionCode());
    }

    @Test
    public void testRecordIdentifierDeclaration() {
        OracleRowTypeExtension ext = new OracleRowTypeExtension();
        ext.setIdentifier("x");
        ext.setSupportedType("EMPLOYEE_TYPE");
        ext.setUnsupportedType("ORACLEPLRECORDTYPETEST.PLEMPLOYEE_RECORD");
        ext.addTypeMap(new ArgumentTypeMap("id", "id"));
        ext.addTypeMap(new ArgumentTypeMap("name", "name"));

        assertEquals("x_unsup ORACLEPLRECORDTYPETEST.PLEMPLOYEE_RECORD;\n", ext.getIdentifierDeclaration());
        ext.setOutParameter(true);
        assertEquals("x_sup EMPLOYEE_TYPE;\nx_unsup ORACLEPLRECORDTYPETEST.PLEMPLOYEE_RECORD;\n", ext.getIdentifierDeclaration());
    }

    @Test
    public void testRecordBeforeCall() {
        OracleRowTypeExtension ext = new OracleRowTypeExtension();
        ext.setIdentifier("x");
        ext.setSupportedType("EMPLOYEE_TYPE");
        ext.setUnsupportedType("ORACLEPLRECORDTYPETEST.PLEMPLOYEE_RECORD");
        ext.addTypeMap(new ArgumentTypeMap("id", "id"));
        ext.addTypeMap(new ArgumentTypeMap("name", "name"));

        assertEquals("x_unsup := type_to_pl_x(?);", ext.getAssignmentCodeBeforeCall());
    }

    @Test
    public void testRecordAfterCall() {
        OracleRowTypeExtension ext = new OracleRowTypeExtension();
        ext.setIdentifier("x");
        ext.setSupportedType("EMPLOYEE_TYPE");
        ext.setUnsupportedType("ORACLEPLRECORDTYPETEST.PLEMPLOYEE_RECORD");
        ext.addTypeMap(new ArgumentTypeMap("id", "id"));
        ext.addTypeMap(new ArgumentTypeMap("name", "name"));
        assertEquals("", ext.getAssignmentCodeAfterCall());
        ext.setOutParameter(true);

        assertEquals("? := pl_to_type_x(x_unsup);", ext.getAssignmentCodeAfterCall());
        ext.setResultsParameter(true);

        assertEquals("? := pl_to_type_x(x_unsup);", ext.getAssignmentCodeAfterCall());
    }

    @Test
    public void testRecordTypeMethodCall() {
        MockProcedure p = new MockProcedure(null);
        p.setSql("smcall");
        p.createRowTypeOutParameter("inparam", "SomeTable");
        Assert.assertEquals("smcall(inparam_unsup);", BaseProcedureCall.getMethodCall(p));
    }

    @Test
    public void testRecordTypeMethodCallAsFunc() {
        MockProcedure p = new MockProcedure(null);
        p.setSql("smcall");
        p.setFunction(true);
        p.createRowTypeOutParameter("o1", "SomeTable");
        p.createRecordParameter("o2", "stype", "ustype");
        p.createParameter("inparam", Types.VARCHAR);
        assertEquals("o1_unsup := smcall(o2_unsup, ?);", BaseProcedureCall.getMethodCall(p));
    }

    @Test
    public void testRecordAssignmentCodeBeforeCall() {
        MockProcedure p = new MockProcedure(null);
        p.setSql("smcall");
        p.setFunction(true);
        SqlArgument arg1 = p.createRowTypeOutParameter("result", "SomeTable");
        SqlArgument arg2 = p.createRowTypeOutParameter("outparam", "SomeTable");
        SqlArgument arg3 = p.createRecordParameter("inparam", "stype", "ustype");
        SqlArgument arg4 = p.createParameter("inparamInt", Types.INTEGER);
        SqlArgument arg5 = p.createOutParameter("outparamStr", Types.VARCHAR);

        SqlArgument arg6 = p.createBooleanOutParameter("outparamA");

        assertEquals("", arg1.getTypeExtension().getAssignmentCodeBeforeCall());
        assertEquals("outparam_unsup := type_to_pl_outparam(outparam_sup);", arg2.getTypeExtension().getAssignmentCodeBeforeCall());
        assertEquals("inparam_unsup := type_to_pl_inparam(?);", arg3.getTypeExtension().getAssignmentCodeBeforeCall());
        assertEquals("", arg4.getTypeExtension().getAssignmentCodeBeforeCall());
        assertEquals("", arg5.getTypeExtension().getAssignmentCodeBeforeCall());
        assertEquals("outparamA_unsup := int2bool(outparamA_sup);", arg6.getTypeExtension().getAssignmentCodeBeforeCall());

        assertEquals("result_unsup := smcall(outparam_unsup, inparam_unsup, ?, ?, outparamA_unsup);", BaseProcedureCall.getMethodCall(p));
    }

    @Test
    public void testRecordAssignmentCodeAfterCall() {
        MockProcedure p = new MockProcedure(null);
        p.setSql("smcall");
        p.setFunction(true);
        SqlArgument arg1 = p.createRowTypeOutParameter("result", "SomeTable");
        SqlArgument arg2 = p.createRowTypeOutParameter("outparam", "SomeTable");
        SqlArgument arg3 = p.createRecordParameter("inparam", "stype", "ustype");
        SqlArgument arg4 = p.createParameter("inparamInt", Types.INTEGER);
        SqlArgument arg5 = p.createOutParameter("outparamStr", Types.VARCHAR);

        SqlArgument arg6 = p.createBooleanOutParameter("outparamA");

        assertEquals("? := pl_to_type_result(result_unsup);", arg1.getTypeExtension().getAssignmentCodeAfterCall());
        assertEquals("? := pl_to_type_outparam(outparam_unsup);", arg2.getTypeExtension().getAssignmentCodeAfterCall());
        assertEquals("", arg3.getTypeExtension().getAssignmentCodeAfterCall());
        assertEquals("", arg4.getTypeExtension().getAssignmentCodeAfterCall());
        assertEquals("", arg5.getTypeExtension().getAssignmentCodeAfterCall());
        assertEquals("? := bool2int(outparamA_unsup);", arg6.getTypeExtension().getAssignmentCodeAfterCall());

    }

    private class MockProcedure extends OracleSpringSqlProcedure {

        public MockProcedure(SqlPackage pkg) {
            super(pkg);
        }

        @Override
        protected void initDataSource() {

        }

        @Override
        protected OracleRowTypeExtension getRowTypeExtension(String rowTypeTableName, String supportedType) {
            return new OracleRowTypeExtension();
        }
    }

}
