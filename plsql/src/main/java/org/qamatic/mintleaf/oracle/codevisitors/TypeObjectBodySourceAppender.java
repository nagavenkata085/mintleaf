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
