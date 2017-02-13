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

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.qamatic.mintleaf.core.ExecuteQuery;
import org.qamatic.mintleaf.dbs.oracle.OracleDbAssert;
import org.qamatic.mintleaf.dbs.oracle.OracleDbContext;
import org.qamatic.mintleaf.oracle.OracleHelperScript;
import org.qamatic.mintleaf.oracle.codeobjects.*;
import org.qamatic.mintleaf.oracle.junitsupport.OracleTestCase;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;

public class PLCreateTypeBodyTest extends OracleTestCase {

    private static OracleHelperScript mvutils;


    @Before
    public void init() {
        if (mvutils == null) {
            mvutils = new OracleHelperScript(getSchemaOwnerContext());
        }
        mvutils.dropType("MyObject");
        mvutils.dropType("A_Test_Type");
    }

    @After
    public void cleanup() {

    }

    @Test
    public void testCreatetypeBody() {
        Assert.assertEquals(String.format("create or replace type body MyObject as %nend;%n"), new PLCreateTypeBody("MyObject").toString());
    }

    @Test
    public void testCreateTypeBodyWithMemberMethod() {
        PLCreateTypeBody p = new PLCreateTypeBody("MyObject") {
            {
                PLTypeMemberMethodBody m = new PLTypeMemberMethodBody("MyMethod1");
                addMemberMethodBody(m);
                m.addMemberMethodDeclaration(new PLMemberField("rowCount", "number"));
            }
        };

        PLStringBuilder builder = new PLStringBuilder();

        builder.appendLine("create or replace type body MyObject as ");
        builder.appendLine("\tMEMBER PROCEDURE MyMethod1");
        builder.appendLine("\tas");
        builder.appendLine("\t\trowCount number;");
        builder.appendLine("");

        builder.appendLine("\tbegin");
        builder.appendLine("");
        builder.appendLine("\tend;");
        builder.appendLine("");
        builder.appendLine("end;");

        assertEquals(builder.toString(), p.toString());
    }

    private OracleDbContext getOracleDbContext() {
        return (OracleDbContext) getSchemaOwnerContext();
    }

    @Test
    public void testCreateTypeBodyWithMemberMethodInDb() throws SQLException, IOException {

        PLCreateType p1 = new PLCreateType("MyObject") {
            {
                addColumnDef(new PLTableColumnDef("id", "number"));
                PLTypeMemberMethod m = new PLTypeMemberMethod();
                addMemberMethod(m);
                m.setConstructor(true);

                m = new PLTypeMemberMethod("MyMethod1");
                addMemberMethod(m);

            }
        };
        new ExecuteQuery().loadSource(getSchemaOwnerContext(), p1.toString() + "\n/", "/");
        OracleDbAssert.assertTypeExists(getOracleDbContext(), "MyObject");

        PLCreateTypeBody p = new PLCreateTypeBody("MyObject") {
            {
                PLTypeMemberMethodBody m = new PLTypeMemberMethodBody();
                addMemberMethodBody(m);
                m.setConstructor(true);

                m = new PLTypeMemberMethodBody("MyMethod1");
                addMemberMethodBody(m);
                m.addMemberMethodDeclaration(new PLMemberField("rowCount", "number"));
                m.addStatement(new PLAssignmentStatement("rowCount", "10"));
            }
        };

        new ExecuteQuery().loadSource(getSchemaOwnerContext(), p.toString() + "\n/", "/");
        OracleDbAssert.assertTypeBodyExists(getOracleDbContext(), "MyObject");

    }

    @Test
    public void testCreateTypeBodyWithMemberMethodMemberParameterInDb() throws SQLException, IOException {

        PLCreateType p1 = new PLCreateType("A_Test_Type") {
            {
                addColumnDef(new PLTableColumnDef("id", "number"));
                PLTypeMemberMethod m = new PLTypeMemberMethod();
                addMemberMethod(m);
                m.setConstructor(true);

                m = new PLTypeMemberMethod("testmethod");
                addMemberMethod(m);
                m.addMemberMethodParameter(new PLMemberMethodParameter("aObjectValue", PLMemberParameterDirection.INOUT, "varchar2") {
                    {
                        setNoCopy(true);
                    }
                });

            }
        };
        new ExecuteQuery().loadSource(getSchemaOwnerContext(), p1.toString() + "\n/", "/");
        OracleDbAssert.assertTypeExists(getOracleDbContext(), "A_Test_Type");

        PLCreateTypeBody p = new PLCreateTypeBody("A_Test_Type") {
            {
                PLTypeMemberMethodBody m = new PLTypeMemberMethodBody();
                addMemberMethodBody(m);
                m.setConstructor(true);

                m = new PLTypeMemberMethodBody("testmethod");
                addMemberMethodBody(m);
                m.addMemberMethodParameter(new PLMemberMethodParameter("aObjectValue", PLMemberParameterDirection.INOUT, "varchar2") {
                    {
                        setNoCopy(true);
                    }
                });

                m.addStatement(new PLAssignmentStatement("aObjectValue", "'smaruth'"));
            }
        };

        new ExecuteQuery().loadSource(getSchemaOwnerContext(), p.toString() + "\n/", "/");
        OracleDbAssert.assertTypeExists(getOracleDbContext(), "A_Test_Type");
    }
}
