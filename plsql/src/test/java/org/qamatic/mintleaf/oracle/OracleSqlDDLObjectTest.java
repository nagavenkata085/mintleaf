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

package org.qamatic.mintleaf.oracle;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.qamatic.mintleaf.DbContext;
import org.qamatic.mintleaf.MintLeafException;
import org.qamatic.mintleaf.SqlReader;
import org.qamatic.mintleaf.core.FluentJdbc;
import org.qamatic.mintleaf.oracle.core.SqlObjectInfo;
import org.qamatic.mintleaf.oracle.junitsupport.OracleTestCase;
import org.qamatic.mintleaf.tools.DbImportSource;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;

public class OracleSqlDDLObjectTest extends OracleTestCase {


    private SqlScriptDDL mvSqlDdl;

    @Before
    public void init() throws SQLException, IOException {

        mvSqlDdl = new TestDDL(getSchemaOwnerContext());
        mvSqlDdl.dropAll();
        mvSqlDdl.createAll();
    }

    @After
    public void cleanUp() throws SQLException, IOException {
        mvSqlDdl.dropAll();
    }

    @Test
    public void testDdlCheckForTablesCreated() throws SQLException {
        OracleDbAssert.assertTableExists(getSchemaOwnerContext(), "table1");
        OracleDbAssert.assertTableExists(getSchemaOwnerContext(), "TABLE2");
    }

    @Test
    public void testImportDbToDb() throws SQLException, IOException, MintLeafException {
        assertEquals(3, getSchemaOwnerContext().getCount("table1"));
        assertEquals(0, getSchemaOwnerContext().getCount("table2"));

        FluentJdbc fluentJdbc = getSchemaOwnerContext().newQuery().withSql("SELECT * FROM TABLE1");

        getSchemaOwnerContext().importDataFrom(new DbImportSource(fluentJdbc.getResultSet()),
                "INSERT INTO TABLE2 (ID, NAME) VALUES ($ID$, '$NAME$')");
        assertEquals(3, getSchemaOwnerContext().getCount("TABLE2"));


    }

    @SqlObjectInfo(name = "a test ddl", source = "res:/Testddl.sql", dropSource = "res:/Testddl_drop.sql")
    public class TestDDL extends SqlScriptDDL {

        public TestDDL(DbContext context) {
            super(context);

        }

        @Override
        protected SqlReader getDropSourceReader(InputStream stream) {
            SqlReader reader = super.getDropSourceReader(stream);
            reader.setDelimiter("/");
            return reader;
        }

    }
}
