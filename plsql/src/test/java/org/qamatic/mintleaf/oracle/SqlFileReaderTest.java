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

import org.junit.Test;
import org.qamatic.mintleaf.core.SqlFileReader;
import org.qamatic.mintleaf.interfaces.SqlReaderListener;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Map;

import static org.junit.Assert.*;

public class SqlFileReaderTest {

    private String actual_emptypackage_block1;
    private String actual_emptypackage_block2;

    @Test
    public void testDelimiterString() {
        SqlFileReader reader = new SqlFileReader(null);
        reader.setDelimiter(";");
        assertEquals(";", reader.getDelimiter());
    }

    @Test
    public void testDelimiterStringDefault() {
        SqlFileReader reader = new SqlFileReader(null);
        assertEquals("/", reader.getDelimiter());
    }

    @Test
    public void testSqlReaderListnerDefault() {
        SqlFileReader reader = new SqlFileReader(null);
        assertNull(reader.getReaderListener());
    }

    @Test
    public void testSqlReaderListnerTest1() {
        SqlFileReader reader = new SqlFileReader(null);
        reader.setReaderListener(new EmptyPackageReadListner());
        assertNotNull(reader.getReaderListener());
    }

    @Test
    public void testSqlReaderReadTest() throws IOException, SQLException {

        InputStream iStream = this.getClass().getResourceAsStream("/EmptyPackage.sql");
        SqlFileReader reader = new SqlFileReader(iStream);

        String actual = reader.read();

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

        assertEquals(expected.toString(), actual);
    }

    @Test
    public void testSqlReaderListnerTest2() throws IOException, SQLException {

        SqlReaderListener listner = new EmptyPackageReadListner();
        InputStream iStream = this.getClass().getResourceAsStream("/EmptyPackage.sql");
        SqlFileReader reader = new SqlFileReader(iStream);
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
