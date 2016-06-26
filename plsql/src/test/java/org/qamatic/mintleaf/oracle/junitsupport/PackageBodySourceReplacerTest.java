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
import org.qamatic.mintleaf.oracle.codevisitors.SqlSourceReplacer;

import static org.junit.Assert.assertEquals;

public class PackageBodySourceReplacerTest {

    @Test
    public void testPackageBodySourceAppender1() {

        SqlSourceVisitor appender = new SqlSourceReplacer();
        StringBuilder packageSource = getTestData1();
        appender.visit(packageSource, new Object[]{"location1", "systemundertest\n"});
        assertEquals(getExpectedData1().toString(), packageSource.toString());
    }


    @Test
    public void testgetStartIndex() {

        SqlSourceReplacer replacer = new SqlSourceReplacer();
        assertEquals(33, replacer.getStartIdx(getTestData2(), "--@testoverride 12345"));
    }


    @Test
    public void testgetEndIndex() {

        SqlSourceReplacer replacer = new SqlSourceReplacer();
        assertEquals(72, replacer.getEndIdx(getTestData2(), 33));
    }


    private String getTestData2() {

        return "1234567890 1234567890 1234567890 --@testoverride 12345 1234567890 --@end 1234567890 1234567890";
    }

    private StringBuilder getTestData1() {
        StringBuilder builder = new StringBuilder();

        builder.append("create or replace package body EmptyPackage\n");
        builder.append("as\n");
        builder.append("--@testoverride location1");
        builder.append("systemundertest");
        builder.append("--@end");
        builder.append("end EmptyPackage;\n");
        return builder;
    }


    private StringBuilder getExpectedData1() {
        StringBuilder builder = new StringBuilder();

        builder.append("create or replace package body EmptyPackage\n");
        builder.append("as\n");
        builder.append("systemundertest\n");
        builder.append("end EmptyPackage;\n");
        return builder;
    }


}
