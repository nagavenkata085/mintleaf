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
