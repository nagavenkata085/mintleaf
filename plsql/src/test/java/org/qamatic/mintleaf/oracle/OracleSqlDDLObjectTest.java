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

public class OracleSqlDDLObjectTest extends OracleTestCase {


    private SqlDDL mvSqlDdl;

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
    public void testDdlCheckForTablesCreated() {
        DbAssert.assertTableExists(getSchemaOwnerContext(), "table1");
        DbAssert.assertTableExists(getSchemaOwnerContext(), "TABLE2");
    }

    @SqlObjectInfo(name = "a test ddl", source = "/Testddl.sql", dropSource = "/Testddl_drop.sql")
    public class TestDDL extends SqlDDL {

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
