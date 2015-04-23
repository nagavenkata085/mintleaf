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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.qamatic.mintleaf.core.SqlObjectInfo;
import org.qamatic.mintleaf.interfaces.DbContext;
import org.qamatic.mintleaf.interfaces.SqlProcedure;
import org.qamatic.mintleaf.interfaces.SqlReaderListener;
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
            return SqlPartListeners.getPLPackageSectionalListner(getSchemaOwnerContext(), "/TestConst_Sections.sql", null);
        }

        public String getTestId() {
            SqlProcedure proc = getFunction("getTEST_ID", Types.VARCHAR);
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
