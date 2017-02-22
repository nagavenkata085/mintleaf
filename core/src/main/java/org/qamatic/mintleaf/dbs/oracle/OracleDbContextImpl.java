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

package org.qamatic.mintleaf.dbs.oracle;


import org.qamatic.mintleaf.DbColumn;
import org.qamatic.mintleaf.DbMetaData;
import org.qamatic.mintleaf.RowListener;
import org.qamatic.mintleaf.core.BaseDbContext;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Senthil Maruthaiappan on 4/6/15.
 */
public class OracleDbContextImpl extends BaseDbContext implements OracleDbContext {
    public OracleDbContextImpl(DataSource datasource) {
        super(datasource);
    }

    @Override
    public boolean isSqlObjectExists(String objectName, String objectType, boolean ignoreValidity) {
        String validSql = ignoreValidity ? "" : "status='VALID' AND ";
        int cnt = getCount("user_objects", validSql + "object_name = ? AND Object_Type = ?", new Object[]{objectName.toUpperCase(), objectType});
        return cnt != 0;
    }


    public boolean isColumnExists(String tableName, String columnName) {
        int cnt = getCount("user_tab_columns", "table_name = ? AND column_name = ?", new Object[]{tableName.toUpperCase(), columnName.toUpperCase()});
        return cnt != 0;
    }

    @Override
    public boolean isTableExists(String tableName) {
        return isSqlObjectExists(tableName, "TABLE", false);
    }

    @Override
    public boolean isdbFeatureExists(String featureName) {

        int cnt = getCount("dba_server_registry", "comp_id=? and status=?", new Object[]{featureName.toUpperCase(), "VALID"});
        return cnt != 0;
    }

    @Override
    public int getNextSequenceNumber(String sequenceName) {

        return queryInt(String.format("select %s.NextVal from dual", sequenceName), null);
    }

    @Override
    public void truncateTable(String tableName) throws SQLException {
        String sql = String.format("truncate  table %s", tableName);
        newQuery().withSql(sql).execute().close();
    }

    @Override
    public boolean isUserExists(String userName) {
        return getCount("all_users", "username = upper(?)", new Object[]{userName}) != 0;
    }

    @Override
    public List<String> getSqlObjects(String objectType) throws SQLException {

        return query(String.format("select object_name from user_objects where object_type='%s'", objectType), new RowListener<String>() {

            @Override
            public String eachRow(int row, ResultSet resultSet) throws SQLException {
                return resultSet.getString("object_name");
            }


        });
    }

    @Override
    public List<String> getPrimaryKeys(String ownerName, String tableName) throws SQLException {

        String owner = "";
        if (ownerName != null) {
            owner = String.format(" and ucc.owner=Upper('%s')", ownerName);
        }
        String sql = String
                .format("select ucc.column_name as keyname from all_constraints uc, all_cons_columns ucc where uc.table_name = upper('%s') and uc.constraint_type = 'P' and (uc.constraint_name=ucc.constraint_name) and  uc.owner=ucc.owner %s",
                        tableName, owner);
        return query(sql, new RowListener<String>() {

            @Override
            public String eachRow(int row, ResultSet resultSet) throws SQLException {
                return resultSet.getString("keyname");
            }


        });
    }

    @Override
    public boolean isPrivilegeExists(String granteeName, String privilegeName, String objectName) {
        int cnt = getCount("user_tab_privs", "grantee = ? AND table_name = ? AND privilege=?", new Object[]{granteeName.toUpperCase(), objectName.toUpperCase(),
                privilegeName.toUpperCase()});
        return cnt != 0;
    }

    @Override
    public boolean isSequenceExists(String sequenceName) {

        int cnt = getCount("user_sequences", "sequence_name = ?", new Object[]{sequenceName.toUpperCase()});
        return cnt != 0;
    }


