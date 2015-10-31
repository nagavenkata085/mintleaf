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

package org.qamatic.mintleaf.interfaces.codeobjects;


import org.junit.Test;
import org.qamatic.mintleaf.oracle.codeobjects.*;

import static org.junit.Assert.assertEquals;

public class PLExceptionTest {

    @Test
    public void testPLExceptionBlockTest() {
        PLException exceptionBlock = new PLException();
        exceptionBlock.addStatement(new PLAssignmentStatement("x", "10"));
        exceptionBlock.addStatement(new PLExecuteImmediate("ALTER SESSION SET SQL_TRACE TRUE", "INTO REC"));

        PLWhenStatement exception1 = exceptionBlock.addExceptionCondition("no_data_found");
        exception1.addStatement(new PLMethodInvoke("docalc", "'test'", "10"));

        PLWhenStatement exception2 = exceptionBlock.addExceptionCondition("too_many_rows");
        exception2.addStatement(new PLMethodInvoke("raise"));

        PLStringBuilder exepctedValue = new PLStringBuilder();
        exepctedValue.appendLine("begin");
        exepctedValue.appendLine("x := 10;");
        exepctedValue.appendLine("execute immediate ALTER SESSION SET SQL_TRACE TRUE INTO REC;");
        exepctedValue.appendLine("exception");
        exepctedValue.appendLine("when no_data_found then");
        exepctedValue.appendLine("docalc('test', 10);");
        exepctedValue.appendLine("when too_many_rows then");
        exepctedValue.appendLine("raise;");
        exepctedValue.appendLine("end;");
        assertEquals(exepctedValue.toString(), exceptionBlock.toString());
    }

}
