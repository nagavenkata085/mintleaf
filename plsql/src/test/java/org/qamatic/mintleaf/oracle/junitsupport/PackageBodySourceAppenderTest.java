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
import org.qamatic.mintleaf.oracle.SqlSourceVisitor;
import org.qamatic.mintleaf.oracle.codeobjects.PLStringBuilder;
import org.qamatic.mintleaf.oracle.codevisitors.PackageBodySourceAppender;

import static org.junit.Assert.assertEquals;

public class PackageBodySourceAppenderTest {

    @Test
    public void testPackageBodySourceNotFoundCase() {
        PackageBodySourceAppender appender = new PackageBodySourceAppender();
        StringBuilder packageSource = getTestData1();
        appender.visit(packageSource, "function getName() return varchar2 as\nbegin\nreturn 'test';\nend;");
        assertEquals(getExpectedData1().toString(), packageSource.toString());
    }

    @Test
    public void testPackageBodySourceAppender2() {
        SqlSourceVisitor appender = new PackageBodySourceAppender();
        StringBuilder packageSource = getTestData2();
        appender.visit(packageSource, "function getName() return varchar2 as\nbegin\nreturn 'test';\nend;");
        assertEquals(getExpectedData2().toString(), packageSource.toString());
    }

    @Test
    public void testPackageBodySourceAppender3() {
        SqlSourceVisitor appender = new PackageBodySourceAppender();
        StringBuilder packageSource = getTestData3();
        appender.visit(packageSource, "function getName() return varchar2 as\nbegin\nreturn 'test';\nend;");
        assertEquals(getTestData3().toString(), packageSource.toString());
    }

    @Test
    public void testPackageBodySourceAtBegining() {

        PLStringBuilder packageSource = new PLStringBuilder();
        packageSource.appendLine("create or replace package body EmptyPackage");
        packageSource.appendLine("as");
        packageSource.appendLine("--@insert code1");
        packageSource.appendLine("function getName() return varchar2 as");
        packageSource.appendLine("begin");
        packageSource.appendLine("return 'test';");
        packageSource.appendLine("end;");
        packageSource.appendLine("end;");

        PLStringBuilder newFunctionToAdd = new PLStringBuilder();
        newFunctionToAdd.appendLine("function getName2() return varchar2 as");
        newFunctionToAdd.appendLine("begin");
        newFunctionToAdd.appendLine("return 'test';");
        newFunctionToAdd.append("end;");

        PackageBodySourceAppender appender = new PackageBodySourceAppender("--@insert code1");

        appender.visit(packageSource.getStringBuilder(), newFunctionToAdd.toString());

        PLStringBuilder exepctedValue = new PLStringBuilder();
        exepctedValue.appendLine("create or replace package body EmptyPackage");
        exepctedValue.appendLine("as");
        exepctedValue.appendLine("function getName2() return varchar2 as");
        exepctedValue.appendLine("begin");
        exepctedValue.appendLine("return 'test';");
        exepctedValue.appendLine("end;");
        exepctedValue.appendLine("function getName() return varchar2 as");
        exepctedValue.appendLine("begin");
        exepctedValue.appendLine("return 'test';");
        exepctedValue.appendLine("end;");
        exepctedValue.appendLine("end;");

        assertEquals(exepctedValue.toString(), packageSource.toString());
    }

    private StringBuilder getTestData1() {
        StringBuilder builder = new StringBuilder();

        builder.append("create or replace package body EmptyPackage\n");
        builder.append("as\n");
        builder.append("end EmptyPackage;\n");
        return builder;
    }

    private StringBuilder getTestData3() {
        StringBuilder builder = new StringBuilder();

        builder.append("create or replace package EmptyPackage\n");
        builder.append("as\n");
        builder.append("end EmptyPackage;\n");
        return builder;
    }

    private StringBuilder getExpectedData1() {
        StringBuilder builder = new StringBuilder();

        builder.append("create or replace package body EmptyPackage\n");
        builder.append("as\n");
        builder.append("function getName() return varchar2 as\n");
        builder.append("begin\n");
        builder.append("return 'test';\n");
        builder.append("end;\n");
        builder.append("end EmptyPackage;\n");
        return builder;
    }

    private StringBuilder getTestData2() {
        StringBuilder builder = new StringBuilder();

        builder.append("create or replace package body EmptyPackage\n");
        builder.append("as\n");
        builder.append("end;\n");
        return builder;
    }

    private StringBuilder getExpectedData2() {
        StringBuilder builder = new StringBuilder();

        builder.append("create or replace package body EmptyPackage\n");
        builder.append("as\n");
        builder.append("function getName() return varchar2 as\n");
        builder.append("begin\n");
        builder.append("return 'test';\n");
        builder.append("end;\n");
        builder.append("end;\n");
        return builder;
    }

}
