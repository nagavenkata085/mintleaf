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

package org.qamatic.mintleaf.oracle;

import oracle.sql.ARRAY;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.qamatic.mintleaf.oracle.core.SqlObjectInfo;
import org.qamatic.mintleaf.DbContext;
import org.qamatic.mintleaf.oracle.junitsupport.OracleTestCase;
import org.qamatic.mintleaf.oracle.spring.OraclePLProcedure;

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

            OraclePLProcedure proc = (OraclePLProcedure) getProcedure("tokenize");

            proc.createInParameter("pstr", Types.VARCHAR);
            proc.createOutParameter("presult", Types.ARRAY, "STRINGLIST");
            proc.createInParameter("ptoken", Types.VARCHAR);

            proc.compile();
            proc.setValue("pstr", inputValue);
            proc.setValue("ptoken", tokenCharacter);
            proc.execute();

            return (String[]) ((ARRAY) proc.getArray("presult")).getArray();
        }
    }
}
