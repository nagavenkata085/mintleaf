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
import org.qamatic.mintleaf.oracle.codeobjects.PLAssignmentStatement;
import org.qamatic.mintleaf.oracle.codeobjects.PLForLoop;
import org.qamatic.mintleaf.oracle.codeobjects.PLStringBuilder;

import static org.junit.Assert.assertEquals;

public class PLForLoopTest {

    @Test
    public void testBasicLoopTest() {
        PLForLoop forLoop = new PLForLoop("i", "1", "10");
        forLoop.addStatement(new PLAssignmentStatement("cal", "i*30"));
        PLStringBuilder exepctedValue = new PLStringBuilder();
        exepctedValue.appendLine("for i in 1..10");
        exepctedValue.appendLine("loop");
        exepctedValue.appendLine("cal := i*30;");
        exepctedValue.appendLine("end loop;");
        assertEquals(exepctedValue.toString(), forLoop.toString());
    }

    @Test
    public void testBasicReverseLoopTest() {
        PLForLoop forLoop = new PLForLoop("i", "1", "10", true);
        forLoop.addStatement(new PLAssignmentStatement("cal", "i*30"));
        PLStringBuilder exepctedValue = new PLStringBuilder();
        exepctedValue.appendLine("for i in reverse 1..10");
        exepctedValue.appendLine("loop");
        exepctedValue.appendLine("cal := i*30;");
        exepctedValue.appendLine("end loop;");
        assertEquals(exepctedValue.toString(), forLoop.toString());
    }

}
