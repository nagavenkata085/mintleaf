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
import org.qamatic.mintleaf.oracle.codeobjects.*;

import static org.junit.Assert.assertEquals;

public class PLExceptionTest {

    @Test
    public void testPLExceptionBlockTest() {
        PLException exceptionBlock = new PLException();
        exceptionBlock.addStatement(new PLAssignmentStatement("x", "10"));
        exceptionBlock.addStatement(new PLExecuteImmediate("ALTER SESSION SET SQL_TRACE TRUE", "INTO REC"));

        PLWhenStatement exception1 = exceptionBlock.addExceptionCondition("no_data_found");
        exception1.addStatement(new PLMethodInvoke("docalc", "'test'", "10"));

        PLWhenStatement exception2 = exceptionBlock.addExceptionCondition("too_many_rows");
        exception2.addStatement(new PLMethodInvoke("raise"));

        PLStringBuilder exepctedValue = new PLStringBuilder();
        exepctedValue.appendLine("begin");
        exepctedValue.appendLine("x := 10;");
        exepctedValue.appendLine("execute immediate ALTER SESSION SET SQL_TRACE TRUE INTO REC;");
        exepctedValue.appendLine("exception");
        exepctedValue.appendLine("when no_data_found then");
        exepctedValue.appendLine("docalc('test', 10);");
        exepctedValue.appendLine("when too_many_rows then");
        exepctedValue.appendLine("raise;");
        exepctedValue.appendLine("end;");
        assertEquals(exepctedValue.toString(), exceptionBlock.toString());
    }

}
