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

package org.qamatic.mintleaf.oracle.examples;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.qamatic.mintleaf.oracle.junitsupport.OracleTestCase;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;

public class StringExampleTest extends OracleTestCase {

    private static StringExample mvstringExample;

    @AfterClass
    public static void cleanUp() throws SQLException, IOException {
        mvstringExample.dropAll();
    }

    @Before
    public void init() throws SQLException, IOException {

        if (mvstringExample == null) {
            mvstringExample = new StringExample(getSchemaOwnerContext());
        }
        mvstringExample.createAll();
    }

    @Test
    public void testReplaceSpaceWithDollarSign() {

        String inputValue = "Database is relational UnitTest";
        String expectedValue = "Database$is$relational$UnitTest";
        assertEquals(expectedValue, mvstringExample.replaceSpaceWithDollarSign(inputValue));
    }

}
