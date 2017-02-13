/*
 * <!--
 *   ~
 *   ~ The MIT License (MIT)
 *   ~
 *   ~ Copyright (c) 2010-2017 Senthil Maruthaiappan
 *   ~
 *   ~ Permission is hereby granted, free of charge, to any person obtaining a copy
 *   ~ of this software and associated documentation files (the "Software"), to deal
 *   ~ in the Software without restriction, including without limitation the rights
 *   ~ to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   ~ copies of the Software, and to permit persons to whom the Software is
 *   ~ furnished to do so, subject to the following conditions:
 *   ~
 *   ~ The above copyright notice and this permission notice shall be included in all
 *   ~ copies or substantial portions of the Software.
 *   ~
 *   ~ THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   ~ IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   ~ FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *   ~ AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *   ~ LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   ~ OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *   ~ SOFTWARE.
 *   ~
 *   ~
 *   -->
 */

package org.qamatic.mintleaf.oracle.spring;

import oracle.sql.STRUCT;
import org.qamatic.mintleaf.DbContext;
import org.qamatic.mintleaf.oracle.SqlObjectHelper;
import org.qamatic.mintleaf.oracle.core.*;
import org.qamatic.mintleaf.oracle.*;
import org.qamatic.mintleaf.oracle.argextensions.OracleBooleanType;
import org.qamatic.mintleaf.oracle.argextensions.OracleRecordType;
import org.qamatic.mintleaf.oracle.argextensions.OracleRowType;
import org.qamatic.mintleaf.oracle.codeobjects.PLCreateType;

