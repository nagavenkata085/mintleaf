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

public class PLMethodInvoke extends StatementObject {

    public String mvmethodName;
    public String[] mvargs;

    public PLMethodInvoke(String methodName, String... args) {
        mvmethodName = methodName;
        mvargs = args;
    }

    @Override
    public void addStatement(StatementObject stmt) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        PLStringBuilder sb = new PLStringBuilder();
        if ((mvargs == null) || (mvargs.length == 0)) {
            sb.append(String.format("%s", mvmethodName));
            sb.appendLine(";");
            return sb.toString();
        }
        sb.append(String.format("%s(", mvmethodName));
        for (int i = 0; i < mvargs.length; i++) {
            if (i >= 1) {
                sb.append(", ");
            }
            sb.append(mvargs[i]);
        }
        sb.appendLine(");");
        return sb.toString();

    }
}
