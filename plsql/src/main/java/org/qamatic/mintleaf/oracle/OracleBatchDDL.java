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
