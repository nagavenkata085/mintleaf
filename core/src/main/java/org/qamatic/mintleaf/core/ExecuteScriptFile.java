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

import org.qamatic.mintleaf.DbContext;
import org.qamatic.mintleaf.SqlReader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by senips on 7/18/16.
 */
public class ExecuteScriptFile extends BaseSqlScript {

    private final DbContext context;
    private final String filename;
    private final String delimiter;

    public ExecuteScriptFile(DbContext context, String filename, String delimiter) {
        super(context);
        this.context = context;
        this.filename = filename;
        this.delimiter = delimiter;
    }

    @Override
    protected SqlReader getSourceReader() {
        InputStream stream = null;
        logger.info("reading file: " + this.filename);
        if (this.filename.startsWith("res:")) {
            String resFile = this.filename.substring(4);
            stream = this.getClass().getResourceAsStream(resFile);
        } else {
            try {
                stream = new FileInputStream(this.filename);
            } catch (FileNotFoundException e) {
                logger.error("file not found " + this.filename, e);
            }
        }

        SqlReader reader = new SqlStreamReader(stream);
        reader.setDelimiter(this.delimiter);
        return reader;
    }
}
