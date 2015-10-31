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

package org.qamatic.mintleaf.oracle.spring;

import oracle.sql.ARRAY;
import oracle.sql.STRUCT;
import org.qamatic.mintleaf.core.BaseProcedureCall;
import org.qamatic.mintleaf.core.SqlObjectHelper;
import org.qamatic.mintleaf.interfaces.*;
import org.qamatic.mintleaf.oracle.DbUtility;
import org.qamatic.mintleaf.oracle.argextensions.OracleArgumentTypeExtension;
import org.qamatic.mintleaf.oracle.argextensions.OracleBooleanTypeExtension;
import org.qamatic.mintleaf.oracle.argextensions.OracleRecordTypeExtension;
import org.qamatic.mintleaf.oracle.argextensions.OracleRowTypeExtension;
import org.qamatic.mintleaf.oracle.codeobjects.PLCreateType;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OracleSpringSqlProcedure extends StoredProcedure implements SqlProcedure {

    private final HashMap<String, Object> mvinParamters = new HashMap<String, Object>();
    private final SqlPackage mvpackage;
    private String mvsql;
    private Map<String, Object> mvoutParamters = null;
    private boolean mvrecompiled;
    private Map<String, SqlTypeObject> mvtypeObjectRegistry;

    public OracleSpringSqlProcedure(SqlPackage pkg) {
        mvpackage = pkg;
        initDataSource();
    }

    protected void initDataSource() {
        setDataSource(mvpackage.getDbContext().getDataSource());
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
        ProcedureCall call = new BaseProcedureCall(this);
        String callString = call.getCallString().toString();

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
            SqlArgumentTypeExtension ext = sqlArgument.getTypeExtension();
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
    public SqlProcedure recompile() {
        SqlProcedure proc = mvpackage.getProcedure(getCallString());
        proc.setSqlReadyForUse(true);
        List<SqlArgument> args = this.getDeclaredArguments().rebuildArguments();
        for (SqlArgument sqlArgument : args) {
            proc.setParameter(sqlArgument);
        }

        ((OracleSpringSqlProcedure) proc).mvtypeObjectRegistry = this.getTypeObjectRegistry();
        proc.setRecompiled(true);
        return proc;
    }

    @Override
    public void execute() {
        if (!isRecompiled()) {
            throw new UnsupportedOperationException("SqlProcedure is using type extension, you must call recompile before calling execute on this");
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
    public ARRAY getArray(String paramterName) {
        return (ARRAY) mvoutParamters.get(paramterName);
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
    public SqlArgument createParameter(String parameterName, int type) {
        SqlArgument arg = new OracleSpringSqlParameter(parameterName, type);
        setParameter(arg);
        return arg;
    }

    @Override
    public SqlArgument createBooleanParameter(String parameterName) {
        SqlArgument arg = new OracleSpringSqlParameter(parameterName, Types.INTEGER);
        setParameter(arg);
        SqlArgumentTypeExtension ext = new OracleBooleanTypeExtension();
        arg.setTypeExtension(ext);
        ext.setIdentifier(parameterName);
        return arg;
    }

    @Override
    public SqlArgument createRecordParameter(String parameterName, String supportedType, String unsupportedType) {
        SqlArgument arg = new OracleSpringSqlParameter(parameterName, Types.STRUCT);
        setParameter(arg);
        SqlArgumentTypeExtension ext = new OracleRecordTypeExtension();
        arg.setTypeExtension(ext);
        ext.setIdentifier(parameterName);
        ext.setSupportedType(supportedType);
        ext.setUnsupportedType(unsupportedType);
        return arg;
    }

    @Override
    public SqlArgument createParameter(String parameterName, int type, String objectType) {
        SqlArgument arg = new OracleSpringSqlParameter(parameterName, type, objectType);
        setParameter(arg);
        return arg;
    }

    @Override
    public SqlArgument createOutParameter(String parameterName, int type) {
        SqlArgument arg = new OracleSpringSqlOutParameter(parameterName, type);
        setParameter(arg);
        arg.getTypeExtension().setResultsParameter(this.getDeclaredArguments().size() == 1);
        return arg;
    }

    @Override
    public SqlArgument createBooleanOutParameter(String parameterName) {
        SqlArgument arg = new OracleSpringSqlOutParameter(parameterName, Types.INTEGER);
        setParameter(arg);
        SqlArgumentTypeExtension ext = new OracleBooleanTypeExtension();
        arg.setTypeExtension(ext);
        ext.setIdentifier(parameterName);
        ext.setOutParameter(true);
        arg.getTypeExtension().setResultsParameter(this.getDeclaredArguments().size() == 1);
        return arg;
    }

    @Override
    public SqlArgument createRecordOutParameter(String parameterName, String supportedType, String unsupportedType) {
        SqlArgument arg = new OracleSpringSqlOutParameter(parameterName, Types.STRUCT, supportedType);
        setParameter(arg);
        SqlArgumentTypeExtension ext = new OracleRecordTypeExtension();
        arg.setTypeExtension(ext);
        ext.setIdentifier(parameterName);
        ext.setOutParameter(true);
        ext.setSupportedType(supportedType);
        ext.setUnsupportedType(unsupportedType);
        arg.getTypeExtension().setResultsParameter(this.getDeclaredArguments().size() == 1);
        return arg;
    }

    @Override
    public SqlArgument createRowTypeOutParameter(String parameterName, String tableName) {
        String supportedType = "TE_" + tableName;
        SqlArgument arg = new OracleSpringSqlOutParameter(parameterName, Types.STRUCT, supportedType);
        setParameter(arg);

        OracleRowTypeExtension ext = getRowTypeExtension(tableName, supportedType);

        arg.setTypeExtension(ext);
        ext.setIdentifier(parameterName);
        ext.setOutParameter(true);
        ext.setSupportedType(supportedType);
        ext.setUnsupportedType(tableName + "%ROWTYPE");

        arg.getTypeExtension().setResultsParameter(this.getDeclaredArguments().size() == 1);
        return arg;
    }

    @Override
    public SqlArgument createRowTypeOutParameter(String parameterName, Class<? extends SqlTypeObject> typeObjectClass) {
        SqlObject sobj = getTypeObjectInstance(parameterName, typeObjectClass);
        SqlArgument arg = createRowTypeOutParameter(parameterName, sobj.getName().toUpperCase());
        sobj.setName(arg.getTypeExtension().getSupportedType());
        return arg;
    }

    protected OracleRowTypeExtension getRowTypeExtension(String rowTypeTableName, String supportedType) {
        PLCreateType p = null;
        try {
            p = new DbUtility(mvpackage.getDbContext()).createTypeFromTable(supportedType, null, rowTypeTableName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        OracleRowTypeExtension ext = new OracleRowTypeExtension();
        for (CodeObject member : p.getColumnDefs()) {
            MemberField field = (MemberField) member;
            ext.addTypeMap(new ArgumentTypeMap(field.getLeftSide(), field.getLeftSide()));
        }
        return ext;
    }

    @Override
    public SqlArgument createOutParameter(String parameterName, int type, String objectType) {
        SqlArgument arg = new OracleSpringSqlOutParameter(parameterName, type, objectType);
        setParameter(arg);
        arg.getTypeExtension().setResultsParameter(this.getDeclaredArguments().size() == 1);
        return arg;
    }

    @Override
    public SqlArgumentCollection getDeclaredArguments() {
        return new OracleSpringSqlArgumentCollection(getDeclaredParameters());
    }

    @SuppressWarnings("boxing")
    @Override
    public void setBooleanValue(String parameterName, Boolean value) {

        setValue(parameterName, value ? 1 : 0);
    }

    @Override
    public STRUCT getStruct(String paramterName) {
        return (STRUCT) mvoutParamters.get(paramterName);
    }

    protected Object[] getOracleAttributes(String parameterName) throws SQLException {
        return getStruct(parameterName).getOracleAttributes();
    }

    @Override
    public SqlTypeObjectValue getTypeObjectValue(String parameterName) throws SQLException {

        SqlTypeObject typeObj = null;

        if (getTypeObjectRegistry().containsKey(parameterName)) {
            typeObj = getTypeObjectInstance(parameterName, null);
        }

        if ((typeObj != null) && !typeObj.isTypeObjectValueNull()) {
            return typeObj.getTypeObjectValue();
        }

        SqlTypeObjectValue typeObjValue = createTypeObjectValueInstance(parameterName);

        if (typeObj != null) {
            typeObj.setTypeObjectValue(typeObjValue);
        }
        return typeObjValue;

    }

    protected SqlTypeObjectValue createTypeObjectValueInstance(String parameterName) {
        final String pName = parameterName;

        SqlTypeObjectValue typeObjValue = new OracleTypeObjectValue(mvpackage.getDbContext(), getDeclaredArguments().get(pName).getTypeExtension()
                .getSupportedType()) {
            @Override
            protected List<Object> getObjects() {
                List<Object> list = new ArrayList<Object>();

                Object[] datums = null;
                try {
                    datums = getOracleAttributes(pName);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                for (Object object : datums) {
                    list.add(object);
                }

                return list;
            }
        };
        return typeObjValue;
    }

    @Override
    public SqlTypeObject getTypeObject(String parameterName) throws SQLException {
        SqlTypeObject typeObj = getTypeObjectInstance(parameterName, null);
        typeObj.setTypeObjectValue(getTypeObjectValue(parameterName));
        typeObj.autoBind();
        return typeObj;
    }

    private SqlTypeObject getTypeObjectInstance(String parameterName, Class<? extends SqlTypeObject> typeObjectClass) {
        try {
            SqlTypeObject typeObj = null;
            if (getTypeObjectRegistry().containsKey(parameterName)) {
                typeObj = getTypeObjectRegistry().get(parameterName);
            } else {
                typeObj = (SqlTypeObject) SqlObjectHelper.createSqlObjectInstance(mvpackage.getDbContext(), typeObjectClass);
                getTypeObjectRegistry().put(parameterName, typeObj);
            }
            return typeObj;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    protected Map<String, SqlTypeObject> getTypeObjectRegistry() {
        if (mvtypeObjectRegistry == null) {
            mvtypeObjectRegistry = new HashMap<String, SqlTypeObject>();
        }
        return mvtypeObjectRegistry;
    }

    @Override
    public SqlArgument createTypeObjectParameter(String parameterName, Class<? extends SqlTypeObject> typeObjectClass) {

        SqlObject sobj = getTypeObjectInstance(parameterName, typeObjectClass);
        SqlArgument arg = createParameter(parameterName, Types.STRUCT, sobj.getName().toUpperCase());
        SqlArgumentTypeExtension ext = new OracleArgumentTypeExtension();
        ext.setSupportedType(arg.getTypeName());
        arg.setTypeExtension(ext);
        return arg;

    }

    @Override
    public SqlArgument createTypeObjectOutParameter(String parameterName, Class<? extends SqlTypeObject> typeObjectClass) {

        SqlObject sobj = getTypeObjectInstance(parameterName, typeObjectClass);
        SqlArgument arg = createOutParameter(parameterName, Types.STRUCT, sobj.getName().toUpperCase());
        SqlArgumentTypeExtension ext = new OracleArgumentTypeExtension();
        ext.setSupportedType(arg.getTypeName());
        ext.setOutParameter(true);
        ext.setResultsParameter(this.getDeclaredArguments().size() == 1);
        arg.setTypeExtension(ext);

        return arg;

    }
}