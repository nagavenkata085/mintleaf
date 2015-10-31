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

package org.qamatic.mintleaf.core;

import org.junit.Test;
import org.qamatic.mintleaf.interfaces.MultiPartReader;
import org.qamatic.mintleaf.interfaces.SqlPart;
import org.qamatic.mintleaf.interfaces.SqlReaderListener;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Map;

import static org.junit.Assert.*;

public class SqlMultiPartlFileReaderTest {

    private String actual_part1;
    private String actual_part2;
    private String actual_part3;

    @Test
    public void testSqlSectionalReaderCount() throws IOException, SQLException {
        InputStream iStream = this.getClass().getResourceAsStream("/SqlPartReaderTest.sql");
        MultiPartReader reader = new SqlMultiPartFileReader(iStream);
        reader.read();
        assertEquals(3, reader.getSqlParts().size());
    }

    @Test
    public void testSqlSectionalReaderSections() throws IOException, SQLException {
        MultiPartReader reader = new SqlMultiPartFileReader("/SqlPartReaderTest.sql");
        reader.read();

        assertTrue(reader.getSqlParts().containsKey("part1"));
        assertTrue(reader.getSqlParts().containsKey("part2"));
        assertTrue(reader.getSqlParts().containsKey("part3"));
        SqlPart part1 = reader.getSqlParts().get("part1");
        assertEquals(getPart1Data(), part1.getSqlPartSource());

        SqlPart part2 = reader.getSqlParts().get("part2");
        assertEquals(getPart2Data(), part2.getSqlPartSource());

        SqlPart part3 = reader.getSqlParts().get("part3");
        assertEquals(getPart3Data(), part3.getSqlPartSource());
    }

    @Test
    public void testgetSectionDetail() {
        String json = "ffccc";
        SqlPart detail = SqlPart.xmlToSqlPart(json);

        assertNull(detail);

        detail = SqlPart.xmlToSqlPart("<sqlpart name=\"delete tables\" delimiter=\"/\" />");
        assertNotNull(detail);
        assertEquals("delete tables", detail.getName());
        assertEquals("/", detail.getDelimiter());

        detail = SqlPart.xmlToSqlPart("<sqlpart name=\"delete tables\"  />");
        assertNotNull(detail);
        assertEquals("delete tables", detail.getName());
        assertEquals("", detail.getDelimiter());

    }

    private String getPart1Data() {
        StringBuilder expected = new StringBuilder();
        expected.append("-- empty package\n");
        expected.append("create or replace package EmptyPackage\n");
        expected.append("as\n");
        expected.append("end EmptyPackage;\n");
        expected.append("/");
        return expected.toString();
    }

    private String getPart2Data() {
        StringBuilder expected = new StringBuilder();
        expected.append("create or replace\n");
        expected.append("package body EmptyPackage\n");
        expected.append("as\n");
        expected.append("end EmptyPackage;\n");
        expected.append("/");
        return expected.toString();
    }

    private String getPart3Data() {
        StringBuilder expected = new StringBuilder();
        expected.append("CREATE TABLE TABLE1\n");
        expected.append("(\n");
        expected.append("ID NUMBER (18)  NOT NULL ,\n");
        expected.append("NAME VARCHAR2 (60 CHAR)  NOT NULL\n");
        expected.append(")\n");
        expected.append(";\n");
        expected.append("CREATE TABLE TABLE2\n");
        expected.append("(\n");
        expected.append("ID NUMBER (18)  NOT NULL ,\n");
        expected.append("NAME VARCHAR2 (60 CHAR)  NOT NULL\n");
        expected.append(")\n");
        expected.append(";");
        return expected.toString();
    }

    @Test
    public void testSqlSectionalReaderListnerTest() throws IOException, SQLException {

        SqlReaderListener listner = new SectionalFileReadListner();
        InputStream iStream = this.getClass().getResourceAsStream("/SectionalFileReaderTest.sql");
        SqlMultiPartFileReader reader = new SqlMultiPartFileReader(iStream);
        reader.setReaderListener(listner);
        actual_part1 = null;
        actual_part2 = null;
        actual_part3 = null;

        reader.read();

        assertEquals(getPart1Data(), actual_part1);

        assertEquals(getPart2Data(), actual_part2);
        assertEquals(getPart3Data(), actual_part3);

    }

    // EmptyPackage.sql
    private class SectionalFileReadListner implements SqlReaderListener {

        @Override
        public void onReadChild(StringBuilder sql, Object context) throws SQLException, IOException {
            if (actual_part1 == null) {
                actual_part1 = sql.toString();
            } else if (actual_part2 == null) {
                actual_part2 = sql.toString();
            } else if (actual_part3 == null) {
                actual_part3 = sql.toString();
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
