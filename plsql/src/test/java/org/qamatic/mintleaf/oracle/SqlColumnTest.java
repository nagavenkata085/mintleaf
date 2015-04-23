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

import org.junit.Assert;
import org.junit.Test;
import org.qamatic.mintleaf.interfaces.SqlColumn;

import java.sql.Types;

import static org.junit.Assert.assertEquals;

public class SqlColumnTest {
    @Test
    public void testSqlDataTypeCheckNULL() {
        Assert.assertEquals(Types.NULL, new SqlColumn().getDatatype());
    }

    @Test
    public void testSqlDataTypeAutoDetect() {
        SqlColumn d = new SqlColumn();

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
