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

public class PLIf extends StatementObject {

    private final String mvcondition;

    private final PLElse mvelse = new PLElse();

    private final CodeObjectCollection<StatementObject> mvelseIfs = new CodeObjects<StatementObject>();

    public PLIf(String condition) {
        mvcondition = condition;
    }

    public PLElse addElseIf(String condition) {
        PLElse elseItem = new PLElse(condition);
        mvelseIfs.add(elseItem);
        return elseItem;
    }

    @Override
    public String toString() {
        PLStringBuilder sb = new PLStringBuilder();
        sb.appendLine(String.format("if %s then", mvcondition));
        for (StatementObject stmt : getStatements()) {
            sb.append(stmt.toString());
        }

        for (StatementObject stmt : mvelseIfs) {
            sb.append(stmt.toString());
        }
        if (getElse().getStatements().size() != 0) {
            sb.append(getElse().toString());
        }
        sb.appendLine(0, "end if;");
        return sb.toString();
    }

    public PLElse getElse() {
        return mvelse;
    }

}
