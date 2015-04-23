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

package org.qamatic.mintleaf.oracle.examples.codeobjects;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.qamatic.mintleaf.oracle.junitsupport.OracleTestCase;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;

public class StringExampleTest extends OracleTestCase {

    private static StringExampleUsingCodeObject mvstringExample;


    @AfterClass
    public static void cleanUp() {
        // mvstringExample.dropAll();
    }

    @Before
    public void init() throws SQLException, IOException {

        if (mvstringExample == null) {
            mvstringExample = new StringExampleUsingCodeObject(getSchemaOwnerContext());
        }
        mvstringExample.dropAll();
        mvstringExample.createAll();
    }

    @Test
    public void testReplaceSpaceWithDollarSign() {

        String inputValue = "Database is relational UnitTest";
        String expectedValue = "Database$is$relational$UnitTest";
        assertEquals(expectedValue, mvstringExample.ReplaceSpaceWithDollarSign(inputValue));
    }

}
