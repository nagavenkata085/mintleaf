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
import org.qamatic.mintleaf.oracle.codeobjects.PLStringBuilder;
import org.qamatic.mintleaf.oracle.codevisitors.TypeObjectSourceAppender;

import static org.junit.Assert.assertEquals;

public class TypeObjectSourceAppenderTest {

    @Test
    public void testTypeObjectSourceAppender1() {
        PLStringBuilder inputValue = new PLStringBuilder();

        inputValue.appendLine("create or replace type MyObject as OBJECT(");
        inputValue.appendLine("id number");
        inputValue.appendLine(") not final;");

        PLStringBuilder expectedValue = new PLStringBuilder();
        expectedValue.appendLine("create or replace type MyObject as OBJECT(");
        expectedValue.appendLine("id number");
        expectedValue.appendLine(",function getName() return varchar2");
        expectedValue.appendLine(") not final;");

        SqlSourceVisitor appender = new TypeObjectSourceAppender();

        appender.visit(inputValue.getStringBuilder(), String.format(",function getName() return varchar2%n"));
        assertEquals(expectedValue.toString(), inputValue.toString());
    }

    @Test
    public void testTypeObjectSourceAppenderNotFoundCase() {
        PLStringBuilder inputValue = new PLStringBuilder();

        inputValue.appendLine("create or replace type body MyObject as OBJECT(");
        inputValue.appendLine("id number");
        inputValue.appendLine(") not final;");

        PLStringBuilder expectedValue = new PLStringBuilder();
        expectedValue.appendLine("create or replace type body MyObject as OBJECT(");
        expectedValue.appendLine("id number");
        expectedValue.appendLine(") not final;");

        SqlSourceVisitor appender = new TypeObjectSourceAppender();

        appender.visit(inputValue.getStringBuilder(), String.format(",function getName() return varchar2%n"));
        assertEquals(expectedValue.toString(), inputValue.toString());
    }

}
