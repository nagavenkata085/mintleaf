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

package org.qamatic.mintleaf.core;


import org.qamatic.mintleaf.DbContext;
import org.qamatic.mintleaf.SqlReader;
import org.qamatic.mintleaf.SqlReaderListener;
import org.qamatic.mintleaf.SqlScript;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

public class SqlScriptObject implements SqlScript {

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    protected DbContext mvContext;
    protected String mvSource;
    protected SqlReaderListener mvfirstChildListner;
    protected SqlReaderListener mvlistener;
    protected String mvdelimiter;

    public SqlScriptObject(DbContext context) {
        mvContext = context;
    }


    @Override
    public DbContext getDbContext() {
        return mvContext;
    }

    @Override
    public String getSource() {
        return mvSource;
    }

    @Override
    public void setSource(String source) {
        mvSource = source;
    }

    public final SqlReaderListener getChildListener() {
        return mvfirstChildListner;
    }

    protected String getCreateSourceDelimiter() {
        return mvdelimiter;
    }

    @Override
    public void create() throws SQLException, IOException {
        if (getSource() == null) {
            return;
        }
        createInternal();
    }

    protected void createInternal() throws IOException, SQLException {
        logger.info(String.format("DbSql.Create: source: %s", getSource()));
        SqlReader reader = getCreateSourceReader();
        if (getCreateSourceDelimiter() != null) {
            reader.setDelimiter(getCreateSourceDelimiter());
        }
        execute(reader);

    }

    public SqlReaderListener getSqlReadListener() {
        if (mvlistener == null) {
            mvlistener = new SqlExecutor(mvContext);
            mvlistener.setChildReaderListener(getChildListener());
        }

        return mvlistener;
    }

    protected SqlReader getCreateSourceReader(InputStream stream) {
        return new SqlFileReader(stream);
    }

    protected SqlReader getCreateSourceReader() {
        InputStream iStream = this.getClass().getResourceAsStream(getSource());
        return getCreateSourceReader(iStream);
    }

    protected String execute(SqlReader reader) throws IOException, SQLException {
        reader.setReaderListener(getSqlReadListener());
        return reader.read();
    }

}
