/*
 * <!--
 *   ~
 *   ~ The MIT License (MIT)
 *   ~
 *   ~ Copyright (c) 2010-2017 Senthil Maruthaiappan
 *   ~
 *   ~ Permission is hereby granted, free of charge, to any person obtaining a copy
 *   ~ of this software and associated documentation files (the "Software"), to deal
 *   ~ in the Software without restriction, including without limitation the rights
 *   ~ to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   ~ copies of the Software, and to permit persons to whom the Software is
 *   ~ furnished to do so, subject to the following conditions:
 *   ~
 *   ~ The above copyright notice and this permission notice shall be included in all
 *   ~ copies or substantial portions of the Software.
 *   ~
 *   ~ THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   ~ IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   ~ FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *   ~ AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *   ~ LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   ~ OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *   ~ SOFTWARE.
 *   ~
 *   ~
 *   -->
 */

package org.qamatic.mintleaf.interfaces.codeobjects;

import org.junit.Before;
import org.junit.Test;
import org.qamatic.mintleaf.core.ApplyChangeSets;
import org.qamatic.mintleaf.oracle.OracleDbAssert;
import org.qamatic.mintleaf.oracle.OracleHelperScript;
import org.qamatic.mintleaf.oracle.TestProvisioningScript;
import org.qamatic.mintleaf.oracle.codeobjects.PLCreatePackage;
import org.qamatic.mintleaf.oracle.codeobjects.PLGrantPrivilege;
import org.qamatic.mintleaf.oracle.codeobjects.PLMemberMethod;
import org.qamatic.mintleaf.oracle.junitsupport.OracleTestCase;
import org.qamatic.mintleaf.oracle.junitsupport.OracleTestDatabase;


import java.io.IOException;
import java.sql.SQLException;

public class PLGrantPrivilegeTest extends OracleTestCase {

    private static OracleHelperScript mvutils;


    @Before
    public void init() {
        if (mvutils == null) {
            mvutils = new OracleHelperScript(getSchemaOwnerContext());
            TestProvisioningScript tpriv = new TestProvisioningScript(OracleTestDatabase.getSysDbaContext());
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
        new ApplyChangeSets().applySource(getSchemaOwnerContext(), p.toString() + "\n/", "/");

        PLGrantPrivilege priv = new PLGrantPrivilege("TESTUSER", "Execute", "MyPackage");

        new ApplyChangeSets().applySource(getSchemaOwnerContext(), priv.toString() + "\n/", "/");
        OracleDbAssert.assertPrivilegeExists(getSchemaOwnerContext(), "TESTUSER", "EXECUTE", "MYPACKAGE");
    }
}
