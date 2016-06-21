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

package org.qamatic.mintleaf.oracle.annotations;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.qamatic.mintleaf.core.SqlObjectDefaults;
import org.qamatic.mintleaf.core.SqlObjectHelper;
import org.qamatic.mintleaf.core.SqlObjectInfo;
import org.qamatic.mintleaf.interfaces.DbContext;
import org.qamatic.mintleaf.oracle.OracleBatchDDL;
import org.qamatic.mintleaf.oracle.OracleDbAssert;
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

            OracleDbAssert.assertCountEquals(2, getSchemaOwnerContext(), "table1");
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
