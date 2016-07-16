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

package org.qamatic.mintleaf.oracle.spring;

import oracle.sql.CHAR;
import oracle.sql.NUMBER;
import org.qamatic.mintleaf.interfaces.*;
import org.qamatic.mintleaf.oracle.SqlTypeObjectValue;
import org.qamatic.mintleaf.oracle.core.DbMetaDataService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OracleTypeObjectValue implements SqlTypeObjectValue {

    private final String mvtypeName;
    protected DbContext mvDbContext;
    private List<Object> mvobjects;
    private DbMetaDataService mvmetaDataService;
    private DbMetaData mvmetaData;

    public OracleTypeObjectValue(DbContext context, String typeName) {
        mvtypeName = typeName;
        mvDbContext = context;
    }

    protected List<Object> getObjects() {
        if (mvobjects == null) {
            mvobjects = new ArrayList<Object>();
        }
        return mvobjects;
    }

    @SuppressWarnings("boxing")
    @Override
    public int getIntValue(String columnName) throws SQLException {
        return (Integer) getValue(columnName);
    }

    @Override
    public String getStringValue(String columnName) throws SQLException {
        return getValue(columnName).toString();
    }

    @SuppressWarnings("boxing")
    @Override
    public Object getValue(String columnName) throws SQLException {
        int idx = getFieldIndex(columnName);
        if (idx == -1) {
            return null;
        }
        Object vobj = getObjects().get(idx);
        if (vobj instanceof NUMBER) {
            NUMBER num = ((NUMBER) vobj);

            DbColumn colMetaData = getMetaData().findColumn(
                    columnName);
            if (colMetaData.getDecimalDigits() == 0) {
                vobj = num.intValue();
            } else {
                vobj = num.doubleValue();
            }
        }
        if (vobj instanceof CHAR) {
            vobj = ((CHAR) vobj).getString();
        }
        return vobj;
    }

    @Override
    public int getFieldIndex(String columnName) throws SQLException {
        return getMetaData().getIndex(columnName);
    }

    @Override
    public String getTypeName() {

        return mvtypeName;
    }

    public String getTypeNameWithSchemaName() {

        if (mvDbContext == null) {
            return getTypeName();
        }
        return mvDbContext.getDbSettings().getUsername() + "." + mvtypeName;
    }

    @Override
    public DbMetaData getMetaData() throws SQLException {
        if (getMetaDataService() == null) {
            if (mvmetaData == null) {
                mvmetaData = new DbMetaData();
            }
        } else {
            mvmetaData = getMetaDataService().getMetaData(getTypeNameWithSchemaName());
            if (mvmetaData == null) {
                mvmetaData = getMetaDataService().addMetaDataFromTable(mvDbContext, getTypeName());
            }
        }
        return mvmetaData;
    }

    @Override
    public void setMetaData(DbMetaData metaData) {
        mvmetaData = metaData;
    }

    @Override
    public DbMetaDataService getMetaDataService() {
        return mvmetaDataService;
    }

    @Override
    public void setMetaDataService(DbMetaDataService metaDataService) {
        mvmetaDataService = metaDataService;
    }
}
