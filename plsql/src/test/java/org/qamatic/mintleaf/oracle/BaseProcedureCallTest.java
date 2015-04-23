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

import org.junit.Test;
import org.qamatic.mintleaf.core.BaseProcedureCall;
import org.qamatic.mintleaf.interfaces.SqlArgument;
import org.qamatic.mintleaf.interfaces.SqlPackage;
import org.qamatic.mintleaf.oracle.argextensions.OracleArgumentTypeExtension;
import org.qamatic.mintleaf.oracle.spring.OracleSpringSqlProcedure;

import java.sql.Types;

import static org.junit.Assert.assertEquals;

public class BaseProcedureCallTest {

    @Test
    public void testProcedureCallString1() {

        MockProcedure p = new MockProcedure(null);
        p.setSql("getEmployee");
        BaseProcedureCall call = new BaseProcedureCall(p);

        assertEquals("declare\nbegin\ngetEmployee();\nend;\n", call.getCallString().toString());
    }

    @Test
    public void testProcedureCallString2() {

        MockProcedure p = new MockProcedure(null);

        p.setFunction(true);
        p.setSql("getEmployee");
        p.createOutParameter("test1", Types.VARCHAR);
        BaseProcedureCall call = new BaseProcedureCall(p);

        assertEquals("declare\nbegin\n? := getEmployee();\nend;\n", call.getCallString().toString());
    }

    @Test
    public void testProcedureCallString3() {

        MockProcedure p = new MockProcedure(null);
        p.setFunction(true);
        p.setSql("getEmployee");
        p.createOutParameter("test1", Types.VARCHAR);
        p.createParameter("test2", Types.VARCHAR);
        BaseProcedureCall call = new BaseProcedureCall(p);

        assertEquals("declare\nbegin\n? := getEmployee(?);\nend;\n", call.getCallString().toString());
    }

    @Test
    public void testProcedureCallString4() {

        MockProcedure p = new MockProcedure(null);
        p.setSql("getEmployee");
        p.createOutParameter("test1", Types.VARCHAR);
        p.createParameter("test2", Types.VARCHAR);
        BaseProcedureCall call = new BaseProcedureCall(p);

        assertEquals("declare\nbegin\ngetEmployee(?, ?);\nend;\n", call.getCallString().toString());
    }

    @Test
    public void testProcedureCallStringReadyForUse() {

        MockProcedure p = new MockProcedure(null);
        p.setSql("getEmployee");
        p.setSqlReadyForUse(true);
        p.createOutParameter("test1", Types.VARCHAR);
        p.createParameter("test2", Types.VARCHAR);
        BaseProcedureCall call = new BaseProcedureCall(p);

        assertEquals("declare\nbegin\ngetEmployee\nend;\n", call.getCallString().toString());
    }

    @Test
    public void testProcedureCallStringWithTypeExtension() {

        MockProcedure p = new MockProcedure(null);
        p.setFunction(true);
        p.setSql("getEmployee");
        SqlArgument a1 = p.createOutParameter("test1", Types.VARCHAR);
        a1.setTypeExtension(new MockTypeExtension1());
        SqlArgument a2 = p.createParameter("test2", Types.VARCHAR);
        a2.setTypeExtension(new MockTypeExtension2());
        BaseProcedureCall call = new BaseProcedureCall(p);
        StringBuilder builder = new StringBuilder();

        builder.append("declare\n");
        builder.append("p1 VARCHAR2;\n");
        builder.append("p2 VARCHAR2;\n");
        builder.append("\n");
        builder.append("function x() return varchar2 begin return 'hi '; end;\n");
        builder.append("function y() return varchar2 begin return 'how are you'; end;\n");
        builder.append("\n");
        builder.append("begin\n");
        builder.append("p1 := x(?)\n");
        builder.append("p2 := y(?)\n");
        builder.append("\n");
        builder.append("p1 := getEmployee(p2);\n");
        builder.append("? : xreverse(p1);\n");
        builder.append("? : yreverse(p2);\n");
        builder.append("\n");
        builder.append("end;\n");

        assertEquals(builder.toString(), call.getCallString().toString());
    }

    private class MockProcedure extends OracleSpringSqlProcedure {

        public MockProcedure(SqlPackage pkg) {
            super(pkg);
        }

        @Override
        protected void initDataSource() {

        }

    }

    public class MockTypeExtension1 extends OracleArgumentTypeExtension {

        @Override
        public String getIdentifier() {

            return "p1";
        }

        @Override
        public String getIdentifierDeclaration() {

            return "p1 VARCHAR2;";
        }

        @Override
        public String getTypeConversionCode() {

            return "function x() return varchar2 begin return 'hi '; end;";
        }

        @Override
        public String getAssignmentCodeBeforeCall() {

            return "p1 := x(?)";
        }

        @Override
        public String getAssignmentCodeAfterCall() {

            return "? : xreverse(p1);";
        }

    }

    public class MockTypeExtension2 extends OracleArgumentTypeExtension {

        @Override
        public String getIdentifier() {

            return "p2";
        }

        @Override
        public String getIdentifierDeclaration() {

            return "p2 VARCHAR2;";
        }

        @Override
        public String getTypeConversionCode() {

            return "function y() return varchar2 begin return 'how are you'; end;";
        }

        @Override
        public String getAssignmentCodeBeforeCall() {

            return "p2 := y(?)";
        }

        @Override
        public String getAssignmentCodeAfterCall() {

            return "? : yreverse(p2);";
        }

    }
}
