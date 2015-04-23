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

public class SqlSourceReplacer implements SqlSourceVisitor {

    @Override
    public void visit(StringBuilder sql, Object actionContent) {

        Object[] param = (Object[]) actionContent;
        String textToFind = "--@testoverride " + param[0].toString();
        String replaceWith = param[1].toString();
        String str = sql.toString().toLowerCase();// any better way other than
        // this?

        if (!str.contains(textToFind)) {
            return;
        }
        int startIdx = getStartIdx(str, textToFind);
        int endIdx = getEndIdx(str, startIdx);
        if (startIdx == -1 || endIdx == -1) {
            return;
        } else {
            sql.replace(startIdx, endIdx, replaceWith);
        }

    }

    public int getStartIdx(String text, String texttofind) {
        return text.indexOf(texttofind);

    }

    public int getEndIdx(String text, int startIdx) {
        return text.indexOf("--@end", startIdx) + 6;

    }
}
