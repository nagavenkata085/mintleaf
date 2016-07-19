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

import oracle.sql.DatumWithConnection;
import oracle.sql.STRUCT;
import oracle.sql.StructDescriptor;
import oracle.sql.TypeDescriptor;
import org.qamatic.mintleaf.*;
import org.qamatic.mintleaf.core.SqlStringReader;
import org.qamatic.mintleaf.dbs.oracle.OracleColumn;
import org.qamatic.mintleaf.dbs.oracle.OracleDbContext;
import org.qamatic.mintleaf.oracle.codeobjects.*;
import org.qamatic.mintleaf.oracle.core.SqlStoredProcedure;
import org.qamatic.mintleaf.oracle.spring.OraclePLProcedure;
import org.springframework.jdbc.core.SqlTypeValue;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class OracleTypeObject extends OracleBaseSqlScriptObject implements SqlScriptTypeObject, SqlTypeValue {

    protected SqlTypeObjectValue mvtypeobjectValue;
    protected PLCreateType mvPLCreateType;
    protected PLCreateTypeBody mvPLCreateTypeBody;
    protected DbMetaData mvmetaData;
    protected boolean mvcustomMetaData;

    public OracleTypeObject(DbContext context) {
        super(context);
    }

    @Override
    public void create() throws SQLException, IOException {

        if (isExists()) {
            return;
        }
        if (!isCreateFromSqlSource()) {
            prepareDefaultPLMethods();
            createInternal();
        } else {
            super.create();
        }
    }

    protected String getSectionalFile() {
        return null;
    }

    @Override
    public SqlReaderListener getSqlReadListener() {
        if (getSectionalFile() != null) {
            return ChangeSetListeners.getPLTypeObjectSectionalListner(getDbContext(), getSectionalFile(), null);
        }
        return super.getSqlReadListener();
    }

    @Override
    protected void onAfterCreate() {
        OracleHelperScript utils = new OracleHelperScript(getDbContext());
        utils.alterType(getName());
    }

    public boolean isCreateFromSqlSource() {
        if ((getSource() == null) || (getSource().trim().length() == 0)) {
            return false;
        }
        return getSource().toLowerCase().contains(".sql");
    }

    protected String getCreateFromSchemaTableName() {
        if ((getSource() == null) || !getSource().toLowerCase().startsWith("schematable:")) {
            return null;
        }
        return getSource().split(":")[1].toUpperCase();
    }

    protected SqlReader getCreateSourceReader(String sql) {
        return new SqlStringReader(sql);
    }

    protected boolean isCustomMetaData() {
        return mvcustomMetaData;
    }

    @Override
    protected SqlReader getCreateSourceReader() {
        try {
            invalidateColumnList();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (!isCreateFromSqlSource()) {

            StringBuilder sb = new StringBuilder();
            try {
                sb.append(getCreateTypeInstance().toString());
            } catch (SQLException e) {
                e.printStackTrace();
            }
            sb.append(String.format("%n/%n"));

            sb.append(getCreateTypeBodyInstance().toString());
            sb.append(String.format("%n/"));

            return getCreateSourceReader(sb.toString());
        }
        return super.getCreateSourceReader();
    }

    protected PLCreateTypeBody getCreateTypeBodyInstance() {
        if (mvPLCreateTypeBody == null) {
            mvPLCreateTypeBody = new PLCreateTypeBody(getName());
        }
        return mvPLCreateTypeBody;
    }

    protected String getTypeObjectExtendsFrom() {
        return "";
    }

    protected PLCreateType getCreateTypeInstance() throws SQLException {
        if (mvPLCreateType == null) {
            OracleHelperScript utils = new OracleHelperScript(getDbContext());
            mvPLCreateType = utils.createType(getName(), getTypeObjectExtendsFrom(), null, false);
            invalidateColumnList();
        }

        return mvPLCreateType;
    }

    public void invalidateColumnList() throws SQLException {

        if ((getMetaData() == null) || (getCreateTypeInstance().getColumnDefs().size() == getMetaData().size())) {
            return;
        }
        getCreateTypeInstance().getColumnDefs().clear();

        for (DbColumn colMetaData : getMetaData().getColumns()) {
            if (colMetaData.isIgnoreColumn()) {
                continue;
            }
            getCreateTypeInstance().getColumnDefs().add(new PLTableColumnDef(colMetaData.getColumnName(), colMetaData.getTypeName()));
        }

    }

    protected void prepareDefaultPLMethods() throws SQLException {
        prepareNoArgPLConstructorMethod();
    }

    protected PLTypeMemberMethodBody prepareNoArgPLConstructorMethod() throws SQLException {
        getCreateTypeInstance().addMemberMethod(new PLTypeMemberMethod());
        PLTypeMemberMethodBody m = new PLTypeMemberMethodBody();
        getCreateTypeBodyInstance().addMemberMethodBody(m);
        return m;
    }

    @Override
    public void createAll() throws SQLException, IOException {
        super.createAll();
        invalidate();
    }

    @Override
    public void invalidate() {
        OracleHelperScript utils = new OracleHelperScript(getDbContext());
        Class<? extends SqlScriptTypeObject>[] dependentTypes = SqlObjectHelper.getDependencyItems(this, SqlScriptTypeObject.class);
        for (Class<? extends SqlScriptTypeObject> typeObjClass : dependentTypes) {
            utils.alterType(typeObjClass);
        }
        utils.alterType(getName());
    }

    @Override
    public void drop() {
        new OracleHelperScript(getDbContext()).dropType(getName());
    }

    private Field[] getTypeObjectFields(Class<?> type) {
        List<Field> result = new ArrayList<Field>();

        Class<?> i = type;
        int insertAt = 0;
        while ((i != null) && (i != Object.class)) {
            for (Field field : i.getDeclaredFields()) {
                TypeObjectField antot = field.getAnnotation(TypeObjectField.class);
                if (antot != null) {
                    result.add(insertAt, field);
                    insertAt++;
                }
            }
            i = i.getSuperclass();
            insertAt = 0;
        }

        return result.toArray(new Field[result.size()]);
    }

    private DbMetaData getMetaDataFromTypeObjectFieldAnnotation(Class<?> type) {
        DbMetaData result = new DbMetaData();
        for (Field field : getTypeObjectFields(type)) {
            TypeObjectField antot = field.getAnnotation(TypeObjectField.class);
            DbColumn colMetaData = new OracleColumn(antot.name(), antot.dataType());
            colMetaData.setCalculated(antot.isCalculated());
            colMetaData.setIgnoreForTypeObjectCreation(antot.isIgnoreForTypeObjectCreation());
            colMetaData.setColumnSize(antot.columnSize());
            colMetaData.setDecimalDigits(antot.decimalDigits());
            colMetaData.setDatatype(antot.jdbcDataType());
            result.add(colMetaData);
        }

        return result;
    }

    @Override
    public DbMetaData getMetaData() throws SQLException {

        if (mvmetaData == null) {
            if (!isCustomMetaData() && (getCreateFromSchemaTableName() != null)) {
                mvmetaData = getDbContext().getMetaData(getCreateFromSchemaTableName());

            } else {
                mvmetaData = getMetaDataFromTypeObjectFieldAnnotation(this.getClass());
            }
        }

        return mvmetaData;
    }

    @Override
    public void setMetaData(DbMetaData metaData) {
        mvmetaData = metaData;
    }

    @Override
    public SqlTypeObjectValue getTypeObjectValue() throws SQLException {
        return mvtypeobjectValue;
    }

    @Override
    public void setTypeObjectValue(SqlTypeObjectValue value) throws SQLException {
        DbMetaData metaData = getMetaData();
        if (metaData != null) {
            value.setMetaData(metaData);
        }
        mvtypeobjectValue = value;
    }

    @Override
    public boolean isTypeObjectValueNull() {
        return mvtypeobjectValue == null;
    }

    @Override
    public void autoBind() throws SQLException {

        if (getTypeObjectValue() == null) {
            return;
        }

        autoBindInternal(getTypeObjectFields(this.getClass()));

    }

    private void autoBindInternal(Field[] fields) throws SQLException {
        for (Field field : fields) {
            TypeObjectField annotation = field.getAnnotation(TypeObjectField.class);
            if (annotation == null) {
                continue;
            }

            Object fValue = getTypeObjectValue().getValue(annotation.name());
            if (fValue == null) {
                continue;
            }
            try {
                field.setAccessible(true);
                if (fValue instanceof oracle.sql.BLOB) {
                    // not supported for now
                } else if (fValue instanceof oracle.sql.TIMESTAMP) {
                    field.set(this, ((oracle.sql.TIMESTAMP) fValue).dateValue());
                } else if (fValue instanceof oracle.sql.DATE) {
                    field.set(this, ((oracle.sql.DATE) fValue).dateValue());
                } else {
                    field.set(this, fValue);
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public final void setTypeValue(PreparedStatement ps, int paramIndex, int sqlType, String typeName) throws SQLException {

        Object value = createTypeValue(ps.getConnection(), sqlType, typeName);
        if (sqlType == TYPE_UNKNOWN) {
            ps.setObject(paramIndex, value);
        } else {
            ps.setObject(paramIndex, value, sqlType);
        }
    }

    protected TypeDescriptor getDescriptor(Connection conn, String typeName) throws SQLException {
        return new StructDescriptor(typeName, conn);
    }

    protected DatumWithConnection getDatum(TypeDescriptor descriptor, Connection conn, String typeName) throws SQLException {
        return new STRUCT((StructDescriptor) getDescriptor(conn, typeName), conn, getValues());
    }

    private Object createTypeValue(Connection conn, int sqlType, String typeName) throws SQLException {
        Connection dconn = conn;
        if (conn instanceof org.apache.commons.dbcp.DelegatingConnection) {
            dconn = ((org.apache.commons.dbcp.DelegatingConnection) conn).getInnermostDelegate();
        }

        return getDatum(getDescriptor(dconn, typeName), dconn, typeName);
    }

    protected Object[] getValues() {
        return new Object[]{};
    }

    @Override
    public SqlStoredProcedure getMemberProcedure(String memberProcName) {
        OraclePLProcedure proc = new OraclePLProcedure( this.getDbContext() );
        proc.setSql(String.format("declare bo %s; begin bo:=?; bo.%s; end;", getName(), memberProcName));
        proc.createTypeObjectParameter("bo", this.getClass());
        proc.compile();
        proc.setValue("bo", this);
        return proc;
    }

    protected OracleDbContext getOracleDbContext() {
        return (OracleDbContext) getDbContext();
    }


    @Override
    public boolean isExists() {

        return (getOracleDbContext().isTypeExists(getName(), true));

    }
}
