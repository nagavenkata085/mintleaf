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

package org.qamatic.mintleaf.oracle;


import org.qamatic.mintleaf.core.SqlCodeExecutor;
import org.qamatic.mintleaf.interfaces.DbContext;
import org.qamatic.mintleaf.oracle.codevisitors.SqlSourceReplacer;

public abstract class VisitorSqlCodeExecutor extends SqlCodeExecutor {

    private String mvinterfaceSource;
    private String mvbodySource;

    public VisitorSqlCodeExecutor(DbContext context) {
        super(context);
    }

    public String getInterfaceSource() {
        return mvinterfaceSource;
    }

    public void setInterfaceSource(String src) {
        mvinterfaceSource = src;
    }

    public String getBodySource() {
        return mvbodySource;
    }

    public void setBodySource(String src) {
        mvbodySource = src;
    }

    protected abstract SqlSourceVisitor[] getInterfaceVisitors();

    protected abstract SqlSourceVisitor[] getBodyVisitors();

    @Override
    protected void onReplaceTemplateValue(StringBuilder sqlBuilder, String findStr, String replaceStr) {
        new SqlSourceReplacer().visit(sqlBuilder, new Object[]{findStr, replaceStr});
        super.onReplaceTemplateValue(sqlBuilder, findStr, replaceStr);
    }

    @Override
    protected void preProcess(StringBuilder sqlText) {
        if (mvinterfaceSource != null) {
            for (SqlSourceVisitor visitorItem : getInterfaceVisitors()) {
                visitorItem.visit(sqlText, mvinterfaceSource);
            }
        }

        if (mvbodySource != null) {
            for (SqlSourceVisitor visitorItem : getBodyVisitors()) {
                visitorItem.visit(sqlText, mvbodySource);
            }
        }

        super.preProcess(sqlText);
    }

}
