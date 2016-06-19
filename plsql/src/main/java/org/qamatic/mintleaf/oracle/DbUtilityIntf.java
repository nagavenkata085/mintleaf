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

import org.qamatic.mintleaf.interfaces.SqlObjectMetaData;
import org.qamatic.mintleaf.interfaces.SqlStoredProcedureModule;
import org.qamatic.mintleaf.interfaces.SqlTypeObject;
import org.qamatic.mintleaf.oracle.codeobjects.PLCreateType;
import org.qamatic.mintleaf.oracle.codeobjects.PLTableColumnDef;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Senthil Maruthaiappan on 4/6/15.
 */
public interface DbUtilityIntf {

    boolean isTableExists(String pkgName);

    boolean isTriggerExists(String triggerName);

    boolean isTriggerExists(String triggerName, boolean igoreValidity);

    boolean isUserObjectExists(String objectName, String objectType);

    boolean isColumnExists(String tableName, String columnName);

    boolean isdbFeatureValid(String featureName);

    int getNextSequenceNumber(String sequenceName);

    void truncateTable(String tableName);

    String getContextParamValue(String nameSpace, String paramName);

    boolean isDatabaseUserExists(String userName);

    List<String> getSqlObjects(String objectType);

    List<String> getPrimaryKeys(String ownerName, String tableName);


    void grantPrivilege(String schemaName, String privilegeName, String objectName);

    boolean isPrivilegeExists(String granteeName, String privilegeName, String objectName);
}