import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OraclePLProcedure extends OracleProcedure {

    private Map<String, SqlScriptTypeObject> mvtypeObjectRegistry;

    public OraclePLProcedure(DbContext context) {
        super(context);
    }

    public SqlArgument createRowTypeOutParameter(String parameterName, String tableName) {
        String supportedType = "TE_" + tableName;
        SqlArgument arg = new OracleSpringSqlOutParameter(parameterName, Types.STRUCT, supportedType);
        setParameter(arg);

        OracleRowType ext = getRowType(tableName, supportedType);

        arg.setCustomArg(ext);
        ext.setIdentifier(parameterName);
        ext.setOutParameter(true);
        ext.setSupportedType(supportedType);
        ext.setUnsupportedType(tableName + "%ROWTYPE");

        arg.getCustomArg().setResultsParameter(this.getDeclaredArguments().size() == 1);
        return arg;
    }



    public SqlStoredProcedure recompile() {
        SqlStoredProcedure proc = new OraclePLProcedure(dbContext);
        proc.setSql(getCallString());
        proc.setSqlReadyForUse(true);
        List<SqlArgument> args = this.getDeclaredArguments().rebuildArguments();
        for (SqlArgument sqlArgument : args) {
            proc.setParameter(sqlArgument);
        }

        this.mvtypeObjectRegistry = this.getTypeObjectRegistry();
        proc.setRecompiled(true);
        return proc;
    }

    public SqlArgument createRowTypeOutParameter(String parameterName, Class<? extends SqlScriptTypeObject> typeObjectClass) {
        SqlScriptObject sobj = getTypeObjectInstance(parameterName, typeObjectClass);
        SqlArgument arg = createRowTypeOutParameter(parameterName, sobj.getName().toUpperCase());
        sobj.setName(arg.getCustomArg().getSupportedType());
        return arg;
    }

    public SqlArgument createTypeObjectParameter(String parameterName, Class<? extends SqlScriptTypeObject> typeObjectClass) {

        SqlScriptObject sobj = getTypeObjectInstance(parameterName, typeObjectClass);
        SqlArgument arg = createInParameter(parameterName, Types.STRUCT, sobj.getName().toUpperCase());
        CustomArg ext = new OracleArg();
        ext.setSupportedType(arg.getTypeName());
        arg.setCustomArg(ext);
        return arg;

    }

    public SqlArgument createTypeObjectOutParameter(String parameterName, Class<? extends SqlScriptTypeObject> typeObjectClass) {

        SqlScriptObject sobj = getTypeObjectInstance(parameterName, typeObjectClass);
        SqlArgument arg = createOutParameter(parameterName, Types.STRUCT, sobj.getName().toUpperCase());
        CustomArg ext = new OracleArg();
        ext.setSupportedType(arg.getTypeName());
        ext.setOutParameter(true);
        ext.setResultsParameter(this.getDeclaredArguments().size() == 1);
        arg.setCustomArg(ext);

        return arg;

    }

    public SqlScriptTypeObject getTypeObject(String parameterName) throws SQLException {
        SqlScriptTypeObject typeObj = getTypeObjectInstance(parameterName, null);
        typeObj.setTypeObjectValue(getTypeObjectValue(parameterName));
        typeObj.autoBind();
        return typeObj;
    }


    protected Map<String, SqlScriptTypeObject> getTypeObjectRegistry() {
        if (mvtypeObjectRegistry == null) {
            mvtypeObjectRegistry = new HashMap<String, SqlScriptTypeObject>();
        }
        return mvtypeObjectRegistry;
    }


    private SqlScriptTypeObject getTypeObjectInstance(String parameterName, Class<? extends SqlScriptTypeObject> typeObjectClass) {
        try {
            SqlScriptTypeObject typeObj = null;
            if (getTypeObjectRegistry().containsKey(parameterName)) {
                typeObj = getTypeObjectRegistry().get(parameterName);
            } else {
                typeObj = (SqlScriptTypeObject) SqlObjectHelper.createSqlObjectInstance(dbContext, typeObjectClass);
                getTypeObjectRegistry().put(parameterName, typeObj);
            }
            return typeObj;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    public SqlTypeObjectValue getTypeObjectValue(String parameterName) throws SQLException {

        SqlScriptTypeObject typeObj = null;

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

    public SqlArgument createRecordOutParameter(String parameterName, String supportedType, String unsupportedType) {
        SqlArgument arg = new OracleSpringSqlOutParameter(parameterName, Types.STRUCT, supportedType);
        setParameter(arg);
        CustomArg ext = new OracleRecordType();
        arg.setCustomArg(ext);
        ext.setIdentifier(parameterName);
        ext.setOutParameter(true);
        ext.setSupportedType(supportedType);
        ext.setUnsupportedType(unsupportedType);
        arg.getCustomArg().setResultsParameter(this.getDeclaredArguments().size() == 1);
        return arg;
    }


    protected SqlTypeObjectValue createTypeObjectValueInstance(String parameterName) {
        final String pName = parameterName;

        SqlTypeObjectValue typeObjValue = new OracleTypeObjectValue(dbContext, getDeclaredArguments().get(pName).getCustomArg()
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

    protected Object[] getOracleAttributes(String parameterName) throws SQLException {
        return ((STRUCT) getStruct(parameterName)).getOracleAttributes();
    }


    protected OracleRowType getRowType(String rowTypeTableName, String supportedType) {
        PLCreateType p = null;
        try {
            p = new OracleHelperScript(dbContext).createTypeFromTable(supportedType, null, rowTypeTableName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        OracleRowType ext = new OracleRowType();
        for (CodeObject member : p.getColumnDefs()) {
            MemberField field = (MemberField) member;
            ext.addTypeMap(new ColumnMap(field.getLeftSide(), field.getLeftSide()));
        }
        return ext;
    }

    public SqlArgument createRecordParameter(String parameterName, String supportedType, String unsupportedType) {
        SqlArgument arg = new OracleSpringSqlParameter(parameterName, Types.STRUCT);
        setParameter(arg);
        CustomArg ext = new OracleRecordType();
        arg.setCustomArg(ext);
        ext.setIdentifier(parameterName);
        ext.setSupportedType(supportedType);
        ext.setUnsupportedType(unsupportedType);
        return arg;
    }

    public SqlArgument createInBooleanParameter(String parameterName) {
        SqlArgument arg = new OracleSpringSqlParameter(parameterName, Types.INTEGER);
        setParameter(arg);
        CustomArg ext = new OracleBooleanType();
        arg.setCustomArg(ext);
        ext.setIdentifier(parameterName);
        return arg;
    }

    public SqlArgument createBooleanOutParameter(String parameterName) {
        SqlArgument arg = new OracleSpringSqlOutParameter(parameterName, Types.INTEGER);
        setParameter(arg);
        CustomArg ext = new OracleBooleanType();
        arg.setCustomArg(ext);
        ext.setIdentifier(parameterName);
        ext.setOutParameter(true);
        arg.getCustomArg().setResultsParameter(this.getDeclaredArguments().size() == 1);
        return arg;
    }


}
