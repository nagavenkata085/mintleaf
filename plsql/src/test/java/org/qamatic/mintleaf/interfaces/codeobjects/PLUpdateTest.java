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
