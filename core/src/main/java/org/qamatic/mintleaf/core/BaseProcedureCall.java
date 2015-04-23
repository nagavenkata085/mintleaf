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

package org.qamatic.mintleaf.core;

import org.qamatic.mintleaf.interfaces.ProcedureCall;
import org.qamatic.mintleaf.interfaces.SqlArgument;
import org.qamatic.mintleaf.interfaces.SqlArgumentCollection;
import org.qamatic.mintleaf.interfaces.SqlProcedure;

public class BaseProcedureCall implements ProcedureCall {

    protected final StringBuilder mvProcCode = new StringBuilder();
    private final SqlProcedure mvProcedure;

    public BaseProcedureCall(SqlProcedure procedure) {
        mvProcedure = procedure;
    }

    public static String getMethodCall(SqlProcedure procedure) {
        String callString;
        SqlArgumentCollection parameters = procedure.getDeclaredArguments();
        int parameterCount = 0;
        if (procedure.isFunction()) {
            callString = " := " + procedure.getSql() + "(";
            callString = parameters.size() == 0 ? "?" + callString : parameters.get(0).getTypeExtension().getUnsupportedVariable() + callString;
            parameterCount = -1;
        } else {
            callString = procedure.getSql() + "(";
        }
        for (SqlArgument parameter : parameters) {
            if (!(parameter.isResultsParameter())) {
                if (parameterCount > 0) {
                    callString += ", ";
                }
                if (parameterCount >= 0) {
                    callString += parameter.getTypeExtension().getUnsupportedVariable();
                }
                parameterCount++;
            }
        }
        callString += ");";
        return callString;
    }

    protected void appendLine(String txt) {
        mvProcCode.append(txt).append("\n");
    }

    private void addCode(String code) {
        if (code.length() != 0) {
            appendLine(code);
        }
    }

    @Override
    public StringBuilder getCallString() {

        if (mvProcedure.isSqlReadyForUse()) {
            appendLine("declare\nbegin\n" + mvProcedure.getSql() + "\nend;");
        } else {

            appendLine("declare");

            addCode(mvProcedure.getDeclaredArguments().getIdentifierDeclaration());
            addCode(mvProcedure.getDeclaredArguments().getTypeConversionCode());

            appendLine("begin");
            addCode(mvProcedure.getDeclaredArguments().getAssignmentCodeBeforeCall());
            appendLine(getMethodCall(mvProcedure));
            addCode(mvProcedure.getDeclaredArguments().getAssignmentCodeAfterCall());
            appendLine("end;");
        }

        return mvProcCode;
    }
}
