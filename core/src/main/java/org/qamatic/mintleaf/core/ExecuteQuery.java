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


import org.qamatic.mintleaf.*;

import java.io.IOException;
import java.sql.SQLException;

public class ExecuteQuery {

    private DbContext dbContext;

    public ExecuteQuery() {

    }

    public ExecuteQuery(DbContext context) {
        dbContext = context;
    }

    public static String getFileName(String commaSepartedChangeSetIds) {
        if ((commaSepartedChangeSetIds == null) || (!commaSepartedChangeSetIds.contains(","))) {
            return null;
        }
        String[] sectionsToLoad = commaSepartedChangeSetIds.split(",");
        if (sectionsToLoad.length >= 1) {
            return sectionsToLoad[0].trim();
        }
        return null;
    }

    public static String[] getChangeSetIds(String commaSepartedChangeSetIds) {
        if ((commaSepartedChangeSetIds == null) || (!commaSepartedChangeSetIds.contains(","))) {
            return null;
        }
        String[] sectionsToLoad = commaSepartedChangeSetIds.split(",");
        if (sectionsToLoad.length <= 1) {
            return null;
        }
        String[] result = new String[sectionsToLoad.length - 1];
        for (int i = 1; i < sectionsToLoad.length; i++) {
            result[i - 1] = sectionsToLoad[i].trim();
        }
        return result;
    }

    // for fitnesse, if context is already available
    public void loadSource(String script, String delimiter) throws SQLException, IOException {
        loadSource(dbContext, script, delimiter);
    }

    // for JUnit, if context is not known
    public void loadSource(DbContext dbcontext, String script, String delimiter) throws SQLException, IOException {
        SqlScript loadSql = new LoadSqlScriptScript(dbcontext, script, delimiter);
        loadSql.create();
    }

    // for JUnit, if sections are specified as a comma delimited string
    public void loadFromSectionalFile(DbContext dbcontext, String fileName, String sectionsToLoadSeparatedByComma) throws SQLException, IOException {
        loadFromSectionalFile(dbcontext, fileName, sectionsToLoadSeparatedByComma.split(","));
    }

    // for fitnesse, if sections are specified as table argument
    public void loadFromSectionalFile(DbContext dbcontext, String fileName, String[] sectionsToLoad) throws SQLException, IOException {
        LoadChangeSetSqlFile loadSql = new LoadChangeSetSqlFile(dbcontext, fileName, sectionsToLoad);
        loadSql.create();
    }



    private static class LoadSqlScriptScript extends SqlScriptObject {
        private final String delimiter;
        private final String scriptText;

        public LoadSqlScriptScript(DbContext context, String scripttext, String delimiter) {
            super(context);
            this.delimiter = delimiter;
            this.scriptText = scripttext;
        }

        @Override
        protected SqlReader getCreateSourceReader() {
            SqlReader reader =new SqlStringReader(scriptText);
            reader.setDelimiter(delimiter);
            return reader;
        }

    }

    private class LoadChangeSetSqlFile {

        private final String[] changeSets;
        private final String fileName;
        private final DbContext dbContext;

        public LoadChangeSetSqlFile(DbContext context, String fileName, String[] sectionsToLoad) {

            this.fileName = fileName;
            dbContext = context;
            changeSets = sectionsToLoad;
        }

        public void create() throws IOException, SQLException {

            if ((changeSets == null) || (changeSets.length == 0)) {
                return;
            }
            ChangeSetReader reader = new SqlChangeSetFileReader(fileName);
            reader.read();
            for (String sectionName : changeSets) {
                if (reader.getChangeSets().containsKey(sectionName)) {
                    ChangeSet section = reader.getChangeSet(sectionName);
                    SqlScript loadSql = new LoadSqlScriptScript(dbContext, section.getChangeSetSource(), section.getDelimiter());
                    loadSql.create();
                }
            }
        }

    }

}
