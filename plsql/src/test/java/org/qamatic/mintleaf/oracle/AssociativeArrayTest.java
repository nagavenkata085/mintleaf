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
import org.qamatic.mintleaf.oracle.junitsupport.OracleTestCase;
import org.qamatic.mintleaf.oracle.spring.OracleSpringSqlProcedure;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Types;

import static org.junit.Assert.assertEquals;

public class AssociativeArrayTest extends OracleTestCase {


    private AssociativeArray mvassociativeArray;

    @Before
    public void init() throws SQLException, IOException {

        mvassociativeArray = new AssociativeArray(getSchemaOwnerContext());
        mvassociativeArray.createAll();
    }

    @After
    public void cleanUp() throws SQLException, IOException {
        mvassociativeArray.dropAll();
    }

    @Test
    public void testAssociativeArrayTest1() throws SQLException {

        String inputValue = "Hat-in-the-hat";
        String[] expectedValue = new String[]{"Hat", "in", "the", "hat"};
        String tokenCharacter = "-";
        String[] actualResult = mvassociativeArray.Tokenize(inputValue, tokenCharacter);
        assertEquals(expectedValue.length, actualResult.length);
        assertEquals(expectedValue[0], actualResult[0]);
        assertEquals(expectedValue[1], actualResult[1]);
        assertEquals(expectedValue[2], actualResult[2]);
        assertEquals(expectedValue[3], actualResult[3]);

    }

    @Test
    public void testAssociativeArrayTest2() throws SQLException {

        String inputValue = "Hat**in**the**hat**test";
        String[] expectedValue = new String[]{"Hat", "*in", "*the", "*hat", "*test"};
        String tokenCharacter = "**";
        String[] actualResult = mvassociativeArray.Tokenize(inputValue, tokenCharacter);
        assertEquals(expectedValue.length, actualResult.length);
        assertEquals(expectedValue[0], actualResult[0]);
        assertEquals(expectedValue[1], actualResult[1]);
        assertEquals(expectedValue[2], actualResult[2]);
        assertEquals(expectedValue[3], actualResult[3]);
        assertEquals(expectedValue[4], actualResult[4]);

    }

    @SqlObjectInfo(name = "AssociativeArray", source = "/AssociativeArrayTest.sql")
    private static class AssociativeArray extends OraclePackage {
        public AssociativeArray(DbContext context) {
            super(context);

        }

        public String[] Tokenize(String inputValue, String tokenCharacter) throws SQLException {

            OracleSpringSqlProcedure proc = (OracleSpringSqlProcedure) getProcedure("tokenize");

            proc.createParameter("pstr", Types.VARCHAR);
            proc.createOutParameter("presult", Types.ARRAY, "STRINGLIST");
            proc.createParameter("ptoken", Types.VARCHAR);

            proc.compile();
            proc.setValue("pstr", inputValue);
            proc.setValue("ptoken", tokenCharacter);
            proc.execute();

            return (String[]) proc.getArray("presult").getArray();
        }
    }
}
