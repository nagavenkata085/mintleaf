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


import org.qamatic.mintleaf.core.BaseSqlObject;
import org.qamatic.mintleaf.interfaces.*;
import org.qamatic.mintleaf.oracle.spring.OracleSpringSqlProcedure;
import org.springframework.jdbc.core.JdbcTemplate;

public abstract class OraclePackage extends BaseSqlObject implements SqlPackage {

    public OraclePackage(DbContext context) {
        super(context);
    }

    public void setValue(String variable, String value) {
        getSqlReadListener().getTemplateValues().put(variable, value);
    }

    @Override
    public SqlProcedure getFunction(String functionName) {
        OracleSpringSqlProcedure proc = (OracleSpringSqlProcedure) createSqlProcedure();
        proc.setFunction(true);
        proc.setSqlReadyForUse(false);
        proc.setSql(getPkgProcedureName(functionName));
        return proc;
    }

    private SqlProcedure getFunction(String functionName, int type, boolean readyForUse, String typeObjectName) {
        SqlProcedure proc = getFunction(functionName);
        proc.setSqlReadyForUse(readyForUse);
        proc.setSql(getPkgProcedureName(functionName));
        if (typeObjectName == null) {
            proc.createOutParameter("result", type);
        } else {
            proc.createOutParameter("result", type, typeObjectName);
        }
        return proc;
    }

    @Override
    public SqlProcedure getFunction(String functionName, Class<? extends SqlTypeObject> typeObjectClass) {
        try {
            SqlProcedure proc = getFunction(functionName);
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
    public SqlProcedure getFunction(String functionName, int type, String typeObjectName) {
        return getFunction(functionName, type, false, typeObjectName);
    }

    @Override
    public SqlProcedure getFunction(String functionName, int type) {
        return getFunction(functionName, type, false, null);
    }

    @Override
    public SqlProcedure getProcedure(String procName) {
        OracleSpringSqlProcedure proc = (OracleSpringSqlProcedure) createSqlProcedure();
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

        SqlProcedure proc = getFunction(String.format("? := %s;", getPkgProcedureName(constantName)), dataType, true, null);
        proc.compile();
        proc.execute();
        return proc;
    }

    @Override
    public void drop() {
        DbUtility utils = new DbUtility(getDbContext());
        if (!utils.isPackageInterfaceExists(this.getName(), true)) {
            return;
        }
        JdbcTemplate template = new JdbcTemplate();
        template.setDataSource(getDbContext().getDataSource());
        template.execute("drop package " + this.getName());
    }

    protected SqlProcedure createSqlProcedure() {
        return new OracleSpringSqlProcedure(this);
    }

    @Override
    public boolean isExists() {
        DbUtility utils = new DbUtility(getDbContext());
        return utils.isPackageExists(getName());
    }
}
