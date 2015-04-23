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

package org.qamatic.mintleaf.oracle.junitsupport;

import org.junit.Test;
import org.qamatic.mintleaf.core.ExecuteQuery;
import org.qamatic.mintleaf.oracle.DbAssert;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ExecuteQueryTest extends OracleTestCase {


    @Test
    public void testLoadFromFile() throws SQLException, IOException {
        new ExecuteQuery().loadFromSectionalFile(getSchemaOwnerContext(), "/Testddl.sql", "create_test_table");
        DbAssert.assertTableExists(getSchemaOwnerContext(), "TABLE1");
        DbAssert.assertTableExists(getSchemaOwnerContext(), "TABLE2");

        new ExecuteQuery().loadFromSectionalFile(getSchemaOwnerContext(), "/Testddl_drop.sql", "drop_test_table");
        DbAssert.assertTableNotExists(getSchemaOwnerContext(), "TABLE1");
        DbAssert.assertTableNotExists(getSchemaOwnerContext(), "TABLE2");
    }

    @Test
    public void testLoadSqlScript() throws SQLException, IOException {
        StringBuilder builder = new StringBuilder();

        builder.append("CREATE TABLE TABLE1 ");
        builder.append("    ( ");
        builder.append("     ");
        builder.append("     ID NUMBER (18)  NOT NULL ,      ");
        builder.append("     NAME VARCHAR2 (60 CHAR)  NOT NULL");
        builder.append("    ");
        builder.append("    )  \n");
        builder.append(";\n");

        builder.append(" ");
        builder.append("CREATE TABLE TABLE2 ");
        builder.append("    ( ");
        builder.append("      ID NUMBER (18)  NOT NULL ,      ");
        builder.append("     NAME VARCHAR2 (60 CHAR)  NOT NULL");
        builder.append("    )  ");
        builder.append(";");

        builder.append(" ");

        new ExecuteQuery().loadSource(getSchemaOwnerContext(), builder.toString(), ";");
        DbAssert.assertTableExists(getSchemaOwnerContext(), "TABLE1");
        DbAssert.assertTableExists(getSchemaOwnerContext(), "TABLE2");

        new ExecuteQuery().loadFromSectionalFile(getSchemaOwnerContext(), "/Testddl_drop.sql", "drop_test_table");
        DbAssert.assertTableNotExists(getSchemaOwnerContext(), "TABLE1");
        DbAssert.assertTableNotExists(getSchemaOwnerContext(), "TABLE2");

    }

    @Test
    public void testLoadFromSectionalFile() throws SQLException, IOException {
        new ExecuteQuery().loadFromSectionalFile(getSchemaOwnerContext(), "/SqlPartReaderTest2.sql", new String[]{"create some tables"});
        DbAssert.assertTableExists(getSchemaOwnerContext(), "TABLE1");
        DbAssert.assertTableExists(getSchemaOwnerContext(), "TABLE2");

        new ExecuteQuery().loadFromSectionalFile(getSchemaOwnerContext(), "/SqlPartReaderTest2.sql", new String[]{"delete tables", "create some tables"});
        DbAssert.assertTableExists(getSchemaOwnerContext(), "TABLE1");
        DbAssert.assertTableExists(getSchemaOwnerContext(), "TABLE2");

        new ExecuteQuery().loadFromSectionalFile(getSchemaOwnerContext(), "/SqlPartReaderTest2.sql", new String[]{"delete tables"});
        DbAssert.assertTableNotExists(getSchemaOwnerContext(), "TABLE1");
        DbAssert.assertTableNotExists(getSchemaOwnerContext(), "TABLE2");

        new ExecuteQuery().loadFromSectionalFile(getSchemaOwnerContext(), "/SqlPartReaderTest2.sql", new String[]{"delete tables", "create some tables",
                "delete tables"});
        DbAssert.assertTableNotExists(getSchemaOwnerContext(), "TABLE1");
        DbAssert.assertTableNotExists(getSchemaOwnerContext(), "TABLE2");
    }

    @Test
    public void testGetFileName() {
        assertEquals("/loadSqlbyPart", ExecuteQuery.getFileName(" /loadSqlbyPart, create_test_table"));
        assertNull(ExecuteQuery.getFileName(null));
        assertNull(ExecuteQuery.getFileName(" /loadSqlbyPart create_test_table"));
    }

    @Test
    public void testGetSectionalNames() {
        assertEquals("create_test_table", ExecuteQuery.getSectionNames(" /loadSqlbyPart, create_test_table")[0]);
        assertNull(ExecuteQuery.getSectionNames(null));
        assertNull(ExecuteQuery.getSectionNames(" /loadSqlbyPart create_test_table"));
    }

}
