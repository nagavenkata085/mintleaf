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
import org.qamatic.mintleaf.oracle.VisitorSqlCodeExecutor;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.Assert.assertTrue;

public class VisitorSqlCodeExecutorTest {

    private static boolean mvvisitor1_hit;
    private static boolean mvvisitor2_hit;

    @Test
    public void testOnReadChild() throws SQLException, IOException {
        mvvisitor1_hit = false;
        mvvisitor2_hit = false;
        VisitorSqlCodeExecutor executor = new VisitorSqlCodeExecutor(null) {

            @Override
            protected SqlSourceVisitor[] getInterfaceVisitors() {

                return new SqlSourceVisitor[]{new Visitor1()};
            }

            @Override
            protected SqlSourceVisitor[] getBodyVisitors() {

                return new SqlSourceVisitor[]{new Visitor2()};
            }

            @Override
            protected void execute(StringBuilder sql) throws SQLException {

            }

        };

        executor.setBodySource("");
        executor.setInterfaceSource("");
        executor.onReadChild(new StringBuilder(), null);
        assertTrue(mvvisitor1_hit);
        assertTrue(mvvisitor2_hit);
    }

    private class Visitor1 implements SqlSourceVisitor {

        @Override
        public void visit(StringBuilder sql, Object actionContent) {
            mvvisitor1_hit = true;

        }

    }

    private class Visitor2 implements SqlSourceVisitor {

        @Override
        public void visit(StringBuilder sql, Object actionContent) {
            mvvisitor2_hit = true;

        }

    }
}
