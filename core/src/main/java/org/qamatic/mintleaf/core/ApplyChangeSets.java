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
import org.qamatic.mintleaf.SqlScript;

import java.io.IOException;
import java.sql.SQLException;

public class ApplyChangeSets {


    public static void applySource(final DbContext dbContext, final String script, final String delimiter) throws SQLException, IOException {
        SqlScript sqlScript = new BaseSqlScript(dbContext) {
            @Override
            protected SqlReader getSourceReader() {
                SqlReader reader = new SqlStringReader(script);
                reader.setDelimiter(delimiter);
                return reader;
            }
        };
        sqlScript.apply();
    }


    public static void apply(DbContext dbcontext, String fileName, String changeSetsToLoadSeparatedByComma) throws SQLException, IOException {
        apply(dbcontext, fileName, changeSetsToLoadSeparatedByComma.split(","));
    }


    public static void apply(DbContext dbcontext, String fileName, String[] changeSetsToLoad) throws SQLException, IOException {
        SqlChangeSets changeSets = new SqlChangeSets(dbcontext, new SqlChangeSetFileReader(fileName), changeSetsToLoad);
        changeSets.apply();
    }


}
