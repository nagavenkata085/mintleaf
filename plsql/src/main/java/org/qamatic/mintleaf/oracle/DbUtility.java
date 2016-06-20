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


import org.qamatic.mintleaf.core.SqlObjectHelper;
import org.qamatic.mintleaf.core.SqlObjectInfo;
import org.qamatic.mintleaf.interfaces.*;
import org.qamatic.mintleaf.oracle.codeobjects.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@SqlObjectInfo(name = "DbUtility")
public class DbUtility extends OraclePackage   {

    public DbUtility(DbContext context) {
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


    public boolean isPackageExistsByName(String pkgName) {
        return isPackageInterfaceExists(pkgName);
    }


    public boolean isPackageExists(String pkgName) {
        return isPackageInterfaceExists(pkgName);
    }


    public boolean isTypeExists(String typeName) {
        return isTypeExists(typeName, false);
    }


    public boolean isTypeBodyExists(String typeName) {
        return isTypeBodyExists(typeName, false);
    }


    public boolean isTypeBodyExists(String typeName, boolean igoreValidity) {
        return getDbContext().isSqlObjectExists(typeName, "TYPE BODY", igoreValidity);
    }


    public boolean isTypeExists(String typeName, boolean igoreValidity) {
        return getDbContext().isSqlObjectExists(typeName, "TYPE", igoreValidity);
    }


    public boolean isTriggerExists(String triggerName) {
        return isTriggerExists(triggerName, false);
    }

    public boolean isTriggerExists(String triggerName, boolean igoreValidity) {
        return getDbContext().isSqlObjectExists(triggerName, "TRIGGER", igoreValidity);
    }


    public boolean isSynonymExists(String synonymName) {
        return isSynonymExists(synonymName, false);
    }


    public boolean isSynonymExists(String synonymName, boolean igoreValidity) {
        return getDbContext().isSqlObjectExists(synonymName, "SYNONYM", igoreValidity);
    }


    public boolean isPackageExists(OraclePackage pkg) {
        return isPackageInterfaceExists(pkg.getName());
    }


    public boolean isPackageExists(Class<? extends OraclePackage> pkgClass) {
        SqlObjectInfo objAnnot = SqlObjectHelper.getDbObjectInfo(pkgClass);
        if (objAnnot == null) {
            return true;
        }
        return isPackageInterfaceExists(objAnnot.name());
    }


    public boolean isPackageInterfaceExists(String pkgName) {
        return getDbContext().isSqlObjectExists(pkgName, "PACKAGE", false);
    }


    public boolean isPackageInterfaceExists(String pkgName, boolean ignoreValidity) {
        return getDbContext().isSqlObjectExists(pkgName, "PACKAGE", ignoreValidity);
    }


    public boolean isPackageBodyExists(String pkgName) {
        return getDbContext().isSqlObjectExists(pkgName, "PACKAGE BODY", false);
    }




    public String getContextParamValue(String nameSpace, String paramName) {
        JdbcTemplate template = new JdbcTemplate();
        template.setDataSource(getDbContext().getDataSource());
        String sql = String.format("Select sys_context('%s','%s') from dual", nameSpace, paramName);
        return template.queryForObject(sql, String.class);
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
        if (!isTypeExists(typeName) && bCreate) {
            JdbcTemplate template = new JdbcTemplate(getDbContext().getDataSource());
            template.execute(t.toString());
            alterType(typeName);

        }

        return t;
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


    public void alterType(Class<? extends SqlTypeObject> aType) {
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

        if (!getDbContext().isSqlObjectExists(typeName, "TYPE", true)) {
            return;
        }
        JdbcTemplate template = new JdbcTemplate(getDbContext().getDataSource());
        template.execute(new PLDropType(typeName).toString());
    }


    public void dropPackage(String typeName) {

        if (!isPackageExists(typeName)) {
            return;
        }
        JdbcTemplate template = new JdbcTemplate(getDbContext().getDataSource());
        template.execute(new PLDropPackage(typeName).toString());
    }


    public boolean isDependencyPackageExists(SqlStoredProcedureModule pkg) {

        Class<OraclePackage>[] items = SqlObjectHelper.getDependencyItems(pkg, OraclePackage.class);
        for (Class<OraclePackage> sqlClass : items) {
            if (!isPackageExists(sqlClass)) {
                return false;
            }
        }
        return true;
    }




    public List<PLTableColumnDef> getTableColumnCodeObjects(SqlObjectMetaData metaData) {

        List<PLTableColumnDef> list = new ArrayList<PLTableColumnDef>();
        for (SqlColumn colMetaData : metaData.getColumns()) {
            list.add(new PLTableColumnDef(colMetaData.getColumnName(), colMetaData.getTypeName()));
        }
        return list;
    }


    public List<PLTableColumnDef> getTableColumnCodeObjects(String tableName) throws SQLException {
        return getTableColumnCodeObjects(getDbContext().getObjectMetaData(tableName));
    }


    public void grantPrivilege(String schemaName, String privilegeName, String objectName) {

        JdbcTemplate template = new JdbcTemplate(getDbContext().getDataSource());
        template.execute(new PLGrantPrivilege(schemaName, privilegeName, objectName).toString());

    }


}
