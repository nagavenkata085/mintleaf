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

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.qamatic.mintleaf.core.ExecuteQuery;
import org.qamatic.mintleaf.oracle.DbAssert;
import org.qamatic.mintleaf.oracle.DbUtility;
import org.qamatic.mintleaf.oracle.codeobjects.*;
import org.qamatic.mintleaf.oracle.junitsupport.OracleTestCase;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;

public class PLCreatePackageBodyTest extends OracleTestCase {

    private static DbUtility mvutils;


    @Before
    public void init() {
        if (mvutils == null) {
            mvutils = new DbUtility(getSchemaOwnerContext());
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
        DbAssert.assertPackageExists(getSchemaOwnerContext(), "MyPackage");

        PLCreatePackageBody p = new PLCreatePackageBody("MyPackage") {
            {
                PLMemberMethodBody m = new PLMemberMethodBody("MyMethod1");
                addMemberMethodBody(m);
                m.addMemberMethodDeclaration(new PLMemberField("rowCount", "number"));
                m.addStatement(new PLAssignmentStatement("rowCount", "10"));
            }
        };

        new ExecuteQuery().loadSource(getSchemaOwnerContext(), p.toString() + "\n/", "/");
        DbAssert.assertPackageBodyExists(getSchemaOwnerContext(), "MyPackage");

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
        DbAssert.assertPackageExists(getSchemaOwnerContext(), "A_Test_Package");

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
        DbAssert.assertPackageBodyExists(getSchemaOwnerContext(), "A_Test_Package");
    }
}
