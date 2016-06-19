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

package org.qamatic.mintleaf.oracle.annotations;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.qamatic.mintleaf.core.SqlObjectDependsOn;
import org.qamatic.mintleaf.core.SqlObjectHelper;
import org.qamatic.mintleaf.core.SqlObjectInfo;
import org.qamatic.mintleaf.interfaces.DbContext;
import org.qamatic.mintleaf.interfaces.SqlObject;
import org.qamatic.mintleaf.interfaces.SqlStoredProcedureModule;
import org.qamatic.mintleaf.oracle.DbUtility;
import org.qamatic.mintleaf.oracle.OraclePackage;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class SqlObjectHelperTest {


    @Before
    public void init() {
    }

    @After
    public void cleanUp() {

    }

    @Test
    public void testPLPackageAnnotationProperty() {
        MockTestPackage3 pkg = new MockTestPackage3(null);
        SqlObjectInfo plAnnotation = SqlObjectHelper.getDbObjectInfo(pkg);
        assertNotNull(plAnnotation);
        assertEquals("MockTestPackage2", plAnnotation.name());
        assertEquals("/MockTestPackage2.sql", plAnnotation.source());
    }

    @Test
    public void testPLObjectDefaultDelimiters() {
        MockTestPackage3 pkg = new MockTestPackage3(null);
        SqlObjectInfo plAnnotation = SqlObjectHelper.getDbObjectInfo(pkg);
        assertNotNull(plAnnotation);
        assertEquals("", plAnnotation.sourceDelimiter());
        assertEquals("", plAnnotation.dropSourceDelimiter());
    }

    @Test
    public void testPLObjectCustomDelimiters() {
        DelimiterPkg pkg = new DelimiterPkg(null);
        SqlObjectInfo plAnnotation = SqlObjectHelper.getDbObjectInfo(pkg);
        assertNotNull(plAnnotation);
        assertEquals(";", plAnnotation.sourceDelimiter());
        assertEquals(";", plAnnotation.dropSourceDelimiter());
    }

    @Test
    public void testPLPackageAnnotationProperty2() {
        MockTestPackage2 pkg = new MockTestPackage2(null);
        SqlObjectInfo plAnnotation = SqlObjectHelper.getDbObjectInfo(pkg);
        assertNull(plAnnotation);

        assertNull(pkg.getName());
        assertNull(pkg.getSource());

    }

    @Test
    public void testPLImportAnnotationNull() {
        MockTestPackage2 pkg = new MockTestPackage2(null);
        SqlObjectDependsOn plAnnotation = SqlObjectHelper.getPLImportAnnotation(pkg);
        assertNull(plAnnotation);
    }

    @Test
    public void testPLImportAnnotation() {
        MockTestPackage3 pkg = new MockTestPackage3(null);
        SqlObjectDependsOn plAnnotation = SqlObjectHelper.getPLImportAnnotation(pkg);
        assertNotNull(plAnnotation);
        assertEquals(2, plAnnotation.Using().length);
        assertTrue(plAnnotation.Using()[0] == DbUtility.class);
        assertTrue(plAnnotation.Using()[1] == MockTestPackage2.class);
    }

    @Test
    public void testPLImportWalkDependencyWithNoDependency() {
        List<Class<MockTestPackage2>> pkgList = new ArrayList<Class<MockTestPackage2>>();
        SqlObjectHelper.walkDependency(pkgList, MockTestPackage2.class);
        assertEquals(0, pkgList.size());

    }

    @Test
    public void testPLImportWalkDependencyWithDependency() {
        List<Class<MockTestPackage3>> pkgList = new ArrayList<Class<MockTestPackage3>>();
        SqlObjectHelper.walkDependency(pkgList, MockTestPackage3.class);

        assertEquals(pkgList.size(), 2);


        assertEquals(pkgList.get(0), DbUtility.class);
        assertEquals(pkgList.get(1), MockTestPackage2.class);
    }

    @Test
    public void testPLImporgetPackageDependency() {
        MockTestPackage3 pkg = new MockTestPackage3(null);

        Class<? extends SqlObject>[] items = SqlObjectHelper.getDependencyItems(pkg);
        assertNotNull(items);

        assertEquals(items.length, 2);


        assertEquals(items[0], DbUtility.class);
        assertEquals(items[1], MockTestPackage2.class);
    }

    @Test
    public void testPLImportWalkDependencyWithDependency2() {
        List<Class<MockTestPackage5>> pkgList = new ArrayList<Class<MockTestPackage5>>();
        SqlObjectHelper.walkDependency(pkgList, MockTestPackage5.class);

        assertEquals(pkgList.size(), 4);
        Object[] items = pkgList.toArray();

        assertEquals(items[0], DbUtility.class);
        assertEquals(items[1], MockTestPackage2.class);
        assertEquals(items[2], MockTestPackage3.class);
        assertEquals(items[3], MockTestPackage4.class);

    }

    @Test
    public void testPLImportWalkDependencyWithSelfDependency() {
        List<Class<MockTestPackageSelfDependency>> pkgList = new ArrayList<Class<MockTestPackageSelfDependency>>();
        SqlObjectHelper.walkDependency(pkgList, MockTestPackageSelfDependency.class);

        assertEquals(pkgList.size(), 4);
        @SuppressWarnings("unchecked")
        Class<? extends SqlStoredProcedureModule>[] items = pkgList.toArray(new Class[pkgList.size()]);



        assertEquals(items[0], DbUtility.class);
        assertEquals(items[1], MockTestPackage2.class);
        assertEquals(items[2], MockTestPackage3.class);
        assertEquals(items[3], MockTestPackage4.class);

    }

    @Test
    public void testPLImportWalkDependencyWithCircularDependency() {
        List<Class<MockTestPackageCircularDependency>> pkgList = new ArrayList<Class<MockTestPackageCircularDependency>>();
        try {
            SqlObjectHelper.walkDependency(pkgList, MockTestPackageCircularDependency.class);
        } catch (IllegalStateException e1) {
            assertTrue(true);
            return;
        }

        assertTrue("circular dependency not detected, logic broken", false);

    }

    @SqlObjectDependsOn(Using = {PackageDependency2.class, TestLog.class, MockTestPackageCircularDependency.class})
    private class MockTestPackageCircularDependency extends OraclePackage {
        public MockTestPackageCircularDependency(DbContext context) {
            super(context);
        }
    }

    @SqlObjectDependsOn(Using = {PackageDependency2.class})
    private class PackageDependency1 extends OraclePackage {
        public PackageDependency1(DbContext context) {
            super(context);

        }
    }

    @SqlObjectInfo(name = "TestLog", source = "/TestLog.sql")
    private class TestLog extends OraclePackage {

        public TestLog(DbContext context) {
            super(context);
        }
    }

    @SqlObjectDependsOn(Using = {PackageDependency1.class})
    private class PackageDependency2 extends OraclePackage {
        public PackageDependency2(DbContext context) {
            super(context);

        }
    }

    @SqlObjectDependsOn(Using = {MockTestPackageSelfDependency.class, MockTestPackage4.class, DbUtility.class, MockTestPackage2.class})
    private class MockTestPackageSelfDependency extends OraclePackage {
        public MockTestPackageSelfDependency(DbContext context) {
            super(context);

        }

    }

    @SqlObjectDependsOn(Using = {MockTestPackage4.class, DbUtility.class, MockTestPackage2.class})
    private class MockTestPackage5 extends OraclePackage {
        public MockTestPackage5(DbContext context) {
            super(context);

        }

    }

    @SqlObjectDependsOn(Using = {MockTestPackage3.class, DbUtility.class, MockTestPackage2.class})
    private class MockTestPackage4 extends OraclePackage {
        public MockTestPackage4(DbContext context) {
            super(context);

        }
    }

    @SqlObjectInfo(name = "MockTestPackage2", source = "/MockTestPackage2.sql")
    @SqlObjectDependsOn(Using = {DbUtility.class, MockTestPackage2.class})
    private class MockTestPackage3 extends OraclePackage {
        public MockTestPackage3(DbContext context) {
            super(context);

        }

    }

    private class MockTestPackage2 extends OraclePackage {
        public MockTestPackage2(DbContext context) {
            super(context);

        }

    }

    @SqlObjectInfo(name = "DelimiterPkg", sourceDelimiter = ";", dropSourceDelimiter = ";", source = "")
    private class DelimiterPkg extends OraclePackage {
        public DelimiterPkg(DbContext context) {
            super(context);

        }

    }
}
