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

import org.qamatic.mintleaf.core.SqlObjectInfo;
import org.qamatic.mintleaf.interfaces.DbContext;
import org.qamatic.mintleaf.interfaces.SqlProcedure;

import java.sql.Types;

@SqlObjectInfo(name = "TestDbProvisioning", source = "/TestDbProvisioning.sql")
public class TestDbProvisioning extends OraclePackage {

    public TestDbProvisioning(DbContext dbContext) {
        super(dbContext);
    }

    public void dropSchemaUser(String userName) {
        SqlProcedure proc = getProcedure("dropApplicationUser");
        proc.createParameter("pusername", Types.VARCHAR);
        proc.compile();
        proc.setValue("pusername", userName);
        proc.execute();
    }

    public void createSchemaUser(String userName, String appUserPassword) {
        SqlProcedure proc = getProcedure("createApplicationUser");
        proc.createParameter("pusername", Types.VARCHAR);
        proc.createParameter("puserPassword", Types.VARCHAR);
        proc.compile();
        proc.setValue("pusername", userName);
        proc.setValue("puserPassword", appUserPassword);
        proc.execute();
    }

    public void recreateSchemaUser(String userName, String appUserPassword) {
        dropSchemaUser(userName);
        createSchemaUser(userName, appUserPassword);
    }

}