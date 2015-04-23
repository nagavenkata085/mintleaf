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

package org.qamatic.mintleaf.core;

import org.qamatic.mintleaf.interfaces.DbContext;
import org.qamatic.mintleaf.interfaces.SqlReaderListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Map;

public class SqlCodeExecutor implements SqlReaderListener {

    protected final DbContext mvContext;
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private final Map<String, String> mvtemplateValues = new Hashtable<String, String>();
    private SqlReaderListener mvchildListner;

    public SqlCodeExecutor(DbContext context) {
        mvContext = context;
    }

    public static void replaceStr(StringBuilder sqlBuilder, String findStr, String replaceStr) {
        int indexOfTarget = -1;
        while ((indexOfTarget = sqlBuilder.indexOf(findStr)) > 0) {
            sqlBuilder.replace(indexOfTarget, indexOfTarget + findStr.length(), replaceStr);
        }
    }

    @Override
    public SqlReaderListener getChildReaderListener() {
        return mvchildListner;
    }

    @Override
    public void setChildReaderListener(SqlReaderListener childListner) {
        mvchildListner = childListner;
    }

    @Override
    public Map<String, String> getTemplateValues() {
        return mvtemplateValues;
    }

    private void findAndReplace(StringBuilder sqlText) {
        for (String key : getTemplateValues().keySet()) {
            onReplaceTemplateValue(sqlText, key, getTemplateValues().get(key).toString());
        }
    }

    protected void onReplaceTemplateValue(StringBuilder sqlBuilder, String findStr, String replaceStr) {
        replaceStr(sqlBuilder, findStr, replaceStr);
    }

    protected void preProcess(StringBuilder sqlText) {
        findAndReplace(sqlText);
    }

    @Override
    public void onReadChild(StringBuilder sql, Object context) throws SQLException, IOException {

        preProcess(sql);
        if (getChildReaderListener() != null) {
            getChildReaderListener().onReadChild(sql, context);
            return;
        }
        execute(sql);
    }

    protected void execute(StringBuilder sql) throws SQLException {
        try {
            JdbcTemplate template = new JdbcTemplate(mvContext.getDataSource());
            template.execute(sql.toString());
        } catch (Throwable e1) {

            logger.error("OracleSqlCodeExecutor:", e1);

            if (e1 instanceof SQLException) {
                throw new SQLException(e1);
            }
        }
    }

}
