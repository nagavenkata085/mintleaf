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

package org.qamatic.mintleaf.oracle.argextensions;

import org.junit.Test;
import org.qamatic.mintleaf.interfaces.SqlArgument;
import org.qamatic.mintleaf.interfaces.SqlArgumentTypeExtension;
import org.qamatic.mintleaf.interfaces.SqlPackage;
import org.qamatic.mintleaf.oracle.spring.OracleSpringSqlProcedure;

import java.sql.Types;

import static org.junit.Assert.assertEquals;

public class OracleArgumentTypeExtensionTest {

    @Test
    public void testExtensionDefaultsValues() {
        SqlArgumentTypeExtension ext = new OracleArgumentTypeExtension();
        assertEquals("?", ext.getIdentifier());
        assertEquals("", ext.getIdentifierDeclaration());
        assertEquals("", ext.getTypeConversionCode());
        assertEquals("", ext.getAssignmentCodeAfterCall());
        assertEquals("", ext.getAssignmentCodeBeforeCall());
    }

    @Test
    public void testExtensionDefaultsWithProcedureInParam() {
        MockProcedure p = new MockProcedure(null);
        p.setSql("getEmployee");
        SqlArgument arg = p.createParameter("test1", Types.VARCHAR);
        SqlArgumentTypeExtension ext = arg.getTypeExtension();
        assertEquals("?", ext.getIdentifier());
        assertEquals("", ext.getIdentifierDeclaration());
        assertEquals("", ext.getTypeConversionCode());
        assertEquals("", ext.getAssignmentCodeAfterCall());
        assertEquals("", ext.getAssignmentCodeBeforeCall());
    }

    @Test
    public void testExtensionDefaultsWithProcedureOutParam() {
        MockProcedure p = new MockProcedure(null);
        p.setSql("getEmployee");
        SqlArgument arg = p.createOutParameter("test1", Types.VARCHAR);
        SqlArgumentTypeExtension ext = arg.getTypeExtension();
        assertEquals("?", ext.getIdentifier());
        assertEquals("", ext.getIdentifierDeclaration());
        assertEquals("", ext.getTypeConversionCode());
        assertEquals("", ext.getAssignmentCodeAfterCall());
        assertEquals("", ext.getAssignmentCodeBeforeCall());
    }

    private class MockProcedure extends OracleSpringSqlProcedure {

        public MockProcedure(SqlPackage pkg) {
            super(pkg);
        }

        @Override
        protected void initDataSource() {

        }

    }

}
