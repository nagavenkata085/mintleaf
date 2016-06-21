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
import org.junit.Before;
import org.junit.Test;
import org.qamatic.mintleaf.core.ExecuteQuery;
import org.qamatic.mintleaf.interfaces.db.OracleDbContext;
import org.qamatic.mintleaf.oracle.OracleDbAssert;
import org.qamatic.mintleaf.oracle.OracleDbHelper;
import org.qamatic.mintleaf.oracle.codeobjects.*;
import org.qamatic.mintleaf.oracle.junitsupport.OracleTestCase;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;

public class PLCreatePackageTest extends OracleTestCase {

    private static OracleDbHelper mvutils;


    @Before
    public void init() {
        if (mvutils == null) {
            mvutils = new OracleDbHelper(getSchemaOwnerContext());
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
        OracleDbAssert.assertPackageInterfaceExists(getOracleSchemaOwnerDbContext(), "MyPackage");

    }

    private OracleDbContext getOracleSchemaOwnerDbContext() {
        return (OracleDbContext) getSchemaOwnerContext();
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
        OracleDbAssert.assertPackageInterfaceExists(getOracleSchemaOwnerDbContext(), "MyPackage");
    }
}
