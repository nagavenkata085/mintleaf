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
import org.qamatic.mintleaf.oracle.codeobjects.PLCreateType;
import org.qamatic.mintleaf.oracle.codeobjects.PLTableColumnDef;
import org.qamatic.mintleaf.oracle.codeobjects.PLTypeMemberMethod;
import org.qamatic.mintleaf.oracle.junitsupport.OracleTestCase;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class PLCreateTypeTest extends OracleTestCase {

    private static DbUtility mvutils;


    @Before
    public void init() {
        if (mvutils == null) {
            mvutils = new DbUtility(getSchemaOwnerContext());
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
        DbAssert.assertTypeExists(getSchemaOwnerContext(), "MyObject");

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
        DbAssert.assertTypeExists(getSchemaOwnerContext(), "ParentObject");
        DbAssert.assertTypeExists(getSchemaOwnerContext(), "ChildObject");

    }

    @Test
    public void testCreateTypeInDb() {
        mvutils.dropType("MyObject");
        mvutils.createType("MyObject");

        DbAssert.assertTypeExists(getSchemaOwnerContext(), "MyObject");
        mvutils.dropType("MyObject");

    }

    @Test
    public void testCreateTypeUnderTypeInDb() {
        mvutils.dropType("ChildObject");
        mvutils.dropType("ParentObject");
        mvutils.createType("ParentObject");
        DbAssert.assertTypeExists(getSchemaOwnerContext(), "ParentObject");
        mvutils.createType("ChildObject", "ParentObject");
        DbAssert.assertTypeExists(getSchemaOwnerContext(), "ChildObject");
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
        DbAssert.assertTypeExists(getSchemaOwnerContext(), "MyObject");
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
        DbAssert.assertTypeExists(getSchemaOwnerContext(), "ParentObject");
        DbAssert.assertTypeExists(getSchemaOwnerContext(), "ChildObject");

    }

}
