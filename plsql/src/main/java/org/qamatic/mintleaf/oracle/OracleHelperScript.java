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

package org.qamatic.mintleaf.oracle;


import org.qamatic.mintleaf.DbColumn;
import org.qamatic.mintleaf.DbContext;
import org.qamatic.mintleaf.DbMetaData;
import org.qamatic.mintleaf.oracle.core.SqlObjectInfo;
import org.qamatic.mintleaf.dbs.oracle.OracleDbContext;
import org.qamatic.mintleaf.oracle.codeobjects.PLCreateType;
import org.qamatic.mintleaf.oracle.codeobjects.PLGrantPrivilege;
import org.qamatic.mintleaf.oracle.codeobjects.PLTableColumnDef;
import org.qamatic.mintleaf.oracle.core.SqlScriptObject;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@SqlObjectInfo(name = "OracleDbHelper")
public class OracleHelperScript extends OraclePackage {

    public OracleHelperScript(DbContext context) {
        super(context);
    }

    @Override
    public void drop() {
        // no need to drop, all test db assertions are depending on it.
    }

    @Override
    public void create() throws SQLException, IOException {
        //no need to create anything.
    }


    public void createType(String typeName) {
        createType(typeName, null, null);
    }


    public void createType(String typeName, List<PLTableColumnDef> columns) {

        createType(typeName, null, columns);
    }


    public PLCreateType createType(String typeName, String parentClassName, List<PLTableColumnDef> columns) {
        return createType(typeName, parentClassName, columns, true);

    }


    public PLCreateType createType(String typeName, String parentClassName, List<PLTableColumnDef> columns, boolean bCreate) {

        PLCreateType t = new PLCreateType(typeName, parentClassName);
        if (columns != null) {
            for (PLTableColumnDef plTableColumnDef : columns) {
                t.addColumnDef(plTableColumnDef);
            }
        }
        if (!getOracleDbContext().isTypeExists(typeName, false) && bCreate) {
            JdbcTemplate template = new JdbcTemplate(getDbContext().getDataSource());
            template.execute(t.toString());
            alterType(typeName);

        }

        return t;
    }

    private OracleDbContext getOracleDbContext() {
        return (OracleDbContext) getDbContext();
    }


    public void createType(String typeName, String parentClassName) {

        createType(typeName, parentClassName, new ArrayList<PLTableColumnDef>());
    }


    public PLCreateType createTypeFromTable(String typeName, String parentClassName, String tableName) throws SQLException {
        return createTypeFromTable(typeName, parentClassName, tableName, true);
    }


    public PLCreateType createTypeFromTable(String typeName, String parentClassName, String tableName, boolean bCreate) throws SQLException {
        return createType(typeName, parentClassName, getTableColumnCodeObjects(tableName), bCreate);
    }


    public void alterType(Class<? extends SqlScriptTypeObject> aType) {
        alterType(SqlObjectHelper.getDbObjectInfo(aType).name());
    }


    public void alterType(String typeName) {
        if (!getDbContext().isSqlObjectExists(typeName, "TYPE", true)) {
            return;
        }
        logger.info("exec:" + typeName);
        JdbcTemplate template = new JdbcTemplate(getDbContext().getDataSource());

        template.execute(String.format("alter type %s compile", typeName));
        if (getDbContext().isSqlObjectExists(typeName, "TYPE BODY", true)) {
            template.execute(String.format("alter type %s compile body", typeName));
        }

    }

    public void dropType(String typeName) {
        getOracleDbContext().dropObject(typeName, "TYPE");
    }


    public void dropPackage(String pkgName) {
        getOracleDbContext().dropObject(pkgName, "package");
    }


    public boolean isDependencyPackageExists(SqlScriptStoredProcedureModule pkg) {

        Class<OraclePackage>[] items = SqlObjectHelper.getDependencyItems(pkg, OraclePackage.class);
        for (Class<OraclePackage> sqlClass : items) {
            if (!isPackageExists(sqlClass)) {
                return false;
            }
        }
        return true;
    }

    public boolean isPackageExists(Class<? extends SqlScriptObject> pkgClass) {
        SqlObjectInfo objAnnot = SqlObjectHelper.getDbObjectInfo(pkgClass);
        if (objAnnot == null) {
            return true;
        }

        return getOracleDbContext().isPackageExists(objAnnot.name(), false);
    }

    public List<PLTableColumnDef> getTableColumnCodeObjects(DbMetaData metaData) {

        List<PLTableColumnDef> list = new ArrayList<PLTableColumnDef>();
        for (DbColumn colMetaData : metaData.getColumns()) {
            list.add(new PLTableColumnDef(colMetaData.getColumnName(), colMetaData.getTypeName()));
        }
        return list;
    }


    public List<PLTableColumnDef> getTableColumnCodeObjects(String tableName) throws SQLException {
        return getTableColumnCodeObjects(getDbContext().getMetaData(tableName));
    }


    public void grantPrivilege(String schemaName, String privilegeName, String objectName) {

        JdbcTemplate template = new JdbcTemplate(getDbContext().getDataSource());
        template.execute(new PLGrantPrivilege(schemaName, privilegeName, objectName).toString());

    }


}
