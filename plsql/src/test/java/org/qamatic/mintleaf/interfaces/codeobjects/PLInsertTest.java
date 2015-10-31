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
