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
import org.qamatic.mintleaf.oracle.codeobjects.PLDelete;

import static org.junit.Assert.assertEquals;

public class PLDeleteTest {

    @Test
    public void testDelete() {
        PLDelete deleteStmt = new PLDelete("mytable");
        assertEquals(String.format("delete from mytable;%n"), deleteStmt.toString());
    }

    @Test
    public void testUpdateWithTailClause() {
        PLDelete deleteStmt = new PLDelete("mytable", "where everything='something'");
        assertEquals(String.format("delete from mytable where everything='something';%n"), deleteStmt.toString());
    }

}
