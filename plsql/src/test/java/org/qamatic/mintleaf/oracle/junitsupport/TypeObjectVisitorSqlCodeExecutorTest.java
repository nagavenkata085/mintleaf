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
import org.qamatic.mintleaf.interfaces.DbContext;
import org.qamatic.mintleaf.interfaces.SqlSourceVisitor;
import org.qamatic.mintleaf.oracle.TypeObjectVisitorSqlCodeExecutor;
import org.qamatic.mintleaf.oracle.codevisitors.TypeObjectBodySourceAppender;
import org.qamatic.mintleaf.oracle.codevisitors.TypeObjectSourceAppender;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TypeObjectVisitorSqlCodeExecutorTest {

    @Test
    public void testVisitors() {
        TypeObjectVisitorSqlCodeExecutorEx exector = new TypeObjectVisitorSqlCodeExecutorEx(null) {

        };

        assertEquals(1, exector.getInterfaceVisitors().length);
        assertTrue(exector.getInterfaceVisitors()[0] instanceof TypeObjectSourceAppender);
        assertEquals(1, exector.getBodyVisitors().length);
        assertTrue(exector.getBodyVisitors()[0] instanceof TypeObjectBodySourceAppender);

    }

    private class TypeObjectVisitorSqlCodeExecutorEx extends TypeObjectVisitorSqlCodeExecutor {

        public TypeObjectVisitorSqlCodeExecutorEx(DbContext context) {
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
