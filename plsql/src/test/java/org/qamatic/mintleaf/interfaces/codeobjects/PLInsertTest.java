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

package org.qamatic.mintleaf.interfaces.codeobjects;

import org.junit.Test;
import org.qamatic.mintleaf.oracle.codeobjects.PLInsert;

import static org.junit.Assert.assertEquals;

public class PLInsertTest {

    @Test
    public void testInsertWithoutColumn() {
        PLInsert insertStmt = new PLInsert("mytable");
        insertStmt.addColumnValue("x");
        assertEquals(String.format("insert into mytable values (x);%n"), insertStmt.toString());

        insertStmt = new PLInsert("mytable");
        insertStmt.addColumnValue("x");
        insertStmt.addColumnValue("2");
        insertStmt.addColumnValue("'name'");
        insertStmt.addColumnValue("self.deptid");
        assertEquals(String.format("insert into mytable values (x, 2, 'name', self.deptid);%n"), insertStmt.toString());
    }

    @Test
    public void testInsertHavingColumnAndColumnValues() {
        PLInsert insertStmt = new PLInsert("mytable");
        insertStmt.addColumn("id");
        insertStmt.addColumnValue("1");
        assertEquals(String.format("insert into mytable (id) values (1);%n"), insertStmt.toString());

        insertStmt = new PLInsert("mytable");
        insertStmt.addColumn("id");
        insertStmt.addColumn("name");
        insertStmt.addColumnValue("1");
        insertStmt.addColumnValue("'smaruth'");
        assertEquals(String.format("insert into mytable (id, name) values (1, 'smaruth');%n"), insertStmt.toString());
    }

}
