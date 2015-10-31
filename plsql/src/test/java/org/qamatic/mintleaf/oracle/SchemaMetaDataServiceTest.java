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
import org.qamatic.mintleaf.core.ExecuteQuery;
import org.qamatic.mintleaf.interfaces.DbMetaDataService;
import org.qamatic.mintleaf.interfaces.SqlColumn;
import org.qamatic.mintleaf.interfaces.SqlObjectMetaData;
import org.qamatic.mintleaf.oracle.junitsupport.OracleTestCase;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class SchemaMetaDataServiceTest extends OracleTestCase {


    private static SqlObjectMetaData getSampleMetaData() {
        SqlObjectMetaData metadata = new SqlObjectMetaData();
        metadata.add(new SqlColumn("emp_id", "number"));
        metadata.add(new SqlColumn("emp_name", "varchar(2000)"));
        return metadata;
    }

    @Test
    public void testDbObjectColumnMetaData() {

        SqlColumn data = new SqlColumn("emp_id", "number");
        assertEquals("emp_id", data.getColumnName());
        assertEquals("number", data.getTypeName());
        data.setColumnName("emp_name");
        data.setTypeName("varchar(2000)");
        assertEquals("emp_name", data.getColumnName());
        assertEquals("varchar(2000)", data.getTypeName());
    }

    @Test
    public void testDbStructuredObjectMetaDataCount() {
        SqlObjectMetaData metadata = getSampleMetaData();
        assertEquals(2, metadata.getColumns().size());
    }

    @Test
    public void testDbStructuredObjectMetaDataColByIndex() {
        SqlObjectMetaData metadata = getSampleMetaData();
        assertEquals(1, metadata.getIndex("emp_name"));
        assertEquals(0, metadata.getIndex("emp_id"));
        assertEquals(-1, metadata.getIndex("no_where_land"));
    }

    @Test
    public void testDbtMetaDataService() {
        DbMetaDataService svc = new SchemaMetaDataService();
        SqlObjectMetaData empMetaData = getSampleMetaData();
        svc.addMetaData("scott.empolyee", empMetaData);

        assertEquals(empMetaData, svc.getMetaData("scott.empolyee"));
        assertNull(svc.getMetaData("no_where_land.empolyee"));

    }

    @Test
    public void testDbtMetaDataServiceFromTable() throws SQLException, IOException {
        new ExecuteQuery().loadFromSectionalFile(getSchemaOwnerContext(), "/examples/employeepackagesectional.sql", new String[]{"drop employee table",
                "create employee table"});
        DbMetaDataService svc = new SchemaMetaDataService();

        try {
            SqlObjectMetaData empMetaData = svc.addMetaDataFromTable(getSchemaOwnerContext(), "EMPLOYEE");
            assertEquals(1, empMetaData.getIndex("name"));
            assertEquals(0, empMetaData.getIndex("emp_id"));
            empMetaData = svc.getMetaData(getSchemaOwnerContext().getDbSettings().getUsername() + ".EMPLOYEE");
            assertEquals(1, empMetaData.getIndex("name"));
            assertEquals(0, empMetaData.getIndex("emp_id"));

        } finally {
            new ExecuteQuery().loadFromSectionalFile(getSchemaOwnerContext(), "/examples/employeepackagesectional.sql", new String[]{"drop employee table"});
        }

    }
}
