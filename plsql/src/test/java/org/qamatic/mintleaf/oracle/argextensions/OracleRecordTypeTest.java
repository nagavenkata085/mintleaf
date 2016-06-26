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
import org.qamatic.mintleaf.dbsupportimpls.oracle.OracleProcedureCall;
import org.qamatic.mintleaf.interfaces.ColumnMap;
import org.qamatic.mintleaf.interfaces.SqlArgument;
import org.qamatic.mintleaf.interfaces.SqlStoredProcedureModule;
import org.qamatic.mintleaf.oracle.spring.OraclePLProcedure;

import java.sql.Types;

import static org.junit.Assert.assertEquals;

public class OracleRecordTypeTest {

    @Test
    public void testRecordAssignmentMap() {
        OracleRecordType ext = new OracleRecordType();
        ColumnMap map = new ColumnMap("id", "id");
        assertEquals("r.id := t.id;", ext.getMappedTypeToRecAssignment(map, "r", "t"));
    }

    @Test
    public void testgetRecField() {
        OracleRecordType ext = new OracleRecordType();
        ColumnMap map = new ColumnMap("id", "id");
        assertEquals("r.id", ext.getRecField(map, "r"));
    }

    @Test
    public void testgetMappedTypeToRecAssignments() {
        OracleRecordType ext = new OracleRecordType();
        ext.addTypeMap(new ColumnMap("id", "id"));
        ext.addTypeMap(new ColumnMap("name", "name"));
        assertEquals("r.id := t.id;\nr.name := t.name;\n", ext.getMappedTypeToRecAssignments("r", "t"));
    }

    @Test
    public void testgetRecFields() {
        OracleRecordType ext = new OracleRecordType();
        ext.addTypeMap(new ColumnMap("id", "id"));
        ext.addTypeMap(new ColumnMap("name", "name"));
        assertEquals("r.id, r.name", ext.getRecFields("r"));
    }

    @Test
    public void testRecordTypeConversion() {
        OracleRecordType ext = new OracleRecordType();
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
        OracleRecordType ext = new OracleRecordType();
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
        OracleRecordType ext = new OracleRecordType();
        ext.setIdentifier("x");
        ext.setSupportedType("EMPLOYEE_TYPE");
        ext.setUnsupportedType("ORACLEPLRECORDTYPETEST.PLEMPLOYEE_RECORD");
        ext.addTypeMap(new ColumnMap("id", "id"));
        ext.addTypeMap(new ColumnMap("name", "name"));

        assertEquals("x_unsup := type_to_pl_x(?);", ext.getCodeBeforeCall());
    }

    @Test
    public void testRecordAfterCall() {
        OracleRecordType ext = new OracleRecordType();
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
        p.createRecordOutParameter("inparam", "stype", "ustype");
        Assert.assertEquals("smcall(inparam_unsup);", OracleProcedureCall.getMethodCall(p));
    }

    @Test
    public void testRecordTypeMethodCallAsFunc() {
        MockPLProcedure p = new MockPLProcedure(null);
        p.setSql("smcall");
        p.setFunction(true);
        p.createRecordOutParameter("o1", "stype", "ustype");
        p.createRecordParameter("o2", "stype", "ustype");
        p.createInParameter("inparam", Types.VARCHAR);
        assertEquals("o1_unsup := smcall(o2_unsup, ?);", OracleProcedureCall.getMethodCall(p));
    }

    @Test
    public void testRecordAssignmentCodeBeforeCall() {
        MockPLProcedure p = new MockPLProcedure(null);
        p.setSql("smcall");
        p.setFunction(true);
        SqlArgument arg1 = p.createRecordOutParameter("result", "stype", "ustype");
        SqlArgument arg2 = p.createRecordOutParameter("outparam", "stype", "ustype");
        SqlArgument arg3 = p.createRecordParameter("inparam", "stype", "ustype");
        SqlArgument arg4 = p.createInParameter("inparamInt", Types.INTEGER);
        SqlArgument arg5 = p.createOutParameter("outparamStr", Types.VARCHAR);

        SqlArgument arg6 = p.createBooleanOutParameter("outparamA");

        assertEquals("", arg1.getTypeExtension().getCodeBeforeCall());
        assertEquals("outparam_unsup := type_to_pl_outparam(outparam_sup);", arg2.getTypeExtension().getCodeBeforeCall());
        assertEquals("inparam_unsup := type_to_pl_inparam(?);", arg3.getTypeExtension().getCodeBeforeCall());
        assertEquals("", arg4.getTypeExtension().getCodeBeforeCall());
        assertEquals("", arg5.getTypeExtension().getCodeBeforeCall());
        assertEquals("outparamA_unsup := int2bool(outparamA_sup);", arg6.getTypeExtension().getCodeBeforeCall());

        assertEquals("result_unsup := smcall(outparam_unsup, inparam_unsup, ?, ?, outparamA_unsup);", OracleProcedureCall.getMethodCall(p));
    }

    @Test
    public void testRecordAssignmentCodeAfterCall() {
        MockPLProcedure p = new MockPLProcedure(null);
        p.setSql("smcall");
        p.setFunction(true);
        SqlArgument arg1 = p.createRecordOutParameter("result", "stype", "ustype");
        SqlArgument arg2 = p.createRecordOutParameter("outparam", "stype", "ustype");
        SqlArgument arg3 = p.createRecordParameter("inparam", "stype", "ustype");
        SqlArgument arg4 = p.createInParameter("inparamInt", Types.INTEGER);
        SqlArgument arg5 = p.createOutParameter("outparamStr", Types.VARCHAR);

        SqlArgument arg6 = p.createBooleanOutParameter("outparamA");

        assertEquals("? := pl_to_type_result(result_unsup);", arg1.getTypeExtension().getCodeAfterCall());
        assertEquals("? := pl_to_type_outparam(outparam_unsup);", arg2.getTypeExtension().getCodeAfterCall());
        assertEquals("", arg3.getTypeExtension().getCodeAfterCall());
        assertEquals("", arg4.getTypeExtension().getCodeAfterCall());
        assertEquals("", arg5.getTypeExtension().getCodeAfterCall());
        assertEquals("? := bool2int(outparamA_unsup);", arg6.getTypeExtension().getCodeAfterCall());

    }

    private class MockPLProcedure extends OraclePLProcedure {

        public MockPLProcedure(SqlStoredProcedureModule pkg) {
            super(pkg);
        }

        @Override
        protected void initDataSource() {

        }

    }

}
