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

package org.qamatic.mintleaf.oracle.junitsupport;

import org.junit.Test;
import org.qamatic.mintleaf.oracle.DbAssert;
import org.qamatic.mintleaf.oracle.DbUtility;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DbAssertsTest extends OracleTestCase {


    @Test
    public void testaPackage() throws SQLException, IOException {
        DbAssert.assertPackageExists(getSchemaOwnerContext(), "DbUtility");
    }

    @Test
    public void sqlutilsSelfTest() throws SQLException, IOException {
        DbUtility utilPkg = new DbUtility(getSchemaOwnerContext());
        DbAssert.assertPackageExists(utilPkg);
    }


    @Test
    public void testAssertDateDifference() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Date beforeDate = sdf.parse("12/12/2000");
        Date afterDate = sdf.parse("12/15/2000");
        DbAssert.assertDateDifference(beforeDate, afterDate, 3);
    }

}
