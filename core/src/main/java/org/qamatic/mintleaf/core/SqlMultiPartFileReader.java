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