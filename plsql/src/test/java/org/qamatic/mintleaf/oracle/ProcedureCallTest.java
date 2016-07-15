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

import org.junit.Test;
import org.qamatic.mintleaf.core.ProcedureCall;
import org.qamatic.mintleaf.oracle.core.OracleArg;
import org.qamatic.mintleaf.oracle.core.OracleProcedureCall;
import org.qamatic.mintleaf.interfaces.DbContext;
import org.qamatic.mintleaf.interfaces.SqlArgument;
import org.qamatic.mintleaf.oracle.spring.OraclePLProcedure;

import java.sql.Types;

import static org.junit.Assert.assertEquals;

public class ProcedureCallTest {

    @Test
    public void testProcedureCallString1() {

        MockPLProcedure p = new MockPLProcedure(null);
        p.setSql("getEmployee");
        ProcedureCall call = new OracleProcedureCall(p);

        assertEquals("declare\nbegin\ngetEmployee();\nend;\n", call.getSQL().toString());
    }

    @Test
    public void testProcedureCallString2() {

        MockPLProcedure p = new MockPLProcedure(null);

        p.setFunction(true);
        p.setSql("getEmployee");
        p.createOutParameter("test1", Types.VARCHAR);
        ProcedureCall call = new OracleProcedureCall(p);

        assertEquals("declare\nbegin\n? := getEmployee();\nend;\n", call.getSQL().toString());
    }

    @Test
    public void testProcedureCallString3() {

        MockPLProcedure p = new MockPLProcedure(null);
        p.setFunction(true);
        p.setSql("getEmployee");
        p.createOutParameter("test1", Types.VARCHAR);
        p.createInParameter("test2", Types.VARCHAR);
        ProcedureCall call = new OracleProcedureCall(p);

        assertEquals("declare\nbegin\n? := getEmployee(?);\nend;\n", call.getSQL().toString());
    }

    @Test
    public void testProcedureCallString4() {

        MockPLProcedure p = new MockPLProcedure(null);
        p.setSql("getEmployee");
        p.createOutParameter("test1", Types.VARCHAR);
        p.createInParameter("test2", Types.VARCHAR);
        ProcedureCall call = new OracleProcedureCall(p);

        assertEquals("declare\nbegin\ngetEmployee(?, ?);\nend;\n", call.getSQL().toString());
    }

    @Test
    public void testProcedureCallStringReadyForUse() {

        MockPLProcedure p = new MockPLProcedure(null);
        p.setSql("getEmployee");
        p.setSqlReadyForUse(true);
        p.createOutParameter("test1", Types.VARCHAR);
        p.createInParameter("test2", Types.VARCHAR);
        ProcedureCall call = new OracleProcedureCall(p);

        assertEquals("declare\nbegin\ngetEmployee\nend;\n", call.getSQL().toString());
    }

    @Test
    public void testProcedureCallStringWithTypeExtension() {

        MockPLProcedure p = new MockPLProcedure(null);
        p.setFunction(true);
        p.setSql("getEmployee");
        SqlArgument a1 = p.createOutParameter("test1", Types.VARCHAR);
        a1.setCustomArg(new MockType1());
        SqlArgument a2 = p.createInParameter("test2", Types.VARCHAR);
        a2.setCustomArg(new MockType2());
        ProcedureCall call = new OracleProcedureCall(p);
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

        assertEquals(builder.toString(), call.getSQL().toString());
    }

    private class MockPLProcedure extends OraclePLProcedure {

        public MockPLProcedure(DbContext context) {
            super(context);
        }


        @Override
        protected void initDataSource() {

        }

    }

    public class MockType1 extends OracleArg {

        @Override
        public String getIdentifier() {

            return "p1";
        }

        @Override
        public String getVariableDeclaration() {

            return "p1 VARCHAR2;";
        }

        @Override
        public String getTypeConversionCode() {

            return "function x() return varchar2 begin return 'hi '; end;";
        }

        @Override
        public String getCodeBeforeCall() {

            return "p1 := x(?)";
        }

        @Override
        public String getCodeAfterCall() {

            return "? : xreverse(p1);";
        }

    }

    public class MockType2 extends OracleArg {

        @Override
        public String getIdentifier() {

            return "p2";
        }

        @Override
        public String getVariableDeclaration() {

            return "p2 VARCHAR2;";
        }

        @Override
        public String getTypeConversionCode() {

            return "function y() return varchar2 begin return 'how are you'; end;";
        }

        @Override
        public String getCodeBeforeCall() {

            return "p2 := y(?)";
        }

        @Override
        public String getCodeAfterCall() {

            return "? : yreverse(p2);";
        }

    }
}
