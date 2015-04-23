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
