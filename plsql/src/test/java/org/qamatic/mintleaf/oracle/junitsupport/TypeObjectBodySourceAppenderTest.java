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
import org.qamatic.mintleaf.interfaces.SqlSourceVisitor;
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
