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

package org.qamatic.mintleaf.oracle.codevisitors;

import org.qamatic.mintleaf.interfaces.SqlSourceVisitor;

public class TypeObjectBodySourceAppender implements SqlSourceVisitor {

    @Override
    public void visit(StringBuilder sql, Object actionContent) {

        String str = sql.toString().toLowerCase();// any better way other than
        int lastIdx = 0;                                          // this?

        if (!str.contains(" body ")) {
            return;
        }
        if (str.contains("\nbegin--initialization")) {

            lastIdx = str.lastIndexOf("\nbegin--initialization");   // at this moment it should be a new line

        } else {

            lastIdx = str.lastIndexOf("\nend");                        // at this moment it should be a new line

        }

        if (lastIdx != -1) {

            sql.insert(lastIdx, "\n" + actionContent.toString());
        }
    }

}
