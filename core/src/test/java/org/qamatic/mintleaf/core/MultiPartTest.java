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

package org.qamatic.mintleaf.core;

import org.junit.Assert;
import org.junit.Test;
import org.qamatic.mintleaf.interfaces.SqlPart;

import static org.junit.Assert.assertEquals;

public class MultiPartTest {

    @Test
    public void testSqlPartJson1() {
        String serialize = new SqlPart().toString();
        Assert.assertTrue(serialize, serialize.contains("<sqlPart name=\"\" delimiter=\"\"/>"));
    }

    @Test
    public void testSqlPartJson2() {
        String serialize = new SqlPart("test", ";", "").toString();
        Assert.assertTrue(serialize, serialize.contains("<sqlPart name=\"test\" delimiter=\";\"/>"));
    }

    @Test
    public void testMultiPartTagFromXml() {
        String xml = "<sqlpart name=\"part1\" delimiter=\"/\" />";
        SqlPart detail = SqlPart.xmlToSqlPart(xml);
        assertEquals("part1", detail.getName());
        assertEquals("/", detail.getDelimiter());
    }


}