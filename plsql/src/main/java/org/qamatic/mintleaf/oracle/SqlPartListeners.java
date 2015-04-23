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

package org.qamatic.mintleaf.oracle;

import org.qamatic.mintleaf.core.SqlMultiPartFileReader;
import org.qamatic.mintleaf.interfaces.DbContext;
import org.qamatic.mintleaf.interfaces.SqlReaderListener;

import java.io.IOException;
import java.sql.SQLException;

public class SqlPartListeners {

    public static SqlReaderListener getPLPackageSectionalListner(DbContext context, String sectionalSqlFile, String[] replaceItems) {
        VisitorSqlCodeExecutor listener = new PackageVisitorSqlCodeExecutor(context);
        return getListner(context, listener, sectionalSqlFile, replaceItems);
    }

    public static SqlReaderListener getPLTypeObjectSectionalListner(DbContext context, String sectionalSqlFile, String[] replaceItems) {
        VisitorSqlCodeExecutor listener = new TypeObjectVisitorSqlCodeExecutor(context);
        return getListner(context, listener, sectionalSqlFile, replaceItems);
    }

    public static SqlReaderListener getListner(DbContext context, VisitorSqlCodeExecutor listener, String sectionalSqlFile, String[] replaceItems) {

        SqlMultiPartFileReader sectionalReader = new SqlMultiPartFileReader(sectionalSqlFile);

        try {
            sectionalReader.read();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String intfSectionName = "package";
        String bodySectionName = "packagebody";
        if (listener instanceof TypeObjectVisitorSqlCodeExecutor) {
            intfSectionName = "type";
            bodySectionName = "typebody";
        }
        if (sectionalReader.getSqlParts().containsKey(intfSectionName)) {
            listener.setInterfaceSource(sectionalReader.getSqlPart(intfSectionName).getSqlPartSource());
        }

        if (sectionalReader.getSqlParts().containsKey(bodySectionName)) {
            listener.setBodySource(sectionalReader.getSqlPart(bodySectionName).getSqlPartSource());
        }

        if (replaceItems != null) {
            for (String sectionalName : replaceItems) {
                if (sectionalReader.getSqlParts().containsKey(sectionalName)) {
                    listener.getTemplateValues().put(sectionalName, sectionalReader.getSqlPart(sectionalName).getSqlPartSource());
                }
            }
        }
        return listener;
    }

}
