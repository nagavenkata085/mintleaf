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

import oracle.sql.NUMBER;
import org.junit.Assert;
import org.junit.Test;
import org.qamatic.mintleaf.interfaces.DbContext;
import org.qamatic.mintleaf.interfaces.SqlColumn;
import org.qamatic.mintleaf.interfaces.SqlObjectMetaData;
import org.qamatic.mintleaf.interfaces.SqlTypeObjectValue;
import org.qamatic.mintleaf.oracle.junitsupport.OracleTestCase;
import org.qamatic.mintleaf.oracle.spring.OracleTypeObjectValue;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class OracleTypeObjectValueTest extends OracleTestCase {

    private static List<Object> mvtestObjects = new ArrayList<Object>();


    private static SqlObjectMetaData getSampleMetaData() {
        SqlObjectMetaData metadata = new SqlObjectMetaData();
        metadata.add(new SqlColumn("emp_id", "number"));
        metadata.add(new SqlColumn("emp_name", "varchar(2000)"));
        return metadata;
    }

    @Test
    public void testTypeName() {
        Assert.assertEquals("Employee", new OracleTypeObjectValue(null, "Employee").getTypeName());
    }

    @Test
    public void testGetColumnUsingMetaData() throws SQLException {
        SqlTypeObjectValue obj = new MockOracleTypeObjectValue(getSchemaOwnerContext(), "Employee");
        obj.getMetaData().add(new SqlColumn("emp_id", "number"));
        obj.getMetaData().add(new SqlColumn("emp_name", "varchar(2000)"));
        mvtestObjects.clear();
        mvtestObjects.add("1");
        mvtestObjects.add("scott");
        assertEquals("scott", obj.getStringValue("emp_name"));
    }

    @Test
    public void testGetColumnUsingService() throws SQLException {
        SqlTypeObjectValue obj = new MockOracleTypeObjectValue(null, "Employee");
        SchemaMetaDataService metaSvc = new SchemaMetaDataService();
        metaSvc.addMetaData("Employee", getSampleMetaData());
        obj.setMetaDataService(metaSvc);
        mvtestObjects.clear();
        mvtestObjects.add("1");
        mvtestObjects.add("scott");
        assertEquals("scott", obj.getStringValue("emp_name"));
    }

    @SuppressWarnings("boxing")
    @Test
    public void testgetIntValue() throws SQLException {
        SqlTypeObjectValue obj = new MockOracleTypeObjectValue(getSchemaOwnerContext(), "Employee");
        obj.getMetaData().add(new SqlColumn("emp_id", "number"));
        mvtestObjects.clear();
        mvtestObjects.add(100);
        assertEquals(100, obj.getIntValue("emp_id"));
        mvtestObjects.clear();
        mvtestObjects.add(new NUMBER(1000));
        assertEquals(1000, obj.getIntValue("emp_id"));
    }

    private class MockOracleTypeObjectValue extends OracleTypeObjectValue {

        public MockOracleTypeObjectValue(DbContext context, String typeName) {
            super(context, typeName);

        }

        @Override
        protected List<Object> getObjects() {
            return mvtestObjects;
        }
    }

}
