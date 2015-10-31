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

import org.qamatic.mintleaf.interfaces.CodeObjectCollection;
import org.qamatic.mintleaf.interfaces.CodeObjects;
import org.qamatic.mintleaf.interfaces.StatementObject;

public class PLUpdate extends StatementObject {

    private final String mvtableName;
    private final CodeObjectCollection<PLAssignmentStatement> mvsetValues = new CodeObjects<PLAssignmentStatement>();
    private String mvtailClause;

    public PLUpdate(String tableName) {
        mvtableName = tableName;
        mvtailClause = null;
    }

    public PLUpdate(String tableName, String tailClause) {
        mvtableName = tableName;
        mvtailClause = tailClause;
    }

    private static PLStringBuilder getFormattedValue(CodeObjectCollection<PLAssignmentStatement> list) {
        PLStringBuilder listSb = new PLStringBuilder();

        for (StatementObject stmt : list) {
            if (listSb.getStringBuilder().length() != 0) {
                listSb.append(", ");
            }
            listSb.append(stmt.toString());
        }
        return listSb;
    }

    public PLAssignmentStatement setValue(String identifier, String value) {
        PLAssignmentStatement assignMent = new PLAssignmentStatement(identifier, value, true);
        mvsetValues.add(assignMent);
        return assignMent;
    }

    public void setTailClause(String tailClause) {
        mvtailClause = tailClause;
    }

    @Override
    public void addStatement(StatementObject stmt) {
        throw new UnsupportedOperationException();
    }

    public CodeObjectCollection<PLAssignmentStatement> getSetValues() {
        return mvsetValues;
    }

    @Override
    public String toString() {
        PLStringBuilder sb = new PLStringBuilder();

        if (mvtailClause == null) {
            sb.append(String.format("update %s set %s;", mvtableName, getFormattedValue(getSetValues()).toString()));
        } else {
            sb.append(String.format("update %s set %s %s;", mvtableName, getFormattedValue(getSetValues()).toString(), mvtailClause));
        }
        sb.appendLine();
        return sb.toString();
    }

}
