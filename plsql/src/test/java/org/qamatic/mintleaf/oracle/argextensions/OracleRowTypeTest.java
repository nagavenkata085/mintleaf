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

package org.qamatic.mintleaf.oracle.argextensions;

import org.junit.Assert;
import org.junit.Test;
import org.qamatic.mintleaf.oracle.core.OracleProcedureCall;
import org.qamatic.mintleaf.oracle.ColumnMap;
import org.qamatic.mintleaf.interfaces.DbContext;
import org.qamatic.mintleaf.oracle.core.SqlArgument;
import org.qamatic.mintleaf.oracle.spring.OraclePLProcedure;

import java.sql.Types;

import static org.junit.Assert.assertEquals;

public class OracleRowTypeTest {

    @Test
    public void testRecordAssignmentMap() {
        OracleRowType ext = new OracleRowType();
        ColumnMap map = new ColumnMap("id", "id");
        assertEquals("r.id := t.id;", ext.getMappedTypeToRecAssignment(map, "r", "t"));
    }

    @Test
    public void testgetRecField() {
        OracleRowType ext = new OracleRowType();
        ColumnMap map = new ColumnMap("id", "id");
        assertEquals("r.id", ext.getRecField(map, "r"));
    }

    @Test
    public void testgetMappedTypeToRecAssignments() {
        OracleRowType ext = new OracleRowType();
        ext.addTypeMap(new ColumnMap("id", "id"));
        ext.addTypeMap(new ColumnMap("name", "name"));
        assertEquals("r.id := t.id;\nr.name := t.name;\n", ext.getMappedTypeToRecAssignments("r", "t"));
    }

    @Test
    public void testgetRecFields() {
        OracleRowType ext = new OracleRowType();
        ext.addTypeMap(new ColumnMap("id", "id"));
        ext.addTypeMap(new ColumnMap("name", "name"));
        assertEquals("r.id, r.name", ext.getRecFields("r"));
    }

    @Test
    public void testRecordTypeConversion() {
        OracleRowType ext = new OracleRowType();
        ext.setIdentifier("x");
        ext.setSupportedType("EMPLOYEE_TYPE");
        ext.setUnsupportedType("ORACLEPLRECORDTYPETEST.PLEMPLOYEE_RECORD");
        ext.addTypeMap(new ColumnMap("id", "id"));
        ext.addTypeMap(new ColumnMap("name", "name"));

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
        OracleRowType ext = new OracleRowType();
        ext.setIdentifier("x");
        ext.setSupportedType("EMPLOYEE_TYPE");
        ext.setUnsupportedType("ORACLEPLRECORDTYPETEST.PLEMPLOYEE_RECORD");
        ext.addTypeMap(new ColumnMap("id", "id"));
        ext.addTypeMap(new ColumnMap("name", "name"));

        assertEquals("x_unsup ORACLEPLRECORDTYPETEST.PLEMPLOYEE_RECORD;\n", ext.getVariableDeclaration());
        ext.setOutParameter(true);
        assertEquals("x_sup EMPLOYEE_TYPE;\nx_unsup ORACLEPLRECORDTYPETEST.PLEMPLOYEE_RECORD;\n", ext.getVariableDeclaration());
    }

    @Test
    public void testRecordBeforeCall() {
        OracleRowType ext = new OracleRowType();
        ext.setIdentifier("x");
        ext.setSupportedType("EMPLOYEE_TYPE");
        ext.setUnsupportedType("ORACLEPLRECORDTYPETEST.PLEMPLOYEE_RECORD");
        ext.addTypeMap(new ColumnMap("id", "id"));
        ext.addTypeMap(new ColumnMap("name", "name"));

        assertEquals("x_unsup := type_to_pl_x(?);", ext.getCodeBeforeCall());
    }

    @Test
    public void testRecordAfterCall() {
        OracleRowType ext = new OracleRowType();
        ext.setIdentifier("x");
        ext.setSupportedType("EMPLOYEE_TYPE");
        ext.setUnsupportedType("ORACLEPLRECORDTYPETEST.PLEMPLOYEE_RECORD");
        ext.addTypeMap(new ColumnMap("id", "id"));
        ext.addTypeMap(new ColumnMap("name", "name"));
        assertEquals("", ext.getCodeAfterCall());
        ext.setOutParameter(true);

        assertEquals("? := pl_to_type_x(x_unsup);", ext.getCodeAfterCall());
        ext.setResultsParameter(true);

        assertEquals("? := pl_to_type_x(x_unsup);", ext.getCodeAfterCall());
    }

