package org.qamatic.mintleaf.oracle.spring;

import org.qamatic.mintleaf.core.BaseProcedureCall;
import org.qamatic.mintleaf.interfaces.SqlStoredProcedure;

/**
 * Created by senips on 6/18/16.
 */
public class OracleProcedureCall extends BaseProcedureCall {
    public OracleProcedureCall(SqlStoredProcedure procedure) {
        super(procedure);
    }

    @Override
    public StringBuilder getCallString() {
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
        return super.getCallString();
    }
}
