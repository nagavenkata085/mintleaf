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

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.qamatic.mintleaf.core.ExecuteQuery;
import org.qamatic.mintleaf.dbsupportimpls.oracle.OracleDbAssert;
import org.qamatic.mintleaf.dbsupportimpls.oracle.OracleDbContext;
import org.qamatic.mintleaf.oracle.OracleHelperScript;
import org.qamatic.mintleaf.oracle.codeobjects.*;
import org.qamatic.mintleaf.oracle.junitsupport.OracleTestCase;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;

public class PLCreatePackageBodyTest extends OracleTestCase {

    private static OracleHelperScript mvutils;


    @Before
    public void init() {
        if (mvutils == null) {
            mvutils = new OracleHelperScript(getSchemaOwnerContext());
        }
        mvutils.dropPackage("MyPackage");
        mvutils.dropPackage("A_Test_Package");
    }

    @After
    public void cleanup() {
        mvutils.dropPackage("MyPackage");
        mvutils.dropPackage("A_Test_Package");
    }

    @Test
    public void testCreatePackageBody() {
        Assert.assertEquals(String.format("create or replace package body MyPackage as %n%nend;%n"), new PLCreatePackageBody("MyPackage").toString());
    }

    @Test
    public void testCreatePackageBodyWithMembers() {
        PLCreatePackageBody p = new PLCreatePackageBody("MyPackage");
        p.addMemberField(new PLMemberField("x", "integer"));
        p.addMemberField(new PLMemberField("y", "varchar(200)"));
        p.addMemberField(new PLMemberField("z", "tablea%rowtype"));
        assertEquals(String.format("create or replace package body MyPackage as %n\tx integer;%n\ty varchar(200);%n\tz tablea%%rowtype;%n%nend;%n"),
                p.toString());
    }

    @Test
    public void testCreatePackageBodyWithMemberMethod() {
        PLCreatePackageBody p = new PLCreatePackageBody("MyPackage") {
            {
                PLMemberMethodBody m = new PLMemberMethodBody("MyMethod1");
                addMemberMethodBody(m);
                m.addMemberMethodDeclaration(new PLMemberField("rowCount", "number"));
            }
        };

        PLStringBuilder builder = new PLStringBuilder();

        builder.appendLine("create or replace package body MyPackage as ");
        builder.appendLine("");
        builder.appendLine("\tPROCEDURE MyMethod1");
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

    @Test
    public void testCreatePackageBodyWithMemberMethodInDb() throws SQLException, IOException {

        PLCreatePackage p1 = new PLCreatePackage("MyPackage") {
            {
                PLMemberMethod m = new PLMemberMethod("MyMethod1");
                addMemberMethod(m);

            }
        };


        new ExecuteQuery().loadSource(getSchemaOwnerContext(), p1.toString() + "\n/", "/");
        OracleDbAssert.assertPackageExists((OracleDbContext) getSchemaOwnerContext(), "MyPackage");

        PLCreatePackageBody p = new PLCreatePackageBody("MyPackage") {
            {
                PLMemberMethodBody m = new PLMemberMethodBody("MyMethod1");
                addMemberMethodBody(m);
                m.addMemberMethodDeclaration(new PLMemberField("rowCount", "number"));
                m.addStatement(new PLAssignmentStatement("rowCount", "10"));
            }
        };

        new ExecuteQuery().loadSource(getSchemaOwnerContext(), p.toString() + "\n/", "/");
        OracleDbAssert.assertPackageBodyExists((OracleDbContext) getSchemaOwnerContext(), "MyPackage");

    }

    @Test
    public void testCreatePackageBodyWithMemberMethodMemberParameterInDb() throws SQLException, IOException {

        PLCreatePackage p1 = new PLCreatePackage("A_Test_Package") {
            {
                PLMemberMethod m = new PLMemberMethod("transform");
                addMemberMethod(m);
                m.addMemberMethodParameter(new PLMemberMethodParameter("aObjectValue", PLMemberParameterDirection.INOUT, "varchar2") {
                    {
                        setNoCopy(true);
                    }
                });

            }
        };
        new ExecuteQuery().loadSource(getSchemaOwnerContext(), p1.toString() + "\n/", "/");
        OracleDbAssert.assertPackageExists((OracleDbContext) getSchemaOwnerContext(), "A_Test_Package");

        PLCreatePackageBody p = new PLCreatePackageBody("A_Test_Package") {
            {
                PLMemberMethodBody m = new PLMemberMethodBody("transform");
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
        OracleDbAssert.assertPackageBodyExists((OracleDbContext) getSchemaOwnerContext(), "A_Test_Package");
    }
}
