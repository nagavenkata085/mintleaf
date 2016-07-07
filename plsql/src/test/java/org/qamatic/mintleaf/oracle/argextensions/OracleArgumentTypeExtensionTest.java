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

package org.qamatic.mintleaf.oracle.argextensions;

import org.junit.Test;
import org.qamatic.mintleaf.dbsupportimpls.oracle.OracleArgumentType;
import org.qamatic.mintleaf.interfaces.CustomArgumentType;
import org.qamatic.mintleaf.interfaces.DbContext;
import org.qamatic.mintleaf.interfaces.SqlArgument;
import org.qamatic.mintleaf.interfaces.SqlStoredProcedureModule;
import org.qamatic.mintleaf.oracle.spring.OraclePLProcedure;

import java.sql.Types;

import static org.junit.Assert.assertEquals;

public class OracleArgumentTypeExtensionTest {

    @Test
    public void testExtensionDefaultsValues() {
        CustomArgumentType ext = new OracleArgumentType();
        assertEquals("?", ext.getIdentifier());
        assertEquals("", ext.getVariableDeclaration());
        assertEquals("", ext.getTypeConversionCode());
        assertEquals("", ext.getCodeAfterCall());
        assertEquals("", ext.getCodeBeforeCall());
    }

    @Test
    public void testExtensionDefaultsWithProcedureInParam() {
        MockPLProcedure p = new MockPLProcedure(null);
        p.setSql("getEmployee");
        SqlArgument arg = p.createInParameter("test1", Types.VARCHAR);
        CustomArgumentType ext = arg.getTypeExtension();
        assertEquals("?", ext.getIdentifier());
        assertEquals("", ext.getVariableDeclaration());
        assertEquals("", ext.getTypeConversionCode());
        assertEquals("", ext.getCodeAfterCall());
        assertEquals("", ext.getCodeBeforeCall());
    }

    @Test
    public void testExtensionDefaultsWithProcedureOutParam() {
        MockPLProcedure p = new MockPLProcedure(null);
        p.setSql("getEmployee");
        SqlArgument arg = p.createOutParameter("test1", Types.VARCHAR);
        CustomArgumentType ext = arg.getTypeExtension();
        assertEquals("?", ext.getIdentifier());
        assertEquals("", ext.getVariableDeclaration());
        assertEquals("", ext.getTypeConversionCode());
        assertEquals("", ext.getCodeAfterCall());
        assertEquals("", ext.getCodeBeforeCall());
    }

    private class MockPLProcedure extends OraclePLProcedure {

        public MockPLProcedure(DbContext context) {
            super(context);
        }

        @Override
        protected void initDataSource() {

        }

    }

}
