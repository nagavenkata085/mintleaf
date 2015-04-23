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

import java.io.IOException;
import java.sql.SQLException;

public class SqlStringReader extends BaseSqlReader {

    private final String mvSqlSource;

    public SqlStringReader(String sqlSource) {
        mvSqlSource = sqlSource;
    }

    @Override
    public String read() throws IOException, SQLException {

        StringBuilder childContents = new StringBuilder();

        String[] sqlLines = mvSqlSource.split("\n");
        try {

            for (String line : sqlLines) {

                line = line.trim();

                if (line.startsWith("show err") || line.startsWith("--")) {
                    continue;
                }

                if ((getDelimiter().equals("/") && line.equals("/")) || (getDelimiter().equals(";") && line.endsWith(";"))) {

                    String[] splits = line.split(getDelimiter());
                    if (splits.length >= 1) {
                        childContents.append(splits[0]);
                    }
                    String sql = childContents.toString().trim();
                    if ((mvreaderListener != null) && (sql.length() != 0)) {
                        mvreaderListener.onReadChild(new StringBuilder(sql), null);
                    }
                    childContents.setLength(0);

                } else {
                    childContents.append(line);
                    childContents.append("\n");
                }

            }
        } finally {

        }
        return mvSqlSource;
    }

}
