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
