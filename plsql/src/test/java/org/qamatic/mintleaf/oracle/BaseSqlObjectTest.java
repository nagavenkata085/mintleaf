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

package org.qamatic.mintleaf.oracle;


import org.junit.Test;
import org.qamatic.mintleaf.oracle.core.BaseSqlObject;
import org.qamatic.mintleaf.core.SqlExecutor;
import org.qamatic.mintleaf.core.SqlObjectInfo;
import org.qamatic.mintleaf.interfaces.DbContext;
import org.qamatic.mintleaf.oracle.core.SqlObject;
import org.qamatic.mintleaf.interfaces.SqlReaderListener;
import org.qamatic.mintleaf.oracle.junitsupport.OracleTestCase;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static org.junit.Assert.*;

public final class BaseSqlObjectTest extends OracleTestCase {


    @Test
    public void testSqlObjectName() {
        SqlObject pkg = new TestObject1(getSchemaOwnerContext());
        assertEquals("atest_pkg1", pkg.getName());

        pkg = new TestObjectWithoutAnnotation(getSchemaOwnerContext());
        assertEquals(null, pkg.getName());

        pkg = new ATestPackage3(getSchemaOwnerContext());
        assertEquals(null, pkg.getName());
    }

    @Test
    public void testSourceTest2() {
        SqlObject pkg = new TestObjectWithoutAnnotation(getSchemaOwnerContext());
        assertNull(pkg.getSource());
    }

    @Test
    public void testSourceTest1() {
        SqlObject pkg = new TestObject1(getSchemaOwnerContext());
        assertEquals("/ATestPackage1.sql", pkg.getSource());
    }

    @Test
    public void testAddChildListener() {
        BaseSqlObject sqlObj = new BaseSqlObject(null) {
        };
        assertNull(sqlObj.getChildListener());
        SqlReaderListener listener = sqlObj.getSqlReadListener();
        assertNotNull(listener);
        assertNull(listener.getChildReaderListener());
        SqlReaderListener child1 = new SqlExecutor(null);
        sqlObj.AddReaderListener(child1);
        SqlReaderListener child2 = new SqlExecutor(null);
        sqlObj.AddReaderListener(child2);
        assertEquals(child1, sqlObj.getChildListener());
        assertEquals(child2, sqlObj.getChildListener().getChildReaderListener());

    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    private @interface ATestPackage3Annotation {
    }

    @SqlObjectInfo(name = "atest_pkg1", source = "/ATestPackage1.sql")
    private class TestObject1 extends BaseSqlObject {

        public TestObject1(DbContext context) {
            super(context);
        }

        @Override
        public void drop() {

        }

    }

    private class TestObjectWithoutAnnotation extends BaseSqlObject {

        public TestObjectWithoutAnnotation(DbContext context) {
            super(context);
        }

        @Override
        public void drop() {

        }

    }

    @ATestPackage3Annotation
    private class ATestPackage3 extends OraclePackage {

        public ATestPackage3(DbContext context) {
            super(context);

        }

    }
}
