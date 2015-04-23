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


import org.qamatic.mintleaf.core.SqlObjectDependsOn;
import org.qamatic.mintleaf.core.SqlObjectHelper;
import org.qamatic.mintleaf.core.SqlObjectInfo;
import org.qamatic.mintleaf.interfaces.*;
import org.qamatic.mintleaf.oracle.codeobjects.*;
import org.qamatic.mintleaf.oracle.typeobjects.TStringList;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;


@SqlObjectInfo(name = "DbUtility", source = "/DbUtility.sql")
@SqlObjectDependsOn(Using = {TStringList.class})
public class DbUtility extends OraclePackage implements DbUtilityIntf {

    public DbUtility(DbContext context) {
        super(context);
    }

    @Override
    public void drop() {
        // no need to drop, all test db assertions are depending on it.
    }

    @Override
    public boolean isPackageExistsByName(String pkgName) {
        return isPackageInterfaceExists(pkgName);
    }

    @Override
    public boolean isTableExists(String pkgName) {
        return isTableInterfaceExists(pkgName);
    }

    @Override
    public boolean isPackageExists(String pkgName) {
        return isPackageInterfaceExists(pkgName);
    }

    @Override
    public boolean isTypeExists(String typeName) {
        return isTypeExists(typeName, false);
    }

    @Override
    public boolean isTypeBodyExists(String typeName) {
        return isTypeBodyExists(typeName, false);
    }

    @Override
    public boolean isTypeBodyExists(String typeName, boolean igoreValidity) {
        return getDbContext().isSqlObjectExists(typeName, "TYPE BODY", igoreValidity);
    }

    @Override
    public boolean isTypeExists(String typeName, boolean igoreValidity) {
        return getDbContext().isSqlObjectExists(typeName, "TYPE", igoreValidity);
    }

    @Override
    public boolean isTriggerExists(String triggerName) {
        return isTriggerExists(triggerName, false);
    }

    @Override
    public boolean isTriggerExists(String triggerName, boolean igoreValidity) {
        return getDbContext().isSqlObjectExists(triggerName, "TRIGGER", igoreValidity);
    }

    @Override
    public boolean isSynonymExists(String synonymName) {
        return isSynonymExists(synonymName, false);
    }

    @Override
    public boolean isSynonymExists(String synonymName, boolean igoreValidity) {
        return getDbContext().isSqlObjectExists(synonymName, "SYNONYM", igoreValidity);
    }

    @Override
    public boolean isPackageExists(OraclePackage pkg) {
        return isPackageInterfaceExists(pkg.getName());
    }

    @Override
    public boolean isPackageExists(Class<? extends OraclePackage> pkgClass) {
        SqlObjectInfo objAnnot = SqlObjectHelper.getDbObjectInfo(pkgClass);
        if (objAnnot == null) {
            return true;
        }
        return isPackageInterfaceExists(objAnnot.name());
    }

    @Override
    public boolean isPackageInterfaceExists(String pkgName) {
        return isUserObjectExists(pkgName, "PACKAGE");
    }

    @Override
    public boolean isPackageInterfaceExists(String pkgName, boolean ignoreValidity) {
        return getDbContext().isSqlObjectExists(pkgName, "PACKAGE", ignoreValidity);
    }

    @Override
    public boolean isPackageBodyExists(String pkgName) {
        return isUserObjectExists(pkgName, "PACKAGE BODY");
    }

    @Override
    public boolean isTableInterfaceExists(String pkgName) {
        return isUserObjectExists(pkgName, "TABLE");
    }

    @Override
    public boolean isSequenceExists(String sequenceName) {

        int cnt = getDbContext().getCount("user_sequences", "sequence_name = ?", new Object[]{sequenceName.toUpperCase()});
        return cnt != 0;
    }

    @Override
    public boolean isUserObjectExists(String objectName, String objectType) {
        return getDbContext().isSqlObjectExists(objectName, objectType, false);
    }

    @Override
    public boolean isColumnExists(String tableName, String columnName) {
        int cnt = getDbContext().getCount("user_tab_columns", "table_name = ? AND column_name = ?", new Object[]{tableName.toUpperCase(), columnName.toUpperCase()});
        return cnt != 0;
    }

    @Override
    public boolean isdbFeatureValid(String featureName) {
        JdbcTemplate template = new JdbcTemplate();
        template.setDataSource(getDbContext().getDataSource());
        int cnt = getDbContext().getCount("dba_server_registry", "comp_id=? and status=?", new Object[]{featureName.toUpperCase(), "VALID"});
        return cnt != 0;
    }

    @Override
    public int getNextSequenceNumber(String sequenceName) {
        JdbcTemplate template = new JdbcTemplate();
        template.setDataSource(getDbContext().getDataSource());

        return template.queryForInt(String.format("select %s.NextVal from dual", sequenceName));
    }


    @Override
    public void truncateTable(String tableName) {
        JdbcTemplate template = new JdbcTemplate();
        template.setDataSource(getDbContext().getDataSource());
        String sql = String.format("truncate  table %s", tableName);
        template.execute(sql);

    }

    @Override
    public String getContextParamValue(String nameSpace, String paramName) {
        JdbcTemplate template = new JdbcTemplate();
        template.setDataSource(getDbContext().getDataSource());
        String sql = String.format("Select sys_context('%s','%s') from dual", nameSpace, paramName);
        return template.queryForObject(sql, String.class);
    }

    @Override
    public boolean isDatabaseUserExists(String userName) {
        return getDbContext().getCount("all_users", "username = upper(?)", new Object[]{userName}) != 0;
    }

