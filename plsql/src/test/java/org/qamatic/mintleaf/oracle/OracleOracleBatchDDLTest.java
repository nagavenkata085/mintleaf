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

package org.qamatic.mintleaf.oracle;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.qamatic.mintleaf.core.SqlDDL;
import org.qamatic.mintleaf.core.SqlObjectInfo;
import org.qamatic.mintleaf.dbsupportimpls.oracle.OracleDbAssert;
import org.qamatic.mintleaf.interfaces.DbContext;
import org.qamatic.mintleaf.interfaces.SqlReader;
import org.qamatic.mintleaf.oracle.junitsupport.OracleTestCase;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;

public class OracleOracleBatchDDLTest extends OracleTestCase {


    private SqlDDL mvdropScript;

    @Before
    public void init() throws SQLException, IOException {

        mvdropScript = new TestDDLDrop(getSchemaOwnerContext());
        mvdropScript.create();

    }

    @After
    public void cleanUp() throws SQLException, IOException {
        mvdropScript.create();
    }

    @Test
    public void testDynamicDdlScript() throws SQLException, IOException {
        OracleBatchDDL dynObject = new TestDDLCreate(getSchemaOwnerContext());
        dynObject.create();

        StringBuilder builder = new StringBuilder();

        builder.append("declare \n");
        builder.append(" begin \n");
        builder.append("\n");
        builder.append("execute immediate 'CREATE TABLE TABLE1\n");
        builder.append("(\n");
        builder.append("\n");
        builder.append("ID NUMBER (18)  NOT NULL ,\n");
        builder.append("NAME VARCHAR2 (60 CHAR)  NOT NULL\n");
        builder.append("\n");
        builder.append(")';\n");
        builder.append("\n");
        builder.append("execute immediate 'CREATE TABLE TABLE2\n");
        builder.append("(\n");
        builder.append("ID NUMBER (18)  NOT NULL ,\n");
        builder.append("NAME VARCHAR2 (60 CHAR)  NOT NULL\n");
        builder.append(")';\n");
        builder.append("\n");
        builder.append("end;");

        assertEquals(builder.toString(), dynObject.getSql());

    }

    @Test
    public void testDynamicDdlScriptExecution() throws SQLException, IOException {
        OracleBatchDDL dynObject = new TestDDLExecute(getSchemaOwnerContext());
        dynObject.create();

        OracleDbAssert.assertTableExists(getSchemaOwnerContext(), "table1");
        OracleDbAssert.assertTableExists(getSchemaOwnerContext(), "TABLE2");
    }

    @SqlObjectInfo(name = "createschema", source = "/Testddl.sql")
    private class TestDDLCreate extends OracleBatchDDL {

        public TestDDLCreate(DbContext context) {
            super(context);

        }

    }

    @SqlObjectInfo(name = "createschema", source = "/Testddl.sql")
    private class TestDDLExecute extends OracleBatchDDL {

        public TestDDLExecute(DbContext context) {
            super(context);

        }

    }

    @SqlObjectInfo(name = "a test ddl", source = "/Testddl_drop.sql")
    private class TestDDLDrop extends SqlDDL {

        public TestDDLDrop(DbContext context) {
            super(context);
        }

        @Override
        protected SqlReader getCreateSourceReader(InputStream stream) {
            SqlReader reader = super.getCreateSourceReader(stream);
            reader.setDelimiter("/");
            return reader;
        }
    }
}
