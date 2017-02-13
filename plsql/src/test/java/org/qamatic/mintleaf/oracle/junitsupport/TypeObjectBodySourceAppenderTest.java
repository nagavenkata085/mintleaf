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

package org.qamatic.mintleaf.oracle.junitsupport;

import org.junit.Test;
import org.qamatic.mintleaf.oracle.SqlSourceVisitor;
import org.qamatic.mintleaf.oracle.codevisitors.TypeObjectBodySourceAppender;

import static org.junit.Assert.assertEquals;

public class TypeObjectBodySourceAppenderTest {

    @Test
    public void testTypeObjectBodySourceNotFoundCase() {
        SqlSourceVisitor appender = new TypeObjectBodySourceAppender();
        StringBuilder inputValue = new StringBuilder();

        inputValue.append("create or replace type EmptyType\n");
        inputValue.append("as\n");
        inputValue.append("end EmptyType;\n");
        appender.visit(inputValue, "function getName() return varchar2 as\nbegin\nreturn 'test';\nend;");

        StringBuilder expectedValue = new StringBuilder();

        expectedValue.append("create or replace type EmptyType\n");
        expectedValue.append("as\n");
        expectedValue.append("end EmptyType;\n");
        appender.visit(inputValue, "function getName() return varchar2 as\nbegin\nreturn 'test';\nend;");

        assertEquals(expectedValue.toString(), inputValue.toString());
    }

    @Test
    public void testTypeObjectBodySourceAppender2() {
        SqlSourceVisitor appender = new TypeObjectBodySourceAppender();
        StringBuilder actualValue = new StringBuilder();

        actualValue.append("create or replace type body EmptyType\n");
        actualValue.append("as\n");
        actualValue.append("end;\n");

        appender.visit(actualValue, "function getName() return varchar2 as\nbegin\nreturn 'test';\nend;");

        StringBuilder expectedValue = new StringBuilder();
        expectedValue.append("create or replace type body EmptyType\n");
        expectedValue.append("as\n");
        expectedValue.append("function getName() return varchar2 as\n");
        expectedValue.append("begin\n");
        expectedValue.append("return 'test';\n");
        expectedValue.append("end;\n");
        expectedValue.append("end;\n");

        assertEquals(expectedValue.toString(), actualValue.toString());
    }

    @Test
    public void testTypeObjectBodySourceAppender3() {
        SqlSourceVisitor appender = new TypeObjectBodySourceAppender();
        StringBuilder typeSource = getTestData3();
        appender.visit(typeSource, "function getName() return varchar2 as\nbegin\nreturn 'test';\nend;");
        assertEquals(getTestData3().toString(), typeSource.toString());
    }

    private StringBuilder getTestData3() {
        StringBuilder actualValue = new StringBuilder();

        actualValue.append("create or replace type EmptyType\n");
        actualValue.append("as\n");
        actualValue.append("end EmptyType;\n");
        return actualValue;
    }

}
