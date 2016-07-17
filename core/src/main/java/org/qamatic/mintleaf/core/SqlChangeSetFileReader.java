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

import org.qamatic.mintleaf.ChangeSetReader;
import org.qamatic.mintleaf.ChangeSet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.HashMap;

public class SqlChangeSetFileReader extends BaseSqlReader implements ChangeSetReader {

    private final HashMap<String, ChangeSet> mvSections = new HashMap<String, ChangeSet>();
    protected InputStream mvStream;

    public SqlChangeSetFileReader(InputStream stream) {
        this.mvStream = stream;

    }

    public SqlChangeSetFileReader(String resource) {
        this.mvStream = this.getClass().getResourceAsStream(resource);
    }

    @Override
    public ChangeSet getChangeSet(String sectionName) {
        return mvSections.get(sectionName);
    }

    @Override
    public HashMap<String, ChangeSet> getChangeSets() {
        return mvSections;
    }

    @Override
    public String read() throws IOException, SQLException {

        StringBuilder childContents = new StringBuilder();
        StringBuilder contents = new StringBuilder();
        BufferedReader input = new BufferedReader(new InputStreamReader(mvStream, "UTF-8"));
        ChangeSet currentSection = null;
        try {
            String line = null; // not declared within while loop

            while ((line = input.readLine()) != null) {
                line = line.trim();
                if (line.length() == 0) {
                    continue;
                }

                contents.append(line);
                contents.append("\n");

                if ((line.trim().contains("<ChangeSet")) && ChangeSet.xmlToChangeSet(line) != null) {
                    if (currentSection == null) {
                        currentSection = ChangeSet.xmlToChangeSet(line);
                    }
                    String sql = childContents.toString().trim();
                    if (sql.length() != 0) {
                        if (mvreaderListener != null) {
                            mvreaderListener.onReadChild(new StringBuilder(sql), currentSection);
                        }
                        currentSection.setChangeSetSource(sql);
                        mvSections.put(currentSection.getId(), currentSection);
                        currentSection = ChangeSet.xmlToChangeSet(line);
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
        if ((currentSection != null) && (currentSection.getId() != null) && (currentSection.getId().length() != 0) && (sql.length() != 0)) {
            if (mvreaderListener != null) {
                mvreaderListener.onReadChild(new StringBuilder(sql), null);
            }
            currentSection.setChangeSetSource(sql);
            mvSections.put(currentSection.getId(), currentSection);
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
