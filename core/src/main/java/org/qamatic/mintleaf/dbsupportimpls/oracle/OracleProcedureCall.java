package org.qamatic.mintleaf.dbsupportimpls.oracle;

import org.qamatic.mintleaf.core.ProcedureCall;
import org.qamatic.mintleaf.interfaces.SqlArgument;
import org.qamatic.mintleaf.interfaces.SqlArgumentCollection;
import org.qamatic.mintleaf.interfaces.SqlStoredProcedure;

/**
 * Created by senips on 6/18/16.
 */
public class OracleProcedureCall extends ProcedureCall {
    public OracleProcedureCall(SqlStoredProcedure procedure) {
        super(procedure);
    }


    public static String getMethodCall(SqlStoredProcedure procedure) {
        String callString;
        SqlArgumentCollection parameters = procedure.getDeclaredArguments();
        int parameterCount = 0;
        if (procedure.isFunction()) {
            callString = " := " + procedure.getSql() + "(";
            callString = parameters.size() == 0 ? "?" + callString : parameters.get(0).getCustomArg().getUnsupportedVariable() + callString;
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
                    callString += parameter.getCustomArg().getUnsupportedVariable();
                }
                parameterCount++;
            }
        }
        callString += ");";
        return callString;
    }

    @Override
    public StringBuilder getSQL() {
        if (mvProcedure.isSqlReadyForUse()) {
            appendLine("declare\nbegin\n" + mvProcedure.getSql() + "\nend;");
        } else {

            appendLine("declare");

            addCode(mvProcedure.getDeclaredArguments().getVariableDeclaration());
            addCode(mvProcedure.getDeclaredArguments().getTypeConversionCode());

            appendLine("begin");
            addCode(mvProcedure.getDeclaredArguments().getCodeBeforeCall());
            appendLine(getMethodCall(mvProcedure));
            addCode(mvProcedure.getDeclaredArguments().getCodeAfterCall());
            appendLine("end;");
        }
        return super.getSQL();
    }
}
