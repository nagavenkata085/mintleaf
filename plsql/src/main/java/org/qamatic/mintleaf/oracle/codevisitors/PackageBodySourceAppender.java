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

import org.qamatic.mintleaf.core.SqlCodeExecutor;
import org.qamatic.mintleaf.interfaces.SqlSourceVisitor;

public class PackageBodySourceAppender implements SqlSourceVisitor {

    private final String mvinsertAtTag;

    public PackageBodySourceAppender() {
        mvinsertAtTag = null;
    }

    public PackageBodySourceAppender(String insertAtTag) {
        mvinsertAtTag = insertAtTag;
    }

    @Override
    public void visit(StringBuilder sql, Object actionContent) {

        String str = sql.toString().toLowerCase();// any better way other than
        int lastIdx = 0; // this?

        if (!str.contains(" body ")) {
            return;
        }

        if (mvinsertAtTag != null) {
            lastIdx = str.indexOf(mvinsertAtTag);

            if (lastIdx != -1) {
                SqlCodeExecutor.replaceStr(sql, mvinsertAtTag, actionContent.toString());
            }

        } else {

            // ugly thing here , go for insert at Tag approach

            if (str.contains("\nbegin--initialization")) {
                lastIdx = str.lastIndexOf("\nbegin--initialization");
            } else {
                // at this moment it should be a new line
                lastIdx = str.lastIndexOf("\nend");
            }

            if (lastIdx != -1) {

                sql.insert(lastIdx, "\n" + actionContent.toString());
            }
        }

    }

}
