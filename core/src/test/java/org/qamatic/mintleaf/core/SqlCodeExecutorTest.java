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

package org.qamatic.mintleaf.core;

import org.junit.Test;
import org.qamatic.mintleaf.interfaces.DbContext;

import static org.junit.Assert.*;

public class SqlCodeExecutorTest {

    @Test
    public void testCheckTemplateValuesNotNull() {
        SqlCodeExecutor executor = new SqlCodeExecutor(null);
        assertNotNull(executor.getTemplateValues());
        assertNull(executor.getChildReaderListener());
        executor.setChildReaderListener(new SqlCodeExecutor(null));
        assertNotNull(executor.getChildReaderListener());
    }


    @Test
    public void testfindAndReplace() {

        SqlCodeExecutorMock executor = new SqlCodeExecutorMock(null);
        StringBuilder inputText = new StringBuilder();
        inputText.append("here is my text $x");
        executor.getTemplateValues().put("$x", "blue");
        executor.preProcess(inputText);
        assertEquals("here is my text blue", inputText.toString());
    }

    private class SqlCodeExecutorMock extends SqlCodeExecutor {

        public SqlCodeExecutorMock(DbContext context) {
            super(context);
        }

        @Override
        public void preProcess(StringBuilder sqlText) {
            super.preProcess(sqlText);
        }
    }

}
