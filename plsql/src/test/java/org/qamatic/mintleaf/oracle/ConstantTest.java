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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.qamatic.mintleaf.oracle.core.SqlObjectInfo;
import org.qamatic.mintleaf.interfaces.DbContext;
import org.qamatic.mintleaf.interfaces.SqlReaderListener;
import org.qamatic.mintleaf.oracle.core.SqlValue;
import org.qamatic.mintleaf.oracle.junitsupport.OracleTestCase;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Types;

import static org.junit.Assert.assertEquals;

public class ConstantTest extends OracleTestCase {


    private TestConst mvtestConst;

    @Before
    public void init() throws SQLException, IOException {
        mvtestConst = new TestConst(getSchemaOwnerContext());
        mvtestConst.createAll();
    }

    @After
    public void cleanUp() {
        //     mvtestConst.dropAll();
    }

    @Test
    public void testgetConstNameSpace() {
        assertEquals("TEST_NS", mvtestConst.getConstNameSpace());
    }

    @Test
    public void testConstantValue() {
        assertEquals("DBWORKS", mvtestConst.getConstAppName());
    }

    @SqlObjectInfo(name = "TestConst", source = "/TestConst.sql")
    private class TestConst extends OraclePackage {
        public TestConst(DbContext context) {
            super(context);
        }

        public String getConstAppName() {
            SqlValue proc = mvtestConst.getConstant("APP_NAME", Types.VARCHAR);
            return proc.getStringValue("result");
        }

        public String getConstNameSpace() {
            SqlValue proc = mvtestConst.getConstant("NAMESPACE", Types.VARCHAR);
            return proc.getStringValue("result");
        }

        @Override
        public SqlReaderListener getSqlReadListener() {
            return SqlPartListeners.getPLPackageSectionalListner(getSchemaOwnerContext(), "/TestConst_Sections.sql", new String[]{"appname", "syncsut"});
        }
    }
}
