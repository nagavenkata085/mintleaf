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
import org.qamatic.mintleaf.interfaces.SqlReaderListener;
import org.qamatic.mintleaf.interfaces.SqlValue;
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
