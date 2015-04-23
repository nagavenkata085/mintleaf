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
