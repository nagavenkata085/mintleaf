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

import org.qamatic.mintleaf.interfaces.StatementObject;

public class PLDelete extends StatementObject {

    private final String mvtableName;
    private String mvtailClause;

    public PLDelete(String tableName) {
        mvtableName = tableName;
        mvtailClause = null;
    }

    public PLDelete(String tableName, String tailClause) {
        mvtableName = tableName;
        mvtailClause = tailClause;
    }

    public void setTailClause(String tailClause) {
        mvtailClause = tailClause;
    }

    @Override
    public void addStatement(StatementObject stmt) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        PLStringBuilder sb = new PLStringBuilder();

        if (mvtailClause == null) {
            sb.append(String.format("delete from %s;", mvtableName));
        } else {
            sb.append(String.format("delete from %s %s;", mvtableName, mvtailClause));
        }
        sb.appendLine();
        return sb.toString();
    }

}
