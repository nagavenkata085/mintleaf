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
