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


import org.qamatic.mintleaf.interfaces.*;
import org.qamatic.mintleaf.dbsupportimpls.oracle.intf.OracleDbContext;
import org.qamatic.mintleaf.oracle.core.SqlStoredProcedure;
import org.qamatic.mintleaf.oracle.core.SqlValue;
import org.qamatic.mintleaf.oracle.spring.OraclePLProcedure;
import org.springframework.jdbc.core.JdbcTemplate;

public class OraclePackage extends OracleBaseSqlScriptObject implements SqlScriptStoredProcedureModule {

    public OraclePackage(DbContext context) {
        super(context);
    }

    public void setValue(String variable, String value) {
        getSqlReadListener().getTemplateValues().put(variable, value);
    }

    @Override
    public SqlStoredProcedure getFunction(String functionName) {
        OraclePLProcedure proc = (OraclePLProcedure) createSqlProcedure();
        proc.setFunction(true);
        proc.setSqlReadyForUse(false);
        proc.setSql(getPkgProcedureName(functionName));
        return proc;
    }

    private SqlStoredProcedure getFunction(String functionName, int type, boolean readyForUse, String typeObjectName) {
        SqlStoredProcedure proc = getFunction(functionName);
        proc.setSqlReadyForUse(readyForUse);
        proc.setSql(getPkgProcedureName(functionName));
        if (typeObjectName == null) {
            proc.createOutParameter("result", type);
        } else {
            proc.createOutParameter("result", type, typeObjectName);
        }
        return proc;
    }


    public SqlStoredProcedure getFunction(String functionName, Class<? extends SqlScriptTypeObject> typeObjectClass) {
        try {
            OraclePLProcedure proc = (OraclePLProcedure) getFunction(functionName);
            proc.setSqlReadyForUse(false);
            proc.setSql(getPkgProcedureName(functionName));
            proc.createTypeObjectOutParameter("result", typeObjectClass);
            return proc;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public SqlStoredProcedure getFunction(String functionName, int type, String typeObjectName) {
        return getFunction(functionName, type, false, typeObjectName);
    }

    @Override
    public SqlStoredProcedure getFunction(String functionName, int type) {
        return getFunction(functionName, type, false, null);
    }

    @Override
    public SqlStoredProcedure getProcedure(String procName) {
        OraclePLProcedure proc = (OraclePLProcedure) createSqlProcedure();
        proc.setSql(getPkgProcedureName(procName));
        return proc;
    }

    private String getPkgProcedureName(String procName) {
        if (!getName().equals("")) {
            if (!procName.contains(".")) {
                procName = getName() + "." + procName;
            }
        }
        return procName;
    }

    @Override
    public SqlValue getConstant(String constantName, int dataType) {

        SqlStoredProcedure proc = getFunction(String.format("? := %s;", getPkgProcedureName(constantName)), dataType, true, null);
        proc.compile();
        proc.execute();
        return proc;
    }

    @Override
    public void drop() {

        if (!getOracleDbContext().isPackageExists(this.getName(), true)) {
            return;
        }
        JdbcTemplate template = new JdbcTemplate();
        template.setDataSource(getDbContext().getDataSource());
        template.execute("drop package " + this.getName());
    }

    protected SqlStoredProcedure createSqlProcedure() {
        return new OraclePLProcedure(getDbContext());
    }


    private OracleDbContext getOracleDbContext() {
        return (OracleDbContext) getDbContext();
    }

    @Override
    public boolean isExists() {

        return getOracleDbContext().isPackageExists(getName(), false);
    }
}
