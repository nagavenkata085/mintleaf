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

package org.qamatic.mintleaf.oracle.junitsupport;

import org.junit.Test;
import org.qamatic.mintleaf.interfaces.SqlSourceVisitor;
import org.qamatic.mintleaf.oracle.codevisitors.PackageSourceAppender;

import static org.junit.Assert.assertEquals;

public class PackageSourceAppenderTest {

    @Test
    public void testPackageSourceAppender1() {
        SqlSourceVisitor appender = new PackageSourceAppender();
        StringBuilder packageSource = getTestData1();
        appender.visit(packageSource, "function getName() return varchar2;");
        assertEquals(getExpectedData1().toString(), packageSource.toString());
    }

    @Test
    public void testPackageSourceAppender2() {
        SqlSourceVisitor appender = new PackageSourceAppender();
        StringBuilder packageSource = getTestData2();
        appender.visit(packageSource, "function getName() return varchar2;");
        assertEquals(getExpectedData2().toString(), packageSource.toString());
    }

    @Test
    public void testPackageSourceAppender3() {
        SqlSourceVisitor appender = new PackageSourceAppender();
        StringBuilder packageSource = getTestData3();
        appender.visit(packageSource, "function getName() return varchar2;");
        assertEquals(getTestData3().toString(), packageSource.toString());
    }

    private StringBuilder getTestData1() {
        StringBuilder builder = new StringBuilder();

        builder.append("create or replace package EmptyPackage\n");
        builder.append("as\n");
        builder.append("end EmptyPackage;\n");
        return builder;
    }

    private StringBuilder getTestData3() {
        StringBuilder builder = new StringBuilder();

        builder.append("create or replace package body EmptyPackage\n");
        builder.append("as\n");
        builder.append("end EmptyPackage;\n");
        return builder;
    }

    private StringBuilder getExpectedData1() {
        StringBuilder builder = new StringBuilder();

        builder.append("create or replace package EmptyPackage\n");
        builder.append("as\n");
        builder.append("function getName() return varchar2;\n");
        builder.append("end EmptyPackage;\n");
        return builder;
    }

    private StringBuilder getTestData2() {
        StringBuilder builder = new StringBuilder();

        builder.append("create or replace package EmptyPackage\n");
        builder.append("as\n");
        builder.append("end;\n");
        return builder;
    }

    private StringBuilder getExpectedData2() {
        StringBuilder builder = new StringBuilder();

        builder.append("create or replace package EmptyPackage\n");
        builder.append("as\n");
        builder.append("function getName() return varchar2;\n");
        builder.append("end;\n");
        return builder;
    }

}