    @Test
    public void testRecordTypeMethodCall() {
        MockPLProcedure p = new MockPLProcedure(null);
        p.setSql("smcall");
        p.createRowTypeOutParameter("inparam", "SomeTable");
        Assert.assertEquals("smcall(inparam_unsup);", OracleProcedureCall.getMethodCall(p));
    }

    @Test
    public void testRecordTypeMethodCallAsFunc() {
        MockPLProcedure p = new MockPLProcedure(null);
        p.setSql("smcall");
        p.setFunction(true);
        p.createRowTypeOutParameter("o1", "SomeTable");
        p.createRecordParameter("o2", "stype", "ustype");
        p.createInParameter("inparam", Types.VARCHAR);
        assertEquals("o1_unsup := smcall(o2_unsup, ?);", OracleProcedureCall.getMethodCall(p));
    }

    @Test
    public void testRecordAssignmentCodeBeforeCall() {
        MockPLProcedure p = new MockPLProcedure(null);
        p.setSql("smcall");
        p.setFunction(true);
        SqlArgument arg1 = p.createRowTypeOutParameter("result", "SomeTable");
        SqlArgument arg2 = p.createRowTypeOutParameter("outparam", "SomeTable");
        SqlArgument arg3 = p.createRecordParameter("inparam", "stype", "ustype");
        SqlArgument arg4 = p.createInParameter("inparamInt", Types.INTEGER);
        SqlArgument arg5 = p.createOutParameter("outparamStr", Types.VARCHAR);

        SqlArgument arg6 = p.createBooleanOutParameter("outparamA");

        assertEquals("", arg1.getCustomArg().getCodeBeforeCall());
        assertEquals("outparam_unsup := type_to_pl_outparam(outparam_sup);", arg2.getCustomArg().getCodeBeforeCall());
        assertEquals("inparam_unsup := type_to_pl_inparam(?);", arg3.getCustomArg().getCodeBeforeCall());
        assertEquals("", arg4.getCustomArg().getCodeBeforeCall());
        assertEquals("", arg5.getCustomArg().getCodeBeforeCall());
        assertEquals("outparamA_unsup := int2bool(outparamA_sup);", arg6.getCustomArg().getCodeBeforeCall());

        assertEquals("result_unsup := smcall(outparam_unsup, inparam_unsup, ?, ?, outparamA_unsup);", OracleProcedureCall.getMethodCall(p));
    }

    @Test
    public void testRecordAssignmentCodeAfterCall() {
        MockPLProcedure p = new MockPLProcedure(null);
        p.setSql("smcall");
        p.setFunction(true);
        SqlArgument arg1 = p.createRowTypeOutParameter("result", "SomeTable");
        SqlArgument arg2 = p.createRowTypeOutParameter("outparam", "SomeTable");
        SqlArgument arg3 = p.createRecordParameter("inparam", "stype", "ustype");
        SqlArgument arg4 = p.createInParameter("inparamInt", Types.INTEGER);
        SqlArgument arg5 = p.createOutParameter("outparamStr", Types.VARCHAR);

        SqlArgument arg6 = p.createBooleanOutParameter("outparamA");

        assertEquals("? := pl_to_type_result(result_unsup);", arg1.getCustomArg().getCodeAfterCall());
        assertEquals("? := pl_to_type_outparam(outparam_unsup);", arg2.getCustomArg().getCodeAfterCall());
        assertEquals("", arg3.getCustomArg().getCodeAfterCall());
        assertEquals("", arg4.getCustomArg().getCodeAfterCall());
        assertEquals("", arg5.getCustomArg().getCodeAfterCall());
        assertEquals("? := bool2int(outparamA_unsup);", arg6.getCustomArg().getCodeAfterCall());

    }

    private class MockPLProcedure extends OraclePLProcedure {

        public MockPLProcedure(DbContext context) {
            super(context);
        }

        @Override
        protected void initDataSource() {

        }

        @Override
        protected OracleRowType getRowType(String rowTypeTableName, String supportedType) {
            return new OracleRowType();
        }
    }

}
