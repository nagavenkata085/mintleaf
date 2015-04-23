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

package org.qamatic.mintleaf.oracle;


import org.qamatic.mintleaf.core.SqlCodeExecutor;
import org.qamatic.mintleaf.interfaces.DbContext;
import org.qamatic.mintleaf.interfaces.SqlSourceVisitor;
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
