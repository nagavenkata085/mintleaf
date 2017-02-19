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

package org.qamatic.mintleaf.core;


import org.qamatic.mintleaf.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

public abstract class BaseSqlScript implements SqlScript {

    private final static MintLogger logger = MintLogger.getLogger(BaseSqlScript.class);
    protected DbContext dbContext;
    protected SqlReaderListener sqlReaderListener;


    public BaseSqlScript(DbContext context) {
        dbContext = context;
    }

    @Override
    public DbContext getDbContext() {
        return dbContext;
    }

    @Override
    public void apply() throws SQLException, IOException {
        SqlReader reader = getSourceReader();
        execute(reader);
    }


    public SqlReaderListener getReadListener() {
        if (sqlReaderListener == null) {
            sqlReaderListener = new CommandExecutor(getDbContext());
        }

        return sqlReaderListener;
    }


    protected abstract SqlReader getSourceReader();

    protected String execute(SqlReader reader) throws IOException, SQLException {
        reader.setReaderListener(getReadListener());
        reader.read();
        return null;//sen
    }

    public static InputStream getInputStreamFromFile(String resourceOrFileName) {
        InputStream stream = null;
        logger.info("reading file: " + resourceOrFileName);
        if (resourceOrFileName.startsWith("res:")) {
            String resFile = resourceOrFileName.substring(4);
            stream = logger.getClass().getResourceAsStream(resFile);
        } else {
            try {
                stream = new FileInputStream(resourceOrFileName);
            } catch (FileNotFoundException e) {
                logger.error("file not found " + resourceOrFileName, e);
            }
        }
        return stream;
    }

}
