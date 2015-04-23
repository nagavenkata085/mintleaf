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

public class PLCreateTypeBodyTest extends OracleTestCase {

    private static DbUtility mvutils;


    @Before
    public void init() {
        if (mvutils == null) {
            mvutils = new DbUtility(getSchemaOwnerContext());
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
        DbAssert.assertTypeExists(getSchemaOwnerContext(), "MyObject");

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
        DbAssert.assertTypeBodyExists(getSchemaOwnerContext(), "MyObject");

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
        DbAssert.assertTypeExists(getSchemaOwnerContext(), "A_Test_Type");

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
        DbAssert.assertTypeExists(getSchemaOwnerContext(), "A_Test_Type");
    }
}
