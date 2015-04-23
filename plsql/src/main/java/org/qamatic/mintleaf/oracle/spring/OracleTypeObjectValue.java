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

package org.qamatic.mintleaf.oracle.spring;

import oracle.sql.CHAR;
import oracle.sql.NUMBER;
import org.qamatic.mintleaf.interfaces.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OracleTypeObjectValue implements SqlTypeObjectValue {

    private final String mvtypeName;
    protected DbContext mvDbContext;
    private List<Object> mvobjects;
    private DbMetaDataService mvmetaDataService;
    private SqlObjectMetaData mvmetaData;

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

            SqlColumn colMetaData = getMetaData().findColumn(
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
    public SqlObjectMetaData getMetaData() throws SQLException {
        if (getMetaDataService() == null) {
            if (mvmetaData == null) {
                mvmetaData = new SqlObjectMetaData();
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
    public void setMetaData(SqlObjectMetaData metaData) {
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
