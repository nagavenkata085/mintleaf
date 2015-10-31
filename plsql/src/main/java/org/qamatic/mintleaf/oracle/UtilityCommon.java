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


import org.qamatic.mintleaf.core.BaseSqlObject;
import org.qamatic.mintleaf.core.SqlObjectInfo;
import org.qamatic.mintleaf.interfaces.DbContext;
import org.qamatic.mintleaf.interfaces.SqlColumn;
import org.qamatic.mintleaf.interfaces.SqlObjectMetaData;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;


@SqlObjectInfo(name = "UtilityCommon")
public class UtilityCommon extends BaseSqlObject {

    public UtilityCommon(DbContext context) {
        super(context);
    }

    @Override
    public void drop() {
        // no need to drop, all test db assertions are depending on it.
    }

    public int getCount(String tableName, String whereClause, Object[] whereClauseValues) {
        JdbcTemplate template = new JdbcTemplate(getDbContext().getDataSource());

        String sql = "";
        if (whereClause != null) {
            sql = String.format("select count(*) from %s where %s", tableName, whereClause);
        } else {
            sql = String.format("select count(*) from %s", tableName);
        }
        return template.queryForInt(sql, whereClauseValues);
    }

    public int getCount(String tableName) {
        return getCount(tableName, null, null);
    }


    @SuppressWarnings({"unchecked", "rawtypes"})
    public SqlObjectMetaData getObjectMetaData(String objectName, boolean bUseSp) throws SQLException {
        final SqlObjectMetaData metaData = new SqlObjectMetaData();
        if (objectName != null) {
            objectName = objectName.toUpperCase();
        }
        final String objectName1 = objectName;
        metaData.setObjectName(objectName);
        if (bUseSp) {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(getDbContext().getDataSource());
            final StringBuilder sql = new StringBuilder(
                    String.format(
                            "Select Column_Name , Data_Type TYPE_NAME, Data_Length, Data_Precision , Data_Scale,Char_Length from all_tab_columns where table_name = upper(DbUtility.getTableNameIfExists('%s')) and owner=DbUtility.getOwnerName('%s') ORDER BY column_id",
                            objectName, objectName));

            jdbcTemplate.query(String.format("Select decode(object_type,'TYPE',1,0) istype FROM user_objects Where object_name = upper('%s')", objectName),
                    new RowMapper() {

                        @Override
                        public Object mapRow(ResultSet rs, int arg1) throws SQLException {
                            if (rs.getInt("ISTYPE") == 1) {
                                sql.setLength(0);
                                sql.append(String
                                        .format("select attr_name column_name, attr_type_name TYPE_NAME, length DATA_LENGTH, precision DATA_PRECISION, scale Data_Scale,length Char_Length from user_type_attrs where type_name =upper('%s')  order by attr_no",
                                                objectName1));
                            }

                            return null;
                        }
                    });
            jdbcTemplate.query(sql.toString(), new RowMapper<String>() {
                @Override
                public String mapRow(ResultSet rs, int rowNum) throws SQLException {

                    SqlColumn colMetaData = new SqlColumn();
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
        // some issue here closing result set
        Connection dconn = getDbContext().getDataSource().getConnection();
        ResultSet rs = dconn.getMetaData().getColumns(null, getDbContext().getDbSettings().getUsername(), objectName, null);
        while (rs.next()) {
            SqlColumn colMetaData = new SqlColumn();
            colMetaData.setColumnName(rs.getString("COLUMN_NAME"));
            colMetaData.setTypeName(rs.getString("TYPE_NAME"));
            colMetaData.setDatatype(rs.getInt("DATA_TYPE"));
            colMetaData.setNullable(rs.getInt("NULLABLE"));
            colMetaData.setColumnSize(rs.getInt("COLUMN_SIZE"));
            colMetaData.setDecimalDigits(rs.getInt("DECIMAL_DIGITS"));
            metaData.add(colMetaData);
        }
        rs.close();

        return metaData;
    }

}