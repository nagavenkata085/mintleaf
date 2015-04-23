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
import org.qamatic.mintleaf.core.BaseSqlObject;
import org.qamatic.mintleaf.core.SqlCodeExecutor;
import org.qamatic.mintleaf.core.SqlObjectInfo;
import org.qamatic.mintleaf.interfaces.DbContext;
import org.qamatic.mintleaf.interfaces.SqlObject;
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
        SqlReaderListener child1 = new SqlCodeExecutor(null);
        sqlObj.AddReaderListener(child1);
        SqlReaderListener child2 = new SqlCodeExecutor(null);
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
