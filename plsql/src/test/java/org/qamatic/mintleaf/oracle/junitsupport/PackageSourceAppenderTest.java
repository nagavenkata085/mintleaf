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
