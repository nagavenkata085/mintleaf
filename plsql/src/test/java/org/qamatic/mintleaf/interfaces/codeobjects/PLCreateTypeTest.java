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
import org.junit.Before;
import org.junit.Test;
import org.qamatic.mintleaf.core.ExecuteQuery;
import org.qamatic.mintleaf.oracle.OracleDbAssert;
import org.qamatic.mintleaf.dbs.oracle.OracleDbContext;
import org.qamatic.mintleaf.oracle.OracleHelperScript;
import org.qamatic.mintleaf.oracle.codeobjects.PLCreateType;
import org.qamatic.mintleaf.oracle.codeobjects.PLTableColumnDef;
import org.qamatic.mintleaf.oracle.codeobjects.PLTypeMemberMethod;
import org.qamatic.mintleaf.oracle.junitsupport.OracleTestCase;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class PLCreateTypeTest extends OracleTestCase {

    private static OracleHelperScript mvutils;


    @Before
    public void init() {
        if (mvutils == null) {
            mvutils = new OracleHelperScript(getSchemaOwnerContext());
        }
    }

    @Test
    public void testCreateType() {
        Assert.assertEquals("create or replace type MyObject as OBJECT(id number\n) not final;", new PLCreateType("MyObject").toString());
    }

    @Test
    public void testCreateTypeWIthSubCalss() {
        assertEquals("create or replace type MyObject UNDER TBusinessObject(\n) not final;", new PLCreateType("MyObject", "TBusinessObject").toString());
    }

    @Test
    public void testCreateTypeWithOneColumn() {
        PLCreateType t = new PLCreateType("MyObject");
        t.addColumnDef(new PLTableColumnDef("x", "varchar(100)"));
        assertEquals(String.format("create or replace type MyObject as OBJECT(%n\tx varchar(100)\n) not final;"), t.toString());
    }

    @Test
    public void testCreateTypeWithMultiColumn() {
        PLCreateType t = new PLCreateType("MyObject");
        t.addColumnDef(new PLTableColumnDef("x", "varchar(100)"));
        t.addColumnDef(new PLTableColumnDef("y", "number"));
        assertEquals(String.format("create or replace type MyObject as OBJECT(%n\tx varchar(100),%n\ty number\n) not final;"), t.toString());
    }

    @SuppressWarnings("serial")
    @Test
    public void testUtilsCreateTypeWithMultiColumnInDbNoInherit() {

        mvutils.createType("MyObject", new ArrayList<PLTableColumnDef>() {
            {
                add(new PLTableColumnDef("x", "varchar(100)"));
                add(new PLTableColumnDef("y", "number"));
            }
        });
        OracleDbAssert.assertTypeExists((OracleDbContext) getSchemaOwnerContext(), "MyObject");

    }

    @SuppressWarnings("serial")
    @Test
    public void testUtilsCreateTypeWithMultiColumnInDbWithInherit() {

        mvutils.createType("ParentObject");
        mvutils.createType("ChildObject", "ParentObject", new ArrayList<PLTableColumnDef>() {
            {
                add(new PLTableColumnDef("x", "varchar(100)"));
                add(new PLTableColumnDef("y", "number"));
            }
        });
        OracleDbAssert.assertTypeExists((OracleDbContext) getSchemaOwnerContext(), "ParentObject");
        OracleDbAssert.assertTypeExists((OracleDbContext) getSchemaOwnerContext(), "ChildObject");

    }

    @Test
    public void testCreateTypeInDb() {
        mvutils.dropType("MyObject");
        mvutils.createType("MyObject");

        OracleDbAssert.assertTypeExists((OracleDbContext) getSchemaOwnerContext(), "MyObject");
        mvutils.dropType("MyObject");

    }

    @Test
    public void testCreateTypeUnderTypeInDb() {
        mvutils.dropType("ChildObject");
        mvutils.dropType("ParentObject");
        mvutils.createType("ParentObject");
        OracleDbAssert.assertTypeExists((OracleDbContext) getSchemaOwnerContext(), "ParentObject");
        mvutils.createType("ChildObject", "ParentObject");
        OracleDbAssert.assertTypeExists((OracleDbContext) getSchemaOwnerContext(), "ChildObject");
        mvutils.dropType("ChildObject");
        mvutils.dropType("ParentObject");

    }

    @Test
    public void testCreateTypeWithMemberMethod() throws SQLException, IOException {
        PLCreateType p = new PLCreateType("MyObject") {
            {
                addColumnDef(new PLTableColumnDef("id", "number"));
                addColumnDef(new PLTableColumnDef("name", "varchar(100)"));
                PLTypeMemberMethod m = new PLTypeMemberMethod();
                addMemberMethod(m);
                m = new PLTypeMemberMethod("MyMethod2", "varchar2");
                addMemberMethod(m);
            }
        };

        assertEquals(
                String.format("create or replace type MyObject as OBJECT(%n\tid number,%n\tname varchar(100),%n\tCONSTRUCTOR FUNCTION MyObject return SELF AS RESULT,%n\tMEMBER FUNCTION MyMethod2 return varchar2\n) not final;"),
                p.toString());

        new ExecuteQuery().loadSource(getSchemaOwnerContext(), p.toString() + "\n/", "/");
        OracleDbAssert.assertTypeExists((OracleDbContext) getSchemaOwnerContext(), "MyObject");
    }

    @SuppressWarnings("serial")
    @Test
    public void testUtilsCreateTypeHavingConstructor() {

        mvutils.createType("ParentObject");
        mvutils.createType("ChildObject", "ParentObject", new ArrayList<PLTableColumnDef>() {
            {
                add(new PLTableColumnDef("x", "varchar(100)"));
                add(new PLTableColumnDef("y", "number"));
            }
        });
        OracleDbAssert.assertTypeExists((OracleDbContext) getSchemaOwnerContext(), "ParentObject");
        OracleDbAssert.assertTypeExists((OracleDbContext) getSchemaOwnerContext(), "ChildObject");

    }

}
