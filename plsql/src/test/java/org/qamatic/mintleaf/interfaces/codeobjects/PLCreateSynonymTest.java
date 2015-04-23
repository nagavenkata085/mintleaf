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
import org.qamatic.mintleaf.oracle.codeobjects.PLCreateSynonym;
import org.qamatic.mintleaf.oracle.junitsupport.OracleTestCase;

import java.io.IOException;
import java.sql.SQLException;

public class PLCreateSynonymTest extends OracleTestCase {

    private static DbUtility mvutils;


    @Before
    public void init() {
        if (mvutils == null) {
            mvutils = new DbUtility(getSchemaOwnerContext());
        }
    }

    @Test
    public void testPLCreateSynonym() throws SQLException, IOException {


        PLCreateSynonym syn = new PLCreateSynonym("TEST", getSchemaOwnerContext().getDbSettings().getUsername(), "MyPackage");
        System.out.println(syn.toString());
        new ExecuteQuery().loadSource(getSchemaOwnerContext(), syn.toString() + "\n/", "/");
        DbAssert.assertSynonymExists(getSchemaOwnerContext(), "TEST");

    }
}
