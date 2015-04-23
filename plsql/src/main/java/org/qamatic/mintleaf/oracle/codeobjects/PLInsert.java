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

package org.qamatic.mintleaf.oracle.codeobjects;

import org.qamatic.mintleaf.interfaces.CodeObjectCollection;
import org.qamatic.mintleaf.interfaces.CodeObjects;
import org.qamatic.mintleaf.interfaces.StatementObject;

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
