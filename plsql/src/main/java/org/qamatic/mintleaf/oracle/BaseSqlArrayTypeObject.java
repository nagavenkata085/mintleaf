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

package org.qamatic.mintleaf.oracle;

import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import oracle.sql.DatumWithConnection;
import oracle.sql.TypeDescriptor;
import org.qamatic.mintleaf.core.SqlObjectDependsOn;
import org.qamatic.mintleaf.interfaces.DbContext;

import java.sql.Connection;
import java.sql.SQLException;

@SqlObjectDependsOn(Using = {OracleDbHelper.class})
public abstract class BaseSqlArrayTypeObject extends OracleTypeObject {

    public BaseSqlArrayTypeObject(DbContext context) {
        super(context);

    }

    @Override
    protected TypeDescriptor getDescriptor(Connection conn, String typeName) throws SQLException {

        return new ArrayDescriptor(typeName, conn);
    }

    @Override
    protected DatumWithConnection getDatum(TypeDescriptor descriptor, Connection conn, String typeName) throws SQLException {

        return new ARRAY((ArrayDescriptor) getDescriptor(conn, typeName), conn, getValues());
    }
}
