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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;

public class SqlFileReader extends BaseSqlReader {

    protected InputStream mvstream;

    public SqlFileReader(InputStream stream) {
        this.mvstream = stream;

    }

    protected boolean isDelimiterFound(String line) {
        return (getDelimiter().equals("/") && line.equals("/") || getDelimiter().equals(";") && line.endsWith(";"));
    }

    @Override
    public String read() throws IOException, SQLException {

        StringBuilder childContents = new StringBuilder();
        StringBuilder contents = new StringBuilder();
        BufferedReader input = new BufferedReader(new InputStreamReader(mvstream, "UTF-8"));
        try {
            String line = null; // not declared within while loop
            while ((line = input.readLine()) != null) {

                line = line.trim();

                if (line.startsWith("show err") || line.startsWith("--") && !line.contains("--@")) {
                    continue;
                }

                contents.append(line);
                contents.append("\n");

                if (isDelimiterFound(line)) {

                    String[] splits = line.split(getDelimiter());
                    if (splits.length >= 1) {
                        childContents.append(splits[0]);
                    }
                    String sql = childContents.toString().trim();
                    if (mvreaderListener != null && sql.length() != 0) {
                        mvreaderListener.onReadChild(new StringBuilder(sql), null);
                    }
                    childContents.setLength(0);

                } else {
                    childContents.append(line);
                    childContents.append("\n");
                }
            }
        } finally {
            input.close();
        }
        return contents.toString();
    }

}
