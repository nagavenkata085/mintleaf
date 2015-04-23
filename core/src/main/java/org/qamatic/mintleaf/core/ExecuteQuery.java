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


import org.qamatic.mintleaf.interfaces.*;

import java.io.IOException;
import java.sql.SQLException;

public class ExecuteQuery {

    private DbContext mvDbContext;

    public ExecuteQuery() {

    }

    public ExecuteQuery(DbContext context) {
        mvDbContext = context;
    }

    public static String getFileName(String sectionalFileCommaSectionalNames) {
        if ((sectionalFileCommaSectionalNames == null) || (!sectionalFileCommaSectionalNames.contains(","))) {
            return null;
        }
        String[] sectionsToLoad = sectionalFileCommaSectionalNames.split(",");
        if (sectionsToLoad.length >= 1) {
            return sectionsToLoad[0].trim();
        }
        return null;
    }

    public static String[] getSectionNames(String sectionalFileCommaSectionalNames) {
        if ((sectionalFileCommaSectionalNames == null) || (!sectionalFileCommaSectionalNames.contains(","))) {
            return null;
        }
        String[] sectionsToLoad = sectionalFileCommaSectionalNames.split(",");
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
        loadSource(mvDbContext, script, delimiter);
    }

    // for JUnit, if context is not known
    public void loadSource(DbContext dbcontext, String script, String delimiter) throws SQLException, IOException {
        SqlObject loadSql = new LoadSqlScript(dbcontext, script, delimiter);
        loadSql.create();
    }

    // for JUnit, if sections are specified as a comma delimited string
    public void loadFromSectionalFile(DbContext dbcontext, String fileName, String sectionsToLoadSeparatedByComma) throws SQLException, IOException {
        loadFromSectionalFile(dbcontext, fileName, sectionsToLoadSeparatedByComma.split(","));
    }

    // for fitnesse, if sections are specified as table argument
    public void loadFromSectionalFile(DbContext dbcontext, String fileName, String[] sectionsToLoad) throws SQLException, IOException {
        LoadSectionalSqlFile loadSql = new LoadSectionalSqlFile(dbcontext, fileName, sectionsToLoad);
        loadSql.create();
    }

    public void loadSqlSectionsFitnesse(String sectionalFileCommaSectionalNames) throws SQLException, IOException {
        new ExecuteQuery().loadFromSectionalFile(mvDbContext, getFileName(sectionalFileCommaSectionalNames), getSectionNames(sectionalFileCommaSectionalNames));
    }

    private static class LoadSqlScript extends BaseSqlObject {
        private final String mvdelimiter;
        private final String mvscripttext;

        public LoadSqlScript(DbContext context, String scripttext, String delimiter) {
            super(context);
            mvdelimiter = delimiter;
            mvscripttext = scripttext;
        }

        @Override
        protected SqlReader getCreateSourceReader() {
            return new SqlStringReader(getSource());
        }

        @Override
        public String getSource() {
            return mvscripttext;
        }

        @Override
        protected String getCreateSourceDelimiter() {
            return mvdelimiter;
        }
    }

    private class LoadSectionalSqlFile {

        private final String[] mvsectionsToLoad;
        private final String mvfileName;
        private final DbContext mvContext;

        public LoadSectionalSqlFile(DbContext context, String fileName, String[] sectionsToLoad) {

            mvfileName = fileName;
            mvContext = context;
            mvsectionsToLoad = sectionsToLoad;
        }

        public void create() throws IOException, SQLException {

            if ((mvsectionsToLoad == null) || (mvsectionsToLoad.length == 0)) {
                return;
            }
            MultiPartReader reader = new SqlMultiPartFileReader(mvfileName);
            reader.read();
            for (String sectionName : mvsectionsToLoad) {
                if (reader.getSqlParts().containsKey(sectionName)) {
                    SqlPart section = reader.getSqlPart(sectionName);
                    SqlObject loadSql = new LoadSqlScript(mvContext, section.getSqlPartSource(), section.getDelimiter());
                    loadSql.create();
                }
            }
        }

    }

}
