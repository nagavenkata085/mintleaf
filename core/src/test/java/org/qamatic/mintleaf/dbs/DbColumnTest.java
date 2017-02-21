/*
 * <!--
 *   ~
 *   ~ The MIT License (MIT)
 *   ~
 *   ~ Copyright (c) 2010-2017 Senthil Maruthaiappan
 *   ~
 *   ~ Permission is hereby granted, free of charge, to any person obtaining a copy
 *   ~ of this software and associated documentation files (the "Software"), to deal
 *   ~ in the Software without restriction, including without limitation the rights
 *   ~ to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   ~ copies of the Software, and to permit persons to whom the Software is
 *   ~ furnished to do so, subject to the following conditions:
 *   ~
 *   ~ The above copyright notice and this permission notice shall be included in all
 *   ~ copies or substantial portions of the Software.
 *   ~
 *   ~ THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   ~ IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   ~ FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *   ~ AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *   ~ LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   ~ OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *   ~ SOFTWARE.
 *   ~
 *   ~
 *   -->
 */

package org.qamatic.mintleaf.dbs;

import org.junit.Assert;
import org.junit.Test;
import org.qamatic.mintleaf.DbColumn;
import org.qamatic.mintleaf.dbs.oracle.OracleColumn;

import java.sql.Types;

import static org.junit.Assert.assertEquals;

public class DbColumnTest {
    @Test
    public void testSqlDataTypeCheckNULL() {
        Assert.assertEquals(Types.NULL, new OracleColumn().getDatatype());
    }

    @Test
    public void testSqlDataTypeAutoDetect() {
        DbColumn d = new OracleColumn();

        d.setTypeName("varchar2");
        d.setColumnSize(10);
        assertEquals("varchar2(10 CHAR)", d.getTypeName());

        d.setTypeName("number");
        d.setColumnSize(10);
        d.setDecimalDigits(2);
        assertEquals("number(10,2)", d.getTypeName());
        d.setDatatype(Types.NUMERIC);
        assertEquals(Double.class, d.getJavaDataType());
        d.setDecimalDigits(0);
        d.setDatatype(Types.NUMERIC);
        assertEquals(Integer.class, d.getJavaDataType());
        d.setDatatype(Types.CHAR);
        assertEquals(String.class, d.getJavaDataType());
        d.setTypeName("date");
        d.setColumnSize(7);
        assertEquals("date", d.getTypeName());
        d.setTypeName("blob");
        d.setColumnSize(4000);
        assertEquals("blob", d.getTypeName());
        d.setTypeName("timestamp");
        d.setColumnSize(6);
        assertEquals("timestamp", d.getTypeName());
        d.setTypeName("timestamp(6)");
        d.setColumnSize(11);
        d.setDecimalDigits(6);
        assertEquals("timestamp(6)", d.getTypeName());
    }

}
