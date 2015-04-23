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

package org.qamatic.mintleaf.interfaces.codeobjects;

import org.junit.Before;
import org.junit.Test;
import org.qamatic.mintleaf.core.ExecuteQuery;
import org.qamatic.mintleaf.oracle.DbAssert;
import org.qamatic.mintleaf.oracle.DbUtility;
import org.qamatic.mintleaf.oracle.TestDbProvisioning;
import org.qamatic.mintleaf.oracle.codeobjects.PLCreatePackage;
import org.qamatic.mintleaf.oracle.codeobjects.PLGrantPrivilege;
import org.qamatic.mintleaf.oracle.codeobjects.PLMemberMethod;
import org.qamatic.mintleaf.oracle.junitsupport.OracleTestCase;
import org.qamatic.mintleaf.oracle.junitsupport.TestDatabase;

import java.io.IOException;
import java.sql.SQLException;

public class PLGrantPrivilegeTest extends OracleTestCase {

    private static DbUtility mvutils;


    @Before
    public void init() {
        if (mvutils == null) {
            mvutils = new DbUtility(getSchemaOwnerContext());
            TestDbProvisioning tpriv = new TestDbProvisioning(TestDatabase.getSysDbaContext());
            tpriv.recreateSchemaUser("TESTUSER", "Oracle");
        }
    }

    @Test
    public void testPLGrantPrivilege() throws SQLException, IOException {
        PLCreatePackage p = new PLCreatePackage("MyPackage") {
            {
                PLMemberMethod m = new PLMemberMethod("MyMethod1");
                addMemberMethod(m);
                m = new PLMemberMethod("MyMethod2", "varchar2");
                addMemberMethod(m);
            }
        };
        new ExecuteQuery().loadSource(getSchemaOwnerContext(), p.toString() + "\n/", "/");

        PLGrantPrivilege priv = new PLGrantPrivilege("TESTUSER", "Execute", "MyPackage");

        new ExecuteQuery().loadSource(getSchemaOwnerContext(), priv.toString() + "\n/", "/");
        DbAssert.assertPrivilegeExists(getSchemaOwnerContext(), "TESTUSER", "EXECUTE", "MYPACKAGE");
    }
}
