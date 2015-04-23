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
import org.qamatic.mintleaf.oracle.codeobjects.PLAssignmentStatement;
import org.qamatic.mintleaf.oracle.codeobjects.PLForLoop;
import org.qamatic.mintleaf.oracle.codeobjects.PLStringBuilder;

import static org.junit.Assert.assertEquals;

public class PLForLoopTest {

    @Test
    public void testBasicLoopTest() {
        PLForLoop forLoop = new PLForLoop("i", "1", "10");
        forLoop.addStatement(new PLAssignmentStatement("cal", "i*30"));
        PLStringBuilder exepctedValue = new PLStringBuilder();
        exepctedValue.appendLine("for i in 1..10");
        exepctedValue.appendLine("loop");
        exepctedValue.appendLine("cal := i*30;");
        exepctedValue.appendLine("end loop;");
        assertEquals(exepctedValue.toString(), forLoop.toString());
    }

    @Test
    public void testBasicReverseLoopTest() {
        PLForLoop forLoop = new PLForLoop("i", "1", "10", true);
        forLoop.addStatement(new PLAssignmentStatement("cal", "i*30"));
        PLStringBuilder exepctedValue = new PLStringBuilder();
        exepctedValue.appendLine("for i in reverse 1..10");
        exepctedValue.appendLine("loop");
        exepctedValue.appendLine("cal := i*30;");
        exepctedValue.appendLine("end loop;");
        assertEquals(exepctedValue.toString(), forLoop.toString());
    }

}
