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

package org.qamatic.mintleaf.dbsupportimpls.oracle;

import org.qamatic.mintleaf.core.ProcedureCall;
import org.qamatic.mintleaf.interfaces.*;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class OracleProcedure extends StoredProcedure implements SqlStoredProcedure {

    protected final HashMap<String, Object> mvinParamters = new HashMap<String, Object>();
    protected final DbContext dbContext;
    private String mvsql;
    private Map<String, Object> mvoutParamters = null;
    private boolean mvrecompiled;


    public OracleProcedure(DbContext context) {
        dbContext = context;
        initDataSource();
    }

    protected void initDataSource() {
        setDataSource(dbContext.getDataSource());
    }

    protected Map<String, Object> executeInternal() {
        return execute(mvinParamters);
    }

    @Override
    public String getSql() {
        return mvsql;
    }

    @Override
    public void setSql(String sql) {
        mvsql = sql;
        if (mvsql.toLowerCase().startsWith("declare")) {
            setSqlReadyForUse(true);
        } else {
            if (mvsql.contains("?") && !isSqlReadyForUse()) {
                setSqlReadyForUse(true);
                if (isFunction()) {
                    mvsql = "? := " + mvsql;
                }
                mvsql += ";";
            }
        }
        super.setSql(mvsql);
    }

    @Override
    public String getCallString() {
        if (isSqlReadyForUse() && mvsql.toLowerCase().startsWith("declare")) {
            return mvsql;
        }
        ProcedureCall call = new OracleProcedureCall(this);
        String callString = call.getSQL().toString();

        if (logger.isDebugEnabled()) {
            logger.debug("Compiled stored procedure. Call string is [" + callString + "]");
        }
        return callString;
    }

    @Override
    public boolean isRecompiled() {
        if (mvrecompiled) {
            return true;
        }
        for (SqlArgument sqlArgument : this.getDeclaredArguments()) {
            CustomArg ext = sqlArgument.getCustomArg();
            if (!ext.getIdentifier().equals("?")) {
                return false;
            }
        }
        mvrecompiled = true;
        return mvrecompiled;
    }

    @Override
    public void setRecompiled(boolean bValue) {
        mvrecompiled = bValue;
    }





    @Override
    public void execute() {
        if (!isRecompiled()) {
            throw new UnsupportedOperationException("SqlStoredProcedure is using type extension, you must call recompile before calling execute on this");
        }
        mvoutParamters = executeInternal();
    }

    @Override
    public void setParameter(SqlArgument parameterObj) {
        declareParameter((SqlParameter) parameterObj);
    }

    @Override
    public void setValue(String parameterName, Object value) {
        mvinParamters.put(parameterName, value);
    }

    @SuppressWarnings("boxing")
    @Override
    public int getIntValue(String paramterName) {
        return (Integer) mvoutParamters.get(paramterName);
    }

    @Override
    public String getStringValue(String paramterName) {
        return (String) mvoutParamters.get(paramterName);
    }

    @Override
    public Object getValue(String paramterName) {
        return mvoutParamters.get(paramterName);
    }

    @Override
    public Object getArray(String paramterName) {
        return mvoutParamters.get(paramterName);
    }

    @Override
    public Date getDateValue(String parameterName) {
        return (Date) mvoutParamters.get(parameterName);
    }

    @Override
    public Timestamp getTimestampValue(String parameterName) {
        return (Timestamp) mvoutParamters.get(parameterName);
    }

    @Override
    public boolean getBooleanValue(String parameterName) {
        return getIntValue(parameterName) == 1;
    }

    @Override
    public SqlArgument createInParameter(String parameterName, int type) {
        SqlArgument arg = new OracleSpringSqlParameter(parameterName, type);
        setParameter(arg);
        return arg;
    }


    @Override
    public SqlArgument createInParameter(String parameterName, int type, String objectType) {
        SqlArgument arg = new OracleSpringSqlParameter(parameterName, type, objectType);
        setParameter(arg);
        return arg;
    }

    @Override
    public SqlArgument createOutParameter(String parameterName, int type) {
        SqlArgument arg = new OracleSpringSqlOutParameter(parameterName, type);
        setParameter(arg);
        arg.getCustomArg().setResultsParameter(this.getDeclaredArguments().size() == 1);
        return arg;
    }


    @Override
    public SqlArgument createOutParameter(String parameterName, int type, String objectType) {
        SqlArgument arg = new OracleSpringSqlOutParameter(parameterName, type, objectType);
        setParameter(arg);
        arg.getCustomArg().setResultsParameter(this.getDeclaredArguments().size() == 1);
        return arg;
    }

    @Override
    public SqlArgumentCollection getDeclaredArguments() {
        return new OracleArgumentCollection(getDeclaredParameters());
    }

    @SuppressWarnings("boxing")
    @Override
    public void setBooleanValue(String parameterName, Boolean value) {

        setValue(parameterName, value ? 1 : 0);
    }

    @Override
    public Object getStruct(String paramterName) {
        return mvoutParamters.get(paramterName);
    }


}
