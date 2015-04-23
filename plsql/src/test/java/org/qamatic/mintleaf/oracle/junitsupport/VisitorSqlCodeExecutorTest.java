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
