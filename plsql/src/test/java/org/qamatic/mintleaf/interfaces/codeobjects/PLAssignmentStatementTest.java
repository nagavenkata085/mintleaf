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

import org.junit.Assert;
import org.junit.Test;
import org.qamatic.mintleaf.oracle.codeobjects.PLAssignmentStatement;

import static org.junit.Assert.assertEquals;

public class PLAssignmentStatementTest {

    @Test
    public void testPLAssignmentStatement() {
        Assert.assertEquals(String.format("x := y;%n"), new PLAssignmentStatement("x", "y").toString());

        assertEquals(String.format("target.x := to_date(source.x, 'ddmmyy');%n"),
                new PLAssignmentStatement("target.x", "to_date(source.x, 'ddmmyy')").toString());

    }

    @Test
    public void testPLAssignmentStatementInPlaceAssign() {
        assertEquals(String.format("x = y"), new PLAssignmentStatement("x", "y", true).toString());

        assertEquals(String.format("target.x = to_date(source.x, 'ddmmyy')"),
                new PLAssignmentStatement("target.x", "to_date(source.x, 'ddmmyy')", true).toString());

    }
}
