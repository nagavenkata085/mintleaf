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

package org.qamatic.mintleaf.tools;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.qamatic.mintleaf.DataImportSource;
import org.qamatic.mintleaf.MintLeafException;

import java.io.IOException;
import java.io.Reader;

/**
 * Created by senips on 2/18/6/16.
 */
public class CsvImportSource implements DataImportSource {

    private Reader afileReader;

    public CsvImportSource(Reader afileReader) {
        this.afileReader = afileReader;
    }

    protected CSVParser getCSVParser() throws IOException {
        return new CSVParser(afileReader, CSVFormat.EXCEL.withHeader().withIgnoreEmptyLines());
    }

    public void doImport(SourceRowListener listener) throws MintLeafException {

        final CSVParser parser;
        try {
            parser = getCSVParser();

            final CsvSourceRowWrapper csvRowWrapper = new CsvSourceRowWrapper();
            int i = 0;
            for (CSVRecord record : parser) {
                csvRowWrapper.setRecord(record);
                listener.eachRow(i++, csvRowWrapper);
            }

        } catch (IOException e) {
            throw new MintLeafException(e);
        }
    }


    private class CsvSourceRowWrapper implements ImportedSourceRow {
        private CSVRecord record;


        public CSVRecord getRecord() {
            return record;
        }


        public void setRecord(CSVRecord record) {
            this.record = record;
        }

        @Override
        public String get(int i) {
            return record.get(i);
        }

        @Override
        public String get(String name) {
            return record.get(name);
        }

        @Override
        public int size() {
            return record.size();
        }
    }

}
