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
import org.qamatic.mintleaf.interfaces.SqlArgument;
import org.qamatic.mintleaf.interfaces.SqlArgumentTypeExtension;
import org.qamatic.mintleaf.interfaces.SqlPackage;
import org.qamatic.mintleaf.oracle.spring.OracleSpringSqlProcedure;

import java.sql.Types;

import static org.junit.Assert.assertEquals;

public class OracleBooleanTypeExtensionTest {

    @Test
    public void testBooleanIdentifierDeclarationWithInParam() {
        SqlArgumentTypeExtension ext = new OracleBooleanTypeExtension();
        ext.setIdentifier("bVal");
        ext.setOutParameter(true);
        assertEquals("bVal", ext.getIdentifier());
        assertEquals("bVal_sup INTEGER;\nbVal_unsup BOOLEAN;\n", ext.getIdentifierDeclaration());
    }

    @Test
    public void testBooleanIdentifierDeclarationWithOutParam() {
        SqlArgumentTypeExtension ext = new OracleBooleanTypeExtension();
        ext.setIdentifier("bVal");
        ext.setOutParameter(false);
        assertEquals("bVal", ext.getIdentifier());
        assertEquals("bVal_unsup BOOLEAN;\n", ext.getIdentifierDeclaration());
    }

    @Test
    public void testBooleanTypeConversion() {
        SqlArgumentTypeExtension ext = new OracleBooleanTypeExtension();
        ext.setIdentifier("bVal");
        StringBuilder builder = new StringBuilder();

        builder.append("  FUNCTION bool2int (b BOOLEAN) RETURN INTEGER IS");
        builder.append("  BEGIN IF b IS NULL THEN RETURN 0; ELSIF b THEN RETURN 1; ELSE RETURN 0; END IF; END bool2int;");

        builder.append("  FUNCTION int2bool (i INTEGER) RETURN BOOLEAN IS");
        builder.append("  BEGIN IF i IS NULL THEN RETURN false; ELSE RETURN i <> 0; END IF; END int2bool;");
        assertEquals(builder.toString(), ext.getTypeConversionCode());
    }

    @Test
    public void testBooleanTypeMethodCall() {
        MockProcedure p = new MockProcedure(null);
        p.setSql("smcall");
        p.createBooleanParameter("inparam");
        Assert.assertEquals("smcall(inparam_unsup);", BaseProcedureCall.getMethodCall(p));
    }

    @Test
    public void testBooleanTypeMethodCallAsFunc() {
        MockProcedure p = new MockProcedure(null);
        p.setSql("smcall");
        p.setFunction(true);
        p.createBooleanOutParameter("result");
        p.createBooleanOutParameter("outparam");
        p.createBooleanParameter("inparam");
        assertEquals("result_unsup := smcall(outparam_unsup, inparam_unsup);", BaseProcedureCall.getMethodCall(p));
    }

    @Test
    public void testBooleanAssignmentCodeBeforeCall() {
        MockProcedure p = new MockProcedure(null);
        p.setSql("smcall");
        p.setFunction(true);
        SqlArgument arg1 = p.createBooleanOutParameter("result");
        SqlArgument arg2 = p.createBooleanOutParameter("outparam");
        SqlArgument arg3 = p.createBooleanParameter("inparam");
        SqlArgument arg4 = p.createParameter("inparamInt", Types.INTEGER);
        SqlArgument arg5 = p.createOutParameter("outparamStr", Types.VARCHAR);

        SqlArgument arg6 = p.createBooleanOutParameter("outparamA");

        assertEquals("", arg1.getTypeExtension().getAssignmentCodeBeforeCall());
        assertEquals("outparam_unsup := int2bool(outparam_sup);", arg2.getTypeExtension().getAssignmentCodeBeforeCall());
        assertEquals("inparam_unsup := int2bool(?);", arg3.getTypeExtension().getAssignmentCodeBeforeCall());
        assertEquals("", arg4.getTypeExtension().getAssignmentCodeBeforeCall());
        assertEquals("", arg5.getTypeExtension().getAssignmentCodeBeforeCall());
        assertEquals("outparamA_unsup := int2bool(outparamA_sup);", arg6.getTypeExtension().getAssignmentCodeBeforeCall());

        assertEquals("result_unsup := smcall(outparam_unsup, inparam_unsup, ?, ?, outparamA_unsup);", BaseProcedureCall.getMethodCall(p));
    }

    @Test
    public void testBooleanAssignmentCodeAfterCall() {
        MockProcedure p = new MockProcedure(null);
        p.setSql("smcall");
        p.setFunction(true);
        SqlArgument arg1 = p.createBooleanOutParameter("result");
        SqlArgument arg2 = p.createBooleanOutParameter("outparam");
        SqlArgument arg3 = p.createBooleanParameter("inparam");
        SqlArgument arg4 = p.createParameter("inparamInt", Types.INTEGER);
        SqlArgument arg5 = p.createOutParameter("outparamStr", Types.VARCHAR);

        SqlArgument arg6 = p.createBooleanOutParameter("outparamA");

        assertEquals("? := bool2int(result_unsup);", arg1.getTypeExtension().getAssignmentCodeAfterCall());
        assertEquals("? := bool2int(outparam_unsup);", arg2.getTypeExtension().getAssignmentCodeAfterCall());
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

    }

}
