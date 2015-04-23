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

public class PLSwitchStatment extends StatementObject {
    private final CodeObjectCollection<StatementObject> mvwhenItems = new CodeObjects<StatementObject>();

    private final String mvcondition;
    private final PLCaseElse mvelse = new PLCaseElse();

    public PLSwitchStatment() {
        mvcondition = null;
    }

    public PLSwitchStatment(String condition) {
        mvcondition = condition;
    }

    public PLWhenStatement when(String condition) {
        PLWhenStatement when = new PLWhenStatement(condition);
        mvwhenItems.add(when);
        return when;
    }

    @Override
    public void addStatement(StatementObject stmt) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        PLStringBuilder sb = new PLStringBuilder();
        if (mvcondition != null) {
            sb.appendLine(String.format("case %s", mvcondition));
        } else {
            sb.appendLine("case");
        }

        for (StatementObject stmt : mvwhenItems) {
            sb.append(stmt.toString());
        }
        if (getElse().getStatements().size() != 0) {
            sb.append(getElse().toString());
        }

        sb.appendLine(0, "end case;");
        return sb.toString();
    }

    public PLCaseElse getElse() {
        return mvelse;
    }
}
