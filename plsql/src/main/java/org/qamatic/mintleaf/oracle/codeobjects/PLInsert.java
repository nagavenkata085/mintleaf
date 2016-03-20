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

package org.qamatic.mintleaf.oracle.codeobjects;

import org.qamatic.mintleaf.oracle.CodeObjectCollection;
import org.qamatic.mintleaf.oracle.CodeObjects;
import org.qamatic.mintleaf.oracle.StatementObject;

public class PLInsert extends StatementObject {

    private final String mvtableName;

    private final CodeObjectCollection<PLIdentifierValue> mvcolumns = new CodeObjects<PLIdentifierValue>();
    private final CodeObjectCollection<PLIdentifierValue> mvvalues = new CodeObjects<PLIdentifierValue>();

    public PLInsert(String tableName) {
        mvtableName = tableName;
    }

    private static PLStringBuilder getFormattedValue(CodeObjectCollection<PLIdentifierValue> list) {
        PLStringBuilder listSb = new PLStringBuilder();

        for (StatementObject stmt : list) {
            if (listSb.getStringBuilder().length() != 0) {
                listSb.append(", ");
            }
            listSb.append(stmt.toString());
        }
        return listSb;
    }

    public PLIdentifierValue addColumn(String identifier) {
        PLIdentifierValue idfier = new PLIdentifierValue(identifier);
        idfier.setLineFeed(false);
        mvcolumns.add(idfier);
        return idfier;
    }

    public PLIdentifierValue addColumnValue(String value) {
        PLIdentifierValue idfier = new PLIdentifierValue(value);
        idfier.setLineFeed(false);
        mvvalues.add(idfier);
        return idfier;
    }

    @Override
    public void addStatement(StatementObject stmt) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        PLStringBuilder sb = new PLStringBuilder();
        if (mvcolumns.size() != 0) {
            sb.append(String
                    .format("insert into %s (%s) values (%s);", mvtableName, getFormattedValue(mvcolumns).toString(), getFormattedValue(mvvalues).toString()));
        } else {
            sb.append(String.format("insert into %s values (%s);", mvtableName, getFormattedValue(mvvalues).toString()));
        }
        sb.appendLine();
        return sb.toString();
    }

}
