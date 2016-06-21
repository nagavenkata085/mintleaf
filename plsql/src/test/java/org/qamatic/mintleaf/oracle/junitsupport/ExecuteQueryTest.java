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

package org.qamatic.mintleaf.oracle.junitsupport;

import org.junit.Test;
import org.qamatic.mintleaf.core.ExecuteQuery;
import org.qamatic.mintleaf.dbsupportimpls.oracle.OracleDbAssert;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ExecuteQueryTest extends OracleTestCase {


    @Test
    public void testLoadFromFile() throws SQLException, IOException {
        new ExecuteQuery().loadFromSectionalFile(getSchemaOwnerContext(), "/Testddl.sql", "create_test_table");
        OracleDbAssert.assertTableExists(getSchemaOwnerContext(), "TABLE1");
        OracleDbAssert.assertTableExists(getSchemaOwnerContext(), "TABLE2");

        new ExecuteQuery().loadFromSectionalFile(getSchemaOwnerContext(), "/Testddl_drop.sql", "drop_test_table");
        OracleDbAssert.assertTableNotExists(getSchemaOwnerContext(), "TABLE1");
        OracleDbAssert.assertTableNotExists(getSchemaOwnerContext(), "TABLE2");
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
        OracleDbAssert.assertTableExists(getSchemaOwnerContext(), "TABLE1");
        OracleDbAssert.assertTableExists(getSchemaOwnerContext(), "TABLE2");

        new ExecuteQuery().loadFromSectionalFile(getSchemaOwnerContext(), "/Testddl_drop.sql", "drop_test_table");
        OracleDbAssert.assertTableNotExists(getSchemaOwnerContext(), "TABLE1");
        OracleDbAssert.assertTableNotExists(getSchemaOwnerContext(), "TABLE2");

    }

    @Test
    public void testLoadFromSectionalFile() throws SQLException, IOException {
        new ExecuteQuery().loadFromSectionalFile(getSchemaOwnerContext(), "/SqlPartReaderTest2.sql", new String[]{"create some tables"});
        OracleDbAssert.assertTableExists(getSchemaOwnerContext(), "TABLE1");
        OracleDbAssert.assertTableExists(getSchemaOwnerContext(), "TABLE2");

        new ExecuteQuery().loadFromSectionalFile(getSchemaOwnerContext(), "/SqlPartReaderTest2.sql", new String[]{"delete tables", "create some tables"});
        OracleDbAssert.assertTableExists(getSchemaOwnerContext(), "TABLE1");
        OracleDbAssert.assertTableExists(getSchemaOwnerContext(), "TABLE2");

        new ExecuteQuery().loadFromSectionalFile(getSchemaOwnerContext(), "/SqlPartReaderTest2.sql", new String[]{"delete tables"});
        OracleDbAssert.assertTableNotExists(getSchemaOwnerContext(), "TABLE1");
        OracleDbAssert.assertTableNotExists(getSchemaOwnerContext(), "TABLE2");

        new ExecuteQuery().loadFromSectionalFile(getSchemaOwnerContext(), "/SqlPartReaderTest2.sql", new String[]{"delete tables", "create some tables",
                "delete tables"});
        OracleDbAssert.assertTableNotExists(getSchemaOwnerContext(), "TABLE1");
        OracleDbAssert.assertTableNotExists(getSchemaOwnerContext(), "TABLE2");
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
