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
