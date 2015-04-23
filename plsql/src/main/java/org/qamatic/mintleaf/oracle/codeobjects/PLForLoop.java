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

public class PLForLoop extends StatementObject {

    private final String mvlabel;
    private final String mvlowerBound;
    private final String mvupperBound;
    private boolean mvreverse;

    public PLForLoop(String label, String lowerBound, String upperBound) {
        mvlabel = label;
        mvlowerBound = lowerBound;
        mvupperBound = upperBound;
        mvreverse = false;
    }

    public PLForLoop(String label, String lowerBound, String upperBound, boolean reverse) {
        this(label, lowerBound, upperBound);
        mvreverse = reverse;
    }

    @Override
    public String toString() {
        PLStringBuilder sb = new PLStringBuilder();
        if (!mvreverse) {
            sb.appendLine(String.format("for %s in %s..%s", mvlabel, mvlowerBound, mvupperBound));
        } else {
            sb.appendLine(String.format("for %s in reverse %s..%s", mvlabel, mvlowerBound, mvupperBound));
        }
        sb.appendLine("loop");
        for (StatementObject stmt : getStatements()) {
            sb.append(stmt.toString());
        }

        sb.appendLine(0, "end loop;");
        return sb.toString();
    }
}
