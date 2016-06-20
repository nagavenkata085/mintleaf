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

package org.qamatic.mintleaf.dbsupportimpls.oracle;

import org.qamatic.mintleaf.core.BaseDbContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Senthil Maruthaiappan on 4/6/15.
 */
public class OracleDbContext extends BaseDbContext {
    public OracleDbContext(DataSource datasource) {
        super(datasource);
    }

    @Override
    public boolean isSqlObjectExists(String objectName, String objectType, boolean ignoreValidity) {
        String validSql = ignoreValidity ? "" : "status='VALID' AND ";
        int cnt = getCount("user_objects", validSql + "object_name = ? AND Object_Type = ?", new Object[]{objectName.toUpperCase(), objectType});
        return cnt != 0;
    }

    @Override
    public int getCount(String tableName, String whereClause, Object[] whereClauseValues) {
        JdbcTemplate template = new JdbcTemplate(getDataSource());

        String sql = "";
        if (whereClause != null) {
            sql = String.format("select count(*) from %s where %s", tableName, whereClause);
        } else {
            sql = String.format("select count(*) from %s", tableName);
        }
        return template.queryForInt(sql, whereClauseValues);
    }

    @Override
    public int getCount(String tableName) {
        return getCount(tableName, null, null);
    }

    public boolean isColumnExists(String tableName, String columnName) {
        int cnt =  getCount("user_tab_columns", "table_name = ? AND column_name = ?", new Object[]{tableName.toUpperCase(), columnName.toUpperCase()});
        return cnt != 0;
    }

    @Override
    public boolean isTableExists(String tableName) {
        return isSqlObjectExists(tableName, "TABLE", false);
    }

    @Override
    public boolean isdbFeatureExists(String featureName) {
        JdbcTemplate template = new JdbcTemplate();
        template.setDataSource(getDataSource());
        int cnt = getCount("dba_server_registry", "comp_id=? and status=?", new Object[]{featureName.toUpperCase(), "VALID"});
        return cnt != 0;
    }

    @Override
    public int getNextSequenceNumber(String sequenceName) {
        JdbcTemplate template = new JdbcTemplate();
        template.setDataSource(getDataSource());

        return template.queryForInt(String.format("select %s.NextVal from dual", sequenceName));
    }

    @Override
    public void truncateTable(String tableName) {
        JdbcTemplate template = new JdbcTemplate();
        template.setDataSource(getDataSource());
        String sql = String.format("truncate  table %s", tableName);
        template.execute(sql);
    }

    @Override
    public boolean isUserExists(String userName) {
        return getCount("all_users", "username = upper(?)", new Object[]{userName}) != 0;
    }

    @Override
    public List<String> getSqlObjects(String objectType) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource());
        return jdbcTemplate.query(String.format("select object_name from user_objects where object_type='%s'", objectType), new RowMapper<String>() {
            @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {

                return rs.getString("object_name");
            }
        });
    }

    @Override
    public List<String> getPrimaryKeys(String ownerName, String tableName) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource());

        String owner = "";
        if (ownerName != null){
            owner = String.format(" and ucc.owner=Upper('%s')", ownerName);
        }
        String sql = String
                .format("select ucc.column_name as keyname from all_constraints uc, all_cons_columns ucc where uc.table_name = upper('%s') and uc.constraint_type = 'P' and (uc.constraint_name=ucc.constraint_name) and  uc.owner=ucc.owner %s",
                        tableName, owner);
        return jdbcTemplate
                .query(sql, new RowMapper<String>() {
                    @Override
                    public String mapRow(ResultSet rs, int rowNum) throws SQLException {

                        return rs.getString("keyname");
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
}
