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
