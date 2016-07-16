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
import org.qamatic.mintleaf.oracle.core.SqlStoredProcedure;
import org.qamatic.mintleaf.oracle.junitsupport.OracleTestCase;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Types;

import static org.junit.Assert.assertEquals;

public class PackagePrivateMethodTest extends OracleTestCase {


    private TestablePackage mvtestConst;

    @Before
    public void init() throws SQLException, IOException {
        mvtestConst = new TestablePackage(getSchemaOwnerContext());
        mvtestConst.createAll();
    }

    @After
    public void cleanUp() {
        // mvtestConst.dropAll();
    }

    @Test
    public void testPrivateMethodTest1() {
        assertEquals("3433", mvtestConst.getTestId());
    }

    @SqlObjectInfo(name = "TestConst", source = "/TestConst.sql")
    private class TestablePackage extends OriginalPackageClass {
        public TestablePackage(DbContext context) {
            super(context);
        }

        @Override
        public SqlReaderListener getSqlReadListener() {
            return ChangeSetListeners.getPLPackageSectionalListner(getSchemaOwnerContext(), "/TestConst_Sections.sql", null);
        }

        public String getTestId() {
            SqlStoredProcedure proc = getFunction("getTEST_ID", Types.VARCHAR);
            proc.execute();
            return proc.getStringValue("result");
        }

    }

    @SqlObjectInfo(name = "TestConst", source = "/TestConst.sql")
    private class OriginalPackageClass extends OraclePackage {
        public OriginalPackageClass(DbContext context) {
            super(context);
        }

    }

}
