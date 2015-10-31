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
