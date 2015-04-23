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

import org.qamatic.mintleaf.interfaces.SqlReader;
import org.qamatic.mintleaf.interfaces.SqlReaderListener;

import java.io.IOException;
import java.sql.SQLException;

public abstract class BaseSqlReader implements SqlReader {

    protected SqlReaderListener mvreaderListener;
    private String mvdelimiter = "/";

    @Override
    public String getDelimiter() {
        return mvdelimiter;
    }

    @Override
    public void setDelimiter(String delimStr) {
        this.mvdelimiter = delimStr;

    }

    @Override
    public abstract String read() throws IOException, SQLException;

    @Override
    public SqlReaderListener getReaderListener() {
        return mvreaderListener;
    }

    @Override
    public void setReaderListener(SqlReaderListener readerListener) {
        this.mvreaderListener = readerListener;
    }
}
