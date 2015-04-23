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

public class PLException extends StatementObject {

    private final CodeObjectCollection<StatementObject> mvwhenItems = new CodeObjects<StatementObject>();

    public PLWhenStatement addExceptionCondition(String condition) {
        PLWhenStatement when = new PLWhenStatement(condition);
        mvwhenItems.add(when);
        return when;
    }

    @Override
    public String toString() {
        PLStringBuilder sb = new PLStringBuilder();
        sb.appendLine("begin");
        for (StatementObject stmt : getStatements()) {
            sb.append(stmt.toString());
        }
        sb.appendLine("exception");
        for (StatementObject stmt : mvwhenItems) {
            sb.append(stmt.toString());
        }
        sb.appendLine(0, "end;");
        return sb.toString();
    }

}