    @Override
    public void createType(String typeName) {
        createType(typeName, null, null);
    }

    @Override
    public void createType(String typeName, List<PLTableColumnDef> columns) {

        createType(typeName, null, columns);
    }

    @Override
    public PLCreateType createType(String typeName, String parentClassName, List<PLTableColumnDef> columns) {
        return createType(typeName, parentClassName, columns, true);

    }

    @Override
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

    @Override
    public void createType(String typeName, String parentClassName) {

        createType(typeName, parentClassName, new ArrayList<PLTableColumnDef>());
    }

    @Override
    public PLCreateType createTypeFromTable(String typeName, String parentClassName, String tableName) throws SQLException {
        return createTypeFromTable(typeName, parentClassName, tableName, true);
    }

    @Override
    public PLCreateType createTypeFromTable(String typeName, String parentClassName, String tableName, boolean bCreate) throws SQLException {
        return createType(typeName, parentClassName, getTableColumnCodeObjects(tableName), bCreate);
    }

    @Override
    public void alterType(Class<? extends SqlTypeObject> aType) {
        alterType(SqlObjectHelper.getDbObjectInfo(aType).name());
    }

    @Override
    public void alterType(String typeName) {
        if (!getDbContext().isSqlObjectExists(typeName, "TYPE", true)) {
            return;
        }
        logger.info("exec:" + typeName);
        JdbcTemplate template = new JdbcTemplate(getDbContext().getDataSource());
        PLAlterType ptype = new PLAlterType(typeName);

        template.execute(ptype.toString());
        if (getDbContext().isSqlObjectExists(typeName, "TYPE BODY", true)) {
            ptype.setBody(true);
            template.execute(ptype.toString());
        }

    }

    @Override
    public void dropType(String typeName) {

        if (!getDbContext().isSqlObjectExists(typeName, "TYPE", true)) {
            return;
        }
        JdbcTemplate template = new JdbcTemplate(getDbContext().getDataSource());
        template.execute(new PLDropType(typeName).toString());
    }

    @Override
    public void dropPackage(String typeName) {

        if (!isPackageExists(typeName)) {
            return;
        }
        JdbcTemplate template = new JdbcTemplate(getDbContext().getDataSource());
        template.execute(new PLDropPackage(typeName).toString());
    }

    @Override
    public boolean isDependencyPackageExists(SqlPackage pkg) {

        Class<OraclePackage>[] items = SqlObjectHelper.getDependencyItems(pkg, OraclePackage.class);
        for (Class<OraclePackage> sqlClass : items) {
            if (!isPackageExists(sqlClass)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public List<String> getUserTableList() {
        return getSqlObjects("TABLE");
    }

    @Override
    public List<String> getSqlObjects(String objectType) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(getDbContext().getDataSource());
        return jdbcTemplate.query(String.format("select object_name from user_objects where object_type='%s'", objectType), new RowMapper<String>() {
            @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {

                return rs.getString("object_name");
            }
        });
    }

    @Override
    public List<String> getPrimaryKeys(String tableName) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(getDbContext().getDataSource());
        String sql = String
                .format("select ucc.column_name as keyname from all_constraints uc, all_cons_columns ucc where uc.table_name = upper(DbUtility.getTableNameIfExists('%s')) and uc.constraint_type = 'P' and (uc.constraint_name=ucc.constraint_name) and  uc.owner=ucc.owner and ucc.owner=Upper(DbUtility.getOwnerName('%s'))",
                        tableName, tableName);
        return jdbcTemplate
                .query(sql, new RowMapper<String>() {
                    @Override
                    public String mapRow(ResultSet rs, int rowNum) throws SQLException {

                        return rs.getString("keyname");
                    }
                });
    }

    @Override
    public String getTableNameIfExists(String psynonymvname) {
        SqlProcedure proc = getFunction("getTableNameIfExists", Types.VARCHAR);
        proc.createParameter("psynonymvname", Types.VARCHAR);
        proc.setValue("psynonymvname", psynonymvname);
        proc.execute();
        return proc.getStringValue("result");
    }

    @Override
    public List<PLTableColumnDef> getTableColumnCodeObjects(SqlObjectMetaData metaData) {

        List<PLTableColumnDef> list = new ArrayList<PLTableColumnDef>();
        for (SqlColumn colMetaData : metaData.getColumns()) {
            list.add(new PLTableColumnDef(colMetaData.getColumnName(), colMetaData.getTypeName()));
        }
        return list;
    }

    @Override
    public List<PLTableColumnDef> getTableColumnCodeObjects(String tableName) throws SQLException {
        UtilityCommon utilityCommon = new UtilityCommon(getDbContext());
        return getTableColumnCodeObjects(utilityCommon.getObjectMetaData(tableName, true));
    }

    @Override
    public void createSynonym(String synonymName, String SchemaName, String objectName) {

        JdbcTemplate template = new JdbcTemplate(getDbContext().getDataSource());
        template.execute(new PLCreateSynonym(synonymName, SchemaName, objectName).toString());
    }

    @Override
    public void grantPrivilege(String schemaName, String privilegeName, String objectName) {

        JdbcTemplate template = new JdbcTemplate(getDbContext().getDataSource());
        template.execute(new PLGrantPrivilege(schemaName, privilegeName, objectName).toString());

    }

    @Override
    public boolean isPrivilegeExists(String granteeName, String privilegeName, String objectName) {
        int cnt = getDbContext().getCount("user_tab_privs", "grantee = ? AND table_name = ? AND privilege=?", new Object[]{granteeName.toUpperCase(), objectName.toUpperCase(),
                privilegeName.toUpperCase()});
        return cnt != 0;
    }
}
