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

package org.qamatic.mintleaf.oracle;


import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class UnitTestHelperTest {

    @Test
    public void testGetListnerForPackageTestsThatContainsValidOnes() {
        VisitorSqlCodeExecutor listener = (VisitorSqlCodeExecutor) SqlPartListeners.getPLPackageSectionalListner(null,
                "/TestConst_Sections.sql", null);
        assertNotNull(listener.getInterfaceSource());
        assertNotNull(listener.getBodySource());

    }

    @Test
    public void testGetListnerForPackageTestsSectionalFileButPackageNotThere() {
        VisitorSqlCodeExecutor listener = (VisitorSqlCodeExecutor) SqlPartListeners.getPLPackageSectionalListner(null,
                "/SqlPartReaderTest.sql", null);
        assertNull(listener.getInterfaceSource());
        assertNull(listener.getBodySource());

    }

    @Test
    public void testGetListnerForPackageTestsForInvalidFile() {
        VisitorSqlCodeExecutor listener = (VisitorSqlCodeExecutor) SqlPartListeners.getPLPackageSectionalListner(null, "/Testddl.sql", null);
        assertNull(listener.getInterfaceSource());
        assertNull(listener.getBodySource());

    }
}
