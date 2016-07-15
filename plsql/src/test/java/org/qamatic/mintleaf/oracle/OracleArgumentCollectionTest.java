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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.qamatic.mintleaf.oracle.core.OracleArg;
import org.qamatic.mintleaf.oracle.core.OracleProcedureCall;
import org.qamatic.mintleaf.interfaces.DbContext;
import org.qamatic.mintleaf.interfaces.SqlArgument;
import org.qamatic.mintleaf.oracle.spring.OraclePLProcedure;

import java.sql.Types;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class OracleArgumentCollectionTest {

    MockPLProcedure p;
    SqlArgument a1;
    SqlArgument a2;
    SqlArgument a3;
    SqlArgument a4;

    @Before
    public void init() {
        p = new MockPLProcedure(null);
        p.setSql("smcall");

        a1 = p.createOutParameter("test1", Types.VARCHAR);
        a2 = p.createInParameter("test2", Types.VARCHAR);
    }

    @Test
    public void testExtensionSoureIdentifierWithNoArg() {
        p = new MockPLProcedure(null);
        p.setSql("smcall");
        Assert.assertEquals("", p.getDeclaredArguments().getIdentifier());
        assertEquals("smcall();", OracleProcedureCall.getMethodCall(p));
    }

    @Test
    public void testExtensionSoureIdentifierWithNoArgButFunction() {
        p = new MockPLProcedure(null);
        p.setSql("smcall");
        p.setFunction(true);
        Assert.assertEquals("", p.getDeclaredArguments().getIdentifier());
        assertEquals("? := smcall();", OracleProcedureCall.getMethodCall(p));
    }

    @Test
    public void testExtensionSoureIdentifierWith() {
        a1.setCustomArg(new MockType1());
        Assert.assertEquals("p1\n?\n", p.getDeclaredArguments().getIdentifier());
        assertEquals("smcall(p1, ?);", OracleProcedureCall.getMethodCall(p));
        a2.setCustomArg(new MockType2());
        Assert.assertEquals("p1\np2\n", p.getDeclaredArguments().getIdentifier());
        assertEquals("smcall(p1, p2);", OracleProcedureCall.getMethodCall(p));

    }

    @Test
    public void testExtensionSoureIdentifierWithFunc() {
        p.setFunction(true);
        a1.setCustomArg(new MockType1());
        Assert.assertEquals("p1\n?\n", p.getDeclaredArguments().getIdentifier());
        assertEquals("p1 := smcall(?);", OracleProcedureCall.getMethodCall(p));
        a2.setCustomArg(new MockType2());
        Assert.assertEquals("p1\np2\n", p.getDeclaredArguments().getIdentifier());
        assertEquals("p1 := smcall(p2);", OracleProcedureCall.getMethodCall(p));
    }

    @Test
    public void testExtensionSoureIdentifierDeclaration() {

        String actual = p.getDeclaredArguments().getVariableDeclaration();
        assertEquals("", actual);

        a1.setCustomArg(new MockType1());
        a2.setCustomArg(new MockType2());

        Assert.assertEquals("p1 VARCHAR2;\np2 VARCHAR2;\n", p.getDeclaredArguments().getVariableDeclaration());
    }

    @Test
    public void testExtensionSoureTypeConversionCode() {

        Assert.assertEquals("", p.getDeclaredArguments().getTypeConversionCode());

        a1.setCustomArg(new MockType1());
        a2.setCustomArg(new MockType2());

        Assert.assertEquals("function x() return varchar2 begin return 'hi '; end;" + "\nfunction y() return varchar2 begin return 'how are you'; end;\n", p
                .getDeclaredArguments().getTypeConversionCode());
    }

    @Test
    public void testExtensionSoureAssignmentCodeBeforeCall() {

        Assert.assertEquals("", p.getDeclaredArguments().getCodeBeforeCall());

        a1.setCustomArg(new MockType1());
        a2.setCustomArg(new MockType2());

        Assert.assertEquals("p1 := x(?)\np2 := y(?)\n", p.getDeclaredArguments().getCodeBeforeCall());
    }

    @Test
    public void testExtensionSoureAssignmentCodeAfterCall() {

        Assert.assertEquals("", p.getDeclaredArguments().getCodeAfterCall());

        a1.setCustomArg(new MockType1());
        a2.setCustomArg(new MockType2());

        Assert.assertEquals("? : xreverse(p1);\n? : yreverse(p2);\n", p.getDeclaredArguments().getCodeAfterCall());
    }

    @Test
    public void testExtensionRebuildArgsForFunctionCall() {

        p = new MockPLProcedure(null);
        p.setSql("smcall");
        p.setFunction(true);
        a1 = p.createBooleanOutParameter("test1");
        a2 = p.createInParameter("test2", Types.VARCHAR);
        a3 = p.createBooleanOutParameter("test4");
        a4 = p.createOutParameter("test5", Types.VARCHAR);

        Assert.assertEquals(4, p.getDeclaredArguments().size());

        List<SqlArgument> list = p.getDeclaredArguments().rebuildArguments();

        assertEquals(4, list.size());

        assertEquals(a1, list.get(2));
        assertEquals(a2, list.get(0));
        assertEquals(a3, list.get(3));
        assertEquals(a4, list.get(1));

    }

    @Test
    public void testExtensionRebuildArgsForProcedureCall() {

        p = new MockPLProcedure(null);
        p.setSql("smcall");
        p.setFunction(false);// ***
        a1 = p.createBooleanOutParameter("test1");
        a2 = p.createInParameter("test2", Types.VARCHAR);
        a3 = p.createBooleanOutParameter("test4");
        a4 = p.createOutParameter("test5", Types.VARCHAR);

        Assert.assertEquals(4, p.getDeclaredArguments().size());

        List<SqlArgument> list = p.getDeclaredArguments().rebuildArguments();

        assertEquals(4, list.size());

        assertEquals(a1, list.get(2));
        assertEquals(a2, list.get(0));
        assertEquals(a3, list.get(3));
        assertEquals(a4, list.get(1));

    }

    @Test
    public void testGetArgument() {

        p = new MockPLProcedure(null);
        p.setSql("smcall");
        p.setFunction(false);// ***
        a1 = p.createBooleanOutParameter("test1");
        a2 = p.createInParameter("test2", Types.VARCHAR);
        a3 = p.createBooleanOutParameter("test4");
        a4 = p.createOutParameter("test5", Types.VARCHAR);

        Assert.assertEquals(4, p.getDeclaredArguments().size());

        Assert.assertEquals(a3, p.getDeclaredArguments().get("test4"));
        Assert.assertEquals(a1, p.getDeclaredArguments().get("test1"));
        Assert.assertEquals(null, p.getDeclaredArguments().get("test1dfdsff"));

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

    private class MockPLProcedure extends OraclePLProcedure {


        public MockPLProcedure(DbContext context) {
            super(context);
        }

        @Override
        protected void initDataSource() {

        }

    }
}
