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
