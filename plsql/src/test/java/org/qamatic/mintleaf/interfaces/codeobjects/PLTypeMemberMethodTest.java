/*
 * <!--
 *   ~
 *   ~ The MIT License (MIT)
 *   ~
 *   ~ Copyright (c) 2010-2017 Senthil Maruthaiappan
 *   ~
 *   ~ Permission is hereby granted, free of charge, to any person obtaining a copy
 *   ~ of this software and associated documentation files (the "Software"), to deal
 *   ~ in the Software without restriction, including without limitation the rights
 *   ~ to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   ~ copies of the Software, and to permit persons to whom the Software is
 *   ~ furnished to do so, subject to the following conditions:
 *   ~
 *   ~ The above copyright notice and this permission notice shall be included in all
 *   ~ copies or substantial portions of the Software.
 *   ~
 *   ~ THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   ~ IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   ~ FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *   ~ AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *   ~ LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   ~ OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *   ~ SOFTWARE.
 *   ~
 *   ~
 *   -->
 */

package org.qamatic.mintleaf.interfaces.codeobjects;

import org.junit.Assert;
import org.junit.Test;
import org.qamatic.mintleaf.oracle.codeobjects.PLMemberMethodParameter;
import org.qamatic.mintleaf.oracle.codeobjects.PLMemberParameterDirection;
import org.qamatic.mintleaf.oracle.codeobjects.PLTypeMemberMethod;

import static org.junit.Assert.assertEquals;

public class PLTypeMemberMethodTest {

    @Test
    public void testMemberProcedure() {
        Assert.assertEquals("\tMEMBER PROCEDURE MyMethod", new PLTypeMemberMethod("MyMethod").toString());

    }

    @Test
    public void testMemberStatic() {
        PLTypeMemberMethod m = new PLTypeMemberMethod("MyMethod");
        m.setStatic(true);
        assertEquals("\tSTATIC PROCEDURE MyMethod", m.toString());
    }

    @Test
    public void testMemberOverriding() {
        PLTypeMemberMethod m = new PLTypeMemberMethod("MyMethod");
        m.setOverride(true);
        assertEquals("\tOVERRIDING MEMBER PROCEDURE MyMethod", m.toString());
    }

    @Test
    public void testMemberConstructor() {
        PLTypeMemberMethod m = new PLTypeMemberMethod("MyMethod");
        m.setConstructor(true);
        assertEquals("\tCONSTRUCTOR FUNCTION MyMethod return SELF AS RESULT", m.toString());
    }

    @Test
    public void testMemberConstructor1() {
        assertEquals("\tCONSTRUCTOR FUNCTION MyMethod(x IN number) return SELF AS RESULT", new PLTypeMemberMethod("MyMethod") {
            {
                setConstructor(true);
                addMemberMethodParameter(new PLMemberMethodParameter("x", PLMemberParameterDirection.IN, "number"));
            }
        }.toString());

    }

    @Test
    public void testMemberFunction() {
        assertEquals("\tMEMBER FUNCTION MyMethod return number", new PLTypeMemberMethod("MyMethod", "number").toString());
    }

    @Test
    public void testMemberProcedureParameters() {
        assertEquals("\tMEMBER PROCEDURE MyMethod(x IN number)", new PLTypeMemberMethod("MyMethod") {
            {

                addMemberMethodParameter(new PLMemberMethodParameter("x", PLMemberParameterDirection.IN, "number"));
            }
        }.toString());

        assertEquals("\tMEMBER PROCEDURE MyMethod(x OUT number)", new PLTypeMemberMethod("MyMethod") {
            {

                addMemberMethodParameter(new PLMemberMethodParameter("x", PLMemberParameterDirection.OUT, "number"));
            }
        }.toString());

        assertEquals("\tMEMBER PROCEDURE MyMethod(x IN OUT number)", new PLTypeMemberMethod("MyMethod") {
            {

                addMemberMethodParameter(new PLMemberMethodParameter("x", PLMemberParameterDirection.INOUT, "number"));
            }
        }.toString());
    }

    @Test
    public void testMemberFunctionParameters() {
        assertEquals("\tMEMBER FUNCTION MyMethod(x IN number) return number", new PLTypeMemberMethod("MyMethod", "number") {
            {

                addMemberMethodParameter(new PLMemberMethodParameter("x", PLMemberParameterDirection.IN, "number"));
            }
        }.toString());

        assertEquals("\tMEMBER FUNCTION MyMethod(x OUT number) return number", new PLTypeMemberMethod("MyMethod", "number") {
            {

                addMemberMethodParameter(new PLMemberMethodParameter("x", PLMemberParameterDirection.OUT, "number"));
            }
        }.toString());

        assertEquals("\tMEMBER FUNCTION MyMethod(x IN OUT number) return number", new PLTypeMemberMethod("MyMethod", "number") {
            {

                addMemberMethodParameter(new PLMemberMethodParameter("x", PLMemberParameterDirection.INOUT, "number"));
            }
        }.toString());
    }

    @Test
    public void testMemberFunctionParametersMore() {
        assertEquals("\tMEMBER FUNCTION MyMethod(x IN number, y IN OUT number, z OUT number) return number", new PLTypeMemberMethod("MyMethod", "number") {
            {
                addMemberMethodParameter(new PLMemberMethodParameter("x", PLMemberParameterDirection.IN, "number"));
                addMemberMethodParameter(new PLMemberMethodParameter("y", PLMemberParameterDirection.INOUT, "number"));
                addMemberMethodParameter(new PLMemberMethodParameter("z", PLMemberParameterDirection.OUT, "number"));
            }
        }.toString());
    }

    @Test
    public void testMemberProcedureParametersMore() {
        assertEquals("\tMEMBER PROCEDURE MyMethod(x IN varchar2, y IN OUT number, z OUT number)", new PLTypeMemberMethod("MyMethod") {
            {
                addMemberMethodParameter(new PLMemberMethodParameter("x", PLMemberParameterDirection.IN, "varchar2"));
                addMemberMethodParameter(new PLMemberMethodParameter("y", PLMemberParameterDirection.INOUT, "number"));
                addMemberMethodParameter(new PLMemberMethodParameter("z", PLMemberParameterDirection.OUT, "number"));
            }
        }.toString());
    }

}
