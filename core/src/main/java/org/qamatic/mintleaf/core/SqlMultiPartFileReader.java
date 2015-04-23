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

import org.qamatic.mintleaf.interfaces.MultiPartReader;
import org.qamatic.mintleaf.interfaces.SqlPart;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.HashMap;

public class SqlMultiPartFileReader extends BaseSqlReader implements MultiPartReader {

    private final HashMap<String, SqlPart> mvSections = new HashMap<String, SqlPart>();
    protected InputStream mvStream;

    public SqlMultiPartFileReader(InputStream stream) {
        this.mvStream = stream;

    }

    public SqlMultiPartFileReader(String resource) {
        this.mvStream = this.getClass().getResourceAsStream(resource);
    }

    @Override
    public SqlPart getSqlPart(String sectionName) {
        return mvSections.get(sectionName);
    }

    @Override
    public HashMap<String, SqlPart> getSqlParts() {
        return mvSections;
    }

    @Override
    public String read() throws IOException, SQLException {

        StringBuilder childContents = new StringBuilder();
        StringBuilder contents = new StringBuilder();
        BufferedReader input = new BufferedReader(new InputStreamReader(mvStream, "UTF-8"));
        SqlPart currentSection = null;
        try {
            String line = null; // not declared within while loop

            while ((line = input.readLine()) != null) {
                line = line.trim();
                if (line.length() == 0) {
                    continue;
                }

                contents.append(line);
                contents.append("\n");

                if ((line.trim().contains("<sqlpart")) && SqlPart.xmlToSqlPart(line) != null) {
                    if (currentSection == null) {
                        currentSection = SqlPart.xmlToSqlPart(line);
                    }
                    String sql = childContents.toString().trim();
                    if (sql.length() != 0) {
                        if (mvreaderListener != null) {
                            mvreaderListener.onReadChild(new StringBuilder(sql), currentSection);
                        }
                        currentSection.setSqlPartSource(sql);
                        mvSections.put(currentSection.getName(), currentSection);
                        currentSection = SqlPart.xmlToSqlPart(line);
                    }
                    childContents.setLength(0);
                    continue;
                }
                childContents.append(line);
                childContents.append("\n");
            }
        } finally {
            input.close();
        }

        String sql = childContents.toString().trim();
        if ((currentSection != null) && (currentSection.getName() != null) && (currentSection.getName().length() != 0) && (sql.length() != 0)) {
            if (mvreaderListener != null) {
                mvreaderListener.onReadChild(new StringBuilder(sql), null);
            }
            currentSection.setSqlPartSource(sql);
            mvSections.put(currentSection.getName(), currentSection);
        }
        return contents.toString();
    }

    @Override
    public String getDelimiter() {
        throw new UnsupportedOperationException("sectional file reader does not support delimiters");
    }

    @Override
    public void setDelimiter(String delimStr) {
        throw new UnsupportedOperationException("sectional file reader does not support delimiters");
    }

}
