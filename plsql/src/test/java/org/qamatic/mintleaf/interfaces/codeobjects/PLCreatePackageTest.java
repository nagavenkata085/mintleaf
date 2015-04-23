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

public class PLCreatePackageTest extends OracleTestCase {

    private static DbUtility mvutils;


    @Before
    public void init() {
        if (mvutils == null) {
            mvutils = new DbUtility(getSchemaOwnerContext());
        }
    }

    @Test
    public void testCreatePackage() {
        Assert.assertEquals(String.format("create or replace package MyPackage as %n%nend;"), new PLCreatePackage("MyPackage").toString());
    }

    @Test
    public void testCreatePackageWithMembers() {
        PLCreatePackage p = new PLCreatePackage("MyPackage");
        p.addMemberField(new PLMemberField("x", "integer"));
        p.addMemberField(new PLMemberField("y", "varchar(200)"));
        p.addMemberField(new PLMemberField("z", "tablea%rowtype"));
        assertEquals(String.format("create or replace package MyPackage as %n\tx integer;%n\ty varchar(200);%n\tz tablea%%rowtype;%n%nend;"), p.toString());
    }

    @Test
    public void testCreatePackageWithMemberMethod() {
        PLCreatePackage p = new PLCreatePackage("MyPackage") {
            {
                PLMemberMethod m = new PLMemberMethod("MyMethod1");
                addMemberMethod(m);
                m = new PLMemberMethod("MyMethod2", "varchar2");
                addMemberMethod(m);
            }
        };

        assertEquals(String.format("create or replace package MyPackage as %n%n\tPROCEDURE MyMethod1;%n\tFUNCTION MyMethod2 return varchar2;%nend;"),
                p.toString());
    }

    @Test
    public void testCreatePackageWithMemberMethodInDb() throws SQLException, IOException {
        PLCreatePackage p = new PLCreatePackage("MyPackage") {
            {
                PLMemberMethod m = new PLMemberMethod("MyMethod1");
                addMemberMethod(m);
                m = new PLMemberMethod("MyMethod2", "varchar2");
                addMemberMethod(m);
            }
        };
        new ExecuteQuery().loadSource(getSchemaOwnerContext(), p.toString() + "\n/", "/");
        DbAssert.assertPackageInterfaceExists(getSchemaOwnerContext(), "MyPackage");

    }

    @Test
    public void testCreatePackageWithMemberMethodMemberParameter() {
        PLCreatePackage p = new PLCreatePackage("MyPackage") {
            {
                PLMemberMethod m = new PLMemberMethod("MyMethod1");
                addMemberMethod(m);
                m.addMemberMethodParameter(new PLMemberMethodParameter("x", PLMemberParameterDirection.IN, "number"));
                m = new PLMemberMethod("MyMethod2", "varchar2");
                addMemberMethod(m);
                m.addMemberMethodParameter(new PLMemberMethodParameter("y", PLMemberParameterDirection.INOUT, "varchar2") {
                    {
                        setNoCopy(true);
                    }
                });

            }
        };

        assertEquals(
                String.format("create or replace package MyPackage as %n%n\tPROCEDURE MyMethod1(x IN number);%n\tFUNCTION MyMethod2(y IN OUT NOCOPY varchar2) return varchar2;%nend;"),
                p.toString());
    }

    @Test
    public void testCreatePackageWithMemberMethodMemberParameterInDb() throws SQLException, IOException {
        PLCreatePackage p = new PLCreatePackage("MyPackage") {
            {
                PLMemberMethod m = new PLMemberMethod("MyMethod1");
                addMemberMethod(m);
                m.addMemberMethodParameter(new PLMemberMethodParameter("x", PLMemberParameterDirection.IN, "number"));
                m = new PLMemberMethod("MyMethod2", "varchar2");
                addMemberMethod(m);
                m.addMemberMethodParameter(new PLMemberMethodParameter("y", PLMemberParameterDirection.INOUT, "varchar2") {
                    {
                        setNoCopy(true);
                    }
                });

            }
        };

        new ExecuteQuery().loadSource(getSchemaOwnerContext(), p.toString() + "\n/", "/");
        DbAssert.assertPackageInterfaceExists(getSchemaOwnerContext(), "MyPackage");
    }
}
