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

import org.junit.Test;
import org.qamatic.mintleaf.core.SqlStringReader;
import org.qamatic.mintleaf.interfaces.SqlReaderListener;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class SqlStringReaderTest {

    private String actual_emptypackage_block1;
    private String actual_emptypackage_block2;

    private String getSamplePackageData() {
        StringBuilder expected = new StringBuilder();

        expected.append("create or replace package EmptyPackage\n");
        expected.append("as\n");
        expected.append("\n");
        expected.append("end EmptyPackage;\n");
        expected.append("\n");
        expected.append("/\n");
        expected.append("\n");
        expected.append("create or replace\n");
        expected.append("package body EmptyPackage\n");
        expected.append("as\n");
        expected.append("\n");
        expected.append("end EmptyPackage;\n");
        expected.append("\n");
        expected.append("/\n");
        expected.append("\n");
        return expected.toString();
    }

    @Test
    public void testSqlStringReaderReadTest() throws IOException, SQLException {

        String expected = getSamplePackageData();

        SqlStringReader reader = new SqlStringReader(expected);
        String actual = reader.read();

        assertEquals(expected.toString(), actual);
    }

    @Test
    public void testSqlReaderListnerTest2() throws IOException, SQLException {

        SqlStringReader reader = new SqlStringReader(getSamplePackageData());
        SqlReaderListener listner = new EmptyPackageReadListner();
        reader.setReaderListener(listner);
        actual_emptypackage_block1 = null;
        actual_emptypackage_block2 = null;

        reader.read();

        StringBuilder expected = new StringBuilder();

        expected.append("create or replace package EmptyPackage\n");
        expected.append("as\n");
        expected.append("\n");
        expected.append("end EmptyPackage;");

        assertEquals(expected.toString(), actual_emptypackage_block1);

        expected.setLength(0);
        expected.append("create or replace\n");
        expected.append("package body EmptyPackage\n");
        expected.append("as\n");
        expected.append("\n");
        expected.append("end EmptyPackage;");

        assertEquals(expected.toString(), actual_emptypackage_block2);

    }

    // EmptyPackage.sql
    private class EmptyPackageReadListner implements SqlReaderListener {

        @Override
        public void onReadChild(StringBuilder sql, Object context) throws SQLException, IOException {
            if (actual_emptypackage_block1 == null) {
                actual_emptypackage_block1 = sql.toString();
            } else if (actual_emptypackage_block2 == null) {
                actual_emptypackage_block2 = sql.toString();
            }
        }

        @Override
        public SqlReaderListener getChildReaderListener() {
            return null;
        }

        @Override
        public void setChildReaderListener(SqlReaderListener childListener) {

        }

        @Override
        public Map<String, String> getTemplateValues() {

            throw new UnsupportedOperationException();
        }
    }
}
