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

package org.qamatic.mintleaf.core;

import org.qamatic.mintleaf.interfaces.ProcedureCall;
import org.qamatic.mintleaf.interfaces.SqlArgument;
import org.qamatic.mintleaf.interfaces.SqlArgumentCollection;
import org.qamatic.mintleaf.interfaces.SqlStoredProcedure;

public class BaseProcedureCall implements ProcedureCall {

    protected final StringBuilder mvProcCode = new StringBuilder();
    private final SqlStoredProcedure mvProcedure;

    public BaseProcedureCall(SqlStoredProcedure procedure) {
        mvProcedure = procedure;
    }

    public static String getMethodCall(SqlStoredProcedure procedure) {
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
