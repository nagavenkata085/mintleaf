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

import oracle.sql.NUMBER;
import org.junit.Assert;
import org.junit.Test;
import org.qamatic.mintleaf.dbsupportimpls.oracle.OracleColumn;
import org.qamatic.mintleaf.interfaces.DbContext;
import org.qamatic.mintleaf.interfaces.TableMetaData;
import org.qamatic.mintleaf.oracle.junitsupport.OracleTestCase;
import org.qamatic.mintleaf.oracle.spring.OracleTypeObjectValue;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class OracleTypeObjectValueTest extends OracleTestCase {

    private static List<Object> mvtestObjects = new ArrayList<Object>();


    private static TableMetaData getSampleMetaData() {
        TableMetaData metadata = new TableMetaData();
        metadata.add(new OracleColumn("emp_id", "number"));
        metadata.add(new OracleColumn("emp_name", "varchar(2000)"));
        return metadata;
    }

    @Test
    public void testTypeName() {
        Assert.assertEquals("Employee", new OracleTypeObjectValue(null, "Employee").getTypeName());
    }

    @Test
    public void testGetColumnUsingMetaData() throws SQLException {
        SqlTypeObjectValue obj = new MockOracleTypeObjectValue(getSchemaOwnerContext(), "Employee");
        obj.getMetaData().add(new OracleColumn("emp_id", "number"));
        obj.getMetaData().add(new OracleColumn("emp_name", "varchar(2000)"));
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
        obj.getMetaData().add(new OracleColumn("emp_id", "number"));
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
