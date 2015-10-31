/*
 *
 * The MIT License (MIT)
 *
 * Copyright (c) 2010-2015 Senthil Maruthaiappan
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 *
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
