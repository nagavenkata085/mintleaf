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

import org.qamatic.mintleaf.DataImportSource;
import org.qamatic.mintleaf.MintLeafException;
import org.qamatic.mintleaf.MintLeafLogger;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by senips on 2/22/17.
 */
public class DbImportSource implements DataImportSource {

    private static final MintLeafLogger logger = MintLeafLogger.getLogger(DbImportSource.class);
    private ResultSet resultSet;

    public DbImportSource(ResultSet resultSet) {

        this.resultSet = resultSet;
    }

    @Override
    public void doImport(SourceRowListener listener) throws MintLeafException {
        final DbSourceRowWrapper dbRowWrapper = new DbSourceRowWrapper();
        int i = 0;
        try {
            while (this.resultSet.next()) {
                dbRowWrapper.setResultSet(this.resultSet);
                listener.eachRow(i++, dbRowWrapper);
            }
        } catch (SQLException e) {
            throw new MintLeafException(e);
        }
    }

    private class DbSourceRowWrapper implements ImportedSourceRow {

        private ResultSet resultSet;

        @Override
        public String get(int i) {
            try {
                if (resultSet.getObject(i) == null)
                    return "NULL";
                return resultSet.getObject(i).toString();
            } catch (SQLException e) {
                logger.error("DbSourceRowWrapper", e);
            }
            return null;
        }

        @Override
        public String get(String name) {
            try {
                if (resultSet.getObject(name) == null)
                    return "NULL";
                return resultSet.getObject(name).toString();
            } catch (SQLException e) {
                logger.error("DbSourceRowWrapper", e);
            }
            return null;
        }

        @Override
        public int size() {
            return -1;
        }


        public void setResultSet(ResultSet resultSet) {
            this.resultSet = resultSet;
        }
    }

}
