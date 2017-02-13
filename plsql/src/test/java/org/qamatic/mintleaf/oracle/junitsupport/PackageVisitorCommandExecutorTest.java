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
import org.qamatic.mintleaf.DbContext;
import org.qamatic.mintleaf.oracle.PackageVisitorCommandExecutor;
import org.qamatic.mintleaf.oracle.SqlSourceVisitor;
import org.qamatic.mintleaf.oracle.codevisitors.PackageBodySourceAppender;
import org.qamatic.mintleaf.oracle.codevisitors.PackageSourceAppender;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PackageVisitorCommandExecutorTest {

    @Test
    public void testVisitors() {
        PackageVisitorCommandExecutorEx exector = new PackageVisitorCommandExecutorEx(null) {

        };

        assertEquals(1, exector.getInterfaceVisitors().length);
        assertTrue(exector.getInterfaceVisitors()[0] instanceof PackageSourceAppender);
        assertEquals(1, exector.getBodyVisitors().length);
        assertTrue(exector.getBodyVisitors()[0] instanceof PackageBodySourceAppender);

    }

    private class PackageVisitorCommandExecutorEx extends PackageVisitorCommandExecutor {

        public PackageVisitorCommandExecutorEx(DbContext context) {
            super(context);

        }

        @Override
        public SqlSourceVisitor[] getInterfaceVisitors() {

            return super.getInterfaceVisitors();
        }

        @Override
        public SqlSourceVisitor[] getBodyVisitors() {

            return super.getBodyVisitors();
        }
    }
}
