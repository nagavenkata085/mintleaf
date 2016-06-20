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

import javax.sql.DataSource;

/**
 * Created by Senthil Maruthaiappan on 4/6/15.
 */
public class OracleDbContext extends BaseDbContext {
    public OracleDbContext(DataSource datasource) {
        super(datasource);
    }

    public boolean isSqlObjectExists(String objectName, String objectType, boolean ignoreValidity) {
        String validSql = ignoreValidity ? "" : "status='VALID' AND ";
        int cnt = getCount("user_objects", validSql + "object_name = ? AND Object_Type = ?", new Object[]{objectName.toUpperCase(), objectType});
        return cnt != 0;
    }

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

    public int getCount(String tableName) {
        return getCount(tableName, null, null);
    }
}
