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
import org.qamatic.mintleaf.oracle.MemberField;
import org.qamatic.mintleaf.oracle.codeobjects.PLAssignmentStatement;
import org.qamatic.mintleaf.oracle.codeobjects.PLMemberMethodBody;
import org.qamatic.mintleaf.oracle.codeobjects.PLMemberMethodParameter;
import org.qamatic.mintleaf.oracle.codeobjects.PLMemberParameterDirection;

import static org.junit.Assert.assertEquals;

public class PLMemberMethodBodyTest {

    @Test
    public void testMemberProcedure() {
        Assert.assertEquals(String.format("\tPROCEDURE MyMethod%n\tas%n\t%n\tbegin%n%n\tend;"), new PLMemberMethodBody("MyMethod").toString());

    }

    @Test
    public void testMemberFunction() {
        assertEquals(String.format("\tFUNCTION MyMethod return number%n\tas%n\t%n\tbegin%n%n\tend;"), new PLMemberMethodBody("MyMethod", "number").toString());
    }

    @Test
    public void testMemberProcedureParameters() {
        assertEquals(String.format("\tPROCEDURE MyMethod(x IN number)%n\tas%n\t\trowCount number;%n\t\ttemp_short_name varchar(200);%n%n\tbegin%n%n\tend;"),
                new PLMemberMethodBody("MyMethod") {
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
                String.format("\tPROCEDURE MyMethod(x IN number)%n\tas%n\t\trowCount number;%n\t\ttemp_short_name varchar(200);%n%n\tbegin%n\t\trowCount := 10;%n\t\ttemp_short_name := 'smaruth';%n%n\tend;"),
                new PLMemberMethodBody("MyMethod") {
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
        assertEquals(String.format("\tFUNCTION MyMethod(x IN number) return number%n\tas%n\t\trowCount number;%n\t\ttemp_short_name varchar(200);%n%n\tbegin%n%n\tend;"),
                new PLMemberMethodBody("MyMethod", "number") {
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
                String.format("\tFUNCTION MyMethod(x IN number, y IN OUT number, z OUT number) return number%n\tas%n\t\trowCount number;%n\t\ttemp_short_name varchar(200);%n%n\tbegin%n%n\tend;"),
                new PLMemberMethodBody("MyMethod", "number") {
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
                String.format("\tPROCEDURE MyMethod(x IN varchar2, y IN OUT number, z OUT number)%n\tas%n\t\trowCount number;%n\t\ttemp_short_name varchar(200);%n%n\tbegin%n%n\tend;"),
                new PLMemberMethodBody("MyMethod") {
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
