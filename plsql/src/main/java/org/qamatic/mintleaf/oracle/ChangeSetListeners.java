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

package org.qamatic.mintleaf.oracle;

import org.qamatic.mintleaf.core.SqlChangeSetFileReader;
import org.qamatic.mintleaf.DbContext;
import org.qamatic.mintleaf.SqlReaderListener;

import java.io.IOException;
import java.sql.SQLException;

public class ChangeSetListeners {

    public static SqlReaderListener getPLPackageSectionalListner(DbContext context, String sectionalSqlFile, String[] replaceItems) {
        VisitorCommandExecutor listener = new PackageVisitorCommandExecutor(context);
        return getListner(context, listener, sectionalSqlFile, replaceItems);
    }

    public static SqlReaderListener getPLTypeObjectSectionalListner(DbContext context, String sectionalSqlFile, String[] replaceItems) {
        VisitorCommandExecutor listener = new TypeObjectVisitorCommandExecutor(context);
        return getListner(context, listener, sectionalSqlFile, replaceItems);
    }

    public static SqlReaderListener getListner(DbContext context, VisitorCommandExecutor listener, String sectionalSqlFile, String[] replaceItems) {

        SqlChangeSetFileReader sectionalReader = new SqlChangeSetFileReader(sectionalSqlFile);

        try {
            sectionalReader.read();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String intfSectionName = "package";
        String bodySectionName = "packagebody";
        if (listener instanceof TypeObjectVisitorCommandExecutor) {
            intfSectionName = "type";
            bodySectionName = "typebody";
        }
        if (sectionalReader.getChangeSets().containsKey(intfSectionName)) {
            listener.setInterfaceSource(sectionalReader.getChangeSet(intfSectionName).getChangeSetSource());
        }

        if (sectionalReader.getChangeSets().containsKey(bodySectionName)) {
            listener.setBodySource(sectionalReader.getChangeSet(bodySectionName).getChangeSetSource());
        }

        if (replaceItems != null) {
            for (String sectionalName : replaceItems) {
                if (sectionalReader.getChangeSets().containsKey(sectionalName)) {
                    listener.getTemplateValues().put(sectionalName, sectionalReader.getChangeSet(sectionalName).getChangeSetSource());
                }
            }
        }
        return listener;
    }

}
