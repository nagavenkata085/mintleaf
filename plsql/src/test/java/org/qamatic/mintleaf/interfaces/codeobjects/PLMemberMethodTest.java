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

package org.qamatic.mintleaf.interfaces.codeobjects;

import org.junit.Assert;
import org.junit.Test;
import org.qamatic.mintleaf.oracle.codeobjects.PLMemberMethod;
import org.qamatic.mintleaf.oracle.codeobjects.PLMemberMethodParameter;
import org.qamatic.mintleaf.oracle.codeobjects.PLMemberParameterDirection;

import static org.junit.Assert.assertEquals;

public class PLMemberMethodTest {

    @Test
    public void testMemberProcedure() {
        Assert.assertEquals("\tPROCEDURE MyMethod", new PLMemberMethod("MyMethod").toString());

    }

    @Test
    public void testMemberFunction() {
        assertEquals("\tFUNCTION MyMethod return number", new PLMemberMethod("MyMethod", "number").toString());
    }

    @Test
    public void testMemberParameterIN() {
        PLMemberMethodParameter mp = new PLMemberMethodParameter("x", PLMemberParameterDirection.IN, "number");
        assertEquals("x IN number", mp.toString());
        mp.setNoCopy(true);
        assertEquals("x IN number", mp.toString());
    }

    @Test
    public void testMemberParameterOUT() {
        PLMemberMethodParameter mp = new PLMemberMethodParameter("x", PLMemberParameterDirection.OUT, "number");
        assertEquals("x OUT number", mp.toString());
        mp.setNoCopy(true);
        assertEquals("x OUT number", mp.toString());
    }

    @Test
    public void testMemberParameterINOUT() {
        PLMemberMethodParameter mp = new PLMemberMethodParameter("x", PLMemberParameterDirection.INOUT, "number");
        assertEquals("x IN OUT number", mp.toString());
        mp.setNoCopy(true);
        assertEquals("x IN OUT NOCOPY number", mp.toString());
    }

    @Test
    public void testMemberProcedureParameters() {
        assertEquals("\tPROCEDURE MyMethod(x IN number)", new PLMemberMethod("MyMethod") {
            {

                addMemberMethodParameter(new PLMemberMethodParameter("x", PLMemberParameterDirection.IN, "number"));
            }
        }.toString());

        assertEquals("\tPROCEDURE MyMethod(x OUT number)", new PLMemberMethod("MyMethod") {
            {

                addMemberMethodParameter(new PLMemberMethodParameter("x", PLMemberParameterDirection.OUT, "number"));
            }
        }.toString());

        assertEquals("\tPROCEDURE MyMethod(x IN OUT number)", new PLMemberMethod("MyMethod") {
            {

                addMemberMethodParameter(new PLMemberMethodParameter("x", PLMemberParameterDirection.INOUT, "number"));
            }
        }.toString());
    }

    @Test
    public void testMemberFunctionParameters() {
        assertEquals("\tFUNCTION MyMethod(x IN number) return number", new PLMemberMethod("MyMethod", "number") {
            {

                addMemberMethodParameter(new PLMemberMethodParameter("x", PLMemberParameterDirection.IN, "number"));
            }
        }.toString());

        assertEquals("\tFUNCTION MyMethod(x OUT number) return number", new PLMemberMethod("MyMethod", "number") {
            {

                addMemberMethodParameter(new PLMemberMethodParameter("x", PLMemberParameterDirection.OUT, "number"));
            }
        }.toString());

        assertEquals("\tFUNCTION MyMethod(x IN OUT number) return number", new PLMemberMethod("MyMethod", "number") {
            {

                addMemberMethodParameter(new PLMemberMethodParameter("x", PLMemberParameterDirection.INOUT, "number"));
            }
        }.toString());
    }

    @Test
    public void testMemberFunctionParametersMore() {
        assertEquals("\tFUNCTION MyMethod(x IN number, y IN OUT number, z OUT number) return number", new PLMemberMethod("MyMethod", "number") {
            {
                addMemberMethodParameter(new PLMemberMethodParameter("x", PLMemberParameterDirection.IN, "number"));
                addMemberMethodParameter(new PLMemberMethodParameter("y", PLMemberParameterDirection.INOUT, "number"));
                addMemberMethodParameter(new PLMemberMethodParameter("z", PLMemberParameterDirection.OUT, "number"));
            }
        }.toString());
    }

    @Test
    public void testMemberProcedureParametersMore() {
        assertEquals("\tPROCEDURE MyMethod(x IN varchar2, y IN OUT number, z OUT number)", new PLMemberMethod("MyMethod") {
            {
                addMemberMethodParameter(new PLMemberMethodParameter("x", PLMemberParameterDirection.IN, "varchar2"));
                addMemberMethodParameter(new PLMemberMethodParameter("y", PLMemberParameterDirection.INOUT, "number"));
                addMemberMethodParameter(new PLMemberMethodParameter("z", PLMemberParameterDirection.OUT, "number"));
            }
        }.toString());
    }

}
