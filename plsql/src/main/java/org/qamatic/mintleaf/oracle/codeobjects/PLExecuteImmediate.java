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

public class PLExecuteImmediate extends StatementObject {

    private final String mvsql;
    private final String mvtailClause;

    public PLExecuteImmediate(String sql, String tailClause) {
        mvsql = sql;
        mvtailClause = tailClause;
    }

    @Override
    public void addStatement(StatementObject stmt) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        PLStringBuilder sb = new PLStringBuilder();
        if ((mvtailClause == null) || (mvtailClause.length() == 0)) {
            sb.appendLine(String.format("execute immediate %s;", mvsql));
        } else {
            sb.appendLine(String.format("execute immediate %s %s;", mvsql, mvtailClause));
        }

        return sb.toString();
    }
}
