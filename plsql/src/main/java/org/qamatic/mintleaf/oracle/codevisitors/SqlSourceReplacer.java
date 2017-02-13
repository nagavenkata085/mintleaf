/*
 * <!--
 *   ~
 *   ~ The MIT License (MIT)
 *   ~
 *   ~ Copyright (c) 2010-2017 Senthil Maruthaiappan
 *   ~
 *   ~ Permission is hereby granted, free of charge, to any person obtaining a copy
 *   ~ of this software and associated documentation files (the "Software"), to deal
 *   ~ in the Software without restriction, including without limitation the rights
 *   ~ to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   ~ copies of the Software, and to permit persons to whom the Software is
 *   ~ furnished to do so, subject to the following conditions:
 *   ~
 *   ~ The above copyright notice and this permission notice shall be included in all
 *   ~ copies or substantial portions of the Software.
 *   ~
 *   ~ THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   ~ IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   ~ FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *   ~ AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *   ~ LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   ~ OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *   ~ SOFTWARE.
 *   ~
 *   ~
 *   -->
 */

package org.qamatic.mintleaf.oracle.codevisitors;

import org.qamatic.mintleaf.oracle.SqlSourceVisitor;

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
