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

import org.qamatic.mintleaf.interfaces.SqlObjectMetaData;
import org.qamatic.mintleaf.interfaces.SqlPackage;
import org.qamatic.mintleaf.interfaces.SqlTypeObject;
import org.qamatic.mintleaf.oracle.codeobjects.PLCreateType;
import org.qamatic.mintleaf.oracle.codeobjects.PLTableColumnDef;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Senthil Maruthaiappan on 4/6/15.
 */
public interface DbUtilityIntf {
    boolean isPackageExistsByName(String pkgName);

    boolean isTableExists(String pkgName);

    boolean isPackageExists(String pkgName);

    boolean isTypeExists(String typeName);

    boolean isTypeBodyExists(String typeName);

    boolean isTypeBodyExists(String typeName, boolean igoreValidity);

    boolean isTypeExists(String typeName, boolean igoreValidity);

    boolean isTriggerExists(String triggerName);

    boolean isTriggerExists(String triggerName, boolean igoreValidity);

    boolean isSynonymExists(String synonymName);

    boolean isSynonymExists(String synonymName, boolean igoreValidity);

    boolean isPackageExists(OraclePackage pkg);

    boolean isPackageExists(Class<? extends OraclePackage> pkgClass);

    boolean isPackageInterfaceExists(String pkgName);

    boolean isPackageInterfaceExists(String pkgName, boolean ignoreValidity);

    boolean isPackageBodyExists(String pkgName);

    boolean isTableInterfaceExists(String pkgName);

    boolean isSequenceExists(String sequenceName);

    boolean isUserObjectExists(String objectName, String objectType);

    boolean isColumnExists(String tableName, String columnName);

    boolean isdbFeatureValid(String featureName);

    int getNextSequenceNumber(String sequenceName);

    void truncateTable(String tableName);

    String getContextParamValue(String nameSpace, String paramName);

    boolean isDatabaseUserExists(String userName);

    void createType(String typeName);

    void createType(String typeName, List<PLTableColumnDef> columns);

    PLCreateType createType(String typeName, String parentClassName, List<PLTableColumnDef> columns);

    PLCreateType createType(String typeName, String parentClassName, List<PLTableColumnDef> columns, boolean bCreate);

    void createType(String typeName, String parentClassName);

    PLCreateType createTypeFromTable(String typeName, String parentClassName, String tableName) throws SQLException;

    PLCreateType createTypeFromTable(String typeName, String parentClassName, String tableName, boolean bCreate) throws SQLException;

    void alterType(Class<? extends SqlTypeObject> aType);

    void alterType(String typeName);

    void dropType(String typeName);

    void dropPackage(String typeName);

    boolean isDependencyPackageExists(SqlPackage pkg);

    List<String> getUserTableList();

    List<String> getSqlObjects(String objectType);

    List<String> getPrimaryKeys(String tableName);

    String getTableNameIfExists(String psynonymvname);

    List<PLTableColumnDef> getTableColumnCodeObjects(SqlObjectMetaData metaData);

    List<PLTableColumnDef> getTableColumnCodeObjects(String tableName) throws SQLException;

    void createSynonym(String synonymName, String SchemaName, String objectName);

    void grantPrivilege(String schemaName, String privilegeName, String objectName);

    boolean isPrivilegeExists(String granteeName, String privilegeName, String objectName);
}