    private String getSqlObjectMetaSql(String objectName) throws SQLException {
        final String objectName1 = objectName;
        final StringBuilder sql = new StringBuilder(
                String.format(
                        "Select Column_Name , Data_Type TYPE_NAME, Data_Length, Data_Precision , Data_Scale,Char_Length from all_tab_columns where table_name = upper('%s') ORDER BY column_id",
                        objectName, objectName));
        query(String.format("Select decode(object_type,'TYPE',1,0) istype FROM user_objects Where object_name = upper('%s')", objectName),
                new RowListener() {

                    @Override
                    public Object eachRow(int row, ResultSet resultSet) throws SQLException {

                        if (resultSet.getInt("ISTYPE") == 1) {
                            sql.setLength(0);
                            sql.append(String
                                    .format("select attr_name column_name, attr_type_name TYPE_NAME, length DATA_LENGTH, precision DATA_PRECISION, scale Data_Scale,length Char_Length from user_type_attrs where type_name =upper('%s')  order by attr_no",
                                            objectName1));
                        }

                        return null;
                    }
                });

        return sql.toString();
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public DbMetaData getMetaData(String objectName) throws SQLException {
        final DbMetaData metaData = new DbMetaData();
        if (objectName != null) {
            objectName = objectName.toUpperCase();
        }
        final String objectName1 = objectName;
        metaData.setObjectName(objectName);


        String sql = getSqlObjectMetaSql(objectName);
        query(sql, new RowListener<String>() {
            @Override
            public String eachRow(int row, ResultSet rs) throws SQLException {


                DbColumn colMetaData = new OracleColumn();
                colMetaData.setColumnName(rs.getString("COLUMN_NAME"));
                colMetaData.setTypeName(rs.getString("TYPE_NAME"));
                // colMetaData.setDatatype(rs.getString("DATA_TYPE"));
                // colMetaData.setNullable(rs.getInt("NULLABLE"));
                if (rs.getString("TYPE_NAME").equals("CHAR")) {
                    colMetaData.setColumnSize(rs.getInt("CHAR_LENGTH"));
                } else if (rs.getString("TYPE_NAME").equals("NUMBER")) {
                    colMetaData.setColumnSize(rs.getInt("DATA_PRECISION"));
                    colMetaData.setDecimalDigits(rs.getInt("DATA_SCALE"));

                } else {
                    colMetaData.setColumnSize(rs.getInt("DATA_LENGTH"));
                    colMetaData.setDecimalDigits(rs.getInt("DATA_PRECISION"));
                }
                metaData.add(colMetaData);
                return null;
            }
        });
        return metaData;

    }

    @Override
    public boolean isPackageExists(String pkgName, boolean igoreValidity) {
        return isSqlObjectExists(pkgName, "PACKAGE", igoreValidity);
    }

    @Override
    public boolean isPackageBodyExists(String pkgName, boolean igoreValidity) {
        return isSqlObjectExists(pkgName, "PACKAGE BODY", igoreValidity);
    }

    @Override
    public boolean isTypeExists(String typeName, boolean igoreValidity) {
        return isSqlObjectExists(typeName, "TYPE", igoreValidity);
    }

    @Override
    public boolean isTypeBodyExists(String typeName, boolean igoreValidity) {
        return isSqlObjectExists(typeName, "TYPE", igoreValidity);
    }

    @Override
    public boolean isTriggerExists(String triggerName, boolean igoreValidity) {
        return isSqlObjectExists(triggerName, "TRIGGER", igoreValidity);
    }

    @Override
    public boolean isSynonymExists(String synonymName, boolean igoreValidity) {
        return isSqlObjectExists(synonymName, "SYNONYM", igoreValidity);
    }


    public void dropObject(String objectName, String objectType) throws SQLException {
        dropObject(objectName, objectType, null);
    }

    public void dropObject(String objectName, String objectType, String clause) throws SQLException {
        objectType = objectType.toUpperCase();
        objectName = objectName.toUpperCase();
        if (!isSqlObjectExists(objectName, objectType, true)) {
            return;
        }

        newQuery().withSql(String.format("DROP %s %s %s", objectType, objectName, clause == null ? "" : "force")).execute().close();
    }


}
