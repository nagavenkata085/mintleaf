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

package org.qamatic.mintleaf.oracle;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.qamatic.mintleaf.core.SqlDDL;
import org.qamatic.mintleaf.core.SqlObjectInfo;
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

        DbAssert.assertTableExists(getSchemaOwnerContext(), "table1");
        DbAssert.assertTableExists(getSchemaOwnerContext(), "TABLE2");
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
