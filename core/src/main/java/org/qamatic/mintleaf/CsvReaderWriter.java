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

package org.qamatic.mintleaf;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.Reader;

/**
 * Created by senips on 2/22/17.
 * wrapping CSV reader library
 */
public class CsvReaderWriter implements DataReaderWriter {

    private static DataReaderWriter instance;


    public void toCSV() {

    }

    public void importFrom(Reader afileReader, DataReaderListener listener) throws IOException, MintLeafException {
        importFrom(afileReader, ',', listener);
    }

    public void importFrom(Reader afileReader, char demilitedBy, DataReaderListener listener) throws IOException, MintLeafException {

        final CSVParser parser = new CSVParser(afileReader, CSVFormat.EXCEL.withHeader().withIgnoreEmptyLines().withDelimiter(demilitedBy));
        final CsvRowWrapper csvRowWrapper = new CsvRowWrapper();
        int i = 0;
        for (CSVRecord record : parser) {
            csvRowWrapper.setRecord(record);
            listener.eachRow(i++, csvRowWrapper);
        }
    }

    public synchronized static DataReaderWriter getInstance() {
        if (instance == null)
            instance = new CsvReaderWriter();
        return instance;
    }

    public static void setInstance(DataReaderWriter instance) {
        CsvReaderWriter.instance = instance;
    }

    private class CsvRowWrapper implements DataRow {
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
