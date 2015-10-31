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
import org.qamatic.mintleaf.oracle.codeobjects.PLUpdate;

import static org.junit.Assert.assertEquals;

public class PLUpdateTest {

    @Test
    public void testUpdate() {
        PLUpdate updateStmt = new PLUpdate("mytable");
        updateStmt.setValue("x", "'a'");
        assertEquals(String.format("update mytable set x = 'a';%n"), updateStmt.toString());

        updateStmt = new PLUpdate("mytable");
        updateStmt.setValue("x", "(Select ac from account where id=2)");
        assertEquals(String.format("update mytable set x = (Select ac from account where id=2);%n"), updateStmt.toString());

        updateStmt = new PLUpdate("mytable");
        updateStmt.setValue("x", "2");
        updateStmt.setValue("y", "Self.Why");
        updateStmt.setValue("z", "'Zee'");
        assertEquals(String.format("update mytable set x = 2, y = Self.Why, z = 'Zee';%n"), updateStmt.toString());

    }

    @Test
    public void testUpdateWithTailClause() {
        PLUpdate updateStmt = new PLUpdate("mytable", "where everything='nothing'");
        updateStmt.setValue("x", "'a'");
        assertEquals(String.format("update mytable set x = 'a' where everything='nothing';%n"), updateStmt.toString());

        updateStmt = new PLUpdate("mytable", "where x=70");
        updateStmt.setValue("x", "2");
        updateStmt.setValue("y", "Self.Why");
        updateStmt.setValue("z", "'Zee'");
        assertEquals(String.format("update mytable set x = 2, y = Self.Why, z = 'Zee' where x=70;%n"), updateStmt.toString());

    }

}
