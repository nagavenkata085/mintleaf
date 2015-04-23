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
import org.qamatic.mintleaf.interfaces.MemberField;
import org.qamatic.mintleaf.oracle.codeobjects.PLAssignmentStatement;
import org.qamatic.mintleaf.oracle.codeobjects.PLMemberMethodParameter;
import org.qamatic.mintleaf.oracle.codeobjects.PLMemberParameterDirection;
import org.qamatic.mintleaf.oracle.codeobjects.PLTypeMemberMethodBody;

import static org.junit.Assert.assertEquals;

public class PLTypeMemberMethodBodyTest {

    @Test
    public void testMemberProcedure() {
        Assert.assertEquals(String.format("\tMEMBER PROCEDURE MyMethod%n\tas%n\t%n\tbegin%n%n\tend;"), new PLTypeMemberMethodBody("MyMethod").toString());

    }

    @Test
    public void testMemberStatic() {
        PLTypeMemberMethodBody m = new PLTypeMemberMethodBody("MyMethod");
        m.setStatic(true);
        assertEquals(String.format("\tSTATIC PROCEDURE MyMethod%n\tas%n\t%n\tbegin%n%n\tend;"), m.toString());
    }

    @Test
    public void testMemberOverriding() {
        PLTypeMemberMethodBody m = new PLTypeMemberMethodBody("MyMethod");
        m.setOverride(true);
        assertEquals(String.format("\tOVERRIDING MEMBER PROCEDURE MyMethod%n\tas%n\t%n\tbegin%n%n\tend;"), m.toString());
    }

    @Test
    public void testMemberConstructor() {
        PLTypeMemberMethodBody m = new PLTypeMemberMethodBody("MyMethod");
        m.setConstructor(true);
        assertEquals(String.format("\tCONSTRUCTOR FUNCTION MyMethod return SELF AS RESULT%n\tas%n\t%n\tbegin%n%n\tRETURN;%n\tend;"), m.toString());
    }

    @Test
    public void testMemberConstructor1() {
        assertEquals(String.format("\tCONSTRUCTOR FUNCTION MyMethod(x IN number) return SELF AS RESULT%n\tas%n\t%n\tbegin%n%n\tRETURN;%n\tend;"),
                new PLTypeMemberMethodBody("MyMethod") {
                    {
                        setConstructor(true);
                        addMemberMethodParameter(new PLMemberMethodParameter("x", PLMemberParameterDirection.IN, "number"));
                    }
                }.toString());

    }

    @Test
    public void testMemberFunction() {
        assertEquals(String.format("\tMEMBER FUNCTION MyMethod return number%n\tas%n\t%n\tbegin%n%n\tend;"),
                new PLTypeMemberMethodBody("MyMethod", "number").toString());
    }

    @Test
    public void testMemberProcedureParameters() {
        assertEquals(
                String.format("\tMEMBER PROCEDURE MyMethod(x IN number)%n\tas%n\t\trowCount number;%n\t\ttemp_short_name varchar(200);%n%n\tbegin%n%n\tend;"),
                new PLTypeMemberMethodBody("MyMethod") {
                    {

                        addMemberMethodParameter(new PLMemberMethodParameter("x", PLMemberParameterDirection.IN, "number"));
                        addMemberMethodDeclaration(new MemberField("rowCount", "number") {
                        });
                        addMemberMethodDeclaration(new MemberField("temp_short_name", "varchar(200)") {
                        });
                    }
                }.toString());
    }

    @Test
    public void testMemberProcedureParametersWithStmts() {
        assertEquals(
                String.format("\tMEMBER PROCEDURE MyMethod(x IN number)%n\tas%n\t\trowCount number;%n\t\ttemp_short_name varchar(200);%n%n\tbegin%n\t\trowCount := 10;%n\t\ttemp_short_name := 'smaruth';%n%n\tend;"),
                new PLTypeMemberMethodBody("MyMethod") {
                    {

                        addMemberMethodParameter(new PLMemberMethodParameter("x", PLMemberParameterDirection.IN, "number"));
                        addMemberMethodDeclaration(new MemberField("rowCount", "number") {
                        });
                        addMemberMethodDeclaration(new MemberField("temp_short_name", "varchar(200)") {
                        });
                        addStatement(new PLAssignmentStatement("rowCount", "10"));
                        addStatement(new PLAssignmentStatement("temp_short_name", "'smaruth'"));
                    }
                }.toString());
    }

    @Test
    public void testMemberFunctionParameters() {
        assertEquals(
                String.format("\tMEMBER FUNCTION MyMethod(x IN number) return number%n\tas%n\t\trowCount number;%n\t\ttemp_short_name varchar(200);%n%n\tbegin%n%n\tend;"),
                new PLTypeMemberMethodBody("MyMethod", "number") {
                    {

                        addMemberMethodParameter(new PLMemberMethodParameter("x", PLMemberParameterDirection.IN, "number"));
                        addMemberMethodDeclaration(new MemberField("rowCount", "number") {
                        });
                        addMemberMethodDeclaration(new MemberField("temp_short_name", "varchar(200)") {
                        });
                    }
                }.toString());

    }

    @Test
    public void testMemberFunctionParametersMore() {
        assertEquals(
                String.format("\tMEMBER FUNCTION MyMethod(x IN number, y IN OUT number, z OUT number) return number%n\tas%n\t\trowCount number;%n\t\ttemp_short_name varchar(200);%n%n\tbegin%n%n\tend;"),
                new PLTypeMemberMethodBody("MyMethod", "number") {
                    {
                        addMemberMethodParameter(new PLMemberMethodParameter("x", PLMemberParameterDirection.IN, "number"));
                        addMemberMethodParameter(new PLMemberMethodParameter("y", PLMemberParameterDirection.INOUT, "number"));
                        addMemberMethodParameter(new PLMemberMethodParameter("z", PLMemberParameterDirection.OUT, "number"));
                        addMemberMethodDeclaration(new MemberField("rowCount", "number") {
                        });
                        addMemberMethodDeclaration(new MemberField("temp_short_name", "varchar(200)") {
                        });
                    }
                }.toString());
    }

    @Test
    public void testMemberProcedureParametersMore() {
        assertEquals(
                String.format("\tMEMBER PROCEDURE MyMethod(x IN varchar2, y IN OUT number, z OUT number)%n\tas%n\t\trowCount number;%n\t\ttemp_short_name varchar(200);%n%n\tbegin%n%n\tend;"),
                new PLTypeMemberMethodBody("MyMethod") {
                    {
                        addMemberMethodParameter(new PLMemberMethodParameter("x", PLMemberParameterDirection.IN, "varchar2"));
                        addMemberMethodParameter(new PLMemberMethodParameter("y", PLMemberParameterDirection.INOUT, "number"));
                        addMemberMethodParameter(new PLMemberMethodParameter("z", PLMemberParameterDirection.OUT, "number"));
                        addMemberMethodDeclaration(new MemberField("rowCount", "number") {
                        });
                        addMemberMethodDeclaration(new MemberField("temp_short_name", "varchar(200)") {
                        });
                    }
                }.toString());
    }

}
