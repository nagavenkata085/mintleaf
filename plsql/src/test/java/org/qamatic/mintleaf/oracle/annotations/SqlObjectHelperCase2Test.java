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

package org.qamatic.mintleaf.oracle.annotations;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.qamatic.mintleaf.core.SqlObjectDefaults;
import org.qamatic.mintleaf.core.SqlObjectHelper;
import org.qamatic.mintleaf.core.SqlObjectInfo;
import org.qamatic.mintleaf.interfaces.DbContext;
import org.qamatic.mintleaf.oracle.DbAssert;
import org.qamatic.mintleaf.oracle.OracleBatchDDL;
import org.qamatic.mintleaf.oracle.OraclePackage;
import org.qamatic.mintleaf.oracle.junitsupport.OracleTestCase;
import org.qamatic.mintleaf.oracle.mocks.CreateTable1;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class SqlObjectHelperCase2Test extends OracleTestCase {


    @Before
    public void init() {

    }

    @After
    public void cleanUp() {

    }

    @Test
    public void testPLDefaultsAnnotationNull() {
        MockTestPackage2 pkg = new MockTestPackage2(getSchemaOwnerContext());
        SqlObjectDefaults plDefaults = SqlObjectHelper.getPLApplyDefaultsAnnotation(pkg);
        assertNull(plDefaults);
    }

    @Test
    public void testPLDefaultsAnnotation() {
        MockTable1 table1 = new MockTable1(getSchemaOwnerContext());
        SqlObjectDefaults plDefaults = SqlObjectHelper.getPLApplyDefaultsAnnotation(table1);
        assertNotNull(plDefaults);

        assertEquals(1, plDefaults.Using().length);
        assertTrue(plDefaults.Using()[0] == DefaultValueDdl.class);
    }

    @Test
    public void testPLDefaultsCreate() throws SQLException, IOException {
        MockTable1 table1 = new MockTable1(getSchemaOwnerContext());
        table1.drop();

        try {

            table1.create();
            table1.applyDefaults();

            DbAssert.assertCountEquals(2, getSchemaOwnerContext(), "table1");
        } finally {
            table1.drop();
        }
    }

    private static class MockTestPackage2 extends OraclePackage {
        public MockTestPackage2(DbContext context) {
            super(context);

        }

    }

    @SqlObjectDefaults(Using = {DefaultValueDdl.class})
    private class MockTable1 extends CreateTable1 {
        public MockTable1(DbContext context) {
            super(context);

        }

    }

    @SqlObjectInfo(name = "default data for table1", source = "/AddDataToTable1Count2.sql", sourceDelimiter = ";")
    public class DefaultValueDdl extends OracleBatchDDL {

        public DefaultValueDdl(DbContext context) {
            super(context);
        }

    }

}
