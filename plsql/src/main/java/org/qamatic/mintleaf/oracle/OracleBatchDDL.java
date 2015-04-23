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

import org.qamatic.mintleaf.core.SqlDDL;
import org.qamatic.mintleaf.interfaces.DbContext;
import org.qamatic.mintleaf.interfaces.SqlDDLObject;
import org.qamatic.mintleaf.interfaces.SqlReaderListener;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

public abstract class OracleBatchDDL extends SqlDDL implements SqlDDLObject, SqlReaderListener {

    private static final int BATCH_SIZE = 1000;

    private final StringBuilder mvScript = new StringBuilder();

    private int curSize;

    public OracleBatchDDL(DbContext context) {
        super(context);

    }


    @Override
    public void onReadChild(StringBuilder sql, Object context) throws SQLException, IOException {
        if (curSize > BATCH_SIZE) {
            mvScript.append("\nend;");
            execute();
            curSize = 0;
            mvScript.setLength(0);
        }

        if (mvScript.length() == 0) {
            mvScript.append("declare \n begin \n");
        }

        mvScript.append("\nexecute immediate '");
        mvScript.append(sql.toString().replace("'", "''"));
        mvScript.append("';\n");
        curSize++;

    }

    @Override
    public SqlReaderListener getSqlReadListener() {
        return this;
    }

    @Override
    public void create() throws SQLException, IOException {
        super.create();
        mvScript.append("\nend;");
        execute();
    }

    private void execute() {
        JdbcTemplate template = new JdbcTemplate(getDbContext().getDataSource());
        template.execute(getSql());
    }

    @Override
    public String getSql() {
        return mvScript.toString();
    }

    @Override
    public SqlReaderListener getChildReaderListener() {

        throw new UnsupportedOperationException();
    }

    @Override
    public void setChildReaderListener(SqlReaderListener childListener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<String, String> getTemplateValues() {

        throw new UnsupportedOperationException();
    }
}
